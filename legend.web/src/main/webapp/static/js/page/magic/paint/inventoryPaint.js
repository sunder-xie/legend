/*
 * create by zmx 2016/11/14
 * 新建油漆盘点单
 */
$(function () {
    var doc = $(document);
    //获取url上goodsIds
    var goodsIds = util.getPara('goodsIds');

    seajs.use([
        'select',
        'art',
        'check',
        'formData',
        'dialog'
    ], function (st, at, ck, fd, dg) {
        //验证
        ck.init();

        //盘点人列表
        st.init({
            dom: '.js-select',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name",
            callback: function (showKey) {
                $(this).find('.paintCheckerId').val(showKey)
            }
        });

        //填充数据(初始化列表)
        $.ajax({
            url: BASE_PATH + '/goods/paintExt/getSelectPaintList',
            data: {
                goodsIds: goodsIds
            },
            success: function (result) {
                if (result.success) {
                    var html = at('tableTpl', {json: result});
                    $('#tableCon').html(html);
                }
            }
        });

        //消耗量所需数据（实盘库存等字段填写）
        doc.on('blur', 'input[name="realStirNum"],input[name="realNoBucketWeight"],input[name="realNoBucketNum"],input[name="realStock"]', function () {
            consumption($(this))
        });

        // 添加物料
        var html = null;
        addGoodsInit({
            dom: '#goodsAddBtn',
            callback: function (result) {
                addPaint(result)
            },
            allCallBack: function (result) {
                addPaint(result)
            }
        });

        //删除按钮
        doc.on('click', '.js-delete', function () {
            var $this = $(this).parents('tr');
            $this.prev().remove();
            $this.next().remove();
            $this.remove();
        });
        //返回按钮
        doc.on('click', '.js-return', function () {
            window.location.href = BASE_PATH + '/paint/inventory/toInventoryPaintList';
        });

        //保存草稿
        doc.on('click', '.js-saveDrafts', function () {
            if (!ck.check('#formData')) {
                return false;
            } else {
                var url = BASE_PATH + '/paint/inventory/toUpdateInventoryPaint?id=';
                confirm(1, url)
            }
        });

        //生成盘点单
        doc.on('click', '.js-save', function () {
            if (!ck.check()) {
                return false;
            } else {
                var url = BASE_PATH + '/paint/inventory/toInventoryPaintDtl?id=';
                confirm(2, url)
            }
        });

        //打印按钮
        doc.on('click', '.js-print', function () {
            var id = $('.record_id').val();
            util.print(BASE_PATH + '/paint/inventory/toInventoryPaintPrint?id=' + id);
        });

        /*****************************函数部分*********************************/
        //添加油漆
        function addPaint(result){
            var id = $('.record_id').val();
            if (id != '') {
                //编辑页面
                html = at("tableTpl", {json: result});
                $('#editTableCon').append(html);
            } else {
                //新建页面
                html = at("tableTpl", {json: result});
                $('#tableCon').append(html);
            }
        }

        //计算消耗量
        function consumption(thisInput) {
            var data,
                prevTr = thisInput.parents('tr').prev(),
                realStock = thisInput.parents('tr').find('input[name="realStock"]').val(),//实盘库存
                realNoBucketWeight = thisInput.parents('tr').find('input[name="realNoBucketWeight"]').val(),//实盘非整桶总质量
                realNoBucketNum = thisInput.parents('tr').find('input[name="realNoBucketNum"]').val(),//实盘非整桶数量
                realStirNum = thisInput.parents('tr').find('input[name="realStirNum"]').val();//实盘搅拌头数量

            if (realStock && realNoBucketWeight && realNoBucketNum && realStirNum) {
                data = fd.get(prevTr, '.list-data');
                data.realStock = realStock;//实盘库存
                data.realNoBucketWeight = realNoBucketWeight;//实盘非整桶总质量
                data.realNoBucketNum = realNoBucketNum;//实盘非整桶数量
                data.realStirNum = realStirNum;//实盘搅拌头数量

                $.ajax({
                    type: 'post',
                    url: BASE_PATH + '/paint/inventory/calculatePaintConsumption',
                    data: JSON.stringify(data),
                    dataType: 'json',
                    contentType: "application/json",
                    success: function (result) {
                        if (result.success) {
                            prevTr.find('.aWeight').val(result.data.diffStock);
                            prevTr.find('.diffStockPrice').val(result.data.diffStockPrice);
                            prevTr.find('.totalStockAmount').val(result.data.totalStockAmount);
                        }
                    }
                });
            }
        }

        //提交数据
        function confirm(paintStatus, url) {
            //基本信息
            var data = fd.get('#formData');
            //备注
            data.paintRemark = $('textarea[name="paintRemark"]').val();
            //提交状态（草稿、生成盘点单）
            data.paintStatus = paintStatus;
            var recordId = $('.record_id').val();
            if (recordId != '') {
                data.id = recordId;
            }
            var paintInventoryStockDTOList = [];
            var last = {};
            $('.table-form').each(function (i) {
                var $this = $(this);
                //获取油漆商品列表数据
                var listData = fd.get($this, '#listData');
                var goodsFormat = $this.find('.goodsFormat').text();
                var goodsName = $this.find('.goodsName').text();
                var measureUnit = $this.find('.measureUnit').text();
                if (goodsFormat != '') {
                    listData.goodsFormat = goodsFormat;
                }
                if (goodsName != '') {
                    listData.goodsName = goodsName;
                }
                if (measureUnit != '') {
                    listData.measureUnit = measureUnit;
                }
                //两行tr数据合并
                $.extend(last, listData);
                if ((i + 1) % 2 == 0) {
                    paintInventoryStockDTOList.push(last);
                    last = {};
                }
            });
            data['paintInventoryStockDTOList'] = paintInventoryStockDTOList;

            if( paintInventoryStockDTOList == ''){
                dg.fail('未选择盘点配件');
                return false;
            }
            $.ajax({
                type: 'post',
                url: BASE_PATH + '/paint/inventory/addOrUpdatePaintInventory',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: "application/json",
                success: function (result) {
                    if (result.success) {
                        dg.success('成功');
                        window.location.href = url+result.data;
                    } else {
                        dg.fail(result.message)
                    }
                }
            })
        }
    })
});
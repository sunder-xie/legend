/**
 * 其他出库
 * zmx 2016-08-25
 */

$(function () {

    seajs.use([
        'select',
        'art',
        "downlist",
        'check',
        'dialog',
        'date',
        'formData'
    ], function (st, at, dl, ck, dg, date, fd) {
        var $doc = $(document);

        ck.init();
        date.datePicker('.js-out-date', {
            maxDate: '%y-%M-%d %H',
            dateFmt: 'yyyy-MM-dd HH:mm'
        });
        ck.init();
        dg.titleInit();
        //初始化车牌下拉框
        dl.init({
            url: BASE_PATH + '/shop/customer/search/mobile',
            searchKey: 'com_license',
            tplCols: {'license': '车牌'},
            tplId: 'carLicenceTpl',
            showKey: 'license',
            dom: 'input[name="carLicense"]',
            hasInput: false,
            callbackFn: function (obj, item) {
                $('input[name="customerName"]').val(item["customerName"]);
                $('input[name="customerMobile"]').val(item["mobile"]);
                $('input[name="carType"]').val(item["carInfo"]);
            }
        });
        //领料人下拉列表
        st.init({
            dom: '.js-picking',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });

        //出库人下拉列表
        st.init({
            dom: '.js-out-person',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });

        //出库类型下拉列表
        st.init({
            dom: '.js-out-type',
            url: BASE_PATH + '/shop/warehouse/out/get_out_type?action=true',
            showKey: "name",
            showValue: "message"
        });
        //保存其他入库
        $(document).on('click', '.js-save', function () {
            var result = ck.check();
            if (!result) {
                return;
            }

            var warehouseOut = fd.get('.js-out'),
                warehouseOutDetail = [];
            $.extend(warehouseOut, fd.get('.js-comment'));

            $('.goods-table .goods-datatr').each(function () {
                warehouseOutDetail.push(fd.get($(this)));
            });

            if (!warehouseOutDetail.length) {
                dg.warn('出库配件不能为空');
                return;
            }
            $.ajax({
                url: BASE_PATH + "/shop/warehouse/out/stock/out",
                dataType: 'json',
                data: {
                    warehouseOutBo: JSON.stringify(warehouseOut),
                    warehouseOutDetailBoList: JSON.stringify(warehouseOutDetail)
                },
                type: 'POST',
                success: function (data) {
                    if (data.success) {
                        dg.success("出库成功",function () {
                            window.location.href = BASE_PATH + "/shop/warehouse/out/out-detail?id=" + data.data + "&refer=out-other";
                        });
                    } else {
                        dg.fail(data.errorMsg);
                    }
                }
            });
        });
        $(document).on('click', '.js-return', function () {
            util.goBack();
        });

        $(document).on('click','.js-del-btn',function(){
            var del = $(this).parents('.goods-datatr');
            dg.confirm('确定要删除吗？',function(){
                del.remove();
            });
        });


        // 配件 添加按钮
        addGoodsInit({
            dom: '.js-add-goods',
            callback: addGoods
        });

        // 批量添加物料
        batchAddPart({
            dom: '.js-batch-add-goods',
            callback: addGoods
        });

        // 新增配件,需要引入模板 yqx/tpl/order/new-part-tpl.ftl
        newPart({
            dom: '.js-new-goods',
            callback: addGoods
        });


        function addGoods(json) {
            var goodsHtml, arr;
            var goodsIds = [];

            $('.goods-datatr').find('input[name=goodsId]').each(function () {
                goodsIds.push($(this).val());
            });

            if (json && json.success) {
                arr = json.data.goodsList.map(function (e) {
                    if (goodsIds.indexOf(e.id + '') > -1) {
                        return {};
                    }
                });
            }

            // 非ajax请求
            if (json && json.id) {
                if (goodsIds.indexOf(json.id + '') > -1) {
                    dg.msg('该配件已存在');
                    return;
                }
                arr = [json];
            }

            // 批量添加物料或其他
            if (json && json.length) {
                arr = json;
            }

            goodsHtml = $(at("goodsTpl", {json: arr}));

            $('.goods-table').append(goodsHtml);

            calMoney();
        }

        function calMoney() {
            var t = 0;
            $('.list-box .yqx-table').find('tr')
                .each(function () {
                    t = t.Jia(+$(this).find('.js-goods-amount').val() || 0);
                });

            $('.js-goods-total').val(t);

            $('.js-money-plus').eq(0).trigger('input');
        }
    });
});
$(function () {
    var doc = $(document);

    seajs.use([
        'art',
        'select',
        'check',
        'dialog'
    ], function (at, st, ck, dg) {

        // 盘点人列表
        st.init({
            dom: ".js-select-user",
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });

        // 添加物料
        addGoodsInit({
            dom: '#goodsAddBtn',
            callback: function (json) {
                var goodsJsonArray = [];
                goodsJsonArray[0] = json;
                var html = at("goodsTpl", {json: goodsJsonArray});
                $('#orderGoodsTB').append(html);
            },
            allCallBack: function (json) {
                var html = at("goodsTpl", {json: json});
                $('#orderGoodsTB').append(html);
            }
        });

        //验证
        ck.init();
        //溢出隐藏
        dg.titleInit();

        // return
        doc.on('click', '.js-return', function () {
            util.goBack();
        });

        // print
        doc.on('click', '.js-print', function () {
            alert("TODO");
        });

        // generate
        doc.on('click', '.js-generate', function () {
            update(2);
        });

        // draft
        doc.on('click', '.js-draft', function () {
            update(1);
        });

        /**
         * 更新
         *
         * @param status 1:草稿 2:正式
         */
        function update(status) {
            var data = {};
            var index = [];
            var inventoryGoods = [];

            // 盘点记录ID
            var recordId = $('input[name="recordId"]').val();
            data["recordId"] = recordId;
            var recordSn = $('input[name="recordSn"]').val();
            data["recordSn"] = recordSn;
            // 盘点人
            var inventoryCheckerId = $('input[name="inventoryCheckerId"]').val();
            data["inventoryCheckerId"] = inventoryCheckerId;
            // 盘点人姓名
            var inventoryCheckerName = $('input[name="inventoryCheckerName"]').val();
            data["inventoryCheckerName"] = inventoryCheckerName;
            // 已盘点
            data["status"] = status;
            // 正式盘点时 检验是否填写实盘库存
            var existUnfillGoods = 1;

            // 盘点配件列表
            $('.goods-datatr', '#orderGoodsTB').each(function (i) {
                var currentRow = $(this);
                var stockId = $('input[name="stockId"]', currentRow).val();
                var goodsId = $('input[name="goodsId"]', currentRow).val();
                var inventoryPrice = $('input[name="inventoryPrice"]', currentRow).val() || 0;
                var reason = $('input[name="reason"]', currentRow).val() || "";

                var orderGoods = {
                    "stockId": stockId,
                    "goodsId": goodsId,
                    "inventoryPrice": $.trim(inventoryPrice),
                    "reason": $.trim(reason)
                };

                var realStock = $('input[name="realStock"]', currentRow).val();
                // 实盘库存为空
                if (typeof(realStock) == 'undefined' || $.trim(realStock) == "") {
                    existUnfillGoods = existUnfillGoods & 0;
                    index.push(i + 1);
                } else {
                    orderGoods["realStock"] = $.trim(realStock);
                }
                inventoryGoods.push(orderGoods);
            });

            if(existUnfillGoods ==0 && status ==2){
                dg.warn('第' + index.join(',') + "行存在物料 未填写'实盘库存' ",null);
                return false;
            }

            // 备注
            data["inventoryRemark"] = $("input[name='inventoryRemark']").val();

            // 配件列表
            data["inventoryGoodsJSON"] = JSON.stringify(inventoryGoods);

            $.ajax({
                type: 'post',
                data: data,
                url: BASE_PATH + '/shop/warehouse/stock/stock-inventory-update'
            }).done(function (json) {
                if (json.success) {
                    dg.success("创建成功", function () {
                        window.document.location.href = BASE_PATH + '/shop/warehouse/stock/stock-inventory-detail?itemid=' + json["data"];
                    });
                } else {
                    var code = json["code"];
                    var errormsg = "";
                    if (code === 'deleted') {
                        errormsg = ["被盘点的配件不存在，请删除后，重新生成盘点单", "零件号：" + json["errorMsg"]].join("<br/>");
                        dg.fail(errormsg);
                        $('input[name=goodsFormat]').each(function () {
                            var partNumber = $(this).val();
                            var messageNumber = json["errorMsg"];
                            if (partNumber == messageNumber) {
                                $(this).parents('.goods-datatr').find('.yqx-input').css('color', 'red');
                            }
                        })
                    } else {
                        errormsg = json["errorMsg"];
                    }
                    dg.fail(errormsg);

                }
            });
        }

        // 配件 添加按钮
        addGoodsInit({
            dom: '.js-add-goods'
        });

        //删除行
        doc.on('click', '.js-trdel', function () {
            var currentRow = $(this).parents('tr');
            currentRow.remove();
        });

        // 实盘库存
        $(document).on('blur', 'input[name="realStock"]', function () {

            var currentRow = $(this).parents(".goods-datatr");

            // 实际库存数量
            var realStock = $(this).val();
            if (!$.isNumeric(realStock) || realStock < 0 || $.trim(realStock) == "") {
                $(this).val("");
                $('[name="realInventoryAmount"]', currentRow).text("");
                return false;
            }

            // 配件成本价
            var inventoryPriceInput = $('input[name="inventoryPrice"]', currentRow);
            var inventoryPrice = inventoryPriceInput.val();
            if (inventoryPrice == '') {
                inventoryPrice = 0;
                inventoryPriceInput.val(0);
            }

            var realInventoryAmount = (parseFloat(inventoryPrice) * parseFloat(realStock)).toFixed(2);
            $('[name="realInventoryAmount"]', currentRow).text(realInventoryAmount);
        });

        // 成本
        $(document).on('blur', 'input[name="inventoryPrice"]', function () {

            var currentRow = $(this).parents(".goods-datatr");
            var inventoryPrice = $(this).val();
            if (!$.isNumeric(inventoryPrice) || inventoryPrice < 0) {
                $(this).val(0);
                return;
            }

            // 实际库存数量
            var realStock = $('input[name="realStock"]', currentRow).val();
            if ($.isNumeric(realStock) && realStock > 0) {
                var realInventoryAmount = (parseFloat(inventoryPrice) * parseFloat(realStock)).toFixed(2);
                $('[name="realInventoryAmount"]', currentRow).text(realInventoryAmount);
            }

            // 现库存
            var stock = $('[name="stock"]', currentRow).text();
            if ($.isNumeric(stock) && stock > 0) {
                var currentInventoryAmount = (parseFloat(inventoryPrice) * parseFloat(stock)).toFixed(2);
                $('[name="currentInventoryAmount"]', currentRow).text(currentInventoryAmount);
            }
        });

        // 成本
        $(document).on('input', 'input[name="inventoryPrice"]', function () {

            var currentRow = $(this).parents(".goods-datatr");
            var inventoryPrice = $(this).val();
            if (!$.isNumeric(inventoryPrice) || inventoryPrice < 0) {
                $(this).val(0);
                return;
            }

            // 实际库存数量
            var realStock = $('input[name="realStock"]', currentRow).val();
            if ($.isNumeric(realStock) && realStock >= 0) {
                var realInventoryAmount = (parseFloat(inventoryPrice) * parseFloat(realStock)).toFixed(2);
                $('[name="realInventoryAmount"]', currentRow).text(realInventoryAmount);
            }

            // 现库存
            var stock = $('[name="stock"]', currentRow).text();
            if ($.isNumeric(stock) && stock >= 0) {
                var currentInventoryAmount = (parseFloat(inventoryPrice) * parseFloat(stock)).toFixed(2);
                $('[name="currentInventoryAmount"]', currentRow).text(currentInventoryAmount);
            }
        });
    });
});
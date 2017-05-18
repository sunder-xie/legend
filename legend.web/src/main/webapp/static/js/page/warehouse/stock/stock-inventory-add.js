/**
 * 添加库存盘点
 * zmx 2016-08-08
 */

$(function () {
    var doc = $(document);
    // 项目的序号
    var getSerialNumber = (function () {
        var count = +$('.td-index').last().text();

        if(count > 0) {
            count += 1;
        } else {
            count = 1;
        }

        return function () {
            return count++;
        }
    }());

    seajs.use([
        'art',
        'select',
        'check',
        'dialog'
    ], function (at, st, ck, dg) {
        at.helper('getSerialNumber', getSerialNumber);
        ck.init();

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
            save(2);
        });

        // draft
        doc.on('click', '.js-draft', function () {
            save(1);
        });

        /**
         * 保存
         *
         * @param status 1:草稿 2:正式
         */
        function save(status) {

            if (!ck.check()) return false;

            var data = {};
            var index = [];
            var inventoryGoods = [];

            // 盘点编号
            data["recordSn"] = $('input[name="recordSn"]').val();
            // 盘点人
            data["inventoryCheckerId"] = $('input[name="inventoryCheckerId"]').val();
            // 盘点人姓名
            data["inventoryCheckerName"] = $('input[name="inventoryCheckerName"]').val();
            // 已盘点
            data["status"] = status;
            // 正式盘点时 检验是否填写实盘库存
            var existUnfillGoods =1;

            // 盘点配件列表
            $('.goods-datatr', '#orderGoodsTB').each(function (i) {
                var currentRow = $(this);
                var goodsId = $('input[name="goodsId"]', currentRow).val();
                var inventoryPrice = $('input[name="inventoryPrice"]', currentRow).val() || 0;
                var inventoryPrePrice = $('input[name="inventoryPrePrice"]', currentRow).val() || 0;
                var reason = $('input[name="reason"]', currentRow).val() || "";
                var orderGoods = {
                    "goodsId": goodsId,
                    "inventoryPrice": $.trim(inventoryPrice),
                    "inventoryPrePrice": $.trim(inventoryPrePrice),
                    "reason": $.trim(reason)
                };

                var realStock = $('input[name="realStock"]', currentRow).val();
                // 实盘库存为空
                if (typeof(realStock) == 'undefined' || $.trim(realStock) == "") {
                    existUnfillGoods = existUnfillGoods & 0;
                    index.push(i+1);
                } else {
                    orderGoods["realStock"] = $.trim(realStock);
                }
                inventoryGoods.push(orderGoods);
            });

            if(existUnfillGoods ==0 && status ==2){
                dg.warn('第' + index.join(',') + "行存在物料 未填写'实盘库存' ");
                return false;
            }

            // 备注
            data["inventoryRemark"] = $("input[name='inventoryRemark']").val();
            // 配件列表
            data["inventoryGoodsJSON"] = JSON.stringify(inventoryGoods);

            $.ajax({
                type: 'post',
                data: data,
                url: BASE_PATH + '/shop/warehouse/stock/stock-inventory-generate'
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
                    } else {
                        errormsg = json["errorMsg"];
                    }
                    dg.fail(errormsg);
                }
            });
        }

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
                $('[name="realInventoryAmount"]', currentRow).val("");
                $('.js-inventory-amount', currentRow).text("");
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
            $('[name="realInventoryAmount"]', currentRow).val(realInventoryAmount);
            $('.js-inventory-amount', currentRow).text(realInventoryAmount);
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
            if ($.isNumeric(realStock) && realStock >= 0) {
                var realInventoryAmount = (parseFloat(inventoryPrice) * parseFloat(realStock)).toFixed(2);
                $('[name="realInventoryAmount"]', currentRow).text(realInventoryAmount);
                $('.js-inventory-amount', currentRow).text(realInventoryAmount);
            }

            // 现库存
            var stock = $('[name="stock"]', currentRow).val();
            if ($.isNumeric(stock) && stock >= 0) {
                var currentInventoryAmount = (parseFloat(inventoryPrice) * parseFloat(stock)).toFixed(2);
                $('[name="currentInventoryAmount"]', currentRow).val(currentInventoryAmount);
                $('.js-cur-inventory-amount', currentRow).text(currentInventoryAmount);
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
                $('.js-inventory-amount', currentRow).text(realInventoryAmount);
            }

            // 现库存
            var stock = $('input[name="stock"]', currentRow).val();
            if ($.isNumeric(stock) && stock >= 0) {
                var currentInventoryAmount = (parseFloat(inventoryPrice) * parseFloat(stock)).toFixed(2);
                $('[name="currentInventoryAmount"]', currentRow).val(currentInventoryAmount);
                $('.js-cur-inventory-amount', currentRow).text(currentInventoryAmount);
            }
        });
    });
});
/**
 * 工单报价详情
 * zmx 2016-08-26
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
        var doc = $(document);

        // 批量添加配件
        batchAddPart({
            dom: '.js-batch-add-goods',
            callback: addGoods
        });

        // 新建配件资料
        newPart({
            dom: '.js-new-part-btn',
            callback: function (json) {
                var html = at("goodsTpl", {json: [json]});
                $('#orderGoodsTB').append(html);

                calculatePriceUtil.drawup();
            }
        });

        // 配件 添加按钮
        addGoodsInit({
            dom: '.js-add-goods',
            callback: function (json) {
                var goodsJsonArray = [];
                goodsJsonArray[0] = json;
                var html = at("goodsTpl", {json: goodsJsonArray});
                $('#orderGoodsTB').append(html);

                calculatePriceUtil.drawup();
            }
        });

        //添加配件
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

            calculatePriceUtil.drawup();
        }

        // 更新工单信息
        function update(callback) {
            var data = fd.get('.base-box'),
                serviceList = [],
                goodsList = [];

            // 基本服务
            $('tr', '#orderServiceTB').each(function () {
                var item = fd.get($(this));
                serviceList.push(item);
            });
            //如果是委托单转工单，则需要添加相应的服务,且工单金额必须和委托单的金额相等
            if ($("input[name='orderInfo.proxyType']").val() == 2) {
                if (serviceList.length == 0) {
                    dg.fail("请选择委托项目对应的服务");
                    return false;
                }
                var orderTotalAmount = $("#orderTotalAmount").html();
                var proxyAmount = $("#proxyAmount").html();
                if (proxyAmount && orderTotalAmount != proxyAmount) {
                    dg.fail("委托金额和工单总金额不一致，无法开单");
                    return false;
                }
            }
            $('tr', '#orderGoodsTB').each(function () {
                var item = fd.get($(this));
                goodsList.push(item);
            });

            data["orderInfo.postscript"] = $("input[name='orderInfo.postscript']").val();

            // 服务列表
            data["orderServiceJson"] = JSON.stringify(serviceList);
            //配件列表
            data.orderGoodJson = JSON.stringify(goodsList);

            //数据发送
            var requestUrl = BASE_PATH + '/shop/warehouse/out/order-quote-detail/update';

            $.ajax({
                type: 'post',
                data: data,
                url: requestUrl
            }).done(function (json) {
                if (json.success) {
                    callback(json["data"]);
                } else {
                    dg.fail(json.errorMsg);
                }
            });
        }

        // 物料.单价
        $(document).on('blur', 'input[name="goodsPrice"]', function () {
            var goodsPrice = $(this).val();
            if (!$.isNumeric(goodsPrice) || goodsPrice < 0) {
                $(this).val(0);
                return;
            }

            var currentRow = $(this).parents(".goods-datatr");
            var goodsNumberInput = $('input[name="goodsNumber"]', currentRow);
            var goodsNumber = goodsNumberInput.val();
            if (goodsNumber == '') {
                goodsNumber = 1;
                goodsNumberInput.val(1);
            }

            var discountInput = $('input[name="discount"]', currentRow);
            var discount = discountInput.val();
            if (discount == '') {
                discountInput.val(0);
            }

            var goodsAmount = goodsPrice * goodsNumber;
            $('input[name="goodsAmount"]', currentRow).val(goodsAmount);
            $('.js-goods-amount', currentRow).text(goodsAmount.toFixed(2));

            // 核算价格
            calculatePriceUtil.drawup();
        });

        // 物料.数量
        $(document).on('blur', 'input[name="goodsNumber"]', function () {
            var flag = true;
            var currentRow = $(this).parents(".goods-datatr");
            var goodsNumber = $(this).val();
            var goodsPriceInput = $('input[name="goodsPrice"]', currentRow);
            var goodsPrice = goodsPriceInput.val();
            var discountInput = $('input[name="discount"]', currentRow);
            var discount = discountInput.val();
            var goodsAmount = goodsPrice * goodsNumber;
            if (!$.isNumeric(goodsNumber) || goodsNumber <= 0) {
                $(this).val(1);
                goodsNumber = 1;
                flag = false;
            }

            if (goodsAmount < discount) {
                discountInput.val(0);
            }

            if (!flag) {
                return false;
            }


            $('input[name="goodsAmount"]', currentRow).text(goodsAmount.toFixed(2));
            $('.js-goods-amount', currentRow).text(goodsAmount.toFixed(2));

            // 配件选中
            var checkbox = $('input[type=checkbox]', currentRow);
            var stock = $('input[name=stock]', currentRow).val();
            if (Number(goodsNumber) > Number(stock)) {
                checkbox.removeAttr('disabled');
                checkbox.attr('checked', true);
            } else {
                checkbox.removeAttr('checked');
                checkbox.attr('disabled', true);
            }

            // 核算价格
            calculatePriceUtil.drawup();
        });

        // 物料.优惠
        var basicGoodsScope = $("#orderGoodsTB");
        $(basicGoodsScope).on('blur', 'input[name="discount"]', function () {
            var currentRow = $(this).parents(".goods-datatr");
            var discount = parseInt($(this).val(), 10);
            var goodsPrice = parseInt($('[name="goodsPrice"]', currentRow).val(), 10);
            var goodsNumber = Number($('[name="goodsNumber"]', currentRow).val());
            if (!$.isNumeric(discount) || discount < 0 || goodsPrice * goodsNumber < discount) {
                $(this).val(0);
                return;
            }

            // 检验优惠金额 >总金额
            var goodsAmount = $('[name="goodsAmount"]', currentRow).text();
            if (parseFloat(discount) > parseFloat(goodsAmount)) {
                $(this).val(0);
                return;
            }

            $(this).val(discount.toFixed(2));

            // 核算价格
            calculatePriceUtil.drawup();
        });

        // 返回
        doc.on('click', '.js-return', function () {
            util.goBack();
        });

        // 提交报价
        doc.on('click', '.js-quote', function () {
            var orderStatus = $('input[name="orderInfo.orderStatus"]').val();
            var title = "您确定对该工单报价吗?";
            if (orderStatus == 'DDBJ') {
                title = "您确定对该工单重新报价吗?";
            }
            dg.confirm(title, function () {
                $('input[name="orderInfo.orderStatus"]').val("DDBJ");
                update(function () {
                    dg.success("操作成功", function () {
                        window.location.href = BASE_PATH + "/shop/warehouse/out/order-quote-list?t=" + new Date().getTime();
                    });
                });
            }, function () {
                return false;
            });
        });

        // 保存
        doc.on('click', '.js-save', function () {
            update(function (itemId) {
                window.location.href = BASE_PATH + "/shop/warehouse/out/order-quote-detail?orderId=" + itemId + "&refer=" + $("#refer").val();
            });
        });

        // 打印
        doc.on('click', '.js-print', function () {
            var orderId = $('input[name="orderInfo.id"]').val();
            util.print(BASE_PATH + "/shop/warehouse/out/shop_pricing_print?orderId=" + orderId);
        });

        // 删除
        doc.on('click', '.js-trdel', function () {
            var currentRow = $(this).parents('tr');
            // IF(物料删除 AND 出库数量 !=0) THEN 不可以删除
            dg.confirm('确定要删除吗？', function () {
                var outNumber = $('input[name="outNumber"]', currentRow).val();
                if (outNumber != "" && parseInt(outNumber) > 0) {
                    dg.warn("该配件已出库，不能删除!");
                    return false;
                }
                currentRow.remove();

                // 计算价格
                calculatePriceUtil.drawup();
            })

        });

        // tab切换
        $('.tabCon').eq(0).show();
        doc.on('click', '.tab-item', function () {
            var index = $(this).index();
            $(this).addClass('current-item').siblings().removeClass('current-item');
            $('.tabCon').eq(index).show().siblings('.tabCon').hide();
        });

        // 转入库
        inWarehouse({
            dom: '.js-in-warehouse',
            callback: function (items) {
                var itemMap = {};
                for (var itemIndex in items) {
                    var item = items[itemIndex];
                    var goodsId = item["id"];
                    var stock = item["stock"];
                    itemMap[goodsId] = stock;
                }

                $('.goods-datatr').find('input[name=goodsId]').each(function () {
                    var goodsId = $(this).val();
                    var goodsRow = $(this).parents(".goods-datatr");
                    var newStock = itemMap[goodsId];
                    if (typeof(newStock) != 'undefined' && Number(newStock) > 0) {
                        $('[name="stock"]', goodsRow).val(newStock);
                        $('.js-repair-checkbox', goodsRow).removeAttr('checked');
                        $('.js-repair-checkbox', goodsRow).attr('disabled', true);
                    }
                });
            },
            list: '.yqx-table .goods-datatr'
        });

        doc.on('click','.js-in-warehouse',function(){
            $.ajax({
                url:BASE_PATH + '/shop/user/operate/count?refer=orderQuoteDetail-warehouseIn'
            });
        });
        doc.on('click','.js-batch-add-goods',function(){
            $.ajax({
                url:BASE_PATH + '/shop/user/operate/count?refer=orderQuoteDetail-batchAddGoods'
            });
        });
        doc.on('click','.js-new-part-btn',function(){
            $.ajax({
                url:BASE_PATH + '/shop/user/operate/count?refer=warehouse-order-quete-detail'
            })
        });

        $('.js-checkbox-all').on('click', function () {
            if (!$(this).prop('checked')) {
                $('.goods-datatr').find('.js-repair-checkbox').each(function () {
                    if (!$(this).attr('disabled') && !$(this).attr('readonly')) {
                        $(this).removeAttr('checked');
                    }
                })
            } else {
                $('.goods-datatr').find('.js-repair-checkbox').each(function () {
                    if (!$(this).attr('disabled') && !$(this).attr('readonly')) {
                        $(this).prop('checked', 'checked');
                    }
                })
            }
        });
    });

    // 核算费用
    var calculatePriceUtil = calculatePriceUtil || {};
    calculatePriceUtil.drawup = function () {
        // 折扣
        var preDiscountRate = $('input[name="orderInfo.preDiscountRate"]').val() || 1;
        // 费用
        var preTaxAmount = $('input[name="orderInfo.preTaxAmount"]').val() || 0;
        // 优惠
        var prePreferentiaAmount = $('input[name="orderInfo.prePreferentiaAmount"]').val() || 0;
        // 代金券
        var preCouponAmount = $('input[name="orderInfo.preCouponAmount"]').val() || 0;

        if (Number(preDiscountRate) < 0 || Number(preDiscountRate) > 1) {
            $('input[name="orderInfo.preDiscountRate"]').val(1);
            preDiscountRate = 1;
        }

        // 物料
        var orderGoods = [];
        var goodsRowArray = $('input[name="goodsId"]').parents(".goods-datatr");
        goodsRowArray.each(function () {
            var goodsId = $('input[name="goodsId"]', this).val();
            var goodsPrice = $('input[name="goodsPrice"]', this).val();
            var goodsNumber = $('input[name="goodsNumber"]', this).val();
            var goodsAmount = $('input[name="goodsAmount"]', this).val();
            var discount = $('input[name="discount"]', this).val();

            if (goodsPrice != ""
                && goodsNumber != ""
                && goodsAmount != ""
                && discount != "") {
                orderGoods.push({
                    goodsId: goodsId,
                    goodsPrice: goodsPrice,
                    goodsNumber: goodsNumber,
                    goodsAmount: goodsAmount,
                    discount: discount
                });
            }
        });

        orderGoods = JSON.stringify(orderGoods);
        var $manageFeeAmount = null;
        var $manageFee = null;
        var orderServices = [];

        var serviceRowArray = $('input[name="serviceId"]').parents(".service-datatr");
        serviceRowArray.each(function () {
            var serviceId = $('input[name="serviceId"]', this).val();
            var servicePrice = $('input[name="servicePrice"]', this).val();
            var serviceHour = $('input[name="serviceHour"]', this).val();
            var serviceAmount = $('input[name="serviceAmount"]', this).val();
            var discount = $('input[name="discount"]', this).val();
            var type = $('input[name="type"]', this).val();
            // TODO flags 何意
            var flags = $('input[name="flags"]', this).val();
            if (typeof flags == "undefined" || flags == null) {
                flags = "";
            }
            if (flags == 'GLF') {
                $manageFeeAmount = $('input[name="serviceAmount"]', this);
                $manageFee = $('input[name="servicePrice"]', this);
            }
            var manageRate = $('input[name="manageRate"]', this).val();
            if (typeof manageRate == "undefined" || manageRate == '') {
                manageRate = 0;
            }

            if (servicePrice != "" &&
                serviceHour != "" &&
                discount != "") {
                if (serviceAmount == "") {
                    $('input[name="serviceAmount"]', this).val((servicePrice * serviceHour).toFixed(2));
                    serviceAmount = servicePrice * serviceHour;
                }
                orderServices.push({
                    serviceId: serviceId,
                    servicePrice: servicePrice,
                    serviceHour: serviceHour,
                    serviceAmount: serviceAmount,
                    discount: discount,
                    type: type,
                    flags: flags,
                    manageRate: manageRate
                });
            } else if (servicePrice == "" && serviceHour == "" &&
                serviceAmount == "" && discount == "") {
            }

        });
        orderServices = JSON.stringify(orderServices);

        $.ajax({
            type: 'post',
            data: {
                orderGoods: orderGoods,
                orderServices: orderServices
            },
            global: false,
            url: BASE_PATH + '/shop/order/calc_price'
        }).done(function (result) {
            if (result.success) {
                if (typeof $manageFee != 'undefined' && $manageFee != null) {
                    $manageFee.val(parseFloat(result.data.manageFee).toFixed(2));
                    $manageFeeAmount.val(parseFloat(result.data.manageFee).toFixed(2));
                }
                var goodsAmount = result.data.goodsAmount;
                var orderAmount = result.data.orderAmount;
                var goodsDiscount = result.data.goodsDiscount;

                // goodsAmount - goodsDiscount
                var goodsTotalAmount = parseFloat(goodsAmount) - parseFloat(goodsDiscount);
                $("#goodsTotalAmount").text(goodsAmount.toFixed(2));
                $("#goodsTotalDiscount").text(goodsDiscount.toFixed(2));
                $("#goodsActualAmount").text(goodsTotalAmount.toFixed(2));

                $("#orderTotalAmount").text(parseFloat(orderAmount).toFixed(2));

                var pretotalAmount = (parseFloat(orderAmount)
                * preDiscountRate
                + parseFloat(preTaxAmount)
                - parseFloat(prePreferentiaAmount)
                - parseFloat(preCouponAmount)).toFixed(2);

                $('input[name="orderInfo.preTotalAmount"]').val(pretotalAmount);
            }
        });
    };

    calculatePriceUtil.drawup();

});


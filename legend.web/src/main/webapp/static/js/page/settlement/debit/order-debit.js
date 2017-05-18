/**
 * zmx  2016-06-03
 * 确认收款 页面
 */

$(function () {
    var $doc = $(document);
    // 工单id
    var orderId = $("input[name='orderId']").val();
    // 车牌
    var carLicense = $('input[name="carLicense"]').val();
    // 应收
    var receivableAmount = Number($('input[name="receivableAmount"]').val());
    // 实收
    var paidAmount = Number($('input[name="paidAmount"]').val());
    // 挂账
    var signAmount = Number($('input[name="signAmount"]').val());

    // 冲红挂账
    var redSignAmount = Number($('input[name="redSignAmount"]').val());

    // 会员卡id
    var memberId = $('input[name="memberId"]').val();
    // 会员卡余额
    var memberBalance = Number($('input[name="memberBalance"]').val());
    // 使用他人券的时候
    var discountGuestMobile = $.trim($('input[name="discountGuestMobile"]').val());

    var $memberPayAmount = $('input[name="memberPayAmount"]');

    var $newPaidAmount = $('#newPaidAmount');
    var $newSignAmount = $('#newSignAmount');

    var settleTypeVIPCard;

    /**
     * 获得卡券请求参数
     * @param guestMobile
     * @param selectedCard
     * @returns {{discountSelectedBo}|*}
     */
    function getOrderParams(guestMobile, selectedCard) {
        var data = _getDiscountSelectedBo(guestMobile, selectedCard);

        data['orderId'] = orderId;

        return data;
    }

    /**
     * 获得已选择的优惠券
     * @param guestMobile
     * @param selectedCard
     * @returns { Object }
     *   @property discountSelectedBo
     */
    function _getDiscountSelectedBo(guestMobile, selectedCard) {
        var data = {};

        if (guestMobile !== undefined && guestMobile !== -1) {
            data['guestMobile'] = guestMobile;
        }

        if (selectedCard !== undefined && selectedCard !== -1) {
            data['selectedCard'] = selectedCard;
        }

        return {discountSelectedBo: data};
    }


    function createVIPCard() {
        if (memberId === '' || memberId == null) {
            settleTypeVIPCard = new Components.ChooseVIPCard({
                scope: '#settMemberCardBox',
                type: 1,
                tplType: 2,
                localSelect: true,
                disabledInput: !!discountGuestMobile,
                params: getOrderParams,
                inputTelFn: function (tel, ischange) {
                    discountGuestMobile = tel;
                    if(ischange) {
                        $memberPayAmount.prop('disabled', true).val('');
                        calculate();
                    }
                },
                selectedCardFn: function (cardInfo, selected) {
                    $memberPayAmount.prop('disabled', !selected).val('');
                    memberBalance = selected ? cardInfo.balance : 0;
                    memberId = selected ? cardInfo.cardId : null;
                },
                updateViewFn: calculate
            });

            Components.$broadcast.distribute(Components.$broadcast.topic, 1, getOrderParams(discountGuestMobile));
        }
    }

    // 计算金额
    function calculate() {

        var totalPayAmount = 0;

        var memberPayAmount = Number($memberPayAmount.val());
        if (memberPayAmount && memberPayAmount > 0) {
            totalPayAmount += memberPayAmount;
        }

        $(".js-pay").each(function () {
            var price = Number($.trim($(this).val()));
            if (price > 0) {
                totalPayAmount += price;
            }
        });
        check: {
            //预付定金支付
            var downPayment = $.trim($("#js-downPayment").val());
            if (downPayment != '') {
                //校验是否需要其他方式收款
                if (Number(downPayment) >= Number(receivableAmount) && Number(totalPayAmount) > 0) {
                    seajs.use('dialog', function (dg) {
                        dg.warn("预付定金大于应收金额，无需其他收款方式收款。");
                    });
                    break check;
                }
            }

            if (redSignAmount < 0 && (signAmount + redSignAmount - totalPayAmount) < 0) {
                seajs.use('dialog', function (dg) {
                    dg.warn("支付金额不能大于(挂账金额 + 冲红挂账金额)");
                });
                $newPaidAmount.text((paidAmount).toFixed(2));
                $newSignAmount.text((signAmount).toFixed(2));
                break check;
            }
        }

        // 应收 >=实收
        var signAmountTemp = (signAmount - totalPayAmount).toFixed(2);
        if (signAmountTemp < 0) {
            seajs.use('dialog', function (dg) {
                dg.warn("'实收金额'大于'应收金额',请重新收款");
            });
        }
        $newPaidAmount.text((paidAmount + totalPayAmount).toFixed(2));
        $newSignAmount.text(signAmountTemp);
    }

    /**
     * 使用别人的会员卡，需要发短信验证
     */
    function sendSMS(params) {
        var $belongOther = $('.js-belongOther'),
            belongOther = $belongOther.data('belongOther'),
            memberPayAmount = $memberPayAmount.val(),
            data, vipCardData;

        if (memberPayAmount > 0 && settleTypeVIPCard) {
            vipCardData = settleTypeVIPCard.getSelectedData();
            if (discountGuestMobile && vipCardData && discountGuestMobile == vipCardData.mobile) {
                data = {
                    name: vipCardData.customerName,
                    mobile: vipCardData.mobile,
                    license: carLicense
                };

                openCodeDialog(data, function () {
                    orderDebit(params);
                });
                return;
            }
        }  else if (memberPayAmount > 0 && belongOther == true) {
            data = {
                name: $belongOther.data('customerName'),
                mobile: $belongOther.val(),
                license: carLicense
            };

            openCodeDialog(data, function () {
                orderDebit(params);
            });
            return;
        }
        orderDebit(params);
    }

    function openCodeDialog(data, cb) {
        Components.codeDialog({
            data: data,
            callback: function () {
                cb && cb();
            }
        });
    }

    function orderDebit(data) {
        $.ajax({
            url: BASE_PATH + '/shop/settlement/debit/order-debit-post',
            type: 'post',
            contentType: 'application/json',
            data: JSON.stringify(data),
            dataType: 'json',
            success: function (result) {
                seajs.use('dialog', function (dg) {
                    if (result.success) {
                        dg.success("操作成功", function () {
                            window.location.href = BASE_PATH + "/shop/settlement/debit/order-detail?orderId=" + orderId + "&refer=order-debit";
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                });
            }
        });
    }

    seajs.use(['select', 'dialog', 'art', 'check'],
        function (st, dg, at, ck) {
            ck.init();

            //付款方式下拉列表
            st.init({
                dom: ".js-method",
                url: BASE_PATH + '/shop/payment/get-order-payment',
                showKey: "id",
                showValue: "name"
            });

            // 会员卡支付金额
            $doc.on('input.member', '.js-member-pay', function () {
                var val = $(this).val();
                if (memberId && memberBalance > 0) {
                    val = checkFloat(val, 2);
                    if (val == '.') {
                        val = 0;
                    }

                    if (val > memberBalance) {
                        dg.warn("会员卡支付金额不能大于会员卡余额");
                        val = 0;
                    }
                } else {
                    val = 0;
                }
                $(this).val(val);

                calculate();
            });

            // 增加收款方式
            $doc.on('click', '.js-addMethod', function () {
                var html = at('methodTpl', {});
                $('#methodcon').append(html);
            });

            // 支付方式金额
            $doc.on('blur', '.js-pay', function () {
                var val = $(this).val();
                val = checkFloat(val, 2);
                if (val == '.') {
                    val = 0;
                }
                $(this).val(val);
                calculate();
            });

            // 删除收款方式
            $doc.on('click', '.js-delete', function () {
                $(this).parents('.method-box').remove();
                calculate();
            });

            //确认收款
            $doc.on('click', '.js-comfirm', function () {
                // 数据校验
                if (!ck.check()) {
                    return;
                }

                var data = {
                    orderId: orderId,
                    billId: $('input[name="billId"]').val()
                };

                var totalPayAmount = 0;
                var memberPayAmount = Number($memberPayAmount.val());

                //组装会员卡数据
                if (memberId && memberPayAmount > 0) {
                    if (memberPayAmount > memberBalance) {
                        dg.warn("会员卡消费金额不能大于会员卡余额");
                        return;
                    }
                    var memberCard = {};
                    memberCard.id = memberId;
                    memberCard.payAmount = memberPayAmount;
                    memberCard.balance = memberBalance;

                    data.memberCard = memberCard;

                    totalPayAmount += memberPayAmount;
                }

                var flowList = [];
                //组装结算方式数组
                $(".js-pay").each(function () {
                    var flow = {};
                    var price = Number($.trim($(this).val()));
                    var parent = $(this).parents("#payment");
                    var paymentId = $("input[name='paymentId']", parent).val();
                    var paymentName = $("input[name='paymentName']", parent).val();
                    if (paymentId && price >= 0) {
                        flow.payAmount = price;
                        flow.paymentId = paymentId;
                        flow.paymentName = paymentName;
                        flowList.push(flow);

                        totalPayAmount += price;
                    }
                });
                //预付定金支付
                var downPayment = $.trim($("#js-downPayment").val());
                if (downPayment != '') {
                    //校验是否需要其他方式收款
                    if (Number(downPayment) >= Number(receivableAmount) && Number(totalPayAmount) > 0) {
                        dg.warn("预付定金大于应收金额，无需其他收款方式收款。");
                        return false;
                    }
                }

                var newSignAmount = Number($.trim($newSignAmount.text()));

                if (newSignAmount < 0) {
                    dg.warn("'实收金额'大于'应收金额',请重新收款");
                    return;
                }

                if (redSignAmount < 0) {
                    if (totalPayAmount > (signAmount + redSignAmount)) {
                        dg.warn("收款总金额不能大于(挂账金额 + 冲红挂账金额)");
                        return;
                    }
                } else {
                    if (totalPayAmount > signAmount) {
                        dg.warn("收款总金额不能大于上次挂账金额");
                        return;
                    }
                }

                data.flowList = flowList;

                data.remark = $.trim($('#remark').val());

                sendSMS(data);
            });

            (function initialize() {
                createVIPCard();
            })()

        });

    //表格展开收缩
    $doc.on('click', '.js-tableShow', function () {
        var btnText = $(this).find('span').text();
        $(this).next('.show-table').slideToggle(500);
        if (btnText == '展开') {
            $(this).find('span').text('收起');
            $(this).find('i').removeClass('icon-angle-down').addClass('icon-angle-up');
        } else {
            $(this).find('span').text('展开');
            $(this).find('i').removeClass('icon-angle-up').addClass('icon-angle-down');
        }
    });

    //返回按钮
    $doc.on('click', '.js-goback', function () {
        window.location.href = BASE_PATH + "/shop/settlement/debit/order-detail?orderId=" + orderId + "&refer=order-debit";
    });


});
/**
 * zmx  2016-06-03
 * 确认账单 页面
 */

$(function () {
        // 实收金额
    var paidAmount = 0,
        // 应收金额
        receivableAmount = 0,
        // 挂账金额
        signAmount = 0;

    // 会员卡实例
    var settleTypeVIPCard;

    // 优惠金额会员卡实例
    var discountAmountVIPCard;

    var guestMobile;
    
    var $memberPayAmount = $('[name="memberPayAmount"]'),
        $orderAmount = $('#orderAmount'),
        $receivableAmount = $('.js-receivable-amount'),
        $tqCouponAmount = $('#taoqiCouponAmount');

    /**
     * 创建会员卡
     */
    function createSettleTypeVIPCard (isDistribute) {
        destroySettleTypeVIPCard();
        settleTypeVIPCard = new Components.ChooseVIPCard({
            scope: '#settMemberCardBox',
            tplType: 2,
            localSelect: true,
            params: getOrderParams,
            selectedCardFn: function (cardInfo, selected, index) {
                // 更新优惠金额
                if (selected) {
                    discountAmountVIPCard.disabledCard(true, index);
                    $memberPayAmount.prop('disabled', false);
                } else {
                    discountAmountVIPCard.disabledCard(false);
                    $memberPayAmount.prop('disabled', true).val('');
                }
            },
            updateViewFn: function(data, params){
                guestMobile = params.discountSelectedBo.guestMobile;
                calcAccounts();
            }
        });

        isDistribute && distribute(-1, null, null, null, true);
    }

    /**
     * 销毁会员卡
     */
    function destroySettleTypeVIPCard () {
        if (settleTypeVIPCard) {
            settleTypeVIPCard.unbindUpdateView();
            settleTypeVIPCard = null;
        }
    }

    /**
     * 分发请求
     * @param guestMobile
     * @param selectedCard
     * @param selectedComboList
     * @param selectedCouponList
     * @param cache 是否读取缓存
     */
    function distribute (guestMobile, selectedCard, selectedComboList, selectedCouponList, cache) {
        var type, params;
        if (cache === true) {
            type = 0;
            params = null;
        } else {
            type = 1;
            params = getOrderParams(guestMobile, selectedCard, selectedComboList, selectedCouponList);
        }

        Components.$broadcast.distribute(Components.$broadcast.topic, type, params);
    }

    /**
     * 获得卡券请求参数
     * @param guestMobile
     * @param selectedCard
     * @param selectedComboList
     * @param selectedCouponList
     * @returns {{discountSelectedBo}|*}
     */
    function getOrderParams(guestMobile, selectedCard, selectedComboList, selectedCouponList) {
        var data = _getDiscountSelectedBo(guestMobile, selectedCard, selectedComboList, selectedCouponList);

        data['orderId'] = $('#orderId').val();

        return data;
    }

    /**
     * 获得已选择的优惠券
     * @param guestMobile
     * @param selectedCard
     * @param selectedComboList
     * @param selectedCouponList
     * @returns { Object }
     *   @property discountSelectedBo
     */
    function _getDiscountSelectedBo (guestMobile, selectedCard, selectedComboList, selectedCouponList) {
        var data = {};

        if (guestMobile !== undefined && guestMobile !== -1) {
            data['guestMobile'] = guestMobile;
        }

        if (selectedCard !== undefined && selectedCard !== -1) {
            data['selectedCard'] = selectedCard;
        }

        if (selectedComboList !== undefined && selectedComboList !== -1) {
            data['selectedComboList'] = selectedComboList;
        }

        if (selectedCouponList !== undefined && selectedCouponList !== -1) {
            data['selectedCouponList'] = selectedCouponList;
        }

        return { discountSelectedBo : data };
    }

    /**
     * 金额检查
     */
    function checkAccounts() {
        var vaild = true;

        calcAccounts();
        seajs.use('dialog', function (dg) {

            // 挂账金额
            if (signAmount < 0) {
                vaild = false;
                dg.warn('挂账金额小于0,请重新收款！');
                return;
            }

            // 应收金额
            if (receivableAmount < 0) {
                vaild = false;
                dg.warn('应收金额小于0，请检查！');
            }
        });

        return vaild;
    }

    /**
     * 应收金额，实收金额，挂账金额计算
     */
    function calcAccounts() {
        _calcAccountsReceivable();
        _calcAccountsPaid();
        _calcAccountsSign();
    }

    /**
     * 重置收款方式
     */
    function resetPaidChannel() {
        var html;

        if (signAmount < 0) {
            html = $('#methodTpl').html();
            $memberPayAmount.val('');
            $('#methodcon').html(html);
            calcAccounts();
        }
    }

    /**
     * 应收金额计算
     * @private
     */
    function _calcAccountsReceivable() {
        var orderAmount = $orderAmount.val(),
            // 优惠券优惠
            couponDiscountAmount = 0,
            // 会员卡优惠
            vipCardDiscountAmount = $('.js-discount-amount-input').val() || 0,
            // 淘汽优惠码优惠
            taoqiCouponAmount = $.trim($tqCouponAmount.text()) || 0,
            discountAmount;

        // 优惠券金额
        $.each(Components.getVoucherData() || [], function(i, obj) {
            couponDiscountAmount += obj.discountAmount;
        });

        discountAmount = Number(couponDiscountAmount) + Number(vipCardDiscountAmount) + Number(taoqiCouponAmount);
        receivableAmount = Number((orderAmount - discountAmount).toFixed(2));

        $('.js-order-discount-amount').text(discountAmount.toFixed(2));
        $receivableAmount.text('¥' + receivableAmount.toFixed(2));
    }

    /**
     * 实收金额计算
     * @private
     */
    function _calcAccountsPaid() {
            // 会员余额
        var memberPayAmount = $memberPayAmount.val() || 0,
            otherPayAmount = 0;

        $('#methodcon').find('.js-pay').each(function () {
            otherPayAmount += Number(this.value || 0);
        });

        paidAmount = Number(memberPayAmount) + otherPayAmount;

        $('#paidAmountSP').text('¥' + paidAmount.toFixed(2));
        $('.js-paidAmountSP').text(paidAmount.toFixed(2));
        $('.js-vip-pay').text(Number(memberPayAmount).toFixed(2));
        $('.js-debit-pay').text(otherPayAmount.toFixed(2));
    }

    /**
     * 挂账金额计算
     * @private
     */
    function _calcAccountsSign() {
        signAmount = Number(receivableAmount - paidAmount);
        $('#signAmountSP').text('¥' + signAmount.toFixed(2));
        $('.js-reAmount').text(receivableAmount.toFixed(2));
        $('.js-payAmount').text(paidAmount.toFixed(2));
        $('.js-signAmountSP').text(signAmount.toFixed(2));
    }

    function getSelectedCardData() {
        var selectedData = discountAmountVIPCard.getSelectedData();
        if (selectedData) {
            return selectedData;
        } else if (settleTypeVIPCard) {
            selectedData = settleTypeVIPCard.getSelectedData();
            return selectedData;
        }
        return null;
    }

    seajs.use(['ajax', 'dialog', 'check', 'art', 'select'],
        function (ajax, dg, ck, art, st) {
            var $doc = $(document);
            var orderId = $.trim($("#orderId").val());
            var $vipCardTips = $('.vip-card-tips');
            var vipCardNote;

            dg.titleInit();
            ck.init();

            //付款方式下拉列表
            st.init({
                dom: ".js-method",
                url: BASE_PATH + '/shop/payment/get-order-payment',
                showKey: "id",
                showValue: "name"
            });

            // 实例化会员卡
            createSettleTypeVIPCard();

            // 实例化优惠金额会员卡
            discountAmountVIPCard = new Components.ChooseVIPCard({
                scope: '#discountAmountVIPCardBox',
                params: getOrderParams,
                autoSelected: 1,
                inputTelFn: function (tel, changed) {
                    if(changed) {
                        createSettleTypeVIPCard(true);
                        // 输入框内容改变，触发重新计算
                        $vipCardTips.html('');
                        $('.js-discount-amount-input').val('0.00');
                        calcAccounts();
                    }
                },
                selectedCardFn: function (cardInfo, selected) {

                    // 会员卡更新
                    if (selected) {
                        vipCardNote = '<span class="vip-note">说明：使用已选会员卡<em>' +
                            cardInfo.cardName + '（' + cardInfo.customerName + '，' + cardInfo.mobile + '）' +
                            '</em>结算，卡内余额<em>' + cardInfo.balance.toFixed(2) + '</em>元</span>' +
                            (cardInfo.balance > paidAmount ? '' : '，请充值');

                        destroySettleTypeVIPCard();
                        // 如果"优惠金额"没有选择会员卡，.vip-card-tips(结算方式)中的会员卡余额容器将被填充
                        $vipCardTips.html(vipCardNote);
                        $memberPayAmount.prop('disabled', false);
                        $('.js-discount-amount-input').val(cardInfo.finalDiscount.toFixed(2));
                        $('.js-discount-box').addClass('selected');
                    } else {

                        createSettleTypeVIPCard(true);
                        $vipCardTips.html('');
                        $memberPayAmount.prop('disabled', true).val('');
                        $('.js-discount-amount-input').val('0.00');
                        $('.js-discount-box').removeClass('selected');
                    }
                    calcAccounts();
                    resetPaidChannel();
                },
                updateViewFn: function(data, params){
                    guestMobile = params.discountSelectedBo.guestMobile;

                    var cardInfo = this.getSelectedData();

                    if (this.hasAvailableCard()) {
                        $('.js-card-err-msg').hide();
                    } else {
                        $('.js-card-err-msg').show();
                    }

                    // 优惠金额更新
                    $('.js-discount-amount-input').val(cardInfo ? cardInfo.discount.toFixed(2) : '0.00');
                }
            });

            $doc.on('change', '#taoqiCouponAmount', function () {
                calcAccounts();
                resetPaidChannel();
            });

            $doc.on('blur', '.js-discount-amount-input', function () {
                var $this = $(this),
                    val = $this.val();

                val = checkFloat(val, 2);
                $this.val(val !== '.' ? val : '0.00');

                checkAccounts();
            });

            $doc.on('blur', 'input[name="memberPayAmount"]', function () {
                var val = $.trim(this.value),
                    vipCardData = getSelectedCardData(),
                    vipCardBalance = vipCardData ? vipCardData.balance : 0;

                if (Number(val) > vipCardBalance) {
                    ck.showCustomMsg('卡内余额不足！', this);
                    this.value = vipCardBalance;
                }

                checkAccounts();
            });

            // tips金额计算提示
            $('.js-tag').hover(function () {
                $(this).next('.total-hide').removeClass('hide');
            }, function () {
                $(this).next('.total-hide').addClass('hide');
            });

            // 工单价目明细表格展开/收起
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
                util.goBack();
            });

            //淘汽优惠券
            $doc.on('change', '.js-check-taoqicouponsn', function () {
                var $this = $(this);
                var taoqiCouponSn = $.trim(this.value);
                if (taoqiCouponSn == '') {
                    $tqCouponAmount.val('');
                    calcAccounts();
                    return false;
                }
                var orderId = $.trim($('#orderId').val());
                $.ajax({
                    url: BASE_PATH + '/shop/settlement/get_taoqi_coupon_amount',
                    data: {
                        taoqiCouponSn: taoqiCouponSn,
                        orderId: orderId
                    },
                    success: function (json) {
                        if (json.success) {
                            //优惠券存在
                            var receivableAmount = Number($receivableAmount.val());
                            if (receivableAmount <= 0) {
                                dg.fail('应收金额小于或等于0，无需使用淘汽优惠券');
                                $tqCouponAmount.val('');
                                $this.val('');
                                return false;
                            }
                            var taoqiCouponAmount = Number(json.data);
                            if (receivableAmount < taoqiCouponAmount) {
                                taoqiCouponAmount = receivableAmount;
                            }
                            $tqCouponAmount.val(taoqiCouponAmount.toFixed(2));
                        } else {
                            //优惠券不不不不存在
                            dg.fail(json.errorMsg);
                            $tqCouponAmount.val('');
                            $this.val('');
                        }
                        calcAccounts();
                    }
                });
            });

            // 增加收款方式
            $doc.on('click', '.js-addMethod', function () {
                var html = $('#methodTpl').html();
                $('#methodcon').append(html);
            });

            // 删除收款方式
            $doc.on('click', '.js-delete', function () {

                var payChannelDiv = $("#methodcon");
                var payChannels = $(".paychannel", payChannelDiv);
                // 至少预留一个支付方式
                if (payChannels.length == 1) {
                    var parent = $(this).parents(".paychannel");
                    $("input[name='payAmount']", parent).val("");
                    $("input[name='paymentName']", parent).val("");
                    $("input[name='paymentId']", parent).val("");
                    dg.warn("至少保留一条支付方式");
                } else {
                    $(this).parent('.method-box').remove();
                }

                // 计算挂账金额\实收金额
                calculateSignAmount();
            });

            // 支付方式金额
            $doc.on('blur', '.js-pay', function () {
                var payAmount = $(this).val();
                payAmount = checkFloat(payAmount, 2);
                if (payAmount == '.') {
                    payAmount = 0;
                }

                $(this).val(payAmount);

                checkAccounts();

                // 有效支付方式
                var parent = $(this).parents(".paychannel");
                var paymentId = $("input[name='paymentId']", parent).val();
                var payChannel = $(".js-method", parent);

                if (typeof(paymentId) === "undefined" || paymentId === "") {
                    payChannel.click();
                    return false;
                }
            });

            // 收款
            $doc.on('click', '.js-confirm-bill', function () {
                if (!ck.check()) return false;
                var totalAmount = $.trim($("#totalAmount").val());
                if (receivableAmount < 0) {
                    dg.warn("应收金额小于0，无法确认账单");
                    return false;
                }
                var remark = $.trim($("#remark").val());
                var debitBillBo = {
                    relId: orderId,
                    totalAmount: totalAmount,
                    receivableAmount: receivableAmount.toFixed(2),// 计算 0.3-0.2=0.09999999999999998。。会踩到坑
                    remark: remark
                };
                //券信息
                var orderDiscountFlowBoList = Components.getVoucherData();
                //优惠信息
                var memberCardId,
                    cardNumber,
                    cardDiscountReason,
                    accountId,
                    discountAmount = $.trim($(".js-discount-amount-input").val() || 0),
                    vipCardInfo = getSettleMemberCardInfo();

                if (vipCardInfo.discountCard) {
                    memberCardId = vipCardInfo.cardId;
                    cardNumber = vipCardInfo.cardNumber;
                    cardDiscountReason = vipCardInfo.discountDesc;
                    accountId = vipCardInfo.accountId;
                }

                //淘汽优惠券信息
                var taoqiCouponSn = $.trim($("#taoqiCouponSn").val());
                var taoqiCouponAmount = $.trim($("#taoqiCouponAmount").val());
                var paidAmount;
                // 收款渠道 [[

                // 会员余额支付
                var memberBalancePay = $.trim($('input[name="memberPayAmount"]').val() || 0);
                var memberIdForSettle;

                if (memberBalancePay > 0 && typeof vipCardInfo.discountCard == 'boolean') {
                    memberIdForSettle = vipCardInfo.cardId;
                }

                // 收款方式列表
                var payChannelBoList = [];
                var payChannelAmount = Number(0);
                //组装结算方式数组
                $(".js-pay").each(function () {
                    var flow = {};
                    var price = Number($.trim($(this).val()));
                    var parent = $(this).parents(".paychannel");
                    var paymentId = $("input[name='paymentId']", parent).val();
                    var paymentName = $("input[name='paymentName']", parent).val();
                    if (paymentId && $.isNumeric(price) && price >= 0) {
                        flow.payAmount = price;
                        flow.channelId = paymentId;
                        flow.channelName = paymentName;
                        flow.remark = remark;
                        payChannelBoList.push(flow);

                        payChannelAmount = Number(price) + Number(payChannelAmount);
                    }
                });
                if (payChannelBoList.length == 0) {
                    dg.warn("至少保留一条支付方式");
                    return false;
                }
                // 会员余额支付+收款方式支付 + 预付定金
                paidAmount = Number(memberBalancePay) + Number(payChannelAmount);
                //预付定金支付
                var downPayment = Number($(".js-downPayment").val()) || 0;
                if (downPayment != '') {
                    if (downPayment >= receivableAmount) {
                        if ((Number(paidAmount) > 0)) {
                            dg.warn("预付定金大于应收金额，无需其他收款方式收款。");
                            return false;
                        }
                        downPayment = receivableAmount;
                    }
                    paidAmount += Number(downPayment.toFixed(2));
                }
                //]]
                // IF 实付金额 > 应收金额 THEN 重新收款
                if (Number(paidAmount) > receivableAmount) {
                    dg.warn("'实付金额' 大于 '应收金额',请重新收款");
                    return false;
                }

                var confirmBillBo = {
                    debitBillBo: debitBillBo,
                    orderDiscountFlowBoList: orderDiscountFlowBoList,
                    memberCardId: memberCardId,
                    accountId: accountId,
                    cardNumber: cardNumber,
                    cardDiscountReason: cardDiscountReason,
                    memberBalancePay: memberBalancePay,
                    memberIdForSettle: memberIdForSettle,
                    discountAmount: discountAmount,
                    taoqiCouponSn: taoqiCouponSn,
                    taoqiCouponAmount: taoqiCouponAmount,
                    payChannelBoList: payChannelBoList
                };

                //遍历优惠券，提示是否有优惠的券金额为0的
                var flags = false;
                if (orderDiscountFlowBoList.length > 0) {
                    for (var i in orderDiscountFlowBoList) {
                        if (orderDiscountFlowBoList.hasOwnProperty(i)) {
                            var orderDiscountFlowBo = orderDiscountFlowBoList[i];
                            if (orderDiscountFlowBo.discountAmount <= 0) {
                                flags = true;
                            }
                        }
                    }
                }

                if (flags) {
                    dg.confirm("有优惠券优惠金额为0，您确定要收款吗？", function () {
                        confirmBill(confirmBillBo, dg, orderId);
                    });
                } else {
                    confirmBill(confirmBillBo, dg, orderId);
                }


            });

            function getSettleMemberCardInfo() {
                var data = {}, vipCardData;
                // 如果选中会员卡优惠
                if(discountAmountVIPCard){
                    vipCardData = discountAmountVIPCard.getSelectedData();
                    if (vipCardData) {
                        data.cardId = vipCardData.cardId;
                        data.cardNumber = vipCardData.cardNumber;
                        data.discountDesc = vipCardData.discountDesc;
                        data.accountId = vipCardData.accountId;
                        data.discountCard = true;
                    }
                }

                // 如果选中会员卡
                if(!data.discountCard && settleTypeVIPCard){
                    vipCardData = settleTypeVIPCard.getSelectedData();
                    if(vipCardData){
                        data.cardId = vipCardData.cardId;
                        data.discountCard = false;
                    }
                }

                return data;
            }

            function confirmBill(confirmBillBo, dg, orderId) {
                //基本收款单信息
                var otherVoucher = $('#guestCouponConunt').find('.voucher-t').hasClass('voucher-hover');

                var vipCardData = getSelectedCardData();

                if (otherVoucher || (guestMobile && vipCardData && guestMobile == vipCardData.mobile)) {
                    //使用其他客户优惠券弹窗
                    var name = $('#guestCouponConunt').find('.voucher-hover .vocher-name').eq(0).text() || vipCardData.customerName;
                    var mobile = $('.js-guestMobile').val() || vipCardData.mobile;
                    var data = {
                        name: name,
                        mobile: mobile,
                        license: $("#carLicense").val()
                    };
                    Components.codeDialog({
                        data: data,
                        callback: function () {
                            speedilySaveBill(confirmBillBo, dg, orderId);
                        }
                    })
                } else {
                    speedilySaveBill(confirmBillBo, dg, orderId);
                }
            }

//收款
            function speedilySaveBill(confirmBillBo, dg, orderId) {
                $.ajax({
                    type: 'POST',
                    dataType: "json",
                    contentType: "application/json",
                    url: BASE_PATH + '/shop/settlement/debit/speedily-save-bill',
                    data: JSON.stringify(confirmBillBo),
                    success: function (data) {
                        if (data.success) {
                            dg.success("确认成功", function () {
                                location.href = BASE_PATH + '/shop/settlement/debit/order-detail?orderId=' + orderId;
                            });
                        } else {
                            dg.fail(data.message);
                        }
                    }
                });
            }
        }); // seajs


    /*********************************券部分展示******************************/
    Components.CouponCard.init({
        carLicense:$("#carLicense").val(),
        callback:function(mobile, ischange){
            if(ischange) {
                createSettleTypeVIPCard(true);
                // 输入框内容改变，触发重新计算
                $('.vip-card-tips').html('');
                $('.js-discount-amount-input').val('0.00');
                $memberPayAmount.prop('disabled', true).val('');
            }
            calcAccounts();
        }
    });

    distribute();
});




/**
 * zmx  2016-06-03
 * 确认账单 页面
 */

$(function () {
    var discountAmountVIPCard;

    var $orderAmount = $('#orderAmount'),
        $receivableAmount = $('.js-receivable-amount'),
        $tqCouponAmount = $('.js-check-taoqicouponsn');

    var receivableAmount = 0,
        orderAmount = Number($orderAmount.val()),
        orderId = $('#orderId').val(),
        guestMobile;

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

        data['orderId'] = orderId;

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
     * 应收金额计算
     * @private
     */
    function calcAccountsReceivable() {
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

    function getSelectedCardData() {
        var selectedData = discountAmountVIPCard.getSelectedData();
        if (selectedData) {
            return selectedData;
        }
        return null;
    }

    /**
     * 金额检查
     */
    function checkAmount() {
        var vaild = true;

        calcAccountsReceivable();
        seajs.use('dialog', function (dg) {
            // 应收金额
            if (receivableAmount < 0) {
                vaild = false;
                dg.warn('应收金额小于0，请检查！');
            }
        });

        return vaild;
    }

    seajs.use(['ajax', 'dialog', 'check', 'art'],
        function (ajax, dg, ck, art) {
        dg.titleInit();
        ck.init();
        var $doc = $(document);

        // 实例化优惠金额会员卡
        discountAmountVIPCard = new Components.ChooseVIPCard({
            scope: '#discountAmountVIPCardBox',
            params: getOrderParams,
            autoSelected: 1,
            selectedCardFn: function (cardInfo, selected) {

                if (selected) {
                    $('.js-discount-box').addClass('selected');
                } else {
                    $('.js-discount-box').removeClass('selected');
                }

                // 会员卡更新
                $('.js-discount-amount-input').val(selected ? cardInfo.finalDiscount.toFixed(2) : '0.00');
                // 输入框内容改变，触发重新计算
                calcAccountsReceivable();
            },
            updateViewFn: function (data, params) {
                var cardInfo = this.getSelectedData();
                guestMobile = params.discountSelectedBo.guestMobile;

                if (this.hasAvailableCard()) {
                    $('.js-card-err-msg').hide();
                } else {
                    $('.js-card-err-msg').show();
                }
                
                // 优惠金额更新
                $('.js-discount-amount-input').val(cardInfo ? cardInfo.discount.toFixed(2) : '0.00');
                // 输入框内容改变，触发重新计算
                calcAccountsReceivable();
            }
        });

        //总数提示
        $doc.on('mouseover', '.js-tag', function () {
            $(this).next('.total-hide').removeClass('hide');
        });
        $doc.on('mouseleave', '.js-tag', function () {
            $(this).next('.total-hide').addClass('hide');
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
            util.goBack();
        });

        //优惠
        $doc.on('input', '.js-discount-amount-input', function () {
            var $this = $(this),
                val = $this.val();

            val = checkFloat(val, 2);
            $this.val(val !== '.' ? val : '0.00');

            checkAmount();
        });

        //淘汽优惠券
        $doc.on('change', '.js-check-taoqicouponsn', function () {
            var $this = $(this);
            var taoqiCouponSn = $.trim(this.value);
            if (taoqiCouponSn == '') {
                $tqCouponAmount.val('');
                checkAmount();
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
                    checkAmount();
                }
            });
        });

        //确认账单
        $doc.on('click', '.js-confirm-bill', function () {
            if (!ck.check()) return false;
            //基本收款单信息

            if (receivableAmount < 0) {
                dg.warn("应收金额小于0，无法确认账单");
                return false;
            }
            var remark = $.trim($("#remark").val());
            var debitBillBo = {
                relId: orderId,
                totalAmount: orderAmount,
                receivableAmount: receivableAmount.toFixed(2),
                remark: remark
            };
            //券信息
            var orderDiscountFlowBoList = Components.getVoucherData();
            //优惠信息
            var vipCardData = discountAmountVIPCard.getSelectedData();
            var memberCardId,
                cardNumber,
                cardDiscountReason,
                accountId,
                discountAmount = $.trim($(".js-discount-amount-input").val() || 0);
            //淘汽优惠券信息
            var taoqiCouponSn = $.trim($("#taoqiCouponSn").val());
            var taoqiCouponAmount = $.trim($("#taoqiCouponAmount").val());

            if (vipCardData) {
                memberCardId = vipCardData.cardId;
                cardNumber = vipCardData.cardNumber;
                cardDiscountReason = vipCardData.discountDesc;
                accountId = vipCardData.accountId;
            }

            var confirmBillBo = {
                debitBillBo: debitBillBo,
                orderDiscountFlowBoList: orderDiscountFlowBoList,
                memberCardId: memberCardId,
                accountId: accountId,
                cardNumber: cardNumber,
                cardDiscountReason: cardDiscountReason,
                discountAmount: discountAmount,
                taoqiCouponSn: taoqiCouponSn,
                taoqiCouponAmount: taoqiCouponAmount
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
                dg.confirm("有优惠券优惠金额为0，您确定要确认账单吗？", function () {
                    confirmBill(confirmBillBo, dg, orderId);
                }, function () {
                    return false;
                });
            } else {
                confirmBill(confirmBillBo, dg, orderId);
            }
        });
    });

    function confirmBill(confirmBillBo, dg, orderId) {
        //基本收款单信息
        var otherVoucher = $('#guestCouponConunt').find('.voucher-t').hasClass('voucher-hover');

        var vipCardData = getSelectedCardData();
        
        if(otherVoucher || (guestMobile && vipCardData && guestMobile == vipCardData.mobile)) {
            //使用其他客户优惠券弹窗
            var name = $('#guestCouponConunt').find('.voucher-hover .vocher-name').eq(0).text() || vipCardData.customerName;
            var mobile = $('.js-guestMobile').val() || vipCardData.mobile;
            var data = {
                name:name,
                mobile:mobile,
                license: $("#carLicense").val()
            };
            Components.codeDialog({
                data:data,
                callback:function(){
                    commonConfirmBill(confirmBillBo, dg, orderId);
                }
            })
        } else {
            commonConfirmBill(confirmBillBo, dg, orderId);
        }
    }

    function commonConfirmBill(confirmBillBo, dg, orderId){
        $.ajax({
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            url: BASE_PATH + '/shop/settlement/debit/save-debit-bill',
            data: JSON.stringify(confirmBillBo),
            success: function (result) {
                if (result.success) {

                    dg.success("确认成功", function () {
                        var debitBill = result.data;
                        var signAmount = debitBill.signAmount;
                        if(signAmount > 0){
                            //挂账，则跳转到收款页面
                            window.location.href = BASE_PATH + '/shop/settlement/debit/order-debit?orderId=' + orderId;
                        }else{
                            //结清，跳转到详情页
                            window.location.href = BASE_PATH + '/shop/settlement/debit/order-detail?orderId=' + orderId;
                        }
                    });
                } else {

                    dg.fail(result.message);
                }
            }
        });
    }



    /*********************************券部分展示******************************/
    Components.CouponCard.init({
        carLicense:$("#carLicense").val(),
        callback:calcAccountsReceivable
    });
    distribute();
});


/**
 * 买保险送服务包
 * Created by zmx on 16/9/22.
 */
$(function () {
    addHeadStyle(4);

    seajs.use('art', function (at) {
        // 服务包提示信息价格加高亮
        at.helper('setMark', function (data) {
            if (data == null) {
                return data;
            }
            return data.replace(/(\d+)元/, function ($0, $1) {
                return '<span class="mark">' + $1 + '</span>元';
            });
        });
    });

    seajs.use([
        'art',
        'dialog'
    ], function (at, dg) {
        dg.titleInit();
        var formId = $('.form-id').val();
        var insuredFee = $('.js-insuredFee').val();
        //初始化优惠券列表
        couponList(insuredFee);
        //服务包
        $.ajax({
            url: BASE_PATH + '/insurance/anxin/api/getPackageList?formId=' + formId + '&isVirtualForm=true',
            success: function (result) {
                if (result.success) {
                    var html = at('servicePackTpl', {json: result});
                    $('#servicePack').html(html);
                } else {
                    dg.fail(result.errorMsg);
                }
            }
        });

        //金额计算
        var colorDialog = null;
        $(document).on('click', '.js-pack-check', function () {
            var marketPrice = $(this).siblings('.pack-content').find('.js-market-price').text();
            var colorType = $(this).next('.pack-content').find('.color-type').val();
            $('.colour-type').val('');
            //防冻液颜色确认
            if (colorType) {
                var html = $('#dialogTpl').html();
                colorDialog = dg.open({
                    area: ['400px', '255px'],
                    content: html,
                    closeBtn: 0
                })
            }
            $.ajax({
                url: BASE_PATH + '/insurance/anxin/virtual/flow/virtual-fee?totalFee=' + marketPrice,
                success: function (result) {
                    if (result.success) {
                        $('.service-amount').text((result.data.secondPaidAmount + result.data.firstPaidAmount).toFixed(2));
                        // $('.secound-paid-amount').text((result.data.secondPaidAmount).toFixed(2));
                        $('.first-paid-amount').text((result.data.firstPaidAmount).toFixed(2));
                        $('.deduction-amount').text((result.data.deductionAmount).toFixed(2));
                        //获取服务包后重新获取优惠券列表
                        couponList(marketPrice);
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            })
        });

        //支付
        $(document).on('click', '.js-pay', function () {
            var packCheck = $('.js-pack-check').is(':checked');
            if (packCheck) {
                var basicId = $('.basic-id').val(),
                    packageId = $('.js-pack-check:checked').next('.pack-content').find('.package-id').val(),
                    prePayFee = $('#prePayFee').text(),
                    colourType = $('.colour-type').val(),
                    $couponChecked = $('li.coupon-checked'),
                    couponId = $couponChecked.data('id'),
                    $couponCheckedL = $couponChecked.length,
                    canUseCoupon = $('li.coupon-list-li:not(.disabled)').length;       //可使用的优惠券
                if (canUseCoupon > 0) {           //有可使用的优惠券
                    if ($couponCheckedL <= 0) {
                        dg.confirm('当前车主有可用优惠劵未使用，是否确定立即提交订单', function () {
                            $.ajax({                //去支付的接口
                                type: 'post',
                                url: BASE_PATH + '/insurance/anxin/virtual/flow/virtual-update-service',
                                data: {
                                    basicId: basicId,
                                    packageId: packageId,
                                    formId: formId,
                                    prePayFee: prePayFee,
                                    colourType: colourType
                                },
                                success: function (result) {        //使用优惠券的接口
                                    if (result.success) {
                                        var orderSn = result.data;
                                        window.location.href = BASE_PATH + '/insurance/anxin/virtual/flow/card-select?totalFee=' + prePayFee + '&orderSn=' + orderSn;
                                    } else {
                                        dg.fail(result.errorMsg);
                                    }
                                }
                            });
                        })
                    } else {
                        couponCheck(couponId, packageId, basicId, prePayFee, colourType);
                    }
                } else {                          //没可使用的优惠券
                    $.ajax({                //去支付的接口
                        type: 'post',
                        url: BASE_PATH + '/insurance/anxin/virtual/flow/virtual-update-service',
                        data: {
                            basicId: basicId,
                            packageId: packageId,
                            formId: formId,
                            prePayFee: prePayFee,
                            colourType: colourType
                        },
                        success: function (result) {
                            if (result.success) {
                                var orderSn = result.data;
                                window.location.href = BASE_PATH + '/insurance/anxin/virtual/flow/card-select?totalFee=' + prePayFee + '&orderSn=' + orderSn;
                            } else {
                                dg.fail(result.errorMsg)
                            }
                        }
                    })
                }
            } else {
                dg.fail('请选择服务包');
            }
        });

        $(document).on('click', '.js-back-btn', function () {
            var basicId = $('.basic-id').val();
            $.ajax({
                url: BASE_PATH + "/insurance/anxin/virtual/flow/virtual-service-return",
                data: {
                    id: basicId
                },
                type: 'GET',
                success: function (result) {
                    if (result.success) {
                        var data = JSON.stringify(result.data);
                        sessionStorage.setItem("virtualData", data);
                        util.goBack();
                    } else {
                        dg.fail(result.errorMsg);
                    }

                }
            })
        });

        $(document).on('click', '.btn-box a', function () {
            $(this).addClass('current').siblings().removeClass('current');
        });

        $(document).on('click', '.js-color-confim', function () {
            var colorType = $('.btn-box').find('.current').data('color');
            $('.colour-type').val(colorType);
            if (!colorType) {
                dg.fail('请选择颜色');
                return;
            }
            dg.close(colorDialog);
        });
        //优惠券选择,点击时校验优惠券能否用
        $(document).on('click', '.coupon-list-li:not(.disabled)', function () {
            var $this = $(this),
                $thisState = $this.data('state'),
                freezeTime = $this.data('freezetime'),
                couponId = $this.data('id'),
                $deductibleAmount = $('.discount-amount'),
                deductibleAmount = $this.data('deductibleamount');
            if ($this.hasClass('coupon-checked')) {
                $this.removeClass('coupon-checked');
                $deductibleAmount.text("0.00");
                return;
            }
            $this.addClass('coupon-checked').siblings().removeClass('coupon-checked');
            $deductibleAmount.text(deductibleAmount.toFixed(2));
            if ($thisState == 2) {
                dg.confirm('由于您于' + toTime(freezeTime) + '使用过此优惠劵 优惠劵已被投保单占用 请确认是否需要释放优惠劵', function () {
                    // 确认往后台掉接口
                    $.ajax({
                        url: BASE_PATH + "/insurance/anxin/coupon/coupon-thaw",
                        data: {
                            couponId: couponId
                        },
                        success: function (result) {
                            if (result.success) {
                                $this.data('state', 1);
                                dg.success('优惠券已释放，请重新选择优惠券');
                            } else {
                                dg.fail(result.errorMsg);
                            }
                        }
                    });
                }, function () {
                    $this.removeClass('coupon-checked');
                    $deductibleAmount.text("0.00");
                    return;
                });
            }
        });
        //优惠券列表
        function couponList(Fee) {
            $('.discount-amount').text("0.00");
            var vehiclePhone = $('.js-mobile').val();
            $.ajax({
                url: BASE_PATH + "/insurance/anxin/coupon/coupon-list",
                data: {
                    amount: Fee,
                    mobile: vehiclePhone
                },
                success: function (result) {
                    if (result.success) {
                        var allData = at("couponTpl", result);
                        $(".coupon-list").html(allData);
                        $('.question-mark').hover(function () {
                            $(this).parents('.coupon-list-box').siblings('.coupon-use-describe').show();
                        }, function () {
                            $(this).parents('.coupon-list-box').siblings('.coupon-use-describe').hide();
                        });
                    }
                }
            });
        }

        //
        function couponCheck(couponId, packageId, basicId, prePayFee, colourType) {
            var serviceAmount = $('.service-amount').text();
            var useMode = $('li.coupon-checked').data('couponusemode');
            if (useMode == '1') {
                dg.warn("抱歉，您选择的优惠劵只能用于买保险返奖励金");
                return;
            }
            $.ajax({
                url: BASE_PATH + "/insurance/anxin/coupon/coupon-check",
                data: {
                    couponId: couponId,
                    mode: 2,
                    amount: serviceAmount
                },
                success: function (result) {
                    if (result.success) {
                        $.ajax({                //去支付的接口
                            type: 'post',
                            url: BASE_PATH + '/insurance/anxin/virtual/flow/virtual-update-service',
                            data: {
                                basicId: basicId,
                                packageId: packageId,
                                formId: formId,
                                prePayFee: prePayFee,
                                colourType: colourType
                            },
                            success: function (result) {        //使用优惠券的接口
                                if (result.success) {
                                    var orderSn = result.data;
                                    useCoupon(couponId, orderSn, prePayFee)
                                } else {
                                    dg.fail(result.errorMsg)
                                }
                            }
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        }

        //使用优惠券接口
        function useCoupon(couponId, orderSn, prePayFee) {
            $.ajax({
                url: BASE_PATH + "/insurance/anxin/coupon/coupon-use",
                data: {
                    couponId: couponId,
                    mode: 2,
                    orderSn: orderSn
                },
                success: function (result) {
                    if (result.success) {
                        window.location.href = BASE_PATH + '/insurance/anxin/virtual/flow/card-select?totalFee=' + prePayFee + '&orderSn=' + orderSn;
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });

        }

        //将时间戳转换成时间格式
        function toTime(Time) {
            var time = new Date(Time);
            var year = time.getFullYear();      //年
            var mon = time.getMonth() + 1;        //月
            var mon2 = mon < 10 ? "0" + mon : mon;
            var date = time.getDate();          //日
            var date2 = date < 10 ? "0" + date : date;
            var hour = time.getHours();         //时
            var hour2 = hour < 10 ? "0" + hour : hour;
            var min = time.getMinutes();        //分
            var min2 = min < 10 ? "0" + min : min;
            var sec = time.getMinutes();        //秒
            var sec2 = sec < 10 ? "0" + sec : sec;
            return year + "-" + mon2 + "-" + date2;
        }
    })
});
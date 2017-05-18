$(function () {
    var cardTypeIds = [];
    var link = '<a href="javascript:;" class="js-retreat-card">退卡</a>';
    var tips = '该客户已办理三张会员卡，重新办理需先' + '';
    var cardOnOff = true, lastCardNum = null, cardNumber = null, carData = null;
    var doc = $(document);
    seajs.use([
        'downlist', 'dialog', 'art', 'check', 'select'
    ], function (dl, dg, at, ck, st) {

        /*********************** 公共部分 **********************/
            //检查
        ck.init();
        //溢出隐藏
        dg.titleInit();
        carLicense();
        //tab切换部分
        $(document).on('click', '.js-tab', function () {
            var index = $(this).index();
            var url, tpl;
            $(this).addClass('tab-hover').siblings().removeClass('tab-hover');
            $('.tab-con').eq(index).show().siblings('.tab-con').hide();
            if (index == 0) {
                if ($('.tab-con').eq(0).html() == '') {
                    //办理会员卡
                    url = BASE_PATH + "/account/member/get_all_card_info?type=1";
                    tpl = 'cardTypeListTpl';
                    templateFill(index, url, tpl, function () {
                        $('.card-tip-box').hide();
                    });
                }
            } else if (index == 1) {
                if ($('.tab-con').eq(1).html() == '') {
                    //办理计次卡
                    url = BASE_PATH + "/account/combo/comboInfo/listEnable";
                    tpl = 'chooseTpl';
                    templateFill(index, url, tpl);
                }
            } else if (index == 2) {
                if ($('.tab-con').eq(2).html() == '') {
                    //发放优惠券
                    url = BASE_PATH + "/account/coupon/search";
                    tpl = 'couponTpl';
                    templateFill(index, url, tpl);
                }
            } else if (index == 3) {
                if ($('.tab-con').eq(3).html() == '') {
                    //发放优惠套餐
                    var componHtml = $('#comboTpl').html();
                    $('.tab-con').eq(3).html(componHtml);
                    $.ajax({
                        url: BASE_PATH + '/account/coupon/suite/suiteList',
                        success: function (result) {
                            if (result.success) {
                                var plan = at('planTpl', {json: result});
                                $('#plan').html(plan);
                                $('.frame').eq(0).click();
                            }
                        }
                    })
                }
            }
        });

        getTab();
        function getTab(){
            var goodsIds = util.getPara('tab');
            if(goodsIds == 'cardNum'){
                $('.js-tab').eq(1).click();
            }
            else if (goodsIds == 'preferential'){
                $('.js-tab').eq(2).click();
            }else if(goodsIds == 'combo'){
                $('.js-tab').eq(3).click();
            }else{
                $('.js-tab').eq(0).click();
            }
        }

        //模板填充
        function templateFill(index, url, tpl, callback) {
            $.ajax({
                url: url,
                success: function (result) {
                    if (result.success) {
                        var html = at(tpl, result);
                        $('.tab-con').eq(index).html(html);
                        if (callback) {
                            callback();
                        }
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        }

        //重置
        doc.on('click','.js-reset',function(){
            $('.js-search').val('');
            $('#basicInfo').html('');
            $('.member-info').hide();
        });
        //会员卡展开
        doc.on("click", ".unfold", function () {
            var $this = $(this);
            if ($this.hasClass("on")) {
                $this.parent().next().hide();
                $this.removeClass("on").find(".unfold-txt").text("展开");
                $this.find(".icon-small").addClass("icon-angle-down").removeClass("icon-angle-up");
            } else {
                $this.parent().next().show();
                $this.closest(".silver-card").siblings().find(".card-info-extend").hide();
                $this.addClass("on").find(".unfold-txt").text("收起");
                $this.closest(".silver-card").siblings().find(".unfold-txt").text("展开");
                $this.find(".icon-small").addClass("icon-angle-up").removeClass("icon-angle-down");
            }
        });

        function addCardType(list) {
            for (var i = 0; i < list.length; i++) {
                var cardTypeId = list[i].cardTypeId;
                cardTypeIds.push(cardTypeId);
            }


        }

        var carId = $("#carId").val();
        if (carId != "") {
            $.ajax({
                url: BASE_PATH + "/account/queryAccountByLicense",
                data: {
                    carId: carId
                },
                success: function (r) {
                    accountInfoCallbackFn(r, carId);
                }
            });
        }

        var accountInfoCallbackFn = function (r, carId) {
            if (r.success) {
                $("#basicInfo").html(at("basicInfoTpl", r.data));
                $("#accountId").val(r.data.accountInfo.id);
                $(".member-info").show();
            } else {
                dg.open({
                    type: 1,
                    title: false,
                    area: ['600px', 'auto'],
                    content: at("completeCarTpl", {
                        message: r.errorMsg,
                        carId: carId,
                        carLicense: $(".member-search-box .js-search").val()
                    })
                });

            }
        };

        /**
         * 初始化服务顾问下拉框
         */
        st.init({
            dom: ".js-card-receiver",
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });
        st.init({
            dom: '.js-payment-select'
        });

        $(document).on('click', '.js-remove', function () {
            var $this = $(this),
                carid = $this.data("carid");
            dg.confirm('确认解绑车辆？', function () {
                //确定
                $.ajax({
                    type: 'post',
                    data: {carId: carid},
                    url: BASE_PATH + "/account/unBundleCarById",
                    success: function (r) {
                        if (r.success) {
                            $this.parent().remove();
                        } else {
                            dialog.fail(r.message);
                        }
                    }
                });
            }, function () {
                //取消
            });
        });

        /**
         * 提交
         * **/
        $(document).on("click", ".js-card-grant-submit", function () {
            var index = $('.tab-hover').data('index');
            var carLicense = $('.js-search').val();
            var accountId = $("input[name='accountId']").val();
            if (accountId == null || accountId == '') {
                dg.warn('请选择车牌信息');
                return;
            }
            if (index == 0) {
                var licenseCheck = $(".license-num1").length || 0,
                    memberNumCheck = $(".member-num").val(),
                    raidoSelect = $('input:radio[name="cardTypeId"]:checked'),
                    memberCheck = raidoSelect.next().find(".member-type").text(),
                    price = raidoSelect.next().find(".price span").text();
                if (!licenseCheck) {
                    dg.warn('请输入有效的车牌查询信息！');
                    return false;
                }
                if (cardOnOff == false) {
                    dg.warn('会员卡已存在!');
                    return false;
                }
                if (!memberNumCheck) {
                    dg.warn('请输入有效会员卡号！');
                    return false;
                }
                if (!memberCheck) {
                    dg.warn('选择会员卡类型！');
                    return false;
                }
                if (cardNumber) {
                    dg.warn('该客户已办理会员卡！');
                    return false;
                }
                if (cardTypeIds.indexOf(parseInt($("input[type='radio']:checked").val())) > -1) {
                    dg.warn('该客户已办理该类型会员卡！');
                    return false;
                }

                var result = {
                    type: memberCheck,
                    amount: price,
                    id: $("input[type='radio']").val() || 1,
                };

                dg.open({
                    type: 1,
                    title: false,
                    area: ['600px', 'auto'],
                    content: at('collection', result)
                });
            } else if (index == 1) {
                var radio = $("input[type='radio']:checked").val();
                if (radio == null || radio == '') {
                    dg.warn('请选择计次卡类型');
                } else {
                    var licenseCheck = $(".license-num1").length || 0,
                        combo = $('input:radio[name="comboId"]:checked'),
                        comboId = combo.val(),
                        comboName = combo.next().find(".comboName").text(),
                        comboPrice = combo.next().find(".salePrice").text();
                    var result = {
                        type: comboName,
                        amount: comboPrice,
                        id: comboId
                    };
                    if (!licenseCheck) {
                        dg.warn('请输入有效的车牌查询信息！');
                        return false;
                    }
                    dg.open({
                        type: 1,
                        title: false,
                        area: ['600px', 'auto'],
                        content: at('collectionMetting', result)
                    });
                }
            } else if (index == 2) {
                if (!ck.check()) {
                    return;
                }
                var data = {};
                var length = $('.js-move').length;
                var coupons = [];
                data.accountId = accountId;
                var number = 0;
                if (length == 0) {
                    dg.warn("请添加优惠券！");
                    return;
                } else {
                    for (var i = 0; i < length; i++) {
                        var couponCount = $("input[name='couponNum']").eq(i).val();
                        if (couponCount == "") {
                            dg.warn("请添加优惠券数量！");
                            return;
                        } else if (couponCount <= 0 || parseInt(couponCount) != couponCount) {
                            dg.warn("请输入大于零的整数！");
                            return;
                        }
                    }
                }
                $("[name='couponId']").each(function () {
                    var coupon = {};
                    coupon.id = $(this).val();
                    coupon.num = $("[name='couponNum']").eq(number).val();
                    coupons.push(coupon);
                    number++;
                });
                data.couponVos = coupons;

                $.ajax({
                    url: BASE_PATH + '/account/coupon/grant/insert',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    type: "POST",
                    success: function (result) {
                        if (result.success) {
                            dg.close();
                            dg.confirm('优惠券发放成功！是否打印回执单', function () {
                                util.print(
                                    BASE_PATH + '/account/coupon/grantPrint?id=' + +result.data.id, {
                                        afterPrint: function () {
                                            $('.js-search-btn').click();
                                            //发放优惠券
                                            var url = BASE_PATH + "/account/coupon/search";
                                            var tpl = 'couponTpl';
                                            templateFill(index, url, tpl);
                                        }
                                    }
                                );
                            }, function () {
                                $('.js-search-btn').click();
                                //发放优惠券
                                var url = BASE_PATH + "/account/coupon/search";
                                var tpl = 'couponTpl';
                                templateFill(index, url, tpl);
                                return false;
                            }, ['打印', '取消']);
                        } else {
                            dg.fail(result.errorMsg);
                        }

                    }
                });
            } else if (index == 3) {
                var data = {};
                data.paymentId = $("[name='paymentId']").val();
                data.paymentName = $("[name='paymentId']  option:selected").text();
                data.payAmount = $("[name='payAmountForCoupon']").val();
                data.note = $("[name='note']").val();
                data.accountId = accountId;
                data.couponSuiteId = $(".js-current").data("id");

                if (!ck.check()) {
                    return;
                }
                $.ajax({
                    url: BASE_PATH + '/account/coupon/suite/grant/insert',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    type: "POST",
                    success: function (result) {
                        if (result.success) {
                            dg.close();
                            dg.warn('');
                            dg.confirm('优惠套餐发放成功！是否打印回执单', function () {
                                util.print(
                                    BASE_PATH + '/account/coupon/grantPrint?id=' + +result.data.id, {
                                        afterPrint: function () {
                                            $('.js-search-btn').click();
                                            //发放优惠套餐
                                            var componHtml = $('#comboTpl').html();
                                            $('.tab-con').eq(3).html(componHtml);
                                            $.ajax({
                                                url: BASE_PATH + '/account/coupon/suite/suiteList',
                                                success: function (result) {
                                                    if (result.success) {
                                                        var plan = at('planTpl', {json: result});
                                                        $('#plan').html(plan);
                                                        $('.frame').eq(0).click();
                                                    }
                                                }
                                            })
                                        }
                                    }
                                );
                            }, function () {
                                $('.js-search-btn').click();
                                //发放优惠套餐
                                var componHtml = $('#comboTpl').html();
                                $('.tab-con').eq(3).html(componHtml);
                                $.ajax({
                                    url: BASE_PATH + '/account/coupon/suite/suiteList',
                                    success: function (result) {
                                        if (result.success) {
                                            var plan = at('planTpl', {json: result});
                                            $('#plan').html(plan);
                                            $('.frame').eq(0).click();
                                        }
                                    }
                                })
                                return false;
                            }, ['打印', '取消']);
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            }
        });

        //会员卡弹框提交按钮
        $(document).on("click", ".js-card-submit", function () {
            var carLicense = $('.js-search').val();
            if (!ck.check()) {
                return;
            }
            var data = {
                accountId: $("input[name=accountId]").val(),
                cardNumber: $("input[name=cardNumber]").val(),
                receiverName: $("input[name=receiverName]").val(),
                receiver: $("input[name=receiver]").val()
            };
            data.memberCardInfoId = $("input[name=cardTypeId]").filter(":checked").val();
            data.paymentId = $("input[name=paymentId]").val();
            data.paymentName = $("input[name=paymentName]").val();
            data.amount = $("input[name=amounts]").val();
            $.ajax({
                url: BASE_PATH + '/account/member/grant',
                data: data,
                type: "POST",
                success: function (result) {
                    if (result.success) {
                        dg.close();
                        dg.warn('');
                        dg.confirm('会员卡办理成功！是否打印回执单', function () {
                            util.print(
                                BASE_PATH + '/account/member/grantPrint?id=' + result.data.id + '&cardId=' + result.data.memberCard.id, {
                                    afterPrint: function () {
                                        $('.js-search-btn').click();
                                        //办理会员卡
                                        var url = BASE_PATH + "/account/member/get_all_card_info?type=1";
                                        var tpl = 'cardTypeListTpl';
                                        templateFill(0, url, tpl, function () {
                                            $('.card-tip-box').hide();
                                        });
                                    }
                                });
                        }, function () {
                            $('.js-search-btn').click();
                            //办理会员卡
                            var url = BASE_PATH + "/account/member/get_all_card_info?type=1";
                            var tpl = 'cardTypeListTpl';
                            templateFill(0, url, tpl, function () {
                                $('.card-tip-box').hide();
                            });
                            return false;
                        }, ['打印', '取消']);
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });

        //计次卡弹框提交按钮
        $(document).on("click", ".js-combo-submit", function () {
            var data = {};
            var carLicense = $('.js-search').val();
            var accountId = $("input[name='accountId']").val();
            data.comboInfoId = $("input[name='comboInfoId']").val();
            if (!ck.check()) {
                return;
            }

            if (accountId != null) {
                data.accountId = accountId;
            }

            data.recieverId = $("input[name='receiver']").val();
            data.recieverName = $("input[name='receiverName']").val();//服务顾问
            data.amount = $("input[name='amount']").val();
            data.payAmount = $("input[name='payAmountForCombo']").val();
            data.paymentId = $("input[name='paymentId']").val();
            data.paymentName = $("input[name='paymentName']").val();//支付方式
            $.ajax({
                url: BASE_PATH + '/account/combo/recharge',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(data),
                type: "POST",
                success: function (result) {
                    seajs.use('dialog', function (dg) {
                        if (result.success) {
                            dg.close();
                            dg.confirm('计次卡办理成功！是否打印回执单', function () {
                                util.print(BASE_PATH + '/account/combo/grantPrint?id=' + +result.data.id, {
                                    afterPrint: function () {
                                        $('.js-search-btn').click();
                                        //办理计次卡
                                        var url = BASE_PATH + "/account/combo/comboInfo/listEnable";
                                        var tpl = 'chooseTpl';
                                        templateFill(1, url, tpl);
                                    }
                                });
                            }, function () {
                                $('.js-search-btn').click();
                                //办理计次卡
                                var url = BASE_PATH + "/account/combo/comboInfo/listEnable";
                                var tpl = 'chooseTpl';
                                templateFill(1, url, tpl);
                                return false;
                            }, ['打印', '取消']);
                        } else {
                            dg.fail(result.message);
                        }
                    });

                }
            });
        });

        //新增车辆提交按钮
        $(document).on('click', '.js-bundle', function () {
            var license = $("input[name='license']").val();
            var accountId = $("#accountId").val();
            if (license == '') {
                dg.fail("车牌号不能为空");
                return;
            }
            $.ajax({
                url: BASE_PATH + "/account/bundleCar",
                type: 'post',
                data: {
                    license: license,
                    accountId: accountId
                },
                success: function (result) {
                    if (result.success) {
                        //车辆已经绑定账户
                        if (result.data.id != '' && result.data.id != undefined) {
                            $(".un-bundle-tip").removeClass("display: block");
                        } else {
                            //车辆绑定成功，返回
                            dg.success(result.data);
                            location.reload(true);
                        }
                    } else {
                        $(".tip").text(result.errorMsg);
                    }
                }
            });
        })
            //个人信息编辑按钮
            .on("click", ".js-edit-btn", function () {
                var cid = $(".personal-info").data("customerid");
                window.location.href = BASE_PATH + '/account/detail?refer=grant&customerId=' + cid;
            });

        //新建车辆
        $("body").on("click", ".js-new-car", function () {
            window.location = BASE_PATH + "/shop/customer/edit?page=0&&license=" + $(".js-search").val();
        });
        $(document).on('click','.js-search-btn',function(){
            var  carLicenseVal = $('.js-search').val();
            $.ajax({
                url: BASE_PATH + '/shop/customer/search/mobile',
                data:{
                    com_license:carLicenseVal
                },
                success: function (json) {
                    $.ajax({
                        url: BASE_PATH + "/account/queryAccountByLicense",
                        data: {
                            carId: $('#carId').val()
                        },
                        success: function (result) {
                            var cardNum = $('.js-card-num');
                            cardNum.prop('disabled', false).val('');
                            $('.card-tip-box').hide();
                            $('.card-tip').text(tips).append(link);
                            carData = result.data;
                            accountInfoCallbackFn(result,  $('#carId').val());
                            if (result.success) {
                                cardTypeIds = [];
                                lastCardNum = null;
                                addCardType(result.data.memberCardList);
                                if (result.data.memberCardList.length >= 3) {
                                    $('.js-card-num').prop('disabled', true);
                                    $('.card-tip-box').show();
                                    $('.icon-exclamation-sign').addClass('sign-red');
                                } else {
                                    cardNumber = null;
                                    cardNum.val('');
                                    cardNum.prop('disabled', false);
                                    $('.card-tip-box').hide();
                                    $('.icon-exclamation-sign').removeClass('sign-red');
                                }
                            }
                        }
                    });
                }
            })
        });

        //车牌选择
        function carLicense(){
            dl.init({
                url: BASE_PATH + '/shop/customer/search/mobile',
                searchKey: 'com_license',
                showKey: 'license',
                hiddenKey: 'id',
                dom: '.js-search',
                tplId: 'carLicenseTpl',
                hasTitle: false,
                hasInput: false,
                autoFill: true,
                callbackFn: function (data, dd) {
                    $.ajax({
                        url: BASE_PATH + "/account/queryAccountByLicense",
                        data: {carId: dd.id},
                        success: function (r) {
                            $('#carId').val(dd.id);
                            var cardNum = $('.js-card-num');
                            cardNum.prop('disabled', false).val('');
                            $('.card-tip-box').hide();
                            $('.card-tip').text(tips).append(link);
                            carData = r.data;
                            accountInfoCallbackFn(r, dd.id);
                            if (r.success) {
                                cardTypeIds = [];
                                lastCardNum = null;
                                addCardType(r.data.memberCardList);
                                if (r.data.memberCardList.length >= 3) {
                                    $('.js-card-num').prop('disabled', true);
                                    $('.card-tip-box').show();
                                    $('.icon-exclamation-sign').addClass('sign-red');
                                } else {
                                    cardNumber = null;
                                    cardNum.val('');
                                    cardNum.prop('disabled', false);
                                    $('.card-tip-box').hide();
                                    $('.icon-exclamation-sign').removeClass('sign-red');
                                }
                            }
                        }
                    });
                }
            });
        }

        /**
         * 会员卡号部分操作
         **/
        $(document).on('focus', '.js-card-num', function () {
            $('.card-tip-box').hide();
            if (lastCardNum != null) {
                if (lastCardNum != '') {
                    $('.input-tips').show();
                    $('.note-num').text(lastCardNum);
                }
            } else {
                $.ajax({
                    url: BASE_PATH + '/account/member/lastCard',
                    success: function (result) {
                        var num = '';
                        if (result.success) {
                            if (result.data) {
                                $('.input-tips').show();
                                num = result.data.cardNumber;
                                $('.note-num').text(num);
                            }

                            lastCardNum = num;
                        }
                    }
                })
            }
        });

        $(document).on('blur', '.js-card-num', function () {
            $('.input-tips').hide();
            var cardNumber = $(this).val();
            if (cardNumber) {
                $.ajax({
                    url: BASE_PATH + "/account/member/number/exists",
                    global: false,
                    data: {
                        cardNumber: cardNumber
                    },
                    success: function (result) {
                        if (result.success) {
                            if (result.data) {
                                $('.card-tip-box').hide();
                                $('.icon-exclamation-sign').removeClass('sign-red');
                                $('.icon-exclamation-sign').css('color', '');
                                cardOnOff = true;
                            }
                        } else {
                            $('.card-tip-box').show();
                            $('.card-tip-box .card-tip').text('会员卡已存在');
                            $('.icon-exclamation-sign').addClass('sign-red');
                            cardOnOff = false;
                        }
                    }
                });
            }
        });

        //退卡操作
        $(document).on('click', '.js-retreat-card', function () {
            window.location.href = BASE_PATH + '/account/member/back?accountId=' + $("#accountId").val()
        });

        //返回
        $(document).on('click', '.js-goback', function () {
            util.goBack();
        });
        if ($("#cardNumber").val()) {
            $('.js-card-num').prop('disabled', true);
            $('.icon-exclamation-sign').addClass('sign-red');
        }

        /****   优惠券部分  ****/
            // 添加基本服务(优惠券)
        getService({
            // 点击按钮的选择器
            dom: '.js-get-service',
            // 回调函数，处理选择的数据
            callback: function (json) {
                if (json.suiteNum > 0) {
                    $.ajax({
                        async: false,
                        type: 'get',
                        url: BASE_PATH + "/shop/shop_service_info/getPackageByServiceId",
                        data: {
                            serviceId: json.id
                        },
                        success: function (packageJson) {
                            if (packageJson.success) {
                                var html = at("serviceTpl", {json: packageJson.data.shopServiceInfoList});
                                $('#orderServiceTB').append(html);
                            }
                        }
                    });
                } else {
                    var jsons = [];
                    jsons[0] = json;
                    var html = at("serviceTpl", {json: jsons});
                    $('#orderServiceTB').append(html);
                }
            }
        });

        $(document).on('click', '.delete', function () {
            $(this).parents('tr').remove();
        })


        /**************优惠套餐部分**************/
        doc.on('click', '.frame', function () {
            var $el = $(this),
                id = $el.data('id'),
                price = $el.data('price');

            $el.addClass('js-current').siblings('.frame').removeClass('js-current');


            $("[name='price']").val(price);
            $.ajax({
                url: BASE_PATH + '/account/coupon/suite/get',
                contentType: 'application/json',
                data: {
                    id: id
                },
                success: function (result) {

                    if (result.success) {
                        var html = at('tableTpl', {json: result});
                        $('#tableFill').html(html).show();
                    } else {
                        dg.fail(result.errorMsg);
                    }

                }
            });

        })
    });
});


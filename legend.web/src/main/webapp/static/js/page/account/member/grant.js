$(function () {
    seajs.use('check', function (ck) {
        //可以传一个作用域,如果不传默认的是整个body
        ck.init();
    });
    var cardTypeIds = [];
    var link = '<a href="javascript:;" class="js-retreat-card">退卡</a>';
    var tips = '该客户已办理三张会员卡，重新办理需先' + '';
    var cardOnOff = true;
    var lastCardNum = null;

    var cardNumber = null;
    $('.card-tip-box').hide();
    var carData = null;
    seajs.use(['downlist', 'dialog', 'art'], function (dl, dg, at) {
        dg.titleInit();
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
                        $('.js-card-num').prop('disabled', false).val('');
                        $('.card-tip-box').hide();
                        $('.card-tip').text(tips).append(link);
                        carData = r.data;
                        accountInfoCallbackFn(r, dd.id);
                        if (r.success) {
                            cardTypeIds = [];
                            lastCardNum = null;
                            addCardType(r.data.memberCardList);
                            if (r.data.memberCardList.length >= 3) {
                                //cardNumber = $('.js-card-num').val(r.data.memberCard.cardNumber);
                                $('.js-card-num').prop('disabled', true);
                                $('.card-tip-box').show();
                                $('.icon-exclamation-sign').addClass('sign-red');
                            } else {
                                cardNumber = null;
                                $('.js-card-num').val('');
                                $('.js-card-num').prop('disabled', false);
                                $('.card-tip-box').hide();
                                $('.icon-exclamation-sign').removeClass('sign-red');
                            }
                        }
                    }
                });
            }
        });
    });

    function addCardType(list) {
        for (var i = 0; i < list.length; i++) {
            var cardTypeId = list[i].cardTypeId;
            cardTypeIds.push(cardTypeId);
        }


    }

    seajs.use([
        'art',
        'dialog'
    ], function (at, dg) {
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
    });

    var accountInfoCallbackFn = function (r, carId) {
        seajs.use(["dialog", "art"], function (dg, at) {
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
        });
    };

    seajs.use([
            'art',
            'ajax',
            'dialog',
            'select'
        ], function (art, ajax, dg, st) {
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
            $.ajax({
                type: 'post',
                url: BASE_PATH + "/account/member/get_all_card_info?type=1",
                success: function (r) {
                    if (r.success) {
                        var htmlText = art("cardTypeListTpl", {data: r.data});
                        $("#card-type-list").html(htmlText);
                    } else {
                        dg.fail(r.message);
                    }
                }
            });
        }
    );
    $(document).on('click', '.js-remove', function () {
        var $this = $(this),
            carid = $this.data("carid");
        seajs.use(['dialog'], function (dialog) {
            dialog.confirm('确认解绑车辆？', function () {
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
    });
    $(document)
        .on("click", ".js-card-grant-submit", function () {
            var licenseCheck = $(".license-num1").length || 0,
                memberNumCheck = $(".member-num").val(),
                raidoSelect = $('input:radio[name="cardTypeId"]:checked'),
                memberCheck = raidoSelect.next().find(".member-type").text(),
                price = raidoSelect.next().find(".price span").text();


            seajs.use('check', function (ck) {
                //可以传一个作用域,如果不传默认的是整个body
                ck.init();
            });
            if (!licenseCheck) {
                seajs.use(['dialog'], function (dialog) {
                    dialog.msg('请输入有效的车牌查询信息！');
                });
                return false;
            }
            if (cardOnOff == false) {
                seajs.use(['dialog'], function (dialog) {
                    dialog.msg('会员卡已存在!');
                });
                return false;
            }
            if (!memberNumCheck) {
                seajs.use(['dialog'], function (dialog) {
                    dialog.msg('请输入有效会员卡号！');
                });
                return false;
            }
            if (!memberCheck) {
                seajs.use(['dialog'], function (dialog) {
                    dialog.msg('选择会员卡类型！');
                });
                return false;
            }
            if (cardNumber) {
                seajs.use(['dialog'], function (dialog) {
                    dialog.msg('该客户已办理会员卡！');
                });
                return false;
            }
            if (cardTypeIds.indexOf(parseInt($("input[type='radio']:checked").val())) > -1) {
                seajs.use(['dialog'], function (dialog) {
                    dialog.msg('该客户已办理该类型会员卡！');
                });
                return false;
            }

            var result = {
                type: memberCheck,
                amount: price,
                id: $("input[type='radio']").val() || 1
            };
            seajs.use(['art', 'dialog'], function (art, dl) {
                dl.open({
                    type: 1,
                    title: false,
                    area: ['600px', 'auto'],
                    content: art('collection', result)
                });
            })

        })
        .on("click", ".js-submit", function () {
            var flag = true;
            seajs.use('check', function (ck) {
                if (!ck.check()) {
                    flag = false;
                }
            });
            if (!flag) {
                return;
            }
            var data = {
                accountId: $("input[name=accountId]").val(),
                cardNumber: $(".member-info input[name=cardNumber]").val(),
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
                    seajs.use('dialog', function (dg) {
                        if (result.success) {
                            dg.close();
                            dg.confirm('提交成功！是否打印回执单', function () {
                                util.print(
                                    BASE_PATH + '/account/member/grantPrint?id=' + result.data.id + '&cardId=' + result.data.memberCard.id, {
                                        afterPrint: function () {
                                            window.location = BASE_PATH + "/account/detail?flag=2&&accountId=" + data.accountId;
                                        }
                                    });
                            }, function () {
                                window.location = BASE_PATH + "/account/detail?flag=2&&accountId=" + data.accountId;
                            }, ['打印', '取消']);
                        } else {
                            dg.fail(result.message);
                        }
                    });
                }
            });
        })
        .on("click", ".unfold", function () {
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
        })
        .on('click', '.js-bundle', function () {
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
        .on("click", ".js-edit-btn", function () {
            var cid = $(".personal-info").data("customerid");
            window.location.href = BASE_PATH + '/account/detail?customerId=' + cid;
        });

    $("body").on("click", ".js-new-car", function () {
        window.location = BASE_PATH + "/shop/customer/edit?page=0&&license=" + $(".js-search").val();
    });

    seajs.use('dialog', function (dg) {
        $(document).on('blur', '.js-card-num', function () {
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

        $(document).on('click', '.js-card-num', function () {
            $('.card-tip-box').hide();
        });

        $(document).on('blur', '.js-card-num', function () {
            $('.input-tips').hide();
        });

        $(document).on('focus', '.js-card-num', function () {
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

    });


    //退卡操作
    seajs.use([
        'dialog',
        'art'
    ], function (dg, at) {
        $(document).on('click', '.js-retreat-card', function () {
            window.location.href = BASE_PATH + '/account/member/back?accountId=' + $("#accountId").val()
        });

        //返回
        $(document).on('click', '.js-goback', function () {
            util.goBack();
        });
    });


    if ($("#cardNumber").val()) {
        //$('.js-card-num').val($("#cardNumber").val());
        $('.js-card-num').prop('disabled', true);
        //$('.card-tip-box').show();
        $('.icon-exclamation-sign').addClass('sign-red');
    }
});


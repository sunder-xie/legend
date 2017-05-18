/**
 * Created by litan on 14-11-5.
 */
$(function () {
    var chooseShopDgId;

    //登录按钮
    $(document).on('click', '.login_c', function () {
        login();
    });

    $(document).on('keydown', function (e) {
        e = e || window.event;
        if (e.keyCode == 13) {
            login();
        }
    });
    //选择门店
    $(document).on('click', '.js-login-shop', function () {
        var shopId = $.trim($(this).data('id'));
        if (shopId == '') {
            taoqi.error("请选择门店");
            return false;
        }
        $("#shopId").val(shopId);
        login(shopId);
    });

    function login(shopId) {
        var supplierObj = {
            username: $("#username").val(),
            password: $("#password").val(),
            validateCode: $("#validateCode").val(),
            shopId: shopId
        };
        var loading = taoqi.loading("登录中...");
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: '/legend/index/check',
            data: supplierObj,
            success: function (data) {
                taoqi.close(loading);
                taoqi.close(chooseShopDgId);
                if (data.success != true) {
                    var code = data.code;
                    if (code == '20000') {
                        chooseShopDgId = $.layer({
                            type: 1,
                            title: false,
                            area: ['auto', 'auto'],
                            border: [0],
                            shade: [0.5, '#000'],
                            bgcolor: '#fff',
                            closeBtn: [1, true],
                            shift: 'top',
                            page: {
                                html: template.render("moreShopTpl", {"templateData": data.data})
                            }
                        });
                    } else {
                        taoqi.error(data.errorMsg);
                    }
                } else {
                    $("#loginForm").submit();
                }
            }
        });
    }


    $('.but_log').on("click", function () {
        if ($(this).hasClass('locks')) {
            return;
        }
        $(this).addClass('locks');
        var t = 60;
        var _this = this;
        var mobile = $("#mobileReg").val();
        $(this).val('重获验证码({0})'.format(t));
        var tid = setInterval(function () {
            if (--t) {
                $(_this).val('重获验证码({0})'.format(t));
            } else {
                clearInterval(tid);
                $(_this).val('点击获取验证码');
                $(_this).removeClass('locks');
            }
        }, 1000);
        if (mobile == '') {
            taoqi.failure('淘汽门店账号不可为空');
            return;
        }
        var supplierObj = {
            mobile: mobile
        };

        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: '/legend/index/flashCode',
            data: supplierObj,
            success: function (data) {
                if (data.success != true) {
                    taoqi.failure(data.errorMsg);
                } else {
                    $('.userId').val(data.data);
                    taoqi.success("亲，验证码获取成功，请接收验证码短信！短信可能被当做垃圾短信拦截，如果未收到短信，请到垃圾短信收件箱中查找！");
                }
            }
        });
    });

    $('.log_t li').click(function () {
        $('.log_t li').removeClass('current');
        $(this).addClass('current');
        $('.log_t').siblings().hide();
        $('.log_t').siblings().eq($(this).index()).show();
    });

    //input聚焦事件
    $(".box_mid input").focus(function () {
        $(this).parents("li").addClass("current").siblings().removeClass("current");
    });
    $(".box_mid input").blur(function () {
        $(this).parents("li").removeClass("current")
    });

    $("body")
        .on("focus", ".identifyTxt", function () {
            $(this).parent(".identifyContent").addClass('current');
        })
        .on("blur", ".identifyTxt", function () {
            $(this).parent(".identifyContent").removeClass('current');
        });
});
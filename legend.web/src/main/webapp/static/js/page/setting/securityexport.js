/**
 *
 * Created by zmx on 16/12/8.
 */

$(function () {
    var doc = $(document);

    seajs.use([
        'dialog',
        'table'
    ], function (dg, tb) {
        var timer = null;

        //tab切换
        doc.on('click', '.js-tab', function () {
            var tab = $(this).data('tab');
            $(this).addClass('current-tab').siblings().removeClass('current-tab');
            if (tab == 1) {
                //加密后
                var html = $('#passwordBeforeTpl').html();
                $('#tabCon').html(html);
            } else if (tab == 2) {
                //表格
                tb.init({
                    //表格数据url，必需
                    url: BASE_PATH + '/shop/exporttrack/page',
                    //表格数据目标填充id，必需
                    fillid: 'tabCon',
                    //分页容器id，必需
                    pageid: 'pagingTest',
                    //表格模板id，必需
                    tplid: 'exportListTpl'
                });
            }
        });
        $('.js-tab').eq(0).trigger('click');

        //保存按钮
        doc.on('click', '.js-save', function () {
            save();
        });

        //验证码
        doc.on('click', '.js-get-code', function () {
            sendSMS($(this));
        });

        // 保存密码设置
        function save() {
            // TODO 2016-09-13 前端密码进行MD5加密传输
            // 密码
            var password = $('input[name="password"]').val();
            if (typeof(password) === 'undefined' || password === "") {
                $("#passwordDiv").show();
                $("#passwordTip").html('请输入 "信息导出密码" ');
                return false;
            }
            // 校验密码格式
            var passwordRule = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$/;
            if (!passwordRule.test(password)) {
                $("#passwordDiv").show();
                $("#passwordTip").html('密码格式不正确');
                return false;
            }

            // 短信验证码
            var SMSCode = $('input[name="SMSCode"]').val();
            if (typeof(SMSCode) === 'undefined' || SMSCode === "") {
                dg.msg('请输入 "验证码" ', 0);
                return false;
            }

            $.ajax({
                type: 'POST',
                url: BASE_PATH + '/shop/conf/exportpassword/save/ng',
                data: {
                    password: password.toLowerCase(),
                    SMSCode: SMSCode
                },
                loadShow: false,
                cache: false,
                success: function (result) {
                    if (!result.success) {
                        var code = result["code"];
                        var errorMsg = result["errorMsg"];
                        if (code == 'login') {
                            $("#passwordDiv").show();
                            $("#passwordTip").html("密码不能与系统登录密码相同");
                        } else {
                            dg.fail(result["errorMsg"], null);
                        }

                        return;
                    }

                    $("#passwordDiv").hide();
                    dg.success("密码保存成功",function () {
                        window.document.location.href = BASE_PATH + '/shop/conf/securityexport/todisable';
                    });
                },
                error: function (e) {
                    console.error(e);
                }
            })
        };

        // 发送验证码
        function sendSMS(obj) {

            // 密码
            var password = $('input[name="password"]').val();
            if (typeof(password) === 'undefined' || password === "") {
                $("#passwordDiv").show();
                $("#passwordTip").html('请输入 "信息导出密码" ');
                return false;
            }
            // 校验密码格式
            var passwordRule = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$/;
            if (!passwordRule.test(password)) {
                $("#passwordDiv").show();
                $("#passwordTip").html('密码格式不正确');
                return false;
            }

            var sendMobile = $('input[name="mobile"]').val();

            $.when(
                $.ajax({
                    url: BASE_PATH + "/shop/conf/exportpassword/checkpassword",
                    data: {
                        password: password.toLowerCase(),
                        SMSCode: ''
                    },
                    type: 'POST',
                })
            ).done(function (resp) {
                if (!resp["success"]) {
                    $("#passwordDiv").show();
                    $("#passwordTip").html(resp["errorMsg"]);
                    return false;
                }

                $.ajax({
                    type: 'GET',
                    url: BASE_PATH + '/shop/conf/exportpassword_sms_verify',
                    data: {
                        mobile: sendMobile
                    },
                    success: function (result2) {
                        if (result2.success != true) {
                            dg.fail(result2["errorMsg"], null);
                            return false;
                        } else {
                            var mobileTemp = sendMobile.substring(0, 3) + "****" + sendMobile.substring(8, 11);
                            dg.success("验证码已经成功发送至" + mobileTemp, function () {
                                countDown(obj);
                            }, true);
                        }
                    },
                    error: function (e) {
                        console.log(e);
                    }
                });
            });
        };

        // 倒计时
        function countDown(thisCode) {
            var num = 60;
            timer = setInterval(function () {
                num--;
                if (num <= 0) {
                    clearInterval(timer);
                    thisCode.text('获取验证码').prop('disabled', false);
                } else {
                    thisCode.text('重获验证码' + num + 's').prop('disabled', true);
                }
            }, 1000);
        }
    })
});
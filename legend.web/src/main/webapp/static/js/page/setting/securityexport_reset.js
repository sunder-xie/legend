/**
 *
 * Created by zmx on 16/12/8.
 */

$(function () {
    var doc = $(document);

    var disableDialog;

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
                var html = $('#passwordAfterTpl').html();
                $('#tabCon').html(html);
            } else if (tab == 2) {
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

        //停用弹窗
        doc.on('click', '.js-stop', function () {
            var html = $('#stopDialogTpl').html();
            disableDialog = dg.open({
                area: ['376px', '220px'],
                content: html
            })
        });

        // 停用按钮
        doc.on("click", ".js-dialog-stop", function () {

            var smscode = $('input[name="smscode"]').val();
            if (typeof(smscode) === 'undefined' || smscode === "") {
                dg.msg('请输入 "短信验证码" ', 0);
                return false;
            }

            $.ajax({
                type: 'GET',
                url: BASE_PATH + '/shop/conf/exportpassword/disable/ng',
                data: {
                    'smscode': smscode
                },
                loadShow: false,
                cache: false,
                success: function (result) {
                    if (!result.success) {
                        dg.fail(result["errorMsg"], null);
                    } else {
                        dg.success("操作成功", function () {
                            window.document.location = BASE_PATH + "/shop/conf/securityexport/toset";
                        });
                    }
                },
                error: function (e) {
                    console.error(e);
                }
            })
        }).on("click", ".js-dialog-cancel", function () {
            disableDialog && dg.close(disableDialog);
        });

        //验证码（弹窗）
        doc.on('input', '.js-dialog-code', function () {
            if ($(this).val() != '') {
                $('.js-dialog-stop').prop('disabled', false);
            }
        });

        //验证码（弹窗）
        doc.on('click', '.js-dialog-get-code', function () {
            sendSMS($(this));
        });

        //重置密码
        doc.on('click', '.js-resetting', function () {
            reset();
        });

        // 密码重置
        function reset() {
            window.document.location = BASE_PATH + "/shop/conf/securityexport/toreset";
        };

        // 发送验证码
        function sendSMS(obj) {
            $.ajax({
                type: 'GET',
                url: BASE_PATH + '/shop/conf/exportpassword_sms_verify',
                data: {},
                success: function (result2) {
                    if (result2.success != true) {
                        dg.fail(result2["errorMsg"], null);
                        return false;
                    } else {
                        var sendMobile = $('input[name="mobile"]').val();
                        var mobileTemp = sendMobile.substring(0, 3) + "****" + sendMobile.substring(8, 11);
                        dg.success("验证码已经成功发送至" + mobileTemp, function () {
                            countDown(obj);
                        });
                    }
                },
                error: function (e) {
                    console.log(e);
                }
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
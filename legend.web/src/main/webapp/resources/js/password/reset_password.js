/**
 * Created by litan on 14-11-5.
 */
$(function(){
    $('.but_log').click(function () {
        if ($(this).hasClass('locks')) {
            return;
        }
        $(this).addClass('locks');
        var t = 60;
        var _this = this;
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
        if($("#mobileReg").val() == '')
        {
            taoqi.failure("淘汽门店账号不可为空")
            return;
        }
        var supplierObj = {
            mobileReg   : $("#mobileReg").val()
        };

        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: '/legend/index/flashCode',
            data: supplierObj,
            success: function (data) {
                if (data.success != true) {
                    taoqi.failure(data.errorMsg);
                    return;
                }
                else{
                    taoqi.success("亲，验证码获取成功，请接收验证码短信！短信可能被当做垃圾短信拦截，如果未收到短信，请到垃圾短信收件箱中查找！");
                    return;
                }
            },
            error: function (a, b, c) {
                //console.log(a,b,c);
            }
        });
    });

    $('.log_t li').click(function () {
        $('.log_t li').removeClass('current');
        $(this).addClass('current');
        $('.log_t').siblings().hide();
        $('.log_t').siblings().eq($(this).index()).show();
    });
});

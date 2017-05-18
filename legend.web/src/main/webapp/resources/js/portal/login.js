/**
 * Send verfiry code
 *
 * @param {type} ctrl
 * @returns {Boolean}
 */
if(typeof time != 'undefined') {
    clearInterval(time);
}
var LoginTime = 60;
var LoginTimer = function() {
    if (LoginTime < 0) {
        clearInterval(time);
        toogleSendSmsBtn($("#verify"), false);
        LoginTime = 60;
        return;
    }
    $("#verify").val(LoginTime + "秒后重获取");
    LoginTime--;
};


function toogleSendSmsBtn(ctrl, isDisabled) {
    if(isDisabled == true) {
        $(ctrl).removeAttr('onclick');
        $(ctrl).val('验证码获取中');

    } else {
        $(ctrl).attr('onclick', 'getVerfiyCode(this)');
        $(ctrl).val('获取验证码');
    }
};

function getVerfiyCode(ctrl) {

    var mobile = $.trim($("#pop_login_mobile").val());
    if (mobile.length !== 11) {
        taoqi.failure("请输入正确的手机号");
        return false;
    }
    toogleSendSmsBtn(ctrl, true);
//        $(ctrl).val("coding...");
    $.ajax({
        url: BASE_PATH+"/portal/send_verify_code",
        data: {mobile: mobile},
        async: true,
        dataType: "JSON",
        success: function(result) {
            if (result.success) {
                time = setInterval(LoginTimer, 1000);
                taoqi.success("短信发送成功");
            } else {
                taoqi.failure(result.errorMsg);
                toogleSendSmsBtn(ctrl, false);
            }
//                $(ctrl).val("code");
            return false;
        },
        error: function() {
            taoqi.failure('error');
            toogleSendSmsBtn(ctrl, false);
//                $(ctrl).val("code");
            return false;
        }
    });
}

/**
 * Do login
 *
 * @param {type} ctrl
 * @returns {Boolean}
 */
function doLogin(ctrl) {
    var mobile = $.trim($("#pop_login_mobile").val());
    var code = $.trim($("#pop_login_code").val());
    if (mobile.length !== 11) {
        taoqi.failure("请输入正确的手机号");
        return false;
    }
    if (code.length <1 ) {
        taoqi.failure("请输入正确的验证码");
        return false;
    }
    $(ctrl).prop("disabled", true);
    $(ctrl).val("Login...");
    $.ajax({
        url: BASE_PATH+"/portal/login_validate_code",
        data: {mobile:mobile,code:code},
        async: false,
        dataType: "JSON",
        beforeSend: function() {

        },
        complete: function() {

        },
        success: function(result) {
            if (result.success) {
                taoqi.success("登录成功");
               window.location.href= BASE_PATH+"/portal/invest/audit_info";
            }
            else {
              taoqi.failure(result.errorMsg);
            }
            $(ctrl).prop("disabled", false);
            $(ctrl).val("Login");
            return false;
        },
        error: function() {
            taoqi.failure('error');
            $(ctrl).val("Login");
            $(ctrl).prop("disabled", false);
            return false;
        }
    });
}

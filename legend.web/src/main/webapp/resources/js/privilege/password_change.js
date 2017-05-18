/**
 * Created by QXD on 2014/11/11.
 */
$(function () {
    //保存按钮
    $("#update").click(function () {
        var oldPassWord = $.trim($("#oldPassWord").val());
        var newPassWord = $.trim($("#newPassWord").val());
        var newPassWordR = $.trim($("#newPassWordR").val());

        if (oldPassWord == null || oldPassWord == "") {
            taoqi.failure("信息不完整 *为必填项");
            return;
        }
        if (newPassWord == null || newPassWord == "") {
            taoqi.failure("信息不完整 *为必填项");
            return;
        }
        if (newPassWordR == null || newPassWordR == "") {
            taoqi.failure("信息不完整 *为必填项");
            return;
        }
        if (newPassWord != newPassWordR) {
            taoqi.failure("新密码不一致");
            return;
        }
        var supplierObj = {
            passwordReg: oldPassWord,
            newPasswordReg: newPassWordR
        };

        var save = taoqi.loading("保存信息中");
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: BASE_PATH+'/shop/member/change_submit',
            data: supplierObj,
            success: function (data) {
                layer.close(save);
                if (data.success != true) {
                    taoqi.error(data.errorMsg);
                    return;
                } else {
                    taoqi.success("操作成功");
                }
            },
            error: function (a, b, c) {
//                    console.log(a,b,c);
            }
        });
    });
});


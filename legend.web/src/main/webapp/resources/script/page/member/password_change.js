seajs.use("dialog",function(dialog) {
    $(document).on("click", "#passSubmit", function () {
        //var pass = $("#passwordReg").val();
        //var pass1 = $("#newPasswordReg").val();
        //var pass2 = $("#newPassWordR").val();
        //if (pass1 != pass2) {
        //    taoqi.failure("新密码不一致");
        //    return;
        //}
        //$.ajax({
        //    url: BASE_PATH + "/shop/member/change_submit",
        //    type:'POST',
        //    data: {
        //        passwordReg: pass,
        //        newPasswordReg: pass1
        //    },
        //    success: function (data) {
        //        //layer.close(save);
        //        if (data.success != true) {
        //            taoqi.error(data.errorMsg);
        //            return;
        //        } else {
        //            taoqi.success("操作成功");
        //        }
        //    }
        //});
        var oldPassWord = $.trim($("#passwordReg").val());
        var newPassWord = $.trim($("#newPasswordReg").val());
        var newPassWordR = $.trim($("#newPassWordR").val());

        if (oldPassWord == null || oldPassWord == "") {
            dialog.info("*为必填项",5);
            return;
        }
        if (newPassWord == null || newPassWord == "") {
            dialog.info("*为必填项",5);
            return;
        }
        if (newPassWordR == null || newPassWordR == "") {
            dialog.info("*为必填项",5);
            return;
        }
        if (newPassWord != newPassWordR) {
            dialog.info("新密码不一致",5);
            return;
        }
        var supplierObj = {
            passwordReg: oldPassWord,
            newPasswordReg: newPassWordR
        };

        var save = dialog.load("正在保存中，请稍后...",60*10);
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: BASE_PATH + '/shop/member/change_submit',
            data: supplierObj,
            success: function (data) {
                layer.close(save);
                if (data.success != true) {
                    dialog.info(data.errorMsg,5);
                    return;
                } else {
                    dialog.info("操作成功",1);
                }
            },
            error: function (a, b, c) {
//                    console.log(a,b,c);
            }
        });
    });
});
// 信息安全导出模块
var exportSecurity = exportSecurity || {};

(function ($, S) {
    // 记录导出
    function doRecord(brief) {
        $.ajax({
            url: BASE_PATH + "/shop/exporttrack/add",
            type: 'POST',
            data: {
                'brief': brief
            },
            async: 'true'
        })
    }

    // 弹出提示框
    exportSecurity.tip = function (option) {

        var defaultOpt = {
            dom: '',
            callback: null
        };
        var doc = $(document);
        var args = $.extend({}, defaultOpt, option);
        var dialogIndex;
        var title = args["title"] || "导出信息";

        S.use([
            'dialog',
            'ajax',
            'artTemplate'
        ], function (dg, ax, at) {
            // 仅显示一次
            var excel_password_tip = localStorage.getItem("excel_password_tip");
            if (excel_password_tip) {
                return;
            }
            localStorage.setItem("excel_password_tip",true);

            // 密码提示设置 模板
            var set_template = at('exportpwd_set_template', {'title': title});
            $.when(
                $.ajax({
                    url: BASE_PATH + "/shop/conf/exportpassword/precheck",
                    data: ""
                })
            ).done(function (resp) {
                var success = resp["success"];
                if (!success) {
                    return;
                }
                var data = resp["data"];
                var isAdmin = data["isAdmin"];
                var isSetedExportPassword = data["isSetedExportPassword"];

                // 未设置密码:弹出密码设置提示框
                if (isAdmin === 'true' && isSetedExportPassword === "false") {
                    dialogIndex = dg.dialog({
                        html: set_template
                    });
                }
            });

            // 设置密码
            doc.on("click", ".set_submit", function () {
                window.document.location.href = BASE_PATH + "/shop/conf/securityexport/toset";
            }).on("click", ".set_cancel", function () {
                dialogIndex && dg.close(dialogIndex);
            });
        });
    };

    // 弹出提示框
    exportSecurity.confirm = function (option) {
        var defaultOpt = {
            dom: '',
            callback: null
        };
        var doc = $(document);
        var args = $.extend({}, defaultOpt, option);
        var dialogIndex;
        var title = args["title"] || "";

        S.use([
            'dialog',
            'ajax',
            'artTemplate',
            'formData',
            'check'
        ], function (dg, ax, at, fd, ck) {

            // 弹出密码输入框
            doc.on("click", args.dom, function () {

                // 密码输入框 模板
                var confirm_template = at('exportpwd_confirm_template', {});
                $.when(
                    $.ajax({
                        url: BASE_PATH + "/shop/conf/exportpassword/precheck",
                        data: ""
                    })
                ).done(function (resp) {
                    var success = resp["success"];
                    if (!success) {
                        return;
                    }
                    var data = resp["data"];
                    var isAdmin = data["isAdmin"];
                    var isSetedExportPassword = data["isSetedExportPassword"];

                    // 未设置密码:直接导出
                    if (isSetedExportPassword === "false") {
                        args["callback"] && args["callback"]();
                        doRecord(title);
                    } else {
                        // 设置过密码,弹出密码输入框
                        dialogIndex = dg.dialog({
                            html: confirm_template
                        });
                        // true:管理员 显示重置按钮
                        if (isAdmin === "true") {
                            $("#resetExportPWD").show();
                        } else {
                            $("#resetExportPWD").hide();
                        }
                    }
                });
            });

            // 提交审核
            doc.on("click", ".confirm_submit", function () {
                var scope = ".pwdconfirm_part";
                var passwordInput = $('.pwdconfirm_part').find('input[name="password"]');
                passwordInput.attr('data-v-type', 'required');
                if (!ck.check(scope)) {
                    passwordInput.removeAttr('data-v-type');
                    return;
                }
                var password = passwordInput.val();
                $.ajax({
                    type: 'POST',
                    url: BASE_PATH + "/shop/conf/exportpassword/confirm",
                    data: {
                        password: password
                    }
                }).done(function (json) {
                    if (json.success) {
                        dialogIndex && dg.close(dialogIndex);
                        dg.info("密码验证通过,正在导出Excel...", 1, "", function () {
                            args["callback"] && args["callback"]();
                            doRecord(title);
                        }, true);
                    } else {
                        $("#tipmsg").html("密码不正确,请重新输入");
                    }
                });
            }).on("click", ".confirm_cancel", function () {
                dialogIndex && dg.close(dialogIndex);
            })

        });
    }
}(jQuery, seajs));
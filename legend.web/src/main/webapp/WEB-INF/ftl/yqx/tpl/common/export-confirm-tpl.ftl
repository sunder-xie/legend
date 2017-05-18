<#--
    信息安全导出模板
 -->
<style>
    .pwdconfirm_part {
        width: 400px;
        background: #fff;
        overflow: hidden;
    }

    .dialog_height_150 {
        height: 150px;
    }

    .dialog_height_200 {
        height: 200px;
    }

    .pwdconfirm_part .dialog-title {
        background: #232e49;
        color: #fff;
        text-align: center;
        font-size: 16px;
        font-weight: 900;
        line-height: 54px;
    }

    .pwdconfirm_part .pwd {
        padding-top: 20px;
        text-align: center;
        display: inline-block;
        padding-left: 80px;
        width: 221px;
    }

    .pwdconfirm_part input.yqx-input {
        width: 233px;
    }

    .pwdconfirm_part .buttons {
        display: inline-block;
        padding-top: 12px;
        padding-left: 80px;
    }

    .pwdconfirm_part .bottom {
        padding-top: 20px;
    }

    .pwdconfirm_part .bottom span.tip {
        padding-left: 80px;
        color: red
    }

    .pwdconfirm_part .bottom span.reset {
        float: right;
        padding-right: 12px;
        text-decoration: underline
    }
</style>

<!-- 密码输入框 模板-->
<script type="text/html" id="exportpwd_confirm_template">
    <div class="pwdconfirm_part dialog_height_200" data-tpl-ref="new-part-tpl">
        <p class="dialog-title">
            输入密码
        </p>
        <div class="pwd">
            <input type="password" name="password" class="yqx-input" placeholder="请输入信息导出密码" autocomplete="new-password">
        </div>

        <div class="buttons">
            <span style="padding-right: 70px;">
                <a href="javascript:void(0);" class="yqx-btn yqx-btn-1 confirm_cancel">取消</a>
            </span>
            <span>
                <a href="javascript:void(0);"
                   <%if(blank == true) {%>target="_blank" <%}%>
                   class="yqx-btn yqx-btn-2 qxy_green_btn confirm_submit">确定</a>
            </span>
        </div>

        <div class="bottom">
            <span id="tipmsg" class="tip"></span>
            <span id="resetExportPWD" class="reset">
                <a href="${BASE_PATH}/shop/conf/securityexport/toreset">重置密码</a>
            </span>
        </div>
    </div>
</script>

<!-- 密码提示设置模板-->
<script type="text/html" id="exportpwd_set_template">
    <div class="pwdconfirm_part dialog_height_150" data-tpl-ref="new-part-tpl">
        <p class="dialog-title">
            <%=title%>，加密更安全
        </p>

        <div class="bottom">
            <span style="padding-left: 100px;"><a href="javascript:void(0);"
                                                  class="yqx-btn yqx-btn-2 qxy_green_btn set_submit">设置密码</a></span>
            <span><a href="javascript:void(0);" class="yqx-btn yqx-btn-1 set_cancel">取消</a></span>
        </div>
    </div>
</script>

<script>
    // 信息安全导出模块
    var exportSecurity = exportSecurity || {};
    var doc = $(document);

    (function ($, S) {
        // 导出简要描述
        exportSecurity.exportBrief = exportSecurity.exportBrief || "";

        // 导出按钮的元素
        var _exportEle;
        var _current = {
            args: null,
            type: 'export_excel'
        };
        var dialogIndex;

        // 提示设置框
        exportSecurity.tip = function (option) {
            var args = option || {};
            var title = args["title"] || "导出信息";

            S.use([
                'dialog',
                'ajax',
                'art'
            ], function (dg, ax, at) {
                // 仅显示一次
                var excel_password_tip = localStorage.getItem("excel_password_tip");
                if (excel_password_tip) {
                    return;
                }
                localStorage.setItem("excel_password_tip", true);

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
                        dialogIndex = dg.open({
                            area: ['400px', '150px'],
                            content: set_template
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

        // 输入确认框
        exportSecurity.confirm = function (option) {
            var args = option;
            exportSecurity.exportBrief = args["title"] || "";

            if (args.async !== false) {
                args.async = true;
            }

            S.use([
                'dialog',
                'ajax',
                'art'
            ], function (dg, ax, at) {
                // 弹出密码输入框
                doc.on("click", args.dom, function () {
                    _current.args = args;
                    _current.type = 'export_excel';

                    _exportEle = this;

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
                            _current.args["callback"] && _current.args["callback"].call(_exportEle);
                            // 记录导出记录
                            doRecord(exportSecurity.exportBrief);
                        } else {
                            // 设置过密码,弹出密码输入框
                            dialogIndex = dg.open({
                                area: ['400px', '200px'],
                                content: confirm_template
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
            });
        }

        // 按钮绑定 先提示->后输入
        exportSecurity.tipAndConfirm = function (option) {
            var args = option;
            exportSecurity.exportBrief = args["title"] || "";

            if (args.async !== false) {
                args.async = true;
            }

            S.use([
                'dialog',
                'ajax',
                'art'
            ], function (dg, ax, at) {
                doc.on("click", args.dom, function () {
                    var blank = $(this).data('blank');
                    var that = this;
                    _current.args = args;
                    _current.type = 'other';
                    _exportEle = this;

                    // 密码设置框
                    var set_template = at('exportpwd_set_template', {'title': exportSecurity.exportBrief});
                    // 密码输入框
                    var confirm_template = at('exportpwd_confirm_template', {blank: blank});

                    $.when(
                            $.ajax({
                                url: BASE_PATH + "/shop/conf/exportpassword/precheck",
                                data: "",
                                async: args.async
                            })
                    ).done(function (resp) {
                        var success = resp["success"];
                        var ret;
                        if (!success) {
                            return;
                        }
                        var data = resp["data"];
                        var isAdmin = data["isAdmin"];
                        var isSetedExportPassword = data["isSetedExportPassword"];

                        _success = success;
                        // 未设置密码
                        if (isSetedExportPassword === "false") {
                            // 管理员:提示密码设置,仅提示一次
                            if (isAdmin === 'true') {
                                var excel_password_tip = localStorage.getItem("excel_password_tip");
                                // 已提示过: 直接下载
                                if (excel_password_tip) {
                                    ret = _current.args["callback"] && _current.args["callback"].call(_exportEle);
                                    doRecord(exportSecurity.exportBrief);
                                } else {
                                    // 未提示: 显示提示(仅显示一次)
                                    dialogIndex = dg.open({
                                        area: ['400px', '150px'],
                                        content: set_template
                                    });
                                    localStorage.setItem("excel_password_tip", true);
                                }
                            } else {
                                // 非管理员: 直接下载
                                ret = _current.args["callback"] && _current.args["callback"].call(_exportEle);
                                doRecord(exportSecurity.exportBrief);
                            }

                            if (_current.args.callbackType == 'url') {
                                $(that).attr('href', ret);
                            }
                        } else {
                            // 已设置密码: 弹出密码输入框
                            dialogIndex = dg.open({
                                area: ['400px', '200px'],
                                content: confirm_template
                            });
                            // 管理员:显示重置按钮
                            if (isAdmin === "true") {
                                $("#resetExportPWD").show();
                            } else {
                                $("#resetExportPWD").hide();
                            }
                        }
                    });

                });

            });
        }

        S.use([
            'dialog'
        ], function (dg) {
            // 设置密码
            doc.on("click", ".set_submit", function () {
                window.document.location.href = BASE_PATH + "/shop/conf/securityexport/toset";
            }).on("click", ".set_cancel", function () {
                dialogIndex && dg.close(dialogIndex);
            });

            // 提交审核
            doc.on("click", ".confirm_submit", function () {
                var passwordInput = $('.pwdconfirm_part').find('input[name="password"]');
                var password = passwordInput.val();
                var that = this, ret;
                if (typeof(password) === 'undefined' || password === "") {
                    $("#tipmsg").html("请输入密码");
                    return;
                }
                $.ajax({
                    type: 'POST',
                    url: BASE_PATH + "/shop/conf/exportpassword/confirm",
                    async: _current.args.async,
                    data: {
                        password: password.toLowerCase()
                    }
                }).done(function (json) {
                    if (json.success) {
                        if (_current.type == 'export_excel') {
                            dg.success("密码验证通过,正在导出Excel...");
                            _current.args["callback"] && _current.args["callback"].call(_exportEle);
                        } else {
                            ret = _current.args["callback"] && _current.args["callback"].call(_exportEle);
                        }

                        if (_current.args.callbackType == 'url') {
                            $(that).attr('href', ret);
                        }

                        setTimeout(function () {
                            dg.close(dialogIndex);
                        }, 400);

                        // 记录导出记录
                        doRecord(exportSecurity.exportBrief);
                    } else {
                        $("#tipmsg").html("密码不正确,请重新输入");
                    }

                });
            }).on("click", ".confirm_cancel", function () {
                dialogIndex && dg.close(dialogIndex);
            });
        });

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
    }(jQuery, seajs));
</script>
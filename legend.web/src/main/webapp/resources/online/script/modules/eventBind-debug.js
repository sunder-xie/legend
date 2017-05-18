define("legend/resources/online/script/eventBind-debug", [ "./check-debug", "./dialog-debug", "../libs/layer/layer-debug", "./ajax-debug", "./cascadeSelect-debug", "../libs/artTemplate/artTemplate-debug", "./chosenSelect-debug", "../libs/chosen/chosen-debug", "./../libs/My97DatePicker/WdatePicker-debug", "./formData-debug" ], function(require, exports, module) {
    var check = require("./check-debug");
    var cascadeSelect = require("./cascadeSelect-debug");
    var chosen = require("./chosenSelect-debug");
    $(function() {
        //绑定动态生成表单项，表单行事件
        $("body").on("click", ".qxy_add_icon", function() {
            var dynamic = $(this).parents(".form_item");
            var formRow = $(this).parents(".form_row");
            if (dynamic[0]) {
                var html = dynamic[0].outerHTML;
                $(this).removeClass("qxy_add_icon").addClass("qxy_del_icon");
                dynamic.after(html);
                /*--------隐藏域无法情况,手工清空--------------*/
                dynamic.next(".form_item").find("input[type='hidden'][clearable]").val("");
                /*---------级联下拉框需要重新渲染-------start-------*/
                dynamic.next(".form_item").find(".cascadeSelect").each(function(i, e) {
                    cascadeSelect.init(e);
                });
                dynamic.next(".form_item").find(".chosen").each(function(i, e) {
                    chosen.handleChoosenSelect(e);
                });
                /*---------级联下拉框需要重新渲染--------end------*/
                if (dynamic.width() < formRow.width()) {
                    //动态生成行认后在该form_row中的索引
                    var index = $(".form_item", formRow).index(dynamic.next()) + 1;
                    //一行能放几个动态元素,结果向下取整
                    var dynamicNum = Math.floor(formRow.width() / dynamic.width());
                    //css margin-right
                    var marginRight = $(".form_item:eq(0)", formRow).css("margin-right");
                    //console.log(index +" % "+dynamicNum + "=" +index % dynamicNum);
                    if (index % dynamicNum == 0) {
                        dynamic.next().css("margin-right", 0);
                    } else {
                        dynamic.next().css("margin-right", marginRight);
                    }
                }
                //移除多余元素
                dynamic.next().find(".part-info").remove();
                dynamic.next().find(".qxy_err_msg").hide();
            }
        });
        //表单行事件删除
        $("body").on("click", ".qxy_del_icon", function() {
            var formItem = $(this).parents(".form_item"), formRow = $(this).parents(".form_row"), delFn = formItem.attr("dynamic_fn") || formItem.attr("del_fn"), delBeforeFn = formItem.attr("del_before_fn"), next = formItem.next(), flag = true;
            if (delBeforeFn) {
                delBeforeFn = eval(delBeforeFn);
                flag = delBeforeFn(formItem);
            }
            if (!flag) {
                return;
            }
            formItem.remove();
            if (delFn) {
                //删除formitem后的回调方法
                delFn = eval(delFn);
                delFn(next);
            }
            if (formItem.width() < formRow.width()) {
                //删除后重新排列formItem
                $(".form_item", formRow).each(function() {
                    var index = $(this).index() + 1;
                    //一行能放几个动态元素,结果向下取整
                    var formItemNum = Math.floor(formRow.width() / formItem.width());
                    //css margin-right
                    var marginRight = $(".form_item:eq(0)", formRow).css("margin-right");
                    if (index % formItemNum == 0) {
                        $(this).css("margin-right", 0);
                    } else {
                        $(this).css("margin-right", marginRight);
                    }
                });
            }
        });
        //在动态行中点击Enter热键自动增加行
        $("body").on("keypress", "div[dynamic_id] input:not([service_url])", function(event) {
            if (event.keyCode == "13") {
                $(event.target).parents(".form_row").find(".qxy_add_icon").trigger("click");
            }
        });
        //绑定表单保存事件
        $("body").on("click", ".qxy_btn[ref_form_op]", function() {
            var $this = $(this);
            var action = $this.attr("ref_form_op");
            if (action && actionList[action]) {
                if (typeof actionList[action] == "function") {
                    actionList[action]($this);
                }
            }
        });
        //日历事件初始化绑定
        $("body").on("focus", ".qxy_picker", function() {
            var wp = require("./../libs/My97DatePicker/WdatePicker-debug");
            var dateFmt = $(this).attr("format") || "yyyy-MM-dd";
            var minDate = $(this).attr("min") || "";
            var maxDate = $(this).attr("max") || "";
            var doubleCalendar = $(this).attr("pairs") || false;
            var args = {
                dateFmt: dateFmt,
                minDate: minDate,
                maxDate: maxDate,
                doubleCalendar: doubleCalendar
            };
            wp.wp(args);
        });
        //group内容显示隐藏事件绑定
        $("body").on("click", ".qxy_group_slider >.group_head", function() {
            var icon = $("i", $(this));
            var iconClass = icon.attr("class");
            var next = $(this).next(".group_content");
            if (!iconClass) {
                return;
            }
            if (iconClass == "arrow_down") {
                icon.attr("class", "arrow_up");
                next.show();
                formLayoutCalc(next);
            } else {
                icon.attr("class", "arrow_down");
                next.hide();
                //收起group时，关闭该group里所有的tips
                $(".xubox_layer[type='tips']", next).remove();
            }
        });
        //group显示更多
        $("body").on("click", ".downward", function() {
            var $this = $(this);
            var parent = $this.parent();
            $(".hide_part", parent).show();
            formLayoutCalc($(".hide_part", parent));
            chosen.handleChoosenSelect($(".chosen", parent));
            if ($(this).hasClass("search_form_slide_btn")) {
                $this.html("高级搜索<i></i>");
            } else {
                $this.html("隐藏更多<i></i>");
            }
            $this.addClass("upward").removeClass("downward");
        });
        //group隐藏更多
        $("body").on("click", ".upward", function() {
            var $this = $(this);
            var parent = $this.parent();
            $(".hide_part", parent).hide();
            if ($(this).hasClass("search_form_slide_btn")) {
                $this.html("高级搜索<i></i>");
            } else {
                $this.html("显示更多<i></i>");
            }
            $this.addClass("downward").removeClass("upward");
        });
        //页面更新时不做存在判断
        $("input[v_type]").each(function(i) {
            var $this = $(this);
            var vType = JSON.parse($this.attr("v_type"));
            if (vType.vurl) {
                $this.data("data", $this.val());
                $this.data("vurl", vType.vurl);
                delete vType.vurl;
                $this.attr("v_type", JSON.stringify(vType));
            } else {
                return true;
            }
            $this.on("keyup", function() {
                var val = $(this).val();
                var data = $(this).data("data");
                //console.log("框中的值："+val);
                //console.log("缓存的值："+data);
                if (data == val) {
                    var temp = JSON.parse($this.attr("v_type"));
                    $this.data("vurl", temp.vurl);
                    delete temp.vurl;
                    $this.attr("v_type", JSON.stringify(temp));
                } else {
                    var temp = JSON.parse($this.attr("v_type"));
                    var vurl = $(this).data("vurl");
                    temp = $.extend(temp, {
                        vurl: vurl
                    });
                    //console.log("跟缓存值不不不不一样，加入vurl："+JSON.stringify(temp));
                    $this.attr("v_type", JSON.stringify(temp));
                }
            });
        });
        //输入限制
        $(document).on("keyup", ".J_input_limit", function() {
            var self = $(this);
            var limit_type = self.data("limit_type");
            var value = self.val();
            var t_value = "";
            if (limit_type === "number") {
                t_value = value.replace(/[^0-9]/g, "");
                //剔除非数字字符
                //  t_value = t_value.replace(/^0+/g,'');//剔除首位为.的数字
                if (t_value !== value) {
                    self.val(t_value);
                }
            }
            if (limit_type === "price") {
                t_value = value.replace(/[^0-9.]/g, "");
                //剔除非数字字符，除去.
                t_value = t_value.replace(/^\.+/g, "");
                //剔除首位为.的数字
                if (t_value !== value) {
                    self.val(t_value);
                }
            }
            if (limit_type === "digits_letters") {
                t_value = value.replace(/[^0-9a-z]/gi, "");
                //剔除非数字和英文字符
                if (t_value !== value) {
                    self.val(t_value);
                }
            }
            if (limit_type === "phone") {
                t_value = value.replace(/[^0-9-]/gi, "");
                if (t_value !== value) {
                    self.val(t_value);
                }
            }
            if (limit_type === "account") {
                t_value = value.replace(/[^0-9\-]/gi, "");
                if (t_value !== value) {
                    self.val(t_value);
                }
            }
        });
        //表单验证事件初始化绑定
        check.init();
        check.bind();
    });
    //按钮事件列表
    var actionList = {
        //保存提交事件
        submit: function(obj) {
            var ajax = require("./ajax-debug");
            var formData = require("./formData-debug");
            var dialog = require("./dialog-debug");
            var formid = obj.attr("ref_form_id");
            var form = $("#" + formid);
            var datas = formData.get(formid);
            //表单全局验证
            var result = check.check("#" + formid);
            if (!result) {
                return;
            }
            var type = form.attr("method") || "post";
            var args = {
                url: form.attr("action"),
                data: datas,
                loadText: "正在保存信息中...",
                success: function(json) {
                    if (json.success) {
                        dialog.info("操作成功", 1);
                        window.location.href = document.referrer;
                    } else {
                        dialog.info(json.errorMsg, 3);
                    }
                }
            };
            type == "post" ? ajax.post(args) : ajax.get(args);
        },
        back: function() {
            window.location.href = document.referrer;
        }
    };
});
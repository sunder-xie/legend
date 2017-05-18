//所有的表单验证
define("legend/resources/online/script/check-debug", [ "./dialog-debug", "../libs/layer/layer-debug", "./ajax-debug" ], function(require, exports, module) {
    var dialog = require("./dialog-debug");
    var ajax = require("./ajax-debug");
    //验证可以绑定的元素
    var elems = "input[v_type],select[v_type],textarea[v_type]";
    var tips = null;
    var html = '<div class="qxy_err_msg"><div class="qxy_err_msg_inner"><p>内容不能为空</p><span></span><i></i></div></div>';
    //失焦验证的方法
    var valid = function(obj, tipType) {
        var val = obj.val();
        var rule = JSON.parse(obj.attr("v_type"));
        var label = obj.attr("label") || "";
        var errTips = obj.siblings(".qxy_err_msg");
        var finalResult = true;
        //验证返回的最终结果
        if (!rule || rule == "") {
            //验证属性不符合规则跳出循环
            return true;
        }
        //替换label中间的空格和冒号为空
        label = label.replace(/\s|[:：]/gm, "");
        $.each(regList, function(k, v) {
            var validResult = undefined;
            var type = rule[k];
            if (!rule[k]) {
                //在属性中没找到regList的验证规则
                return true;
            }
            if (k == "type") {
                //验证格式
                validResult = regList[k][type](val);
            } else if (k == "vurl") {
                //验证值是否存在
                var v_name = obj.attr("v_name");
                validResult = regList[k](val, rule[k], v_name, obj, label, errTips);
            } else {
                //验证长度，最大最小值，空值
                validResult = regList[k](val, rule[k]);
            }
            if (validResult && validResult.result) {
                //通过验证
                errTips.hide();
                finalResult = true;
            } else if (validResult != undefined) {
                //聚焦到该文本框
                //obj.focus();
                if (tipType == "tips") {
                    //气泡方式提示
                    $("p", errTips).html(label + validResult.msg);
                    errTips.show();
                } else if (tipType == "alert") {
                    //弹框方式提示
                    dialog.info(label + validResult.msg, 5);
                }
                finalResult = false;
                return false;
            }
        });
        return finalResult;
    };
    var insertMsg = function() {
        //插入验证提示
        $(elems).each(function() {
            $(this).parent().css("position", "relative");
            $(this).siblings(".qxy_err_msg").remove();
            $(this).before(html);
        });
        //动态元素全部添加动态name.
        $("div[dynamic_name]").each(function() {
            var this_name = $(this).attr("dynamic_name");
            $(elems, $(this)).each(function() {
                $(this).attr("dynamic_name", this_name);
            });
        });
    };
    module.exports = {
        //初始化验证提示信息
        init: function(scope) {
            insertMsg();
        },
        //表单保存或提交时全局验证
        check: function(scope) {
            var result = true;
            $(elems, scope || "body").each(function() {
                $(this).trigger("blur");
                if ($(this).attr("dynamic_name") != undefined) {
                    //动态元素跳出验证，下面会单独验证动态行元素
                    var dynamic_item = $(this).parents("[dynamic_name]");
                    var inputs = $(elems, dynamic_item);
                    if (dynamic_item.next("[dynamic_name]").size() > 0) {
                        //不是动态行的最后一行
                        inputs.each(function() {
                            valid($(this), "tips");
                        });
                    } else {
                        //是动态行的最后一行
                        var isValid = false;
                        inputs.each(function() {
                            if ($(this).val() != "" && $(this).is(":visible")) {
                                isValid = true;
                            }
                        });
                        if (isValid) {
                            //表示该动态行是最后一行,并且里面的输入框有值
                            inputs.each(function() {
                                valid($(this), "tips");
                            });
                        } else {
                            //相邻输入框没值，隐藏提示信息，并不做验证
                            inputs.each(function() {
                                $(this).siblings(".qxy_err_msg").hide();
                            });
                        }
                    }
                } else {
                    result = valid($(this), "tips");
                }
            });
            if ($(".qxy_err_msg:visible", scope || "body").size() > 0) {
                return false;
            } else {
                return true;
            }
        },
        //绑定验证失焦获焦事件
        bind: function(scope) {
            $(scope || "body").on("blur", elems, function() {
                if ($(this).attr("dynamic_name") != undefined) {
                    //动态元素跳出验证，下面会单独验证动态行元素
                    var dynamic_item = $(this).parents("[dynamic_name]");
                    if (dynamic_item.next("[dynamic_name]").size() > 0) {
                        //不是动态行的最后一行
                        valid($(this), "tips");
                    } else {
                        //是动态行的最后一行
                        var inputs = $(elems, dynamic_item);
                        var isValid = false;
                        inputs.each(function() {
                            if ($(this).val() != "" && $(this).is(":visible")) {
                                isValid = true;
                            }
                        });
                        if (isValid) {
                            //表示该动态行是最后一行,并且里面的输入框有值
                            valid($(this), "tips");
                        } else {
                            //相邻输入框没值，隐藏提示信息，并不做验证
                            $(this).siblings(".qxy_err_msg").hide();
                        }
                    }
                } else {
                    valid($(this), "tips");
                }
            });
        }
    };
    var regList = {
        required: function(val, v) {
            //必填字段
            var msg = [ "", "不能为空" ];
            if (!v) {
                return undefined;
            }
            if (val == "") {
                return {
                    msg: msg[1],
                    result: false
                };
            } else {
                return {
                    msg: msg[0],
                    result: true
                };
            }
        },
        maxLength: function(val, v) {
            //字符串最大长度限制
            var msg = [ "", "字符串长度不能大于" + v ];
            if (val.length <= v) {
                return {
                    msg: msg[0],
                    result: true
                };
            } else {
                return {
                    msg: msg[1],
                    result: false
                };
            }
        },
        minLength: function(val, v) {
            //字符串最小长度限制
            var msg = [ "", "字符串长度不能小于" + v ];
            if (val.length >= v) {
                return {
                    msg: msg[0],
                    result: true
                };
            } else {
                return {
                    msg: msg[1],
                    result: false
                };
            }
        },
        maxValue: function(val, v) {
            //数字最大值限制
            var msg = [ "", "数字值不能大于" + v, "请输入数字" ];
            if (!$.isNumeric(val)) {
                return {
                    msg: msg[2],
                    result: false
                };
            }
            if (Number(val) <= v) {
                return {
                    msg: msg[0],
                    result: true
                };
            } else {
                return {
                    msg: msg[1],
                    result: false
                };
            }
        },
        minValue: function(val, v) {
            //数字最小值限制
            var msg = [ "", "数字值不能大于" + v, "请输入数字" ];
            if (!$.isNumeric(val)) {
                return {
                    msg: msg[2],
                    result: false
                };
            }
            if (Number(val) <= v) {
                return {
                    msg: msg[0],
                    result: true
                };
            } else {
                return {
                    msg: msg[1],
                    result: false
                };
            }
        },
        vurl: function(val, url, vname, obj, label, errTips) {
            //服务器验证，例如帐号是否存在
            var msg = [ "", "已存在" ];
            var data = {};
            data[vname] = val;
            var dfd = $.Deferred();
            var req = function(dfd) {
                ajax.post({
                    url: BASE_PATH + url,
                    loadShow: false,
                    async: false,
                    data: data,
                    success: function(json) {
                        dfd.resolve(json);
                    }
                });
                return dfd;
            };
            var relt = req(dfd).done(function(json) {
                if (json.success != true) {
                    $("p", errTips).html(json.errorMsg);
                    errTips.show();
                } else {
                    if (json.data == false) {
                        $("p", errTips).html(label + msg[1]);
                        errTips.show();
                    }
                }
            });
        },
        type: {
            //验证是否是正整数
            number: function(val) {
                var msg = [ "", "数值不能小于0", "请输入数字" ];
                if ($.trim(val) == "") {
                    return {
                        msg: msg[0],
                        result: true
                    };
                }
                if ($.isNumeric(val)) {
                    if (val < 0) {
                        return {
                            msg: msg[1],
                            result: false
                        };
                    } else {
                        return {
                            msg: msg[0],
                            result: true
                        };
                    }
                } else {
                    return {
                        msg: msg[2],
                        result: false
                    };
                }
            },
            //验证是否邮箱
            email: function(val) {
                var reg = /\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+/;
                var msg = [ "", "格式不正确" ];
                if ($.trim(val) == "") {
                    return {
                        msg: msg[0],
                        result: true
                    };
                }
                if (reg.test(val)) {
                    return {
                        msg: msg[0],
                        result: true
                    };
                } else {
                    return {
                        msg: msg[1],
                        result: false
                    };
                }
            },
            //验证是否邮编
            zip: function(val) {
                var reg = /^[1-9]\d{5}$/;
                var msg = [ "", "格式不正确" ];
                if ($.trim(val) == "") {
                    return {
                        msg: msg[0],
                        result: true
                    };
                }
                if (reg.test(val)) {
                    return {
                        msg: msg[0],
                        result: true
                    };
                } else {
                    return {
                        msg: msg[1],
                        result: false
                    };
                }
            },
            //验证是否座机
            tel: function(val) {
                var reg = /^[0-9]\d{9,11}$/;
                var msg = [ "", "格式不正确" ];
                if ($.trim(val) == "") {
                    return {
                        msg: msg[0],
                        result: true
                    };
                }
                if (reg.test(val)) {
                    return {
                        msg: msg[0],
                        result: true
                    };
                } else {
                    return {
                        msg: msg[1],
                        result: false
                    };
                }
            },
            //验证是否手机号
            phone: function(val) {
                var reg = /^1[3|4|5|8][0-9]\d{8}$/;
                var msg = [ "", "格式不正确" ];
                if ($.trim(val) == "") {
                    return {
                        msg: msg[0],
                        result: true
                    };
                }
                if (reg.test(val)) {
                    return {
                        msg: msg[0],
                        result: true
                    };
                } else {
                    return {
                        msg: msg[1],
                        result: false
                    };
                }
            },
            //验证是否QQ
            qq: function(val) {
                var reg = /^[1-9]\d{5,10}$/;
                var msg = [ "", "格式不正确" ];
                if ($.trim(val) == "") {
                    return {
                        msg: msg[0],
                        result: true
                    };
                }
                if (reg.test(val)) {
                    return {
                        msg: msg[0],
                        result: true
                    };
                } else {
                    return {
                        msg: msg[1],
                        result: false
                    };
                }
            },
            ooxx: function() {},
            ooxx: function() {}
        }
    };
});
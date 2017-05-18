/**
 * ch 2016-04-14
 * 所有的表单验证
 *
 * wry 04-22
 * add: clearMsg 显示提示信息后过几秒清除
 * sky 2016-04-25
 * 封装了insertMsg接口和bind接口
 * 对phone、integer的正则进行改动
 * 添加check全局校验提示错误时，滚动错误到位置
 */


//所有的表单验证
define(function (require, exports, module) {
    var ajax = require("./ajax");
    //验证可以绑定的元素
    var elems = "input[data-v-type],select[data-v-type],textarea[data-v-type]";
    var html = '<div class="yqx-err-msg"><div class="yqx-err-msg-inner"><p>内容不能为空</p><em></em><i></i></div></div>';
    //失焦验证的方法
    var valid = function (obj) {
        var val = obj.val(),
            // 防止自定义错误类型和基础错误类型冲突，导致fadeOut(100)
            custom = obj.prop('custom') || false,
        //验证规则字符串
            ruleStr = obj.data("vType"),
        //验证规则列表
            ruleList = null,
        //提示信息的前辍
            label = obj.data("label") || "",
        //验证提示的dom,为了兼容downlist.js
            errTips = obj.siblings('.yqx-err-msg').size() > 0 ? obj.siblings('.yqx-err-msg') : obj.parent().siblings('.yqx-err-msg'),
        //验证返回的最终结果
            finalResult = true;

        if (!ruleStr || ruleStr == "") {
            //验证属性不符合规则跳出循环
            return true;
        }

        if (!errTips.length) {
            errTips = insertMsg('', obj);
        }
        //替换label中间的空格和冒号为空
        label = label.replace(/\s|[:：]/gm, "");
        ruleList = ruleStr.split('|');

        for (var i = 0; i < ruleList.length; i++) {
            var resultObj,
                argsList;
            ruleList[i] = $.trim(ruleList[i]);
            if (!ruleList[i]) {
                console.error('check.js：注意 ' + i + ' 不存在');
                continue;
            }

            //现只支持普通规则验证只能传一个入参
            if (ruleList[i] != "exist") {
                if (ruleList[i].indexOf(':') !== -1) {
                    argsList = ruleList[i].split(':');
                    resultObj = regList[$.trim(argsList[0])].call(obj, val, $.trim(argsList[1]));
                } else {
                    resultObj = regList[ruleList[i]].call(obj, val, null);
                }
                if (!resultObj.result) {
                    errTips.hide();
                    $("p", errTips).html(label + resultObj.msg);
                    errTips.fadeIn(100);
                    finalResult = false;

                    clearMsg(errTips);
                    break;
                } else if (!custom) {
                    errTips.fadeOut(100);
                }
            } else {
                //去服务器验证值是否存在的url
                var url = obj.data('vUrl'),
                //url带的参数名称，未配置的或会取当前input的name
                    vName = obj.data('vName') || obj.attr('name');
                resultObj = regList[ruleList[i]].call(obj, val, url, vName, label, errTips);
            }
        }
        return finalResult;
    };

    var insertMsg = function (scope, obj) {
        //插入验证提示

        // 直接加在obj的前面
        if (obj) {
            var msg = $(html);
            obj.siblings(".yqx-err-msg").remove();
            obj.before(msg);
            return msg;
        }
        $(elems, scope || 'body').each(function () {
            $(this).siblings(".yqx-err-msg").remove();
            $(this).before(html);

        });
    };

    var clearMsg = function (node) {
        setTimeout(function () {
            node.fadeOut(500);
        }, 3000);
    };

    var bindEvent = function (scope) {
        $(scope || "body").off('blur.check').on("blur.check", elems, function () {
            var $this = $(this);
            setTimeout(function () {
                if (!$this.prop('disabled')) {
                    valid($this);
                }
            }, 150);
        });
    };
    bindEvent();

    var regList = {
        //服务器验证，例如帐号是否存在
        exist: function (val, url, vname, label, errTips) {
            var msg = [
                "",
                "已存在"
            ];
            var data = {};
            data[vname] = val;
            var dfd = $.Deferred();
            var req = function (dfd) {
                $.ajax({
                    url: url,
                    global: false,
                    async: false,
                    data: data,
                    success: function (json) {
                        dfd.resolve(json);
                    }
                });
                return dfd;
            }
            var relt = req(dfd).done(function (json) {
                if (json.success != true) {
                    $("p", errTips).html(json.errorMsg);
                    errTips.show();

                } else {
                    //true是存在,false是不存在
                    if (json.data == true) {
                        $("p", errTips).html(label + msg[1]);
                        errTips.show();

                    }
                }
            });
        },
        //必填字段
        required: function (val) {
            var msg = [
                "",
                "不能为空"
            ];
            if (val == "" || val == "请选择" || val == "--请选择--") {
                return {msg: msg[1], result: false};
            } else {
                return {msg: msg[0], result: true};
            }
        },
        //字符串最大长度限制
        maxLength: function (val, v) {
            var msg = [
                "",
                "长度不能大于" + v
            ];
            if (val.length <= v) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        //字符串最小长度限制
        minLength: function (val, v) {
            var msg = [
                "",
                "长度不能小于" + v
            ];
            if (val.length >= v) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        //数字最大值限制
        maxValue: function (val, v) {
            var msg = [
                "",
                "不能大于 " + v,
                "请输入数字"
            ];
            if (!$.isNumeric(val)) {
                return {msg: msg[2], result: false};
            }
            if (Number(val) <= v) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        //数字最小值限制
        minValue: function (val, v) {
            var msg = [
                "",
                "不能小于等于" + v,
                "请输入数字"
            ];
            if (!$.isNumeric(val)) {
                return {msg: msg[2], result: false};
            }
            if (Number(val) > v) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        // 身份证
        idCard: function (val) {
            var reg = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
            var msg = [
                "",
                "格式不正确"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            }
            if (reg.test(val)) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        //验证是否是正数
        number: function (val) {
            var msg = [
                "",
                "数字不能小于0",
                "请输入数字"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            }
            if ($.isNumeric(val)) {
                if (val < 0) {
                    return {msg: msg[1], result: false};
                } else {
                    return {msg: msg[0], result: true};
                }
            } else {
                return {msg: msg[2], result: false};
            }
        },
        negativeNumber: function (val) {
            var msg = [
                '',
                '必须为负数'
            ];

            if (!$.isNumeric(val) || val > 0) {
                return {msg: msg[1], result: false};
            } else {
                return {msg: msg[0], result: true};
            }
        },
        //验证是否是正整数
        // v 为 0/undefined 时判断是否大于等于0
        // v 为 1 时判断其是否是大于0的整数
        integer: function (val, v) {
            var reg = /^\d+$/;
            var msg = [
                "",
                "请输入大于等于0的整数"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            }

            // v 为 0/undefined 时判断 是否大于等于0
            // v 为 1 时判断其 是否是大于0的整数
            if (!v && !reg.test(val)) {
                return {msg: msg[1], result: false};
            } else if (v && (!reg.test(val) || val <= 0)) {
                return {msg: '请输入大于0的整数', result: false};
            } else {
                return {msg: '', result: true};
            }
        },

        //保留两位小数（可正可负）
        floating: function (val) {
            var reg = /^[+-]?\d*\.?\d{1,2}$/;

            var msg = [
                "",
                "请输入数字且最多保留两位小数"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            }
            if (reg.test(val)) {
                return {msg: msg[0], result: true};
            }
            else {
                return {msg: msg[1], result: false};
            }
        },
        // 验证是否为金额（直接处理不产生提示框）
        price: function (val) {
            var result = regList.number(val);
            if (result.result) {
                $(this).val(Number(val).toFixed(2));
            }

            return result;
        },
        //验证是否邮箱
        email: function (val) {
            var reg = /\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+/;
            var msg = [
                "",
                "格式不正确"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            }
            if (reg.test(val)) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        //验证是否邮编
        zip: function (val) {
            var reg = /^[0-9]\d{5}$/;
            var msg = [
                "",
                "格式不正确"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            }
            if (reg.test(val)) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        //验证是否座机
        tel: function (val) {
            var reg = /(^0\d{2,3}-*\d{7,8}$)|(^0\d{2,3}-*\d{7,8}-\d+$)/;
            var msg = [
                "",
                "格式不正确"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            }
            if (reg.test(val)) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        //验证是否手机号
        phone: function (val) {
            var reg = /^1[34578][0-9]\d{8}$/;
            var msg = [
                "",
                "格式不正确"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            }
            if (reg.test(val)) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        //验证是否QQ
        qq: function (val) {
            var reg = /^[1-9]\d{5,10}$/;
            var msg = [
                "",
                "格式不正确"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            }
            if (reg.test(val)) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        //vin码
        vin: function (val) {
            var reg = /^[0-9A-Za-z]{17}$/;
            var msg = [
                "",
                "格式不正确"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            }
            if (reg.test(val)) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        //车牌
        licence: function (val) {
            var reg = /^.{3,10}$/;
            var msg = [
                "",
                "格式不正确"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            }
            if (reg.test(val)) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        // 发动机号
        engine: function (val) {
            var reg = /^[0-9a-zA-Z-]+$/;
            var msg = [
                "",
                "格式不正确"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            } else if (reg.test(val)) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        // 非中文验证
        notchinese: function (val) {
            var reg = /[\u4e00-\u9fa5]/;
            var msg = [
                "",
                "请不要输入中文"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            } else if (val.search(reg) === -1) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        },
        //非空字段
        notempty: function (val) {
            var msg = [
                "",
                "不能为0"
            ];
            if (val == 0) {
                return {msg: msg[1], result: false};
            } else {
                return {msg: msg[0], result: true};
            }
        },
        // http
        httpUrl: function (val) {
            var reg = /[a-zA-z]+:\/\/[^\s]*/;
            var msg = [
                "",
                "请输入正确的链接，如: http://www.yunqixiu.com"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            } else if (reg.test(val)) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        }
    }

    /* 滚动到错误信息位置 */
    function scrollToElement($e) {
        // 70 是 header 的高度
        var scrollTop;
        var view = document.body.clientHeight,
            viewTop = document.body.scrollTop;
        // 视窗区域
        var range = [],
        // 提示高出输入宽的高度, 12 为补偿
            tipsHeight = 18 + 12;
        var scrollableParents = [$e[0]];
        var p = $e[0];

        // 获取可滚动的父元素
        while ((p = p.parentNode) !== document.body) {
            if (p.scrollHeight != p.clientHeight) {
                scrollableParents.push(p);
            }
        }

        // 父元素滚动
        scrollableParents.forEach(function (e, i, arr) {
            var top;
            if (!arr[i + 1]) {
                return;
            }

            if (e.offsetParent
                && e.offsetParent.className.indexOf('layui-layer-content') > -1
                && e !== $e[0]) {
                return;
            }

            top = $(e).offset().top - $(arr[i + 1]).offset().top + $(arr[i + 1]).scrollTop();

            arr[i + 1].scrollTop = top - tipsHeight;
        });
        // e 位置已经发生变化

        scrollTop = $e.offset().top - 70 - tipsHeight;

        range[0] = viewTop;
        range[1] = range[0] + view;

        // window 滚动
        // 元素不在视图范围内
        if (scrollTop > range[1] || scrollTop < range[0]) {
            $(window).scrollTop(scrollTop);
        }

    }

    module.exports = {
        //插入验证提示
        insertMsg: function (scope) {
            insertMsg(scope)
        },
        //初始化验证提示信息
        init: function () {
            insertMsg()
        },
        //表单保存或提交时全局验证
        check: function (scope, focus) {
            var result = true;
            var errArr = [];
            $(elems, scope || "body").not(":hidden").each(function (i) {
                var $this = $(this);
                //disabled不校验
                if ($(this).attr("disabled")) {
                    return;
                }
                result = valid($this);

                if (!result) {
                    errArr.push(false);
                    if (focus !== false) {
                        scrollToElement($this);
                    }
                    return false;
                }
            });

            return errArr.length ? false : true;
        },
        showCustomMsg: function (msg, $$ele) {
            var $ele = $($$ele),
                $html = $ele.siblings('.yqx-err-msg');

            if (!$html.length) {
                $html = $(html);
                $ele.before($html);
            }

            $ele.prop('custom', true);

            $html.find('p').html(msg);
            $html.show();
            clearMsg($html);
        },
        //绑定验证失焦获焦事件
        bind: function (scope) {
            bindEvent(scope)
        },
        // 扩展方法
        helper: function (vType, fn, callback) {
            if (typeof regList[vType] === 'function') {
                console.error(vType + "命名冲突！");
            } else {
                regList[vType] = fn;
                insertMsg();
                callback && callback();
            }
        },
        regList: regList
    };
});
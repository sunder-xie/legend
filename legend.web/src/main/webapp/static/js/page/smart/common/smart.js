var Smart = function () {
    var date_format = function (date, format) {
        date = new Date(date);
        var map = {
            M: date.getMonth() + 1,
            //月份
            d: date.getDate(),
            //日
            h: date.getHours(),
            //小时
            m: date.getMinutes(),
            //分
            s: date.getSeconds(),
            //秒
            q: Math.floor((date.getMonth() + 3) / 3),
            //季度
            S: date.getMilliseconds()
        };
        format = format.replace(/([yMdhmsqS])+/g, function (all, t) {
            var v = map[t];
            if (v !== undefined) {
                if (all.length > 1) {
                    v = "0" + v;
                    v = v.substr(v.length - 2);
                }
                return v;
            } else if (t === "y") {
                return (date.getFullYear() + "").substr(4 - all.length);
            }
            return all;
        });
        return format;
    };


    // 日期辅助方法
    var art_date = function (at) {
        at.helper("$dateFormat", function dateFormat(date, format) {
            return date_format(date, format);
        });
    };
    // 金钱辅助方法
    var art_money = function (at) {
        at.helper("$moneyFormat", function moneyFormat(money) {
            return parseFloat(money);
        });
    };
    // 保留两位小数，若无则尾部增加2个0辅助方法
    var art_decimal_point = function (at) {
        at.helper("$decimalPointFormat", function decimalTwoFormat(str,fix_num) {
            var the_val = Number(str);
            return the_val.toFixed(fix_num);
        });
    };
    //阻止事件冒泡
    var stopPropagation = function (e) {
        if (e.stopPropagation) {
            e.stopPropagation();
        } else {
            e.cancelBubble = true;
        }
    };
    //下拉框
    var select_list = function (select_obj, btn_obj) {       //btn_obj 下拉框的上下箭头变换的按钮
        var this_sib = select_obj.siblings("div"),
            this_child = select_obj.children("ul");
        if (select_obj.hasClass("cur")) {           //若当前有样式 特定样式，便隐藏
            this_child.slideUp();
            select_obj.children(btn_obj).addClass("s_up").removeClass("s_down");
            select_obj.removeClass("cur");
        } else {
            this_child.slideDown();
            select_obj.children(btn_obj).removeClass("s_up").addClass("s_down");
            select_obj.addClass("cur");
        }
    };

    // 判断输入框
    var judge_input = function ($this, input_val, alert_msg, $class, alert_msg2, condition) {
        var $tagName = $this[0].tagName;
        if (input_val) {
            if ($tagName == 'INPUT') {
                if (condition) {
                    $this.addClass($class);
                    throw alert_msg2;
                }
            }
        } else {
            if ($tagName == 'INPUT') {
                $this.addClass($class);
            }
            throw '请' + alert_msg;
        }
    };

    return {
        init_art: function (art_obj) {
            art_date(art_obj);
            art_money(art_obj);
            art_decimal_point(art_obj);
        },
        date_format: function (date, format) {
            return date_format(date, format);
        },
        select_list: function (select_obj, btn_obj) {
            return select_list(select_obj, btn_obj)
        },
        stopPropagation: function (e) {
            return stopPropagation(e);
        },
        judge_input:function ($this, input_val, alert_msg, $class, alert_msg2, condition) {
            return judge_input($this, input_val, alert_msg, $class, alert_msg2, condition)
        },
        mode_url:function (next_url) {
            var modeV = util.getPara("mode");   //拿到mode的值
            if (modeV) {
                next_url = next_url + "?mode=" + modeV;
            }
            return next_url
        }
    }
}();
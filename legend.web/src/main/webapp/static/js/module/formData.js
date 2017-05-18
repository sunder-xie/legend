/**
 * ch 2016-03-21
 * 循环获取表单内的数据,formid是form表单的id，返回json格式的数据
 */

define(function (require, exports, module) {

    /**
     * 获取表单内容
     * @param scope
     * @param submit 如果表单更新，提交空内容, default: false
     * @returns {{}}
     */
    exports.get = function (scope, submit) {
        var dd = {};
        $("input,select,textarea", scope).each(function () {
            var $this = $(this);
            var type = $this.attr("type");
            var val = $.trim($this.val());
            var exclude = $this.attr("no_submit");
            var inputName = $this.attr("name");

            if (!inputName) {
                return true;
            }

            //以下几种情况的值不做处理
            if (type == "button" || type == "reset" || type == "submit" ||
                (val == "" && !submit) // 不是提交的时候过滤空
                || exclude != undefined || type == "file") {
                return true;
            }
            if (type == "radio") {
                if ($this.is(":checked")) {
                    dd[inputName] = val;
                } else {
                    return true;
                }
            } else if (type == "checkbox") {
                if ($this.is(":checked")) {
                    if (!dd[inputName]) {
                        dd[inputName] = [val];
                    } else {
                        dd[inputName].push(val);
                    }
                } else {
                    return true;
                }
            } else {
                dd[inputName] = val;
            }
        });
        return dd;
    };

});

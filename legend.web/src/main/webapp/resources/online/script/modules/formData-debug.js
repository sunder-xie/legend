//此模块是循环获取表单内的数据,formid是form表单的id，返回json格式的数据
define("legend/resources/online/script/formData-debug", [], function(require, exports, module) {
    //根据form_item获取表单数据
    exports.get = function(formid) {
        var dd = {};
        $(".form_item,.form_item_hidden", "#" + formid).each(function() {
            //循环formitem
            var formItem = $(this);
            var dynamic = formItem.attr("dynamic_id");
            var arrName = formItem.attr("dynamic_name");
            var ddc = null;
            //如果有动态添加行，会使用此变量，该变量是json对象，代表每一个动态添加行对象
            if (formItem.parent().hasClass("form_item")) {
                return true;
            }
            $("input,select,textarea", formItem).each(function() {
                var $this = $(this);
                var type = $this.attr("type");
                var val = $.trim($this.val());
                var exclude = $this.attr("no_submit");
                var inputName = $this.attr("name");
                //以下几种情况的值不做处理
                if (type == "button" || type == "reset" || type == "submit" || val == "" || exclude != undefined || type == "file" || val == null) {
                    return true;
                }
                if (dynamic) {
                    //需要组成json数组，并转换成json字符串
                    if (ddc != null) {
                        ddc[inputName] = val;
                    } else {
                        ddc = {};
                        ddc[inputName] = val;
                    }
                } else if (type == "radio") {
                    if ($this.is(":checked")) {
                        dd[inputName] = val;
                    } else {
                        return true;
                        ª;
                    }
                } else if (type == "checkbox") {
                    if ($this.is(":checked")) {
                        if (!dd[inputName]) {
                            dd[inputName] = [ val ];
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
            if (dynamic) {
                //需要组成json数组，并转换成json字符串
                if (!dd[arrName]) {
                    dd[arrName] = [];
                    if (ddc != null) {
                        dd[arrName].push(ddc);
                    }
                } else {
                    if (ddc != null) {
                        dd[arrName].push(ddc);
                    }
                }
            }
        });
        //把子数组或对象转成字符串
        $.each(dd, function(k, v) {
            if (typeof v == "object") {
                dd[k] = JSON.stringify(v);
            }
        });
        return dd;
    };
    exports.get2 = function(scope) {
        var dd = {};
        $("input,select,textarea", scope).each(function() {
            var $this = $(this);
            var type = $this.attr("type");
            var val = $.trim($this.val());
            var exclude = $this.attr("no_submit");
            var inputName = $this.attr("name");
            //以下几种情况的值不做处理
            if (type == "button" || type == "reset" || type == "submit" || val == "" || exclude != undefined || type == "file") {
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
                        dd[inputName] = [ val ];
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
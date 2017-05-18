//select数据异步加载的模块
define("legend/resources/online/script/select-debug", [ "./ajax-debug", "./dialog-debug", "../libs/layer/layer-debug", "./../libs/artTemplate/artTemplate-debug", "./chosenSelect-debug", "../libs/chosen/chosen-debug" ], function(require, exports, module) {
    var ajax = require("./ajax-debug");
    var art = require("./../libs/artTemplate/artTemplate-debug");
    var chosen = require("./chosenSelect-debug");
    exports.init = function() {
        var selects = $("select");
        var _useSelect = [];
        //请求发送前相同url的合并
        $.each(selects, function(index, el) {
            var _select = {};
            var serviceUrl = $(el).attr("service_url");
            if (!serviceUrl) {
                return true;
            }
            _select.obj = [ el ];
            _select.url = serviceUrl;
            $.each(selects, function(index, ele) {
                var _serviceUrl = $(ele).attr("service_url");
                if (el === ele || !_serviceUrl || $(ele).data("flag")) {
                    return true;
                }
                //相同url的select 其中一个打标记，下次将不参与循环。
                if (_serviceUrl == serviceUrl) {
                    $(ele).data("flag", true);
                    _select.obj.push(ele);
                }
            });
            if (!$(el).data("flag")) {
                _useSelect.push(_select);
            }
        });
        for (var i = 0; i < _useSelect.length; i++) {
            (function(item, serviceUrl) {
                ajax.get({
                    url: serviceUrl,
                    success: function(json) {
                        for (var j = 0; j < item.obj.length; j++) {
                            success(json, $(item.obj[j]));
                        }
                    }
                });
            })(_useSelect[i], _useSelect[i].url);
        }
        var success = function(json, obj) {
            obj.find("option:gt(0)").remove();
            var tpl = obj.siblings("script").html();
            var temp = art.compile(tpl);
            var html = temp({
                json: json
            });
            obj.append(html);
            var val = obj.attr("val");
            if (val) {
                obj.val(val);
            }
            //初始化select下拉框
            if (obj.hasClass("chosen")) {
                chosen.handleChoosenSelect(obj);
            }
        };
    };
});
//填充表格数据
define("legend/resources/online/script/table-debug", [ "./ajax-debug", "./dialog-debug", "../libs/layer/layer-debug", "./formData-debug", "./../libs/artTemplate/artTemplate-debug", "./paging-debug", "./check-debug" ], function(require, exports, module) {
    var ajaxModule = require("./ajax-debug");
    var formModule = require("./formData-debug");
    var template = require("./../libs/artTemplate/artTemplate-debug");
    var pageModule = require("./paging-debug");
    var dialogModule = require("./dialog-debug");
    var checkModule = require("./check-debug");
    exports.fill = function(args) {
        //表格
        var table;
        //传入的参数json格式
        if (typeof args == "object") {
            table = $("#" + args.tableid);
        } else {
            table = $("#" + args);
        }
        //表格头上的表单
        var formid = table.attr("ref_search_form_id");
        var form = $("form:eq(0)", $("#" + formid));
        //表单数据提交方法
        var method = form.attr("method");
        //查询的url
        var serviceUrl = form.attr("action") || table.attr("service_url");
        //表单的参数数据
        var datas = formModule.get(formid);
        //循环对象的模板
        var tpl = $("script[type='text/html']", table).html();
        //表格的回调方法
        var callback = table.attr("callback_fn");
        //表格数据第一次是否加载
        var autoLoad = table.attr("auto_load");
        //传入的参数json格式
        if (typeof args == "object" && args.data) {
            datas = $.extend(datas, args.data);
        }
        //表格关联的搜索表单绑定
        form.off("click", ".search_btn").on("click", ".search_btn", function() {
            if (checkModule.check("#" + formid)) {
                if (method && method == "post") {
                    ajaxModule.post({
                        url: serviceUrl,
                        data: $.extend(formModule.get(formid), {
                            page: 1
                        }),
                        success: success
                    });
                } else {
                    ajaxModule.get({
                        url: serviceUrl,
                        data: $.extend(formModule.get(formid), {
                            page: 1
                        }),
                        success: success
                    });
                }
            }
        });
        //给表格添加回车事件
        form.on("keydown", function(e) {
            if (e.keyCode == 13) {
                $(".search_btn").trigger("click");
                return false;
            }
        });
        //表格数据项的删除事件绑定
        table.off("click").on("click", ".data_del", function() {
            var id = $(this).attr("id_value");
            var url = $(this).attr("service_url");
            dialogModule.confirm("确认删除？", function() {
                ajaxModule.post({
                    url: url,
                    data: {
                        id: id
                    },
                    loadText: "正在删除中...",
                    success: function(json) {
                        var p = $("span.current", table).text();
                        if (!json.success) {
                            dialogModule.info(json.errorMsg, 3);
                        } else {
                            dialogModule.info("删除成功", 1);
                            ajaxModule.get({
                                url: serviceUrl,
                                data: $.extend({
                                    page: p
                                }, formModule.get(formid)),
                                success: success
                            });
                        }
                    }
                });
            });
        });
        //模板添加自定义方法
        template.helper("$jsonToString", function(v) {
            return JSON.stringify(v);
        });
        template.helper("$dateFormat", function dateFormat(date, format) {
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
            format = format.replace(/([yMdhmsqS])+/g, function(all, t) {
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
        });
        //v是值，n是截断几位字符。
        template.helper("$substr", function(v, n) {
            if (!v && v.lengthB < n) {
                return v;
            }
            return v.substr(0, n) + "...";
        });
        //数据请求成功后的回调
        var success = function(json) {
            if (json.success && json.data != null) {
                var current = json.data.number + 1;
                //删除数据时，当前页被删除完后跳转到第一页
                if (json.data.content && json.data.content.length == 0 && current != 1) {
                    current = 1;
                    location.hash = 1;
                    backFn(current);
                    return;
                } else {
                    if (!isNaN(current)) {
                        location.hash = current;
                    }
                }
                var temp = template.compile(tpl);
                var html = temp({
                    templateData: json
                });
                $(".table_content", table).html(html);
                //插入验证提示
                checkModule.init();
                pageModule.init({
                    dom: table,
                    itemSize: json.data.totalElements,
                    pageCount: json.data.totalPages,
                    current: current,
                    backFn: function(p) {
                        backFn(p);
                    }
                });
                if (callback) {
                    try {
                        eval(callback)(json, tpl);
                    } catch (e) {
                        console.error("没找到表格的回调方法");
                    }
                }
                args.callback && args.callback();
            } else {
                $(".table_content", table).html("");
                pageModule.init({
                    dom: table,
                    itemSize: 0,
                    pageCount: 0,
                    current: current,
                    backFn: function(p) {
                        backFn(p);
                    }
                });
            }
        };
        var hashPage = location.hash;
        var pageNum = 1;
        // 记忆页码功能
        if (hashPage == "") {
            pageNum = 1;
            location.hash = pageNum;
        } else {
            pageNum = hashPage.substr(1);
        }
        //分页点击后的回调
        var backFn = function(p) {
            ajaxModule.get({
                url: serviceUrl,
                data: $.extend({
                    page: p
                }, formModule.get(formid)),
                success: success
            });
        };
        //页面第一次访问时加载数据
        if (autoLoad && autoLoad == "true") {
            if (method && method == "post") {
                ajaxModule.post({
                    url: serviceUrl,
                    data: $.extend({
                        page: pageNum
                    }, datas),
                    success: success
                });
            } else {
                ajaxModule.get({
                    url: serviceUrl,
                    data: $.extend({
                        page: pageNum
                    }, datas),
                    success: success
                });
            }
        }
    };
});
//tab模块封装
define("legend/resources/online/script/tab-debug", [ "./ajax-debug", "./dialog-debug", "../libs/layer/layer-debug" ], function(require, exports, module) {
    var ajaxModule = require("./ajax-debug");
    exports.init = function() {
        var tabs = $(".qxy_tab");
        //多个tab循环处理
        tabs.each(function() {
            var oTab = $(this), links = $("ul a", oTab);
            //tab里面的tabItem循环处理url和显示当前tab项的内容
            links.each(function() {
                var oLink = $(this);
                var tagUrl = oLink.attr("tagurl");
                if (tagUrl) {
                    // 转移目标链接地址
                    oLink.attr("tagurl", $.trim(oLink.attr("href")));
                    oLink.attr("href", "javascript:void(0);");
                    // 数据请求成功后的回调
                    var success = function(json) {
                        $("font", oLink).html(json.data);
                    };
                    ajaxModule.get({
                        url: tagUrl,
                        success: success
                    });
                }
            });
            // tab里面的tabItem绑定事件
            links.click(function() {
                links.removeClass("current");
                $(this).addClass("current");
            });
        });
    };
    /**
	 * 异步请求后在下层div反显响应页面
	 */
    function fillTabContent(model) {
        ajaxModule.get({
            url: model.attr("tagurl"),
            dataType: "html",
            success: function(html) {
                var tabContent = model.closest("fieldset").next("div");
                tabContent.html(html);
            }
        });
    }
});
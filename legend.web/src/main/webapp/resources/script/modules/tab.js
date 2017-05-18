//tab模块封装
define(function(require, exports, module) {
	var ajaxModule = require("./ajax");
	exports.init = function() {
		var tabs = $(".qxy_tab");
		//多个tab循环处理
		tabs.each(function(){
			var oTab = $(this),
				links = $("ul a",oTab);
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
						$("font",oLink).html(json.data);
					}
					ajaxModule.get({
						url : tagUrl,
						success : success
					});
					//current的TAB要显示内容
					// var clazz = oLink.attr("class");
					// if (clazz == "current") {
					// 	fillTabContent(oLink);
					// }
				}
			});
			// tab里面的tabItem绑定事件
			oTab.on("click","ul a",function() {
				// links.removeClass('current');
				$(this).parent().siblings("li").find("a").removeClass('current');
				$(this).addClass('current');
				// fillTabContent($(this));
			});
		});
	};

	/**
	 * 异步请求后在下层div反显响应页面
	 */
	function fillTabContent(model) {
		ajaxModule.get({
			url : model.attr("tagurl"),
			dataType : "html",
			success : function(html) {
				var tabContent = model.closest("fieldset").next("div");
				tabContent.html(html);
			}
		});
	}
	;
});

/**
 * ch 2016-03-21
 * 全局ajax设置
 */

define(function(require,exports,module){
	var dg = require("dialog");
	var loadding = false;
	
	$.ajaxSetup({
		dataType: "json",
		async:true,
		cache:false,
		contentType:"application/x-www-form-urlencoded;charset=utf-8"
	});

	/*  */
	$(document)
		.ajaxStart(function () {
			//请求发送之前加载loadding动画
			loadding && dg.close(loadding);
			loadding = dg.load();
		})
		.ajaxStop(function () {
			loadding && dg.close(loadding);
		});
});
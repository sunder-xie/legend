//统一的ajax入口模块，方便以后做全局设置
define(function(require,exports,module){
	var dialogModule = require("./dialog");
	var loadding = false;
	var ajaxFn = function(option){
		var deferred = $.Deferred();
		$.ajax({
			type: option.type,
			dataType : option.dataType,
			cache: option.cache,
			url: option.url,
			async:option.async,
			contentType : option.contentType,
			data: option.data,
			beforeSend : function(){
				//lodding动画
				if(option.loadShow){
					if(option.beforeSend){
						option.beforeSend();
					}else{
						loadding && dialogModule.close(loadding);
						loadding = dialogModule.load(option.loadText);
					}
				}
			},
			success: function(json){
				if(option.loadShow){
					loadding && dialogModule.close(loadding);
				}
				deferred.resolve(json);
				typeof(option.success) == "function" && option.success(json,dialogModule);

			},
			error:function(){
				// console.error("返回数据格式有误");
				if(option.loadShow){
					loadding && dialogModule.close(loadding);
				}
				deferred.reject();
				typeof(option.failded) == "function" && option.failded();
			}
		});
		return deferred.promise();
	}
	var optExt = function(option,type){
		return $.extend({
			type:type,
			url:"",
			data:{},
			dataType : "json",
			async:true,
			cache:false,
			contentType:"application/x-www-form-urlencoded",
			loadShow:true,
			loadText:'正在加载，请稍候...',
			success:function(){},
			failded:function(){}
		},option);
	}
	exports.post = function(option){
		return ajaxFn(optExt(option,"post"));
	}
	exports.get = function(option){
		return ajaxFn(optExt(option,"get"));
	}
});
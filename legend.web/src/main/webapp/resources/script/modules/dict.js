//dict模块封装
define(function(require, exports, module) {
	var ajaxModule = require("./ajax");
	exports.init = function(scope) {
		var selects = $("select",scope);
		//请求数据方法
		var getData = function(obj,dict_type,dict_code,evt){
			var selectInitValue = obj.val();
			var html = "";
			//获取当前下拉框的数据
			ajaxModule.get({
				url:BASE_PATH + "/dict/getChildDictType",
				data:{"dict_type":dict_type,"dict_code":dict_code},
				loadShow:false,
				success:function(json){
					if(json.success){
						success(json);
					}else{
						console.error(json.errorMsg);
					}
				}
			});
			//请求数据成功后的回调
			var success = function(json){
				$.each(json.data,function(k,v){
					html += "<option value='"+v.id+"'>"+v.name+"</option>";
				});
				obj.html(html);
				if(selectInitValue!=""){
					if(evt == "change"){
						//联动的触发
						$("option:eq(0)",obj).attr("selected","selected");
					}else{
						//编辑回显
						obj.val(selectInitValue);
					}
				}else if(selectInitValue == ""){
					//初始化显示
					$("option:eq(0)",obj).attr("selected","selected");
				}
				ld(obj);
			}
		}
		//联动方法
		var ld = function(obj,evt){
			var index  = selects.index(obj);
			if(obj.length==0) return;
			var nextSelect = $("select:eq("+(index+1)+")",scope);
			var type = obj.attr("dicttype");
			getData(nextSelect,type,obj.val(),evt);
		}
		//前一个下拉框改变时，后面的下拉框联动
		selects.on("change",function(){
			ld($(this),"change");
		});
	};
});
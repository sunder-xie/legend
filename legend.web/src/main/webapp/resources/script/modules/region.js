//dict模块封装
define(function(require, exports, module) {
	var ajaxModule = require("./ajax");
	exports.init = function(scope) {
		var selects = $("select", scope);

		// 请求数据方法
		var getData = function(obj, p_id, id, evt) {
			var index = selects.index(obj);
			var html = "";
			// 获取当前下拉框的数据
			ajaxModule.get({
				url : BASE_PATH + "/dict/getSubRegionByPid",
				data : {
					"p_id" : p_id
				},
				loadShow : false,
				success : function(json) {
					if (json.success) {
						success(json, id);
					} else {
						console.error(json.errorMsg);
					}
				}
			});
			// 请求数据成功后的回调
			var success = function(json, id) {
				var p_id = 1;
				if (json.data[0] != null)
					p_id = json.data[0].id;
				$.each(json.data, function(k, v) {
					if (id == v.id) {
						html += "<option selected='selected' value='" + v.id
								+ "'>" + v.regionName + "</option>";
						p_id = id;
					} else {
						html += "<option value='" + v.id + "'>" + v.regionName
								+ "</option>";
					}

				});
				obj.html(html);
				ld(obj, p_id);
			};
		};
		// 联动方法
		var ld = function(obj, p_id,p_name, evt) {
			var curId = obj.attr('id');
			var input = $("input[id^='" + curId + "']", scope);
			//录入当前隐藏区域ID
			input.val(p_id);
			
			var inputName = $("input[name^='" + curId + "']", scope);
			
			//录入当前隐藏区域名称
			inputName.val(obj.find("option:selected").text());
			
			var index = selects.index(obj);
			if (selects.length == index + 1)
				return;
			var nextSelect = $("select:eq(" + (index + 1) + ")", scope);
			var input = $("input[type=hidden]:eq(" + (index + 1) + ")", scope);
			obj.attr("region_p_id", p_id);
			getData(nextSelect, p_id, input.val(), evt);
		};

		// 前一个下拉框改变时，后面的下拉框联动
		selects.on("change", function() {
			var p_id = $(this).val();
			ld($(this), p_id, "change");
		});

		// 如果有传区域参数，则反显下拉框数据
		var input = $("input[type=hidden]:eq(0)", scope);
		var firstSelect = $("select:eq(0)", scope);
		if (input.val() != "") {
			ajaxModule.get({
				url : BASE_PATH + "/dict/getRegionListByIds",
				data : {
					"ids" : input.val()
				},
				loadShow : false,
				success : function(result) {
					if (result.success) {
						getData(firstSelect, result.data[0].parentId, input
								.val(), "change");
					} else {
						console.error(result.errorMsg);
					}
				}
			});
		} else {// 否则级联初始数据

			var type_id = firstSelect.attr("region_p_id");
			getData(firstSelect, type_id, "", "change");
		}
	};
});
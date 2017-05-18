$(document).ready(function() {
	$("body").on("focusout", "input[name='serviceName']", function() {
		validateRepeat();
	})
})

function validateRepeat() {
	var vMap = {};
	var flag = true;
	$("input[name='serviceName']").each(function(i, e) {
		if (vMap[$(e).val()] && $(e).val()!="") {
			seajs.use([ "dialog" ], function(dialog) {
				dialog.info("服务名[" + $(e).val() + "]不能重复.", 5);
			});
			flag = false;
		} else {
			vMap[$(e).val()] = true;
		}
	})
	return flag;
}
function updateSuite() {
	if (validateRepeat()) {
		var cnt = 0;
		$("input[name='serviceName']").each(function(i, e) {
			if ($.trim($(e).val()) != '') {
				cnt++;
			}
		})
		if (cnt == 0) {
			seajs.use([ "dialog" ], function(dialog) {
				dialog.info("请至少添加一项服务.", 5);
			});

		} else {
			var data = {};
			data["id"] = $("input[name=id]").val();
			data["suiteName"] = $("input[name=suiteName]").val();
			data["price"] = $("input[name=price]").val();
			data["validDays"] = $("input[name=validDays]").val();
			var serviceList = [];

			$(".suite_edit_service_content .form_item").each(function(i, e){
				var suiteName = $(e).find("input[name=serviceName]").val();
				if($.trim(suiteName) != "") {
					var serviceCount = $(e).find("input[name=serviceCount]").val();
					if($.trim(serviceCount) != "") {
						serviceList.push({serviceName:suiteName,serviceCount:serviceCount});
					}
				}
			});
			data["servicesStr"] = JSON.stringify(serviceList);
			seajs.use(["ajax","dialog"],function(ax,dg){
				ax.post({
					url: BASE_PATH+"/member/suite/update",
					data: data,
					dataType : "json",
					success:function(json) {
						if (json.success) {
							dg.info("操作成功",1,1,function(){
								history.go(-1);
							},true);
						} else {
							dg.info(json.errorMsg, 3);
						}
					}
				});
			});
		}
	}
};
seajs.use("downlist");
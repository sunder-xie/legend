$(document).ready(function() {
	$("body").on("focusout", "input[name='serviceName']", function() {
		validateRepeat();
	})
})

function validateRepeat() {
	var vMap = {};
	var flag = true;
	$("input[name='serviceName']").each(function(i, e) {
		if (vMap[$(e).val()] && $(e).val() != "") {
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
			util.submit({
				formid : 'serviceSaveForm',
				callback : function(json, dialog) {
					if (json.success) {
						dialog.info("操作成功", 1);
						history.go(-1);
					} else {
						dialog.info(json.errorMsg, 3);
					}
				}
			})
		}
	}
};
seajs.use("downlist");
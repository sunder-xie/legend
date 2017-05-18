$(document).ready(function() {
	$("body").on("focusout", "input[name='serviceName']", function() {
		validateRepeat();
	})
})

function validateRepeat() {
	var vMap = {};
	var flag = true;
	$("input[name='serviceName']").each(function(i, e) {
		if (vMap[$(e).val()]) {
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
function addServiceSuite() {
	seajs.use([ "dialog" ], function(dg) {
		dg.dialog({
			"dom" : "#addServiceSuite"
		});
	});
}
function saveSuite() {
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
				formid : 'suiteSaveForm',
				callback : function(json, dialog) {
					if (json.success) {
						dialog.info("操作成功", 1);
						$("#suiteSaveForm").find("input").val('');
						seajs.use('table', function(table) {
							table.fill("suite_table");
						});
						layer.closeAll();
					} else {
						dialog.info(json.errorMsg, 3);
					}
				}
			})
		}
	}
};
function closeDlg() {
	$("#suiteSaveForm").find("input").val('');
	layer.closeAll();
}
seajs.use("downlist");
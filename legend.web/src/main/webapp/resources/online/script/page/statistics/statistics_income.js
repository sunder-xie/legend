/**
 * Created by wjc on 15/5/26.
 */

$(function () {
	$(document).ready(function(){
		$("#d4311").attr("value",getNow())
		submitForm();
	    /*统计*/
	    $("#searchButton").click(function () {
	        if ($.trim($("#d4311").val()) == '') {
	            seajs.use("dialog", function (dialog) {
	                dialog.info("请选择查询时间");
	            });
	            return;
	        }
	        submitForm();
	    });
	});
});

function getNow() {
	var dd = new Date();
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();
    return y+"-"+m+"-"+d;
}

function submitForm() {
    seajs.use(["ajax", "artTemplate"], function (ajax, template) {

        var data = $("#searchForm").serializeArray();
        var url = $("#searchForm").attr("action");
        ajax.post({
            url: url,
            data: data,
            success: function (result, dialog) {
                if (!result.success) {
                    dialog.info(result.errorMsg, 5);
                    $('.content_amount').html('');
                    return;
                } else {
                    var temp = result.data.resultList;
                    var startTime = result.data.startTime;
                    var endTime = result.data.endTime;
                    $("#d4311").val(startTime);
                    $("#d4312").val(endTime);
                    $('.content_amount').html(template.render('contentTemplate', {'templateData': temp}));
                }
            }
        });
    });
};


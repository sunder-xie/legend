/**
 * Created by twg on 15/5/4.
 */
$(function () {

    /*统计*/
    $("#searchButton").click(function () {
        if ($.trim($("#d4311").val()) == '') {
            seajs.use("dialog", function (dialog) {
                dialog.info("请选择开始时间");
            });
            return;
        }
        submitForm();
    });

});
/*打印*/
function print_stats(url){
    if ($.trim($("#d4311").val()) == '') {
        seajs.use("dialog", function (dialog) {
            dialog.info("请选择开始时间");
        });
        return;
    }
    var startTime = $.trim($("#d4311").val());
    var stopTime = $.trim($("#d4312").val());
    url = BASE_PATH+url+"?search_startTime="+startTime+"&search_endTime="+stopTime;
    window.open(url);
}
/*导出*/
function export_stats(url){
    if ($.trim($("#d4311").val()) == '') {
        seajs.use("dialog", function (dialog) {
            dialog.info("请选择开始时间");
        });
        return;
    }
    var startTime = $.trim($("#d4311").val());
    var stopTime = $.trim($("#d4312").val());
    url = BASE_PATH+url+"?search_startTime="+startTime+"&search_endTime="+stopTime;
    window.location.href = url;
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


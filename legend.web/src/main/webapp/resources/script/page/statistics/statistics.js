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
        $("#search_searchType").val("");
        submitForm();
    });
    /*今天*/
    $("#today_stats").click(function () {
        $("#d4311").val("");
        $("#d4312").val("");
        $("#search_searchType").val("today");
        submitForm();
    });
    /*昨天*/
    $("#yesterday_stats").click(function () {
        $("#d4311").val("");
        $("#d4312").val("");
        $("#search_searchType").val("yesterday");
        submitForm();
    });

});
/*打印*/
function print_stats(url){
    var searchType = $.trim($("#search_searchType").val());
    if (null != searchType && searchType != '') {
        util.print(BASE_PATH +url+ "?search_searchType=" + searchType);
        return;
    }
    if ($.trim($("#d4311").val()) == '') {
        seajs.use("dialog", function (dialog) {
            dialog.info("请选择开始时间");
        });
        return;
    }
    var startTime = $.trim($("#d4311").val());
    var stopTime = $.trim($("#d4312").val());
    util.print(BASE_PATH + url +"?search_startTime=" + startTime
    + "&search_endTime=" + stopTime);
    return;
}
/*导出*/
function export_stats(url){
    var searchType = $.trim($("#search_searchType").val());

    if (null != searchType && searchType != '') {
        window.location.href = BASE_PATH + url + "?search_searchType=" + searchType;
        return;
    }
    if ($.trim($("#d4311").val()) == '') {
        seajs.use("dialog", function (dialog) {
            dialog.info("请选择开始时间");
        });
        return;
    }

    var startTime = $.trim($("#d4311").val());
    var stopTime = $.trim($("#d4312").val());
    window.location.href = BASE_PATH + url+ "?search_startTime=" + startTime
    + "&search_endTime=" + stopTime;
    return;

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

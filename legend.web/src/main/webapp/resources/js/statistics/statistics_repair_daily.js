/**
 * Created by QXD on 2015/3/4.
 */
$(function(){
    $("#searchForm").submit(function(){
        var save = taoqi.loading("获取信息中");
        $(this).ajaxSubmit({
            success: function (result) {
                taoqi.close(save);
                if (result.success != true) {
                    taoqi.error(result.errorMsg);
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
            },
            error: function (a, b, c) {
            },
            cache: false
        });
        return false;
    });

    $("#searchButton").click(function(){
        if($.trim($("#d4311").val()) == ''){
            taoqi.failure("请选择开始时间");
            return;
        }
        $("#search_searchType").val("");
        $("#start_time_lab").text($.trim($("#d4311").val())+"至");
        if($.trim($("#d4312").val()) == ''){
            $("#stop_time_lab").text("今");
        }else{
            $("#stop_time_lab").text($.trim($("#d4312").val()));
        }
        $("#searchForm").submit();
        return false;
    });
    $("#today_stats").click(function(){
        $("#d4311").val("");
        $("#d4312").val("");
        $("#start_time_lab").text("今天");
        $("#stop_time_lab").text("");
        $("#search_searchType").val("today");
        $("#searchForm").submit();
        return false;
    });
    $("#yesterday_stats").click(function(){
        $("#d4311").val("");
        $("#d4312").val("");
        $("#start_time_lab").text("昨天");
        $("#stop_time_lab").text("");
        $("#search_searchType").val("yesterday");
        $("#searchForm").submit();
        return false;
    });

    //导出excel相关
    $(document).on('click', '#export_stats', function () {
        var searchType = $.trim($("#search_searchType").val());
        var startTime = $.trim($("#d4311").val());

        if(null == startTime || startTime == ''){
            if(null != searchType){
                window.location.href = BASE_PATH + "/shop/stats/daily/get_stats_amount/export?search_searchType="+searchType;
                return false;
            }else{
                taoqi.failure("请选择开始时间");
                return;
            }
        }
        var stopTime = $.trim($("#d4312").val());
        window.location.href = BASE_PATH + "/shop/stats/daily/get_stats_amount/export?search_startTime=" + startTime
            + "&search_endTime=" + stopTime;
        return false;
    });

    //打印
    $(document).on('click', '#print_stats', function () {
        var searchType = $.trim($("#search_searchType").val());
        if(null != searchType && searchType!=''){
            window.open(BASE_PATH + "/shop/stats/daily/print_stats_repair?search_searchType="+searchType);
            return false;
        }
        if($.trim($("#d4311").val()) == ''){
            taoqi.failure("请选择开始时间");
            return;
        }
        var startTime = $.trim($("#d4311").val());
        var stopTime = $.trim($("#d4312").val());
        window.open(BASE_PATH + "/shop/stats/daily/print_stats_repair?search_startTime=" + startTime
            + "&search_endTime=" + stopTime);
        return false;
    });

});

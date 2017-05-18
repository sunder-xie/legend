/**
 * Created by QXD on 2015/1/23.
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
        $("#start_time_lab").text($.trim($("#d4311").val())+"至");
        if($.trim($("#d4312").val()) == ''){
            $("#stop_time_lab").text("今");
        }else{
            $("#stop_time_lab").text($.trim($("#d4312").val()));
        }
        $("#searchForm").submit();
    });

    //导出excel相关
    $(document).on('click', '#export_stats', function () {
        if($.trim($("#d4311").val()) == ''){
            taoqi.failure("请选择开始时间");
            return;
        }
        var startTime = $.trim($("#d4311").val());
        var stopTime = $.trim($("#d4312").val());
        window.location.href = BASE_PATH + "/shop/stats/stats_repair/get_stats_amount/export?search_startTime=" + startTime
            + "&search_endTime=" + stopTime;
        return false;
    });

    //打印
    $(document).on('click', '#print_stats', function () {
        if($.trim($("#d4311").val()) == ''){
            taoqi.failure("请选择开始时间");
            return;
        }
        var startTime = $.trim($("#d4311").val());
        var stopTime = $.trim($("#d4312").val());
        window.open(BASE_PATH + "/shop/stats/stats_repair/print_stats_repair?search_startTime=" + startTime
            + "&search_endTime=" + stopTime);
       return false;
    });
});

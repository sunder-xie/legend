/**
 * Created by QXD on 2015/4/29.
 */
$(function(){
    $("#searchForm").submit(function(){
        var save = taoqi.loading("获取信息中");
        $(this).ajaxSubmit({
            success: function (result) {
                taoqi.close(save);
                if (result.success != true) {
                    taoqi.error(result.errorMsg);
                    $('.content').html('');
                    return;
                } else {
                    var temp = result.data.resultList;
                    $("#content").html(template.render('contentTemplate', {'templateData': temp}));
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
/*        $("#start_time_lab").text($.trim($("#d4311").val())+"至");
        if($.trim($("#d4312").val()) == ''){
            $("#stop_time_lab").text("今");
        }else{
            $("#stop_time_lab").text($.trim($("#d4312").val()));
        }*/
        if(!dateCompare()){
            taoqi.failure("最长选择31天");
            return;
        }

        $("#searchForm").submit();
    });

    //导出excel相关
    $(document).on('click', '#export_stats', function () {
        if($.trim($("#d4311").val()) == ''){
            taoqi.failure("请选择开始时间");
            return;
        }
        if(!dateCompare()){
            taoqi.failure("最长选择31天");
            return;
        }
        var startTime = $.trim($("#d4311").val());
        var stopTime = $.trim($("#d4312").val());
        window.location.href = BASE_PATH + "/shop/stats/stats_settlement_daily/get_stats_amount/export?search_startTime=" + startTime
            + "&search_endTime=" + stopTime;
        return false;
    });

    //打印
    $(document).on('click', '#print_stats', function () {
        if($.trim($("#d4311").val()) == ''){
            taoqi.failure("请选择开始时间");
            return;
        }
        if(!dateCompare()){
            taoqi.failure("最长选择31天");
            return;
        }
        var startTime = $.trim($("#d4311").val());
        var stopTime = $.trim($("#d4312").val());
        window.open(BASE_PATH + "/shop/stats/stats_settlement_daily/print_stats_settlement_daily?search_startTime=" + startTime
            + "&search_endTime=" + stopTime);
        return false;
    });
    function dateCompare(){
        var startTime = $.trim($("#d4311").val());
        var endTime = $.trim($("#d4312").val());
        var arrDate, objDate1, objDate2, intDays;
        objDate1 = new Date();
        objDate2 = new Date();

        arrDate = startTime.split("-");
        objDate1.setFullYear(arrDate[0], arrDate[1]-1, arrDate[2]);

        if(endTime != ''){
            arrDate = endTime.split("-");
            objDate2.setFullYear(arrDate[0], arrDate[1]-1, arrDate[2]);
        }

        intDays = parseInt(Math.abs(objDate1 - objDate2) / 1000 / 60 / 60 / 24);

        if(intDays + 1 > 31){
            return false;
        }
        return true;
    }
});

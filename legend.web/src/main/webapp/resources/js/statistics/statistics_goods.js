/**
 * Created by zsy on 2015/3/3
 */
$(function(){
    var pageNum = 1;
    $("#searchForm").submit(function () {
        pageNum = 1;
        moreData($(this), pageNum, false);
        return false;
    });

    $("#searchButton").click(function(){
        var startDay = $.trim($(".startDay").val());
        var endDay = $.trim($(".endDay").val());
        if(!checkDate(startDay,endDay)){
            return false;
        }
        $("#start_day_lab").text(startDay+"至");
        $("#end_day_lab").text(endDay);
        $("#searchForm").submit();
    });

    //excel导出
    $(document).on('click', '#export_goods', function () {
        var startDay = $.trim($(".startDay").val());
        var endDay = $.trim($(".endDay").val());
        if(!checkDate(startDay,endDay)){
            return false;
        }
        window.location.href=BASE_PATH+"/shop/stats/goods/list/export?search_startDay="+startDay+"&search_endDay="+endDay
    });

    //打印
    $("#goods_print").click(function(){
        var startDay = $(".startDay").val();
        var endDay = $(".endDay").val();
        if(!checkDate(startDay,endDay)){
            return false;
        }
        window.open(BASE_PATH+"/shop/stats/goods/print?search_startDay="+startDay+"&search_endDay="+endDay);
    });

});


//验证日期输入是否正确
function checkDate(startDay,endDay){
    if(startDay==''){
        taoqi.failure("请输入天数");
        $(".startDay").focus();
        return false;
    }
    if(endDay==''){
        taoqi.failure("请输入天数");
        $(".endDay").focus();
        return false;
    }
    if(parseInt(endDay) <= parseInt(startDay)){
        taoqi.failure("天数输入有误");
        return false;
    }
    return true;
}
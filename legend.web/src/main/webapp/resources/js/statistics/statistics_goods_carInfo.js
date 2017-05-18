/**
 * Created by zsy on 2015/3/3
 */
$(function(){
    //统计
    $("#searchButton").click(function(){
        $("#searchForm").submit();
    });

    $("#searchForm").submit(function(){
        var startTime = $.trim($(".startTime").val());
        var endTime = $.trim($(".endTime").val());
        if(!checkTime(startTime,endTime)){
            return false;
        }
        var loading = taoqi.loading("获取信息中...");
        $(this).ajaxSubmit({
            success: function (result) {
                taoqi.close(loading);
                if (!result.success) {
                    taoqi.error(result.errorMsg);
                    return false;
                } else {
                    var map = result.data;
                    var temp = map.statisticsGoodsByCarInfoList;
                    var totalStock = map.totalStock;
                    var totalType = map.totalType;
                    $("#totalStock").text("库存总数量："+totalStock);
                    $("#totalType").text("库存品种数："+totalType);
                    $('#content').html(template.render('contentTemplate', {'templateData': temp}));
                }
            },
            cache: false
        });
        return false;
    });

    //当日
    $("#searchToday").click(function(){
        $.ajax({
            type : "POST",
            url : BASE_PATH+"/shop/stats/goods_carInfo/getSystemDate",
            dataType : "json",
            success : function (result) {
                if (result.success != true) {
                    taoqi.failure(result.errorMsg);
                    return false;
                }
                else {
                    var data = result.data;
                    var year = data.year;
                    var month = data.month;
                    var day = data.day;
                    var startTime=year+"-"+month+"-"+day;
                    var endTime=year+"-"+month+"-"+day;
                    $(".startTime").val(startTime);
                    $(".endTime").val(endTime);
                    $("#searchForm").submit();
                }
            },
            error:function(a,b,c){
                return false;
            }
        });
    });

    //当月
    $("#searchThisMonth").click(function(){
        $.ajax({
            type : "POST",
            url : BASE_PATH+"/shop/stats/goods_carInfo/getSystemDate",
            dataType : "json",
            success : function (result) {
                if (result.success != true) {
                    taoqi.failure(result.errorMsg);
                    return false;
                }
                else {
                    var data = result.data;
                    var year = data.year;
                    var month = data.month;
                    var endDay = data.endDay;
                    var startTime=year+"-"+month+"-"+1;
                    var endTime=year+"-"+month+"-"+endDay;
                    $(".startTime").val(startTime);
                    $(".endTime").val(endTime);
                    $("#searchForm").submit();
                }
            },
            error:function(a,b,c){
                return false;
            }
        });
    });

    //当年
    $("#searchThisYear").click(function(){
        $.ajax({
            type : "POST",
            url : BASE_PATH+"/shop/stats/goods_carInfo/getSystemDate",
            dataType : "json",
            success : function (result) {
                if (result.success != true) {
                    taoqi.failure(data.errorMsg);
                    return false;
                }
                else {
                    var data = result.data;
                    var year = data.year;
                    var month = data.month;
                    var day = data.day;
                    var startTime=year+"-"+1+"-"+1;
                    var endTime=year+"-"+12+"-"+31;
                    $(".startTime").val(startTime);
                    $(".endTime").val(endTime);
                    $("#searchForm").submit();
                }
            },
            error:function(a,b,c){
                return false;
            }
        });
    });


    //excel导出
    $(document).on('click', '#export_goods_carInfo', function () {
        var startTime = $.trim($(".startTime").val());
        var endTime = $.trim($(".endTime").val());
        if(!checkTime(startTime,endTime)){
            return false;
        }
       window.location.href = BASE_PATH+"/shop/stats/goods_carInfo/list/export?search_startTime="+startTime+"&search_endTime="+endTime;
    });

    //打印
    $("#goods_carInfo_print").click(function(){
        var startTime = $.trim($(".startTime").val());
        var endTime = $.trim($(".endTime").val());
        if(!checkTime(startTime,endTime)){
            return false;
        }
        window.open(BASE_PATH+"/shop/stats/goods_carInfo/print?search_startTime="+startTime+"&search_endTime="+endTime);
    });

    //验证开始时间和结束时间
    function checkTime(startTime,endTime){
        if(startTime==''){
            taoqi.failure("请选择开始时间");
            return false;
        }
        if(endTime==''){
            taoqi.failure("请选择结束时间");
            return false;
        }
        $("#start_day_lab").text(startTime+"至");
        $("#end_day_lab").text(endTime);
        return true;
    }
});
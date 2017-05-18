/**
 * Created by twg on 15/5/4.
 */
$(function(){

    //excel导出
    $("#export_stats").click(function () {
        var startDay = $.trim($("#startDay").val());
        var endDay = $.trim($("#endDay").val());
        if(startDay==''){
            seajs.use(["dialog"],function(dialog){
                dialog.info("请输入天数");
                $("#startDay").focus();
            });
            return;
        }
        if(endDay==''){
            seajs.use(["dialog"],function(dialog){
                dialog.info("请输入天数");
                $("#endDay").focus();
            });
            return;
        }
        if(parseInt(endDay) <= parseInt(startDay)){
            seajs.use(["dialog"],function(dialog){
                dialog.info("天数输入有误");
            });
            return;
        }
        window.location.href = BASE_PATH+"/shop/stats/goods/list/export_ng?search_startDay="+startDay+"&search_endDay="+endDay
    });
    //打印
    $("#print_stats").click(function(){
        var startDay = $.trim($("#startDay").val());
        var endDay = $.trim($("#endDay").val());
        if(startDay==''){
            seajs.use(["dialog"],function(dialog){
                dialog.info("请输入天数");
                $("#startDay").focus();
            });
            return;
        }
        if(endDay==''){
            seajs.use(["dialog"],function(dialog){
                dialog.info("请输入天数");
                $("#endDay").focus();
            });
            return;
        }
        if(parseInt(endDay) <= parseInt(startDay)){
            seajs.use(["dialog"],function(dialog){
                dialog.info("天数输入有误");
            });
            return;
        }
        window.open(BASE_PATH+"/shop/stats/goods/print_ng?search_startDay="+startDay+"&search_endDay="+endDay);
    });
});


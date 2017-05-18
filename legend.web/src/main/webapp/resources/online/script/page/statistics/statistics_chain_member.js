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
        if ($.trim($("#d4312").val()) == '') {
            seajs.use("dialog", function (dialog) {
                dialog.info("请选择结束时间");
            });
            return;
        }
        submitForm();
    });


    //当月
    $("#searchThisMonth").click(function(){
        seajs.use(["ajax"],function(ajax){
            ajax.post({
                    url : BASE_PATH+"/shop/stats/goods_carInfo/getSystemDate",
                    success:function(result,dialog){
                        if(!result.success){
                            dialog.info(result.errorMsg,5);
                            return false;
                        }else{
                            var data = result.data;
                            var year = data.year;
                            var month = data.month;
                            var endDay = data.endDay;
                            var startTime=year+"-"+month+"-"+1;
                            var endTime=year+"-"+month+"-"+endDay;
                            $("#d4311").val(startTime);
                            $("#d4312").val(endTime);
                            submitForm();
                        }
                    }

                }
            );

        });
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
    if ($.trim($("#d4312").val()) == '') {
        seajs.use("dialog", function (dialog) {
            dialog.info("请选择结束时间");
        });
        return;
    }
    var startTime = $.trim($("#d4311").val());
    var stopTime = $.trim($("#d4312").val());
    window.open(BASE_PATH+url+"?search_startTime="+startTime+"&search_endTime="+stopTime);
}
/*导出*/
function export_stats(url){
    if ($.trim($("#d4311").val()) == '') {
        seajs.use("dialog", function (dialog) {
            dialog.info("请选择开始时间");
        });
        return;
    }
    if ($.trim($("#d4312").val()) == '') {
        seajs.use("dialog", function (dialog) {
            dialog.info("请选择结束时间");
        });
        return;
    }
    var startTime = $.trim($("#d4311").val());
    var stopTime = $.trim($("#d4312").val());
    window.location.href = BASE_PATH+url+"?search_startTime="+startTime+"&search_endTime="+stopTime;
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

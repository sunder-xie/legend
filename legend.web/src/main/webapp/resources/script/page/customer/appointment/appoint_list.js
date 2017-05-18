var appointList = appointList || {};

/**
 * Created by jason on 15-06-03
 */
appointList.reload = function(){
    history.go(0);
}
$(document).ready(function ($) {

    $(document).on('click','.order_create_btn',function(){
        var license = $(this).attr("license");
        var appointId = $(this).attr("appointId");
        var scope = $(this).parents(".order_item");
        seajs.use(['ajax',"dialog"],function(ajax,dg){
            ajax.post({
                url: BASE_PATH + '/shop/customer/checkLicense',//创建新客户方法
                data: {
                    license:license
                },
                success: function (result) {
                    if(result.success==true){
                        dg.confirm("此车辆还不是门店客户，需要马上新增客户?",function(){
                            var customerName = $("input[name='customerName']",scope).val();
                            var mobile = $("input[name='mobile']",scope).val();
                            var appointId = $("input[name='appointId']",scope).val();
                            var carBrandId = $("input[name='carBrandId']",scope).val();
                            var carBrandName = $("input[name='carBrandName']",scope).val();
                            var carSeriesId = $("input[name='carSeriesId']",scope).val();
                            var carSeriesName = $("input[name='carSeriesName']",scope).val();
                            var carModelId = carBrandName + carSeriesName;
                            var obj = {
                                customerName: customerName,
                                mobile: mobile,
                                license:license,
                                appointId:appointId,
                                carBrandId:carBrandId,
                                carBrandName:carBrandName,
                                carSeriesId:carSeriesId,
                                carSeriesName:carSeriesName,
                                carModelId:carModelId
                            }
                            createCustomer(obj);
                        });
                    }else{
                        window.location.href = BASE_PATH+"/shop/customer_appoint/createOrderInfo/ng?id="+appointId;
                    }
                }
            });
        });
    });

    //确认预约
    $(document).on("click",".appoint_confirm_btn",function(){
        var appointId = $(this).attr("appointId");
        seajs.use(["ajax","dialog"],function(ajax,dg){
            dg.confirm("您确定要确认该预约单吗?",function(){
                ajax.post({
                    url : BASE_PATH + "/shop/customer_appoint/confirmAppoint",
                    data : {
                        appointId : appointId
                    },
                    success: function (result){
                        if (result.success != true) {
                            dg.info(result.errorMsg,3);
                            return false;
                        } else {
                            dg.info(result.data,1,3,function(){
                                history.go(0);
                            });
                        }
                    }
                });
            },function (){
                return false;
            });
        });

    });

    //取消预约
    $(document).on("click",".appoint_cancel_btn",function(){
        var appointId = $(this).parents(".order_item").attr("appoint_id");
        seajs.use(["ajax","dialog"],function(ajax,dg){
            dg.dialog({
                dom : "#appoint_cancel_dialog",
                init : function(){
                    $("#appoint_cancel_dialog").data("appointId",appointId);
                }
            });
        });
    });
    //取消预约原因事件
    $("#appoint_cancel_dialog li").click(function(){
        if($(this).hasClass("f_li")){
            $(this).removeClass("f_li");
        }else{
            $(this).addClass("f_li").siblings().removeClass("f_li");
        }
    })
    //取消按钮事件绑定
    $(".cancel","#appoint_cancel_dialog").click(function(){
        seajs.use(["dialog"],function(dg){
            dg.closeDialog("#appoint_cancel_dialog");
        });
    });
    //提交取消预约
    $(document).on('click','.cancelAppoint',function(){
        var appointId = $("#appoint_cancel_dialog").data("appointId");
        var cancelReason = $("#appoint_cancel_dialog li.f_li").text();
        seajs.use(["ajax","dialog"], function (ajax,dialog){
            if(cancelReason == ""){
                dialog.info("请选择取消预约的原因！",3);
                return false;
            }
            ajax.post({
                url : BASE_PATH + "/shop/customer_appoint/cancelAppoint",
                data : {
                    appointId : appointId,
                    cancelReason : cancelReason
                },
                success: function(result) {
                    if (result.success != true) {
                        dialog.info(result.errorMsg,3);
                        return false;
                    } else {
                        dialog.closeDialog("#appoint_cancel_dialog");
                        dialog.info("取消预约单成功",1,3,function(){
                            window.location.href = BASE_PATH+"/shop/customer_appoint/index/ng";
                        },true);
                    }
                }
            });
        });
    });

    //导出
    $(document).on('click', '#exp_btn', function () {
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        var keyword = $("#search_keyword").val();
        window.location.href=BASE_PATH+"/shop/customer_appoint/list/export?search_startTime="+startTime+"&search_endTime="+endTime+
            "&search_keyword="+keyword;

    });

    $(document).on("click", ".urlBtn", function (e) {
        window.location.href = $(this).data('url');
    });

});


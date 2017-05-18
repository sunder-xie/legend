/**
 * Created by jason on 15-06-03
 */
$(document).ready(function ($) {

    //tab切换的时候，对searchForm，search_payStatus进行赋值
    $(".rows_p li").click(function () {    //tab切换

            var tabId = $.trim($(this).find("a").attr("id"));
            if (tabId == 'needOrder') {
                $("#search_orderStatuss").val("CJDD");
                $("#search_payStatus").val("");
            }
            if (tabId == 'offeredOrder') {
                $("#search_orderStatuss").val("DDBJ");
                $("#search_payStatus").val("");
            }
            if (tabId == 'fixOrder') {
                $("#search_orderStatuss").val("FPDD,DDSG");
                $("#search_payStatus").val("");
            }
            if (tabId == "finishedOrder") {
                $("#search_orderStatuss").val("DDWC");
                $("#search_payStatus").val("");
            }
            if (tabId == "payedOrder") {
                $("#search_orderStatuss").val("");
                $("#search_payStatus").val("2");
            }
            if (tabId == "signedOrder") {
                $("#search_orderStatuss").val("");
                $("#search_payStatus").val("1");
            }
            if (tabId == "allOrder") {
                $("#search_orderStatuss").val("");
                $("#search_payStatus").val("");
            }
            //把flag值set到cookie中
            $("#flag").val(tabId);
            seajs.use(["cookie"],function(cookie){
                cookie.setCookie(COOKIE_PREFIX + "order_list_flag",tabId);
            });

            $(".search_btn").trigger('click');//触发search_btn按钮
        }
    );

    //cookie设值
    $(".search_btn").click(function() {
        seajs.use(['cookie'],function(cookie){
            cookie.setCookie(COOKIE_PREFIX + "order_list_carLicenseLike",$("#carLicenseLike").val());//车牌号
            cookie.setCookie(COOKIE_PREFIX + "order_list_mobileLike",$("#mobileLike").val());//手机号
            cookie.setCookie(COOKIE_PREFIX + "order_list_startTime",$("#startTime").val());
            cookie.setCookie(COOKIE_PREFIX + "order_list_endTime",$("#endTime").val());
            cookie.setCookie(COOKIE_PREFIX + "order_list_receiver",$("#receiver").val());//服务顾问
            cookie.setCookie(COOKIE_PREFIX + "order_list_orderType",$("#orderType").val());//业务类型
            cookie.setCookie(COOKIE_PREFIX + "order_list_customerNameLike",$("#customerNameLike").val());//车主
            cookie.setCookie(COOKIE_PREFIX + "order_list_flag",$("#flag").val());
        });

    });

    //如果值都是空得，从cookie里面取值
    seajs.use(['tab','cookie'],function(tab,cookie){
        tab.init();
        //服务顾问
        var receiver = $("#receiver").val();
        if (receiver == null || receiver=='') {
            var cookieReceiver = cookie.getCookie(COOKIE_PREFIX + "order_list_receiver");
            if (cookieReceiver != null && cookieReceiver != "" && cookieReceiver !='undefined') {
                $("#receiver").val(cookieReceiver);
            }
        }
        //业务类型
        var orderType = $("#orderType").val();
        if (orderType ==null || orderType=='') {
            var cookieOrderType = cookie.getCookie(COOKIE_PREFIX + "order_list_orderType");
            if (cookieOrderType != null && cookieOrderType != "" && cookieOrderType !='undefined') {
                $('#orderType').val(cookieOrderType);
            }
        }
        //车牌号
        var carLicenseLike = $("#carLicenseLike").val();
        if (carLicenseLike==null || carLicenseLike=='') {
            var cookieCarLicenseLike = cookie.getCookie(COOKIE_PREFIX + "order_list_carLicenseLike");
            if (cookieCarLicenseLike!=null && cookieCarLicenseLike != "" && cookieCarLicenseLike !='undefined') {
                $("#carLicenseLike").val(cookieCarLicenseLike);
            }
        }
        //手机号
        var mobileLike = $("#mobileLike").val();
        if (mobileLike==null || mobileLike=='') {
            var cookieMobileLike = cookie.getCookie(COOKIE_PREFIX + "order_list_mobileLike");
            if (cookieMobileLike!=null && cookieMobileLike != "" && cookieMobileLike !='undefined') {
                $("#mobileLike").val(cookieMobileLike);
            }
        }
        var startTime = $("#startTime").val();
        if (startTime ==null || startTime=='') {
            var cookieStartTime = cookie.getCookie(COOKIE_PREFIX + "order_list_startTime");
            if (cookieStartTime !=null && cookieStartTime != "" && cookieStartTime != 'undefined') {
                $("#startTime").val(cookieStartTime);

            }
        }
        var endTime = $("#endTime").val();
        if (endTime==null || endTime=='') {
            var cookieEndTime = cookie.getCookie(COOKIE_PREFIX + "order_list_endTime");
            if (cookieEndTime != null && cookieEndTime != "" && cookieEndTime != 'undefined') {
                $("#endTime").val(cookieEndTime);
            }
        }
        //车主
        var customerNameLike =  $("#customerNameLike").val();
        if(customerNameLike==null || customerNameLike == ''){
            var cookieCustomerName = cookie.getCookie(COOKIE_PREFIX+"order_list_customerNameLike");
            if(cookieCustomerName !=null && cookieCustomerName !='' && cookieCustomerName != 'undefined'){
                $("#customerNameLike").val(cookieCustomerName);
            }
        }

        //先从form中去flag的值,如果flag为空,再从cookie中取值
        var flag = $("#flag").val();
        if (flag == null || flag == "") {
            flag = cookie.getCookie(COOKIE_PREFIX + "order_list_flag");
        }
        if (flag != null && flag != "") {
            //<a>tab选中效果
            //flag的值等于tab中得id的某个值,就class=current,把另外的tab的选中效果去掉,再触发tab click
            var currentFlag = $('ul a[id="'+flag+'"]',".qxy_tab");
            currentFlag.addClass("current");
            currentFlag.parent().siblings().find("a").removeClass("current");
            currentFlag.trigger('click');

        }


    });


    //导出
    $(document).on('click', '.exp_btn', function () {
        var carLicenseLike = $("#carLicenseLike").val();
        var mobileLike = $("#mobileLike").val();
        var customerNameLike = $("#customerNameLike").val();
        var companyLike = $("#companyLike").val();
        var contactNameLike = $("#contactNameLike").val();
        var orderSnLike = $("#orderSnLike").val();
        var carLike = $("#carLike").val();
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
       // var workerId = $("#workerId").val();
        var receiver = $("#receiver").val();
        //var serviceCatId = $("#serviceCatId").val();
        var orderType = $("#orderType").val();
        var orderStatuss = $("#search_orderStatuss").val();
        var payStatus = $("#search_payStatus").val();
        var vin = $("#vin").val();
        window.location.href=BASE_PATH+"/shop/order/list/export?search_carLicenseLike="+carLicenseLike+"&search_startTime="+startTime+"&search_endTime="+endTime+
            "&search_receiver="+receiver+"&search_orderType="+orderType+"&search_orderStatuss="+orderStatuss+"&search_payStatus="+payStatus+
        "&search_mobileLike="+mobileLike+"&search_customerNameLike=" +customerNameLike+"&search_companyLike="+companyLike+"&search_contactNameLike="+contactNameLike+
        "&search_orderSnLike="+orderSnLike+"&search_carLike="+carLike+"&search_vin="+vin;

    });


    $(document).on('click', '.midc', function () {
        var orderId = $(this).parent().find(".orderIdHidden").val();
        window.location.href=BASE_PATH+"/shop/order/detail?orderId="+orderId;
    });
    //派工方法
    $(document).on('click','.order_tasking_btn',function () {
        var orderId = $(this).attr("order_id");
        var orderSn = $(this).attr("order_sn");
        seajs.use(["ajax","dialog"],function (ajax,dg) {
            dg.confirm("您确定要把该工单派工吗?",function(){
                ajax.post({
                    url:BASE_PATH + '/shop/order/order_track/tasking',
                    data:{
                        orderId: orderId,
                        orderSn: orderSn
                    },
                    success: function (data) {
                        if (!data.success) {
                            dg.info(data.errorMsg,5);
                            return false;
                        } else {
                            dg.info("操作成功",1,3,function(){
                                window.location.reload();
                            })
                        }
                    }
                });
            });
        });
    });

    //完工方法
    $(document).on('click','.order_finish_btn',function(){
        var orderId = $(this).attr("order_id");
        var orderSn = $(this).attr("order_sn");
        seajs.use(['ajax',"dialog"],function(ajax,dg){
            ajax.post({
                url: BASE_PATH + '/shop/order/getRealOrderGoodsListByOrderId',
                data: {
                    orderId:orderId,
                    checkFinish:true
                },
                success: function (result) {
                    if(result.success==true){

                        if(result.data.code==true) {
                            ajax.post({
                                url: BASE_PATH + '/shop/order/order_track/finish',
                                data: {
                                    orderSn: orderSn,
                                    orderId: orderId
                                },
                                success: function (data) {
                                    if (!data.success) {
                                        dg.info(data.errorMsg,5);
                                        return false;
                                    } else {
                                        dg.info("操作成功",1);
                                        window.location.reload();
                                    }
                                }
                            });
                        }else{
                            orderFunc.showDialog(orderId);
                        }
                    }else{
                        dg.info(result.errorMsg,5);
                        return false;
                    }
                }
            });
        });
    });


    var orderFunc = {
        //显示弹框
        showDialog: function(orderId){
            seajs.use(["ajax", "artTemplate", "dialog"], function (ajax, template, dg) {
                    ajax.post({
                        url: BASE_PATH + '/shop/order/getRealOrderGoodsListByOrderId?orderId='+orderId,
                        success: function (result, dialog) {
                            if (!result.success) {
                                dg.close(dialogId);
                                dialog.info(result.errorMsg, 5);
                                return;
                            } else {
                                var temp = result.data;
                                $('#warehouseGoodslist').html(template.render('moreWarehouseTpl',
                                    {'templateData': temp,'success':result.success}));
                                dg.dialog({
                                    "dom": "#warehouseGoodslist",
                                    init: function(){
                                    }
                                });
                            }
                        }
                    });
            });
        }
    }

    $(document).on("click", ".urlBtn", function (e) {
        window.location.href = $(this).data('url');
    });

});


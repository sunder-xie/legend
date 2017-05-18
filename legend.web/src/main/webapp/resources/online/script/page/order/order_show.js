/**
 * Created by jason on 15-06-04.
 */
$(document).ready(function ($) {

    //点击搜索按钮覅后把搜索条件放到cookie中
    $(".search_btn").click(function(){
        seajs.use(["cookie"],function(cookie){
            cookie.setCookie(COOKIE_PREFIX + "order_show_carLicenseLike",$("#carLicenseLike").val());//车牌号
            cookie.setCookie(COOKIE_PREFIX + "order_show_mobileLike",$("#mobileLike").val());//手机号
            cookie.setCookie(COOKIE_PREFIX + "order_show_startTime",$("#startTime").val());
            cookie.setCookie(COOKIE_PREFIX + "order_show_endTime",$("#endTime").val());
            cookie.setCookie(COOKIE_PREFIX + "order_show_receiver",$("#receiver").val());//服务顾问
            cookie.setCookie(COOKIE_PREFIX + "order_show_orderType",$("#orderType").val());//业务类型
            cookie.setCookie(COOKIE_PREFIX + "order_show_customerNameLike",$("#customerNameLike").val());//车主
        });
    });

    //从cookie中取值
    seajs.use(["cookie"],function(cookie){

        //服务顾问
        var receiver = $("#receiver").val();
        if (receiver == null || receiver == "") {
            var cookieReceiver = cookie.getCookie(COOKIE_PREFIX + "order_show_receiver");
            if (cookieReceiver != null && cookieReceiver != "" && cookieReceiver !='undefined') {
                $("#receiver").val(cookieReceiver);
            }
        }
        //业务类型
        var orderType = $("#orderType").val();
        if (orderType ==null || orderType=='') {
            var cookieOrderType = cookie.getCookie(COOKIE_PREFIX + "order_show_orderType");
            if (cookieOrderType != null && cookieOrderType != "" && cookieOrderType !='undefined') {
                $('#orderType').val(cookieOrderType);
            }
        }
        //车牌号
        var carLicenseLike = $("#carLicenseLike").val();
        if (carLicenseLike==null || carLicenseLike=='') {
            var cookieCarLicenseLike = cookie.getCookie(COOKIE_PREFIX + "order_show_carLicenseLike");
            if (cookieCarLicenseLike!=null && cookieCarLicenseLike != "" && cookieCarLicenseLike !='undefined') {
                $("#carLicenseLike").val(cookieCarLicenseLike);
            }
        }
        //手机号
        var mobileLike = $("#mobileLike").val();
        if (mobileLike==null || mobileLike=='') {
            var cookieMobileLike = cookie.getCookie(COOKIE_PREFIX + "order_show_mobileLike");
            if (cookieMobileLike!=null && cookieMobileLike != "" && cookieMobileLike !='undefined') {
                $("#mobileLike").val(cookieMobileLike);
            }
        }
        var startTime = $("#startTime").val();
        if (startTime ==null || startTime=='') {
            var cookieStartTime = cookie.getCookie(COOKIE_PREFIX + "order_show_startTime");
            if (cookieStartTime !=null && cookieStartTime != "" && cookieStartTime != 'undefined') {
                $("#startTime").val(cookieStartTime);

            }
        }
        var endTime = $("#endTime").val();
        if (endTime==null || endTime=='') {
            var cookieEndTime = cookie.getCookie(COOKIE_PREFIX + "order_show_endTime");
            if (cookieEndTime != null && cookieEndTime != "" && cookieEndTime != 'undefined') {
                $("#endTime").val(cookieEndTime);
            }
        }
        //车主
        var customerNameLike =  $("#customerNameLike").val();
        if(customerNameLike==null || customerNameLike == ''){
            var cookieCustomerName = cookie.getCookie(COOKIE_PREFIX+"order_show_customerNameLike");
            if(cookieCustomerName !=null && cookieCustomerName !='' && cookieCustomerName != 'undefined'){
                $("#customerNameLike").val(cookieCustomerName);
            }
        }
    });












});


$(document).ready(function ($) {


    $("li").click(function () {
        $(this).siblings().removeClass("checked");
        $(this).addClass("checked");
    });


    $(".bank_pay").click(function () {
        seajs.use(["ajax", "dialog"], function (ajax, dg) {
            var smsTplId = $("li.checked").data('sms_tpl_id');
            dg.info("如需充值,请线下联系客服");
            return false;
            /**
            if (smsTplId == undefined) {
                dg.info("请选择充值金额");
                return false;
            }else{
                ajax.post({
                    url: BASE_PATH + '/shop/marketing/sms/recharge/create_marketing_order?smsTplId='+smsTplId,
                    success: function (result, dialog) {
                        if (!result.success) {
                            dialog.info(result.errorMsg, 5);
                            return;
                        } else {
                           window.location.href= BASE_PATH + "/shop/lian_pay/ng?orderSn="+result.data;
                        }
                    }
                });
            }
             */
        });
    });


});
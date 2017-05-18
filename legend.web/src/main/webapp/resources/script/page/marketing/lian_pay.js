$(document).ready(function ($) {

    $(document).on('click', ".zyy-bank-item", function () {
        $(".zyy-bank-item").removeClass('zyy-bank-item-active');
        $(this).addClass('zyy-bank-item-active');
    });

    $(".confirmToPay").click(function () {
        seajs.use(["ajax", "dialog"], function (ajax, dg) {
            var orderSn = $("#orderSn").val();

            var bank_length = $(".zyy-bank-info").length;
            if(bank_length==0){
                window.location.href= BASE_PATH + "/shop/lian_pay/bound_card/ng?orderSn="+orderSn;
            }
            var hasErrorTag = $('.zyy-bank-item-active').length;
            if (hasErrorTag != 1) {
                dg.info("请选择银行卡");
                return false;
            }
            var noAgree = $(".noAgree").val();

            ajax.get({
                url: BASE_PATH + '/shop/lian_pay/doPay',
                data: {
                    tradeSn: orderSn,
                    noAgree: noAgree
                },
                success: function (result, dialog) {
                    if (result.success) {
                        $('#resultForm').html(result.data);
                    } else {
                        dialog.info(result.errorMsg, 5);
                    }
                }
            });

        });

    });
});
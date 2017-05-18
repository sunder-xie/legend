$(document).ready(function ($) {
    $(document).on('click',".J-bank-item", function () {
        $(".J-bank-item").removeClass('zyy-bank-item-active');
        $(this).addClass('zyy-bank-item-active');
    });


    $("#bankAccount").blur(function () {
        var bankCode = $(".zyy-bank-item-active").attr("rel");
        var bankAccount = $("#bankAccount").val();
        seajs.use(["ajax", "dialog"], function (ajax, dg) {

            ajax.get({
                url: BASE_PATH + '/shop/lian_pay/card/bin',
                data:{
                    bankCode:bankCode,
                    bankAccount :bankAccount
                },
                success: function (result, dialog) {
                    if (!result.success) {
                        if (bankAccount != result.data.bankCode) {
                            dialog.info('该卡号开户行不属于该银行！', 5);
                        }
                    } else {
                        dialog.info('您输入的卡号错误！', 5);
                    }
                }
            });

        });
    });


    $(".zyy-bank-bind-btn").click(function () {
        var hasErrorTag = $('.zyy-bank-item-active').length;
        if (hasErrorTag == 0) {

        }
        var orderId = $("#orderId").val();
        var bankAccount = $("#bankAccount").val();
        var accountName = $("#accountName").val();
        var idCard = $("#idCard").val();

    });
});
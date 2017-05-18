$(document).ready(function ($) {
    $(document).on('click', ".J-bank-item", function () {
        $(".zyy-bank-item").removeClass('zyy-bank-item-active');
        $(this).addClass('zyy-bank-item-active');
    });


    $("#bankAccount").blur(function () {
        var bankCode = $(".zyy-bank-item-active").attr("rel");
        var cardNo = $("#bankAccount").val();
        if(cardNo==''){
            return false;
        }
        seajs.use(["ajax", "dialog"], function (ajax, dg) {

            ajax.get({
                url: BASE_PATH + '/shop/lian_pay/card/bin',
                data: {
                    cardNo:cardNo
                },
                success: function (result, dialog) {
                    if (result.success) {
                        if (bankCode != result.data.bankCode) {
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
        seajs.use(["ajax", "dialog"], function (ajax, dg) {

            var hasErrorTag = $('.zyy-bank-item-active').length;
            if (hasErrorTag != 1) {
                dg.info("请选择银行卡");
                return false;
            }
            var orderSn = $("#orderSn").val();
            if(orderSn == ''){
                dg.info("请输入订单号");
                return false;
            }
            var cardNo = $("#bankAccount").val();
            if(cardNo ==''){
                dg.info("请输入银行卡号");
                return false;
            }
            var acctName = $("#accountName").val();
            if(acctName ==''){
                dg.info("请输入银行卡姓名");
                return false;
            }
            var idNo = $("#idCard").val();
            if(idNo ==''){
                dg.info("请输入身份证号码");
                return false;
            }

            ajax.get({
                url: BASE_PATH + '/shop/lian_pay/doPay',
                data: {
                    tradeSn: orderSn,
                    cardNo: cardNo,
                    acctName: acctName,
                    idNo: idNo
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
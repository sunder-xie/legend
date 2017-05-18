/**
 * Created by huage on 16/9/26.
 */
$(function () {
    addHeadStyle(5);
    $(document).on('click','.js-pay',function () {
        var prePayFee = $(this).data("prePayFee");
        var orderSn = $(this).data("orderSn");
        window.location.href = BASE_PATH + '/insurance/anxin/virtual/flow/card-select?totalFee='+ prePayFee +'&orderSn='+ orderSn;
    })
});

/**
 * Created by huage on 2017/3/9.
 */
$(function () {
    addHeadStyle(5);
    var sn = $('#sn').val();
    $(document).on('click','.go-recharge',function () {
        $('#openPay').attr('href',BASE_PATH + '/insurance/anxin/pay/ali?orderSn='+sn);
        })
});
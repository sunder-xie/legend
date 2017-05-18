/**
 * Created by huage on 2017/3/8.
 */
$(function () {
    addHeadStyle(5);
    var sn = util.getPara('sn');
    $(document).on('click','.go-pay',function () {
        $('#openPay').attr('href',BASE_PATH + '/insurance/anxin/pay/ali?orderSn='+sn);
    })
});
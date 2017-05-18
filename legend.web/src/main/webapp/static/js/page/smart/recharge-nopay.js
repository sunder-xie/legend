/**
 * Created by huage on 2016/12/22.
 */
$(function () {
    seajs.use('dialog',function (dg) {
        /*=====进页面加载的事件 开始====*/
        addHeadStyle(2);
        var $doc = $(document),
            NumRecharge = $('#recharge-number').val(),
            feeRecharge = $('#recharge-fee').val();
        // 点击事件
        init_click();
        /*=====进页面加载的事件 结束====*/

        /*=====点击事件 开始====*/
        function init_click() {
            $doc.on('click','.go-recharge',function () {
                setTimeout(dg.confirm('是否完成支付', function () {
                    isConfirm(NumRecharge);
                }, function () {
                    isConfirm(NumRecharge);
                }, ['是', '否']),1000);
                window.open(BASE_PATH + '/smart/bihu/recharge/alipay?payFee=' + feeRecharge + '&rechargeNumber=' + NumRecharge)
            });
            $doc.on('click','.go-record',function () {
                window.location.href = BASE_PATH + '/smart/bihu/usedView/recharge-list';
            });
        }
        /*=====点击事件 结束====*/

        /*===========是否支付完成的确认框 开始============*/
        function  isConfirm(NumRecharge) {
            window.location.href = BASE_PATH + '/smart/bihu/recharge/judge-go?rechargeNumber='+ NumRecharge;
        }
        /*===========是否支付完成的确认框 结束============*/

    })
});

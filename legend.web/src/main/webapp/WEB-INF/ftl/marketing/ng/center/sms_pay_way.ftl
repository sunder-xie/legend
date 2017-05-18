<#include "layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/resources/css/marketing/sms_pay_way.css?ebd7cba52a0c94f5ca8453dcbfa112a5"/>
<div class="wrapper">
<#include "marketing/ng/left_nav.ftl"/>
    <div class="Z-right">
        <h3 class="Z-title">
                客户营销 > <a href="${BASE_PATH}/marketing/ng/center/sms">短信充值</a> > <i>核对订单信息</i>
        </h3>
    <#if chargeTpl?exists>
        <div class="order-info">
            <p class="order-money">订单金额: <span>${chargeTpl.rechargeMoney}</span>元</p>
            <p class="order-infor">订单详细： <span>充值${chargeTpl.buySmsNum}条再送${chargeTpl.freeSmsNum}条短信</span></p>
        </div>
        <div class="pay-way">
            <form action="${BASE_PATH}/shop/onlinepay/pay" method="post">
            <div class="pay-mode-box">
                <h1>请选择支付方式:</h1>
                <input type="hidden" name="payFee" value="${chargeTpl.rechargeMoney}">
                <input type="hidden" name="payFee" value="${chargeTpl.rechargeMoney}">
                <input type="hidden" name="smsNum" value="${chargeTpl.buySmsNum + chargeTpl.freeSmsNum}">
                <input type="hidden" name="payWay" value="zhifubao"/>
                <div class="mode-box">
                    <div class="pay-mode hover" data-pay-way="zhifubao"><img src="${BASE_PATH}/resources/images/marketing/pay-mode1.png">支付宝支付</div>
                    <#--<div class="pay-mode" data-pay-way="lianlian"><img src="${BASE_PATH}/resources/images/marketing/pay-mode2.png">银行卡支付</div>-->
                </div>
            </div>
            <div class="pay-btn-box">
                    <input type="submit" class="pay-btn js-pay-btn" value="下一步 > "/>
            </div>
            </form>
        </div>
        <#else>
        <h1>系统错误!</h1>
    </#if>
    </div>
</div>

<#include "layout/footer.ftl" >
<script>
    jQuery(function($) {

        var $document = $(document);


        $document
                //选择支付方式
                .on('click', '.mode-box div', function() {
                    var $this = $(this),
                            payWay = $this.data('payWay');
                    $this.addClass("hover").siblings().removeClass("hover");

                        $('[name=payWay]').val(payWay);

                })

    });

</script>
<#--充值等待页面-->
<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/smart/recharge-wait.css?caf429f8b75836de3d68e4026c76a286"
      xmlns="http://java.sun.com/jsf/facelets"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="right">
        <div class="process-nav-true">
        <#include "yqx/page/smart/recharge-process-nav.ftl">
        </div>
    <#--支付中页面 开始-->
        <input type="hidden" id="state" value="${recordResult.billStatus}">
        <input type="hidden" id="recharge-number" value="${recordResult.rechargeNumber}">
        <div class="pay-ing-box">
            <p class="big-title">支付等待</p>
            <div class="enter clearfix">
                <p class="wait">正在支付，请耐心等待...</p>
                <div class="wait-pic-box">
                    <ul class="process-box clearfix">
                        <li class="process-icon process-paying"></li>
                        <li class="line line-bgColor-green"></li>
                        <li>
                            <div class="process-icon aliPay-receipt"></div>
                            <div class="process-icon-word">收</div>
                        </li>
                        <li class="line line-bgColor-gray"></li>
                        <li>
                            <div class="process-icon aliPay-receipt-gray"></div>
                            <div class="process-icon-word">验</div>

                        </li>
                        <li class="line line-bgColor-gray"></li>
                        <li class="process-icon payState-unExecute"></li>
                    </ul>
                    <ul class="process-box clearfix">
                        <li>支付中</li>
                        <li class="process-icon-one">支付宝已收款</li>
                        <li class="process-icon-two">收款校验成功</li>
                        <li class="process-icon-three">充值成功</li>
                    </ul>

                </div>
            </div>
        </div>
    <#--支付中页面 结束-->
    </div>
</div>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart-ajax.js?5c4be91860d3a836366b110367358251"></script>
<script src="${BASE_PATH}/static/js/page/smart/recharge-wait.js?504fd409fc9f4f3c3b9c40a2d50a5fdb"></script>
<#include "yqx/layout/footer.ftl">

<!--充值失败页面-->
<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/smart/recharge-fail.css?d0230f637bbe76832d468f7cb9b82006"
      xmlns="http://java.sun.com/jsf/facelets"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="right">
        <div class="process-nav-true">
        <#include "yqx/page/smart/recharge-process-nav.ftl">
        </div>
        <input type="hidden" id="recharge-number" value="${recordResult.rechargeNumber}">
    <#--支付失败页面 开始-->
        <div class="pay-fail-box">
            <p class="big-title">支付失败</p>
            <div class="enter clearfix">
                <div class="left-box">
                    <div class="fail-icon"></div>
                    <span class="fail-word">抱歉，充值失败了!</span>
                    <p>本次充值未能成功，未收到支付金额</p>
                    <button class="go-record">充值记录</button>
                    <button class="go-recharge">重新充值</button>
                </div>
                <div class="right-box">
                    <div class="fail-img"></div>
                </div>
            </div>
        </div>
    <#--支付失败页面 结束-->
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/smart/recharge-fail.js?89008d31ff7f19b396fa6fe141fb906a"></script>
<script src="${BASE_PATH}/static/js/page/smart/common/smart.js?4abf167c5df3e2fa7854a453b2cec085"></script>
<#include "yqx/layout/footer.ftl">
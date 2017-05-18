<!--充值成功页面-->
<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/ax_insurance/create/payResult.css?acb9072fe92acb7e15653e6c5c8459bc"
      xmlns="http://java.sun.com/jsf/facelets"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="right">
        <div class="process-nav-true">
        <#include "yqx/page/ax_insurance/process-nav.ftl">
        </div>
    <#--支付成功页面 开始-->
        <div class="pay-success-box">
            <p class="big-title">支付成功</p>
            <div class="enter clearfix">
                <div class="left-box">
                    <div class="success-icon"></div>
                    <span class="congratulate-word">恭喜您，支付成功!</span>
                    <#if insuranceFeeDTO && insuranceFeeDTO.insuredTotalFee>
                        <p>应付金额：<span class="display-color">${insuranceFeeDTO.insuredTotalFee?string('0.00')}</span>元</p>
                        <p>实付金额：<span class="display-color">${insuranceFeeDTO.insuredTotalFee?string('0.00')}</span>元</p>
                    </#if>
                    <button class="go-insure mg-left">继续投保</button>
                </div>
                <div class="right-box">
                    <div class="success-img"></div>
                    <p class="success_tips">提示车主扫码下载APP查看服务包</p>
                </div>
            </div>
        </div>
    <#--支付中页面 结束-->
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/ax_insurance/create/paySuccess.js?13d7a972d097aca3551608a8c5c82330"></script>
<#include "yqx/layout/footer.ftl">
<!--充值失败页面-->
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
        <input type="hidden" id="sn" value="${orderSn}">
    <#--支付失败页面 开始-->
        <div class="pay-fail-box">
            <p class="big-title">支付失败</p>
            <div class="enter clearfix">
                <div class="left-box">
                    <div class="fail-icon"></div>
                    <span class="fail-word">抱歉，您的投保单支付失败!</span>
                    <p>请检查网络是否正常/是否中途关闭支付页面</p>
                    <a id="openPay" target="_self" ><button class="go-recharge mg-left">重新支付</button></a>
                </div>
            </div>
        </div>
    <#--支付失败页面 结束-->
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/ax_insurance/create/payFail.js?7f1fee8e96091e9260f540a81e309da0"></script>
<#include "yqx/layout/footer.ftl">
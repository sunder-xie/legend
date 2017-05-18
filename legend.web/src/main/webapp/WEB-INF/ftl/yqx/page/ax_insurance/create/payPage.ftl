<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/ax_insurance/create/payPage.css?99e7a13d60b54219681ebb143f7fdc7e"
      xmlns="http://java.sun.com/jsf/facelets"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="right">
        <div class="process-nav-true">
        <#include "yqx/page/ax_insurance/process-nav.ftl">
        </div>
    <#--选择支付方式 开始-->
        <div class="choose-payWay">
            <p class="big-title">选择支付方式</p>
            <div class="enter clearfix">
                <div class="enter_info">
                    <p class="small-title pay-method">支付方式</p>
                    <div class="pay-way">
                        <input type="checkbox" checked disabled>
                        <img src="${BASE_PATH}/static/img/page/ax_insurance/alipay.png">
                        <span class="payWay-name">支付宝</span>
                    </div>
                </div>
                    <p class="must-pay">应付金额：<span class="must-pay-color"><i class="must-pay-money">
                    <#if insuranceFeeDTO && insuranceFeeDTO.insuredTotalFee>
                    ${insuranceFeeDTO.insuredTotalFee?string("0.00")}
                    <#else>
                        0.00
                    </#if>
                    </i> 元</span></p>
                <a id="openPay" target="_self" ><span class="go-pay">去支付</span></a>
            </div>
        </div>
    <#--选择支付方式 结束-->
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/ax_insurance/create/payPage.js?23be509de81d3550da9f5dfaf57aadf4"></script>
<#include "yqx/layout/footer.ftl">

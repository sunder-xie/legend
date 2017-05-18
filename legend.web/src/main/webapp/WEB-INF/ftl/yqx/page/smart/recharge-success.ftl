<!--充值成功页面-->
<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/smart/recharge-success.css?115d4e16b7bc64a18926befbe122ca36"
      xmlns="http://java.sun.com/jsf/facelets"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="right">
        <div class="process-nav-true">
        <#include "yqx/page/smart/recharge-process-nav.ftl">
        </div>
    <#--支付成功页面 开始-->
        <div class="pay-success-box">
            <p class="big-title">支付成功</p>
            <div class="enter clearfix">
                <div class="left-box">
                    <div class="success-icon"></div>
                    <span class="congratulate-word">恭喜您，充值成功!</span>
                    <#assign vo = recordResult>
                    <p>本次充值金额：<span class="display-color">${vo.payFee}</span></p>
                    <p>本次充值次数：<span class="display-color"><i class="recharge-times">${vo.rechargeNum}</i></span>次</p>
                    <p>剩余可用次数：<span class="display-color"><i class="remain-times">${shopAllNum}</i></span>次</p>
                    <button class="go-record">充值记录</button>
                    <button class="go-insure">返回投保</button>
                </div>
                <div class="right-box">
                    <div class="success-img"></div>
                </div>
            </div>
        </div>
    <#--支付中页面 结束-->
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/smart/recharge-success.js?59fef697f0e8761a3fae798e143781ff"></script>
<#include "yqx/layout/footer.ftl">
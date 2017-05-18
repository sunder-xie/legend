<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/smart/recharge-scheme.css?b2a984b0c5f00119215c87c3fc1b60c6"
      xmlns="http://java.sun.com/jsf/facelets"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="right">
        <div class="process-nav-true">
        <#include "yqx/page/smart/recharge-process-nav.ftl">
        </div>
    <#--选择充值方案开始-->
        <div class="recharge-box dis">
            <p class="big-title">选择充值方案</p>
            <div class="enter clearfix">
                <div class="enter_info">
                    <p class="remain-box">您剩余可用次数: <span class="remain-number">0</span> 次</p>
                    <p class="small-title">快速充值</p>
                    <ul class="fast-recharge clearfix">

                    </ul>
                    <#if rechargeRuleTimes>
                    <p class="recharge-rule">基准资费：1元=#{rechargeRuleTimes}次</p>
                    </#if>
                    <#--自定义充值先隐藏 开始-->
                    <p class="small-title dis">自定义充值 <span class="minimum">(<i class="minimumFee">${minimumFee}</i>元起充)</span></p>
                    <input class="input-money dis" placeholder="请输入充值金额"  type="number">
                    <span class="units dis">元</span>
                    <#--自定义充值先隐藏 结束-->
                </div>
                <button class="js-search-button">提交订单</button>
                <button class="back-smart-index">返回</button>
            </div>
        </div>
    <#--选择充值方案结束-->

    <#--选择支付方式 开始-->
        <div class="choose-payWay dis">
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
                <p class="must-pay">应付金额：<span class="must-pay-color"><i class="must-pay-money">0.00</i> 元</span></p>
                <a id="openPay" target="_blank"><span class="go-pay">去支付</span></a>
                <button class="back-recharge">返回</button>
            </div>
        </div>
    <#--选择支付方式 结束-->
    </div>
</div>
<#--快速充值页面模版 开始-->
<script id="fastRechargeList" type="text/html">
    <%if(success && data){%>
    <%for(var i = 0;i < data.length;i++){%>
    <%var data_i = data[i]%>
    <li class="fast-recharge-li <%if(i==0){%>li-checked<%}%>" data-id="<%=data_i.id%>" data-fee="<%=data_i.rechargeFeeNumber%>">
        <p class="fast-recharge-money">
            <span class="rechargeFee"><%=data_i.rechargeFeeNumber%>元</span>
            <%if(data_i.rechargeType && data_i.rechargeType == 3){%>
            <span class="rechargeType">优惠</span>
            <%}%>
        </p>

        <p class="fast-recharge-remain <%if(data_i.rechargeType && data_i.rechargeType == 3){%> change_location <%}%>">(<%=data[i].effectiveNum%>次)</p>
    </li>
    <%}%>
    <%}%>
</script>
<#--快速充值页面模版 结束-->
<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart-ajax.js?5c4be91860d3a836366b110367358251"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart.js?4abf167c5df3e2fa7854a453b2cec085"></script>
<script src="${BASE_PATH}/static/js/page/smart/recharge-scheme.js?170ab16a10251e5e038c1ed20374db6b"></script>
<#include "yqx/layout/footer.ftl">
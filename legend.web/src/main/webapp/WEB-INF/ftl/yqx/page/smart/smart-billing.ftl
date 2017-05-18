<#--智能投保 开始-->
<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/smart/smart-billing.css?024bc83bda433fb7470c41b10da45cd0"
      xmlns="http://java.sun.com/jsf/facelets"/>
<link href="${BASE_PATH}/static/css/page/ax_insurance/create/firstepInsure.css?d554cad1bad1c9c17d0a2e2968ce2307" type="text/css" rel="stylesheet">
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="right">
    <#--智能开单头部-->
        <div class="smart-header-box clearfix">
            <div class="img"></div>
            <div class="smart-header-left">
                <span class="smart-word">智能开单</span>
                <span class="span-color">输入车牌号，系统自动返回车辆信息、去年险种信息</span>
            </div>
            <div class="smart-header-right">
                <label class="subsidy-rules">结算规则<i class="pop-tips js-show-tips" data-tips="1、车牌号首次查询且成功，扣次数<br>
2、同份保单周期内，同个车牌号重复查询，不扣次数<br>
3、查询不成功，不扣次数">?</i>
                </label>
                <span class="remain-time-box">剩余可用次数：<i class="remain-time">0</i></span>
                <a class="want-used-list">使用记录</a>
                <#--<button class="want-recharge">我要充值</button>-->
            </div>
        </div>
    <#--智能开单流程指向-->
        <div class="process-nav-true dis">
        <#include "yqx/page/ax_insurance/process-nav.ftl">
        </div>
        <div class="process-nav-virtual dis">
        <#include "yqx/page/ax_insurance/virtual-process-nav.ftl">
        </div>
    <#--第一步样式-->
        <input class="isHaveID" type="hidden" value="<#if insuranceBasic> ${insuranceBasic.id}</#if>">
        <div class="bInfo">
            <p class="big-title clearfix">输入基本信息
                <span class="smart-mask"></span>
            </p>
            <div class="enter">
            <#include "yqx/page/ax_insurance/create/firstepInsure.ftl">
                <div class="js-search-button">智能查询</div>
                <p class="time-tips">注：每日<span class="time-tips-red">23:30至隔天7:00</span>之间数据升级，无法使用智能开单，请知晓！</p>
            </div>
        </div>
    </div>
    <!--第一步样式结束-->
</div>

<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart-ajax.js?5c4be91860d3a836366b110367358251"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart.js?4abf167c5df3e2fa7854a453b2cec085"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/smart-billing.js?f062923771a92932d934cd531db654d2"></script>
<#include "yqx/layout/footer.ftl">
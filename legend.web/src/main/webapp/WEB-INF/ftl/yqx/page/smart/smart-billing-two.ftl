<#--智能投保 开始-->
<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/create/vehicle-type-choose.css?e78a504b45d569f9fc7185ce55c8680c"
      xmlns="http://java.sun.com/jsf/facelets">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/create/second-step-insure.css?cdf4212558793375c2ed98706e5ec850"
      xmlns="http://java.sun.com/jsf/facelets"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/create/three-step-insure.css?e2d5d8a56e385d798098d88323c6cdcc"
      xmlns="http://java.sun.com/jsf/facelets"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/smart/smart-billing.css?024bc83bda433fb7470c41b10da45cd0"
      xmlns="http://java.sun.com/jsf/facelets"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="right">
    <#--智能开单流程指向-->
        <div class="process-nav-true dis">
        <#include "yqx/page/ax_insurance/process-nav.ftl">
        </div>
        <div class="process-nav-virtual dis">
        <#include "yqx/page/ax_insurance/virtual-process-nav.ftl">
        </div>
        <input class="isHaveID" type="hidden" value="<#if insuranceBasic.id> ${insuranceBasic.id}</#if>">
    <#--第二步页面-->
        <div class="carInfo">
            <p class="big-title">填写车辆信息
                <span class="smart-mask"></span>
            </p>
        <#--第一次时间戳-->
        <#include "yqx/page/ax_insurance/create/second-step-insure.ftl">
            <input type="hidden" name="firstTimestamp" class="firstTimestamp">
        </div>
    <#--第三步页面-->
        <div class="chooseScheme">
            <p class="choose">选择投保方案
                <span class="smart-mask"></span>
            </p>
        <#include "yqx/page/ax_insurance/create/three-step-insure.ftl">
        </div>
    </div>
</div>
<!--车辆弹出框-->
<div class="mask dis"></div>
<#--车型查询的模版-->
<#include "yqx/page/ax_insurance/create/vehicle-type-choose.ftl">
<#--投保选择方案-->
<script type="text/html" id="scheme-choose">

</script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart-ajax.js?5c4be91860d3a836366b110367358251"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart.js?4abf167c5df3e2fa7854a453b2cec085"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/ax_insurance/create/second-step-insure.js?a1a86f8cdd85a24b5b0289be5f4a11e8"></script>
<#include "yqx/layout/footer.ftl">
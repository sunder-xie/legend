<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/create/insureBaseInfo.css?0a8f5879095e9eb130d0a9f1a2461ef5"
      xmlns="http://java.sun.com/jsf/facelets"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/create/vehicle-type-choose.css?e78a504b45d569f9fc7185ce55c8680c"
      xmlns="http://java.sun.com/jsf/facelets">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/create/second-step-insure.css?cdf4212558793375c2ed98706e5ec850"
      xmlns="http://java.sun.com/jsf/facelets"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/create/three-step-insure.css?e2d5d8a56e385d798098d88323c6cdcc"
      xmlns="http://java.sun.com/jsf/facelets"/>
<link href="${BASE_PATH}/static/css/page/ax_insurance/create/firstepInsure.css?d554cad1bad1c9c17d0a2e2968ce2307" type="text/css" rel="stylesheet">
<div class="wrapper clearfix">
    <div class="content clearfix">
    <#include "yqx/page/ax_insurance/left-nav.ftl">
        <div class="right">
            <div class="process-nav-true dis">
            <#include "yqx/page/ax_insurance/process-nav.ftl">
            </div>
            <div class="process-nav-virtual dis">
            <#include "yqx/page/ax_insurance/virtual-process-nav.ftl">
            </div>

        <#--第一步-->
            <input class="isHaveID" type="hidden" value="<#if insuranceBasic.id> ${insuranceBasic.id}</#if>">
            <div class="bInfo">
                <p class="big-title">输入基本信息</p>
                <div class="enter">
                    <#include "yqx/page/ax_insurance/create/firstepInsure.ftl">
                    <div class="n-button">下一步</div>
                </div>
            </div>
            <#--第二步第三步合集-->
            <div class="second-third-mix">
            <#include "yqx/page/ax_insurance/create/second-three-mix.ftl">
            </div>
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
<script src="${BASE_PATH}/static/js/page/ax_insurance/create/insureBaseInfo.js?e344c7be386d5a8d3875186d9a3e5c36"></script>
<#include "yqx/layout/footer.ftl">
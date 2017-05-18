<#include "yqx/tpl/home-header.ftl">
<#--生态页-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/home/home.css?b05f4c082e8f0fce691da818b316690e">
<body>
<div class="yqx-wrapper">
<#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
    <#include "yqx/page/home/entry-yunxiu.ftl">
<#else>
    <#include "yqx/page/home/entry-tqmall.ftl">
</#if>
    <#include "yqx/page/home/home-main.ftl">
</div>
</body>
<script src="${BASE_PATH}/static/js/page/home/home.js?2824720552cc1f89868ca63083387305"></script>
<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/print-setting.css?5be1132eafe9a7359f3adcebeca9a863">
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#if refer>
        <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    <#else>
        <ul class="aside-nav">
            <li class="aside-nav-root">
                功能配置
            </li>
            <li class="aside-nav-list">
                <dl>
                    <dd><a href="/legend/shop/print-config" class="current">打印设置</a></dd>
                </dl>
            </li>
        </ul>
    </#if>
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline">打印设置
                <button style="margin-top: 10px;"
                        class="yqx-btn yqx-btn-1 js-back fr">返回
                </button>
            </h1>
        </div>
        <div class="container">
            <h2 class="title-container">选择需要打印的单据</h2>
            <p style="margin-bottom: 19px;">单据开启后即可打印，使用三联纸打印需要在系统中先设置好纸张大小，<span class="print-guide js-print-guide">查看操作说明</span>
                <#if SESSION_SHOP_LEVEL == 10 || SESSION_SHOP_LEVEL == 11 || SESSION_SHOP_LEVEL == 12>
                <#else>
                    <button class="yqx-btn yqx-btn-1 js-change-version change-version hide"></button>
                </#if>
            </p>
        <#list shopPrintConfigs as item>
            <div class="choose-box" data-print-template="${item.printTemplate}">
                <h3 class="title">${item.printConfigName}</h3>
                <p class="description"
                   <#if item.printTemplate == 5>style="margin-left: 34px;"</#if>>
                    <#if item.printTemplate == 1>
                        工单确认后施工时打印
                    </#if>
                    <#if item.printTemplate == 2>
                        结算确认后打印
                    </#if>
                    <#if item.printTemplate == 3>
                        工单已报价给客户确认时打印
                    </#if>
                    <#if item.printTemplate == 4>
                        维修完毕后开出检验时打印
                    </#if>
                    <#if item.printTemplate == 5>
                        结算，充值确认后打印，<span class="js-guide receipt-guide">查看打印教程</span>
                    </#if>
                </p>
                <#if item.openStatus == 0>
                    <div class="status-box js-edit">
                        <div class="white-cube"></div>
                        <i>未开启</i>
                    </div>
                </#if>
                <#if item.openStatus == 1>
                    <div class="status-box opened js-edit">
                        <div class="white-cube"></div>
                        <i>已开启</i>
                    </div>
                </#if>
                <#if item.printTemplate != 5>
                    <a class="btn-edit"
                       href="${BASE_PATH}/shop/print-config/print-setting-detail?printTemplate=${item.printTemplate}">编辑</a>
                </#if>
            </div>
        </#list>
        </div>
    </div>
</div>

<#--小票打印教程-->
<#include "yqx/tpl/print/receipt-guide-tpl.ftl">
<script src="${BASE_PATH}/static/js/page/setting/print-setting.js?5ae8c733cda8fcab79199624957c873f"></script>
<#include "yqx/layout/footer.ftl">
<!-- 采购金 start -->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/buy/common.css">
<style>
    .banner-panel {
        margin-bottom: 10px;
    }

    .fl {
        float: left;
    }

    .fr {
        float: right;
    }
</style>
<section class="banner-panel">
<#include "yqx/tpl/buy/banner.ftl">
</section>
<!-- 采购金 end -->
<ul class="gd_tab">
    <li class="item"><a <#if buyTab == "order_list">class="current" </#if> href="${BASE_PATH}/shop/buy">采购订单</a></li>
    <li class="item"><a
    <#if buyTab == "purchase_detail">class="current" </#if>href="${BASE_PATH}/shop/yunxiu/purchase/detail">采购金明细</a>
    </li>
    <li class="item"><a <#if buyTab == "short_goods">class="current" </#if> href="${BASE_PATH}/shop/buy/short_goods">缺件配件</a></li>
</ul>

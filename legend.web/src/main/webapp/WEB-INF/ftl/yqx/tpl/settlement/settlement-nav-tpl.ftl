<#-- 
    结算页面 通用左侧菜单
    sky 2016-06-01
    
    用到的页面：接车维修下的 all
 -->
<ul class="aside-nav" data-tpl-ref="settlement-nav-tpl">
    <li class="aside-nav-root">
        收款
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/settlement/debit/order-list">工单收款</a></dd>
            <!-- 档口店不显示引流活动对账单 -->
            <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false' && SESSION_SHOP_LEVEL?number != 10 && SESSION_SHOP_LEVEL?number != 11 && SESSION_SHOP_LEVEL?number != 12>
                <dd><a href="${BASE_PATH}/shop/settlement/activity">引流活动对账单</a></dd>
            </#if>
            <dd><a href="${BASE_PATH}/shop/settlement/debit/bill-add">新建收款单</a></dd>
            <dd><a href="${BASE_PATH}/shop/settlement/debit/flow-list">收款流水记录</a></dd>
        <#if SESSION_SHOP_JOIN_STATUS ==1 || BPSHARE == 'true'>
            <dd><a href="${BASE_PATH}/proxy/settlement/index">委托对账单</a></dd>
        </#if>
        </dl>
    </li>
    <li class="aside-nav-root">
        付款
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/settlement/pay/pay-supplier">供应商付款</a></dd>
            <dd><a href="${BASE_PATH}/shop/settlement/pay/pay-add">新建付款单</a></dd>
            <dd><a href="${BASE_PATH}/shop/settlement/pay/pay-flow">付款流水记录</a></dd>
        </dl>
    </li>
    <#if SESSION_USER_IS_ADMIN == 1>
    <li class="aside-nav-root">
        在线支付
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/settlement/online/online-payment">微信支付</a></dd>
        </dl>
    </li>
    </#if>
</ul>
<script>
    //菜单回显逻辑
    /**
     * sky 2016-06-01
     * xxxxxx是页面名字,不带ftl,对应菜单每一项
     * yyyyyy是当前菜单子页面的名字。
     */
    function getAsideNav() {
        return [
            //收款
            {
                "url": [
                    {"settlement/debit/order-list": [
                        "settlement/debit/order-detail",
                        "settlement/debit/order-debit",
                        "settlement/debit/batch-list",
                        "settlement/debit/batch-debit",
                        "settlement/debit/speedily-confirm-bill",
                        "settlement/debit/confirm-bill"]
                    },
                    <!-- 档口店不显示综合维修单 -->
                    <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false' && SESSION_SHOP_LEVEL?number != 10 && SESSION_SHOP_LEVEL?number != 11 && SESSION_SHOP_LEVEL?number != 12>
                    //引流活动
                    {"settlement/activity": []},
                    </#if>
                    //新建收款单
                    {"settlement/debit/bill-add":[]},
                    //收款流水记录
                    {"settlement/debit/flow-list":["settlement/debit/detail"]}
                    <#if SESSION_SHOP_JOIN_STATUS ==1 || BPSHARE == 'true'>
                    //委托对账单
                    ,{"proxy/settlement/index":[]}
                    </#if>
                ]
            },
            //付款
            {
                "url": [
                     //供应商付款
                    {"settlement/pay/pay-supplier": []},
                    //新建付款单
                    {"settlement/pay/pay-add": []},
                    //付款单流水
                    {"settlement/pay/pay-flow": []}
                ]
            },
            //在线支付
            {
                "url": [
                    //在线申请支付
                    {"settlement/online/online-payment": []}
                ]
            }
        ];
    }
</script>
<#include "yqx/tpl/common/aside-nav-tpl.ftl">
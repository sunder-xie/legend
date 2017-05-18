<#--
    工单页面 通用左侧菜单
    ch 2016-04-01
    
    用到的页面：接车维修下的 all
 -->
<ul class="aside-nav" data-tpl-ref="order-nav-tpl">
    <li class="aside-nav-root">
        客户预约
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/appoint/appoint-list">预约单查询</a></dd>
            <dd><a href="${BASE_PATH}/shop/appoint/appoint-edit">新建预约单</a></dd>
        </dl>
    </li>
    <li class="aside-nav-root">
        车辆接待
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/customer/list">车辆查询</a></dd>
            <dd><a href="${BASE_PATH}/shop/customer/edit">新建车辆</a></dd>
            <dd><a href="${BASE_PATH}/shop/precheck/precheck">新建预检单</a></dd>
        </dl>
    </li>
    <li class="aside-nav-root">
        维修开单
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/order/order-list">工单查询</a></dd>
        <#if BPSHARE == 'true'>
            <dd><a href="${BASE_PATH}/proxy/proxyList">受托单查询</a></dd>
        <#elseif SESSION_SHOP_JOIN_STATUS == 1>
            <dd><a href="${BASE_PATH}/proxy/proxyList">委托单查询</a></dd>
        </#if>
        <#if BPSHARE != 'true'>
            <dd><a href="${BASE_PATH}/shop/order/carwash">新建洗车单</a></dd>
            <dd><a href="${BASE_PATH}/shop/order/speedily">新建快修快保单</a></dd>
        </#if>
        <!-- 档口店不显示综合维修单 -->
        <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
            <dd><a href="${BASE_PATH}/shop/order/common-add">新建综合维修单</a></dd>
        </#if>
        <#if BPSHARE != 'true'>
            <dd><a href="${BASE_PATH}/shop/order/sell-good">新建销售单</a></dd>
        </#if>
            <dd><a href="${BASE_PATH}/shop/order/history/history-list">导入工单查询</a></dd>
        </dl>
    </li>
    <!-- 是否使用车间 -->
    <#if SESSION_SHOP_WORKSHOP_STATUS == 1>
        <li class="aside-nav-root">
            车间管理
        </li>
        <li class="aside-nav-list">
            <dl>
                <dd><a href="${BASE_PATH}/workshop/workOrder/toWorkOrderList">施工单查询</a></dd>
                <dd><a href="${BASE_PATH}/workshop/process/scanpage">施工作业扫描</a></dd>
                <dd><a href="${BASE_PATH}/workshop/workOrder/toWorkOrderBreakList">中断管理</a></dd>
                <dd><a href="${BASE_PATH}/workshop/loadplate/loadplate-show" target="_blank">看板查询</a></dd>
            </dl>
        </li>
    </#if>
</ul>

<!-- 档口店ppt不同 -->
<#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
<div role="link" class="link-download js-link-download">
    <strong>接车维修</strong>
    <p>培训资料下载</p>
</div>
<#else>
<div role="link" class="link-download js-link-tq-download">
    <strong>接车维修</strong>
    <p>培训资料下载</p>
</div>
</#if>
<script>
    //菜单回显逻辑
    /**
     * ch 2016-04-01
     * xxxxxx是页面名字,不带ftl,对应菜单每一项
     * yyyyyy是当前菜单子页面的名字。
     */
    function getAsideNav() {
        return [
            //客户预约
            {
                "url": [
                    {"appoint/list": [
                        //列表
                        "appoint/appoint-list",
                        //详情
                        "appoint/appoint-detail"
                    ]},
                    {"appoint/add": [
                        //新建
                        "appoint/appoint-edit"
                    ]}
                ]
            },
            //车辆接待
            {
                "url": [
                    {"customer/search": [
                        //列表
                        "customer/list",
                        //详情
                        "customer/car-detail",
                    ]},
                    {"customer/edit":[]},
                    {"precheck/precheck": [
                        //新建预检单
                        "precheck/precheck",
                        //详情页
                        "precheck/precheck-detail"
                    ]}
                ]
            },
            //维修开单
            {
                "url": [
                    {"order/order-list": [
                        "order/detail",
                        //子单
                        "order/virtualorder-edit",
                        "order/virtualorder-add"
                    ]},
                <#if BPSHARE == 'true' ||  SESSION_SHOP_JOIN_STATUS == 1>
                    //委托单列表
                    {"proxy/proxyList":[]},
                </#if>
                    // 开工单
                <#if BPSHARE != 'true'>
                    {"order/carwash": []},
                    {"order/speedily": []},
                </#if>
                <!-- 档口店不显示综合维修单 -->
                <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                    {"order/common-add": [
                        //编辑
                        "order/common-edit"
                    ]},
                </#if>
                <#if BPSHARE != 'true'>
                    //新建销售单
                    {"order/sell-good": [
                        //保存
                        "sell-good/save",
                        //更新
                        "sell-good/update",
                        //进入结算页面
                        "sell-good/tosettle",
                        //作废
                        "sell-good/invalid",
                        //打印
                        "sell-good/print"
                    ]},
                </#if>
                    // 历史工单查询
                    {"order/history/history-list":[]}
                ]
            },
            //车间管理
            {
                "url": [
                    {"workshop/workOrder/toWorkOrderList": [
                            "workshop/workOrder/toWorkOrderDetail"
                    ]},
                    {"workshop/process/scanpage": []},
                    {"workshop/workOrder/toWorkOrderBreakList": []},
                    {"workshop/loadplate/loadplate-show": []}
                ]
            }
        ];
    }
    $(function(){
        // 点击展开隐藏列表
        $(document).on('click', '.js-link-download', function () {
            // 下载ppt
            window.location.href = 'http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/legend/ppt/jiecheweixiu.ppt';
        });
        // 档口版培训ppt下载
        $(document).on('click', '.js-link-tq-download', function () {
            // 下载ppt
            window.location.href = 'http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/images/201608/source_img/original_p_147159257219417289.ppt';
        });
    });

</script>

<#include "yqx/tpl/common/aside-nav-tpl.ftl">
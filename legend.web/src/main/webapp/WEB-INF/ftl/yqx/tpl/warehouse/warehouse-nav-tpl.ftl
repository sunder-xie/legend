<style>
    .link-download {
        width: 140px;
        margin-top: 10px;
        padding: 10px;
        font-size: 12px;
        color: #333;
        background: url("${BASE_PATH}/static/img/common/order/download_03.png") 98px 13px no-repeat #fff;
        border: 1px solid #ddd;
    }
    .material{border-bottom: 1px solid #ddd;}
    .material .material-title{font-weight: bold; color: #333; line-height: 20px;}
    .material .material-english{font-family: arial, verdana, sans-serif; color: #666; line-height: 20px;}
    .material-list{ margin-top: 10px;}
    .material-list a{display: block; color: #333; line-height: 25px; background: url("${BASE_PATH}/static/img/common/order/download-ico1.png") no-repeat right;}
    .material-list a:hover{cursor:pointer;color: #85af1d;background: url("${BASE_PATH}/static/img/common/order/download-ico2.png") no-repeat right; }

</style>

<ul class="aside-nav" data-tpl-ref="warehouse-nav-tpl">
    <#if BPSHARE == 'true'>
        <li class="aside-nav-root">
            油漆管理
        </li>
        <li class="aside-nav-list">
            <dl>
                <dd><a href="${BASE_PATH}/goods/paintExt/toPaintStock">油漆库存</a></dd>
                <dd><a href="${BASE_PATH}/paint/inventory/toInventoryPaintList">油漆盘点</a></dd>
                <dd><a href="${BASE_PATH}/shop/paint/record/toPaintUseRecordList">使用记录</a></dd>
            </dl>
        </li>
    </#if>
    <li class="aside-nav-root">
        库存管理
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/warehouse/stock/stock-query">库存查询</a></dd>
            <dd><a href="${BASE_PATH}/shop/warehouse/stock/stock-inventory">库存盘点</a></dd>
            <dd><a href="${BASE_PATH}/shop/warehouse/stock/stock-warning">库存预警</a></dd>
        </dl>
    </li>
    <li class="aside-nav-root">
        出库管理
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/warehouse/out/out-list">出库单查询</a></dd>
        <!-- 档口店不显示工单报价,工单出库 -->
        <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
            <dd><a href="${BASE_PATH}/shop/warehouse/out/order-quote-list">工单报价</a></dd>
            <dd><a href="${BASE_PATH}/shop/warehouse/out/order-out-list">工单出库</a></dd>
        </#if>
            <dd><a href="${BASE_PATH}/shop/warehouse/out/out-other">其他出库</a></dd>
        </dl>
    </li>
    <li class="aside-nav-root">
        入库管理
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/warehouse/in/in-list">入库单查询</a></dd>
            <dd><a href="${BASE_PATH}/shop/warehouse/in/in-edit/blue">采购入库</a></dd>
        </dl>
    </li>
    <#if SESSION_USER_IS_ADMIN == 1 || SESSION_WAREHOUSE_SHARE_ROLE == true>
    <li class="aside-nav-root">
        库存配件买卖
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/warehouse/share/sale">出售库存配件</a></dd>
            <dd><a href="${BASE_PATH}/shop/warehouse/share/buy">购买库存配件</a></dd>
        </dl>
    </li>
    </#if>
</ul>

<div role="link" class="link-download">
    <div class="material">
        <p class="material-title">培训资料下载</p>
        <p class="material-english">Download</p>
    </div>
    <ul class="material-list">
        <li class="js-marketing"><a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/image/orig_147600510668864724.ppt">仓库管理资料</a></li>
    </ul>
</div>
<script>
    //菜单回显逻辑
    function getAsideNav() {
        return [
            <#if BPSHARE == 'true'>
                {
                    "url": [
                        {"goods/paintExt/toPaintStock": []},
                        {"paint/inventory/toInventoryPaintList": [
                            "paint/inventory/toInventoryPaintDtl",
                            "paint/inventory/toUpdateInventoryPaint",
                            "paint/inventory/toInventoryPaint"
                        ]},
                        {"shop/paint/record/toPaintUseRecordList": [
                                "shop/paint/record/toPaintUseRecordAdd",
                                "shop/paint/record/toPaintUseRecord"
                        ]}
                    ]
                },
            </#if>
            //库存管理
            {
                "url": [
                    {"shop/warehouse/stock/stock-query": []},
                    {"shop/warehouse/stock/stock-inventory": [
                        "shop/warehouse/stock/stock-inventory-detail"
                    ]},
                    {"shop/warehouse/stock/stock-warning": []}
                ]
            },
            //出库管理
            {
                "url": [
                    {"shop/warehouse/out/out-list": [
                        "shop/warehouse/out/out-refund",
                        "shop/warehouse/out/out-detail"
                    ]},
                        <!-- 档口店不显示工单报价,工单出库 -->
                    <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                    {"shop/warehouse/out/order-quote-list": [
                        "shop/warehouse/out/order-quote-detail"
                    ]},
                    {"shop/warehouse/out/order-out-list": [
                        "shop/warehouse/out/order-out-detail"
                    ]},
                    </#if>
                    {"shop/warehouse/out/out-other": []}
                ]
            },
            //入库管理
            {
                "url": [
                    {"shop/warehouse/in/in-list": [
                        "shop/warehouse/in/in-red",
                        "shop/warehouse/in/in-detail"
                    ]},
                    //新建入库单
                    {"shop/warehouse/in/in-edit/blue": []}
                ]
            },
            {
                "url": [
                    {"/shop/warehouse/share/sale": []},
                    {"/shop/warehouse/share/buy": []}
                ]
            }
        ];
    }

</script>
<#include "yqx/tpl/common/aside-nav-tpl.ftl">
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/stock/stock-common.css?2a6120c599f20dbc8877c532837dd82f"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/stock/stock-inventory-detail.css?98a4a97cb727b8b010f4c1649dbbc99c"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fr">
        <!-- 标题 start -->
        <div class="order-head clearfix">
            <h3 class="headline fl">盘点单详情</h3>
        </div>
        <!-- 标题 end -->
        <!-- 右侧内容区 end -->
        <div class="form-box">
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label">
                        盘点编号：
                    </div>
                    <div class="form-item">
                        <input type="hidden" name="recordId" value="${record.id}"/>
                        <div class="yqx-text js-show-tips">
                            ${record.recordSn}
                        </div>
                    </div>
                </div>
                <div class="col-6">
                    <div class="form-label">
                        盘点日期：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        ${record.gmtModifiedStr}
                        </div>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label">
                        盘点人：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text js-show-tips">
                        ${record.inventoryCheckerName}
                        </div>
                    </div>
                </div>
                <div class="col-6">
                    <div class="form-label">
                        开单人：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        ${record.operatorName}
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="accessories-box">
            <div class="accessories-title">配件项目</div>
            <div class="accessories-table">
                <table class="yqx-table yqx-table-big yqx-table-hover yqx-table-hidden">
                    <thead>
                    <tr>
                        <th></th>
                        <th class="text-l tc-2">配件名称</th>
                        <th class="text-l tc-3">零件号</th>
                        <th class="text-l tc-4">实盘库存</th>
                        <th class="text-r tc-5">实盘库存金额</th>
                        <th class="text-r tc-price">成本</th>
                        <th class="text-l tc-7">现库存</th>
                        <th class="text-r tc-price">库存金额</th>
                        <th class="text-l tc-9">库位</th>
                        <th class="text-l tc-10">备注</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list stocks as stock>
                    <tr>
                        <td>
                            ${stock_index +1}
                        </td>
                        <td class="text-l">
                            <div class="ellipsis-2 text-important js-show-tips">${stock.goodsName}</div>
                        </td>
                        <td class="text-l">
                            <div class="ellipsis-1 text-important js-show-tips">${stock.goodsFormat}</div>
                            <div class="ellipsis-1 text-minor js-show-tips">${stock.catName}</div>
                        </td>
                        <td class="text-l">
                            <div>${stock.realStock}</div>
                            <div>${stock.measureUnit}</div>
                        </td>
                        <td class="money-small-font text-r">&yen;${(stock.realInventoryAmount?string("0.00"))!}</td>
                        <td class="money-small-font text-r">&yen;${(stock.inventoryPrice?string("0.00"))!}</td>
                        <td class="text-l">
                            <div>${stock.currentStock} </div>
                            <div class="text-minor">${stock.measureUnit}</div>
                        </td>
                        <td class="money-small-font text-r">&yen;${(stock.currentInventoryAmount?string("0.00"))!}</td>
                        <td class="text-l">
                            <div class="ellipsis-1 js-show-tips">${stock.depot}</div>
                        </td>
                        <td class="text-l">
                            <div class="ellipsis-2 js-show-tips">${stock.reason}</div>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>

        <#if record.inventoryRemark>
            <div style="padding-top:12px;">
                <div class="form-label">备注: </div>
                <div class="form-item">
                    <div class="ellipsis-2 js-show-tips">${record.inventoryRemark}</div>
                </div>
            </div>
        </#if>

            <div class="btn-group">
                <#if !record || record.status==1 >
                    <button class="yqx-btn yqx-btn-2 yqx-btn-small js-generate">生成盘点单</button>
                </#if>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-export">导出</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-print">打印</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-return">返回</button>
                <#if !record || record.status==1 >
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small fr m-right js-edit">编辑</button>
                </#if>
            </div>
        </div>

        <!-- 右侧内容区 end -->
    </div>
</div>

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/warehouse/stock/stock-inventory-detail.js?412fc31df0ab5ad30ea1fb57f241eb09"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
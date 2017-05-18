<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/in/in-edit.css?37152abc0bdd84be5be3092dea091691">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/in/in-detail.css?dc8573d74d04176697860fdb6202a73c">
<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl detail-box">
        <div class="order-list-title clearfix">
            <h3 class="headline fl"><#if warehouseInVo.status == "DRAFT">草稿详情<#else>入库单详情</#if></h3>
        </div>
        <form class="main">
            <#if warehouseInVo.status == "HZRK" || warehouseInVo.status == "HZZF"><div class="form-item fr other-order">
                <label>关联入库单（蓝字）：</label><i class="order-num blue-num">${warehouseInVo.relSn}</i>
            </div>
            </#if>
            <#if warehouseInVo.status == "HZZF" || warehouseInVo.status == "LZZF">
                <div class="seal-2 yzf"></div>
            </#if>
            <div class="show-grid order-num-box clearfix">
                <div class="form-item">
                    <label><#if warehouseInVo.status == "DRAFT"> 草稿单号:<#elseif warehouseInVo.status == "HZZF" || warehouseInVo.status == "HZRK">退货单号: <#else >入库单号:</#if>
                    </label> <#if warehouseInVo.status == "HZRK" || warehouseInVo.status == "HZZF"> <i class="order-num red-num"><#else><i class="order-num blue-num"></#if>${warehouseInVo.warehouseInSn}</i>
                    <div
                        class="form-item time-box">（<#if warehouseInVo.status == "DRAFT">草稿日期:
                    <#elseif warehouseInVo.status == "HZRK" || warehouseInVo.status == "HZZF"> 退货日期:
                    <#else>入库日期:</#if>${warehouseInVo.inTime?string("yyyy-MM-dd HH:mm")}）
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item supplier-box js-show-tips">
                    <label>　供应商：</label><i>${warehouseInVo.supplierName}</i>
                </div><div class="form-item js-show-tips">
                <label>　联系人：</label><i>${warehouseInVo.contact}</i>
                </div><div class="form-item">
                    <label>联系电话：</label><i>${warehouseInVo.contactMobile}</i>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item">
                    <label>发票类型：</label><i>${warehouseInVo.invoiceTypeName}</i>
                </div><div class="form-item">
                    <label>付款方式：</label><i>${warehouseInVo.paymentMode}</i>
                </div><div class="form-item">
                    <label>关联淘汽：</label><i><#if warehouseInVo.purchaseSn>${warehouseInVo.purchaseSn}<#else >否</#if></i>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item js-show-tips">
                    <label>　采购人：</label><i>${warehouseInVo.purchaseAgentName}</i>
                </div><div class="form-item">
                <label>　开单人：</label><i>${warehouseInVo.operatorName}</i>
            </div>
            </div>
        </form>
        <div class="list-box">
            <div class="nav clearfix">
                <h3>退货配件</h3>
            </div>
            <table class="yqx-table-2 yqx-table yqx-table-hover yqx-table-big yqx-table-hidden">
                <thead>
                <tr>
                    <th></th>
                    <th class="text-l tc-2">零件号</th>
                    <th class="text-l tc-3">配件名称</th>
                    <th class="text-r tc-price">入库单价</th>
                    <th class="tc-5">入库数量</th>
                    <th class="text-r tc-price">总金额</th>
                    <th class="tc-7">库存数量</th>
                    <th class="text-l tc-8">适配车型</th>
                </tr>
                </thead>
                <#list warehouseInVo.detailList as detail>
                <tr>
                    <td>${detail_index + 1}</td>
                    <td class="text-l goods-format js-show-tips ellipsis-1">${detail.goodsFormat}</td>
                    <td class="text-l">
                        <div class="ellipsis-2 js-show-tips">${detail.goodsName}</div>
                    </td>
                    <td class="text-r money-small-font">&yen;${(detail.purchasePrice?string("0.00"))!}</td>
                    <td>${detail.goodsCount}</td>
                    <td class="text-r money-small-font">&yen;${(detail.purchaseAmount?string("0.00"))!}</td>
                    <!-- 库存数量 -->
                    <td>${detail.stock}</td>
                    <td class="text-l">
                        <div class="ellipsis-2 js-show-tips">${detail.carInfoStr}</div>
                    </td>
                </tr>
                </#list>
            </table>
            <div class="remark">
                <div class="form-label">
                    备注：
                </div>
                <div class="form-item">
                    <div class="yqx-text">
                    ${warehouseInVo.comment}
                    </div>
                </div>
            </div>
            <div class="mark-box">
               <div class="show-grid money-count">
                    <div class="form-item">
                        <label>配件合计金额：</label><i class="money-font">&yen;${(warehouseInVo.goodsAmount?string("0.00"))!}</i> + 税费 <i class="money-font">&yen;${(warehouseInVo.tax?string("0.00"))!}</i> + 运费 <i class="money-font">&yen;${(warehouseInVo.freight?string("0.00"))!}</i>
                        <input readonly hidden value="${(warehouseInVo.goodsAmount?string("0.00"))!}">
                    </div>
                </div>
            </div>
            <div class="total-box">
                总计：<i class="money-font">&yen;${(warehouseInVo.totalAmount?string("0.00"))!}</i>
            </div>
            <div class="form-footer btn-group">
                <div class="fl">
                   <input type="hidden" id="id" value=${warehouseInVo.id} />
                    <#if warehouseInVo.status =="LZRK">
                        <#if warehouseInVo.paymentStatus != "ZFWC">
                            <button class="yqx-btn yqx-btn-2 js-pay"
                                data-supplier-id="${warehouseInVo.supplierId}"
                                data-supplier-name="${warehouseInVo.supplierName}">付款</button>
                         </#if>
                        <button class="yqx-btn yqx-btn-1 js-stock-refund">退货</button>
                        <button class="yqx-btn yqx-btn-1 js-abolish" data-status="LZRK">作废</button>
                    </#if>
                    <#if warehouseInVo.status =="DRAFT">
                        <button class="yqx-btn yqx-btn-2 js-stock">入库</button>
                        <button class="yqx-btn yqx-btn-1 js-edit">编辑</button>
                        <button class="yqx-btn yqx-btn-1 js-delete">删除</button>
                    </#if>
                    <#if warehouseInVo.status =="HZRK">
                        <button class="yqx-btn yqx-btn-1 js-abolish">作废</button>
                    </#if>
                    <#if warehouseInVo.status !="DRAFT">
                        <button class="yqx-btn yqx-btn-1 js-print">打印</button>
                    </#if>
                <#if warehouseInVo.status =="HZZF" || warehouseInVo.status =="LZZF">
                    <button class="yqx-btn yqx-btn-1 js-delete">删除</button>
                </#if>
                </div>
                <div class="fr">
                    <#if warehouseInVo.status == "HZZF" || warehouseInVo.status == "LZZF">
                        <a href="${BASE_PATH}/shop/warehouse/in/in-list" class="yqx-btn yqx-btn-1">返回</a>
                    <#else>
                        <button class="yqx-btn yqx-btn-1 js-back">返回
                        </button>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${BASE_PATH}/static/js/page/warehouse/in/in-detail.js?b617a398d37099a84b2db1956cd8bbb0"></script>
<#include "yqx/layout/footer.ftl">

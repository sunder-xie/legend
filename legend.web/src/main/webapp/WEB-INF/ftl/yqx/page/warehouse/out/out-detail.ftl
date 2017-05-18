<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/out/out-common.css?10e3e17a63bb9323c82892932093d3e5"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/out/out-detail.css?53443e2f09db7e36ced6e7922db6b95b">
<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">出库单详情</h3>
        </div>
        <div class="detail-form-box">
            <input id="warehouseOutId"  hidden value="${warehouseOut.id}">
<#if warehouseOut.status == "CK_LZZF" || warehouseOut.status == "CK_HZZF">
            <div class="seal-2 yzf"></div>
    </#if>
        <#if warehouseOut.status == "HZCK" || warehouseOut.status == "CK_HZZF">
            <div class="show-grid text-r">
               关联出库单（蓝字）：<span class="color-blue order-num weight">${warehouseOut.relSn}</span>
            </div>
    </#if>
            <div class="show-grid">
                <div class="form-label weight">
<#if warehouseOut.status == "HZCK" || warehouseOut.status == "CK_HZZF">退货单(红字) <#else >出库单（蓝字）：</#if>
                </div>
                <div class="form-item item-width">
                    <div class="yqx-text">
<#if warehouseOut.status == "HZCK" || warehouseOut.status == "CK_HZZF">  <span class="color-red order-num weight"><#else > <span class="color-blue order-num weight"></#if>${warehouseOut.warehouseOutSn}</span><span class="color-grey">（<#if warehouseOut.status == "HZCK" || warehouseOut.status == "CK_HZZF">退货日期<#else >出库日期：</#if>${warehouseOut.gmtCreate?string("yyyy-MM-dd HH:mm")}）</span>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label">
                        关联工单：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                           ${warehouseOut.orderSn}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        车牌：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text max-text js-show-tips">
                            ${warehouseOut.carLicense}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        车型：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text max-text js-show-tips">
                        ${warehouseOut.carType}
                        </div>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label">
                        联系人：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text max-text js-show-tips">
                            ${warehouseOut.customerName}
                        </div>
                    </div>
                </div>

                <div class="col-4">
                    <div class="form-label">
                        联系电话：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text max-text js-show-tips">
                        ${warehouseOut.customerMobile}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        出库类型：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text max-text js-show-tips">
                            ${warehouseOut.outTypeName}
                        </div>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label">
                        领料人：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text max-text js-show-tips">
                        ${warehouseOut.receiverName}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        出库人：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text max-text js-show-tips">
                        ${warehouseOut.operatorName}
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="list-box">
            <div class="list-title">
                出库配件
            </div>
            <table class="yqx-table yqx-table-hover yqx-table-hidden yqx-table-big" id="list">
                <thead>
                <tr>
                    <th class="text-l">序号</th>
                    <th class="text-l tc-2">零件号</th>
                    <th class="text-l tc-3">配件名称</th>
                    <th class="text-l tc-4">出库数量</th>
                    <th class="text-r tc-price">销售价</th>
                    <th class="text-r tc-price">成本价</th>
                    <th class="text-l tc-7">库存数量</th>
                    <th class="text-l tc-8">适配车型</th>
                    <th class="text-l tc-9">仓位</th>
                </tr>
                </thead>
                <tbody>
                <#list warehouseOut.detailVoList as detail>
                <tr>
                    <td class="text-l">${detail_index + 1}</td>
                    <td class="text-l">
                        <input name="id" hidden value="${detail.id}">
                        <input name="warehouseOutId" hidden value="${detail.warehouseOutId}">
                        <div class="ellipsis-1 js-show-tips">${detail.goodsFormat}</div>
                    </td>
                    <td class="text-l">
                        <div class="ellipsis-2 js-show-tips ellipsis-1">${detail.goodsName}</div>
                    </td>
                    <td class="text-l">
                        <p>${detail.goodsCount}</p>
                        <p>${detail.measureUnit}</p>
                    </td>
                    <td class="text-r">
                        <div class="form-item">
                            <input name="salePrice" class="yqx-input"
                                   data-v-type="price|required"
                                   value="${(detail.salePrice?string("0.00"))!}">
                        </div>
                    </td>
                    <td class="text-r money-small-font">&yen;${(detail.inventoryPrice?string("0.00"))!}</td>
                    <!-- 库存数量 -->
                    <td class="text-l">
                        <p>${detail.stock}</p>
                        <p class="text-minor">${detail.measureUnit}</p>
                    </td>
                    <!-- 适配车型 -->
                    <td class="text-l">
                        <div class="ellipsis-1 text-minor js-show-tips">${detail.carInfoStr}</div>
                    </td>
                    <td class="text-l">
                        <div class="ellipsis-1 js-show-tips">${detail.depot}</div>
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>
            <div class="remark">
                <div class="form-label">
                    备注：
                </div>
                <div class="form-item">
                    <div class="yqx-text max-remark js-show-tips">
                    ${warehouseOut.comment}
                    </div>
                </div>
            </div>
            <div class="total">
                配件合计金额: <span class="money-small-font">&yen;${(warehouseOut.saleAmount?string("0.00"))!}</span>
            </div>
            <div class="total">
                成本价总计：<span class="money-font">&yen;${(warehouseOut.costAmount?string("0.00"))!}</span>
            </div>
            <div class="btn-box clearfix">
                <button class="yqx-btn yqx-btn-3 js-save">保存</button>
                <#if warehouseOut.status == "LZCK">
                    <button class="yqx-btn yqx-btn-2 js-refund " data-id="${warehouseOut.id}">退货</button>
                    <button class="yqx-btn yqx-btn-2 js-abolish " data-status="LZCK"
                            data-id="${warehouseOut.id}">作废</button>
                </#if>
                <#if warehouseOut.status == "HZCK">
                     <button class="yqx-btn yqx-btn-2 js-abolish " data-id="${warehouseOut.id}">作废</button>
                </#if>
                <button class="yqx-btn yqx-btn-2 js-print" data-id="${warehouseOut.id}">打印</button>
                <#if warehouseOut.status == "CK_LZZF" || warehouseOut.status == "CK_HZZF">
                    <button class="yqx-btn yqx-btn-1 js-delete " data-id="${warehouseOut.id}">删除</button>
                    <a href="${BASE_PATH}/shop/warehouse/out/out-list#1" class="yqx-btn yqx-btn-1 fr">返回</a>
                <#else>
                <button class="yqx-btn yqx-btn-1 js-return fr">返回</button>
                </#if>
            </div>
        </div>

    </div>
</div>

<script src="${BASE_PATH}/static/js/page/warehouse/out/out-detail.js?4e01ecf48820066d5b1d39379daec931"></script>
<#include "yqx/layout/footer.ftl">

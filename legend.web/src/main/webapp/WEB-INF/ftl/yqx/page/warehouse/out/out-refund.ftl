<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/out/out-common.css?10e3e17a63bb9323c82892932093d3e5"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/out/out-refund.css?d64bd476f5a45731ff5f38bd0f12c93d">
<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">出库退货</h3>
        </div>
        <div class="detail-form-box js-out" >

            <div class="show-grid text-r">
                关联出库单（蓝字）：<span class="color-blue weight">${warehouseOut.warehouseOutSn}</span>
                <input type="hidden" name="relSn" value="${warehouseOut.warehouseOutSn}"/>
                <input type="hidden" name="relId" value="${warehouseOut.id}"/>
            </div>
            <div class="show-grid">
                <div class="form-label">
                    退货单（红字）：
                </div>
                <div class="form-item item-width">
                    <div class="yqx-text">
                        <span class="color-red weight">${warehouseOutSn}</span>
                        <input type="hidden" name="warehouseOutSn" value="${warehouseOutSn}"/>
                        <span class="color-grey">（退货日期：${.now?string("yyyy-MM-dd HH:mm")}）</span>
                        <input type="hidden" name="gmtCreateStr" value="${.now?string("yyyy-MM-dd HH:mm:ss")}"/>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label">
                        关联工单：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text form-max-text js-show-tips">
                        ${warehouseOut.orderSn}
                            <input type="hidden" name="orderId" value="${warehouseOut.orderId}"/>
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        车牌：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text form-max-text js-show-tips">
                        ${warehouseOut.carLicense}
                            <input type="hidden" name="carLicense" value="${warehouseOut.carLicense}"/>
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        车型：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text form-max-text js-show-tips">
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
                        <div class="yqx-text form-max-text js-show-tips">
                        ${warehouseOut.customerName}
                            <input type="hidden" name="customerName" value=" ${warehouseOut.customerName}"/>
                        </div>
                    </div>
                </div>

                <div class="col-4">
                    <div class="form-label">
                        联系电话：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text form-max-text js-show-tips">
                        ${warehouseOut.customerMobile}
                            <input type="hidden" name="customerMobile" value=" ${warehouseOut.customerMobile}"/>
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        出库类型：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text form-max-text js-show-tips">
                            ${warehouseOut.outTypeName}
                            <input type="hidden" name="outType" value="${warehouseOut.outType}"/>
                        </div>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-must">
                        领料人：
                    </div>
                    <div class="form-item">
                        <input type="text"  class="yqx-input yqx-input-icon yqx-input-small js-picking" value="${warehouseOut.receiverName}" placeholder="" data-v-type="required">
                        <span class="fa icon-angle-down icon-small"></span>
                        <input type="hidden" name="goodsReceiver" value="${warehouseOut.goodsReceiver}"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="list-box">
            <div class="list-title">
                出库配件
            </div>
            <table class="yqx-table yqx-table-hover yqx-table-big">
                <thead>
                <tr>
                    <th></th>
                    <th class="text-l tc-1">零件号</th>
                    <th class="text-l tc-2">配件名称</th>
                    <th class="text-c tc-3">出库数量</th>
                    <th class="text-r tc-price">销售价</th>
                    <th class="text-l tc-5">可退数量</th>
                    <th class="text-r tc-price">成本价</th>
                    <th class="text-l tc-6">适配车型</th>
                    <th class="text-c tc-7">库位</th>
                    <th class="text-c">操作</th>
                </tr>
                </thead>
                <tbody>
                <#list warehouseOut.detailVoList as detail>
                <tr class="js-detail">
                    <td>${detail_index + 1}</td>
                    <td class="text-l">
                        <div class="ellipsis-1 money-highlight js-show-tips">${detail.goodsFormat}</div>
                        <input type="hidden" name="goodsId" value="${detail.goodsId}"/>
                        <input type="hidden" name="orderGoodsId" value="${detail.orderGoodsId}"/>
                    </td>
                    <td class="text-l">
                        <div class="ellipsis-2 js-show-tips">${detail.goodsName}</div>
                    </td>
                    <td class="text-c td-position">
                        <div class="form-item goods-count has-unit">
                            <input type="text" name="goodsCount" class="yqx-input goods-count" value="-${detail.goodsCount}" placeholder="" data-v-type="maxValue:0">
                            <i class="fa goods-fa">${detail.measureUnit}</i>
                        </div>
                    </td>
                    <#--销售价-->
                    <td class="text-r">
                        <div class="form-item ">
                            <input type="text" class="yqx-input"
                                   data-v-type="required|price"
                                   name="salePrice" value="${detail.salePrice}"/>
                        </div>
                    </td>
                    <#--可退数量-->
                    <td class="text-l">
                        <div class="js-goods-real" data-count="${detail.goodsRealCount}">${detail.goodsRealCount} <span class="text-minor">${detail.measureUnit}</span></div>
                    </td>
                    <#--成本价-->
                    <td class="text-r money-small-font">&yen;<span class="cost-price">${detail.inventoryPrice}</span></td>
                    <#-- 适配车型 -->
                    <td class="text-l">
                        <div class="ellipsis-1 js-show-tips">${detail.carInfoStr}</div>
                    </td>
                    <#-- 库位 -->
                    <td class="text-c">
                        <div class="ellipsis-1 js-show-tips">${detail.depot}</div>
                    </td>
                    <td class="text-c">
                        <a href="javascript:;" class="yqx-link yqx-link-2 js-delete-btn">删除</a>
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>
            <div class="remarks">
                <div class="form-label">
                    备注
                </div>
                <div class="form-item js-comment">
                    <textarea class="yqx-textarea" name="comment" id="" cols="100" rows="1" placeholder="备注"></textarea>
                </div>
            </div>
            <div class="total">
                配件合计金额: <span class="money-font">&yen;</span><span class="money-font accessories-total"><#if warehouseOut.saleAmount ==0>0.00<#else >-${warehouseOut.saleAmount}</#if></span>
            </div>
            <div class="total">
                成本价总计：<span class="money-font">&yen;</span><span class="money-font cost-total"><#if warehouseOut.costAmount ==0>0.00<#else >-${warehouseOut.costAmount}</#if></span>
            </div>
            <div class="btn-box clearfix">
                <button class="yqx-btn yqx-btn-2 js-refund">退货</button>
                <button class="yqx-btn yqx-btn-1 js-return fr">返回</button>
            </div>
        </div>

    </div>
</div>

<script src="${BASE_PATH}/static/js/page/warehouse/out/out-refund.js?1b29e8d1209484afd43131774b7d2987"></script>
<#include "yqx/layout/footer.ftl">

<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/out/out-common.css?10e3e17a63bb9323c82892932093d3e5"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/out/order-out-detail.css?b510e3820b0f832885c37c920e6623cb">
<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">工单出库单</h3>
        </div>
        <div class="js-stock">
            <div class="detail-box">
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label">
                            关联工单：
                        </div>
                        <div class="form-item">
                            <input type="hidden" name="orderId" id="orderId" value="${orderInfo.id}"/>
                            <div class="yqx-text max-width weight js-show-tips">
                                <a href="${BASE_PATH}/shop/warehouse/out/stock-quote-detail?orderId=${orderInfo.id}&refer=outbound" style="text-decoration: underline">${orderInfo.orderSn}</a>
                            </div>
                        </div>
                    </div>

                    <div class="col-4">
                        <div class="form-label">
                            出库单（蓝字）：
                        </div>
                        <div class="form-item">
                            <div class="yqx-text text-blue weight max-width js-show-tipss">${warehouseOutSn}
                                <input type="hidden" name="warehouseOutSn"  value=" ${warehouseOutSn}"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label">
                            车牌：
                        </div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">${orderInfo.carLicense}
                                <input type="hidden" name="carLicense"  value="${orderInfo.carLicense}"/>
                            </div>
                        </div>
                    </div>

                    <div class="col-4">
                        <div class="form-label">
                            车型：
                        </div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                                <span>车辆型号 ${orderInfo.carInfo} ${orderInfo.carAlias}</span>
                                <input type="hidden" name="carType" value="${orderInfo.carInfo}">
                                <input type="hidden" name="carByName" value="${orderInfo.carAlias}">
                            </div>
                        </div>
                    </div>

                    <div class="col-4">
                        <div class="form-label">
                            VIN码：
                        </div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${orderInfo.vin}
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
                            <div class="yqx-text max-width js-show-tips">${orderInfo.customerName}
                                <input type="hidden" name="customerName" value="${orderInfo.customerName}"/>
                            </div>
                        </div>
                    </div>

                    <div class="col-4">
                        <div class="form-label">
                            联系电话：
                        </div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${orderInfo.customerMobile}
                                <input type="hidden" name="customerMobile" value="${orderInfo.customerMobile}"/>
                            </div>
                        </div>
                    </div>

                    <div class="col-4">
                        <div class="form-label">
                            开单人：
                        </div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${orderInfo.operatorName}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="detail-form-box">
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            出库日期：
                        </div>
                        <div class="form-item">
                            <input type="text" id ="outboundDate"
                                   name="gmtCreateStr" value="${.now?string("yyyy-MM-dd HH:mm")}"
                                   class="yqx-input yqx-input-small js-outTime" data-v-type="required">
                            <span class="fa icon-calendar icon-small"></span>
                        </div>
                    </div>

                    <div class="col-6">
                        <div class="form-label form-label-must">
                            领料人：
                        </div>
                        <div class="form-item">
                            <input type="text" name="receiverName"
                                   class="yqx-input yqx-input-small js-select-user"
                                   placeholder="请选择领料人" data-v-type="required">
                            <input type="hidden" name="goodsReceiver"/>
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            出库人：
                        </div>
                        <div class="form-item">
                            <input type="text"
                                   class="yqx-input yqx-input-small js-select-user"
                                   placeholder="" data-v-type="required" value="${SESSION_USER_NAME}" disabled>
                            <input type="hidden" name="creator" value="${SESSION_USER_ID}"/>
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                    </div>

                    <div class="col-6">
                        <div class="form-label form-label-must">
                            出库类型：
                        </div>
                        <div class="form-item">
                            <input type="text"
                                   class="yqx-input yqx-input-small js-out-type"
                                   placeholder="请选择出库类型" data-v-type="required">
                            <input type="hidden" name="outType"/>
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="list-box">
            <div class="list-title">
                出库配件<span class="tips">温馨提示：如发生配件变更请前往工单变更数据。</span>
            </div>
            <table class="yqx-table yqx-table-hover yqx-table-big">
                <thead>
                <tr>
                    <th>序号</th>
                    <th class="text-l tc-2">零件号</th>
                    <th class="text-l tc-3">配件名称</th>
                    <th class="tc-4">开单数量</th>
                    <th class="tc-5">已出数量</th>
                    <th class="tc-6">出库数量</th>
                    <th class="text-r tc-price">销售价</th>
                    <th class="text-r tc-price">成本价</th>
                    <th class="tc-9">库存</th>
                    <th class="text-l tc-10">适配车型</th>
                    <th class="tc-11">库位</th>
                </tr>
                </thead>
                <tbody id="orderGoodsTB">
                <#list orderGoodsList as item>
                <tr class="js-detail">
                    <td>${item_index + 1}</td>
                    <td class="text-l">
                        <div class="ellipsis-1 js-show-tips">${item.goodsFormat}</div>
                        <input type="hidden" name="goodsId" value="${item.goodsId}" />
                        <input type="hidden" name="orderGoodsId" value="${item.id}"/>
                        <input type="hidden" id="goodsSn" name="goodsSn" value="${item.goodsSn}" />
                    </td>
                    <td class="text-l">
                        <div class="ellipsis-2 js-show-tips">${item.goodsName}</div>
                        <input type="hidden" name="goodsName" value="${item.goodsName}"/>
                    </td>
                    <td>${item.goodsNumber}
                        <input type="hidden" name="orderCount" value="${item.goodsNumber}"/>
                    </td>
                    <td>${item.outNumber}
                        <input type="hidden" name="outNumber" value="${item.outNumber}"/>
                    </td>
                    <#--出库数量-->
                    <td class="td-position">
                        <input type="text" name="goodsCount" value="${item.remainingNumber}"
                               class="yqx-input text-c js-goods-num js-float-1" data-v-type="number"/>
                    </td>
                    <#--销售价-->
                    <td class="td-position">
                        <input type="text" name="salePrice" value="${(item.goodsPrice?string("0.00"))!}"
                             class="yqx-input  money-small-font js-goods-price js-float-2 js-show-tips"
                               data-v-type="required | price"/>
                    </td>
                    <#--成本价-->
                    <td class="text-r money-small-font">${(item.inventoryPrice?string("0.00"))!}
                        <input type="hidden" name="inventoryPrice" value="${item.currentInventoryPrice}">
                    </td>
                    <#--库存-->
                    <td>${item.stock}</td>
                    <#--适配车型-->
                    <td class="text-l">
                        <div class="ellipsis-1 js-show-tips">${item.carInfoStr}</div>
                    </td>
                    <#--库位-->
                    <td>
                        <div class="ellipsis-1 js-show-tips">${item.depot}</div>
                        <input type="hidden" name="depot" value="${item.depot}">
                    </td>
                </tr>
                </#list>

                </tbody>
            </table>

            <div class="btn-box clearfix">
                <button class="yqx-btn yqx-btn-2 js-save">出库</button>
                <button class="yqx-btn yqx-btn-1 js-back fr">返回</button>
            </div>
        </div>
    </div>
</div>


<script src="${BASE_PATH}/static/js/page/warehouse/out/order-out-detail.js?a06d6a60e279cacfff461138df69f1db"></script>

<#include "yqx/layout/footer.ftl">

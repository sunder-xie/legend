<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/common/report/report-common.css?c58c9a3ee3acf5c0dbc310373fe3c349">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/report/statistics/order/sale-detail.css?24b9f4125e0e0b72a4860fa3ce46dd17">
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/report/left-nav.ftl"/>
    <!-- 配件销售 start -->
    <div class="content fr goods-content">
        <input type="hidden" id="istqmall" value="${SESSION_SHOP_IS_TQMALL_VERSION}">
        <div class="content-head">
            <input type="hidden" id="start-time" value="${orderSettleStartDate}"/>
            <input type="hidden" id="start-pay-time" value="${orderSettleEndDate}"/>
            <div class="tab-item current-item goods-tab"
                 data-target=".goods-content" data-tab="0">配件销售明细
            </div>
            <div
                    class="tab-item services-tab" data-target=".services-content" data-tab="1">服务销售明细
            </div>
        </div>
        <div class="container search-form" id="goodsForm">
            <div class="show-grid input-width-155">
                <div class="input-group">
                    <label>开单时间</label>

                    <div class="form-item">
                        <input type="text" id="start" name="orderCreateStartDate"
                               class="yqx-input" placeholder="选择开始时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                    <label>至</label>

                    <div class="form-item">
                        <input type="text" id="end" name="orderCreateEndDate"
                               class="yqx-input" placeholder="选择结束时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                </div>
                <div class="input-group fr">
                    <label>结算时间</label>

                    <div class="form-item">
                        <input type="text" id="start1" name="orderSettleStartDate"
                               class="yqx-input" value="${orderSettleStartDate}" placeholder="选择开始时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                    <label>至</label>

                    <div class="form-item">
                        <input type="text" id="end1" name="orderSettleEndDate"
                               class="yqx-input" value="${orderSettleEndDate}" placeholder="选择结束时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                </div>
            </div>
            <div class="show-grid margin-bt-70">
                <div class="input-group ">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-middle"
                               name="orderGoodsName"
                                placeholder="配件名称"
                                >
                    </div>
                </div>
                <div class="input-group ">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-middle"
                               name="orderGoodsNumber"
                               placeholder="零件号"
                                >
                    </div>
                </div>
                <div class="input-group ">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-middle"
                               name="carType"
                               placeholder="车型"
                                >
                    </div>
                </div>
                <div class="input-group ">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-middle"
                               name="orderNumber"
                               placeholder="工单号"
                                >
                    </div>
                </div>

                <div class="input-group ">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-middle"
                               name="carLicense"
                               placeholder="车牌"
                                >
                    </div>
                </div>
            </div>
            <div class="btn-group">
                <button class="js-search-btn yqx-btn yqx-btn-3 yqx-btn-small">查询</button>
                <button class="js-reset yqx-btn yqx-btn-1 yqx-btn-small">重置</button>
                <div class="show-more">
                    <i class="js-more-options options" data-target=".more-options"><i class="fa icon-small icon-plus"></i>更多筛选</i>
                </div>
            </div>
            <div class="more-options hide selects-box">
                <div class="show-grid">
                    <div class="input-group ">
                        <label>配件类型</label>

                        <div class="form-item">
                            <input class="yqx-input js-cat-1 js-show-tips"
                                   placeholder="一级分类"
                                    >
                            <span class="fa icon-small icon-angle-down"></span>
                            <input type="hidden" name="firstCatId">
                        </div>
                    </div>
                    <div class="input-group ">
                        <div class="form-item">
                            <input class="yqx-input js-cat-2 js-show-tips"
                                   placeholder="二级分类"
                                    >
                            <span class="fa icon-small icon-angle-down"></span>
                            <input type="hidden" name="secondCatId">
                        </div>
                    </div>
                    <div class="input-group ">
                        <div class="form-item">
                            <input class="yqx-input js-cat-3 js-show-tips"
                                   placeholder="三级分类"
                                    >
                            <span class="fa icon-small icon-angle-down"></span>
                            <input type="hidden" name="thirdCatId">
                        </div>
                    </div>


                </div>
                <div class="show-grid">
                    <div class="input-group">
                        <div class="form-item select-form">
                            <input type="text" class="yqx-input yqx-input-small js-select-order-status js-show-tips"
                                   placeholder="工单状态"
                                    >
                            <span class="icon-small fa icon-angle-down"></span>
                            <input type="hidden" name="orderStatus">
                        </div>
                    </div>
                    <div class="input-group">
                        <div class="form-item">
                            <input class="yqx-input yqx-input-long js-goods-brand js-show-tips"
                                   placeholder="配件品牌"
                                    >
                            <span class="fa icon-small icon-angle-down"></span>
                            <input type="hidden" name="orderGoodsBrandId">
                        </div>
                    </div>
                    <div class="input-group ">
                        <div class="form-item">
                            <input class="yqx-input js-saler js-show-tips"
                                   placeholder="销售员"
                                    >
                            <span class="fa icon-small icon-angle-down"></span>
                            <input type="hidden" name="orderGoodSaleId">
                        </div>
                    </div>
                    <div class="input-group ">
                        <div class="form-item">
                            <input class="yqx-input js-receiver js-show-tips"
                                   placeholder="服务顾问"
                                    >
                            <span class="fa icon-small icon-angle-down"></span>
                            <input type="hidden" name="orderReceiverId">
                        </div>
                    </div>
                </div>

            </div>
            <div class="lines"></div>
            <div class="form-options text-right">
                <div class="form-option">
                    <i class="js-list-option" data-target=".dropdown"><span class="icon-plus"></span>列表字段设置</i>
                </div>
                <div class="vertical-line"></div>
                <div class="form-option">
                    <a href="javascript:;" class="js-excel" data-target="goods" type="button"><i class="icon-signout"></i>导出excel</a>
                </div>
            </div>
            <div class="dropdown hide">
                <ul class="dropdown-menu">
                    <li><label><input type="checkbox" data-ref="create-date" checked>开单时间</label></li>
                    <li><label><input type="checkbox" data-ref="confirm-time" checked>结算时间</label></li>
                    <li><label><input type="checkbox" data-ref="order-status" checked>工单状态</label></li>
                    <li><label><input type="checkbox" data-ref="order-sn" checked>工单号</label></li>
                    <li><label><input type="checkbox" data-ref="car-license" checked>车牌</label></li>
                    <li><label><input type="checkbox" data-ref="car-models" checked>车型</label></li>
                    <li><label><input type="checkbox" data-ref="customer-name" checked>车主</label></li>
                    <li><label><input type="checkbox" data-ref="goods-name" checked>配件名称</label></li>
                    <li><label><input type="checkbox" data-ref="goods-format" checked>零件号</label></li>
                    <li><label><input type="checkbox" data-ref="inventory-price" checked>成本单价</label></li>
                    <li><label><input type="checkbox" data-ref="sale-price" checked>销售单价</label></li>
                    <li><label><input type="checkbox" data-ref="goods-number" checked>销售数量</label></li>
                    <li><label><input type="checkbox" data-ref="measure-unit" checked>单位</label></li>
                    <li><label><input type="checkbox" data-ref="inventory-amount-total" checked>成本总额</label></li>
                    <li><label><input type="checkbox" data-ref="sale-total-amount" checked>销售总额</label></li>
                    <li><label><input type="checkbox" data-ref="gross-profit" checked>毛利</label></li>
                    <li><label><input type="checkbox" data-ref="sale-name" checked>销售员</label></li>
                    <li><label><input type="checkbox" data-ref="receiver-name" checked>服务顾问</label></li>
                    <li><label><input type="checkbox" data-ref="cat-name" checked>配件类型</label></li>
                    <li><label><input type="checkbox" data-ref="brand-name" checked>配件品牌</label></li>
                </ul>
            </div>
        </div>
        <div class="table-box">
            <div class="scroll-x">
                <table class="yqx-table" data-local-storage="shopOrderGoodsDetail">
                    <thead>
                    <tr>
                        <th class="create-date">开单时间</th>
                        <th class="confirm-time">结算时间</th>
                        <th class="order-status">工单状态</th>
                        <th class="order-sn">工单号</th>
                        <th class="car-license">车牌</th>
                        <th class="car-models">车型</th>
                        <th class="customer-name">车主</th>
                        <th class="goods-name">配件名称</th>
                        <th class="goods-format">零件号</th>
                        <th class="inventory-price money-font">成本单价</th>
                        <th class="sale-price money-font">销售单价</th>
                        <th class="goods-number">销售数量</th>
                        <th class="measure-unit">单位</th>
                        <th class="inventory-amount-total money-font">成本总额</th>
                        <th class="sale-total-amount money-font">销售总额</th>
                        <th class="gross-profit money-font">毛利</th>
                        <th class="sale-name">销售员</th>
                        <th class="receiver-name">服务顾问</th>
                        <th class="cat-name">配件类型</th>
                        <th class="brand-name">配件品牌</th>
                    </tr>
                    </thead>
                    <tbody id="goodsFill">

                    </tbody>
                </table>
            </div>
        </div>
        <div class="total-box goods-box">
            查询结果：
            销售总额： <span class="money-font gross-sales"></span>
            成本总额： <span class="money-font cost"></span>
            毛利： <span class="money-font gross-profit"></span>
        </div>
        <div class="yqx-page" id="goodsPage"></div>
    </div>
    <!-- 配件销售 end -->
    <!-- 服务销售明细 start -->
    <div class="content fr services-content hide">
        <div class="content-head">
            <div class="tab-item goods-tab"
                 data-target=".goods-content" data-tab="0">配件销售明细
            </div>
            <div
                    class="tab-item services-tab" data-target=".services-content" data-tab="1">服务销售明细
            </div>
        </div>
        <div class="container search-form" id="servicesForm">
            <div class="show-grid input-width-155">
                <div class="input-group">
                    <label>开单时间</label>

                    <div class="form-item">
                        <input type="text" id="start2" name="orderCreateStartDate"
                               class="yqx-input" placeholder="选择开始时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                    <label>至</label>

                    <div class="form-item">
                        <input type="text" id="end2" name="orderCreateEndDate"
                               class="yqx-input" placeholder="选择结束时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                </div>
                <div class="input-group">
                    <label>结算时间</label>

                    <div class="form-item">
                        <input type="text" id="start3" name="orderSettleStartDate"
                               class="yqx-input" placeholder="选择开始时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                    <label>至</label>

                    <div class="form-item">
                        <input type="text" id="end3" name="orderSettleEndDate"
                               class="yqx-input" placeholder="选择结束时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="input-group selects-box">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-middle js-worker-2 js-show-tips"
                               placeholder="维修工"
                                >
                        <input type="hidden"
                               name="orderWorkerId">
                        <span class="fa icon-small icon-angle-down"></span>
                    </div>
                </div>
                <div class="input-group selects-box">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-middle js-service-cat js-show-tips"
                               placeholder="服务类别"
                                >
                        <input type="hidden"
                               name="serviceCateId">
                        <span class="fa icon-small icon-angle-down"></span>
                    </div>
                </div>
                <div class="input-group selects-box">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-middle js-receiver-2 js-show-tips"
                               placeholder="服务顾问"
                                >
                        <input type="hidden"
                               name="orderReceiverId">
                        <span class="fa icon-small icon-angle-down"></span>
                    </div>
                </div>
                <div class="input-group ">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-middle"
                               name="carType"
                               placeholder="车型"
                                >
                    </div>
                </div>
                <div class="input-group ">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-middle"
                               name="customerName"
                               placeholder="车主"
                                >
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="input-group selects-box">
                    <div class="form-item select-form">
                        <input type="text" class="yqx-input yqx-input-small js-select-order-status js-show-tips"
                               placeholder="工单状态"
                                >
                        <span class="icon-small fa icon-angle-down"></span>
                        <input type="hidden" name="orderStatus">
                    </div>
                </div>
                <div class="input-group ">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-middle"
                               name="serviceName"
                               placeholder="服务名称"
                                >
                    </div>
                </div>
                <div class="input-group ">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-middle"
                               name="orderNumber"
                               placeholder="工单号"
                                >
                    </div>
                </div>

                <div class="input-group ">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-middle"
                               name="carLicense"
                               placeholder="车牌"
                                >
                    </div>
                </div>
                <div class="btn-group fr">
                    <button class="js-search-btn yqx-btn yqx-btn-3 yqx-btn-small">查询</button>
                    <button class="js-reset yqx-btn yqx-btn-1 yqx-btn-small">重置</button>
                </div>
            </div>
            <div class="lines"></div>
            <div class="form-options text-right">
                <div class="form-option">
                    <i class="js-list-option" data-target=".dropdown"><span class="icon-plus"></span>列表字段设置</i>
                </div>
                <div class="vertical-line"></div>
                <div class="form-option">
                    <a href="javascript:;" class="js-excel" data-target="services" type="button"><i class="icon-signout"></i>导出excel</a>
                </div>
            </div>
            <div class="dropdown hide">
                <ul class="dropdown-menu">
                    <li><label><input type="checkbox" data-ref="create-date" checked>开单时间</label></li>
                    <li><label><input type="checkbox" data-ref="confirm-time" checked>结算时间</label></li>
                    <li><label><input type="checkbox" data-ref="order-status" checked>工单状态</label></li>
                    <li><label><input type="checkbox" data-ref="order-sn" checked>工单号</label></li>
                    <li><label><input type="checkbox" data-ref="car-license" checked>车牌</label></li>
                    <li><label><input type="checkbox" data-ref="car-models" checked>车型</label></li>
                    <li><label><input type="checkbox" data-ref="customer-name" checked>车主</label></li>
                    <li><label><input type="checkbox" data-ref="service-name" checked>服务名称</label></li>
                    <li><label><input type="checkbox" data-ref="cat-name" checked>服务类别</label></li>
                    <li><label><input type="checkbox" data-ref="service-price" checked>工时费</label></li>
                    <li><label><input type="checkbox" data-ref="service-hour" checked>工时</label></li>
                    <li><label><input type="checkbox" data-ref="service-amount" checked>金额</label></li>
                    <li><label><input type="checkbox" data-ref="discount" checked>优惠</label></li>
                    <li><label><input type="checkbox" data-ref="worker-name" checked>维修工</label></li>
                    <li><label><input type="checkbox" data-ref="receiver-name" checked>服务顾问</label></li>
                </ul>
            </div>
        </div>
        <div class="table-box">
            <div class="scroll-x">
                <table class="yqx-table" data-local-storage="shopOrderServicesDetail">
                    <thead>
                    <tr>
                        <th class="create-date">开单时间</th>
                        <th class="confirm-time">结算时间</th>
                        <th class="order-status">工单状态</th>
                        <th class="order-sn">工单号</th>
                        <th class="car-license">车牌</th>
                        <th class="car-models">车型</th>
                        <th class="customer-name">车主</th>
                        <th class="service-name">服务名称</th>
                        <th class="cat-name">服务类别</th>
                        <th class="service-price money-font">工时费</th>
                        <th class="service-hour">工时</th>
                        <th class="service-amount money-font">金额</th>
                        <th class="discount money-font">优惠</th>
                        <th class="worker-name">维修工</th>
                        <th class="receiver-name">服务顾问</th>
                    </tr>
                    </thead>
                    <tbody id="servicesFill">

                    </tbody>
                </table>
            </div>
        </div>
        <div class="total-box service-box">
            查询结果：
             金额： <span class="money-font total-hours-price"></span>
             工时： <span class="money-font total-hours"></span>
             优惠： <span class="money-font preferential"></span>
        </div>
        <div class="yqx-page" id="servicesPage"></div>
    </div>
    <!-- 服务销售明细 end -->
</div>

<script type="text/template" id="goodsTpl">
    <% var data = json.data && json.data.orderGoodsList; %>
    <% if(data && data.length) {%>
    <%for(var i in data) {%>
    <tr>
        <td class="create-date"><%=data[i].orderCreateDate%></td>
        <td class="confirm-time"><%=data[i].orderConfirmTime%></td>
        <td class="order-status"><%=data[i].orderStatus%></td>
        <td class="order-sn"><%=data[i].orderSn%></td>
        <td class="car-license"><%=data[i].carLicense%></td>
        <td class="car-models"><%=data[i].carModels%></td>
        <td class="customer-name"><%=data[i].customerName%></td>
        <td class="goods-name"><%=data[i].goodsName%></td>
        <td class="goods-format"><%=data[i].goodsFormat%></td>
        <td class="inventory-price money-font"><%=data[i].inventoryPrice%></td>
        <td class="sale-price money-font"><%=data[i].salePrice%></td>
        <td class="goods-number"><%=data[i].goodsNumber%></td>
        <td class="measure-unit"><%=data[i].measureUnit%></td>
        <td class="inventory-amount-total money-font"><%=data[i].inventoryAmountTotal%></td>
        <td class="sale-total-amount money-font"><%=data[i].saleTotalAmount%></td>
        <!--毛利 -->
        <%if(!data[i].orderConfirmTime || data[i].orderConfirmTime == ''){%>
        <td class="gross-profit">--</td>
        <%} else {%>
        <td class="gross-profit money-font"><%=data[i].grossProfit%></td>
        <%}%>
        <td class="sale-name"><%=data[i].saleName%></td>
        <td class="receiver-name"><%=data[i].receiverName%></td>
        <td class="cat-name"><%=data[i].catName%></td>
        <td class="brand-name"><%=data[i].brandName%></td>
    </tr>
    <%}%>
    <%} else {%>
    <tr>
        <td colspan="25">暂无数据</td>
    </tr>
    <%}%>
</script>
<script type="text/template" id="servicesTpl">
    <% var data = json.data && json.data.orderServicesList; %>
    <% if(data && data.length) {%>
    <%for(var i in data) {%>
    <tr>
        <td class="create-date"><%=data[i].orderCreateDate%></td>
        <td class="confirm-time"><%=data[i].orderConfirmTime%></td>
        <td class="order-status"><%=data[i].orderStatus%></td>
        <td class="order-sn"><%=data[i].orderSn%></td>
        <td class="car-license"><%=data[i].carLicense%></td>
        <td class="car-models"><%=data[i].carModels%></td>
        <td class="customer-name"><%=data[i].customerName%></td>
        <td class="service-name"><%=data[i].serviceName%></td>
        <td class="cat-name"><%=data[i].serviceCatName%></td>
        <td class="service-price money-font"><%=data[i].servicePrice%></td>
        <td class="service-hour"><%=data[i].serviceHour%></td>
        <td class="service-amount money-font"><%=data[i].serviceAmount%></td>
        <td class="discount money-font"><%=data[i].discount%></td>
        <td class="worker-name"><%=data[i].workerName%></td>
        <td class="receiver-name"><%=data[i].receiverName%></td>
    </tr>
    <%}%>
    <%} else {%>
    <tr>
        <td colspan="15">暂无数据</td>
    </tr>
    <%}%>
</script>
<script src="${BASE_PATH}/static/js/page/report/statistics/order/sale-detail.js?2cc5bf2bb313589906034d7502e31cb5"></script>
<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<#include "yqx/layout/footer.ftl">

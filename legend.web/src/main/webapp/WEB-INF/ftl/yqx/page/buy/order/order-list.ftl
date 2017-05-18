<#-- Created by sky on 16/11/24. -->
<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/buy/common.css?b3b5b13cc75d51ec75cf1538617ecf8b">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/buy/order/order-list.css?a533b6f817f692c7d2a23eb8cdfbb3f9">

<section class="yqx-wrapper">
    <section class="banner-panel">
    <#include "yqx/tpl/buy/banner.ftl">
    </section>

    <section class="nav-panel">
    <#include "yqx/tpl/buy/nav.ftl">
    </section>

    <section class="main" role="main">
        <ul class="condition-list clearfix" id="conditionList">
            <#list legendTradeStatusList as status>
            <li class="yqx-link<#if status.key == 'ALL'> active</#if>" data-condition="${status.key}">${status.name}<i>(${status.count})</i></li>
            </#list>
        </ul>

        <section class="order-list" id="orderListBox"></section>
    </section>

    <section class="yqx-page" id="pagingBox"></section>
</section>

<script type="text/html" id="orderListTpl" data-desc="订单列表">
    <% if (data && data.content && data.content.length) { %>
    <% for (var i = 0; i < data.content.length; i++) { %>
    <% var item = data.content[i]; %>
    <% var thisSrc = getFlag(item.orderFlags); %>
    <% var isSPRAY = item.orderFlags == 'SPRAY'; %>
    <table class="yqx-table yqx-table-big order-item">
        <thead>
        <tr>
            <th class="tc-1">
                <img class="order-type" src="<%= thisSrc %>">
            </th>
            <th class="text-left tc-2">订单编号:<%= item.orderSn %>-日期:<%= item.addTime %> </th>
            <th class="info-highlight tc-3">
                <% if (item.payName == '云修月结支付') { %>
                    账期订单-
                    <% if (item.tradeStats == 'CWQRSK') { %>
                        已付款
                    <% } else if (item.tradeStats != 'WXDD') { %>
                        未付款
                    <% } %>
                <% } %>
                <% if (item.tradeStats == 'WXDD') { %>已取消<% } %>
            </th>
            <th class="text-right" colspan="3">
                <span <% if('GXD,PHZ,YFH,YQS,YFK'.indexOf(item.legendTradeStatus) != -1) { %>class="finished"<% } %>>1.客户下单 -</span>
                <span <% if('PHZ,YFH,YQS,YFK'.indexOf(item.legendTradeStatus) != -1) { %>class="finished"<% } %>>2.配货中 -</span>
                <span <% if('YFH,YQS,YFK'.indexOf(item.legendTradeStatus) != -1) { %>class="finished"<% } %>>3.已发货 -</span>
                <span <% if('YQS,YFK'.indexOf(item.legendTradeStatus) != -1) { %>class="finished"<% } %>>4.<% if (!isSPRAY) { %>已签收<% } else { %>已收货<% } %> -</span>
                <span <% if('YFK' == item.legendTradeStatus) { %>class="finished"<% } %>>5.已付款</span>
            </th>
        </tr>
        </thead>
        <tbody>
        <% for (var j = 0; j < item.goodsList.length; j++) { %>
        <% var subItem = item.goodsList[j]; %>
        <tr>
            <td>
                <img class="goods-photo" src="<%= subItem.goodsImg %>" alt="<%= subItem.goodsName %>"
                     onerror="this.src='http://tqmall-image.oss-cn-hangzhou.aliyuncs.com/images/no_picture.gif'">
            </td>
            <td class="text-left" colspan="2">
                <p class="info-row ellipsis-2 js-show-tips"><%= subItem.goodsName %></p>
                <% if (subItem.goodsResult.data) { %>
                <p class="info-row info-highlight">配件库已存在</p>
                <% } else { %>
                <p class="info-row"><a href="${BASE_PATH}/shop/goods/goods-tqmall-list?goodsId=<%= subItem.goodsId %>" class="info-highlight">配件库里不存在,请点击添加</a></p>
                <% } %>
            </td>
            <td class="text-right">
                <p class="info-row">价格：
                    <% var flag = false; %>
                    <% if (subItem.goodsPrice < 0.1) { %>
                    <% flag = true; %>0.00
                    <% } else { %>
                    <%= subItem.goodsPrice %>
                    <% } %>
                    元</p>
                <% if (subItem.activityName) { %>
                <div class="info-row">
                    <span class="act-tag"><%= subItem.activityName %></span>
                </div>
                <% } %>
            </td>
            <td class="text-right">数量：<%= subItem.goodsNumber + subItem.measureUnit %></td>
            <td class="text-right">
                <p class="info-row">金额：<%= flag ? '0.00' : subItem.goodsAmount %>元</p>
                <% if (!isSPRAY) { %>
                <div class="info-row">
                    <a href="http://www.tqmall.com/Goods/detail.html?id=<%=subItem.goodsId%>" target="_blank" class="yqx-btn yqx-btn-1 yqx-btn-micro">再次采购</a>
                </div>
                <% } %>
            </td>
        </tr>
        <% } %>
        </tbody>
        <tfoot>
        <tr>
            <td class="text-left" colspan="3">
                <span>合计金额：<strong class="money"><%= item.goodsAmount %></strong>元</span>
                <% if (item.purchaseAmountDiscount) { %>
                <span>(采购金：<strong class="money"><%= item.purchaseAmountDiscount %></strong>元)</span>
                <% } %>
                <span>- 优惠：<strong class="money"><%= item.discount %></strong>元</span>
                <span>+ 运费：<strong class="money"><%= item.shippingFee %></strong>元</span>
                <% if (item.deductAmount > 0) { %>
                <span>- 奖励金：<strong class="money"><%= item.deductAmount %></strong></span>
                <% } %>
                <span>= 应付金额：<strong class="money"><%= item.orderAmount %></strong>元</span>
                <% if (item.orderFlags == 'HHDD') { %>
                <a href="javascript:;" class="yqx-link yqx-link-1 fr js-exchange-list-btn" data-refund-goods="<%= $toString(item.refundGoodsApplyDTOList); %>">换货清单</a>
                <% } %>
            </td>
            <td class="text-right" colspan="3">
                <input type="hidden" name="orderId" value="<%= item.orderId %>">
                <input type="hidden" name="uid" value="<%= item.userId %>">
                <input type="hidden" name="orderSn" value="<%= item.orderSn %>">
                <input type="hidden" name="flag" value="<%= item.showBatchAdd ? 1 : 0; %>">
                <% if (isSPRAY && 'YFK,YQS'.indexOf(item.legendTradeStatus) != -1) { %>
                <button class="yqx-btn yqx-btn-3  yqx-btn-micro" disabled>已收货</button>
                <% } %>
                <% if (isSPRAY && item.tradeStats == 'XEJHJS') { %>
                <button class="yqx-btn yqx-btn-3  yqx-btn-micro js-received-btn"
                        data-status="<%= item.tradeStats %>" data-order-flags="<%= item.orderFlags %>"
                        data-order-id="<%= item.orderId %>">确认收货</button>
                <% } %>
                <% if ('PHZ,YQS,YFH,YFK'.indexOf(item.legendTradeStatus) != -1) { %>
                <% if (item.warehouseResult.data == 0) { %>
                <button class="yqx-btn yqx-btn-4  yqx-btn-micro js-put-in-btn">签收入库</button>
                <% } else { %>
                <button class="yqx-btn yqx-btn-4  yqx-btn-micro" disabled>已入库</button>
                <% } %>
                <% } %>
            </td>
        </tr>
        </tfoot>
    </table>
    <% } } %>
</script>

<script type="text/html" id="exchangeListTpl" data-desc="换货清单">
    <dg-title>换货清单</dg-title>
    <dg-body>
        <table class="yqx-table yqx-table-hover yqx-table-border">
            <thead>
            <tr>
                <th class="extc-1"></th>
                <th class="text-left">产品名称</th>
                <th class="text-right extc-3">价格</th>
                <th class="extc-4">换货数量</th>
            </tr>
            </thead>
            <% var amount = 0; %>
            <% if (data && data.length) { %>
            <tbody>
            <% for (var i = 0; i < data.length; i++) { %>
            <% var item = data[i]; %>
            <% amount += item.soldPrice * item.goodsNumber; %>
            <tr>
                <td><img src="<%= item.goodsImage %>" alt="<%= item.goodsName %>"
                         class="exchange-goods-photo"
                         onerror="this.src='http://tqmall-image.oss-cn-hangzhou.aliyuncs.com/images/no_picture.gif'"></td>
                <td><p class="ellipsis-2 js-show-tips"><%= item.goodsName %></p></td>
                <td><%= item.soldPrice.toFixed(2) %></td>
                <td><%= item.goodsNumber %></td>
            </tr>
            <% } %>
            </tbody>
            <% } %>
        </table>
        <div class="amount-box">合计：&yen;<span><%= amount.toFixed(2) %></span></div>
    </dg-body>
    <dg-foot>
        <button class="yqx-btn yqx-btn-3 yqx-btn-small js-exclose-btn">确定</button>
    </dg-foot>
</script>

<script src="${BASE_PATH}/static/js/page/buy/order/order-list.js?ad27468e69fe788413f069a6c23607e8"></script>

<#include "yqx/layout/footer.ftl">

<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/common/report/report-common.css?c58c9a3ee3acf5c0dbc310373fe3c349">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/report/statistics/business/gross-profits.css?2bb4168cb7f8e5e86bb377f28baefbd2">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/report/statistics/order-info-detail.css?8830836bc556cfc5df6026b983b74100">
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/report/left-nav.ftl"/>
    <div class="content fr">
        <div class="container">
            <div id="searchForm">
                <div class="search-form">
                    <div class="show-grid input-width-middle">
                        <div class="input-group">
                            <label>选择日期：</label>
                            <div class="form-item">
                                <input type="text" id="startDate" name="startDate"
                                       class="yqx-input yqx-input-small" placeholder="选择开始时间">
                                <span class="fa icon-small icon-calendar"></span>
                            </div>
                            <label>至</label>

                            <div class="form-item w140">
                                <input type="text" id="endDate" name="endDate"
                                       class="yqx-input yqx-input-small" placeholder="选择结束时间">
                                <span class="fa icon-small icon-calendar"></span>
                            </div>
                            <button class="yqx-btn yqx-btn-3 search-btn yqx-btn-small">统计</button>
                            <div class="quick-filter js-filter-date">
                                <label for="">快速筛选：</label>
                                <i data-target="-1" class="js-quick-select" data-date="2017-04-06">昨天</i>
                                <i data-target="0" class="js-quick-select" data-date="2017-04-12">今天</i>
                            </div>
                        </div>
                    </div>
                    <div class="lines"></div>
                </div>
                <div class="form-options text-right">
                    <div class="form-option">
                        <a href="javascript:;"  id="excelBtn"  class="js-excel" data-target="services" type="button"><i
                                class="icon-signout"></i>导出excel</a>
                    </div>
                </div>
            </div>
        </div>
        <div class="table-box hide">
            <div class="gross-profits-table scroll-x" id="grossProfitsFill"></div>
        </div>
        <div class="total-box">
            查询结果：
            收入合计： <span class="money-font totalAmount"></span>
            物料成本： <span class="money-font totalInventoryAmount"></span>
            毛利： <span class="money-font grossProfits"></span>
            单数： <span class="money-font totalOrderCount"></span>
        </div>
        <div class="yqx-page" id="grossProfitsPage">
        </div>
    </div>
</div>
<script id="grossProfitsTpl" type="text/html">
    <table class="yqx-table">
        <thead>
        <tr>
            <th class="cb1">单号</th>
            <th class="cb2">车牌</th>
            <th class="cb3">客户</th>
            <th class="cb4">确认账单时间</th>
            <th class="cb5 money-font">物料销售收入</th>
            <th class="cb6 money-font">工时收入</th>
            <th class="cb7 money-font">其他收入</th>
            <th class="cb8 money-font">收入合计</th>
            <th class="cb9 money-font">物料成本</th>
            <th class="cb10 money-font">优惠</th>
            <th class="cb11 money-font">毛利</th>
            <th class="cb12">毛利率</th>
        </tr>
        </thead>
        <% var i, item; %>
        <tbody>
        <% if(json.data && json.data.content && json.data.content.length) {%>
        <%
        for(i = 0; i < json.data.content.length; i++) {
        item = json.data.content[i];
        %>
        <tr>
            <td class="cb1"><%= item.orderSn %></td>
            <td class="cb2"><%= item.carLicense %></td>
            <td class="cb3"><%= item.customerName %></td>
            <td class="cb4"><%= item.confirmTimeStr %></td>
            <td class="cb5 money-font"><%= item.goodsFinalAmount %></td>
            <td class="cb6 money-font"><%= item.serviceFinalAmount %></td>
            <td class="cb7 money-font"><%= item.feeFinalAmount %></td>
            <td class="cb8 money-font"><%= item.orderAmount %></td>
            <td class="cb9 money-font"><%= item.inventoryFinalAmount %></td>
            <td class="cb10 money-font"><%= item.discountAmount %></td>
            <td class="cb11 money-font"><%= item.grossProfits %></td>
            <td class="cb12"><%= item.grossProfitsRate %>%</td>
        </tr>
        <% } %>
        <%} else {%>
        <tr><td colspan="26">暂无数据</td></tr>
        <%}%>
        </tbody>
    </table>
</script>
<script src="${BASE_PATH}/static/js/page/report/statistics/business/gross-profits.js?3009ddd07e57ff94367598cb767498f0"></script>
<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<#include "yqx/layout/footer.ftl">
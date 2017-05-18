<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/common/report/report-common.css?c58c9a3ee3acf5c0dbc310373fe3c349">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/report/statistics/order-info-detail.css?8830836bc556cfc5df6026b983b74100">
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/report/left-nav.ftl"/>
    <div class="content fr">
        <input type="hidden" id="istqmall" value="${SESSION_SHOP_IS_TQMALL_VERSION}">
        <div class="container">
            <form id="searchForm">
                <div class="search-form">
                    <div class="show-grid input-width-middle">
                        <div class="input-group">
                            <label>结算时间</label>
                            <input id="ssTime" type="hidden" value="${sPayTime}"/>
                            <div class="form-item">
                                <input type="text" id="sPayTime" name="search_sPayTime" value="${sPayTime}"
                                       class="yqx-input yqx-input-small" placeholder="选择开始时间">
                                <span class="fa icon-small icon-calendar"></span>
                            </div>
                            <label>至</label>

                            <div class="form-item w140">
                                <input type="text" id="ePayTime" name="search_ePayTime" value="${ePayTime}"
                                       class="yqx-input yqx-input-small" placeholder="选择结束时间">
                                <span class="fa icon-small icon-calendar"></span>
                            </div>
                        </div>
                        <div class="input-group">

                            <div class="form-item w140">
                                <input type="text" id="license" class="yqx-input yqx-input-small"
                                       name="search_license"
                                       placeholder="车牌"
                                        >
                            </div>
                        </div>
                        <div class="input-group">
                            <div class="form-item w140">
                                <input type="text" id="customerName" class="yqx-input yqx-input-small"
                                       name="search_customerName"
                                       placeholder="车主"
                                        >
                            </div>
                        </div>
                    </div>
                    <div class="show-grid">
                        <div class="input-group ">
                            <div class="form-item">
                                <input type="text" id="orderSn" class="yqx-input yqx-input-small"
                                       placeholder="工单编号"
                                       name="search_orderSn"
                                        >
                            </div>
                        </div>
                        <div class="input-group ">
                            <div class="form-item select-form">
                                <input class="yqx-input yqx-input-small js-status-select js-show-tips""
                                       placeholder="结算类型"
                                        >
                                <input type="hidden" id="status" name="search_status">
                                <span class="fa icon-angle-down icon-small"></span>
                            </div>
                        </div>

                        <div class="input-group ">
                            <div class="form-item select-form">
                                <input class="yqx-input yqx-input-small js-server-select js-show-tips"
                                       placeholder="服务顾问"
                                        >
                                <input type="hidden" id="serverId" class="yqx-input yqx-input-small"
                                       name="search_serverId"
                                        >

                                <div class="yqx-select-options" style="display: none;width: 150px;">
                                    <dl>
                                        <dd class="yqx-select-option" data-key="">请选择</dd>
                                    <#list shopManagers as item >
                                        <dd data-key="${item.id}" class="yqx-select-option">${item.name}</dd>
                                    </#list>
                                    </dl>
                                </div>
                                <span class="fa icon-angle-down icon-small"></span>
                            </div>
                        </div>
                        <div class="input-group">
                            <div class="form-item hide select-form">
                                <input class="yqx-input yqx-input-small js-select-order-tag js-show-tips"
                                       placeholder="工单类型" value="${orderTagName}"
                                >
                                <input type="hidden"
                                       name="search_orderTag" value="${orderTag}">
                                <span class="icon-angle-down fa icon-small"></span>
                            </div>
                        </div>
                        <div class="search-btns fr">
                            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn" type="button">查询</button>
                        </div>
                    </div>
                </div>
            </form>
            <div class="lines"></div>
            <div class="form-options text-right">
                <div class="form-option">
                    <i class="js-list-option" data-target=".dropdown"><span class="icon-plus"></span>列表字段设置</i>
                </div>
                <div class="vertical-line"></div>
                <div class="form-option">
                    <a href="javascript:;" id="excelBtn" class="mt14ml25" type="button"><i class="icon-signout"></i>导出excel</a>
                </div>
            </div>
            <div class="dropdown hide">
                <ul class="dropdown-menu">
                    <li><label for="cb1"><input type="checkbox" data-ref="cb1" checked/>收款时间</label></li>
                    <li><label for="cb2"><input type="checkbox" data-ref="cb2" checked/>工单编号</label></li>
                    <li><label for="cb3"><input type="checkbox" data-ref="cb3" checked/>工单类型</label></li>
                    <li><label for="cb4"><input type="checkbox" data-ref="cb4" checked/>车牌</label></li>
                    <li><label for="cb5"><input type="checkbox" data-ref="cb5" checked/>车主</label></li>
                    <li><label for="cb6"><input type="checkbox" data-ref="cb6" checked/>总计</label></li>
                    <li><label for="cb7"><input type="checkbox" data-ref="cb7" checked/>应收金额</label></li>
                    <li><label for="cb8"><input type="checkbox" data-ref="cb8" checked/>物料成本</label></li>
                    <li><label for="cb9"><input type="checkbox" data-ref="cb9" checked/>毛利</label></li>
                    <li><label for="cb10"><input type="checkbox" data-ref="cb10" checked/>实收金额</label></li>
                    <li><label for="cb11"><input type="checkbox" data-ref="cb11" checked/>现金</label></li>
                    <li><label for="cb12"><input type="checkbox" data-ref="cb12" checked/>会员卡</label></li>
                    <li><label for="cb13"><input type="checkbox" data-ref="cb13" checked/>银行转账</label></li>
                    <li><label for="cb14"><input type="checkbox" data-ref="cb14" checked/>支付宝支付</label></li>
                    <li><label for="cb15"><input type="checkbox" data-ref="cb15" checked/>信用卡</label></li>
                    <li><label for="cb16"><input type="checkbox" data-ref="cb16" checked/>储蓄卡</label></li>
                    <li><label for="cb17"><input type="checkbox" data-ref="cb17" checked/>微信支付</label></li>
                    <li><label for="cb18"><input type="checkbox" data-ref="cb18" checked/>转账支票</label></li>
                    <li><label for="cb19"><input type="checkbox" data-ref="cb19" checked/>现金支票</label></li>
                    <li><label for="cb20"><input type="checkbox" data-ref="cb20" checked/>其它</label></li>
                    <li><label for="cb21"><input type="checkbox" data-ref="cb21" checked/>自定义</label></li>
                    <li><label for="cb22"><input type="checkbox" data-ref="cb22" checked/>挂账</label></li>
                    <li><label for="cb23"><input type="checkbox" data-ref="cb23" checked/>结算状态</label></li>
                    <li><label for="cb24"><input type="checkbox" data-ref="cb24" checked/>坏账金额</label></li>
                    <li><label for="cb25"><input type="checkbox" data-ref="cb25" checked/>结算人</label></li>
                    <li><label for="cb26"><input type="checkbox" data-ref="cb26" checked/>服务顾问</label></li>
                </ul>
            </div>
            </div>

            <div class="table-box hide">
                <div class="payment-table scroll-x" id="paymentFill"></div>
            </div>
            <div class="total-box">
                查询结果：
                应收金额： <span class="money-font payAmount"></span>
                毛利： <span class="money-font grossAmount"></span>
                挂帐： <span class="money-font signAmount"></span>
                坏帐： <span class="money-font badAmount"></span>
            </div>
            <div class="yqx-page" id="paymentPage">
            </div>
        </div>
    </div>

</div>
<script id="paymentTpl" type="text/html">
    <table class="yqx-table">
        <thead>
        <tr>
            <th class="cb1">收款时间</th>
            <th class="cb2">工单编号</th>
            <th class="cb3">工单类型</th>
            <th class="cb4">车牌</th>
            <th class="cb5">车主</th>
            <th class="cb6">总计</th>
            <th class="cb7">应收金额</th>
            <th class="cb8">物料成本</th>
            <th class="cb9">毛利</th>
            <th class="cb10">实收金额</th>
            <th class="cb11 money-font">现金</th>
            <th class="cb12 money-font">会员卡</th>
            <th class="cb13 money-font">银行转账</th>
            <th class="cb14 money-font">支付宝支付</th>
            <th class="cb15 money-font">信用卡</th>
            <th class="cb16 money-font">储蓄卡</th>
            <th class="cb17 money-font">微信支付</th>
            <th class="cb18 money-font">转账支票</th>
            <th class="cb19 money-font">现金支票</th>
            <th class="cb20 money-font">其它</th>
            <th class="cb21 money-font">自定义</th>
            <th class="cb22">挂账</th>
            <th class="cb23">结算状态</th>
            <th class="cb24">坏账金额</th>
            <th class="cb25">结算人</th>
            <th class="cb26">服务顾问</th>
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
            <td class="cb1"><%= item.statisDate %></td>
            <td class="cb2"><%= item.orderSn %></td>
            <td class="cb3"><%= item.orderTagStr %></td>
            <td class="cb4"><%= item.license %></td>
            <td class="cb5 hiddenLong" title="<%= item.customerName %>"><%= item.customerName %></td>
            <td class="cb6"><%= item.totalPayAmount %></td>
            <td class="cb7"><%= item.totalAmount %></td>
            <td class="cb8"><%= item.costAmount %></td>
            <td class="cb9"><%= item.grossProfit %></td>
            <td class="cb10"><%= item.payAmount %></td>
            <td class="cb11 money-font"><%= item.cash %></td>
            <td class="cb12 money-font"><%= item.member %></td>
            <td class="cb13 money-font"><%= item.bank %></td>
            <td class="cb14 money-font"><%= item.thirdPay %></td>
            <td class="cb15 money-font"><%= item.credit %></td>
            <td class="cb16 money-font"><%= item.card %></td>
            <td class="cb17 money-font"><%= item.wei %></td>
            <td class="cb18 money-font"><%= item.transfer %></td>
            <td class="cb19 money-font"><%= item.check %></td>
            <td class="cb20 money-font"><%= item.other %></td>
            <td class="cb21 money-font"><%= item.defined %></td>
            <td class="cb22"><%= item.signAmount %></td>
            <td class="cb23"><%= item.status %></td>
            <td class="cb24"><%= item.bad %></td>
            <td class="cb25"><%= item.operatorName %></td>
            <td class="cb26"><%= item.serverName %></td>
        </tr>
        <% } %>
        <%} else {%>
        <tr><td colspan="26">暂无数据</td></tr>
        <%}%>
        </tbody>
    </table>
</script>

<script src="${BASE_PATH}/static/js/page/report/statistics/order/payment.js?3b7ba325d62c72635a243a4f2933b520"></script>

<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<#include "yqx/layout/footer.ftl" >
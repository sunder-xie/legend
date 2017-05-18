<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/common/report/report-common.css?c58c9a3ee3acf5c0dbc310373fe3c349">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/report/statistics/order-info-detail.css?8830836bc556cfc5df6026b983b74100">
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/report/left-nav.ftl"/>
    <input type="hidden" id="istqmall" value="${SESSION_SHOP_IS_TQMALL_VERSION}">
    <div class="content fr">
        <div class="container">
            <form id="searchForm" class="search-form">
                <div class="show-grid">
                    <div class="input-group">
                        <label>开单时间</label>
                        <div class="form-item">
                            <input type="text" id="createTimeStart" name="search_createTimeStart" value="${createTimeStart}"
                                   class="yqx-input yqx-input-small js-date" placeholder="开始时间">
                            <span class="fa icon-small icon-calendar"></span>
                        </div>
                        <label>至</label>

                        <div class="form-item">
                            <input type="text" id="createTimeEnd" name="search_createTimeEnd" value="${createTimeEnd}"
                                   class="yqx-input yqx-input-small js-date" placeholder="结束时间">
                            <span class="fa icon-small icon-calendar"></span>
                        </div>
                    </div>
                    <div class="input-group fr">
                        <label>结算时间</label>

                        <div class="form-item">
                            <input type="text" id="payTimeStart" name="search_payTimeStart" value="${payTimeStart}"
                                   class="yqx-input yqx-input-small" placeholder="开始时间">
                            <span class="fa icon-small icon-calendar"></span>
                        </div>
                        <label>至</label>

                        <div class="form-item">
                            <input type="text" id="payTimeEnd" name="search_payTimeEnd" value="${payTimeEnd}"
                                   class="yqx-input yqx-input-small" placeholder="结束时间">
                            <span class="fa icon-small icon-calendar"></span>
                        </div>
                    </div>

                </div>
                <div class="show-grid heit input-width-xs">
                     <div class="input-group">
                        <div class="form-item">
                            <input type="text" id="carLicense" class="yqx-input yqx-input-small"
                                   name="search_carLicense"
                                   placeholder="车牌"
                                    >
                        </div>
                    </div>
                    <div class="input-group">
                        <div class="form-item">
                            <input type="text" id="customerName" class="yqx-input yqx-input-small"
                                   name="search_customerName"
                                   placeholder="车主"
                                    >
                        </div>
                    </div>
                    <div class="input-group">
                        <div class="form-item">
                            <input type="text" id="orderSn" class="yqx-input yqx-input-small"
                                   name="search_orderSn"
                                   placeholder="工单编号"
                                    >
                        </div>
                    </div>

                    <div class="input-group">

                        <div class="form-item hide select-form">
                            <input class="yqx-input yqx-input-small js-select-order-tag js-show-tips"
                                   placeholder="工单类型"
                                    >
                            <input type="hidden" name="search_orderTag">
                            <span class="icon-angle-down fa icon-small"></span>
                        </div>
                    </div>

                    <div class="input-group">
                        <div class="form-item select-form">
                            <input type="text" class="yqx-input yqx-input-small js-select-order-status js-show-tips"
                                   placeholder="工单状态" value="${orderStatusName}"
                                    >
                            <span class="icon-small fa icon-angle-down"></span>
                            <input type="hidden" name="search_orderStatus" value="${orderStatus}">
                        </div>
                    </div>

                </div>
                <div class="show-grid heit input-width-xs">

                    <div class="input-group">

                        <div class="select-form form-item">
                            <input type="text" class="yqx-input yqx-input-small js-select-receiver js-show-tips" placeholder="服务顾问">
                            <span class="fa icon-angle-down icon-small"></span>
                            <input type="hidden" name="search_receiver">
                        </div>

                    </div>
                    <div class="input-group">

                        <div class="form-item select-form">
                            <input type="text" class="yqx-input yqx-input-small js-select-saler js-show-tips"
                                   placeholder="销售员"
                                    >
                            <span class="fa icon-small icon-angle-down"></span>
                            <input type="hidden" name="search_saler">
                        </div>
                    </div>
                   <div class="input-group">

                        <div class="form-item select-form">
                            <input type="text" class="yqx-input yqx-input-small js-select-worker js-show-tips"
                                   placeholder="维修工">
                            <span class="fa icon-small icon-angle-down"></span>
                            <input type="hidden" name="search_worker">
                        </div>
                   </div>
                    <div class="fr">
                        <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn" id="searchBtn" type="button">查询
                        </button>
                    </div>
                </div>
            </form>
            <div class="lines"></div>
            <div class="form-options text-right">
                <div class="form-option">
                    <i class="js-list-option" data-target=".dropdown"><i class="fa icon-plus icon-small"></i>列表字段设置</i>
                </div>
                <div class="vertical-line"></div>
                <div class="form-option">
                    <a href="javascript:;" id="excelBtn" class="mt14ml25" type="button"><i class="icon-signout"></i>导出excel</a>
                </div>
            </div>

            <div class="dropdown hide">
                <ul class="dropdown-menu">
                    <li><label for="cb1"><input type="checkbox" data-ref="cb1" checked/>工单编号</label></li>
                    <li><label for="cb2"><input type="checkbox" data-ref="cb2" checked/>工单类型</label></li>
                    <li><label for="cb3"><input type="checkbox" data-ref="cb3" checked/>业务类型</label></li>
                    <li><label for="cb4"><input type="checkbox" data-ref="cb4" checked/>车牌</label></li>
                    <li><label for="cb5"><input type="checkbox" data-ref="cb5" checked/>车主</label></li>
                    <li><label for="cb6"><input type="checkbox" data-ref="cb6" checked/>开单时间</label></li>
                    <li><label for="cb7"><input type="checkbox" data-ref="cb7" checked/>结算时间</label></li>
                    <li><label for="cb8"><input type="checkbox" data-ref="cb8" checked/>工单状态</label></li>
                    <li><label for="cb9"><input type="checkbox" data-ref="cb9" checked/>服务顾问</label></li>
                    <li><label for="cb10"><input type="checkbox" data-ref="cb10" checked/>销售员</label></li>
                    <li><label for="cb11"><input type="checkbox" data-ref="cb11" checked/>维修工</label></li>
                    <li><label for="cb12"><input type="checkbox" data-ref="cb12" checked/>工时</label></li>
                    <li><label for="cb14"><input type="checkbox" data-ref="cb14" checked/>总计</label></li>
                    <li><label for="cb13"><input type="checkbox" data-ref="cb13" checked/>应收金额</label></li>
                    <li><label for="cb18"><input type="checkbox" data-ref="cb18" checked/>附加费用</label></li>
                    <li><label for="cb15"><input type="checkbox" data-ref="cb15" checked/>物料成本</label></li>
                    <li><label for="cb16"><input type="checkbox" data-ref="cb16" checked/>毛利</label></li>
                    <li><label for="cb17"><input type="checkbox" data-ref="cb17" checked/>毛利率</label></li>
                </ul>
            </div>
        </div>
        <div class="table-box hide">
            <div id="infoFill" class="scroll-x"></div>
        </div>
        <div class="total-box">
            查询结果：
            总计： <span class="money-font payAmount"></span>
            应收金额： <span class="money-font grossAmount"></span>
            毛利： <span class="money-font signAmount"></span>
        </div>
        <div class="yqx-page" id="infoPage"></div>
    </div>
</div>
<script id="infoTpl" type="text/html">
    <table class="yqx-table">
        <thead>
        <tr>
            <th class="cb1">工单编号</th>
            <th class="cb2">工单类型</th>
            <th class="cb3">业务类型</th>
            <th class="cb4">车牌</th>
            <th class="cb5">车主</th>
            <th class="cb6">开单时间</th>
            <th class="cb7">结算时间</th>
            <th class="cb8">工单状态</th>
            <th class="cb9">服务顾问</th>
            <th class="cb10">销售员</th>
            <th class="cb11">维修工</th>
            <th class="cb12">工时</th>
            <th class="cb14">总计</th>
            <th class="cb13">应收金额</th>
            <th class="cb18">附加费用</th>
            <th class="cb15">物料成本</th>
            <th class="cb16">毛利</th>
            <th class="cb17">毛利率</th>
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
            <td class="cb2"><%= item.orderTag %></td>
            <td class="cb3"><%= item.orderType %></td>
            <td class="cb4"><%= item.carLicense %></td>
            <td class="cb5 hiddenLong" title="<%= item.customerName %>"><%= item.customerName %></td>
            <td class="cb6"><%= item.createTime %></td>
            <td class="cb7"><%= item.payTime %></td>
            <td class="cb8"><%= item.orderStatus %></td>
            <td class="cb9"><%= item.receiverName %></td>
            <td class="cb10"><%= item.salerName %></td>
            <td class="cb11 js-show-tips"><%= item.workers %></td>
            <td class="cb12"><%= item.serviceHour %></td>
            <td class="cb14"><%= item.allTotalAmount %></td>
            <%if(item.orderStatus != "已结算"){%>
            <td class="cb13">--</td>
            <%}else{%>
            <td class="cb13"><%= item.totalAmount %></td>
            <%}%>
            <%if(item.orderTag != "综合维修"){ %>
            <td class="cb18">--</td>
            <%}else{%>
            <td class="cb18"><%= item.extraAmount %></td>
            <%}%>
            <td class="cb15"><%= item.goodsAmount %></td>
            <%if(item.orderStatus != "已结算"){%>
            <td class="cb16">--</td>
            <%}else{%>
            <td class="cb16"><%= item.grossProfitAmount %></td>
            <%}%>
            <%if(item.orderStatus != "已结算"){%>
            <td class="cb17">--</td>
            <%}else{%>
            <td class="cb17"><%= item.grossProfitRate %>%</td>
            <%}%>
        </tr>
        <% } %>
        <%} else {%>
        <tr><td colspan="18">暂无数据</td></tr>
        <%}%>
        </tbody>
    </table>
</script>
<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">

<script src="${BASE_PATH}/static/js/page/report/statistics/order/info-detail.js?b9fe2292b6275952f5c38b76064f03f7"></script>



<#include "yqx/layout/footer.ftl" >
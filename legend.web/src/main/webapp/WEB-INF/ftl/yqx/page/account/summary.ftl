<#include "yqx/layout/header.ftl">
<#--卡券汇总-->
<#--样式引入区-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/summary.css?33bd445060be24451e88ff76e7929bf7" type="text/css"/>
<#--样式引入区-->
<div class="yqx-wrapper clearfix">
    <div class="aside">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="aside-main member current-main">
        <h3 class="Z-title">客户营销  > <a href="${BASE_PATH}/account">客户查询</a> > <i>优惠详情</i></h3>
        <div class="tab-box">
            <div class="tab">
                <div class="list-tab current-tab js-tab"
                     data-desc="member"
                     data-target=".aside-main.member">
                    会员卡
                </div><div class="list-tab js-tab"
                           data-desc="combo"
                           data-target=".aside-main.combo">
                    计次卡
                </div>
            </div>
            <div class="form clearfix" id="memberForm">
                <div class="form-item has-icon">
                    <input class="yqx-input yqx-input-small js-card-type" placeholder="会员卡类型">
                    <input type="hidden" name="cardInfoId">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <div class="form-item">
                    <input class="yqx-input yqx-input-small"
                           name="cardNum"
                           placeholder="会员卡号">
                </div>
                <div class="form-item">
                    <input class="yqx-input yqx-input-small"
                           name="mobile"
                           placeholder="车主电话">
                </div>
                <div class="form-item">
                    <input class="yqx-input yqx-input-small"
                           name="customerName"
                           placeholder="车主姓名">
                </div>
                <input type="hidden" name="sort" value="">
                <button class="fr yqx-btn yqx-btn-1 js-reset yqx-btn-small">重置</button>
                <button class="fr yqx-btn yqx-btn-3 js-search-btn yqx-btn-small" data-desc="member">查询</button>
            </div>
        </div>
        <div class="export-box clearfix">
            <div class="export-i fr js-excel"
                 data-desc="member"
                 title="导出会员卡汇总">
                <img src="${BASE_PATH}/static/img/page/account/export-icon.png">导出Excel
            </div>
        </div>
        <div class="container">
            <ul class="table-result clearfix">
                <li>查询结果：</li>
                <li class="gary">有效会员卡总数 <i class="money-font numberSummary">0</i> 张</li>
                <li class="gary">余额合计 <i class="money-font balanceSummary">0</i> 元</li>
                <li class="gary">累计充值金额合计 <i class="money-font depositSummary">0</i> 元</li>
            </ul>
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>车主</th>
                    <th>车主电话</th>
                    <th>会员卡类型</th>
                    <th>会员卡号</th>
                    <th class="has-sort js-sort text-r"
                        data-key="balance"
                        title="">
                        会员卡余额<span class="sort-i" data-target=""></span>
                    </th>
                    <th class="text-r">累计充值金额</th>
                    <th class="has-sort js-sort"
                        data-key="expireDate"
                        title="">
                        过期时间<span class="sort-i" data-target=""></span>
                    </th>
                </tr>
                </thead>
                <tbody id="memberFill"></tbody>
            </table>
            <ul class="table-result clearfix">
                <li>查询结果：</li>
                <li class="gary">有效会员卡总数 <i class="money-font numberSummary">0</i> 张</li>
                <li class="gary">余额合计 <i class="money-font balanceSummary">0</i> 元</li>
                <li class="gary">累计充值金额合计 <i class="money-font depositSummary">0</i> 元</li>
            </ul>
        </div>
        <div class="yqx-page" id="memberPage"></div>
    </div>
    <#--计次卡部分-->
    <div class="aside-main combo hide">
        <h3 class="Z-title">客户营销  > <a href="${BASE_PATH}/account">客户查询</a> > <i>优惠详情</i></h3>
        <div class="tab-box">
            <div class="tab">
                <div class="list-tab current-tab js-tab"
                     data-desc="member"
                     data-target=".aside-main.member">
                    会员卡
                </div><div class="list-tab js-tab"
                           data-desc="combo"
                           data-target=".aside-main.combo">
                    计次卡
                </div>
            </div>
            <div class="form clearfix" id="comboForm">
                <div class="form-item has-icon">
                    <input class="yqx-input yqx-input-small js-combo-type" placeholder="计次卡类型">
                    <input type="hidden" name="comboInfoId">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <div class="form-item has-icon">
                    <input class="yqx-input yqx-input-small js-combo-service" placeholder="计次卡服务项目">
                    <input type="hidden" name="serviceId">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <div class="form-item">
                    <input class="yqx-input yqx-input-small" name="mobile" placeholder="车主电话">
                </div>
                <div class="form-item">
                    <input class="yqx-input yqx-input-small" name="customerName" placeholder="车主姓名">
                </div>
                <input type="hidden" name="sort" value="">
                <button class="fr yqx-btn yqx-btn-1 js-reset yqx-btn-small">重置</button>
                <button class="fr yqx-btn yqx-btn-3 js-search-btn yqx-btn-small" data-desc="combo">查询</button>
            </div>
        </div>
        <div class="export-box clearfix">
            <div class="js-excel export-i fr" data-desc="combo"
                 title="导出计次卡汇总">
                <img src="${BASE_PATH}/static/img/page/account/export-icon.png">导出Excel
            </div>
        </div>
        <div class="container">
            <ul class="table-result clearfix">
                <li>查询结果：</li>
                <li class="gary">有效计次卡总数 <i class="money-font comboNumberSummary">0</i> 张</li>
                <li class="gary">服务项目合计 <i class="money-font serviceNumberSummary">0</i> 项</li>
                <li class="gary">服务项目剩余次数合计 <i class="money-font comboServiceRemainSummary">0</i> 次</li>
            </ul>
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>车主</th>
                    <th>车主电话</th>
                    <th>计次卡类型</th>
                    <th>计次卡服务项目</th>
                    <th class="js-sort has-sort"
                        data-key="remainNumber"
                    >
                        剩余次数<span class="sort-i" data-target=""></span>
                    </th>
                    <th class="js-sort has-sort"
                        data-key="expireDate"
                    >
                        过期时间<span class="sort-i" data-target=""></span>
                    </th>
                </tr>
                </thead>
                <tbody id="comboFill"></tbody>
            </table>
            <ul class="table-result clearfix">
                <li>查询结果：</li>
                <li class="gary">有效计次卡总数 <i class="money-font comboNumberSummary">0</i> 张</li>
                <li class="gary">服务项目合计 <i class="money-font serviceNumberSummary">0</i> 项</li>
                <li class="gary">服务项目剩余次数合计 <i class="money-font comboServiceRemainSummary">0</i> 次</li>
            </ul>
        </div>
        <div class="yqx-page" id="comboPage"></div>
    </div>
</div>

<script type="text/template" id="memberTpl">
    <% var t;%>
    <% if(json.success && json.data && json.data.content && json.data.content.length) {%>
    <%for(var i in json.data.content) {%>
    <%t = json.data.content[i];%>
    <tr>
        <td><%=(json.data.number - 1 ) * json.data.size + (+i + 1)%></td>
        <td class="js-show-tips"><%=t.customerName%></td>
        <td><a href="${BASE_PATH}/account/detail?customerId=<%=t.customerId%>"><%=t.mobile%></a></td>
        <td class="js-show-tips"><%=t.cardInfo%></td>
        <td><%=t.cardNum%></td>
        <td class="text-r money-font"><%=t.balance%></td>
        <td class="text-r money-font"><%=t.cumulativeDeposit%></td>
        <td><%=t.expireDate ? new Date(t.expireDate).getFormat('yyyy-MM-dd') : '-'%></td>
    </tr>
    <%}%>
    <%}%>
    <%if( i == null) {%>
    <tr>
        <td colspan="8">暂无数据</td>
    </tr>
    <%}%>
</script>

<script type="text/template" id="comboTpl">
    <% var t;%>
    <% if(json.success && json.data && json.data.content && json.data.content.length) {%>
    <%for(var i in json.data.content) {%>
    <%t = json.data.content[i];%>
    <tr>
        <td><%=(json.data.number - 1 ) * json.data.size + (+i + 1)%></td>
        <td class="js-show-tips"><%=t.customerName%></td>
        <td><a href="${BASE_PATH}/account/detail?customerId=<%=t.customerId%>"><%=t.mobile%></a></td>
        <td class="js-show-tips"><%=t.comboInfo%></td>
        <td class="js-show-tips"><%=t.comboService%></td>
        <td><%=t.remainNumber%></td>
        <td><%=t.expireDate ? new Date(t.expireDate).getFormat('yyyy-MM-dd') : '-'%></td>
    </tr>
    <%}%>
    <%}%>
    <%if( i == null) {%>
    <tr>
        <td colspan="7">暂无数据</td>
    </tr>
    <%}%>
</script>

<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<script src="${BASE_PATH}/static/js/page/account/summary.js?a28bd97321214cd8b883f1ee76a103e0"></script>
<#include "yqx/layout/footer.ftl">

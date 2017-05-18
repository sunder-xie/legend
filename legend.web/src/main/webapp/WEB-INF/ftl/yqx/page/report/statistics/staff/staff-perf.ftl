<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/common/report/report-common.css?c58c9a3ee3acf5c0dbc310373fe3c349">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/report/statistics/staff/perf-common.css?9698ce167cd7842fcea413c2f989cdff">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/report/statistics/staff/staff-perf.css?5b13a71b45f64e59b4551c1a3682afb3">
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/report/left-nav.ftl"/>
    <input type="hidden" id="isYBD" value="${YBD}">
<#assign now = .now>

    <div class="aside-main fl setting-main content perf-content">
        <h2 class="headline">绩效管理</h2>

        <div class="container service-form service" id="form">
            <div class="show-grid form">
                <label>选择日期</label>

                <div class="form-item form-date">
                    <input class="yqx-input js-date"
                           value="${now?string["yyyy-MM"]}"
                           name="dateStr"><i class="fa icon-calendar"></i>
                </div>
                <button class="yqx-btn yqx-btn-3 js-search-btn">查询</button>
                <div class="fr rule-box js-rule-detail">
                    <i class="rule-detail font-yahei">当月绩效规则</i><i class="c-question-i"></i>
                </div>
            </div>
        </div>
        <div class="container">
            <div class="container-header">
                <i class="table-header-icon"></i>个人业绩汇总
            </div>
            <div class="table-box">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>员工</th>
                        <th>维修业绩</th>
                        <th>维修提成</th>
                        <th>销售业绩</th>
                        <th>销售提成</th>
                        <th>服务顾问业绩</th>
                        <th>服务顾问提成</th>
                        <th>到店新客户</th>
                        <th>新客户提成</th>
                        <th>客户消费</th>
                        <th>客户消费提成</th>
                        <th>销售之星提成</th>
                        <th>总提成</th>
                    </tr>
                    </thead>
                    <tbody id="fill">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script type="text/template" id="tpl">
    <% if(json.success && json.data ) {%>
    <% var t = json.data;%>
    <% var total = 0;%>
    <tr>
        <td class="js-show-tips">
            <%=t.empName%>
        </td>
        <td class="js-show-tips">
            &yen;<%=t.repairAmount%>
        </td>
        <td class="js-show-tips">
            &yen;<%=t.repairPercentage%>
            <%total = total.Jia(t.repairPercentage)%>
        </td>
        <td>
            &yen;<%=t.saleAmount%>
        </td>
        <td class="money-font">
            &yen;<%=t.salePercentage%>
            <%total = total.Jia(t.salePercentage)%>
        </td>
        <td>
            &yen;<%=t.saAmount%>
        </td>
        <td class="money-font">
            &yen;<%=t.saPercentage%>
            <%total = total.Jia(t.saPercentage)%>
        </td>
        <td>
            <%=t.newCustomerNum%>
        </td>
        <td class="money-font">
            &yen;<%=t.newCustomerReward%>
            <%total = total.Jia(t.newCustomerReward)%>
        </td>
        <td>
            &yen;<%=t.businessAmount%>
        </td>
        <td class="money-font">
            &yen;<%=t.businessBeloneReward%>
            <%total = total.Jia(t.businessBeloneReward)%>
        </td>
        <td class="money-font">
            &yen;<%=t.saleStarReward%>
            <%total = total.Jia(t.saleStarReward)%>
        </td>
        <td class="money-font">
            &yen;<%=total%>
        </td>
    </tr>
    <%}%>
</script>
<#--提成规则详细 tips-->
<script type="text/template" id="ruleDetailTpl">
    <div class="rule-detail-tips">
        <div class="angle"></div>
        <% var item;%>
        <div class="tips-box">
            <h4><i class="month"><%=data.month%></i> 提成规则</h4>
            <%if(data.gather) {%>
            <% var t = data.gather.gatherPerfConfigVO;%>
            <p>加点提成：</p>
            <p>新客户到店奖励：<i class="rate"><%=t.newCustomerReward%>元</i><%if(t.orderMinAmount > 0) {%><i class="info">(工单金额满 <i class="rate"><%=t.orderMinAmount%></i> 元以上)</i><%}%>
            </p>
            <p>
                业绩归属奖励：<%=(data.gather.percentageMethod === 0 ? '营业额' : '毛利率')%> * <i class="rate"><%=data.gather.percentageRate > 0 ? data.gather.percentageRate + '%' : 0%></i><i class="info">（<%=t.isContainWashCarOrder == 0 ? '不包含洗车单' : '包含洗车单'%>）</i>
            </p>
            <p>销售之星：按照每<%=t.saleRewardCycle == 0 ? '周' : '月'%><%=(t.salePercentageType == 0 ? '营业额' : '毛利率')%>奖励</p>
            <p>第一名奖励：<i class="rate"><%=t.firstSaleReward%>元</i></p>
            <p>第二名奖励：<i class="rate"><%=t.secondSaleReward%>元</i></p>
            <p>第三名奖励：<i class="rate"><%=t.thirdSaleReward%>元</i></p>
            <%}%>
            <p>服务提成比例：<i class="rate"><%=(data.servicePer > 0 ? data.servicePer + '%' : 0)%></i><%if (data.servicerPer) {%><i class="info">（按照<%=data.serviceMethod == 0 ? '营业额' : '毛利率'%>提成）</i><%}%></p>
            <% if(data.serviceRules) {%>
            <p>特殊规则：</p>
            <% for(i in data.serviceRules) {%>
            <% item = data.serviceRules[i];%>
            <p><%=item.name%>：<i class="rate"><%=item.value%></i></p>
            <%}%>
            <%}%>
            <p>配件提成比例：<i class="rate"><%=data.goodsPer > 0 ? data.goodsPer + '%' : 0%></i><%if (data.goodsPer) {%><i class="info">（按照<%=data.goodsMethod == 0 ? '营业额' : '毛利率'%>提成）</i><%}%></p>
            <% if(data.goodsRules) {%>
            <p>特殊规则：</p>
            <% for(i in data.goodsRules) {%>
            <% item = data.goodsRules[i];%>
            <p><%=item.name%>：<i class="rate"><%=item.value%></i></p>
            <%}%>
            <%}%>
            <p>服务顾问提成比例：<i class="rate"><%=data.servicerPer > 0 ? data.servicerPer + '%' : 0%></i><%if (data.servicerPer) {%><i class="info">（按照<%=data.servicerMethod == 0 ? '营业额' : '毛利率'%>提成）</i><%}%></p>
        </div>
    </div>
</script>

<script src="${BASE_PATH}/static/js/page/report/statistics/staff/staff-perf.js?eec110e8cc4002239e6bd4529a00408f"></script>

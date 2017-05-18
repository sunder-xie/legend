<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/gather_rule.css?b86235fd513c4f4b2c5e181eaadc769c"/>
<div class="yqx-wrapper clearfix">
    <input type="hidden" id="isYBD" value="${YBD}">

    <div class="aside fl">
    <#include "yqx/page/marketing/left-nav.ftl">
    </div>
    <div class="aside-main">
        <h1 class="headline">奖励规则展示
            <#if SESSION_USER_IS_ADMIN == 1>
            <a href="${BASE_PATH}/shop/report/staff/perf?setting=true"
               style="margin-top: -9px;"
               class="yqx-btn yqx-btn-1 fr">设置奖励规则</a>
            </#if>
        </h1>

        <div class="box empty-box hide">
            <div class="empty-i">
            </div>
            <div class="rule-detail">
                <h4>暂无绩效奖励规则</h4>

                <#if SESSION_USER_IS_ADMIN != 1>
                <p>奖励规则为空</p>
                </#if>
                <#if SESSION_USER_IS_ADMIN == 1>
                <p>奖励规则为空，马上进行设置吧！</p>
                <a href="${BASE_PATH}/shop/report/staff/perf?setting=true" class="yqx-btn yqx-btn-3 to-rule">设置奖励规则</a>
                </#if>
            </div>
        </div>
        <div class="box rule-box hide">

        </div>
    </div>
</div>
<#--提成规则详细 tips-->
<script type="text/template" id="ruleDetailTpl">
    <div class="rule-detail-tips">
        <% var item;%>
        <h4 class="month-detail"><i class="month"><%=data.month%></i> 提成规则</h4>
        <ul>
        <%if(data.gather) {%>
        <% var t = data.gather.gatherPerfConfigVO;%>
            <li>
                <h4 class="rule-title">加点提成规则</h4>
                <p>
                    <strong>新客户到店奖励：</strong><i class="rate"><%=t.newCustomerReward%>元</i><%if(t.orderMinAmount > 0) {%><i class="info">（工单金额必须满 <i class="rate"><%=t.orderMinAmount%></i> 元以上）</i><%}%>
                </p>
                <p>
                    <strong>业绩归属奖励：</strong><%=data.gather.percentageMethod == 0 ? '营业额 * ' : '毛利率 * '%><i class="rate"><%=data.gather.percentageRate > 0 ? data.gather.percentageRate + '%' : 0%></i><i class="info"><%=t.isContainWashCarOrder == 0 ? '（不包含洗车单）' : '（包含洗车单）'%></i>
                </p>
                <p>
                    <strong>销售之星：</strong>第一名奖励<i class="rate"><%=t.firstSaleReward%>元</i>，第二名奖励<i class="rate"><%=t.secondSaleReward%>元</i>，第三名奖励<i class="rate"><%=t.thirdSaleReward%>元</i><i class="info">（按照每<%=t.saleRewardCycle == 0 ? '周' : '月'%><%=t.salePercentageType == 0 ? '营业额' : '毛利率'%>奖励）</i>
                </p>
            </li>
        <%}%>
            <li>
                <h4 class="rule-title">维修服务提成规则</h4>

                <p>
                    <strong>服务提成比例：</strong><i class="rate"><%=(data.servicePer > 0 ? data.servicePer + '%' : 0)%></i><i class="info">（按照<%=data.serviceMethod == 0 ? '营业额' : '毛利率'%>提成）</i>
                </p>
                <% if(data.serviceRules) {%>
                <p>特殊规则：</p>
                <% for(var i in data.serviceRules) {%>
                <% item = data.serviceRules[i];%>
                <p><%=item.name%>：<i class="rate"><%=item.value%></i></p>
                <%}%>
                <%}%>
            </li>
            <li>
                <h4 class="rule-title">配件销售提成规则</h4>

                <p><strong>配件提成比例：</strong><i class="rate"><%=data.goodsPer > 0 ? data.goodsPer + '%' : 0%></i><i class="info">（按照<%=data.goodsMethod == 0 ? '营业额' : '毛利率'%>提成）</i></p>
                <% if(data.goodsRules) {%>
                <p>特殊规则：</p>
                <% for(i in data.goodsRules) {%>
                <% item = data.goodsRules[i];%>
                <p><%=item.name%>：<i class="rate"><%=item.value%></i></p>
                <%}%>
                <%}%>
            </li>
            <li>
                <h4 class="rule-title">服务顾问提成规则</h4>

                <p><strong>服务顾问提成比例：</strong><i class="rate"><%=data.servicerPer > 0 ? data.servicerPer + '%' : 0%></i><i class="info">（按照<%=data.servicerMethod == 0 ? '营业额' : '毛利率'%>提成）</i></p>
            </li>
        </ul>
    </div>
</script>
<script src="${BASE_PATH}/static/js/page/marketing/gather/gather_rule.js?85bb9f4aa620857383b5af7a47c93f6c"></script>
<#include "yqx/layout/footer.ftl">
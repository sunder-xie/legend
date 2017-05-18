<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/ax_insurance/coupon/coupon-settle.css?37a82f437c047452713f2cbadbd95f6a">
<div class="yqx-wrapper clearfix">
    <#include "yqx/page/ax_insurance/left-nav.ftl">

    <div class="aside-main">
        <h3 class="headline mb-10">活动补贴明细</h3>
        <div class="panel">
            <div class="panel-body row clearfix">
                <div class="box fl">
                    <label class="subsidy-label">优惠券：</label>
                    <ul class="subsidy-detail clearfix">
                        <li>累计发放 <strong class="subsidy-mark">${shopCouponDetail.issueNumber}</strong> 张</li>
                        <li>累计核销金额 <strong class="subsidy-mark">${shopCouponDetail.usedAmount?string("0.00")}</strong></li>
                        <li>累计核销 <strong class="subsidy-mark">${shopCouponDetail.usedNumber}</strong> 张</li>
                    </ul>
                </div>
                <b class="spacer-line"></b>
                <div class="box fl">
                    <label class="subsidy-label">补贴金额：</label>
                    <ul class="subsidy-detail clearfix">
                        <li>已结算 <strong class="subsidy-mark">${shopCouponDetail.settledAmount?string("0.00")}</strong></li>
                        <li>本月可结算 <strong class="subsidy-mark">${shopCouponDetail.currentSettlingAmount?string("0.00")}</strong></li>
                        <li>未结算 <strong class="subsidy-mark">${shopCouponDetail.settlingAmount?string("0.00")}</strong></li>
                    </ul>
                </div>
                <label class="subsidy-rules">结算规则 <i class="pop-tips js-show-tips" data-tips="1、用于买保险送服务包或买保险送奖励金模式的优惠劵，须在保险公司确认保单后的方可结算对应补贴金额；2、用于买服务包送保险陌生的优惠劵，须在服务包付款后的方可结算对应补贴金额；3、每月有固定结算日进行补贴金额的结算">?</i>
                </label>
            </div>
        </div>
        <div class="panel">
            <div class="panel-header search-form" id="searchForm">
                <div class="form-item form-item-1">
                    <input type="text" name="search_mobile" class="yqx-input yqx-input-small js-mobile" placeholder="车主电话">
                </div>
                <div class="form-item form-item-1">
                    <input type="text" class="yqx-input yqx-input-small yqx-input-icon js-settle-status" placeholder="结算状态">
                    <input type="hidden" name="search_settleStatus" value="">
                    <i class="fa icon-angle-down icon-small"></i>
                </div>
                <div class="fr">
                    <label class="check-box">
                        <input type="checkbox" name="search_currentMonth" value="" class="js-current">本月可结算
                        <input type="hidden" name="search_currentMonth" value="">
                    </label>
                    <button class="yqx-btn yqx-btn-small yqx-btn-3 js-search-btn">搜　索</button>
                </div>
            </div>
            <table class="yqx-table yqx-table-hover">
                <thead>
                <tr>
                    <th class="tc-1">优惠券面值</th>
                    <th class="tc-2">车主电话</th>
                    <th class="tc-3">核销状态</th>
                    <th class="tc-4">核销日期</th>
                    <th class="tc-5">核销保单号</th>
                    <th class="tc-6">补贴金额</th>
                    <th>结算状态</th>
                </tr>
                </thead>
                <tbody id="contentListBox">

                </tbody>
            </table>
        </div>
        <div class="yqx-page" id="pageBox"></div>
    </div>
</div>

<script type="text/html" id="contentList">
    <%if(json.data && json.data.content && json.data.content.length >0){%>
    <%for(var i=0;i < json.data.content.length;i++){%>
    <%var item = json.data.content[i];%>
    <tr>
        <td>&yen; <%=item.deductibleAmount%></td>
        <td><%=item.userMobile%></td>
        <td><%=item.couponStatusName%></td>
        <td><%=item.gmtCouponUsedStr%></td>
        <td><%=item.insuranceOrderSn%></td>
        <td>&yen; <%=item.allowanceAmount%></td>
        <td><%=item.couponSettleStatusName%></td>
    </tr>
    <%}%>
    <%} else {%>
    <tr>
        <td class="no-data" colspan="7">暂无数据</td>
    </tr>
    <%}%>
</script>

<script src="${BASE_PATH}/static/js/page/ax_insurance/coupon/coupon-settle.js?e29dedef6807e81a7a1ba1237a858bde"></script>
<#include "yqx/layout/footer.ftl">


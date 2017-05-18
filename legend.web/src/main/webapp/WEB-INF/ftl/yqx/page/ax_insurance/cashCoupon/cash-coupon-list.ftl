<#include "yqx/layout/header.ftl">
<link href="${BASE_PATH}/static/css/page/ax_insurance/cashCoupon/cash-coupon-list.css?b796e3c08f771c5e7e9b13e81ea388a7" type="text/css" rel="stylesheet">
<div class="yqx-wrapper clearfix">
    <div class="content clearfix">
        <#include "yqx/page/ax_insurance/left-nav.ftl">
        <div class="right">
            <div class="cash-total">
                <p class="page-title">查询现金券
                    <a href="${BASE_PATH}/insurance/anxin/cashCoupon/cashCouponIntroduction">现金券规则</a>
                </p>
                <div class="cash-total-content">
                    <div class="cash-sum">
                        <div class="cash-icon"></div>
                        <div class="cash-sum-info">
                            <a class="cash-jump-btn purchase">去采货</a>
                            <a class="cash-jump-btn calc">返利试算</a>
                            <div class="cash-des">
                                <div class="case-des-items">
                                    <div class="des-item">
                                        未用<span class="coupon-num cashCouponEffective un-use"></span>张
                                    </div>
                                    <div class="des-item">
                                        已用<span class="coupon-num used cashCouponConsumed"></span>张
                                    </div>
                                    <div class="des-item">
                                        十天内过期<span class="coupon-num near-overdue expiredTenDay"></span>张
                                    </div>
                                    <div class="des-item">
                                        已过期<span class="coupon-num overdue expired"></span>张
                                    </div>
                                </div>
                                <div class="case-des-items">
                                    <div class="des-item">
                                        累计获赠券额 <span class="coupon-num totalCashCouponFee"></span>元
                                    </div>
                                    <div class="des-item">
                                        今日可用券保单 <span class="coupon-num effectToday"></span>单
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="cash-list">
                        <div class="search-form">
                            <div class="form-item">
                                <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-select coupon-state" readonl value="现金券状态">
                                <span class="fa icon-angle-down icon-small"></span>
                            </div>
                            <input type="button" value="今日生效" class="condition-btn today" data-val="0">
                            <input type="button" value="十天内过期" class="condition-btn ten-day" data-val="1">
                        </div>
                        <table class="cash-table">
                            <thead>
                            <tr>
                                <th width="20%">现金券面值</th>
                                <th width="20%">生效日期</th>
                                <th width="20%">失效日期</th>
                                <th width="20%">现金券状态</th>
                                <th width="10%">操作</th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                    <section class="yqx-page" id="pagingBox"></section>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/ax_insurance/cashCoupon/cash-coupon-list.js?fc7ff45c460d242cfc7b003f6b5d8f55"></script>
<script type="text/template" id="cashCouponListTpl">
    <%for(var i = 0; i < content.length; i++){%>
        <%var item = content[i];%>
        <tr data-item='<%=stringify(item)%>' >
            <td><%=item.faceAmount%>元</td>
            <td><%=timeFormat(item.gmtValidate, 'YYYY-MM-DD')%></td>
            <td><%=timeFormat(item.gmtExpired, 'YYYY-MM-DD')%></td>
            <td><%=parseStatus(item.cashCouponStatus)%></td>
            <td>
                <%if(item.cashCouponStatus == 0){%>
                    <input type="button" value="立刻使用" class="use-cash-coupon">
                <%}%>
            </td>
        </tr>
    <%}%>
</script>
<script type="text/template" id="couponOrderTpl">
    <div class="coupon-start-use">
        <div class="order-title">以下为今日可用券的保单，请选择</div>
        <div class="order-list">
            <#--<p class="order-tip">注：今日保单只能在今日用劵，若不使用，次日此保单将无法用劵</p>-->
            <table class="order-head">
                <thead>
                    <tr>
                        <th width="10%" class="text-c">勾选</th>
                        <th width="45%">交易单号</th>
                        <th width="15%">车牌号</th>
                        <th width="15%">商业保险费</th>
                        <th width="15%">交强险保费</th>
                    </tr>
                </thead>
            </table>
            <div style="overflow-y: scroll;max-height: 156px;">
                <table class="order-tbody">
                    <colgroup>
                        <col width="10%">
                        <col width="45%">
                        <col width="15%">
                        <col width="15%">
                        <col width="15%">
                    </colgroup>
                    <tbody>
                    <%if(!data.length) {%>
                        <tr class="order-tr">
                            <td class="text-c" colspan="4">暂无可用劵的保单，赶紧去出保单吧！</td>
                        </tr>
                    <%}%>
                    <%for(var i = 0; i< data.length; i++) {%>
                    <%var item = data[i];%>
                    <tr class="order-tr">
                        <td class="text-c">
                            <input type="checkbox" value="<%=item.insuranceOrderSn%>" class="like-checkbox">
                        </td>
                        <td>
                            <%=item.insuranceOrderSn%>
                        </td>
                        <td>
                            <%=item.vehicleSn||'未填写车牌'%>
                        </td>
                        <td>
                            <%if(item.commercialInsuredFee){%><%=item.commercialInsuredFee%><%}else{%>无<%}%>
                        </td>
                        <td>
                            <%if(item.forcibleInsuredFee){%><%=item.forcibleInsuredFee%><%}else{%>无<%}%>
                        </td>
                    </tr>
                    <%}%>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="order-foot">
            <input type="button" value="取消" class="use-btn choice-close">
            <input type="button" value="确认用券" class="use-btn choice-use" disabled>
        </div>
    </div>
</script>
<script type="text/template" id="cashSuccessTpl">
    <div class="success-tip">
        <img src="/legend/static/img/page/ax_insurance/cash-success.png">
        <div class="success-msg">
            <p class="msg-info">
                恭喜您，成功兑现<span class="cash-amount"><%=faceAmount%></span>元
            </p>
            <p class="msg-tip">
                现金将于结算日打入门店收款账户，注意查收
            </p>
        </div>
    </div>
</script>
<#include "yqx/layout/footer.ftl">

<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/common/report/report-common.css?c58c9a3ee3acf5c0dbc310373fe3c349">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/report/statistics/business/daily.css?5cf25dce3062fb70385be9afca5ef282">
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/report/left-nav.ftl"/>
    <div class="content fr">
        <h1 class="headline">营业日报</h1>
        <div class="container search-form" id="searchForm">
            <div class="form-item select-date">
                <label class="font-yahei">选择日期：</label>
                <div class="form-item">
                <#assign aDateTime = .now>
                <#assign aDate = aDateTime?date>
                    <input class="yqx-input date" value="${aDate?iso_utc}"
                            name="dateStr">
                    <span class="fa icon-calendar"></span>
                </div>
            </div><button class="js-search-btn yqx-btn yqx-btn-3 search-btn">
                统计</button><div class="quick-filter js-filter-date">
                <label>快速筛选：</label><i data-target="-7" class="js-quick-select">
                   </i>，<i data-target="-1" class="js-quick-select">
                        昨天</i>，<i class="js-quick-select"
                   data-target="0">今天</i>
            </div>
        </div>
        <div class="container module-statistic">
            <div class="module-box"></div><div class="module-box"></div><div class="module-box"></div><div class="module-box"></div>
        </div>
        <div class="container account-detail table-container">
            <h2>收款统计<i class="fr js-slide-up" data-target=".account-detail .table-box">收起</i></h2>
            <div class="table-box">
            <table class="yqx-table">
                <thead>
                    <tr>
                        <th style="width: 104px">收款方式/业务</th>
                        <th>洗车<i class="question-icon js-show-tips" data-tips="当日洗车单金额及单数">
                        </i></th>
                        <th>维修保养<i class="question-icon js-show-tips" data-tips="当日快修快保单和综合维修单的总金额及单数">
                        </i></th>
                        <th>销售单<i class="question-icon js-show-tips" data-tips="当日销售单金额及单数">
                        </i></th>
                        <th>会员卡<i class="question-icon js-show-tips" data-tips="当日会员卡办理和充值的收款金额及卡数">
                        </i></th>
                        <th>计次卡<i class="question-icon js-show-tips" data-tips="当日办理的计次卡的收款金额及卡数">
                        </i></th>
                        <th>优惠券<i class="question-icon js-show-tips" data-tips="当日发放的优惠券收款金额及优惠券数量">
                        </i></th>
                        <th>收款单<i class="question-icon js-show-tips" data-tips="当日收款单收款金额及单数">
                        </i></th>
                        <th>合计</th>
                    </tr>
                </thead>
            </table>
            </div>
        </div>
        <div class="container order-info table-container">
            <h2>营业明细<i class="question-icon js-show-tips"
                       data-tips="当日确认账单的工单列表"></i><i class="fr js-slide-up" data-target=".order-info .table-box">展开</i></h2>
            <div class="table-box" style="display: none">
            <table class="yqx-table">
                <thead>
                    <tr>
                        <th>确认账单日期</th>
                        <th>工单号</th>
                        <th>车牌</th>
                        <th>车主</th>
                        <th>开单日期</th>
                        <th class="text-right">总计</th>
                        <th class="text-right">应收金额</th>
                        <th class="text-right">实收金额</th>
                        <th class="text-right">挂账</th>
                        <th class="text-right">毛利</th>
                    </tr>
                </thead>
                <tbody id="orderFill"></tbody>
            </table>
                </div>
        </div>
        <div class="yqx-page order-info" id="orderPage"></div>
    </div>
</div>
<script type="text/template" id="revenueTpl">
    <%var data = json.success && json.data || {};  %>
    <div class="statistic-show" data-index="0">
        <h2>营收</h2>
        <a href="" target="_blank" class="js-report-order-pay">
            <label>营业额<i class="question-icon js-show-tips"
                         data-tips="当日确认账单的工单应收金额总和"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.sumOfBusiness || 0%></i> 元</label>
        </a>
        <a href="" target="_blank" class="js-report-order-pay">
            <label>毛利<i class="question-icon js-show-tips"
                         data-tips="毛利=营业额-配件成本"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.grossAmount || 0%></i> 元</label>
        </a>
        <a href="" target="_blank" class="js-report-order-pay">
            <label>确认账单<i class="question-icon js-show-tips"
                          data-tips="当日确认账单的工单数"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.confirmOrderNums || 0%></i> 单</label>
        </a>
        <a href="javascript:void(0)" target="_blank" class="no-underline">
            <label>单车产值<i class="question-icon js-show-tips"
                          data-tips="营业额/确认账单数"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.outputValueByCar || 0%></i> 元</label>
        </a>
        <a href="javascript:void(0)" target="_blank" class="no-underline">
            <label>收款金额<i class="question-icon js-show-tips"
                   data-tips="当日所有实收金额总和，含工单+收款单，包含充值、办卡、发券的收款；不包含工单中会员卡支付的金额"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.paidAmount || 0%></i> 元</label>
        </a>
        <a href="javascript:void(0)" target="_blank" class="no-underline">
            <label>会员卡支付<i class="question-icon js-show-tips"
                          data-tips="当日工单收款使用会员卡支付的金额"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.memberCardPayAmount || 0%></i> 元</label>
        </a>
    </div>

</script>
<script type="text/template" id="customerTpl">
    <%var data = json.success && json.data || {};  %>
    <div class="statistic-show" data-index="3">
        <h2>客户</h2>
        <a href="" target="_blank" class="js-report-order-create">
            <label>接车<i class="question-icon js-show-tips"
                        data-tips="当日开单总数，不计算无效工单">
            </i></label><label class="fr"><i class="money-font js-show-tips"><%=data.orderNums || 0%></i> 台</label>
        </a>
        <a href="javascript:void(0)" target="_blank" class="no-underline">
            <label>新建车辆<i class="question-icon js-show-tips" data-tips="当日创建的车辆总数">
            </i></label><label class="fr"><i class="money-font js-show-tips"><%=data.carNums || 0%></i> 台</label>
        </a>
    </div>
</script>
<script type="text/template" id="purchaseTpl">
    <%var data = json.success && json.data || {};  %>
    <div class="statistic-show" data-index="1">
        <h2>采销</h2>
        <a href="" target="_blank" class="js-warehouse-in ">
            <label>采购金额<i class="question-icon js-show-tips"
                           data-tips="当日入库单金额总和，入库退货退款不会自动扣除"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.warehouseInTotalAmount || 0%></i> 元</label>
        </a>
        <a href="" target="_blank" class="js-settle-pay ">
            <label>付款金额<i class="question-icon js-show-tips"
                           data-tips="当日采购付款及付款单付款金额总和，入库退货退款不会自动扣除"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.actuallyPaidAmount || 0%></i> 元</label>
        </a>
        <a href="" target="_blank" class="js-report-sale-goods ">
            <label>配件销量<i class="question-icon js-show-tips"
                        data-tips="当日确认账单的工单中配件销售总额"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.goodsSoldAmount || 0%></i> 元</label>
        </a>
    </div>

</script>
<script type="text/template" id="cardTpl">
    <%var data = json.success && json.data || {};  %>
    <div class="statistic-show" data-index="2">
        <h2>卡券</h2>
        <a href="" target="_blank" class="js-account-coupon ">
            <label>发券<i class="question-icon js-show-tips"
                         data-tips="当日所有发放的优惠券总数，包括手动发放和微信自动发放"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.couponNums || 0%></i> 张</label>
        </a>
        <a href="" target="_blank" class="js-account-card-recharge ">
            <label>充值<i class="question-icon js-show-tips"
                      data-tips="当日会员卡充值总额"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.cardRechargeAmount || 0%></i> 元</label>
        </a>
        <a href="" target="_blank" class="js-account-card-retreat ">
            <label>退款<i class="question-icon js-show-tips"
                      data-tips="当日会员卡退卡退款金额合计"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.retreatCardAmount || 0%></i> 元</label>
        </a>
        <a href="" target="_blank" class="js-account-combo ">
            <label>发计次卡<i class="question-icon js-show-tips"
                           data-tips="当日办理的计次卡数量"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.comboNums || 0%></i> 张<label>
        </a>
        <a href="" target="_blank" class="js-account-card ">
            <label>发会员卡<i class="question-icon js-show-tips"
                        data-tips="当日办理的会员卡数量"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.cardNums || 0%></i> 张<label>
        </a>
        <a href="" target="_blank" class="js-account-card-retreat ">
            <label>退会员卡<i class="question-icon js-show-tips"
                        data-tips="当日会员卡退卡数量"></i>
            </label><label class="fr"><i class="money-font js-show-tips"><%=data.retreatCardNums || 0%></i> 张<label>
        </a>
    </div>
</script>
<script type="text/template" id="orderTpl">
    <% if(json.success && json.data && json.data.content ) {%>
    <% var data = json.data.content %>
    <%for(var i in data) {%>
    <tr>
        <td class="js-show-tips"><%=data[i].confirmTimeStr || data[i].confirmTime%></td>
        <td class="js-show-tips"><%=data[i].orderSn%></td>
        <td class="js-show-tips"><%=data[i].carLicense%></td>
        <td class="js-show-tips"><%=data[i].customerName%></td>
        <td class="js-show-tips"><%=data[i].createTimeStr || data[i].createTimeStr%></td>
        <td class="money-font">&yen; <%=data[i].orderAmount%></td>
        <td class="money-font color-money">&yen; <%=data[i].receivableAmount%></td>
        <td class="money-font color-money">&yen; <%=data[i].paidAmount%></td>
        <td class="money-font color-money">&yen; <%=data[i].signAmount%></td>
        <td class="money-font color-money">&yen; <%=data[i].grossAmount%></td>
    </tr>
    <%}%>
    <%} else {%>
    <tr><td colspan="8">暂无数据</td></tr>
    <%}%>
</script>
<script type="text/template" id="accountTpl">
<% if(json && json.success && json.data) {%>

<%var sum = {
carwash: 0,maintain: 0, sellGoods: 0, accountCard: 0, accountCombo: 0, accountCoupon: 0, debitBill: 0
};%>
<% var content = json.data, t = 0, num = 0, total = 0;%>
<%var data = content.businessPaidAmountList;%>
<% for(var i in data) {%>
<tr>
    <% sum.carwash = sum.carwash.Jia(data[i].carwashPaidAmount);%>
        <%sum.maintain = sum.maintain.Jia(data[i].maintainPaidAmount);%>
    <% sum.sellGoods = sum.sellGoods.Jia(data[i].sellGoodsPaidAmount);%>
    <% sum.accountCard = sum.accountCard.Jia(data[i].accountCardPaidAmount);%>
    <% sum.accountCombo = sum.accountCombo.Jia(data[i].accountComboPaidAmount);%>
    <% sum.accountCoupon = sum.accountCoupon.Jia(data[i].accountCouponPaidAmount);%>
    <% sum.debitBill = sum.debitBill.Jia(data[i].debitBillPaidAmount);%>
    <% t = data[i].carwashPaidAmount.Jia(data[i].maintainPaidAmount)
        .Jia(data[i].sellGoodsPaidAmount).Jia(data[i].accountCardPaidAmount).Jia(data[i].accountComboPaidAmount)
        .Jia(data[i].accountCouponPaidAmount).Jia(data[i].debitBillPaidAmount)%>
    <td><%=data[i].paymentName%></td>
    <td>&yen; <%=data[i].carwashPaidAmount%></td>
    <td>&yen; <%=data[i].maintainPaidAmount%></td>
    <td>&yen; <%=data[i].sellGoodsPaidAmount%></td>
    <td>&yen; <%=data[i].accountCardPaidAmount%></td>
    <td>&yen; <%=data[i].accountComboPaidAmount%></td>
    <td>&yen; <%=data[i].accountCouponPaidAmount%></td>
    <td>&yen; <%=data[i].debitBillPaidAmount%></td>
    <#--合计-->
    <td class="money-font js-sum color-money">&yen; <%=t%></td>
    <% total = total.Jia(t); %>
</tr>
<%}%>
<tr class="strong-tr">
    <% var card = getCard(); %>
    <% for(var i in content){%>
        <%if(i != 'businessPaidAmountList') {
            num = num.Jia( content[i] );
        }
    }%>
    <td>合计(金额/单数)</td>
    <td class="js-show-tips">
        <i class="money-font">&yen; <%=sum.carwash%>
        </i>/<%=content.carwashNums%>
    </td>
    <td class="js-show-tips">
        <i class="money-font">&yen; <%=sum.maintain%>
        </i>/<%=content.maintainNums%>
    </td>
    <td class="js-show-tips">
        <i class="money-font">&yen; <%=sum.sellGoods%>
        </i>/<%=content.sellGoodsNums%>
    </td>
    <td class="js-show-tips">
        <i class="money-font">&yen; <%=sum.accountCard%>
        </i>/<%=card.cardNums%>
    </td>
    <td class="js-show-tips">
        <i class="money-font">&yen; <%=sum.accountCombo%>
        </i>/<%=card.comboNums%>
    </td>
    <td class="js-show-tips">
        <i class="money-font">&yen; <%=sum.accountCoupon%>
        </i>/<%=card.couponNums%>
    </td>
    <td class="js-show-tips">
        <i class="money-font">&yen; <%=sum.debitBill%>
        </i>/<%=content.debitBillNums%>
    </td>
    <td class="js-show-tips"><i class="money-font color-money">&yen; <%=total%></i></td>
</tr>
<%}%>

</script>

<script src="${BASE_PATH}/static/js/page/report/statistics/business/daily.js?55ae4f4e458858a15e26cd61ae4a5e3a"></script>
<#include "yqx/layout/footer.ftl">
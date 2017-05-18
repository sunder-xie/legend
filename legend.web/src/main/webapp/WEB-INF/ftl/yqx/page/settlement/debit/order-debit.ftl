<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet"
      href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"
      xmlns="http://www.w3.org/1999/html"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/debit/order-debit.css?b3ca265d4ddacfdd6ecec26a07a06935"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/settlement/settlement-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h3 class="headline fl">工单收款－
                <small>${orderInfo.orderTagName}单详情</small>
                －
                <small>收款</small>
            </h3>
        </div>
        <!--工单价目明细表格 start-->
        <div class="pay-box clearfix js-tableShow">
            工单价目明细表格
            <div class="table-btn"><i class="icon-angle-down"></i><span>展开</span></div>
            <input name="orderId" type="hidden" value="${orderInfo.id}">
            <input name="carLicense" type="hidden" value="${orderInfo.carLicense}">
        </div>
        <div class="show-table">
            <#if orderServicesList>
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>服务项目名称</th>
                        <th>工时</th>
                        <th>工时费</th>
                        <th>优惠金额</th>
                        <th>总价</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#list orderServicesList as orderServices>
                        <tr>
                            <td class="js-show-tips">${orderServices.serviceName}</td>
                            <td>${orderServices.serviceHour}</td>
                            <td>${orderServices.servicePrice}元</td>
                            <td>${orderServices.discount}元</td>
                            <td class="js-show-tips border_right_show">${orderServices.soldAmount}元</td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
            </#if>
            <#if orderGoodsList>
                <table class="yqx-table table-margin">
                    <thead>
                    <tr>
                        <th>配件名称</th>
                        <th>数量</th>
                        <th>单价</th>
                        <th>优惠金额</th>
                        <th>总价</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#list orderGoodsList as orderGoods>
                        <tr>
                            <td class="js-show-tips">${orderGoods.goodsName}</td>
                            <td>${orderGoods.goodsNumber}</td>
                            <td>${orderGoods.goodsPrice}元</td>
                            <td>${orderGoods.discount}元</td>
                            <td class="border_right_show js-show-tips">${orderGoods.soldAmount}元</td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
            </#if>
            <div class="table-total">
                总计：<span class="money-font">&yen;${debitBill.totalAmount}</span>
            </div>
        </div>
        <!--工单价目明细表格 end-->

        <!--收款记录 start-->
        <div class="pay-box box-margin clearfix js-tableShow">
            收款记录
            <div class="table-btn"><i class="icon-angle-down"></i><span>展开</span></div>
        </div>
        <div class="show-table">
            <ul>
            <#--工单优惠流水-->
            <#if discountFlowList>
                <#list discountFlowList as discountFlow>
                    <li>
                        <div class="date">日期：${discountFlow.gmtCreateStr}</div>
                        <div class="money">${discountFlow.discountName}：${discountFlow.discountAmount}元</div>
                        <div class="person">收银人员：${discountFlow.operatorName}</div>
                    </li>
                </#list>
            </#if>
            <#--收款单流水-->
            <#if debitBillFlowList>
                <#list debitBillFlowList as debitBillFlow>
                    <li>
                        <div class="date">日期：${debitBillFlow.flowTimeStr}</div>
                        <div class="money"><#if debitBillFlow.flowStatus == 0>${debitBillFlow.paymentName}<#else>坏账</#if>：${debitBillFlow.payAmount}元</div>
                        <div class="person">收银人员：${debitBillFlow.operatorName}</div>
                    </li>
                </#list>
            </#if>
            </ul>
        </div>
        <!--收支历史记录 end-->

        <!--付款信息 start-->
        <div class="pay-inforbox">
            <div class="pay-title">工单确认账单信息</div>
            <input type="hidden" name="billId" value="${debitBill.id}">
            <div class="pay-con">
                <div class="method-title">会员卡余额</div>
                <input type="hidden" name="memberId" value="${memberCard.id}">
                <input type="hidden" name="memberBalance" value="${memberCard.balance}">
                <input type="hidden" name="discountGuestMobile" value="${guestMobile}"><!-- 用他人优惠券、未用他人的卡优惠的时候，他人的手机号码 -->
                <div class="method-box">
                    <div class="form-label">
                        本次使用
                    </div><div class="form-item input-width">
                        <input type="text" name="memberPayAmount"
                               class="yqx-input yqx-input-icon yqx-input-small js-member-pay" data-v-type="price"
                               maxlength="10" value="" placeholder="" <#if !memberCard || !(memberCard.id) || memberCard.expired || memberCard.isDeleted == 'Y'>disabled</#if>>
                        <span class="fa icon-small">元</span>
                    </div>
                    <#if memberCard>
                        <input type="hidden" class="js-belongOther" value="${memberCard.mobile}" data-belong-other="${memberCard.belongOther?string('true','false')}" data-customer-name="${memberCard.customerName}"/>
                        <span>说明：使用已选会员卡 <span class="money-font">${memberCard.memberCardInfo.typeName}（${memberCard.customerName}，${memberCard.mobile}）</span><#if !memberCard || !(memberCard.id) || memberCard.expired || memberCard.isDeleted == 'Y'>已过期/已退，不可使用<#else>结算，卡内余额 <span class="money-font">#{memberCard.balance;m2M2}</span> 元<#if memberCard.balance lt orderInfo.signAmount>，请充值</#if></#if></span>
                    </#if>
                    <div class="sett-member-card-box" id="settMemberCardBox"></div>
                </div>

                <div class="method-title">收款方式&金额</div>
                <div id="methodcon">
                    <div id="payment" class="method-box">
                        <div class="form-label">
                            收款方式
                        </div><div class="form-item yqx-downlist-wrap input-width">
                            <input type="text" name="paymentName"
                                   class="yqx-input yqx-input-icon yqx-input-small js-method"
                                   value="${defaultPaymentName}" placeholder="">
                            <input type="hidden" name="paymentId" value="${defaultPaymentId}">
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                        <div class="form-label label-margin">
                            收款
                        </div><div class="form-item input-width">
                            <input type="text" name="payAmount" class="yqx-input yqx-input-icon yqx-input-small js-pay"
                                   data-v-type="price" maxlength="10" value="" placeholder="">
                            <span class="fa icon-small">元</span>
                        </div>
                        <a href="javascript:;" class="js-delete delete-btn">删除</a>
                    </div>
                </div>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-addMethod">添加收款方式</button>

                <div class="remarks">
                    <div class="form-label">
                        备注
                    </div>
                    <div class="form-item">
                        <textarea class="yqx-textarea text-area-width" name="remark" rows="1" id="remark" maxlength="100"
                                  placeholder="请输入备注"></textarea>
                    </div>
                </div>
                <div class="total-box">
                    <p>应收金额：<span id="newReceivableAmount" class="money-font">&yen;<#if debitBill>${debitBill.receivableAmount?string("0.00")}<#else>0.00</#if></span></p>
                <#if orderInfo && orderInfo.downPayment gt 0.00>
                    <p>预付定金：<span id="newReceivableAmount" class="money-font">&yen;#{orderInfo.downPayment;m2M2}</span></p>
                    <input type="hidden" value="${orderInfo.downPayment}" id="js-downPayment">
                </#if>
                <#if redBill>
                    <p>冲红应收金额: <span class="money-font">&yen;${redBill.receivableAmount?string("0.00")}</span></p>
                </#if>

                    <p>实收金额：<span class="money-font">&yen;</span><span id="newPaidAmount" class="money-font"><#if debitBill>${debitBill.paidAmount?string("0.00")}<#else>0.00</#if></span></p>
                <#if redBill && redBill.paidAmount < 0>
                    <p>冲红实收金额: <span class="money-font">&yen;${redBill.paidAmount?string("0.00")}</span></p>
                </#if>

                    <p>挂账金额：<span class="money-font">&yen;</span><span id="newSignAmount" class="money-font"><#if debitBill>${debitBill.signAmount?string("0.00")}<#else>0.00</#if></span></p>
                <#if redBill && redBill.signAmount < 0>
                    <p>冲红挂账金额: <span class="money-font">&yen;${redBill.signAmount?string("0.00")}</span></p>
                </#if>

                    <input type="hidden" name="receivableAmount" value="<#if debitBill>${debitBill.receivableAmount}<#else>0</#if>"/>
                    <input type="hidden" name="paidAmount" value="<#if debitBill>${debitBill.paidAmount}<#else>0</#if>"/>
                    <input type="hidden" name="signAmount" value="<#if debitBill>${debitBill.signAmount}<#else>0</#if>"/>

                    <input type="hidden" name="redReceivableAmount" value="<#if redBill>${redBill.receivableAmount}<#else>0</#if>"/>
                    <input type="hidden" name="redPaidAmount" value="<#if redBill>${redBill.paidAmount}<#else>0</#if>"/>
                    <input type="hidden" name="redSignAmount" value="<#if redBill>${redBill.signAmount}<#else>0</#if>"/>

                    <div class="payee">
                        <span>收银人员：${SESSION_USER_NAME}</span>
                        <span>收款日期：${.now?string("yyyy-MM-dd HH:mm")}</span>
                    </div>
                </div>
            </div>
            <div class="btn-box clearfix">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-comfirm">确认收款</button>
                <#if orderInfo && (orderInfo.downPayment == 0.00 || hasDownPaymentFlow)>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-bad-bill">坏账</button>
                </#if>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
            </div>
        </div>
        <!--付款信息 end-->
    </div>
    <!-- 右侧内容区 end -->
</div>


<script type="text/html" id="methodTpl">
    <div id="payment" class="method-box">
        <div class="form-label">
            收款方式
        </div><div class="form-item yqx-downlist-wrap input-width">
            <input type="text" name="paymentName" class="yqx-input yqx-input-icon yqx-input-small js-method" value="${defaultPaymentName}"
                   data-v-type="required" placeholder="">
            <input type="hidden" name="paymentId" value="${defaultPaymentId}">
            <span class="fa icon-angle-down icon-small"></span>
        </div>
        <div class="form-label label-margin">
            收款
        </div><div class="form-item input-width">
            <input type="text" name="payAmount" class="yqx-input yqx-input-icon yqx-input-small js-pay"
                   data-v-type="required | price" maxlength="10" value="" placeholder="">
            <span class="fa icon-small">元</span>
        </div>
        <a href="javascript:;" class="js-delete delete-btn">删除</a>
    </div>
</script>

<#include "yqx/tpl/account/broadcast-tpl.ftl">
<#include "yqx/tpl/account/vip-card-tpl.ftl">
<!--坏帐弹窗-->
<#include "yqx/tpl/settlement/debit/bad-debit-tpl.ftl">
<!-- 使用他人会员卡，发送验证码 -->
<#include "yqx/tpl/account/code-dialog-tpl.ftl">

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script src="${BASE_PATH}/static/js/page/settlement/debit/order-debit.js?b879e604d820e74b544c1a6697969f6e"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
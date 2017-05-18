<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet"
      href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/debit/confirm-bill.css?706be8d8393871c6d4fa1f9678332a6e"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix speedily-confirm">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/settlement/settlement-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <input type="hidden" id="orderId" value="${orderInfo.id}"/>
    <input type="hidden" id="carLicense" value="${orderInfo.carLicense}"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h3 class="headline fl">工单收款－
                <small><#if orderInfo><#if orderInfo.orderTag == 3>快修快保详情－<#elseif orderInfo.orderTag == 5>销售单详情－</#if><#else></#if></small>
                <small>收款</small>
            </h3>
        </div>
        <!--工单价目明细表格 start-->
        <div class="pay-box clearfix js-tableShow">
            工单价目明细表格
            <div class="table-btn "><i class="icon-angle-down"></i><span>展开</span></div>
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
                        <td>${orderServices.serviceName}</td>
                        <td>${orderServices.serviceHour}</td>
                        <td>${orderServices.servicePrice}</td>
                        <td>${orderServices.discount}</td>
                        <td>${orderServices.soldAmount}</td>
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
                        <td>${orderGoods.goodsName}</td>
                        <td>${orderGoods.goodsNumber}</td>
                        <td>${orderGoods.goodsPrice}</td>
                        <td>${orderGoods.discount}</td>
                        <td>${orderGoods.soldAmount}</td>
                    </tr>
                    </#list>
                </tbody>
            </table>
        </#if>
            <div class="table-total">
                总计：<span class="money-font">&yen;${orderInfo.orderAmount}</span>
            </div>
        </div>
        <!--工单价目明细表格 end-->

        <!--工单确认账单信息 start-->
        <div class="pay-inforbox">
            <div class="pay-title">工单收款</div>
            <div class="pay-con">
            <#-- 计次卡临时使用次数存储start-->
                <input type="hidden" class="js-combo-use">
                <input type="hidden" class="js-member-car-id" value="<#if memberCard>${memberCard.id}</#if>">
                <input type="hidden" class="js-member-car-number" value="<#if memberCard>${memberCard.cardNumber}</#if>">
                <input type="hidden" class="js-member-car-used" value="<#if memberCard>${memberCard.id}</#if>">
            <#-- 计次卡临时使用次数存储end-->
                <div class="method-title">券</div>
                <div class="voucher-box clearfix" >
                    <#include "yqx/tpl/account/coupon-tpl.ftl">
                </div>
                <div class="method-title">优惠</div>
                <div class="method-box">
                    <div class="form-label form-label-must">
                        优惠金额：
                    </div>
                    <div class="form-item discount-box js-discount-box">
                        <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-discount-amount-input" placeholder="优惠金额">
                        <span class="fa icon-small">元</span>
                    </div>
                    <div class="form-item">
                        <span class="form-text js-card-err-msg hide">无可用会员卡</span>
                    </div>
                </div>
                <div class="method-box discount-amount-vip-card-box" id="discountAmountVIPCardBox">
                </div>
                <div class="method-title">淘汽优惠码</div>
                <div class="method-box">
                    <div class="form-item">
                        <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-check-taoqicouponsn"
                               placeholder="请输入淘汽优惠码" id="taoqiCouponSn">
                    </div>
                    <div class="form-item">
                        <input type="text" class="yqx-input yqx-input-icon yqx-input-small"
                               placeholder="优惠金额" disabled="disabled" id="taoqiCouponAmount">
                        <span class="fa icon-small">元</span>
                    </div>
                </div>

                <!--应收总金额-->
                <input type="hidden" id="totalAmount" value="${orderInfo.orderAmount}"/>
                <input type="hidden" id="preReceivableAmount" value="#{orderInfo.orderAmount;m2M2}"/>
                <input type="hidden" id="receivableAmount" value="#{orderInfo.orderAmount;m2M2}"/>
                <div class="total-box border-tb margin-top-10">
                    <div class="bot-text">
                        <input type="hidden" id="orderAmount" value="<#if orderInfo.orderAmount>#{orderInfo.orderAmount;m2M2}<#else>0.00</#if>">
                        工单总计金额：<span class="money-font">&yen;<#if orderInfo.orderAmount>#{orderInfo.orderAmount;m2M2}<#else>0.00</#if></span>
                        <div class="total-details-tag js-tag"></div>
                        <div class="total-hide hide">
                            <div class="total-arrow"></div><div class="total-details">服务费用：<span class="money-font white"> &yen;${orderInfo.serviceAmount - orderInfo.serviceDiscount}</span> + 配件费用：<span class="money-font white"> &yen;${orderInfo.goodsAmount-orderInfo.goodsDiscount}</span> + 附加费用: <span class="money-font white"> &yen;${orderInfo.feeAmount-orderInfo.feeDiscount}</span> = 总计：<span class="money-font white"> &yen; ${orderInfo.orderAmount}</span></div>
                        </div>
                    </div>
                    <div class="bot-text">
                        应收金额：<span class="js-receivable-amount money-font">&yen;<#if orderInfo.payAmount gt 0>#{orderInfo.payAmount;m2M2}<#else>#{orderInfo.orderAmount;m2M2}</#if></span>
                        <div class="total-details-tag js-tag"></div>
                        <div class="total-hide hide">
                            <div class="total-arrow"></div><div class="total-details">工单总计金额： <span class="money-font white">&yen;<#if orderInfo.orderAmount>#{orderInfo.orderAmount;m2M2}<#else>0.00</#if></span> - 优惠总金额： <span class="money-font white">&yen;<em class="js-order-discount-amount order-discount"></em></span> = 应收金额： <span class="money-font white"><em class="js-receivable-amount order-discount"></em></span></div>
                        </div>
                    </div>
                    <#if orderInfo && orderInfo.downPayment gt 0.00>
                    <div class="bot-text">
                        预付定金：<span class="money-font">&yen;#{orderInfo.downPayment;m2M2}</span>
                        <input type="hidden" value="${orderInfo.downPayment}" class="js-downPayment">
                    </div>
                    </#if>
                </div>


                <!--收款-->
                <div class="method-title label-margin">会员卡余额</div>
                <input type="hidden" name="memberId" value="${memberCard.id}">
                <div class="method-box">
                    <div class="form-label label-margin">
                        本次使用
                    </div>
                    <div class="form-item input-width">
                        <input type="text"
                               name="memberPayAmount"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               data-v-type="price"
                               maxlength="10"
                               value=""
                               placeholder="" <#if !memberCard || !(memberCard.id) || memberCard.expired>disabled</#if>>
                        <span class="fa icon-small">元</span>
                    </div>
                    <span class="vip-card-tips"></span>
                    <div class="sett-member-card-box" id="settMemberCardBox"></div>
                </div>

                <div class="method-title">收款方式&金额</div>
                <div class="method-box">
                    <div id="methodcon">
                        <div class="method-box paychannel">
                            <div class="form-label label-margin">
                                收款方式
                            </div>
                            <div class="form-item yqx-downlist-wrap input-width">
                                <input type="text"
                                       name="paymentName"
                                       class="yqx-input yqx-input-icon yqx-input-small js-method "
                                       value="${defaultPaymentName}"
                                       placeholder="">
                                <input type="hidden" name="paymentId" value="${defaultPaymentId}">
                                <span class="fa icon-angle-down icon-small"></span>
                            </div>
                            <div class="form-label label-margin" style="padding-left: 12px;">
                                收款
                            </div>
                            <div class="form-item input-width">
                                <input type="text" name="payAmount"
                                       class="yqx-input yqx-input-icon yqx-input-small js-pay"
                                       data-v-type="price" maxlength="10" value="" placeholder="">
                                <span class="fa icon-small">元</span>
                            </div>
                            <a href="javascript:;" class="js-delete delete-btn delete-color">删除</a>
                        </div>
                    </div>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small js-addMethod margin-top20">添加收款方式</button>
                    <div class="remarks">
                        <div class="form-label">
                            备注
                        </div>
                        <div class="form-item">
                        <textarea class="yqx-textarea text-area-width" name="remark" rows="1" id="remark" maxlength="100"
                                  placeholder="请输入备注"></textarea>
                        </div>
                    </div>
                </div>

                <div class="total-box">

                    <div class="bot-text">
                        实收金额：<span id="paidAmountSP" class="money-font">&yen;0</span>
                        <div class="total-details-tag js-tag"></div>
                        <div class="total-hide hide">
                            <div class="total-arrow"></div><div class="total-details">会员卡余额支付:<span class="js-vip-pay white money-font">0</span> + 收款方式支付:<span class="js-debit-pay white money-font">0</span> = 实收金额:<span class="js-paidAmountSP white money-font">0</span></div>
                        </div>
                    </div>
                    <div class="bot-text">
                        挂账金额：<span id="signAmountSP" class="money-font">&yen;0</span>
                        <div class="total-details-tag js-tag"></div>
                        <div class="total-hide hide">
                            <div class="total-arrow"></div><div class="total-details">应收金额:<span class="js-reAmount white money-font">0</span> — 实收金额:<span class="js-payAmount white money-font">0</span> = 挂账金额：<span class="white js-signAmountSP money-font">0</span></div>
                        </div>
                    </div>

                    <div class="payee">
                        <span>收银人员：${operatorName}</span>
                        <span>账单日期：${orderInfo.createTimeStr}</span>
                    </div>
                </div>
            </div>

            <input type="hidden" id="paidAmount" value=""/>
            <input type="hidden" id="signAmount" value=""/>

            <div class="btn-box clearfix">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-confirm-bill">收款</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
            </div>
        </div>
        <!--工单确认账单信息 end-->
    </div>
    <!-- 右侧内容区 end -->
</div>

<!-- 支付渠道-->
<script type="text/html" id="methodTpl">
    <div class="method-box paychannel">
        <div class="form-label label-margin">
            收款方式
        </div>
        <div class="form-item yqx-downlist-wrap input-width">
            <input type="text"
                   name="paymentName"
                   class="yqx-input yqx-input-icon yqx-input-small js-method" value="${defaultPaymentName}"
                   placeholder="">
            <input type="hidden" name="paymentId" value="${defaultPaymentId}">
            <span class="fa icon-angle-down icon-small"></span>
        </div>
        <div class="form-label label-margin">
            收款
        </div>
        <div class="form-item input-width">
            <input type="text"
                   name="payAmount"
                   class="yqx-input yqx-input-icon yqx-input-small js-pay"
                   data-v-type="price"
                   maxlength="10"
                   value=""
                   placeholder="">
            <span class="fa icon-small">元</span>
        </div>
        <a href="javascript:;" class="js-delete delete-btn delete-color">删除</a>
    </div>
</script>
<#include "yqx/tpl/account/broadcast-tpl.ftl">
<#include "yqx/tpl/account/vip-card-tpl.ftl">
<#include "yqx/tpl/account/code-dialog-tpl.ftl">
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script src="${BASE_PATH}/static/js/page/settlement/debit/speedily-confirm-bill.js?a261c03dc798082e458b83fe1ef23661"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/debit/confirm-bill.css?706be8d8393871c6d4fa1f9678332a6e"/>
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
                <small>综合维修单详情－</small>
                <small>确认账单</small>
            </h3>
        </div>
        <!--工单价目明细表格 start-->
        <div class="pay-box clearfix js-tableShow">
            工单价目明细表格
            <div class="table-btn"><i class="icon-angle-down"></i><span>展开</span></div>
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
            <div class="pay-title">工单确认账单信息</div>
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
                        <input type="text" name="" class="yqx-input yqx-input-icon js-check-taoqicouponsn" value="" placeholder="请输入淘汽优惠码" id="taoqiCouponSn">
                    </div>
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-icon " value="" placeholder="优惠金额" disabled="disabled" id="taoqiCouponAmount">
                        <span class="fa icon-small">元</span>
                    </div>
                </div>

                <div class="remarks">
                    <div class="form-label">
                        备注
                    </div>
                    <div class="form-item">
                        <textarea class="yqx-textarea text-area-width" name="" id="remark" rows="1" placeholder="请输入备注"></textarea>
                    </div>
                </div>
                <div class="total-box">
                    <div class="bot-text">
                        工单总计金额：<span class="money-font">&yen;<#if orderInfo.orderAmount>#{orderInfo.orderAmount;m2M2}<#else>0.00</#if></span>
                        <div class="total-details-tag js-tag"></div>
                        <div class="total-hide hide">
                            <div class="total-arrow"></div><div class="total-details">服务费用：<span class="money-font white">&yen;${orderInfo.serviceAmount - orderInfo.serviceDiscount}</span> + 配件费用：<span class="money-font white">&yen;${orderInfo.goodsAmount-orderInfo.goodsDiscount}</span> + 附加费用: <span class="money-font white">&yen;${orderInfo.feeAmount-orderInfo.feeDiscount}</span> = 工单总计金额：<span class="money-font white">&yen; ${orderInfo.orderAmount}</span></div>
                        </div>
                    </div>
                    <div class="bot-text">
                        应收金额：<span class="js-receivable-amount money-font">&yen;<#if orderInfo.payAmount gt 0>#{orderInfo.payAmount;m2M2}<#else>#{orderInfo.orderAmount;m2M2}</#if></span>
                        <div class="total-details-tag js-tag"></div>
                        <div class="total-hide hide">
                            <input type="hidden" id="orderAmount" value="<#if orderInfo.orderAmount>#{orderInfo.orderAmount;m2M2}<#else>0.00</#if>">
                            <div class="total-arrow"></div><div class="total-details">工单总计金额：<span class="money-font white"> &yen;<#if orderInfo.orderAmount>#{orderInfo.orderAmount;m2M2}<#else>0.00</#if></span> - 优惠总金额： <span class="money-font white">&yen;<em class="js-order-discount-amount order-discount"></em></span> = 应收金额： <span class="money-font white"><em class="js-receivable-amount order-discount"></em></span></div>
                        </div>
                    </div>
                    <#if orderInfo && orderInfo.downPayment gt 0.00>
                        <div class="bot-text">
                            预付定金：<span class="money-font">&yen;#{orderInfo.downPayment;m2M2}</span>
                        </div>
                    </#if>
                    <div class="payee">
                        <span>收银人员：${operatorName}</span>
                        <#--<span>账单日期：${orderInfo.createTimeStr}</span>-->
                    </div>
                </div>
            </div>
            <input type="hidden" value="${orderInfo.id}" id="orderId">
            <input type="hidden" value="${orderInfo.carLicense}" id="carLicense">
            <div class="btn-box clearfix">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-confirm-bill">确认账单</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
            </div>
        </div>
        <!--工单确认账单信息 end-->
    </div>
    <!-- 右侧内容区 end -->
</div>
<#include "yqx/tpl/account/broadcast-tpl.ftl">
<#include "yqx/tpl/account/vip-card-tpl.ftl">
<#include "yqx/tpl/account/code-dialog-tpl.ftl">
<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script  src="${BASE_PATH}/static/js/page/settlement/debit/confirm-bill.js?b01833cc6c4f5f236dfb5228ae41838e"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
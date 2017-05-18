<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/debit/batch-debit.css?0c8c84b80b9032bb28c0d3cdcbb64dce"/>
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
                <small>批量收款</small>
            </h3>
        </div>
        <!--工单价目明细表格 start-->
        <div class="pay-box clearfix js-tableShow">
            工单价目明细表格
            <div class="table-btn"><i class="icon-angle-down"></i><span>展开</span></div>
        </div>
        <div class="show-table">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>工单编号</th>
                    <th>开单时间</th>
                    <th>车牌</th>
                    <th>联系人</th>
                    <th>联系电话</th>
                    <th>客户单位</th>
                    <th>挂账金额</th>
                </tr>
                </thead>
                <tbody>
                <#if orderInfoList>
                    <#list orderInfoList as orderInfo>
                    <tr class="js-order-id" data-id="${orderInfo.id}">
                        <td class="js-order-sn">
                            <div class="max-text js-show-tips">${orderInfo.orderSn}</div>
                        </td>
                        <td>${orderInfo.createTimeStr}</td>
                        <td>${orderInfo.carLicense}</td>
                        <td class="max-text js-show-tips">${orderInfo.contactName}</td>
                        <td>${orderInfo.contactMobile}</td>
                        <td>
                            <div class="max-text js-show-tips">${orderInfo.company}</div>
                        </td>
                        <td class="money-font">&yen;${orderInfo.signAmount}</td>
                    </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
        <!--工单价目明细表格 end-->

        <!--工单确认账单信息 start-->
        <div class="pay-inforbox">
            <div class="pay-title">工单确认账单信息</div>
            <div class="pay-con">
                <div class="method-title">收款方式&金额</div>
                <div class="method-box">
                    <div class="form-label">
                        收款方式
                    </div>
                    <div class="form-item yqx-downlist-wrap">
                        <input type="text" name="paymentName" class="yqx-input yqx-input-icon yqx-input-small js-method" data-v-type="required" value="${defaultPaymentName}" placeholder="">
                        <input type="hidden" name="paymentId" value="${defaultPaymentId}">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-label">
                        收款
                    </div>
                    <div class="form-item">
                        <input type="text" name="payAmount" class="yqx-input yqx-input-icon yqx-input-small js-pay" data-v-type="required | price" maxlength="10" value="" placeholder="">
                        <span class="fa icon-small">元</span>
                    </div>

                </div>

                <div class="remarks">
                    <div class="form-label">
                        备注
                    </div>
                    <div class="form-item">
                        <textarea class="yqx-textarea text-area-width"  name="remark" id="remark" rows="1" placeholder="请输入备注" maxlength="100"></textarea>
                    </div>
                </div>
                <div class="total-box">
                    <p>应收金额：<span class="money-font">&yen;</span><span class="money-font" id="newTotalReceivableAmount">${totalReceivableAmount?string("0.00")}</span></p>
                <#if totalDownPayment>
                    <p>预付定金: <em class="money-font">&yen;${totalDownPayment?string("0.00")}</em></p>
                </#if>
                <#if totalRedReceivableAmount != 0>
                    <p>冲红应收金额: <em class="money-font">&yen;${totalRedReceivableAmount?string("0.00")}</em></p>
                </#if>

                    <p>实收金额：<span class="money-font">&yen;</span><span class="money-font" id="newTotalPaidAmount">${totalPaidAmount?string("0.00")}</span></p>
                <#if totalRedPaidAmount != 0>
                    <p>冲红实收金额: <em class="money-font">&yen;${totalRedPaidAmount?string("0.00")}</em></p>
                </#if>
                    
                    <p>挂账金额：<span class="money-font">&yen;</span><span class="money-font" id="newTotalSignAmount">${totalSignAmount?string("0.00")}</span></p>
                <#if totalRedSignAmount != 0>
                    <p>冲红挂账金额: <em class="money-font">&yen;${totalRedSignAmount?string("0.00")}</em></p>
                </#if>

                    <input type="hidden" name="totalReceivableAmount" value="${totalReceivableAmount}"/>
                    <input type="hidden" name="totalPaidAmount" value="${totalPaidAmount}"/>
                    <input type="hidden" name="totalSignAmount" value="${totalSignAmount}"/>

                    <input type="hidden" name="totalRedReceivableAmount" value="${totalRedReceivableAmount}"/>
                    <input type="hidden" name="totalRedPaidAmount" value="${totalRedPaidAmount}"/>
                    <input type="hidden" name="totalRedSignAmount" value="${totalRedSignAmount}"/>

                    <div class="payee">
                        <span>收银人员：${SESSION_USER_NAME}</span>
                        <span>收款日期：${.now?string("yyyy-MM-dd HH:mm")}</span>
                    </div>
                </div>
            </div>
            <div class="btn-box clearfix">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-comfirm">收款</button>
                <#if !totalDownPayment>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-bad-bill">坏账</button>
                </#if>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
            </div>
        </div>
        <!--工单确认账单信息 end-->
    </div>
    <!-- 右侧内容区 end -->
</div>

<!--坏帐弹窗-->
<script type="text/html" id="badDebitTpl">
    <div class="dialog bad-debit">
        <div class="dialog-title">坏账处理</div>
        <div class="dialog-con">
            <div class="dialog-text debit-info">您确定要处理坏账金额：&yen;<span class="money-font" id="badAmount"></span>元？</div>
            <div class="form-label">
                备注
            </div>
            <div class="form-item form-text-width">
                <input type="text" id="badRemark" class="yqx-input" value="" maxlength="200" placeholder="请填写备注">
            </div>
            <div class="dialog-text"><span>收银人员：${SESSION_USER_NAME}</span> <span>收款日期：${.now?string("yyyy-MM-dd HH:mm")}</span></div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-bad-confirm">确认</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-bad-cancel">取消</button>
            </div>
        </div>
    </div>
</script>

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script src="${BASE_PATH}/static/js/page/settlement/debit/batch-debit.js?5f8feae4d835e1eaff3c02b3ac549708"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
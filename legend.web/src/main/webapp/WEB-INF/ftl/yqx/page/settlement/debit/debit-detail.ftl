<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/debit/debit-detail.css?8d33fa017efb857e1e8832162e92c3a8"/>
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
            <h3 class="headline fl">收款流水记录-
                <small>收款单详情</small>
            </h3>
        </div>
        <div class="pay-form" id="formData">
            <div class="show-grid">
                <input type="hidden" name="billId" value="${debitBill.id}"/>
                <input type="hidden" name="billName" value="${debitBill.billName}"/>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        收款类型:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                            ${debitBill.debitTypeName}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        款项名称:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text overflow-width js-show-tips">
                        ${debitBill.billName}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        付款方式:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                            <#if debitBillFlow>
                                <#if debitBillFlow.flowStatus == 0><#if debitBillFlow.flowType == 1>冲红</#if>${debitBillFlow.paymentName}<#else>坏账</#if>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-width">
                        收款金额:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text red">
                            <span class="money-font">&yen;${debitBillFlow.payAmount}</span>
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        付款方:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text overflow-width js-show-tips">
                        ${debitBill.payerName}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        收银人员:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        ${flowCreatorName}
                        </div>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-width">
                        收款日期:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        <#if debitBillFlow.flowTime>${debitBillFlow.flowTime?string("yyyy-MM-dd HH:mm")}</#if>
                        </div>
                    </div>
                </div>
                <div class="col-8">
                    <div class="form-label form-label-width">
                        备注:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text overflow-width js-show-tips overflow-remark">
                        ${debitBillFlow.remark}
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="btn-box clearfix">
            <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
            <#if isShowInvalid>
                <button class="yqx-btn yqx-btn-2 yqx-btn-small fr js-invalid">无效</button>
            </#if>
            <button class="yqx-btn yqx-btn-2 yqx-btn-small fr js-print">收款单打印</button>
        </div>
    </div>
</div>



<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script src="${BASE_PATH}/static/js/page/settlement/debit/debit-detail.js?e27a8e9d63ca76f924383f5a7d9ecfd4"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
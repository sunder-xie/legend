<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/pay/pay-detail.css?a3c105c533a8752b019d49efcf6f28a8"/>
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
            <h3 class="headline fl">付款单详情</h3>
        </div>
        <div class="pay-form" id="formData">
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-width">
                        付款类型:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                            ${payBillAndFlowResult.payTypeDTO.typeName}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        款项名称:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text overflow-width js-show-tips" title="">
                        ${payBillAndFlowResult.payBillDTO.billName}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        付款方式:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        ${payBillAndFlowResult.payBillFlowDTOList[0].paymentName}
                        </div>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-width">
                        付款金额:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        ${payBillAndFlowResult.payBillFlowDTOList[0].payAmount}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        收款方:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text overflow-width js-show-tips">
                        ${payBillAndFlowResult.payBillDTO.payeeName}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        收银人员:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text" >
                        ${flowCreatorName}
                        </div>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-width">
                        付款日期:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        ${flowBillTime}
                        </div>
                    </div>
                </div>
                <div class="col-8">
                    <div class="form-label form-label-width">
                        备注:
                    </div>
                    <div class="form-item">
                        <div class="yqx-text overflow-width js-show-tips overflow-remark">
                        ${payBillAndFlowResult.payBillDTO.remark}
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="btn-box clearfix">
            <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
        <#if isInvalid>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-invalid" data-id="${payBillAndFlowResult.payBillDTO.id}">无效</button>
        <#elseif isDeleted>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-delete" data-id="${payBillAndFlowResult.payBillDTO.id}">删除</button>
        </#if>
            <button class="yqx-btn yqx-btn-2 yqx-btn-small fr js-print">付款单打印</button>
        </div>
    </div>
</div>



<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script src="${BASE_PATH}/static/js/page/settlement/pay/pay-detail.js?454118dd2bd311bc731bc39035e76713"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
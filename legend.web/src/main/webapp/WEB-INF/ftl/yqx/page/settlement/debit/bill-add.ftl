<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/debit/bill-add.css?1c54a4250e3d432f988f5847cdb70e3d"/>
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
            <h3 class="headline fl">新建收款单</h3>
        </div>
        <div class="pay-form" id="formData">
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-width form-label-must">
                        收款类型
                    </div>
                    <div class="form-item yqx-downlist-wrap form-item-width">
                        <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-debit-type" value="" data-v-type="required" placeholder="请选择">
                        <input type="hidden" name="debitTypeId"/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width form-label-must">
                        款项名称
                    </div>
                    <div class="form-item form-item-width">
                        <input type="text" name="billName" class="yqx-input yqx-input-icon yqx-input-small" value="" data-v-type="required | maxLength:50" placeholder="请输入" maxlength="50">
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width form-label-must">
                        支付方式
                    </div>
                    <div class="form-item yqx-downlist-wrap form-item-width">
                        <input type="text" name="paymentName" class="yqx-input yqx-input-icon yqx-input-small js-payment-type" value="" placeholder="请选择" data-v-type="required">
                        <input type="hidden" name="paymentId"/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-width form-label-must">
                        支付金额
                    </div>
                    <div class="form-item form-item-width">
                        <input type="text" name="payAmount" class="yqx-input yqx-input-icon yqx-input-small js-pay-amount" value="" placeholder="请输入" data-v-type="required | price" maxlength="10">
                        <span class="fa icon-small">元</span>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        付款方
                    </div>
                    <div class="form-item form-item-width">
                        <input type="text" name="payerName" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="请输入">
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        收银人员
                    </div>
                    <div class="form-item form-item-width">
                        <input type="text" name="operatorName" class="yqx-input yqx-input-icon yqx-input-small" value="${SESSION_USER_NAME}" disabled data-v-type="required">
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-width">
                        收款日期
                    </div>
                    <div class="form-item form-item-width">
                        <input type="text" name="billTimeStr" class="yqx-input yqx-input-icon yqx-input-small js-gmtCreateStr" value="${.now?string("yyyy-MM-dd HH:mm")}" placeholder="" disabled="">
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                </div>
                <div class="col-8">
                    <div class="form-label form-label-width">
                        备注
                    </div>
                    <div class="form-item remarks-width">
                        <input type="text" name="remark" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="请输入" data-v-type="maxLength:100" maxlength="100">
                    </div>
                </div>
            </div>
        </div>
        <div class="btn-box">
            <button class="yqx-btn yqx-btn-2 yqx-btn-small js-submit">提交</button>
        </div>
    </div>
</div>



<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script src="${BASE_PATH}/static/js/page/settlement/debit/bill-add.js?15d431f36b74fffa7ca144aa8006d511"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/pay/pay-new.css?548723717536dea260cd6390c3710644"/>
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
            <h3 class="headline fl">新建付款单</h3>
        </div>
        <div class="pay-form" id="payForm">
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-width form-label-must">
                        付款类型
                    </div>
                    <div class="form-item form-width">
                        <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-pay-type" value="" placeholder="请选择" data-v-type=" required">
                        <input type="hidden" name="payTypeId" value=""/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width form-label-must">
                        款项名称
                    </div>
                    <div class="form-item form-width">
                        <input type="text" name="billName" class="yqx-input yqx-input-small" value="" placeholder="请输入" data-v-type="maxLength:50 | required" >
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width form-label-must">
                        支付方式
                    </div>
                    <div class="form-item form-width">
                        <input type="text" name="paymentName" class="yqx-input yqx-input-icon yqx-input-small js-method" value="" placeholder="请选择" data-v-type="required">
                        <input type="hidden" name="paymentId" value=""/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-width form-label-must">
                        支付金额
                    </div>
                    <div class="form-item form-width">
                        <input type="text"  name="amount" class="yqx-input yqx-input-icon yqx-input-small" value="" data-v-type="price | required " maxlength="10">
                        <span class="fa icon-small">元</span>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        收款方
                    </div>
                    <div class="form-item form-width">
                        <input type="text" name="payeeName" class="yqx-input yqx-input-small" value="" placeholder="请输入" data-v-type="maxLength:50">
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        收银人员
                    </div>
                    <div class="form-item form-width">
                        <input type="text" name="operatorName" class="yqx-input yqx-input-icon yqx-input-small" value="${operatorName}" placeholder="" disabled="">
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-width">
                        付款日期
                    </div>
                    <div class="form-item form-width">
                        <input type="text" name="billTime" class="yqx-input yqx-input-icon yqx-input-small" value="${.now?string("yyyy-MM-dd HH:mm")}" placeholder="" disabled="">
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                </div>
                <div class="col-8">
                    <div class="form-label form-label-width">
                        备注
                    </div>
                    <div class="form-item remarks-width">
                        <input type="text" name="remark" class="yqx-input yqx-input-small" value="" placeholder="请输入" data-v-type="maxLength:200">
                    </div>
                </div>
            </div>
        </div>
        <div class="btn-box">
            <button class="yqx-btn yqx-btn-2 yqx-btn-small js-save-btn">提交</button>
        </div>
    </div>
</div>



<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/settlement/pay/pay-add.js?625de592eb68e87f6fbca5f4802fa063"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
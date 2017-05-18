<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/finance/finance-detail.css?29c00e819d8eeb5b4b040acdd45dc871"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">银行卡信息</h3>
        </div>
        <div class="content" id="formData">
            <input type="hidden" id="isShop" value="${isShop}"/>
            <input type="hidden" name="id" value="${financeAccount.id}"/>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    开户银行:
                </div>
                <div class="form-item">
                    <input type="text" name="bank" class="yqx-input yqx-input-icon yqx-input-small js-bank" value="${financeAccount.bank}" placeholder="请选择银行" data-v-type="required" >
                    <span class="fa icon-angle-down icon-small"></span>
                    <div class="yqx-select-options" style="width: 140px; display: none;">
                        <dl>
                        <#list bankEnums as item>
                            <dd class="yqx-select-option" data-key="${item.message}" <#if item.message == financeAccount.bank> selected </#if> > ${item.message}</dd>
                        </#list>
                        </dl>
                    </div>
                </div>
                <div class="form-item">
                    <input type="text" name="bankProvince" class="yqx-input yqx-input-icon yqx-input-small js-province" value="${financeAccount.bankProvince}" placeholder="省" data-v-type="required">
                    <input type="hidden" value="${financeAccount.bankProvinceId}" name="bankProvinceId">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <div class="form-item city-box">
                    <input type="text" name="bankCity" class="yqx-input yqx-input-icon yqx-input-small js-city" value="${financeAccount.bankCity}" placeholder="市" data-v-type="required">
                    <input type="hidden" value="${financeAccount.bankCityId}" name="bankCityId">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    开户支行:
                </div>
                <div class="form-item">
                    <input type="text" name="accountBank" class="yqx-input yqx-input-small" value="${financeAccount.accountBank}" placeholder="" data-v-type="required">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    收款人:
                </div>
                <div class="form-item">
                    <input type="text" name="accountUser" class="yqx-input yqx-input-small" value="${financeAccount.accountUser}" placeholder="" data-v-type="required">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    银行卡号:
                </div>
                <div class="form-item w-214">
                    <input type="text" name="account" class="yqx-input yqx-input-small" value="${financeAccount.account}" placeholder="" data-v-type="required" autocomplete="new-password">
                </div>
            </div>
            <#if isShop == 'false'>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    登陆密码:
                </div>
                <div class="form-item w-214">
                    <input type="password" id="password" class="yqx-input yqx-input-small" value="" placeholder="" data-v-type="required" autocomplete="new-password">
                </div>
            </div>
            </#if>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    联系电话:
                </div>
                <div class="form-item">
                    <input type="text" name="mobile" class="yqx-input yqx-input-small" value="${shopManager.mobile}" placeholder="" disabled>
                </div>
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-getCode">获取验证码</button>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    验证码:
                </div>
                <div class="form-item w-70">
                    <input type="text" name="identifyingCode" class="yqx-input yqx-input-small js-code" value="" placeholder="" data-v-type="required">
                </div>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save">保存</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goBack">返回</button>

            </div>
        </div>
    </div>
</div>

<script src="${BASE_PATH}/static/js/page/setting/finance/finance-detail.js?15c4ebbf4a57fc102f7459f675b03e85"></script>
<#include "yqx/layout/footer.ftl">
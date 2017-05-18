<#--银行卡信息-->
<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/ax_insurance/settle/bank-card.css?89f0ca863c11db7416a270d358643120"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">
    <div class="content clearfix">
        <!-- 左侧导航区 start -->
        <#include "yqx/page/ax_insurance/left-nav.ftl">
        <!-- 左侧导航区 end -->
        <!-- 右侧内容区 start -->
        <div class="order-right fl">
            <div class="title clearfix">
                <h3 class="fl">银行卡信息</h3>
                <p class="fr">提示:与保险业务相关的门店收入，每月由淘汽档口财务统一打入以下银行账户</p>
            </div>
            <div class="form-box" id="bankInfo">
                <div class="show-grid">
                    <input type="hidden" name="id" value="${account.id}">
                    <div class="form-label form-label-must">
                        开户银行
                    </div>
                    <div class="form-item">
                        <input type="text" name="bank" class="yqx-input yqx-input-icon js-bank" value="${account.bank}" placeholder="请选择银行" data-v-type="required">
                        <span class="fa icon-angle-down"></span>
                    </div>
                    <div class="form-item">
                        <input type="text" name="bankProvince" class="yqx-input yqx-input-icon js-province" value="${account.bankProvince}" placeholder="请选择省" data-v-type="required">
                        <input type="hidden" name="bankProvinceId" value="${account.bankProvinceId}">
                        <span class="fa icon-angle-down"></span>
                    </div>
                    <div class="form-item">
                        <input type="text" name="bankCity" class="yqx-input yqx-input-icon js-city" value="${account.bankCity}" placeholder="请选择市" data-v-type="required">
                        <input type="hidden" name="bankCityId" value="${account.bankCityId}">
                        <span class="fa icon-angle-down "></span>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        开户支行
                    </div>
                    <div class="form-item long">
                        <input type="text" name="accountBank" class="yqx-input" value="${account.accountBank}" placeholder="请输入" data-v-type="required">
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        收款人
                    </div>
                    <div class="form-item">
                        <input type="text" name="accountUser" class="yqx-input" value="${account.accountUser}" placeholder="请输入" data-v-type="required">
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        银行卡号
                    </div>
                    <div class="form-item">
                        <input type="text" name="account" class="yqx-input" value="${account.account}" placeholder="请输入" data-v-type="required">
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        联系电话
                    </div>
                    <div class="form-item yz-box">
                        <input type="text" name="mobile" class="yqx-input yz" value="${mobile}" placeholder="请输入" data-v-type="required | phone" disabled>
                        <button class="yqx-btn yqx-btn-3 yqx-btn-small yqx-btn-micro js-captcha">发送验证码</button>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        验证码
                    </div>
                    <div class="form-item">
                        <input type="text" name="identifyingCode" class="yqx-input" value="" placeholder="请输入" data-v-type="required">
                    </div>
                </div>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-save-btn">提交</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
            </div>
        </div>
        <!-- 右侧内容区 end -->
    </div>
</div>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/ax_insurance/settle/bank-card.js?60795340d463272acb5fc57efbe74eab"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
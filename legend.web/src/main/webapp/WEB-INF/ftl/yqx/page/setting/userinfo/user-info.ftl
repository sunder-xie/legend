<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/userinfo/user-info.css?f0135324743ef7e584dd9c309e1492c5"/>
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
            <h3 class="headline">个人信息</h3>
        </div>
        <div class="content" id="formData">
            <input type="hidden" name="managerId" id="managerId" value="${shopManagerInfo.managerId}"/>
            <div class="company-info clearfix">
                <div class="company-pic fl">
                    <#if isAdmin == 1>
                        <img src="${BASE_PATH}/static/img/page/setting/boss.png"/>
                        <p>老板</p>
                    <#else >
                        <img src="${BASE_PATH}/static/img/page/setting/technician.png"/>
                        <p>${shopManagerInfo.rolesName}</p>
                    </#if>
                </div>
                <div class="company-box fl <#if !showTechnic>w-290</#if>">
                    <h3 class="js-show-tips shop-address">${shopManagerInfo.name} <span>${shopManagerInfo.mobile}</span></h3>
                    <p class="js-show-tips shop-address">${shop.name}</p>
                    <p class="js-show-tips shop-address">${shop.address}</p>
                </div>
                <#if showTechnic??>
                <div class="shop-box fl">
                    <p>技师认证</p>
                    <#if hasAuth == "true">
                        <h3>已认证</h3>
                        <a href="${BASE_PATH}/shop/setting/technician/technician-detail"> <img src="${BASE_PATH}/static/img/page/setting/shopSetting/edit-ico.png"/> 修改</a>
                    <#else >
                        <h3>未认证</h3>
                        <#if showTechnic == "show">
                            <a href="${BASE_PATH}/shop/setting/technician/technician-detail"> <img src="${BASE_PATH}/static/img/page/setting/shopSetting/edit-ico.png"/> 去认证</a>
                        </#if>
                    </#if>
                </div>
                </#if>
                <div class="shop-box1 fl">
                    <p>银行卡信息</p>
                    <#if financeAccount??>
                        <h3>${financeAccount.bank}  ${financeAccount.account}</h3>
                        <a href="${BASE_PATH}/shop/setting/finance/finance-detail-user"> <img src="${BASE_PATH}/static/img/page/setting/shopSetting/edit-ico.png"/> 修改</a>
                    <#else >
                    <h3>暂无银行卡信息</h3>
                        <a href="${BASE_PATH}/shop/setting/finance/finance-detail-user"> <img src="${BASE_PATH}/static/img/page/setting/shopSetting/edit-ico.png"/> 去绑定</a>
                    </#if>
                </div>
            </div>
            <div class="form-box1">
                <div class="show-grid">
                    <div class="form-label">
                        账号:
                    </div>
                    <div class="form-item">
                        <input type="text" name="account" class="yqx-input yqx-input-small" value="${shopManagerInfo.account}" placeholder="" disabled>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label">
                        姓名:
                    </div>
                    <div class="form-item">
                        <input type="text" name="name" id="name" class="yqx-input yqx-input-small" value="${shopManagerInfo.name}" placeholder="" data-v-type="required">
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label">
                        联系电话:
                    </div>
                    <div class="form-item">
                        <input type="text" name="mobile" id="mobile" class="yqx-input yqx-input-small" value="${shopManagerInfo.mobile}" placeholder="" disabled>
                        <input type="hidden" id="oldMobile" value="${shopManagerInfo.mobile}"/>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label">
                        APP角色:
                    </div>
                    <div class="form-item w-700">
                        <ul class="app-roles">
                            <#list rolesNameList as rolesName>
                                <li>${rolesName}</li>
                            </#list>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save">保存</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goBack">返回</button>
            </div>
        </div>
    </div>
</div>

<script src="${BASE_PATH}/static/js/page/setting/userinfo/user-info.js?2fe6b1b827004e58a3b0d46c7f4beae1"></script>
<#include "yqx/layout/footer.ftl">
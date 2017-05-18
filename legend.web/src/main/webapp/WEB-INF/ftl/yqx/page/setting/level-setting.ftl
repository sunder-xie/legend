<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/level-setting.css?68b5843a2bf32f3ff5d9344103455f3e"/>
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
            <h3 class="headline">安全登录设置</h3>
        </div>
        <div class="content">
            <div class="level-title">
                选择门店安全级别
            </div>
            <div class="level-box clearfix">
                <div class="level fl <#if level == 1>level-current</#if>">
                    <div class="level-ico level1"></div>
                    <div class="level-num">安全级别【一级】</div>
                    <div class="text-box">
                        <div class="level-text">只允许账号密码登录</div>
                    </div>
                    <div class="select-btn-box">
                    <#if level == 1>
                        <a href="javascript:;" class="js-select select-btn-text" data-level="1">已选择</a>
                    <#else>
                        <a href="javascript:;" class="select-btn js-select" data-level="1">选择</a>
                    </#if>
                    </div>
                    <div class="line">
                        <div class="arrow-top"></div>
                    </div>
                </div>
                <div class="level fl <#if level == 2>level-current</#if>">
                    <div class="level-ico level2"></div>
                    <div class="level-num">安全级别【二级】</div>
                    <div class="text-box">
                        <div class="level-text">只允许扫码登录</div>
                    </div>
                    <div class="select-btn-box">
                    <#if level == 2>
                        <a href="javascript:;" class="js-select select-btn-text" data-level="2">已选择</a>
                    <#else>
                        <a href="javascript:;" class="select-btn js-select" data-level="2">选择</a>
                    </#if>
                    </div>
                    <div class="line">
                        <div class="arrow-top"></div>
                    </div>
                </div>

                <div class="level fl <#if level == 3>level-current</#if>">
                    <div class="level-ico level3"></div>
                    <div class="level-num">安全级别【三级】</div>
                    <div class="text-box">
                        <div class="level-text">只允许扫码登录</div>
                    </div>
                    <div class="select-btn-box">
                    <#if level == 3>
                        <a href="javascript:;" class="js-select select-btn-text" data-level="3">已选择</a>
                    <#else>
                        <a href="javascript:;" class="select-btn js-select" data-level="3">选择</a>
                    </#if>
                    </div>
                    <div class="line">
                        <div class="arrow-top"></div>
                    </div>
                </div>
            </div>
            <div class="save-service">
                <div class="service-title">设置安全服务</div>
                <ul class="service-list">
                    <li>
                        <div class="setting-title">模块权限设置</div>
                        <div class="setting-text">模块权限管控</div>
                        <a href="${BASE_PATH}/shop/setting/roles/roles-list" class="see yqx-link-3 js-see"><img src="${BASE_PATH}/static/img/page/setting/eye-ico.png"/>查看</a>
                    </li>
                    <li>
                        <div class="setting-title">员工登录时间设置</div>
                        <div class="setting-text">控制员工登录有效时间</div>
                        <a href="${BASE_PATH}/shop/setting/user-info/user-list" class="see yqx-link-3 js-see"><img src="${BASE_PATH}/static/img/page/setting/eye-ico.png"/>查看</a>
                    </li>
                <#if level == 2 || level == 3>
                    <li class="level2-show">
                        <div class="setting-title">App授权设置</div>
                        <div class="setting-text">App授权登录，防止账号泄露导致不安全</div>
                        <a href="${BASE_PATH}/shop/security-login/device-list" class="see yqx-link-3 js-see"><img src="${BASE_PATH}/static/img/page/setting/eye-ico.png"/>查看</a>
                    </li>
                <#else>
                    <li class="level2-show hide">
                        <div class="setting-title">App授权设置</div>
                        <div class="setting-text">App授权登录，防止账号泄露导致不安全</div>
                        <a href="${BASE_PATH}/shop/security-login/device-list" class="see yqx-link-3 js-see"><img src="${BASE_PATH}/static/img/page/setting/eye-ico.png"/>查看</a>
                    </li>
                </#if>
                <#if level == 3>
                    <li class="level3-show">
                        <div class="setting-title">环境控制设置</div>
                        <div class="setting-text">控制员工在门店中使用系统</div>
                        <a href="${BASE_PATH}/shop/security-login/network-setting" class="see yqx-link-3 js-see"><img src="${BASE_PATH}/static/img/page/setting/eye-ico.png"/>查看</a>
                    </li>
                <#else>
                    <li class="level3-show hide">
                        <div class="setting-title">环境控制设置</div>
                        <div class="setting-text">控制员工在门店中使用系统</div>
                        <a href="${BASE_PATH}/shop/security-login/network-setting" class="see yqx-link-3 js-see"><img src="${BASE_PATH}/static/img/page/setting/eye-ico.png"/>查看</a>
                    </li>
                </#if>
                </ul>
            </div>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/setting/level-setting.js?da423c070ef2fc1d507de3137a520f9c"></script>
<#include "yqx/layout/footer.ftl">

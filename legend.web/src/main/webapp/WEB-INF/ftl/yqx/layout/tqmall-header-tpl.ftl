<div class="yqx-header" data-ref-tpl="layout/tqmall-header-tpl">
    <div class="header-qrcode-box hide">
        <div class="qrcode-back"></div>
        <div class="header-qrcode">
            <img src="${BASE_PATH}/static/img/page/home/subscription-qrcode.jpg">
            <button class="qrcode-close js-qrcode-close"></button>
            <div class="header-qrcode-text">
                <h3 class="font-yahei"></h3>
                <h4 class="font-yahei">手机扫一扫</h4>
            </div>
        </div>
    </div>
    <div class="header">
        <div class="topnav">
            <a href="${BASE_PATH}/home" class="header-logo">
                <img src="${BASE_PATH}/static/img/common/header/logo_1.png">
            </a>
            <#-- 是否是管理员 -->
            <input type="hidden" name="isAdmin" value="${SESSION_USER_IS_ADMIN}"/>
            <#-- 顶部导航条 start -->
            <ul class="topmenu">
                <input type="hidden" id="moduleUrl" value="${moduleUrl}">
            <#if SESSION_USER_IS_ADMIN == 1>
                <#if moduleUrl =="wechat">
                    <#if SESSION_SHOP_LEVEL?number != 10>
                    <li data-mu="wechat"><a href="${BASE_PATH}/shop/wechat">微信公众号</a></li>
                    </#if>
                <#elseif moduleUrl == "settings">
                    <li data-mu="settings"><a href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-list">设置</a></li>
                <#elseif moduleUrl == "report">
                    <li data-mu="report"><a href="${BASE_PATH}/shop/report">报表</a></li>
                <#elseif moduleUrl == "help">
                    <li data-mu="help"><a href="${BASE_PATH}/shop/help">帮助中心</a></li>
                <#else>
                    <li data-mu="home"><a href="${BASE_PATH}/guide">首页</a></li>
                    <li data-mu="reception"><a href="${BASE_PATH}/shop/reception">接车维修</a></li>
                    <li data-mu="warehouse"><a href="${BASE_PATH}/shop/warehouse/in/in-list">仓库</a></li>
                    <li data-mu="settlement"><a href="${BASE_PATH}/shop/settlement/debit/order-list">财务</a>
                    <li data-mu="customer"><a href="${BASE_PATH}/account">客户管理</a>
                </#if>
            <#else>
                <#assign homeFlag =0>
            <#if SESSION_USER_ROLE_FUNC?exists>
                <#list SESSION_USER_ROLE_FUNC as temp>
                    <#if moduleUrl =="wechat">
                        <#if temp.name =="微信公众号首页">
                            <#if SESSION_SHOP_LEVEL?number != 10>
                            <li data-mu="wechat"><a href="${BASE_PATH}/shop/wechat">微信公众号</a></li>
                            </#if>
                        </#if>
                    <#elseif moduleUrl == "settings">
                        <#if temp.name =="设置首页">
                            <li data-mu="settings"><a href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-list">设置</a></li>
                        </#if>
                    <#elseif moduleUrl == "report">
                        <#if temp.name =="报表首页">
                            <li data-mu="report"><a href="${BASE_PATH}/shop/report">报表</a></li>
                        </#if>
                    <#else>
                        <#assign homeFlag= homeFlag +1/>
                        <#if homeFlag == 1>
                        <li data-mu="home"><a href="${BASE_PATH}/guide">首页</a></li>
                        </#if>
                    <#if temp.name =="接车维修首页">
                        <li data-mu="reception"><a href="${BASE_PATH}/shop/reception">接车维修</a></li>
                    </#if>
                    <#if temp.name =="仓库首页">
                        <li data-mu="warehouse"><a href="$${BASE_PATH}/shop/warehouse/in/in-list">仓库</a></li>
                    </#if>
                    <#if temp.name =="财务首页">
                        <li data-mu="settlement"><a href="${BASE_PATH}/shop/settlement/debit/order-list">财务</a>
                    </#if>
                    <#if temp.name =="客户管理首页">
                        <li data-mu="customer"><a href="${BASE_PATH}/account">客户管理</a>
                    </#if>
                </#if>
                </#list>
            </#if>
            </#if>
            </ul>
            <script>
                $(function () {
                    var moduleUrl = $('#moduleUrl').val();
                    // [data-mu~="module1 module2"]
                    $('[data-mu~="' + moduleUrl + '"]').addClass('current');
                });
            </script>
            <#-- 顶部导航条 end -->
            <#-- 顶部工具条 start -->
            <ul class="header-ul-right fr">
                <li class="header-entry">
                    <a href="${BASE_PATH}/home"><span class="user-icon"></span><span>云修生态</span></a>
                    <#include "yqx/tpl/common/header-dialog/common-dialog/tqmall-dialog-tpl.ftl">
                </li>
                <li class="li-user-wrapper">
                    <div class="nav-user-box">
                        <span class="user-icon person-icon"></span><div class="user-account">
                    ${SESSION_USER_NAME}</div>
                        <div class="user-info">
                            <div class="angle-top"></div>
                            <div class="info-show">
                                <div class="fr color-333"><span>${SESSION_PVGROLE_NAME}<#if SESSION_READYCHECK><i class="tech-icon verified-icon"></i><#else><i class="tech-icon unverified-icon"></i></#if></span></div>
                                <div class="user-name color-333">${SESSION_USER_NAME}</div>
                            </div>
                            <div class="links-box">
                                <a href="${BASE_PATH}/shop/member/show_user_info?managerId=${SESSION_USER_ID}">个人信息</a>
                                <a href="${BASE_PATH}/shop/member/change">修改密码</a>
                                <a class="js-user-logout" href="javascript:void(0)">退出</a>
                            </div>
                            <script>
                                $(function () {
                                    var src = {
                                        app: BASE_PATH + '/static/img/page/home/app-qrcode.png',
                                        subscription: BASE_PATH + '/static/img/page/home/subscription-qrcode.jpg',
                                    };

                                    $('.yqx-header .js-header-qrcode').on('click.qrcode', function () {
                                        var data = $(this).data('src');
                                        var text = $(this).find('p').text();

                                        $('.yqx-header .header-qrcode-box').removeClass('hide')
                                                .find('img').attr('src', src[data])
                                                .end().find('.header-qrcode-text h3').text(text);
                                    });

                                    $('.yqx-header .js-qrcode-close').on('click.qrcode', function () {
                                        $('.yqx-header .header-qrcode-box').addClass('hide');
                                    });
                                    //登出前清空cookie
                                    $(".yqx-header .js-user-logout").click(function () {
                                        window.location.href = BASE_PATH + "/logout";
                                    });

                                    $('.yqx-header .nav-user-box').hover(function () {
                                        // username 较长的情况
                                        var h1 = $('.yqx-header .user-name')[0].offsetTop,
                                                e2 = $('.yqx-header .info-show .fr');

                                        if (e2.length && h1 > e2[0].offsetTop) {
                                            e2.removeClass('fr')
                                                    .css('padding-bottom', '3px');

                                            $('.yqx-header .info-show .rate-info').css('padding-top', 5);
                                        }
                                    });

                                    // 调整 生态图标的长度
                                    var li = $('.business-entry li');
                                    var len = li.length;

                                    if(len <= 10 && len > 8) {
                                        li.eq(4).css('margin-right', 0)
                                                .parent().parent().css('width', 400);
                                    }
                                });
                            </script>
                        </div>
                    </div>

                </li>
            </ul>
            <#-- 顶部工具条 end -->
        </div>
    </div>
</div>

<script src="${BASE_PATH}/static/js/common/base/head-menu.js?8113e2495849369d41c5d4ce7da46779"></script>
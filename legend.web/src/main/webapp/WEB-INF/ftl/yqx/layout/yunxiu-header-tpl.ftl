<div class="yqx-header" data-ref-tpl="layout/yunqixiu-header-tpl">
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
                <li data-mu="home"><a href="${BASE_PATH}/guide?refer=header">首页</a></li>
                <li data-mu="reception"><a href="${BASE_PATH}/shop/reception?refer=header">接车维修</a></li>
                <li data-mu="warehouse"><a href="${BASE_PATH}/shop/warehouse/out/order-out-list?refer=header">仓库</a></li>
                <li data-mu="settlement"><a href="${BASE_PATH}/shop/settlement/debit/order-list?refer=header">财务</a>
                <li data-mu="buy" class="pos"><a href="${BASE_PATH}/shop/buy/tqmall_goods/index?refer=header">淘汽采购</a></li>
                <li data-mu="marketing customer wechat"><a href="${BASE_PATH}/marketing?refer=header">客户营销</a></li>
                <li data-mu="report"><a href="${BASE_PATH}/shop/report?refer=header">报表</a></li>
            <#else>
                <li data-mu="home"><a href="${BASE_PATH}/guide?refer=header">首页</a></li>
                <#assign flag =0>
                <#if SESSION_USER_ROLE_FUNC?exists>
                <#list SESSION_USER_ROLE_FUNC as temp>
                    <#if temp.name =="接待首页" || temp.name =="派工首页">
                        <#assign flag= flag +1/>
                        <#if flag == 1>
                            <li data-mu="reception"><a href="${BASE_PATH}/shop/reception?refer=header">接车维修</a></li>
                        </#if>
                    <#elseif temp.name =="仓库首页">
                        <li data-mu="warehouse"><a href="${BASE_PATH}/shop/warehouse/out/order-out-list?refer=header">仓库</a></li>
                    <#elseif temp.name =="结算首页">
                        <li data-mu="settlement"><a href="${BASE_PATH}/shop/settlement/debit/order-list?refer=header">财务</a></li>
                    <#elseif temp.name =="淘汽首页">
                        <li data-mu="buy" class="pos"><a href="${BASE_PATH}/shop/buy/tqmall_goods/index?refer=header">淘汽采购</a></li>

                    <#elseif temp.name =="营销首页">
                        <li data-mu="marketing" class="pos"><a href="${BASE_PATH}/marketing?refer=header">客户营销</a></li>
                    <#elseif temp.name == "报表首页">
                        <li data-mu="report"><a href="${BASE_PATH}/shop/report?refer=header">报表</a></li>
                    </#if>
                </#list>
                </#if>
            </#if>
                <li data-mu="other" class="tophelp">
                    <a>更多</a>
                    <ul class="topHelpNav">
                    <#if SESSION_USER_IS_ADMIN == 1>
                            <li><a href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-list?refer=header">设置</a></li>
                        <#if SESSION_SHOP_LEVEL?number != 10 && SESSION_SHOP_LEVEL?number != 11 && SESSION_SHOP_LEVEL?number != 12>
                            <li><a href="${BASE_PATH}/shop/activity/main?refer=header">引流活动</a></li>
                        </#if>
                    <#else>
                        <#if SESSION_USER_ROLE_FUNC?exists>
                        <#list SESSION_USER_ROLE_FUNC as temp>
                            <#if temp.name =="设置首页">
                                    <li onclick="location.href='${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-list?refer=header'">设置</li>
                            <#elseif temp.name =="在线引流首页">
                                <#if SESSION_SHOP_LEVEL?number != 10 && SESSION_SHOP_LEVEL?number != 11 && SESSION_SHOP_LEVEL?number != 12>
                                    <li><a href="${BASE_PATH}/shop/activity/main?refer=header">引流活动</a></li>
                                </#if>
                            </#if>
                        </#list>
                        </#if>
                    </#if>
                        <#if SESSION_SHOP_LEVEL != 10 && SESSION_SHOP_LEVEL != 11>
                            <li><a href="${BASE_PATH}/shop/tech/book?refer=header">淘汽知道</a></li>
                        </#if>
                        <li><a href="${BASE_PATH}/shop/help?refer=header">系统使用帮助</a></li>
                    </ul>
                </li>
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
            <#if SESSION_USER_IS_ADMIN == 1>
                <li class="li-form">
                    <form action="${BASE_PATH}/shop/customer/search/common?refer=header" method="post" name="commonForm" id="commonForm">
                        <span class="header-search-icon"></span>
                        <input class="header-search" name="keyword" id="keyword" placeholder="车牌查找" type="text"/>
                    </form>
                </li>
            <#else>
                <#if SESSION_USER_ROLE_FUNC?exists>
                <#list SESSION_USER_ROLE_FUNC as temp>
                    <#if temp.name == "接待首页">
                        <li class="li-form li-form-2">
                            <form action="${BASE_PATH}/shop/customer/search/common?refer=header" method="post" name="commonForm" id="commonForm">
                                <span class="header-search-icon"></span>
                                <input class="header-search" name="keyword" id="keyword" placeholder="车牌查找" type="text"/>
                            </form>
                        </li>
                    </#if>
                </#list>
                </#if>
            </#if>
                <li class="header-entry">
                    <a href="${BASE_PATH}/home?refer=header"><span class="user-icon"></span><span>云修生态</span></a>
                    <#include "yqx/tpl/common/header-dialog/common-dialog/yunxiu-dialog-tpl.ftl">
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

                                    //新老版本切换
                                    $(".js-switch-version").click(function () {
                                        var version = $(this).data("version");
                                        var module = $(this).data("module");
                                        var url = $(this).data("url");
                                        $.ajax({
                                            url: BASE_PATH + "/shop/gray/release/switch-version",
                                            data: {
                                                version: version,
                                                moduleKey: module
                                            },
                                            type: 'POST',
                                            success: function (data) {
                                                if (data.success) {
                                                    window.location.href = url;
                                                } else {
                                                    try {
                                                        seajs.use([
                                                            'dialog'
                                                        ], function (dg) {
                                                            dg.warn(data.errMsg);
                                                        });
                                                    } catch (e) {
                                                        layer.msg(data.errMsg);
                                                    }
                                                }
                                            }
                                        });
                                    });
                                    $('.yqx-header .js-header-qrcode').on('click.qrcode', function () {
                                        var data = $(this).data('src');
                                        var text = $(this).find('p').text();

                                        $('.yqx-header .header-qrcode-box').removeClass('hide')
                                                .find('img').attr('src', src[data])
                                                .end().find('.header-qrcode-text h3').text(text);
                                    });

                                    $(document).on('click','.js-app',function(){
                                        $.ajax({
                                            url:BASE_PATH + '/shop/user/operate/count?refer=shopApp-dialog'
                                        });
                                    });
                                    $(document).on('click','.js-wx',function(){
                                        $.ajax({
                                            url:BASE_PATH + '/shop/user/operate/count?refer=subNum-dialog'
                                        });
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
                                    var len = li.length, num = 5;

                                    if(len <= 10 && len > 5) {
                                        num = 4;
                                        li.parent().parent().css('width', 400);
                                    }
                                    if(len > 4) {
                                        li.eq(num).css('margin-right', 0);
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

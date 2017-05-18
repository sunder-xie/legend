<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>淘汽云修</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <link rel="shortcut icon" href="${BASE_PATH}/static/img/common/base/favicon_x48.ico" type="image/x-icon"/>
    <link rel="stylesheet" type="text/css"
          href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d"/>
    <link rel="stylesheet" type="text/css"
          href="${BASE_PATH}/static/third-plugin/Font-Awesome-3.2.1/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css"
          href="${BASE_PATH}/static/css/page/home/header.css?b21c268e146d739cad252b338f92c148">
    <script>
        var BASE_PATH = "${BASE_PATH}";
        var COOKIE_PREFIX = "legend_cookie_";
        var Components = {};
    </script>
    <script src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script src="${BASE_PATH}/static/third-plugin/seajs/sea.js"></script>
    <script src="${BASE_PATH}/static/third-plugin/path.config.js?98f845edf92898b7a23a5b384185c04c"></script>
${head}
    <link rel="stylesheet" href="${BASE_PATH}/static/third-plugin/layer/skin/layer.css">

</head>
<body>
<!-- 公共头部 start -->
<div class="yqx-header">
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
    <input type="hidden" id="isTqmall" value="${SESSION_SHOP_IS_TQMALL_VERSION}">
    <div class="header">
        <div class="home-topnav topnav">
            <a href="${BASE_PATH}/home" class="header-logo">
                <img src="${BASE_PATH}/static/img/common/header/logo_1.png">
            </a>
            <img src="${BASE_PATH}/static/img/common/header/home-title.png">
            <input type="hidden" name="isAdmin" value="${SESSION_USER_IS_ADMIN}"/>
            <ul class="topmenu">
                <li <#if moduleUrl=="newHome"> class="current" </#if>>
                    <a href="${BASE_PATH}/home">首页</a>
                </li>
                <li class="header-entry">
                    <a href="${BASE_PATH}/home">云修生态</a>
                    <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                        <#include "yqx/tpl/common/header-dialog/home-dialog/yunxiu-home-dialog-tpl.ftl">
                    <#else>
                        <#include "yqx/tpl/common/header-dialog/home-dialog/tqmall-home-dialog-tpl.ftl">
                    </#if>
                </li>
                <li <#if moduleUrl=="news"> class="current" </#if>>
                    <a href="${BASE_PATH}/home/news/news-list">云修资讯</a>
                </li>
                <li <#if moduleUrl=="products"> class="current" </#if>>
                    <a href="${BASE_PATH}/home/products/products-list">云修产品</a>
                </li>
                <li class="li-user-wrapper">
                    <div class="nav-user-box">
                        <img src="${BASE_PATH}/static/img/common/header/home-user-icon.png"><div class="user-account font-arial">
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
                                    var permission = {};
                                    var src = {
                                        app: BASE_PATH + '/static/img/page/home/app-qrcode.png',
                                        subscription: BASE_PATH + '/static/img/page/home/subscription-qrcode.jpg',
                                    };

                                    var isTqmall = $('#isTqmall').val();
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
                                        window.location.href =  BASE_PATH + "/logout";
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

                                    // 权限控制
                                    seajs.use('dialog', function (dg) {

                                    $(document).on('click', '.js-check-func', function (e) {
                                        var name = $(this).data('funcName');

                                        if(permission[name] === false) {
                                            dg.warn('您的账号没有登陆此入口权限，请联系管理员进行开通')
                                            e.preventDefault();
                                        } else if(permission[name] === 0) {
                                            dg.warn('请稍后再试');
                                            e.preventDefault();
                                        } else if(permission[name] == null) {
                                            dg.warn('请稍后再试');
                                            getPermission.call(this);
                                            e.preventDefault();
                                        }

                                        if(this.tagName != 'A' && permission[name] === true) {
                                            window.location.href = $(this).data('href') || $(this).attr('href');
                                            e.stopImmediatePropagation();
                                            e.stopPropagation();
                                            e.preventDefault();
                                        }
                                    });

                                    $(".js-check-func").each(getPermission);

                                    function getPermission(e) {
                                        var href = $(this).data('href');
                                        var funcName = $(this).data('funcName');
                                        var data, isRetUrl = false;;

                                        if(permission[funcName] === 0 || !funcName) {
                                            return;
                                        }
                                        // 客户营销特殊处理 (微信公众号使用客户营销权限)
                                        if (funcName === "客户营销" || (funcName === "微信公众号" && isTqmall != 'true')) {
                                            data = ["客户分析", "提醒中心", "精准营销", "短信充值", "门店推广", "客户管理"];
                                            isRetUrl = true;
                                        }　else　{
                                            data = [funcName];
                                        }

                                        permission[funcName] = 0;
                                        $.ajax({
                                            type: 'GET',
                                            url: BASE_PATH + '/shop/func/check_func_list',
                                            global: false,
                                            data: {
                                                funcNameList: data.join(',')
                                            },
                                            success: function (result) {
                                                if (result.success) {
                                                    permission[funcName] = true;

                                                    $('a[data-func-name="' + funcName + '"]')
                                                        .removeAttr('data-href');
                                                    if(typeof result.data == 'string' && isRetUrl && funcName != '微信公众号') {
                                                        $('a[data-func-name="' + funcName + '"]')
                                                            .attr('href', result.data);
                                                    } else {
                                                        $('a[data-func-name="' + funcName + '"]').each(function () {
                                                            $(this).attr('href', $(this).data('href'));
                                                        });
                                                    }
                                                } else {
                                                    permission[funcName] = false;
                                                    $('a[data-func-name="' + funcName + '"]')
                                                        .attr('href', 'javascript:void(0)')
                                                        .removeAttr('data-href');
                                                }
                                            }
                                        });
                                    }
                                    });
                                });
                            </script>
                        </div>
                    </div>
                </li>
            </ul>

        </div>
    </div>
</div>
<!-- 公共头部 end -->
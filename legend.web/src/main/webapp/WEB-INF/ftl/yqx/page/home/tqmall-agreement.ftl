<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>淘汽云修</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
    <div class="header">
        <div class="home-topnav topnav">
            <a href="${BASE_PATH}/portal" class="header-logo">
                <img src="${BASE_PATH}/static/img/common/header/logo_1.png">
            </a>
            <img src="${BASE_PATH}/static/img/common/header/home-title.png">
            <input type="hidden" name="isAdmin" value="${SESSION_USER_IS_ADMIN}"/>
            <ul class="topmenu">

                <li class="li-user-wrapper">
                    <div class="nav-user-box">
                        <img src="${BASE_PATH}/static/img/common/header/home-user-icon.png"><div class="user-account font-arial">
                    ${SESSION_USER_NAME}</div>
                    </div>
                </li>
            </ul>

        </div>
    </div>
</div>
<!-- 公共头部 end -->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/home/tqmall-agreement.css?ef6a8529a3274c4836723a0d8b4f5014">
<div class="yqx-wrapper">

    <#if shopStatus == 4>
        <#include "yqx/tpl/home/try-agreement-tpl.ftl">
    <#elseif shopStatus = 6>
        <#include "yqx/tpl/home/experience-agreement-tpl.ftl">
    <#else>
        <#if shopLevel = 10>
            <#include "yqx/tpl/home/base-tpl.ftl">
        <#elseif shopLevel = 11>
            <#include "yqx/tpl/home/base-tpl.ftl">
        <#elseif shopLevel = 12>
            <#include "yqx/tpl/home/base-tpl.ftl">
        <#else>
        <#include "yqx/tpl/home/agreement-tpl.ftl">
        </#if>
    </#if>
    <div class="center">
        <button class="agreement-btn js-tqmall-agreement">同意协议并进入系统（<i class="time">10S</i>）</button>
    </div>
</div>
<div class="footer font-arial">Copyright © 2013-2016 Tqmall.com All Rights Reserved</div>
<script src="${BASE_PATH}/static/js/page/home/tqmall-agreement.js?63dd060dabd773ecf58c4116fe519c30"></script>

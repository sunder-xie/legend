<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>淘汽云修</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <link rel="shortcut icon" href="${BASE_PATH}/resources/img/common/favicon_x48.ico" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/online/style/base.css?4733722a8f450458320041f00ac46229" />
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/module/header.css?1ce22044e7c56c92a53568b2574a37fc"/>
    <script type="text/javascript" src="${BASE_PATH}/resources/online/script/libs/jquery.js?426696605a7e0ef51ee331b85cfa7150"></script>
    <#-- 右侧工具栏 start -->
    <link rel="stylesheet" href="${BASE_PATH}/static/css/module/extension-tools.css?5fafd83102aa1e2dc22f050f309d9c0c">
    <link rel="stylesheet" href="${BASE_PATH}/static/css/module/shortcut.css?9fe112bbc479ce296de20a1eacfc66aa">
    <#-- 右侧工具栏 end -->
    <script type="text/javascript" src="${BASE_PATH}/resources/online/script/libs/jquery.js?7bc76bf6ea0eeac3fad61db1da04be37"></script>
    <script type="text/javascript" src="${BASE_PATH}/resources/script/libs/jquery.form.js"></script>
    <script type="text/javascript" src="${BASE_PATH}/resources/online/script/libs/seajs/sea.js?0eba4f6d7ef30c34921228ec84c57814"></script>
${head}
    <script type="text/javascript">
        var BASE_PATH="${BASE_PATH}";
        var COOKIE_PREFIX = "legend_cookie_";
        var Components = {};
    </script>
    <script type="text/javascript" src="${BASE_PATH}/resources/online/script/libs/path.config.js?0397b8618de248acb71684539c75afe9"></script>
    <script type="text/javascript" src="${BASE_PATH}/resources/script/libs/util.js?56f17bc60165c9a5bc831af152bf8550"></script>
    <script type="text/javascript" src="${BASE_PATH}/resources/js/common/feedback.js?8e69026f6df4a97c18779640e08a5b43"></script>

    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/style/modules/tip_new.css?afed54cdf843c2f3c64e6e38a22a0579"/>
</head>
<body>
<!-- 公共头部 start -->
<link rel="stylesheet" href="${BASE_PATH}/static/css/module/head-menus.css?82f4ff637216ffad2a5b8b703468a425">
<input type="hidden" id="isTqmall" value="${SESSION_SHOP_IS_TQMALL_VERSION}">
<#if SESSION_SHOP_IS_TQMALL_VERSION == 'true'>
    <#include "yqx/layout/tqmall-header-tpl.ftl">
<#else>
    <#include "yqx/layout/yunxiu-header-tpl.ftl">
</#if>
<!-- 公共头部 end -->

<#-- 右侧工具栏 start -->
<!-- ftl/layout/ng-hedder.ftl -->
<#include  "yqx/tpl/common/extension-tools-compatible-tpl.ftl">
<#-- 右侧工具栏 end -->

<script>
    // 权限控制
    $(function () {
    seajs.use([
        'layer'
    ],function(dg){
    var isTqmall = $('#isTqmall').val();
    var permission = {};
    // 权限控制
    $(document).on('click', '.js-check-func', function (e) {
        var name = $(this).data('funcName');

        if(permission[name] === false) {
            dg.msg('您的账号没有登陆此入口权限，请联系管理员进行开通')
            e.preventDefault();
        } else if(permission[name] === 0) {
            dg.msg('请稍后再试');
            e.preventDefault();
        } else if(permission[name] == null) {
            dg.msg('请稍后再试');
            getPermission.call(this);
            e.preventDefault();
        }
    });

    $(".js-check-func").each(getPermission);

    function getPermission(e) {
        var href = $(this).data('href');
        var funcName = $(this).data('funcName');
        var data, isRetUrl = false;

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
                    if(typeof result.data == 'string' && isRetUrl && funcName != '微信公众号'){
                        $('a[data-func-name="' + funcName + '"]')
                            .attr('href', result.data);
                    } else {
                        $('a[data-func-name="' + funcName + '"]').each(function () {
                            $(this).attr('href', $(this).data('href'));
                        })
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
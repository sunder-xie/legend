<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <title>淘汽云修</title>
    <link rel="shortcut icon" href="${BASE_PATH}/resources/img/common/favicon_x48.ico" type="image/x-icon"/>
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/base.css?9a6e71972981853d8244af7aed722478"/>
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/module/header.css?1ce22044e7c56c92a53568b2574a37fc"/>
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/common_ui.css"/>
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/common_ui_sample.css"/>
    <link rel="stylesheet" type="text/css"
          href="${BASE_PATH}/resources/style/modules/chosen.css?30ab5c0b469dabe167389465b133233f"/>
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/pagination.css"/>
    <link rel="stylesheet" type="text/css"
          href="${BASE_PATH}/resources/style/modules/tip_new.css?afed54cdf843c2f3c64e6e38a22a0579"/>
<#-- 右侧工具栏 start -->
    <link rel="stylesheet" href="${BASE_PATH}/static/css/module/extension-tools.css?5fafd83102aa1e2dc22f050f309d9c0c">
    <link rel="stylesheet" href="${BASE_PATH}/static/css/module/shortcut.css?9fe112bbc479ce296de20a1eacfc66aa">
<#-- 右侧工具栏 end -->

    <script type="text/javascript"
            src="${BASE_PATH}/resources/js/common/legend_common.js?5c2ee7d21ab23dfa1a36c733be4eccd3"></script>

    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.js"></script>
    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.form.js"></script>
    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.chosen.js"></script>
    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.json.min.js"></script>
    <script type="text/javascript"
            src="${BASE_PATH}/resources/js/lib/layer/layer.min.js?20547d53fa61b28dcf6645462ebc8907"></script>

    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/Dao.js?91adfa36bf24157d8750feee19c20436"></script>
    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/artTemplate/artTemplate.js"></script>
    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/artTemplate/artTemplate-plugin.js"></script>

    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${BASE_PATH}/resources/js/common/cookie.js?4f9d2ea24d0fa234847ecd925a614460"></script>
    <script type="text/javascript"
            src="${BASE_PATH}/resources/script/libs/util.js?56f17bc60165c9a5bc831af152bf8550"></script>
    <script type="text/javascript"
            src="${BASE_PATH}/resources/js/common/feedback.js?8e69026f6df4a97c18779640e08a5b43"></script>
</head>
<body>
<input type="hidden" id="isTqmall" value="${SESSION_SHOP_IS_TQMALL_VERSION}">

<!-- 公共头部 start -->
<link rel="stylesheet" href="${BASE_PATH}/static/css/module/head-menus.css?82f4ff637216ffad2a5b8b703468a425">
<#if SESSION_SHOP_IS_TQMALL_VERSION == 'true'>
    <#include "yqx/layout/tqmall-header-tpl.ftl">
<#else>
    <#include "yqx/layout/yunxiu-header-tpl.ftl">
</#if>
<!-- 公共头部 end -->

<#-- 右侧工具栏 start -->
<!-- ftl/layout/header.ftl -->
<#include  "yqx/tpl/common/extension-tools-compatible-tpl.ftl">
<#-- 右侧工具栏 end -->

<script>
$(function () {
    var isTqmall = $('#isTqmall').val();
    var permission = {};
    // 权限控制
    $(document).on('click', '.js-check-func', function (e) {
        var name = $(this).data('funcName');

        if(permission[name] === false) {
            layer.msg('您的账号没有登陆此入口权限，请联系管理员进行开通')
            e.preventDefault();
        } else if(permission[name] === 0) {
            layer.msg('请稍后再试');
            e.preventDefault();
        } else if(permission[name] == null) {
            layer.msg('请稍后再试');
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


                    if(typeof result.data == 'string' && isRetUrl && funcName != '微信公众号') {
                        var refer = isTqmall != true ? 'yunxiu-dialog' : 'tqmall-dailog';
                        $('a[data-func-name="' + funcName + '"]')
                                .attr('href', result.data + "?refer=" + refer);
                    } else {
                        $('a[data-func-name="' + funcName + '"]').each(function () {
                            $(this).attr('href', $(this).data('href'));
                        });
                    }
                    $('a[data-func-name="' + funcName + '"]')
                            .removeAttr('data-href');
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
</script>




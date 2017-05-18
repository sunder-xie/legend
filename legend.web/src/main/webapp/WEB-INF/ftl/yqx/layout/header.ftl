<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>淘汽云修</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <link rel="shortcut icon" href="${BASE_PATH}/static/img/common/base/favicon_x48.ico" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d" />
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/third-plugin/Font-Awesome-3.2.1/css/font-awesome.min.css" />

    <script>
        var BASE_PATH="${BASE_PATH}";
        var COOKIE_PREFIX = "legend_cookie_";
        var Components = {};
    </script>
    <script src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script src="${BASE_PATH}/static/third-plugin/seajs/sea.js"></script>
    <script src="${BASE_PATH}/static/third-plugin/path.config.js?98f845edf92898b7a23a5b384185c04c"></script>
    <script src="${BASE_PATH}/static/js/common/base/util.js?411c94ad46cf0b9ced08a7f3ce95ed23"></script>
${head}
    <link rel="stylesheet" href="${BASE_PATH}/static/third-plugin/layer/skin/layer.css" id="layui_layer_skinlayercss">

</head>
<body>
<input type="hidden" id="isTqmall" value="${SESSION_SHOP_IS_TQMALL_VERSION}">
<!-- 公共头部 start -->
<#if SESSION_SHOP_IS_TQMALL_VERSION == 'true'>
    <#include "yqx/layout/tqmall-header-tpl.ftl">
<#else>
    <#include "yqx/layout/yunxiu-header-tpl.ftl">
</#if>
<!-- 公共头部 end -->

<#-- 右侧工具栏 start -->
<#include  "yqx/tpl/common/extension-tools-tpl.ftl">
<#-- 右侧工具栏 end -->

<script>
$(function () {
    // 权限控制
    seajs.use([
        'dialog'
    ],function(dg){
        var permission = {};
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
});
</script>
<!-- 反馈 -->

<#include "layout/portal-header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/dandelion/dandelion_download.css?901af6d719309a8dfdfbc667fa8ca25b"/>
<#--<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>淘汽云修-车主版下载页</title>
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/join/common.css?1262e30bf6dcc0789fedcab8e6531e40"/>
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/dandelion/dandelion_download.css?v=1.0"/>
</head>
<body>-->
<div class="flow-container">
    <div class="flow flow-head">
        <div class="flow-head-content">
            <div class="link-buttons">
                    <a class="btn btn-iphone" href = "https://itunes.apple.com/us/app/tao-qi-yun-xiu-che-zhu-ban/id1026391647?mt=8">iPhone 版下载</a>
                    <a class="btn btn-android" href = "http://ddlwechat.oss-cn-hangzhou.aliyuncs.com/dandelion/apk/taoqiyunxiu_czapp_release.apk?t=">安卓版下载</a>
                    <a class="btn btn-code" href="javascript:void(0);"></a>
                    <div class="code-box">
                        <img class="Qr-code" src="${BASE_PATH}/resources/images/portal/dandelion/app_code.jpg" width="120" height="120"/>
                        <h2>扫描下载</h2>
                    </div>
            </div>
            <#--<div class="link-authorization">-->
                <#--<h2>官方授权下载：</h2>-->
                <#--<ul class="link-list">-->
                    <#--<li><a target="_blank" href="javascript:void(0);"><img src="${BASE_PATH}/resources/images/portal/dandelion/360.png" width="42" height="42"/></a></li>-->
                    <#--<li><a target="_blank" href="javascript:void(0);"><img src="${BASE_PATH}/resources/images/portal/dandelion/tx.png" width="42" height="42"/></a></li>-->
                    <#--<li><a target="_blank" href="javascript:void(0);"><img src="${BASE_PATH}/resources/images/portal/dandelion/baidu.png" width="42" height="42"/></a></li>-->
                    <#--<li><a target="_blank" href="javascript:void(0);"><img src="${BASE_PATH}/resources/images/portal/dandelion/91.png" width="42" height="42"/></a></li>-->
                    <#--<li><a target="_blank" href="javascript:void(0);"><img src="${BASE_PATH}/resources/images/portal/dandelion/androidmarket.png" width="42" height="42"/></a></li>-->
                    <#--<li><a target="_blank" href="javascript:void(0);"><img src="${BASE_PATH}/resources/images/portal/dandelion/wandoujia.png" width="42" height="42"/></a></li>-->
                    <#--<li><a target="_blank" href="javascript:void(0);"><img src="${BASE_PATH}/resources/images/portal/dandelion/jifeng.png" width="42" height="42"/></a></li>-->
                <#--</ul>-->
            <#--</div>-->
        </div>
    </div>
    <div class="flow flow-center"></div>
    <div class="flow flow-foot"></div>
</div>
<#include "layout/portal-footer.ftl">
<script type="text/javascript">
    $(".btn-code").on("mouseover", function() {
        $(".code-box").show();
    }).on("mouseout", function() {
        $(".code-box").hide();
    });
    //    鼠标经过时，iPhone版下载 文字改变  start
//    $(".btn-iphone").on("mouseover", function() {
//        $(this).html("点击下载");
//    }).on("mouseout", function() {
//        $(this).html("iPhone 版下载");
//    }).on("click", function() {
//        location.href = "https://itunes.apple.com/us/app/tao-qi-yun-xiu-che-zhu-ban/id1026391647?mt=8";
//    });
    //    鼠标经过时，iPhone版下载 文字改变  end

    //    鼠标经过时，Android版下载 文字改变  start
//    $(".btn-android").on("mouseover", function() {
//        $(this).html("点击下载");
//    }).on("mouseout", function() {
//        $(this).html("安卓版下载");
//    }).on("click", function() {
//        location.href = "http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/dandelion/taoqiyunxiu_czapp_release.apk?t=" + new Date().getTime();
//    });
    //    鼠标经过时，Android版下载 文字改变  end
</script>
<#include "layout/portal-header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/sell/jquery.fullPage.css?f880acd1458c05fb043bfe6d72b0651e"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/sell/version-detail.css?b46e584cd99399f82e1149d8b5ed10ea"/>
<ul id="menu">
    <li data-menuanchor="page1" class="active"><a href="#page1">门店系统</a></li>
    <li data-menuanchor="page2"><a href="#page2">手机APP</a></li>
    <li data-menuanchor="page3"><a href="#page3">微信公众号</a></li>
    <li data-menuanchor="page4"><a href="#page4">淘汽知道</a></li>
    <li data-menuanchor="page5"><a href="#page5">淘汽车险</a></li>
    <li data-menuanchor="page6"><a href="#page6">云修采购价</a></li>
</ul>
<a href="${BASE_PATH}/portal/shop/sell/select/version" class="return js-return">返回 <img src="${BASE_PATH}/static/img/page/sell/return.png"/></a>
<div id="dowebok">
    <div class="section" style="background: url('${BASE_PATH}/static/img/page/sell/detail1.jpg');background-size:cover;"></div>
    <div class="section" style="background: url('${BASE_PATH}/static/img/page/sell/detail2.jpg');background-size:cover;"></div>
    <div class="section" style="background: url('${BASE_PATH}/static/img/page/sell/detail3.jpg');background-size:cover;"></div>
    <div class="section" style="background: url('${BASE_PATH}/static/img/page/sell/detail4.jpg');background-size:cover;"></div>
    <div class="section" style="background: url('${BASE_PATH}/static/img/page/sell/detail5.jpg');background-size:cover;"></div>
    <div class="section" style="background: url('${BASE_PATH}/static/img/page/sell/detail6.jpg');background-size:cover;"></div>
</div>


<script>
    $(function(){
        $('#dowebok').fullpage({
            anchors: ['page1', 'page2', 'page3', 'page4','page5','page6'],
            menu: '#menu'
        });
    });
</script>
<script src="${BASE_PATH}/static/js/page/sell/jquery.fullPage.min.js?a59f5bcf96df06e51cd4007f1b1723a7"></script>
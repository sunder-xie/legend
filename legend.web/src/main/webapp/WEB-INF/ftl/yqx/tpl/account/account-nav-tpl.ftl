<#include "yqx/page/marketing/left-nav.ftl"/>
<#--<style>-->
    <#--.aside-nav-root {-->
        <#--cursor: pointer;-->
    <#--}-->

    <#--.wechat-root.current {-->
        <#--padding-left: 0;-->
        <#--border-left: 4px solid #8fb027;-->
        <#--background: #F2F2F2;-->
    <#--}-->

    <#--.order-left .wechat-root>a:hover::before {-->
        <#--width: 0;-->
    <#--}-->

    <#--.order-left .wechat-root>a {-->
        <#--padding-left: 0;-->
        <#--font-size: 16px;-->
        <#--font-weight: 900;-->
        <#--color:#333;-->
    <#--}-->
    <#--.order-left .wechat-root>a:hover{-->
        <#--background: none;-->
    <#--}-->
<#--</style>-->
<#--<#if SESSION_SHOP_IS_TQMALL_VERSION == 'true'>-->
    <#--<div class="order-left fl">-->
    <#--<ul class="aside-nav" data-tpl-ref="account-nav-tpl">-->
        <#--<#if moduleUrl == "customer">-->
            <#--<li class="aside-nav-root">-->
                <#--客户管理-->
            <#--</li>-->
            <#--<li class="aside-nav-list hide">-->
                <#--<dl>-->
                    <#--<dd><a class=' <#if subModule=="account-index">current</#if>' href="${BASE_PATH}/account">账户充值</a></dd>-->
                    <#--<dd><a class=' <#if subModule=="member-grant">current</#if>' href="${BASE_PATH}/account/member/grant">会员卡办理</a></dd>-->
                    <#--<dd><a class=' <#if subModule=="combo-grant">current</#if>' href="${BASE_PATH}/account/combo/grant">计次卡办理</a></dd>-->
                    <#--<dd><a class=' <#if subModule=="account-setting">current</#if>' href="${BASE_PATH}/account/setting">优惠设置</a></dd>-->
                    <#--<dd><a class=' <#if subModule=="account-flow">current</#if>' href="${BASE_PATH}/account/flow">充值记录</a></dd>-->
                <#--</dl>-->
            <#--</li>-->
        <#--</#if>-->
        <#--<#if moduleUrl=='wechat'>-->
            <#--<li class="aside-nav-root wechat-root">-->
                <#--&lt;#&ndash;<a href="${BASE_PATH}/shop/wechat">微信公众号</a>&ndash;&gt;-->
            <#--</li>-->
            <#--<li class="aside-nav-list hide wechat">-->
                <#--<dl>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-index">current</#if>' href="${BASE_PATH}/shop/wechat/wechat-index">主页</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="article-list">current</#if>' href="${BASE_PATH}/shop/wechat/article-list">文章管理</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-activity">current</#if>' href="${BASE_PATH}/shop/wechat/activity-list">活动管理</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-lottery">current</#if>' href="${BASE_PATH}/shop/wechat/activity-lottery-management">抽奖活动</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-coupon">current</#if>' href="${BASE_PATH}/shop/wechat/wechat-coupon">关注送券</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-favormall">current</#if>' href="${BASE_PATH}/shop/wechat/favormall-list">卡券商城</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-rescue-assessment">current</#if>' href="${BASE_PATH}/shop/wechat/rescue-assessment-list">救援定损</a>-->
                    <#--</dd>-->
                    <#--<#if SESSION_USER_IS_ADMIN == 1>-->
                        <#--<dd>-->
                            <#--<a class=' <#if subModule?? && subModule=="wechat-appservice">current</#if>'-->
                               <#--href="${BASE_PATH}/shop/wechat/appservice/list">发布服务</a>-->
                        <#--</dd>-->
                    <#--<#else>-->
                        <#--<#if SESSION_USER_ROLE_FUNC?exists>-->
                            <#--<#list SESSION_USER_ROLE_FUNC as temp>-->
                                <#--<#if temp.name =="设置">-->
                                    <#--<dd>-->
                                        <#--<a class=' <#if subModule?? && subModule=="wechat-appservice">current</#if>'-->
                                           <#--href="${BASE_PATH}/shop/wechat/appservice/list">发布服务</a>-->
                                    <#--</dd>-->
                                    <#--<#break>-->
                                <#--</#if>-->
                            <#--</#list>-->
                        <#--</#if>-->
                    <#--</#if>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-menu">current</#if>' href="${BASE_PATH}/shop/wechat/wechat-menu">微信菜单配置</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="msg-list">current</#if>' href="${BASE_PATH}/shop/wechat/msg-list">自动回复</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wifi-manage">current</#if>' href="${BASE_PATH}/shop/wechat/wifi-manage">设置WIFI</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="qrcode-list">current</#if>' href="${BASE_PATH}/shop/wechat/qrcode-list">二维码</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-info">current</#if>' href="${BASE_PATH}/shop/wechat/wechat-info">资料维护</a>-->
                    <#--</dd>-->
                <#--</dl>-->
            <#--</li>-->
        <#--</#if>-->
    <#--</ul>-->
    <#--<#else>-->
    <#--<div class="order-left fl">-->
        <#--<ul class="aside-nav" data-tpl-ref="account-nav-tpl">-->
            <#--<li class="aside-nav-root">-->
                <#--客户分析-->
            <#--</li>-->
            <#--<li class="aside-nav-list hide">-->
                <#--<dl>-->
                    <#--<dd><a href="/legend/marketing/ng/analysis">客户分析汇总</a></dd>-->
                    <#--<dd><a href="/legend/marketing/ng/analysis/lost?tag=high">客户流失分析</a></dd>-->
                    <#--<dd><a href="/legend/marketing/ng/analysis/type?tag=older">客户类型分析</a></dd>-->
                    <#--<dd><a href="/legend/marketing/ng/analysis/level?tag=high">客户级别分析</a></dd>-->
                <#--</dl>-->
            <#--</li>-->
            <#--<li class="aside-nav-root">-->
                <#--客情维护-->
            <#--</li>-->
            <#--<li class="aside-nav-list hide">-->
                <#--<dl>-->
                    <#--<dd><a href="/legend/marketing/ng/maintain">客情维护汇总</a></dd>-->
                    <#--<dd><a href="/legend/marketing/ng/maintain/center">提醒中心</a></dd>-->
                    <#--<dd><a href="/legend/marketing/ng/maintain/effect">效果汇总</a></dd>-->
                    <#--<dd><a href="/legend/marketing/ng/maintain/detail">客情明细</a></dd>-->
                    <#--<dd><a href="/legend/marketing/ng/maintain/type_analysis">客情类型分析</a></dd>-->

                <#--</dl>-->
            <#--</li>-->
            <#--<li class="aside-nav-root">-->
                <#--营销中心-->
            <#--</li>-->
            <#--<li class="aside-nav-list hide">-->
                <#--<dl>-->
                    <#--<dd><a href="/legend/marketing/ng/center/accurate">精准营销</a></dd>-->
                    <#--<dd><a href="/legend/shop/cz_app/activity/act_list">办单店活动</a></dd>-->
                    <#--<dd><a href="/legend/shop/cz_app/notice/notice_list">发门店公告</a></dd>-->
                    <#--<dd><a href="/legend/shop/activity" class="<#if subModule?? && subModule=="center-activity">current</#if>">朋友圈推广</a></dd>-->
                    <#--<dd class="wechat-promotion"><a class="<#if subModule?? && subModule=="wechat-spread">current</#if>" href="/legend/shop/wechat/activity-list">微信推广</a></dd>-->
                    <#--<dd><a href="/legend/shop/shop_service_info/giftInfo" class="<#if subModule?? && subModule=="giftInfo">current</#if>">礼品发放</a></dd>-->
                    <#--<dd><a href="/legend/marketing/ng/center/sms">短信充值</a></dd>-->
                <#--</dl>-->
            <#--</li>-->
            <#--<li class="aside-nav-root">-->
                <#--客户管理-->
            <#--</li>-->
            <#--<li class="aside-nav-list hide">-->
                <#--<dl>-->
                    <#--<dd><a class=' <#if subModule=="account-index">current</#if>' href="${BASE_PATH}/account">账户充值</a></dd>-->
                    <#--<dd><a class=' <#if subModule=="member-grant">current</#if>' href="${BASE_PATH}/account/member/grant">会员卡办理</a></dd>-->
                    <#--<dd><a class=' <#if subModule=="combo-grant">current</#if>' href="${BASE_PATH}/account/combo/grant">计次卡办理</a></dd>-->
                    <#--<dd><a class=' <#if subModule=="account-setting">current</#if>' href="${BASE_PATH}/account/setting">优惠设置</a></dd>-->
                    <#--<dd><a class=' <#if subModule=="account-flow">current</#if>' href="${BASE_PATH}/account/flow">充值记录</a></dd>-->
                <#--</dl>-->
            <#--</li>-->

            <#--<li class="aside-nav-root wechat-root">-->
            <#--&lt;#&ndash;<a href="${BASE_PATH}/shop/wechat">微信公众号</a>&ndash;&gt;-->
            <#--</li>-->
            <#--<li class="aside-nav-list hide wechat">-->
                <#--<dl>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-index">current</#if>' href="${BASE_PATH}/shop/wechat/wechat-index">主页</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="article-list">current</#if>' href="${BASE_PATH}/shop/wechat/article-list">文章管理</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-activity">current</#if>' href="${BASE_PATH}/shop/wechat/activity-list">活动管理</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-lottery">current</#if>' href="${BASE_PATH}/shop/wechat/activity-lottery-management">抽奖活动</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-coupon">current</#if>' href="${BASE_PATH}/shop/wechat/wechat-coupon">关注送券</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-favormall">current</#if>' href="${BASE_PATH}/shop/wechat/favormall-list">卡券商城</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-rescue-assessment">current</#if>' href="${BASE_PATH}/shop/wechat/rescue-assessment-list">救援定损</a>-->
                    <#--</dd>-->
                    <#--<#if SESSION_USER_IS_ADMIN == 1>-->
                        <#--<dd>-->
                            <#--<a class=' <#if subModule?? && subModule=="wechat-appservice">current</#if>'-->
                               <#--href="${BASE_PATH}/shop/wechat/appservice/list">发布服务</a>-->
                        <#--</dd>-->
                    <#--<#else>-->
                        <#--<#if SESSION_USER_ROLE_FUNC?exists>-->
                            <#--<#list SESSION_USER_ROLE_FUNC as temp>-->
                                <#--<#if temp.name =="设置">-->
                                    <#--<dd>-->
                                        <#--<a class=' <#if subModule?? && subModule=="wechat-appservice">current</#if>'-->
                                           <#--href="${BASE_PATH}/shop/wechat/appservice/list">发布服务</a>-->
                                    <#--</dd>-->
                                    <#--<#break>-->
                                <#--</#if>-->
                            <#--</#list>-->
                        <#--</#if>-->
                    <#--</#if>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-menu">current</#if>' href="${BASE_PATH}/shop/wechat/wechat-menu">微信菜单配置</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="msg-list">current</#if>' href="${BASE_PATH}/shop/wechat/msg-list">自动回复</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wifi-manage">current</#if>' href="${BASE_PATH}/shop/wechat/wifi-manage">设置WIFI</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="qrcode-list">current</#if>' href="${BASE_PATH}/shop/wechat/qrcode-list">二维码</a>-->
                    <#--</dd>-->
                    <#--<dd>-->
                        <#--<a class=' <#if subModule?? && subModule=="wechat-info">current</#if>' href="${BASE_PATH}/shop/wechat/wechat-info">资料维护</a>-->
                    <#--</dd>-->
                <#--</dl>-->
            <#--</li>-->
        <#--</ul>-->
<#--</#if>-->
    <#--<#include "yqx/tpl/common/another-download.ftl">-->
    <#--</div>-->
<#--&lt;#&ndash;微信公众号判断&ndash;&gt;-->
<#--<script>-->
    <#--jQuery(function() {-->
        <#--var url = '/shop/wechat/op/qry-wechat-info';-->
        <#--if(BASE_PATH) {-->
            <#--url = BASE_PATH + url;-->
        <#--} else {-->
            <#--url = '/legend' + url;-->
        <#--}-->
        <#--$.ajax({-->
            <#--url: url,-->
            <#--success: function(json) {-->
                <#--var data = json.data;-->
                <#--if(!json || !json.success || !data || data.shopStatus != 3) {-->
                    <#--$('.wechat').remove();-->
                    <#--$('.wechat-root').append('<a href="${BASE_PATH}/shop/wechat">微信公众号</a>');-->
                <#--} else{-->
                    <#--$('.wechat-promotion').remove();-->
                    <#--$('.wechat-root').append('微信公众号');-->
                <#--}-->

            <#--},-->
            <#--error: function() {-->
                <#--$('.wechat').remove();-->
                <#--$('.wechat-promotion').remove();-->
            <#--}-->
        <#--});-->
    <#--});-->
<#--</script>-->
<#--<script>-->
    <#--jQuery(function($) {-->
        <#--// 展开当前-->
        <#--var current = $('.aside-nav .current');-->

        <#--if(current.length) {-->
            <#--current.parents('.aside-nav-list:hidden').toggle(500);-->
        <#--}-->

        <#--// 点击展开隐藏列表-->
        <#--$(document)-->
                <#--.on('click', '.aside-nav-root', function () {-->
                    <#--$(this).next('.aside-nav-list').eq(0).toggle(500);-->
                <#--})-->
                <#--.on('click', '.js-link-download', function() {-->
                    <#--// 下载ppt-->
                    <#--window.location.href = 'http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/images/201603/' +-->
                            <#--'source_img/original_p_145879025648953042.ppt';-->
                <#--});-->
    <#--});-->
<#--</script>-->

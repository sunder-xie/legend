<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/on-off-common.css?6fb5ea71934b5a899146d7872c8b9337"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">消息推送设置</h3>
        </div>
        <div class="content clearfix">
            <div class="title">
                <i></i>管理给车主推送消息的方式
            </div>
            <div class="left-box fl">
                <div class="btn-group clearfix">
                    <img src="${BASE_PATH}/static/img/page/setting/mail.png" class="fl"/>
                    <p class="fl">短信消息推送</p>
                    <#--1是关闭-->
                    <div class="on-off fl <#if smsConf == 1>red-color</#if>">
                        <span class="fl">已开启</span>
                        <span class="fl">未开启</span>
                        <a href="javascript:;" class="on-off-btn js-on-btn <#if smsConf == 1>off-btn</#if>" data-confkey="sms_conf"></a>
                    </div>
                </div>
                <div class="btn-group clearfix">
                    <img src="${BASE_PATH}/static/img/page/setting/weixin.png" class="fl"/>
                    <p class="fl">微信消息推送</p>
                    <div class="on-off fl <#if wechatConf == 1>red-color</#if>">
                        <span class="fl">已开启</span>
                        <span class="fl">未开启</span>
                        <a href="javascript:;" class="on-off-btn js-on-btn <#if wechatConf == 1>off-btn</#if>" data-confkey="wechat_conf"></a>
                    </div>
                </div>
            </div>
            <div class="right-box fr">
                <p>开启消息推送后</p>
                <p>在车主预约，工单结算等状态后，车主会接收到消息</p>
            </div>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/setting/function/msg-push.js?ff2aa96fa8018b1ba8364c8885d6bd6f"></script>
<#include "yqx/layout/footer.ftl">
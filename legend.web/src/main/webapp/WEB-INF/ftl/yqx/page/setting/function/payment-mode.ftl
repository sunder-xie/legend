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
            <h3 class="headline">支付方式</h3>
        </div>
        <div class="content clearfix">
            <div class="title">
                <i></i>管理车主支付方式
            </div>
            <div class="left-box fl">
            <#list paymentConfigs as paymentConfig>
                <#if paymentConfig.confKey == 'wechat'>
                    <div class="btn-group clearfix">
                        <img src="${BASE_PATH}/static/img/page/setting/weixin.png" class="fl"/>
                        <p class="fl">微信支付</p>
                        <div class="on-off fl <#if paymentConfig.confValue !='open'>red-color</#if>">
                            <span class="fl">已开启</span>
                            <span class="fl">未开启</span>
                            <a href="javascript:;" class="on-off-btn js-on-btn <#if paymentConfig.confValue !='open'>off-btn</#if>"
                               data-confkey="${paymentConfig.confKey}"
                               data-applytype="0"></a>
                        </div>
                    </div>
                </#if>
            </#list>
            </div>
            <div class="right-box p-top fr">
                <p>开启支付后</p>
                <p>在车主预约活动服务时，可以进行支付</p>
            </div>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/setting/function/payment-mode.js?6fb364b9603f2ae041aa4af7a9630b66"></script>
<#include "yqx/layout/footer.ftl">
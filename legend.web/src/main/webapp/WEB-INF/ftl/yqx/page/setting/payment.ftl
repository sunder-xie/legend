<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/paymentType/pay-type.css?c23965f01cfa00ba23b9e7ba14de41a3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/paymentType/guest-conf.css?623e19939f906098603228ec6cf61e04"/>
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
            <h3 class="headline">结算方式设置</h3>
        </div>
        <div class="content clearfix">
            <div class="title">
                <i></i>管理门店中的结算方式
            </div>
            <div class="form-box fl">
                <div class="pay-type-box">
                <#list paymentList as payment>
                    <div class="col-6">
                        <div class="form-label">
                        ${payment.name}
                        </div>
                        <#if payment.shopId gt 0>
                            <div class="on-off<#if payment.showStatus==1> background-green<#else> background-red</#if>">
                                <span class="fl">已开启</span>
                                <span class="fl">未开启</span>
                                <a href="javascript:;" class="on-off-btn js-on-btn<#if payment.showStatus==1><#else> off-btn</#if>" data-id="${payment.id}"></a>
                            </div>
                        </#if>
                    </div>
                </#list>
                </div>
                <p class="mobile">如您需添加新的结算方式请致电: <img src="${BASE_PATH}/static/img/page/setting/phone.png"> <span>400－9937288-2-3</span> </p>
            </div>
            <div class="pay-picture fr">
                <p>将用于门店收款时的结算方式</p>
                <div class="pay-type-picture">
                    <img src="${BASE_PATH}/static/img/page/setting/payment.png"/>
                </div>
            </div>
        </div>

        <!-- 卡券优惠开关 -->
        <div class="content clearfix guest-conf-content">
            <div class="title">
                <i></i>管理卡券优惠使用
            </div>
            <div class="clearfix guest-conf">
                <div class="left-box fl">
                    <span>允许使用其他客户的卡券优惠及结算</span>
                </div>
                <div class="fl">
                    <div class="on-off fl <#if !shopConfValue || shopConfValue != 'yes'>background-red</#if>">
                        <span class="fl">已开启</span>
                        <span class="fl">未开启</span>
                        <a href="javascript:;" class="on-off-btn js-change-guest <#if !shopConfValue || shopConfValue != 'yes'>off-btn</#if>"></a>
                    </div>
                </div>
            </div>
            <div class="coupon-desc">
                <span class="fl">使用说明：</span>
                <div class="overflow-hidden">
                    <p>开启使用后，任意车辆工单结算时，可以使用未绑定客户的会员卡、优惠券、计次卡进行工单优惠和结算支付。</p>
                    <p>使用其他客户卡券时，需持卡人手机号码短信验证。</p>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${BASE_PATH}/static/js/page/setting/payment.js?0277feafd2a4bfb2df3075b5ac357080"></script>
<#include "yqx/layout/footer.ftl">


<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/settle/new/settle-index.css?ad5c0f641436b025c89413aa36111727"/>
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="settle-right fl">
        <p class="big-title">对账</p>

        <div class="settle-item-box">
            <div class="settle-item-inner">
                <div class="item-icon cash-icon"></div>
                <div class="detail-box">
                    <p>现金</p>
                    <a href='/legend/insurance/settle/view/cash'>查看详情</a>
                </div>
                <div class="cash-detail">
                    <div class="part">
                        <p>已结算: <span class="color-orange">
                           ${shopDetail.settledCashAmount?default(0.00)?string("0.00")}
                        </span></p>

                        <p>未结算: <span class="color-orange">${shopDetail.payableCashAmount?default(0.00)?string("0.00")}</span></p>
                    </div>
                </div>
            </div>
        </div>
    <#if SESSION_ANXIN_INSURANCE_MODEL == 2 || SESSION_ANXIN_INSURANCE_MODEL == 0>
        <div class="settle-item-box">
            <div class="settle-item-inner">
                <div class="item-icon service-pack-icon"></div>
                <div class="detail-box">
                    <p>服务包</p>
                    <a href='/legend/insurance/settle/view/package'>查看详情</a>
                </div>
                <div class="service-pack-detail">
                    <div class="part">
                        <p>待生效: <span class="color-black">${shopDetail.waitEffectPackageNum!'0'}</span></p>
                        <p>待发货: <span class="color-black">${shopDetail.waitPackageNum!'0'}</span></p>

                    </div>
                    <div class="part">
                        <p>配送中: <span class="color-black">${shopDetail.sendPackageNum!'0'}</span></p>
                        <p>已签收: <span class="color-black">${shopDetail.receivePackageNum!'0'}</span></p>
                    </div>
                </div>
            </div>
        </div>
    </#if>
        <#--<div class="settle-item-box">-->
            <#--<div class="settle-item-inner">-->
                <#--<div class="item-icon bounty-icon"></div>-->
                <#--<div class="detail-box">-->
                    <#--<p>奖励金</p>-->
                    <#--<a href='/legend/insurance/settle/view/bonus'>查看详情</a>-->
                <#--</div>-->
                <#--<div class="bounty-detail">-->
                    <#--<div class="part">-->
                        <#--<p>已结算: <span class="color-orange">${shopDetail.settledBonusAmount?default(0.00)?string("0.00")}</span></p>-->

                        <#--<p>未结算: <span class="color-orange">${shopDetail.payableBonusAmount?default(0.00)?string("0.00")}</span></p>-->
                    <#--</div>-->
                    <#--<div class="part">-->
                        <#--<p>支出: <span class="color-orange">-->
                            <#--<#if expendBonusAmount == "-1">-->
                                <#--暂无-->
                             <#--<#else >-->
                             <#--${expendBonusAmount?default(0.00)?string("0.00")}-->
                            <#--</#if>-->
                        <#--</span></p>-->

                        <#--<p>返还: <span class="color-orange">-->
                            <#--<#if returnBonusAmount == "-1">-->
                                <#--暂无-->
                            <#--<#else >-->
                            <#--${returnBonusAmount?default(0.00)?string("0.00")}-->
                            <#--</#if>-->
                            <#--</span></p>-->
                    <#--</div>-->
                    <#--<div class="part">-->
                        <#--<p>剩余可用: <span class="color-orange">-->
                            <#--<#if leftBonusAmount == "-1">-->
                                <#--暂无-->
                            <#--<#else >-->
                            <#--${leftBonusAmount?default(0.00)?string("0.00")}-->
                            <#--</#if>-->
                        <#--</span></p>-->
                    <#--</div>-->
                <#--</div>-->
            <#--</div>-->
        <#--</div>-->
    </div>
</div>
<#include "yqx/layout/footer.ftl">
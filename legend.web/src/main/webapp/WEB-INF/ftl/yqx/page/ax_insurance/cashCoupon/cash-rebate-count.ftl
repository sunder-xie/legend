<#--返利计算页面-->
<#include "yqx/layout/header.ftl">
<link href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d" type="text/css" rel="stylesheet">
<link href="${BASE_PATH}/static/css/page/ax_insurance/cashCoupon/cash-rebate-count.css?b61a4fb537902ca8e68e60512fb2386c" type="text/css" rel="stylesheet">
<div class="wrapper clearfix">
    <div class="content clearfix">
    <#include "yqx/page/ax_insurance/left-nav.ftl">
        <div class="right">
            <div class="contain-wrap">
                <p class="big-title">返利试算
                    <a class="goBack" href="${BASE_PATH}/insurance/anxin/cashCoupon/cashCouponList"><span> 返回 ></span></a>
                </p>
                <div class="rebate-li">
                    <span class="rebate-li-title">试算公式</span>
                    <input type="text" value="交强险保费*${cashCouponSettleRule.forcibleRebateRatio*100}%+(商业险保费-现金券面值)*${cashCouponSettleRule.commercialRebateRatio*100}%+现金券面值" class="selectBox" readonly>
                    <i class="s_up"></i>
                    <ul class="select-ul dis-none">
                        <li>交强险保费*${cashCouponSettleRule.forcibleRebateRatio*100}%+(商业险保费-现金券面值)*${cashCouponSettleRule.commercialRebateRatio*100}%+现金券面值</li>

                    </ul>
                </div>
                <div class="rebate-li">
                    <span class="rebate-li-title">现金券</span>
                    <input type="text" value="面值&yen;${cashCouponSettleRule.faceAmount}" class="selectBox face_value" data-val="${cashCouponSettleRule.faceAmount}" readonly>
                    <i class="s_up"></i>
                    <ul class="select-ul dis-none">
                        <li>面值&yen;${cashCouponSettleRule.faceAmount}</li>
                    </ul>
                </div>
                <input type="hidden" id ="forcibleRebateRatio" value="${cashCouponSettleRule.forcibleRebateRatio*100}">
                <input type="hidden" id ="commercialRebateRatio" value="${cashCouponSettleRule.commercialRebateRatio*100}">

                <div class="rebate-li">
                    <span class="rebate-li-title">试算保单</span>
                    <div class="checkbox-li">
                        <input type="checkbox" class="rebate-checkbox" checked value="1"> 自定义
                    </div>
                    <div class="checkbox-li">
                        <input type="checkbox" class="rebate-checkbox" value="2"> 今日可用劵保单
                    </div>
                    <div id="customize" class="rebate-li-div">
                        <span>交强险保费</span>
                        <input type="text" placeholder="请输入" class="fee-box" id="compulsoryFee">
                        <span>商业险险保费</span>
                        <input type="text" placeholder="请输入" class="fee-box" id="businessFee">
                    </div>
                    <div id="today" class="rebate-li-div dis-none">
                        <input type="text" value="请选择" class="selectBox rebate-selectBox"
                               readonly data-compulsory="" data-business="">
                        <i class="s_up"></i>
                        <ul class="select-ul dis-none">
                            <#if list>
                                <#list list as li>
                                    <li data-OrderSn="${li.insuranceOrderSn}" data-compulsory="${li.forcibleInsuredFee}" data-business="${li.commercialInsuredFee}"><#if li.vehicleSn>${li.vehicleSn}<#else>未填车牌</#if>
                                        交强险保费￥<#if li.forcibleInsuredFee>${li.forcibleInsuredFee}<#else>0</#if> 商业险保费￥<#if li.commercialInsuredFee>${li.commercialInsuredFee}<#else>0</#if></li>
                                </#list>
                             <#else >
                                <div class="no-data">暂无数据</div>
                            </#if>

                        </ul>
                    </div>
                    <button class="count-button">开始试算</button>
                    <p class="count-result-title">试算结果:</p>
                    <div class="count-result-box">
                         暂无
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/ax_insurance/cashCoupon/cash-rebate-count.js?c85596de5c60068c9b5f9fd274784d14"></script>
<#include "yqx/layout/footer.ftl">
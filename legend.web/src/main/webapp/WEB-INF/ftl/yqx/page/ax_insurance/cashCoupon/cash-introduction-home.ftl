<#--获得现金劵介绍页面-->
<#include "yqx/layout/header.ftl">
<link href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d" type="text/css"
      rel="stylesheet">
<link href="${BASE_PATH}/static/css/page/ax_insurance/cashCoupon/cash-introduction-home.css?35457d860eee3fab2da2546413b5364a" type="text/css"
      rel="stylesheet">
<div class="wrapper clearfix">
    <div class="content clearfix">
    <#include "yqx/page/ax_insurance/left-nav.ftl">
        <div class="right">
            <div class="contain-wrap">
                <p class="big-title">获得现金劵
                    <a href="${BASE_PATH}/insurance/anxin/cashCoupon/cashCouponIntroduction">现金券规则</a>
                </p>
                <div class="introduction-box">
                    <div class="buy-logo"></div>
                    <p class="buy-word">买的越多送的越多</p>
                    <div class="cash-logo"></div>
                    <div class="way-box">
                        <div class="how-get float-le"></div>
                        <div class="float-le">
                            <p class="how-get-p">在淘汽档口网站或APP挑选任意配件进行下单</p>
                            <p class="how-get-p">采购订单付款完成</p>
                            <p class="how-get-p">系统自动赠送现金劵</p>
                        </div>
                    </div>
                    <button class="int-button go_buy_button"><a href="${BASE_PATH}/home/avoid/tqmall/index" target="_blank">去采货</a></button>
                    <button class="int-button"><a href="${BASE_PATH}/insurance/anxin/cashCoupon/cashCouponList">查询现金券</a></button>
                </div>
            </div>
        </div>
    </div>
</div>
<#include "yqx/layout/footer.ftl">
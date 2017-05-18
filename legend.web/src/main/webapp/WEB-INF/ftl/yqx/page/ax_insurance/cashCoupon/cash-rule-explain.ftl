<#--规则说明页面-->
<#include "yqx/layout/header.ftl">
<link href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d" type="text/css"
      rel="stylesheet">
<link href="${BASE_PATH}/static/css/page/ax_insurance/cashCoupon/cash-rule-explain.css?1d34697b88bfd9eecbaeebaaad3d8b5a" type="text/css"
      rel="stylesheet">
<div class="wrapper clearfix">
    <div class="content clearfix">
    <#include "yqx/page/ax_insurance/left-nav.ftl">
        <div class="right">
            <div class="contain-wrap">
                <p class="big-title">现金劵规则说明
                    <a class="goBack"><span> 返回 ></span></a>
                </p>
                <div class="rule-box">
                    <div class="paragraph-box">
                        <p class="paragraph-title"> 如何获得现金劵？</p>
                        <div>
                        ${HowGetCashCoupon}
                        </div>
                    </div>
                    <div class="paragraph-box">
                        <p class="paragraph-title"> 保险现金券有什么用？</p>
                        <div>
                        ${WhatUseCashCoupon}
                        </div>
                    </div>
                    <div class="paragraph-box">
                        <p class="paragraph-title"> 什么样的保单可以使用现金券？</p>
                        <div>
                        ${KindInsuranceFormCanUse}
                        </div>
                    </div>
                    <div class="paragraph-box">
                        <p class="paragraph-title"> 除了现金券，门店还能得到什么？</p>
                        <div>
                        ${CahCouponElseAdvantage}
                        </div>
                    </div>
                    <div class="paragraph-box">
                        <p class="paragraph-title"> 券的有效期是多久？</p>
                        <div>
                        ${CashCouponValidityDate}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="application/javascript">
    $(function () {
        $(document).on('click','.goBack',function () {
            history.go(-1);
        })
    })
</script>
<#include "yqx/layout/footer.ftl">
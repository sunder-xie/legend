<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/smart/common/smart.css?84206c0cf2345ab77f31da9c1687703b"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/third-plugin/select2/select2_metro.css"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/ax_insurance/settle/new/settle-common.css?131f6e62ee99a009392707f208c82487"/>
<!-- 样式引入区 end-->
<body>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">


    <div class="order-box fl" style="padding: 0">
        <div class="order-box-head ">
            <span class="right-title">结算规则</span>
            <a href="${BASE_PATH}/insurance/settle/view/index" class="right return">返回对账
                <div class="right_arrows"></div>
            </a>
        </div>
    </div>

    <div class="tabbable-custom order-box fl">
        <ul class="nav nav-tabs ">
            <li class="active"><a class="tab_page_a" href="JavaScript:void(0)" data-type="cash_tab">现金</a></li>
            <li class=""><a class="tab_page_a" href="JavaScript:void(0)" data-type="package_tab">服务包</a></li>
            <#--<li class=""><a class="tab_page_a" href="JavaScript:void(0)" data-type="bonus_tab">奖励金</a></li>-->
        </ul>
        <div class="tab-content">
            <div class="tab-pane tab_bonus active" id="cash_tab">
                <script type="text/html" id="cashDataTpl">
                    ${cashRule}
                </script>
                <#--<div class="bord_bottom">-->
                <#---->
                <#--</div>-->
            </div>
            <div class="tab-pane tab_bonus " id="package_tab">
                <script type="text/html" id="packageDataTpl">
                    ${packageRule}
                </script>
                <#--<div class="bord_bottom">-->
                <#---->
                <#--</div>-->
            </div>
            <#--<div class="tab-pane tab_bonus " id="bonus_tab">-->
                <#--<div class="bord_bottom">-->
                <#--${packageRule}-->
                <#--</div>-->
            <#--</div>-->

        </div>
    </div>
</div>
<!-- 脚本引入区 start -->

<#--<script type="text/javascript" src="${BASE_PATH}/static/js/common/base/app.js?9786bd74e565a5ce39170374cdc655dd"></script>-->
<script type="text/javascript"
        <#--src="${BASE_PATH}/static/js/page/smart/common/smart.js?4abf167c5df3e2fa7854a453b2cec085"></script>-->
<script src="${BASE_PATH}/static/js/page/ax_insurance/settle/new/settle_rule.js?43c6974e9100b4a4921a84dc3ad35d7f"></script>

<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
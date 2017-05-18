<!--支付结果页面 -->
<#include "yqx/layout/header.ftl">
<link href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d" type="text/css" rel="stylesheet">
<link href="${BASE_PATH}/static/css/page/ax_insurance/virtual/virtual-pay-result.css?3e11e9795abcae196160fdbdda139309" type="text/css" rel="stylesheet">
<div class="wrapper clearfix">
    <div class="content clearfix">
    <#include "yqx/page/ax_insurance/left-nav.ftl">
        <div class="right">
        <#include "yqx/page/ax_insurance/virtual-process-nav.ftl">
            <!--核保失败-->
            <#if info == "defeat">
            <div class="fail">
                <p>支付失败</p>
                <div class="allFail clearfix">
                    <div class="failLeft">
                        <div class="Fail_all">
                            <span class="pic"></span>
                            <span class="f_word">支付失败</span>
                            <div class="back_re js-pay" data-order-sn="${orderSn}"
                            data-pre-pay-fee="${prePayFee}">重新支付</div>
                        </div>
                    </div>
                </div>
            </div>
            </#if>
            <!--核保成功-->
            <#if info == "success">
            <div class="success">
                <p>支付成功</p>
                <div class="allSec clearfix">
                    <div class="secLeft">
                        <div class="sec_p"></div>
                        <span class="cong">支付成功</span>
                        <div class="S_word">支付金额:<span>${(payment.firstPaidAmount?string("0.00"))!}</span></div>
                       <div class="amount">
                           <p class="hei">服务包总金额：<span>${((payment.firstPaidAmount+payment.secondPaidAmount)?string("0.00"))!}</span>元</p>
                           <p class="hei">本期预付金额：<span>${(payment.firstPaidAmount?string("0.00"))!}</span>元</p>
                           <p>后续补付金额：<span>下年同一车子的商业车险保费金额与已购买服务包金额两者之间高者和已支付款项的差额。</span></p>
                           <p class="hei">商业险抵价劵面值：<span>${(payment.deductionAmount?string("0.00"))!}</span>元</p>
                       </div>
                    </div>
                    <div class="secRight">
                        <div class="QR_code">
                            <img src="${BASE_PATH}/static/img/page/ax_insurance/vin.jpg">
                        </div>
                        <div class="Q_w">提醒车主下载APP,查看服务包</div>
                    </div>
                    <div class="QR_p"></div>
                </div>
            </div>
            </#if>
        </div>
    </div>
</div>
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/ax_insurance/virtual/virtual-pay-result.js?a67d620cfe55b6b0f8ec833a403c1de5"></script>
<#include "yqx/layout/footer.ftl">
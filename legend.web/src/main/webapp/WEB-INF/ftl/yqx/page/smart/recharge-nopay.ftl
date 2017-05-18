 <#--未支付的页面-->
 <#include "yqx/layout/header.ftl">
 <link rel="stylesheet" type="text/css"
       href="${BASE_PATH}/static/css/page/smart/recharge-fail.css?d0230f637bbe76832d468f7cb9b82006"
       xmlns="http://java.sun.com/jsf/facelets"/>
 <div class="yqx-wrapper clearfix">
 <#include "yqx/page/ax_insurance/left-nav.ftl">
     <div class="right">
         <div class="process-nav-true">
         <#include "yqx/page/smart/recharge-process-nav.ftl">
         </div>
     <#--未支付页面 开始-->
         <input id="recharge-number" type="hidden" value="${recordResult.rechargeNumber}">
         <input id="recharge-fee" type="hidden" value="${recordResult.rechargeFee}">
         <div class="pay-fail-box">
             <p class="big-title">未支付</p>
             <div class="enter clearfix">
                 <div class="left-box">
                     <div class="fail-icon"></div>
                     <span class="fail-word">抱歉，您的订单未支付！</span>
                     <p>本次充值订单编号：<span class="display-color">${recordResult.rechargeNumber}</span></p>
                     <p>本次充值金额：<span class="display-color">${recordResult.rechargeFee} 元（${recordResult.rechargeNum}次）</span></p>
                     <button class="go-record">充值记录</button>
                     <button class="go-recharge">重新支付</button>
                 </div>
                 <div class="right-box">
                     <div class="fail-img"></div>
                 </div>
             </div>
         </div>
     <#--未支付页面 结束-->
     </div>
 </div>
 <script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart-ajax.js?5c4be91860d3a836366b110367358251"></script>
 <script src="${BASE_PATH}/static/js/page/smart/recharge-nopay.js?e1840d9986b34324604482db70791e74"></script>
 <#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">
<link href="${BASE_PATH}/static/css/page/ax_insurance/virtual/virtual-service.css?1ecadc72e6551b0bdac1d31892b9918b" type="text/css" rel="stylesheet">
<body>
<div class="wrapper clearfix">
    <div class="content clearfix">
    <#include "yqx/page/ax_insurance/left-nav.ftl">
        <div class="right">
        <#include "yqx/page/ax_insurance/virtual-process-nav.ftl">
            <p class="choose">买服务包送保险 <span class="back-btn fr js-back-btn">返回投保 &gt;</span></p>
            <!--服务包 start-->
            <div class="service-pack">
                <input type="hidden" value="${formId}" class="form-id">
                <input type="hidden" value="${id}" class="basic-id"/>
                <input type="hidden" value="${mobile}" class="js-mobile">
                <input type="hidden" value="${insuredFee}" class="js-insuredFee">
                <input type="hidden" value="" class="colour-type">
                <h2 class="pack-title">请选择车主喜欢的服务包</h2>
                <!--服务包list start-->
                <div id="servicePack">

                </div>
                <!--服务包list end-->

                <!--支付方式 start-->
                <div class="pay-way-box">
                    <h2 class="pay-title">请选择支付方式</h2>
                    <div class="pay-way-list clearfix">
                        <div class="pay-way">
                            <label>
                                <input type="checkbox" checked disabled>
                                <img src="${BASE_PATH}/static/img/page/ax_insurance/bankpay.png" alt="bankpay">银行卡支付
                            </label>
                        </div>
                    </div>
                </div>
                <!--支付方式 end-->

                <!--优惠券选择 start-->
                <div class="coupon-choose-box">
                    <h2 class="coupon-choose-title">请选择优惠券</h2>
                    <div class="coupon-list">

                    </div>
                </div>
                <!--优惠券选择 end-->

                <!--车主须知 start-->
                <div class="owner-notice-box">
                    <h2 class="notice-title">车主须知：</h2>
                    <ol class="notice-intro">
                        <li>1、参加【买服务包送保险】活动，车主需先预付服务包总金额的60%。</li>
                        <li>2、车主预付完服务包总金额的60%后，可以获得一张与预付金额等值的商业险抵价劵，可在后续投保时抵保费。</li>
                        <li>3、下次投保时，车主使用商业险抵价劵并支付一定的差额后才可完成投保。</li>
                        <li>4、差额的组成包含服务包总金额的40%、交强险保费金额、车船税金额、商业险保费差额。</li>
                        <li>
                            5、差额计算公式如下：
                            <div class="formula-box">
                                <span class="formula-title">场景1：</span>
                                <div class="formula-info">
                                    商业险保费≥服务包总金额<br/>
                                    应付差额＝商业险保费－服务包预付金额＋其他保费
                                </div>
                            </div>
                            <div class="formula-box">
                                <span class="formula-title">场景2：</span>
                                <div class="formula-info">
                                    商业险保费＜服务包总金额<br/>
                                    应付差额＝服务包总金额－服务包预付金额＋其他保费
                                </div>
                            </div>
                        </li>
                    </ol>
                </div>
                <!--车主须知 end-->

                <!--金额计算 start-->
                <div class="amount-summary-box">
                    <ul class="amount-detail">
                        <li>
                            <span class="amount-item">服务包总金额：<strong class="amount service-amount">0.00</strong>元</span>
                            <span class="amount-item" >本期预付金额：<strong class="amount first-paid-amount" id="prePayFee">0.00</strong>元</span>
                        </li>
                        <li>
                            <span class="amount-item">商业险抵价劵面值：<strong class="amount deduction-amount">0.00</strong>元</span>
                        </li>
                    </ul>
                    <p class="amount-payable">
                        应付金额：<strong class="amount first-paid-amount">0.00</strong>
                        <span class="amount-coupon">优惠：<strong class="amount discount-amount">0.00</strong><span class="withStore">（优惠金额由门店与车主线下结算)</span></span>
                    </p>

                    <button class="yqx-btn yqx-btn-2 js-pay">去支付</button>
                </div>
                <!--金额计算 end-->

            </div>
            <!--服务包 end-->
        </div>
    </div>
</div>
</body>
<script type="text/html" id="couponTpl">
    <%if(success && data){%>
    <%if(data.length>0){%>
    <%for(var i = 0;i < data.length;i++){%>
    <%var dataList = data[i];%>
    <li class="coupon-list-li <%if(!dataList.valid){%>disabled<%}%>" data-id="<%=dataList.id%>"
        data-state="<%=dataList.couponStatus%>" data-freezeTime="<%=dataList.gmtCouponFrozen%>"
        data-couponUseMode = '<%=dataList.couponUseMode%>' data-deductibleAmount = <%=dataList.deductibleAmount%> >
        <div class="coupon-list-box">
            <p class="clearfix">
                <span class="coupon-title <%if(!dataList.valid){%>dis-coupon<%}%>">满<%=dataList.satisfyUse%>减<%=dataList.deductibleAmount%></span>
                <span class="question-mark">?</span>
            </p>
            <p class="coupon-time">
                <%=dataList.validateTimeStr%>-<%=dataList.gmtCouponExpiredStr%>
            </p>
            <p class="coupon-describe">
                <%=dataList.applicationScope%>
            </p>
        </div>
        <div class="coupon-use-describe">
            <%==dataList.couponRuleDescription%>
        </div>
        <%if(!dataList.valid){%>
        <div class="disable-coupon">
            <img src="${BASE_PATH}/static/img/common/seals/disable.png">
        </div>
        <%}%>
    </li>
    <%}%>
    <%}else{%>
    <p class="no-coupon"><i class="sad-expr"></i><span class="no-coupon-word">车主没有可用的优惠劵</span></p>
    <%}%>
    <%}%>
</script>
<script type="text/html" id="servicePackTpl">
    <%if(json.data && json.data.length > 0){%>
    <h2 class="pack-tips">友情提示：<%=json.errorMsg%></h2>
    <%for(var i= 0; i< json.data.length;i++){%>
    <%var item = json.data[i]%>
    <div class="pack-list clearfix">
        <input type="radio" name="service-select" class="pack-check js-pack-check fl">
        <div class="pack-content fl">
            <input type="hidden" value="<%=item.id%>" class="package-id"/>
            <div class="consume-title clearfix">
                <h3 class="fl">
                    <%if (item.recommend){%>
                    <span class="recommend">推荐</span>
                    <%}%>
                    <span class="pack-name font-yahei"><%=item.packageName%></span>
                    <span class="summary"><%=item.description%></span></h3>
                <p class="fr">市场售价：<span class="mark money js-market-price"><%= item.marketPrice %></span></p>
            </div>
            <table>
                <thead>
                <tr>
                    <th class="col1">项目名称</th>
                    <th class="col2">型号</th>
                    <th class="col3">单位</th>
                    <th class="col4">市场价</th>
                    <th class="col5">次数</th>
                </tr>
                </thead>
                <tbody>
                <%for(var j=0; j < item.serviceItems.length; j++){%>
                <%var subItem =item.serviceItems[j]%>
                <% var serviceItemMaterialBOList = subItem.serviceItemMaterialBOList; %>
                <tr>
                    <%if(subItem.id == '49'){%>
                    <input type="hidden" value="49" class="color-type"/>
                    <%}%>
                    <td><%=subItem.serviceName%></td>
                    <td class="max-text js-show-tips ellipsis-1 "><%if(subItem.serviceMaterialModel){%><%=subItem.serviceMaterialModel%><%}else{%>/<%}%></td>
                    <td><%=subItem.serviceItemUnit%></td>
                    <td><%=subItem.servicePrice%></td>
                    <td><%=subItem.serviceTimes%></td>
                </tr>
                <%}%>
                <tr>
                    <td colspan="5" class="promt-note">
                   <%== setMark(item.promtNote) %>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <%}}%>
</script>

<script type="text/html" id="dialogTpl">
    <div class="yqx-dialog">
        <div class="yqx-dialog-header">
            <h1 class="yqx-dialog-headline">防冻液颜色确认</h1>
        </div>
        <div class="yqx-dialog-body">
            <div class="tip-text">您选择的服务包内容包含“更换防冻液”项目，
                请与车主确认当前车辆使用的防冻液颜色</div>
            <div class="btn-box">
                <a href="javascript:;" data-color="1">使用红色</a>
                <a href="javascript:;" data-color="2">使用绿色</a>
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-color-confim">确认</button>
        </div>
    </div>
</script>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/ax_insurance/virtual/virtual-service.js?aff27f5b95d2c3f4c3d7c6a32d98c69a"></script>
<#include "yqx/layout/footer.ftl">
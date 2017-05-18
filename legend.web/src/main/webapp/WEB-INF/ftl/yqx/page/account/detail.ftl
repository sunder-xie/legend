<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/member/detail.css?d950ce958e599e013c0da07688a922b9"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="aside-main">
        <h3 class="Z-title">
            客户管理 > <a href="${BASE_PATH}/account"> 客户查询</a> > <i>客户详情</i>
        </h3>
        <input type="hidden" id="accountId" value="${accountInfo.id}"/>
        <input type="hidden" id="customerId" value="${accountInfo.customerId}"/>

        <div class="customer-panel">
            <#--车辆详情 start-->
            <div class="detail-box box">
                <div class="show-grid">
                    <div class="col-8">
                        <div class="customer-info">
                        ${accountInfo.customer.customerName} ${accountInfo.customer.mobile}
                        </div>
                        <input type="hidden" id="customerName" value="${accountInfo.customer.customerName}">
                        <input type="hidden" id="customerMobile" value="${accountInfo.customer.mobile}">
                    </div>
                    <a href="${BASE_PATH}/account/edit?refer=customer_detail&accountId=${accountInfo.id}"
                       class="btn-edit yqx-btn yqx-btn-2 yqx-btn-micro">编辑</a>
                </div>

                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label">
                            固定电话：
                        </div><div class="form-item">
                            <div class="yqx-text">
                            ${accountInfo.customer.tel}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label">
                            联 系 人：
                        </div><div class="form-item">
                            <div class="yqx-text ellipsis-1 js-show-tips">
                            ${accountInfo.customer.contact}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-w1">
                            联系电话：
                        </div><div class="form-item form-item-w1">
                            <div class="yqx-text">
                            ${accountInfo.customer.contactMobile}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label">
                            客户生日：
                        </div><div class="form-item">
                            <div class="yqx-text">
                            ${accountInfo.customer.birthdayStr}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label">
                            身份证号：
                        </div><div class="form-item">
                            <div class="yqx-text">
                            ${accountInfo.customer.identityCard}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-w1">
                            驾驶证号：
                        </div><div class="form-item form-item-w1">
                            <div class="yqx-text">
                            ${accountInfo.customer.drivingLicense}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label">
                            客户来源：
                        </div><div class="form-item form-item-w2">
                            <div class="yqx-text ellipsis-1 js-show-tips">
                            ${accountInfo.customer.source}
                            </div>
                        </div>
                    </div>
                    <div class="col-8">
                        <div class="form-label">
                            客户地址：
                        </div><div class="form-item form-item-w3">
                            <div class="yqx-text ellipsis-1 js-show-tips">
                            ${accountInfo.customer.customerAddr}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-8">
                        <div class="form-label">
                            备　　注：
                        </div><div class="form-item form-item-w3">
                            <div class="yqx-text ellipsis-1 js-show-tips">
                            ${accountInfo.customer.remark}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-w1">
                            客户创建日期：
                        </div><div class="form-item form-item-w1">
                            <div class="yqx-text">
                            ${accountInfo.customerCreateTime}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <#--车辆详情 end-->

            <#-- 会员卡 start -->
                <#if (accountInfo.memberCards?size>0)>
                    <div class="member-box box">
                        <h2 class="box-title">会员卡</h2>
                        <div class="vip-box">
                            <#list accountInfo.memberCards as card>
                                <div class="vip-item show-grid clearfix">
                                    <div class="col-4">
                                        <div class="form-label form-label-minor">
                                        ${card.cardTypeName}：
                                        </div><div class="form-item">
                                        <strong class="strong">${card.cardNumber}</strong>
                                    </div>
                                    </div>
                                    <div class="col-4">
                                        <div class="form-label form-label-minor">
                                            余　　额：
                                        </div><div class="form-item">
                                        <strong class="money-small-font">&yen;${card.balance}</strong>
                                    </div>
                                    </div>
                                    <div class="col-4">
                                        <div class="form-label form-label-minor">
                                            积　　分：
                                        </div><div class="form-item">
                                        <strong class="strong">${card.expenseAmount}分</strong>
                                    </div>
                                    </div>
                                    <#if card.cardNumber??>
                                        <button class="yqx-btn-micro yqx-btn yqx-btn-3 upgrade-btn js-upgrade" data-card-id="${card.id}">升级</button>
                                    </#if>
                                </div>
                            </#list>
                        </div>
                    </div>
                <#-- 会员卡 end -->
                </#if>


            <#--绑定车牌 start-->
            <div class="binding-box box">
                <h2 class="box-title">拥有车辆</h2>
                <ul class="car-box">
                <#list cars?keys as carkey>
                    <li class="car-item">
                        <div class="form-label form-label-minor">
                            车主：
                        </div><div
                            class="form-item car-owners">
                            <strong class="strong">${carkey} </strong>
                        </div>
                        <#list cars[carkey] as v>
                        <div class="car-license">
                            <a href="${BASE_PATH}/shop/customer/car-detail?id=${v.id}&refer=customer">${v.license}</a>
                            <a href="javascript:void(0)" class="delete js-deletecar" data-carId="${v.id}">&times;</a>
                        </div>
                        </#list>
                    </li>
                </#list>
                </ul>
                <div>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small js-add-license">添加车辆</button>
                </div>
            </div>
            <#--绑定车牌 end-->
        </div>

        <!--tab start-->
        <div class="tab-box">
            <div class="tab">
                <div class="tab-item current-item">优惠券(${accountCouponNum})</div><div class="tab-item">计次卡(${accountComboNum})</div><div class="tab-item" id="card-info">会员特权(${memberCardNum})</div>
            </div>
            <!-- tab容器 start -->
            <div class="tabcon" id="tabCon">

            </div>
            <!-- tab容器 end -->

        </div>
        <!--tab end-->

        <!--账户使用记录 start-->
        <div class="use-record">
            <div class="record-title">账户使用记录</div>
            <!-- 表格容器 start -->
            <div class="record-table" id="recordTable">

            </div>
            <!-- 表格容器 end -->

            <!-- 分页容器 start -->
            <div class="yqx-page" id="paging"></div>
            <!-- 分页容器 end -->
        </div>
        <!--账户使用记录 end-->
    </div>
</div>

<!--优惠券 模板-->
<script type="text/html" id="DiscountTpl">
    <%if(json.data){%>
    <%for(var i=0; i < json.data.length;i++){%>
    <%var item = json.data[i]%>
    <div class="voucher">
        <div class="voucher-title">
            <div class="voucher-discount fl">
                <%if(item.couponType === 0){%>
                折扣券
                <%}else if(item.couponType === 1){%>
                现金券
                <%}else if(item.couponType === 2){%>
                通用券
                <%}%>
            </div>
            <div class="voucher-discount fl"><%=item.couponName%></div>
            <a href="javascript:;" class="show-btn fr js-show-btn"><i class="icon-angle-down"></i><span>展开</span></a>

            <div class="voucher-discount fr"><%=item.couponNum%>张</div>
            <div class="voucher-time fr">过期时间：<%=item.expireDateStr%></div>
            <%if(item.expired){%>
                <div class="voucher-time fr expried-color">已过期</div>
            <%}%>
        </div>
        <div class="voucher-content">
            <%if(item.expired){%>
                <div class="expired"></div>
            <%}%>
            <div class="show-grid">
                <div class="voucher-label">
                    抵扣金额：
                </div>
                <div class="voucher-item">
                    <%if(item.couponType === 2){%>
                    在结算时手动输入金额，直接扣减
                    <%}else{%>
                    <%=item.couponInfo.discountAmount%>
                    <%}%>
                </div>
            </div>
            <div class="show-grid">
                <div class="voucher-label">
                    使用范围：
                </div>
                <div class="voucher-item member-info">
                    <%var str = item.couponInfo.useRangeDescript, overflow = false;%>
                    <%if(str.length > 151) {%>
                    <% overflow = true;%>
                    <% str = str.slice(0, 151) + '...';%>
                    <%}%>
                    <p class="discount-info js-show-tips"
                    <%if(overflow){%><%='data-tips=' + item.couponInfo.useRangeDescript%><%}%>><%=str%></p>
                </div>
            </div>
            <div class="show-grid">
                <div class="voucher-label">
                    优惠码：
                </div>
                <div class="voucher-coupon-item voucher-coupon-overflow js-show-tips">
                    <%=item.couponCode%>
                </div>
            </div>
            <div class="show-grid">
                <div class="voucher-label">
                    生效时间：
                </div>
                <div class="voucher-item">
                    <%=item.effectiveDateStr%>
                </div>
            </div>
            <div class="show-grid">
                <div class="voucher-label">
                    过期时间：
                </div>
                <div class="voucher-item">
                    <%=item.expireDateStr%>
                </div>
            </div>
            <div class="show-grid">
                <div class="voucher-label">
                    使用规则：
                </div>
                <div class="voucher-item">
                    <%=item.couponInfo.ruleStr%>
                </div>
            </div>
            <div class="show-grid">
                <div class="voucher-label">
                    备注：
                </div>
                <div class="voucher-item remark-item">
                    <%=item.couponInfo.remark%>
                </div>
            </div>
            <div class="voucher-bot">
                <div class="voucher-label">
                    购买时间：
                </div>
                <div class="voucher-item">
                    <%=item.effectiveDateStr%>
                </div>
                <div class="voucher-label">
                    来源：
                </div>
                <div class="voucher-item">
                    <%=item.suiteName%>
                </div>
                <div class="voucher-label">
                    操作人：
                </div>
                <div class="voucher-item">
                    <%=item.operatorName%>
                </div>
            </div>
        </div>
    </div>
    <%}}%>
</script>

<!--计次卡 模板-->
<script type="text/html" id="MeterTpl">
    <%if(json.data){%>
    <%for(var i = 0; i < json.data.length; i++){%>
    <%var item = json.data[i]%>
    <div class="voucher">
        <div class="voucher-title">
            <div class="voucher-discount fl">计次卡</div>
            <div class="voucher-discount fl"><%=item.comboName%></div>
            <a href="javascript:;" class="show-btn fr js-show-btn"><i class="icon-angle-down"></i><span>展开</span></a>

            <div class="voucher-time fr">过期时间：<%=item.expireDateStr%></div>
            <%if(item.expireDateCheck){%>
            <div class="voucher-time fr expried-color">已过期</div>
            <%}%>
        </div>
        <div class="voucher-content">
            <%if(item.expireDateCheck){%>
            <div class="expired"></div>
            <%}%>
            <div class="show-grid ml120">
                <div class="voucher-label ml-120">服务项目：</div>
                <%if (item.serviceList && item.serviceList.length){%>
                <%for(var j=0; j < item.serviceList.length;j++){%>
                <%var serviceItem = item.serviceList[j]%>
                <div class="clearfix">
                    <div class="voucher-item fl w190"><%=serviceItem.serviceName%></div>
                    <div class="fl w190">
                        <div class="voucher-label w80 fl">总次数：</div>
                        <div class="voucher-item fl"><%=serviceItem.totalServiceCount%></div>
                    </div>
                    <div class="fl w190">
                        <div class="voucher-label w80 fl">剩余次数：</div>
                        <div class="voucher-item fl red"><%=serviceItem.leftServiceCount%></div>
                    </div>
                </div>
                <%}}%>
            </div>
            <div class="show-grid">
                <div class="voucher-label">
                    生效时间：
                </div>
                <div class="voucher-item">
                    <%=item.effectiveDateStr%>
                </div>
            </div>
            <div class="show-grid">
                <div class="voucher-label">
                    过期时间：
                </div>
                <div class="voucher-item">
                    <%=item.expireDateStr%>
                </div>
            </div>
            <div class="voucher-bot">
                <div class="voucher-label">
                    购买时间：
                </div>
                <div class="voucher-item">
                    <%=item.gmtCreateStr%>
                </div>
                <div class="voucher-label ml79">
                    来源：
                </div>
                <div class="voucher-item">
                    充值
                </div>
                <div class="voucher-label ml5">
                    操作人：
                </div>
                <div class="voucher-item">
                    <%=item.operatorName%>
                </div>
            </div>
        </div>
    </div>
    <%}}%>
</script>

<!--会员卡特权 模板-->
<script type="text/html" id="vipTpl">
    <%for(var i=0; i < json.data.length;i++){%>
    <%var item = json.data[i]%>
    <div class="voucher">
        <div class="voucher-title">
            <div class="voucher-discount fl">会员卡</div>
            <div class="voucher-discount fl"><%=item.typeName%></div>
            <div class="voucher-discount fl"><%=item.cardNum%></div>
            <a href="javascript:;" class="show-btn fr js-show-btn"><i class="icon-angle-down"></i><span>展开</span></a>

            <div class="voucher-time fr">过期时间：<%=item.outTimeDate%></div>
            <%if(item.expired){%>
            <div class="voucher-time fr expried-color">已过期</div>
            <%}%>
            <div class="voucher-discount fr">已使用<span><%=item.usedNum%></span>次</div>

        </div>
        <div class="voucher-content">
            <%if(item.expired){%>
            <div class="expired"></div>
            <%}%>
            <div class="show-grid">
                <div class="voucher-label member-info-label">
                    折扣：
                </div>
                <div class="voucher-item member-info">
                    <%var str = item.discountDescript, overflow = false;%>
                    <%if(str.length > 151) {%>
                    <% overflow = true;%>
                    <% str = str.slice(0, 151) + '...';%>
                    <%}%>
                    <p class="discount-info js-show-tips"
                        <%if(overflow){%><%='data-tips=' + item.discountDescript%><%}%>
                    ><%=str%></p>

                </div>
            </div>

            <div class="voucher-bot">
                <div class="voucher-label">
                    有效期：
                </div>
                <div class="voucher-item">
                    <%=item.effectivePeriodDays%>天
                </div>
                <div class="voucher-label">
                    过期时间:
                </div>
                <div class="voucher-item">
                    <%=item.outTimeDate%>
                </div>
            </div>
        </div>
    </div>
    <%}%>
</script>

<!--表格模板-->
<script type="text/html" id="tableTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th class="pl15" style="width: 80px;">日期</th>
            <th>交易类型</th>
            <th>工单编号</th>
            <th>优惠券变更</th>
            <th>项目变更</th>
            <th>余额变更</th>
            <th style="width: 100px;">收款金额</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.data && json.data.content){%>
        <%for(var i=0;i < json.data.content.length ;i++){%>
            <%var item = json.data.content[i];%>
            <tr>
                <td class="pl15"><%=item.gmtCreateStr%></td>
                <td>
                    <%if(item.tradeType == 1 && item.consumeType == 1){%>
                    <%=item.tradeTypeName%>发券
                    <%}else if(item.tradeType == 1 && item.consumeType == 2) {%>
                    <%=item.tradeTypeName%>发放撤销
                    <%}else if(item.tradeType == 2 && item.consumeType == 1) {%>
                    <%=item.tradeTypeName%>办理
                    <%}else if(item.tradeType == 2 && item.consumeType == 2) {%>
                    <%=item.tradeTypeName%>办理撤销
                    <%}else if(item.tradeType == 3 && item.consumeType == 6) {%>
                    <%=item.tradeTypeName%>退卡
                    <%}else if(item.tradeType == 3 && item.consumeType == 10) {%>
                    <%=item.tradeTypeName%>升级
                    <%}else{%>
                    <%=item.tradeTypeName%><%=item.consumeTypeName%>
                    <%}%>
                </td>
                <td>
                    <%if(item.orderSn!=null){%>
                    <a href="${BASE_PATH}/shop/order/detail?orderId=<%=item.orderId%>&refer=account-used-list"><%=item.orderSn%></a>
                    <%}else{%>
                    --
                    <%}%>
                </td>
                <td class="w117 js-show-tips"><%=item.couponExplain %></td>
                <td class="w117 js-show-tips"><%=item.serviceExplain %></td>
                <td class="ellipsis-1 js-show-tips"><%=item.cardExplain %></td>
                <td class="money-font">&yen;<%=item.payAmount %></td>
            </tr>
            <%}%>
            <%}%>
        </tbody>
    </table>
</script>

<!--新增车辆 模板-->
<script type="text/html" id="addlicenseTpl">
    <div class="yqx-dialog">
        <dg-title>添加车辆</dg-title>
        <dg-body>
            <div class="car-infor">
                <div>
                    <div class="form-label">
                        车牌
                    </div>
                    <div class="form-item">
                        <input type="text" name="license" class="yqx-input yqx-input-small js-search" autocomplete="off">
                    </div>
                </div>
                <div class="license-tip js-add-license-tip">
                    <p>提示：该车辆暂无车主，添加车辆后，该车辆车主信息将更新为该车主</p>
                </div>
            </div>
            <div class="dialog-footer">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-bundle" disabled>添加</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-unbundle">取消</button>
            </div>
        </dg-body>
    </div>
</script>


<script type="text/html" id="carLicenseTpl">
    <% if(templateData && templateData && templateData.length) { %>
    <ul class="yqx-downlist-content js-downlist-content">
        <% for(var i = 0; i < templateData.length; i++) { %>
        <% var item = templateData[i]; %>
        <li class="js-downlist-item">
            <span title="<%=item.id%>" style="width:100%"><%=item.searchValue%></span>
        </li>
        <% } %>
    </ul>
    <% } else {%>
    <div class="downlist-new-car js-new-car">
        车牌不存在,请先 <a href="javascript:void">新建车辆</a>
    </div>
    <%}%>
</script>

<script type="text/template" id="basicInfoTpl">
    <div class="personal-info">
        <div class="form-label">个人信息</div>
        <div class="form-label">车&nbsp;主:<%=customer.customerName%></div>
        <div class="form-label">车主电话：<%=customer.mobile%></div>
        <button class="edit-btn">编辑</button>
    </div>
    <div class="license">
        <div class="form-label">拥有车牌
        </div>
        <div class="form-item license-num license-num-width">
            <%for(var i = 0;i < customerCarList.length;i++) {%>
            <div class="car-license">
                <input type="text" name=""
                       class="yqx-input yqx-input-icon yqx-input-small license-num1 license-num1-width" value="浙A66666"
                       placeholder="">
                <span class="fa icon-small js-remove css-remove">&times;</span>
            </div>
            <%}%>
        </div>
        <button class="new-license license-num1">
            添加车牌
        </button>
    </div>
</script>

<#--会员卡升级-->
<script type="template/text" id="upgradeDialogTpl">
<div class="yqx-dialog upgrade-dialog">
    <div class="dialog-title">会员卡升级</div>
    <div class="dialog-content">
        <div class="member-info">
        <div class="show-grid">
            <div class="col-6">客户姓名：<%=data.customerName%></div>
            <div class="col-6">联系电话：<%=data.mobile%></div>
        </div>
        <div class="show-grid">
            <div class="col-6">　会员卡：<%=data.cardType%></div>
            <div class="col-6">会员卡号：<%=data.cardNumber%></div>
        </div>
        <div class="show-grid">
            <div class="col-6">卡内余额：<%=data.cardBalance%></div>
            <div class="col-6">累计充值：<%=data.totalChargeAmount || 0%></div>
        </div>
        </div>
        <div class="upgrade-info">
            <div class="show-grid">
                <p class="col-2" style="margin-top: 13px;">升级为</p>
                <div class="form-item col-4">
                    <input class="yqx-input js-card-type" placeholder="会员卡类型">
                    <input type="hidden" name="cardTypeId">
                    <span class="fa icon-angle-down"></span>
                </div>
            </div>
            <div class="show-grid">
                <p class="col-2">过期时间</p>
                <ul class="col-10">
                    <li>
                        <label>
                            <input type="radio" name="expireTimeType" value="0" checked>旧卡过期时间 <%=data.expireTimeStr%>
                        </label>
                    </li>
                    <li>
                        <label>
                            <input type="radio" name="expireTimeType" value="1">按升级时间及新卡有效期重新计算 <span class="upgrade-expire-time"></span>
                        </label>
                    </li>
                </ul>
            </div>
            <p class="mark">说明：会员卡升级后，会员卡卡号、卡内余额、积分及累计充值金额，均保持不变</p>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-small yqx-btn-1 js-cancel">
                    取消</button><button class="yqx-btn yqx-btn-small yqx-btn-3 js-confirm" data-card-id="<%=data.cardId%>">
                    提交</button>
            </div>
        </div>
    </div>
</div>
</script>

<script type="text/javascript" src="${BASE_PATH}/static/js/page/account/detail.js?8665831440421ad0a1b05f71c06e90e5"></script>
<#include "yqx/layout/footer.ftl">
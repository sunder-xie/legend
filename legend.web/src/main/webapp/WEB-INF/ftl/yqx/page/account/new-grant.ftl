<#include "yqx/layout/header.ftl">
<#include "yqx/tpl/common/car-licence-tpl.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/member/new-grant.css?2826ae62bbef1ce10a65c7d85bcfa57b"
      type="text/css"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/combo/new-grant.css?75489336df9e70f0e4e43ddee91d1b2e"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/new-grant.css?284377aaf21bdff3876e95e7770c7fcd"
      type="text/css"/>

<div class="yqx-wrapper clearfix">
    <div class="left-menu" style="float: left;">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="aside-main">
        <h3 class="Z-title">客户管理 > <i>卡券办理</i></h3>

        <div class="member-search-form">
            <div class="form-item">
                <input type="hidden" value="${carId}" id="carId">
                <input class="yqx-input" type="hidden" name="accountId" id="accountId">
                <input class="yqx-input search js-search" placeholder="输入车牌..." value="${license?default('')}">
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-reset">重置</button>
        </div>
        <div class="member-info" style="display: none;">
            <div id="basicInfo">
            </div>
        </div>
        <!--tab部分-->
        <div class="tab-box">
            <span class="tab js-tab tab-hover" data-index="0">办理会员卡</span>
            <span class="tab js-tab" data-index="1">办理计次卡</span>
            <span class="tab js-tab" data-index="2">发放优惠券</span>
            <span class="tab js-tab" data-index="3">发放优惠套餐</span>

            <div class="consultant fr">
                <div class="form-label">
                    服务顾问
                </div>
                <div class="form-item">
                    <input type="text" name="receiverName"
                           class="yqx-input yqx-input-icon yqx-input-small js-card-receiver"
                           value="${SESSION_USER_NAME}" placeholder="请选择服务顾问">
                    <input type="hidden" name="receiver" value="${SESSION_USER_ID}"/>
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </div>
        </div>
        <!--办理会员卡-->
        <div class="tab-con"></div>
        <!--办理计次卡-->
        <div class="tab-con"></div>
        <!--发放优惠券-->
        <div class="tab-con"></div>
        <!--发放优惠套餐-->
        <div class="tab-con"></div>
        <div class="card-submit-panel">
            <div class="yqx-btn yqx-btn-2 js-card-grant-submit">提交</div>
            <div class="yqx-btn yqx-btn-1 fr js-goback">返回</div>
        </div>
    </div>
</div>

<!--办理会员卡 start-->
<script type="text/html" id="cardTypeListTpl">
    <div class="card-num">
        <div class="form-label">
            会员卡号
            <input type="hidden" id="cardNumber" value="${cardNumber}"/>
        </div>
        <div class="form-item card-box">
            <input type="text" name="cardNumber" class="yqx-input yqx-input-icon yqx-input-small member-num js-card-num"
                   data-v-type="required | notchinese | maxLength:16">
            <span class="fa  icon-exclamation-sign icon-small"></span>

            <div class="input-tips">
                <p>最近办卡卡号</p>

                <p class="note-num"></p>
            </div>
        </div>
        <div class="card-tip-box">
            <i class="triangle-left"></i>

            <p class="card-tip"></p>
        </div>
    </div>
    <%if(data.length > 0 ){%>
    <%for(var i=0;i< data.length;i++){%>
    <%var d = data[i]%>
    <div class="silver-card">
        <input type="radio" name="cardTypeId" value="<%=d.id%>"/>

        <div class="silver">
            <img src="${BASE_PATH}/static/img/page/account/card.png">
            <span class="ml35 bold">会员卡&#12288;&#12288;<em class="member-type"><%=d.typeName%></em>&#12288;</span>
            <span class="description"><%=d.cardInfoExplain%></span>

            <div class="unfold fr">
                <span class="fa icon-angle-down icon-small"></span>
                <span class="unfold-txt">展开</span>
            </div>
            <div class="price fr bold">售价：<span class="money-font">&yen;<%=d.salePrice%></span></div>
        </div>
        <div class="card-info-extend">
            <div class="service-discount">
                <p class="member-balance"><span class="card-info-extend-txt">会员卡可用余额:</span><span
                        class="card-info-extend-content money-font">&yen;<%=d.initBalance%></span></p>
                <%var str = d.discountDescript, overflow = false;%>
                <%if(str.length > 148) {%>
                <% overflow = true;%>
                <% str = str.slice(0, 148) + '...';%>
                <%}%>
                <p>
                                <span class="card-info-extend-txt">
                                    折扣:</span><span class="card-info-extend-content discount-info js-show-tips"
                    <%if(d.discountDescript.length > 148) {%><%='data-tips=' + d.discountDescript
                    %><%}%>><%=str%></span>
                </p>
            </div>
            <div class="service-description">
                <p><span class="card-info-extend-txt">有效期:</span><span class="card-info-extend-content"><%=d.effectivePeriodDays%>天</span>
                </p>
            </div>
        </div>
    </div>
    <%}%>
    <%}else{%>
    <div class="nodata clearfix">
        <div class="nodata-l fl">
            <p>亲，您还没有会员卡类型</p>

            <p>赶紧去创建会员卡类型吧！</p>
            <a href="${BASE_PATH}/account/member/create" class="yqx-btn yqx-btn-2 yqx-btn-small">新建会员卡类型</a>
        </div>
        <div class="nodata-r fr">
            <i class="triangle-left"></i>

            <div class="nodata-tips">
                <p>暂无数据</p>
            </div>
        </div>
    </div>
    <%}%>
</script>

<!--办理会员卡 end-->

<!--办理计次卡 start-->
<script type="text/template" id="chooseTpl">
    <%if(data.length > 0 ){%>
    <%for(var i=0;i< data.length;i++){%>
    <%var item=data[i]%>
    <div class="cardTpl">
        <input type="radio" value="<%=item.id%>" name="comboId"/>
        <div class="info-item">
            <img src="${BASE_PATH}/static/img/page/account/combo.png">
            <span class="ml35 bold">计次卡&#12288;&#12288;<em class="comboName"><%=item.comboName%></em></span>

            <div class="unfold fr">
                <span class="fa icon-angle-down icon-small"></span>
                展开
            </div>
            <div class="fold fr">
                <span class="fa icon-angle-up icon-small"></span>
                收起
            </div>
            <div class="price fr bold">售价：<span class="red money-font">&yen;<span
                    class="salePrice"><%=item.salePrice%></span></span></div>
        </div>
        <div class="detail js-detail-box">
            <%if(item.content&&item.content.length){%>
            <div class="service-item">
                <div class="service-title">
                    服务项目：
                </div>
                <div class="service-content">
                    <%for(var j=0;j< item.content.length;j++){%>
                    <%var serviceItem=item.content[j]%>
                    <input type="hidden" value="<%=serviceItem.serviceId%>">

                    <div class="content-item"><span
                            class="service-name js-show-tips"><%=serviceItem.serviceName%></span><span
                            class="service-count"><%=serviceItem.serviceCount%>次</span></div>
                    <%}%>
                </div>
            </div>
            <%}%>
            <% if(item.customizeTime == 1){ %>
            <div class="service-item">
                <div class="service-title">
                    生效时间：
                </div>
                <div class="service-content">
                    <%=item.effectiveDateStr%>
                </div>
            </div>
            <div class="service-item">
                <div class="service-title">
                    过期时间：
                </div>
                <div class="service-content">
                    <%=item.expireDateStr%>
                </div>
            </div>
            <% }else if(item.customizeTime == 0){ %>
            <div class="service-project clearfix">
                <div class="show-grid">
                    <div class="voucher-label service-title">
                        生效时间：
                    </div>
                    <div class="voucher-item service-content">
                        发放后立即生效可用
                    </div>
                </div>
                <div class="show-grid">
                    <div class="voucher-label service-title">
                        过期时间：
                    </div>
                    <div class="voucher-item service-content">
                        发放后<%=item.effectivePeriodDays%>天
                    </div>
                </div>
            </div>
            <%}%>
            <div class="service-item" style="margin-top: 10px;">
                <div class="service-title">
                    备注：
                </div>
                <div class="service-content">
                    <div class="content-item"><%=item.remark%></div>
                </div>
            </div>
        </div>
    </div>
    <%}%>
    <%}else{%>
    <div class="nodata clearfix">
        <div class="nodata-l fl">
            <p>亲，您还没有计次卡类型</p>

            <p>赶紧去创建计次卡类型吧！</p>
            <a href="${BASE_PATH}/account/combo/create" class="yqx-btn yqx-btn-2 yqx-btn-small">新建计次卡类型</a>
        </div>
        <div class="nodata-r fr">
            <i class="triangle-left"></i>

            <div class="nodata-tips">
                <p>暂无数据</p>
            </div>
        </div>
    </div>
    <%}%>
</script>
<!--办理计次卡 end-->

<!--发放优惠券 start-->
<script type="text/html" id="couponTpl">
    <div class="charge-table">
        <table>
            <tr class="table-head">
                <th class="w200">优惠券</th>
                <th class="w90">数量</th>
                <th class="w50">操作</th>
            </tr>
            <tbody class="yqx-tbody" id="orderServiceTB">

            </tbody>
        </table>
        <button class="yqx-btn yqx-btn-3 add js-get-service">添加</button>
    </div>
    <div class="remark">
        <div class="form-label w60">
            备注信息
        </div>
        <div class="form-item w700">
            <textarea class="yqx-textarea" name="" id="" cols="100" rows="3"></textarea>
        </div>
    </div>
</script>
<!--发放优惠券 end-->

<!--发放优惠券table填充 start-->
<script type="text/html" id="serviceTpl">
    <% if (json && json.length) { %>
    <% for (var i = 0; i < json.length; i++) { %>
    <% var orderService = json[i]; %>
    <tr class="service-datatr js-move" data-id="<%=orderService.id%>">
        <td class="p10">
            <div class="form-item w100">
                <input type="text" name="" class="yqx-input" value="<%= orderService.couponName %>">
                <input type="hidden" name="couponId" value="<%= orderService.id %>"/>
            </div>
        </td>
        <td class="p10">
            <div class="form-item w100">
                <input type="text" name="couponNum" class="yqx-input yqx-input-icon"
                       data-v-type="number|maxValue:999"
                        >
                <span class="fa">张</span>
            </div>
        </td>
        <td>
            <span class="delete">删除</span>
        </td>
    </tr>
    <% }} %>
</script>
<!--发放优惠券table填充 end-->

<!--发放优惠套餐 start-->
<script type="text/html" id="comboTpl">
    <div class="content">
        <div class="choose-package">
            <div class="plan" id="plan">

            </div>
        </div>
        <div class="charge-table">
            <table>
                <tr class="table-head">
                    <th class="w200">优惠券</th>
                    <th class="w90">数量</th>
                    <th class="w50">操作</th>
                </tr>
                <tbody class="yqx-tbody" id="tableFill">
                <#list basicOrderService as orderService>
                <tr class="service-datatr js-move" data-id="<%=orderService.id%>">
                    <td class="p10">
                        <div class="form-item w100">
                            <input type="text" name="serviceName" class="yqx-input yqx-input-small js-show-tips"
                                   value="${orderService.couponName}" disabled="" readonly/>
                        </div>
                    </td>
                    <td class="p10">
                        <div class="form-item w100">
                            <input type="text" name="couponNum" class="yqx-input yqx-input-icon"
                                   data-v-type="number|maxValue:999"
                                    >
                            <span class="fa">张</span>
                        </div>
                    </td>
                    <td>
                        <span class="delete">删除</span>
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
        <div class="input-info">
            <div class="amount">
                <div class="form-label">
                    套餐售价
                </div>
                <div class="form-item">
                    <input type="text" name="price" class="yqx-input yqx-input-icon" disabled="disabled">
                    <span class="fa">元</span>
                </div>
            </div>
            <div class="pay-way">
                <div class="form-label">
                    支付方式
                </div>
                <div class="form-item">
                    <select name="paymentId" class="yqx-input yqx-input-icon h36 js-payment-id">
                    <#list paymentList as item>
                        <option value="${item.id}">${item.name}
                        </option>
                    </#list>
                    </select>
                </div>
            </div>
            <div class="collection">
                <div class="form-label">
                    收款金额
                </div>
                <div class="form-item">
                    <input type="text" name="payAmountForCoupon" class="yqx-input yqx-input-icon" data-v-type="required|number"
                           placeholder="请输入金额">
                    <span class="fa">元</span>
                </div>
            </div>
        </div>
        <div class="remark">
            <div class="form-label w60">
                备注信息
            </div>
            <div class="form-item w700">
                <textarea class="yqx-textarea" name="" id="" cols="100" rows="3"></textarea>
            </div>
        </div>
    </div>
</script>
<!--发放优惠套餐 end-->

<!--发放优惠套餐套餐列表 start-->
<script type="text/html" id="planTpl">
    <%if(json.success){%>
    <%for(var i=0;i< json.data.length;i++){%>
    <%var item=json.data[i]%>
    <div class="frame ellipsis-1" data-id="<%=item.id%>" data-price="<%=item.salePrice%>" name="suiteId">
        <%=item.suiteName%>
    </div>
    <%}}%>
</script>
<!--发放优惠套餐套餐列表 end-->

<!--优惠券 。。。。。-->
<script type="text/template" id="tableTpl">
    <%if(json){%>
    <%for(i=0;i
    <json.data.length;i++){%>
    <tr class="service-datatr js-move">
        <%var a=json.data[i];%>
        <td class="p10">
            <div class="form-item w100">
                <input type="text" name="serviceName" class="yqx-input" readonly
                       value="<%=a.couponName%>" disabled=""/>
            </div>
        </td>
        <td class="p10">
            <div class="form-item w100">
                <input type="text" name="couponNum" class="yqx-input yqx-input-icon" readonly value="<%=a.couponCount%>"
                       disabled="">
                <span class="fa">张</span>
            </div>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>
<!---->

<!--车牌-->
<script type="text/html" id="carLicenseTpl">
    <% if(templateData && templateData && templateData.length) { %>
    <ul class="yqx-downlist-content js-downlist-content">
        <% var tdLen = templateData.length; %>
        <% for(var i = 0; i < tdLen; i++) { %>
        <% var item = templateData[i]; %>
        <li class="js-downlist-item">
            <span title="<%=item.id%>" style="width:100%"><%=item.license%></span>
        </li>
        <% } %>
    </ul>
    <% } else {%>
    <div class="downlist-new-car js-new-car">
        车牌不存在,请先 <a href="javascript:void">新建车辆</a>
    </div>
    <%}%>
</script>

<!--账户不存在弹窗-->
<script type="text/template" id="completeCarTpl">
    <div class="account-no-exist-dg">
        <div class="member-card-title">
            账户不存在
        </div>
        <div class="content">
            <p class="mgt40">该车辆<span style="color: red"><%= carLicense%></span>未绑定任何账户,请先完善该车辆信息中的<span
                    style="color:red;">车主电话</span></p>

            <p><a class="complete_info" href="${BASE_PATH}/shop/customer/edit?refer=customer&id=<%=carId%>">完善信息</a></p>
        </div>
    </div>
</script>
<!--个人信息-->
<script type="text/template" id="basicInfoTpl">
    <div class="personal-info show-grid" data-customerid="<%=customer.id%>">
        <div class="col-1">
            <div class="form-label">个人信息</div>
        </div>
        <div class="col-4">
            <div class="form-label sub-form-label">车主：</div>
            <div
                    class="form-item">
                <%=customer.customerName%>
            </div>
        </div>
        <div class="col-4">
            <div class="form-label sub-form-label">车主电话：</div>
            <div class="form-item">
                <%=customer.mobile%>
            </div>
        </div>
        <button class="yqx-btn yqx-btn-2 yqx-btn-micro edit-btn js-edit-btn">查看客户详情</button>
    </div>
    <% var mcLen = memberCardList.length; %>
    <div class="member-card-info <% if (!mcLen) { %> hide <% } %>">
        <div class="form-label member-card-label">会员卡</div>
        <div class="form-item card-width">
            <table class="member-card-table">
                <%for(var i=0;i < mcLen;i++) {%>
                <tr class="member-item-info">
                    <td>
                        <div class="form-label sub-form-label"><%= memberCardList[i].cardTypeName%>:</div>
                        <div class="form-item"><%= memberCardList[i].cardNumber%></div>
                    </td>
                    <td>
                        <div class="form-label sub-form-label blank">余额:</div>
                        <div class="form-item">
                            <span class="money-font">&yen;<%= memberCardList[i].balance%></span>
                        </div>
                    </td>
                </tr>
                <%}%>
            </table>
        </div>
    </div>
    <div class="license clearfix">
        <div class="form-label car-label">
            拥有车辆
        </div>
        <div class="car-license">
            <% var ccLen = customerCarList.length; %>
            <%for(var i=0; i < ccLen; i++) {%>
            <div class="form-item license-num">
                <a href="${BASE_PATH}/shop/customer/car-detail?id=<%=customerCarList[i].id%>&refer=member-grant">
                    <input type="text" class="yqx-input yqx-input-small license-num1"
                           value="<%= customerCarList[i].license%>" readonly>
                </a>
            </div>
            <%}%>
        </div>
    </div>
</script>
<!--会员卡办理收款弹窗-->
<script type="text/template" id="collection">
    <div class="collection-bounce">
        <div class="bounce-title">
            收款
        </div>
        <div class="bounce-content">
            <div class="collection-title">
                <span class="bold">会员卡办理</span>—收款
            </div>
            <div class="combo-type">
                <div class="form-label">会员卡类型：</div>
                <div class="form-item">
                    <input type="text" class="yqx-input" value="<%=type%>" disabled>
                    <input type="hidden" name="comboInfoId" value="<%=id%>">
                </div>
            </div>
            <div class="receivable-amount">
                <div class="form-label">应收金额：</div>
                <div class="form-item">
                    <input type="text" name="amount" class="yqx-input yqx-input-icon" value="<%=amount%>" disabled>
                    <span class="fa">元</span>
                </div>
            </div>
            <div class="collection">
                <div class="form-label">&nbsp;&nbsp;&nbsp;收款：</div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon js-payment-select" data-v-type="required"
                           name="paymentName" placeholder="请选择支付方式"/>
                    <input type="hidden" class="yqx-input" data-v-type="required" name="paymentId"/>
                    <span class="fa icon-angle-down"></span>
                    <div class="yqx-select-options" style="width: 212px; display: none;">
                        <dl>
                        <#list paymentList as payment>
                            <dd class="yqx-select-option js-show-tips" title="${payment.name}"
                                data-key="${payment.id}">${payment.name}</dd>
                        </#list>
                        </dl>
                    </div>
                </div>
                <div class="form-item"><input type="text" class="yqx-input" name="amounts"
                                              data-v-type="required | price" placeholder="金额"/></div>
            </div>
            <button class="yqx-btn yqx-btn-2 js-card-submit">提交</button>
        </div>
    </div>
</script>

<!--计次卡办理收款弹窗-->
<script type="text/template" id="collectionMetting">
    <div class="collection-bounce">
        <div class="bounce-title">
            收款
        </div>
        <div class="bounce-content">
            <div class="collection-title">
                <span class="bold">计次卡办理</span>—收款
            </div>
            <div class="combo-type">
                <div class="form-label">计次卡类型：</div>
                <div class="form-item">
                    <input type="text" class="yqx-input" value="<%=type%>" disabled>
                    <input type="hidden" name="comboInfoId" value="<%=id%>">
                </div>
            </div>
            <div class="receivable-amount">
                <div class="form-label">应收金额：</div>
                <div class="form-item">
                    <input type="text" name="amount" class="yqx-input yqx-input-icon" value="<%=amount%>" disabled>
                    <span class="fa">元</span>
                </div>
            </div>
            <div class="collection">
                <div class="form-label">&nbsp;&nbsp;&nbsp;收款：</div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon js-payment-select" name="paymentName" data-v-type="required" placeholder="请选择支付方式"/>
                    <input type="hidden" class="yqx-input" data-v-type="required" name="paymentId"/>
                    <span class="fa icon-angle-down"></span>
                    <div class="yqx-select-options" style="width: 212px; display: none;">
                        <dl>
                        <#list paymentList as payment>
                            <dd class="yqx-select-option js-show-tips" title="${payment.name}"
                                data-key="${payment.id}">${payment.name}</dd>
                        </#list>
                        </dl>
                    </div>
                </div>
                <div class="form-item">
                    <input type="text" name="payAmountForCombo" class="yqx-input yqx-input-icon" data-v-type="number| required" placeholder="金额">
                    <span class="fa">元</span>
                </div>
            </div>
            <button class="yqx-btn yqx-btn-2 js-combo-submit">提交</button>
        </div>
    </div>
</script>

<!--新增车辆 模板-->
<script type="text/html" id="addlicenseTpl">
    <div class="dialog">
        <div class="dialog-title">新增车辆</div>
        <div class="dialog-con">
            <div class="car-infor">
                <div class="show-grid">
                    <div class="form-label">
                        车牌
                    </div>
                    <div class="form-item">
                        <input type="text" name="license" class="yqx-input yqx-input-small js-search" value=""
                               placeholder="">
                    </div>
                </div>
                <div class="show-grid license-show">
                    <div style="margin-bottom: 10px" class="tip"></div>
                    <div style="margin-bottom: 10px;" id="un-bundle-tip">车牌 <span class="carlicense-number"></span>已绑定其它账户（<a
                            href="javascript:;" class="un-bundle-mobile"></a>），<a href="javascript:;"
                                                                                  class="link-bd js-link-bd">请先去该账户解绑！</a>
                    </div>
                </div>
            </div>
            <div class="dialog-btn">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-bundle">提交</button>
            </div>
        </div>
    </div>
</script>
<#include "yqx/tpl/common/get-coupon-tpl.ftl">
<script type="text/javascript"
        src="${BASE_PATH}/static/js/page/account/new-grant.js?ceea13c5779749eab57a671283bbbd69"></script>
<#include "yqx/layout/footer.ftl">
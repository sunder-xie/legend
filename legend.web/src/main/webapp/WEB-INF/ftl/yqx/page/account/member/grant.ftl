<#include "yqx/layout/header.ftl">
<#include "yqx/tpl/common/car-licence-tpl.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/member/grant.css?b0d6170f0c15c971573d45f455365402" type="text/css"/>
<div class="yqx-wrapper clearfix">
    <div class="left-menu" style="float: left;">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="aside-main">
        <div class="member-header clearfix">
            <h1 class="headline fl">
                会员卡办理
            </h1>
        </div>
        <div class="member-search-form">
            <div class="tab-title">车牌查询</div>
            <div class="member-search-box">
                <div class="form-item">
                    <input type="hidden" value="${carId}" id="carId">
                    <input class="yqx-input search js-search" placeholder="输入车牌..." value="${license?default('')}">
                </div>
            </div>
        </div>
        <div class="member-info" style="display: none;">
            <div id="basicInfo">
            </div>
            <div class="card-num">
                <div class="form-label">
                    会员卡号
                    <input type="hidden" id="cardNumber" value="${cardNumber}" />
                </div>
                <div class="form-item card-box">
                    <input type="text" name="cardNumber" class="yqx-input yqx-input-icon yqx-input-small member-num js-card-num" data-v-type="required | notchinese | maxLength:16">
                    <span class="fa  icon-exclamation-sign icon-small"></span>
                    <div class="input-tips">
                        <p>最近办卡卡号</p>
                        <p class="note-num"></p>
                    </div>
                </div>
                <div class="card-tip-box">
                    <i class="triangle-left"></i>
                    <p class="card-tip">该客户已办理三张会员卡，继续办理需先<a href="javascript:void" class="js-retreat-card">退卡</a></p>
                </div>
                <div class="consultant fr">
                    <div class="form-label">
                        服务顾问
                    </div>
                    <div class="form-item">
                        <input type="text" name="receiverName" class="yqx-input yqx-input-icon yqx-input-small js-card-receiver" value="${SESSION_USER_NAME}"  placeholder="请选择服务顾问">
                        <input type="hidden" name="receiver" value="${SESSION_USER_ID}"/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="choose-type">
            <div class="choose-title">选择会员卡类型</div>
            <div id="card-type-list">
                <div class="card-info">
                </div>
            </div>
            <div class="card-submit-panel mgt10">
                <div class="yqx-btn yqx-btn-2 js-card-grant-submit">提交</div>
                <div class="yqx-btn yqx-btn-1 fr js-goback">返回</div>
            </div>
        </div>
        <input class="yqx-input" type="hidden" name="accountId" id="accountId">
    </div>
</div>
<script type="text/html" id="cardTypeListTpl">
    <%if(data.length > 0 ){%>
    <%for(var i=0;i< data.length;i++){%>
    <%var d = data[i]%>
    <div class="silver-card">
        <input type="radio" name="cardTypeId" value="<%=d.id%>" />
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
                <p class="member-balance"><span class="card-info-extend-txt">会员卡可用余额:</span><span class="card-info-extend-content money-font">&yen;<%=d.initBalance%></span></p>
                <%var str = d.discountDescript, overflow = false;%>
                <%if(str.length > 148) {%>
                <% overflow = true;%>
                <% str = str.slice(0, 148) + '...';%>
                <%}%>
                <p>
                                <span class="card-info-extend-txt">
                                    折扣:</span><span class="card-info-extend-content discount-info js-show-tips"
                    <%if(d.discountDescript.length > 148) {%><%='data-tips=' + d.discountDescript %><%}%>><%=str%></span>
                </p>
            </div>
            <div class="service-description">
                <p><span class="card-info-extend-txt">有效期:</span><span class="card-info-extend-content"><%=d.effectivePeriodDays%>天</span></p>
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

<script type="text/template" id="completeCarTpl">
    <div class="account-no-exist-dg">
        <div class="member-card-title">
            账户不存在
        </div>
        <div class="content">
            <p class="mgt40">该车辆<span style="color: red"><%= carLicense%></span>未绑定任何账户,请先完善该车辆信息中的<span style="color:red;">车主电话</span></p>
            <p><a class="complete_info" href="${BASE_PATH}/shop/customer/edit?refer=customer&id=<%=carId%>">完善信息</a></p>
        </div>
    </div>
</script>
<script type="text/template" id="basicInfoTpl">
    <div class="personal-info show-grid" data-customerid="<%=customer.id%>">
        <div class="col-1">
            <div class="form-label">个人信息</div>
        </div>
        <div class="col-4">
            <div class="form-label sub-form-label">车主：</div><div
                class="form-item">
            <%=customer.customerName%>
        </div>
        </div>
        <div class="col-4">
            <div class="form-label sub-form-label">车主电话：</div><div class="form-item">
            <%=customer.mobile%>
        </div>
        </div>
        <button class="yqx-btn yqx-btn-2 yqx-btn-micro edit-btn js-edit-btn">查看详情</button>
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
                        <div  class="form-item"><%= memberCardList[i].cardNumber%></div>
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
                <div class="form-label">收款：</div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon js-payment-select" data-v-type="required" name="paymentName" placeholder="请选择支付方式"/>
                    <input type="hidden" class="yqx-input" data-v-type="required" name="paymentId"/>
                    <span class="fa icon-angle-down"></span>
                    <div class="yqx-select-options" style="width: 212px; display: none;">
                        <dl>
                        <#list paymentList as payment>
                            <dd class="yqx-select-option js-show-tips" title="${payment.name}" data-key="${payment.id}">${payment.name}</dd>
                        </#list>
                        </dl>
                    </div>
                </div>
                <div class="form-item"><input type="text" class="yqx-input" name="amounts" data-v-type="required | price" placeholder="金额"/></div>
            </div>
            <button class="yqx-btn yqx-btn-2 js-submit">提交</button>
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
                        <input type="text" name="license" class="yqx-input yqx-input-small js-search" value="" placeholder="">
                    </div>
                </div>
                <div class="show-grid license-show">
                    <div style="margin-bottom: 10px" class="tip"></div>
                    <div style="margin-bottom: 10px;" id="un-bundle-tip">车牌 <span class="carlicense-number"></span>已绑定其它账户（<a href="javascript:;" class="un-bundle-mobile"></a>），<a href="javascript:;" class="link-bd js-link-bd">请先去该账户解绑！</a></div>
                </div>
            </div>
            <div class="dialog-btn">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-bundle">提交</button>
            </div>
        </div>
    </div>
</script>

<script type="text/javascript" src="${BASE_PATH}/static/js/page/account/member/grant.js?be6cceeea37f00433e8061fbc82066e4"></script>
<#include "yqx/layout/footer.ftl">
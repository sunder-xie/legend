<#include "/layout/portal-header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d" />
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/onlinepay/lianlian/card-select.css?3476b32f09732de8df3237e708e3b62d">


<div class="yqx-wrapper">
    <div class="main" id="newMain">
        <div class="main-header">
            <h2 class="headline">选择支付方式</h2>
        </div>
        <div class="main-body panel">
            <div class="panel-header">
                <div class="row">
                    <label class="text-label">订单号：</label><strong class="mark-1 divide" name="orderSn">${payVo.orderSn}</strong>
                    <label class="text-label">支付金额：</label><strong class="mark-2" name="totalFee"><#if payVo.totalFee??>#{payVo.totalFee;m2M2}</#if></strong>
                    <input type="hidden" name="source" value="${payVo.source}"/>
                    <input type="hidden" name="ucShopId" value="${payVo.ucShopId}"/>
                    <input type="hidden" name="paymentMethod" value="${payVo.payMethod}"/>
                </div>
                <p class="row">请您下提交订单 24小时 内完成付款，否则订单会自动取消。</p>
            </div>
            <div class="panel-body" id="payContainer"></div>
            <div class="panel-footer">
                <button class="yqx-btn yqx-btn-2 js-pay-btn" disabled>确定支付</button>
            </div>
        </div>
    </div>
</div>

<script type="text/html" id="payedCardListTpl">
    <div class="box">
        <ul class="card-type-tabs js-card-type-tabs">
            <li>选择支付的储蓄卡</li>
            <li class="hide">选择支付的信用卡</li>
        </ul>
        <% var data1 = []; %>
        <ul class="bank-card-list js-card-box">
            <% if (data && data.length) { %>
            <% for (var i = 0; i < data.length; i++) { %>
            <% var item = data[i]; %>
            <% if (item.cardType == 0) { %>
            <li class="bank-item js-bank-item" data-no-agree="<%= item.noAgree %>"
                data-id-no="<%= item.idNo %>" data-acct-name="<%= item.acctName %>"
                data-card-no="<%= item.cardNo %>" data-pay-method="1">
                <img class="bank-icon" src="<%= item.bankLogo %>">
                <span class="bank-name ellipsis-1 js-show-tips"><%= item.bankName %></span>
                <span class="card-account"><%= item.cardNo %></span>
            </li>
            <% } else { %>
            <% data1.push(item); %>
            <% } } } %>
            <li class="add-bank-item">
                <button data-card-type="0" class="add-bank-btn js-add-card-btn">+ 添加银行卡</button>
            </li>
        </ul>
        <ul class="bank-card-list js-card-box">
            <% if (data1.length) { %>
            <% for (var i = 0; i < data1.length; i++) { %>
            <% var item1 = data1[i]; %>
            <li class="bank-item js-bank-item" data-no-agree="<%= item1.noAgree %>"
                data-id-no="<%= item1.idNo %>" data-acct-name="<%= item1.acctName %>"
                data-card-no="<%= item1.cardNo %>" data-pay-method="2">
                <img class="bank-icon" src="<%= item1.bankLogo %>">
                <span class="bank-name ellipsis-1 js-show-tips"><%= item1.bankName %></span>
                <span class="card-account"><%= item1.cardNo %></span>
            </li>
            <% } } %>
            <li class="add-bank-item">
                <button data-card-type="1" class="add-bank-btn js-add-card-btn">+ 添加新信用卡</button>
            </li>
        </ul>
    </div>
</script>

<script type="text/html" id="supportedCardListTpl">
    <div class="box">
        <h3 class="box-title">请选择支付的银行卡（<%= cardType == 0 ? '储蓄卡' : '信用卡'; %>）</h3>
        <ul class="bank-card-list js-add-bank-list limit-6-rows">
            <% if (data && data.length) { %>
            <% for (var i = 0; i < data.length; i++) { %>
            <% var item = data[i]; %>
            <li class="bank-item js-bank-item" data-bank-code="<%= item.bankCode %>">
                <img class="bank-icon" src="<%= item.bankLogo %>">
                <span class="bank-name-1"><%= item.bankName %></span>
            </li>
            <% } } %>
        </ul>
        <div class="text-right">
            <span class="show-more-btn js-show-more" data-type="0">更多</span>
        </div>
    </div>
    <div class="box">
        <h3 class="box-title">请选择支付的银行卡（<%= cardType == 0 ? '储蓄卡' : '信用卡'; %>）</h3>
        <ul class="form-horizontal">
            <li class="form-group">
                <label class="form-label label-must">银行卡号</label>
                <div class="form-item">
                    <input type="text" class="yqx-input js-card-no-input" name="cardNo" placeholder="请输入银行卡号">
                </div>
            </li>
            <li class="form-group">
                <label class="form-label label-must">姓名</label>
                <div class="form-item">
                    <input type="text" class="yqx-input" name="acctName" placeholder="请输入银行预留姓名">
                </div>
            </li>
            <li class="form-group">
                <label class="form-label label-must">身份证号</label>
                <div class="form-item">
                    <input type="text" class="yqx-input" data-v-type="idCard" data-label="身份证号" name="idNo" placeholder="请输入银行身份证号">
                </div>
            </li>
        </ul>
    </div>
</script>
<script src="${BASE_PATH}/static/third-plugin/seajs/sea.js"></script>
<script src="${BASE_PATH}/static/third-plugin/path.config.js?98f845edf92898b7a23a5b384185c04c"></script>
<script src="${BASE_PATH}/static/js/page/onlinepay/lianlian/card-select.js?fa2bdcf48ff29a0cf1c89a354f1e0aa4"></script>
<#include "yqx/layout/footer.ftl">
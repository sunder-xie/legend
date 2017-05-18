<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/wechat-sms.css?87c4c67d21eee2fdc89e69c35324396e">
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="aside-main">
        <div class="row">
            <h3 class="headline">群发短信</h3>
        </div>
        <div class="main-content">
            <div class="row">
                <button class="yqx-btn yqx-btn-2 js-send-part">群发短信</button>
                <button class="yqx-btn yqx-btn-1 js-send-all">全部群发短信</button><p class="sms-info">
                    已选 <span id="selectedNum">0</span> 位客户
                </p><p class="sms-info">
                    剩余短信额度 <span id="restSMSNum">${smsNum}</span> 条，<a class="yqx-link link-sms" href="${BASE_PATH}/marketing/ng/center/sms" >短信充值
                </a></p>
                <a class="yqx-link link-export js-check-func" href="javascript:void(0)"  data-href="${BASE_PATH}/init/import/customerCar" data-func-name="设置">
                    <i class="icon-signout"></i>导入车辆信息
                </a>
            </div>
            <div class="table-box js-select-group" id="customerInfo"></div>
            <div class="yqx-page" id="pageBox"></div>
        </div>
    </div>
</div>
<script id="customerListTpl" type="text/html">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>
                <input class="js-select-all" type="checkbox">
            </th>
            <th>车牌</th>
            <th>车主</th>
            <th>车主电话</th>
            <th>车型</th>
        </tr>
        </thead>
        <tbody>
        <% if (json.data.content) { %>
        <% for (var i = 0; i < json.data.content.length; i++) { %>
        <% var item = json.data.content[i]; %>
        <tr>
            <td>
                <input class="js-select-item" type="checkbox" data-id="<%= item.id %>" data-mobile="<%= item.mobile %>">
            </td>
            <td><%= item.license %></td>
            <td><%= item.customerName %></td>
            <td><%= item.mobile %></td>
            <td><div class="ellipsis-1 js-show-tips"><%= item.carBrand + " " + item.carSeries + " " + item.carModel %></div></td>
        </tr>
        <%}%>
        <%}%>
        </tbody>
    </table>
</script>

<script id="smsDialogTpl" type="text/html">
    <div class="yqx-dialog send-sms-dialog">
        <header class="yqx-dialog-header">
            <h3 class="dialog-title">短信内容</h3>
        </header>
        <section class="dialog-content">
            <div class="row clearfix">
                <span class="rest-sms-box">剩余短信额度 <span class="text-danger"><span id="dgRestSMSNum"></span>条</span></span>
                <a class="yqx-btn yqx-btn-2 fr" href="${BASE_PATH}/marketing/ng/center/sms">立即充值</a>
            </div>
            <div class="row">
                <textarea class="yqx-textarea" id="smsTemplate"></textarea>
            </div>
            <div class="row clearfix">
                <div class="fl">
                    <span>
                        本次发送对象：<span id="sendPeople"></span>
                    </span><span>
                        预估发送短信：<span id="sendNum"></span>
                    </span>
                </div>
                <div class="fr">
                    <button class="yqx-btn yqx-btn-1 js-cancel-btn">取 消</button>
                    <button class="yqx-btn yqx-btn-3 js-send-btn">发 送</button>
                </div>
            </div>
        </section>
    </div>
</script>
<script src="${BASE_PATH}/static/js/page/wechat/wechat-sms.js?03844b6524c608eae367b21dd4ca6615"></script>
<#include "yqx/layout/footer.ftl" >
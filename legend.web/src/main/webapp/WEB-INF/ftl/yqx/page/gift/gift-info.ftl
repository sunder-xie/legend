<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/gift/gift-info.css?efbb29d16a0b04a5388501fe19355323">
<div class="yqx-wrapper clearfix">
    <div class="left-menu" style="float: left;">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="aside-main">

        <div class="btn-group fr">
            <a href="${BASE_PATH}/shop/shop_service_info/addGift" class="yqx-btn btn-1">
                <span class="fa icon-plus"></span>礼品发放
            </a>
        </div>
        <h1 class="headline">发放记录</h1>
        <table class="yqx-table" style="margin-top: 10px;">
            <thead>
            <tr>
                <th>时间</th>
                <th>车牌</th>
                <th>联系人</th>
                <th>联系电话</th>
                <th>礼品内容</th>
                <th>使用兑换码</th>
                <th>礼品发放人</th>
                <th>备注</th>
            </tr>
            </thead>
            <tbody id="listFill"></tbody>

        </table>
        <div class="yqx-page" id="listPage"></div>
    </div>
</div>
<script id="listTpl" type="text/template">
    <%for(var index in json.data.content){%>
    <%item = json.data.content[index]%>
    <tr>
    <td><%=item.gmtCreateStr%></td>
    <td><%=item.license%></td>
    <td><%=item.customerName%></td>
    <td><%=item.mobile%></td>
    <td><%=item.giftContent%></td>
    <td><%=item.giftSn%></td>
    <td><%=item.registrantName%></td>
    <td class="ellipsis-1 js-show-tips"><%=item.giftNote%></td>
    </tr>
    <%}%>
</script>
<script src="${BASE_PATH}/static/js/page/gift/gift-info.js?e63a86374ba16660c4ccb4dc43ab8a64"></script>
<#include "yqx/layout/footer.ftl">
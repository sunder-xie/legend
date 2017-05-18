<#include "yqx/layout/header.ftl">
<#--样式引入区-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/index.css?c6b553843758afac37f08b568533e6eb" type="text/css"/>
<#--样式引入区-->
<div class="yqx-wrapper clearfix">
    <div class="aside">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="aside-main">
        <h3 class="Z-title">
            客户管理  > <i>客户查询</i>
            <a href="${BASE_PATH}/account/summary" class="yqx-btn yqx-btn-1 yqx-btn-small fr">优惠详情</a>
            <a href="${BASE_PATH}/account/flow" class="yqx-btn yqx-btn-1 yqx-btn-small fr" style="margin-right: 10px;">充值记录</a>
        </h3>

        <div class="yqx-group search-form">
            <!-- group内容 start -->
            <div class="yqx-group-content clearfix" id="formId">
                <div class="form-item">
                    <input type="text" name="license" class="yqx-input yqx-input-small" value="" placeholder="车牌">
                </div>
                <div class="form-item">
                    <input type="text" name="mobile" class="yqx-input yqx-input-small" value="" placeholder="车主电话">
                </div>
                <div class="form-item">
                    <input type="text" name="customerName" class="yqx-input yqx-input-small" value="" placeholder="车主">
                </div>
                <div class="form-item">
                    <input type="text" name="cardNumber" class="yqx-input yqx-input-small" value="" placeholder="会员卡号">
                </div>
                <div class="form-item">
                    <select name="cardTypeId" class="yqx-input yqx-input-icon yqx-input-small list">
                    </select>
                </div>
                <button class="yqx-btn yqx-btn-3 js-search-btn yqx-btn-small">查询</button>
            </div>
            <!-- group内容 end -->
        </div>

        <div class="yqx-group search-result" id="info">
            <!-- group标题 start -->
            <div class="yqx-group-head">
                客户列表
            </div>
            <!-- group标题 end -->
            <!-- group内容 start -->
            <div class="yqx-group-content">
                <!-- 表格容器 start -->
                <div id="tableTest"></div>
                <!-- 表格容器 end -->

                <!-- 分页容器 start -->
                <div class="yqx-page" id="pagingTest"></div>
                <!-- 分页容器 end -->

            </div>
            <!-- group内容 end -->
        </div>
        <!-- 表格数据模板 start -->
        <script type="text/template" id="tableTestTpl">
            <table class="yqx-table yqx-table-hover yqx-table-link">
                <thead>
                <tr>
                    <th width="13%">车主</th>
                    <th width="12%">车主电话</th>
                    <th width="18%">车牌</th>
                    <th width="13%">会员卡类型</th>
                    <th width="14%">会员卡号</th>
                    <th width="13%">会员卡余额</th>
                    <th width="17%" class="text-l">操作</th>
                </tr>
                </thead>
                <tbody>
                <%if(json.data.content&&json.data.content.length){%>
                <%for(var i=0;i< json.data.content.length;i++){%>
                <%var item=json.data.content[i]%>
                <tr class="js-account-tr" data-customer-id="<%=item.customerId%>">
                    <input type="hidden" id="accountId" value="<%=item.accountId%>">
                    <td class="ellipsis-1 js-show-tips" title="<%=item.customerName%>"><%=item.customerName%></td>
                    <td><%=item.mobile%></td>
                    <td class="ellipsis-1 w150 js-show-tips table-license">
                        <%= item.licenseList && item.licenseList.length ? item.licenseList.join(';') : '' %>
                    </td>
                    <td class="card-type-name">
                        <div class="column">
                            <%if(item.memberCards != null && item.memberCards.length > 0){%>
                            <%for(var j=0;j< item.memberCards.length;j++){%>
                            <%var card=item.memberCards[j]%>
                            <div class="ellipsis-1 js-show-tips row-text">
                                <%= card.typeName!=null ? (card.expire ? card.typeName + ' (过期)' : card.typeName) : '--' %>
                            </div>
                            <%}}else{%>
                            <div class="ellipsis-1 js-show-tips row-text">
                                <%= '--'%>
                            </div>
                            <%}%>
                        </div>
                    </td>
                    <td class="card-num">
                        <div class="column">
                            <%if(item.memberCards != null && item.memberCards.length > 0){%>
                            <%for(var j=0;j< item.memberCards.length;j++){%>
                            <%var card = item.memberCards[j]%>
                            <div class="ellipsis-1 js-show-tips row-text">
                                <%= card.cardNumber!=null ? card.cardNumber : '--' %>
                            </div>
                            <%}}else{%>
                            <div class="ellipsis-1 js-show-tips row-text">
                                <%= '--'%>
                            </div>
                            <%}%>
                        </div>
                    </td>
                    <td class="money-font card-balance">
                        <div class="column">
                            <%if(item.memberCards != null && item.memberCards.length > 0){%>
                            <%for(var j=0;j< item.memberCards.length;j++){%>
                            <%var card = item.memberCards[j]%>
                            <div class="ellipsis-1 js-show-tips text-right row-text">
                                &yen;<%= card.balance != null ? card.balance : 0 %>
                            </div>
                            <%}}else{%>
                            <div class="ellipsis-1 js-show-tips text-right row-text">
                                &yen;<%= 0 %>
                            </div>
                            <%}%>
                        </div>
                    </td>
                    <td class="text-left">
                        <a href="${BASE_PATH}/account/grant?refer=customer_search&accountId=<%=item.accountId%>&tab=preferential">发券</a><%if(item.unExpireMemberCards!=null && item.unExpireMemberCards.length > 0){%><a
                            href="${BASE_PATH}/account/member/back?accountId=<%=item.accountId%>">退卡</a><a
                            class="js-func-check" href="javascript:void(0);" data-href="${BASE_PATH}/account/member/charge?accountId=<%=item.accountId%>" data-func-name="会员充值">充值</a><%}else if (item.licenseList!=null && item.licenseList.length > 0) {%><a
                            href="${BASE_PATH}/account/grant?refer=customer_search&accountId=<%=item.accountId%>">办卡</a><a
                            href="javascript:void(0);" class="a-disabled">充值</a>
                        <%}%>
                    </td>
                </tr>
                <%}}%>
                </tbody>
            </table>
        </script>
        <script type="text/template" id="listTpl">
            <%if(data&&data.length){%>
            <option value="">会员卡类型</option>
            <%for(var i=0;i< data.length;i++){%>
            <%var item=data[i]%>
                <option value="<%=item.id%>"><%=item.typeName%></option>
            <%}}else{%>
            <option value="">会员卡类型</option>
            <%}%>
        </script>
    </div>
</div>

<#--脚本引入区-->
<script src="${BASE_PATH}/static/js/page/account/index.js?bc90a19df5c4fd29b705d17e951bc432" type="text/javascript"></script>
<#--脚本引入区-->
<#include "yqx/layout/footer.ftl">
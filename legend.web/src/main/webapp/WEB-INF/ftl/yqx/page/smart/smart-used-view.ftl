<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/smart/common/smart.css?84206c0cf2345ab77f31da9c1687703b"/>

<style type="text/css">
    .button_a {
        text-decoration: underline;
    }
</style>

<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="">
        <div class="right-title">智能查询使用记录</div>
        <div class="tabbable-custom ">
            <ul class="nav nav-tabs ">
                <li class="active"><a class="tab_page_a" href="JavaScript:void(0)" data-type="recharge">充值记录</a></li>
                <li class=""><a class="tab_page_a" href="JavaScript:void(0)" data-type="consume">消费记录</a></li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active">
                    <!-- 表格容器 start -->
                    <div id="tableContent"></div>
                    <!-- 表格容器 end -->
                    <!-- 分页容器 start -->
                    <div class="yqx-page" id="pagingColumn"></div>
                    <!-- 分页容器 end -->
                </div>
                <div class="tab-pane">

                </div>
            </div>
        </div>


    </div>
</div>

<!-- 表格数据模板 start -->
<script type="text/template" id="rechargeTableTemplate">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>序号</th>
            <th>充值时间</th>
        <#--<th>订单编号</th>-->
            <th>充值金额</th>
            <th>充值次数</th>
            <th>充值结果</th>
            <th>操作</th>
        </tr>
        </thead>

        <% if(json.success && json.data.content.length > 0){%>
        <% var content = json.data.content %>
        <%for(var i=0;i
        <content.length
        ;i++){%>
        <tr>
            <td><%=i+1%></td>
            <td><%=$dateFormat(content[i].payStartTime,'yyyy-MM-dd hh:mm:ss')%></td>
        <#--<td><%= content[i].rechargeNumber %></td>-->
            <% var billStatus = content[i].billStatus%>

            <td><span class="red">
                <% if(billStatus != 4){%>
                    <%= content[i].rechargeFee %>
                <%} else {%>
                    <%= content[i].payFee %>
                <%}%>
            </span>
            </td>
            <td><%= content[i].rechargeNum %></td>
            <% if(billStatus == 0){%>
            <td><span>未支付</span></td>
            <td>-</td>
                <#--<td><a href="/legend/smart/bihu/recharge/alipay?payFee=<%= content[i].rechargeFee %>&rechargeNumber=<%= content[i].rechargeNumber %>"-->
                   <#--target="_blank" class="button_a">马上支付</a></td>-->

            <%} else if (billStatus == 1){%>
            <td><span>金额确认中</span></td>
            <td><a href="/legend/smart/bihu/recharge/ judge-go?rechargeNumber=<%= content[i].rechargeNumber %>"
                   target="_blank" class="button_a">查看进度</a></td>
            <%}else if (billStatus == 2){%>
            <td><span class="red">充值失败</span></td>
            <td>-</td>
            <%}else if (billStatus > 2){%>
            <td><span>充值成功</span></td>
            <td>-</td>
            <%}%>
        </tr>
        <%}%>
        <%}else{%>
        <tr class="no-data">
            <td  colspan="6">暂无数据</td>
        </tr>
        <%}%>
    </table>
</script>


<!-- 表格数据模板 start -->
<script type="text/template" id="consumeTableTemplate">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>序号</th>
            <th>查询时间</th>
            <th>查询车牌</th>
            <th>查询结果</th>
            <th>扣除次数</th>
        </tr>
        </thead>

        <% if(json.success && json.data.content.length > 0){%>
        <% var content = json.data.content %>
        <%for(var i=0;i<content.length ;i++){%>
        <tr>
            <td><%=i+1%></td>
            <td><%=$dateFormat(content[i].gmtCreate,'yyyy-MM-dd hh:mm:ss')%></td>
            <td><%= content[i].licenseNo %></td>
            <td><% if(content[i].searchResult) {%>
                查询成功
                <%} else {%>
                <span class="red">查询失败</span>
                <%}%>
            </td>
            <td><% if(content[i].isUsedSearchNum) {%>
                1
                <%} else {%>
                0
                <%}%>
            </td>
        </tr>
        <%}%>
        <%}else{%>
        <tr class="no-data">
            <td colspan="5">暂无数据</td>
        </tr>
        <%}%>
    </table>
</script>
<!-- 表格数据模板 end -->

<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart.js?4abf167c5df3e2fa7854a453b2cec085"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart-ajax.js?5c4be91860d3a836366b110367358251"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/smart-used-view.js?224705b208e15c89aa1723862fbb1f6f"></script>
<#include "yqx/layout/footer.ftl">
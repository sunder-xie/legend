<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/reward/list.css?cf6701688bcf1cf4439d8f4629534b06"/>
<!-- 样式引入区 end-->
<body>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="Z-right">
        <div class="Z-title">奖励金<em>(可用于采购淘汽档口任意商品)</em>
            <p>
                <span class="un-effect">待生效余额：<em>0.00</em></span>
                <span class="effected">已生效余额：<em>0.00</em></span>
                <#if ratio.shopId ??>
                    <span class="rule">奖励金收入=商业险保费*${ratio.commercialCommissions}+交强险保费*${ratio.trafficCommissions}</span>
                </#if>
            </p>
        </div>
        <div class="Z-tab">
            <span class="Z-tab-item first">待生效收入</span>
            <span class="Z-tab-item second">已生效收入</span>
            <span class="Z-tab-item">支出</span>
        </div>
        <!-- 表格容器 start -->
        <div id="tableTest"></div>
        <!-- 表格容器 end -->

        <!-- 分页容器 start -->
        <div class="yqx-page" id="pagingTest"></div>
        <!-- 分页容器 end -->

        <!-- 表格数据模板 start -->
        <script type="text/template" id="tableTestTpl">
            <table class="yqx-table" id="tableTest">
                <thead>
                <tr>
                    <% if(type == 0||type == 1){%>
                    <th>收入</th>
                    <%}else{%>
                    <th>支出</th>
                    <%}%>
                    <% if(type == 0){%>
                    <th>创建时间</th>
                    <%}%>
                    <% if(type == 1){%>
                    <th>生效时间</th>
                    <%}%>
                    <% if(type == 0||type == 1){%>
                    <th>获得原因</th>
                    <th>业务来源</th>
                    <%}else{%>
                    <th>消耗时间</th>
                    <th>消耗原因</th>
                    <%}%>
                    <th class="Z-orderSn">单号</th>
                </tr>
                </thead>
                <tbody>
                <% if(json.success && json.data.content.length > 0){%>
                <% var content = json.data.content %>
                <%for(var i=0;i<content.length;i++){%>
                <tr>
                    <% if(type == 0){%>
                    <td><%= content[i].rewardFee %></td>
                    <%}else{%>
                    <td><%= content[i].amount %></td>
                    <%}%>
                    <% if(type == 0){%>
                    <td><%= $dateFormat(content[i].gmtCreate,'yyyy.MM.dd') %></td>
                    <%}%>
                    <% if(type == 0){%>
                    <td>
                        <% if(content[i].insuranceType == 1){%>
                        交强险
                        <%}else if(content[i].insuranceType == 2){%>
                        商业险
                        <%}%>
                    </td>
                    <td><%= content[i].companyName %></td>
                    <%}else if(type == 1){%>
                    <td><%= $dateFormat(content[i].gmtCreate,'yyyy.MM.dd') %></td>
                    <td><%= content[i].recordReason %></td>
                    <td><%= content[i].businessSource %></td>
                    <%}else{%>
                    <td><%= $dateFormat(content[i].gmtCreate,'yyyy.MM.dd') %></td>
                    <td><%= content[i].recordReason %></td>
                    <%}%>
                    <% if(type == 0){%>
                    <td title="<%= content[i].outerInsuranceFormNo %>"><%= content[i].outerInsuranceFormNo %></td>
                    <%}else{%>
                    <td title="<%= content[i].outOrderSn %>"><%= content[i].outOrderSn %></td>
                    <%}%>
                </tr>
                <%}%>
                <%}else{%>
                <tr><td class="no-data">暂无数据</td></tr>
                <%}%>
                </tbody>
            </table>
        </script>
    </div>
</div>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/ax_insurance/reward/list.js?b46237e26a5791319eee8709f8f26a5e"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
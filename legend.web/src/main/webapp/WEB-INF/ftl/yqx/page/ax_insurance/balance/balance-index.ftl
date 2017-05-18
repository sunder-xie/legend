<#--淘汽对账页面-->
<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/balance/balance-index.css?042edcb068e0202db7a34088b095cdee"/>
<!-- 样式引入区 end-->
<body>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="Z-right">
        <div class="Z-title">服务包
            <#--<p>-->
                <#--<span class="un-effect">待生效余额：<em>${noEffectiveNum}</em></span>-->
                <#--<span class="effected">已生效余额：<em>${surplusRewardNum}</em></span>-->
            <#--</p>-->
        </div>
        <div class="Z-tab">
            <span class="Z-tab-item first">买保险送服务包</span>
            <span class="Z-tab-item second">买服务包送保险</span>
            <#--<span class="Z-tab-item">支出</span>-->
        </div>
        <div class="infoDetail"></div>
        <script id="infoDetailTpl" type="text/html">
            <ul class="Z-info-detail">
                <% if(json.success && json.data){%>
                    <% var info = json.data %>
                    <li class="bd">
                        <p>保单:</p>
                        <p>
                            <% if(type == 0){%>
                                <span>累计销售<em><%= info.orderTotalCount %></em>单</span>
                                <span>本月销售<em><%= info.orderMonthCount %></em>单</span>
                            <%}else{%>
                                <span>累计赠送<em><%= info.orderTotalCount %></em>单</span>
                                <span>本月赠送<em><%= info.orderMonthCount %></em>单</span>
                            <%}%>
                        </p>
                        <p>
                            <span>待生效<em><%= info.orderDsxCount %></em>单</span>
                            <span>已生效<em><%= info.orderYsxCount %></em>单</span>
                        </p>
                    </li>
                    <li class="fwb">
                        <p>服务包:</p>
                        <p>
                            <% if(type == 0){%>
                            <span>累计赠送<em><%= info.packageTotalCount %></em>个</span>
                            <span>本月赠送<em><%= info.packageMonthCount %></em>个</span>
                            <%}else{%>
                            <span>累计销售<em><%= info.packageTotalCount %></em>个</span>
                            <span>本月销售<em><%= info.packageMonthCount %></em>个</span>
                            <%}%>
                            <% if(type == 0){%>
                            <span>待生效<em><%= info.packageDsxCount %></em>个</span>
                            <%}%>
                        </p>
                        <p>
                            <span>待发货<em><%= info.packageDfhCount %></em>个</span>
                            <span>配送中<em><%= info.packagePszCount %></em>个</span>
                            <span>已签收<em><%= info.packageYqsCount %></em>个</span>
                        </p>
                    </li>
                    <li class="xj">
                        <p>现金:</p>
                        <p><span>累计已结算<em> <%= info.cashTotalAmount %></em></span></p>
                        <p><span>本月可结算<em> <%= info.cashMonthAmount %></em></span></p>
                    </li>
                <%}%>
            </ul>
        </script>
        <div class="Z-select">
            <span class="Z-check"><input type="checkbox"/>本月可结算</span>
            <span class="Z-search">搜索</span>
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
                <% if(type == 0){%>
                <thead>
                <tr>
                    <th>商业险保费</th>
                    <#--<th>交强险保费</th>-->
                    <th>保单创建日期</th>
                    <th>保单生效日期</th>
                    <th>服务包</th>
                    <th>服务包状态</th>
                    <th class="Z-orderSn">物料订单</th>
                    <th>现金收入</th>
                    <th>结算状态</th>
                </tr>
                </thead>
                <tbody>
                <% if(json.success && json.data.content.length > 0){%>
                <% var content = json.data.content %>
                <%for(var i=0;i<content.length;i++){%>
                <tr>
                    <td><%= content[i].commercialInsureFee %></td>
                    <#--<td><%= content[i].compulsoryInsureFee %></td>-->
                    <td><%= content[i].insureCreateTimeStr %></td>
                    <td><%= content[i].insureStartTimeStr %></td>
                    <td><%= content[i].packageName %></td>
                    <td><%= content[i].packageStatusStr %></td>
                    <td><%= content[i].materialOrderSn %></td>
                    <td><%= content[i].rewardAmount %></td>
                    <td><%= content[i].confirmAgentMoneyStr %></td>
                </tr>
                <%}%>
                <%}else{%>
                <tr><td class="no-data">暂无数据</td></tr>
                <%}%>
                </tbody>
                <%}else{%>
                <thead>
                <tr>
                    <th>服务包</th>
                    <th>服务包总价</th>
                    <th>服务包购买日期</th>
                    <th>服务包状态</th>
                    <th>物料订单</th>
                    <th>商业险保单号</th>
                    <th>现金收入</th>
                    <th>结算状态</th>
                </tr>
                </thead>
                <tbody>
                <% if(json.success && json.data.content.length > 0){%>
                <% var content = json.data.content %>
                <%for(var i=0;i<content.length;i++){%>
                <tr>
                    <td><%= content[i].packageName %></td>
                    <td><%= content[i].packageMarketPrice %></td>
                    <td><%= content[i].packageGmtValidStr %></td>
                    <td><%= content[i].packageStatusStr %></td>
                    <td><%= content[i].materialOrderSn %></td>
                    <td><%= content[i].outerInsuranceFormNo %></td>
                    <td><%= content[i].rewardAmount %></td>
                    <td><%= content[i].confirmAgentMoneyStr %></td>
                </tr>
                <%}%>
                <%}else{%>
                <tr><td class="no-data">暂无数据</td></tr>
                <%}%>
                </tbody>
                <%}%>
            </table>
        </script>
    </div>
</div>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/ax_insurance/balance/balance-index.js?c6122e7ddc26832759d1a20aaf982254"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/smart/common/smart.css?84206c0cf2345ab77f31da9c1687703b"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/third-plugin/select2/select2_metro.css"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/ax_insurance/settle/new/settle-common.css?131f6e62ee99a009392707f208c82487"/>

<!-- 样式引入区 end-->
<body>
<div class="yqx-wrapper  clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <!-- 右侧内容区 start -->
    <div class="order-box fl">
        <div class="order-box-head bord_bottom">
            <span class="right-title">现金对账</span>
            <a href="/legend/insurance/settle/view/rule" class="underline_a settle-rule">结算规则</a>

            <a href="/legend/insurance/settle/view/index" class="right return">返回对账
                <div class="right_arrows"></div>
            </a>
        </div>
        <!-- 查询条件 start -->
        <div class="order-box-body" id="orderSearchForm">
            <input type="hidden" name="search_settleRule" value="1"> <!--现金 -->
            <div class="row">
                <div class="form-item">
                    <input type="text" id="search_settle_start_time" name="search_settleStartTime"
                           class="yqx-input yqx-input-icon yqx-input-small datepicker"
                           value="" placeholder="结算条件开始时间">
                    <span class="fa icon-calendar icon-small"></span>
                </div>
                至
                <div class="form-item">
                    <input type="text" id="search_settle_end_time" name="search_settleEndTime"
                           class="yqx-input yqx-input-icon yqx-input-small datepicker"
                           value="" placeholder="结算条件结束时间">
                    <span class="fa icon-calendar icon-small"></span>
                </div>
                <div class="form-item">
                    <select id="choose_cooperation" name="search_cooperationId" style="width: 150px;">
                        <option value="-1">模式</option>
                    <#list cooperationModeList as cooperationMode>
                        <option value="${cooperationMode.dicId}">${cooperationMode.dicValue}</option>
                    </#list>
                    </select>
                </div>
                <div class="form-item">
                    <input type="text" id="search_license" name="search_license" class="yqx-input yqx-input-small"
                           value=""
                           placeholder="车牌号">
                </div>
            </div>
            <div class="row">
                <div class="form-item">
                    <input type="text" id="search_car_owner" name="search_carOwner" class="yqx-input yqx-input-small"
                           value=""
                           placeholder="车主姓名">
                </div>
                <div class="form-item">
                    <input type="text" id="search_company" name="search_company" class="yqx-input yqx-input-small"
                           value=""
                           placeholder="保险公司">
                </div>
                <div class="form-item">
                    <select id="choose_settle_project" name="search_settleProjectId" style="width: 150px;">
                        <option value="-1">结算项目</option>
                    <#list settleProjectList as dicDTO>
                        <option value="${dicDTO.dicId}">${dicDTO.dicValue}</option>
                    </#list>
                    </select>

                </div>
                <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn fr search_button">搜索</a>
            </div>
        </div>
    </div>
    <div class="clear_line"></div>
    <!-- 查询条件 end -->
    <div class="fl order-box">

        <!-- 表格容器 start -->
        <div id="cash_table" >

        </div>
        <!-- 表格容器 end -->
        <!-- 分页容器 start -->
        <div class="yqx-page" id="pagingColumn">

        </div>
        <!-- 分页容器 end -->
    </div>
    <!-- 右侧内容区 end -->
</div>

<script type="text/html" id="cacheTableTpl">
    <div class="order-box-head box-content">
        <span class="right-content">
            <span>结算金额合计：</span>
            <span class="red">
                <% if(json.success && json.data.content.length > 0){%>
                <%= $decimalPointFormat(json.data.content[0].pageAmount,2) %>
                <%}else{%>
                    0.00
                <%}%>
            </span>
        </span>
    <#--todo 导出明细-->
    </div>
    <!-- 表格容器 start -->
    <div class="order-box-body">
        <table class="yqx-table">
            <thead>
            <tr>
                <th>序号</th>
                <th>模式</th>
                <th>车牌号</th>
                <th>车主姓名</th>
                <th>保险公司</th>
                <th>结算项目</th>
                <th>结算条件</th>
                <th>结算条件值</th>
                <th>结算金额</th>
                <th>状态</th>
            </tr>
            </thead>

            <% if(json.success && json.data.content.length > 0){%>
            <% var content = json.data.content %>
            <%for(var i=0;i< content.length ;i++){%>
            <tr>
                <td><%=i+1%></td>
                <td><%=content[i].cooperationModeName%></td>
                <td><%=content[i].vehicleSn%></td>
                <td><%=content[i].carOwnerName%></td>
                <td><%=content[i].insuranceCompanyName%></td>
                <td><%=content[i].settleProjectName%></td>
                <td><%=content[i].settleConditionName%></td>
                <% var settleConditionTime = $dateFormat(content[i].settleConditionTime,'yyyy-MM-dd')%>
                <% if(settleConditionTime == null || settleConditionTime == "1970-01-01"){%>
                <td>无</td>
                <%} else {%>
                <td><%=settleConditionTime%></td>
                <%}%>
                <td><%= $decimalPointFormat(content[i].settleFee,2) %></td>
                <% var settleFeeStatus = content[i].settleFeeStatus%>
                <% if(settleFeeStatus < 2){%>
                <td>未结算</td>
                <%} else {%>
                <td>已结算</td>
                <%}%>
            </tr>
            <%}%>
            <%}else{%>
            <tr class="no-data">
                <td colspan="10">暂无数据</td>
            </tr>
            </tr>
            <%}%>
        </table>
    </div>
</script>
<!-- 脚本引入区 start -->
<#--select2 drop-->
<#--<script type="text/javascript" src="${BASE_PATH}/static/third-plugin/Font-Awesome-3.2.1/src/assets/js/bootstrap-2.3.1.min.js"></script>-->
<script type="text/javascript" src="${BASE_PATH}/static/js/common/base/app.js?9786bd74e565a5ce39170374cdc655dd"></script>
<script type="text/javascript" src="${BASE_PATH}/static/third-plugin/select2/select2.js"></script>
<script type="text/javascript"
        src="${BASE_PATH}/static/js/page/smart/common/smart.js?4abf167c5df3e2fa7854a453b2cec085"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart-ajax.js?5c4be91860d3a836366b110367358251"></script>
<script src="${BASE_PATH}/static/js/page/ax_insurance/settle/new/settle_cash.js?c594d1ed85989d05bab8d6ac689e268d"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/settle/service-list.css?5a39f6f39e1d1550def6a20b93914b9a"/>
<!-- 样式引入区 end-->
<body>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">服务券</h3>
        </div>
        <!-- 查询条件 start -->
        <div class="condition-box" id="searchForm">
            <div class="form-item">
                <input type="text" name="search_license" class="yqx-input yqx-input-small" value="" placeholder="车牌号">
            </div>
            <div class="form-item">
                <input type="text" name="search_startTime" class="yqx-input yqx-input-icon yqx-input-small datepicker" id="start1"
                       value="" placeholder="生效开始时间">
                <span class="fa icon-calendar icon-small"></span>
            </div>
            至
            <div class="form-item">
                <input type="text" name="search_endTime" class="yqx-input yqx-input-icon yqx-input-small datepicker" id="end1"
                       value="" placeholder="生效结束时间">
                <span class="fa icon-calendar icon-small"></span>
            </div>
            <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn fr">查询</a>
        </div>
        <!-- 查询条件 end -->
        <!-- 表格容器 start -->
        <div class="order-list-content" id="orderListContent">

        </div>
        <!-- 表格容器 end -->
        <!-- 分页容器 start -->
        <div class="yqx-page" id="orderListPage">

        </div>
        <!-- 分页容器 end -->
    </div>
    <!-- 右侧内容区 end -->
</div>
<script type="text/html" id="orderListTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th class="text-l">服务包</th>
            <th class="text-l">车牌号</th>
            <th class="text-l">车主姓名</th>
            <th class="text-l">车主电话</th>
            <th class="text-l">创建时间</th>
            <th class="text-l">生效时间</th>
            <th class="text-l">失效时间</th>
            <th class="text-c">操作</th>
        </tr>
        </thead>
        <% if(json.success && json.data){%>
        <%for(var i=0;i<json.data.content.length;i++){%>
        <% var item = json.data.content[i] %>
        <tr data-id = '<%=item.id%>'>
            <td class="text-l">
                <div class="max-text js-show-tips"><%=item.packageName%></div>
            </td>
            <td class="text-l"><%=item.vehiclePlateNo%></td>
            <td class="text-l"><%=item.clientName%></td>
            <td class="text-l"><%=item.clientPhone%></td>
            <td class="text-l"><%=item.gmtCreateStr%></td>
            <td class="text-l"><%=item.gmtValidStr%></td>
            <td class="text-l"><%=item.gmtExpireStr%></td>
            <td class="text-c">
                <%if(item.available){%>
                <a href="javascript:;" class="js-cancel-btn">核销</a>
                <%}%>
            </td>
        </tr>
        <%}}%>
    </table>
</script>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/ax_insurance/settle/service-list.js?88cffdfe3df16255696a036d90e4d2a0"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/paint/paintUseRecordList.css?d35b65e2d63043c2322f62bc78722c89"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fr">
        <!-- 标题 start -->
        <div class="order-head clearfix">
            <h3 class="headline fl">油漆使用记录</h3>
            <button class="yqx-btn yqx-btn-2 yqx-btn-small fr new-inventory js-add-inventory"><i>+</i>新增使用记录</button>
        </div>
        <!-- 标题 end -->
        <!-- 右侧内容区 start -->
        <!-- form start -->
        <div class="form-box" id="formData">
            <div class="form-item">
                <input type="text" name="startTime" class="yqx-input yqx-input-icon yqx-input-small datepicker" value="" placeholder="出库开始日期">
                <span class="fa icon-calendar"></span>
            </div>
            至
            <div class="form-item">
                <input type="text" name="endTime" class="yqx-input yqx-input-icon yqx-input-small datepicker" value="" placeholder="出库结束日期">
                <span class="fa icon-calendar"></span>
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
        </div>
        <!-- form end -->
        <!-- list start -->
        <!-- 表格容器 start -->
        <div class="table-list"  id="tableCon"></div>
        <!-- 表格容器 end -->


        <!-- list end -->
        <!-- 右侧内容区 end -->
    </div>
</div>

<script type="text/html" id="tableTpl">
    <%if(json.data && json.data.content){%>
    <table class="yqx-table yqx-table-hover yqx-table-link" id="tableTest">
        <thead>
        <tr>
            <th class="text-l">序号</th>
            <th class="text-l">使用单号</th>
            <th class="text-l">出库日期</th>
            <th class="text-r">出库质量</th>
            <th class="text-r">总金额</th>
            <th class="text-l">出库人</th>
            <th class="text-l">领料人</th>
            <th class="text-c">操作</th>
        </tr>
        </thead>
        <tbody>
        <%for(var i=0;i < json.data.content.length ;i++){%>
        <%var item = json.data.content[i];%>
        <tr data-list-id="<%=item.id%>">
            <td class="text-l"><%=json.data.size*(json.data.number-1)+i+1%></td>
            <td class="text-l">
                <div class="max-width js-show-tips"><%=item.useRecordSn%></div>
            </td>
            <td class="text-l">
                <div class="max-width js-show-tips"><%=item.warehouseOutTimeStr%></div>
            </td>
            <td class="text-r"><%=item.totalWeight%>g</td>
            <td class="text-r money-font">&yen;<%=item.totalAmount%></td>
            <td class="text-l"><%=item.operatorName%></td>
            <td class="text-l"><%=item.painterName%></td>
            <td class="text-c">
                <a href="javascript:;" class="view js-view">查看</a>
            </td>
        </tr>
        <%}%>
        </tbody>
    </table>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="paging"></div>
    <!-- 分页容器 end -->
    <%}%>
</script>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/magic/paint/paintUseRecordList.js?5478993c942582c613b2305b409a8b17"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
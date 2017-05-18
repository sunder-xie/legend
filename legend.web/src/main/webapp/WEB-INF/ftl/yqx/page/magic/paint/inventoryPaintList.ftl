<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/paint/inventoryPaintList.css?789376277a73895ff80be86ea5b359e1"/>
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
            <h3 class="headline fl">油漆盘点</h3>
            <button class="yqx-btn yqx-btn-2 yqx-btn-small fr new-inventory js-add-new-inventory"><i>+</i>新建盘点单</button>
        </div>
        <!-- 标题 end -->
        <!-- 右侧内容区 start -->
        <!-- form start -->
        <div class="form-box" id="formData">
            <input type="hidden" value="1" name="search_status"/>
            <div class="form-item">
                <input type="text" name="search_recordSn" class="yqx-input yqx-input-small" value="" placeholder="请输入盘点单号">
            </div>
            <div class="form-item">
                <input type="text" name="search_inventoryStartTime" class="yqx-input yqx-input-icon yqx-input-small" id="startDate" value="" placeholder="盘点开始日期">
                <span class="fa icon-calendar"></span>
            </div>
            至
            <div class="form-item">
                <input type="text" name="search_inventoryEndTime" class="yqx-input yqx-input-icon yqx-input-small" id="endDate" value="" placeholder="盘点结束日期">
                <span class="fa icon-calendar"></span>
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
        </div>
        <!-- form end -->
        <!-- list start -->
        <div class="list-tab-box js-list-tab">
            <div class="list-tab current-tab" data-status="1">已保存
            </div><div class="list-tab" data-status="2">已盘点
            </div><div class="list-tab" data-status="">全部</div>
        </div>
        <!-- 表格容器 start -->
        <div class="table-list"  id="tableCon"></div>
        <!-- 表格容器 end -->

        <!-- 分页容器 start -->
        <div class="yqx-page" id="paging"></div>
        <!-- 分页容器 end -->
        <!-- list end -->
        <!-- 右侧内容区 end -->
    </div>
</div>

<script type="text/html" id="tableTpl">

    <table class="yqx-table yqx-table-hover" id="tableTest">
        <thead>
        <tr>
            <th class="text-l"><div class="p-left">序号</div></th>
            <th class="text-l">盘点单号</th>
            <th class="text-l">盘点日期</th>
            <th class="text-r">盘点油漆数</th>
            <th class="text-r">油漆消耗量</th>
            <th class="text-r">油漆消耗成本</th>
            <th class="text-l">开单人</th>
            <th class="text-l">盘点人</th>
            <th class="text-l">状态</th>
            <th class="text-c">操作</th>
        </tr>
        </thead>
        <tbody>
        <%for(var i=0;i< json.data.content.length;i++){%>
        <%var item = json.data.content[i]%>
        <tr data-id="<%=item.id%>">
            <td class="text-l">
                <div class="p-left"><%=json.data.size*(json.data.number)+i+1%></div>
            </td>
            <td class="text-l">
                <div class="max-width js-show-tips"><%=item.recordSn%></div>
            </td>
            <td class="text-l">
                <div class="max-width js-show-tips"><%=item.inventoryTimeStr%></div>
            </td>
            <td class="text-r"><%=item.paintNum%>种</td>
            <td class="text-r"><%=item.diffStock%>g</td>
            <td class="text-r money-font">&yen;<%=item.diffStockPrice%></td>
            <td class="text-l"><%=item.operatorName%></td>
            <td class="text-l"><%=item.paintCheckerName%></td>
            <td class="text-l">
                <%=item.paintStatusStr%>
            </td>
            <td class="text-c">
                <%if(item.paintStatus == 1){%>
                <a href="javascript:;" class="edit js-edit">编辑</a>
                <a href="javascript:;" class="delete js-delete">删除</a>
                <%}else{%>
                <a href="javascript:;" class="look js-look">查看</a>
                <%}%>
            </td>
        </tr>
        <%}%>
        </tbody>
    </table>

</script>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/magic/paint/inventoryPaintList.js?14af32da9da3d05a3b8a075d3c015774"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
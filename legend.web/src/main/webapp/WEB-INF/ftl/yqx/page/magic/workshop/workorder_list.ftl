<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/workorder_list.css?bcb999e848e7ee09cd2fda77b699cd3c"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h3 class="headline fl">施工单查询</h3>
        </div>

        <div class="search-info" id="search">
            <div class="search-up">
                <div class="form-item">
                    <input type="text" name="carLicense" class="yqx-input yqx-input-small" value="" placeholder="车牌">
                </div>
                <div class="form-item">
                    <input type="text" name="workOrderSn" class="yqx-input yqx-input-small" value="" placeholder="施工单编号">
                </div>
                <div class="form-item">
                    <input type="text" name="orderSn" class="yqx-input yqx-input-small" value="" placeholder="工单编号">
                </div>
            </div>
            <div class="search-middle">
                <div class="form-item">
                    <input type="hidden" name="workOrderStatus"/>
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-status" value="" placeholder="施工单状态">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <div class="form-item mr0">
                    <input id="startDate" type="text" name="startTime" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="施工单开单开始时间">
                    <span class="fa icon-calendar icon-small"></span>
                </div><div class="until">至
                </div><div class="form-item">
                    <input id="endDate" type="text" name="endTime" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="施工单开单结束时间">
                    <span class="fa icon-calendar icon-small"></span>
                </div>
            </div>
            <div class="search-down">
                <div class="form-item w150">
                    <input type="hidden" name="accessoriesPrepare">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-accessories" value="" placeholder="配件准备状态">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <div class="form-item w150">
                    <input type="hidden" name="paintPrepare">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-paint" value="" placeholder="面漆准备状态">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <div class="form-item w180 mr0">
                    <input id="startDate1" type="text" name="startExpectTime" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="客户期望交车时间">
                    <span class="fa icon-calendar icon-small"></span>
                </div><div class="until">至
                </div><div class="form-item w180">
                    <input id="endDate1" type="text" name="endExpectTime" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="客户期望交车时间">
                    <span class="fa icon-calendar icon-small"></span>
                </div><button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
            </div>
        </div>

        <div class="excel fr">
            <a href="javascript:;" class="export-excel">导出Excel</a>
        </div>

        <div class="order-list">
            <div class="list-head">
                <div class="tab-item current-item">施工单列表</div>
            </div>
            <!-- 表格容器 start -->
            <div id="tableTest"></div>
            <!-- 表格容器 end -->

            <!-- 分页容器 start -->
            <div class="yqx-page" id="pagingTest"></div>
            <!-- 分页容器 end -->
        </div>
    </div>
</div>

<script type="text/template" id="tableTestTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th class="text-c" width="35px">序号</th>
            <th class="text-l" width="70px">车牌</th>
            <th class="text-l" width="76px">生产线</th>
            <th class="text-l" width="100px">车型</th>
            <th class="text-l" width="70px">状态</th>
            <th class="text-l" width="70px">服务顾问</th>
            <th class="text-l" width="100px">施工时长（分钟）</th>
            <th class="text-l" width="120px">计划完工时间</th>
            <th class="text-l" width="120px">创建时间</th>
        </tr>
        </thead>
        <%if(json.data&&json.data.content&&json.data.content.length){%>
        <%for(var i=0;i<json.data.content.length;i++){%>
        <%var content=json.data.content[i]%>
        <tr class="detail-page" data-content-id="<%=content.id%>">
            <td class="text-c"><%=json.data.size*(json.data.number)+i+1%></td>
            <td class="js-show-tips ellipsis-1 text-l"><%=content.carLicense%></td>
            <td class="js-show-tips ellipsis-1 text-l"><%=content.lineName%></td>
            <td class="js-show-tips ellipsis-1 text-l"><%=content.carInfo%></td>
            <td class="js-show-tips ellipsis-1 text-l"><%=content.statusStr%></td>
            <td class="js-show-tips ellipsis-1 text-l"><%=content.serviceSa%></td>
            <td class="js-show-tips ellipsis-1 text-l"><%=content.totalTimeStr%></td>
            <td class="js-show-tips ellipsis-1 text-l"><%=content.planEndTimeStr%></td>
            <td class="js-show-tips ellipsis-1 text-l"><%=content.createStr%></td>
        </tr>
        <%}%>
        <%}%>
    </table>
</script>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/workshop/workorder_list.js?767e0cb3d1827c6ef673329f3e8f758e"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
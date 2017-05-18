<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/workorder-break-list.css?bafafefb4405395f191d395591829393"/>
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
            <h3 class="headline fl">中断管理</h3>
        </div>
        <div class="content">
            <div class="search-box" id="search">
                <div class="show-grid">
                    <div class="form-label">
                        施工人员:
                    </div>
                    <div class="form-item">
                        <input type="text" name="realOperator" class="yqx-input yqx-input-icon yqx-input-small js-person" value="" placeholder="">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-label">
                        工序：
                    </div>
                    <div class="form-item">
                        <input type="text" name="processName" class="yqx-input yqx-input-icon yqx-input-small js-procedure" value="" placeholder="">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-label">
                        施工单号：
                    </div>
                    <div class="form-item">
                        <input type="text" name="workOrderSn" class="yqx-input yqx-input-small" value="" placeholder="">
                    </div>
                </div>

                <div class="show-grid">
                    <div class="form-label">
                        车牌号：
                    </div>
                    <div class="form-item">
                        <input type="text" name="carLicense" class="yqx-input yqx-input-small" value="" placeholder="">
                    </div>
                    <div class="form-label">
                        工单号：
                    </div>
                    <div class="form-item">
                        <input type="text" name="orderSn" class="yqx-input yqx-input-small" value="" placeholder="">
                    </div>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn fr">查询</button>
                </div>
            </div>
            <div class="table-box" id="breakTable">

            </div>
            <!-- 分页容器 start -->
            <div class="yqx-page" id="breakPaging"></div>
            <!-- 分页容器 end -->
        </div>

    </div>
</div>


<!--中断管理列表-->
<script type="text/template" id="breakTableTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>序号</th>
            <th>施工单号</th>
            <#--<th>工单号</th>-->
            <th>车牌号</th>
            <th>工序</th>
            <#--<th>施工人员</th>-->
            <th>中断原因</th>
            <th>中断时间</th>
            <#--<th>恢复时间</th>-->
            <#--<th>中断总时长（分钟）</th>-->
            <th>操作</th>
        </tr>
        </thead>
        <%if(json.success && json.data && json.data.content){%>
        <%for(var i=0; i<json.data.content.length;i++){%>
        <%var item = json.data.content[i]%>
        <tr data-id="<%=item.id%>">
            <td><%=i+1%></td>
            <td>
                <a href="javascript:;" class="order-number js-order-number"><%=item.workOrderSn%></a>
            </td>
            <#--<td><%=item.orderSn%></td>-->
            <td><%=item.carLicense%></td>
            <%var subItem =item.workOrderProcessRelDTO%>
            <input type="hidden" value="<%=subItem.id%>" id="listId">
            <input type="hidden" value="<%=subItem.workOrderId%>" id="workOrderId">
            <input type="hidden" value="<%=subItem.processId%>" id="processId">
            <td><%=subItem.processName%></td>
            <#--<td><%=subItem.realOperator%></td>-->
            <td><%=subItem.breakReason%></td>
            <td><%=subItem.breakStartTimeStr%></td>
            <#--<td><%=subItem.breakEndTimeStr%></td>-->
            <#--<td>-->
                <#--<%var a = Date(subItem.currentTimeStr) - Date(subItem.breakStartTimeStr)%>-->
                <#--<%=a/1000/60%>-->
            <#--</td>-->
            <td>
                <a href="javascript:;" class="recovery-btn js-recovery">施工恢复</a>
                <a href="javascript:;" class="recovery-btn js-finished">提前完工</a>
                <a href="javascript:;" class="order-number js-detail">查看详情</a>
            </td>

        </tr>
        <%}}%>
    </table>
</script>


<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/workshop/workorder-break-list.js?364c4a0006f78d2c0e1722463fee4a63"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/workOrderBreakList.css?0c1c6e356f7716dba67e95fbc847f110"/>
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
            <h3 class="headline fl">中断管理<small>-中断车辆详情</small></h3>
        </div>
        <div class="content">
            <div class="top-contact">
                <div class="form-label">
                    施工单号：
                </div>
                <div class="form-item">
                    <div class="yqx-text">
                        <input type="hidden" value="${workOrder.id}" class="order-id">
                        <a href="javascript:;" class="order-num js-order-num">${workOrder.workOrderSn}</a>
                    </div>
                </div>
                <div class="form-label">
                    工单号：
                </div>
                <div class="form-item">
                    <div class="yqx-text">
                    ${workOrder.orderSn}
                    </div>
                </div>
                <div class="form-label">
                    车牌号：
                </div>
                <div class="form-item">
                    <div class="yqx-text">
                    ${workOrder.carLicense}
                    </div>
                </div>
            </div>

            <div class="table-content">
                <input type="hidden" id="work-order-id" value="${workOrder.workOrderProcessRelDTO.workOrderId}">
                <input type="hidden" id="process-id" value="${workOrder.workOrderProcessRelDTO.processId}">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>中断时间</th>
                        <th>中断工序</th>
                        <th>施工人员</th>
                        <th>中断原因</th>
                        <th>中断时长</th>
                        <th>恢复时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${workOrder.workOrderProcessRelDTO.breakStartTimeStr}</td>
                        <td>${workOrder.workOrderProcessRelDTO.processName}</td>
                        <td>${workOrder.workOrderProcessRelDTO.realOperator}</td>
                        <td>${workOrder.workOrderProcessRelDTO.breakReason}</td>
                        <td>${workOrder.workOrderProcessRelDTO.totalBreakTimeStr}</td>
                        <td>${workOrder.workOrderProcessRelDTO.breakEndTimeStr}</td>
                    </tr>
                    </tbody>
                </table>
                <div class="btn-box">
                    <button class="yqx-btn yqx-btn-2 yqx-btn-small js-recovery">恢复施工</button>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small js-finished">提前完工</button>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small js-return fr">返回</button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/workshop/workOrderBreakListDtl.js?8f4b6f2f1880a39b099ddbc5b0739ce9"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
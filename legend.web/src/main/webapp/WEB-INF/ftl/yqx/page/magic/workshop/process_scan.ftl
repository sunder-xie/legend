<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet"
      href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet"
      href="${BASE_PATH}/static/css/page/magic/workshop/process_scan.css?62de7290f4db36900dd342fe29ad0695"/>
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
            <h3 class="headline fl">施工作业扫描</h3>
        </div>
        <div class="scan-area">
            <div class="step1">
                <div class="step1-head">
                    <h4 class="headline"> 注意：</h4>

                    <p class="form-label-large"> 1.施工扫描，请扫描施工单条码<br/>2.个人待修车辆查询，请扫描个人工牌条码</p>
                </div>

                <div class="form-label">
                    请扫描施工单条码：
                </div>
                <div class="form-item">
                    <input type="text" name="order" class="yqx-input yqx-input-small keydown" value="">
                </div>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-scan-order">扫描</button>
            </div>
            <div class="step2">
                <div class="scan-result">
                    <div>扫描结果：
                        <span class="order-result"></span>
                    </div>
                </div>
                <div class="step2-scan">
                    <div class="form-label">
                        请扫描工牌条码：
                    </div>
                    <div class="form-item">
                        <input type="text" name="workcard" class="yqx-input yqx-input-small keydown" value="">
                    </div>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-scan-card">扫描</button>
                </div>
            </div>
            <div class="step3">
                <div class="scan-result1">
                    <div>扫描结果：<span class="card-result"></span>
                    </div>
                </div>
                <div class="step3-scan">
                    <div class="form-label">
                        请扫描工序条码：
                    </div>
                    <div class="form-item">
                        <input type="text" name="process" class="yqx-input yqx-input-small keydown" value="">
                    </div>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-scan-process">扫描</button>
                </div>
            </div>
            <div class="step4">
                <div class="scan-result2">
                    <div>扫描结果：<span class="process-result"></span></div>
                </div>
            </div>
            <div class="step5 ">
                <div class="scan-result3">
                    <div>扫描结果：<span class="worker-result"></span></div>
                </div>
                <div class="workerInfo">
                    <h5 class="workInfo-head headline">当前员工：<span class="margin-r workerName">施某某</span>工牌号：<span
                            class="cardName"></span></h5>
                    <table class="yqx-table" id="workerResult">
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script id="workerResultTpl" type="text/template">
    <thead>
    <tr>
        <th>待修车辆</th>
        <th>生产线</th>
        <th>工序</th>
        <th>计划开始时间</th>
        <th>计划完工时间</th>
    </tr>
    </thead>
    <%if (json.success&&json.data&&json.data.workOrderVoDTOList&&json.data.workOrderVoDTOList.length > 0){%>
    <%for(var i = 0;i < json.data.workOrderVoDTOList.length;i ++){%>
    <tr>
        <td><%=json.data.workOrderVoDTOList[i].carLicense%></td>
        <td><%=json.data.workOrderVoDTOList[i].lineName%></td>
        <td><%=json.data.workOrderVoDTOList[i].processName%></td>
        <td><%=json.data.workOrderVoDTOList[i].planStartTimeStr%></td>
        <td><%=json.data.workOrderVoDTOList[i].planEndTimeStr%></td>
    </tr>
    <%}}%>
</script>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/magic/workshop/workorder-scan.js?5dec4a72ddf181754b3e51b55a0ab0d6"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
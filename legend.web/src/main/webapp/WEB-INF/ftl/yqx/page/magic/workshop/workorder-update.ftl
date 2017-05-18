<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"
      xmlns="http://xmlns.jcp.org/jsf/html"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/worker-update.css?3dbf2bb95c70815288020e81059d9068"/>
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
            <h3 class="headline fl">施工单编辑</h3>
        </div>
        <input type="hidden" value="${workOrder.id}" class="workOrder-id">
        <input type="hidden" value="${workOrder.shopId}" class="shopId">
        <!--表单内容 start-->
        <div class="form-box">
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label">
                        车牌：
                    </div>
                    <div class="form-item carLicense">${workOrder.carLicense}</div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        车辆型号：
                    </div>
                    <div class="form-item carInfo">${workOrder.carInfo}</div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        年款排量：
                    </div>
                    <div class="form-item yearPower">${orderInfo.carYear} <#if orderInfo.carGearBox>${orderInfo.carGearBox}<#else>${orderInfo.carPower}</#if></div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label">
                        颜色：
                    </div>
                    <div class="form-item carColor">${workOrder.carColor}</div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        VIN码：
                    </div>
                    <div class="form-item vin">${orderInfo.vin}</div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        期望交车时间：
                    </div>
                    <div class="form-item expectTime">${orderInfo.expectTimeStr}</div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label">
                        委托方联系人：
                    </div>
                    <div class="form-item contactName">${proxy.contactName}</div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        委托方联系电话：
                    </div>
                    <div class="form-item contactMobile">${proxy.contactMobile}</div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        委托方联系地址：
                    </div>
                    <div class="form-item addressName"><#--${proxyInfo.addressName}--></div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label">
                        服务顾问：
                    </div>
                    <div class="form-item serviceSa">${orderInfo.serviceSa}</div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        备注：
                    </div>
                    <div class="form-item postscript">${orderInfo.postscript}</div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label">
                        渠道：
                    </div>
                    <div class="form-item channelName">${orderInfo.channelName}</div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        委托单：
                    </div>
                    <div class="form-item proxySn">${proxy.proxySn}</div>
                </div>
            </div>
        </div>
        <!--表单内容 end-->
        <div class="table-box">
            <h3>事故线</h3>
            <input type="hidden" value="${workOrder.lineId}" class="lineId">
            <div id="tableCon">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>工序</th>
                        <th>工时（分钟）</th>
                        <th>班组</th>
                        <th>预计开始时间</th>
                        <th>预计完工时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if processManagerRelVoList??>
                    <#list processManagerRelVoList as processManagerRelVo>
                    <tr class="accident-list">
                        <input type="hidden" class="processId" value="${processManagerRelVo.processId}">
                        <input type="hidden" class="teamId" value="${processManagerRelVo.teamId}">
                        <input type="hidden" class="operatorId" value="${processManagerRelVo.operatorId}">
                        <input type="hidden" class="processManagerId" value="${processManagerRelVo.id}">
                        <input type="hidden" class="processSort" value="${processManagerRelVo.processSort}">
                        <td class="processName">${processManagerRelVo.processName}</td>
                        <td>
                            <div class="form-item">
                                <input type="text" name="" class="yqx-input yqx-input-small workTime js-calculation"  value="${processManagerRelVo.workTime}" placeholder="">
                            </div>
                        </td>
                        <td>
                            <div class="form-item">
                                <input type="text" name="" class="yqx-input yqx-input-small teamName" value="${processManagerRelVo.teamName}" placeholder="" disabled="">
                            </div>
                        </td>
                        <td class="planStartTime">${processManagerRelVo.startTimeStr}</td>
                        <td class="planEndTime">${processManagerRelVo.endTimeStr}</td>
                    </tr>
                    </#list>
                    </#if>
                    </tbody>
                </table>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-save">保存</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
            </div>
        </div>
    </div>
</div>

<script type="text/html" id="tableTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>工序</th>
            <th>工时（分钟）</th>
            <th>班组</th>
            <th>预计开始时间</th>
            <th>预计完工时间</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.data){%>
        <%for(var i=0; i<json.data.length;i++){%>
        <%var item = json.data[i]%>
            <tr class="accident-list">
                <input type="hidden" class="processId" value="<%=item.processId%>">
                <input type="hidden" class="teamId" value="<%=item.teamId%>">
                <input type="hidden" class="processManagerId" value="<%=item.id%>">
                <input type="hidden" class="operatorId" value="<%=item.operatorId%>">
                <input type="hidden" class="processSort" value="<%=item.processSort%>">
                <td class="processName"><%=item.processName%></td>
                <td>
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-small workTime js-calculation" value="<%=item.workTime%>" placeholder="">
                    </div>
                </td>
                <td>
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-small teamName" value="<%=item.teamName%>" placeholder="" disabled="">
                    </div>
                </td>
                <td class="planStartTime"><%=item.startTimeStr%></td>
                <td class="planEndTime"><%=item.endTimeStr%></td>
            </tr>
        <%}}%>
        </tbody>
    </table>
</script>





<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/workshop/workorder-update.js?a1cf68728ffac935d7538091bad1cd81"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
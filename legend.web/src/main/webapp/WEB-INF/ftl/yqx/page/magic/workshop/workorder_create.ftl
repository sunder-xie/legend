<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/workorder-create.css?62fc838d81546d76526638f9e266085c"/>
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
            <h3 class="headline fl">维修工单派工</h3>
        </div>

        <input type="hidden" value="${orderInfo.shopId}" class="shopId">
        <input type="hidden" value="${orderInfo.id}" class="orderId">
        <input type="hidden" value="${orderInfo.orderSn}" class="orderSn">
        <input type="hidden" value="${orderInfo.customerCarId}" class="customerCarId">

        <!--表单内容 start-->
        <div class="form-box">
            <div class="show-grid">
                <div class="form-label font-weight">
                    工单编号：
                </div>
                <div class="form-item time-width font-weight">${orderInfo.orderSn}<span>（开单时间：${orderInfo.createTimeStr}）</span></div>
            </div>
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label">
                        车牌：
                    </div>
                    <div class="form-item carLicense">${orderInfo.carLicense}</div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        车辆型号：
                    </div>
                    <div class="form-item carInfo">${orderInfo.carInfo}</div>
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
                    <div class="form-item carColor">${orderInfo.carColor}</div>
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
                    <div class="form-item contactName">${proxyInfo.contactName}</div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        委托方联系电话：
                    </div>
                    <div class="form-item contactMobile">${proxyInfo.contactMobile}</div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        委托方联系地址：
                    </div>
                    <div class="form-item addressName">${proxyInfo.proxyAddress}</div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label">
                        服务顾问：
                    </div>
                    <div class="form-item serviceSa">${orderInfo.receiverName}</div>
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
                    <div class="form-item proxySn">${proxyInfo.proxySn}</div>
                </div>
            </div>
        </div>
        <!--表单内容 end-->

        <!--tab start-->
        <div class="tab-box">
            <h3>请选择流水线</h3>
            <div class="tab" id="tabCon">

            </div>
            <div class="tabcon" id="tableCon">

            </div>
            <div class="work-btn js-work-btn">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-dispat">自动派工</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-revoke">撤销派工</button>
            </div>
            <div class="dispatching" id="dispatchingCon">

            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-save－type">保存</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
            </div>
        </div>
        <!--tab end-->
    </div>
</div>


<!---tab模板-->
<script type="text/html" id="tabTpl">
    <%if(json.data){%>
    <%for(var i=0; i<json.data.length;i++){%>
    <%var item = json.data[i]%>
        <div class="tab-item" data-type="<%=item.type%>" data-line-id="<%=item.id%>" data-line-name="<%=item.name%>"><%=item.name%></div>
    <%}}%>
</script>

<!--快修线-->
<script type="text/html" id="repair">
    <table class="yqx-table">
        <thead>
        <tr>
            <th rowspan="2">流水线名称</th>
            <th rowspan="2">等待数量</th>
            <th rowspan="2">工作中数量</th>
            <th colspan="4">接车台次</th>
            <th colspan="4">负载率</th>
            <th rowspan="2">最后车辆完工时间</th>
            <th rowspan="2">预计交车时间</th>
        </tr>
        <tr>
            <th>前天</th>
            <th>昨天</th>
            <th>今天</th>
            <th>当月</th>
            <th>前天</th>
            <th>昨天</th>
            <th>今天</th>
            <th>当月</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.data){%>
        <tr>
            <td><%=json.data.lineName%></td>
            <td><%=json.data.waitNumber%></td>
            <td><%=json.data.workNumber%></td>
            <td><%=json.data.rnumberByesterday%></td>
            <td><%=json.data.rnumberYesterday%></td>
            <td><%=json.data.rnumberToday%></td>
            <td><%=json.data.rnumberMonth%></td>
            <td><%=json.data.bnumberByesterday%>%</td>
            <td><%=json.data.bnumberYesterday%>%</td>
            <td><%=json.data.bnumberToday%>%</td>
            <td><%=json.data.bnumberMonth%>%</td>
            <td><%=json.data.lastTimeStr%></td>
            <td><%=json.data.planTimeStr%></td>
        </tr>
        <%}%>
        </tbody>
    </table>
</div>
</script>


<!--快修线自动派工模板-->
<script type="text/html" id="dispatchingTpl">
        <table class="yqx-table">
            <thead>
            <tr>
                <th width="13%">工序</th>
                <th width="13%">工时（分钟）</th>
                <th>班组</th>
                <th>技师</th>
                <th>预计开始时间</th>
                <th>预计完工时间</th>
            </tr>
            </thead>
            <tbody>
            <%if(json.data){%>
            <%for(var i=0; i<json.data.length;i++){%>
            <%var item = json.data[i]%>
            <tr class="repair-list">
                <input type="hidden" value="<%=item.processId%>" class="processId">
                <input type="hidden" value="<%=item.processSort%>" class="processSort">
                <input type="hidden" value="<%=item.teamId%>" class="teamId">
                <input type="hidden" value="<%=item.operatorId%>" class="operatorId">

                <td class="processName" width="13%"><%=item.processName%></td>
                <td width="13%">
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-small workTime js-repair-workTiem" value="<%=item.workTime%>" placeholder="" data-v-type="integer">
                    </div>
                </td>
                <td>
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-small teamName input-width" value="<%=item.teamName%>" placeholder="" disabled="" >
                    </div>
                </td>
                <td>
                    <div class="form-item">
                        <input type="text" name=""  class="yqx-input yqx-input-small operator input-width" value="<%=item.operator%>" placeholder="" disabled="">
                    </div>
                </td>
                <td class="planStartTime"><%=item.startTimeStr%></td>
                <td class="planEndTime"><%=item.endTimeStr%></td>
            </tr>
            <%}}%>
            </tbody>
        </table>
</script>

<!--事故线自动派工模板-->
<script type="text/html" id="accidentTpl">
    <table class="yqx-table accident-line">
        <thead>
        <tr>
            <th>工序</th>
            <th>工时（分钟）</th>
            <th>班组</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.data){%>
        <%for(var i=0; i<json.data.length;i++){%>
        <%var item = json.data[i]%>
        <tr class="accident-list">
            <input type="hidden" value="<%=item.processId%>" class="processId">
            <input type="hidden" value="<%=item.processSort%>" class="processSort">
            <td class="processName"><%=item.processName%></td>
            <td>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-small workTime" value="<%=item.workTime%>" placeholder="" data-v-type="integer">
                </div>
            </td>
            <%if(i == 0){%>
            <td>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-small teamName js-team" value="" placeholder="">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </td>
            <%}else{%>
            <td>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-small teamName js-team other-team" value="" placeholder="" disabled>
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </td>
            <%}%>
        </tr>
        <%}}%>
        </tbody>
    </table>
</script>


<!--快喷线自动派工模板-->
<script type="text/html" id="jetTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>工序</th>
            <th>工时（分钟）</th>
            <th>班组</th>
            <th>技师</th>
            <th>预计开始时间</th>
            <th>预计完工时间</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.data){%>
        <%for(var i=0; i<json.data.length;i++){%>
        <%var item = json.data[i]%>
        <tr class="jet-list">
            <input type="hidden" value="<%=item.processId%>" class="processId">
            <input type="hidden" value="<%=item.processSort%>" class="processSort">
            <input type="hidden" value="<%=item.id%>" class="lineProcessId">
            <td class="processName"><%=item.processName%></td>
            <td>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-small workTime js-workTime" value="<%=item.workTime%>" placeholder="" data-v-type="integer">
                </div>
            </td>
            <td>
                <div class="form-item">
                    <input type="hidden" val="<%=item.teamId%>" class="teamId-p"/>
                    <input type="text" name="" class="yqx-input yqx-input-small js-team teamName-p" value="<%=item.teamName%>" placeholder="" >
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </td>
            <td>
                <div class="form-item form-opereator">
                    <input type="hidden" value="<%=item.operatorId%>" class="operatorId-p"/>
                    <input type="text" name=""  class="yqx-input yqx-input-small operator js-operator operatorName-p" value="<%=item.operator%>" placeholder="">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </td>
            <td class="planStartTime "><%=item.startTimeStr%></td>
            <td class="planEndTime"><%=item.endTimeStr%></td>
        </tr>
        <%}}%>
        </tbody>
    </table>
</script>


<!--小钣金事故线自动派工模板-->
<script type="text/html" id="amallAccidentTpl">
    <table class="yqx-table accident-line">
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
        <tr class="small-accident-list">
            <input type="hidden" value="<%=item.processId%>" class="processId">
            <input type="hidden" value="<%=item.processName%>" class="processName">
            <input type="hidden" value="<%=item.processSort%>" class="processSort">
            <input type="hidden" value="" class="teamId">
            <input type="hidden" value="<%=item.lineId%>" class="lineId">
            <td class="processName"><%=item.processName%></td>
            <td>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-small workTime js-work-tiem" value="<%=item.workTime%>" placeholder="" data-v-type="integer">
                </div>
            </td>
            <%if(i == 0){%>
            <td>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-small teamName js-team js-malfunction-team" value="" placeholder="">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </td>
            <%}else{%>
            <td>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-small teamName js-team other-team" value="" placeholder="" disabled>
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </td>
            <%}%>
            <td class="planStartTime "><%=item.startTimeStr%></td>
            <td class="planEndTime"><%=item.endTimeStr%></td>
        </tr>
        <%}}%>
        </tbody>
    </table>
</script>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/workshop/workorder-create.js?82c99023e18f5d84277d46fa019c4f7e"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/workorder-detail.css?0836bcec9a57befe48ba6fd8da3cb683"/>
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
            -施工单详情页
        </div>
        <input type="hidden" value="${workOrder.id}" class="work-id">
        <div class="order-detail">
            <div class="order-info">
                <#if workOrder.status=="DSG">
                    <div class="status-image image-dsg"></div>
                </#if>
                <#if workOrder.status=="SGZ">
                    <div class="status-image image-sgz"></div>
                </#if>
                <#if workOrder.status=="SGZD">
                    <div class="status-image image-sgzd"></div>
                </#if>
                <#if workOrder.status=="YWG">
                    <div class="status-image image-ywg"></div>
                </#if>
                <div>
                    <div class="order-number inline"><b>施工单编号：${workOrder.workOrderSn}</b></div>
                    <div class="time inline">（开单时间：${workOrder.createStr} ）</div>
                    <div class="order-number inline"><b>工单编号：${workOrder.orderSn}</b></div>
                    <div class="time inline">（开单时间：${orderInfo.createTimeStr} )</div>
                </div>
                <div>
                    <div class="license inline w245"><div class="form-label">车牌：</div><div class="form-item">${workOrder.carLicense}</div></div>
                    <div class="type inline w245"><div class="form-label">车辆型号：</div><div class="form-item js-show-tips ellipsis-1 w160">${workOrder.carInfo}</div></div>
                    <div class="displacement inline w245"><div class="form-label">年款排量：</div><div class="form-item">${orderInfo.carYear} <#if orderInfo.carGearBox>${orderInfo.carGearBox}<#else>${orderInfo.carPower}</#if></div></div>
                </div>
                <div>
                    <div class="consultant inline w245"><div class="form-label">颜色：</div><div class="form-item">${workOrder.carColor}</div></div>
                    <div class="contact inline w245"><div class="form-label">VIN码：</div><div class="form-item">${orderInfo.vin}</div></div>
                    <div class="telephone inline w245"><div class="form-label">期望交车时间：</div><div class="form-item">${orderInfo.expectTimeStr}</div></div>
                </div>
                <div>
                    <div class="begintime inline w245"><div class="form-label">委托方联系人：</div><div class="form-item">${proxy.contactName}</div></div>
                    <div class="endtime inline w245"><div class="form-label">计划完工时间：</div><div class="form-item">${workOrder.planEndTimeStr}  </div></div>
                    <div class="team inline w245"><div class="form-label">委托方联系电话：</div><div class="form-item">${proxy.contactMobile}</div></div>
                </div>
                <div>
                    <div class="begintime inline w245"><div class="form-label">委托方联系地址：</div><div class="form-item js-show-tips ellipsis-1 w130">${proxy.proxyAddress}</div></div>
                    <div class="endtime inline w245"><div class="form-label">服务顾问： </div><div class="form-item">${orderInfo.serviceName} </div></div>
                    <div class="team inline w245"><div class="form-label">备注：</div><div class="form-item js-show-tips ellipsis-1 w160">${orderInfo.postscript}</div></div>
                </div>
            </div>
            <#if type == 1 || type == 2>
                <div class="replacement-box">
                    <h3>配件准备</h3>
                    <ul>
                        <li>${workOrder.accessoriesPrepareStr}</li>
                    </ul>
                </div>
            </#if>
            <div class="replacement-box">
                <h3>面漆准备</h3>
                <ul>
                    <li>${workOrder.paintPrepareStr}</li>
                </ul>
            </div>
            <div class="project-box">
                <h3 class="project-title">服务项目</h3>
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>服务名称</th>
                        <th>服务类别</th>
                        <th>工时费</th>
                        <th>工时</th>
                        <th>金额</th>
                        <th>服务备注</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if orderServiceses ??>
                    <#list orderServiceses as orderServices>
                    <tr>
                        <td class="js-show-tips ellipsis-1">${orderServices.serviceName}</td>
                        <td class="js-show-tips ellipsis-1">${orderServices.serviceCatName}</td>
                        <td>${orderServices.servicePrice}</td>
                        <td>${orderServices.serviceHour}</td>
                        <td>${orderServices.serviceAmount}</td>
                        <td>
                            <div class="max-text js-show-tips">${orderServices.serviceNote}</div>
                        </td>
                    </tr>
                    </#list>
                    </#if>
                    </tbody>
                </table>

                <h3 class="project-title">配件项目</h3>
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>零件号</th>
                        <th>配件名称</th>
                        <th>售价</th>
                        <th>数量</th>
                        <th>金额</th>
                        <th>服务备注</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if orderGoodses ??>
                    <#list orderGoodses as orderGoods>
                    <tr>
                        <td class="js-show-tips ellipsis-1">${orderGoods.goodsFormat}</td>
                        <td class="js-show-tips ellipsis-1">${orderGoods.goodsName}</td>
                        <td>${orderGoods.goodsPrice}</td>
                        <td>${orderGoods.goodsNumber}</td>
                        <td>${orderGoods.goodsAmount}</td>
                        <td>
                            <div class="max-text js-show-tips">${orderGoods.goodsNote}</div>
                        </td>
                    </tr>
                    </#list>
                    </#if>
                    </tbody>
                </table>
                <div class="cost-box">
                    <div class="cost">服务费用:<span class="money-font">&yen;<#if orderInfo??>${(orderInfo.serviceAmount - orderInfo.serviceDiscount)?string(",###.##")}</#if>
                    </span> + 配件费用:<span class="money-font">&yen;<#if orderInfo??>${(orderInfo.goodsAmount - orderInfo.goodsDiscount)?string(",###.##")}</#if></div>
                    <div class="cost">总计:<span class="money-font">&yen;<#if orderInfo??>${orderInfo.orderAmount?string(",###.##")}</#if></span></div>
                </div>

                <h3 class="project-title">
                    <#if workOrder??>
                        ${workOrder.lineName}
                    </#if>
                </h3>
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th width="5%">工序</th>
                        <th width="10%">工时（分钟）</th>
                        <th width="5%">班组</th>
                        <#if type == 2>

                        <#else>
                            <th>计划安排技师</th>
                        </#if>
                        <th>实际施工技师</th>
                        <th>计划开始时间</th>
                        <th>实际开始时间</th>
                        <th>计划完工时间</th>
                        <th>实际完工时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if workOrder??>
                    <#if workOrder.workOrderProcessRelDTOList??>
                    <input type="hidden" value="${flag1}" class="work-order-status">
                    <#list workOrder.workOrderProcessRelDTOList as workOrderProcessRel>
                    <tr>
                        <td>${workOrderProcessRel.processName}</td>
                        <td>${workOrderProcessRel.workTime}</td>
                        <td class="js-show-tips ellipsis-1">${workOrderProcessRel.teamName}</td>

                        <#if type == 2>

                        <#else>
                            <td>${workOrderProcessRel.operator}</td>
                        </#if>
                        <td>${workOrderProcessRel.realOperator}</td>
                        <td>${workOrderProcessRel.planStartTimeStr}</td>
                        <td>${workOrderProcessRel.startTimeStr}</td>
                        <td>${workOrderProcessRel.planEndTimeStr}</td>
                        <td>${workOrderProcessRel.endTimeStr}</td>
                    </tr>
                    </#list>
                    </#if>
                    </#if>
                    </tbody>
                </table>
            </div>
            <div class="button clearfix">
                <#if type == 1 || type == 2>
                    <button class="yqx-btn yqx-btn-1 fl mr10 js-replacementPrepare">配件准备</button>
                </#if>
                <#if workOrder.status=='SGZ'>
                    <button class="yqx-btn yqx-btn-1 fl mr10 js-break">中断</button>
                </#if>
                <button class="yqx-btn yqx-btn-1 fl js-prepare">面漆准备</button>
                <#if type == 2>
                    <#if flag == 'SGZ' || flag == 'SGWC'>
                    <#else>
                    <button class="yqx-btn yqx-btn-1 fr mr10 js-schedule">排工</button>
                    </#if>
                </#if>
                <button class="yqx-btn yqx-btn-1 fr mr10 js-print">打印</button>
                <button class="yqx-btn yqx-btn-1 fr mr10" onclick="history.go(-1)">返回</button>
            </div>
        </div>
    </div>
</div>

<!--施工中断弹窗-->
<script type="text/html" id="recoveryDialog">
    <div class="dialog">
        <div class="dialog-title">施工中断</div>
        <div class="dialog-con">
            <input type="hidden" name="workOrderId">
            <input type="hidden" name="processId">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>施工单号</th>
                    <th>中断工序</th>
                    <th>施工人员</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="work-num">${workOrder.workOrderSn}</td>
                    <td class="break-process"></td>
                    <td class="operator"></td>
                </tr>
                </tbody>
            </table>
            <div class="break-reason">
                <div class="form-label">
                    中断原因：
                </div>
                <div class="form-item">
                    <input type="hidden" name="breakReason">
                    <input type="text" class= "yqx-input yqx-input-icon js-reason"  data-v-type="required">
                    <span class="fa icon-angle-down"></span>
                </div>
                <div class="form-label">
                    备注：
                </div>
                <div class="form-item">
                    <input type="text" name="remark" class="yqx-input remark js-show-tips ellipsis-1" data-v-type="required">
                </div>
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small dialog-break">中断</button>
        </div>
    </div>
</script>

<#--面漆准备弹框-->
<script type="text/html" id="paintPrepare">
    <div class="dialog">
        <div class="dialog-title">面漆准备</div>
        <div class="dialog-con">
            <div class="choose">
                <input type="radio" name="status" value="1" checked>准备就绪</input>
                <input type="radio" name="status" value="0">尚未就绪</input>
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-paint-submit">提交</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-paint-cancel">取消</button>
        </div>
    </div>
</script>

<#--备件准备弹框-->
<script type="text/html" id="replacementPrepare">
    <div class="dialog">
        <div class="dialog-title">配件准备</div>
        <div class="dialog-con">
            <div class="choose">
                <input type="radio" name="status1" value="1" checked>准备就绪</input>
                <input type="radio" name="status1" value="2">部分就绪</input>
                <input type="radio" name="status1" value="0">尚未就绪</input>
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-replace-submit">提交</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-replace-cancel">取消</button>
        </div>
    </div>
</script>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/workshop/workorder-detail.js?92dd4a013f7139f7ce9f1ec162a4dcb4"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
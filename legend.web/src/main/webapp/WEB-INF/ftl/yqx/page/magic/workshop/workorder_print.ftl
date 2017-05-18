<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>维修施工单</title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/base.css?8bb0f9a84cfb070249dcc1186f9f6a25"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print_common.css?e174653fe93fff7de684a409310c97d7"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/workorder_print.css?3e441b407582f59ee9a4154f164b7905"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/order_print_blank.css?f9cc77af03e07dee8e01b589d4a298f8"/>
    <script src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script>
        window.onload = function () {
            window.print();
        };
    </script>
</head>
<body>
<div class="w980">
    <h2 class="text-center">维修施工单</h2>

    <div class="order-info order-print-box clearfix">
        <div class="fl">
            <div class="clearfix">
                <div class="fl bold w300">施工单号：${workOrderVo.workOrderSn}</div>
                <div class="fl bold w300">工单号：${workOrderVo.orderSn}</div>
            </div>
            <div class="bold w300">开单日期：${workOrderVo.gmtCreateStr}</div>
        </div>
        <img class="fr print-code" src="${BASE_PATH}/pub/createImg?text=${workOrderVo.workOrderSn}&width=394&height=97"></img>
    </div>

    <div class="info">
        <table class="info-table">
            <tr>
                <td width="245px">车主：${workOrderVo.contactName}</td>
                <td width="245px">联系电话：${workOrderVo.contactMobile}</td>
                <td width="245px">计划开工时间：${workOrderVo.planStartTimeStr}</td>
                <td width="245px">服务顾问：${workOrderVo.serviceSa}</td>
            </tr>
            <tr>
                <td width="245px">车牌号：${workOrderVo.carLicense}</td>
                <td width="245px">车型：${workOrderVo.carInfo}</td>
                <td width="245px">年款排量：${workOrderVo.carYearGearBox}</td>
                <td></td>
            </tr>
            <tr>
                <td width="245px">车辆颜色：${workOrderVo.carColor}</td>
                <td width="245px">Vin码：${workOrderVo.vin}</td>
                <td colspan="2">计划完工时间：${workOrderVo.planEndTimeStr}</td>
            </tr>
            <tr>
                <td colspan="4">生产线：${workOrderVo.lineName}</td>
            </tr>
        </table>

        <div class="padding">
            <#include "yqx/tpl/order/static-precheck-tpl.ftl">
        </div>

        <div class="project-box">
            <table>
                <thead>
                <tr>
                    <th>工序</th>
                    <th>工时（分钟）</th>
                    <th>计划开始时间</th>
                    <th>计划结束时间</th>
                    <th>班组</th>
                    <th>技师</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody>
                <#if workOrderProcessRelVoList ??>
                    <#list workOrderProcessRelVoList as workOrderProcessRel>
                    <tr>
                        <td>${workOrderProcessRel.processName}</td>
                        <td>${workOrderProcessRel.workTime}</td>
                        <td>${workOrderProcessRel.planStartTimeStr}</td>
                        <td>${workOrderProcessRel.planEndTimeStr}</td>
                        <td>${workOrderProcessRel.teamName}</td>
                        <td>${workOrderProcessRel.operator}</td>
                        <td></td>
                    </tr>
                    </#list>
                </#if>
                </tbody>
            </table>

            <h3 class="project-title">服务项目</h3>
            <table>
                <thead>
                <tr>
                    <th width="20%">服务名称</th>
                    <th width="30%">服务类别</th>
                    <th width="10%">工时</th>
                    <th width="40%">服务备注</th>
                </tr>
                </thead>
                <tbody>
                <#if orderServiceses ??>
                    <#list orderServiceses as orderServices>
                    <tr>
                        <td>${orderServices.serviceName}</td>
                        <td>${orderServices.serviceCatName}</td>
                        <td>${orderServices.serviceHour}</td>
                        <td>
                            <div class="max-text js-show-tips">${orderServices.serviceNote}</div>
                        </td>
                    </tr>
                    </#list>
                </#if>
                </tbody>
            </table>

            <h3 class="project-title">配件项目</h3>
            <table>
                <thead>
                <tr>
                    <th width="20%">零件号</th>
                    <th width="30%">配件名称</th>
                    <th width="10%">数量</th>
                    <th width="40%">服务备注</th>
                </tr>
                </thead>
                <tbody>
                <#if orderGoodses ??>
                    <#list orderGoodses as orderGoods>
                    <tr>
                        <td>${orderGoods.goodsFormat}</td>
                        <td>${orderGoods.goodsName}</td>
                        <td>${orderGoods.goodsNumber}</td>
                        <td>
                            <div class="max-text js-show-tips">${orderGoods.goodsNote}</div>
                        </td>
                    </tr>
                    </#list>
                </#if>
                </tbody>
            </table>

            <#if workOrderVo.produceLineType==1>
                <div class="remark-table">
                    <table border="1">
                        <tr class="h80">
                            <td>车间检验备注：</td>
                            <td>维修项目修改记录：</td>
                        </tr>
                        <tr class="h80">
                            <td class="bottom-border" colspan="2">服务顾问检验备注：</td>
                        </tr>
                    </table>
                </div>
            </#if>
            <#if workOrderVo.produceLineType==2||workOrderVo.produceLineType==3>
                <div class="remark-table">
                    <table border="1">
                        <tr class="h80">
                            <td colspan="4">施工备注：</td>
                            <td colspan="3">维修项目修改记录：</td>
                        </tr>
                        <tr class="text-center h25">
                            <td rowspan="2">校验签字</td>
                            <td>钣金</td>
                            <td>做底</td>
                            <td>中涂</td>
                            <td>烤漆</td>
                            <td>抛光</td>
                            <td>组装</td>
                        </tr>
                        <tr class="text-center h25">
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr class="h80">
                            <td class="bottom-border" colspan="4">车间检验备注：</td>
                            <td class="bottom-border" colspan="3">服务顾问检验备注：</td>
                        </tr>
                        <tr class="h80">
                            <td class="bottom-border" colspan="7"></td>
                        </tr>
                    </table>
                </div>
            </#if>
        </div>
    </div>
</div>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title></title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/base.css?8bb0f9a84cfb070249dcc1186f9f6a25"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print_common.css?e174653fe93fff7de684a409310c97d7"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/order_print_blank.css?f9cc77af03e07dee8e01b589d4a298f8"/>
    <script type="text/javascript" src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script>
        $(document).ready(function ($) {
            window.print();
        });
    </script>
</head>
<body>
<div id="hdr">
    <p class="tq_print_logo"><img src="${BASE_PATH}/static/img/common/print/print_logo.png"/></p>

    <p class="blankPage"></p>

    <p class="company">${shop.companyName}<span></span></p>

    <p class="tit">结　算　单 <#if orderInfo.payStatus == 1><span>[已挂账]</span></#if></p>
    <span class="orderID">单号：${orderInfo.orderSn}</span>
    <span class="record_date">打印日期：&nbsp;${.now?string("yyyy-MM-dd HH:mm")}</span>
    <span class="count">
    </span>
</div>

<table class="costomInfoTable" cellspacing="0" cellpadding="0">
    <tr>
        <td class="dInfo">
            <div class="td" style="width:86px;">车　　牌：</div>
            <div class="td" style="width:226px;text-align: center;">${orderInfo.carLicense}</div>
            <div class="td" style="width:85px;">受理日期：</div>
            <div class="td"
                 style="width:149px; text-align: center;"><#if orderInfo.gmtCreate>${orderInfo.gmtCreate?string("yyyy-MM-dd HH:mm")}</#if></div>
            <div class="td" style="width:85px;">结算日期：</div>
            <div class="td"
                 style="width:149px; text-align: center;"><#if orderInfo.payTime && orderInfo.payStatus != 0>${orderInfo.payTime?string("yyyy-MM-dd HH:mm")}</#if></div>
            <div class="td" style="width:85px;">维修类别：</div>
            <div class="td" style="width:104px; text-align: center;">${orderInfo.orderTypeName}</div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:86px;">VIN  码：</div>
            <div class="td" style="width:226px; text-align: center;">${orderInfo.vin}</div>
            <div class="td" style="width:85px;">发动机号：</div>
            <div class="td" style="width:149px; text-align: center;">${orderInfo.engineNo}</div>
            <div class="td" style="width:85px;">车辆颜色：</div>
            <div class="td" style="width:149px; text-align: center;">${orderInfo.carColor}</div>
            <div class="td" style="width:85px;">购车日期：</div>
            <div class="td"
                 style="width:104px; text-align: center;"><#if orderInfo.buyTime>${orderInfo.buyTime?string("yyyy-MM-dd")}</#if></div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:86px;">车辆厂牌：</div>
            <div class="td" style="width:226px;text-align: center;">${orderInfo.carBrand}</div>
            <div class="td" style="width:85px;">车辆型号：</div>
            <div class="td" style="width:149px; text-align: center;">${orderInfo.carInfo}</div>
            <div class="td" style="width:85px;">承保公司：</div>
            <div class="td" style="width:149px; text-align: center;">${orderInfo.insuranceCompanyName}</div>
            <div class="td" style="width:85px;">行驶里程：</div>
            <div class="td" style="width:104px; text-align:center;">${orderInfo.mileage}</div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:86px;">车　　主：</div>
            <div class="td" style="width:226px;text-align: center;">${orderInfo.customerName}</div>
            <div class="td" style="width:85px;">车主地址：</div>
            <div class="td" style="width:385px;">${orderInfo.customerAddress}</div>
            <div class="td" style="width:85px;">进厂方式：</div>
            <div class="td" style="width:104px;"></div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:86px;">联 系 人：</div>
            <div class="td" style="width:226px;text-align: center;">${orderInfo.contactName}</div>
            <div class="td" style="width:85px;">电　　话：</div>
            <div class="td" style="width:149px;">${orderInfo.contactMobile}</div>
            <div class="td" style="width:85px;">手　　机：</div>
            <div class="td" style="width:149px;">${orderInfo.customerMobile}</div>
            <div class="td" style="width:85px;">洗车&nbsp;<span class="_block"></span></div>
            <div class="td" style="width:104px;">旧件带回&nbsp;<span class="_block"></span></div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:86px;">客户单位：</div>
            <div class="td" style="width:226px;display:block;white-space:nowrap">${orderInfo.company}</div>
        </td>
    </tr>
</table>

<#if orderServicesList1>
<table class="orderpp" cellspacing="0" cellpadding="0">
    <caption class="item_tit">维修项目清单</caption>
    <tr class="service-item">
        <th width="35" class="sequence">序号</th>
        <th width="70">工种</th>
        <th width="375">维修内容</th>
        <th width="50">工时</th>
        <th width="80">工时费</th>
        <th width="80" ">优惠</th>
        <th width="80">指派人员</th>
        <th width="80">组长复检</th>
        <th width="80">主管复检</th>
    </tr>
<#list orderServicesList1 as orderServices>
    <tr>
        <td class="sequence">${orderServices_index+1}</td>
        <td class="center">${orderServices.serviceCatName}</td>
        <td>&nbsp;${orderServices.serviceName}<#if orderServices.serviceNote>(${orderServices.serviceNote})</#if></td>
        <td class="center">&nbsp;${orderServices.serviceHour}</td>
        <td class="center">&nbsp;${orderServices.serviceAmount}</td>
        <td class="center">&nbsp;${orderServices.discount}</td>
        <td class="center">&nbsp;${orderServices.workerNames}</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</#list>
    <tr>
        <td colspan="10"><p class="total">工时费合计：<span>${orderInfo.serviceAmount}</span></p>

            <p class="total">优惠合计：${orderInfo.serviceDiscount}<span></span></p>

            <p class="total">客户应付工时费合计：<span>${orderInfo.serviceAmount-orderInfo.serviceDiscount}</span></p></td>
    </tr>
</table>
</#if>

<#if orderGoodsList>
<table class="orderpp" cellspacing="0" cellpadding="0">
    <caption class="item_tit">材料配件清单</caption>
    <tr class="accessories-material">
        <th width="35" class="sequence">序号</th>
    <#--<th class="col_2">零件号</th>-->
        <th width="375">配件名称</th>
        <th width="50">数量</th>
        <th width="50">单位</th>
        <th width="100">单价</th>
        <th width="150">总价</th>
        <th width="100">优惠</th>
    </tr>
<#list orderGoodsList as orderGoods>
    <tr>
        <td class="center">&nbsp;${orderGoods_index+1}</td>
        <td>&nbsp;${orderGoods.goodsName}<#if orderGoods.goodsNote>(${orderGoods.goodsNote})</#if></td>
        <td class="center">&nbsp;${orderGoods.goodsNumber}</td>
        <td class="center">&nbsp;${orderGoods.measureUnit}</td>
        <td class="center">&nbsp;${orderGoods.goodsPrice}</td>
        <td class="center">&nbsp;${orderGoods.goodsAmount}</td>
        <td class="center">&nbsp;${orderGoods.discount}</td>
    </tr>
</#list>
    <tr>
        <td colspan="8"><p class="total">配件费合计：<span>${orderInfo.goodsAmount}</span></p>

            <p class="total">优惠合计：<span>${orderInfo.goodsDiscount}</span></p>

            <p class="total">客户应付配件费合计：<span>${orderInfo.goodsAmount-orderInfo.goodsDiscount}</span></p></td>
    </tr>
</table>
</#if>

<#if orderServicesList2>
<table class="orderpp" cellspacing="0" cellpadding="0">
    <caption class="item_tit">附加费用清单</caption>
    <tr class="extra-costs">
        <th width="35" class="sequence">序号</th>
        <th width="329">附加费用</th>
        <th width="130">金额</th>
        <th width="80">优惠</th>
        <th width="400">费用备注</th>
    </tr>
<#list orderServicesList2 as orderServices>
    <tr class="extra-costs">
        <td class="sequence">${orderServices_index+1}</td>
        <td class="center">&nbsp;${orderServices.serviceName}</td>
        <td class="center">&nbsp;${orderServices.serviceAmount}</td>
        <td class="center">&nbsp;${orderServices.discount}</td>
        <td class="center">&nbsp;${orderServices.serviceNote}</td>
    </tr>
</#list>
    <tr>
        <td colspan="8"><p class="total">附加费合计：<span>${orderInfo.feeAmount}</span></p>

            <p class="total">优惠合计：<span>${orderInfo.feeDiscount}</span></p>

            <p class="total">客户应付附加费合计：<span>${orderInfo.feeAmount-orderInfo.feeDiscount}</span></p></td>
    </tr>
</table>
</#if>

<table class="totalTable" cellspacing="0" cellpadding="0">
    <caption class="item_tit">费用清单</caption>
    <tr>
        <td>&nbsp;工时费：${orderInfo.serviceAmount}<span class="fee"></span></td>
        <td>&nbsp;实收工时费：${orderInfo.serviceAmount-orderInfo.serviceDiscount}<span class="fee"></span></td>
        <td>&nbsp;附加费合计：${orderInfo.feeAmount}<span class="fee"></span>
    </tr>
    <tr>
        <td>&nbsp;材料费合计：${orderInfo.goodsAmount}<span class="fee"></span></td>
        <td>&nbsp;实收材料费：${orderInfo.goodsAmount-orderInfo.goodsDiscount}<span class="fee"></span></td>
        <td>&nbsp;优惠合计：${orderInfo.discount}<span class="fee"></span></td>
    </tr>
<#if debitBill>
    <tr>
        <td colspan="4"><label class="zfy">
            &nbsp;应收金额：=&nbsp;总计： ${orderInfo.orderAmount}元 <#if orderInfo.taxAmount gt 0>+ 费用：${orderInfo.taxAmount}元 </#if>- 工单优惠：${orderInfo.orderDiscountAmount}元
        </label></td>
    </tr>
    <tr>
        <td colspan="4"><label class="zfy">
            &nbsp;应收金额：&nbsp;<span>${receivableAmountUpper}(${debitBill.receivableAmount})</span>
        </label>
        </td>
    </tr>
    <tr>
        <td colspan="4"><label class="zfy">
            &nbsp;实收金额：&nbsp;<span>${paidAmountUpper}(${debitBill.paidAmount})</span>
        </label>
        </td>
    </tr>
<#else>
    <tr>
        <td colspan="4">
            <label class="zfy">
                &nbsp;总计：&nbsp;<span>${orderAmountUpper}(${orderInfo.orderAmount})</span>
            </label>
        </td>
    </tr>
</#if>
<#if debitBillFlowList?exists || orderInfo.signAmount gt 0>
    <tr>
        <td colspan="2"><label class="zfy">
        <#if debitBillFlowList?exists>
            &nbsp;收款明细：&nbsp;&nbsp;<#list debitBillFlowList as item>&nbsp;<#if item.flowStatus == 0><#if item.flowType == 1>冲红</#if>${item.paymentName}<#else>优惠</#if>
            ：${item.payAmount} ;&nbsp;</#list>
        </#if> </label>
        </td>
        <td colspan="2"><label class="zfy">
            <#if orderInfo.signAmount gt 0>&nbsp;挂账金额：&nbsp;&nbsp;${orderInfo.signAmount}&nbsp;</#if></label>
        </td>
    </tr>
</#if>

<#if orderInfo.postscript != null >
    <tr class="t_tr3">
        <td colspan="4">&nbsp;备注：${orderInfo.postscript}</td>
    </tr>
<#else>
    <tr>
        <td colspan="4">&nbsp;备注：</td>
    </tr>
</#if>

</table>
<table class="information" cellspacing="0" cellpadding="0">
    <caption class="item_tit">门店信息</caption>
    <tr>
        <td>&nbsp;门店名称：${shop.name}</td>
        <td>&nbsp;座机电话：${shop.tel}</td>
    </tr>
    <tr>
        <td colspan="2">
            &nbsp;地址：${shop.provinceName}${shop.cityName}${shop.districtName}<#if shop.streetName!="其它">${shop.streetName}</#if>${shop.address}</td>
    </tr>
    <tr class="t_tr4">
        <td colspan="2">
            <div style="padding:10px 0 0 10px;">
                <p>提示：</p>
                <p>${conf.settleComment}</p>
            </div>
        </td>
    </tr>
</table>
<div class="signature">
    <div style="width:200px;">
        <span>下次保养里程：</span>
        <span><#if orderInfo.upkeepMileage>${orderInfo.upkeepMileage}<#else>    </#if> km</span>
    </div>
    <div style="width:390px;">
        <span>服务顾问：</span><span>${orderInfo.receiverName}</span>
    </div>
    <div style="width:390px;">
        <span>客户签字：</span><span class="sign"></span>
        <span class="ymd">年　　月　　日</span>
    </div>
</div>

<div id="ftr">

</div>
</body>
</html>

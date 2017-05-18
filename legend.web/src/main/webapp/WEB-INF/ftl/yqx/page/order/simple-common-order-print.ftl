<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title></title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/base.css?8bb0f9a84cfb070249dcc1186f9f6a25"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print_common.css?e174653fe93fff7de684a409310c97d7"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/order_print_blank.css?f9cc77af03e07dee8e01b589d4a298f8"/>
    <style>
        #hdr {
            overflow: hidden;
        }
        .company {
            margin-bottom: 10px;
        }
        .company small {
            font-size: 18px;
            font-weight: 400;
            vertical-align: middle;
        }
        .orderID {
            padding-right: 0;
        }
        .caption {
            width: 14px;
            padding: 10px;
            vertical-align: middle;
        }
        .signature .fr {
            float: right;
        }
    </style>
    <script>
        window.onload = function () {
            window.print();
        }
    </script>
</head>
<body>
<div id="hdr">
    <p class="tq_print_logo"><img src="${BASE_PATH}/static/img/common/print/print_logo.png"/></p>

    <p class="company">${shop.companyName}<small>(维修工单)</small></p>

    <span class="orderID">单号：${orderInfo.orderSn}<#if workOrder && workOrder.workOrderSn>&nbsp;&nbsp;&nbsp;施工单号：${workOrder.workOrderSn}</#if></span>
    <span class="record_date">打印日期：&nbsp;${.now?string("yyyy-MM-dd HH:mm")}</span>
</div>
<#-- 基本信息 -->
<table class="costomInfoTable" cellspacing="0" cellpadding="0">
    <tr>
        <th width="100" class="text-right">车牌：</th>
        <td width="147">&nbsp;${orderInfo.carLicense}</td>

        <th width="100" class="text-right">车辆厂牌：</th>
        <td width="147">&nbsp;${orderInfo.carBrand}</td>

        <th width="100" class="text-right">车辆型号：</th>
        <td width="147">&nbsp;${orderInfo.carInfo}</td>

        <th width="100" class="text-right">颜色：</th>
        <td width="147">&nbsp;${orderInfo.carColor}</td>
    </tr>
    <tr>
        <th class="text-right">车主：</th>
        <td>&nbsp;${orderInfo.customerName}</td>

        <th class="text-right">车主电话：</th>
        <td>&nbsp;${orderInfo.customerMobile}</td>

        <th class="text-right">联系人：</th>
        <td>&nbsp;${orderInfo.contactName}</td>

        <th class="text-right">联系人电话：</th>
        <td>&nbsp;${orderInfo.contactMobile}</td>
    </tr>
    <tr>
        <td colspan="8">
            &nbsp;
            <span>开单日期：<#if orderInfo.createTime>${orderInfo.createTime?string("yyyy-MM-dd HH:mm")}</#if></span>
            <span>期望交车日期：${orderInfo.expectTimeStr}</span>
            <div class="fr">
                <span class="_block"></span>&nbsp;洗车
                &nbsp;
                <span class="_block"></span>&nbsp;旧件带回
                &nbsp;
            </div>
        </td>
    </tr>
</table>
<#-- 车辆外观 -->
<table cellspacing="0" cellpadding="0">
    <caption class="item_tit">车辆外观</caption>
    <tr>
        <th width="100">序号</th>
        <th width="187">车辆部位</th>
        <th width="182">受损程度</th>
        <th width="100">序号</th>
        <th width="187">车辆部位</th>
        <th width="182">受损程度</th>
    </tr>
<#list precheckDetailsList as precheckValue>
    <#if precheckValue_index%2 == 0>
    <tr>
        <td class="center">${precheckValue_index + 1}</td>
        <td class="center">${precheckValue.precheckItemName}</td>
        <td class="center">${precheckValue.precheckValue?substring(2)}</td>
        <#if !precheckValue_has_next>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        </#if>
        <#else>
        <td class="center">${precheckValue_index + 1}</td>
        <td class="center">${precheckValue.precheckItemName}</td>
        <td class="center">${precheckValue.precheckValue?substring(2)}</td>
    </tr>
    </#if>
</#list>
</table>
<#-- 服务项目 -->
<table class="orderpp" cellspacing="0" cellpadding="0">
    <caption class="item_tit">服务项目</caption>
    <tr class="service-item">
        <th width="35" class="sequence">序号</th>
        <th width="452">维修内容</th>
        <th width="100">工种</th>
        <th width="76">工时</th>
        <th width="103">工时费</th>
        <th width="103">金额</th>
        <th width="103">优惠</th>
    </tr>
<#list orderServicesList1 as orderServices>
    <tr>
        <td class="sequence">${orderServices_index+1}</td>
        <td>&nbsp;${orderServices.serviceName}<#if orderServices.serviceNote>(${orderServices.serviceNote})</#if></td>
        <td class="center">${orderServices.serviceCatName}</td>
        <td class="center">&nbsp;${orderServices.serviceHour}</td>
        <td class="center">&nbsp;${orderServices.soldPrice}</td>
        <td class="center">&nbsp;${orderServices.serviceAmount}</td>
        <td class="center">&nbsp;${orderServices.discount}</td>
    </tr>
</#list>
</table>
<#-- 配件项目 -->
<table class="orderpp" cellspacing="0" cellpadding="0">
    <caption class="item_tit">配件项目</caption>
    <tr class="accessories-material">
        <th width="35" class="sequence">序号</th>
        <th width="452">配件名称</th>
        <th width="100">数量</th>
        <th width="76">单位</th>
        <th width="103">单价</th>
        <th width="103">总价</th>
        <th width="103">优惠</th>
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
</table>
<#-- 附加项目 -->
<table class="orderpp" cellspacing="0" cellpadding="0">
    <caption class="item_tit">附加项目</caption>
    <tr class="extra-costs">
        <th width="35" class="sequence">序号</th>
        <th width="366">附加费用</th>
        <th width="85">金额</th>
        <th width="101">优惠</th>
        <th width="387">费用备注</th>
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
</table>
<#-- 合计 -->
<table cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <span>&nbsp;总计：<strong class="strong">${orderInfo.orderAmount}</strong>元&nbsp;</span>
            <span>(工时费：${orderInfo.serviceAmount}元 - 工时优惠：${orderInfo.serviceDiscount}元 + 材料费：${orderInfo.goodsAmount}元 -
                材料优惠：${orderInfo.goodsDiscount}元 + 附加费：${orderInfo.feeAmount}元 - 附加优惠：${orderInfo.feeDiscount}元)</span>
        </td>
    </tr>
</table>
<div class="signature">
    <div class="fl">
        <span>服务顾问：</span><span class="sign">${orderInfo.receiverName}</span>
        <span>座机电话：${shop.tel}</span>
    </div>
    <div class="fr">
        <span>客户签字：</span><span class="sign"></span>
        <span class="ymd">年　　月　　日</span>
    </div>
</div>
</body>
</html>

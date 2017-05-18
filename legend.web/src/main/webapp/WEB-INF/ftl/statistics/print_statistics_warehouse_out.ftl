<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>出库明细报表</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" >
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/print_common.css"/>
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/watehouse_out_print.css?0aa51758d43b0c1965a7d84c8f12f8c4"/>
    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.js"></script>
    <style>
        td,th{
            font-size: xx-small;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function(){
            window.print();
        });
    </script>
</head>
<body>
<div class="head">
    <p class="tq_print_logo"><img src="${BASE_PATH}/resources/images/print_logo.png"/></p>

    <h2>${shop.companyName}</h2>
    <h3>出库明细报表</h3><span class="date">${startTime}至${endTime}</span>
</div>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <thead>
    <th width="20">编号</th>
    <#--<th>单据号</th>-->
    <#--<th>单据类型</th>-->
    <th width="120">零件号</th>
    <th width="120">零件名</th>
    <th width="30">出库数</th>
    <th width="20">单位</th>
    <th width="40">成本价</th>
    <th width="40">成本金额</th>
    <th width="40">售卖价</th>
    <th width="40">售卖金额</th>
    <#--<th>类别</th>-->
    <th width="50">仓位</th>
    <#--<th>工单号</th>-->
    <th width="40">车牌</th>
    <#--<th width="50">客户名</th>-->
    <th width="50">出库日期</th>
    <#--<th>维修接待</th>-->
    <th width="40">领料人</th>
    <#--<th>开单人</th>-->
    </thead>
    <tbody>
    <#if statisticsWarehouseOutList>
        <#list statisticsWarehouseOutList as item >
        <tr>
            <td style="text-align: center">${item_index+1}</td>
        <#--<td>${item.warehouseOutSn}</td>-->
        <#--<td>-->
        <#--<#if item.status=='LZCK'>蓝字出库</#if>-->
        <#--<#if item.status=='HZCK'>红字出库</#if>-->
        <#--</td>-->
            <td style="text-align: right">${"&nbsp;"+item.goodsFormat}</td>
            <td style="text-align: right">${item.goodsName}</td>
            <th>${item.goodsCount}</th>
            <td  style="text-align: right">${item.measureUnit}</td>
            <th><#if item.inventoryPrice>#{item.inventoryPrice;m2M2}</#if></th>
            <th><#if item.inventoryPriceAmount>#{item.inventoryPriceAmount;m2M2}</#if></th>
            <th><#if item.salePrice>#{item.salePrice;m2M2}</#if></th>
            <th><#if item.salePriceAmount>#{item.salePriceAmount;m2M2}</#if></th>
        <#--<td>${item.catName}</td>-->
            <td>${item.depot}</td>
        <#--<td>${item.orderSn}</td>-->
            <td style="text-align: center">${item.carLicense}</td>
            <#--<td style="text-align: center">${item.customerName}</td>-->
            <td style="text-align: center">${item.gmtCreateStr}</td>
        <#--<td style="text-align: center">${item.receiverName}</td>-->
            <td style="text-align: center">${item.goodsReceiverName}</td>
        <#--<td style="text-align: center">${item.operatorName}</td>-->
        </tr>
            <#if item_index==statisticsWarehouseOutList?size-1>
            <tr>
                <td colspan="3" style="text-align: center">合计:</td>
                <th>${item.totalCount}</th>
                <td colspan="2"></td>
                <th><#if item.totalInventoryPriceAmount>#{item.totalInventoryPriceAmount;m2M2}</#if></th>
                <td></td>
                <th><#if item.totalSalePriceAmount>#{item.totalSalePriceAmount;m2M2}</#if></th>
                <td colspan="4"></td>
            </tr>
            </#if>
        </#list>
    </#if>
    </tbody>
</table>
<div class="signature">
    <span class="date"><label>打印日期：</label>${.now}</span><span
        class="sign"><label>打印人：</label>${SESSION_USER_NAME}</span>
</div>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>入库明细报表</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" >
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/print_common.css"/>
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/watehouse_in_print.css?7f0754ffe9a98d0cf997a214c2ff5899"/>
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
    <h3>入库明细报表</h3>
    <span class="date">${startTime}至${endTime}</span>
</div>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <thead>
    <th width="20">编号</th>
    <#--<th width="40">单据号</th>-->
    <#--<th width="20">单据类型</th>-->

    <th width="150">零件号</th>
    <th width="150">零件名</th>
    <th width="40">成本价</th>
    <th width="50">成本金额</th>
    <th width="30">入库数</th>
    <th width="20">单位</th>
    <#--<th width="20">类别</th>-->
    <th width="60">仓位</th>
    <#--<th width="40">适用车型</th>-->
    <th width="50">入库日期</th>
    <th width="120">供应商</th>
    <th width="50">采购人</th>
    <#--<th>开单人</th>-->
    </thead>
<tbody>
<#if statisticsWarehouseInList>
<#list statisticsWarehouseInList as item >
    <tr>
        <td style="text-align: center">${item_index+1}</td>
        <#--<td>${item.warehouseInSn}</td>-->
        <#--<td>-->
            <#--<#if item.status=='LZRK'>蓝字入库</#if>-->
            <#--<#if item.status=='HZRK'>红字入库</#if>-->
        <#--</td>-->

        <td style="text-align: right">${"&nbsp;"+item.goodsFormat}</td>
        <td style="text-align: right">${item.goodsName}</td>
        <th><#if item.purchasePrice>#{item.purchasePrice;m2M2}</#if></th>
        <th><#if item.purchaseAmount>#{item.purchaseAmount;m2M2}</#if></th>
        <th>${item.goodsCount}</th>
        <td style="text-align: right">${item.measureUnit}</td>
        <#--<td>${item.catName}</td>-->
        <td style="text-align: right">${item.depot}</td>
        <#--<td>-->
            <#--<#if item.carInfoStr>${item.carInfoStr}</#if>-->
            <#--<#if item.carInfoList>-->
                <#--<#list item.carInfoList as carInfoList>-->
                <#--${carInfoList.carBrandName} ${carInfoList.carSeriesName} ${carInfoList.carAlias}&nbsp;-->
                <#--</#list>-->
            <#--</#if>-->
        <#--</td>-->
        <td style="text-align: center">${item.gmtCreateStr}</td>
        <td style="text-align: center">${item.supplierName}</td>
        <td style="text-align: center">${item.purchaseAgentName}</td>
        <#--<td style="text-align: center">${item.creatorName}</td>-->
    </tr>
<#if item_index==statisticsWarehouseInList?size-1>
    <tr>
        <td colspan="4" style="text-align: center">合计：</td>
        <th><#if item.totalPurchase>#{item.totalPurchase;m2M2}</#if></th>
        <th>${item.totalCount}</th>
        <td colspan="5"></td>
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
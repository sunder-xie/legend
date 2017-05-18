<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>入库单导出</title>
</head>

<body>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <tr><th colspan="16" style="text-align: center"><h3>${shop.companyName}</h3></td></tr>
    <tr>
        <td colspan="13" style="text-align: center">入库明细报表</td>
        <th>${startTime}</th>
        <th>至</th>
        <th>${endTime}</th></tr>
    <tr>
    <thead>
    <th>入库单号</th>
    <th>类型</th>
    <th>入库日期</th>
    <th>零件号</th>
    <th>零件名</th>
    <th>类别</th>
    <th>单价</th>
    <th>入库数量</th>
    <th>单位</th>
    <th>总金额</th>
    <th>仓位</th>
    <th>适用车型</th>
    <th>供应商</th>
    <th>采购人</th>
    <th>开单人</th>
    <th>备注</th>
    </thead>
    <tbody>

    <#list viewList as warehouseInVo>
        <#list warehouseInVo.detailList as detail>
    <tr>
        <td>${warehouseInVo.warehouseInSn}</td>
        <th><#if warehouseInVo.status == "LZRK">蓝字入库
        <#elseif warehouseInVo.status == "HZRK">红字入库
        <#elseif warehouseInVo.status == "HZZF">红字作废
        <#elseif warehouseInVo.status == "LZZF">蓝字作废
        <#else >
            草稿
        </#if></th>
        <th >${warehouseInVo.inTime}</th>
        <td>${detail.goodsFormat}</td>
        <td>${detail.goodsName}</td>
        <td>${detail.catName}</td>
        <th>${detail.purchasePrice}</th>
        <th>${detail.goodsCount}</th>
        <th>${detail.measureUnit}</th>
        <td>${detail.purchaseAmount}</td>
        <td>${detail.depot}</td>
        <th>${detail.carInfoStr}</th>
        <td>&nbsp;${warehouseInVo.supplierName}</td>
        <td>${warehouseInVo.purchaseAgentName}</td>
        <td>${warehouseInVo.operatorName}</td>
        <td>${warehouseInVo.comment}</td>
    </tr>
    </#list>
    </#list>
    <tr>
        <td colspan="7">合计：</td>
        <th>${count}</th>
        <th> </th>
        <th>${totalAmount}</th>
        <td colspan="6"></td>
    </tr>
    <tr>
        <th>导出人:${operatorName}</th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th>导出时间:${.now?string("yyyy-MM-dd")}</th>
    </tr>
    </tbody>
</table>
</body>
</html>
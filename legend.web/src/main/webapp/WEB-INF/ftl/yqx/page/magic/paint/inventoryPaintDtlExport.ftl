<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>油漆盘点单导出</title>
</head>

<body>

<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <tr>
    <th rowspan="2">配件ID</th>
    <th rowspan="2">配件名称</th>
    <th rowspan="2">零件号</th>
    <th colspan="4">现库存</th>
    <th colspan="4">实盘库存</th>
    <th rowspan="2">油漆消耗量</th>
    <th rowspan="2">油漆消耗成本</th>
    </tr>

    <tr>
    <th>整桶数量</th>
    <th>非整桶总质量</th>
    <th>非整桶数量</th>
    <th>搅拌头数量</th>
    <th>整桶数量</th>
    <th>非整桶总质量</th>
    <th>非整桶数量</th>
    <th>搅拌头数量</th>
    </tr>

    <tbody>
    <#if paintInventoryStockList??>
        <#list paintInventoryStockList as paintInventoryStock>
        <tr>
            <td>${paintInventoryStock.goodsId}</td>
            <td>${paintInventoryStock.goodsName}</td>
            <td>${paintInventoryStock.goodsFormat}</td>
            <td>${paintInventoryStock.currentStock}</td>
            <td>${paintInventoryStock.currentNoBucketWeight}g</td>
            <td>${paintInventoryStock.currentNoBucketNum}</td>
            <td>${paintInventoryStock.currentStirNum}</td>
            <td>${paintInventoryStock.realStock}</td>
            <td>${paintInventoryStock.realNoBucketWeight}g</td>
            <td>${paintInventoryStock.realNoBucketNum}</td>
            <td>${paintInventoryStock.realStirNum}</td>
            <td>${paintInventoryStock.diffStock}g</td>
            <td>&yen;${paintInventoryStock.diffStockPrice}</td>
        </tr>
        </#list>
    </#if>
    </tbody>
</table>
</body>
</html>
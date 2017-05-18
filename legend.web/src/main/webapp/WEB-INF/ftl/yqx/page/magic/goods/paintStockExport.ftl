<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>油漆库存</title>
</head>

<body>

<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <thead>
    <th colspan="10">油漆库存导出</th>
    </thead>
</table>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <thead>
    <th>油漆名称</th>
    <th>配件编号</th>
    <th>零件号</th>
    <th>适配车型</th>
    <th>整桶数量</th>
    <th>非整桶质量</th>
    <th>非整桶数量</th>
    <th>搅拌头数量</th>
    <th>库存总成本</th>
    <th>仓库货位</th>
    </thead>
    <tbody>

    <#if page?? && page.content??>
    <#list page.content as goodsPaintVo>
    <tr>
        <td>${goodsPaintVo.name}</td>
        <td>${goodsPaintVo.goodsSn}</td>
        <td>${goodsPaintVo.format}</td>
        <td>${goodsPaintVo.carInfoStr}</td>
        <td>${goodsPaintVo.stock}</td>
        <td>${goodsPaintVo.noBucketWeight}g</td>
        <td>${goodsPaintVo.noBucketNum}</td>
        <td>${goodsPaintVo.stirNum}</td>
        <td>&yen;${goodsPaintVo.totalStockAmount}</td>
        <td>${goodsPaintVo.depot}</td>
    </tr>
    </#list>
    </#if>
    </tbody>
</table>
</body>
</html>
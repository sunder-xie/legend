<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>库存盘点表导出</title>
</head>

<body>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <thead>
    <th>配件ID</th>
    <th>配件名称</th>
    <th>零件号</th>
    <th>配件分类</th>
    <th>实盘库存</th>
    <th>实盘库存金额</th>
    <th>原因</th>
    <th>现库存</th>
    <th>库存金额</th>
    <th>盘盈/盘亏</th>
    <th>盘盈/盘亏金额</th>
    <th>仓库货位</th>
    <th>最后入库时间</th>
    </thead>
    <tbody>
    <#list viewList as stock>
    <tr>
        <td style="text-align: left">${stock.goodsId}</td>
        <td style="text-align: left">${stock.goodsName}</td>
        <td style="text-align: left">${stock.goodsFormat}</td>
        <td style="text-align: left">${stock.catName}</td>
        <td style="text-align: center">${stock.realStock}</td>
        <td style="text-align: right">${stock.realInventoryAmount}</td>
        <td style="text-align: left">${stock.reason}</td>
        <td style="text-align: center">${stock.currentStock}</td>
        <td style="text-align: right">${stock.currentInventoryAmount}</td>
        <td style="text-align: center">${stock.diffStock}</td>
        <td style="text-align: right">${stock.diffInventoryAmount}</td>
        <td style="text-align: center">${stock.depot}</td>
        <td style="text-align: center">${stock.lastInTimeStr}</td>
    </tr>
    </#list>
    </tbody>
</table>
</body>
</html>


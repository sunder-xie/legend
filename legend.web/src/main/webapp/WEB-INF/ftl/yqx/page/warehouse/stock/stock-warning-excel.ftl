<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>库存预警导出</title>
</head>

<body>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <thead>
    <th>配件ID</th>
    <th>配件名称</th>
    <th>库存数量</th>
    <th>预警库存</th>
    <th>库存单位</th>
    <th>前三月均销量</th>
    <th>建议库存</th>
    <th>云修采购价</th>
    <th>适配车型</th>
    </thead>
    <tbody>
    <#list goodslist as goods>
    <tr>
        <td style="text-align: left">${goods.id}</td>
        <td style="text-align: left">${goods.name}</td>
        <td style="text-align: right">${goods.stock}</td>
        <td style="text-align: right">${goods.shortageNumber}</td>
        <td style="text-align: center">${goods.measureUnit}</td>
        <td style="text-align: right">${goods.averageNumber}</td>
        <th style="text-align: center">
            <#if goods.suggestGoodsNumber gt 0>
            ${goods.suggestGoodsNumber}
            <#else>
                不建议采购
            </#if>

        </th>
        <th style="text-align: right">
        <#if goods.tqmallPrice == "">
            --
        <#else>
        ${goods.tqmallPrice}
        </#if>
        </th>
        <th style="text-align: left">${goods.carInfoStr}</th>
    </tr>
    </#list>
    </tbody>
</table>
</body>
</html>


<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>委托单导出</title>
</head>

<body>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <thead>
    <th>序号</th>
    <th>油漆等级</th>
    <th>油漆类型</th>
    <th>出库质量（g）</th>
    <th>销售价（元/100g）</th>
    <th>金额</th>
    <th>备注</th>
    </thead>
    <tbody>
    <#list record as paintRecordDetail>
    <tr>
        <td>${paintRecordDetail.seqno}</td>
        <td>${paintRecordDetail.paintLevel}</td>
        <td>${paintRecordDetail.paintType}</td>
        <td>${paintRecordDetail.warehouseOutWeight}</td>
        <th>${paintRecordDetail.salePrice}</th>
        <th>${paintRecordDetail.warehouseOutAmount}</th>
        <th>${paintRecordDetail.detailRemark}</th>
    </tr>
    </#list>
    </tbody>
</table>
</body>
</html>
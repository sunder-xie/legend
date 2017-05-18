<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>施工单导出</title>
</head>

<body>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <thead>
    <th>车牌号</th>
    <th>生产线</th>
    <th>车型</th>
    <th>状态</th>
    <th>服务顾问</th>
    <th>施工总时长（分钟）</th>
    <th>计划完工时间</th>
    <th>创建时间</th>
    </thead>
    <tbody>

    <#list page.content as workOrder>
    <tr>
        <td>${workOrder.carLicense}</td>
        <td>${workOrder.lineName}</td>
        <td>${workOrder.carInfo}</td>
        <th>${workOrder.statusStr}</th>
        <th>${workOrder.serviceSa}</th>
        <th>${workOrder.totalTimeStr}</th>
        <th>${workOrder.planEndTimeStr}</th>
        <th>${workOrder.createStr}</th>
    </tr>
    </#list>
    </tbody>
</table>
</body>
</html>
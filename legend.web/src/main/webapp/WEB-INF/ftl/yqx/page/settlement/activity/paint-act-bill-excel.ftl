<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>结算单导出</title>
</head>

<body>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <thead>
    <th>审核通过时间</th>
    <th>车牌</th>
    <th>车主</th>
    <th>车主电话</th>
    <th>保单号</th>
    <th>受损部位</th>
    <th>核销码</th>
    <th>状态</th>
    </thead>
    <tbody>

    <#list page.content as insuranceBill >
    <tr>
        <td>${insuranceBill.auditPassTimeStr}</td>
        <td>${insuranceBill.carLicense}</td>
        <td>${insuranceBill.customerName}</td>
        <td>${insuranceBill.customerMobile}</td>
        <td>${insuranceBill.insuredCode}</td>
        <td>${insuranceBill.woundPart}</td>
        <td>${insuranceBill.verificationCode}</td>
        <td><#if insuranceBill.shopConfirmStatus == 0>待确定<#else>已确定</#if></td>
    </tr>
    </#list>
    </tbody>
</table>
</body>
</html>


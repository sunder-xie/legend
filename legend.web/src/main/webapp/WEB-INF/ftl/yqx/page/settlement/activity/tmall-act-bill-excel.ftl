<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>结算单导出</title>
</head>

<body>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <thead>
    <th>审核通过时间</th><!---->
    <th>车牌</th>
    <th>车主</th>
    <th>车主电话</th>
    <th>优惠券</th>
    <th>套餐服务</th>
    <th>结算价</th>
    <th>状态</th>
    </thead>
    <tbody>

    <#list page.content as insuranceBillVo >
    <tr>
        <td>${insuranceBillVo.auditPassTimeStr}</td>
        <td>${insuranceBillVo.carLicense}</td>
        <td>${insuranceBillVo.customerName}</td>
        <td>${insuranceBillVo.customerMobile}</td>
        <td>${insuranceBillVo.verificationCode}</td>
        <td>${insuranceBillVo.serviceName}</td>
        <td>${insuranceBillVo.settlePrice}</td>
        <td>
            <#if insuranceBillVo.shopConfirmStatus==1>
                已确认
            <#else>
                未确认
            </#if>
        </td>
    </tr>
    </#list>
    </tbody>
</table>
</body>
</html>


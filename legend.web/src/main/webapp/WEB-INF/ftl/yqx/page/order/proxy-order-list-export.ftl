<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>委托单导出</title>
</head>

<body>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <thead>
    <th>委托单号</th>
    <th>车牌</th>
    <th>车型</th>
    <th>委托方</th>
    <th>委托时间</th>
    <th>委托金额</th>
    <th>工时</th>
    <th>委托方联系人</th>
    <th>委托方联系方式</th>
    <th>受托方</th>
    <th>受托方联系人</th>
    <th>受托方联系方式</th>
    <th>完工时间</th>
    <th>委托单状态</th>


    </thead>
    <tbody>

    <#list page.content as proxyOrderVo>
    <tr>
        <td>${proxyOrderVo.proxySn}</td>
        <td>${proxyOrderVo.carLicense}</td>
        <td>${proxyOrderVo.carInfo}</td>
        <td>${proxyOrderVo.shopName}</td>
        <th>${proxyOrderVo.proxyTimeStr}</th>
        <th>${proxyOrderVo.proxyAmount}</th>
        <th>${proxyOrderVo.serviceNum}</th>
        <th>${proxyOrderVo.contactName}</th>
        <th>${proxyOrderVo.contactMobile}</th>
        <th>${proxyOrderVo.proxyShopName}</th>
        <th>${proxyOrderVo.shareName}</th>
        <th>${proxyOrderVo.shareMobile}</th>
        <th>${proxyOrderVo.completeTimeStr}</th>
        <th>${proxyOrderVo.proxyStatusStr}</th>
    </tr>
    </#list>
    </tbody>
</table>
</body>
</html>
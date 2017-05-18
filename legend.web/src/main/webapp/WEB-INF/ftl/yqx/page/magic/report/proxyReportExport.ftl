<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>委托数据统计</title>
</head>

<body>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <thead>
    <th colspan="6" align="center">委托数据统计</th>
    </thead>
    <tr>
        <td>委托时间</td>
        <td>${startTime}</td>
        <td>${endtime}</td>
        <td></td>
        <td>是否为股东店</td>
        <td>${partnerStatusStr}</td>
    </tr>
</table>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <thead>
    <th>委托方名称</th>
    <th>委托方车辆总数</th>
    <th>委托总面数</th>
    <th>面漆总金额</th>
    <th>钣金总金额</th>
    <th>委托总金额</th>
    </thead>
    <tbody>

    <#list proxyReportDTO.proxyVoDTOList as proxyVoDTO>
    <tr>
        <td>${proxyVoDTO.shopName}</td>
        <td>${proxyVoDTO.carNum}</td>
        <td>${proxyVoDTO.proxySurface}</td>
        <th>&yen;${proxyVoDTO.paintAmount}</th>
        <th>&yen;${proxyVoDTO.metalAmount}</th>
        <th>&yen;${proxyVoDTO.proxyAmount}</th>
    </tr>
    </#list>

    <tr>
        <td>合计</td>
        <td>${proxyReportDTO.totalCarNum}</td>
        <td>${proxyReportDTO.totalProxySurface}</td>
        <td class="money-font">&yen;${proxyReportDTO.totalPaintAmount}</td>
        <td class="money-font">&yen;${proxyReportDTO.totalMetalAmount}</td>
        <td class="money-font">&yen;${proxyReportDTO.totalProxyAmount}</td>
    </tr>

    </tbody>
</table>
</body>
</html>
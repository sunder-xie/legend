<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>出库单</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/out/out-print.css?0ded944b4a9672510846a75d0d2606b9">

</head>
<body>
<div class="head">
    <#if SESSION_SHOP_LEVEL != 10 && SESSION_SHOP_LEVEL != 11 && SESSION_SHOP_LEVEL != 12>
    <p class="tq_print_logo"><img src="${BASE_PATH}/static/img/common/print/print_logo.png"/></p>
    </#if>
    <h1>${shop.companyName}</h1>

    <h3>出库单</h3>

    <div class="headInfo">
        <span class="customName"><label>客户名称：</label>${warehouseOut.customerName}<#if warehouseOut.carLicense != null >
            (${warehouseOut.carLicense})</#if></span><span
            ><label>单据号：</label>${warehouseOut.warehouseOutSn}</span><span
            class="salesDate"><label>领料日期：</label>${warehouseOut.gmtCreate?string("yyyy-MM-dd HH:mm")}</span>

        <div class="clear"></div>
    </div>
    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.js"></script>
    <script>
        $(document).ready(function ($) {
            window.print();
        });
    </script>
</div>
<table>
    <tr>
        <th class="center">序号</th>
        <th colspan="4">零件号</th>
        <th colspan="4">零件名称</th>
        <th colspan="2">车辆型号</th>
        <th>类别</th>
        <th colspan="2">数量</th>
        <th colspan="2">单价</th>
        <th colspan="2">销售金额</th>
        <th colspan="2">货位</th>
    </tr>
<#assign num = 0 >
<#list warehouseOut.detailVoList as detail>
    <tr class="content">
        <#assign num = num + 1 >
        <td class="center">${num}</td>
        <td colspan="4">&nbsp;${detail.goodsFormat}</td>
        <td colspan="4">&nbsp;${detail.goodsName}</td>
        <td colspan="2">&nbsp;${detail.carInfoStr}</td>
        <td>&nbsp;</td>
        <td class="center" colspan="2">&nbsp;${detail.goodsCount}</td>
        <td class="center" colspan="2">&nbsp;${detail.salePrice}</td>
        <td class="center" colspan="2">&nbsp;${detail.goodsCount * detail.salePrice}</td>
        <td class="center" colspan="2">&nbsp;${detail.depot}</td>
    </tr>

</#list>
    <tr>
        <td colspan="4">&nbsp;累计：${saleAmount}整</td>
        <td colspan="5">&nbsp;含税金额：${warehouseOut.saleAmount}</td>
        <td colspan="4">&nbsp;去税金额：</td>
        <td colspan="4">&nbsp;税金：</td>
        <td colspan="3">&nbsp;合计数量：${totalCount}</td>
    </tr>
</table>
<div class="signature">
    <span><label>领料人：${warehouseOut.receiverName}</label></span><span
        style="width: 200px;"><label>开单员：</label></span><span
        style="width:250px;"><label>打印日期：</label> ${.now?string("yyyy-MM-dd HH:mm")}</span><span
        ><label>用户签名：</label></span>
</div>
</body>
</html>
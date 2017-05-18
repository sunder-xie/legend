<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>盘点表</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css"
          href="${BASE_PATH}/static/css/common/base.css?8bb0f9a84cfb070249dcc1186f9f6a25"/>
    <link rel="stylesheet" type="text/css"
          href="${BASE_PATH}/static/css/page/warehouse/stock/inventory_sheet.css?d5bd6e74fbb1076abfe5e47682d249ff"/>

    <script type="text/javascript" src="${BASE_PATH}/static/third-plugin/jquery.js"></script>
    <script>
        $(document).ready(function ($) {
            window.print();
        });
    </script>
</head>
<body>
<div class="head">
    <div>
        <p class="company">${shop.companyName}</p>
        <p class="tit">盘点表</p>
    </div>
    <p>
        <span class="date">日期：${record.gmtModified?string("yyyy年MM月dd日")}</span>
        <span class="code">单号：${record.recordSn}</span>
    </p>
</div>
<table>
    <tr>
        <th class="w1">序号</th>
        <th class="w2">配件名称</th>
        <th class="w3">零件号</th>
        <th class="w4">现库存</th>
        <th class="w4">库存总金额</th>
        <th class="w5">实盘库存</th>
        <th class="w6">盘亏/盘盈</th>
        <th class="w7">原因</th>
    </tr>
<#list stocks as stock>
    <tr>
        <td class="w1" style="text-align: center">${stock_index + 1}</td>
        <td class="w2">${stock.goodsName}</td>
        <td class="w3">${stock.goodsFormat}</td>
        <td class="w4">${stock.currentStock}</td>
        <td class="w4">${stock.currentInventoryAmount}</td>
        <td class="w5">${stock.realStock}</td>
        <td class="w6">${stock.diffStock}</td>
        <td class="w7">${stock.reason}</td>
    </tr>
</#list>
    <tr>
        <td class="w1"></td>
        <td class="w2"></td>
        <td class="w3"></td>
        <td class="w4"></td>
        <td class="w4">总计：${totalCurrentInventoryAmount}</td>
        <td class="w5"></td>
        <td class="w6"></td>
        <td class="w7"></td>
    </tr>

</table>
<div class="foot">
    <div>
        <p>盘点人签字：</p>
        <p>签字时间：<span class="sign_date">年　　月　　日</span></p>
    </div>
    <div>
        <p>仓库主管签字：</p>
        <p>签字时间：<span class="sign_date">年　　月　　日</span></p>
    </div>
    <div>
        <p>财务主管签字：</p>
        <p>签字时间：<span class="sign_date">年　　月　　日</span></p>
    </div>
</div>
</body>
</html>

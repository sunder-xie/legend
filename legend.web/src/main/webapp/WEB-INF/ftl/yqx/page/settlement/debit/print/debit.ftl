<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>收款单打印</title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print.css?0c3994b5134160a03fdfc1ddc44657ff"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/print/print-pay.css?dfebf7e7b5ac39214127f0063e97feb2"/>
    <script src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script src="${BASE_PATH}/static/js/common/print/printDisplay.js?8d8402217e141c345ee32115cf794361"></script>
    <script>
        $(document).ready(function ($) {
            Components.changeCompanyWidth();
            $('.uppercase').text( Components.digitUppercaseCN($('.money').text()) );
            window.print();
        });
    </script>
</head>
<body class="A4">
<section class="a-main print-box dispatch-box">
    <header>
    <#if SESSION_SHOP_LEVEL != 10 && SESSION_SHOP_LEVEL != 11 && SESSION_SHOP_LEVEL != 12>
    <img class="print-logo" src="${BASE_PATH}/static/img/common/print/print-logo.png">
    </#if>
        <div class="title-box">
            <h1 class="text" id="维修厂名称"><i class="text">
                ${shop.companyName}
            </i></h1>
            <h2 class="text">收款单</i></h2>
        </div>
        <div class="box">
            <p>收款时间：${debitBillFlow.flowTime?string("yyyy-MM-dd HH:mm")}</p>
        </div>
        <div class="table-box">
            <table class="box">
                <tr>
                    <td>收款类型：</td>
                    <td class="project">收款项目：</td>
                    <td>收款方式：</td>
                    <td>收款金额：<i class="money">${debitBillFlow.payAmount}</i>元</td>
                </tr>
                <tr>
                    <td>${debitBill.debitTypeName}</td>
                    <td>${debitBill.billName}</td>
                    <td>
                    <#if debitBillFlow>
                        <#if debitBillFlow.flowStatus == 0><#if debitBillFlow.flowType == 1>冲红</#if>${debitBillFlow.paymentName}<#else>坏账</#if>
                    </#if>

                    </td>
                    <td class="uppercase">

                    </td>
                </tr>
            </table>
        </div>
        <div class="box no-padding">
            <table class="box no-border">
            <tr>
                <td colspan="3">付款方：${debitBill.payerName}</td>
                <td>收银人员：${debitBill.operatorName}</td>
            </tr>
            </table>
        </div>
        <#if debitBillFlow.remark>
        <div class="box remark-box">
            <p>备注：${debitBillFlow.remark}</p>
        </div>
        </#if>
    </header>
</section>
</body>
</html>
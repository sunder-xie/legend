<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>小票打印</title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print.css?0c3994b5134160a03fdfc1ddc44657ff"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/print/print-receipt.css?9a97d3c1be50f24441c0a27e36dbc34c"/>

    <script src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script>
        $(document).ready(function () {
            window.print();
        });
    </script>
</head>
<body>
<#--小票-->
<section class="print-box receipt-box">
    <header>
        <h2>${shop.companyName}</h2>
        <h3>结算单</h3>
    </header>
    <ul class="info-box box">
        <li>
            <i style="width: 15mm;">车牌号：</i><i>
                <p>${orderInfo.carLicense}</p>
            </i>
        </li>
        <#if memberCards>
        <#list memberCards as memberCard>
            <li><i style="width: 15mm;">会员卡：</i><i>${memberCard.cardNumberStr}</i></li>
        </#list>
        </#if>
        <li><i style="width: 19mm;">工单编号：</i><i style="font-size: 9pt;">${orderInfo.orderSn}</i></li>
        <li>服务顾问：${SESSION_USER_NAME}</li>
    </ul>
    <table class="box">
        <thead>
            <tr>
                <th class="name">名称</th>
                <th style="width: 9mm">数量</th>
                <th style="width: 16mm">总计(&yen;)</th>
            </tr>
        </thead>
        <#assign count = 0>
    <#list orderServicesList1 as item>
        <#assign count = count + 1>
        <tr class="<#if count == 1>padding-top</#if> <#if orderGoodsList?size == 0 && orderServicesList2?size == 0 && item_has_next == false>padding-bottom</#if>">
            <td>${item.serviceName}</td>
            <td>${item.serviceHour} </td>
            <td>${item.serviceAmount}</td>
        </tr>
    </#list>

    <#list orderGoodsList as orderGoods>
        <#assign count = count + 1>
        <tr class="<#if count == 1>padding-top</#if> <#if orderServicesList2?size == 0  && orderGoods_has_next == false>padding-bottom</#if>">
            <td>${orderGoods.goodsName}</td>
            <td>${orderGoods.goodsNumber}</td>
            <td>${orderGoods.goodsAmount}</td>
        </tr>
    </#list>
    <#--附加项目-->
    <#list orderServicesList2 as orderServices>
        <#assign count = count + 1>
        <tr class="<#if count == 1>padding-top</#if> <#if orderServices_has_next == false>padding-bottom</#if>">
            <td>${orderServices.serviceName}</td>
            <td>1</td>
            <td>${orderServices.serviceAmount}</td>
        </tr>
    </#list>
    <tr class="padding-top box border-top-dashed" style="border-bottom: 0;">
        <td>总优惠</td>
        <td></td>
        <td>
        ${orderInfo.serviceDiscount + orderInfo.goodsDiscount + orderInfo.feeDiscount + orderInfo.orderDiscountAmount}
        </td>
    </tr>
        <tr class="padding-bottom">
            <td>应收金额</td>
            <td></td>
            <td>${debitBill.receivableAmount}</td>
        </tr>
        <#if debitBill.signAmount && debitBill.signAmount gt 0>
        <tr class="padding-bottom">
            <td>挂帐金额</td>
            <td></td>
            <td>${debitBill.signAmount}</td>
        </tr>
        </#if>
    <#list debitBillFlowList as item>
        <tr class="<#if item_index == 0>padding-top</#if> <#if item_has_next == false>padding-bottom</#if> <#if item_index == 0>border-top-dashed</#if>">
            <td>
                <#if item.flowStatus == 0><#if item.flowType == 1>冲红</#if>${item.paymentName}支付<#else>优惠</#if>
            </td>
            <td></td>
            <td>${item.payAmount}</td>
        </tr>
    </#list>
    <#list comboFlowDetails as combo>
        <tr class="<#if combo_has_next == false>padding-bottom</#if>">
            <td class="combo-name">扣除${combo.serviceName}</td>
            <td class="combo-count">-${combo.changeCount}</td>
            <td class="right">--.--</td>
        </tr>
    </#list>
    <#if memberCards>
    <#list memberCards as memberCard>
        <tr class=" border-top-dashed padding-top">
            <td>会员卡余额</td>
            <td>${memberCard.cardNumberStr}</td>
            <td>${memberCard.balance}</td>
        </tr>
    </#list>

    </#if>

    <#list comboServiceList as combo>
        <tr class="<#if memberCards?? == false && combo_index == 0> border-top-dashed</#if> <#if combo_has_next == false>padding-bottom</#if>">
            <td class="combo-name">剩余${combo.serviceName}</td>
            <td class="combo-count">${combo.leftServiceCount}</td>
            <td class="right">--.--</td>
    </tr>
    </#list>
    </table>
    <ul class="border-top box">
    <#if orderInfo.orderStatus == "DDYFK">
        <li><i>打印时间：</i><i>${.now?string("MM-dd HH:mm")}</i></li>
    </#if>
        <li>联系电话：${shop.tel}</li>
        <li id="address"><i style="width: 11mm;">地址：</i><i>${shop.address}</i></li>
    </ul>
    <#if code>
    <div class="code-box">
        <img src="${code}">
        <p>扫一扫，查看更多卡券、活动信息</p>
    </div>
    </#if>

</section>
</body>
</html>
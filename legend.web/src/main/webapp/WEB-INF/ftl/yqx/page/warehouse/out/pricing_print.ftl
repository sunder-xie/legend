<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title></title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print.css?0c3994b5134160a03fdfc1ddc44657ff"/>

    <script src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script src="${BASE_PATH}/static/js/common/print/printDisplay.js?8d8402217e141c345ee32115cf794361"></script>
    <script>
        $(document).ready(function ($) {
            // 大写金额
            $('#应收金额大写 .text').text( Components.digitUppercaseCN( $('#应收金额 .text').text().replace('元', '') ) );
            window.print();
        });
    </script>
</head>
<body class="A4">
<#--报价单-->
<section class="a-main print-box bill-box <#if fontStyle == 1> font-12</#if>" data-print="bill">
    <header>
        <img class="print-logo" src="${BASE_PATH}/static/img/common/print/print-logo.png"><div class="title-box">
        <h1 class="text" id="维修厂名称"><i class="text">${shop.companyName}</i></h1>
        <h2 class="text" id="编号">（报价单） NO：<i class="text">${orderInfo.orderSn}</i></h2>
    </div><div class="contact-box">
        <h3 id="地址" ><i>地址：</i><i class="address text">${shop.address}</i></h3>
        <h3 id="电话">电话：<i class="text">${shop.tel}</i></h3>
    </div>
    </header>
    <article>
        <ul class="box order-info">
            <li id="开单时间" class="time">
                <i class="key">开单时间：</i><i class="text">${orderInfo.createTimeStr}</i>
            </li>
            <li id="服务顾问" class="fr server">
                <i class="key">服务顾问：</i><i class="text">${orderInfo.receiverName}</i>
            </li>
        </ul>
        <ul class="box customer-box">
            <li class="text customer" id="车主">
                <i class="text"><#if orderInfo.customerName || orderInfo.customerMobile>${orderInfo.customerName} ${orderInfo.customerMobile}(车主)</#if></i>
            </li><li class="text relative-customer" id="联系人">
            <i class="text">
            <#if orderInfo.contactName != orderInfo.customerName || orderInfo.customerMobile != orderInfo.contactMobile>
                <#if orderInfo.contactName || orderInfo.contactMobile>
                ${orderInfo.contactName} ${orderInfo.contactMobile}(联系人)
                </#if>
            </#if></i>
        </li><li id="车牌">
            <i class="key">车牌：</i><i class="text">${orderInfo.carLicense}</i>
        </li><li id="车辆型号">
            <i class="key">车辆型号：</i><i class="text">${orderInfo.carInfo}</i>
        </li>
        </ul>
    <#if orderGoodsList>
        <table class="box goods-box normal-table" id="配件物料">
            <thead>
            <tr>
                <th>序号</th>
                <th class="name">配件物料</th>
                <th class="number">数量</th>
                <th class="number per-hour-price">金额</th>
                <th class="number">优惠</th>
            </tr>
            </thead>
            <tbody>
                <#list orderGoodsList as orderGoods>
                <tr>
                    <td>${orderGoods_index+1}</td>
                    <td>${orderGoods.goodsName}<#if orderGoods.goodsNote>(${orderGoods.goodsNote})</#if></td>
                    <td>${orderGoods.goodsNumber} ${orderGoods.measureUnit}</td>
                    <td>${orderGoods.goodsAmount}</td>
                    <td>${orderGoods.discount}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </#if>
        <ul class="box total-box">
            <li>合计</li>
            <li id="配件总费用">配件总费用：<i class="text">${orderInfo.goodsAmount}元</i></li>
            <li id="总优惠">总优惠：<i class="text">${orderInfo.goodsDiscount}元</i></li>
            <li class="border-left">
                <ul>
                    <li id="应收金额"> 总计：<i class="text"><#if orderInfo.goodsAmount>${orderInfo.goodsAmount-orderInfo.goodsDiscount}元</#if></i></li>
                    <li id="应收金额大写"><i class="text"></i></li>
                </ul>
            </li>
        </ul>
        <ul class="box footer-box">
            <li>
                <i class="five">打印时间：</i><i class="print-time text">${.now?string("yyyy-MM-dd HH:mm")}</i>
                <i class="four">客户签字</i><i class="sign-text"></i><i class="space year-space"></i><i>年</i><i class="space"></i><i>月</i><i class="space"></i><i>日</i>
            </li>
        </ul>
        <#if orderInfo.postscript>
            <ul class="box footer-box">
                <li>
                    备注：${orderInfo.postscript}
                </li>
            </ul>
        </#if>

</body>
</html>
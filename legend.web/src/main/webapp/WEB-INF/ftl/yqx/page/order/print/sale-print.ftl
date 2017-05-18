<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>销售单打印</title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print.css?0c3994b5134160a03fdfc1ddc44657ff"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/print/print-sale.css?d41d8cd98f00b204e9800998ecf8427e"/>
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
<#--销售单-->
<body class="A4">
<input value='${printConfigVO.configField}'' hidden id="configField">
<section class="a-main print-box settle-box <#if printConfigVO.fontStyle == 1> font-12</#if>" data-print="settle">
    <header>
    <#if printLogo != null>
        <img class="print-logo" src="${printLogo}">
    <#elseif SESSION_SHOP_LEVEL != 10 && SESSION_SHOP_LEVEL != 11 && SESSION_SHOP_LEVEL != 12 && printTitle == null>
        <img class="print-logo" src="${BASE_PATH}/static/img/common/print/print-logo.png">
    </#if>
        <div class="title-box">
        <#if printTitle != null >
            <img src="${printTitle}" alt="${shop.companyName}" width="445px" height="29px">
        <#else>
            <h1 class="text" id="维修厂名称"><i class="text">${shop.companyName}</i></h1>
        </#if>
        <h2 class="text" id="编号">（销售单） NO：<i class="text">${orderInfo.orderSn}</i></h2>
    </div><div class="contact-box">
        <h3 id="地址"><i>地址：</i><i class="address text">${shop.address}</i></h3>

        <h3 id="电话">电话：<i class="text">${shop.tel}</i></h3>
    </div>
    </header>
    <article>
        <ul class="box order-info">
            <li id="开单时间" class="time"><i class="key">开单时间：</i><i class="text">${orderInfo.createTimeStr}</i></li>
            <li id="服务顾问" class="fr server"><i class="key">服务顾问：</i><i class="text">${orderInfo.receiverName}</i></li>
        </ul>
        <ul class="box customer-box">
            <li class="text customer" id="车主"><i
                    class="text">
            <#if orderInfo.customerName || orderInfo.customerMobile>
            ${orderInfo.customerName} ${orderInfo.customerMobile}(车主)
            </#if></i></li>
            <li class="text relative-customer" id="联系人"><i
                    class="text">
            <#if orderInfo.contactName != orderInfo.customerName || orderInfo.customerMobile != orderInfo.contactMobile>
                <#if orderInfo.contactName || orderInfo.contactMobile>
                ${orderInfo.contactName} ${orderInfo.contactMobile}(联系人)
                </#if>
            </#if>
            </i></li>
            <li class="customer-company" id="单位"><i class="key">客户单位：</i><i class="text">${orderInfo.company}</i></li>
        </ul>
    <#if orderGoodsList>
        <table class="box goods-box normal-table" id="配件物料">
            <thead>
            <tr>
                <th>序号</th>
                <th class="name">配件物料</th>
                <th class="number per-hour-price">售价</th>
                <th class="unit">数量</th>
                <th class="price">金额</th>
                <th class="number">优惠</th>
            </tr>
            </thead>
            <tbody>
                <#list orderGoodsList as item>
                <tr class="goods-tr">
                    <td>${item_index+1}</td>
                    <td>${item.goodsName}<#if item.goodsNote><i class="note">(${item.goodsNote})</i></#if></td>
                    <td>${item.goodsPrice}</td>
                    <td>${item.goodsNumber} ${item.measureUnit}</td>
                    <td>${item.goodsAmount}</td>
                    <td>${item.discount}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </#if>
        <ul class="box total-box">
            <li id="合计">合计</li>
            <li class="normal-fee" id="配件总费用">配件总费用：<i class="text">${orderInfo.goodsAmount}元</i></li>
            <li id="总优惠">总优惠：<i class="text">${orderInfo.goodsDiscount + orderInfo.orderDiscountAmount}元</i></li>
            <li class="border-left fr">
                <ul>
                    <li id="应收金额">总计：<i class="text money">${orderInfo.goodsAmount-orderInfo.goodsDiscount}元</i></li>
                    <li id="应收金额大写"><i class="text uppercase"></i></li>
                </ul>
            </li>
        </ul>
        <ul class="box footer-box">
            <li class="fl" id="打印时间">
                <i class="five">打印时间：</i><i class="print-time text">${.now?string("yyyy-MM-dd HH:mm")}</i>
            </li>
            <li class="fr">
                <i class="four">客户签字</i><i class="sign-text text"></i><i class="space year-space"></i><i>年</i><i
                    class="space"></i><i>月</i><i class="space"></i><i>日</i>
            </li>
        </ul>
    <#if orderInfo.postscript>
        <ul class="box footer-box mark-box">
            <li>
                备注：${orderInfo.postscript}
            </li>
        </ul>
    </#if>
    </article>
    <footer>
        <ol id="提示语">
            <li>
                该单项目及金额经双方核实，客户签字生效
            </li>
        </ol>
    </footer>
</section>
</body>
</html>
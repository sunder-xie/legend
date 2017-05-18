<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>试车单打印</title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print.css?0c3994b5134160a03fdfc1ddc44657ff"/>
    <script src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script src="${BASE_PATH}/static/js/common/print/printDisplay.js?8d8402217e141c345ee32115cf794361"></script>
    <script>
        $(document).ready(function ($) {
            window.print();
        });
    </script>
</head>
<body class="A4">
<#--试车单-->
<section class="a-main print-box car-test-box <#if fontStyle == 1> font-12</#if>" data-print="car-test">
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
        <h2 class="text" id="编号">（试车单） NO：<i class="text">${orderInfo.orderSn}</i></h2>
    </div><div class="contact-box">
        <h3 id="地址" ><i>地址：</i><i class="address text">${shop.address}</i></h3>
        <h3 id="电话">电话：<i class="text">${shop.tel}</i></h3>
    </div>
    </header>
    <article>
        <ul class="box"></ul>
        <div class="info-box box">
            <div class="left">
                <ul>
                    <li id="车牌"><i>车牌：</i><i class="text">${orderInfo.carLicense}</i></li>
                    <li id="工单号"><i>工单号：</i><i class="text">${orderInfo.orderSn}</i></li>
                </ul>
                <ul>
                    <li id="开始时间">开始时间：<i class="space"></i><i>月</i><i class="space"></i><i>日</i><i class="space"></i><i>时</i><i class="space"></i><i>分</i></li>
                    <li id="出厂里程">出厂里程：</li>
                </ul>
                <ul>
                    <li id="结束时间">结束时间：<i class="space"></i><i>月</i><i class="space"></i><i>日</i><i class="space"></i><i>时</i><i class="space"></i><i>分</i></li>
                    <li id="回厂里程">回厂里程：</li>
                </ul>
            </div><div class="right border-left ryb-box">
            <div class="ryb-title">燃油表</div>
            <div class="ran_you_biao">
                <span class="xline"></span>
                <span class="yline"></span>
                <span class="txt1 text">1</span>
                <span class="txt2 text">3/4</span>
                <span class="txt3 text">1/2</span>
                <span class="txt4 text">1/4</span>
                <span class="txt5 text">0</span>
            </div>
        </div>
        </div>
        <div class="box height-70">
            <h2 class="title">试车原因</h2>
        </div>
        <ol class="box detail-box">
            <li>试车协议</li>
            <li>1.该单项目及金额经双方核实，客户签字生效</li>
            <li>2.客户自带配件到我厂维修的项目本厂对此不作保修</li>
        </ol>
        <div class="box height-70">
            <h2 class="title">试车结果</h2>
        </div>
        <div class="box note-box">
            <h2 class="title">备注</h2>
        </div>
        <ul class="box footer-box">
            <li style="width: 52mm;">打印时间：${.now?string("yyyy-MM-dd HH:mm")}</li>
            <li><i>试车员（签字）</i><i class="width-23 border-bottom"></i></li>
            <li style="margin-left: 3mm"><i>本人同意上述条款进行试车（客户签字）</i><i class="width-23 border-bottom"></i></li>
        </ul>
    </article>
</section>
</body>
</html>

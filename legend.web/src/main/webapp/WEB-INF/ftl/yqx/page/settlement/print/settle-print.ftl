<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>结算单打印</title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print.css?0c3994b5134160a03fdfc1ddc44657ff"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/print/print-setting.css?15821b0c071ace199da1b907de7bc227"/>
    <script src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script src="${BASE_PATH}/static/js/common/print/printDisplay.js?8d8402217e141c345ee32115cf794361"></script>
    <script>
        $(document).ready(function ($) {
            // 字段显示等控制
            Components.printDisplay($('#configField').remove().val());
            window.print();
        });
    </script>
    <style>
        .card-box{
            border-bottom: 1px solid #000;
            overflow: hidden;
        }
        .card-box li,.card-box h3{
            height: 30px;
            line-height: 30px;
        }
    </style>
</head>
<body class="A4">
<#--结算单-->
<input value='${printConfigVO.configField}' type="hidden" id="configField">
<section class="a-main print-box settle-box <#if printConfigVO.fontStyle == 1> font-12</#if>" data-print="settle">
    <header>
    <#if printPostion == 1>
            <div class="title-center">
                <#if printTitle != null >
                    <img src="${printTitle}" alt="${shop.companyName}" width="445px" height="29px">
                <#else>
                    <h1 class="text" id="维修厂名称"><i class="text">${shop.companyName}</i></h1>
                </#if>
                <h2 class="text" id="编号">（结算单） NO：<i class="text">${orderInfo.orderSn}</i></h2>
            </div>
    <#else>
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
            <h2 class="text" id="编号">（结算单） NO：<i class="text">${orderInfo.orderSn}</i></h2>
        </div><div class="contact-box">
        <h3 id="地址"><i>地址：</i><i class="address text">${shop.address}</i></h3>

        <h3 id="电话">电话：<i class="text">${shop.tel}</i></h3>
        </div>
    </#if>
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
        <div class="car-info box">
            <ul>
                <li id="车牌">
                    <i class="key">车牌：</i><i class="text">${orderInfo.carLicense}</i>
                </li><li id="车辆颜色">
                    <i class="key">车辆颜色：</i><i class="text">${orderInfo.carColor}</i>
                </li><li id="行驶里程">
                    <i class="key">行驶里程数：</i><i class="text">${orderInfo.mileage} km</i>
                </li><li id="承保公司">
                    <i class="key">承保公司：</i><i class="text">${orderInfo.insuranceCompanyName}</i>
                </li><li id="发动机号">
                    <i class="key">发动机号：</i><i class="text">${orderInfo.engineNo}</i>
                </li><li id="VIN码">
                    <i class="key">VIN码：</i><i class="text">${orderInfo.vin}</i>
                </li><li id="油表油量">
                    <i class="key">油表油量：</i><i class="text">${orderInfo.oilMeter}</i>
                </li><li id="下次保养里程">
                    <i class="key">下次保养里程：</i><i class="text"><#if orderInfo.upkeepMileage>${orderInfo.upkeepMileage}
                    km</#if></i>
                </li><li id="车辆型号">
                    <i class="key">车辆型号：</i><i class="text">${orderInfo.carInfo}</i>
                </li>
            </ul>
        </div>
        <ul class="box split-vertical">
            <li>
                <ul>
                    <li id="维修类别">
                        <i>维修类别：</i><i class="text">${orderInfo.orderTypeName}</i>
                    </li>
                    <li id="进场方式">
                        <i>进场方式：</i><i class="text"></i>
                    </li>
                </ul>
            </li>
            <li class="checkbox-box">
                <ul>
                    <li id="洗车">
                        <div class="checkbox"></div><i class="text">
                        洗车
                    </i>
                    </li>
                    <li id="旧件带回">
                        <div class="checkbox"></div><i class="text">
                        旧件带回
                    </i>
                    </li>
                </ul>
            </li>
        </ul>
    <#if orderServicesList1>
        <table class="box service-box normal-table" id="服务项目">
            <thead>
            <tr>
                <th>序号</th>
                <th class="name">服务项目</th>
                <th class="number per-hour-price">工时费</th>
                <th class="unit">工时</th>
                <th class="price">金额</th>
                <th class="number">优惠</th>
            </tr>
            </thead>
            <tbody>
                <#list orderServicesList1 as item>
                <tr class="service-tr">
                    <td>${item_index + 1}</td>
                    <td>${item.serviceName}<#if item.serviceNote><i class="note">(${item.serviceNote})</i></#if></td>
                    <td>${item.servicePrice}</td>
                    <td>${item.serviceHour}</td>
                    <td>${item.serviceAmount}</td>
                    <td>${item.discount}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </#if>
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
    <#if orderServicesList2>
        <table class="box addon-box normal-table" id="附加项目">
            <thead>
            <tr>
                <th>序号</th>
                <th class="name">附加项目</th>
                <th class="note">附加备注</th>
                <th class="price">金额</th>
                <th class="number">优惠</th>
            </tr>
            </thead>
            <#list orderServicesList2 as item>
                <tr class="addon-tr">
                    <td>${item_index+1}</td>
                    <td>${item.serviceName}<#if item.serviceNote><i class="note">(${item.serviceNote})</i></#if></td>
                    <td>${item.serviceAmount}</td>
                    <td>${item.discount}</td>
                </tr>
            </#list>
        </table>
    </#if>
        <#assign count = 0/>
        <table class="box merge-table hide addon-box">
            <thead>
            <tr>
                <th>序号</th>
                <th class="name">名称</th>
                <th class="measure">数量</th>
                <th class="price">金额</th>
                <th class="number">优惠</th>
            </tr>
            </thead>
        <#list orderServicesList1 as item>
            <tr class="service-tr">
                <td>${count + 1}</td>
                <td>${item.serviceName}<#if item.serviceNote><i class="note">(${item.serviceNote})</i></#if></td>
                <td class="measure">${item.serviceHour} 工时</td>
                <td>${item.serviceAmount}</td>
                <td>${item.discount}</td>
            </tr>
            <#assign count = count + 1/>
        </#list>
        <#list orderGoodsList as item>
        <tr class="goods-tr">
            <td>${count+1}</td>
            <td>${item.goodsName}<#if item.goodsNote><i class="note">(${item.goodsNote})</i></#if></td>
            <td class="measure">${item.goodsNumber} ${item.measureUnit}</td>
            <td>${item.goodsAmount}</td>
            <td>${item.discount}</td>
        </tr>
            <#assign count = count + 1/>
        </#list>
        <#--附加项目-->
        <#list orderServicesList2 as item>
            <tr class="addon-tr">
                <td>${count+1}</td>
                <td>${item.serviceName}<#if item.serviceNote><i class="note">(${item.serviceNote})</i></#if></td>
                <td class="measure"></td>
                <td>${item.serviceAmount}</td>
                <td>${item.discount}</td>
            </tr>
            <#assign count = count + 1/>
        </#list>
        </table>
        <ul class="box total-box">
            <li id="合计">合计</li>
            <li class="hide merge-fee" id="总费用">总费用：<i class="text">${orderInfo.serviceAmount + orderInfo.goodsAmount + orderInfo.feeAmount}元</i></li>
            <li class="normal-fee" id="服务总费用">服务总费用：<i class="text">${orderInfo.serviceAmount}元</i></li>
            <li class="normal-fee" id="配件总费用">配件总费用：<i class="text">${orderInfo.goodsAmount}元</i></li>
            <li class="normal-fee" id="附加总费用">附加总费用：<i class="text">${orderInfo.feeAmount}元</i></li>
            <li id="总优惠">总优惠：<i class="text">${orderInfo.serviceDiscount + orderInfo.goodsDiscount + orderInfo.feeDiscount + orderInfo.orderDiscountAmount}元</i></li>
            <li class="border-left">
                <ul>
                    <#if orderInfo.payStatus == 0 >
                        <li id="应收金额">总计：<i class="text">${orderInfo.orderAmount}元</i></li>
                        <li id="应收金额大写"><i class="text">${orderAmountUpper}</i></li>
                    <#else>
                        <li id="应收金额">应收金额：<i class="text">${debitBill.receivableAmount}元</i></li>
                        <li id="应收金额大写"><i class="text">${receivableAmountUpper}</i></li>
                    </#if>

                </ul>
            </li>
        </ul>
        <#if debitBillFlowList?exists || orderInfo.signAmount gt 0>
        <ul class="box cash-info">
        <#if orderInfo.signAmount gt 0>
            <li>挂账金额：${orderInfo.signAmount}
            </li>
        </#if>
        <li class="center">实收金额：${debitBill.paidAmount}<#if debitBillFlowList?exists>(<#list debitBillFlowList as item>&nbsp;<#if item.flowStatus == 0><#if item.flowType == 1>
                冲红</#if>${item.paymentName}<#else>优惠</#if>${item.payAmount}<#if item_has_next>+</#if></#list>)</#if>
        </li>
        </ul>
        </#if>
        <#if memberCards>
        <div class="card-box" id="showAccount">
            <h3 class="fl">会员卡余额：</h3>
            <ul class="fl">
                <#list memberCards as memberCard>
                    <li>${memberCard.cardTypeName} ${memberCard.balance}元</li>
                </#list>
            </ul>
        </div>
        </#if>
        <#if comboServiceList>
            <ul class="box cash-info">
                <li class="center">
                    <#list comboServiceList as combo>${combo.serviceName}剩余${combo.leftServiceCount}次<#if combo_has_next>，</#if></#list>
                </li>
            </ul>
        </#if>
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
    <#if printPostion == 1>
    <div class="footer-address">
        <h3 id="地址"><i>地址：</i><i class="address1 text">${shop.address}</i></h3>
        <h3 id="电话">电话：<i class="text">${shop.tel}</i></h3>
    </div>
    </#if>
        <ol id="提示语">
            <li>
                1.该单项目及金额经双方核实，客户签字生效
            </li>
        </ol>
    </footer>
</section>
</body>
</html>
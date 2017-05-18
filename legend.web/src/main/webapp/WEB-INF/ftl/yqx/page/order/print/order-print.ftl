<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>派工单打印</title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print.css?0c3994b5134160a03fdfc1ddc44657ff"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/print/print-order.css?9dcbc7be883ae8bc24691406d844e26d"/>
    <script src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script src="${BASE_PATH}/static/js/common/print/printDisplay.js?8d8402217e141c345ee32115cf794361"></script>
    <script>
        $(document).ready(function ($) {
            // 字段显示等控制
            Components.printDisplay( $('#configField').remove().val() );
            // 大写金额
            $('#应收金额大写 .text').text( Components.digitUppercaseCN( $('#应收金额 .text').text().replace('元', '') ) );
            window.print();
        });
    </script>
</head>
<body class="A4">
<input value='${printConfigVO.configField}'' hidden id="configField">
<#--派工单-->
<section class="a-main print-box dispatch-box <#if printConfigVO.fontStyle == 1> font-12</#if>" data-print="dispatch">
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
        <h2 class="text" id="编号">（派工单） NO：<i class="text">${orderInfo.orderSn}</i></h2>
    </div><div class="contact-box">
        <h3 id="地址" ><i>地址：</i><i class="address text">${shop.address}</i></h3>
        <h3 id="电话">电话：<i class="text">${shop.tel}</i></h3>
    </div>
    </header>
    <article>
        <ul class="box order-info">
            <li id="开单时间" class="time"><i class="key">开单时间：</i><i class="text">${orderInfo.createTimeStr}</i></li>
            <li id="预完工时间" class="time"><i class="key">预完工时间：</i><i class="text"><#if orderInfo.expectedTime>${orderInfo.expectedTime?string("yyyy-MM-dd HH:mm")}</#if></i></li>
            <li id="服务顾问" class="fr server"><i class="key">服务顾问：</i><i class="text">${orderInfo.receiverName}</i></li>
        </ul>
        <ul class="box customer-box">
            <li class="text customer" id="车主"><i class="text"><#if orderInfo.customerName || orderInfo.customerMobile>${orderInfo.customerName} ${orderInfo.customerMobile}(车主)</#if></i></li>
            <li class="text relative-customer" id="联系人">
                <i class="text">
                     <#--联系人和车主姓名\手机都一致的时候不显示联系人-->
                <#if orderInfo.contactName != orderInfo.customerName || orderInfo.customerMobile != orderInfo.contactMobile>
                <#if orderInfo.contactName || orderInfo.contactMobile>
                    ${orderInfo.contactName} ${orderInfo.contactMobile}(联系人)
                </#if>
                </#if>
                </i>
            </li>
            <li class="customer-company" id="单位"><i class="key">客户单位：</i><i class="text">${customerCar.company}</i></li>
        </ul>
        <div class="car-info box">
            <ul>
                <li id="车牌">
                    <i class="key">车牌：</i><i class="text">${customerCar.license}</i>
                </li><li id="车辆颜色">
                    <i class="key">车辆颜色：</i><i class="text">${orderInfo.carColor}</i>
                </li><li id="行驶里程">
                    <i class="key">行驶里程数：</i><i class="text">${orderInfo.mileage} km</i>
                </li><li id="承保公司">
                    <i class="key">承保公司：</i><i class="text">${orderInfo.insuranceCompanyName}</i>
                </li><li id="发动机号">
                    <i class="key">发动机号：</i><i class="text">${customerCar.engineNo}</i>
                </li><li id="VIN码">
                    <i class="key">VIN码：</i><i class="text">${customerCar.vin}</i>
                </li><li id="油表油量">
                    <i class="key">油表油量：</i><i class="text">${orderInfo.oilMeter}</i>
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
            <li>
                <ul>
                    <li id="客户描述">
                        客户描述：<i class="text"></i>
                    </li>
                </ul>
            </li>
        </ul>
        <#if orderServicesList1>
        <table class="box service-box" id="服务项目">
            <thead>
            <tr>
                <th>序号</th>
                <th class="name">服务项目</th>
                <th class="number per-hour-price">工时费</th>
                <th class="unit">工时</th>
                <th class="price">金额</th>
                <th class="number">优惠</th>
                <th class="people">指派人员</th>
                <th style="width: 9mm;">复检</th>
            </tr>
            </thead>
            <tbody>
            <#list orderServicesList1 as item>
                <tr class="service-tr">
                    <td>${item_index + 1}</td>
                    <td>${item.serviceName}<#if item.serviceNote>(${item.serviceNote})</#if></td>
                    <td>${item.servicePrice}</td>
                    <td>${item.serviceHour}</td>
                    <td>${item.serviceAmount}</td>
                    <td>${item.discount}</td>
                    <td style="text-align: center">${item.workerNames}</td>
                    <td></td>
                </tr>
            </#list>
            </tbody>
        </table>
        </#if>

    <#if orderGoodsList>
        <table class="box goods-box" id="配件物料">
            <thead>
            <tr>
                <th>序号</th>
                <th class="name">配件物料</th>
                <th class="number">售价</th>
                <th class="unit">数量</th>
                <th class="price">金额</th>
                <th class="number">优惠</th>
            </tr>
            </thead>
            <tbody>
            <#list orderGoodsList as item>
            <tr class="goods-tr">
                <td>${item_index+1}</td>
                <td>${item.goodsName}<#if item.goodsNote>(${item.goodsNote})</#if></td>
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

        <table class="box addon-box" id="附加项目">
            <thead>
            <tr>
                <th>序号</th>
                <th class="name">附加项目</th>
                <th class="price">金额</th>
                <th class="number">优惠</th>
            </tr>
            </thead>
            <#list orderServicesList2 as item>
            <tr class="addon-tr">
                <td>${item_index+1}</td>
                <td>${item.serviceName}<#if item.serviceNote>(${item.serviceNote})</#if></td>
                <td>${item.serviceAmount}</td>
                <td>${item.discount}</td>
            </tr>
            </#list>
        </table>
        </#if>
        <ul class="box total-box">
            <li>合计</li>
            <li id="服务总费用">服务总费用：<i class="text">${orderInfo.serviceAmount}元</i></li>
            <li id="配件总费用">配件总费用：<i class="text">${orderInfo.goodsAmount}元</i></li>
            <li id="附加总费用">附加总费用：<i class="text">${orderInfo.feeAmount}元</i></li>
            <li id="总优惠">总优惠：<i class="text">${orderInfo.serviceDiscount + orderInfo.goodsDiscount + orderInfo.feeDiscount}元</i></li>
            <li class="border-left">
                <ul>
                    <li id="应收金额">总计：<i class="text">${orderInfo.orderAmount}元</i></li>
                    <li id="应收金额大写"><i class="text"></i></li>
                </ul>
            </li>
        </ul>
        <ul class="box service-note-box">
            <li id="fixRecord">
                <i>维修项目修改记录：</i><i class="text">
            </i>
            </li>
            <li id="维修建议">
                <i>维修建议：</i><i class="text">
            </i>
            </li>
        </ul>
        <ul class="box check-box" id="检验签字">
            <li>
                检验签字
            </li>
            <li>
                机电
            </li>
            <li>
                钣喷
            </li>
            <li>
                美容
            </li>
            <li>
                总检
            </li>
        </ul>
        <ul class="box footer-box">
            <li>
                <i class="five">打印时间：</i><i class="print-time text">${.now?string("yyyy-MM-dd HH:mm")}</i>
                <i class="four">客户签字</i><i class="sign-text"></i><i class="space year-space"></i><i>年</i><i class="space"></i><i>月</i><i class="space"></i><i>日</i>
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
                1.此单一式三联，客户白联用户提车，红联用户系统开单，黄联用户车辆检修。
            </li>
            <li>
                2.友情提示：请您将车内贵重物品及现金带走，如有遗失本公司盖不承担责任。
            </li>
            <li>
                3.维修项目以工单为准。
            </li>
        </ol>
    </footer>
</section>
</body>
</html>
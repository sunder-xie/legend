<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title></title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/base.css?8bb0f9a84cfb070249dcc1186f9f6a25"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print_common.css?e174653fe93fff7de684a409310c97d7"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/order_print_blank.css?f9cc77af03e07dee8e01b589d4a298f8"/>
    <script type="text/javascript" src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script>
        $(document).ready(function ($) {
            window.print();
        });
    </script>
</head>
<body>
<div id="hdr">
    <p class="tq_print_logo"><img src="${BASE_PATH}/static/img/common/print/print_logo.png"/></p>
    <p class="blankPage"></p>

    <p class="company">${shop.companyName}<span></span></p>

    <p class="tit">结 算 单</p>
    <span class="orderID">单号：${orderInfo.orderSn}</span>
    <span class="record_date">打印日期：&nbsp;${.now?string("yyyy-MM-dd HH:mm")}</span>
    <span class="count">
    </span>
</div>

<table class="costomInfoTable" cellspacing="0" cellpadding="0">
    <tr>
        <td class="dInfo">
            <div class="td" style="width:85px;">车牌：</div>
            <div class="td" style="width:104px;text-align: center;">&nbsp;${orderInfo.carLicense}</div>
            <div class="td" style="width:85px;">车主姓名：</div>
            <div class="td" style="width:149px;text-align: center;">&nbsp;${orderInfo.customerName}</div>
            <div class="td" style="width:85px;">联系人：</div>
            <div class="td" style="width:149px;text-align: center;">&nbsp;${orderInfo.contactName}</div>
            <div class="td" style="width:85px;">联系电话：</div>
            <div class="td" style="width:226px;text-align: center;">&nbsp;${orderInfo.contactMobile}</div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:85px;">维修类别：</div>
            <div class="td" style="width:104px; text-align: center;">${orderInfo.orderTypeName}</div>
            <div class="td" style="width:85px;">开单日期：</div>
            <div class="td"
                 style="width:149px; text-align: center;"><#if orderInfo.createTime>${orderInfo.createTime?string("yyyy-MM-dd HH:mm")}</#if></div>
            <div class="td" style="width:85px;">预计完工：</div>
            <div class="td"
                 style="width:149px; text-align: center;"><#if orderInfo.finishTime>${orderInfo.finishTime?string("yyyy-MM-dd HH:mm")}</#if></div>
            <div class="td" style="width:85px;">车辆型号：</div>
            <div class="td" style="width:149px; text-align: center;">${orderInfo.carInfo}</div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:86px;">客户单位：</div>
            <div class="td" style="width:226px;display:block;white-space:nowrap">${orderInfo.company}</div>
        </td>
    </tr>
</table>

<#if orderServicesList1>
<table class="orderpp" cellspacing="0" cellpadding="0">
    <tr class="service-item">
        <th width="35" class="sequence">序号</th>
        <th width="375">服务名称</th>
        <th width="100">总工时费</th>
        <th width="50">优惠</th>
    </tr>
<#list orderServicesList1 as orderServices>
    <tr>
        <td class="sequence">${orderServices_index+1}</td>
        <td>&nbsp;${orderServices.serviceName}<#if orderServices.serviceNote>(${orderServices.serviceNote})</#if></td>
        <td class="center">&nbsp;${orderServices.serviceAmount}</td>
        <td class="center">&nbsp;${orderServices.discount}</td>
    </tr>
</#list>
</table>
</#if>

<#if orderGoodsList>
<table class="orderpp" cellspacing="0" cellpadding="0">
    <caption class="item_tit">材料配件清单</caption>
    <tr class="accessories-material">
        <th width="35" class="sequence">序号</th>
        <th width="375">配件名称</th>
        <th width="50">单价</th>
        <th width="50">数量</th>
        <th width="50">总价</th>
    </tr>
<#list orderGoodsList as orderGoods>
    <tr>
        <td class="center">&nbsp;${orderGoods_index+1}</td>
        <td>&nbsp;${orderGoods.goodsName}<#if orderGoods.goodsNote>(${orderGoods.goodsNote})</#if></td>
        <td class="center">&nbsp;${orderGoods.goodsPrice}</td>
        <td class="center">&nbsp;${orderGoods.goodsNumber}</td>
        <td class="center">&nbsp;${orderGoods.goodsAmount}</td>
    </tr>
</#list>
</table>
</#if>

<#if orderServicesList2>
<table class="orderpp" cellspacing="0" cellpadding="0">
    <caption class="item_tit">附加费用清单</caption>
    <tr class="extra-costs">
        <th width="35" class="sequence">序号</th>
        <th width="329">附加费用</th>
        <th width="130">金额</th>
        <th width="80">优惠</th>
    </tr>
    <#list orderServicesList2 as orderServices>
        <tr class="extra-costs">
            <td class="sequence">${orderServices_index+1}</td>
            <td class="center">&nbsp;${orderServices.serviceName}</td>
            <td class="center">&nbsp;${orderServices.serviceAmount}</td>
            <td class="center">&nbsp;${orderServices.discount}</td>
        </tr>
    </#list>
</table>
</#if>

<table class="totalTable" cellspacing="0" cellpadding="0">
    <caption class="item_tit">费用清单</caption>
    <tr>
        <td>&nbsp;工时费：${orderInfo.serviceAmount}<span class="fee"></span></td>
        <td>&nbsp;实收工时费：${orderInfo.serviceAmount-orderInfo.serviceDiscount}<span class="fee"></span></td>
        <td>&nbsp;材料费合计：${orderInfo.goodsAmount}<span class="fee"></span></td>
        <td>&nbsp;实收材料费：${orderInfo.goodsAmount-orderInfo.goodsDiscount}<span class="fee"></span></td>
    </tr>

<#if debitBill>
    <tr>
        <td colspan="4">
            <label class="zfy">
            &nbsp;应收金额：=&nbsp;总计： ${orderInfo.orderAmount}元 <#if orderInfo.taxAmount gt 0>+ 费用：${orderInfo.taxAmount}元 </#if>- 工单优惠：${orderInfo.orderDiscountAmount}元
            </label>
        </td>
    </tr>
    <tr>
        <td colspan="4">
            <label class="zfy">
                &nbsp;应收金额：&nbsp;<span>${receivableAmountUpper}(${debitBill.receivableAmount})</span>
            </label>
        </td>
    </tr>
<#else>
    <tr>
        <td colspan="4">
            <label class="zfy">
                &nbsp;总计：&nbsp;<span>${orderAmountUpper}(${orderInfo.orderAmount})</span>
            </label>
        </td>
    </tr>
</#if>
    <#if orderInfo.postscript != null >
        <tr class="t_tr3">
            <td colspan="4">&nbsp;备注：${orderInfo.postscript}</td>
        </tr>
    <#else>
        <tr>
            <td colspan="4">&nbsp;备注：</td>
        </tr>
    </#if>
    <tr class="t_tr4">
        <td colspan="4">
            <div style="padding:10px 0 0 10px;">
                <p>经客户代表检测，本次维修质量合格，维修项目和维修材料费正确</p>
                <p>请客户代表（取车人）签字确认：</p>
            </div>
        </td>
    </tr>

</table>
<div class="signature">
    <div style="width:390px;">
        <span>下次保养里程：</span>
        <span><#if orderInfo.upkeepMileage>${orderInfo.upkeepMileage}<#else>    </#if> km</span>
    </div>
    <div style="width:390px;">
        <span>&nbsp;服务顾问：</span><span>${orderInfo.receiverName}</span>
    </div><br>
    <div style="width:390px;">
        <span>&nbsp;门店名称：${shop.name}</span>
    </div>
    <div style="width:500px;">
        <span>&nbsp;地址：${shop.provinceName}${shop.cityName}${shop.districtName}<#if shop.streetName!="其它">${shop.streetName}</#if>${shop.address}</span>
    </div>
    <div style="width:390px;">
        <span>&nbsp;座机电话：${shop.tel}</span>
    </div>
</div>

<div id="ftr">

</div>
</body>
</html>

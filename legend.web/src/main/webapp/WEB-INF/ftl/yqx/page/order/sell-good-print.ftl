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
    <script src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
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

    <p class="company">${shop.companyName}</p>

    <p class="tit">销售单
    <#if orderInfo.payStatus == 1>
        <span>[已挂账]</span>
    </#if>
    </p>
    <span class="orderID">单号：${orderInfo.orderSn}</span>
    <span class="record_date">打印日期：&nbsp;${.now?string("yyyy-MM-dd HH:mm")}</span>
    <span class="count">
    <#--第&nbsp;&nbsp;次来厂-->
    </span>
</div>
<table class="costomInfoTable" cellspacing="0" cellpadding="0">
    <tr>
        <td class="dInfo">
            <div class="td" style="width:85px;">
                车牌：
            </div><div class="td" style="width:195px;">
                ${orderInfo.carLicense}
            </div><div class="td" style="width:85px;">
            联系人：
            </div><div class="td" style="width:195px;">
            ${orderInfo.contactName}
            </div><div class="td" style="width:103px;">联系电话：
        </div><div class="td" style="width:148px;">${orderInfo.contactMobile}</div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:85px;">客户单位：</div><div class="td" style="width:195px;display:block;white-space:nowrap">${customerCar.company}</div>
        </td>
    </tr>
</table>
<table class="orderpp" cellspacing="0" cellpadding="0">
    <tr class="accessories-material">
        <th width="35" class="sequence">序号</th>
        <th width="447">配件名称</th>
        <th width="50">数量</th>
        <th width="50">单位</th>
        <th width="120">单价</th>
        <th width="150">总价</th>
        <th width="120">优惠</th>
    </tr>
<#list orderGoodsList as orderGoods>
    <tr>
        <td class="center">&nbsp;${orderGoods_index+1}</td>
        <td>&nbsp;${orderGoods.goodsName}<#if orderGoods.goodsNote>(${orderGoods.goodsNote})</#if></td>
        <td class="center">&nbsp;${orderGoods.goodsNumber}</td>
        <td class="center">&nbsp;${orderGoods.measureUnit}</td>
        <td class="center">&nbsp;${orderGoods.goodsPrice}</td>
        <td class="center">&nbsp;${orderGoods.goodsAmount}</td>
        <td class="center">&nbsp;${orderGoods.discount}</td>
    </tr>
</#list>
    <tr>
        <td height="30" colspan="7"><div class="total">配件费合计：<span>${orderInfo.goodsAmount}</span></div><div
                class="total">优惠合计：<span>${orderInfo.goodsDiscount}</span></div><div
                class="total">客户应付配件费合计：<span>${orderInfo.goodsAmount-orderInfo.goodsDiscount}</span></div>
        </td>
    </tr>
</table>
<table id="tab" cellspacing="0" cellpadding="0">
    <tr class="t_tr3">
        <td width="489" colspan="10">&nbsp;备注：${orderInfo.postscript}</td>
    </tr>
</table>
<table class="information" cellspacing="0" cellpadding="0">
    <caption class="item_tit">门店信息</caption>
    <tr>
        <td>&nbsp;门店名称：${shop.name}</td>
        <td>&nbsp;座机电话：${shop.tel}</td>
    </tr>
    <tr>
        <td colspan="2">&nbsp;地址：${shop.provinceName}${shop.cityName}${shop.districtName}${shop.streetName}${shop.address}</td>
    </tr>
    <tr class="t_tr4">
        <td colspan="2">
            <div style="padding:10px 0 0 10px;">
                <p>提示：</p>
                <p>${conf.settleComment}</p>
            </div>
        </td>
    </tr>
</table>
<div class="signature">
    <div style="width:200px;">
        <span>下次保养里程：</span>
        <span>${orderInfo.upkeepMileage} km</span>
    </div><div style="width:390px;">
        <span>服务顾问：</span><span class="sign">${orderInfo.receiverName}</span>
    </div><div style="width:390px;">
        <span>客户签字：</span><span class="sign"></span>
        <span class="ymd">年　　月　　日</span>
    </div>
</div>
</body>
</html>

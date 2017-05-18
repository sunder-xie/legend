<!DOCTYPE Html>
<html>
<head>
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
    <p class="company">${shop.companyName}<span></span></p>
    <p class="tit">派工单</p>
    <span class="orderID">工单编号：${orderInfo.orderSn}</span>
    <span class="record_date">打印日期：&nbsp;${.now?string("yyyy-MM-dd HH:mm")}</span>
</div>
<table class="costomInfoTable" cellspacing="0" cellpadding="0">
    <tr>
        <td class="dInfo">
            <div class="td" style="width:88px; text-align: center;">车牌</div><div class="td" style="width:154px; text-align: center;">${orderInfo.carLicense}</div>
            <div class="td" style="width:88px; text-align: center;">车主姓名</div>
            <div class="td" style="width:154px; text-align: center;">${orderInfo.customerName}</div>
            <div class="td" style="width:88px; text-align: center;">联 系 人</div>
            <div class="td" style="width:154px; text-align: center;">${orderInfo.contactName}</div>
            <div class="td" style="width:88px; text-align: center;">联系电话</div>
            <div class="td" style="width:154px; text-align: center;">${orderInfo.contactMobile}</div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:88px; text-align: center;">维修类别</div>
            <div class="td" style="width:154px; text-align: center;">${orderInfo.orderTypeName}</div>
            <div class="td" style="width:88px; text-align: center;">开单日期</div>
            <div class="td"
                 style="width:154px; text-align: center;"><#if orderInfo.createTime>${orderInfo.createTime?string("yyyy-MM-dd")}</#if></div>
            <div class="td" style="width:88px; text-align: center;">预完工期</div>
            <div class="td"
                 style="width:154px; text-align: center;"><#if orderInfo.expectedTime>${orderInfo.expectedTime?string("yyyy-MM-dd")}</#if></div>
            <div class="td" style="width:88px; text-align: center;">车辆型号</div>
            <div class="td" style="width:154px; text-align: center;">${orderInfo.carInfo}</div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:86px;">客户单位：</div>
            <div class="td" style="width:226px;display:block;white-space:nowrap">${orderInfo.company}</div>
        </td>
    </tr>
</table>
<table class="orderpp" cellspacing="0" cellpadding="0">

    <tr class="service-item">
        <th width="35" class="sequence">序号</th>
        <th width="375">服务名称</th>
        <th width="50">工时</th>
        <th width="80">维修工</th>
        <th width="80">组长复检</th>
        <th width="80">主管复检</th>
    </tr>
<#if orderServicesList1>
    <#list orderServicesList1 as orderServices>
        <tr>
            <td class="sequence">${orderServices_index+1}</td>
            <td>${orderServices.serviceName}</td>
            <td class="center">&nbsp;${orderServices.serviceHour}</td>
            <td class="center">&nbsp;${orderServices.workerNames}</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
    </#list>
<#else>
    <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</#if>
</table>
<table class="orderpp" cellspacing="0" cellpadding="0">
    <tr class="accessories-material">
        <th width="35" class="sequence">序号</th>
        <th width="447">配件名称</th>
        <th width="50">数量</th>
        <th width="50">单位</th>
    </tr>
<#if orderGoodsList>
    <#list orderGoodsList as orderGoods>
        <tr>
            <td class="center">&nbsp;${orderGoods_index+1}</td>
            <td>&nbsp;${orderGoods.goodsName}<#if orderGoods.goodsNote>(${orderGoods.goodsNote})</#if></td>
            <td class="center">&nbsp;${orderGoods.goodsNumber}</td>
            <td class="center">&nbsp;${orderGoods.measureUnit}</td>
        </tr>
    </#list>
<#else>
    <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</#if>
</table>
<table id="tab" cellspacing="0" cellpadding="0">
    <tr class="t_tr3">
        <td width="489" colspan="10">&nbsp;用户描述：</td>
        <td width="489" colspan="10">&nbsp;维修项目修改记录：</td>
    </tr>
    <tr class="t_tr3">
        <td width="978" colspan="20">&nbsp;维修建议：
        </td>
    </tr>
</table>
<div class="signature">
    <div style="width:390px;">
        <span>服务顾问：</span><span class="sign">${orderInfo.receiverName}</span>
    </div>
    <div style="width:390px;">
        <span>客户签字：</span><span class="sign"></span>
    </div>
    <span class="ymd">年　　月　　日</span>
</div>
</body>
</html>

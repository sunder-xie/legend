<!DOCTYPE HTML>
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
    <p class="blankPage"></p>

    <p class="company">${shop.companyName}</p>

    <p class="tit">维修工单
    <#if orderInfo.payStatus == 1>
        <span>[已挂账]</span>
    <#else>
        <span>[${orderInfo.orderStatusClientName}]</span>
    </#if>
    </p>
    <#if workOrder && workOrder.workOrderSn>
        <img class="worksn-img" src="${BASE_PATH}/pub/createImg?text=${workOrder.workOrderSn}&width=350&height=50"></img>
    </#if>
    <span class="orderID">单号：${orderInfo.orderSn}<#if workOrder && workOrder.workOrderSn>&nbsp;&nbsp;&nbsp;施工单号：${workOrder.workOrderSn}</#if></span>
    <span class="record_date">打印日期：&nbsp;${.now?string("yyyy-MM-dd HH:mm")}</span>
    <span class="count">
    </span>
</div>
<table class="costomInfoTable" cellspacing="0" cellpadding="0">
    <tr>
        <td class="dInfo">
            <div class="td" style="width:85px;">车牌：</div>
            <div class="td" style="width:195px;text-align: center;">${orderInfo.carLicense}</div>
            <div class="td" style="width:103px;">开单日期：</div>
            <div class="td"
                 style="width:148px; text-align: center;"><#if orderInfo.createTime>${orderInfo.createTime?string("yyyy-MM-dd HH:mm")}</#if></div>
            <div class="td" style="width:120px;">期望交车日期：</div>
            <div class="td"
                 style="width:148px; text-align: center;">${orderInfo.expectTimeStr}</div>
            <div class="td" style="width:85px;">颜色：</div>
            <div class="td" style="width:80px; text-align: center;">${orderInfo.carColor}</div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:85px;">车辆厂牌：</div>
            <div class="td" style="width:195px;text-align: center;">${orderInfo.carBrand}</div>
            <div class="td" style="width:103px;">车辆型号：</div>
            <div class="td" style="width:148px; text-align: center;">${orderInfo.carInfo}</div>
            <div class="td" style="width:103px;">承保公司：</div>
            <div class="td" style="width:148px; text-align: center;">${orderInfo.insuranceCompanyName}</div>
            <div class="td" style="width:85px;">行驶里程：</div>
            <div class="td" style="width:100px; text-align: center;">${orderInfo.mileage}</div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:85px;">车　　主：</div>
            <div class="td" style="width:195px;text-align: center;">${orderInfo.customerName}</div>
            <div class="td" style="width:103px;">车主地址：</div>
            <div class="td" style="width:401px;">${orderInfo.customerAddress}</div>
            <div class="td" style="width:85px;">进厂方式：</div>
            <div class="td" style="width:100px;"></div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:85px;">联 系 人：</div>
            <div class="td" style="width:195px;text-align: center;">${orderInfo.contactName}</div>
            <div class="td" style="width:103px;">电　　话：</div>
            <div class="td" style="width:148px;">${orderInfo.contactMobile}</div>
            <div class="td" style="width:103px;">手　　机：</div>
            <div class="td" style="width:148px;">${orderInfo.customerMobile}</div>
            <div class="td" style="width:85px;">洗车&nbsp;<span class="_block"></span></div>
            <div class="td" style="width:100px;">旧件带回&nbsp;<span class="_block"></span></div>
        </td>
    </tr>
    <tr>
        <td class="dInfo">
            <div class="td" style="width:85px;">客户单位：</div>
            <div class="td" style="width:195px;display:block;white-space:nowrap">${customerCar.company}</div>
        </td>
    </tr>

</table>
<!-- 外观start -->
    <#include "yqx/tpl/order/static-precheck-tpl.ftl">
<!-- 外观end -->
<table class="orderpp" cellspacing="0" cellpadding="0">

    <tr class="service-item">
        <th width="35" class="sequence">序号</th>
        <th width="70">工种</th>
        <th width="375">维修内容</th>
        <th width="50">工时</th>
        <th width="80">工时费</th>
        <th width="80">优惠</th>
        <th width="80">指派人员</th>
        <th width="80">组长复检</th>
        <th width="80">主管复检</th>
    </tr>
<#list orderServicesList1 as orderServices>
    <tr>
        <td class="sequence">${orderServices_index+1}</td>
        <td class="center">${orderServices.serviceCatName}</td>
        <td>&nbsp;${orderServices.serviceName}<#if orderServices.serviceNote>(${orderServices.serviceNote})</#if></td>
        <td class="center">&nbsp;${orderServices.serviceHour}</td>
        <td class="center">&nbsp;${orderServices.serviceAmount}</td>
        <td class="center">&nbsp;${orderServices.discount}</td>
        <td class="center">&nbsp;${orderServices.workerNames}</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</#list>

    <tr>
        <td colspan="10"><p class="total">工时费合计：<span>${orderInfo.serviceAmount}</span></p>

            <p class="total">优惠合计：${orderInfo.serviceDiscount}<span></span></p>

            <p class="total">客户应付工时费合计：<span>${orderInfo.serviceAmount-orderInfo.serviceDiscount}</span></p></td>
    </tr>
</table>
<table class="orderpp" cellspacing="0" cellpadding="0">
    <tr class="accessories-material">
        <th width="35" class="sequence">序号</th>
    <#--<th class="col_2">零件号</th>-->
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
        <td class="center"></td>
        <td></td>
        <td class="center"></td>
        <td class="center"></td>
        <td class="center"></td>
        <td class="center"></td>
        <td class="center"></td>
    </tr>
    <tr>
        <td class="center"></td>
        <td></td>
        <td class="center"></td>
        <td class="center"></td>
        <td class="center"></td>
        <td class="center"></td>
        <td class="center"></td>
    </tr>
    <tr>
        <td height="30" colspan="7"><div class="total">配件费合计：<span>${orderInfo.goodsAmount}</span></div><div
                class="total">优惠合计：<span>${orderInfo.goodsDiscount}</span></div><div
                class="total">客户应付配件费合计：<span>${orderInfo.goodsAmount-orderInfo.goodsDiscount}</span></div>
        </td>
    </tr>
</table>
<table class="orderpp" cellspacing="0" cellpadding="0">
    <caption class="item_tit">附加费用清单</caption>
    <tr class="extra-costs">
        <th width="35" class="sequence">序号</th>
        <th width="329">附加费用</th>
        <th width="130">金额</th>
        <th width="80">优惠</th>
        <th width="400">费用备注</th>
    </tr>
<#list orderServicesList2 as orderServices>
    <tr class="extra-costs">
        <td class="sequence">${orderServices_index+1}</td>
        <td class="center">&nbsp;${orderServices.serviceName}</td>
        <td class="center">&nbsp;${orderServices.serviceAmount}</td>
        <td class="center">&nbsp;${orderServices.discount}</td>
        <td class="center">&nbsp;${orderServices.serviceNote}</td>
    </tr>
</#list>
    <tr>
        <td colspan="7"><p class="total">附加费合计：<span>${orderInfo.feeAmount}</span></p>

            <p class="total">优惠合计：<span>${orderInfo.feeDiscount}</span></p>

            <p class="total">客户应付附加费合计：<span>${orderInfo.feeAmount-orderInfo.feeDiscount}</span></p></td>
    </tr>

    <tr>
        <td colspan="7"><label class="zfy">&nbsp;总计：&nbsp;<span> ${orderInfo.orderAmount}元</span> (
            工时费：${orderInfo.serviceAmount}元 -工时优惠：${orderInfo.serviceDiscount}元 + 材料费：${orderInfo.goodsAmount}元
            -材料优惠：${orderInfo.goodsDiscount}元 + 附加费：${orderInfo.feeAmount}元 - 费用优惠：${orderInfo.feeDiscount}元)</label>
        </td>
    </tr>
</table>
<table id="tab" cellspacing="0" cellpadding="0">
    <tr class="t_tr3">
        <td width="489" colspan="10">&nbsp;用户描述：</td>
        <td width="489" colspan="10">&nbsp;维修项目修改记录：</td>
    </tr>

    <tr class="sign-row">
        <th class="col_1" rowspan="2" colspan="2"><p>检验</p>

            <p>签字</p></th>
        <th colspan="5" class="td_sign">机电</th>
        <th colspan="4" class="td_sign">钣喷</th>
        <th colspan="4" class="td_sign">美容</th>
        <th colspan="5" class="td_sign">总检</th>
    </tr>
    <tr>
        <td colspan="5" class="td_sign"></td>
        <td colspan="4" class="td_sign"></td>
        <td colspan="4" class="td_sign"></td>
        <td colspan="5" class="td_sign"></td>
    </tr>
    <tr class="t_tr3">
        <td width="489" colspan="10">&nbsp;备注：${orderInfo.postscript}</td>
        <td width="489" colspan="10">&nbsp;维修建议：</td>
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
        <span><#if orderInfo.upkeepMileage>${orderInfo.upkeepMileage}<#else>&nbsp;&nbsp;&nbsp;</#if> km</span>
    </div>
    <div style="width:390px;">
        <span>服务顾问：</span><span class="sign">${orderInfo.receiverName}</span>
    </div>
    <div style="width:390px;">
        <span>客户签字：</span><span class="sign"></span>
        <span class="ymd">年　　月　　日</span>
    </div>
</div>
<div id="ftr">
</div>
</body>
</html>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>采购入库单</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/common/print/print_common.css?e174653fe93fff7de684a409310c97d7"/>
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/warehouse/in/warehouse_print.css?560ec7e89ff3394fb1fd416e1c594963"/>

    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.js"></script>
    <script>
        $(document).ready(function ($) {
            window.print();
        });
    </script>
</head>
<body>
<div class="head">
    <#if SESSION_SHOP_LEVEL != 10 && SESSION_SHOP_LEVEL != 11 && SESSION_SHOP_LEVEL != 12>
    <p class="tq_print_logo"><img src="${BASE_PATH}/static/img/common/print/print_logo.png"/></p>
    </#if>
    <h1>${shop.companyName}</h1>
<#if warehouseInVo.status =="HZRK">
    <h3>材料入库单(退货)</h3>
<#else >
    <h3>材料入库单</h3>
</#if>

    <div>
        <span><label>单据号：</label></span><span
            ><label>订单号：${warehouseInVo.warehouseInSn}</label></span><span
            ><label>入库时间：</label>${warehouseInVo.inTime?string("yyyy-MM-dd HH:mm")}</span>

        <div class="clear"></div>
    </div>
</div>
<table>
    <tr class="tr_1">
        <td colspan="10">
        <div class="right_boder" style="width:594px;">&nbsp;供应商名称：${warehouseInVo.supplierName}</div>
        <div style="width:381px">&nbsp;发票号码：</div>
        </td>
    </tr>
    <tr class="tr_1">
        <td colspan="10">
            <div class="right_boder" style="width:594px;">&nbsp;联系人：${warehouseInVo.contact}</div>
            <div style="width:381px">&nbsp;联系人号码：${warehouseInVo.contactMobile}</div>
        </td>
    </tr>
    <tr>
        <th class="sequence" width="35">序号</th>
        <th width="175">零件号</th>
        <th width="190">零件名称</th>
        <th width="155">车辆型号</th>
        <#--<th colspan="2">配件类别</th>-->
        <th width="125">显示货位</th>
        <th width="35">数量</th>
        <th width="70">入库单价</th>
        <th width="75">入库金额</th>
        <th width="50">库存量</th>
        <th width="70">订货客户</th>
    </tr>
<#list warehouseInVo.detailList as warehouseInDetail>
    <tr class="content">
        <td class="sequence">${warehouseInDetail_index+1}</td>
        <td class="center">${warehouseInDetail.goodsFormat}</td>
        <td class="center">
            <div style="width:100%;overflow: hidden;">${warehouseInDetail.goodsName}</div>
        </td>
        <td class="center">
            <div style="width:100%;overflow: hidden;">${warehouseInDetail.carInfoStr}</div>
        </td>
        <#--<td class="center" colspan="2">&nbsp;</td>-->
        <td class="center">${warehouseInDetail.depot}</td>
        <td class="center">${warehouseInDetail.goodsCount}</td>
        <td class="right">${warehouseInDetail.purchasePrice}</td>
        <td class="right">${warehouseInDetail.purchaseAmount}</td>
        <td class="center">${warehouseInDetail.stock}</td>
        <td>&nbsp;</td>
    </tr>
</#list>
    <tr class="tr_3">
        <td colspan="2">
            &nbsp;入库数量总计
            <#--<div class="center right_boder" style="width:138px;"><span>&nbsp;入库数量总计</span></div>-->
            <#--<div class="right right_boder" style="width:249px;">${totalCount}&nbsp;</div>-->
            <#--<div class="center right_boder" style="width:138px;">&nbsp;入库金额总计</div>-->
            <#--<div class="left" style="width:448px;">${upperAmount}整(¥${warehouseInVo.goodsAmountStr})&nbsp;</div>-->
        </td>
        <td>${totalCount}&nbsp;</td>
        <td colspan="2">&nbsp;入库金额总计</td>
        <td colspan="5">${chineseTotalAmount} 整(¥${warehouseInVo.goodsAmount})&nbsp;</td>
    </tr>
</table>
<div class="signature">
    <span><label>经手人：</label></span><span
        ><label>审核：</label></span><span
        ><label>开单员：</label>${warehouseInVo.operatorName}</span><span
        ><label>打印日期：</label>${.now?string("yyyy-MM-dd HH:mm")}</span><span
        ><label>验收人：</label></span>
</div>
</body>
</html>
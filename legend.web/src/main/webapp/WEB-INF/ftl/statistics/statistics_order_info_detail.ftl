<#include "layout/header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/statistics.css?20150323">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/statistics_order_info_detail.css?72faa272f030e9d7a08cd234b415a242">
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/pagination.js?3f20a000af0278d601fa443295a4a7a3"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/statistics/statistics_order_info_detail.js?e52163ae78001e43994967a8f68d6d51"></script>

<div class="wrapper">
<#include "statistics/statistics_nav.ftl">
    <div class="wrright">
        <div class="title_1">工单明细表</div>
        <div class="info_1">报表说明：本报表主要展示时间段内的维修工单的流水明细</div>
        <div class="search">
            <form action="${BASE_PATH}/shop/stats/stats_repair_car/get_stats_amount/get" name="searchForm" id="searchForm">
                <span class="info_2">开单时间:</span><input
                    id="d4311" class="selcont wid" type="text"
                    onclick="WdatePicker({maxDate:'#F{$dp.$D(\'d4312\')}',doubleCalendar:true})" value=""
                    name="search_startTime"><span class="info_3">至</span><input
                    id="d4312" class="selcont wid" type="text"
                    onclick="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',doubleCalendar:true})" value=""
                    name="search_endTime">
                <span class="info_4">车牌:</span>
                <select class="wid selcont">
                    <option></option>
                    <option>1</option>
                    <option>2</option>
                </select>
                <span class="info_4">车主:</span>
                <input class="wid selcont" id="search_customerName"></input>
                <a class="export_2 wid" id="export_stats" href="#">导出excel</a>

                <#--<input-->
                    <#--class="searchBtn_statistics" id="searchButton" type="button" value="统计"></input>-->
                <#--<input class="searchBtn" id="print_stats" type="button" value="打印"></input>-->
                <#--<a class="export" id="export_stats" href="#">导出excel</a>-->
                <#--<input type="hidden" name="page" id="search_page" value="1"/>-->
            </form>
        </div>
    <#--<div class="headInfo">-->
    <#--<span class="salesDate"><label id="start_time_lab"></label><label-->
    <#--id="stop_time_lab"></label></span>-->
    <#--<div class="clear"></div>-->
    <#--</div>-->
        <div class="bx_cont" id="content"></div>
        <script id="contentTemplate" type="text/html">
            <table border="0" cellpadding="0" cellspacing="0">
                <%for(var index in templateData){%>
                <%if(index == 0){%>
                <%itemZero = templateData[index]%>
                <tr class="table-content">
                    <td width="65" class="center td0">日期</td>
                    <td width="80" class="center td1">车牌</td>
                    <td width="80" class="center td2">车辆型号</td>
                    <td width="80" class="center td3">维修类别</td>
                    <td width="80" class="center td4">应收款(备注)</td>
                    <%if(itemZero.managerServiceList != null){%>
                    <%for(var indexOne in itemZero.managerServiceList){%>
                    <%itemTwo = itemZero.managerServiceList[indexOne]%>
                    <td width="100" class="center td0"><%=itemTwo.rolesName+itemTwo.shopManager%></td>
                    <%}%>
                    <%}%>
                    <td width="80" class="center td5">材料费</td>
                    <td width="80" class="center td6">管理费</td>
                    <td width="80" class="center td7">税费</td>
                    <td width="80" class="center td8">其它</td>
                    <td width="80" class="center td9">合计</td>
                    <td width="80" class="center td10">实收</td>
                    <td width="80" class="center td11">优惠</td>
                    <td width="80" class="center td12">发票</td>
                    <td width="80" class="center td13">材料成本</td>
                    <td width="150" class="center td14">收款方式</td>
                </tr>
                <%}%>
                <%}%>
                <%for(var index in templateData){%>
                <%itemOne = templateData[index]%>
                <tr class="table-content">
                    <td class="right td0"><%=itemOne.settlementDateStr%></td>
                    <td class="right td1"><%=itemOne.carLicense%></td>
                    <td class="right td2"><%=itemOne.carBlandAndSeries%></td>
                    <td class="right td3"><%=itemOne.orderTypeName%></td>
                    <td class="right td4"><%=itemOne.settlementPostscript%></td>
                    <%if(itemOne.managerServiceList != null){%>
                    <%for(var indexTwo in itemZero.managerServiceList){%>
                    <%itemThr = itemOne.managerServiceList[indexTwo]%>
                    <td class="right td0"><%=itemThr.serviceAmount%></td>
                    <%}%>
                    <%}%>
                    <td class="right td5"><%=itemOne.goodsAmount%></td>
                    <td class="right td6"><%=itemOne.goodsManagerAmount%></td>
                    <td class="right td7"><%=itemOne.faxAmount%></td>
                    <td class="right td8"><%=itemOne.otherAmount%></td>
                    <td class="right td9"><%=itemOne.totalAmount%></td>
                    <td class="right td10"><%=itemOne.payAmount%></td>
                    <td class="right td11"><%=itemOne.discountAmount%></td>
                    <td class="right td12"><%=itemOne.invoiceName%></td>
                    <%if(itemOne.inventoryAmount != null){%>
                    <td class="right td13"><%=itemOne.inventoryAmount.toFixed(2)%></td>
                    <%}else{%>
                    <td class="right td13">0.00</td>
                    <%}%>
                    <td class="right td14"><%=itemOne.paymentName%></td>
                </tr>
                <%}%>
            </table>
            </div>
        </script>
        <div id="pageDiv" class="text-center">
            <div class="clear"></div>
        </div>
    </div>
</div>
<#include "layout/footer.ftl">

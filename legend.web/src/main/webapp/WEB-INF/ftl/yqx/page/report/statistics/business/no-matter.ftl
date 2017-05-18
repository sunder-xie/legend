<div class="main service-main no-border-to">
    <div class="main-header">
        <h3 class="performance-title"><i></i>非物料成本</h3>
    </div>
    <div class="table-box performance-table">
        <table class="yqx-table">
            <thead>
            <tr>
                <th class="text-l">费用类型</th>
                <th class="text-l">费用名称</th>
                <th class="text-r">费用金额</th>
            </tr>
            </thead>
            <tbody id="tableCon">

            </tbody>
        </table>
    </div>
</div>
<script type="text/html" id="tableTpl">
    <%if(json.success && json.data){%>
    <%for(var i=0; i< json.data.length; i++){%>
    <%var item = json.data[i]%>
    <tr>
        <td rowspan="<%=item.goodCostDetailDTOList.length + 2%>" class="text-l line1"><%=item.costTypeStr%></td>
    </tr>
    <%for(var j=0; j< item.goodCostDetailDTOList.length; j++){%>
    <%var subItem = item.goodCostDetailDTOList[j]%>
    <tr>
        <td class="text-l"><%=subItem.costName%></td>
        <td class="text-r">&yen;<%=subItem.costAmount%></td>
    </tr>
    <%}%>
    <tr>
        <td class="text-l "></td>
        <td class="text-r money-font">&yen;<%=item.totalAmount%></td>
    </tr>
    <%}}%>
</script>

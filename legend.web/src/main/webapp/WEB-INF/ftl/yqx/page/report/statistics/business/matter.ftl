<div class="main service-main no-border-to">
    <div class="main-header">
        <h3 class="performance-title"><i></i>油漆成本</h3>
    </div>
    <div class="table-box performance-table">
        <table class="yqx-table">
            <thead>
            <tr>
                <th class="text-l">类型</th>
                <th class="text-l" width="300">商品名称</th>
                <th class="text-r">期初库存</th>
                <th class="text-r">采购入库</th>
                <th class="text-r">期末库存</th>
                <th class="text-r">消耗数量<i data-tips="消耗数量=期初库存+采购入库-期末库存" class="question-icon js-show-tips">?</i></th>
                <th class="text-r">油漆成本<i data-tips="油漆成本=（消耗数量/净含量）* 采购成本" class="question-icon js-show-tips">?</i></th>
            </tr>
            </thead>
            <tbody id="paintCostCon">

            </tbody>
        </table>
        &nbsp;
        <h3>注意：请每月定期对油漆库存进行盘点</h3>
    </div>

    <div class="main-header m-top">
        <h3 class="performance-title"><i></i>辅料成本</h3>
    </div>
    <div class="table-box performance-table">
        <table class="yqx-table">
            <thead>
            <tr>
                <th class="text-l">员工姓名</th>
                <th class="text-l">零件号</th>
                <th class="text-l" width="400">商品名称</th>
                <th class="text-l">出库数量</th>
                <th class="text-r">出库金额</th>
            </tr>
            </thead>
            <tbody id="accessiesCostCon">

            </tbody>
        </table>
    </div>
</div>
<!--油漆成本模板-->
<script type="text/html" id="paintCostTpl">
    <%if(json.success && json.data){%>
    <%for(var i=0; i< json.data.length; i++){%>
    <%var item = json.data[i]%>
    <tr>
        <td rowspan="<%=item.paintCostDetailDTOList.length + 2%>" class="text-l line1"><%=item.paintLevel%></td>
    </tr>
    <%for(var j=0; j< item.paintCostDetailDTOList.length; j++){%>
    <%var subItem = item.paintCostDetailDTOList[j]%>
    <tr>
        <td class="text-l js-show-tips ellipsis-1" width="300"><%=subItem.goodsName%></td>
        <td class="text-r">
            <%if(subItem.initStock != null){%>
                <%=subItem.initStock%><%=subItem.measureUnit%>
            <%}else{%>
                暂无数据
            <%}%>
        </td>
        <td class="text-r">
            <%if(subItem.warehouseInCount != null){%>
            <%=subItem.warehouseInCount%><%=subItem.measureUnit%>
            <%}else{%>
            0<%=subItem.measureUnit%>
            <%}%>
        </td>
        <td class="text-r">
            <%if(subItem.lastStock != null){%>
            <%=subItem.lastStock%><%=subItem.measureUnit%>
            <%}else{%>
            暂无数据
            <%}%>
        </td>
        <td class="text-r">
            <%if(subItem.diffStock != null){%>
            <%=subItem.diffStock%>g
            <%}else{%>
            --
            <%}%>
        </td>
        <td class="text-r">
            <%if(subItem.diffStockPrice != null){%>
            &yen;<%=subItem.diffStockPrice%>
            <%}else{%>
            --
            <%}%>

        </td>
    </tr>
    <%}%>
    <tr>
        <td colspan="6" class="text-r money-font">
            <%if(item.paintAmount != null){%>
            <input type="hidden" value="<%=item.paintAmount%>" name="paintAmount"/>
            &yen;<%=item.paintAmount%>
            <%}else{%>
            <input type="hidden" value="0" name="paintAmount"/>
            --
            <%}%>

        </td>
    </tr>
    <tr>
        <td colspan="7" class="blank"></td>
    </tr>
    <%}%>
    <tr class="bg-color">
        <td colspan="7" class="text-l bold">油漆成本：<span class="money-font">&yen;</span><span class="money-font js-paint-amount">0.00</span></td>
    </tr>
    <%}else{%>
    <tr>
        <td colspan="7">暂无数据</td>
    </tr>
    <%}%>
</script>

<!--辅料成本模板-->
<script type="text/html" id="accessiesCostTpl">
    <%if(json.success && json.data){%>
    <%for(var i=0; i< json.data.length; i++){%>
    <%var item = json.data[i]%>
    <tr>
        <td rowspan="<%=item.accessiesCostDetailDTOList.length + 2%>" class="text-l line1"><%=item.receiverName%></td>
    </tr>
    <%for(var j=0; j< item.accessiesCostDetailDTOList.length; j++){%>
    <%var subItem = item.accessiesCostDetailDTOList[j]%>
    <tr>
        <td class="text-l"><%=subItem.goodsFormat%></td>
        <td class="text-l js-show-tips ellipsis-1" width="400"><%=subItem.goodsName%></td>
        <td class="text-l"><%=subItem.warehouseOutCount%></td>
        <td class="text-r">&yen;<%=subItem.inventoryPrice%></td>
    </tr>
    <%}%>
    <tr>
        <td colspan="4" class="text-r money-font">&yen;<span class="js-cost"><%=item.warehouseOutAmount%></span></td>
    </tr>
    <tr>
        <td colspan="5" class="blank"></td>
    </tr>
    <%}%>
    <tr class="bg-color">
        <td colspan="5" class="text-l bold">辅料成本：<span class="money-font">&yen;</span><span class="money-font js-costing">0.00</span></td>
    </tr>
    <%}else{%>
    <tr>
        <td colspan="5">暂无数据</td>
    </tr>
    <%}%>
</script>


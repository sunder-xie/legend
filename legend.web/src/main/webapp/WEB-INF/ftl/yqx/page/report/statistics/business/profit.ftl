<div class="main service-main no-border-to">
    <div class="main-header">
        <h3 class="performance-title"><i></i>盈亏平衡点</h3>
    </div>
    <div class="table-box performance-table">
        <table class="yqx-table">
            <thead>
            <tr>
                <th class="text-r">单面毛利<i data-tips="单面毛利 = (指定服务类型（钣喷、钣金、喷漆、补漆）总金额-油漆成本-辅料成本）/面漆总数" class="question-icon js-show-tips">?</i></th>
                <th class="text-r">单面变动成本<i data-tips="单面变动成本=变动费用/面漆总数" class="question-icon js-show-tips">?</i></th>
                <th class="text-r">固定费用</th>
                <th class="text-r">平衡面漆数量(面)<i data-tips="平衡面漆数量=固定费用/（单面毛利-单面变动成本）" class="question-icon js-show-tips">?</i></th>
                <th class="text-r">实际面漆数量(面)</th>
            </tr>
            </thead>
            <tbody id="profitCon">

            </tbody>
        </table>
    </div>
</div>

<script type="text/html" id="profitTpl">
    <%if(json.success){%>
    <%var item = json.data%>
    <tr>
        <td class="text-r money-font">&yen;<%=item.singleInterest%></td>
        <td class="text-r money-font">&yen;<%=item.singleCost%></td>
        <td class="text-r money-font">&yen;<%=item.fixedFee%></td>
        <td class="text-r">
            <%if(item.balancePaintNum != null){%>
                <%=item.balancePaintNum%>
            <%}else{%>
                无
            <%}%>
        </td>
        <td class="text-r"><%=item.realPaintNum%></td>
    </tr>
    <%if(item.singleInterest < item.singleCost){%>
    <tr>
        <td colspan="5" class="text-l"><p>注意：本月单面毛利 < 单面变动成本，门店需降低变动成本或提高毛利。</p></td>
    </tr>
    <%}%>
    <%}else{%>
    <tr>
        <td colspan="5">暂无数据</td>
    </tr>
    <%}%>
</script>

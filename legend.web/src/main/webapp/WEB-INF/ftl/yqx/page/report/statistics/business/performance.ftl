<div class="main service-main no-border-to">
    <div class="main-header">
        <h3 class="performance-title"><i></i>服务顾问工作绩效</h3>
    </div>
    <div class="table-box performance-table">
    <table class="yqx-table">
        <thead>
        <tr>
            <th class="text-l">服务顾问</th>
            <th></th>
            <th class="text-r">接车总数(辆)</th>
            <th class="text-r">总面数(面)</th>
            <th class="text-r">准时交车数量<i data-tips="准时交车 = 工单到完工状态的时间早于或等于预设的交车时间" class="question-icon js-show-tips">?</i></th>
            <th class="text-r">超时交车数量<i data-tips="超时交车 = 工单到完工状态的时间晚于预设的交车时间" class="question-icon js-show-tips">?</i></th>
            <th class="text-r">准时交车率</th>
            <th class="text-r">超时交车率<i data-tips="红底色底标注的表示超时交车率大于10%" class="question-icon js-show-tips">?</i></th>
        </tr>
        </thead>
        <tbody id ="serviceSaPerformanceCon">

        </tbody>
    </table>
    </div>


    <div class="main-header m-top">
        <h3 class="performance-title"><i></i>技工工作绩效</h3>
    </div>
    <div class="table-box performance-table">
        <table class="yqx-table">
            <thead>
            <tr>
                <th class="text-l">姓名</th>
                <th class="text-r">实际工作时间(h)</th>
                <th class="text-r">派工工作时间(h)</th>
                <th class="text-r">出勤时间(h)</th>
                <th class="text-r">劳动利用率<i data-tips="  劳动利用率=实际工作时间/出勤时间（绿底色表示劳动利用率大于75%,红底色表示劳动利用率小于25%）" class="question-icon js-show-tips">?</i></th>
                <th class="text-r">劳动生产率<i data-tips="劳动生产率=派工工作时间/出勤时间(绿底色表示劳动生产率大于75%,红底色表示劳动生产率小于25%)" class="question-icon js-show-tips">?</i></th>
                <th class="text-r">工位工作效率<i data-tips="工位工作效率=派工工作时间/实际工作时间" class="question-icon js-show-tips">?</i></th>
            </tr>
            </thead>
            <tbody id="mechanicPerformanceCon">

            </tbody>
        </table>
    </div>


    <div class="main-header m-top">
        <h3 class="performance-title"><i></i>工序时间绩效</h3>
    </div>
    <div class="table-box performance-table">
        <table class="yqx-table">
            <thead>
            <tr>
                <th class="text-l">生产线</th>
                <th class="text-l">工序</th>
                <th class="text-r">标准作业工时(m)</th>
                <th class="text-r">平均工作时间(m)</th>
                <th class="text-r">执行次数</th>
                <th class="text-r">标准工时达成率<i data-tips="绿底色表示达成率大于90%,红底色表示达成率小于70%" class="question-icon js-show-tips">?</i></th>
            </tr>
            </thead>
            <tbody id="timePerformanceCon">

            </tbody>
        </table>
    </div>
</div>

<script type="text/html" id="timePerformanceTpl">
    <%if(json.success && json.data){%>
    <%for(var i=0; i< json.data.length; i++){%>
    <%var item = json.data[i]%>
    <tr>
        <td class="text-l line1" rowspan="<%=item.processIndexDTOs.length + 2%>"><%=item.lineName%></td>
    </tr>
    <%if(item.processIndexDTOs){%>
    <%for(var j=0; j< item.processIndexDTOs.length; j++){%>
    <%var subItem = item.processIndexDTOs[j]%>
    <tr>
        <td class="text-l"><%=subItem.processName || 0%></td>
        <td class="text-r"><%=subItem.workTime || 0 %></td>
        <td class="text-r"><%=subItem.perWorkTime || 0%></td>
        <td class="text-r"><%=subItem.executeCount || 0%></td>
        <td class="text-r<%if(subItem.achieveRate > 90){%> green <%}else if(subItem.achieveRate<70){%> red<%}%>">
            <span></span>
            <%=subItem.achieveRate || 0 %>%
        </td>
    </tr>
    <%}}%>
    <tr>
        <td class="text-l bold" colspan="5">维修车辆总数：<%=item.carNum || 0 %></td>
    </tr>
    <tr>
     <td colspan="6" class="blank"></td>
    </tr>
    <%}}else{%>
        暂无数据
    <%}%>
</script>

<script type="text/html" id="mechanicPerformanceTpl">
    <%if(json.success && json.data){%>
    <%var data = json.data%>
    <%if(data.mechanicPerformanceVOs){%>
    <%for(var i=0; i< data.mechanicPerformanceVOs.length; i++){%>
    <%var item = data.mechanicPerformanceVOs[i]%>
    <tr>
        <td class="text-l"><%=item.name%></td>
        <td class="text-r"><%=(item.realWorkTime).toFixed(2)%></td>
        <td class="text-r"><%=(item.planWorkTime).toFixed(2)%></td>
        <td class="text-r"><%=(item.attendanceTime).toFixed(2)%></td>
        <td class="text-r
        <%if(item.workUseRate > 75){%>
            green
        <%}else if(item.workUseRate < 25){%>
            red
        <%}%>">
            <span></span><%=(item.workUseRate).toFixed(2)%>%</td>
        <td class="text-r
        <%if(item.workProductRate > 75){%>
            green
        <%}else if(item.workProductRate < 25){%>
            red
        <%}%>"><span></span><%=(item.workProductRate).toFixed(2)%>%</td>
        <td class="text-r"><span></span><%=(item.stationWorkRate).toFixed(2)%>%</td>
    </tr>
    <%}}%>
    <tr class="bg-color">
        <td class="bold text-l">总计：<%=data.personNum%>人</td>
        <td class="bold text-r"><%=(data.totalRealWorkTime).toFixed(2)%></td>
        <td class="bold text-r"><%=(data.totalPlanWorkTime).toFixed(2)%></td>
        <td class="bold text-r"><%=(data.totalAttendanceTime).toFixed(2)%></td>
        <td class="bold text-r
        <%if(data.totalWorkUseRate > 75){%>
            green
        <%}else if(data.totalWorkUseRate < 25){%>
            red
        <%}%>"><span></span><%=(data.totalWorkUseRate).toFixed(2)%>%</td>
        <td class="bold text-r
        <%if(data.totalWorkProductRate > 75){%>
            green
        <%}else if(data.totalWorkProductRate < 25){%>
            red
        <%}%>"><span></span><%=(data.totalWorkProductRate).toFixed(2)%>%</td>
        <td class="bold text-r"><%=(data.totalStationWorkRate).toFixed(2)%>%</td>
    </tr>
    <%}%>
</script>

<script type="text/html" id="serviceSaPerformanceTpl">
    <%if(json.success && json.data){%>
    <%var data = json.data%>

    <%if(data.serviceSaPerformanceMapVOs){%>
    <%for(var i=0; i< data.serviceSaPerformanceMapVOs.length; i++){%>
    <%var item = data.serviceSaPerformanceMapVOs[i]%>
    <%if(item.serviceSaPerformanceDTOs){%>
    <%var item1 = item.serviceSaPerformanceDTO%>
    <tr>
        <td rowspan="<%=item.serviceSaPerformanceDTOs.length+2%>" class="text-l"><%=item1.serviceSa%></td>
    </tr>
    <%for(var j=0; j< item.serviceSaPerformanceDTOs.length; j++){%>
    <%var item2 = item.serviceSaPerformanceDTOs[j]%>
    <tr>
        <td class="line">
            <%if(item2.lineType == 1){%>快修线<%}%>
            <%if(item2.lineType == 2){%>事故线<%}%>
            <%if(item2.lineType == 3){%>快喷线<%}%>
            <%if(item2.lineType == 4){%>小事故线<%}%>
        </td>
        <td class="text-r"><%=item2.receiveNum%></td>
        <td class="text-r"><%=item2.paintNum%></td>
        <td class="text-r"><%=item2.onTimeNum%></td>
        <td class="text-r"><%=item2.outTimeNum%></td>
        <td class="text-r"><%=(item2.onTimeRate).toFixed(2)%>%</td>
        <td class="text-r <%if(item2.outTimeRate > 10){%>
            red
        <%}%>"><span></span><%=(item2.outTimeRate).toFixed(2)%>%</td>
    </tr>
    <%}%>
    <tr>
        <td class="line bold">统计</td>
        <td class="text-r bold"><%=item1.receiveNum%></td>
        <td class="text-r bold"><%=item1.paintNum%></td>
        <td class="text-r bold"><%=item1.onTimeNum%></td>
        <td class="text-r bold"><%=item1.outTimeNum%></td>
        <td class="text-r bold"><%=(item1.onTimeRate).toFixed(2)%>%</td>
        <td class="text-r bold
        <%if(item1.outTimeRate > 10){%>
            red
        <%}%>"><span></span><%=(item1.outTimeRate).toFixed(2)%>%</td>
    </tr>
    <tr>
        <td colspan="8" class="blank"></td>
    </tr>
    <%}}}%>
    <tr class="bg-color">
        <td class="bold text-l">总计：<%=data.personNum%>人</td>
        <td></td>
        <td class="bold text-r"><%=data.totalReceiveNum%></td>
        <td class="bold text-r"><%=data.totalPaintNum%></td>
        <td class="bold text-r"><%=data.totalOnTimeNum%></td>
        <td class="bold text-r"><%=data.totalOutTimeNum%></td>
        <td class="bold text-r"><%=(data.totalOnTimeRate).toFixed(2)%>%</td>
        <td class="bold text-r
        <%if(data.totalOutTimeRate > 10){%>
            red
        <%}%>"><span></span><%=(data.totalOutTimeRate).toFixed(2)%>%</td>
    </tr>
    <%}%>
</script>


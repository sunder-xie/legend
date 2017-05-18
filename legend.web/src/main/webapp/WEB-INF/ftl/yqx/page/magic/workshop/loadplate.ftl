<#include "yqx/layout/kb-header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/loadplate.css?862f692790f80078e01a91e4a9bad3e0"/>
<!-- 样式引入区 end-->
<div class="fluid-wrapper clearfix">
    <#--<#include "yqx/layout/kb-top.ftl">-->
    <#include "yqx/layout/kb-aside.ftl">
    <div class="main" id="main">

    </div>
</div>

<script type="text/template" id="timeTpl">
    <%if(timeLine){%>
    <div class="time_line clearfix <%=working(1)%>" style="width: <%= mainWidth%>px">
        <%if(timeLine <= working(1)){%>
        <div class="time_title">
            <p class="tc tc_fst"><%= today()%></p>
        </div>
        <ul class="hour">
            <%for(var i = 0, len =  working(1) ; i < len; i++){%>
                <%if(i == 0){%>
                    <li class="start"><%=i+working(2)%>:00</li>
                <%}else if(i == len - 1){%>
                    <li class="w40"><%=i+working(2)%>:00</li>
                <%}else{%>
                    <li><%=i+working(2)%>:00</li>
                <%}%>
            <%}%>
        </ul>
        <%}else{
            var len, wlen;
            if(timeLine-working(1) >= working(1)){
                len = working(1);
                wlen = 2 * working(1);
            }else{
                len = timeLine-working(1);
                wlen = timeLine;
            }
        %>
            <div class="time_title">
                <p class="tc tc_fst"><%= today()%></p>
                <p class="tc" style="width: <%= (wlen - working(1)) / wlen * (mainWidth - 112)%>px"><%= tomorrow()%></p>
            </div>
            <ul class="hour">
                <%for(var i = 0, len = working(1) ; i < len; i++){%>
                    <%if(i == 0){%>
                        <li class="start"><%=i+working(2)%>:00</li>
                    <%}else if(i == len - 1){%>
                        <li><%=i+working(2)%>:00</li>
                    <%}else{%>
                        <li><%=i+working(2)%>:00</li>
                    <%}%>
                <%}%>
                <%for(var j = 0 ; j < len; j++){%>
                    <%if(j == 0){%>
                        <li class="start"><%=j+working(2)%>:00</li>
                    <%}else if(j == len - 1){%>
                        <li class="w40"><%=j+working(2)%>:00</li>
                    <%}else{%>
                        <li><%=j+working(2)%>:00</li>
                    <%}%>
                <%}%>
            </ul>
        </div>
        <%}%>
    </div>
    <div id="loadplate"></div>
    <%}%>
</script>


<script type="text/template" id="mainTpl">
    <%if(json){%>
    <div class="loadplate">
        <%if(json.loadPlateVO && json.loadPlateVO.length){
            for(var i = 0, len = json.loadPlateVO.length; i < len; i++){
                var flow = json.loadPlateVO[i];
            %>
                <p class="item"><%=flow.typeName%></p>
                <%if(flow && flow.balanceBoardVOs && flow.balanceBoardVOs.length){
                    for(var m = 0, len3 = flow.balanceBoardVOs.length; m < len3; m++){
                        var line = flow.balanceBoardVOs[m];
                        if(len3 != 1){
                    %>
                        <p class="item1"><%=line.lineName%></p>
                    <%}%>
                        <ul class="flow clearfix">
                            <%if(line && line.lineBalanceVOs && line.lineBalanceVOs.length){
                            for(var j = 0, len1 = line.lineBalanceVOs.length; j < len1; j++){
                                var loadList = line.lineBalanceVOs[j];
                            %>
                                <li>
                                    <div class="name">
                                        <span class="pname"><%=loadList.processName%></span>
                                        <span class="fr mname" title="<% if(flow.type != 2){%><%=loadList.managerName%><%}else{%><%=loadList.teamName%><%} %>"><% if(flow.type != 2){%><%=loadList.managerName%><%}else{%><%=loadList.teamName%><%} %></span>

                                    </div>
                                    <ul class="flow-block"><%
                                        if(loadList.carVOList && loadList.carVOList.length){
                                            for(var k = 0, len2 = loadList.carVOList.length; k < len2; k++){
                                                var takelist = loadList.carVOList[k];
                                                var gapWidth = gapTime(eightOclock,takelist.planStartTime,timeWidth);
                                                %>
                                                <li class="c<%= colorPlate(takelist.license) %>
                                                <%if(flow.type == 1){
                                                    if(loadList.managerId != takelist.managerId || takelist.processId != loadList.processId){%>
                                                        vhidden
                                                    <%}
                                                }else{
                                                    if(takelist.processId != loadList.processId){%>
                                                        vhidden
                                                    <%}
                                                }%>"
                                                style="left: <%=gapWidth%>px; width:<%= duringTime(takelist.planStartTime, takelist.planEndTime, timeWidth)%>px;" title=" 车 牌 号：<%=takelist.license%> &#13;开始时间：<%=time(takelist.planStartTime, 'yyyy.MM.dd hh:mm')%> &#13;结束时间：<%=time(takelist.planEndTime, 'yyyy.MM.dd hh:mm')%>"><%=takelist.license%></li>
                                           <%}
                                        }%>
                                    </ul>
                                </li>
                            <%}
                        }%>
                        </ul>
                    <%}%>
                    <ul class="sum_up">
                        <li class="sum2 border0">
                            <p class="sum_title">流水线名称</p>
                            <p>
                                <%if(flow && flow.balanceBoardVOs && flow.balanceBoardVOs[0] && flow.balanceBoardVOs[0].lineName){%>
                                    <%=flow.balanceBoardVOs[0].lineName%>
                                <%}%>
                            </p>
                        </li>
                        <li class="sum1">
                            <p class="sum_title">等待数量</p>
                            <p>（<%=flow.plateVOs.waitNumber%>）</p>
                        </li>
                        <li class="sum1">
                            <p class="sum_title">工作中数量</p>
                            <p>（<%=flow.plateVOs.workNumber%>）</p>
                        </li>
                        <li class="sum3">
                            <p class="sum_title">接车台次</p>
                            <ul class="ul-center clearfix">
                                <li>
                                    <p>前天</p>
                                    <p>（<%=flow.plateVOs.rnumberByesterday%>）</p>
                                </li>
                                <li>
                                    <p>昨天</p>
                                    <p>（<%=flow.plateVOs.rnumberYesterday%>）</p>
                                </li>
                                <li>
                                    <p>今天</p>
                                    <p>（<%=flow.plateVOs.rnumberToday%>）</p>
                                </li>
                                <li>
                                    <p>当月</p>
                                    <p>（<%=flow.plateVOs.rnumberMonth%>）</p>
                                </li>
                            </ul>
                        </li>
                        <li class="sum3">
                            <p class="sum_title">负载率</p>
                            <ul class="ul-center clearfix">
                                <li>
                                    <p>前天</p>
                                    <p>（<%=flow.plateVOs.bnumberByesterday%>%）</p>
                                </li>
                                <li>
                                    <p>昨天</p>
                                    <p>（<%=flow.plateVOs.bnumberYesterday%>%）</p>
                                </li>
                                <li>
                                    <p>今天</p>
                                    <p>（<%=flow.plateVOs.bnumberToday%>%）</p>
                                </li>
                                <li>
                                    <p>当月</p>
                                    <p>（<%=flow.plateVOs.bnumberMonth%>%）</p>
                                </li>
                            </ul>
                        </li>
                        <li class="sum2">
                            <p class="sum_title">最后车辆结束时间</p>
                            <p><%=time(flow.plateVOs.lastTime,'yyyy.MM.dd hh:mm')%></p>
                        </li>
                        <li class="sum2">
                            <p class="sum_title">预计交车时间</p>
                            <p><%=time(flow.plateVOs.planTime,'yyyy.MM.dd hh:mm')%></p>
                        </li>
                    </ul>
                <%}
            }
        }%>

        <#--总结-->
        <ul class="sum_up summary">
            <li class="sum2">
                <p class="sum_title">当前待修车辆数：<%=json.carNumberQuick%></p>
                <p>（快喷/快修线）</p>
            </li>
            <li class="sum2">
                <p class="sum_title">当前滞留车辆数：<%=json.carNumberSlow%></p>
                <p>（事故线）</p>
            </li>
            <li class="sum2">
                <p class="sum_title">当前中断车辆：<%=json.carNumberInterrupt%></p>
            </li>
            <li class="sum4"></li>
        </ul>
    </div>
    <%}%>
</script>


<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/workshop/loadplate.js?01182b6d962ba5d117769fe5f9b9b657"></script>
<!-- 脚本引入区 end -->
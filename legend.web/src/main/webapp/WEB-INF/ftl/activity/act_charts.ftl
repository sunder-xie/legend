<#include "layout/ng-header.ftl" >
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/resources/css/drainage_activities/drainage_display.css?65da8f010fbd86b5a43a460da6517722"
      xmlns="http://www.w3.org/1999/html"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/banner.css?ce49e105e2f15bdeea782c45ef0a778c"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/drainage_activities/drainage_charts.css?6cdfa623cd366e8d19182bf4872e2898"/>

<div class="qxy_wrapper clearfix">
<#include "activity/activity_left.ftl" >
    <div class="main">
        <h1 class="headline">活动业绩</h1>

        <div class="content clearfix">
            <form id="order_search">
            <div class="mb_40 form_item">
                <input id="d4311" class="input w_130 auditPassStartTime" type="text" placeholder="开始时间"
                    onclick="WdatePicker({onpicked:function(){document.getElementById('d4312').focus();},maxDate:'#F{$dp.$D(\'d4312\')}',doubleCalendar:true})" value=""
                    name="search_auditPassStartTime"> &nbsp;<font style="font-size: 15px;">~</font>&nbsp; <input
                    id="d4312" class="input w_130 auditPassEndTime mr_10" type="text" placeholder="结束时间"
                    onclick="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',doubleCalendar:true})" value=""
                    name="search_auditPassEndTime">
                <input type="button" href="javascript:void(0);" class="btn btn_primary pick_btn" value="查询"/>
            </div>
            </form>
            <div class="clearfix">
                <div class="table_content bx_cont"></div>
                <script type="text/html" id="contentTemplate">
                    <%if(templateData){%>
                    <% var insuranceBillSize = templateData.length;%>
                    <div class="row mb_40"><span class="label" style="font-size: medium">引流活动总收入：</span><strong
                            class="highlight_r"><%= templateData[insuranceBillSize-1].totalSettleAmount%></strong>元
                    </div>
                    <% for(var i = 0;i < insuranceBillSize - 1; i++){%>
                    <% var insuranceBillVo = templateData[i]%>
                    <div class="row color<%= insuranceBillVo.actTplId%> act_name" style="line-height: 2em"><%= insuranceBillVo.actName%>：<strong
                            class="highlight_y"><%= insuranceBillVo.totalSettleAmount%></strong>元
                        <%if(insuranceBillVo.needSettle){%>
                        <span class="note ml_10">您还有未完成的账单，请立即 &nbsp;<a
                                href="javascript:void(0);" act-tpl-id="<%= insuranceBillVo.actTplId%>" class="go_detail">前往对账</a></span>
                        <%}%>
                    </div>
                    <%}%>
                    <%}%>
                </script>
                <!-- 活动业绩统计-->
                <div id="act_charts" class="charts"></div>
            </div>
        </div>
    </div>
</div>

<#include "layout/ng-footer.ftl">
<script type="text/javascript" src="${BASE_PATH}/resources/js/echarts/echarts-all.js?e2bfd1e079dac28b25dd00a2ac458b24"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/script/page/activity/act_charts.js?05d995abc2532649ae5a0646ddd1113d"></script>

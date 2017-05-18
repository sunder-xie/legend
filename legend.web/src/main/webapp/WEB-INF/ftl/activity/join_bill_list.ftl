<#include "layout/ng-header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/drainage_activities/drainage_table.css?1aaf72f52175ea90f834513afe83d602"/>
<div class="qxy_wrapper clearfix">
<#include "activity/activity_left.ftl" >
    <div class="aside_main">
        <h1 class="head clearfix">
            <span class="headline">核销单列表</span>
        </h1>
        <div class="content">
            <div class="group_row" id="bill_search">
                <form>
                    <input type="hidden" name="page" value="1"/>
                    <div class="row clearfix">
                        <div class="col form_item">
                            <span class="label">创建时间</span><input
                                id="d4311" class="input w_130 startTime" type="text" placeholder="开始时间"
                                onclick="WdatePicker({maxDate:'#F{$dp.$D(\'d4312\')}',doubleCalendar:true})" value=""
                                name="search_startTime"> - <input
                                id="d4312" class="input w_130 endTime" type="text" placeholder="结束时间"
                                onclick="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',doubleCalendar:true})" value=""
                                name="search_endTime">
                        </div>
                        <div class="col form_item">
                            <span class="label">活动类型</span>
                            <select class="select w_130" name="search_shopActId">
                                <option value="">请选择</option>
                            <#list shopActivityList as shopActivity>
                                <option value="${shopActivity.id}">${shopActivity.actName}</option>
                            </#list>
                            </select>
                        </div>
                        <div class="col form_item">
                            <span class="label">账单状况</span>
                            <select class="select w_130" name="search_auditStatus">
                                <option value="">请选择</option>
                                <option value="0">已保存</option>
                                <option value="1">审核中</option>
                                <option value="2">审核成功</option>
                                <option value="3">审核失败</option>
                                <option value="4">已收款</option>
                            </select>
                        </div>
                    </div>
                    <div class="row clearfix">
                        <div class="col form_item">
                            <span class="label">关 键 字</span><input
                                class="input w_210" type="text" placeholder="查找车主、车主电话、车牌" name="search_keywords" id="search_keywords"/>
                        </div>
                        <div class="col">
                            <input type="button" class="btn btn_primary pick_btn search_btn mr-10" value="查询"/><input
                                type="button" class="btn btn-default clear_btn" value="清空"/>
                        </div>
                    </div>
                </form>
            </div>
            <div ref_search_form_id="bill_search" id="bill_search_table"
                 service_url="${BASE_PATH}/insurance/bill/list" auto_load="true">
                <div class="table_content bx_cont"></div>
                <script type="text/html">
                    <table class="table group_select">
                        <thead>
                        <tr>
                            <th>活动类型</th>
                            <th>创建时间</th>
                            <th>车牌</th>
                            <th>车主</th>
                            <th>车主电话</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                        var auditStatus = [
                        '已保存',
                        '审核中',
                        '审核成功',
                        '审核失败',
                        '已收款'
                        ];
                        %>
                        <%if(templateData.data.content){%>
                        <% var insuranceBillSize = templateData.data.content.length;%>
                        <% for(var i = 0;i < insuranceBillSize; i++){%>
                        <% var insuranceBillVo = templateData.data.content[i]%>
                        <tr>
                            <td><%= insuranceBillVo.actName%></td>
                            <td><%= insuranceBillVo.gmtCreateStr%></td>
                            <td><%= insuranceBillVo.carLicense%></td>
                            <td><%= insuranceBillVo.customerName%></td>
                            <td><%= insuranceBillVo.customerMobile%></td>
                            <td><%= auditStatus[insuranceBillVo.auditStatus]%></td>
                            <td><%if(insuranceBillVo.actTplId){%>
                                <a href="${BASE_PATH}/shop/activity/join?acttplid=<%= insuranceBillVo.actTplId%>&billid=<%= insuranceBillVo.id%>">查看详情</a>
                                <%}%>
                            </td>
                        </tr>
                        <%}%>
                        <%}%>
                        </tbody>
                    </table>
                </script>
                <div class="qxy_page" style="text-align: center;">
                    <div class="qxy_page_inner"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<#include "layout/ng-footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/script/page/activity/join_bill_list.js?94549b3ae534efa9934758b2dea08e2c"></script>

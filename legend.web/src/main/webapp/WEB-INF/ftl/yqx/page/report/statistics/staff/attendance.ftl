<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/common/report/report-common.css?c58c9a3ee3acf5c0dbc310373fe3c349">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/report/statistics/staff/attendance.css?13484e3ad764192d64b8d37c12ffb65c">
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/report/left-nav.ftl"/>
    <input type="hidden" id="istqmall" value="${SESSION_SHOP_IS_TQMALL_VERSION}">

    <div class="content fr">
        <div class="container">
            <div id="searchForm" class="search-form">
                <div class="show-grid">
                    <div class="input-group">
                        <label>统计时间</label>
                        <div class="form-item">
                            <input type="text" id="createTimeStart" name="search_startTime"
                                   class="yqx-input yqx-input-small js-date" placeholder="开始时间">
                            <span class="fa icon-small icon-calendar"></span>
                        </div>
                        <label>至</label>

                        <div class="form-item">
                            <input type="text" id="createTimeEnd" name="search_endTime"
                                   class="yqx-input yqx-input-small js-date" placeholder="结束时间">
                            <span class="fa icon-small icon-calendar"></span>
                        </div>
                    </div>
                    <div class="input-group">
                        <label>员工名称</label>
                        <div class="form-item">
                            <input class="yqx-input  yqx-input-small js-staff" placeholder="输入查询员工">
                            <input type="hidden" name="search_statusname">
                            <span class="fa icon-angle-down"></span>
                        </div>
                    </div>
                    <div class="search-btns fr">
                        <a href="javascript:void(0)" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn" style="padding: 0 10px;">查询</a>
                        <button class="yqx-btn yqx-btn-2 yqx-btn-small js-print-btn" style="padding: 0 10px;">打印</button>
                        <button id="excelBtn" class="yqx-btn yqx-btn-1 yqx-btn-small" style="padding: 0 10px;">
                            导出excel
                        </button>
                    </div>
                </div>

            </div>

        </div>
        <div class="explain-box">
            <div class="explain-left">
                <img src="${BASE_PATH}/resources/images/explain_ico.png">
                <p>说明</p>
            </div>
            <div class="explain-right">
                <p>标准上下班时间设置请到“<a href="${BASE_PATH}/shop/conf/work-check-on">其他设置</a>”中进行设置</p>
                <p>打卡坐标“<img src="${BASE_PATH}/resources/images/location2.png">”说明员工没有在门店附近进行打卡，具体坐标请到商家版APP中进行查看</p>
            </div>
        </div>
        <div class="container">
            <div id="infoFill" class="scroll-x"></div>
        </div>
        <div class="yqx-page" id="infoPage"></div>
    </div>
</div>
<script id="infoTpl" type="text/html">
    <table class="yqx-table">
    <% if (json && json.data.content && json.data.content.length) { %>
    <% for (var i = 0; i < json.data.content.length; i++) { %>
    <% var item = json.data.content[i]; %>
    <%if(item!=null && item[0]!=null){%>
    <tr class="record-title">
        <th><%= (item[0].signDateStr) %></th>
        <th>上班时间</th>
        <th>下班时间</th>
        <th>状态</th>
    </tr>

    <% if (item && item.length) { %>
    <% for (var j=0; j<item.length; j++) { %>
    <% var subItem = item[j];%>
    <tr class="record-infor">
        <td><%= subItem.userName %></td>
        <td>
            <% if( !(subItem.signInStatus === 4) ) { %>
            <%= subItem.signInTimeStr %>
            <img src="${BASE_PATH}/resources/images/location<% if(subItem.signInLocationIsValid===1) {%>1.png<%}else{%>2.png<%}%>">
            <% } else { %>
            <span>-</span>
            <% } %>
        </td>
        <td>
            <% if( !(subItem.signOutStatus === 4) ) { %>
            <%= subItem.signOutTimeStr %>
            <img src="${BASE_PATH}/resources/images/location<% if(subItem.signOutLocationIsValid===1) {%>1.png<%}else{%>2.png<%}%>">
            <% } else { %>
            <span>-</span>
            <% } %>
        </td>
        <td
        <%if( !(subItem.signInStatus === 1 && subItem.signOutStatus === 1) ) {%>class="red"<% } %>><%=
        getSignStatus(subItem.signInStatus, subItem.signOutStatus) %></td>
    </tr>
    <%}}%>
    <% }}}else{%>
    <tr class="record-infor">
        <td colspan="4">暂无记录</td>
    </tr>
    <%  } %>
        </table>
</script>

<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<script src="${BASE_PATH}/static/js/page/report/statistics/staff/attendance.js?44c80c30455cc54a554e40c60eaf9a5e"></script>
<#include "yqx/layout/footer.ftl" >
<#--已录入列表页面-->
    <#include "yqx/layout/header.ftl">
        <link href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d" type="text/css" rel="stylesheet">
        <link href="${BASE_PATH}/static/css/page/ax_insurance/offline_insurance/entering-list.css?c72ea4082f30b8294833ff71120f9301" type="text/css" rel="stylesheet">
        <div class="wrapper clearfix">
            <div class="content clearfix">
                <#include "yqx/page/ax_insurance/left-nav.ftl">
                    <div class="right">
                        <div class="contain-wrap">
                            <p class="big-title">已录入保单列表</p>
                            <div class="main-wrap">
                                <button class="entering-btn">
                                    +录入
                                </button>
                                <div class="search-condition" id="search-condition">
                                    <input class="yqx-input car-input" placeholder="请输入车牌号">
                                    <input class="yqx-input time-choose" id="startDate" placeholder="录入时间">
                                    -
                                    <input class="yqx-input time-choose" id="endDate" placeholder="录入时间">
                                    <div class="state-div">
                                        <input class="yqx-input state-choose" value="审核状态" readonly><i class="state-choose-btn s_up"></i>
                                        <ul class="dis-none select-ul">
                                            <li data-code="">审核状态</li>
                                            <li data-code="0">未审核</li>
                                            <li data-code="1">审核通过</li>
                                            <li data-code="2">审核驳回</li>
                                        </ul>
                                    </div>
                                    <button class="search-btn">搜索</button>
                                </div>
                                <!-- 表格容器 start -->
                                <div id="tableTest"></div>
                                <!-- 表格容器 end -->
                                <!-- 分页容器 start -->
                                <div class="yqx-page" id="pagingTest"></div>
                                <!-- 分页容器 end -->
                            </div>
                        </div>
                    </div>
            </div>
        </div>
        <script type="text/html" id="tableTestTpl">
            <table class="entering-list">
                <thead>
                <tr>
                    <th>车牌号</th>
                    <th>保单号</th>
                    <th>保费</th>
                    <th>车船税</th>
                    <th>录入时间</th>
                    <th>审核状态</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
                <%if(json.data && json.data.content){%>
                <%var content = json.data.content%>
                <%if(content.length<=0){%>
                <tr class="no-data">
                    <td colspan="6">暂无数据</td>
                </tr>
                <%}%>
                <%for(var i = 0;i < content.length;i++){%>
                <tr>
                    <td rowspan="2"><%=content[i].vehicleSn%></td>
                    <td>
                        <span class="org-color">商</span>

                        <span><%if(content[i].commercialFormNo){%><%=content[i].commercialFormNo%><%}else{%>未填<%}%></span>
                    </td>
                    <td>
                        <span class="org-color">商</span>
                        <span><%if(content[i].commercialFee){%>¥ <%=content[i].commercialFee%><%}else{%>未填<%}%></span>
                    </td>
                    <td rowspan="2">
                        <span><%if(content[i].vesselTaxFee){%>¥ <%=content[i].vesselTaxFee%><%}else{%>未填<%}%></span>
                    </td>
                    <td rowspan="2"><%=$dateFormat(content[i].gmtCreate,'yyyy-MM-dd hh:mm:ss')%></td>
                    <%var auditStatus = content[i].auditStatus%>
                    <%if(auditStatus == 0){%>
                    <td rowspan="2">未审核</td>
                    <%}else if(auditStatus == 1){%>
                    <td rowspan="2" class="pass-green">审核通过</td>
                    <%}else if(auditStatus == 2){%>
                    <td rowspan="2">
                        <a href="javascript:void(0);" class="reject">审核驳回</a>
                        <div class="rejectReason">
                            <p class="reject-head">驳回原因</p>
                            <p class="reject-content"><%=content[i].rejectReason%></p>
                        </div>
                    </td>
                    <%}%>
                </tr>
                <tr class="border-bottom">
                    <td>
                        <span class="org-color">交</span>
                        <span><%if(content[i].forcibleFormNo){%><%=content[i].forcibleFormNo%><%}else{%>未填<%}%></span>
                    </td>
                    <td>
                        <span class="org-color">交</span>
                        <span>
                            <%if(content[i].forcibleFee){%>¥ <%=content[i].forcibleFee%><%}else{%>未填<%}%>
                        </span>
                    </td>
                </tr>
                <%}%>
                <%}else{%>
                <tr class="no-data">
                    <td colspan="6">暂无数据</td>
                </tr>
                <%}%>
            </table>
        </script>
        <script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart.js?4abf167c5df3e2fa7854a453b2cec085"></script>
        <script type="text/javascript" src="${BASE_PATH}/static/js/page/ax_insurance/offline_insurance/entering-list.js?7cad93440b236def597112c695cc5a6d"></script>

    <#include "yqx/layout/footer.ftl">
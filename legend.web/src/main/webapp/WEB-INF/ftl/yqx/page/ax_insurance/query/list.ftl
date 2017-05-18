
<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/query/list.css?01ab76079dc2653440e6d44b92c0f319"/>
<!-- 样式引入区 end-->
<body>
    <div class="yqx-wrapper clearfix">
    <#include "yqx/page/ax_insurance/left-nav.ftl">
        <div class="Z-right">
            <p class="Z-title">查询保单</p>
            <div class="Z-tab">
                <span class="Z-tab-item first" data-type="tbd">投保单列表</span>
                <span class="Z-tab-item second" data-type="bd">保单列表</span>
                <#if SESSION_ANXIN_INSURANCE_MODEL == 2 || SESSION_ANXIN_INSURANCE_MODEL == 0>
                <span class="Z-tab-item" data-type="tbd-fictitious">投保单(虚拟)</span>
                </#if>
            </div>
            <div class="Z-select" data-status="">
                <div class="Z-select-item">
                    <div class="form-item Z-insurerName hidden">
                        <input type="text" name="" class="yqx-input yqx-input-small insuredName" value="" placeholder="请输入被保人姓名">
                    </div>
                    <div class="form-item Z-carOwner hidden">
                        <input type="text" name="" class="yqx-input yqx-input-small carOwner" value="" placeholder="车主姓名">
                    </div>
                    <div class="form-item Z-vin hidden">
                        <input type="text" name="" class="yqx-input yqx-input-small vehicleSn" value="" placeholder="车牌号">
                    </div>
                    <div class="form-item Z-status hidden">
                        <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-select insureStatus" value="" readonly placeholder="保单状态">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-item Z-status-fictitious hidden">
                        <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-select insureStatus-fictitious" value="" readonly placeholder="虚拟投保单状态">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="Z-time hidden">
                        <div class="form-item Z-startDate-fictitious">
                            <input type="text" class="yqx-input yqx-input-small yqx-input-icon" id="startDate" placeholder="车主通知时间">
                            <span class="fa icon-small icon-calendar"></span>
                        </div>
                        <span>至</span>
                        <div class="form-item Z-endDate-fictitious">
                            <input type="text" class="yqx-input yqx-input-small yqx-input-icon" id="endDate" placeholder="车主通知时间">
                            <span class="fa icon-small icon-calendar"></span>
                        </div>
                    </div>
                </div>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small Z-search">查询</button>
            </div>

            <!-- 表格容器 start -->
            <div id="tableTest"></div>
            <!-- 表格容器 end -->

            <!-- 分页容器 start -->
            <div class="yqx-page" id="pagingTest"></div>
            <!-- 分页容器 end -->

            <!-- 表格数据模板 start -->
            <script type="text/template" id="tableTestTpl">
                <table class="yqx-table" id="tableTest">
                    <thead>
                    <tr>
                        <% if(type == 'tbd-fictitious'){%>
                        <th>创建时间</th>
                        <th>车主姓名</th>
                        <th>车主电话</th>
                        <th>车牌号</th>
                        <th>车主通知时间</th>
                        <th class="Z-expire-time">上份商业保险到期日期</th>
                        <th>虚拟投保单状态</th>
                        <%}else{%>
                        <% if(type == 'tbd'){%>
                        <th class="Z-orderSn">投保单号</th>
                        <%}else if(type == 'bd'){%>
                        <th class="Z-orderSn">保单号</th>
                        <%}%>
                        <th>险别</th>
                        <th>被保人姓名</th>
                        <th>车牌号</th>
                        <% if(type == 'tbd'){%>
                        <th>创建时间</th>
                        <%}else{%>
                        <th>缴费时间</th>
                        <th>生效时间</th>
                        <%}%>
                        <% if(type == 'tbd'){%>
                        <th>投保单状态</th>
                        <%}else{%>
                        <th>保单状态</th>
                        <%}%>
                        <%}%>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <% if(json.success && json.data.content.length > 0){%>
                        <% var content = json.data.content %>
                        <%for(var i=0;i<content.length;i++){%>
                        <tr>
                            <% if(type == 'tbd-fictitious'){%>
                                <td><%=$dateFormat(content[i].gmtCreate,'yyyy.MM.dd')%></td>
                                <td><%= content[i].basicVirtualDTO.vehicleOwnerName %></td>
                                <td><%= content[i].basicVirtualDTO.vehicleOwnerPhone %></td>
                                <td><%= content[i].basicVirtualDTO.vehicleSn %></td>
                                <td><%= $dateFormat(content[i].gmtUserNotified,'yyyy.MM.dd') %></td>
                                <td><%= $dateFormat(content[i].packageStartTime,'yyyy.MM.dd') %></td>
                                <td><%= content[i].insuranceVirtualStatusName %></td>
                                <td><span class="Z-check-detail" data-id="<%= content[i].id %>" data-basicid="<%= content[i].insuranceVirtualBasicId %>">核对信息</span></td>
                            <%}else{%>
                            <% if(type == 'tbd'){%>
                            <td class="Z-insuranceNo" title="<%= content[i].outerInsuranceApplyNo %>"><%= content[i].outerInsuranceApplyNo %></td>
                            <%}else if(type == 'bd'){%>
                            <td class="Z-insuranceNo" title="<%= content[i].outerInsuranceFormNo %>"><%= content[i].outerInsuranceFormNo %></td>
                            <%}%>
                            <td>
                                <% if(content[i].insuranceType == 1){%>
                                交强险
                                <%}else if(content[i].insuranceType == 2){%>
                                商业险
                                <%}%>
                            </td>
                            <td><%= content[i].basicVo.insuredName %></td>
                            <td><%= content[i].basicVo.vehicleSn %></td>
                            <% if(type == 'tbd'){%>
                            <td><%=$dateFormat(content[i].gmtCreate,'yyyy.MM.dd')%></td>
                            <% if(content[i].insureStatus == 0){%>
                            <td>暂存</td>
                            <%}else if(content[i].insureStatus == 1){%>
                            <td>核保失败</td>
                            <%}else if(content[i].insureStatus == 2){%>
                            <td>待缴费</td>
                            <%}else if(content[i].insureStatus == 3){%>
                            <td>生成保单</td>
                            <%}%>
                            <%}else{%>
                            <td><%=$dateFormat(content[i].payTime,'yyyy.MM.dd')%></td>
                            <td><%=$dateFormat(content[i].packageStartTime,'yyyy.MM.dd')%></td>
                            <% if(content[i].quitStatus == 0){%>
                            <td>未退保</td>
                            <%}else if(content[i].quitStatus == 1){%>
                            <td>已退保</td>
                            <%}%>
                            <%}%>
                            <td class="text-align">
                                <span class="Z-check-detail" data-id="<%= content[i].id %>" >查看</span>
                                <% if(type == 'tbd'){%>
                                <%if(content[i].insureStatus == 2 && content[i].uploadImg){%>
                                <span data-sn='<%=content[i].orderSn%>' class="rendImg">上传照片</span>
                                    <%} else if(content[i].insureStatus == 2 ){%>
                                        <span data-sn='<%=content[i].orderSn%>' class="goPay">去缴费</span>
                                    <%}%>
                                <%}%>
                                <% if(content[i].insureStatus == 0 || content[i].insureStatus == 2 ){%>
                                <span class="Z-delete" data-id="<%= content[i].id %>" >删除</span>
                                <%}%>
                            </td>
                            <%}%>
                        </tr>
                        <%}%>
                    <%}else{%>
                        <tr><td class="no-data">暂无数据</td></tr>
                    <%}%>
                </table>
            </script>
        </div>
    </div>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/ax_insurance/query/list.js?ddead7a1cb9abe1273cbc05681cc6381"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
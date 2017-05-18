<#include "layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/rp-common.css?dd7cb8a982e68410bef755700d5a34c4"/>
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/index.css?adb24f81584fdc4737865b50a51f326e"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/style/modules/paging.css?0a59540bff205589e7dc7ad09496808e"/>
<script type="application/javascript" src="${BASE_PATH}/resources/js/common/paging.js?c4ece14e0f4c534b806efc96ae712410"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/report/statistics_order_info_detail.js?68911bbebe9c621957805afa1bdf9f35"></script>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/report/statistics_order_info_detail.css?c12d1c429d380634e455599b51cffe46">
<div class="wrapper clearfix">
    <#include "report/left-nav.ftl"/>
    <div class="main">
        <div class="main-head">
            <form id="searchForm">
                <div class="search-form">
                    <div class="show-grid">
                        <div class="col-9">
                            <label class="w49">开单时间</label>
                            <div class="form-item wid2">
                            <input type="text" id="createTimeStart" name="createTimeStart" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'createTimeEnd\')||\'new Date()\'}'})" class="rp-input rp-input-icon2" placeholder="开始时间">
                            </div>
                            <label class="w49">至</label>
                            <div class="form-item wid2">
                            <input type="text" id="createTimeEnd" name="createTimeEnd" onclick="WdatePicker({minDate:'#F{$dp.$D(\'createTimeStart\')}', maxDate:new Date()})" class="rp-input rp-input-icon2" placeholder="结束时间">
                            </div>
                            <label class="w49">车牌</label>
                            <div class="form-item wid22">
                                <input type="text" id="carLicense" class="rp-input">
                            </div>
                            <label class="w49">车主</label>
                            <div class="form-item wid22">
                                <input type="text" id="customerName" class="rp-input">
                            </div>

                        </div>
                        <div class="col-1 right">
                            <button class="btn btn-1 btn-save" id="searchBtn" type="button">查询</button>
                        </div>
                    </div>
                    <div class="show-grid heit">
                        <div class="col-9">
                            <label class="w49">工单编号</label>
                            <div class="form-item wid2">
                                <input type="text" id="orderSn" class="rp-input">
                            </div>

                            <label class="w49">工单类型</label>
                            <div class="form-item wid2">
                                <select id="orderTag" class="rp-input">
                                    <option value="">全部</option>
                                    <option value="1">综合维修</option>
                                    <option value="2">洗车单</option>
                                    <option value="3">快修快保</option>
                                    <option value="4">引流活动</option>
                                    <option value="5">销售</option>
                                </select>
                            </div>

                            <label class="w49">工单状态</label>
                            <div class="form-item wid22">
                                <select id="orderStatus" class="rp-input">
                                    <option value="">全部</option>
                                    <option value="CJDD">待报价</option>
                                    <option value="DDBJ">已报价</option>
                                    <option value="FPDD">已派工</option>
                                    <option value="DDSG">修理中</option>
                                    <option value="DDWC">已完工</option>
                                    <option value="DDYFK">已结算</option>
                                </select>
                            </div>

                            <label class="w49">服务顾问</label>
                            <div class="form-item wid22">
                                <select id="receiver" class="rp-input">
                                    <option value="">全部</option>
                                </select>
                            </div>

                        </div>
                        <a href="javascript:;" id="excelBtn" class="mt14ml25" type="button">导出excel</a>

                        <div class="dropdown fr">
                            <p class="dropdown-title"><span class="rp-plus-icon">+</span>列表字段设置</p>
                            <ul class="dropdown-menu">
                                <li><label for="cb1"><input  type="checkbox" data-ref="cb1" checked/>工单编号</label></li>
                                <li><label for="cb2"><input  type="checkbox" data-ref="cb2" checked/>工单类型</label></li>
                                <li><label for="cb3"><input  type="checkbox" data-ref="cb3" checked/>业务类型</label></li>
                                <li><label for="cb4"><input  type="checkbox" data-ref="cb4" checked/>车牌</label></li>
                                <li><label for="cb5"><input  type="checkbox" data-ref="cb5" checked/>车主</label></li>
                                <li><label for="cb6"><input  type="checkbox" data-ref="cb6" checked/>开单时间</label></li>
                                <li><label for="cb7"><input  type="checkbox" data-ref="cb7" checked/>结算时间</label></li>
                                <li><label for="cb8"><input  type="checkbox" data-ref="cb8" checked/>工单状态</label></li>
                                <li><label for="cb9"><input  type="checkbox" data-ref="cb9" checked/>服务顾问</label></li>
                                <li><label for="cb10"><input  type="checkbox" data-ref="cb9" checked/>销售员</label></li>
                                <li><label for="cb11"><input  type="checkbox" data-ref="cb10" checked/>维修工</label></li>
                                <li><label for="cb12"><input  type="checkbox" data-ref="cb11" checked/>工时</label></li>
                                <li><label for="cb14"><input  type="checkbox" data-ref="cb14" checked/>总计</label></li>
                                <li><label for="cb13"><input  type="checkbox" data-ref="cb13" checked/>应收金额</label></li>
                                <li><label for="cb15"><input  type="checkbox" data-ref="cb15" checked/>物料成本</label></li>
                                <li><label for="cb16"><input  type="checkbox" data-ref="cb16" checked/>毛利</label></li>
                                <li><label for="cb17"><input  type="checkbox" data-ref="cb17" checked/>毛利率</label></li>
                            </ul>
                        </div>
                    </div>
                    <div class="show-grid heit">
                        <div class="col-9">
                            <label class="w49">结算时间</label>
                            <div class="form-item wid2">
                                <input type="text" id="payTimeStart" name="payTimeStart" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'payTimeEnd\')||\'new Date()\'}'})" class="rp-input rp-input-icon2" placeholder="开始时间">
                            </div>
                            <label class="w49">至</label>
                            <div class="form-item wid2">
                                <input type="text" id="payTimeEnd" name="payTimeEnd" onclick="WdatePicker({minDate:'#F{$dp.$D(\'payTimeStart\')}', maxDate:new Date()})" class="rp-input rp-input-icon2" placeholder="结束时间">
                            </div>


                            <label class="w49">维修工</label>
                            <div class="form-item wid22">
                                <select id="worker" class="rp-input">
                                    <option value="">全部</option>
                                </select>
                            </div>

                            <label class="w49">销售员</label>
                            <div class="form-item wid22">
                                <select id="saler" class="rp-input">
                                    <option value="">全部</option>
                                </select>
                            </div>


                        </div>
                    </div>

                </div>
            </form>
        </div>
        <#--<div class="rp-panel">-->
            <#--<div class="rp-panel-head clearfix">-->
                <#--<div class="dropdown fr">-->
                    <#--<p class="dropdown-title"><span class="rp-plus-icon">+</span>添加</p>-->
                    <#--<ul class="dropdown-menu">-->
                        <#--<li><label for="cb1"><input id="cb1" type="checkbox" data-ref="order" checked/>工单</label></li>-->
                        <#--<li><label for="cb2"><input id="cb2" type="checkbox" data-ref="revenue" checked/>营收</label></li>-->
                        <#--<li><label for="cb3"><input id="cb3" type="checkbox" data-ref="purchase" checked/>采购</label></li>-->
                        <#--<li><label for="cb4"><input id="cb4" type="checkbox" data-ref="recharge" checked/>会员充值</label></li>-->
                        <#--<li><label for="cb5"><input id="cb5" type="checkbox" data-ref="account" checked/>账务</label></li>-->
                    <#--</ul>-->
                <#--</div>-->
            <#--</div>-->
            <#--<div class="rp-panel-body" id="content"></div>-->
        <#--</div>-->
        <div class="rp-panel-body scroll" id="content"></div>
        <div class="qxy_page" style="text-align: left;">
            <div class="qxy_page_inner"></div>
        </div>
    </div>
</div>
<script id="mainTpl" type="text/html">
    <table class="table-hover table2">
        <thead>
        <tr>
            <th class="cb1">工单编号</th>
            <th class="cb2">工单类型</th>
            <th class="cb3">业务类型</th>
            <th class="cb4">车牌</th>
            <th class="cb5">车主</th>
            <th class="cb6">开单时间</th>
            <th class="cb7">结算时间</th>
            <th class="cb8">工单状态</th>
            <th class="cb9">服务顾问</th>
            <th class="cb10">销售员</th>
            <th class="cb11">维修工</th>
            <th class="cb12">工时</th>
            <th class="cb14">总计</th>
            <th class="cb13">应收金额</th>
            <th class="cb15">物料成本</th>
            <th class="cb16">毛利</th>
            <th class="cb17">毛利率</th>
        </tr>
        </thead>
        <% var i, item; %>
        <tbody>
        <%
        for(i = 0; i < data.content.length; i++) {
            item = data.content[i];
        %>
        <tr>
            <td class="cb1"><%= item.orderSn %></td>
            <td class="cb2"><%= item.orderTag %></td>
            <td class="cb3"><%= item.orderType %></td>
            <td class="cb4"><%= item.carLicense %></td>
            <td class="cb5 hiddenLong" title="<%= item.customerName %>"><%= item.customerName %></td>
            <td class="cb6"><%= item.createTime %></td>
            <td class="cb7"><%= item.payTime %></td>
            <td class="cb8"><%= item.orderStatus %></td>
            <td class="cb9"><%= item.receiverName %></td>
            <td class="cb10"><%= item.salerName %></td>
            <td class="cb11"><%= item.workers %></td>
            <td class="cb12"><%= item.serviceHour %></td>
            <th class="cb14"><%= item.allTotalAmount %></th>
            <%if(item.orderStatus != "已结算"){%>
            <td class="cb13">--</td>
            <%}else{%>
            <td class="cb13"><%= item.totalAmount %></td>
            <%}%>
            <td class="cb15"><%= item.goodsAmount %></td>
            <%if(item.orderStatus != "已结算"){%>
            <td class="cb16">--</td>
            <%}else{%>
            <td class="cb16"><%= item.grossProfitAmount %></td>
            <%}%>
            <%if(item.orderStatus != "已结算"){%>
            <td class="cb17">--</td>
            <%}else{%>
            <td class="cb17"><%= item.grossProfitRate %></td>
            <%}%>
        </tr>
        <% } %>
        </tbody>
    </table>
</script>
<script src="${BASE_PATH}/resources/js/report/rp-common.js?a9101f6d273e478d666d3d8625fbd697"></script>
<#include "layout/footer.ftl" >
<#include "layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/rp-common.css?dd7cb8a982e68410bef755700d5a34c4"
      xmlns="http://www.w3.org/1999/html"/>
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/index.css?adb24f81584fdc4737865b50a51f326e"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/style/modules/paging.css?0a59540bff205589e7dc7ad09496808e"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/report/statistics_order_info_detail.css?c12d1c429d380634e455599b51cffe46">
<script type="application/javascript" src="${BASE_PATH}/resources/js/common/paging.js?c4ece14e0f4c534b806efc96ae712410"></script>
<div class="wrapper clearfix">
    <#include "report/left-nav.ftl"/>
    <div class="main">
        <div class="main-head">
            <form id="searchForm">
                <div class="search-form">
                    <div class="show-grid">
                        <div class="col-9">
                            <label>收款时间</label>
                            <div class="form-item w140">
                            <input type="text" id="sPayTime" name="sPayTime" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'ePayTime\')||\'new Date()\'}'})" class="rp-input rp-input-icon" placeholder="选择开始时间">
                            </div>
                            <label class="w50">至</label>
                            <div class="form-item w140">
                            <input type="text" id="ePayTime" name="ePayTime" onclick="WdatePicker({minDate:'#F{$dp.$D(\'sPayTime\')}', maxDate:new Date()})" class="rp-input rp-input-icon" placeholder="选择结束时间">
                            </div>
                            <label class="w50">车牌</label>
                            <div class="form-item w140">
                                <input type="text" id="license" class="rp-input">
                            </div>
                        </div>
                        <div class="col-1 right">
                            <button class="btn btn-1 btn-save" id="searchBtn" type="button">查询</button>
                        </div>
                        <div class="col-9 mt5">
                            <label>结算类型</label>
                            <div class="form-item w140">
                                <select id="status" class="rp-input">
                                    <option value="">全部</option>
                                    <option value="0">实付</option>
                                    <option value="1">坏账</option>
                                </select>
                            </div>
                            <label class="w50">车主</label>
                            <div class="form-item w140">
                                <input type="text" id="customerName" class="rp-input">
                            </div>
                        </div>
                        <div class="col-9 mt5">
                            <label>工单编号</label>
                            <div class="form-item w140">
                                <input type="text" id="orderSn" class="rp-input">
                            </div>
                            <label>服务顾问</label>
                            <div class="form-item w140">
                                <select id="serverId" class="rp-input">
                                    <option value ="">全部</option>
                                    <#list shopManagers as item >
                                        <option value ="${item.id}">${item.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>

                        <a href="javascript:;" id="excelBtn" class="mt14ml25" type="button">导出excel</a>
                        <div class="dropdown fr">
                            <p class="dropdown-title"><span class="rp-plus-icon">+</span>列表字段设置</p>
                            <ul class="dropdown-menu">
                                <li><label for="cb1"><input  type="checkbox" data-ref="cb1" checked/>收款时间</label></li>
                                <li><label for="cb2"><input  type="checkbox" data-ref="cb2" checked/>工单编号</label></li>
                                <li><label for="cb3"><input  type="checkbox" data-ref="cb3" checked/>车牌</label></li>
                                <li><label for="cb4"><input  type="checkbox" data-ref="cb4" checked/>车主</label></li>
                                <li><label for="cb5"><input  type="checkbox" data-ref="cb5" checked/>总计</label></li>
                                <li><label for="cb6"><input  type="checkbox" data-ref="cb6" checked/>应收金额</label></li>
                                <li><label for="cb7"><input  type="checkbox" data-ref="cb7" checked/>物料成本</label></li>
                                <li><label for="cb8"><input  type="checkbox" data-ref="cb8" checked/>毛利</label></li>
                                <li><label for="cb9"><input  type="checkbox" data-ref="cb9" checked/>实收金额</label></li>
                                <li><label for="cb10"><input  type="checkbox" data-ref="cb10" checked/>现金</label></li>
                                <li><label for="cb11"><input  type="checkbox" data-ref="cb11" checked/>会员卡</label></li>
                                <li><label for="cb12"><input  type="checkbox" data-ref="cb12" checked/>银行转账</label></li>
                                <li><label for="cb13"><input  type="checkbox" data-ref="cb13" checked/>支付宝支付</label></li>
                                <li><label for="cb14"><input  type="checkbox" data-ref="cb14" checked/>信用卡</label></li>
                                <li><label for="cb15"><input  type="checkbox" data-ref="cb15" checked/>储蓄卡</label></li>
                                <li><label for="cb16"><input  type="checkbox" data-ref="cb16" checked/>微信支付</label></li>
                                <li><label for="cb17"><input  type="checkbox" data-ref="cb17" checked/>转账支票</label></li>
                                <li><label for="cb18"><input  type="checkbox" data-ref="cb18" checked/>现金支票</label></li>
                                <li><label for="cb19"><input  type="checkbox" data-ref="cb19" checked/>其它</label></li>
                                <li><label for="cb20"><input  type="checkbox" data-ref="cb20" checked/>自定义</label></li>
                                <li><label for="cb21"><input  type="checkbox" data-ref="cb21" checked/>挂账</label></li>
                                <li><label for="cb22"><input  type="checkbox" data-ref="cb22" checked/>结算状态</label></li>
                                <li><label for="cb23"><input  type="checkbox" data-ref="cb23" checked/>坏账金额</label></li>
                                <li><label for="cb24"><input  type="checkbox" data-ref="cb24" checked/>结算人</label></li>
                                <li><label for="cb25"><input  type="checkbox" data-ref="cb25" checked/>服务顾问</label></li>
                            </ul>
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
            <th class="cb1">收款时间</th>
            <th class="cb2">工单编号</th>
            <th class="cb3">车牌</th>
            <th class="cb4">车主</th>
            <th class="cb5">总计</th>
            <th class="cb6">应收金额</th>
            <th class="cb7">物料成本</th>
            <th class="cb8">毛利</th>
            <th class="cb9">实收金额</th>
            <th class="cb10 red">现金</th>
            <th class="cb11 red">会员卡</th>
            <th class="cb12 red">银行转账</th>
            <th class="cb13 red">支付宝支付</th>
            <th class="cb14 red">信用卡</th>
            <th class="cb15 red">储蓄卡</th>
            <th class="cb16 red">微信支付</th>
            <th class="cb17 red">转账支票</th>
            <th class="cb18 red">现金支票</th>
            <th class="cb19 red">其它</th>
            <th class="cb20 red">自定义</th>
            <th class="cb21">挂账</th>
            <th class="cb22">结算状态</th>
            <th class="cb23">坏账金额</th>
            <th class="cb24">结算人</th>
            <th class="cb25">服务顾问</th>
        </tr>
        </thead>
        <% var i, item; %>
        <tbody>
        <%
        for(i = 0; i < data.content.length; i++) {
            item = data.content[i];
        %>
        <tr>
            <td class="cb1"><%= item.statisDate %></td>
            <td class="cb2"><%= item.orderSn %></td>
            <td class="cb3"><%= item.license %></td>
            <td class="cb4 hiddenLong" title="<%= item.customerName %>"><%= item.customerName %></td>
            <td class="cb5"><%= item.totalPayAmount %></td>
            <td class="cb6"><%= item.totalAmount %></td>
            <td class="cb7"><%= item.costAmount %></td>
            <td class="cb8"><%= item.grossProfit %></td>
            <td class="cb9"><%= item.payAmount %></td>
            <td class="cb10 red"><%= item.cash %></td>
            <td class="cb11 red"><%= item.member %></td>
            <td class="cb12 red"><%= item.bank %></td>
            <td class="cb13 red"><%= item.thirdPay %></td>
            <td class="cb14 red"><%= item.credit %></td>
            <td class="cb15 red"><%= item.card %></td>
            <td class="cb16 red"><%= item.wei %></td>
            <td class="cb17 red"><%= item.transfer %></td>
            <td class="cb18 red"><%= item.check %></td>
            <td class="cb19 red"><%= item.other %></td>
            <td class="cb20 red"><%= item.defined %></td>
            <td class="cb21"><%= item.signAmount %></td>
            <td class="cb22"><%= item.status %></td>
            <td class="cb23"><%= item.bad %></td>
            <td class="cb24"><%= item.operatorName %></td>
            <td class="cb25"><%= item.serverName %></td>
        </tr>
        <% } %>
        </tbody>
    </table>
</script>
<script src="${BASE_PATH}/resources/js/report/rp-common.js?a9101f6d273e478d666d3d8625fbd697"></script>
<script src="${BASE_PATH}/resources/js/report/statistics_order_payment.js?f40529996a1ad519541f0d3b0046269f"></script>
<#include "layout/footer.ftl" >
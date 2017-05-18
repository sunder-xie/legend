<#include "layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/rp-common.css?dd7cb8a982e68410bef755700d5a34c4"/>
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/index.css?adb24f81584fdc4737865b50a51f326e"/>
<div class="wrapper clearfix">
    <div class="aside">
        <!-- 导航栏 start -->
        <div class="nav js-new-nav">
            <div class="nav-box">
                <h1 class="nav-box-title js-nav-title">经营报表</h1>
                <ul class="nav-box-list js-nav-list">
                    <li class="current" id="n11"><a href="${BASE_PATH}/report">营业日报</a></li>
                    <li <#if moduleUrlTab == "order_info_detail">class="current" id="n12"</#if>><a href="${BASE_PATH}/shop/stats/order_info_detail">工单流水表</a></li>
                    <#--<li>工单流水明细</li>-->
                    <#--<li>配件销售明细</li>-->
                </ul>
            </div>
            <#--<div class="nav-box">-->
                <#--<h1 class="nav-box-title js-nav-title">管理报表</h1>-->
                <#--<ul class="nav-box-list js-nav-list">-->
                    <#--<li>营业汇总</li>-->
                    <#--<li>服务类型分析</li>-->
                    <#--<li>配件销售概况</li>-->
                    <#--<li>车流统计</li>-->
                    <#--<li>库存分析</li>-->
                    <#--<li>员工绩效</li>-->
                <#--</ul>-->
            <#--</div>-->
            <#--<div class="nav-box">-->
                <#--<h1 class="nav-box-title js-nav-title">采销存报表</h1>-->
                <#--<ul class="nav-box-list js-nav-list">-->
                    <#--<li>库存表</li>-->
                    <#--<li>配件入库表</li>-->
                    <#--<li>配件出库表</li>-->
                <#--</ul>-->
            <#--</div>-->
            <#--<div class="nav-box">-->
                <#--<h1 class="nav-box-title js-nav-title">财务报表</h1>-->
                <#--<ul class="nav-box-list js-nav-list">-->
                    <#--<li>工单结算收款表</li>-->
                    <#--<li>会员充值收款表</li>-->
                    <#--<li>采购付款表</li>-->
                <#--</ul>-->
            <#--</div>-->
        </div>
        <!-- 导航栏 end -->
        <!-- 下线提醒弹窗 start -->
        <script id="oldTipsTpl" type="text/html">
            <div class="dg-panel">
                <div class="dg-panel-head">
                    <h1 class="dg-panel-title">下线提醒</h1>
                </div>
                <div class="dg-panel-body">
                    <div class="dg-content">
                        <p class="dg-msg">亲，该报表将于2016年5月23日下线，请去新版“<%= msg %>”查看经营毛利，谢谢！</p>
                        <div class="dg-note">（有疑问请联系淘汽云修客服，联系电话400-9937-288转2转3，或添加客服QQ3320478090、3056630970、3274979298）</div>
                        <div class="dg-btns-tool">
                            <a class="btn btn-1" href="<%= newLink %>">新版<%= msg %></a>
                            <a class="btn btn-2" href="<%= oldLink %>" target="_blank">继续看老版</a>
                        </div>
                    </div>
                </div>
            </div>
        </script>
        <!-- 下线提醒弹窗 end -->
        <!-- 导航JS代码 start -->
        <script>
            $(function() {
                $(document).on('click', '.js-jump', function() {
                    var $this = $(this),
                        data = {
                            newLink : $this.data('newLink'),
                            oldLink : $this.data('oldLink'),
                            msg     : $this.data('msg')
                        };

                    $.layer({
                        type: 1,
                        title: false,
                        area: ['auto', 'auto'],
                        border: [0],
                        shadow: ['0.5', '#000'],
                        closeBtn: [1, true],
                        shift: "top",
                        page: {
                            html: template('oldTipsTpl', data)
                        }
                    });
                });
            })
        </script>
        <!-- 导航JS代码 end -->
    </div>
    <div class="main">
        <div class="main-head">
            <form id="searchForm">
                <div class="search-form">
                    <div class="show-grid">
                        <div class="col-9">
                            <div class="form-label">每日统计：</div>
                            <div class="form-item w160">
                                <input type="hidden" id="shopId" value="${shopId}"/>
                                <input type="text" class="rp-input rp-input-icon" id="time"
                                       onfocus="WdatePicker({maxDate: '%y-%M-%d'})" value=""/>
                                <span class="rp-suffix-icon rp-calendar-icon"></span>
                            </div>
                            <div class="quick-search">
                                <span class="qs-item js-qs-item" data-offset="0">今天</span>
                                <span class="qs-item js-qs-item" data-offset="-1">昨天</span>
                            </div>
                        </div>
                        <div class="col-3 right">
                            <button class="btn btn-1 btn-save" id="searchBtn" type="button">查询</button>
                        </div>
                    </div>
                </div>
            </form>
            <div class="rp-tool-row clearfix">
                <p class="rp-note fl js-dialog dl">报表说明</p>
                <a class="rp-export-excel fr" href="#" id="exportBtn"><span class="rp-export-icon"></span>导出Excel</a>
            </div>
        </div>
        <div class="rp-panel">
            <div class="rp-panel-head clearfix">
                <div class="dropdown fr">
                    <p class="dropdown-title"><span class="rp-plus-icon">+</span>添加</p>
                    <ul class="dropdown-menu">
                        <li><label for="cb1"><input id="cb1" type="checkbox" data-ref="order" checked/>工单</label></li>
                        <li><label for="cb2"><input id="cb2" type="checkbox" data-ref="revenue" checked/>营收</label></li>
                        <li><label for="cb3"><input id="cb3" type="checkbox" data-ref="purchase" checked/>采购</label></li>
                        <li><label for="cb4"><input id="cb4" type="checkbox" data-ref="recharge" checked/>会员充值</label></li>
                        <li><label for="cb5"><input id="cb5" type="checkbox" data-ref="account" checked/>账务</label></li>
                    </ul>
                </div>
            </div>
            <div class="rp-panel-body" id="content"></div>
        </div>
    </div>
</div>
<script id="mainTpl" type="text/html">
    <% if(success && data) { %>
    <table class="table collect">
        <thead class="highlight-head">
        <tr>
            <th>今日收款</th>
            <th>今日付款</th>
            <th>今日结算</th>
            <th>单车产值</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><%= data.totalPayAmount %></td>
            <td><%= data.purchaseTotalPayAmount %></td>
            <td><%= data.orderTotalPayCount %>单</td>
            <td><%= data.totalCarValueAmount %></td>
        </tr>
        </tbody>
    </table>
    <% include('orderTpl', data); %>
    <% if(data.paymentVOList && data.paymentVOList.length) { %>
        <% include('receivableTpl', data); %>
        <% include('purchaseTpl', data); %>
        <% include('rechargeTpl', data); %>
        <% include('accountTpl', data); %>
    <% }} %>
</script>
<script id="orderTpl" type="text/html">
    <!-- 工单 start -->
    <table class="table table-hover" id="order">
        <caption>
            <h1 class="table-title">工单</h1>
        </caption>
        <thead>
        <tr>
            <th>类型</th>
            <th class="right">今日接单</th>
            <th class="right">今日结算</th>
            <th class="right">累计待结算车辆</th>
            <th class="right">今日新建预约</th>
        </tr>
        </thead>
        <% var i, item, amount; %>
        <% if( orderInfoVOList && (len = orderInfoVOList.length) ) { %>
        <tbody>
        <%
        for(i = 0; i < len; i++) {
            item = orderInfoVOList[i];
            if(item == null){
                continue;
            }
            if(item.orderType == '总计') {
                amount = item;
                continue;
            }
        %>
        <tr>
            <td><%= item.orderType %></td>
            <td class="right"><%= item.orderCreateCount %></td>
            <td class="right"><%= item.orderPayCount %></td>
            <td class="right"><%= item.orderUnPayCount %></td>
            <td class="right"><%= item.appointCreateCount %></td>
        </tr>
        <% } %>
        </tbody>
        <% if(amount) { %>
        <tfoot>
        <tr>
            <th>总计</th>
            <td class="right"><%= amount.orderCreateCount %></td>
            <td class="right"><%= amount.orderPayCount %></td>
            <td class="right"><%= amount.orderUnPayCount %></td>
            <td class="right"><%= amount.appointCreateCount %></td>
        </tr>
        </tfoot>
        <% }} %>
    </table>
    <!-- 工单 end -->
</script>
<script id="receivableTpl" type="text/html">
    <!-- 营收 start -->
    <table class="table table-hover" id="revenue">
        <caption>
            <h1 class="table-title">营收</h1>
        </caption>
        <thead>
        <tr>
            <th>支付方式</th>
            <th class="right">今日营业应收</th>
            <th class="right">成本</th>
            <th class="right">毛利</th>
            <th class="right">今日营业实收</th>
        </tr>
        </thead>
        <% var i, item, amount; %>
        <% if ( paymentVOList[0] && (len = paymentVOList[0].length) ) { %>
        <tbody>
        <%
        for(i = 0; i < len; i++) {
            item = paymentVOList[0][i];
            if(item == null){
                continue;
            }
            if(item.payMethodName == '总计') {
                amount = item;
                continue;
            }
        %>
        <tr>
            <td><%= item.payMethodName %></td>
            <td class="right"><%= thousandSep(item.totalAmount) %></td>
            <td class="right"><%= thousandSep(item.costAmount) %></td>
            <td class="right"><%= thousandSep(item.grossProfitAmount) %></td>
            <td class="right"><%= thousandSep(item.payAmount) %></td>
        </tr>
        <% } %>
        </tbody>
        <% if(amount) { %>
        <tfoot>
        <tr>
            <th>总计</th>
            <td class="right"><%= thousandSep(amount.totalAmount) %></td>
            <td class="right"><%= thousandSep(amount.costAmount) %></td>
            <td class="right"><%= thousandSep(amount.grossProfitAmount) %></td>
            <td class="right"><%= thousandSep(amount.payAmount) %></td>
        </tr>
        </tfoot>
        <% }} %>
    </table>
    <!-- 营收 end -->
</script>
<script id="purchaseTpl" type="text/html">
    <!-- 采购 start -->
    <table class="table table-hover" id="purchase">
        <caption>
            <h1 class="table-title">采购</h1>
        </caption>
        <thead>
        <tr>
            <th>支付方式</th>
            <th class="right">今日采购应付</th>
            <th class="right">今日采购实付</th>
        </tr>
        </thead>
        <% var i, item, amount; %>
        <% if ( paymentVOList[1] && (len = paymentVOList[1].length) ) { %>
        <tbody>
        <%
        for(i = 0; i < len; i++) {
            item = paymentVOList[1][i];
            if(item == null){
                continue;
            }
            if(item.payMethodName == '总计') {
                amount = item;
                continue;
            }
        %>
        <tr>
            <td><%= item.payMethodName %></td>
            <td class="right"><%= thousandSep(item.purchaseTotalAmount) %></td>
            <td class="right"><%= thousandSep(item.purchasePayAmount) %></td>
        </tr>
        <% } %>
        </tbody>
        <% if(amount) { %>
        <tfoot>
        <tr>
            <th>总计</th>
            <td class="right"><%= thousandSep(amount.purchaseTotalAmount) %></td>
            <td class="right"><%= thousandSep(amount.purchasePayAmount) %></td>
        </tr>
        </tfoot>
        <% }} %>
    </table>
    <!-- 采购 end -->
</script>
<script id="rechargeTpl" type="text/html">
    <!-- 会员充值 start -->
    <table class="table table-hover" id="recharge">
        <caption>
            <h1 class="table-title">会员充值</h1>
        </caption>
        <thead>
        <tr>
            <th>支付方式</th>
            <th class="right">今日充值应收</th>
            <th class="right">今日充值实收</th>
        </tr>
        </thead>
        <% var i, item, amount; %>
        <% if ( paymentVOList[2] && (len = paymentVOList[2].length) ) { %>
        <tbody>
        <%
        for(i = 0; i < len; i++) {
            item = paymentVOList[2][i];
            if(item == null){
                continue;
            }
            if(item.payMethodName == '总计') {
                amount = item;
                continue;
            }
        %>
        <tr>
            <td><%= item.payMethodName %></td>
            <td class="right"><%= thousandSep(item.totalAmount) %></td>
            <td class="right"><%= thousandSep(item.payAmount) %></td>
        </tr>
        <% } %>
        </tbody>
        <% if(amount) { %>
        <tfoot>
        <tr>
            <th>总计</th>
            <td class="right"><%= thousandSep(amount.totalAmount) %></td>
            <td class="right"><%= thousandSep(amount.payAmount) + "（" + memberChargeCount + "单）" %></td>
        </tr>
        </tfoot>
        <% }} %>
    </table>
    <!-- 会员充值 end -->
</script>
<script id="accountTpl" type="text/html">
    <!-- 账务 start -->
    <table class="table table-hover" id="account">
        <caption>
            <h1 class="table-title">账务</h1>
        </caption>
        <thead>
        <tr>
            <th>支付方式</th>
            <th class="right">累计营业应收欠款</th>
            <th class="right">今日营业实收欠款</th>
            <th class="right">累计采购应付欠款</th>
            <th class="right">今日采购实付欠款</th>
        </tr>
        </thead>
        <% var i, item, amount; %>
        <% if ( paymentVOList[3] && (len = paymentVOList[3].length) ) { %>
        <tbody>
        <%
        for(i = 0; i < len; i++) {
            item = paymentVOList[3][i];
            if(item == null){
                continue;
            }
            if(item.payMethodName == '总计') {
                amount = item;
                continue;
        }
        %>
        <tr>
            <td><%= item.payMethodName %></td>
            <td class="right"><%= thousandSep(item.totalAmount) %></td>
            <td class="right"><%= thousandSep(item.payAmount) %></td>
            <td class="right"><%= thousandSep(item.purchaseTotalAmount) %></td>
            <td class="right"><%= thousandSep(item.purchasePayAmount) %></td>
        </tr>
        <% } %>
        </tbody>
        <% if(amount) { %>
        <tfoot>
        <tr>
            <th>总计</th>
            <td class="right"><%= thousandSep(amount.totalAmount) %></td>
            <td class="right"><%= thousandSep(amount.payAmount) %></td>
            <td class="right"><%= thousandSep(amount.purchaseTotalAmount) %></td>
            <td class="right"><%= thousandSep(amount.purchasePayAmount) %></td>
        </tr>
        </tfoot>
        <% }} %>
    </table>
    <!-- 账务 end -->

</script>
<#--弹框-->
<script type="text/template" id="ins-dialog">
<div id="instruction">
    <div class="rp-panel-head">
        <h1 class="rp-panel-title">报表说明</h1>
    </div>
    <div class="rp-panel-body">
        <div class="ins">
            <h2>营业日报</h2>
            <ul>
                <li><span>今日收款：</span>今日营业实收 + 今日会员充值实收 + 今日实收营业欠款</li>
                <li><span>今日付款：</span>今日采购实付 + 今日实付采购欠款</li>
                <li><span>今日结算：</span>今日结算的工单数</li>
                <li><span>单车产值：</span>今日结算的工单应收金额平均数，即今日营业应收 ÷ 今日结算单数</li>
            </ul>
        </div>

        <div class="ins">
            <h2>接单</h2>
            <ul>
                <li><span>今日接单：</span>今日开单总数，不包含无效工单</li>
                <li><span>今日结算：</span>今日结算的工单数</li>
                <li><span>累计未结车辆：</span>当前未结算的所有工单数</li>
                <li><span>今日新建预约：</span>今日新产生的预约单数</li>
            </ul>
        </div>
        <div class="ins">
            <h2>营收</h2>
            <ul>
                <li><span>今日营业应收：</span>今日结算工单的应收金额总和</li>
                <li><span>今日营业实收：</span>今日结算工单的实收金额总和（包含会员卡支付）</li>
                <li><span>成本：</span>当日结算工单的配件采购价总和</li>
                <li><span>毛利：</span>今日营业应收 - 成本</li>
            </ul>
        </div>
        <div class="ins">
            <h2>采购</h2>
            <ul>
                <li><span>今日采购应付：</span>今日创建的入库单的总金额</li>
                <li><span>今日采购实付：</span>今日创建的入库单中，已付款的金额</li>
            </ul>
        </div>
        <div class="ins">
            <h2>会员充值</h2>
            <ul>
                <li><span>今日充值应收：</span>今日会员充值收款金额</li>
                <li><span>今日充值实收：</span>今日会员充值收款金额</li>
            </ul>
        </div>
        <div class="ins">
            <h2>账务</h2>
            <ul>
                <li><span>累计应收营业欠款：</span>所有结算后未付清（即挂账中）的工单，挂账部分的总额</li>
                <li><span>今日实收营业欠款：</span>今日实收的挂账金额</li>
                <li><span>累计应付采购欠款：</span>所有今日之前创建的，未结清的入库单，未结清部分的总额</li>
                <li><span>今日实付采购欠款：</span>今日实付的今日之前创建的入库单金额</li>
            </ul>
        </div>
        <div class="ins">

            <p><span>注：</span>对工单进行重新结算和无效都会影响报表历史数据，请谨慎操作。</p>

        </div>
    </div>
</div>
</script>
<#--弹框end-->
<script src="${BASE_PATH}/resources/js/report/rp-common.js?a9101f6d273e478d666d3d8625fbd697"></script>
<script src="${BASE_PATH}/resources/js/report/index.js?77d1f87436a391c7aba03e0d9958cbf8"></script>
<#include "layout/footer.ftl" >
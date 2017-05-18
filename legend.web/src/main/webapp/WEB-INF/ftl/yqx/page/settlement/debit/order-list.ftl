<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/card.css?b7e57f9bc402289671f029f941d2ffe3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/debit/order-list.css?74979bc1e9040fb2d134386e8e9ee3ab"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/settlement/settlement-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline fl">工单收款</h1>
        </div>
        <!-- 查询条件 start -->
        <div class="search-box clearfix" id="searchForm">
            <div class="form-item form-item-width">
                <input type="text" name="search_keyword" class="yqx-input yqx-input-small" value="" placeholder="车牌/车主电话/车主">
            </div>
            <div class="form-item form-item-width">
                <input type="text" name="search_startTime" class="yqx-input yqx-input-icon yqx-input-small js-date" id="startDate" value="" placeholder="开单开始日期">
                <span class="fa icon-calendar icon-small"></span>
            </div>
            至
            <div class="form-item form-item-width">
                <input type="text" name="search_endTime" class="yqx-input yqx-input-icon yqx-input-small js-date" id="endDate" value="" placeholder="开单结束日期">
                <span class="fa icon-calendar icon-small"></span>
            </div>

            <div class="form-item form-item-width">
                <input type="text" name="search_confirmStartTime" class="yqx-input yqx-input-icon yqx-input-small js-date" id="startDate" value="" placeholder="账单开始日期">
                <span class="fa icon-calendar icon-small"></span>
            </div>
            至
            <div class="form-item form-item-width">
                <input type="text" name="search_confirmEndTime" class="yqx-input yqx-input-icon yqx-input-small js-date" id="endDate" value="" placeholder="账单结束日期">
                <span class="fa icon-calendar icon-small"></span>
            </div>

            <div class="form-item form-item-width">
                <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-order-type"
                       value="${orderStatusName}" placeholder="业务类型"/>
                <input type="hidden" id="search_orderType" name="search_orderType"
                       value="${orderType}"/>
                <span class="fa icon-angle-down icon-small"></span>
            </div>
            <div class="form-item form-item-width">
                <input type="text" name="search_orderSnLike" class="yqx-input yqx-input-small" value="" placeholder="工单编号">
            </div>
            <div class="form-item form-item-width">
                <input type="text" id="receiver" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="服务顾问">
                <span class="fa icon-angle-down icon-small"></span>
                <input type="hidden" name="search_receiver">
            </div>
            <div class="form-item with123">
                <input type="text" name="search_companyLike" class="yqx-input yqx-input-small" value="" placeholder="客户单位">
            </div>
            <div class="form-item with123">
                <input type="text" name="search_postscript" class="yqx-input yqx-input-small" value="" placeholder="备注查询">
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-search-btn btn-margin">查询</button>
        </div>
        <!-- 查询条件 end -->

        <!--操作按钮 start-->
        <div class="list-tab-box js-list-tab btn-box">
            <div class="list-tab current-tab" data-key="0">
            待结算
            </div><div class="list-tab" data-key="1">
            已挂账
            </div><div class="list-tab" data-key="2">
            已结清
            </div><div class="list-tab" data-key="3">
            无效
            </div><div class="list-tab">
            全部
            </div>
            <div class="tools-bar fr">
                <a href="javascript:;" class="yqx-link js-order-list-card"><i class="icon-th-large icon-color"></i>卡片</a>
                <span>|</span><a href="javascript:;" class="yqx-link js-order-list-table"><i class="icon-reorder icon-color"></i>列表</a>
                <span>|</span>
                <a href="${BASE_PATH}/shop/settlement/debit/batch-list?refer=order-list" class="yqx-link icon-color collection"><i>￥</i>批量收款</a>
                <span>|</span>
                <a href="javascript:;" class="yqx-link export-excel"><i class="icon-signout icon-fontsize icon-color"></i>导出Excel</a>
            </div>
        </div>
        <!--操作按钮 end-->

        <!-- 数据列表>>卡片 start -->
        <div class="order-list-card" id="orderListCard">

        </div>
        <!-- 数据列表>>卡片 end -->

        <!-- 数据列表>>表格 start -->
        <div class="order-list-table" id="orderListTable">

        </div>
        <!-- 数据列表>>表格  end -->
    </div>
    <!-- 右侧内容区 end -->
</div>


<!-- 表格模板 start -->
<script type="text/html" id="orderListTableTpl">
    <div class="order-list-table-inner">
        <div class="table-scroll-box">
            <table class="yqx-table yqx-table-hover yqx-table-link">
                <thead>
                <tr>
                    <th class="th-center" width="5%">序号</th>
                    <th class="th-left">车牌</th>
                    <th class="th-left">车型</th>
                    <th class="th-left">车主</th>
                    <th class="th-left">工单类型</th>
                    <th class="th-left">工单状态</th>
                    <th class="th-left" style="width:82px;">开单日期</th>
                    <th class="th-right">总计</th>
                    <th class="th-right">应收金额</th>
                    <th class="th-right">实收金额</th>
                    <th class="th-right">挂账金额</th>
                    <th class="th-center" width="3%">发票</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <%var orderTagClassList = ["synthetical","carwarsh","speedily","synthetical","sale"];%>
                <%if(json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr data-order-tableid="<%=item.id%>" class="yqx-link js-inforlink">
                    <td class="td-center"><%=json.data.size*(json.data.number)+i+1%></td>
                    <td class="td-left ellipsis-1 js-show-tips"><%=item.carLicense%></td>
                    <td class="td-left"><div class="max-text js-show-tips ellipsis-1" title="<%=item.carInfo%>"><%=item.carInfo%></div></td>
                    <td class="td-left"><div class="max-text js-show-tips ellipsis-1" title="<%=item.customerName%>"><%=item.customerName%></div></td>
                    <td class="td-left"><%=item.orderTagName%></td>
                    <td class="td-left card-status
                                   <%if(item.orderStatus=='WXDD'){%>status-wx<%}%>
                                   <%if(item.orderStatus=='CJDD'){%>status-dbj<%}%>
                                   <%if(item.orderStatus=='DDBJ'){%>status-ybj<%}%>
                                   <%if(item.orderStatus=='FPDD'||item.orderStatus=='DDSG'){%>status-ypg<%}%>
                                   <%if(item.orderStatus=='DDWC'){%>status-ywg<%}%>
                                   <%if(item.orderStatus=='DDYFK'&&item.payStatus==1){%>status-ygz<%}%>
                                   <%if(item.orderStatus=='DDYFK'&&item.payStatus==2){%>status-yjq<%}%>">
                    <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                        <%=item.orderStatusName%>
                    <#else>
                        <%=item.tqmallOrderStatusName%>
                    </#if>
                    </td>
                    <td class="td-left ellipsis-1 js-show-tips"><%=item.createTimeStr%></td>
                    <td class="td-right  js-show-tips">
                        <span class="rmb-font">&yen;</span><span class="money-margin money-fontsize"><%=item.orderAmount%></span>
                    </td>
                    <td class="td-right  js-show-tips">
                        <%if(item.orderStatus=='DDYFK'){%>
                        <span class="rmb-font">&yen;</span><span class="money-margin money-fontsize"><%=item.payAmount%></span>
                        <%}%>
                    </td>
                    <td class="td-right js-show-tips money-font">
                        <%if(item.orderStatus=='DDYFK'){%>
                        <span class="rmb-font">&yen;</span><span class="money-margin"><%=item.payedAmount%></span>
                        <%}%>
                    </td>
                    <td class="td-right js-show-tips money-font">
                        <%if(item.orderStatus=='DDYFK'){%>
                        <span class="rmb-font">&yen;</span><span class="money-margin"><%=item.signAmount%></span>
                        <%}%>
                    </td>
                    <td>
                        <span><%if(item.invoiceType > 0){%><span class="invoice-type">√</span><%} else {%>-<%}%></span>
                    </td>
                    <td>
                        <%if(item.payStatus == 1 && item.proxyType != 2){%>
                        <a href="${BASE_PATH}/shop/settlement/debit/order-debit?refer=debit-list&orderId=<%=item.id%>" class="inlist-link debit">收款</a>
                        <% }else if(item.orderStatus == 'DDWC' && item.proxyType != 2 && item.orderTag != 4){%>
                        <a href="${BASE_PATH}/shop/settlement/debit/confirm-bill?orderId=<%=item.id%>" class="inlist-link confirm">确认账单</a>
                       <% }else if(item.orderStatus =='CJDD' && (item.orderTag ==3 || item.orderTag == 5)){%>
                        <a href="${BASE_PATH}/shop/settlement/debit/speedily-confirm-bill?refer=debit-list&orderId=<%=item.id%>" class="inlist-link debit">收款</a>
                        <% }else{%>
                        <a href="${BASE_PATH}/shop/settlement/debit/order-detail?orderId=<%=item.id%>" class="inlist-link link-detail">详情</a>
                        <%}%>
                    </td>

                </tr>
                <%}%>
                <%}%>
                </tbody>
            </table>
        </div>
        <%if(json.data && json.data.otherData){%>
        <%var total = json.data.otherData;%>
        <div class="search-result">
            查询结果
            <span>总计 <i class="money-font js-money-font"><%= total.orderAmount%></i></span>
            <span>应收金额：<i class="money-font js-money-font"><%= total.payAmount%></i></span>
            <span>实收金额：<i class="money-font js-money-font"><%= total.paidAmount%></i></span>
            <span>挂账金额：<i class="money-font js-money-font"><%= total.signAmount%></i></span>
        </div>
        <%}%>
        <!-- 分页容器 start -->
        <div class="yqx-page" id="orderListTablePage"></div>
        <!-- 分页容器 end -->
    </div>
</script>
<!-- 表格模板 end -->
<!-- 卡片模板 start -->
<script type="text/html" id="orderListCardTpl">
    <div class="card-box">
        <%var orderTagClassList = ["synthetical","carwarsh","speedily","synthetical","sale"];%>
        <%if(json.data && json.data.content){%>
        <%for(var i=0;i<json.data.content.length;i++){%>
        <%var item = json.data.content[i];%>

        <div class="card-item">
            <a href="${BASE_PATH}/shop/settlement/debit/order-detail?orderId=<%=item.id%>&refer=order-list">
            <div class="card-info">
                <div class="card-check-box">
                    <input type="hidden" name="id" value="<%=item.id%>"/>
                    <div>
                        <span class="card-licence"><%=item.carLicense%></span>
                        <div class="card-status
                                            <%if(item.orderStatus=='WXDD'){%>status-wx<%}%>
                                            <%if(item.orderStatus=='CJDD'){%>status-dbj<%}%>
                                            <%if(item.orderStatus=='DDBJ'){%>status-ybj<%}%>
                                            <%if(item.orderStatus=='FPDD'||item.orderStatus=='DDSG'){%>status-ypg<%}%>
                                            <%if(item.orderStatus=='DDWC'){%>status-ywg<%}%>
                                            <%if(item.orderStatus=='DDYFK'&&item.payStatus==1){%>status-ygz<%}%>
                                            <%if(item.orderStatus=='DDYFK'&&item.payStatus==2){%>status-yjq<%}%>">
                        <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                            <%=item.orderStatusName%>
                        <#else>
                            <%=item.tqmallOrderStatusName%>
                        </#if>
                        </div>
                    </div>
                    <div class="card-car-type js-show-tips ellipsis-1" title="<%=item.carInfo%>">
                        <%=item.carInfo%>
                    </div>

                </div>
                <div class="car-check-body">
                    <p class="type-parent">
                        <i>工单类型：</i><span class="card-tag"><%=item.orderTagName%></span>
                    </p>
                    <p>
                        <i>开单日期：</i><span><%=item.createTimeStr%></span>
                    </p>
                    <p>
                        <%if(item.orderStatus =='DDYFK'){%>
                        <i>总计：</i><span class="money-font-card"><i class="rmb-font">&yen;</i><em class="money-margin"><%=item.orderAmount%></em></span>
                        <%}else{%>
                        <i>&nbsp;</i>
                        <%}%>
                    </p>
                    <p>
                        <%if(item.orderStatus =='DDYFK'){%>
                        <i>应收金额：</i><span class="money-font-card"><i class="rmb-font">&yen;</i><em class="money-margin"><%=item.payAmount%></em></span>
                        <%}else{%>
                        <i>&nbsp;</i>
                        <%}%>
                    </p>
                    <p>
                        <%if(item.orderStatus=='DDYFK'){%>
                        <i>实收金额：</i><span class="money-font"><i class="rmb-font">&yen;</i><em class="money-margin"><%=item.payedAmount%></em></span>
                        <%}else{%>
                        <i>&nbsp;</i>
                        <%}%>
                    </p>
                </div>
            </div>
            <div class="card-money cleafix">
                <div class="card-price">
                    <%if(item.orderStatus=='DDYFK'){%>
                    挂账金额：<span class="money-font"><i class="rmb-font">&yen;</i><em class="money-margin"><%=item.signAmount%></em></span>
                    <%}else{%>
                    总计：<span class="money-font"><i class="rmb-font">&yen;</i><em class="money-margin"><%=item.orderAmount%></em></span>
                    <%}%>

                    <%if(item.payStatus == 1 && item.proxyType != 2){%>
                        <a href="${BASE_PATH}/shop/settlement/debit/order-debit?refer=debit-list&orderId=<%=item.id%>" class="card-btn">收款</a>
                        <% }else if(item.orderStatus == 'DDWC' && item.proxyType != 2){%>
                        <a href="${BASE_PATH}/shop/settlement/debit/confirm-bill?orderId=<%=item.id%>" class="card-confirm">确认账单</a>
                       <% }else if(item.orderStatus =='CJDD' && (item.orderTag ==3 || item.orderTag == 5)){%>
                        <a href="${BASE_PATH}/shop/settlement/debit/speedily-confirm-bill?refer=debit-list&orderId=<%=item.id%>" class="card-btn">收款</a>
                        <% }else{%>
                        <a href="${BASE_PATH}/shop/settlement/debit/order-detail?orderId=<%=item.id%>" class="card-btn">详情</a>
                        <%}%>
                </div>
            </div>
        </a>
        </div>
        <%}%>
        <%}%>
    </div>
    <%if(json.data && json.data.otherData){%>
    <%var total = json.data.otherData;%>
    <div class="search-result">
        查询结果
        <span>总计 <i class="money-font js-money-font"><%= total.orderAmount%></i></span>
        <span>应收金额：<i class="money-font js-money-font"><%= total.payAmount%></i></span>
        <span>实收金额：<i class="money-font js-money-font"><%= total.paidAmount%></i></span>
        <span>挂账金额：<i class="money-font js-money-font"><%= total.signAmount%></i></span>
    </div>
    <%}%>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="orderListCardPage"></div>
    <!-- 分页容器 end -->

</script>
<!-- 卡片模板 end -->


<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/settlement/debit/order-list.js?a7c2270965fc524e48069bb2372d1859"></script>
<!-- 脚本引入区 end -->

<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<#include "yqx/layout/footer.ftl">
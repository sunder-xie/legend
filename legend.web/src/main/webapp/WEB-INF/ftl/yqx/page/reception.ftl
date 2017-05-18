<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/reception.css?991aa254180fef81caeb158f70eebe83"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!--车牌查询 start-->
    <#--<div class="car-title">-->
        <#--<p>接车维修</p>-->
    <#--</div>-->
    <div class="license-search-content">
        <span class="Z-search-title">车辆查询</span>
        <div class="yqx-downlist-wrap">
            <i class="Z-search"></i>
            <input name="orderInfo.carLicense" placeholder="车牌/车主电话/车主" class="license"/>
        </div>
        <button class="btn-save js-save" type="button">查询</button>
    </div>
    <!--车牌查询 end-->

    <!---link start-->
    <ul class="link-box clearfix">
        <div class="appointment clearfix">
            <h3 class="appointment-customer" onclick="window.location.href= '${BASE_PATH}/shop/appoint/appoint-list'"><i></i><span>客户预约</span></h3>
            <ul>
                <li class="appointment-search" onclick="window.location.href= '${BASE_PATH}/shop/appoint/appoint-list'"><i>•</i><span>预约单查询</span></li>
                <li class="appointment-new" onclick="window.location.href= '${BASE_PATH}/shop/appoint/appoint-edit'"><i>•</i><span>新建预约单</span></li>
            </ul>
        </div>
        <div class="appointment clearfix">
            <h3 class="reception-cars" onclick="window.location.href= '${BASE_PATH}/shop/customer/list?refer=home-left'"><i></i><span>车辆接待</span></h3>
            <ul>
                <li class="reception-customer-search" onclick="window.location.href= '${BASE_PATH}/shop/customer/list?refer=home-right'"><i>•</i><span>车辆查询</span></li>
                <li class="reception-customer-new" onclick="window.location.href= '${BASE_PATH}/shop/customer/edit?refer=home-right'"><i>•</i><span>新建车辆</span></li>
                <li class="reception-new" onclick="window.location.href= '${BASE_PATH}/shop/precheck/precheck'"><i>•</i><span>新建预检单</span></li>
            </ul>
        </div>
        <div class="appointment clearfix">
            <h3 class="repair-bill" onclick="window.location.href= '${BASE_PATH}/shop/order/order-list?refer=home-left'"><i></i><span>维修开单</span></h3>
            <ul>
                <li class="repair-search" onclick="window.location.href= '${BASE_PATH}/shop/order/order-list?refer=home-right'"><i>•</i><span>工单查询</span></li>
                <#if BPSHARE == 'true'>
                    <li class="repair-proxied" onclick="window.location.href= '${BASE_PATH}/proxy/proxyList'"><i>•</i><span>受托单查询</span></li>
                    <#if SESSION_SHOP_ID == 4143>
                        <li class="repair-carWash-new" onclick="window.location.href= '${BASE_PATH}/shop/order/carwash?refer=home'"><i>•</i><span>新建洗车单</span></li>
                        <li class="repair-new" onclick="window.location.href= '${BASE_PATH}/shop/order/speedily?refer=home'"><i>•</i><span>新建快修快保单</span></li>
                    </#if>
                <#else>
                    <li class="repair-carWash-new" onclick="window.location.href= '${BASE_PATH}/shop/order/carwash?refer=home'"><i>•</i><span>新建洗车单</span></li>
                    <li class="repair-new" onclick="window.location.href= '${BASE_PATH}/shop/order/speedily?refer=home'"><i>•</i><span>新建快修快保单</span></li>
                </#if>
                <!-- 档口店不显示综合维修单 -->
                <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                    <li class="repair-comprehensive" onclick="window.location.href= '${BASE_PATH}/shop/order/common-add'"><i>•</i><span>新建综合维修单</span></li>
                </#if>
                <#if BPSHARE != 'true'>
                    <li class="repair-sales-new" onclick="window.location.href= '${BASE_PATH}/shop/order/sell-good'"><i>•</i><span>新建销售单</span></li>
                </#if>
                <#if SESSION_SHOP_JOIN_STATUS == 1 && BPSHARE != 'true'>
                    <li class="repair-proxy" onclick="window.location.href= '${BASE_PATH}/proxy/proxyList'"><i>•</i><span>委托单查询</span></li>
                </#if>
            </ul>
        </div>
        <!-- 是否使用车间 -->
        <#if SESSION_SHOP_WORKSHOP_STATUS == 1>
        <div class="appointment clearfix">
            <h3 class="work-shop" onclick="window.location.href= '${BASE_PATH}/workshop/workOrder/toWorkOrderList'"><i></i><span>车间管理</span></h3>
                <ul>
                    <li class="repair-search" onclick="window.location.href= '${BASE_PATH}/workshop/workOrder/toWorkOrderList'"><i>•</i><span>施工单查询</span></li>
                    <li class="repair-sales-new" onclick="window.open('${BASE_PATH}/workshop/loadplate/loadplate-show')"><i>•</i><span>看板查询</span></li>
                    <li class="repair-comprehensive" onclick="window.location.href= '${BASE_PATH}/workshop/workOrder/toWorkOrderBreakList'"><i>•</i><span>中断管理</span></li>
                    <li class="repair-carWash-new" onclick="window.location.href= '${BASE_PATH}/workshop/process/scanpage'"><i>•</i><span>施工作业扫描</span></li>
                </ul>
         </div>
        </#if>
    </ul>
    <!---link end-->

    <div class="yqx-board">

        <div class="yqx-board-head">车间看板</div>
    <#--表格容器-->
        <div class="yqx-group-content table-overflow-y-auto" id="tableTest">
        </div>
    <#--表格容器 end-->
    </div>

<!--表格数据模版 start-->
<script type="text/template" id="tableTestTpl">
    <table class="yqx-table" id="table">
        <thead>
        <tr>
            <th class="padding-10">车牌</th>
            <th>车型</th>
            <th>工单编号</th>
            <th>工单类型</th>
            <th>服务顾问</th>
            <th>开单日期</th>
            <th>维修工</th>
            <th>工单状态</th>
        </tr>
        </thead>
        <tbody>
        <%if(data){%>
        <%for(var i=0;i< data.length;i++){%>
        <% var item=data[i];%>
        <tr data-order-id="<%=item.id%>">
            <td class="padding-10"><%=item.carLicense%></td>
            <td class="js-show-tips"><%=item.carInfo%></td>
            <td><%=item.orderSn%></td>
            <td><%=item.orderTagName%></td>
            <td><%=item.receiverName%></td>
            <td><%=item.createTimeStr%></td>
            <td class="js-show-tips"><%=item.workerNames%></td>
            <td class="state-color">
            <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                <%=item.orderStatusName%>
            <#else>
                <%=item.tqmallOrderStatusName%>
            </#if></td>
        </tr>
        <%}}%>
        </tbody>
    </table>
</script>
<!--表格数据模版 end-->
<#--车牌模板 start-->
<script type="text/html" id="carLicenceTpl">
    <%if(templateData && templateData.length > 0){%>
    <ul class="yqx-downlist-content js-downlist-content" data-tpl-ref="car-licence-tpl">
        <%for(var i=0;i<templateData.length;i++){%>
        <%var item=templateData[i];%>
        <li class="js-downlist-item">
            <a href="${BASE_PATH}/shop/customer/car-detail?refer=reception&id=<%= item.id%>"><span title="<%=item.id%>" style="width:100%"><%=item.searchValue%></span></a>
        </li>
        <%}%>
    </ul>
    <%}else{%>
    <div class="downlist-new-license yqx-dl-no-data"><a href="javascript:;">新建车牌</a></div>
    <%}%>
</script>
<#--车牌模板 end-->
</div>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/reception.js?5392bca105db0910d08e5ab5b5f7d793"></script>
<script  src="${BASE_PATH}/static/third-plugin/freezeheader/freezeheader.js"></script>

<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
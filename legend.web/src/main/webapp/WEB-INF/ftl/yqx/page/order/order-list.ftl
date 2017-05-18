<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/card.css?b7e57f9bc402289671f029f941d2ffe3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/order-list.css?e8f8dea45141da137c38adaec2f6f07e"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <!-- 标题 start -->
        <div class="order-list-title clearfix">
            <h3 class="headline fl">工单查询</h3>

            <div class="tools-btn fr">
                <#if BPSHARE != 'true'>
                <a href="${BASE_PATH}/shop/order/carwash?refer=order-list" class="yqx-btn btn-1"><span
                        class="fa icon-plus"></span>洗车单</a>
                <a href="${BASE_PATH}/shop/order/speedily?refer=order-list" class="yqx-btn btn-2"><span
                        class="fa icon-plus"></span>快修快保单</a>
                </#if>
                <!-- 档口店不显示综合维修单 -->
                <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                <a href="${BASE_PATH}/shop/order/common-add?refer=order-list" class="yqx-btn btn-3"><span
                        class="fa icon-plus"></span>综合维修单</a>
                </#if>
                <#if BPSHARE != 'true'>
                <a href="${BASE_PATH}/shop/order/sell-good?refer=order-list" class="yqx-btn btn-4"><span
                        class="fa icon-plus"></span>销售单</a>
                </#if>
            </div>
        </div>
        <!-- 标题 end -->
        <!-- 查询条件 start -->
        <div class="condition-box" id="orderListForm">
            <div class="condition-input">
                <div class="senior-box">
                    <div class="form-item senior-width">
                        <input type="text" name="search_keyword" class="yqx-input yqx-input-small" value="" placeholder="车牌/车主电话/车主">
                    </div><div class="form-item senior-width">
                        <input type="text" name="search_orderSnLike" class="yqx-input yqx-input-small" value="" placeholder="工单编号">
                    </div><div class="form-item senior-width">
                        <input type="text" name="search_carLike" class="yqx-input yqx-input-small" value="" placeholder="车型">
                    </div><div class="form-item senior-width">
                        <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-order-tag" value="${orderTypeName}" placeholder="工单类型"/>
                        <input type="hidden" id="search_orderTag" name="search_orderTag" value="${orderType}"/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                </div>
                <div class="senior-box">
                    <div class="form-item senior-width">
                        <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-order-type"
                               value="" placeholder="业务类型"/>
                        <input type="hidden" id="search_orderType" name="search_orderType"
                               value="${orderStatus}"/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div><div class="form-item senior-width">
                        <input type="text" name="search_startTime" class="yqx-input yqx-input-small yqx-input-icon" id="start1" placeholder="开单开始日期">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>至<div class="form-item senior-width">
                        <input type="text" name="search_endTime" class="yqx-input yqx-input-small yqx-input-icon" id="end1" placeholder="开单结束日期">
                        <span class="fa icon-small icon-calendar"></span>
                    </div><div class="form-item senior-width">
                        <input type="text" name="search_postscript" class="yqx-input yqx-input-small" placeholder="备注查询">
                    </div>
                </div>
                <#if SESSION_SHOP_JOIN_STATUS == 1 || BPSHARE == 'true'>
                <div class="senior-box">
                    <#if BPSHARE == 'true'>
                    <div class="form-item senior-width">
                        <input type="text" name="search_channelNameLike" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="渠道"/>
                    </div>
                    </#if>
                    <#if SESSION_SHOP_JOIN_STATUS == 1>
                    <div class="form-item senior-width">
                        <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-proxy-type" value="" placeholder="是否委托"/>
                        <input type="hidden" name="search_proxyType" value=""/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    </#if>
                </div>
                </#if>
                <div class="search-btns">
                    <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</a>
                </div>
            </div>
        </div>
        <div class="list-tab-box js-list-tab">
            <!-- 档口店不显示综合维修单 -->
            <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
            <div class="list-tab" data-key="DBJ">待报价</div><div class="list-tab" data-key="YBJ">已报价
            </div><div class="list-tab" data-key="YPG">已派工
            </div><div class="list-tab" data-key="YWG">已完工
            </div><div class="list-tab" data-key="YGZ">已挂账
            </div><div class="list-tab" data-key="DDYFK">已结清
            </div><div class="list-tab current-tab" data-key="">全部
            </div>
            <#else>
            <div class="list-tab" data-key="DBJ">待结算
            </div><div class="list-tab" data-key="YGZ">已挂账
            </div><div class="list-tab" data-key="DDYFK">已结清
            </div><div class="list-tab current-tab" data-key="">全部
            </div>
            <input type="hidden" class="js-tqmall-version" value="${SESSION_SHOP_IS_TQMALL_VERSION}">
            </#if>
            <div class="tools-bar clearfix select-btns-right">
                <a href="javascript:;" class="yqx-link js-order-list-card"><i class="icon-th-large"></i>卡片</a>
                <span>|</span>
                <a href="javascript:;" class="yqx-link js-order-list-table"><i class="icon-reorder"></i>列表</a>
                <span>|</span>
                <a href="javascript:;" class="yqx-link export-excel display-none"><i class="icon-signout"></i>导出Excel</a>
            </div>
        </div>
        <!-- 查询条件 end -->

        <!-- 数据列表>>表格 start -->
        <div class="order-list-table" id="orderListTable">

        </div>
        <!-- 数据列表>>表格  end -->

        <!-- 数据列表>>卡片 start -->
        <div class="order-list-card" id="orderListCard">

        </div>
        <!-- 数据列表>>卡片 end -->
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
                    <th class="td-center">序号</th>
                    <th>车牌</th>
                    <th>车型</th>
                    <th>车主</th>
                    <th>车主电话</th>
                    <th>工单类型</th>
                    <th>业务类型</th>
                    <th>工单状态</th>
                    <th>服务顾问</th>
                    <th>开单日期</th>
                    <th class="th-right">总计</th>
                    <th class="th-right">应收金额</th>
                    <th class="th-right padding-10">挂账金额</th>
                    <#if SESSION_SHOP_JOIN_STATUS == 1>
                        <th>是否委托</th>
                    </#if>
                </tr>
                </thead>
                <tbody>
                <%if(json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr  data-order-tableid="<%=item.id%>" class="yqx-link js-inforlink">
                    <td class="td-center"><%=json.data.size*(json.data.number)+i+1%></td>
                    <td class="js-show-tips"><%=item.carLicense%></td>
                    <td><div class="max-text js-show-tips ellipsis-1" title="<%=item.carInfo%>"><%=item.carInfo%></div></td>
                    <td><div class="max-text js-show-tips ellipsis-1" title="<%=item.customerName%>"><%=item.customerName%></div></td>
                    <td><%=item.customerMobile%></td>
                    <td><%=item.orderTagName%></td>
                    <td><%=item.orderTypeName%></td>
                    <td class="card-status
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
                    <td><%=item.receiverName%></td>
                    <td><%=item.createTimeStr%></td>
                    <td class="td-right card-price-1 js-show-tips">￥<%=item.orderAmount%></td>
                    <td class="td-right card-price-1 js-show-tips"><% if(item.orderStatus=='DDYFK'){%>￥<%=item.payAmount%><%}%></td>
                    <td class="td-right padding-10 card-price-1 js-show-tips"><% if(item.orderStatus=='DDYFK'){%>￥<%=item.signAmount%><%}%></td>
                <#if SESSION_SHOP_JOIN_STATUS == 1>
                    <td><%if(item.proxyType == 1){%>是<%}else{%>否<%}%></td>
                </#if>
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
            <span>总计：<i class="money-font js-money-font"><%= total.orderAmount%></i></span>
            <span>应收金额：<i class="money-font js-money-font"><%= total.payAmount%></i></span>
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
            <a href="${BASE_PATH}/shop/order/detail?orderId=<%=item.id%>&refer=order-list">
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
                            <i>服务顾问：</i><span><%=item.receiverName%></span>
                        </p>
                        <p class="card-time">
                            <i>开单日期：</i><%=item.createTimeStr%>
                        </p>
                        <p class="card-info ellipsis-1 js-show-tips">
                            <i>　　备注：</i><span>  <%if(item.postscript){%><%=item.postscript%> <% }else{%>无<%}%></span>
                        </p>
                    </div>
                </div>
                <div class="card-money cleafix">
                    <div class="card-price">
                        <%if(item.payStatus == 1){%>
                        挂帐：<span class="card-price-1">￥<%=item.signAmount%></span>
                        <%}else{%>
                        总计：<span class="card-price-1">￥<%=item.orderAmount%></span>
                        <%}%>
                        <a href="${BASE_PATH}/shop/order/detail?orderId=<%=item.id%>&refer=order-list" class="yqx-input yqx-input-3 card-btn">详情</a>
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
        <span>总计：<i class="money-font js-money-font"><%= total.orderAmount%></i></span>
        <span>应收金额：<i class="money-font js-money-font"><%= total.payAmount%></i></span>
        <span>挂账金额：<i class="money-font js-money-font"><%= total.signAmount%></i></span>
    </div>
    <%}%>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="orderListCardPage"></div>
    <!-- 分页容器 end -->

</script>
<!-- 卡片模板 end -->
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/order/order-list.js?e8079a2f4d567fbb9492b2021dd711ee"></script>
<!-- 脚本引入区 end -->

<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<#include "yqx/layout/footer.ftl">
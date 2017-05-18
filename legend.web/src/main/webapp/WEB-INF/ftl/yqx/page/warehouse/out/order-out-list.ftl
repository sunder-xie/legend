<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/stock/stock-common.css?2a6120c599f20dbc8877c532837dd82f"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/out/order-out-list.css?590f6f62d267a59e35e567756bd641dc"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <!-- 标题 start -->
        <div class="order-head">
            <h3 class="headline">工单出库</h3>
        </div>
        <!-- 标题 end -->

        <!-- 查询条件 start -->
        <div class="condition-box" id="listForm">
            <input type="hidden" name="search_hasOut"/>
            <div class="condition-input">
                <div class="show-grid">
                    <div class="form-item">
                        <input type="text" name="search_startTime" class="yqx-input yqx-input-small yqx-input-icon"
                               id="start1" placeholder="开单开始日期">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                    至
                    <div class="form-item">
                        <input type="text" name="search_endTime" class="yqx-input yqx-input-small yqx-input-icon"
                               id="end1" placeholder="开单结束日期">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>

                    <div class="form-item">
                        <input type="text" id="receiver" class="yqx-input yqx-input-icon yqx-input-small" value=""
                               placeholder="服务顾问">
                        <span class="fa icon-angle-down icon-small"></span>
                        <input type="hidden" name="search_receiver">
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="form-item col-11">
                    <input type="text" name="search_keyword"
                           class="yqx-input yqx-input-icon yqx-input-small"
                           value=""
                           placeholder="车牌/工单编号/车主电话"/>
                </div>
                <div class="form-item col-1">
                    <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn fr">查询</a>
                </div>
            </div>
        </div>
        <!-- 查询条件 end -->

        <div class="list-tab-box js-list-tab">
            <div class="list-tab" data-key="0">未领料工单</div><div class="list-tab" data-key="1">已领料工单</div><div class="list-tab current-tab" data-key="">全部</div>
        </div>

        <!-- 数据列表>>表格 start -->
        <div class="order-list-table" id="tableCon">

        </div>
        <!-- 数据列表>>表格  end -->
        <!-- 分页容器 start -->
        <div class="yqx-page" id="pagingCon">

        </div>
        <!-- 分页容器 end -->
    </div>
    <!-- 右侧内容区 end -->
</div>

<!-- 表格模板 start -->
<script type="text/html" id="tableTpl">
    <div class="order-list-table-inner">
        <div class="table-scroll-box">
            <table class="yqx-table yqx-table-border yqx-table-hidden yqx-table-hover yqx-table-link yqx-table-reverse">
                <thead>
                <tr>
                    <th class="text-c">序号</th>
                    <th class="text-l tc-datetime">开单日期</th>
                    <th class="text-l tc-3">工单编号</th>
                    <th class="text-l tc-4">车牌</th>
                    <th class="text-l tc-5">车型</th>
                    <th class="text-l tc-6">业务类型</th>
                    <th class="text-l tc-7">服务顾问</th>
                    <th class="text-l tc-8">工单状态</th>
                    <th class="text-c">操作</th>
                </tr>
                </thead>
                <tbody>
                <%if(json.data && json.data.content){%>
                <%for(var i = 0; i < json.data.content.length; i++) {%>
                    <%var item = json.data.content[i];%>
                    <tr data-itemid="<%=item.id%>" data-goodsoutflag="<%=item.goodsOutFlag%>"
                        class="detail-list">
                        <td class="text-c"> <%=json.data.size*(json.data.number)+i+1%></td>
                        <td class="text-l"><%=item.createTimeStr%></td>
                        <td class="text-l">
                            <div class="ellipsis-1 js-show-tips">
                                <%=item.orderSn%>
                            </div>
                        </td>
                        <td class="text-l">
                            <%=item.carLicense%>
                        </td>
                        <td class="text-l">
                            <div class="ellipsis-1 js-show-tips">
                                <%=item.carInfo%>
                            </div>
                        </td>
                        <td class="text-l"><%=item.orderTypeName%></td>
                        <td class="text-l">
                            <div class="ellipsis-1 js-show-tips">
                                <%=item.receiverName%>
                            </div>
                        </td>
                        <td class="text-l card-status
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
                        <td class="text-c">
                            <%if (item.goodsOutFlag == 0) {%>
                            <a href="<%=BASE_PATH%>/legend/shop/warehouse/out/order-quote-detail?orderId=<%=item.id%>&refer=outbound"
                               class="yqx-link yqx-link-2">
                                添加物料
                            </a>
                            <%} else if (item.goodsOutFlag == 1) {%>
                            <a href="<%=BASE_PATH%>/legend/shop/warehouse/out/order-out-detail?orderId=<%=item.id%>"
                               class="yqx-link yqx-link-1">
                                领料出库
                            </a>
                            <%}else if(item.goodsOutFlag==3){%>
                            <span class="text-grey">
                                <B>已全出库</B>
                            </span>
                            <%}else if(item.goodsOutFlag==2){%>
                            <a href="<%=BASE_PATH%>/legend/shop/warehouse/out/order-out-detail?orderId=<%=item.id%>"
                               class="yqx-link yqx-link-3">
                                继续出库
                            </a>
                            <%}%>
                        </td>
                    </tr>
                    <%}%>
                    <%}%>
                </tbody>
            </table>
        </div>
</script>
<!-- 表格模板 end -->
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/warehouse/out/order-out-list.js?ce3a3a85719a5545cc1e3b70b6dc83e3"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
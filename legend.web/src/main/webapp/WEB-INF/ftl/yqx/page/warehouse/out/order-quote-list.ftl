<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/stock/stock-common.css?2a6120c599f20dbc8877c532837dd82f"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/out/order-quote-list.css?7aa6633288196ed2781e81f93bfb6ac9"/>
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
            <h3 class="headline">工单报价</h3>
        </div>
        <!-- 标题 end -->
        <!-- 查询条件 start -->
        <div class="condition-box" id="listForm">
            <!--只筛选 综合维修单-->
            <input type="hidden" name="search_orderStatuss" value="DBJANDYBJ"/>
            <input type="hidden" name="search_orderTag" value="1">
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
                <div class="show-grid">
                    <div class="form-item col-11">
                        <input type="text" name="search_keyword"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value=""
                               placeholder="车牌/工单编号/车主电话"/>
                    </div>
                    <div class="form-item col-1 clearfix">
                        <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn fr">查询</a>
                    </div>
                </div>
            </div>
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
                    <th class="text-l tc-6">服务顾问</th>
                    <th class="text-l tc-7">工单状态</th>
                    <th class="text-c">操作</th>
                </tr>
                </thead>
                <tbody>
                <%if(json.data && json.data.content){%>
                <%for(var i=0;i < json.data.content.length ;i++){%>
                    <%var item = json.data.content[i];%>
                    <tr data-itemid="<%=item.id%>" data-orderstatus="<%=item.orderStatus%>"
                        class="detail-list">
                        <td class="text-c"><%=json.data.size*(json.data.number)+i+1%></td>
                        <td class="text-l"><%=item.createTimeStr%></td>
                        <td class="text-l">
                            <%=item.orderSn%>
                        </td>
                        <td class="text-l">
                            <div class="ellipsis-1 js-show-tips"><%=item.carLicense%></div>
                        </td>
                        <td class="text-l">
                            <div class="ellipsis-1 js-show-tips">
                                <%=item.carInfo%>
                            </div>
                        </td>
                        <td class="text-l">
                            <div class="ellipsis-1 js-show-tips">
                                <%=item.receiverName%>
                            </div>
                        </td>
                        <td class="text-l card-status
                                   <%if(item.orderStatus=='CJDD'){%>status-dbj color-red<%}%>
                                   <%if(item.orderStatus=='DDBJ'){%>status-ybj color-green<%}%>">
                        <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                            <%=item.orderStatusName%>
                        <#else>
                            <%=item.tqmallOrderStatusName%>
                        </#if>
                        </td>
                        <td class="text-c">
                            <%if(item.orderStatus=='CJDD'){%>
                            <a href="javascript:;" class="blue js-quote">报价</a>
                            <%}%>
                            <%if(item.orderStatus=='DDBJ'){%>
                            <a href="javascript:;" class="green js-quote">编辑</a>
                            <%}%>
                        </td>
                    </tr>
                    <%}%>
                    <%}%>
                </tbody>
            </table>
        </div>
    </div>
</script>
<!-- 表格模板 end -->
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/warehouse/out/order-quote-list.js?e0f5e11bc9bcc67c6c42ee206032b483"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
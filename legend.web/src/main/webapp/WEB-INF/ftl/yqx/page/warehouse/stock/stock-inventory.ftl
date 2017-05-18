<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/stock/stock-common.css?2a6120c599f20dbc8877c532837dd82f"/>
<link rel="stylesheet"
      href="${BASE_PATH}/static/css/page/warehouse/stock/stock-inventory.css?fb5ba5a13d59113433c43938a344c231"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fr">
        <!-- 标题 start -->
        <div class="order-head clearfix">
            <h3 class="headline fl">库存盘点</h3>
        </div>
        <!-- 标题 end -->
        <!-- 查询条件 start -->
        <div class="form-box" id="listForm">
            <input type="hidden" name="search_status"/>
            <div class="form-item">
                <input class="yqx-input yqx-input-small"
                       placeholder="请输入配件名称"
                       name="search_goodsNameLike">
            </div>
            <div class="form-item">
                <input class="yqx-input yqx-input-small"
                       placeholder="请输入盘点单号"
                       name="search_recordSnLike">
            </div>
            <div class="form-item">
                <input type="text"
                       class="yqx-input yqx-input-icon yqx-input-small"
                       placeholder="盘点开始日期"
                       name="search_startTime"
                       id="startDate">
                <i class="fa icon-calendar icon-small"></i>
            </div>
            至
            <div class="form-item">
                <input type="text"
                       class="yqx-input yqx-input-icon yqx-input-small"
                       placeholder="盘点结束日期"
                       name="search_endTime"
                       id="endDate">
                <i class="fa icon-calendar icon-small"></i>
            </div>

            <div class="search-btns fr">
                <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</a>
            </div>
        </div>
        <div class="list-tab-box clearfix js-list-tab">
            <div class="list-tab " data-status="1">已保存</div><div
                class="list-tab" data-status="2">已盘点</div><div
                class="list-tab current-tab" data-status="">全部</div>
            <a class="yqx-link text-minor new-menu fr" href="${BASE_PATH}/shop/warehouse/stock/stock-inventory-add"><i>＋</i>盘点单</a>
        </div>
        <!-- 查询条件 end -->

        <!-- 表格容器 start -->
        <div id="tableCon" class="table-con">

        </div>
        <!-- 表格容器 end -->

        <!-- 分页容器 start -->
        <div class="yqx-page" id="pagingCon">

        </div>
        <!-- 分页容器 end -->

    </div>
    <!-- 右侧内容区 end -->
</div>
<!-- 表格模板 start -->
<script type="text/template" id="tableTpl">
    <table class="yqx-table yqx-table-hidden yqx-table-big yqx-table-border yqx-table-hover yqx-table-link yqx-table-reverse">
        <thead>
        <tr>
            <th class="text-l tc-1">盘点单号</th>
            <th class="text-l tc-date">盘点日期</th>
            <th class="text-l tc-3">配件数</th>
            <th class="text-r tc-4">盘亏数量</th>
            <th class="text-r tc-price">盘亏金额</th>
            <th class="text-r tc-6">盘盈数量</th>
            <th class="text-r tc-price">盘盈金额</th>
            <th class="text-l tc-8">盘点人</th>
            <th class="text-l tc-9">状态</th>
            <th class="text-c tc-10">操作</th>
        </tr>
        </thead>
        <%for (var i = 0; i < json.data.content.length; i++) {%>
            <%var item = json.data.content[i];%>
            <tr id="TR_<%=item.id%>" data-item-id="<%=item.id%>" class="detail-list">
                <input type="hidden" name="itemId" value="<%=item.id%>"/>
                <td class="text-l">
                    <div class="ellipsis-1 js-show-tips"><%=item.recordSn%></div>
                </td>
                <td class="text-l"><%=item.gmtModifiedStr%></td>
                <td class="text-c"><%=item.goodsCount%></td>
                <td class="text-c" name="kuiTotal"></td>
                <td class="money-highlight text-bold text-r" name="kuiTotalAmount"></td>
                <td class="text-c" name="yinTotal"></td>
                <td class="money-highlight text-bold text-r" name="yinTotalAmount"></td>
                <td class="text-l">
                    <div class="ellipsis-1 js-show-tips"><%=item.inventoryCheckerName%></div>
                </td>
                <td class="text-l"><%if(item.status==1){%>草稿<%}%><%if(item.status==2){%>已盘点<%}%></td>
                <td class="text-r">
                    <%if(item.status==1){%>
                    <a href="javascript:;" class="yqx-link yqx-link-1 js-edit">编辑</a>
                    <a href="javascript:;" class="yqx-link yqx-link-2 js-record_del" record_id="<%=item.id%>">删除</a>
                    <%}%>

                    <%if(item.status==2){%>
                    <a href="javascript:;" class="yqx-link yqx-link-1 js-see">查看</a>
                    <%}%>
                </td>
            </tr>
            <%}%>
    </table>
</script>
<!-- 表格模板 end -->

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/warehouse/stock/stock-inventory.js?6552e5828c039785a7832e7b3f667760"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
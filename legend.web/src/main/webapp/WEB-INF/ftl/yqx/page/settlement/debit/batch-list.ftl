<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/debit/batch-list.css?ce6fbfc7b9f89e6a083a6a412b4b50db"/>
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
            <h3 class="headline fl">工单收款-
                <small>批量收款</small>
            </h3>
        </div>
        <!--查询条件 start-->
        <div class="search-box clearfix" id="searchForm">
            <div class="show-grid">
                <div class="form-item yqx-downlist-wrap from-width">
                    <input type="text" name="search_carLicenseLike" class="yqx-input yqx-input-small" placeholder="车牌">
                </div>
                <div class="form-item">
                    <input type="text" name="search_companyLike" class="yqx-input yqx-input-small company-width" value="" placeholder="客户单位">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item">
                    <input type="text" id="startTime" name="search_startTime" class="yqx-input yqx-input-icon yqx-input-small from-width" value="" placeholder="开单开始日期">
                    <span class="fa icon-calendar icon-small"></span>
                </div>
                至
                <div class="form-item">
                    <input type="text" id="endTime" name="search_endTime" class="yqx-input yqx-input-icon yqx-input-small from-width" value="" placeholder="开单结束日期">
                    <span class="fa icon-calendar icon-small"></span>
                </div>
                <input type="hidden" name="search_orderStatuss" value="DDYFK" />
                <input type="hidden" name="search_payStatusList" value="1" />

                <a href="javascript:;" class="condition-btn js-cdt-btn pick_all_btn condition-active" key="all">全部</a>
                <a href="javascript:;" class="condition-btn js-cdt-btn pick_this_week_btn" key="this_week">本周</a>
                <a href="javascript:;" class="condition-btn js-cdt-btn pick_last_week_btn" key="last_week">上周</a>
                <a href="javascript:;" class="condition-btn js-cdt-btn pick_this_month_btn" key="this_month">本月</a>
                <a href="javascript:;" class="condition-btn js-cdt-btn pick_last_month_btn" key="last_month">上月</a>

                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
            </div>

        </div>
        <!--查询条件 end-->

        <!--操作按钮 start-->
        <div class="btn-box">
            <button class="yqx-btn yqx-btn-2 yqx-btn-small js-batch-pay">批量收款</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-pay-all">全部收款</button>
        </div>
        <!--操作按钮 end-->

        <!-- 列表 start-->
        <div class="tableCon" id="tableCon">

        </div>
        <!-- 列表 end-->

        <!-- 分页容器 start -->
        <div class="yqx-page" id="tablePage"></div>
        <!-- 分页容器 end -->
    </div>
    <!-- 右侧内容区 end -->
</div>

<!-- 列表模板 start -->
<script type="text/html" id="tableTpl">
    <div class="order-list-table-inner">
        <div class="table-scroll-box">
            <table class="yqx-table yqx-table-hover yqx-table-link appoint-table">
                <thead>
                <tr>
                    <th><input type="checkbox" class="js-select-all"></th>
                    <th>序号</th>
                    <th>工单编号</th>
                    <th>车牌</th>
                    <th>类型</th>
                    <th>状态</th>
                    <th>开单日期</th>
                    <th>客户单位</th>
                    <th>总计</th>
                    <th>应收金额</th>
                    <th>实收金额</th>
                    <th>挂账金额</th>
                </tr>
                </thead>
                <tbody>
                <% for(var i=0;i<json.data.content.length;i++){ %>
                <% var item = json.data.content[i]; %>
                <tr data-order-id="<%=item.id%>" class="js-detail">
                    <td><input name="checkId" class="js-select" type="checkbox" value="<%=item.id%>"></td>
                    <td><%=json.data.size*(json.data.number)+i+1%></td>
                    <td><%=item.orderSn%></td>
                    <td><%=item.carLicense%></td>
                    <td><%=item.orderTagName%></td>
                    <td><%=item.orderStatusName%></td>
                    <td><%=item.createTimeStr%></td>
                    <td>
                        <div class="td-company-width js-show-tips"><%=item.company%></div>
                    </td>
                    <td class="money-font">&yen;<%=item.orderAmount%></td>
                    <td class="money-font">&yen;<%=item.payAmount%></td>
                    <td class="money-font">&yen;<%=item.payedAmount%></td>
                    <td class="money-font">&yen;<%=item.signAmount%></td>
                </tr>
                <%}%>
                </tbody>
            </table>
        </div>
    </div>
</script>
<!-- 列表模板 end -->


<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/settlement/debit/batch-list.js?3b11625e89f37206587183f4bb9dcb6f"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
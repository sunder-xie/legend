<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/pay/pay-flow.css?8cc6961335663c1030a7ec2ea3339d48"/>
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
            <h3 class="headline fl">付款流水记录</h3>
            <div class="tag-box fr">
                <!--  <div class="tag">
                    <div class="ico ico2"></div>
                    <div class="tagcon">
                        <p class="tag-title">今日付款</p>
                        <p class="tag-money">${dayTotal}</p>
                    </div>
                </div>-->
            </div>
        </div>

        <!--查询条件 start-->
        <div class="search-box clearfix" id="searchForm">
            <div class="form-item text-width text-conditionLike">
                <input type="text" name="search_conditionLike" class="yqx-input yqx-input-small" value="" placeholder="款项名称/收银人员/收款方">
            </div>
            <div class="form-item text-width">
                <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-pay-type" value="" placeholder="付款类型">
                <input type="hidden" name="search_payTypeId" value=""/>
                <span class="fa icon-angle-down icon-small"></span>
            </div>
            <div class="form-item text-width">
                <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-payment" value="" placeholder="支付方式">
                <input type="hidden" name="search_paymentId"/>
                <span class="fa icon-angle-down icon-small"></span>
            </div>
            <div class="form-item text-width">
                <input type="text" id="startTime" name="search_startTime" class="yqx-input yqx-input-icon yqx-input-small" value="${startTime}"  placeholder="付款开始日期">
                <span class="fa icon-calendar icon-small"></span>
            </div>
            至
            <div class="form-item text-width">
                <input type="text" id="endTime"  name="search_endTime" class="yqx-input yqx-input-icon yqx-input-small"  value="${endTime}" placeholder="付款结束日期">
                <span class="fa icon-calendar icon-small"></span>
            </div>
            <div class="show-grid">
                <a href="javascript:;" class="condition-btn js-cdt-btn pick_all_btn condition-active" key="all">全部付款流水</a>
                <a href="javascript:;" class="condition-btn js-cdt-btn pick_this_week_btn" key="this_week">本周付款流水</a>
                <a href="javascript:;" class="condition-btn js-cdt-btn pick_last_week_btn" key="last_week">上周付款流水</a>
                <a href="javascript:;" class="condition-btn js-cdt-btn pick_this_month_btn" key="this_month">本月付款流水</a>
                <a href="javascript:;" class="condition-btn js-cdt-btn pick_last_month_btn" key="last_month">上月付款流水</a>

                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
            </div>

        </div>
        <!--查询条件 end-->

        <!--操作按钮 start-->
        <div class="btn-box">
            <a href="javascript:;" class="fr js-export"><i class="icon-signout "></i>导出Excel</a>
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
        <class="table-scroll-box">
            <table class="yqx-table yqx-table-hover yqx-table-link appoint-table">
                <thead>
                <tr>
                    <th>序号</th>
                    <th class="text-l">业务类型</th>
                    <th class="text-l">款项名称</th>
                    <th class="text-l">支付方式</th>
                    <th class="text-l">收银人员</th>
                    <th class="text-l">收款方</th>
                    <th class="text-r">付款金额</th>
                    <th class="text-l text-margin">付款日期</th>
                    <th class="text-l">备注</th>
                </tr>
                </thead>
                <tbody>
                <%if(json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr data-id="<%=item.billId%>" class="yqx-link  js-inforlink">
                    <td><%=json.data.size*(json.data.number-1)+i+1%></td>
                    <td class="text-l"><%=item.typeName%></td>
                    <td class="text-l">
                        <div class="overflow-width js-show-tips"><%=item.billName%></div>
                    </td>
                    <td class="text-l"><%=item.paymentName%></td>
                    <td class="text-l"><%=item.creatorName%></td>
                    <td class="text-l">
                        <div class="overflow-width js-show-tips"><%=item.payeeName%></div></td>
                    <td class="money-font text-r">&yen;<%=item.payAmount%></td>
                    <td class="text-l text-margin"><%=item.flowTime%></td>
                    <td class="text-l">
                        <div class="overflow-width js-show-tips"><%=item.remark%></div>
                    </td>
                </tr>

                <%}%>
                <%}%>
                </tbody>
            </table>
        </div>
    </div>
</script>
<!-- 列表模板 end -->

<#include "yqx/tpl/common/export-confirm-tpl.ftl">

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/settlement/pay/pay-flow.js?ced9082f54f419678ad012019cccb278"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
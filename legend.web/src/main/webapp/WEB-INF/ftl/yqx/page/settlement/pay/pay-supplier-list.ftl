<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/pay/pay-supplier-list.css?7fdd1eeb86addceec66815282d315f47"/>
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
            <h3 class="headline fl">供应商付款－
                <small>${supplierSettlementVO.supplierName}付款明细账单</small>
            </h3>
        </div>
        <!--查询条件 start-->
        <div class="search-box clearfix" id="searchForm">
            <a href="javascript:;" class="condition-btn js-cdt-btn condition-active js-search-btn " id="search_all">全部</a>
            <a href="javascript:;" class="condition-btn js-cdt-btn js-search-btn" id="search_part">未结清入库单</a>
            <input type="hidden" name="search_paymentStatus" value="">
            <input type="hidden" name="search_supplierId" value="${supplierSettlementVO.supplierId}">
            <input type="hidden" name="search_startInTime" value="${startInTime}"/>
            <input type="hidden" name="search_endInTime" value="${endInTime}"/>
        </div>
        <!--查询条件 end-->

        <!--操作按钮 start-->
        <div class="btn-box">
            <button class="yqx-btn yqx-btn-2 yqx-btn-small js-batch-pay">批量付款</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-all-pay">全部付款</button>
        </div>
        <!--操作按钮 end-->

        <!-- 列表 start-->
        <div class="tableCon" id="tableCon">

        </div>
        <!-- 列表 end-->
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
                    <th class="text-l"><input type="checkbox" class="js-select-all">序号</th>
                    <th>入库单号</th>
                    <th>入库日期</th>
                    <th >配件总价</th>
                    <th >税费</th>
                    <th >运费</th>
                    <th>应付金额</th>
                    <th>实付金额</th>
                    <th>未付金额</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <%if(json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr data-id="<%=item.id%>" class="yqx-link ">
                    <td class="text-l"><input type="checkbox"  value="<%=item.id%>" class="js-select"><%=json.data.size*(json.data.number-1)+i+1%></td>
                <#assign flag = 0>
                <#if SESSION_USER_IS_ADMIN == 1>
                    <#assign flag= 1>
                <#else>
                    <#if SESSION_USER_ROLE_FUNC?exists>
                        <#list SESSION_USER_ROLE_FUNC as temp>
                            <#if temp.name =="仓库首页">
                                <#assign flag= 1>
                                <#break>
                            </#if>
                        </#list>
                    </#if>
                </#if>
                    <td>
                    <#if flag == 1>
                        <a class="inlist-link js-inforlink" href="javascript:;" data-id="<%=item.id%>"><%=item.warehouseInSn%></a>
                    <#else>
                        <%=item.warehouseInSn%>
                    </#if>
                    </td>
                    <td ><%=item.inTimeStr%></td>
                    <td class="money-font">&yen;<%=item.goodsAmount%></td>
                    <td class="money-font">&yen;<%=item.tax%></td>
                    <td class="money-font">&yen;<%=item.freight%></td>
                    <td class="money-font">&yen;<%=item.totalAmount%></td>
                    <td class="money-font">&yen;<%=item.amountPaid%></td>
                    <td class="money-font">&yen;<%=item.amountPayable%></td>
                    <% if(item.amountPayable !=0){%>
                    <td><a  class="js-pay" href="javascript:;" data-id="<%=item.id%>">付款</a></td>
                    <%}else {%>
                    <td><a  class="inlist-link js-inforlink" href="javascript:;" data-id="<%=item.id%>">详情</a></td>
                    <%}%>
                </tr>

                <%}%>
                <%}%>
                </tbody>
            </table>
        </div>
    </div>
    <%if(json.data && json.data.otherData){%>
    <%var total = json.data.otherData;%>
    <div class="search-result">
        查询结果
        <span>配件总价：<i class="money-font js-money-font"><%= total.goodsAmountStatistics%></i></span>
        <span>应付金额：<i class="money-font js-money-font"><%= total.totalAmountStatistics%></i></span>
        <span>实付金额：<i class="money-font js-money-font"><%= total.amountPaidStatistics%></i></span>
        <span>未付金额：<i class="money-font js-money-font"><%= total.amountPayableStatistics%></i></span>
    </div>
    <%}%>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="tablePage"></div>
    <!-- 分页容器 end -->
</script>
<!-- 列表模板 end -->


<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/settlement/pay/pay-supplier-list.js?17996af54d0c637ca6fadcd387bc6914"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/pay/pay-supplier.css?f8aacbda7d1cd00f62465cb4aaefeef5"/>
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
            <h1 class="headline fl">供应商付款</h1>
        </div>
        <!--查询条件 start-->
        <div class="search-box clearfix" id="searchForm">
            <div class="form-item form-widh">
                <input type="text" name="keyword"  class="yqx-input yqx-input-icon yqx-input-small js-supplier" value="" placeholder="供应商名称">
                <input type="hidden" name="search_supplierId" value=""/>
            </div>
            <div class="form-item form-widh">
                <input type="text" name="search_startInTime"  class="yqx-input yqx-input-icon yqx-input-small" id="startDate" value="" placeholder="入库开始日期">
                <span class="fa icon-calendar icon-small"></span>
            </div>
            至
            <div class="form-item form-widh">
                <input type="text" name="search_endInTime"  class="yqx-input yqx-input-icon yqx-input-small" id="endDate" value="" placeholder="入库结束日期">
                <span class="fa icon-calendar icon-small"></span>
            </div>
            <div class="form-item form-widh">
                <input type="text"  class="yqx-input yqx-input-icon yqx-input-small js-payment" value="" placeholder="支付周期">
                <input type="hidden" name="search_payMethod" value=""/>
                <span class="fa icon-angle-down icon-small"></span>
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
        </div>
        <!--查询条件 end-->

        <!--操作按钮 start-->
        <div class="btn-box">
            <a href="javascript:;" class="fr export-excel"><i class="icon-signout "></i>导出Excel</a>
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
                    <th>序号</th>
                    <th class="text-l">供应商名称</th>
                    <th class="text-r">配件总价</th>
                    <th class="text-r">税费</th>
                    <th class="text-r">运费</th>
                    <th class="text-r">应付金额</th>
                    <th class="text-r">实付金额</th>
                    <th class="text-r">未付金额</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <%if(json.data && json.data.content){%>
                <%for(var i=0;i< json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>

                 <tr data-pay-supplier-id="<%=item.supplierId%>"
                     data-pay-supplier-name="<%=item.supplierName%>"
                     class="yqx-link js-inforlink">
                    <td><%=json.data.size*(json.data.number-1)+i+1%></td>
                    <td class="text-l ellipsis-1 js-show-tips"><%=item.supplierName%></td>
                     <td class="money-font text-r">&yen;<%=item.goodsAmount%></td>
                     <td class="money-font text-r">&yen;<%=item.taxAmount%></td>
                     <td class="money-font text-r">&yen;<%=item.freightAmount%></td>
                    <td class="money-font text-r">&yen;<%=item.totalAmount%></td>
                    <td class="money-font text-r">&yen;<%=item.amountPaid%></td>
                    <td class="money-font text-r">&yen;<%=item.amountPayable%></td>
                     <td><a href="javascript:void(0)"
                            data-pay-supplier-id="<%=item.supplierId%>"
                            data-pay-supplier-name="<%=item.supplierName%>"
                            class=" js-inforlink">
                    <% if(item.amountPayable !=0){%>
                   付款
                         <%}else {%>
                         <span class="inlist-link"> 详情</span>

                <%}%>
                     </a>
                    </td>
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

<!--供应商名称 -->
<#--<script type="text/html" id="supplierTpl">-->
    <#--<%if (templateData){%>-->
    <#--<ul class="yqx-downlist-content js-downlist-content">-->
        <#--<%for(var i=0; i < templateData.length; i++){%>-->
        <#--<%var item = templateData[i]%>-->
        <#--<li class="js-downlist-item">-->
            <#--<span title="<%=item.supplierName%>"><%=item.supplierName%></span>-->
        <#--</li>-->
        <#--<%}%>-->
    <#--</ul>-->
    <#--<%}%>-->
<#--</script>-->

<!-- 脚本引入区 end -->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/settlement/pay/pay-supplier.js?a61ab5002aaca3414916912c3edad382"></script>

<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
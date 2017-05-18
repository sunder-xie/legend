<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/proxy_settlement_info.css?332ead6b38eb3e62d4e42255afbfbff5"/>
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">
    <div class="order-left fl">
    <#include "yqx/tpl/settlement/settlement-nav-tpl.ftl">
    </div>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h3 class="headline fl js-headline"></h3>
        </div>
        <div class="yqx-group" id="client">
            <!-- group标题 start -->
            <div class="yqx-group-head">
                委托方对账单汇总
            </div>
            <!-- group标题 end -->
            <!-- group内容 start -->
            <div class="yqx-group-content js-formInfo1">
                <input type="hidden" name="proxyShopId" class="proxyShopId wt-shop-id" value="">
                <input type="hidden" name="proxyName" class="proxyName" value="">
                <div class="clearfix formInfo getform1">
                    <div class="show-grid">
                        <div class="form-label">
                            时间区间：
                        </div>
                        <div class="form-item">
                            <input type="text" name="proxyStartTime" id="carStartTime" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="">
                            <span class="fa icon-calendar icon-small"></span>
                        </div>
                        -
                        <div class="form-item">
                            <input type="text" name="proxyEndTime" id="carEndTime" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="">
                            <span class="fa icon-calendar icon-small"></span>
                        </div>
                        <div class="form-label">
                            受托方
                        </div>
                        <div class="form-item">
                            <input name="" class="yqx-input yqx-input-small js-wt-select">
                        </div>
                        <button class="yqx-btn yqx-btn-3 yqx-btn-small js-wt-search fr">搜索</button>
                    </div>
                </div>
                <!-- 表格容器 start -->
                <div id="detailInfo1"></div>
                <!-- 表格容器 end -->
                <!-- 表格数据模板 start -->
                <script type="text/template" id="detailTpl1">
                    <table class="yqx-table">
                        <thead>
                        <tr>
                            <th>接单总数</th>
                            <th>交车总数</th>
                            <th>总金额</th>
                            <th>交车已结算数量</th>
                            <th>交车未结算数量</th>
                            <th>应付金额</th>
                        </tr>
                        </thead>
                        <%if(json && json.data){%>
                        <%var item = json.data;%>
                        <tr>
                            <td><%=item.orderTakeCount%></td>
                            <td><%=item.completeCount%></td>
                            <td><%=item.totalAmount%></td>
                            <td><%=item.balanceProxyCount%></td>
                            <td><%=item.completeNoBalanceCount%></td>
                            <td><%=item.realAmount%></td>
                        </tr>
                        <%}%>
                    </table>
                </script>
            </div>
            <!-- group内容 end -->
        </div>
        <div class="yqx-group" id="server">
            <!-- group标题 start -->
            <div class="yqx-group-head">
                受托方对账单汇总
            </div>
            <!-- group标题 end -->
            <!-- group内容 start -->
            <div class="yqx-group-content js-formInfo2">
                <div class="clearfix formInfo getform2">
                    <div class="show-grid">
                        <div class="form-label">
                            时间区间：
                        </div>
                        <div class="form-item">
                            <input type="text" name="proxyStartTime" id="carStartDate" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="">
                            <span class="fa icon-calendar icon-small"></span>
                        </div>
                        -
                        <div class="form-item">
                            <input type="text" name="proxyEndTime" id="carEndDate" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="">
                            <span class="fa icon-calendar icon-small"></span>
                        </div>
                        <div class="form-label">
                            委托方
                        </div>
                        <div class="form-item">
                            <input name="" class="yqx-input js-st-select">
                        </div>
                        <button class="yqx-btn yqx-btn-3 js-st-search fr">搜索</button>
                    </div>
                </div>
                <!-- 表格容器 start -->
                <div id="detailInfo2"></div>
                <!-- 表格容器 end -->
                <!-- 表格数据模板 start -->
                <script type="text/template" id="detailTpl2">
                    <table class="yqx-table">
                        <thead>
                        <tr>
                            <th>接单总数</th>
                            <th>交车总数</th>
                            <th>总金额</th>
                            <th>交车已结算数量</th>
                            <th>交车未结算数量</th>
                            <th>应收金额</th>
                        </tr>
                        </thead>
                        <%if(json && json.data){%>
                        <%var item = json.data;%>
                        <tr>
                            <td><%=item.orderTakeCount%></td>
                            <td><%=item.completeCount%></td>
                            <td><%=item.totalAmount%></td>
                            <td><%=item.balanceProxyCount%></td>
                            <td><%=item.completeNoBalanceCount%></td>
                            <td><%=item.realAmount%></td>
                        </tr>
                        <%}%>
                    </table>
                </script>
            </div>
            <!-- group内容 end -->
        </div>
    </div>
</div>
<#--弹窗 start-->
<script type="text/template" id="dialogTpl">
    <div id="detail-dialog">
        <h1>查看明细</h1>
        <table class="yqx-table">
            <thead>
            <tr>
                <th>序号</th>
                <th>交车时间</th>
                <th>委托时间</th>
                <th>委托单号</th>
                <th>服务类型</th>
                <th>服务项目</th>
                <th>工时</th>
                <th>应付金额</th>
            </tr>
            </thead>
            <tbody id="tableTpl"></tbody>
        </table>
        <!-- 分页容器 start -->
        <div class="yqx-page" id="paging"></div>
        <!-- 分页容器 end -->
    </div>
</script>

<script id="tableCon" type="text/template">
    <%if(json && json.data && json.data.content){%>
    <%for(var i=0;i<json.data.content.length;i++){%>
    <%var item = json.data.content[i];%>
    <tr>
        <td><%=i+1%></td>
        <td><%=item.completeTimeStr%></td>
        <td><%=item.proxyTimeStr%></td>
        <td><%=item.proxySn%></td>
        <td><%=item.serviceType%></td>
        <td><%=item.serviceName%></td>
        <td><%=item.serviceHour%></td>
        <td><%=item.proxyAmount%></td>
    </tr>
    <%}}%>
</script>
<#--弹窗end-->

<#--受托方模板 start-->
<script type="text/html" id="entrustingTpl">
    <%if(templateData && templateData.length > 0){%>
    <ul class="yqx-downlist-content js-downlist-content">
        <%for(var i=0;i<templateData.length;i++){%>
        <%var item=templateData[i];%>
        <li class="js-downlist-item">
            <span class="js-show-tips" style="width:100%"><%=item.shopName%></span>
        </li>
        <%}%>
    </ul>
    <%}%>
</script>
<#--受托方模板 end-->


<#--委托方模板 start-->
<script type="text/html" id="principalTpl">
    <%if(templateData && templateData.length > 0){%>
    <ul class="yqx-downlist-content js-downlist-content">
        <%for(var i=0;i<templateData.length;i++){%>
        <%var item=templateData[i];%>
        <li class="js-downlist-item">
            <span class="js-show-tips" style="width:100%"><%=item.name%></span>
        </li>
        <%}%>
    </ul>
    <%}%>
</script>
<#--委托方模板 end-->


<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/proxy_settlement_info.js?de0892acff984a54b862bd162be9147f"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/nav.css?4e0e2488e506519360b2e11d3e7d7769"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/proxy_settlement.css?816eccfe3fec9723290f727c1c541d2e"/>
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">
    <div class="order-left fl">
    <#include "yqx/tpl/settlement/settlement-nav-tpl.ftl">
    </div>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h3 class="headline fl js-headline"><#if BPSHARE == 'true'>受<#else>委</#if>托方对账单</h3>
        </div>
        <div class="yqx-group">
            <!-- group标题 start -->
            <div class="yqx-group-head">
                <div id="chooseTable">
                <#if BPSHARE == 'true'>
                    <div class="tab-item current-item" data-tag="server">受托方对账单</div><#if SESSION_SHOP_JOIN_STATUS == 1><div class="tab-item" data-tag="client">委托方对账单</div></#if>
                <#elseif SESSION_SHOP_JOIN_STATUS == 1>
                    <div class="tab-item current-item" data-tag="client">委托方对账单</div>
                </#if>
                </div>
            </div>
            <!-- group标题 end -->
            <!-- group内容 start -->
            <div class="yqx-group-content clearfix formInfo js-formInfo1" id="formInfo1">
                <div class="show-grid">
                    <div class="form-label">
                        交车时间：
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
                        委托时间：
                    </div>
                    <div class="form-item">
                        <input id="wtStartTime" type="text" name="completeStartTime" class="yqx-input yqx-input-icon yqx-input-small datepicker" value="" placeholder="">
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                    -
                    <div class="form-item">
                        <input id="wtEndTime" type="text" name="completeEndTime" class="yqx-input yqx-input-icon yqx-input-small datepicker" value="" placeholder="">
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                </div>
                <div class="show-grid formInfo">
                    <div class="form-label">
                        受托方：
                    </div>
                    <div class="form-item">
                        <input type="hidden" class="proxyShopId st-proxy-shop" name="proxyShopId" value=""/>
                        <input type="text" class="yqx-input yqx-input-small js-wt-select js-shopid" value="" placeholder="">
                    </div>
                    <div class="form-label">
                        车牌：
                    </div>
                    <div class="form-item">
                        <input type="text" name="carLicense" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="">
                    </div>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn fr">搜索</button>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small look1 fr">查看汇总</button>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small js-wt-export fr">导出</button>
                </div>
            </div>

            <div class="yqx-group-content clearfix formInfo js-formInfo2" id="formInfo2">

                <div class="show-grid">
                    <div class="form-label">
                        交车时间：
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
                        委托时间：
                    </div>
                    <div class="form-item">
                        <input id="wtStartDate" type="text" name="completeStartTime" class="yqx-input yqx-input-icon yqx-input-small datepicker" value="" placeholder="">
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                    -
                    <div class="form-item">
                        <input id="wtEndDate" type="text" name="completeEndTime" class="yqx-input yqx-input-icon yqx-input-small datepicker" value="" placeholder="">
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                </div>
                <div class="show-grid formInfo">
                    <div class="form-label">
                        委托方：
                    </div>
                    <div class="form-item">
                        <input type="hidden" class="proxyShopId wt-proxy-shop" name="proxyShopId" value=""/>
                        <input type="text" class="yqx-input yqx-input-small js-st-select js-shopid" value="" placeholder="">
                    </div>
                    <div class="form-label">
                        车牌：
                    </div>
                    <div class="form-item">
                        <input type="text" name="carLicense" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="">
                    </div>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn fr">搜索</button>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-settlement-btn fr">结算</button>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small look2 fr">查看汇总</button>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small js-st-export fr">导出</button>
                </div>

            </div>

        </div>
        <div class="yqx-group">
            <!-- group标题 start -->
            <div class="yqx-group-head">
                <div id="tableTag">
                    <div class="tab-item current-item">交车待结算</div>
                    <div class="tab-item">交车已结算</div>
                    <div class="tab-item">开单未交车</div>
                    <div class="tab-item">全部</div>
                </div>
            </div>
            <!-- group标题 end -->
            <!-- group内容 start -->
            <div class="yqx-group-content table-content">
                <!-- 表格容器 start -->
                <div id="tableInfo">

                </div>
                <!-- 表格容器 end -->

                <!-- 分页容器 start -->
                <div class="yqx-page" id="paging"></div>
                <!-- 分页容器 end -->
            </div>
            <!-- group内容 end -->
        </div>
        <!--------------------------------  委托方数据模板 start ------------------------------------------->
        <!-- 表格数据模板 start -->
        <script type="text/template" id="tableClientTpl0">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>车牌号</th>
                    <th>受托方门店名称</th>
                    <th>委托单号</th>
                    <th>工时</th>
                    <th>应付金额</th>
                </tr>
                </thead>
                <%if(json && json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr>
                    <td><%=item.carLicense%></td>
                    <td><div class="max-text js-show-tips"><%=item.proxyShopName%></div></td>
                    <td><div class="max-text js-show-tips"><%=item.proxySn%></div></td>
                    <td><%=item.serviceHour%></td>
                    <td><%=item.proxyAmount%></td>
                </tr>
                <%}%>
                <%}%>
            </table>


        </script>
        <script type="text/template" id="tableClientTpl1">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>车牌号</th>
                    <th>受托方门店名称</th>
                    <th>委托单号</th>
                    <th>工时</th>
                    <th>应付金额</th>
                </tr>
                </thead>
                <%if(json && json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr>
                    <td><%=item.carLicense%></td>
                    <td><div class="max-text js-show-tips"><%=item.proxyShopName%></div></td>
                    <td><div class="max-text js-show-tips"><%=item.proxySn%></div></td>
                    <td><%=item.serviceHour%></td>
                    <td><%=item.proxyAmount%></td>
                </tr>
                <%}%>
                <%}%>
            </table>

        </script>
        <script type="text/template" id="tableClientTpl2">

            <table class="yqx-table">
                <thead>
                <tr>
                    <th>车牌号</th>
                    <th>受托方门店名称</th>
                    <th>委托单号</th>
                    <th>工时</th>
                    <th>应付金额</th>
                </tr>
                </thead>
                <%if(json && json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr>
                    <td><%=item.carLicense%></td>
                    <td><div class="max-text js-show-tips"><%=item.proxyShopName%></div></td>
                    <td><div class="max-text js-show-tips"><%=item.proxySn%></div></td>
                    <td><%=item.serviceHour%></td>
                    <td><%=item.proxyAmount%></td>
                </tr>
                <%}%>
                <%}%>
            </table>

        </script>
        <script type="text/template" id="tableClientTpl3">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>车牌号</th>
                    <th>受托方门店名称</th>
                    <th>委托单号</th>
                    <th>工时</th>
                    <th>应付金额</th>
                </tr>
                </thead>
                <%if(json && json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr>
                    <td><%=item.carLicense%></td>
                    <td><div class="max-text js-show-tips"><%=item.proxyShopName%></div></td>
                    <td><div class="max-text js-show-tips"><%=item.proxySn%></div></td>
                    <td><%=item.serviceHour%></td>
                    <td><%=item.proxyAmount%></td>
                </tr>
                <%}%>
                <%}%>
            </table>

        </script>


        <!--------------------------------  受托方数据模板 start ------------------------------------------->
        <!-- 表格数据模板 start -->
        <script type="text/template" id="tableServerTpl0">
            <table class="yqx-table table-server">
                <thead>
                <tr>
                    <th><input type="checkbox" class="js-select-all"/></th>
                    <th>车牌号</th>
                    <th>委托方门店名称</th>
                    <th>委托单号</th>
                    <th>工时</th>
                    <th>应收金额</th>
                    <th>操作</th>
                </tr>
                </thead>
                <%if(json && json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr>
                    <input type="hidden" id="ids" value="">
                    <td><input type="checkbox" class="js-checkbox" value="<%=item.id%>"/></td>
                    <td><%=item.carLicense%></td>
                    <td><div class="max-text js-show-tips"><%=item.shopName%></div></td>
                    <td><div class="max-text js-show-tips"><%=item.proxySn%></div></td>
                    <td><%=item.serviceHour%></td>
                    <td><%=item.proxyAmount%></td>
                    <td><a href="javascript:;" class="comfirm js-comfirm" data-table-id="<%=item.id%>">结算</a></td>
                </tr>
                <%}%>
                <%}%>
            </table>

        </script>
        <script type="text/template" id="tableServerTpl1">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>车牌号</th>
                    <th>委托方门店名称</th>
                    <th>委托单号</th>
                    <th>工时</th>
                    <th>应收金额</th>
                </tr>
                </thead>
                <%if(json && json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr>
                    <td><%=item.carLicense%></td>
                    <td><div class="max-text js-show-tips"><%=item.shopName%></div></td>
                    <td><div class="max-text js-show-tips"><%=item.proxySn%></div></td>
                    <td><%=item.serviceHour%></td>
                    <td><%=item.proxyAmount%></td>
                </tr>
                <%}%>
                <%}%>
            </table>

        </script>
        <script type="text/template" id="tableServerTpl2">

            <table class="yqx-table">
                <thead>
                <tr>
                    <th>车牌号</th>
                    <th>委托方门店名称</th>
                    <th>委托单号</th>
                    <th>工时</th>
                    <th>应付金额</th>
                </tr>
                </thead>
                <%if(json && json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr>
                    <td><%=item.carLicense%></td>
                    <td><div class="max-text js-show-tips"><%=item.shopName%></div></td>
                    <td><div class="max-text js-show-tips"><%=item.proxySn%></div></td>
                    <td><%=item.serviceHour%></td>
                    <td><%=item.proxyAmount%></td>
                </tr>
                <%}%>
                <%}%>
            </table>

        </script>
        <script type="text/template" id="tableServerTpl3">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>车牌号</th>
                    <th>委托方门店名称</th>
                    <th>委托单号</th>
                    <th>工时</th>
                    <th>应收金额</th>
                </tr>
                </thead>
                <%if(json && json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr>
                    <td><%=item.carLicense%></td>
                    <td><div class="max-text js-show-tips"><%=item.shopName%></div></td>
                    <td><div class="max-text js-show-tips"><%=item.proxySn%></div></td>
                    <td><%=item.serviceHour%></td>
                    <td><%=item.proxyAmount%></td>
                </tr>
                <%}%>
                <%}%>
            </table>

        </script>

    </div>
    <!-- group内容 end -->
</div>
</div>
</div>

<#--车牌模板 start-->
<script type="text/html" id="carLicenceTpl">
    <%if(templateData && templateData.length > 0){%>
    <ul class="yqx-downlist-content js-downlist-content" data-tpl-ref="car-licence-tpl">
        <%for(var i=0;i<templateData.length;i++){%>
        <%var item=templateData[i];%>
        <li class="js-downlist-item">
            <span title="<%=item.id%>" style="width:100%"><%=item.license%></span>
        </li>
        <%}%>
    </ul>
    <%}%>
</script>
<#--车牌模板 end-->
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
<script  src="${BASE_PATH}/static/js/page/magic/proxy_settlement.js?9e6e4f749e11660d7d5760338f8a3412"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
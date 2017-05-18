<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/report/proxyReport.css?1b779acff950af7e910ce74cddf4229c"/>
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/report/left-nav.ftl"/>
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <input type="hidden" class="start-time" value=""/>
        <input type="hidden" class="end-time" value=""/>
        <input type="hidden" class="isPartner" value=""/>
        <!-- 标题 start -->
        <div class="order-head clearfix">
            <h3 class="headline">
                钣喷受托数据统计
            </h3>
        </div>
        <div class="search-form-box" id="searchForm">
            <div class="search-form">
                <div class="form-label">
                    委托时间：
                </div>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" id="startDate" placeholder="">
                    <span class="fa icon-calendar icon-small"></span>
                </div>
                —
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" id="endDate" placeholder="">
                    <span class="fa icon-calendar icon-small"></span>
                </div>
                <div class="form-label">
                    是否为股东店：
                </div>
                <div class="form-item">
                    <input type="hidden" value="" class="key">
                    <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-shareholder-select" value="" placeholder="">
                    <span class="fa icon-angle-down icon-small"></span>
                    <#--<input type="radio" name="stockholders" value="1"/> 是-->
                    <#--<input type="radio" name="stockholders" value="2"/> 否-->
                </div>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search">查询</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-reset">重置</button>
            </div>
            <div class="export-box">
                <a href="javascript:;" class="fr export-excel"><i class="icon-signout"></i>导出Excel</a>
            </div>
        </div>

        <div class="table-list">
            <table class="yqx-table" id="tableTest">
            <thead>
            <tr>
                <th>委托方门店名称</th>
                <th class="direction">委托车辆总数<i class="icon-caret-up js-car-up" data-car-sort-status=""></i></th>
                <th class="direction">委托总面数<i class="icon-caret-up js-surface-up" data-surface-status="1"></i></th>
                <th>面漆总金额</th>
                <th>钣金总金额</th>
                <th>委托总金额</th>
            </tr>
            </thead>
            <tbody id="tableCon">

            </tbody>
            </table>
        </div>
    </div>
</div>

<!-- 表格数据 start -->
<script type="text/html" id="tableTpl">
        <%if(json.success && json.data && json.data.proxyVoDTOList){%>
        <%for(var i=0; i< json.data.proxyVoDTOList.length; i++){%>
        <%var item = json.data.proxyVoDTOList[i]%>
        <tr class="js-detail">
            <input type="hidden" class="shop-id" value="<%=item.shopId%>"/>
            <td>
                <div class="max-width js-show-tips shop-name"><%=item.shopName%></div>
            </td>
            <td><%=item.carNum%></td>
            <td><%=item.proxySurface%></td>
            <td class="money-font">&yen;<%=item.paintAmount%></td>
            <td class="money-font">&yen;<%=item.metalAmount%></td>
            <td class="money-font">&yen;<%=item.proxyAmount%></td>
        </tr>
        <%}%>
        <%var subItem = json.data%>
        <tr>
            <td>合计</td>
            <td><%=subItem.totalCarNum%></td>
            <td><%=subItem.totalProxySurface%></td>
            <td class="money-font">&yen;<%=subItem.totalPaintAmount%></td>
            <td class="money-font">&yen;<%=subItem.totalMetalAmount%></td>
            <td class="money-font">&yen;<%=subItem.totalProxyAmount%></td>
        </tr>
        <%}%>
</script>
<!-- 表格数据 end -->

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/magic/report/proxyReport.js?c694995d445eb78d72e58516c9431c2b"></script>
<!-- 脚本引入区 end -->

<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
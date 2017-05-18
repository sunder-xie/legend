<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/report/proxyReportDtl.css?b8fa2d204de421759158f01aa25b8cfe"/>
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/report/left-nav.ftl"/>
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <!-- 标题 start -->
        <div class="order-head clearfix">
            <h3 class="headline">
                委托数据统计<small>－委托明细</small>
            </h3>
        </div>
        <div class="search-form-box" id="searchForm">
            <input type="hidden" class="shop-id" value="${shopId}"/>
            <div class="search-form">
                <div class="form-label">
                    委托方门店名称：
                </div>
                <div class="form-item">
                    <div class="yqx-text max-width js-show-tips proxy-name">
                        ${shopName!''}
                    </div>
                </div>
                <div class="form-label">
                   委托时间：
                </div>
                <div class="form-item">
                    <div class="yqx-text proxy-times">
                        <#if startTime ?? || endTime ??>
                            <span class="start-time">${startTime!''}</span> — <span class="end-time">${endTime!''}</span>
                        </#if>
                    </div>
                </div>
            </div>
            <div class="export-box">
                <a href="javascript:;" class="fr export-excel "><i class="icon-signout"></i>导出Excel</a>
            </div>
        </div>

        <div class="table-list" id="tableCon">

        </div>
    </div>
</div>

<!-- 表格数据 start -->
<script type="text/html" id="tableTpl">
    <table class="yqx-table" id="tableTest">
        <thead>
        <tr>
            <th>时间</th>
            <th>委托车辆总数</th>
            <th>委托总面数</th>
            <th>面漆总金额</th>
            <th>钣金总金额</th>
            <th>委托总金额</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.success && json.data && json.data.proxyVoDTOList){%>
        <%for(var i=0; i< json.data.proxyVoDTOList.length; i++){%>
        <%var item = json.data.proxyVoDTOList[i]%>
        <tr>
            <td>
                <div class="max-width js-show-tips"><%=item.proxyTimeStr%></div>
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
            <td>&yen;<%=subItem.totalPaintAmount%></td>
            <td>&yen;<%=subItem.totalMetalAmount%></td>
            <td>&yen;<%=subItem.totalProxyAmount%></td>
        </tr>
        <%}%>
        </tbody>
    </table>
</script>
<!-- 表格数据 end -->

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/magic/report/proxyReportDtl.js?575b800d505590e4b4a7f0368a8ac378"></script>
<!-- 脚本引入区 end -->

<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
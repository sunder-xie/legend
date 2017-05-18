<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/out/out-common.css?10e3e17a63bb9323c82892932093d3e5"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/out/out-list.css?bdbb302ebb3cce0584630bf679bd7cc5"/>
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
            <h3 class="headline">出库单查询</h3>
        </div>
        <!-- 标题 end -->
        <!-- 查询条件 start -->
        <div class="form-box" id="listForm">
            <div class="show-grid">
                <div class="form-item">
                    <input class="yqx-input yqx-input-icon yqx-input-small" id="start"
                           name="search_startTime"
                           placeholder="开始时间">
                    <i class="fa icon-calendar icon-small"></i>
                </div>
                至
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small"
                           name="search_endTime"
                           id="end"
                           placeholder="结束时间">
                    <i class="fa icon-calendar icon-small"></i>
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-agent"
                           placeholder="领料人">
                    <i class="fa icon-angle-down icon-small"></i>
                    <input type="hidden" name="search_goodsReceiver">
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small" name="search_keyword"
                           placeholder="手机号/车牌">
                </div>
            </div>

            <div class="show-grid">
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small" name="search_conditionLike"
                           placeholder="配件编号/配件名称/零件号">
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small" name="search_warehouseOutSn"
                           placeholder="出库单号">
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small" name="search_carInfo"
                           placeholder="适配车型">
                </div>
                <input type="hidden" name="search_status">
                <div class="search-btns fr">
                    <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</a>
                </div>
            </div>
        </div>
        <div class="list-tab-box js-list-tab">
            <div class="list-tab" data-key="LZCK">蓝字出库
            </div><div class="list-tab" data-key="HZCK">红字出库
        </div><div class="list-tab" data-key="CK_LZZF">蓝字作废
        </div><div class="list-tab" data-key="CK_HZZF">红字作废
        </div><div class="list-tab current-tab" data-key="">全部
        </div>

        </div>
        <!-- 查询条件 end -->

        <div class="order-list-table normal-table current-tab" id="listFill"></div>
        <!-- 数据列表>>表格  end -->

    </div>
    <!-- 右侧内容区 end -->
</div>
<!-- 表格模板 start -->
<script type="text/html" id="listTpl">
    <table class="yqx-table yqx-table-hidden yqx-table-big yqx-table-border yqx-table-hover yqx-table-link yqx-table-reverse">
        <thead>
        <tr class="tr-head">
            <th class="text-l tc-2">出库日期</th>
            <th class="text-l tc-3">出库详情</th>
            <th class="text-l tc-4"></th>
            <th class="text-r tc-price">销售总价</th>
            <th class="text-r tc-price">成本总价</th>
            <th class="text-c tc-7">操作</th>
        </tr>
        </thead>
        <tbody>
        <%if (json.data && json.data.content) {%>
        <%for (var i = 0;i < json.data.content.length; i++) {%>
        <%var item = json.data.content[i];%>
        <tr class="js-inforlink tr-title" data-id="<%= item.id%>">
            <td class="text-l text-bold text-important"><%=item.gmtCreate%></td>
            <td class="text-l">
                <div class="ellipsis-1 text-bold text-important js-show-tips">
                    <%=item.warehouseOutSn%>
                    <%=item.customerName%>
                    <%=item.carLicense%>
                    <%=item.carType%>
                </div>
            </td>
            <td class="text-l"></td>
            <td class="text-r money-font">&yen;<%=item.saleAmount%></td>
            <td class="text-r money-font">&yen;<%=item.costAmount%></td>
            <td class="op-re">
                <% if (item.status == "LZCK") {%><a href="javascript:void(0)" class=" yqx-link js-refund color-red" data-id="<%= item.id%>">退货</a>
                <% } else if (item.status == "HZCK") {%>
                <a href="javascript:void(0)" class="js-abolish color-red" data-id="<%= item.id%>">作废</a>
                <% } else {%>
                <a href="javascript:void(0)" class="js-detail color-blue" data-id="<%= item.id%>">查看</a>
                <% }%>
            </td>
        </tr>
        <%if (item.detailVoList) {%>
        <%for (var j = 0; j < item.detailVoList.length; j++) {%>
        <%var subItem = item.detailVoList[j];%>
        <tr class="detail-list">
            <td class="text-l">
                <div class="ellipsis-1 text-bold js-show-tips"><%=subItem.goodsFormat%></div>
                <div class="ellipsis-1 text-minor js-show-tips"><%=subItem.goodsSn%></div>
            </td>
            <td class="text-l">
                <div class="ellipsis-1 text-bold js-show-tips"><%=subItem.goodsName%></div>
                <div class="ellipsis-1 text-minor js-show-tips"><%=subItem.carInfoStr%></div>
            </td>
            <td class="text-l">
                <div class="ellipsis-1 text-minor js-show-tips">出库 </div>
                <div class="ellipsis-1 js-show-tips"><%=subItem.goodsCount%><span class="text-minor"><%=subItem.measureUnit%></span></div>
            </td>
            <td class="text-r">
                <div class="text-minor">出库价</div>
                <div class="money-small-font">&yen;<%=subItem.salePrice.toFixed(2)%></div>
            </td>
            <td class="text-r">
                <div class="text-minor">成本价</div>
                <div class="money-small-font">&yen;<%=subItem.inventoryPrice.toFixed(2)%></div>
            </td>
            <td>
                <%if(j == item.detailVoList.length-1 ){%>
                <% if (item.status == "LZCK") {%>

                <% } else if (item.status == "HZCK") {%>
                <p class="seal-3 yth"></p>
                <% } else {%>
                <p class="seal-3 yzf"></p>
                <% }%>
                <% }%>
            </td>
        </tr>

        <%}}%>
        <% if (i < json.data.content.length - 1) { %>
        <tr class="blank">
            <td colspan="6"></td>
        </tr>
        <%}}}%>
        </tbody>
    </table>
    <%if(json.data && json.data.otherData){%>
    <%var total = json.data.otherData;%>
    <div class="search-result">
        查询结果
        <span>出库总计：<i class="money-font js-money-font"><%= total.totalAmountStatistics%></i></span>
    </div>
    <%}%>
    <div class="yqx-page" id="listPage">
    </div>
</script>
<!-- 表格模板 end -->

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/warehouse/out/out-list.js?20784dc93a811ebda72bab85238b077d"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
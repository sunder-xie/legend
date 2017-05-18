<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/stock/stock-common.css?2a6120c599f20dbc8877c532837dd82f"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/stock/stock-query.css?e65ee94efd9e7e11cc33228ebf9d9240"/>
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
            <h3 class="headline fl">库存查询</h3>
        </div>
        <!-- 标题 end -->
        <!-- 查询条件 start -->
        <div class="form-box" id="listForm">
            <div class="show-grid">
                <div class="form-item">
                    <input class="yqx-input yqx-input-icon yqx-input-small goods_type_input"
                           placeholder="选择配件类型"
                           name="goodsTypeText"
                           readonly>
                    <input type="hidden" id="search_catId" name="search_stdCatId">
                    <input type="hidden" id="aa" name="customCat">
                    <i class="fa icon-angle-down icon-small"></i>
                </div><div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-brand"
                           placeholder="选择配件品牌">
                    <input type="hidden" name="search_brandId">
                    <i class="fa icon-angle-down icon-small"></i>
                </div><div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-stock"
                           placeholder="选择库存状态">
                    <i class="fa icon-angle-down icon-small"></i>
                    <input type="hidden" name="search_zeroStockRange">
                </div><div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-sale"
                           placeholder="选择上架状态" value="上架">
                    <input type="hidden" name="search_onsaleStatus" value="1" />
                    <i class="fa icon-angle-down icon-small"></i>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small"
                            placeholder="配件名称"
                            name="search_goodsNameLike">
                </div><div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small"
                            placeholder="零件号"
                            name="search_goodsFormatLike">
                </div><div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small js-supplier"
                            placeholder="适配车型"
                            name="search_carInfoLike">
                </div>
                    <div class="form-item">
                <input type="text"  name="search_depotLike" class="yqx-input yqx-input-icon yqx-input-small js-depot"
                       placeholder="仓库货位">
                <i class="fa icon-angle-down icon-small"></i>
            </div>
                <div class="search-btns fr">
                    <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</a>
                </div>
            </div>
        </div>
        <div class="btn-group">
            <button class="yqx-btn yqx-btn-4 yqx-btn-small outstock">出库</button>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small instock">入库</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small merge">配件合并</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small toinventory">库存盘点</button>
            <a href="javascript:;" class="yqx-link export-excel fr"><i class="icon-signout"></i>导出Excel</a>
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
    <table class="yqx-table yqx-table-hidden yqx-table-border yqx-table-hover yqx-table-link yqx-table-reverse">
        <thead>
        <tr>
            <th class="text-c tc-checkbox"><input type="checkbox" class="js-select-all"></th>
            <th class="text-l tc-2">配件详情</th>
            <th class="tc-3"></th>
            <th class="text-c tc-4">库存</th>
            <th class="text-r tc-price">销售价</th>
            <th class="text-r tc-price">成本价</th>
            <th class="text-r tc-price">上次采购价</th>
            <th class="text-l tc-date">入库日期</th>
            <th class="text-r tc-price">库存总成本</th>
        </tr>
        </thead>
        <tbody>
        <%for(var i=0;i < json.data.content.length;i++){%>
        <%
        var item = json.data.content[i];
        if (item.tqmallStatus == 4) {
            var modifyAddress = "/shop/goods/add_custom_page/ng?id="+ item.id;
        } else {
            var modifyAddress = "/shop/goods/update_not_tq_page/ng?id="+ item.id;
        }

        var itemInventoryPrice =0 ;
        if(item.inventoryPrice!=null){
            itemInventoryPrice = item.inventoryPrice.toFixed(2)
        }
        var itemStock =0;
        if(item.stock !=null){
            itemStock = item.stock
        }
        var shortageNumber =0;
        if(item.shortageNumber !=null){
             shortageNumber = item.shortageNumber;
        }
        var itemTotalAmount = (itemStock * itemInventoryPrice).toFixed(2)

        var itemLastprice = "";
        if(item.lastInPrice !=0 && item.lastInPrice != null){
            itemLastprice =item.lastInPrice
        }
        %>
        <tr class="detail-list" data-itemid="<%=item.id%>" data-tqmallstatus="<%=item.tqmallStatus%>">
            <td class="text-c">
                <input type="checkbox" name="checkId" value="<%=item.id%>" class="line-select js-goods" <% if(item.onsaleStatus == 0){%>disabled<%}%>>
            </td>
            <#--配件详情-->
            <td class="text-l">
                <div class="ellipsis-1 info text-bold text-important js-show-tips">
                    <%=item.format%>
                </div>
                <div class="ellipsis-1 info text-important js-show-tips">
                    <%=item.goodsSn%>
                </div>
            </td>
            <#--配件名称+适配车型-->
            <td class="text-l">
                <div class="ellipsis-2 text-bold text-important js-show-tips" data-id = '<%=item.id%>'>
                <% if(item.onsaleStatus == 0){%><span class="money-highlight">(已下架)</span><%}%>
                   <%=item.name%>
                </div>
                <div class="ellipsis-1 text-minor js-show-tips">
                    <%=item.carInfoStr%>
                </div>
            </td>
            <#--库存-->
            <td class="text-c">
                <div class="text-bold text-important"><%=item.stock%></div>
                <div class="text-minor"><%=item.depot == null ? "" : "(" + item.depot + ")仓"%></div>
            </td>
            <#--销售价-->
            <td class="text-r text-bold money-highlight">&yen;<%=item.price%></td>
            <#--成本价-->
            <td class="text-r text-bold money-highlight">&yen;<%=itemInventoryPrice%></td>
            <#--上次采购价-->
            <td class="text-r text-minor money-highlight-size"><% if(item.lastInPrice){ %>&yen;<%=item.lastInPrice%> <%}%></td>
            <#--入库日期-->
            <td class="text-l">
                <div class="ellipsis-1 text-r text-important js-show-tips"><%=item.lastInTimeStr%></div>
            </td>
            <#--库存总成本-->
            <td class="text-r">
                <div class="p-right text-bold money-highlight">&yen;<%=itemTotalAmount%></div>
            </td>
        </tr>
        <%}%>
        </tbody>
    </table>
    <%if(json.data && json.data.otherData){%>
    <%var total = json.data.otherData;%>
    <div class="search-result">
        查询结果
        <span>库存总成本：<i class="money-font js-money-font"><%= total.totalAmountStatistics%></i></span>
    </div>
    <%}%>
</script>
<!-- 表格模板 end -->

<!-- 配件合并模板  start-->
<script type="text/html" id="combineTpl">
    <div class="dialog combine-dialog">
        <div class="dialog-title">配件合并</div>
        <div>
            <div class="tips">*被合并的原有配件, 库存变为0,并下架; 目标配件, 库存增加,成本单价重新结算。</div>
        </div>
        <div class="dialog-con">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th class="text-l">序号</th>
                    <th class="text-l">配件名称</th>
                    <th class="text-l">零件号</th>
                    <th class="text-l">仓库货位</th>
                    <th class="text-r">销售价</th>
                    <th class="text-r">成本价</th>
                    <th>数量</th>
                </tr>
                </thead>
                <% for (var i = 0; i < data.length; i++) {var item = data[i];%>
                <tr>
                    <td><%=i+1%></td>
                    <td><%=item.name%></td>
                    <td><%=item.format%></td>
                    <td><%=item.depot%></td>
                    <td><%=item.price%></td>
                    <td><%=item.inventoryPrice%></td>
                    <td><%=item.stock%></td>
                </tr>
                <%}%>
            </table>

        </div>
        <div class="dialog-combine-btn">
            <div class="form-label">
                目标配件:
            </div>
            <div class="form-item">
                <input type="hidden" id="oldGoodsIds" value=""/>
                <select name="newGoodsId" id="newGoodsId" class="new-goods">
                    <% for (var i in data) {var item = data[i];%>
                    <option value="<%=item.id%>"><%=item.name%>--<%=item.format%></option>
                    <%}%>
                </select>
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js_merge">确认配件合并</button>
        </div>
    </div>
</script>

<!-- 配件分类模板  end-->
<#include "yqx/tpl/common/goods-type-tpl.ftl">
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/warehouse/stock/stock-query.js?f4c0527d8948f29c4fb0e3853119e25a"></script>
<!-- 脚本引入区 end -->

<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">

<#include "yqx/layout/footer.ftl">
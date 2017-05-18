<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/stock/stock-common.css?2a6120c599f20dbc8877c532837dd82f"/>
<link rel="stylesheet"
      href="${BASE_PATH}/static/css/page/warehouse/stock/stock-warning.css?85f665261a85ea97e7c4bf7401d3018c"/>
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
            <h3 class="headline fl">库存预警</h3>
            <a href="javascript:;" class="yqx-btn yqx-btn-2 yqx-btn-small fr new-menu js-caigou"><i>＋</i>新建采购单</a>
        </div>
        <!-- 标题 end -->
        <!-- 查询条件 start -->
        <div class="form-box" id="listForm">
            <!-- 1:上架的配件-->
            <input type="hidden" name="search_onsaleStatus" value="1"/>
            <input type="hidden" name="search_onlywarning" value="true"/>
            <div class="show-grid">

                <div class="form-item">
                    <input class="yqx-input yqx-input-icon yqx-input-small goods_type_input"
                           placeholder="选择配件类型"
                           name="goodsTypeText"
                           readonly>
                    <input type="hidden" id="search_catId" name="search_stdCatId">
                    <input type="hidden" id="aa" name="customCat">
                    <i class="fa icon-angle-down icon-small"></i>
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-brand"
                           placeholder="选择配件品牌">
                    <input type="hidden" name="search_brandId">
                    <i class="fa icon-angle-down icon-small"></i>
                </div>
                <div class="form-item">
                    <input class="yqx-input yqx-input-small"
                           placeholder="请输入配件名称"
                           name="search_goodsNameLike">
                </div>
                <div class="form-item">
                    <input type="text"
                           class="yqx-input yqx-input-icon yqx-input-small"
                           placeholder="请输入适配车型"
                           name="search_carInfoLike">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item">
                    <input type="text"
                           class="yqx-input yqx-input-icon yqx-input-small js-warning"
                           placeholder="请选择库存预警数量">
                    <input type="hidden" name="search_shortageNumberScope">
                    <i class="fa icon-angle-down icon-small"></i>
                </div>

                <div class="search-btns fr">
                    <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</a>
                </div>
            </div>
        </div>

        <div class="btn-group">
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
    <table class="yqx-table yqx-table-border yqx-table-hidden yqx-table-hover yqx-table-link yqx-table-reverse">
        <thead>
        <tr>
            <th class="text-c tc-checkbox"><input type="checkbox" class="js-select-all"></th>
            <th class="text-l tc-1">序号</th>
            <th class="text-l tc-3">配件名称</th>
            <th class="text-l tc-4">库存数量</th>
            <th class="text-l tc-5">预警库存</th>
            <th class="text-l tc-6">前3个月<br/>月均销量</th>
            <th class="text-l tc-7">建议库存</th>
            <th class="text-r tc-price">云修采购价</th>
            <th class="text-l tc-9">适配车型</th>
            <th class="text-l tc-brand">配件品牌</th>
            <th class="text-c">操作</th>
        </tr>
        </thead>
        <%for(var i=0;i < json.data.content.length; i++){%>
        <%
        var item = json.data.content[i];

        var itemInventoryPrice =0 ;
        if(item.inventoryPrice!=null){
        itemInventoryPrice = item.inventoryPrice.toFixed(2)
        }
        var itemStock =0;
        if(item.stock !=null){
        itemStock = item.stock
        }
        var itemTotalAmount = (itemStock * itemInventoryPrice).toFixed(2)

        var itemLastprice = "";
        if(item.lastInPrice !=0 && item.lastInPrice != nul){
        itemLastprice =item.lastInPrice
        }
        %>
        <tr id="TR_<%=item.id%>" class="detail-list" data-itemid="<%=item.id%>"
            data-unit="<%=item.measureUnit%>"
            data-tqmallstatus="<%=item.tqmallStatus%>">
            <input type="hidden" name="itemId" value="<%=item.id%>"/>
            <td class="text-c"><input type="checkbox" name="checkId" value="<%=item.id%>" class="line-select"></td>
            <td class="text-l"><%=json.data.size*(json.data.number)+i+1%></td>
            <td class="text-l">
                <div class="ellipsis-1 js-show-tips text-bold"><%=item.name%></div>
                <div class="ellipsis-1 js-show-tips"><%=item.format%></div>
            </td>
            <td class="text-l"><%=item.stock%> <%=item.measureUnit%></td>
            <td class="text-l"><%=item.shortageNumber%> <%=item.measureUnit%></td>
            <td class="text-l" name="averageNumber"></td>
            <td class="text-l" name="suggestGoodsNumber"></td>
            <td class="text-r" name="inventoryPrice">
                <div class="text-c">　　--</div>
            </td>
            <td class="text-l">
                <div class="ellipsis-1 js-show-tips"><%=item.carInfoStr%></div>
            </td>
            <td class="text-l">
                <div class="ellipsis-1 js-show-tips"><%=item.goodsBrand%></div>
            </td>
            <td class="text-c">
                <a href="${BASE_PATH}/shop/warehouse/in/in-edit/blue?goodsIds=<%=item.id%>" class="yqx-link yqx-link-1 js-in-edit">采购单</a>
            </td>
        </tr>
        <%}%>
    </table>
</script>
<!-- 表格模板 end -->

<!-- 配件分类模板  end-->
<#include "yqx/tpl/common/goods-type-tpl.ftl">
<!-- 脚本引入区 start -->

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/warehouse/stock/stock-warning.js?7f34cf59aec00cb2c5b23feca44aa488"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
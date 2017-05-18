<#-- Created by sky on 16/11/24. -->

<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/buy/common.css?b3b5b13cc75d51ec75cf1538617ecf8b">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/buy/shortgoods/goods-list.css?7cc79eb666c98e83538d074ccdf16000">

<section class="yqx-wrapper">
    <section class="banner-panel">
    <#include "yqx/tpl/buy/banner.ftl">
    </section>

    <section class="nav-panel">
    <#include "yqx/tpl/buy/nav.ftl">
    </section>

    <section class="main" id="main" role="main">
        <section class="search-form" id="searchForm">
            <input type="hidden" name="search_onsaleStatus" value="1">
            <div class="show-grid show-gap-grid">
                <div class="col-4">
                    <div class="form-item">
                        <input type="text" name="goodsTypeText" class="yqx-input yqx-input-icon goods_type_input" placeholder="请选择配件类别" readonly>
                        <input type="hidden" name="search_stdCatId">
                        <input type="hidden" name="customCat">
                        <span class="fa icon-angle-down"></span>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-item">
                        <input type="hidden" name="search_brandId">
                        <input type="text" class="yqx-input yqx-input-icon js-goods-brand" placeholder="请选择配件品牌">
                        <span class="fa icon-angle-down"></span>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-item">
                        <input type="hidden" name="search_tqmallStatus">
                        <input type="text" class="yqx-input yqx-input-icon js-goods-status" readonly>
                        <span class="fa icon-angle-down"></span>
                    </div>
                </div>
            </div>
            <div class="show-grid show-gap-grid">
                <div class="col-4">
                    <div class="form-item">
                        <input class="yqx-input"
                               placeholder="查找配件编号、配件名称、零件号"
                               name="search_likeKeyWords">
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-item">
                        <input type="text"
                               class="yqx-input"
                               placeholder="查找适配车型"
                               name="search_carInfoLike">
                    </div>
                </div>
                <div class="form-item col-4">
                    <button class="yqx-btn yqx-btn-1 btn-half js-search-btn">搜索库存配件</button>
                    <a href="javascript:;" class="yqx-btn yqx-btn-1 btn-half js-search-goods-lib">搜索淘汽配件库</a>
                </div>
            </div>
        </section>
        <section class="search-box">
            <table class="yqx-table yqx-table-border yqx-table-big goods-table">
                <thead>
                <tr>
                    <th class="tc-1"></th>
                    <th class="tc-2">配件名称</th>
                    <th class="tc-3">零件号</th>
                    <th class="tc-4">配件编号</th>
                    <th class="tc-5">库存</th>
                    <th class="tc-6">淘汽采购价</th>
                    <th class="text-center">操作</th>
                </tr>
                </thead>
                <tbody id="searchListBody"></tbody>
            </table>
        </section>
        <section class="yqx-page" id="pagingBox"></section>
    </section>
</section>

<script type="text/html" id="goodsListTpl" data-desc="配件列表">
    <% if (json.data && json.data.content && json.data.content.length) { %>
    <% for (var i = 0; i < json.data.content.length; i++) { %>
    <% var item = json.data.content[i]; %>
    <tr id="TR_<%=item.id%>" class="detail-list" data-itemid="<%=item.id%>">
        <input type="hidden" name="itemId" value="<%=item.id%>"/>
        <td>
                <img src="<%= item.imgUrl %>" alt="<%= item.name %>" class="goods-photo"
                     onerror="this.src='http://tqmall-image.oss-cn-hangzhou.aliyuncs.com/images/no_picture.gif'">
            </a>
        </td>
        <td>
            <p class="info-row ellipsis-1 js-show-tips">
                <%= item.name %>
            </p>
            <% if (item.priceName) { %>
            <p class="info-row info-highlight"><%= item.priceName %></p>
            <% } %>
            <p class="info-row ellipsis-2 js-show-tips">
                关联车型:
                <% var carStr = ''; %>
                <% if (item.carInfoList && item.carInfoList.length) { %>
                <% for (var j = 0; j < item.carInfoList.length; j++) { %>
                    <% carStr += item.carInfoList[j].carInfo + ' '; %>
                <% } } %>
                <%= carStr + item.carInfoStr; %>
            </p>
        </td>
        <td>
            <p class="info-row"><%= item.format %></p>
            <p class="info-row"><%= item.relGoodsFormatList ? '关联零件号' + item.relGoodsFormatList : ''; %></p>
        </td>
        <td><%= item.goodsSn %></td>
        <td class="info-highlight"><%= item.stock + item.measureUnit %></td>
        <td name="inventoryPrice"></td>
        <td class="text-center">
            <% if (item.tqmallGoodsId && item.tqmallGoodsId >0) { %>
            <a href="http://www.tqmall.com/Goods/detail.html?id=<%=item.tqmallGoodsId%>" target="_blank" class="yqx-btn yqx-btn-3 yqx-btn-micro">立即购买</a>
            <% } else {%>
            <a href="http://www.tqmall.com/Search.html?q=<%=item.name%>" target="_blank" class="yqx-btn yqx-btn-2 yqx-btn-micro">搜索淘汽</a>
            <% } %>
        </td>
    </tr>
    <% } } %>
</script>

<#include "yqx/tpl/common/goods-type-tpl.ftl">

<script src="${BASE_PATH}/static/js/page/buy/shortgoods/goods-list.js?fca845693f67085b494244cedc3b0e01"></script>

<#include "yqx/layout/footer.ftl">
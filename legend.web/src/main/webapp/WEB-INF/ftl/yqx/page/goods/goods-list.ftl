<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet"
      href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/goods/goods-list.css?e809df67f41b75cedf4fc88b9248cc1b"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">配件资料</h3>
        </div>
        <div class="content">
            <div class="form-box" id="formData">
                <!-- 1:在售的商品-->
                <input type="hidden" name="search_onsaleStatus" value="1">
                <div class="show-grid">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-icon yqx-input-small goods_type_input"
                               placeholder="配件类别"
                               name="goodsTypeText"
                               readonly>
                        <input type="hidden" id="search_catId" name="search_stdCatId">
                        <input type="hidden" id="aa" name="customCat">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-item">
                        <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-brand"
                               placeholder="配件品牌">
                        <input type="hidden" name="search_brandId">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-item">
                        <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-sale"
                               placeholder="配件上架" value="上架">
                        <input type="hidden" name="search_onsaleStatus" value="1" />
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="search_goodsNameLike"
                               class="yqx-input yqx-input-small"
                               placeholder="配件名称">
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-item">
                        <input type="text"
                               name="search_goodsFormatLike"
                               class="yqx-input yqx-input-small"
                               placeholder="零件号">
                    </div>
                    <div class="form-item">
                        <input type="text" name="search_depotLike" class="yqx-input yqx-input-icon yqx-input-small js-depot"
                               placeholder="配件所在货位">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="search_carInfoLike"
                               class="yqx-input yqx-input-small"
                               placeholder="车型">
                    </div>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
                </div>
            </div>
            <div class="btn-box clearfix">
                <button class="yqx-btn yqx-btn-4 js-tqmall-list">新增淘汽配件</button>
                <button class="yqx-btn yqx-btn-3 js-add">新增配件</button>
            <#if BPSHARE == 'true'>
                <button class="yqx-btn yqx-btn-3 js-paint-add">添加油漆资料</button>
            </#if>
                <div class="manage fr">
                    <img src="${BASE_PATH}/static/img/page/goods/img-02.png"><a href="${BASE_PATH}/shop/goods_category/goods-category-list">配件类别管理</a>
                </div>
                <div class="manage fr">
                    <img src="${BASE_PATH}/static/img/page/goods/img-01.png"><a href="${BASE_PATH}/shop/goods_brand/goods-brand-list">配件品牌管理</a>
                </div>
                <div class="manage fr">
                    <img src="${BASE_PATH}/static/img/page/goods/img-03.png"><a href="${BASE_PATH}/shop/goods_unit/goods-unit-list">配件单位管理</a>
                </div>
            </div>
            <!-- 表格容器 start -->
            <div>
                <table class="yqx-table yqx-table-hover">
                    <thead>
                    <tr>
                        <th class="text-l w-270 p-20">配件</th>
                        <th class="text-l">零售单价</th>
                        <th class="text-l">仓库货位</th>
                        <th class="text-l">预警库存</th>
                        <th class="text-l">状态</th>
                        <th class="text-c">操作</th>
                    </tr>
                    </thead>
                    <tbody id="tableListCon">
                    </tbody>
                </table>
            </div>
            <!-- 表格容器 end -->

            <!-- 分页容器 start -->
            <div class="yqx-page" id="pagingCon"></div>
            <!-- 分页容器 end -->
        </div>
    </div>
</div>

<!-- 表格模板-->
<script type="text/html" id="tableTpl">
    <%if(json.success && json.data.content){%>
    <%for(var i=0; i< json.data.content.length;i++){%>
    <%var item = json.data.content[i]%>
    <tr data-item-id="<%=item.id%>" data-tqmall-status="<%=item.tqmallStatus%>" class="js-edit-tr">
        <td class="text-l p-20  w-270">
            <h3 class="js-show-tips ellipsis-1"><%=item.name%></h3>
            <p class="js-show-tips ellipsis-1">配件编码:<%=item.goodsSn%></p>
            <p class="js-show-tips ellipsis-1">零件号:<%=item.format%></p>
        </td>
        <td class="text-l"><%=item.price%>元</td>
        <td class="text-l"><%=item.depot%></td>
        <td class="text-l"><%=item.shortageNumber%></td>
        <td class="text-l">
            <%if(item.onsaleStatus ==1){%>
            上架
            <%}else if(item.onsaleStatus ==0){%>
            下架
            <%}else {%>
            全部
            <%}%>
        </td>
        <td class="text-c">
            <%if(item.onsaleStatus ==1){%>
            <p><a href="javascript:;" class="yqx-link-2 js-downshelf">确认下架</a></p>
            <%}%>
            <%if(item.onsaleStatus ==0){%>
            <p><a href="javascript:;" class="yqx-link-3 js-upshelf">确认上架</a></p>
            <%}%>
            <p><a href="javascript:;" class="yqx-link-3 js-edit">编辑</a></p>
        <#if hasDelPartPrv == true>
            <p><a href="javascript:;" class="yqx-link-2 js-delete">删除</a></p>
        </#if>
        </td>
    </tr>
    <%}}%>
</script>

<script src="${BASE_PATH}/static/js/page/goods/goods-list.js?a09581c1cd153045d871e767f5b0a627"></script>

<#include "yqx/tpl/common/goods-type-tpl.ftl">
<#include "yqx/layout/footer.ftl">

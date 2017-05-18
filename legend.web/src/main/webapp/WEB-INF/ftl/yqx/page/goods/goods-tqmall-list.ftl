<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet"
      href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/goods/goods-tqmall-list.css?00dd7bf51e419c6e2b0299597ecbfbed"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/style/modules/chosen.css?30ab5c0b469dabe167389465b133233f"/>
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
            <h3 class="headline">配件资料
                <small>－淘汽采购</small>
            </h3>
        </div>
        <div class="content">
            <input type="hidden" name="search_goodsId" value="${goodsId}"/>
            <div class="form-box" id="formData">
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
                        <input type="text"
                               class="yqx-input yqx-input-icon yqx-input-small js-brand"
                               placeholder="配件品牌">
                        <input type="hidden" name="search_brandId">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-item">
                        <select id="allBrand" name="search_carModelId" val="" data-placeholder="适配品牌">
                        </select>
                        <script id="allBrandTpl" type="text/html">
                            <optgroup label="">
                                <option value="">请选择适配品牌</option>
                            </optgroup>
                            <%for(var cascadeSelectItem in templateData){%>
                            <optgroup label="<%= cascadeSelectItem%>">
                                <%for(var i=0;i< templateData[cascadeSelectItem].length;i++){%>
                                <% var groupcascadeSelectItem = templateData[cascadeSelectItem][i]%>
                                <option  value="<%= groupcascadeSelectItem.id%>"><%= groupcascadeSelectItem.name%></option>
                                <%}%>
                            </optgroup>
                            <%}%>
                        </script>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-item w-387">
                        <input type="text"
                               name="search_keyword"
                               class="yqx-input yqx-input-small"
                               placeholder="配件编号、配件名称、零件号、VIN码、车型名称">
                    </div>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
                </div>
            </div>
            <!-- 表格容器 start -->
            <div class="table-con">
                <table class="yqx-table yqx-table-hover">
                    <thead>
                    <tr>
                        <th class="text-l w-480">配件</th>
                        <th class="text-r p-right">定价</th>
                        <th class="text-l">仓库货位</th>
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

<script type="text/html" id="tableTpl">
    <%if(json.success && json.data.content){%>
    <%for(var i=0; i< json.data.content.length; i++){%>
    <%var item = json.data.content[i]%>
    <tr data-item-id="<%=item.goodsId%>"
        id="tqmallgoods_<%=item.goodsId%>"
        data-refer-legend-id="<%=item.legendGoodsId%>">
        <td class="text-l w-480">
            <input type="hidden" name="tqmallGoodsId" value="<%=item.goodsId %>"/>
            <input type="hidden" name="name" value="<%=item.goodsName %>"/>
            <input type="hidden" name="measureUnit" value="<%=item.minMeasureUnit %>"/>
            <input type="hidden" name="origin" value="<%=item.orign %>"/>
            <input type="hidden" name="cat1Id" value="<%=item.cat1Id %>"/>
            <input type="hidden" name="cat1Name" value="<%=item.cat1Name %>"/>
            <input type="hidden" name="cat2Id" value="<%=item.cat2Id %>"/>
            <input type="hidden" name="cat2Name" value="<%=item.catName %>"/>
            <input type="hidden" name="format" value="<%=item.goodsFormat %>"/>
            <input type="hidden" name="goodsSn" value="<%=item.goodsSn %>"/>
            <input type="hidden" name="tqmallGoodsSn" value="<%=item.goodsSn %>"/>
            <input type="hidden" name="brandId" value="<%=item.brandId %>"/>
            <input type="hidden" name="brandName" value="<%=item.brandName %>"/>
            <input type="hidden" name="imgUrl" value="<%=item.goodsImg %>"/>
            <input type="hidden" name="tqmallStatus" value="1"/>
            <input type="hidden" name="goodsStatus" value="0"/>
            <input type="hidden" name="goodsAttrRelList" value='<%=$jsonToString(item.goodsAttrList)%>'/>
            <input type="hidden" name="goodsCarList" value='<%=$jsonToString(item.carModelList)%>'/>
            <input type="hidden" name="catId" value="<%=item.catId %>"/>
            <div class="accessories-photo">
                <img src="<%=item.goodsImg%>" alt=""
                     onerror="this.src='http://tqmall-image.oss-cn-hangzhou.aliyuncs.com/images/no_picture.gif'"/>
            </div>
            <div class="accessories-text">
                <h3 class="w-280 ellipsis-2 js-show-tips"><%=item.goodsName%></h3>
                <p class="w-280 p-10">
                    <span class="ellipsis-1 js-show-tips">配件编码:<%=item.goodsSn%></span>
                    <span class="ellipsis-1 js-show-tips">商品品牌:<%=item.brandName%></span>
                </p>
                <p class="w-280">
                    <span class="ellipsis-1 js-show-tips">零件号:<%=item.goodsFormat%></span>
                    <%
                    var carModels = (item.carModelList == null) ? [] : item.carModelList
                    var carNameArr = [];
                    for(var j = 0; j < carModels.length; j++) {
                    carNameArr.push(carModels[j].name);
                    }%>
                    <span class="ellipsis-1 js-show-tips">适配车型: <%=carNameArr.join(" ")%></span>
                </p>
            </div>
        </td>
        <td class="text-r p-right money-font">
            <%if(item.legendGoodsPrice !=null){%>
            &yen;<%=item.legendGoodsPrice%>
            <%}%>
        </td>
        <td class="text-l">
            <%if(item.legendGoodsDepot !=null){%>
            货架号：<%=item.legendGoodsDepot%>
            <%}%>
        </td>
        <td class="text-c btn-padding">
            <%if(item.legendGoodsId !=null){%>
            <p>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-edit">编辑</button>
            </p>
            <p>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-delete">删除</button>
            </p>
            <%} else{%>
            <p>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-add">添加配件</button>
            </p>
            <%}%>
        </td>
    </tr>
    <%}}%>
</script>

<!-- 普通配件弹出框 -->
<script type="text/html" id="addInfoTpl">
    <div class="yqx-dialog">
        <div class="dialog-title">请补充配件信息</div>
        <div class="dialog-content addGoodsDIV">
            <!-- 淘汽配件ID-->
            <input type="hidden" name="tqmallGoodsId" value="<%=tqmallGoodsId%>"/>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    定价:
                </div>
                <div class="form-item">
                    <input type="text"
                           name="price"
                           class="yqx-input yqx-input-icon yqx-input-small"
                           data-v-type="required | number"
                           placeholder="请输入定价">
                    <span class="fa icon-small">元</span>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label">
                    仓库货位:
                </div>
                <div class="form-item">
                    <input type="text"
                           name="depot"
                           class="yqx-input yqx-input-small">
                </div>
            </div>
            <p id="tipmsg" class="tip"></p>
            <div class="dialog-btn">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-dialog-save">保存</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-dialog-cancle">取消</button>
            </div>
        </div>
    </div>
</script>

<!-- 油漆弹出框-->
<script type="text/html" id="addBPInfoTpl">
    <div class="yqx-dialog">
        <div class="dialog-title">添加油漆配件</div>
        <div class="dialog-content addGoodsDIV">
            <!-- 淘汽配件ID-->
            <input type="hidden" name="tqmallGoodsId" value="<%=tqmallGoodsId%>"/>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    定价:
                </div>
                <div class="form-item">
                    <input type="text"
                           name="price"
                           class="yqx-input yqx-input-icon yqx-input-small"
                           data-v-type="required | number"
                           placeholder="请输入定价">
                    <span class="fa icon-small">元</span>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label">
                    仓库货位:
                </div>
                <div class="form-item">
                    <input type="text"
                           name="depot"
                           class="yqx-input yqx-input-small">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    净含量:
                </div>
                <div class="form-item">
                    <input type="text"
                           name="netWeight"
                           data-v-type="required | number"
                           class="yqx-input yqx-input-small">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    带桶质量:
                </div>
                <div class="form-item">
                    <input type="text"
                           name="bucketWeight"
                           data-v-type="required | number"
                           class="yqx-input yqx-input-small">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    带桶和搅拌头的质量:
                </div>
                <div class="form-item">
                    <input type="text"
                           name="stirWeight"
                           data-v-type="required | number"
                           class="yqx-input yqx-input-small">
                </div>
            </div>

            <div class="show-grid">
                <div class="form-label form-label-must">
                    油漆等级:
                </div>
                <div class="form-item">
                    <input type="text" name="paintLevel" class="yqx-input yqx-input-icon yqx-input-small js-paint-level"
                           value="" placeholder="">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    油漆类型:
                </div>
                <div class="form-item">
                    <input type="text" name="paintType" class="yqx-input yqx-input-icon yqx-input-small js-paint-type"
                           value="" placeholder="">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </div>
            <p id="tipmsg" class="tip"></p>
            <div class="dialog-btn">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-dialog-save">保存</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-dialog-cancle">取消</button>
            </div>
        </div>
    </div>
</script>


<script src="${BASE_PATH}/static/js/page/goods/goods-tqmall-list.js?86622ae69f2c52d8a6c486ecfdbdecb0"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.chosen.js"></script>
<#include "yqx/layout/footer.ftl">

<#include "yqx/tpl/common/goods-type-tpl.ftl">


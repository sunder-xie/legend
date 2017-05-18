<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet"
      href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/goods/goods-toadd.css?ae770a7e06bb248658b24d29c15f92ba"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fr">
        <!-- 标题 start -->
        <div class="order-head clearfix">
            <h3 class="headline fl">配件资料
                <small>－新增配件</small>
            </h3>
        </div>
        <!-- 标题 end -->
        <!-- 右侧内容区 start -->
        <div class="content">

            <div class="title">
                <i></i>基本信息
                <div class="accessories-num fr">
                    配件编码：<span class="goodsSn">${goodsSn}</span>
                </div>
            </div>
            <div class="form-box" id="formData">
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            配件名称:
                        </div>
                        <div class="form-item">
                            <input type="text"
                                   name="name"
                                   class="yqx-input yqx-input-small"
                                   placeholder="请输入配件名称"
                                   data-v-type="required">
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            零件号:
                        </div>
                        <div class="form-item">
                            <input type="text"
                                   name="format"
                                   class="yqx-input yqx-input-small"
                                   data-v-type="required">
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            零售单价:
                        </div>
                        <div class="form-item sale-input-width">
                            <input type="text"
                                   name="price"
                                   class="yqx-input yqx-input-small"
                                   placeholder="请输入"
                                   data-v-type="required | number">
                            <span class="fa icon-small">元</span>
                        </div>
                        /
                        <div class="form-item small-input-width">
                            <input type="text" name="measureUnit"
                                   class="yqx-input yqx-input-small small-input-width js-units"
                                   placeholder="单位"
                                   data-v-type="required">
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label">
                            关联零件号:
                        </div>
                        <div class="form-item">
                            <input type="text" name="relGoodsFormatList" class="yqx-input yqx-input-small" value=""
                                   placeholder="">
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label">
                            仓库货位:
                        </div>
                        <div class="form-item">
                            <input type="text" name="depot" class="yqx-input yqx-input-icon yqx-input-small js-depot"
                                   placeholder="仓库货位">
                            <span class="fa icon-angle-down icon-small"></span>
                    </div>
                        </div>
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            预警库存:
                        </div>
                        <div class="form-item small-input-width">
                            <input type="text"
                                   name="shortageNumber"
                                   class="yqx-input yqx-input-small"
                                   placeholder=""
                                   data-v-type="required | number" value="0">
                        </div>
                        <div class="form-label small-label-width">
                            上架状态:
                        </div>
                        <div class="form-item small-input-width">
                            <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-sale"
                                   placeholder="配件上架">
                            <input type="hidden" name="onsaleStatus">
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                    </div>
                </div>

            </div>
            <div class="title"><i></i>基本属性</div>
            <div class="form-bottom-box">
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            配件类别:
                        </div>
                        <div class="form-item">
                            <input type="text"
                                   name="cat2Name"
                                   class="yqx-input yqx-input-small goods_type_input"
                                   placeholder="请选择配件类别"
                                   data-v-type="required"
                                   readonly data-custom-goods="true">
                            <input type="hidden" name="customCat"/>
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label">
                            配件品牌:
                        </div>
                        <div class="form-item stock-input-width">
                            <input type="text"
                                   name="brandName"
                                   class="yqx-input yqx-input-icon yqx-input-small js-brand"
                                   placeholder="请选择配件品牌">
                            <span class="fa icon-angle-down icon-small"></span>
                            <input type="hidden" name="brandId" value=""/>
                        </div>
                        <button class="yqx-btn yqx-btn-1 yqx-btn-small js-add-brand">新增自定义品牌</button>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label ">
                            配件适用部位:
                        </div>
                        <div class="form-item">
                            <input type="text"
                                   name="partUsedTo"
                                   class="yqx-input yqx-input-small"
                                   placeholder="">
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label ">
                        配件图片:
                    </div>
                    <div class="form-item upload">
                        <a href="javascript:;" class="upload-btn">
                            <input type="file" value="" id="fileBtn" class="file-btn"/>
                            <p class="before-upload">选择图片</p>
                            <i class="before-upload"><img src="${BASE_PATH}/static/img/page/magic/upload-add.png"/></i>
                            <input type="hidden" name="imgUrl" value="">
                            <div class="upload-pic">

                            </div>
                        </a>
                    </div>
                    <div class="mode">
                        <p>支持图片格式包括：</p>
                        <p>JIG、JPEG、PNG、JPG，大小不超过5M</p>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        配件为通用件:
                    </div>
                    <div class="form-item">
                        <input type="radio" name="general" class="js-general-yes"/>　是
                        <input type="radio" name="general" class="js-general-no" checked/>　否
                    </div>
                </div>
                <div class="cartype-content">
                    <h3>温馨提示: 用车型别名替代车型选择，开单更方便</h3>
                    <div class="cartype-content-box">
                        <div class="show-grid js-car-list" data-index="0">
                            <div class="form-label">
                                适用的车型:
                            </div>
                            <div class="form-item">
                                <input type="text" name="carInfo"  class="yqx-input yqx-input-icon yqx-input-small js-carSeries" value="" placeholder="请选择">
                                <span class="fa icon-angle-down icon-small"></span>
                                <input type="hidden" value="" name="carBrandId"/>
                                <input type="hidden" value="" name="carBrandName"/>
                                <input type="hidden" value="" name="carSeriesName"/>
                                <input type="hidden" value="" name="carSeriesId"/>
                                <input type="hidden" value="" name="carTypeName"/>
                                <input type="hidden" value="" name="carTypeId"/>
                            </div>
                            <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-car-type">选择车型</a>
                            <div class="form-label">
                                车型别名:
                            </div>
                            <div class="form-item w-150">
                                <input type="text" name="carAlias"  class="yqx-input yqx-input-small js-car-alias" value="" placeholder="车型参数/别名"  data-v-type="maxLength:20" data-label="车型别名">
                            </div>
                            <a href="javascript:;" class="yqx-btn yqx-btn-2 yqx-btn-small js-remove-carType">删除</a>
                        </div>
                    </div>
                    <div class="add-box">
                        <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-add-carType">添加</a>
                    </div>
                </div>
                <div class="addCustomBrandCon" id="dynamicAttr">
                    <div class="show-grid rel-list">
                        <div class="form-item val-small-width">
                            <input type="text" name="attrName" class="yqx-input yqx-input-small" value=""
                                   placeholder="自定义属性">
                        </div>
                        <div class="form-item">
                            <input type="text" name="attrValue" class="yqx-input yqx-input-small js-select" value=""
                                   placeholder="属性内容">
                        </div>
                        <a href="javascript:;" class="yqx-btn yqx-btn-2 yqx-btn-small js-remove-custom-brand">删除</a>
                    </div>
                </div>
                <div class="add-box1">
                    <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-add-custom-brand">添加</a>
                </div>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save">保存</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-return fr">返回</button>
            </div>
            <!-- 右侧内容区 end -->
        </div>
    </div>
</div>

<!--新增自定义品牌弹窗-->
<script type="text/html" id="addBrand">
    <div class="yqx-dialog" id="addBrandCheck">
        <div class="dialog-title">新增自定义品牌</div>
        <div class="dialog-con">
            <div class="form-label form-label-must">
                品牌名称：
            </div>
            <div class="form-item">
                <input type="text" name="brandName" class="yqx-input yqx-input-small js-addbrand-name" value="" placeholder="" data-v-type="required">
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save-brand-name">保存</button>
            </div>
        </div>
    </div>
</script>
<!--适用的车型模板-->
<script type="text/html" id="addCarTypeTpl">
    <div class="show-grid js-car-list">
        <div class="form-label">
            适用的车型:
        </div>
        <div class="form-item">
            <input type="text" name="carInfo" id="carModel" class="yqx-input yqx-input-icon yqx-input-small js-carSeries" value="" placeholder="请选择">
            <span class="fa icon-angle-down icon-small"></span>
            <input type="hidden" value="" name="carBrandId"/>
            <input type="hidden" value="" name="carBrandName"/>
            <input type="hidden" value="" name="carSeriesName"/>
            <input type="hidden" value="" name="carSeriesId"/>
            <input type="hidden" value="" name="carTypeName"/>
            <input type="hidden" value="" name="carTypeId"/>
        </div>
        <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-car-type">选择车型</a>
        <div class="form-label">
            车型别名:
        </div>
        <div class="form-item w-150">
            <input type="text" name="carAlias"  class="js-car-alias yqx-input yqx-input-small" value="" placeholder="车型参数/别名" data-v-type="maxLength:20" data-label="车型别名">
        </div>
        <a href="javascript:;" class="yqx-btn yqx-btn-2 yqx-btn-small js-remove-carType">删除</a>
    </div>
</script>

<!--添加自定义品牌-->
<script type="text/html" id="addCustomBrandTpl">
    <div class="show-grid rel-list">
        <div class="form-item val-small-width">
            <input type="text" name="attrName" class="yqx-input yqx-input-small" value=""
                   placeholder="自定义属性">
        </div>
        <div class="form-item">
            <input type="text" name="attrValue" class="yqx-input yqx-input-small js-select" value=""
                   placeholder="属性内容">
        </div>
        <a href="javascript:;" class="yqx-btn yqx-btn-2 yqx-btn-small js-remove-custom-brand">删除</a>
    </div>
</script>


<!-- 配件属性模板-->
<script type="text/html" id="attr_template">
    <%if(data){%>
    <%for(idx in data){%>
    <% var item = data[idx];%>
    <% if(item.attrInputType == 1) {%>
    <div class="show-grid rel-list" dynamicName="goodsAttrRelList">
        <div class="form-item val-small-width">
            <input type="text"
                   name="attrName"
                   class="yqx-input yqx-input-small"
                   placeholder="自定义属性"
                   value="<%=item.attrName%>">
        </div>
        <div class="form-item">
            <input type="text" name="attrValue"
                   dynamic_name="goodsAttrRelList"
                   class="yqx-input yqx-input-icon yqx-input-small js-add-val"
                   value=""
                   placeholder="">
            <span class="fa icon-angle-down icon-small"></span>
            <div class="yqx-select-options" style="width: 249px; display: none;">
                <%
                var attrList = item.attrValueList;
                for (var j = 0; j < attrList.length; j++){
                %>
                <dl>
                    <dd class="yqx-select-option" data-key="<%= attrList[j] %>"><%= attrList[j] %></dd>
                <dl>
                <% }%>
            </div>
        </div>
        <a class="yqx-btn yqx-btn-2 yqx-btn-small js-remove-custom-brand">删除</a>
    </div>
    <%} else{ %>
    <div class="show-grid rel-list" dynamic_name="goodsAttrRelList">
        <div class="form-item val-small-width form-label-must">
            <input name="attrName"
                   type="text"
                   value="<%=item.attrName%>"
                   class="yqx-input yqx-input-small js-attr-name"
                   placeholder="自定义属性"
                   >
        </div>
        <div class="form-item">
            <input type="text"
                   name="attrValue"
                   class="yqx-input yqx-input-small js-select"
                   placeholder="请输入属性内容..."
                   data-v-type="required">
        </div>

        <button class="yqx-btn yqx-btn-2 yqx-btn-small js-remove-custom-brand">删除</button>
    </div>
    <%}}%>
    <%}else{%>
    <div class="show-grid rel-list" dynamic_name="goodsAttrRelList">
        <div class="form-item val-small-width">
            <input name="attrName"
                   type="text"
                   value=""
                   class="yqx-input yqx-input-small"
                   placeholder="自定义属性">
        </div>
        <div class="form-item">
            <input type="text"
                   name="attrValue"
                   class="yqx-input yqx-input-small js-select"
                   placeholder="请输入属性内容...">
        </div>

        <button class="yqx-btn yqx-btn-2 yqx-btn-small js-remove-custom-brand hide">删除</button>
    </div>
    <%}%>
</script>


<!--配件类型-->
<#include "yqx/tpl/common/goods-type-tpl.ftl">
<!--车型选择-->
<#include "yqx/tpl/common/car-type-tpl.ftl">
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/goods/goods-toadd.js?24ad1ca6576b6117b3416af19571ac8b"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/out/out-common.css?10e3e17a63bb9323c82892932093d3e5"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/out/out-other.css?cc09f448fcf4c924390eeb4ea34824e4">
<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">其他出库</h3>
        </div>
        <div class="detail-form-box js-out">
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label text-height">
                        出库单：
                    </div>
                    <div class="form-item text-height">
                       ${warehouseOutSn}
                    </div>
                    <input type="hidden" name="warehouseOutSn" value="${warehouseOutSn}"/>
                </div>

                <div class="col-6">
                    <div class="form-label form-label-must">
                        出库日期：
                    </div>
                    <div class="form-item">
                        <input type="text" name="gmtCreateStr" class="yqx-input yqx-input-small l js-out-date" value="${.now?string("yyyy-MM-dd HH:mm")}" placeholder="" data-v-type="required">
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label">
                        车牌：
                    </div>
                    <div class="form-item">
                        <input type="text" name="carLicense" class="yqx-input yqx-input-small">
                    </div>
                </div>

                <div class="col-6">
                    <div class="form-label">
                        车型：
                    </div>
                    <div class="form-item">
                        <input type="text" name="carType" class="yqx-input yqx-input-small" value="" placeholder="" disabled>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label">
                        联系人：
                    </div>
                    <div class="form-item">
                        <input type="text" name="customerName" class="yqx-input yqx-input-small" value="" placeholder="">
                    </div>
                </div>

                <div class="col-6">
                    <div class="form-label">
                        联系电话：
                    </div>
                    <div class="form-item">
                        <input type="text" name="customerMobile" class="yqx-input yqx-input-small" value="" placeholder="" data-v-type="phone">
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        出库类型：
                    </div>
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-small js-out-type" value=""
                               placeholder="请选择" data-v-type="required">
                        <span class="fa icon-angle-down icon-small"></span>
                        <input type="hidden" name="outType" value="">
                    </div>
                </div>
                <div class="col-6">
                    <div class="form-label form-label-must">
                        领料人：
                    </div>
                    <div class="form-item">
                        <input type="text"  class="yqx-input yqx-input-small js-picking" value=""
                               placeholder="请选择" data-v-type="required">
                        <input type="hidden" name="goodsReceiver"/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        出库人：
                    </div>
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-small js-out-person" value="${operatorName}"
                               placeholder="请选择" data-v-type="required" disabled>
                        <input type="hidden" name="creator"/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                </div>
            </div>
        </div>

        <div class="list-box">
            <div class="list-title">
                出库配件
                <a href="javascript:;" class="fr js-batch-add-goods">＋批量添加配件</a>
            </div>
            <table class="yqx-table yqx-table-hover yqx-table-big">
                <thead>
                <tr>
                    <th class="text-l tc-1">零件号</th>
                    <th class="text-l tc-2">配件名称</th>
                    <th class="text-l tc-3">出库数量</th>
                    <th class="text-r tc-price">销售价</th>
                    <th class="text-r tc-price">成本价</th>
                    <th class="text-c tc-6">库存</th>
                    <th class="text-l tc-7">适配车型</th>
                    <th class="text-l tc-8">库位</th>
                    <th class="text-c tc-9">操作</th>
                </tr>
                </thead>
                <tbody class="goods-table">
                    <#list goodsList as goods>
                    <tr class="goods-datatr">
                        <!--零件号-->
                        <td class="text-l">
                            <div class="form-item">
                                <div class="ellipsis-1 js-show-tips">${goods.format}</div>
                                <input type="hidden" name="goodsId" value="${goods.id}"/>
                                <input type="hidden" name="goodsSn" value="<${goods.goodsSn}"/>
                            </div>
                        </td>
                        <!--配件名称-->
                        <td class="text-l">
                            <div class="form-item">
                                <div class="ellipsis-2 js-show-tips">${goods.name}</div>
                            </div>
                        </td>
                        <!--出库数量-->
                        <td class="text-l">
                            <div class="form-item goods-count has-unit">
                                <input type="text" name="goodsCount" value="1"
                                       class="yqx-input yqx-input-small text-c js-goods-num"
                                       data-v-type="required | number" maxlength="10"/>
                                <i class="fa goods-fa">${goods.measureUnit}</i>
                            </div>
                        </td>
                        <!--销售价-->
                        <td class="text-r">
                            <div class="form-item">
                                <input type="text" name="salePrice" value="${(goods.price?string("0.00"))!}" data-v-type="required | price"
                                       class="yqx-input money-small-font js-show-tips js-goods-amount" />
                            </div>
                        </td>
                        <!--成本价-->
                        <td class="text-r">
                            <div class="form-item money-small-font">
                               ${(goods.inventoryPrice?string("0.00"))!}
                                <input type="hidden" name="inventoryPrice"
                                       value="${goods.inventoryPrice}"
                                       class="hidden-input js-goods-amount js-show-tips" disabled/>
                            </div>
                        </td>
                        <!--库存-->
                        <td class="text-c">
                            <div class="form-item">${goods.stock}</div>
                        </td>
                        <!--适配车型-->
                        <td class="text-l">
                            <div class="form-item">
                                <div class="ellipsis-1 text-minor js-show-tips">${goods.carInfoStr}</div>
                            </div>
                        </td>
                        <td class="text-l">
                            <div class="form-item">
                                <div class="js-show-tips">${goods.depot}</div>

                            </div>
                        </td>
                        <td class="text-c">
                            <button class="delete-btn js-del-btn">删除</button>
                        </td>
                    </tr>
                    </#list>
                </tbody>
            </table>
            <button class="yqx-btn yqx-btn-3 js-add-goods">选择配件</button>
            <div class="remarks">
                <div class="form-label ">
                    备注
                </div>
                <div class="form-item js-comment">
                    <textarea class="yqx-textarea" name="comment" id="" cols="100" rows="1" placeholder="备注"></textarea>
                </div>
            </div>
            <div class="btn-box clearfix">
                <button class="yqx-btn yqx-btn-2 js-save">提交</button>
                <button class="yqx-btn yqx-btn-1 js-return fr">返回</button>
            </div>
        </div>
    </div>
</div>

<script type=" text/html" id="goodsTpl">
    <%if (json){%>
    <%for (var i=0;i< json.length;i++){%>
    <%var goods = json[i]%>
    <% if(goods.id){%>
    <tr class="goods-datatr">
        <#--零件号-->
        <td class="text-l">
            <div class="form-item">
                <div class="ellipsis-1 js-show-tips"><%=goods.format%></div>
                <input type="hidden" name="goodsFormat" value="<%=goods.format%>"
                       class="yqx-input yqx-input-small js-show-tips" disabled/>
                <input type="hidden" name="goodsId" value="<%=goods.id%>"/>
                <input type="hidden" name="goodsSn" value="<%=goods.goodsSn%>"/>
            </div>
        </td>
        <#--配件名称-->
        <td class="text-l">
            <div class="form-item">
                <div class="ellipsis-2 js-show-tips"><%=goods.name%></div>
                <input type="hidden" name="goodsName" value="<%=goods.name%>"
                       class="yqx-input js-show-tips" disabled/>
            </div>
        </td>
        <#--出库数量-->
        <td class="text-l">
            <div class="form-item goods-count has-unit">
                <input type="text" name="goodsCount" value="1"
                       class="yqx-input text-c js-goods-num"
                       data-v-type="required | number" maxlength="10"/>
                <i class="fa goods-fa"><%=goods.measureUnit%></i>
            </div>
        </td>
        <#--销售价-->
        <td>
            <div class="form-item">
                <input type="text" name="salePrice" value="<%=goods.price.toFixed(2)%>" data-v-type="required | price"
                       class="yqx-input hidden-input money-small-font text-r js-goods-amount" />
            </div>
        </td>
        <#--成本价-->
        <td class="text-r">
            <div class="form-item money-small-font">&yen;<%=goods.inventoryPrice.toFixed(2)%>
                <input type="hidden" name="inventoryPrice" value="<%=goods.inventoryPrice%>"
                       class="hidden-input js-goods-amount js-show-tips" disabled/>
            </div>
        </td>
        <#--库存-->
        <td class="text-c">
            <div class="form-item"><%=goods.stock%>
                <input type="hidden" name="stock" value="<%=goods.stock%>"/>
            </div>
        </td>
        <#--适配车型-->
        <td class="text-l">
            <div class="form-item">
                <div class="form-item">
                    <div class="ellipsis-1 text-minor js-show-tips"><%=goods.carInfoStr%></div>
                </div>
                <input type="hidden" name="carInfo" value="<%=goods.carInfo%>"/>
            </div>
        </td>
        <#--库位-->
        <td class="text-l">
            <div class="form-item">
                <div class="ellipsis-1 js-show-tips"><%=goods.depot%></div>
                <input type="hidden" name="depot" value="<%=goods.depot%>"/>
            </div>
        </td>
        <#--操作-->
        <td class="text-c">
            <button class="delete-btn js-del-btn">删除</button>
        </td>
    </tr>
    <%}%>
    <%}%>
    <%}%>
</script>
<!-- 车牌模版 -->
<#include "yqx/tpl/common/car-licence-tpl.ftl">
<!-- 批量添加物料模板 -->
<#include "yqx/tpl/order/batch-add-part-tpl.ftl">
<!-- 新增配件模板 -->
<#include "yqx/tpl/order/new-part-tpl.ftl">
<!-- 添加配件 -->
<#include "yqx/tpl/common/goods-add-tpl.ftl">
<!-- 配件分类模板 -->
<#include "yqx/tpl/common/goods-type-tpl.ftl">
<script src="${BASE_PATH}/static/js/page/warehouse/out/out-other.js?eddfbe320b1df6c6db26e4a1cd338249"></script>
<#include "yqx/layout/footer.ftl">

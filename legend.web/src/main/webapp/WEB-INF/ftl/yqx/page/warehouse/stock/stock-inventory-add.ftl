<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/stock/stock-common.css?2a6120c599f20dbc8877c532837dd82f"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/stock/stock-inventory-add.css?a7196c3d434f234f8632bc69358f76c2"/>
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
            <h3 class="headline fl">新建盘点单</h3>
        </div>
        <!-- 标题 end -->
        <!-- 右侧内容区 end -->
        <div class="form-box">
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        盘点编号
                    </div>
                    <div class="form-item">
                        <input type="text"
                               class="yqx-input yqx-input-small"
                               name="recordSn"
                               value="${inventorySN}"
                               disabled
                               data-v-type="required">
                    </div>
                </div>
                <div class="col-6">
                    <div class="form-label form-label-must">
                        盘点日期
                    </div>
                    <div class="form-item">
                        <input type="text"
                               class="yqx-input yqx-input-small"
                               value="${.now?string('yyyy-MM-dd HH:mm')}"
                               disabled
                               data-v-type="required">
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        盘点人
                    </div>
                    <div class="form-item">
                        <input type="text"
                               class="yqx-input yqx-input-small yqx-input-icon js-select-user"
                               placeholder="请选择"
                               name="inventoryCheckerName"
                               data-v-type="required">
                        <input type="hidden" name="inventoryCheckerId"/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                </div>
                <div class="col-6">
                    <div class="form-label form-label-must">
                        开单人
                    </div>
                    <div class="form-item">
                        <input type="text"
                               class="yqx-input yqx-input-small"
                               value="${userInfo.name}"
                               disabled>
                    </div>
                </div>
            </div>
        </div>

        <div class="accessories-box">
            <div class="accessories-title">配件项目</div>
            <div class="accessories-table">
                <table class="yqx-table yqx-table-hover yqx-table-big">
                    <thead>
                    <tr>
                        <th></th>
                        <th class="text-l tc-1">配件名称</th>
                        <th class="text-l tc-2">零件号</th>
                        <th class="text-l tc-3">实盘库存</th>
                        <th class="text-r tc-4">实盘库存金额</th>
                        <th class="tc-price">成本</th>
                        <th class="text-c tc-6">现库存</th>
                        <th class="text-r tc-price">库存金额</th>
                        <th class="text-l tc-8">库位</th>
                        <th class="text-r tc-9">备注</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="orderGoodsTB">
                    <#list selectedGoods as item>
                    <tr class="goods-datatr">
                        <td>${item_index + 1}</td>
                        <#--配件名称-->
                        <td class="text-l">
                            <div class="ellipsis-2 js-show-tips">${item.name}</div>
                        </td>
                        <#-- 零件号 -->
                        <td class="text-l">
                            <input type="hidden" name="goodsId" value="${item.id}"/>
                            <input type="hidden" name="goodsSn" value="${item.goodsSn}"/>
                            <#-- 0:基本服务-->
                            <input type="hidden" name="goodsType" value="0"/>
                            <#--零件号-->
                            <div class="ellipsis-1 js-show-tips">${item.format}</div>
                            <#--配件类别-->
                            <div class="ellipsis-1 text-minor js-show-tips">${item.catName}</div>
                        </td>
                        <#--实盘库存-->
                        <td class="text-l">
                            <div class="form-item has-unit">
                                <input type="hidden" name="measureUnit" value="${item.measureUnit}"/>
                                <input type="text" name="realStock" value="${item.stock}"
                                       data-limit_type="price"
                                       class="yqx-input js-goods-num js-float-1"/>
                                <i class="fa goods-fa">${item.measureUnit}</i>
                            </div>
                        </td>
                        <#--实盘库存金额-->
                        <td class="text-r">
                            <span class="money-small-font js-inventory-amount">${(item.inventoryPrice * item.stock)?string("###.##")}</span>
                            <input type="hidden" name="realInventoryAmount" value="${(item.inventoryPrice * item.stock)?string("###.##")}">
                        </td>
                        <#--成本-->
                        <td>
                            <div class="form-item">
                                <input type="hidden" name="inventoryPrePrice" value="${(item.inventoryPrice)?string("###.##")}"/>
                                <input type="text" name="inventoryPrice" class="yqx-input money-highlight js-show-tips js-float-2"
                                       value="${(item.inventoryPrice)?string("###.##")}"/>
                            </div>
                        </td>
                        <#--现库存-->
                        <td class="text-c">${item.stock}
                            <input type="hidden" name="stock" value="${item.stock}">
                        </td>
                        <#--现库存总金额-->
                        <td class="text-r">
                            <span class="money-small-font js-cur-inventory-amount">${(item.inventoryPrice * item.stock)?string("###.##")}</span>
                            <input type="hidden" name="currentInventoryAmount" value="${(item.inventoryPrice * item.stock)?string("###.##")}">
                        </td>
                        <#--库位-->
                        <td class="text-l">${item.depot}
                            <input type="hidden" name="depot" value="${item.depot}">
                        </td>
                        <#--备注-->
                        <td class="text-l">
                            <div class="form-item">
                                <input type="text" name="reason" value="${item.reason}"
                                       class="yqx-input js-show-tips" placeholder="备注"/>
                            </div>
                        </td>
                        <#-- 操作 -->
                        <td>
                            <a href="javascript:;" class="yqx-link yqx-link-2 js-trdel">删除</a>
                        </td>
                    </tr>

                    </#list>
                    </tbody>
                </table>
            </div>
            <div class="materials-btn">
                <button class="yqx-btn yqx-btn-3 select-server" id="goodsAddBtn">选择配件</button>
            </div>
            <div class="remarks-box">
                <div class="form-label">
                    备注
                </div>
                <div class="form-item">
                    <input class="yqx-input js-show-tips"
                           name="inventoryRemark"
                           placeholder="请填写备注信息" data-v-type="maxLength:200" style="width:730px;"  />
                </div>
            </div>
            <div class="btn-group">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-generate">生成盘点单</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-draft">保存草稿</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-return">返回</button>
            </div>
        </div>

        <!-- 右侧内容区 end -->
    </div>
</div>


<!--物料-->
<script type=" text/html" id="goodsTpl">
    <% if (json) { %>
    <% for (var i = 0; i < json.length; i++) { %>
    <% var item = json[i] %>
    <tr class="goods-datatr">
        <td><%=getSerialNumber()%></td>
        <!--配件名称-->
        <td class="text-l">
            <div class="ellipsis-2 js-show-tips"><%=item.name%>
                <input type="hidden" name="goodsName" value="<%=item.name%>">
            </div>
        </td>
        <td class="text-l">
            <input type="hidden" name="goodsId" value="<%=item.id%>"/>
            <input type="hidden" name="goodsSn" value="<%=item.goodsSn%>"/>
            <!-- 0:基本服务-->
            <input type="hidden" name="goodsType" value="0"/>
            <!--零件号-->
            <div class="ellipsis-1 js-show-tips"><%=item.format%>
                <input type="hidden" name="goodsFormat" value="<%=item.format%>">
            </div>
            <!--配件类别-->
            <div class="ellipsis-1 js-show-tips"><%=item.goodsCat%>
                <input type="hidden" name="catName" value="<%=item.goodsCat%>">
            </div>
        </td>
        <!--实盘库存-->
        <td class="text-l">
            <div class="form-item has-unit">
                <input type="hidden" name="measureUnit" value="<%=item.measureUnit%>"/>
                <input type="text" name="realStock" data-limit_type="price"
                       class="yqx-input js-goods-num js-float-1"/>
                <i class="fa goods-fa"><%=item.measureUnit%></i>
            </div>
        </td>
        <!--实盘库存金额-->
        <td class="text-r">
            <span class="money-small-font js-inventory-amount"></span>
            <input type="hidden" name="realInventoryAmount">
        </td>
        <!--成本-->
        <td class="text-r">
            <div class="form-item">
                <input type="hidden" name="inventoryPrePrice" value="<%=item.inventoryPrice.toFixed(2)%>"/>
                <input type="text" name="inventoryPrice" class="yqx-input money-highlight js-show-tips js-float-2"
                       value="<%=item.inventoryPrice.toFixed(2)%>"/>
            </div>
        </td>
        <!--现库存-->
        <td class="text-c"><%=item.stock%>
            <input type="hidden" name="stock" value="<%=item.stock%>">
        </td>
        <!--现库存总金额-->
        <td class="text-r">
            <span class="money-small-font js-cur-inventory-amount"><%=(item.inventoryPrice * item.stock).toFixed(2)%></span>
            <input type="hidden" name="currentInventoryAmount" value="<%=(item.inventoryPrice * item.stock).toFixed(2)%>">
        </td>
        <!--库位-->
        <td class="text-l">
            <div class="ellipsis js-show-tips"><%=item.depot%></div>
            <input type="hidden" name="depot" value="<%=item.depot%>">
        </td>
        <!--备注-->
        <td class="text-l">
            <div class="form-item">
                <input type="text" name="reason" value="<%=item.reason%>"
                       class="yqx-input js-show-tips" placeholder="备注"/>
            </div>
        </td>
        <td>
            <a href="javascript:;" class="yqx-link yqx-link-2 js-trdel">删除</a>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>



<!-- 添加物料模版 -->
<#include "yqx/tpl/common/add-accessories-tpl.ftl">

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/warehouse/stock/stock-inventory-add.js?60fb8dd217ca6e4b1524911aaa871792"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/sell-good.css?efe637a55fc7373ba80877a5ee29424b"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline fl">新建销售单</h1>
            <div class="order-process clearfix fr">
                <div class="order-step order-step-finish">
                    <div class="order-step-circle">1</div>
                    <p class="order-step-title">开销售单</p>
                </div>
                <div class="order-step">
                    <div class="order-step-circle">2</div>
                    <p class="order-step-title">销售单结算</p>
                </div>
            </div>
        </div>

        <div class="order-form js-sell-goods-form">
            <div class="show-grid">
                <div class="col-3">
                    <div class="form-label">
                        车牌
                    </div><div class="form-item yqx-downlist-wrap">
                        <input type="text" class="yqx-input" name="orderInfo.carLicense"
                               value="${formEntity.orderInfo.carLicense}"
                               data-v-type="maxLength:10"
                               autocomplete="off" placeholder="请输入车牌"/>
                        <input type="hidden" name="orderInfo.customerCarId"
                               value="${formEntity.orderInfo.customerCarId}"/>
                        <input type="hidden" name="orderInfo.id" value="${formEntity.orderInfo.id}" id="orderId"/>
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label form-label-must">
                        联系人
                    </div><div class="form-item">
                        <input type="text" name="orderInfo.contactName" data-label="联系人" class="yqx-input" data-v-type="required"
                               value="${formEntity.orderInfo.contactName}" title="联系人姓名" placeholder="请输入联系人" maxlength="10">
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label">
                        联系电话
                    </div><div class="form-item">
                        <input type="text" name="orderInfo.contactMobile" class="yqx-input" data-label="联系电话"
                               data-v-type="phone"
                               value="${formEntity.orderInfo.contactMobile}" title="联系电话" placeholder="请输入联系电话">
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label form-label-must">
                        服务顾问
                    </div><div class="form-item">
                        <input type="text" name="orderInfo.receiverName" data-v-type="required"
                               class="yqx-input yqx-input-icon js-select-user" data-label="服务顾问"
                               value="<#if (formEntity.orderInfo.receiverName)??>${formEntity.orderInfo.receiverName}<#else>${SESSION_USER_NAME}</#if>" placeholder="">
                        <input type="hidden" name="orderInfo.receiver" title="服务顾问"
                               value="<#if (formEntity.orderInfo.receiver)??>${formEntity.orderInfo.receiver}<#else>${SESSION_USER_ID}</#if>"/>
                        <span class="fa icon-angle-down"></span>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        开单日期
                    </div><div class="form-item form-data-width">
                        <input type="text" class="yqx-input yqx-input-icon js-createTime"
                               name="orderInfo.createTimeStr" data-label="开单日期"
                               title="开单日期"
                               data-v-type="required"
                               value="<#if (formEntity.orderInfo.createTime)??>${formEntity.orderInfo.createTime?string("yyyy-MM-dd HH:mm")}<#else>${.now?string("yyyy-MM-dd HH:mm")}</#if>"
                               placeholder="请输入开单日期"/>
                        <span class="fa icon-calendar"></span>
                    </div>
                </div>
                <div class="col-6">
                    <div class="form-label">
                        客户单位
                    </div><div class="form-item yqx-downlist-wrap form-data-width">
                        <input type="text" name="orderInfo.company" class="yqx-input js-show-tips"
                               title="客户单位" placeholder="请输入客户单位"
                               value="${formEntity.orderInfo.company}" placeholder="" maxlength="100">
                    </div>
                </div>
            </div>
        </div>

        <!-- 配件区域 start -->
        <div class="part-box">
            <div class="tabs-control">
                <span data-ref="a1" class="tab-item current-item">配件项目</span>
            </div>
            <div class="tools-bar">
                <a href="javascript:;" class="js-in-warehouse-btn"><span class="icon-download"></span>转入库</a>
                <a href="javascript:;" class="js-batch-add-part-btn"><span class="icon-layers"></span>批量添加配件</a>
                <a href="javascript:;" class="js-new-part-btn"><span class="icon-plus"></span>新建配件资料</a>
            </div>
            <table class="dynamic-table">
                <thead>
                <tr>
                    <th class="dynamic-col0"><input type="checkbox" class="checkbox_all"/></th>
                    <th class="dynamic-col1">零件号</th>
                    <th class="dynamic-col1">配件名称</th>
                    <th class="dynamic-col4">售价</th>
                    <th class="dynamic-col5">数量</th>
                    <th class="dynamic-col6">金额</th>
                    <th class="dynamic-col7">优惠金额</th>
                    <th class="dynamic-col8">库存</th>
                    <th class="dynamic-col9">销售员</th>
                    <th class="dynamic-col9">配件备注</th>
                    <th class="dynamic-col10">操作</th>
                </tr>
                </thead>
                <tbody id="dynamic-box" class="dynamic-tbody">
                <#list formEntity.orderGoodsList as orderGoods>
                <tr class="goods-datatr">
                    <td>
                        <input type="checkbox" value="${orderGoods.id}"/>
                        <input type="hidden" name="id" value="${orderGoods.id}"/>
                        <input type="hidden" name="goodsId" value="${orderGoods.goodsId}"/>
                        <input type="hidden" name="goodsSn" value="${orderGoods.goodsSn}"/>
                        <input type="hidden" name="goodsType" value="${orderGoods.goodsType}" clear="false"/>
                    </td>
                    <td>
                        <!-- 零件号 -->
                        <div class="form-item">
                            <input type="text" class="yqx-input yqx-input-small js-show-tips" name="goodsFormat"
                                   value="${orderGoods.goodsFormat}" placeholder="" disabled/>
                        </div>
                    </td>
                    <td>
                        <!-- 配件名称 -->
                        <div class="form-item">
                            <input type="text" class="yqx-input yqx-input-small js-show-tips" value="${orderGoods.goodsName}"
                                   name="goodsName" placeholder="" disabled/>
                        </div>
                    </td>
                    <td>
                        <!-- 售价 -->
                        <div class="form-item">
                            <input type="text" class="yqx-input yqx-input-small js-float-2 js-show-tips" value="${orderGoods.goodsPrice}"
                                   name="goodsPrice" placeholder=""/>
                            <input type="hidden" name="measureUnit" value="${orderGoods.measureUnit}"/>
                            <input type="hidden" name="inventoryPrice" value="${orderGoods.inventoryPrice}"/>
                        </div>
                    </td>
                    <td>
                        <!-- 数量 -->
                        <div class="form-item">
                            <input type="text" class="yqx-input yqx-input-small js-float-1 js-show-tips" value="${orderGoods.goodsNumber}"
                                   name="goodsNumber" placeholder=""/>
                        </div>
                    </td>
                    <td>
                        <!-- 金额 -->
                        <div class="form-item">
                            <input type="text" class="yqx-input yqx-input-small js-goods-amount" value="${orderGoods.goodsAmount}"
                                   name="goodsAmount" placeholder="" disabled/>
                        </div>
                    </td>
                    <td>
                        <!-- 优惠金额 -->
                        <div class="form-item">
                            <input type="text" class="yqx-input yqx-input-small js-float-2" value="${orderGoods.discount}"
                                   data-v-type="number|compareGoods"
                                   name="discount" placeholder=""/>
                        </div>
                    </td>
                    <td>
                        <!-- 库存 -->
                        <div class="form-item">
                            <input type="text" class="yqx-input yqx-input-small js-show-tips" value="${orderGoods.stock}"
                                   name="stock" placeholder="" disabled/>
                        </div>
                    </td>
                    <td>
                        <!-- 销售员 -->
                        <div class="form-item">
                            <input type="text" value="${orderGoods.saleName}" class="yqx-input yqx-input-icon js-speedily-sale yqx-input-small"/>
                            <input type="hidden" name="saleId" value="${orderGoods.saleId}"/>
                            <span class="fa icon-angle-down"></span>
                        </div>
                    </td>
                    <!--配件备注-->
                    <td>
                        <div class="form-item">
                            <input type="text" name="goodsNote" value="${orderGoods.goodsNote}"
                                   class="yqx-input yqx-input-small js-show-tips" maxlength="100"/>
                        </div>
                    </td>
                    <td class="dynamic-del-btn">
                        <div class="form-item">
                            <a href="javascript:;" class="js-dynamic-del-btn">删除</a>
                        </div>
                    </td>
                </tr>
                </#list>

                </tbody>
            </table>
            <div>
                <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-dynamic-add-btn">选择配件</a>
            </div>
            <div class="commit-box">
                <div class="form-label form-label-remark">
                    备注
                </div><div class="form-item form-item-remark">
                    <input class="yqx-input js-show-tips" name="orderInfo.postscript" data-v-type="maxLength:200"
                           value="${formEntity.orderInfo.postscript}" placeholder="请输入备注">
                </div>
            </div>
            <div class="order-count">
                <p>配件费用：<span id="part_count">0.00</span>元</p>
                <p>总计：<span id="order_count">0.00</span>元</p>
            </div>
            <div class="btn-group">
            <#assign saveBtnName ="开单">
            <#if formEntity.orderInfo.id gt 0>
                <#assign saveBtnName ="编辑保存">
            </#if>
            <#if formEntity.orderInfo.id ==null || formEntity.orderInfo.orderStatus=='CJDD' >
                <a href="javascript:void(0);" class="yqx-btn yqx-btn-2 js-create-sell">${saveBtnName}</a>
            </#if>
            <#if formEntity.orderInfo.id ==null || formEntity.orderInfo.orderStatus=='CJDD' >
                <a href="javascript:;" class="yqx-btn yqx-btn-1 js-settle">收款</a>
            </#if>
                <button class="yqx-btn yqx-btn-1 js-return fr">返回</button>
            </div>
        </div>
        <!-- 配件区域 end -->
    </div>
    <!-- 右侧内容区 end -->
</div>
<script type="text/html" id="partTpl">
    <tr class="goods-datatr">
        <td>
            <input type="checkbox" value="<%=json.id%>"/>
            <input type="hidden" name="goodsId" value="<%=json.id%>"/>
            <input type="hidden" name="goodsSn" value="<%=json.goodsSn%>"/>
        </td>
        <td>
            <!-- 零件号 -->
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-small" name="goodsFormat" value="<%=json.format%>" disabled/>
            </div>
        </td>
        <td>
            <!-- 配件名称 -->
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-small js-show-tips" value="<%=json.name%>" name="goodsName" disabled/>
            </div>
        </td>
        <td>
            <!-- 售价 -->
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-small" value="<%=json.price%>" name="goodsPrice"/>
                <input type="hidden" name="inventoryPrice" value="<%=json.inventoryPrice%>"/>
            </div>
        </td>
        <td>
            <!-- 数量 -->
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-small js-show-tips"  value="1" name="goodsNumber"/>
            </div>
        </td>
        <td>
            <!-- 金额 -->
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-small js-show-tips js-goods-amount" value="<%=json.price%>" name="goodsAmount" disabled/>
            </div>
        </td>
        <td>
            <!-- 优惠金额 -->
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-small"
                       data-v-type="number|compareGoods"
                       value="0" name="discount"/>
            </div>
        </td>
        <td>
            <!-- 库存 -->
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-small js-show-tips" value="<%=json.stock%>" name="stock" disabled/>
            </div>
        </td>
        <td>
            <!-- 销售员 -->
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-icon js-speedily-sale yqx-input-small"/>
                <input type="hidden" name="saleId" value=""/>
                <span class="fa icon-small icon-angle-down"></span>
            </div>
        </td>
        <!--配件备注-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsNote" class="yqx-input yqx-input-small js-show-tips" maxlength="100"/>
            </div>
        </td>
        <td class="dynamic-del-btn">
            <div class="form-item">
                <a href="javascript:;" class="js-dynamic-del-btn">删除</a>
            </div>
        </td>
    </tr>
</script>

<!-- 模板区域 start -->
<!-- 转入库模板 -->
<#include "yqx/tpl/order/in-warehouse-tpl.ftl">
<!-- 批量添加物料模板 -->
<#include "yqx/tpl/order/batch-add-part-tpl.ftl">
<!-- 新增配件模板 -->
<#include "yqx/tpl/order/new-part-tpl.ftl">
<!-- 配件分类模板 -->
<#include "yqx/tpl/common/goods-type-tpl.ftl">
<!-- 配件添加弹框 -->
<#include "yqx/tpl/common/goods-add-tpl.ftl">
<!-- 车牌下拉框 -->
<#include "yqx/tpl/common/car-licence-tpl.ftl">
<!-- 模板区域 end -->

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/order/sell-good.js?1d0b1ab0b75aaf4578077d6af3f81a42"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
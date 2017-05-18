<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/out/out-common.css?10e3e17a63bb9323c82892932093d3e5">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/in/in-red.css?9f47a3c41ee9e9bb39c006f53094168b">
<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-list-title clearfix">
            <h3 class="headline fl">入库退货</h3>
        </div>
        <form class="main in-red">
            <div class="show-grid">
                <div class="form-item">
                        <input type="hidden" name="relId" value="${warehouseInVo.id}" />
                        <input type="hidden" name="relSn" value="${warehouseInVo.warehouseInSn}" />
                    <label>  退货单(红字）：
                        <input type="hidden" name="warehouseInSn" value=" ${warehouseInSn}"/>
                    </label>
                  <i class="order-num red-num weight">${warehouseInSn}</i>
                </div><div class="fr other-order">
                    <label>关联入库单（蓝字）：</label><i class="blue-num weight">${warehouseInVo.warehouseInSn}</i>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item">
                    <label class="must">退货日期</label><div class="form-item">
                        <input class="yqx-input js-in-date"
                               data-v-type="required"
                               name="inTimeStr" value="${.now?string("yyyy-MM-dd HH:mm")}">
                        <i class="fa icon-calendar"></i>
                    </div>
                </div><div class="form-item">
                    <label class="must">　供应商</label><div class="form-item">
                        <input class="yqx-input js-supplier"  value="${warehouseInVo.supplierName}" disabled>
                        <input type="hidden"
                               data-v-type="required"
                               name="supplierId" value="${warehouseInVo.supplierId}"/>
                        <input type="hidden" name="supplierName" value="${warehouseInVo.supplierName}"/>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item">
                    <label for="">　联系人</label><input type="text" name="contact" class="yqx-input" value="${warehouseInVo.contact}">
                </div><div class="form-item">
                    <label>联系电话</label><input class="yqx-input" name="contactMobile" value="${warehouseInVo.contactMobile}">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item pay-mode"><label>付款方式</label><input class="yqx-input js-pay-method" value="${warehouseInVo.paymentMode}"  disabled>
                    <input type="hidden" name="paymentMode" value="${warehouseInVo.paymentMode}"/>
                <span class="fa icon-angle-down"></span>
                </div><div class="form-item">
                    <label>发票类型</label><div class="form-item">
                        <input class="yqx-input js-invoice-type" name="invoiceTypeName" value="${warehouseInVo.invoiceTypeName}" >
                        <i class="fa icon-angle-down"></i>
                        <input type="hidden" name="invoiceType" value="${warehouseInVo.invoiceType}"/>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item">
                <label class="must">　采购人</label><div class="form-item">
                <input type="text"
                       class="yqx-input js-agent" value="${warehouseInVo.purchaseAgentName}">
                <i class="fa icon-angle-down"></i>
                <input type="hidden"
                       data-v-type="required"
                       name="purchaseAgent" value="${warehouseInVo.purchaseAgent}"/>
                <input type="hidden" name="purchaseAgentName" value="${warehouseInVo.purchaseAgentName}"/>
                    </div>
                </div><div class="form-item">
                    <label>　开单人</label><div class="form-item operator-name">
                        ${operatorName}
                    </div>
                </div>
            </div>
        </form>
        <div class="list-box">
            <div class="nav clearfix">
                <h3 class="fl">退货配件</h3>
            </div>
            <table class="yqx-table goods-table yqx-table-hover yqx-table-big">
                <thead>
                    <tr>
                        <th></th>
                        <th class="text-l tc-1">零件号</th>
                        <th class="text-l tc-2">配件名称</th>
                        <th class="tc-3">退货数量</th>
                        <th class="text-r tc-price">单价</th>
                        <th class="text-r tc-price">总金额</th>
                        <th class="tc-5">库存</th>
                        <th class="text-l tc-6">适配车型</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <#list warehouseInVo.detailList as detail>
                <tr class="goods-datatr">
                    <td>${detail_index + 1}</td>
                    <!--零件号-->
                    <td class="text-l">
                        <div class="form-item ellipsis-1 money-highlight js-show-tips">${detail.goodsFormat}
                            <input type="hidden" name="goodsFormat" value="${detail.goodsFormat}"
                                   class="yqx-input yqx-input-small" disabled/>
                            <input type="hidden" name="goodsId" value="${detail.goodsId}"/>
                            <input type="hidden" name="id" value="${detail.id}"/>
                            <input type="hidden" name="goodsSn" value="${detail.goodsSn}"/>
                        </div>
                    </td>
                    <!--配件名称-->
                    <td class="text-l">
                        <div class="form-item ellipsis-2 js-show-tips">${detail.goodsName}
                            <input type="hidden" name="goodsName" value="${detail.goodsName}"/>
                        </div>
                    </td>
                     <!--退货数量-->
                    <td>
                        <div class="form-item goods-count has-unit">
                            <input type="text" name="goodsCount" value="-${detail.goodsCount}"
                                   class="yqx-input text-c js-goods-num" data-v-type="required | negativeNumber" maxlength="10"/>
                            <i class="fa goods-fa">${detail.measureUnit}</i>
                        </div>
                    </td>
                    <!--入库单价-->
                    <td class="text-r">
                        <div class="form-item">
                            <i class="money-small-font">&yen;${(detail.purchasePrice?string("0.00"))!}</i>
                            <input type="hidden" name="purchasePrice" value="${(detail.purchasePrice?string("0.00"))!}"
                                   class="js-goods-price js-show-tips hidden-input"
                                   data-v-type="required | price" maxlength="10"/>
                        </div>
                    </td>
                    <!--金额-->
                    <td class="text-r">
                        <div class="form-item ellipsis-1 js-show-tips">
                            <i class="money-small-font">&yen;<i class=" js-goods-amount"><#if detail.purchaseAmount != 0>-</#if>${(detail.purchaseAmount?string("0.00"))!}</i></i>
                            <input type="hidden" name="purchaseAmount" class="js-goods-amount"
                                   value="<#if detail.purchaseAmount != 0>-</#if>${(detail.purchaseAmount?string("0.00"))!}"/>
                        </div>
                    </td>
                    <!--库存-->
                    <td>${detail.stock} <span class="text-minor">${detail.measureUnit}</span>
                        <input type="hidden" name="stock" value="${detail.stock}"/>
                    </td>
                    <!--适配车型-->
                    <td class="text-l">
                        <div class="form-item text-minor ellipsis-1 js-show-tips">${detail.carInfoStr}
                            <input type="hidden" value="${detail.carInfo}" name="carInfo">
                        </div>
                    </td>
                    <td>
                        <a href="javascript:;" class="yqx-link yqx-link-2 js-del-btn">删除</a>
                    </td>
                </tr>

                </#list>
            </table>
            <div class="mark-box">
                <div class="show-grid remark-info">
                <div class="form-item">
                    <label>备注</label><input class="yqx-input" name="comment"
                        placeholder="请填写备注信息"/>
                </div>
                </div>
                <div class="show-grid money-count">
                    <div class="form-item">
                        <label>配件合计金额</label><div class="form-item inline-block">
                        <input disabled class="yqx-input js-goods-total money-highlight" name="goodsAmount" data-v-type="price" value="<#if warehouseInVo.goodsAmount != 0>-</#if>${(warehouseInVo.goodsAmount?string("0.00"))!}">
                        </div>
                    </div><i class="fa icon-plus"></i><div class="form-item">
                        <label>税费</label><div class="form-item inline-block">
                            <input class="yqx-input js-money-plus money-highlight"
                            data-v-type="negativeNumber|floating|negPrice"
                            name="tax"
                            value="<#if warehouseInVo.tax != 0>-</#if>${(warehouseInVo.tax?string("0.00"))!}">
                        </div>
                    </div><i class="fa icon-plus"></i><div class="form-item">
                        <label>运费</label><div class="form-item inline-block">
                            <input class="yqx-input js-money-plus money-highlight"
                            data-v-type="negativeNumber|floating|negPrice"
                            name="freight"
                            value="<#if warehouseInVo.freight != 0>-</#if>${(warehouseInVo.freight?string("0.00"))!}">
                        </div>
                    </div>
                </div>
                <div class="money-total">
                    总计：<i class="money-font">&yen;</i><i class="money-font money-text"><#if warehouseInVo.totalAmount != 0>-</#if>${(warehouseInVo.totalAmount?string("0.00"))!}</i>
                    <input type="hidden" name="totalAmount" value="<#if warehouseInVo.totalAmount != 0>-</#if>${(warehouseInVo.totalAmount?string("0.00"))}"/>
                </div>
            </div>
            <div class="form-footer btn-group">
                <div class="fl">
                           <button class="yqx-btn yqx-btn-2 js-stock-refund" data-id="${warehouseInVo.id}">退货</button>
                </div>
                <div class="fr">
                    <button class="yqx-btn yqx-btn-1 js-back">返回</button>
                </div>
            </div>
        </div>
    </div>
</div>


<script src="${BASE_PATH}/static/js/page/warehouse/in/in-red.js?12e8c8cdf9ff01d01fdf855950710da7"></script>
<#include "yqx/layout/footer.ftl">

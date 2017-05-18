<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/out/out-common.css?10e3e17a63bb9323c82892932093d3e5"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/out/order-quote-detail.css?0ea70dfd6962b1e4da0be02642b431f5">
<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <input type="hidden" id="refer" value="${refer}"/>
            <h3 class="headline">
            <#if refer ="outbound">
                工单详情
            </#if>
            <#if refer ="quote">
                工单报价
            </#if>
            </h3>
        </div>
        <div class="detail-form-box base-box">
            <!--印章-->
        <#if orderInfo.orderStatus == "CJDD">
            <div class="seal"></div>
        <#elseif orderInfo.orderStatus == "DDBJ">
            <div class="seal quoted"></div>
        <#elseif orderInfo.orderStatus == "FPDD" || orderInfo.orderStatus == "DDSG">
            <div class="seal dispatching"></div>
        <#elseif orderInfo.orderStatus == "DDWC">
            <div class="seal finished"></div>
        <#elseif orderInfo.orderStatus == 'DDYFK' && orderInfo.payStatus == 1>
            <div class="seal sign"></div>
        <#elseif orderInfo.orderStatus == 'DDYFK' && orderInfo.payStatus == 2>
            <div class="seal settlement"></div>
        <#elseif orderInfo.orderStatus == "WXDD">
            <div class="seal invalid"></div>
        </#if>
            <div class="show-grid">
                <div class="form-label">
                    工单编号：
                </div>
                <div class="form-item item-width">
                    <div class="yqx-text">
                    ${orderInfo.orderSn}(开单时间:${orderInfo.gmtCreateStr})
                    </div>
                    <input type="hidden" id="shopType" name="shopType" value="${shopType}"/>
                    <input type="hidden" name="orderInfo.id" value="${orderInfo.id}"/>
                    <input type="hidden" name="orderInfo.orderSn" value="${orderInfo.orderSn}"/>
                    <input type="hidden" name="orderInfo.customerCarId" value="${orderInfo.customerCarId}"/>
                    <input type="hidden" name="orderInfo.orderStatus" value="${orderInfo.orderStatus}"/>
                    <input type="hidden" name="orderInfo.gmtModifiedYMDHMS" value="${orderInfo.gmtModifiedYMDHMS}"/>
                    <input type="hidden" name="orderInfo.payStatus" value="${orderInfo.payStatus}"/>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label">
                        车牌：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text max-width js-show-tips">
                        ${orderInfo.carLicense}
                            <input type="hidden" name="orderInfo.carLicense" value="${orderInfo.carLicense}"/>
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        车型：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text max-width js-show-tips">
                        ${orderInfo.carInfo} ${orderInfo.carAlias}
                            <input type="hidden" name="orderInfo.carModel" value="${orderInfo.carInfo}"/>
                            <input type="hidden" name="orderInfo.carAlias" value="${orderInfo.carAlias}"/>
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        VIN码：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text max-width js-show-tips">
                        ${orderInfo.vin}
                            <input type="hidden" name="orderInfo.vin" value="${orderInfo.vin}"/>
                        </div>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label">
                        联系人：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text max-width js-show-tips">
                        ${orderInfo.contactName}
                            <input type="hidden" name="orderInfo.contactName" value="${orderInfo.contactName}"/>
                        </div>
                    </div>
                </div>

                <div class="col-4">
                    <div class="form-label">
                        联系电话：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text max-width js-show-tips">
                        ${orderInfo.contactMobile}
                            <input type="hidden" name="orderInfo.contactMobile" value="${orderInfo.contactMobile}"/>
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label">
                        服务顾问：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text max-width js-show-tips">
                        ${orderInfo.receiverName}
                            <input type="hidden" name="orderInfo.receiver" value="${orderInfo.receiver}"/>
                            <input type="hidden" name="orderInfo.receiverName" value="${orderInfo.receiverName}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 服务列表-->
        <div class="list-box">
            <div class="list-title">
                服务项目
            </div>
            <table class="yqx-table yqx-table-hidden yqx-table-hover service-table">
                <thead>
                <tr>
                    <th class="tc-1">服务编码</th>
                    <th class="tc-2">服务名称</th>
                    <th class="tc-3">服务类别</th>
                    <th class="tc-4">工时</th>
                    <th class="tc-5">维修工</th>
                    <th>服务备注</th>
                </tr>
                </thead>
                <tbody id="orderServiceTB">
                <#list orderServicesList as item>
                <tr class="service-datatr" data-id="${item.id}">
                    <td>${item.serviceSn}
                        <input type="hidden" name="id" value="${item.id}"/>
                        <input type="hidden" name="serviceId" value="${item.serviceId}"/>
                        <input type="hidden" name="serviceSn" value="${item.serviceSn}"/>
                        <input type="hidden" name="parentServiceId" value="${item.parentServiceId}"/>
                        <input  type="hidden" name="flags" value="${item.flags}"/>
                        <input type="hidden" name="type" value="1"/>
                        <input type="hidden" name="serviceCatId" value="${item.serviceCatId}"/>
                        <input type="hidden" name="discount" value="${item.discount}"/>
                        <input type="hidden" name="serviceAmount" value="${item.serviceAmount}"/>
                    </td>
                    <td>
                        <div class="ellipsis-1 js-show-tips">${item.serviceName}</div>
                        <input type="hidden" name="serviceName" value="${item.serviceName}"/>
                    </td>
                    <td>${item.serviceCatName}
                        <input type="hidden" name="serviceCatName" value="${item.serviceCatName}"/>
                    </td>
                    <td>${item.serviceHour}
                        <input type="hidden" name="serviceHour" value="${item.serviceHour}"/>
                    </td>
                    <td>${item.workerName}
                        <input type="hidden" name="workerNames" value="${item.workerNames}"/>
                        <input type="hidden" name="workerIds" value="${item.workerIds}"/>
                    </td>
                    <td>
                        <div class="ellipsis-1 js-show-tips">${item.serviceNote}</div>
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>

        <!-- 配件列表-->
        <div class="list-box">
                <div class="list-title">
                    配件项目
                    <a href="javascript:;" class="fr js-new-part-btn">＋新建配件资料</a>
                    <span class="fr">|</span>
                    <a href="javascript:;" class="fr js-batch-add-goods">＋批量添加配件</a>
                    <span class="fr">|</span>
                    <a href="javascript:;" class="fr js-in-warehouse"><span class="icon-download"></span>转入库</a>
                </div>
                <table class="yqx-table yqx-table-hover">
                    <thead>
                    <tr>
                        <th class="tc-checkbox">
                            <input type="checkbox" class="js-checkbox-all"/>
                        </th>
                        <th class="text-l tc-2">零件号</th>
                        <th class="text-l tc-3">配件名称</th>
                        <th class="tc-price">售价</th>
                        <th class="tc-5">数量</th>
                        <th class="text-r tc-price">金额</th>
                        <th class="text-r tc-price">优惠</th>
                        <th class="tc-8">库存</th>
                        <th class="tc-9">配件备注</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody class="goods-table" id="orderGoodsTB">
                    <#list orderGoodList as orderGoods>
                    <tr class="goods-datatr">
                        <td class="speedily-checkbox">
                        <#if orderGoods.stock < orderGoods.goodsNumber>
                            <input type="checkbox" class="js-repair-checkbox" checked="checked"/>
                        <#else>
                            <input type="checkbox" class="js-repair-checkbox" disabled/>
                        </#if>

                        </td>
                        <!--零件号-->
                        <td class="text-l">
                            <input type="hidden" name="id" value="${orderGoods.id}"/>
                            <input type="hidden" name="goodsId" value="${orderGoods.goodsId}"/>
                            <input type="hidden" name="goodsSn" value="${orderGoods.goodsSn}"/>
                            <input type="hidden" name="goodsType" value="${orderGoods.goodsType}"/>
                            <input type="hidden" name="outNumber" value="${orderGoods.outNumber}"/>
                            <div class="ellipsis-1 js-show-tips">${orderGoods.goodsFormat}
                                <input type="hidden" name="goodsFormat" value="${orderGoods.goodsFormat}">
                            </div>
                        </td>
                        <!--配件名称-->
                        <td class="text-l">
                            <div class="ellipsis-2 js-show-tips">${orderGoods.goodsName}
                                <input type="hidden" name="goodsName" value="${orderGoods.goodsName}">
                            </div>
                        </td>
                        <!--售价-->
                        <td>
                            <div class="form-item">
                                <input type="text" name="goodsPrice" value="${(orderGoods.goodsPrice?string("0.00"))!}"
                                       class="yqx-input money-small-font js-goods-price js-float-2 js-show-tips"/>
                            </div>
                        </td>
                        <!--数量-->
                        <td>
                            <input type="hidden" name="measureUnit" value="${orderGoods.measureUnit}"/>
                            <input type="hidden" name="inventoryPrice" value="${orderGoods.inventoryPrice}"/>
                            <div class="form-item has-unit">
                                <input type="text" name="goodsNumber" value="${orderGoods.goodsNumber}"
                                       class="yqx-input text-c js-goods-num js-float-1" placeholder="单位"/>
                                <i class="fa goods-fa">${orderGoods.measureUnit}</i>
                            </div>
                        </td>
                        <!--金额-->
                        <td class="text-r">
                            <span class="money-small-font js-goods-amount">${(orderGoods.goodsAmount?string("0.00"))!}</span>
                            <input type="hidden" name="goodsAmount" value="${orderGoods.goodsAmount}">
                        </td>
                        <!--优惠-->
                        <td>
                            <div class="form-item">
                                <input type="text" name="discount" value="${(orderGoods.discount?string("0.00"))!}"
                                       class="yqx-input money-small-font js-goods-discount js-float-2"/>
                            </div>
                        </td>
                        <!--库存-->
                        <td>${orderGoods.stock}
                            <input type="hidden" name="stock" value="${orderGoods.stock}">
                        </td>
                        <!--配件备注-->
                        <td>
                            <div class="form-item">
                                <input type="text" name="goodsNote" value="${orderGoods.goodsNote}"
                                       class="yqx-input js-show-tips" placeholder="备注"/>
                            </div>
                        </td>
                        <td>
                            <a href="javascript:;" class="yqx-link yqx-link-2 js-trdel">删除</a>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
                <button class="yqx-btn yqx-btn-3 add-goods js-add-goods">选择配件</button>

            <div class="remarks">
                <div class="form-label">
                    备注
                </div>
                <div class="form-item">
                    <input class="yqx-input js-show-tips" name="orderInfo.postscript" placeholder="请填写备注信息"
                           data-v-type="maxLength:200" value="${orderInfo.postscript}"/>
                </div>
            </div>
            <div class="total">
                配件合计金额:<span class="money-font">&yen;<span id="goodsTotalAmount"></span></span> - 配件优惠:<span class="money-font">&yen;<span id="goodsTotalDiscount"></span></span>
            </div>
            <div class="total">
                总计：<span class="money-font">&yen;</span><span class="money-font" id="goodsActualAmount"></span>
            </div>
            <div class="btn-box clearfix">

                <!--出库单.增加配件页面 -->
            <#if refer ="outbound">
                <button class="yqx-btn yqx-btn-2 js-save">保存</button>
                <button class="yqx-btn yqx-btn-1 js-return fr">返回</button>
            </#if>

            <!--出库单.报价页面 -->
            <#if refer ="quote">
                <#if orderInfo.orderStatus == "CJDD">
                    <button class="yqx-btn yqx-btn-2 js-quote">
                        提交报价
                    </button>
                </#if>
                <#if orderInfo.orderStatus == "DDBJ">
                    <button class="yqx-btn yqx-btn-2 js-quote">
                        重新报价
                    </button>
                </#if>
                <#if orderInfo.orderStatus != 'DDWC' && orderInfo.orderStatus != 'DDYFK'>
                    <button class="yqx-btn yqx-btn-1 js-save">保存</button>
                </#if>
                <button class="yqx-btn yqx-btn-1 js-print">打印</button>
                <button class="yqx-btn yqx-btn-1 js-return fr">返回</button>
            </#if>
            </div>
        </div>

    </div>
</div>

<!--物料-->
<script type=" text/html" id="goodsTpl">
    <%if (json) {%>
    <%for (var i = 0; i < json.length; i++) {%>
    <%var orderGoods = json[i]%>
    <tr class="goods-datatr">
        <td class="speedily-checkbox">
            <%orderGoods.goodsNum = orderGoods.goodsNum || 1;%>
        <%if(orderGoods.stock < orderGoods.goodsNum) {%>
            <input type="checkbox" class="js-repair-checkbox" checked="checked"/>
        <%} else {%>
            <input type="checkbox" class="js-repair-checkbox" disabled/>
        <%}%>

        </td>
        <!--零件号-->
        <td class="text-l">
            <input type="hidden" name="goodsId" value="<%=orderGoods.id%>"/>
            <input type="hidden" name="goodsSn" value="<%=orderGoods.goodsSn%>"/>
            <input type="hidden" name="inventoryPrice" value="<%=orderGoods.inventoryPrice%>"/>
            <!-- 0:基本服务-->
            <input type="hidden" name="goodsType" value="0"/>
            <div class="ellipsis-1"><%=orderGoods.format%>
                <input type="hidden" name="goodsFormat" value="<%=orderGoods.format%>">
            </div>
        </td>
        <!--配件名称-->
        <td class="text-l">
            <div class="ellipsis-2 js-show-tips"><%=orderGoods.name%></div>
            <input type="hidden" name="goodsName" value="<%=orderGoods.name%>">
        </td>
        <!--售价-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsPrice" value="<%=orderGoods.price.toFixed(2)%>"
                       class="yqx-input money-small-font js-goods-price js-float-2 js-show-tips"/>
            </div>
        </td>
        <!--数量-->
        <td>
            <div class="form-item has-unit">
                <input type="text" name="goodsNumber" value="<%=orderGoods.goodsNum%>"
                       class="yqx-input js-goods-num text-c js-float-1" placeholder="单位"/>
                <i class="fa goods-fa"><%=orderGoods.measureUnit%></i>
                <input type="hidden" name="measureUnit" value="<%=orderGoods.measureUnit%>"/>
            </div>
        </td>
        <!--金额-->
        <td class="text-r">
            <span class="money-small-font js-goods-amount"><%= (orderGoods.price * (orderGoods.goodsNum || 1)).toFixed(2) %></span>
            <input type="hidden" name="goodsAmount" value="<%=(orderGoods.price * (orderGoods.goodsNum || 1))%>">
        </td>
        <!--优惠-->
        <td>
            <div class="form-item">
                <input type="text" name="discount" value="0.00"
                       class="yqx-input money-small-font js-goods-discount js-float-2"/>
            </div>
        </td>
        <!--库存-->
        <td><%=orderGoods.stock%>
            <input type="hidden" name="stock" value="<%=orderGoods.stock%>">
        </td>
        <!--配件备注-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsNote" value="<%=orderGoods.goodsNote%>"
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


<!-- 转入库模板 -->
<#include "yqx/tpl/order/in-warehouse-tpl.ftl">
<!-- 批量添加物料模板 -->
<#include "yqx/tpl/order/batch-add-part-tpl.ftl">
<!-- 新增配件模板 -->
<#include "yqx/tpl/order/new-part-tpl.ftl">
<!-- 添加配件 -->
<#include "yqx/tpl/common/goods-add-tpl.ftl">
<!-- 配件分类模板 -->
<#include "yqx/tpl/common/goods-type-tpl.ftl">
<script src="${BASE_PATH}/static/js/page/warehouse/out/order-quote-detail.js?005ec9e38a3e9a20ffc691f562f6c082"></script>
<#include "yqx/layout/footer.ftl">

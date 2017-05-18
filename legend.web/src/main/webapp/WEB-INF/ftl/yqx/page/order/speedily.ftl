<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/speedily.css?b1f347d6a240f5d60c9ba0bef59c801a"/>
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
            <h1 class="headline fl"><#if orderInfo.id>编辑<#else>新建</#if>快修快保单</h1>
            <div class="order-process clearfix fr">
                <div class="order-step order-step-finish">
                    <div class="order-step-circle">1</div>
                    <p class="order-step-title">开快修快保单</p>
                </div>
                <div class="order-step">
                    <div class="order-step-circle">2</div>
                    <p class="order-step-title">快修快保单结算</p>
                </div>
            </div>
        </div>
        <div class="order-form mb10 base-box">
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        车牌
                    </div><div class="form-item yqx-downlist-wrap form-data-width">
                        <input type="text" class="yqx-input" name="orderInfo.carLicense"
                               value="<#if orderInfo.carLicense>${orderInfo.carLicense}<#else>${license}</#if>"
                               data-v-type="required | licence"
                               autocomplete="off" placeholder="请输入车牌"/>
                        <input type="hidden" name="orderInfo.customerCarId" value="${orderInfo.customerCarId}"/>
                        <input type="hidden" name="orderInfo.id" value="${orderInfo.id}"/>
                        <input type="hidden" name="orderInfo.orderSn" value="${orderInfo.orderSn}"/>
                        <input type="hidden" name="orderInfo.appointId" value="${orderInfo.appointId}"/>
                        <input type="hidden" name="orderInfo.downPayment" value="${orderInfo.downPayment}"/>
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label">
                        联系人
                    </div><div class="form-item">
                        <input type="text" name="orderInfo.contactName" class="yqx-input"
                               value="${orderInfo.contactName}" data-v-type="maxLength:20" maxlength="20"
                               autocomplete="off" placeholder="请输入联系人"/>
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label">
                        联系电话
                    </div><div class="form-item">
                        <input type="text" name="orderInfo.contactMobile" class="yqx-input" autocomplete="off"
                               data-v-type="phone"
                               value="${orderInfo.contactMobile}" placeholder="请输入联系电话"/>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label">
                        车型
                    </div><div class="form-item form-data-width">
                        <input type="text" name="carModeBak" class="js-show-tips yqx-input" disabled="disabled"
                               value="${orderInfo.carInfo}" autocomplete="off"/>
                        <!-- 品牌 -->
                        <input type="hidden" name="orderInfo.carBrandId" value="${orderInfo.carBrandId}"/>
                        <input type="hidden" name="orderInfo.carBrand" value="${orderInfo.carBrand}"/>
                        <!-- 车系 -->
                        <input type="hidden" name="orderInfo.carSeriesId" value="${orderInfo.carSeriesId}"/>
                        <input type="hidden" name="orderInfo.carSeries" value="${orderInfo.carSeries}"/>
                        <!-- 车辆型号 -->
                        <input type="hidden" name="orderInfo.carModelsId" value="${orderInfo.carModelsId}"/>
                        <input type="hidden" name="orderInfo.carModels" value="${orderInfo.carModels}"/>
                        <!-- 进口与国产 -->
                        <input type="hidden" name="orderInfo.importInfo" value="${orderInfo.importInfo}"/>
                    </div>
                </div>
                <div class="col-6">
                    <div class="form-label form-label-must">
                        开单日期
                    </div><div class="form-item form-data-width">
                        <input type="text" name="orderInfo.createTimeStr" class="yqx-input js-date" data-v-type="required"
                               data-label="开单日期"
                               value="<#if (orderInfo.createTime)??>${orderInfo.createTime?string("yyyy-MM-dd HH:mm")}<#else>${.now?string("yyyy-MM-dd HH:mm")}</#if>"
                               placeholder="请输入开单日期"/>
                        <span class="fa icon-calendar"></span>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-3">
                    <div class="form-label">
                        行驶里程
                    </div><div class="form-item">
                        <input type="text" name="orderInfo.mileage" class="yqx-input js-number" autocomplete="off"
                               value="${orderInfo.mileage}" data-v-type="integer|maxLength:8" maxlength="8" placeholder=""/>
                        <span class="fa">km</span>
                    </div>
                </div><div class="col-3">
                    <div class="form-label">
                        下次保养里程
                    </div><div class="form-item">
                        <input type="text" name="orderInfo.upkeepMileage" class="yqx-input js-number" autocomplete="off"
                               value="${orderInfo.upkeepMileage}" data-v-type="integer|maxLength:8" maxlength="8" placeholder="" />
                        <i class="fa">km</i>
                    </div>
                </div><div class="col-3">
                    <div class="form-label">
                        下次保养时间
                    </div><div class="form-item">
                        <input type="text" name="customerCar.keepupTimeStr" class="yqx-input js-keepup-time-date"
                               value="<#if (customerCar.keepupTime)??>${customerCar.keepupTime?string("yyyy-MM-dd")}</#if>"
                               placeholder=""/>
                        <span class="fa icon-calendar"></span>
                    </div>
                </div><div class="col-3">
                    <div class="form-label form-label-must">
                        服务顾问
                    </div><div class="form-item yqx-downlist-wrap">
                        <input type="text" name="orderInfo.receiverName"
                               class="yqx-input yqx-input-icon js-speedily-receiver" value="<#if (orderInfo.receiverName)??>${orderInfo.receiverName}<#else>${SESSION_USER_NAME}</#if>"
                               data-v-type="required" placeholder="请选择服务顾问" >
                        <input type="hidden" name="orderInfo.receiver" value="<#if (orderInfo.receiver)??>${orderInfo.receiver}<#else>${SESSION_USER_ID}</#if>"/>
                        <span class="fa icon-angle-down"></span>
                    </div>
                </div>

            </div>
            <div class="show-grid">
                <div class="col-6">
                    <div class="input-item">
                        <div class="form-label">
                            VIN码
                        </div><div class="form-item form-data-width">
                        <input type="text" name="orderInfo.vin" class="yqx-input" value="${orderInfo.vin}" disabled/>
                        </div>
                    </div>
                </div>
                <div class="col-6">
                    <div class="input-item">
                        <div class="form-label">
                            客户单位
                        </div><div class="form-item yqx-downlist-wrap form-data-width">
                            <input type="text" name="orderInfo.company" class="yqx-input js-show-tips" value="${customer.company}"
                                   maxlength="100" placeholder="请输入客户单位" />
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 工单申请优惠 兼容普通工单-->
        <input type="hidden" name="orderInfo.preDiscountRate" value="1"/>
        <input type="hidden" name="orderInfo.preTaxAmount" value="0"/>
        <input type="hidden" name="orderInfo.prePreferentiaAmount" value="0"/>
        <input type="hidden" name="orderInfo.preCouponAmount" value="0"/>
        <input type="hidden" name="orderInfo.preTotalAmount"/>

        <div class="query-orderdetail">
            <div class="orderdetail-nav">
                <div class="tab-item js-tabs current-item title-active" data-class="repair-service">服务项目</div><div class="tab-item js-tabs" data-class="repair-goods">配件项目
                    <i class="tip">有新配件<i class="tip-angl"></i></i>
                </div>
                <div class="quick-btn nav-btn js-speedily-to-more">
                    <span>更多车辆信息</span>
                </div>
            </div>
            <!-- 服务项目 start -->
            <div class="margin-body repair-service">
                <div class="tools-bar">
                    <#if shopServiceInfoList>
                        <p class="tools-bar-alert service-alert">
                            <i class="strong-i"><span class="hot-service js-hot-service">?</span>常用服务:</i>
                            <#list shopServiceInfoList as service>
                                <i class="i-service js-select-service" data-id="${service.id}">${service.name}</i>
                            </#list>
                        </p>
                    </#if>
                    <div class="tools-bar-new js-tools-bar-new">
                        <div class="add-new-btn">
                            <i class="icon-plus"></i><i class="i-btn" id="serviceAddBtn">新建服务资料</i>
                        </div>
                    </div>
                </div>
                <div class="form-body ">
                    <table class="yqx-table" id="service">
                        <thead>
                        <tr>
                            <th>
                                <div>服务名称</div>
                            </th>
                            <th class="yqx-th-10">
                                <div>服务类别</div>
                            </th>
                            <th class="yqx-th-7">
                                <div class="form-label form-label-must">工时费</div>
                            </th>
                            <th class="yqx-th-5">
                                <div class="form-label form-label-must">工时</div>
                            </th>
                            <th class="yqx-th-7">
                                <div>金额</div>
                            </th>
                            <th class="yqx-th-5">
                                <div>优惠</div>
                            </th>
                            <th>
                                <div>维修工</div>
                            </th>
                            <th>
                                <div>服务备注</div>
                            </th>
                            <th class="yqx-th-5">
                                操作
                            </th>
                        </tr>
                        </thead>
                        <tbody class="yqx-tbody">
                        <#list basicOrderService as orderService>
                        <tr class="service-datatr" data-id="${orderService.id}">
                            <!--服务名称-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceName" class="js-show-tips yqx-input yqx-input-small"
                                           value="${orderService.serviceName}" disabled/>
                                    <input type="hidden" name="id" value="${orderService.id}"/>
                                    <input type="hidden" name="serviceId" value="${orderService.serviceId}"/>
                                    <input type="hidden" name="serviceSn" value="${orderService.serviceSn}"/>
                                    <input type="hidden" name="parentServiceId"
                                           value="${orderService.parentServiceId}"/>
                                    <!-- 基础服务类型 -->
                                    <input type="hidden" name="type" clear="false" value="${orderService.type}"/>
                                </div>
                            </td>
                            <!--服务类别-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceCatName" value="${orderService.serviceCatName}"
                                           class="yqx-input yqx-input-small js-show-tips" disabled/>
                                    <input type="hidden" name="serviceCatId" value="${orderService.serviceCatId}"/>
                                </div>
                            </td>
                            <!--工时费-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="servicePrice" value="<#if orderService.servicePrice>${(orderService.servicePrice)?string("0.00")}<#else>0.00</#if>"
                                           class="yqx-input yqx-input-small js-service-price" data-v-type="required | number" maxlength="10"/>
                                </div>
                            </td>
                            <!--工时-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceHour" value="<#if orderService.serviceHour>${orderService.serviceHour}<#else>0</#if>"
                                           class="yqx-input yqx-input-small js-service-hour" data-v-type="required | number" maxlength="10"/>
                                </div>
                            </td>
                            <!--金额-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceAmount" value="<#if orderService.serviceAmount>${(orderService.serviceAmount)?string("0.00")}<#else>0.00</#if>"
                                           class="yqx-input yqx-input-small js-service-amount js-show-tips" disabled/>
                                </div>
                            </td>
                            <!--优惠-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="discount" value="<#if orderService.discount>${(orderService.discount)?string("0.00")}<#else>0.00</#if>"
                                           class="yqx-input yqx-input-small js-service-discount" data-v-type="number|compareService" maxlength="10"/>
                                </div>
                            </td>
                            <!--维修工-->
                            <td>
                                <div class="form-item yqx-downlist-wrap">
                                    <input type="text" value="${orderService.workerNames}"
                                           class="yqx-input yqx-input-icon js-speedily-worker yqx-input-small js-show-tips"/>
                                    <input type="hidden" name="workerIds" value="${orderService.workerIds}"/>
                                    <span class="fa icon-small icon-angle-down"></span>
                                </div>
                            </td>
                            <!--服务备注-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceNote" value="${orderService.serviceNote}"
                                           class="yqx-input js-show-tips yqx-input-small" maxlength="100"/>
                                </div>
                            </td>
                            <td>
                                <button class="tabletr-del js-trdel">删除</button>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                    <div class="get-service">
                        <button class="yqx-btn yqx-btn-3 js-get-service">选择服务</button>
                    </div>
                </div>

            </div>
            <!-- 服务项目 end -->

            <!-- 配件物料 start -->
            <div class="margin-body repair-goods" style="display: none">
                <div class="tools-bar">
                    <div class="tools-bar-icons">
                        <a href="javascript:;" class="js-in-warehouse"><span class="icon-download"></span>转入库</a>
                        <a href="javascript:;" class="js-batch-add-part-btn"><span class="icon-layers"></span>批量添加配件</a>
                        <a href="javascript:;" class="js-new-part-btn"><span class="icon-plus"></span>新建配件资料</a>
                    </div>
                </div>
                <div class="form-body ">
                    <table class="yqx-table" id="goods">
                        <thead>
                        <tr>
                            <th class="yqx-th-2">
                                <input type="checkbox" class="js-checkbox-all"/></th>
                            <th class="yqx-th-9">
                                <div>零件号</div>
                            </th>
                            <th>
                                <div>配件名称</div>
                            </th>
                            <th class="yqx-th-5">
                                <div class="form-label form-label-must">售价</div>
                            </th>
                            <th class="yqx-th-6">
                                <div class="form-label form-label-must">数量</div>
                            </th>
                            <th class="yqx-th-7">
                                <div>金额</div>
                            </th>
                            <th class="yqx-th-5">
                                <div>优惠</div>
                            </th>
                            <th class="yqx-th-6">
                                <div>库存</div>
                            </th>
                            <th class="yqx-th-8">
                                <div>销售员</div>
                            </th>
                            <th class="yqx-th-12"">
                                <div>配件备注</div>
                            </th>
                            <th class="yqx-th-5">
                                操作
                            </th>
                        </tr>
                        </thead>
                        <tbody class="yqx-tbody">
                        <#list orderGoodsList as orderGoods>
                        <tr class="goods-datatr">
                            <td class="speedily-checkbox">
                                <input type="checkbox" class="js-repair-checkbox" disabled/>
                            </td>
                            <!--零件号-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsFormat" value="${orderGoods.goodsFormat}"
                                           class="yqx-input yqx-input-small js-show-tips" disabled/>
                                    <input type="hidden" name="id" value="${orderGoods.id}"/>
                                    <input type="hidden" name="goodsId" value="${orderGoods.goodsId}"/>
                                    <input type="hidden" name="goodsSn" value="${orderGoods.goodsSn}"/>
                                    <input type="hidden" name="goodsType" value="${orderGoods.goodsType}"
                                           clear="false"/>
                                </div>
                            </td>
                            <!--配件名称-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsName" value="${orderGoods.goodsName}"
                                           class="yqx-input yqx-input-small js-show-tips" disabled/>
                                </div>
                            </td>
                            <!--售价-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsPrice" value="<#if orderGoods.goodsPrice>${(orderGoods.goodsPrice)?string("0.00")}<#else>0.00</#if>"
                                           class="yqx-input yqx-input-small js-goods-price js-show-tips" data-v-type="required | number" maxlength="10"/>
                                </div>
                            </td>
                            <!--数量-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsNumber" value="<#if orderGoods.goodsNumber>${orderGoods.goodsNumber}<#else>0</#if>"
                                           class="yqx-input yqx-input-small js-goods-num" placeholder="单位" data-v-type="required | number" maxlength="10"/>
                                    <input type="hidden" name="measureUnit" value="${orderGoods.measureUnit}"/>
                                    <input type="hidden" name="inventoryPrice" value="${orderGoods.inventoryPrice}"/>
                                    <i class="fa goods-fa">${orderGoods.measureUnit}</i>
                                </div>
                            </td>
                            <!--金额-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsAmount" value="<#if orderGoods.goodsAmount>${(orderGoods.goodsAmount)?string("0.00")}<#else>0.00</#if>"
                                           class="yqx-input yqx-input-small js-goods-amount js-show-tips" disabled/>
                                </div>
                            </td>
                            <!--优惠-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="discount" value="<#if orderGoods.discount>${(orderGoods.discount)?string("0.00")}<#else>0.00</#if>"
                                           class="yqx-input yqx-input-small js-goods-discount" data-v-type="number|compareGoods" maxlength="10"/>
                                </div>
                            </td>
                            <!--库存-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="stock" value="${orderGoods.stock}"
                                           class="yqx-input yqx-input-small" disabled/>
                                </div>
                            </td>
                            <!--销售员-->
                            <td>
                                <div class="form-item">
                                    <input type="text" value="${orderGoods.saleName}"
                                           class="yqx-input yqx-input-icon js-speedily-sale yqx-input-small js-show-tips"/>
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
                            <td>
                                <button class="tabletr-del js-trdel">删除</button>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                    <div class="get-service">
                        <button class="yqx-btn yqx-btn-3 js-add-goods">选择配件</button>
                    </div>
                </div>
            </div>
            <!-- margin-body end-->
            <!-- 备注及按钮 -->
            <div style="padding: 0 10px;">
                <div class="input-mark">
                    <div class="form-label form-label-remark">备注</div>
                    <div class="form-item form-item-remark">
                        <input class="js-show-tips yqx-input" name="orderInfo.postscript" data-v-type="maxLength:200" autocomplete="off"
                               value="${orderInfo.postscript}" placeholder="请输入备注">
                    </div>
                </div>
                <p class="pay-line">
                    <span>服务费用：</span>
                    <i id="serviceMoneySum"
                       class="money-sum"><#if (orderInfo && orderInfo.serviceAmount && orderInfo.serviceDiscount)>${(orderInfo.serviceAmount - orderInfo.serviceDiscount)?string("0.00")}<#else>
                        0.00</#if></i>
                    元 +
                    <span>配件费用：</span>
                    <i id="goodsMoneySum"
                       class="money-sum"><#if (orderInfo && orderInfo.goodsAmount && orderInfo.goodsDiscount)>${(orderInfo.goodsAmount - orderInfo.goodsDiscount)?string("0.00")}<#else>
                        0.00</#if></i>
                    元
                </p>

                <p class="pay-line" >
                    <span>总计：</span>
                    <i id="allMoneySum"
                       class="money-sum"><#if (orderInfo && orderInfo.orderAmount)>${orderInfo.orderAmount?string("0.00")}<#else>
                        0.00</#if></i>
                    元
                    <#if orderInfo && orderInfo.downPayment gt 0.00>
                    <i class="pre-payment">
                        <span>预付定金: </span><i class="money-sum">#{orderInfo.downPayment;m2M2}</i>元
                    </i>
                    </#if>
                </p>

                <div class="form-last-btns">
                    <button class="yqx-btn yqx-btn-2 js-submit-create">
                    <#if orderInfo.id>保存<#else>开单</#if>
                    </button>
                    <button class="yqx-btn yqx-btn-1 js-submit-settle">
                        收款
                    </button>
                    <button class="yqx-btn yqx-btn-1 fr js-return">返回</button>
                </div>
            </div>
            <!-- 备注及按钮  end -->
        </div>
    </div>
    <!-- 右侧内容区 end -->
</div>

<!-- 脚本引入区 start -->
<!--服务项目-->
<script type="text/html" id="serviceTpl">
    <%if (json){%>
    <%for(var i in json) { %>
    <%var service = json[i]%>
    <tr class="service-datatr" data-id="<%=service.id%>">
        <!--服务名称-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceName" class="js-show-tips yqx-input yqx-input-small"
                       value="<%=service.name%>" disabled/>
                <input type="hidden" name="serviceId" value="<%=service.id%>"/>
                <input type="hidden" name="serviceSn" value="<%=service.serviceSn%>"/>
                <input type="hidden" name="parentServiceId" value="<%if(parentService){%><%= parentService.id%><%}else{%>0<%}%>"/>
                <!-- 基础服务类型 -->
                <input type="hidden" name="type" clear="false" value="<%=service.type%>"/>
                <input type="hidden" class="input-suiteNum" value="<%=service.suiteNum%>">
            </div>
        </td>
        <!--服务类别-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceCatName" value="<%=service.serviceCatName || service.categoryName%>"
                       class="yqx-input yqx-input-small js-show-tips" disabled/>
                <input type="hidden" name="serviceCatId" value="<%=service.categoryId%>"/>
            </div>
        </td>
        <!--工时费-->
        <td>
            <div class="form-item">
                <input type="text" name="servicePrice" value="<%=service.servicePrice%>" class="js-show-tips yqx-input yqx-input-small js-service-price"
                       data-v-type="required | price" maxlength="10"/>
            </div>
        </td>
        <!--工时-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceHour" value="1" class="yqx-input yqx-input-small js-service-hour"
                       data-v-type="required | number" maxlength="10"/>
            </div>
        </td>
        <!--金额-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceAmount" value="<%=service.servicePrice%>"
                       class="yqx-input yqx-input-small js-service-amount js-show-tips" disabled/>
            </div>
        </td>
        <!--优惠-->
        <td>
            <div class="form-item">
                <input type="text" name="discount" value="0.00" class="yqx-input yqx-input-small js-service-discount"
                       data-v-type="number|compareService" maxlength="10"/>
            </div>
        </td>
        <!--维修工-->
        <td>
            <div class="form-item yqx-downlist-wrap">
                <input type="text" class="yqx-input yqx-input-icon js-speedily-worker js-show-tips yqx-input-small"/>
                <input type="hidden" name="workerIds"/>
                <span class="fa icon-small icon-angle-down"></span>
            </div>
        </td>
        <!--服务备注-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceNote" value="<%=service.serviceNote%>"
                       class="yqx-input yqx-input-small js-show-tips" maxlength="100"/>
            </div>
        </td>
        <td>
            <button class="tabletr-del js-trdel">删除</button>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>
<!--物料-->
<script type=" text/html" id="goodsTpl">
    <%if (json){%>
    <%for (var i=0;i< json.length;i++){%>
    <%var goods = json[i]%>
    <tr class="goods-datatr">
        <td class="speedily-checkbox">
            <input type="checkbox" class="js-repair-checkbox"/>
        </td>
        <!--零件号-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsFormat" value="<%=goods.format%>"
                       class="yqx-input yqx-input-small js-show-tips" disabled/>
                <input type="hidden" name="goodsId" value="<%=goods.id%>"/>
                <input type="hidden" name="goodsSn" value="<%=goods.goodsSn%>"/>
                <input type="hidden" name="goodsType" value="<%=goods.goodsType%>" clear="false"/>
            </div>
        </td>
        <!--配件名称-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsName" value="<%=goods.name%>"
                       class="yqx-input yqx-input-small js-show-tips" disabled/>
            </div>
        </td>
        <!--售价-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsPrice" value="<%=goods.price%>"
                       class="yqx-input yqx-input-small js-goods-price js-show-tips" data-v-type="required | price" maxlength="10"/>
            </div>
            </div>
        </td>
        <!--数量-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsNumber" value="<%=goods.goodsNum || 1%>"
                       class="yqx-input yqx-input-small js-goods-num" placeholder="单位" data-v-type="required | number" maxlength="10"/>
                <input type="hidden" name="measureUnit" value="<%=goods.measureUnit%>"/>
                <input type="hidden" name="inventoryPrice" value="<%=goods.inventoryPrice%>"/>
                <i class="fa goods-fa"><%=goods.measureUnit%></i>
            </div>
        </td>
        <!--金额-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsAmount" value="<%=goods.goodsAmount || (goods.price * (goods.goodsNum || 1))%>"
                       class="yqx-input yqx-input-small js-goods-amount js-show-tips" disabled/>
            </div>
        </td>
        <!--优惠-->
        <td>
            <div class="form-item">
                <input type="text" name="discount" value="0.00"
                       class="yqx-input yqx-input-small js-goods-discount" data-v-type="number|compareGoods" maxlength="10"/>
            </div>
        </td>
        <!--库存-->
        <td>
            <div class="form-item">
                <input type="text" name="stock" value="<%=goods.stock%>" class="yqx-input yqx-input-small js-show-tips"
                       disabled/>
            </div>
        </td>
        <!--销售员-->
        <td>
            <div class="form-item yqx-downlist-wrap">
                <input type="text" class="yqx-input yqx-input-icon js-speedily-sale yqx-input-small js-show-tips"/>
                <input type="hidden" name="saleId"/>
                <span class="fa icon-small icon-angle-down"></span>
            </div>
        </td>
        <!--配件备注-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsNote" class="yqx-input yqx-input-small js-show-tips" maxlength="100"/>
            </div>
        </td>
        <td>
            <button class="tabletr-del js-trdel">删除</button>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>
<script src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script src="${BASE_PATH}/static/js/page/order/speedily.js?512b311d6b79316c424cb2fa6fc521b5"></script>
<!-- 脚本引入区 end -->
<!-- 转入库模板 -->
<#include "yqx/tpl/order/in-warehouse-tpl.ftl">
<!-- 批量添加物料模板 -->
<#include "yqx/tpl/order/batch-add-part-tpl.ftl">
<!-- 新增配件模板 -->
<#include "yqx/tpl/order/new-part-tpl.ftl">
<!-- 添加服务模版 -->
<#include "yqx/tpl/common/get-service-tpl.ftl">
<!-- 新增服务模版 -->
<#include "yqx/tpl/common/service-add-tpl.ftl">
<!-- 配件分类模板 -->
<#include "yqx/tpl/common/goods-type-tpl.ftl">
<!-- 维修工／洗车工 多选 -->
<#include "yqx/tpl/order/worker-multiple-tpl.ftl">
<!-- 添加配件 -->
<#include "yqx/tpl/common/goods-add-tpl.ftl">
<!-- 公用车牌模板 -->
<#include "yqx/tpl/common/car-licence-tpl.ftl">

<#include "yqx/layout/footer.ftl">
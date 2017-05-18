<!-- create by sky 20160406 新建综合维修单 -->
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/virtualorder-edit.css?105109988ff9d480fa6fe2962b753c22"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <!-- 内容区头部标题区 start -->
        <div class="order-head clearfix">
            <#if virtualOrderId>
                <h1 class="headline fl">编辑子单</h1>
            <#else>
                <h1 class="headline fl">新建子单</h1>
            </#if>
        </div>
        <!-- 内容区头部标题区 end -->

        <!-- 基本信息区域 start -->
        <div class="order-form mb10 base-box">
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        车牌
                    </div><div class="form-item">
                        <input type="text"
                               class="yqx-input form-data-width"
                               name="orderInfo.carLicense"
                               title="车牌"
                               data-label="车牌"
                               data-v-type="required | licence"
                               value="${orderInfo.carLicense}"
                               autocomplete="off" placeholder="请输入车牌号码"/>
                        <input type="hidden" name="orderInfo.customerCarId" value="${orderInfo.customerCarId}"/>
                        <input type="hidden" name="orderInfo.customerId" value="${orderInfo.customerId}"/>
                        <input type="hidden" name="orderInfo.orderStatus" value="${orderInfo.orderStatus}"/>
                        <input type="hidden" name="orderInfo.engineNo" value="${orderInfo.engineNo}"/>
                        <input type="hidden" name="orderInfo.carColor" value="${orderInfo.carColor}"/>
                        <input type="hidden" name="orderInfo.payStatus" value="${orderInfo.payStatus}"/>
                        <input type="hidden" name="orderInfo.carAlias" value="${orderInfo.carAlias}"/>
                        <input type="hidden" name="orderInfo.vin" value="${orderInfo.vin}"/>
                        <input type="hidden" name="orderInfo.customerAddress" value="${orderInfo.customerAddress}"/>
                        <input type="hidden" name="orderInfo.buyTime" value="<#if orderInfo.buyTime>${orderInfo.buyTime?string("yyyy-MM-dd")}</#if>"/>
                    <#if virtualOrderId>
                            <input type="hidden" name="orderInfo.id" value="${virtualOrderId}"/>
                            <input type="hidden" name="orderInfo.parentId" value="${orderInfo.parentId}"/>
                        <#else>
                            <input type="hidden" name="orderInfo.parentId" value="${orderInfo.id}"/>
                        </#if>
                        <input type="hidden" name="orderInfo.orderSn" value="${orderInfo.orderSn}"/>
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label">
                        联系人
                    </div><div class="form-item">
                        <input type="text" name="orderInfo.contactName"
                               value="${orderInfo.contactName}" class="yqx-input"/>
                    </div>
                </div>

                <div class="col-3">
                    <div class="form-label">
                        联系电话
                    </div><div class="form-item">
                        <input type="text" name="orderInfo.contactMobile" class="yqx-input"
                               data-v-type="phone" value="${orderInfo.contactMobile}" data-label="联系电话"/>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        车型
                    </div><div class="form-item form-data-car-model yqx-downlist-wrap w204">
                        <input type="text" class="yqx-input" name="carModeBak" data-label="车型"
                               data-v-type="required" value="${orderInfo.carInfo}"/>
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
                        <!-- 年款排量-->
                        <input type="hidden" name="orderInfo.yearPowerBak" value="${orderInfo.carYear}&nbsp;<#if orderInfo.carGearBox>${orderInfo.carGearBox}<#else>${orderInfo.carPower}</#if>"/>
                        <input type="hidden" name="orderInfo.carYear" value="${orderInfo.carYear}"/>
                        <input type="hidden" name="orderInfo.carYearId" value="${orderInfo.carYearId}"/>
                        <input type="hidden" name="orderInfo.carPower" value="${orderInfo.carPower}"/>
                        <input type="hidden" name="orderInfo.carPowerId" value="${orderInfo.carPowerId}"/>
                        <input type="hidden" name="orderInfo.carGearBox" value="${orderInfo.carGearBox}"/>
                        <input type="hidden" name="orderInfo.carGearBoxId" value="${orderInfo.carGearBoxId}"/>
                    </div><button
                        type="button" class="yqx-btn yqx-btn-1 margin-r-10 js-car-type">
                        选择车型
                    </button>
                </div>
                <div class="col-3">
                    <div class="form-label">
                        车主
                    </div><div class="form-item">
                        <input type="text"
                               name="orderInfo.customerName"
                               class="yqx-input"
                               value="${orderInfo.customerName}"/>
                    </div>
                </div>

                <div class="col-3">
                    <div class="form-label">
                        车主电话
                    </div><div class="form-item">
                        <input type="text"
                               name="orderInfo.customerMobile"
                               class="yqx-input"
                               value="${orderInfo.customerMobile}"/>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        开单日期
                    </div><div class="form-item form-data-width">
                        <input type="text" class="yqx-input yqx-input-icon js-create-time"
                               name="orderInfo.createTime"
                               title="开单日期"
                               data-label="开单日期"
                               data-v-type="required"
                               value="<#if (orderInfo.createTime)??>${orderInfo.createTime?string("yyyy-MM-dd HH:mm")}<#else>${.now?string("yyyy-MM-dd HH:mm")}</#if>"
                               placeholder="请选择开单日期"/>
                        <span class="fa icon-calendar"></span>
                    </div>
                </div>

                <div class="col-6">
                    <div class="form-label">
                        预计完工日期
                    </div><div class="form-item form-data-width">
                        <input type="text" class="yqx-input yqx-input-icon js-expected-time"
                               name="orderInfo.expectedTime"
                               title="预计完工日期"
                               data-label="预计完工日期"
                               value="<#if (orderInfo.expectedTime)??>${orderInfo.expectedTime?string("yyyy-MM-dd HH:mm")}<#else>${.now?string("yyyy-MM-dd HH:mm")}</#if>"
                               placeholder="请选择预计完工日期"/>
                        <span class="fa icon-calendar"></span>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        结算日期
                    </div><div class="form-item form-data-width">
                        <input type="text" class="yqx-input yqx-input-icon js-pay-time"
                               name="orderInfo.payTime"
                               title="结算日期"
                               data-label="结算日期"
                               data-v-type="required"
                               value="<#if (orderInfo.payTime)??>${orderInfo.payTime?string("yyyy-MM-dd HH:mm")}<#else>${.now?string("yyyy-MM-dd HH:mm")}</#if>"
                               placeholder="请选择结算日期"/>
                        <span class="fa icon-calendar"></span>
                    </div>
                </div>

                <div class="col-6">
                    <div class="form-label form-label-must">
                        打印日期
                    </div><div class="form-item form-data-width">
                        <input type="text" class="yqx-input yqx-input-icon js-print-time"
                               name="orderInfo.printTime"
                               title="打印日期"
                               data-label="打印日期"
                               data-v-type="required"
                               value="<#if (orderInfo.printTime)??>${orderInfo.printTime?string("yyyy-MM-dd HH:mm")}<#else>${.now?string("yyyy-MM-dd HH:mm")}</#if>"
                               placeholder="请选择打印日期"/>
                        <span class="fa icon-calendar"></span>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-3">
                    <div class="form-label form-label-must">
                        行驶里程
                    </div><div class="form-item">
                        <input type="text" name="orderInfo.mileage"
                               class="yqx-input yqx-input-icon"
                               placeholder="行驶里程"
                               title="行驶里程"
                               data-label="行驶里程"
                               value="${orderInfo.mileage}"
                               data-v-type="required|integer|maxLength:8" maxlength="8"/>
                        <span class="fa">km</span>
                    </div>
                </div>

                <div class="col-3">
                    <div class="form-label form-label-must">
                        服务顾问
                    </div><div class="form-item">
                        <input type="text"
                               name="orderInfo.receiverName"
                               class="yqx-input yqx-input-icon js-select-user"
                               data-v-type="required"
                               title="服务顾问"
                               data-label="服务顾问"
                               value="<#if (orderInfo.receiverName)??>${orderInfo.receiverName}<#else>${SESSION_USER_NAME}</#if>"
                               placeholder="服务顾问">
                        <input type="hidden"
                               name="orderInfo.receiver"
                               title="服务顾问"
                               value="<#if (orderInfo.receiver)??>${orderInfo.receiver}<#else>${SESSION_USER_ID}</#if>"/>
                        <span class="fa icon-angle-down"></span>
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label">
                        业务类型
                    </div><div class="form-item">
                        <input type="text"
                               name="orderInfo.orderTypeName"
                               class="yqx-input yqx-input-icon js-ordertype"
                               title="业务类型"
                               value="${orderInfo.orderTypeName}"
                               placeholder="业务类型">
                        <input type="hidden" name="orderInfo.orderType" value="${orderInfo.orderType}"/>
                    <span class="fa icon-angle-down"></span>
                    </div>
                </div>

                <div class="col-3">
                    <div class="form-label">
                        油表油量
                    </div><div class="form-item">
                        <input type="text"
                               name="orderInfo.oilMeter"
                               class="yqx-input"
                               title="油表油量"
                               value="${orderInfo.oilMeter}"
                               placeholder="油表油量">
                    <span class="fa icon-angle-down"></span>
                    </div>
                </div>
            </div>
            <div class="js-toggle-a1 hide">
                <div class="show-grid">
                    <div class="col-3">
                        <div class="form-label">
                            承保公司
                        </div><div class="form-item">
                            <input type="text"
                                   name="orderInfo.insuranceCompanyName"
                                   class="yqx-input js-insurance"
                                   title="承保公司"
                                   value="${orderInfo.insuranceCompanyName}"
                                   placeholder="承保公司">
                            <input type="hidden" name="orderInfo.insuranceCompanyId" value="${orderInfo.insuranceCompanyId}"/>
                        <span class="fa icon-angle-down"></span>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="form-label">
                            对方保险
                        </div><div class="form-item">
                            <input type="text"
                                   name="orderInfo.otherInsuranceCompanyName"
                                   class="yqx-input js-otherinsurance"
                                   title="对方保险"
                                   value="${orderInfo.otherInsuranceCompanyName}"
                                   placeholder="对方保险">
                            <input type="hidden" name="orderInfo.otherInsuranceCompanyId" value="${orderInfo.otherInsuranceCompanyId}"/>
                        <span class="fa icon-angle-down"></span>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label">
                            客户单位
                        </div><div class="form-item">
                            <input type="text"
                                   name="orderInfo.company"
                                   class="yqx-input form-data-width"
                                   title="客户单位"
                                   value="${orderInfo.company}"
                                   placeholder="客户单位">
                        </div>
                    </div>
                </div>
            </div>
            <div class="toggle-control arrow-down js-toggle-control" data-ref="a1" data-status="hide"></div>
        </div>
        <!-- 基本信息区域 end -->

        <div class="order-panel">
            <div class="order-panel-head clearfix">
                <div class="tabs-control fl">
                    <span data-ref="a1" class="tab-item current-item js-tab-item">服务项目</span><span data-ref="a2" class="tab-item js-tab-item">配件项目<i class="tip js-tip">有新配件<i
                        class="tip-angl"></i></i></span><span data-ref="a3" class="tab-item js-tab-item">附加项目</span>
                </div>

                <div class="quick-btn js-tocustomer more-car fr">
                    <sapn>更多车辆信息</sapn>
                </div>
            </div>
            <div class="order-panel-body">
                <div class="tab-content js-tab-content js-tab-content-a1">
                    <div class="tools-bar-new">
                        <div class="add-new-btn">
                            <i class="icon-plus"></i><i class="i-btn" id="serviceAddBtn">新建服务资料</i>
                        </div>
                    </div>
                    <table class="yqx-table">
                        <thead>
                        <tr>
                            <th>
                                <div class="form-label">服务名称</div>
                            </th>
                            <th class="yqx-th-8">
                                <div class="form-label">服务类别</div>
                            </th>
                            <th class="yqx-th-6">
                                <div class="form-label">工时费</div>
                            </th>
                            <th class="yqx-th-5">
                                <div class="form-label">工时</div>
                            </th>
                            <th class="yqx-th-6">
                                <div class="form-label">金额</div>
                            </th>
                            <th class="yqx-th-6">
                                <div>优惠</div>
                            </th>
                            <th>
                                <div>服务备注</div>
                            </th>
                            <th class="yqx-th-5">
                                操作
                            </th>
                        </tr>
                        </thead>
                        <tbody class="yqx-tbody" id="orderServiceTB">
                        <#list basicOrderService as orderService>
                        <tr class="service-datatr" data-id="${orderService.id}">
                            <!--服务名称-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceName" class="yqx-input yqx-input-small"
                                           value="${orderService.serviceName}"/>
                                    <input type="hidden" name="id" value="${orderService.id}"/>
                                    <input type="hidden" name="serviceId" value="${orderService.serviceId}"/>
                                    <input type="hidden" name="serviceSn" value="${orderService.serviceSn}"/>
                                    <input type="hidden" name="parentServiceId"
                                           value="${orderService.parentServiceId}"/>
                                    <input type="hidden" name="flags" value="${orderService.flags}"/>
                                    <input type="hidden" name="type" value="1"/>
                                    <input type="hidden" name="serviceCatId" value="${orderService.serviceCatId}"/>
                                </div>
                            </td>
                            <!--服务类别-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceCatName" value="${orderService.serviceCatName}"
                                           class="yqx-input yqx-input-small"/>
                                    <input type="hidden" name="serviceCatId" value="${orderService.serviceCatId}"/>
                                </div>
                            </td>
                            <!--工时费-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="servicePrice" value="${orderService.servicePrice}"
                                           class="yqx-input yqx-input-small js-service-price"/>
                                </div>
                            </td>
                            <!--工时-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceHour" value="${orderService.serviceHour}"
                                           class="yqx-input yqx-input-small js-service-hour"/>
                                </div>
                            </td>
                            <!--金额-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceAmount" value="${orderService.serviceAmount}"
                                           class="yqx-input yqx-input-small js-service-amount" disabled/>
                                </div>
                            </td>
                            <!--优惠-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="discount" value="${orderService.discount}"
                                           class="yqx-input yqx-input-small js-service-discount"/>
                                </div>
                            </td>
                            <!--服务备注-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceNote" value="${orderService.serviceNote}"
                                           class="yqx-input yqx-input-small"/>
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
                        <button class="yqx-btn yqx-btn-3 select-server js-get-service">选择服务</button>
                    </div>
                </div>

                <div class="tab-content js-tab-content js-tab-content-a2">
                    <div class="tools-bar-new">
                        <div class="add-new-btn">
                            <a href="javascrip:void(0);">
                                <i class="icon-layers add-new-btn"></i><i class="i-btn js-batch-add-part-btn">批量添加配件</i>
                            </a>
                            <a href="javascript:void(0);">
                                <i class="icon-plus"></i><i class="i-btn js-new-part-btn">新建配件资料</i>
                            </a>
                        </div>
                    </div>
                    <table class="yqx-table">
                        <thead>
                        <tr>
                            <th class="yqx-th-9">
                                <div class="form-label">零件号</div>
                            </th>
                            <th class="yqx-th-8">
                                <div class="form-label">配件名称</div>
                            </th>
                            <th class="yqx-th-6">
                                <div class="form-label">售价</div>
                            </th>
                            <th class="yqx-th-6">
                                <div class="form-label">数量</div>
                            </th>
                            <th class="yqx-th-6">
                                <div>金额</div>
                            </th>
                            <th class="yqx-th-5"">
                            <div>优惠</div>
                            </th>
                            <th class="yqx-th-8 hide">
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
                        <tbody class="yqx-tbody" id="orderGoodsTB">
                        <#list orderGoodsList as orderGoods>
                        <tr class="goods-datatr">
                            <!--零件号-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsFormat" value="${orderGoods.goodsFormat}"
                                           class="yqx-input yqx-input-small"/>
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
                                           class="yqx-input yqx-input-small"/>
                                </div>
                            </td>
                            <!--售价-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsPrice" value="${orderGoods.goodsPrice}"
                                           class="yqx-input yqx-input-small js-goods-price"/>
                                </div>
                            </td>
                            <!--数量-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsNumber" value="${orderGoods.goodsNumber}"
                                           class="yqx-input yqx-input-small js-goods-num" placeholder="单位"/>
                                    <input type="hidden" name="measureUnit" value="${orderGoods.measureUnit}"/>
                                    <input type="hidden" name="inventoryPrice" value="${orderGoods.inventoryPrice}"/>
                                    <i class="fa goods-fa">${orderGoods.measureUnit}</i>
                                </div>
                            </td>
                            <!--金额-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsAmount" value="${orderGoods.goodsAmount}"
                                           class="yqx-input yqx-input-small js-goods-amount" disabled/>
                                </div>
                            </td>
                            <!--优惠-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="discount" value="${orderGoods.discount}"
                                           class="yqx-input yqx-input-small js-goods-discount"/>
                                </div>
                            </td>
                            <!--销售员-->
                            <td class="hide">
                                <div class="form-item">
                                    <input type="text" value="${orderGoods.saleName}"
                                           class="yqx-input yqx-input-icon js-speedily-sale yqx-input-small"/>
                                    <input type="hidden" name="saleId" value="${orderGoods.saleId}"/>
                                    <span class="fa icon-angle-down"></span>
                                </div>
                            </td>
                            <!--配件备注-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsNote" value="${orderGoods.goodsNote}"
                                           class="yqx-input yqx-input-small"/>
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
                        <button class="yqx-btn yqx-btn-3 select-server" id="goodsAddBtn">选择配件</button>
                    </div>
                </div>
                <div class="tab-content js-tab-content js-tab-content-a3">
                    <div class="tools-bar-new">
                        <div class="add-new-btn">
                            <i class="icon-plus"></i><i class="i-btn" id="feeAddBtn">新建附加资料</i>
                        </div>
                    </div>
                    <table class="yqx-table" id="orderAdditionalTB">
                        <thead>
                        <tr>
                            <th style="width: 400px;">
                                <div class="form-label">附加名称</div>
                            </th>
                            <th class="yqx-th-6" style="width: 50px;">
                                <div>金额</div>
                            </th>
                            <th style="width: 45px;">
                                <div>优惠金额</div>
                            </th>
                            <th style="width: 128px;">
                                <div>附加备注</div>
                            </th>
                            <th class="yqx-th-5">
                                操作
                            </th>
                        </tr>
                        </thead>
                        <tbody class="yqx-tbody" id="additionalOrderServiceTB">
                        <#list additionalServices as exService>
                        <tr class="service-datatr">
                            <!--费用项目-->
                            <td>
                                <div class="form-item">
                                    <#if exService.flags == "GLF">
                                        <input type="hidden" name="flags" value="${exService.flags}"/>
                                        <input type="hidden" name="manageRate" value="${exService.manageRate}"/>
                                    </#if>
                                    <input type="hidden" name="id" value="${exService.id}"/>
                                    <input type="hidden" name="serviceId" value="${exService.serviceId}"/>
                                    <input type="hidden" name="serviceSn" value="${exService.serviceSn}"/>
                                    <input type="hidden" name="type" value="2"/>
                                    <input type="hidden" name="serviceHour" value="1"/>
                                    <input type="hidden" name="serviceAmount" value="${exService.serviceAmount}"/>
                                    <input type="text" name="serviceName" value="${exService.serviceName}"
                                           class="yqx-input yqx-input-small service-name-width"/>
                                </div>
                            </td>
                            <!--金额-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="servicePrice" value="${exService.servicePrice}"
                                           class="yqx-input yqx-input-small js-goods-amount"/>
                                </div>
                            </td>
                            <!--优惠-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="discount" value="${exService.discount}"
                                           class="yqx-input yqx-input-small js-goods-discount"/>
                                </div>
                            </td>

                            <!--备注-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceNote" value="${exService.serviceNote}"
                                           class="yqx-input yqx-input-small"/>
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
                        <button class="yqx-btn yqx-btn-3 select-server" id="additionalAddBtn">选择附加</button>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-remark">
                        备注
                    </div><div class="form-item form-item-remark">
                    <input class="yqx-input" name="orderInfo.postscript" placeholder="请填写备注" data-v-type="maxLength:200" value="${orderInfo.postscript}" />
                </div>
                </div>
                <div class="show-grid">
                    <div>
                        <div class="form-label">服务费用：</div>
                        <div class="form-item">
                            <div class="yqx-text">
                                <strong class="ml-15 hl-o js-service-amount"
                                        id="serviceTotalAmount"><#if orderInfo.id>${(orderInfo.serviceAmount - orderInfo.serviceDiscount)?string(",###.##")}<#else>
                                    0.00</#if></strong>元
                            </div>
                        </div>
                        <span>+</span>
                        <div class="form-label">配件费用：</div>
                        <div class="form-item">
                            <div class="yqx-text">
                                <strong class="ml-15 hl-o"
                                        id="goodsTotalAmount"><#if orderInfo.id>${(orderInfo.goodsAmount - orderInfo.goodsDiscount)?string(",###.##")}<#else>
                                    0.00</#if></strong>元
                            </div>
                        </div>
                        <span>+</span>
                        <div class="form-label">附加费用：</div>
                        <div class="form-item">
                            <div class="yqx-text">
                                <strong class="ml-15 hl-o"
                                        id="feeTotalAmount"><#if orderInfo.id>${(orderInfo.feeAmount - orderInfo.feeDiscount)?string(",###.##")}<#else>
                                    0.00</#if></strong>元
                            </div>
                        </div>
                    </div>
                    <div class="form-label">总计：</div>
                    <div class="form-item">
                        <div class="yqx-text">
                            <strong class="ml-15 hl-o"
                                    id="orderTotalAmount"><#if orderInfo.id>${orderInfo.orderAmount?string(",###.##")}<#else>
                                0.00</#if></strong>元
                        </div>
                    </div>
                </div>
            </div>
            <div class="order-panel-foot">
            <#if virtualOrderId>
                <button class="yqx-btn yqx-btn-2 js-create">保存</button>
                <button class="yqx-btn yqx-btn-1 js-virtualorder-print">打印</button>
                <div class="fr">
                    <button class="yqx-btn yqx-btn-1 js-virtualorder-delete">删除</button>
                    <button class="yqx-btn yqx-btn-1 js-backmain">返回主单</button>
                </div>
            <#else>
                <button class="yqx-btn yqx-btn-2 js-create">开单</button>
                <div class="fr">
                    <button class="yqx-btn yqx-btn-1 js-backmain">返回主单</button>
                </div>
            </#if>
            </div>
        </div>
    </div>
    <!-- 右侧内容区 end -->
</div>

<!--服务项目-->
<script type="text/html" id="serviceTpl">
    <%if (json){%>
    <%for(var i in json) { %>
    <%var orderService = json[i]%>
    <tr class="service-datatr" data-id="<%=orderService.id%>">
        <!--服务名称-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceName" class="yqx-input yqx-input-small"
                       value="<%=orderService.name%>"/>
                <input type="hidden" name="id" value=""/>
                <input type="hidden" name="serviceId" value="<%=orderService.id%>"/>
                <input type="hidden" name="serviceSn" value="<%=orderService.serviceSn%>"/>
                <input type="hidden" name="parentServiceId"
                       value="<%=orderService.parentServiceId%>"/>
                <!-- 基础服务类型 -->
                <input type="hidden" name="type" value="<%=orderService.type%>"/>
            </div>
        </td>
        <!--服务类别-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceCatName" value="<%=orderService.serviceCatName%>"
                       class="yqx-input yqx-input-small"/>
                <input type="hidden" name="serviceCatId" value="<%=orderService.categoryId%>"/>
            </div>
        </td>
        <!--工时费-->
        <td>
            <div class="form-item">
                <input type="text" name="servicePrice" value="<%=orderService.servicePrice%>"
                       class="yqx-input yqx-input-small js-service-price" data-v-type="price"/>
            </div>
        </td>
        <!--工时-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceHour" value="1"
                       class="yqx-input yqx-input-small js-service-hour" data-v-type="number"/>
            </div>
        </td>
        <!--金额-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceAmount" value="<%=orderService.servicePrice%>"
                       class="yqx-input yqx-input-small js-service-amount" data-v-type="price" disabled/>
            </div>
        </td>
        <!--优惠-->
        <td>
            <div class="form-item">
                <input type="text" name="discount" value="0"
                       class="yqx-input yqx-input-small js-service-discount" data-v-type="price"/>
            </div>
        </td>
        <!--服务备注-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceNote" value="<%=orderService.serviceNote%>"
                       class="yqx-input yqx-input-small"/>
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
    <%var orderGoods = json[i]%>
    <tr class="goods-datatr">
        <!--零件号-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsFormat" value="<%=orderGoods.format%>"
                       class="yqx-input yqx-input-small"/>
                <input type="hidden" name="id" value=""/>
                <input type="hidden" name="goodsId" value="<%=orderGoods.id%>"/>
                <input type="hidden" name="goodsSn" value="<%=orderGoods.goodsSn%>"/>
                <input type="hidden" name="inventoryPrice" value="<%=orderGoods.inventoryPrice%>"/>
                <!-- 0:基本服务-->
                <input type="hidden" name="goodsType" value="0"/>
            </div>
        </td>
        <!--配件名称-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsName" value="<%=orderGoods.name%>"
                       class="yqx-input yqx-input-small"/>
            </div>
        </td>
        <!--售价-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsPrice" value="<%=orderGoods.price%>"
                       class="yqx-input yqx-input-small js-goods-price" data-v-type="price"/>
            </div>
        </td>
        <!--数量-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsNumber" value="<%=orderGoods.goodsNum || 1%>"
                       class="yqx-input yqx-input-small js-goods-num" data-v-type="price" placeholder="单位"/>
                <input type="hidden" name="measureUnit" value="<%=orderGoods.measureUnit%>"/>
                <i class="fa goods-fa"><%=orderGoods.measureUnit%></i>
            </div>
        </td>
        <!--金额-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsAmount" value="<%=(orderGoods.price * (orderGoods.goodsNum || 1))%>"
                       class="yqx-input yqx-input-small js-goods-amount" data-v-type="price" disabled/>
            </div>
        </td>
        <!--优惠-->
        <td>
            <div class="form-item">
                <input type="text" name="discount" value="0"
                       class="yqx-input yqx-input-small js-goods-discount" data-v-type="price"/>
            </div>
        </td>
        <!--配件备注-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsNote" value="<%=orderGoods.goodsNote%>"
                       class="yqx-input yqx-input-small"/>
            </div>
        </td>
        <td>
            <button class="tabletr-del js-trdel">删除</button>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>

<!--附加费用-->
<script type=" text/html" id="additionalTpl">
    <%if (json){%>
    <%for(var i in json) { %>
    <%var orderService = json[i]%>
    <tr class="service-datatr">
        <!--费用项目-->
        <td>
            <div class="form-item">
                <input type="hidden" name="id" value=""/>
                <input type="hidden" name="serviceId" value="<%=orderService.id%>"/>
                <input type="hidden" name="serviceSn" value="<%=orderService.serviceSn%>"/>
                <!-- 附加服务 -->
                <input type="hidden" name="type" value="2"/>
                <input type="hidden" name="serviceHour" value="1"/>
                <input type="hidden" name="serviceAmount" value="<%=orderService.servicePrice%>"/>
                <input type="text" name="serviceName" value="<%=orderService.name%>"
                       class="yqx-input yqx-input-small service-name-width"/>
            </div>
        </td>
        <!--金额-->
        <td>
            <div class="form-item">
                <input type="text" name="servicePrice" value="<%=orderService.servicePrice%>"
                       class="yqx-input yqx-input-small js-goods-amount" data-v-type="price"/>
            </div>
        </td>
        <!--优惠-->
        <td>
            <div class="form-item">
                <input type="text" name="discount" value="0"
                       class="yqx-input yqx-input-small js-goods-discount" data-v-type="price"/>
            </div>
        </td>
        <!--备注-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceNote" value="<%=orderService.serviceNote%>"
                       class="yqx-input yqx-input-small"/>
            </div>
        </td>
        <td>
            <button class="tabletr-del js-trdel">删除</button>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>

<!-- 添加服务模版 -->
<#include "yqx/tpl/common/get-service-tpl.ftl">
<!-- 批量添加物料模板 -->
<#include "yqx/tpl/order/batch-add-part-tpl.ftl">
<!-- 添加物料模版 -->
<#include "yqx/tpl/common/goods-add-tpl.ftl">
<!-- 添加附加费用模版 -->
<#include "yqx/tpl/common/get-additional-tpl.ftl">
<!-- 维修工／洗车工 多选 -->
<#include "yqx/tpl/order/worker-multiple-tpl.ftl">
<!-- 新增服务模版 -->
<#include "yqx/tpl/common/service-add-tpl.ftl">
<!-- 配件分类模板 -->
<#include "yqx/tpl/common/goods-type-tpl.ftl">
<!-- 新增配件模版 -->
<#include "yqx/tpl/order/new-part-tpl.ftl">
<!-- 公用车牌模板 -->
<#include "yqx/tpl/common/car-licence-tpl.ftl">
<!-- 新增附加模板 -->
<#include "yqx/tpl/order/new-additional-tpl.ftl">
<#--车型选择的dialog-->
<#include "yqx/tpl/common/car-type-tpl.ftl">
<!-- 配件分类模板 -->
<#include "yqx/tpl/common/goods-type-tpl.ftl">


<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script src="${BASE_PATH}/static/js/page/order/virtualorder-edit.js?3d01ccea15ff50430e1b8eacb779f5fe"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
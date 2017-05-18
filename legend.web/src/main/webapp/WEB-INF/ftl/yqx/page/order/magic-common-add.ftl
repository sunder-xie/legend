<!-- create by sky 20160406 新建综合维修单 -->
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/common-add.css?ee0498e585f85de8117de163d75964cb"/>
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
            <#if proxyId || orderInfo.proxyType == 2>
                <h1 class="headline fl">工单查询 - <small>委托单转工单</small></h1>
            <#else>
                <h1 class="headline fl"><#if orderInfo.id>编辑<#else>新建</#if>综合维修单</h1>
                <!-- 工作进度 start -->
                <div class="order-process clearfix fr">
                    <div class="order-step order-step-finish">
                        <div class="order-step-circle">1</div>
                        <p class="order-step-title">开综合维修单</p>
                    </div>
                    <div class="order-step">
                        <div class="order-step-circle">2</div>
                        <p class="order-step-title">仓库报价</p>
                    </div>
                    <div class="order-step">
                        <div class="order-step-circle">3</div>
                        <p class="order-step-title">技师仓库领料</p>
                    </div>
                    <div class="order-step">
                        <div class="order-step-circle">4</div>
                        <p class="order-step-title">综合维修单结算</p>
                    </div>
                </div>
                <!-- 工作进度 end -->
            </#if>
        </div>
        <!-- 内容区头部标题区 end -->

        <!-- 基本信息区域 start -->
        <div class="order-form mb10 base-box">
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        车牌
                    </div><div class="form-item yqx-downlist-wrap">
                        <input type="text"
                               class="yqx-input form-data-width"
                               name="orderInfo.carLicense"
                               title="车牌"
                               data-v-type="required|licence"
                               value="<#if orderInfo.carLicense>${orderInfo.carLicense}<#else>${license}</#if>"
                               autocomplete="off" placeholder="请输入车牌"/>
                        <input type="hidden" name="orderInfo.customerCarId" value="${orderInfo.customerCarId}"/>
                        <input type="hidden" name="orderInfo.id" value="${orderInfo.id}"/>
                        <input type="hidden" name="orderInfo.orderSn" value="${orderInfo.orderSn}"/>
                        <input type="hidden" name="orderInfo.appointId" value="${orderInfo.appointId}"/>
                        <input type="hidden" name="orderInfo.downPayment" value="${orderInfo.downPayment}"/>
                        <input type="hidden" id="shopType" name="shopType" value="${shopType}"/>
                        <input type="hidden" name="orderInfo.orderStatus" value="${orderInfo.orderStatus}"/>
                        <input type="hidden" name="orderInfo.gmtModifiedYMDHMS" value="${orderInfo.gmtModifiedYMDHMS}"/>
                        <input type="hidden" name="orderInfo.payStatus" value="${orderInfo.payStatus}"/>
                        <input type="hidden" name="orderInfo.proxyType" value="<#if proxyId>2<#else>${orderInfo.proxyType}</#if>"/>
                        <input type="hidden" name="proxyId" value="${proxyId}"/>
                        <input type="hidden" name="precheckId" value="${precheckId}"/>

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
                <div class="col-3">
                    <div class="form-label">联系人</div><div class="form-item">
                        <input type="text"
                               name="orderInfo.contactName"
                               class="yqx-input" placeholder="请输入联系人" value="${orderInfo.contactName}"/>
                    </div>
                </div>

                <div class="col-3">
                    <div class="form-label">联系电话</div><div class="form-item">
                        <input type="text"
                               name="orderInfo.contactMobile"
                               class="yqx-input"
                               placeholder="请输入联系电话"
                               data-v-type="phone"
                               data-label="联系电话" value="${orderInfo.contactMobile}"/>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-3">
                    <div class="form-label">车型</div><div class="form-item">
                        <input type="text"
                               class="yqx-input"
                               name="carModeBak"
                               value="${orderInfo.carInfo}"
                               disabled/>
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label">年款排量</div><div class="form-item">
                    <input type="text"
                           class="yqx-input"
                           name="yearPowerBak"
                           value="${orderInfo.carYear}&nbsp;<#if orderInfo.carGearBox>${orderInfo.carGearBox}<#else>${orderInfo.carPower}</#if>"
                           disabled/>
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label">车主</div><div class="form-item">
                        <input type="text"
                               name="orderInfo.customerName"
                               class="yqx-input js-show-tips"
                               value="${orderInfo.customerName}"
                               disabled/>
                    </div>
                </div>

                <div class="col-3">
                    <div class="form-label">车主电话</div><div class="form-item">
                        <input type="text"
                               name="orderInfo.customerMobile"
                               class="yqx-input"
                               value="${orderInfo.customerMobile}"
                               disabled/>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        开单日期
                    </div><div class="form-item form-data-width">
                        <input type="text" class="yqx-input yqx-input-icon js-create-time"
                               name="orderInfo.createTimeStr"
                               title="开单日期"
                               data-v-type="required"
                               data-label="开单日期"
                               value="<#if (orderInfo.createTime)??>${orderInfo.createTime?string("yyyy-MM-dd HH:mm")}<#else>${.now?string("yyyy-MM-dd HH:mm")}</#if>"
                               placeholder="请选择开单日期"/>
                        <span class="fa icon-calendar"></span>
                    </div>
                </div>

                <div class="col-6">
                    <div class="form-label form-label-must">期望交车日期</div><div class="form-item form-data-width">
                        <input type="text" class="yqx-input yqx-input-icon js-expected-time"
                               name="orderInfo.expectTime"
                               data-label="期望交车日期"
                               value="<#if (orderInfo.expectTime)>${orderInfo.expectTimeStr}<#elseif proxyOrderDetailVo.expectTime>${proxyOrderDetailVo.expectTime?string("yyyy-MM-dd HH:mm")}</#if>"
                               placeholder="请选择期望交车日期"
                               data-v-type="required"
                               <#if proxyOrderDetailVo.expectTime>disabled="disabled"</#if>/>
                        <span class="fa icon-calendar"></span>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-3">
                    <div class="form-label">
                        行驶里程
                    </div><div class="form-item">
                        <input type="text" name="orderInfo.mileage"
                               class="yqx-input yqx-input-icon js-number"
                               data-label="行驶里程"
                               title="行驶里程"
                               value="${orderInfo.mileage}"
                               data-v-type="integer|maxLength:8" maxlength="8"/>
                        <span class="fa">km</span>
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
                           placeholder="">
                    <span class="fa icon-angle-down"></span>
                </div>
                </div>
                <div class="col-6">
                    <div class="form-label">
                        VIN码
                    </div><div class="form-item form-data-width">
                    <input type="text"
                           name="orderInfo.vin"
                           class="yqx-input"
                           title="客户单位"
                           value="${orderInfo.vin}" disabled>
                </div>
                </div>


            </div>
            <div class="js-toggle-a1">
                <div class="show-grid">
                    <div class="col-3">
                        <div class="form-label form-label-must">
                            颜色
                        </div><div class="form-item">
                        <input type="text"
                               name="orderInfo.carColor"
                               class="yqx-input yqx-input-icon"
                               data-v-type="required"
                               data-label="颜色"
                               title="颜色"
                               value="${orderInfo.carColor}"
                               placeholder="请输入颜色"/>
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
                               value="<#if (orderInfo.receiverName)??>${orderInfo.receiverName}<#else>${SESSION_USER_NAME}</#if>"
                               placeholder="请输入服务顾问">
                        <input type="hidden"
                               name="orderInfo.receiver"
                               title="服务顾问"
                               value="<#if (orderInfo.receiver)??>${orderInfo.receiver}<#else>${SESSION_USER_ID}</#if>"/>
                        <span class="fa icon-angle-down"></span>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label">
                            保险到期
                        </div><div class="form-item form-data-width">
                        <input type="text" class="yqx-input yqx-input-icon js-insurance-time"
                               name="orderInfo.insuranceTimeStr"
                               title="保险到期"
                               value="${orderInfo.insuranceTimeStr}"
                               placeholder="请选择保险到期日期"/>
                        <span class="fa icon-calendar"></span>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-3">
                        <div class="form-label">
                            下次保养里程
                        </div><div class="form-item">
                        <input type="text"
                               name="orderInfo.upkeepMileage"
                               class="yqx-input js-number"
                               data-v-type="maxLength:8"
                               value="${orderInfo.upkeepMileage}"/>
                        <span class="fa">km</span>
                    </div>
                    </div>

                    <div class="col-3">
                        <div class="form-label">
                            下次保养时间
                        </div><div class="form-item">
                        <input type="text"
                               name="customerCar.keepupTimeStr"
                               class="yqx-input js-keepup-time-date"
                               value="<#if (customerCar.keepupTime)??>${customerCar.keepupTime?string("yyyy-MM-dd")}</#if>"
                               placeholder=""/>
                        <span class="fa icon-calendar"></span>
                    </div>
                    </div>

                    <div class="col-3">
                        <div class="form-label">
                            承保公司
                        </div><div class="form-item">
                        <input type="text"
                               name="orderInfo.insuranceCompanyName"
                               class="yqx-input js-insurance js-show-tips"
                               title="承保公司"
                               placeholder="输入查询"
                               value="${orderInfo.insuranceCompanyName}">
                        <input type="hidden"
                               name="orderInfo.insuranceCompanyId"
                               value="${orderInfo.insuranceCompanyId}"/>
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
                               placeholder="输入查询"
                               value="${orderInfo.otherInsuranceCompanyName}">
                        <input type="hidden"
                               name="orderInfo.otherInsuranceCompanyId"
                               value="${orderInfo.otherInsuranceCompanyId}"/>
                        <span class="fa icon-angle-down"></span>
                    </div>
                    </div>
                </div>
            <#if !proxyId && orderInfo.proxyType !=2 && BPSHARE == 'true'>
                <div class="show-grid js-check-channel">
                    <div class="col-6">
                        <div class="form-label">
                            渠道
                        </div><div class="form-item form-data-width">
                        <input type="text"
                               name="orderInfo.channelName"
                               class="yqx-input js-show-tips"
                               title="渠道"
                               value="${orderInfo.channelName}">
                        <input type="hidden"
                               name="orderInfo.channelId"
                               value="${orderInfo.channelId}"/>
                        <span class="fa icon-angle-down"></span>
                    </div>
                    </div>
                </div>
            </#if>
            </div>
            <div class="toggle-control js-toggle-control arrow-up" data-ref="a1" data-status="show"></div>
        </div>
        <!-- 基本信息区域 end -->

        <!-- 车况信息start -->
        <#if BPSHARE == 'true' || SESSION_SHOP_JOIN_STATUS == 1 || SESSION_SHOP_WORKSHOP_STATUS == 1>
            <div class="wg-precheck">
                <div class="wgtitle">外观</div>
            <#include "yqx/tpl/order/precheck-tpl.ftl">
            </div>
        </#if>
        <!-- 车况信息end-->
        <#if proxyOrderDetailVo>
            <!--委托单 start-->
            <div class="wt-box">
                <div class="show-grid">
                    <div class="form-label">
                        委托方：${proxyOrderDetailVo.shopName}
                    </div>
                </div>
                <div class="wt-project">
                    <div class="wt-title">委托项目</div>
                    <div class="wt-list">
                        <table class="yqx-table">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>服务类型</th>
                                <th>服务项目</th>
                                <th>工时</th>
                                <th>委托金额</th>
                                <th>服务备注</th>
                            </tr>
                            </thead>
                            <tbody>
                                <#list proxyOrderDetailVo.proxyServicesVoList as proxyService>
                                <tr>
                                    <td>${proxyService_index+1}</td>
                                    <td>${proxyService.serviceType}</td>
                                    <td>${proxyService.serviceName}</td>
                                    <td>${proxyService.serviceHour}</td>
                                    <td>${proxyService.proxyAmount}</td>
                                    <td class="ellipsis-1 js-show-tips">${proxyService.serviceNote}</td>
                                </tr>
                                </#list>
                            </tbody>
                        </table>
                    </div>
                    <div class="form-label">委托金额合计：</div>
                    <div class="form-item">
                        <div class="yqx-text">
                            <#if proxyOrderDetailVo.proxyAmount><strong class="ml-15 hl-o" id="proxyAmount">#{proxyOrderDetailVo.proxyAmount;m2M2}</strong></#if>元
                        </div>
                    </div>
                </div>
                <div class="form-label">备注：</div>
                <div class="form-item">
                    <div class="yqx-text">
                        <div class="ellipsis-1 js-show-tips form-item-remark">${proxyOrderDetailVo.remark}</div>
                    </div>
                </div>
            </div>
            <!--委托单 end-->
        </#if>
        <div class="order-panel">
            <div class="order-panel-head clearfix">
                <div class="tabs-control fl">
                    <span data-ref="a1" class="tab-item current-item js-tab-item">服务项目</span><span data-ref="a2" class="tab-item js-tab-item">配件项目<i class="tip js-tip hide">有新配件<i
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
                                <div>服务名称</div>
                            </th>
                            <th class="yqx-th-8">
                                <div>服务类别</div>
                            </th>
                            <th class="yqx-th-6">
                                <div>工时费</div>
                            </th>
                            <th class="yqx-th-5">
                                <div>工时</div>
                            </th>
                            <th class="yqx-th-6">
                                <div>金额</div>
                            </th>
                            <th class="yqx-th-6">
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
                        <tbody class="yqx-tbody" id="orderServiceTB">
                        <#list basicOrderService as orderService>
                        <tr class="service-datatr" data-id="${orderService.id}">
                            <!--服务名称-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceName" class="yqx-input yqx-input-small js-show-tips"
                                           value="${orderService.serviceName}" disabled/>
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
                                           class="yqx-input yqx-input-small js-show-tips" disabled/>
                                </div>
                            </td>
                            <!--工时费-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="servicePrice" value="${orderService.servicePrice}"
                                           class="yqx-input yqx-input-small js-service-price js-float-2"/>
                                </div>
                            </td>
                            <!--工时-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceHour" value="${orderService.serviceHour}"
                                           class="yqx-input yqx-input-small js-service-hour js-float-1"/>
                                </div>
                            </td>
                            <!--金额-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceAmount" value="${orderService.serviceAmount}"
                                           class="yqx-input yqx-input-small js-service-amount js-show-tips" disabled/>
                                </div>
                            </td>
                            <!--优惠-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="discount" value="${orderService.discount}"
                                           class="yqx-input yqx-input-small js-service-discount js-float-2"/>
                                </div>
                            </td>
                            <!--维修工-->
                            <td>
                                <div class="form-item">
                                    <input type="text" value="${orderService.workerNames}"
                                           class="yqx-input yqx-input-icon js-worker yqx-input-small js-show-tips"/>
                                    <input type="hidden" name="workerIds" value="${orderService.workerIds}"/>
                                    <span class="fa icon-small icon-angle-down"></span>
                                </div>
                            </td>
                            <!--服务备注-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceNote" value="${orderService.serviceNote}"
                                           class="yqx-input yqx-input-small js-show-tips"/>
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
                            <i class="icon-layers"></i><i class="i-btn js-batch-add-part-btn">批量添加配件</i>
                            <i class="icon-plus"></i><i class="i-btn js-new-part-btn">新建配件资料</i>
                        </div>
                    </div>
                    <table class="yqx-table">
                        <thead>
                        <tr>
                            <th class="yqx-th-9">
                                <div>零件号</div>
                            </th>
                            <th>
                                <div>配件名称</div>
                            </th>
                            <th class="yqx-th-5">
                                <div>售价</div>
                            </th>
                            <th class="yqx-th-6">
                                <div>数量</div>
                            </th>
                            <th class="yqx-th-7">
                                <div>金额</div>
                            </th>
                            <th class="yqx-th-5"">
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
                        <tbody class="yqx-tbody" id="orderGoodsTB">
                        <#list orderGoodsList as orderGoods>
                        <tr class="goods-datatr">
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
                                    <input type="hidden" name="outNumber" value="<#if copyOrderId>0<#else>${orderGoods.outNumber}</#if>"/>
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
                                    <input type="text" name="goodsPrice" value="${orderGoods.goodsPrice}"
                                           class="yqx-input yqx-input-small js-goods-price js-float-2 js-show-tips"/>
                                </div>
                            </td>
                            <!--数量-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsNumber" value="${orderGoods.goodsNumber}"
                                           class="yqx-input yqx-input-small js-goods-num js-float-1" placeholder="单位"/>
                                    <input type="hidden" name="measureUnit" value="${orderGoods.measureUnit}"/>
                                    <input type="hidden" name="inventoryPrice" value="${orderGoods.inventoryPrice}"/>
                                    <i class="fa goods-fa">${orderGoods.measureUnit}</i>
                                </div>
                            </td>
                            <!--金额-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsAmount" value="${orderGoods.goodsAmount}"
                                           class="yqx-input yqx-input-small js-goods-amount js-show-tips" disabled/>
                                </div>
                            </td>
                            <!--优惠-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="discount" value="${orderGoods.discount}"
                                           class="yqx-input yqx-input-small js-goods-discount js-float-2"/>
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
                                    <span class="fa icon-small icon-angle-down"></span>
                                </div>
                            </td>
                            <!--配件备注-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsNote" value="${orderGoods.goodsNote}"
                                           class="yqx-input yqx-input-small js-show-tips"/>
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
                                <div>附加名称</div>
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
                                           class="yqx-input yqx-input-small service-name-width js-show-tips" disabled/>
                                </div>
                            </td>
                            <!--金额-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="servicePrice" value="${exService.servicePrice}"
                                           class="yqx-input yqx-input-small js-goods-amount" />
                                </div>
                            </td>
                            <!--优惠-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="discount" value="${exService.discount}"
                                           class="yqx-input yqx-input-small js-goods-discount js-float-2"/>
                                </div>
                            </td>

                            <!--备注-->
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceNote" value="${exService.serviceNote}"
                                           class="yqx-input yqx-input-small js-show-tips"/>
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
                    <div class="form-label form-label-remark">备注</div><div class="form-item form-item-remark">
                        <input class="yqx-input js-show-tips" name="orderInfo.postscript" placeholder="请填写备注信息" data-v-type="maxLength:200" value="<#if orderInfo.postscript>${orderInfo.postscript}<#elseif prechecks>${prechecks.comments}</#if>" />
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
                <#if orderInfo && orderInfo.downPayment gt 0.00>
                    <div class="form-label">预付定金：</div>
                    <div class="form-item">
                        <div class="yqx-text">
                            <strong class="ml-15 hl-o">#{orderInfo.downPayment;m2M2}</strong>元
                        </div>
                    </div>
                </#if>
                </div>
            </div>
            <div class="order-panel-foot">
                <button class="yqx-btn yqx-btn-2 js-create">
                <#if orderInfo.id>保存<#else>开单</#if>
                </button>
                <button class="yqx-btn yqx-btn-1 fr js-return">返回</button>
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
                <input type="text" name="serviceName" class="yqx-input yqx-input-small js-show-tips"
                       value="<%=orderService.name%>" disabled/>
                <input type="hidden" name="id" value=""/>
                <input type="hidden" name="serviceId" value="<%=orderService.id%>"/>
                <input type="hidden" name="serviceSn" value="<%=orderService.serviceSn%>"/>
                <input type="hidden" name="parentServiceId"
                       value="<%=orderService.parentServiceId%>"/>
                <!-- 基础服务类型 -->
                <input type="hidden" name="type" value="<%=orderService.type%>"/>
                <input type="hidden" class="input-suiteNum" value="<%=orderService.suiteNum%>">
            </div>
        </td>
        <!--服务类别-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceCatName" value="<%=orderService.serviceCatName || orderService.categoryName%>"
                       class="yqx-input yqx-input-small js-show-tips" disabled/>
                <input type="hidden" name="serviceCatId" value="<%=orderService.categoryId%>"/>
            </div>
        </td>
        <!--工时费-->
        <td>
            <div class="form-item">
                <input type="text" name="servicePrice" value="<%=orderService.servicePrice%>"
                       class="yqx-input yqx-input-small js-service-price js-float-2 js-show-tips"/>
            </div>
        </td>
        <!--工时-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceHour" value="1"
                       class="yqx-input yqx-input-small js-service-hour js-float-1"/>
            </div>
        </td>
        <!--金额-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceAmount" value="<%=orderService.servicePrice%>"
                       class="yqx-input yqx-input-small js-service-amount js-show-tips" disabled/>
            </div>
        </td>
        <!--优惠-->
        <td>
            <div class="form-item">
                <input type="text" name="discount" value="0"
                       class="yqx-input yqx-input-small js-service-discount js-float-2"/>
            </div>
        </td>
        <!--维修工-->
        <td>
            <div class="form-item yqx-downlist-wrap">
                <input type="text" value="<%=orderService.workerNames%>"
                       class="yqx-input yqx-input-icon js-worker yqx-input-small js-show-tips"/>
                <input type="hidden" name="workerIds" value="<%=orderService.workerIds%>"/>
                <span class="fa icon-small icon-angle-down"></span>
            </div>
        </td>
        <!--服务备注-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceNote" value="<%=orderService.serviceNote%>"
                       class="yqx-input yqx-input-small js-show-tips"/>
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
                       class="yqx-input yqx-input-small js-show-tips" disabled/>
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
                       class="yqx-input yqx-input-small js-show-tips" disabled/>
            </div>
        </td>
        <!--售价-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsPrice" value="<%=orderGoods.price%>"
                       class="yqx-input yqx-input-small js-goods-price js-float-2 js-show-tips"/>
            </div>
        </td>
        <!--数量-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsNumber" value="<%=orderGoods.goodsNum || 1%>"
                       class="yqx-input yqx-input-small js-goods-num js-float-1" placeholder="单位"/>
                <input type="hidden" name="measureUnit" value="<%=orderGoods.measureUnit%>"/>
                <i class="fa goods-fa"><%=orderGoods.measureUnit%></i>
            </div>
        </td>
        <!--金额-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsAmount" value="<%=(orderGoods.price * (orderGoods.goodsNum || 1))%>"
                       class="yqx-input yqx-input-small js-goods-amount js-show-tips" disabled/>
            </div>
        </td>
        <!--优惠-->
        <td>
            <div class="form-item">
                <input type="text" name="discount" value="0"
                       class="yqx-input yqx-input-small js-goods-discount js-float-2"/>
            </div>
        </td>
        <!--库存-->
        <td>
            <div class="form-item">
                <input type="text" name="stock" value="<%=orderGoods.stock%>" class="yqx-input yqx-input-small js-show-tips"
                       disabled/>
            </div>
        </td>
        <!--销售员-->
        <td>
            <div class="form-item yqx-downlist-wrap">
                <input type="text" class="yqx-input yqx-input-icon js-speedily-sale yqx-input-small js-show-tips"/>
                <input type="hidden" name="saleId"/>
                <span class="fa icon-angle-down"></span>
            </div>
        </td>
        <!--配件备注-->
        <td>
            <div class="form-item">
                <input type="text" name="goodsNote" value="<%=orderGoods.goodsNote%>"
                       class="yqx-input yqx-input-small js-show-tips"/>
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
                       class="yqx-input yqx-input-small service-name-width js-show-tips" disabled/>
            </div>
        </td>
        <!--金额-->
        <td>
            <div class="form-item">
                <input type="text" name="servicePrice" value="<%=orderService.servicePrice%>"
                       class="yqx-input yqx-input-small js-goods-amount js-show-tips" />
            </div>
        </td>
        <!--优惠-->
        <td>
            <div class="form-item">
                <input type="text" name="discount" value="0"
                       class="yqx-input yqx-input-small js-goods-discount js-float-2"/>
            </div>
        </td>
        <!--备注-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceNote" value="<%=orderService.serviceNote%>"
                       class="yqx-input yqx-input-small js-show-tips"/>
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
<!-- 添加物料模版 -->
<#include "yqx/tpl/common/goods-add-tpl.ftl">
<!-- 添加附加费用模版 -->
<#include "yqx/tpl/common/get-additional-tpl.ftl">
<!-- 批量添加物料模板 -->
<#include "yqx/tpl/order/batch-add-part-tpl.ftl">
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

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script src="${BASE_PATH}/static/js/page/order/common-add.js?9c0fbdd39c4743b560d9a4866979b965"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
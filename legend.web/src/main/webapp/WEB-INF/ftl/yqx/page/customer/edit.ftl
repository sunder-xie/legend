<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/customer/edit.css?7723ae403139fe92791e122595e6f55b"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    <#--车型选择的dialog-->
    <#include "yqx/tpl/common/car-type-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline" >
                <#if null == carDetailVo.customerCar.id>
                    新建车辆
                <#else>
                    完善车辆
                </#if>
            </h3>
        </div>
        <form id="customerInfo">
            <div class="complete-sec">
                <div class="complete-form">
                    <div class="form-line">
                        <input type="hidden" name="id" id="id"
                               value="${carDetailVo.customerCar.id}"/>
                        <input type="hidden" name="customerOldName"
                               value="${carDetailVo.customer.customerName}"/>
                        <input type="hidden" name="customerId"
                               value="${carDetailVo.customerCar.customerId}"/>


                        <div class="form-label form-label-must">车牌</div>
                        <div class="form-item">
                            <input type="text" name="license" class="yqx-input js-check-license" data-v-type="required | licence" data-label="车牌"
                                   value="${license}"
                                   placeholder="请输入车牌">
                        </div>
                        <div class="form-label form-label-must">车主</div>
                        <div class="form-item">
                            <input type="text" name="customerName" class="yqx-input" data-v-type="required | maxLength:50 " data-label="车主"
                                   value="${carDetailVo.customer.customerName}"
                                   placeholder="请输入车主">
                        </div>
                        <div class="form-label">车主电话</div>
                        <div class="form-item form-data-width car-from-item">
                            <input type="text" id="mobile" name="mobile" class="yqx-input" data-v-type="phone " data-label="车主电话"
                                   value="${carDetailVo.customer.mobile}"
                                    <#if carDetailVo.customer.mobile>
                                        disabled
                                    </#if>
                                   placeholder="请输入车主电话">
                        </div>
                    </div>
                    <div class="form-line">
                        <div class="form-label notice-label">VIN码<img src="${BASE_PATH}/static/img/common/base/question.png" id="notice" class="notice"/></div>
                        <div class="form-item form-data-width">
                            <input type="text" name="vin" class="yqx-input" data-v-type="vin" maxlength="17" data-label="VIN码"
                                   value="${carDetailVo.customerCar.vin}"
                                   placeholder="VIN码匹配车型">
                        </div>

                        <div class="form-label form-label-must">车型</div>
                        <div class="form-item yqx-downlist-wrap car-from-item">
                        <#--车型字段start-->
                            <input type="hidden" name="carBrandId" value="${carDetailVo.customerCar.carBrandId}"/>
                            <input type="hidden" name="carBrand" value="${carDetailVo.customerCar.carBrand}"/>
                            <input type="hidden" name="carSeriesId" value="${carDetailVo.customerCar.carSeriesId}"/>
                            <input type="hidden" name="carSeries" value="${carDetailVo.customerCar.carSeries}"/>
                            <input type="hidden" name="carModelId" value="${carDetailVo.customerCar.carModelId}"/>
                            <input type="hidden" name="carModel" value="${carDetailVo.customerCar.carModel}"/>
                            <input type="hidden" name="importInfo" value="${carDetailVo.customerCar.importInfo}"/>
                        <#--车型字段end-->
                            <input type="text" class="yqx-input js-show-tips" id="carModel" data-v-type="required " data-label="车型"
                                   value="${carDetailVo.customerCar.carInfo}"
                                   placeholder="请选择车型">
                        </div>
                        <div class="form-item">
                            <button type="button" class="yqx-btn yqx-btn-1 js-car-type">
                                选择车型
                            </button>
                        </div>
                    </div>
                    <div class="form-line">
                        <div class="form-label form-label">年款排量</div>
                        <div class="form-item yqx-downlist-wrap form-data-width">
                        <#--年款排量字段start-->
                            <input type="hidden" name="carYear" value="${carDetailVo.customerCar.carYear}"/>
                            <input type="hidden" name="carYearId" value="${carDetailVo.customerCar.carYearId}"/>
                            <input type="hidden" name="carPower" value="${carDetailVo.customerCar.carPower}"/>
                            <input type="hidden" name="carPowerId" value="${carDetailVo.customerCar.carPowerId}"/>
                            <input type="hidden" name="carGearBox" value="${carDetailVo.customerCar.carGearBox}"/>
                            <input type="hidden" name="carGearBoxId" value="${carDetailVo.customerCar.carGearBoxId}"/>
                        <#--年款排量字段end-->
                            <input type="text" id="yearPowerBak" class="yqx-input js-show-tips" placeholder="" disabled
                                   <#if carDetailVo.customerCar.carGearBox>
                                   value="${carDetailVo.customerCar.carYear}&nbsp;${carDetailVo.customerCar.carGearBox}">
                                    <#else>
                                    value="${carDetailVo.customerCar.carYear}&nbsp;${carDetailVo.customerCar.carPower}">
                                   </#if>
                        </div>
                        <div class="form-label">客户单位</div>
                        <div class="form-item form-data-width yqx-downlist-wrap">
                            <input type="text" name="company" class="yqx-input js-show-tips" data-v-type='maxLength:100'
                                   value="${carDetailVo.customer.company}"
                                   placeholder="请输入客户单位">
                        </div>
                    </div>
                </div>
                <!-- 输入部分结束 -->
            </div>
            <div class="complete-content">
            <div class="orderdetail-nav">
                <div class="order-title">车辆详情</div>
            </div>
            <div class="margin-body car-info">
                <div class="body-section">
                    <div class="order-subtitle">基本信息</div>
                    <div class="form-line">
                        <#--车辆级别、车辆别名、行驶里程、品牌型号、-->
                        <div class="form-label form-label">车辆级别</div><div class="form-item">
                            <input type="text" name="carLevel" class="yqx-input" disabled="disabled"
                                   value="${carDetailVo.customerCar.carLevel}"
                                   placeholder="">
                        </div><div class="form-label">车辆别名</div><div class="form-item">
                            <input type="text" name="byName" class="yqx-input" data-v-type='maxLength:20'
                                   value="${carDetailVo.customerCar.byName}"
                                   placeholder="">
                        </div><div class="form-label">行驶里程</div><div class="form-item">
                            <input type="text" name="mileage" class="yqx-input js-number" data-v-type="integer| maxLength:8" maxlength="8" data-label="行驶里程"
                                   value="${carDetailVo.customerCar.mileage}"
                                   placeholder="">
                            <span class="fa">km</span>
                        </div><div class="form-label">品牌型号</div><div class="form-item">
                            <input type="text" name="carNumber" class="yqx-input js-show-tips" data-v-type="maxLength:50"
                                   value="${carDetailVo.customerCar.carNumber}"
                                   placeholder="行驶证品牌型号">
                        </div>
                    </div>
                    <div class="form-line">
                    <#--保险到期、年审日期、领证日期、购车日期、-->
                    <div class="form-label">保险到期</div><div class="form-item">
                        <input type="text" name="insuranceTimeStr" class="yqx-input js-date-2"
                               value="${carDetailVo.customerCar.insuranceTimeStr}"
                               placeholder="请选择时间">
                        <span class="fa icon-calendar"></span>
                    </div><div class="form-label">年审日期</div><div class="form-item">
                        <input type="text" name="auditingTimeStr" class="yqx-input js-date-2    "
                               value="${carDetailVo.customerCar.auditingTimeStr}"
                               placeholder="请选择时间">
                        <span class="fa icon-calendar"></span>
                    </div><div class="form-label">领证日期</div><div class="form-item">
                        <input type="text" name="receiveLicenseTimeStr" class="yqx-input js-date"
                               value="${carDetailVo.customerCar.receiveLicenseTimeStr}" placeholder="行驶证日期">
                        <span class="fa icon-calendar"></span>
                    </div><div class="form-label">购车日期</div><div class="form-item">
                        <input type="text" name="buyTimeStr" class="yqx-input js-date"
                               value="${carDetailVo.customerCar.buyTimeStr}"
                               placeholder="请选择时间">
                        <span class="fa icon-calendar"></span>
                    </div>
                    </div>
                    <div class="form-line">
                        <#--发动机号、出厂年月、承保公司、车身颜色、-->
                        <div class="form-label">承保公司</div><div class="form-item">
                            <div class="form-item">
                                <input type="text" name="insuranceCompany" class="yqx-input yqx-input-icon js-select js-show-tips" value="${carDetailVo.customerCar.insuranceCompany}"
                                       placeholder="输入查询">
                                <span class="fa icon-angle-down"></span>
                                <input type="hidden" name="insuranceId" value="${carDetailVo.customerCar.insuranceId}"/>
                            </div>
                        </div><div class="form-label">出厂年月</div><div class="form-item">
                            <input type="text" name="productionDateStr" class="yqx-input js-date-month2"
                                   value="${carDetailVo.customerCar.productionDateStr}"
                                   placeholder="请选择时间">
                            <span class="fa icon-calendar"></span>
                        </div><div class="form-label">车辆颜色</div><div class="form-item">
                                    <input type="text" name="color" class="yqx-input" data-v-type='maxLength:10'
                                           value="${carDetailVo.customerCar.color}"
                                           placeholder="例如：红色">
                        </div><div class="form-label">发动机号</div><div class="form-item">
                            <input type="text" name="engineNo" class="yqx-input" data-v-type='engine | maxLength:45'
                                   value="${carDetailVo.customerCar.engineNo}"
                                   placeholder="发动机识别码">
                        </div>
                    </div>
                    <div class="form-line">
                        <div class="form-label">牌照类型</div><div class="form-item">
                            <input type="text" name="licenseType" class="yqx-input"
                                   value="${carDetailVo.customerCar.licenseType}"
                                   data-v-type="maxLength:2" placeholder="本地/外地">
                        </div><div class="form-label">下次保养时间</div><div class="form-item">
                            <input type="text" name="keepupTimeStr" class="yqx-input js-date-min"
                                   value="${carDetailVo.customerCar.keepupTimeStr}"
                                   placeholder="请选择时间">
                            <span class="fa icon-calendar"></span>
                        </div><div class="form-label">下次保养里程</div><div class="form-item">
                        <input type="text" name="upkeepMileage" class="yqx-input js-number" data-v-type="integer| maxLength:8" maxlength="8" data-label="下次保养里程"
                               value="${carDetailVo.customerCar.upkeepMileage}">
                        <span class="fa">km</span>
                    </div>
                    </div>
                </div>
                <div class="body-section">
                    <div class="order-subtitle">客户信息</div>
                    <#--联系人、联系电话、客户生日、客户来源、-->
                    <div class="form-line">
                            <div class="form-label">联系人</div><div class="form-item">
                                <input type="text" name="contact" class="yqx-input"
                                       data-v-type='maxLength:50' data-label="联系人"
                                       value="${carDetailVo.customer.contact}"
                                       placeholder="请输入联系人">
                            </div><div class="form-label">联系电话</div><div class="form-item">
                                <input type="text" name="contactMobile" class="yqx-input"
                                       data-v-type='phone' data-label="联系电话"
                                       value="${carDetailVo.customer.contactMobile}"
                                       placeholder="请输入联系电话">
                            </div><div class="form-label">固定电话</div><div class="form-item">
                                <input type="text" name="tel" class="yqx-input"
                                       maxlength="15" data-label="固定电话"
                                       value="${carDetailVo.customer.tel}"
                                       placeholder="请输入固定电话">
                            </div><div class="form-label">客户生日</div><div class="form-item form-item-width">
                                <input type="text" name="birthdayStr" class="yqx-input yqx-input-icon js-date"
                                       value="${carDetailVo.customer.birthdayStr}"
                                       placeholder="请选择时间">
                                <span class="fa icon-calendar"></span>
                            </div>
                        </div>
                    <div class="form-line">
                    <#--身份号、驾照号、客户地址-->
                        <div class="form-label">客户来源</div><div class="form-item">
                            <input type="text" name="customerSource" class="yqx-input source-select"
                                   value="${carDetailVo.customer.source}"
                                   placeholder="">
                        </div><div class="form-label">身份证号</div><div class="form-item">
                            <input type="text" name="identityCard" class="yqx-input" data-v-type='maxLength:20' data-label="身份证号"
                                   value="${carDetailVo.customer.identityCard}"
                                   placeholder="">
                        </div><div class="form-label">驾驶证号</div><div class="form-item input-address">
                            <input type="text" name="drivingLicense" class="yqx-input" data-v-type='maxLength:50' data-label="驾驶证号"
                                   value="${carDetailVo.customer.drivingLicense}"
                                   placeholder="">
                        </div>
                    </div>
                    <div class="form-line">
                        <div class="form-label">客户地址</div><div class="form-item input-address">
                            <input type="text" name="customerAddr" class="yqx-input js-show-tips"
                                   data-v-type='maxLength:50' data-label="客户地址"
                                   value="${carDetailVo.customer.customerAddr}"
                                   placeholder="请输入地址信息">
                        </div><div class="form-label">备注</div><div class="form-item input-address">
                            <input type="text" name="remark" class="yqx-input" data-v-type='maxLength:200' data-label="备注"
                               value="${carDetailVo.customer.remark}" placeholder="请输入备注">
                        </div>
                    </div>
                </div>
                <div class="foot-btns clearfix">
                    <button type="button" class="yqx-btn yqx-btn-2 foot-btn js-save-btn">保存</button>
                    <button type="button" class="yqx-btn yqx-btn-1 foot-btn js-return-btn" >返回</button>
                </div>
            </div>
        </div>
        </form>
    </div>

</div>
<!-- 右侧内容区 end -->

<script id="yearPowerDLTpl" type="text/html">
    <% if (templateData&&templateData.length) { %>
    <% var index = 0, name; %>
    <ul class="yqx-downlist-content js-downlist-content">
        <%
        for (var i = 0; i < templateData.length; i++) {
            var item = templateData[i];
            index = 0;
        %>
        <li class="js-downlist-item js-show-tips">
            <span style="width: 200px;" title="<%= item['year'] + ' ' + item['gearbox'] %>"><%= item['year'] + ' ' + item['gearbox'] %></span>
        </li>
        <% } %>
    </ul>
    <% } else { %>
    <p class="yqx-dl-no-data">暂无数据</p>
    <% } %>
</script>

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script src="${BASE_PATH}/static/js/page/customer/edit.js?da508ea13d1ec9495b7ed9d28cd5581f"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
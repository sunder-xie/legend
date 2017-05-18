<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/carwash-perfect.css?a39e48498357daeb9c20c159999d17b7"/>
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right">
        <div class="order-head clearfix">
        <h1 class="headline fl">工单查询-<small>洗车单详情-完善信息</small></h1>
        </div>
        <div class="yqx-group" id="form">
            <#--group标题end-->
            <div class="yqx-group-head">
                <ul id="information" class="clearfix">
                <#--工单编号-->
                    <li class="js-show-tips">
                        <div class="form-label">
                           <span class="num"> 工单编号:</span>
                        </div>
                        <div class="form-item">
                            <div class="yqx-text">
                            <span class="num">${formEntity.orderSn}</span><div class="form-label num">(开单日期:</div>
                                <div class="form-item">
                                    <div class="yqx-text num">${formEntity.createTimeStr})</div>
                                </div>
                            </div>
                        </div>
                    </li>

                    <#--车牌-->
                    <li>
                        <div class="form-label">
                            车牌:
                        </div>
                        <div class="form-item yqx-downlist-wrap">
                            <input type="text" class="yqx-input js-carlicense-input"
                                                                          data-label="车牌" name="carLicense" value="${formEntity.carLicense}"  data-v-type="licence" />
                        </div>
                    </li>


                    <#--服务顾问-->
                    <li>
                        <div class="form-label">
                            服务顾问:
                        </div>
                        <div class="form-item">
                            <div class="yqx-text">${formEntity.receiverName}</div>
                        </div>
                    </li>

                    <#--洗车工-->
                    <li>
                        <div class="form-label">
                            洗车工:
                        </div>
                        <div class="form-item">
                            <div class="yqx-text js-show-tips wash ellipsis-1">${formEntity.workerNames}</div>
                        </div>
                    </li>

                    <#--收款金额-->
                    <li>
                        <div class="form-label">
                            收款金额:
                        </div>
                        <div class="form-item">
                            <div class="yqx-text js-show-tips"><span>${formEntity.orderAmount}</span>元</div>
                        </div>
                    </li>
                </ul>
            </div>
            <#--group内容start-->
            <#--group标题start-->
                <div class="yqx-group-content">
                    <div >
                        <input type="hidden" name="orderId" value="${formEntity.orderId}" />
                        <input type="hidden" name="customerCarId" value="${formEntity.customerCarId}" id="customerCarId"/>
                        <#--信息输入-->
                        <ul id="input" class="clearfix">
                            <li>
                                <div class="form-label">VIN码</div>
                                <div class="form-item">
                                    <input type="text" class="yqx-input" name="vin" value="${formEntity.vin}"  data-v-type="vin" data-label="vin码" placeholder="请输入VIN码" maxlength="17"/>
                                </div>
                            </li>
                            <li>
                                <div class="form-label">车型</div>
                                <div class="form-item yqx-downlist-wrap">
                                    <input type="text" class="yqx-input car-model-input js-show-tips" name="carModeBak" value="${formEntity.carInfo}"/><button
                                        type="button" class="yqx-btn yqx-btn-1 js-car-type">选择车型</button>
                                    <!-- 品牌 -->
                                    <input type="hidden" name="carBrandId" value="${formEntity.carBrandId}"/>
                                    <input type="hidden" name="carBrand" value="${formEntity.carBrand}"/>
                                    <!-- 车系 -->
                                    <input type="hidden" name="carSeriesId" value="${formEntity.carSeriesId}"/>
                                    <input type="hidden" name="carSeries" value="${formEntity.carSeries}"/>
                                    <!-- 车型 -->
                                    <input type="hidden" name="carModelId" value="${formEntity.carModelId}"/>
                                    <input type="hidden" name="carModel" value="${formEntity.carModel}"/>
                                    <!-- 进口与国产 -->
                                    <input type="hidden" name="importInfo" value="${formEntity.importInfo}"/>
                                </div>

                            </li>
                            <li>
                                <div class="form-label">年款排量</div>
                                <div class="form-item yqx-downlist-wrap">
                                    <input type="text" class="yqx-input" name="yearPowerBak" value="${formEntity.carYear}&nbsp;<#if formEntity.carGearBox>${formEntity.carGearBox}<#else>${formEntity.carPower}</#if>" disabled/>
                                    <input type="hidden" name="carYear" value="${formEntity.carYear}"/>
                                    <input type="hidden" name="carYearId" value="${formEntity.carYearId}"/>
                                    <input type="hidden" name="carPower" value="${formEntity.carPower}"/>
                                    <input type="hidden" name="carPowerId" value="${formEntity.carPowerId}"/>
                                    <input type="hidden" name="carGearBox" value="${formEntity.carGearBox}"/>
                                    <input type="hidden" name="carGearBoxId" value="${formEntity.carGearBoxId}"/>
                                </div>
                            </li>


                            <li>
                                <div class="form-label">联系人</div>
                                <div class="form-item">
                                    <input type="text" class="yqx-input" data-v-type="maxLength:20" name="contactName" value="${formEntity.contactName}" placeholder="请输入联系人"/>
                                </div>
                            </li>
                            <li>
                                <div class="form-label">联系电话</div>
                                <div class="form-item">
                                    <input type="text" class="yqx-input" name="contactMobile" value="${formEntity.contactMobile}" placeholder="请输入联系电话" data-v-type="phone" data-label="联系电话"/>
                                </div>
                            </li>
                            <li>
                                <div class="form-label">行驶里程</div>
                                <div class="form-item">
                                    <input type="text" class="yqx-input yqx-input-icon" data-v-type="integer|maxLength:8" maxlength="8" name="mileage" value="${formEntity.mileage}" placeholder="请输入行驶里程"/>
                                    <span class="fa">km</span>
                                </div>
                            </li>
                            <li>
                                <div class="form-label">客户单位</div>
                                <div class="form-item form-item-company">
                                    <input type="text" class="yqx-input js-show-tips" name="company" value="${formEntity.company}" data-v-type="maxLength:100" maxlength="100" placeholder="请输入客户单位"/>
                                </div>
                            </li>
                        </ul>
                            <#--信息输入end-->
                            <#--添加内容-->
                        <div id="add">
                            <#--添加图片-->
                            <div id="addImg" class="clearfix">
                                <span>添加照片</span>
                                <div class="addDiv">
                                    <img class="example" src="${BASE_PATH}/static/img/common/base/tqyx_img.png"/>
                                    <img class="close" src="${BASE_PATH}/static/img/page/order/carwashPrefect/close.png"/>
                                </div>

                            <input type="hidden" name="carLicensePicture" value="${formEntity.carLicensePicture}"/>
                            <#if formEntity.carLicensePicture>
                                <div class="info-text">
                                    <div class="info-td"><img class="car-img" src="${formEntity.carLicensePicture}"/>
                                    <img class="close" src="${BASE_PATH}/static/img/page/order/carwashPrefect/close.png"/>
                                    </div>
                                </div>
                            </#if>
                            <div class="fileBtn">
                                <input type="file" id="fileBtn">
                            </div>
                            </div>
                                <#--添加信息-->
                            <div id="addInf">
                               <div class="form-label">备注</div>
                                <div class="form-item">
                                    <input class="yqx-textarea js-show-tips" name="postscript" value="${formEntity.postscript}" data-v-type="maxLength:200"/>
                                </div>
                            </div>
                        </div>
                            <#--添加内容end-->
                        <div id="submit">
                            <input type="submit" value="提交" class="yqx-btn yqx-btn-2 submit"/>
                            <input type="button" value="取消" class="yqx-btn yqx-btn-1 cancel"/>
                            <input type="button" value="返回" class="yqx-btn yqx-btn-1 js-return fr"/>
                        </div>
                    </div>
                </div>
            <#--group内容end-->
        </div>
    </div>
    <!-- 右侧内容区 end -->
</div>

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
<script  src="${BASE_PATH}/static/js/page/order/carwash-perfect.js?ad082912b3c71d6de946ad144e030856"></script>

<!-- 脚本引入区 end -->

<#--车型选择的dialog-->
<#include "yqx/tpl/common/car-type-tpl.ftl">

<#include "yqx/layout/footer.ftl">
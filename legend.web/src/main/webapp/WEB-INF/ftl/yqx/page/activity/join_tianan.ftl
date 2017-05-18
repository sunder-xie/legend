<#--<#include "layout/ng-header.ftl" >-->
<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/activity/join_tianan.css?2b2180cf8656e59428b78f71aea8a61f"/>

<div class="yqx-wrapper clearfix">
    <div class="left-menu" style="float: left;">
        <#--<#include "activity/activity_left.ftl" >-->
       <#include "yqx/tpl/activity/activity-left.ftl">
    </div>

    <div class="main">
        <div class="head clearfix">
            <h1 class="headline">天安合作快速核销<strong><#if orderInfo>---<span class="color${orderInfo.auditStatus}">${orderInfo.auditStatusName}</span></#if></strong></h1>
            <div class="order_info">
            <#if orderInfo><#if order><label>工单编号：<strong><span style="color:#8836b2;cursor: pointer" onclick="window.location.href='${BASE_PATH}/shop/order/detail?orderId=${order.id}'">${order.orderSn}</span></strong></label></#if><label>服务时间：<strong>${orderInfo.gmtCreateStr}</strong></#if></label>
            </div>
            </div>
        <div>
            <ul class="flow">
                <li content="1">创建天安合作服务单</li>
                <li content="2">系统自动生成工单</li>
                <li content="3">天安合作服务单审核</li>
                <li content="4">天安合作服务单对账</li>
            </ul>
        </div>

        <div class="text-content" id="formData">
            <input type="hidden" name="shopActId" value="${shopActId}"/>
            <input type="hidden" name="actTplId" value="${actTplId}"/>
            <#--<input type="hidden" name="billType" value="PINGAN.BAOYANG"/>-->
            <input type="hidden" name="orderId" value="${orderInfo.orderId}"/>
            <input type="hidden" name="customerCarId" value="${orderInfo.customerCarId}"/>
            <input type="hidden" name="id" value="${orderInfo.id}"/>
            <input type="hidden" name="serviceId" value="${orderInfo.serviceId}"/>
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        车牌
                    </div>
                    <div class="form-item form-item-long">
                        <input type="text" name="carLicense" class="yqx-input js-car-licence" value="${orderInfo.carLicense}" placeholder="请输入车牌" data-v-type="required | licence">
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label form-label-must">
                        车主
                    </div>
                    <div class="form-item">
                        <input type="text" name="customerName" class="yqx-input" value="${orderInfo.customerName}" placeholder="车主姓名" data-v-type="required">
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label form-label-must">
                        车主电话
                    </div>
                    <div class="form-item">
                        <input type="text" name="customerMobile" class="yqx-input" value="${orderInfo.customerMobile}" placeholder="车主电话" data-v-type="required | phone">
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label">
                        车辆型号
                    </div>
                    <div class="form-item form-item-long vehicle">
                        <!-- 品牌 -->
                        <input type="hidden" name="carBrandId" value="${orderInfo.carBrandId}"/>
                        <input type="hidden" name="carBrand" value="${orderInfo.carBrand}"/>
                        <!-- 车系 -->
                        <input type="hidden" name="carSeriesId" value="${orderInfo.carSeriesId}"/>
                        <input type="hidden" name="carSeries" value="${orderInfo.carSeries}"/>
                        <!-- 车辆型号 -->
                        <input type="hidden" name="carModelsId" value="${orderInfo.carModelsId}"/>
                        <input type="hidden" name="carModels" value="${orderInfo.carModels}"/>
                        <!-- 进口与国产 -->
                        <input type="hidden" name="importInfo" value="${orderInfo.importInfo}"/>
                        <!-- ]] hidden -->
                        <div class="car-model">
                            <input type="text" class="yqx-input vehicle-text"  id="carModel" value="${orderInfo.carInfo}" placeholder="请选择车辆型号">
                        </div>
                            <a href="javascript:;" class="js-car-type btn">选择车型</a>
                    </div>
                </div>
                <div class="col-3 yearPowerBak-box">
                    <div class="form-label">
                        年款排量
                    </div>
                    <div class="form-item">
                        <input type="text" id="yearPowerBak" class="yqx-input" value="${orderInfo.carYear} <#if orderInfo.carGearBox>${orderInfo.carGearBox}<#else>${orderInfo.carPower}</#if>" placeholder="请选择年款排量">
                        <!-- 年款 -->
                        <input type="hidden" name="carYear" value="${orderInfo.carYear}"/>
                        <input type="hidden" name="carYearId" value="${orderInfo.carYearId}"/>
                        <!-- 排量 -->
                        <input type="hidden" name="carPower" value="${orderInfo.carPower}"/>
                        <input type="hidden" name="carPowerId" value="${orderInfo.carPowerId}"/>
                        <!-- 变速箱 -->
                        <input type="hidden" name="carGearBox" value="${orderInfo.carGearBox}"/>
                        <input type="hidden" name="carGearBoxId" value="${orderInfo.carGearBoxId}"/>
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label">
                        行驶里程
                    </div>
                    <div class="form-item">
                        <input type="text" name="mileage" class="yqx-input yqx-input-icon" value="${orderInfo.mileage}" placeholder="请输入数字" data-v-type="number">
                        <span class="fa">km</span>
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label">
                        客户单位
                    </div>
                    <div class="form-item form-item-long">
                        <input type="text" name="company" class="yqx-input" value="${orderInfo.company}" placeholder="请输入客户单位">
                    </div>
                </div>
                <div class="col-6">
                    <div class="form-label">
                        VIN码
                    </div>
                    <div class="form-item form-item-long">
                        <input type="text" name="vin" class="yqx-input" value="${orderInfo.vin}" placeholder="请输入VIN码" data-v-type="vin">
                    </div>
                </div>
            </div>

            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        受损部位
                    </div>
                    <div class="form-item form-item-long">
                        <input type="text" name="woundPart" class="yqx-input" value="${orderInfo.woundPart}" placeholder="请输入受损部位" data-v-type="required">
                    </div>
                </div>
                <div class="col-6">
                    <div class="form-label form-label-must">
                        核销码
                    </div>
                    <div class="form-item form-item-long vehicle vehicle-num">
                        <input type="text" name="verificationCode" class="yqx-input js-vehicle-num" disabled value="${orderInfo.carLicense}" placeholder="自动读取车牌信息" data-v-type="required | licence">
                        <a href="javascript:;" class="btn js-search">查询</a>
                    </div>
                </div>
            </div>
        </div>
            <div class="group-row clearfix">
                <div class="group-title">服务项目</div>
                <div class="container">
                    <table class="yqx-table">
                        <thead>
                        <tr>
                            <th>服务名称</th>
                            <th>服务类别</th>
                            <th>工时费</th>
                            <th>本次核销次数</th>
                            <th>剩余核销次数</th>
                        </tr>
                        </thead>
                        <tbody id="serviceCon">

                        </tbody>
                    </table>
                </div>
            </div>

            <div class="group_row clearfix">
                <div class="group-title">照片</div>
                <div class="group_photo" id="addPhoto">
                    <div class="photo_box notDel">
                        <div class="img_item_inner">
                        <#-- 默认图片 -->
                            <#if orderInfo.imgUrl>
                            <img src="${orderInfo.imgUrl}" alt="" width="120" height="120" data-content="true"  class="fileCallbackData car-imgurl">
                            <#else>
                            <img src="${BASE_PATH}/static/img/page/activity/img_u1.jpg" alt="" data-content="" width="120" height="120" class="fileCallbackData car-imgurl default"/>
                            </#if>
                        </div>
                        <div class="upload-btn js-upload-btn">
                            上传车牌照片
                            <input type="file" class="file-btn">
                        </div>

                    </div>
                    <div class="photo-data-list notDel">
                        <div class="photo_box">
                            <div class="img_item_inner" >
                            <#-- 默认图片 -->
                                <#if orderInfo.woundSnapshoot>
                                <img src="${orderInfo.woundSnapshoot}"class="fileCallbackData woundSnapshoot" data-content="true"  alt="" width="120" height="120"/>
                                <#else>
                                    <img src="${BASE_PATH}/static/img/page/activity/img_u2.jpg" class="fileCallbackData woundSnapshoot" data-content="" alt="" width="120" height="120" data-content=""/>
                                </#if>
                                </div>
                            <div class="upload-btn js-upload-btn">
                                上传受损部位照片
                                <input type="file" class="file-btn">
                            </div>

                        </div>
                        <div class="photo_box">
                            <div class="img_item_inner">
                            <#-- 默认图片 -->
                                <#if orderInfo.acceptanceSnapshoot>
                                    <img src="${orderInfo.acceptanceSnapshoot}" alt="" data-content="true" width="120" height="120" class="fileCallbackData acceptanceSnapshoot">
                                <#else>
                                    <img src="${BASE_PATH}/static/img/page/activity/img_u3.jpg" data-content="" alt="" width="120" height="120" class="fileCallbackData acceptanceSnapshoot" data-content=""/>
                                </#if>
                            </div>
                            <div class="upload-btn js-upload-btn">
                                上传服务后照片
                                <input type="file" class="file-btn">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="group_row group-padding">
                <div class="group-title">服务备注</div>
                <div class="form-item">
                    <textarea class="yqx-textarea textarea-width" name="billNote" >${orderInfo.billNote}</textarea>
                </div>
            </div>

            <div class="btn-box">
            <#if orderInfo.id gt 0>
                <#if orderInfo.auditStatus == 0>
                    <button class="yqx-btn yqx-btn-2 js-update" type="button">编辑保存</button>
                </#if>
            <#else>
                <button class="yqx-btn yqx-btn-2 js-save" type="button">保存</button>
            </#if>
            <#if orderInfo.id gt 0>
                <#if orderInfo.auditStatus == 0>
                    <button class="yqx-btn yqx-btn-2 js-submit" type="button">提交审核</button>

                </#if>
                <#if orderInfo.auditStatus == 3>
                    <button class="yqx-btn yqx-btn-2 js-resubmit" type="button">提交审核</button>
                </#if>
                <button class="yqx-btn yqx-btn-2" type="button" onclick="util.print('${BASE_PATH}/insurance/tianan/join_tianan_print?billid=${orderInfo.id}')">打印</button>
            </#if>
                <button href="javascript:;" class="yqx-btn yqx-btn-1 js-return">返回</button>
            </div>
    </div>
</div>


<!--服务项目-->
<script type="text/html" id="serviceTpl">
    <%if(json.data){%>
    <%var item = json.data%>
    <tr>
        <td>
            <div class="max-width js-show-tips"><%=item.serviceName%></div>
        </td>
        <td>
            <div class="max-width js-show-tips"><%=item.serviceCate%></div>
        </td>
        <td><%=item.settlePrice%></td>
        <td>
            <div class="form-item">
                <input type="text" name="usedTime" class="yqx-input yqx-input-small timework" value="" placeholder="" data-v-type="integer">
            </div>
        </td>
        <td class="js-surplus"><%=item.totalCount - item.usedCount%></td>
    </tr>
    <%}%>
</script>


<!--照片模板-->
<script type="text/html" id="addPhotoTpl">
    <div class="photo-data-list">
        <div class="photo_box">
            <div class="img_item_inner" >
            <#-- 默认图片 -->
            <#if orderInfo.woundSnapshoot>
                <img src="${orderInfo.woundSnapshoot}"class="fileCallbackData woundSnapshoot" alt="" width="120" height="120"/>
            <#else>
                <img src="${BASE_PATH}/static/img/page/activity/img_u2.jpg"  class="fileCallbackData woundSnapshoot" alt="" width="120" height="120" data-content=""/>
            </#if>
            </div>
            <div class="upload-btn js-upload-btn">
                上传受损部位照片
                <input type="file" class="file-btn">
            </div>

        </div>
        <div class="photo_box">
            <div class="img_item_inner">
            <#-- 默认图片 -->
            <#if orderInfo.acceptanceSnapshoot>
                <img src="${orderInfo.acceptanceSnapshoot}" alt="" width="120" height="120"  class="fileCallbackData acceptanceSnapshoot">
            <#else>
                <img src="${BASE_PATH}/static/img/page/activity/img_u3.jpg" alt="" width="120" height="120" class="fileCallbackData acceptanceSnapshoot" data-content=""/>
            </#if>
            </div>
            <div class="upload-btn js-upload-btn">
                上传服务后照片
                <input type="file" class="file-btn">
            </div>
        </div>
    </div>
</script>

<!-- 年款排量 -->
<script id="yearPowerDLTpl" type="text/html">
    <% var hasData = false;%>
    <ul class="yqx-downlist-content js-downlist-content">
        <% if (templateData&&templateData.length) { %>
        <% var index = 0, name; %>
        <%
        for (var i = 0; i < templateData.length && templateData[i].gearbox; i++) {
        var item = templateData[i];
        index = 0;
        %>
        <li class="js-downlist-item">
            <span class="js-show-tips"><%= item['year'] + ' ' + item['gearbox'] %></span>
        </li>
        <% hasData = true;%>
        <% } %>
        <% }%>
        <%if(hasData == false){ %>
        <li class="yqx-dl-no-data"><span>暂无数据</span></li>
        <% } %>
    </ul>

</script>

<!--车型-->
<#include "yqx/tpl/common/car-type-tpl.ftl">
<!-- 车牌模板 -->
<#--<#include "yqx/activity/car-licence-tpl.ftl">-->
<#include  "yqx/page/activity/car-licence-tpl.ftl">
<script type="text/javascript" src="${BASE_PATH}/static/js/page/activity/join_tianan.js?9bf0a7312976507c179da9ba0f61c139"></script>
<#include "yqx/layout/footer.ftl">

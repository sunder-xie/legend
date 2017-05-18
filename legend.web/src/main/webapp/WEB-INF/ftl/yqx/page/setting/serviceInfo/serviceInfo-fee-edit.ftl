<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/serviceInfo/serviceInfo-fee-edit.css?4f03ca85fa1fcb6fbcb1363dee5a7869"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">服务资料<small>－<#if shopServiceInfo.id>编辑<#else>新增</#if>费用资料</small></h3>
        </div>
        <div class="content">
            <div class="title-box clearfix">
                <p class="title fl">基本信息</p>
                <p class="number fr">费用编号:<input value="${shopServiceInfo.serviceSn}" name="serviceSn" disabled></p>
            </div>
            <div class="form-box" id="formData">
                <input type="hidden" name="id" class="js-serviceInfoId" value="${shopServiceInfo.id}">
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        费用名称:
                    </div>
                    <div class="form-item">
                        <input type="text" name="name" class="yqx-input yqx-input-small js-name" value="${shopServiceInfo.name}" placeholder="" data-v-type="required">
                    </div>
                    <div class="form-label form-label-must">
                        车辆级别:
                    </div>
                    <div class="form-item">
                        <input type="text" name="carLevelName" class="yqx-input yqx-input-small js-car-level" value="${shopServiceInfo.carLevelName}" placeholder="" data-v-type="required">
                        <input type="hidden" name="carLevelId" value="${shopServiceInfo.carLevelId}">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        费用定价:
                    </div>
                    <div class="form-item">
                        <input type="text" name="servicePrice" class="yqx-input yqx-input-small" value="${shopServiceInfo.servicePrice}" placeholder="" data-v-type="required | number">
                    </div>
                </div>
                <div class="btn-box">
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save">保存</button>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-back">返回</button>
                </div>
            </div>
        </div>
    </div>
</div>


<script src="${BASE_PATH}/static/js/page/setting/serviceInfo/serviceInfo-fee-edit.js?65f305b706d2560014b1fd3a061c8ba0"></script>
<#include "yqx/layout/footer.ftl">

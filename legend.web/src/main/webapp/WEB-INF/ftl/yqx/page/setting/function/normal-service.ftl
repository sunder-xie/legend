<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/normal-service.css?d4e27373b2a5df213bbc491159a060d5"/>
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
            <h3 class="headline">标准化服务</h3>
        </div>
        <div class="content">
            <h3>
                请给您门店有的服务项目，输入价格（您可以在<strong>微信公众号-发布服务</strong>中发布车主服务到微信端展示）
            </h3>
            <#list shopServiceCateList as shopServiceCate>
            <div class="yqx-group">
                <!-- group标题 start -->
                <div class="yqx-group-head">
                    <input type="hidden" value="${shopServiceCate.id}"/>
                    <#if shopServiceCate.id == '9145'>
                        <i class="ico0"></i>
                    <#elseif shopServiceCate.id == '9146'>
                        <i class="ico1"></i>
                    <#elseif shopServiceCate.id == '9147'>
                        <i class="ico2"></i>
                    <#elseif shopServiceCate.id == '9148'>
                        <i class="ico3"></i>
                    <#elseif shopServiceCate.id == '9149'>
                        <i class="ico4"></i>
                    <#elseif shopServiceCate.id == '9150'>
                        <i class="ico5"></i>
                    </#if>

                     ${shopServiceCate.name}
                    <input type="checkbox" class="js-all-select"/>
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->

                <ul class="yqx-group-content clearfix">
                <#if  shopServiceCate.serviceTemplateList>
                    <#list shopServiceCate.serviceTemplateList as serviceTemplate>
                    <li class="service-item js-service-item">
                        <input type="hidden" name="categoryId" value="${shopServiceCate.id}">
                        <input type="hidden" name="cateTag" value="${shopServiceCate.cateTag}">
                        <input type="hidden" name="parentId" value="${serviceTemplate.id}"/>
                        <input type="hidden" name="name" value="${serviceTemplate.name}"/>
                        <input type="hidden" name="serviceSn" value="${serviceTemplate.serviceSn}"/>
                        <input type="hidden" name="sort" value="${serviceTemplate.sort}"/>
                        <input type="hidden" name="imgUrl" value="${serviceTemplate.imgUrl}"/>
                        <input type="hidden" name="serviceNote" value="${serviceTemplate.serviceNote}"/>
                        <input type="hidden" name="thirdServiceInfo" value='${serviceTemplate.thirdServiceInfo}'/>
                        <input type="checkbox"  <#if serviceTemplate.check==true>checked disabled</#if> class="js-service-checkbox"/>
                        <div class="form-label js-show-tips">
                            ${serviceTemplate.name}
                        </div>
                        <div class="form-item js-serviceprice-input <#if serviceTemplate.check!=true>display-none</#if>">
                            <input type="text" name="servicePrice" class="yqx-input yqx-input-icon yqx-input-small <#if serviceTemplate.servicePrice==0>red-color</#if>" value="${serviceTemplate.servicePrice}" placeholder="" data-v-type="required | number">
                            <span class="fa icon-small">元</span>
                        </div>
                    </li>
                    </#list>
                </#if>
                </ul>
                <!-- group内容 end -->
            </div>
            </#list>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save">保存</button>
        </div>
    </div>
</div>
<#include "yqx/layout/footer.ftl">
<script src="${BASE_PATH}/static/js/page/setting/function/normal-service.js?35a394fef543aaf79721d28697dedc70"></script>
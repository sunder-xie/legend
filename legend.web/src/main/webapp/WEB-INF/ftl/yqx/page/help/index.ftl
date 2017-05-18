<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/help/help.css?e8e86a275a7bad7b7de03dace6831b86">
<div class="yqx-wrapper clearfix">
    <#include "yqx/tpl/help/help-nav-tpl.ftl"/>
        <div class="head-title">
             <span class="guide_tree"><#list catList as cat>${cat.title} > </#list>${articleDetail.title}</span>
        </div>
    <div class="content">
    <#if articleDetail>
        <p class="main_tit">${articleDetail.title}</p>
        <#if articleDetail.imageGalleryList>
            <div id="galleria" class="galleria">
                <#list articleDetail.imageGalleryList as galleryImage>
                    <a href="${galleryImage.url}"><img src="${galleryImage.url}"></a>
                </#list>
            </div>
        </#if>
        <#if articleDetail.content><div class="introduce">${articleDetail.content}</div></#if>
    </#if>
    </div>
</div>
<#include "yqx/layout/footer.ftl">

<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/galleria/galleria-1.4.2.min.js"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/help/help.js?28809d03eec87e6f2f97b95a2c041717"></script>

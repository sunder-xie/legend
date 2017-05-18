<#include "layout/portal-header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/help.css?e1bd3ef9b147a1f419e2f69b1be3dd12">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/resources/css/page/notice.css?ca70e2efe14f630d1f9bc6e9f5e5e260">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/resources/css/portal/fenye.css?68b329da9893e34099c7d8ad5cb9c940">
<div class="wrapper">
<#--<div class="guide"><span class="help_tit">帮助&nbsp;<label>help</label></span><span-->
<#--class="guide_tree"><#list catList as cat>${cat.title} > </#list>${articleDetail.title}</span></div>-->
    <div class="content">
        <!-- 这边是存放内容-->
        <!-- 这边存放的是文章的信息-->
        <p class="main_tit">${articleDetail.title}</p>
    <#if articleDetail.imageGalleryList>
        <div id="galleria" class="galleria">
            <#list articleDetail.imageGalleryList as galleryImage>
                <a href="${galleryImage.url}"><img src="${galleryImage.url}"></a>
            </#list>
        </div>
    </#if>
    <#if articleDetail.content>
        <div class="introduce">${articleDetail.content}</div></#if>
        <!-- 这边存放的是文章的信息 结束-->
    </div>
    <!-- 这边是存放内容结束-->
    <div class="sidebar" id="sidebar">
        <h2 class="title_first title_help <#if isHelp>current_title</#if>"><a href="${BASE_PATH}/portal/help"><p>帮助</p>
        </a></h2>
        <ul class="firstNav">
        <#list menuList as menu>
            <li <#if menu_has_next>class="bottomBorder"</#if>>
                <p class="fItem">${menu.title}</p>
                <ul class="secondNav">
                    <#list menu.articleList as article>
                        <li <#if article.id == articleDetail.id>class="current"</#if>><a
                                href="${BASE_PATH}/portal/help?id=${article.id}"><p
                                class="sItem">${article.title}</p></a></li>
                    </#list>
                    <#if menu.childCatList>
                        <#list menu.childCatList as menu>
                            <li><p class="sItem childCat">${menu.title}</p>
                                <ul class="thirdNav">
                                    <#list menu.articleList as article>
                                        <li <#if article.id == articleDetail.id>class="current"</#if>><a
                                                href="${BASE_PATH}/portal/help?id=${article.id}"><p
                                                class="tItem">${article.title}</p></a></li>
                                    </#list>
                                </ul>
                            </li>
                        </#list>
                    </#if>
                </ul>
            </li>
        </#list>
        </ul>
    </div>
    <div class="clear"></div>
    <a id="to-top" class="top" href="#"></a>
</div>
<#include "layout/portal-footer.ftl">
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/galleria/galleria-1.4.2.min.js"></script>
<script type="text/javascript"
        src="${BASE_PATH}/resources/online/script/libs/seajs/sea.js?0eba4f6d7ef30c34921228ec84c57814"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/script/libs/path.config.js?629d366782908eaaa898bd099f4054bf"></script>

<script type="text/javascript" src="${BASE_PATH}/resources/js/help/help.js?273ff56d5342bd9a63598900c56ed5e5"></script>

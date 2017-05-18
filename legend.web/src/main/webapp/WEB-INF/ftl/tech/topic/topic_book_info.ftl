<#include "layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/resources/css/page/database_detail.css?20150424"/>
<div class="wrapper">
    <div class="search_tech-box">
        <label>汽车维修技术搜索：</label><input type="text"
                                       id="search_keywords" class="search_tech_keyword txt" placeholder="请输入关键词..."/><#--
        -->
        <button type="button" class="search_tech-btn" id="search_book_btn"></button>
    </div>
    <div class="content">
        <div class="praise-box">
            <button type="button" class="like-btn" data-id="${topic.id}"><span
                    class="like praise number">${topic.likeNum}</span></button><#--
            -->
            <button type="button" class="unlike-btn" data-id="${topic.id}"><span
                    class="unlike praise number">${topic.unlikeNum} </span></button>
        </div>
        <div>
            <button type="button" class="full_preview_btn" id="bookPreviewFull"></button>
        </div>
        <div class="pdf-box">
            <button type="button" class="prev_img-btn prev-btn"></button><#--
            -->
            <div class="pdf-content" id="content">

            </div>
            <script type="text/html" id="contentTemplate">
                <%for(var index in templateData){%>
                <%item = templateData[index]%>
                <img src="<%=item.pic%>" alt="<%=item.title%>"/>
                <%}%>
            </script>
        <#--
        -->
            <button type="button" class="next_img-btn next-btn"></button>
        </div>
        <div class="page-box">
            <button type="button" class="prev_button prev-btn">上一页</button><#--
            -->
            <div class="page">共<span class="totalPage">0</span>页，去第<input type="text" class="pageNum txt"/>页
                <button
                        type="button" class="redirect-btn">跳转
                </button>
            </div><#--
            -->
            <button
                    type="button" class="next_button next-btn">下一页
            </button>

                <input type="hidden" name="search_topicId" id="search_topicId" value="${topic.id}"/>
                <input type="hidden" name="page" id="page" value="1"/>
        </div>
    </div>
    <div class="slider">
        <div class="slider_nav">
            <div class="nav_tit" title="${topic.title}">${topic.title}</div>
            <i class="nav_underline"></i>

        <#list topicBookCatalogueList as topicBookCatalogue>
            <div class="first_nav">
                <p <#if topicBookCatalogue.childList?exists> class="first_nav_tit" <#else>class="first_nav_tit_noarrow"</#if> data-page="${topicBookCatalogue.jumpPage}">
                    <#if topicBookCatalogue.title>
                        <#if topicBookCatalogue.title?length gt 16>
                            ${topicBookCatalogue.title?substring(0,16)}
                        <#else>
                            ${topicBookCatalogue.title}
                        </#if>
                    </#if>
                </p>
                <#if topicBookCatalogue.childList>
                    <#list topicBookCatalogue.childList as child>
                        <ul class="second_nav">
                            <li class="second_nav_tit" data-page="${child.jumpPage}">${child.title}</li>
                        </ul>
                    </#list>
                </#if>
            </div>
        </#list>
        </div>

    <#include "tech/recommend.ftl" >
    </div>
</div>



<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/tech/topic/topic_info.js?da6783784c34d13f1e5eb5e1bf413214"></script>

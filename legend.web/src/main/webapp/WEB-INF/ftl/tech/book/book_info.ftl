<#include "layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/resources/css/page/database_detail.css?20150428"/>
<div class="wrapper">
    <div class="search_tech-box">
        <label>汽车维修技术搜索：</label><input type="text"
                                       id="search_keywords" class="search_tech_keyword txt" placeholder="请输入关键词..."/><#--
        -->
        <button type="button" class="search_tech-btn" id="search_book_btn"></button>
    </div>
    <div class="content">
        <div class="praise-box">
            <button type="button" class="like-btn" data-id="${book.id}"><span
                    class="like praise number">${book.likeNum}</span></button><#--
            -->
            <button type="button" class="unlike-btn" data-id="${book.id}"><span
                    class="unlike praise number">${book.unlikeNum} </span></button>
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

                <input type="hidden" name="search_bookId" id="search_bookId" value="${book.id}"/>
                <input type="hidden" name="page" id="page" value="1"/>
        </div>
    </div>
    <div class="slider">
        <div class="slider_nav">
            <div class="nav_tit" title="${book.title}">${book.title}</div>
            <i class="nav_underline"></i>

        <#list bookCatalogueList as bookCatalogue>
            <div class="first_nav">
                <p <#if bookCatalogue.childList?exists> class="first_nav_tit" <#else>class="first_nav_tit_noarrow"</#if> data-page="${bookCatalogue.jumpPage}">
                    <#if bookCatalogue.title>
                        <#if bookCatalogue.title?length gt 16>
                            ${bookCatalogue.title?substring(0,16)}
                        <#else>
                            ${bookCatalogue.title}
                        </#if>
                    </#if>
                </p>
                <#if bookCatalogue.childList>
                    <#list bookCatalogue.childList as child>
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
<script type="text/javascript" src="${BASE_PATH}/resources/js/tech/book/book_info.js?0c57056f741fe8441354fb8d290af6c6"></script>

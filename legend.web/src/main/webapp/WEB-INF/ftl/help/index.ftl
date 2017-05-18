<#include "layout/header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/help.css?e1bd3ef9b147a1f419e2f69b1be3dd12">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/resources/css/page/notice.css?ca70e2efe14f630d1f9bc6e9f5e5e260">

<div class="wrapper">
<#if articleType == true>
    <div class="guide"><span class="help_tit">帮助&nbsp;<label>help</label></span><span
            class="guide_tree"><#list catList as cat>${cat.title} > </#list>${articleDetail.title}</span></div>
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
    <#else>
        <div class="guide">
                <span class="help_tit">帮助&nbsp;<label>help</label>
                </span>
            <span class="guide_tree">系统公告</span>
        </div>
    <div class="content">
        <!--这边存放的是公告信息-->
        <input type='hidden' id='provSelect1' name='provSelect1' value='notice'/>
        <div class="notice_box">
            <p class="notice_title">系统公告列表</p>

            <div class="notice_wrap">
                <div id="notice"></div>
                <!--这边开始分页的组建-->
                <div class="page_box">
                    <span id="prePage"><</span><span id="nextPage">></span>
                    <span class="page_num"><b id="currentPage"></b>/<b id="totalPages"></b>页</span>
                    跳转至<input type="text" class="page_input" name="pageNum" value=""/>页
                    <input class="page_btn" type="button" value="跳转" name="pageAction"/>
                </div>
            </div>
        </div>
        <!--这边存放的是公告信息 结束-->
    </#if>
</div>
    <div class="sidebar" id="sidebar">
        <ul class="firstNav">
            <li class="bottomBorder">
                <p>
                    <a class="nItem" href="${BASE_PATH}/shop/help/notice">系统公告</a>
                </p>
            </li>
            <li class="bottomBorder">
                <p>
                    <a class="nItem" href="${BASE_PATH}/shop/help/feedback/feedback-list">历史反馈问题</a>
                </p>
            </li>
        <#list menuList as menu>
            <li <#if menu_has_next>class="bottomBorder"</#if>>
                <p class="fItem">${menu.title}</p>
                <ul class="secondNav">
                    <#list menu.articleList as article>
                        <li <#if article.id == articleDetail.id>class="current"</#if>><a
                                href="${BASE_PATH}/shop/help?id=${article.id}"><p
                                class="sItem">${article.title}</p></a></li>
                    </#list>
                    <#if menu.childCatList>
                        <#list menu.childCatList as menu>
                            <li><p class="sItem childCat">${menu.title}</p>
                                <ul class="thirdNav">
                                    <#list menu.articleList as article>
                                        <li <#if article.id == articleDetail.id>class="current"</#if>><a
                                                href="${BASE_PATH}/shop/help?id=${article.id}"><p
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

    <!--公告数据模版-->
    <script id="noticeData" type="text/html">
        <%if(templateData!=null){
        for(var index in templateData){
        item = templateData[index];
        %>
        <div class="notice_item">
            <span class="icon"></span>

            <p><%=item.publishTime%></p>

            <h1><%=item.noticeTitle%></h1>

            <p><%==item.noticeContent%></p>
        </div>
        <%}}%>
    </script>
    <!--准备数据模版结束-->
</div>
<#include "layout/footer.ftl">
    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/galleria/galleria-1.4.2.min.js"></script>
    <script type="text/javascript"
            src="${BASE_PATH}/resources/online/script/libs/seajs/sea.js?0eba4f6d7ef30c34921228ec84c57814"></script>
    <script type="text/javascript" src="${BASE_PATH}/resources/script/libs/path.config.js?629d366782908eaaa898bd099f4054bf"></script>
    <script type="text/javascript"
            src="${BASE_PATH}/resources/js/help/help.js?273ff56d5342bd9a63598900c56ed5e5"></script>

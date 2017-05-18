<#include "yqx/tpl/home-header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/home/article.css?2774ec4fd1ac6ea4e6acbff6d2219584">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/home/paging2.css?7dd80331de8b24302227664d100b2b30">
<div class="yqx-wrapper home-article">
    <div class="banner news">
        <div class="banner-title">
            <div class="news-title"></div>
            <img class="banner-front" src="${BASE_PATH}/static/img/page/home/news-front.png">
        </div>
    </div>
    <div class="main clearfix">
        <div class="menu-bar">
            <h4><a href="${BASE_PATH}/home">首页</a> > <a href="${BASE_PATH}/home/news/news-list">云修资讯</a></h4>
        </div>
        <div class="preview-box fr">
        <h3>热门资讯</h3>
        <ul>
        <#list popularNewsList as news>
           <li><a href="${BASE_PATH}/home/news/news-detail?id=${news.id}&refer=news-detail">${news.newsTitle}</a></li>
        </#list>
        </ul>
        </div>
        <div class="article-tab-box">
            <div class="article-tab js-type" data-type-id="0">全部</div>
            <#list newsTypeList as newsType>
                <div class="article-tab js-type" data-type-id="${newsType.id}">${newsType.typeName}</div>
            </#list>
        </div>

        <div class="article-content fl">
        </div>
        <div class="yqx-page fl"></div>

    </div>
</div>
<script type="text/template" id="newsList">
    <% for(var i = 0; i < data.length; i++) {%>
    <% var item = data[i];%>
    <div class="article-item">
        <% if(item.imgUrl !=null &&item.imgUrl ){%>
        <img src="<%=item.imgUrl%>">
        <%} else{%>
            <img src="<%=defaultImgUrl%>">
        <%} %>
        <div class="text">
            <h3><a href="${BASE_PATH}/home/news/news-detail?id=<%=item.id%>&refer=news-list"><%=item.newsTitle%></a></h3>
            <h4><%=item.newsSource%><%if(item.modifiedTimeStr){%><i class="vertical-line"></i><i><%=item.modifiedTimeStr%></i><%}%></h4>
            <% var str = $('<div>' + item.newsSummary + '</div>').text();%>
            <% if(str.length > 73 ){%>
            <% str = str.slice(0, 73) + '...'%>
            <%}%>
            <p><%=str%></p>
        </div>
    </div>
    <%}%>
</script>
<script src="${BASE_PATH}/static/js/page/home/paging.js?c1413b4d01031a0a44751de21ba8f895"></script>
<script src="${BASE_PATH}/static/js/page/home/news/news-list.js?aa587cdac3c0808b55946e048c86a654"></script>
<#include "yqx/layout/footer.ftl">
<#include "yqx/tpl/home-header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/home/article.css?2774ec4fd1ac6ea4e6acbff6d2219584">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/home/article-detail.css?dc9ba5c42e2afa03a23a48bd28facbdd">
<div class="yqx-wrapper home-article">
    <div class="banner news">
        <div class="banner-title">
            <div class="news-title"></div>
            <img class="banner-front" src="${BASE_PATH}/static/img/page/home/news-front.png">
        </div>
    </div>
    <div class="main clearfix">
        <div class="menu-bar">
            <h4><a href="${BASE_PATH}/home">首页</a> > <a href="${BASE_PATH}/home/news/news-list">云修资讯</a> > 详情</h4>
        </div>
        <div class="preview-box fr">
            <h3>热门资讯</h3>
            <ul>
            <#list popularNewsList as news>
                <li class="ellipsis-1 js-show-tips"><a href="${BASE_PATH}/home/news/news-detail?id=${news.id}&refer=news-detail">${news.newsTitle}</a></li>
            </#list>
            </ul>
        </div>
        <h1 class="font-yahei">${news.newsTitle}</h1>
        <h4 class="time">${news.modifiedTimeStr}</h4>
        <div class="detail-content">${news.newsContent}</div>

    </div>
</div>

<script src="${BASE_PATH}/static/js/page/home/news/news-detail.js?cd31fa4b5024fd00dba675ab465a7350"></script>
<#include "yqx/layout/footer.ftl">
<#include "yqx/tpl/home-header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/home/article.css?2774ec4fd1ac6ea4e6acbff6d2219584">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/home/article-detail.css?dc9ba5c42e2afa03a23a48bd28facbdd">
<div class="yqx-wrapper home-article">
    <div class="banner">
        <div class="banner-title">
            <div class="products-title"></div>
            <img class="banner-front" src="${BASE_PATH}/static/img/page/home/product-front.png">
        </div>
    </div>
    <div class="main">
        <div class="menu-bar">
            <h4><a href="${BASE_PATH}/home">首页</a> > <a href="${BASE_PATH}/home/products/products-list">云修产品</a> > 详情</h4>
        </div>
        <div class="preview-box fr">
            <h3>明星产品</h3>
            <ul id="hotProducts">
            </ul>
        </div>
        <h1 class="font-yahei">${product.itemTitle}</h1>
        <h4 class="time">${product.gmtCreateStr}</h4>
        <div class="detail-content">${product.itemContent}</div>

    </div>
</div>
<script src="${BASE_PATH}/static/js/page/home/products/products-detail.js?bb317b3e8c33cd84d879ef0c287de523"></script>
<#include "yqx/layout/footer.ftl">

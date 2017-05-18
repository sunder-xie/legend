<#include "yqx/tpl/home-header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/home/article.css?2774ec4fd1ac6ea4e6acbff6d2219584">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/home/paging2.css?7dd80331de8b24302227664d100b2b30">
<div class="yqx-wrapper home-article">
    <div class="banner">
        <div class="banner-title">
            <div class="products-title"></div>
            <img class="banner-front" src="${BASE_PATH}/static/img/page/home/product-front.png">
        </div>
    </div>
    <div class="main clearfix">
        <div class="menu-bar">
            <h4><a href="${BASE_PATH}/home">首页</a> > <a href="${BASE_PATH}/home/products/products-list">云修产品</a></h4>
        </div>
        <div class="article-content fl">
        </div>
        <div class="preview-box fr">
            <h3>明星产品</h3>
            <ul id="hotProducts">
            </ul>
        </div>
        <div class="yqx-page fl"></div>
    </div>
</div>
<script type="text/template" id="newsList">
    <% for(var i = 0; i < data.length; i++) {%>
    <% var e = data[i];%>
    <div class="article-item">
        <img src="<%=e.imgUrl || defaultImgUrl%>">
        <div class="text">
            <h3>
                <% if(e.targetUrl) {%>
                <a href="<%=e.targetUrl%>" target="_blank"><%=e.itemTitle%></a>
                <%} else {%>
                <a href="${BASE_PATH}/home/products/products-detail?itemid=<%=e.id%>"><%=e.itemTitle%></a></h3>
                <%}%>
            <h4>淘汽云修<i class="vertical-line"></i><i><%=e.gmtCreateStr%></i></h4>
            <% if(e.targetUrl && e.targetUrl != '') {%>
            <div class="link-box">
                <a href="<%=e.targetUrl%>" target="_blank">
                    <img src="${BASE_PATH}/static/img/page/home/link.png">购买
                </a>
            </div>
            <%} else {%>
            <% var str = $('<div>' + e.itemContent + '</div>');%>
            <% if(str.length > 73 ){%>
            <% str = str.slice(0, 73) + '...'%>
            <%}%>
            <p><%=str%></p>
            <%}%>
        </div>
    </div>
    <%}%>
</script>
<script src="${BASE_PATH}/static/js/page/home/paging.js?c1413b4d01031a0a44751de21ba8f895"></script>
<script src="${BASE_PATH}/static/js/page/home/products/products-list.js?af39bb2d08cb09aa29ead5504591edbb"></script>
<#include "yqx/layout/footer.ftl">
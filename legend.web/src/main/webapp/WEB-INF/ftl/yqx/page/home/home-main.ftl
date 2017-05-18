<div class="info-container clearfix">
    <div class="news">
        <h2 class="font-yahei">云修资讯</h2>
        <div class="news-group">
        <#list newsList as news>
            <div class="new-item">
                <a href="${BASE_PATH}/home/news/news-detail?id=${news.id}">
                    <h3 class="ellipsis-1 js-show-tips font-yahei">
                        <i class="font-arial">[${news.modifiedTime?string("MM-dd")}]</i>
                    ${news.newsTitle}
                    </h3></a>
                <#if news_index <= 1>
                    <#if news.newsSummary?length < 58 >
                        <p class="js-show-tips">${news.newsSummary}</p>
                    <#else>
                        <p class="js-show-tips">${news.newsSummary[0..58] + '...'}</p>
                    </#if>
                </#if>
            </div>
        </#list>
            <a href="${BASE_PATH}/home/news/news-list" class="more font-yahei">查看更多</a>
        </div>
    </div>
    <div class="products fr">
        <h2 class="font-yahei">云修产品</h2>
    <#list productAlertList as productAlert>
        <#assign jumpUrl = "${BASE_PATH}/home/products/products-detail?itemid=${productAlert.id}" >
        <#if productAlert.targetUrl != ''>
            <#assign  jumpUrl = "${productAlert.targetUrl}">
        </#if>
        <#if productAlert_index == 0>
            <div class="product-main">
                <a href="${jumpUrl}" target="_blank">
                    <img class="main-img" src="${productAlert.topImgUrl}">
                    <div class="product-text">
                    <#--<h3 class="ellipsis-1 js-show-tips font-yahei">${productAlert.itemTitle}</h3>-->
                    </div>
                </a>
            </div>
        <#else>
            <#if productAlert_index == 1>
            <div class="products-group">
            </#if>
            <div class="product-item">
                <a href="${jumpUrl}" target="_blank">
                    <h3 class="ellipsis-1 js-show-tips font-yahei"><i>[产品资讯]</i> ${productAlert.itemTitle}</h3>
                </a>
            </div>
            <#if !productAlert_has_next>
            </div>
            </#if>
        </#if>
    </#list>
        <a href="${BASE_PATH}/home/products/products-list" class="more font-yahei">查看更多</a>
    </div>
    <div class="product-show">

    </div>
</div>
<#if bannerList>
<div class="banner">
    <div class="banner-view clearfix">
        <#list bannerList as banner>
            <a target="_blank" href="<#if banner.jumpUrl>${banner.jumpUrl}<#else>javascript:void(0)</#if>">
                <img src="${banner.imgUrl}">
            </a>
        </#list>
    </div>
    <div class="pointer-box js-pointers">
        <#list bannerList as banner>
            <i class="<#if banner_index == 0>pointer-current</#if> pointer"
               data-index="${banner_index}"></i>
        </#list>
    </div>
</div>
</#if>
<div class="advantage-container">
    <h2 class="font-yahei">服务亮点</h2>
    <h3 class="font-yahei">帮助您更快了解云修</h3>
    <div class="advantages-group">
        <div class="advantage-item tech-1">
            <div class="icon"></div>
            <h2>淘汽知道</h2>
            <h3>让天下没有难修的车</h3>
        </div>
        <div class="advantage-item marketing-1">
            <div class="icon"></div>
            <h2>客户营销</h2>
            <h3>多维度分析，精准营销</h3>
        </div>
        <div class="advantage-item reward-1">
            <div class="icon"></div>
            <h2>技师奖励</h2>
            <h3>技师的奖励，云修来发</h3>
        </div>
        <div class="advantage-item crm-1">
            <div class="icon"></div>
            <h2>客户管理</h2>
            <h3>总有一款解决方案适合您</h3>
        </div>
        <div class="advantage-item wechat-1">
            <div class="icon"></div>
            <h2>微信公众号</h2>
            <h3>互联网+营销解决方案</h3>
        </div>
    </div>
</div>
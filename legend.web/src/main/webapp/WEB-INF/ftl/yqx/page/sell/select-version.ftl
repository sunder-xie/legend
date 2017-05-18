<#include "layout/portal-header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d" />
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/third-plugin/Font-Awesome-3.2.1/css/font-awesome.min.css" />
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/sell/select-version.css?8439dff79e112ffc219cefd8fdca27da"/>

<div class="wrap">
    <input type="hidden" value="${mobile}" name="mobile"/>
    <h3 class="title">您手中的客户雷达</h3>
    <h4 class="title-two">云修系统，帮您做更好的门店</h4>
    <div class="version-box clearfix" id="versionBox">

    </div>
    <div class="tq-explain">
        <img src="${BASE_PATH}/static/img/page/sell/tq-explain.png"/>
        <a target="_blank" href="
http://tqmall-legend.oss-cn-hangzhou.aliyuncs.com/sell/2017%E4%BA%91%E4%BF%AE%E7%B3%BB%E7%BB%9F%E9%94%80%E5%94%AE%E7%89%88%E6%9C%AC%E8%A7%84%E5%88%92.pdf">
            <p>淘汽云修</p>
            <p>系统版本说明</p>
        </a>
    </div>
</div>

<script type="text/html" id="versionTpl">
    <%if(json.data){%>
    <%for(var i=0;i< json.data.length;i++){%>
    <%var item = json.data[i]%>
    <div class="version-wrap js-version">
        <div class="version">
            <%if(item.shopLevel == 10){%>
            <div class="version-pic">
                <img src="${BASE_PATH}/static/img/page/sell/pic02.jpg"/>
            </div>
            <div class="version-text">
                <p>汽修店的专业管理解决方案，包含进销存管理、移动办公、经营分析、业务提醒等功能，轻松解决门店日常经营管理问题。</p>
            </div>
            <%}else if(item.shopLevel == 11){%>
            <div class="version-pic">
                <img src="${BASE_PATH}/static/img/page/sell/pic01.jpg"/>
            </div>
            <div class="version-text">
                <p>汽修店的营销管理解决方案，包含门店管理、移动办公、微信营销等功能，多种客户营销和吸粉方案，让门店经营业绩快速提升。</p>
            </div>
            <%}else if(item.shopLevel == 12){%>
            <div class="version-pic">
                <img src="${BASE_PATH}/static/img/page/sell/pic03.jpg"/>
            </div>
            <div class="version-text">
                <p>汽修店的综合管理解决方案，包含门店管理、移动办公、微信营销和汽修技术问答平台，更有高效集客功能，为门店创造更高价值。</p>
            </div>
            <%}%>
            <div class="version-price">
                <%if(item.isShowDiscountPrice == true){%>
                    <p class="prime">原价：<em><%=item.originalPrice%></em></p>
                <%}else{%>
                <p class="prime"></p>
                <%}%>
                <h3 class="present-price"><span class="bold">&yen;</span> <span class="sale sell-amount"><%=item.price%></span><span class="year"> / 3年</span> <a href="${BASE_PATH}/portal/shop/sell/version/detail" class="fr">查看详情 　> </a></h3>
            </div>
        </div>
        <div class="btn-box">
            <p class="agree">
                <input type="checkbox" class="js-agree"/>
                我已同意<a href="javascript:;" class="protocol-link js-protocol" data-shop-level="<%=item.shopLevel%>">《系统服务条款》</a>
            </p>
            <button class="buy-btn js-buy" disabled data-shop-level="<%=item.shopLevel%>">立即购买</button>
            <p class="protocol-explain">系统服务条款关系到您的消费权益<p>
            <p class="protocol-explain">购买前请仔细阅读云修系统服务条款</p>
        </div>
    </div>
    <%}}%>
</script>
<script type="text/html" id="protocolDialog">
     <div class="dialog-box">
         <h3 class="dialog-title">淘汽智能门店管理系统服务条款</h3>
         <div class="protocol-con">
         <#include "yqx/page/sell/agreement.ftl"/>
         </div>
         <a href="javascript:;" class="agree-protocol js-join">我已阅读并同意系统服务条款</a>
     </div>
</script>

<script type="text/html" id="buyDialog">
    <div class="yqx-dialog">
        <div class="yqx-dialog-header">
            <h3 class="yqx-dialog-headline">确认购买</h3>
        </div>
        <div class="yqx-dialog-body">
            <img src="${BASE_PATH}/static/img/page/sell/buy-ico.png" class="buy-pic">
            <div class="buy-flow clearfix">
                <p class="fl">选择版本</p>
                <p class="fr">支付成功</p>
            </div>
            <ul class="edition">
                <input type="hidden" value="" class="shopLevel">
                <li>购买版本：<span class="name" data-shop-level=""></span></li>
                <li>购买价格：<span class="price"></span>元</li>
                <li>有效时间：<span class="effectiveStr"></span></li>
            </ul>
            <a href="javascript:;" class="go-pay js-pay">马上支付</a>
        </div>
    </div>
</script>


<script src="${BASE_PATH}/static/js/page/sell/select-version.js?b9ff9cf4f610d6c1e75f9c1d34bc44e4"></script>

<script src="${BASE_PATH}/static/third-plugin/seajs/sea.js"></script>
<script src="${BASE_PATH}/static/third-plugin/path.config.js?98f845edf92898b7a23a5b384185c04c"></script>
<#include "yqx/page/sell/footer.ftl">
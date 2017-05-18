<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-base.css?9f840e9f3c3f39c54bcbe9b7350edbe9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/article-view.css?77db7e04e11e40c3706566aa078763f7"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline">文章管理</h1>
        </div>
        <div class="order-body">
            <input type="hidden" name="shopAricleRelId" value="${wechatArticleVo.relId}">
            <div class="qrcode-box">

                <input type="hidden" id="articleTitle" value="${wechatArticleVo.articleTitle}">
                <a class="qrcode-view" id="qrcodeView" href="${wechatArticleVo.viewUrl}" target="_blank"
                        title="点击在浏览器中查看">
                    <div></div>
                </a>
                <h2>手机扫一扫，预览效果</h2>
            </div>
            <div class="preview-box">
                <div class="inner-box">
                    <p>图文消息</p>
                    <input type="hidden" value="${wechatArticleVo.viewUrl}" id="viewUrl">
                    <div class="iframe-box">
                        <iframe class="viewer" src="${wechatArticleVo.viewUrl}">
                        </iframe>
                    </div>
                    <div class="circle">
                        <div class="circle-inner"></div>
                    </div>
                </div>
            </div>
            <div class="btn-group">
                <#if wechatArticleVo.sendStatus == 0>
                <button class="yqx-btn yqx-btn-3 js-send">发送</button>
                    </#if>
                <button class="yqx-btn yqx-btn-default js-back">返回</button>
            </div>
        </div>
    </div>
</div>

<script src="${BASE_PATH}/static/third-plugin/qrcode/jquery.qrcode.min.js"></script>
<script src="${BASE_PATH}/static/js/page/wechat/article-view.js?ec9255d66eb79a55c3d465e97630cc2b"></script>
<#include "yqx/layout/footer.ftl">

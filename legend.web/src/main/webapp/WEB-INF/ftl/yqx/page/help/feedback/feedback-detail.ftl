<!-- create by zsy 20160805 门店反馈信息详情页 -->
<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/help/help.css?e8e86a275a7bad7b7de03dace6831b86">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/help/feedback/feedback-list.css?58de43fee1f4685adc510b22d2794c57"
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">
    <#include "yqx/tpl/help/help-nav-tpl.ftl"/>
    <div class="content">
        <p class="main_tit">历史反馈问题详情</p>
        <div class="feedback-list">
            <div class="list-item">
                <a href="javascript:void(0)"
                   class="continue-feedback js-continue-feedback"
                   style="top: 0;"
                   data-id="${feedback.id}">继续反馈</a>
                <div class="feedback-time"><i class="green-circle"></i>${feedback.gmtCreateStr}</div>
                <p class="history-text">${feedback.feedbackContent}</p>
            <#if feedback.imgUrl>
                <div class="feedback-view js-pic-view" data-src="${feedback.imgUrl}">
                    <img src="${BASE_PATH}/static/img/page/help/feedback/white-image-icon.png" class="img-icon">查看图片
                </div>
            </#if>
            <#list feedbackAnswerList as data>
                    <div class="history-reply">
                        <div class="feedback-time">${data.gmtCreateStr}</div>
                        <p><#if data.answerType == 1>追问(${data.operatorName})：<#else>回复：</#if><i class="history-text hide">${data.answerContent}</i></p>
                    </div>
            </#list>
            </div>
        </div>
    </div>
</div>
<#include "yqx/layout/footer.ftl">
<script type="text/javascript" src="${BASE_PATH}/static/js/page/feedback/feedback-detail.js?e36f5eefa5abcfbb255b0a392916d7c7"></script>

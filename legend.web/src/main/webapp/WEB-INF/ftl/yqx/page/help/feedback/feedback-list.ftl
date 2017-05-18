<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/help/help.css?e8e86a275a7bad7b7de03dace6831b86">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/help/feedback/feedback-list.css?58de43fee1f4685adc510b22d2794c57"

<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/help/help-nav-tpl.ftl"/>
    <div class="content">
        <p class="main_tit">历史反馈问题</p>

        <div class="feedback-list dialog-feedback">
            <div class="form-item input-search" id="listForm">
                <input class="js-search-content yqx-input-small yqx-input" type="text"
                       name="search_feedbackContentLike"
                        placeholder="反馈内容">
                <i class="fa icon-search icon-small js-search-btn"></i>
            </div>
            <div class="list-content" id="listFill">

            </div>
            <div class="yqx-page" id="listPage"></div>
        </div>
    </div>
</div>
<script id="listTpl" type="text/template">
    <% if(json.success && json.data && json.data.content.length) {
        var data = json.data.content, t;
        for(var i in data) {%>
    <div class="list-item">
        <div class="feedback-time"><i class="green-circle"></i><%=data[i].gmtCreateStr%></div>
        <p class="history-text"><%=data[i].feedbackContent%></p>
        <% if(data[i].imgUrl){%>
        <div class="feedback-view js-pic-view" data-src="<%=data[i].imgUrl%>">
            <img src="${BASE_PATH}/static/img/page/help/feedback/white-image-icon.png" class="img-icon">查看图片
        </div>
        <%}%>
        <% if(t = data[i].newFeedbackAnswer){%>
        <div class="history-reply">
            <a class="more-ref" href="${BASE_PATH}/shop/help/feedback/detail?id=<%=data[i].id%>">更多回复<i class="icon-angle-down fa"></i></a>
            <div class="feedback-time"><%=t.gmtCreateStr%></div>
            <p>回复：<i class="history-text"><%=$('<div>'+t.answerContent+'</div>').text()%></i></p>
        </div>
        <a href="javascript:void(0)" class="continue-feedback js-continue-feedback" data-id="<%=data[i].id%>">继续反馈</a>
        <%}%>
    </div>
    <%}}%>
</script>
<#include "yqx/layout/footer.ftl">
<script type="text/javascript" src="${BASE_PATH}/static/js/page/feedback/feedback-list.js?52ddc3b7131193903630971ba78ac246"></script>

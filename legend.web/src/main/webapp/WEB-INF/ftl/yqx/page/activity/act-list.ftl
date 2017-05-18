<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/activity/act-list.css?9cdcc137a8ecd1d20541a870e64c24b2">
<div class="yqx-wrapper clearfix">
    <div class="left-menu" style="float: left;">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <h3 class="Z-title">客户营销  > <a href="${BASE_PATH}/marketing/ng/analysis/promotion">门店推广</a> >  <i>朋友圈推广</i></h3>
    <ul class="model-list" id="listFill">

    </ul>
</div>
<script type="template" id="listTpl">
    <%for(var index in json.data) {%>
    <%item = json.data[index]%>
    <li>
        <div class="model-item">
            <h2 class="activity-title code-hover"><%=item.title%></h2>

            <p class="activity-desc code-hover"><%=item.content%></p>

            <div class="buttons-box">
                <a class="btn-edit" href="${BASE_PATH}/shop/activity/edit?id=<%=item.id%>&templateId=<%=item.templateId%>">编辑发布内容</a>
            </div>
        </div>
        <div class="scan-mask scan-show hide"></div>
        <div class="scan-box scan-show hide">
            <img class="scan-Qr-code" src="<%=item.imgUrl%>" width="149" height="149"/>

            <p class="scan-tip">扫一扫 轻松分享</p>

            <div class="scan-buttons-box">
                <a class="btn-edit" href="${BASE_PATH}/shop/activity/edit?id=<%=item.id%>&templateId=<%=item.templateId%>">编辑发布内容</a>
            </div>
        </div>
    </li>
    <%}%>
</script>
<script id="scancopyTpl" type="text/html">
    <!-- 直接分享模板 -->
    <div class="scan-pop-box">
        <div class="scan-pop-content">
            <img src="<%=data.src%>" width="130" height="130"/>

            <p class="pop-scan-tip">扫一扫 轻松分享</p>

            <div class="pop-copy-box">
                <label for="copy-input">分享地址：</label><input
                    id="copy-input" class="copy-input" type="text" value="<%=data.url%>" readonly/>

                <div
                        id="btn-copy" class="btn-copy">复制
                </div>
            </div>
        </div>
    </div>
</script>
<script src="${BASE_PATH}/static/js/page/activity/act-list.js?13ebaa35d8de78de287cbd72b3b2f463"></script>
<#include "yqx/layout/footer.ftl">

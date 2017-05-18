<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/help/help.css?e8e86a275a7bad7b7de03dace6831b86">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/help/notice.css?88fef9f1c4d8198370b774e2dbd5aa65">

<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/help/help-nav-tpl.ftl"/>
    <div class="head-title">
        <span class="guide_tree">系统公告</span>
    </div>
    <div class="content">
        <!--这边存放的是公告信息-->
        <div class="notice_box">
            <p class="notice_title">系统公告列表</p>

            <div class="notice_wrap">
                <div id="notice"></div>
                <!--这边开始分页的组建-->
                <div class="page_box">
                    <span id="prePage"><</span><span id="nextPage">></span>
                    <span class="page_num"><b id="currentPage"></b>/<b id="totalPages"></b>页</span>
                    跳转至<input type="text" class="page_input" name="pageNum" value=""/>页
                    <input class="page_btn" type="button" value="跳转" name="pageAction"/>
                </div>
            </div>
        </div>
        <!--这边存放的是公告信息 结束-->
    </div>
</div>

<!--公告数据模版-->
<script id="noticeData" type="text/html">
    <%if(templateData!=null){
    for(var index in templateData){
    item = templateData[index];
    %>
    <div class="notice_item">
        <span class="icon"></span>

        <p><%=item.publishTime%></p>

        <h1><%=item.noticeTitle%></h1>

        <p><%==item.noticeContent%></p>
    </div>
    <%}}%>
</script>
<!--准备数据模版结束-->

<#include "yqx/layout/footer.ftl">
<script type="text/javascript" src="${BASE_PATH}/static/js/page/help/notice.js?0f9d8e9f2f262e40055e1de3549c8197"></script>
<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-base.css?9f840e9f3c3f39c54bcbe9b7350edbe9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/msg-list.css?913ae3210ec977281a44fd4b653968a0"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline">自动回复
                <a class="yqx-btn yqx-btn-default help" href="${BASE_PATH}/shop/help?id=89">
                    <i class="question"></i><i>帮助中心</i>
                </a>
            </h1>

            <div class="search-form" id="msgForm">
                <input class="yqx-input key-search"
                       name="search_keyword"
                       placeholder="请输入关键字">

                <div class="form-item">
                    <input type="text" class="yqx-input js-reply-status" placeholder="请选择类型">
                    <input type="hidden" name="search_replyStatus">
                    <i class="fa icon-angle-down"></i>
                </div>
                <button class="js-search-btn yqx-btn yqx-btn-3 search-btn">查询</button>
            </div>
        </div>
        <div class="right-btn">
            <a href="${BASE_PATH}/shop/wechat/msg-edit" class="yqx-btn yqx-btn-default"><i class="fa icon-plus"></i>添加回复</a>
        </div>
        <div class="order-body">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>关键字</th>
                    <th class="width-33">回复消息</th>
                    <th>类型</th>
                    <th>操作</th>
                </tr>
                </thead>
            </table>
            <div id="msgFill"></div>
        </div>
        <div class="yqx-page" id="msgPage"></div>
    </div>
</div>
<script id="msgTpl" type="text/template">
<%if(json && json.success && json.data && json.data.content && json.data.totalElements && json.data.totalElements>0) {%>
<%var data = json.data.content %>
<table class="yqx-table">
<%for(var i in data) {%>
    <tr>
        <td><%=(+i+1)%></td>
        <td <%if(data[i].replyKeyword!=null && data[i].replyKeyword.length >= 10){%> class="text-left" <%}%>>
            <%=data[i].replyKeyword%>
        </td>
        <%var str = data[i].replyContent !== '' ? data[i].replyContent : data[i].articleTitle%>
        <td class="width-33 <%if(str && str.length > 22) {%><%='text-left'%><%}%>">
            <input type="hidden" name="articleTitle" value="<%=data[i].articleTitle%>">
            <input type="hidden" name="replyContent" value="<%=data[i].replyContent%>">
            <input type="hidden" name="picUrl" value="<%=data[i].picUrl%>">
            <input type="hidden" name="replyDescription" value="<%=data[i].replyDescription%>">
            <%if(str && str.length > 96){%>
            <%=str.slice(0, 96) + '...'%>
            <%} else {%>
            <%=str%>
            <%}%>
            <%if((data[i].picUrl && data[i].picUrl != '') || str.length > 96) {%>
            <button class="more-detail js-detail">[查看更多]</button>
            <%}%>
        </td>
        <td><%if(data[i].replyStatus == '0') {%><%='用户消息回复'%><%}%><%if(data[i].replyStatus == '1') {%><%='关键字回复'%><%}%><%if(data[i].replyStatus == '2') {%><%='被添加回复'%><%}%></td>
        <td>
            <a href="${BASE_PATH}/shop/wechat/msg-edit?msgId=<%=data[i].id%>" class="text-underline edit">编辑</a>
            <a class="text-underline js-del del" data-id="<%=data[i].id%>">删除</a>
        </td>
    </tr>
<%}%>
    </div>
<%} else {%>
    <h4 class="no-data">
        暂无数据
    </h4>
    <%}%>
</script>

<script id="detail" type="text/template">
<div class="detail-box">
    <h3 class="detail-title"><%=data.articleTitle%></h3>
    <div class="detail-body">
        <p><%=data.replyContent && data.replyContent != '' ? data.replyContent : data.replyDescription%></p>
        <%if(data.picUrl){%>
        <div class="detail-img clearfix">
            <img src="<%=data.picUrl%>" class="fl">
        </div>
        <%}%>
    </div>
</div>
</script>
<script src="${BASE_PATH}/static/js/page/wechat/msg-list.js?f31b4e9d9e7ae5c6dfa46cfe02c564aa"></script>
<#include "yqx/layout/footer.ftl" >
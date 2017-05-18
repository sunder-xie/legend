<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-base.css?9f840e9f3c3f39c54bcbe9b7350edbe9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/article-list.css?7189a8616fecb01ffd273366a7d51bbc"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline">文章管理</h1>

            <p class="headline-detail"><i class="wechat-icons icon-voice"></i>您本月还能群发 <i
                    class="number">${canSendArticleCount}</i> 条文章</p>
            <a class="yqx-btn yqx-btn-default help" href="${BASE_PATH}/shop/help?id=88">
                <i class="wechat-icons question"></i>帮助中心
            </a>

        </div>
        <div class="search-form" id="articleForm">
            <input class="yqx-input" placeholder="标题/摘要" name="search_keyword">
            <input type="hidden" name="search_sendStatus">
            <div class="single-tag">
                <button class="yqx-btn yqx-btn-xs tag-control selected yqx-btn-default js-search-input"
                    data-name="search_sendStatus">全部</button><button class="yqx-btn tag-control yqx-btn-xs yqx-btn-default js-search-input"
                    data-name="search_sendStatus"
                    data-value="0">未发送</button><button class="yqx-btn tag-control yqx-btn-default yqx-btn-xs js-search-input"
                    data-name="search_sendStatus"
                    data-value="1">已发送</button>
            </div><button class="yqx-btn yqx-btn-3 yqx-btn-xs js-search-btn search-btn">查询</button>
        </div>

        <div class="order-body" style="margin-top: 10px;">
            <table class="yqx-table">
                <thead>
                <th class="col-xs-6">文章标题</th>
                <th>发送时间</th>
                <th>状态</th>
                <th>操作</th>
                </thead>
            </table>
            <div class="article-content">
            <table class="yqx-table" id="articleFill">

            </table>
                </div>
        </div>
        <div id="articlePage" class="yqx-page"></div>
    </div>
</div>
<script id="articleTpl" type="template/text">
    <%if(json && json.success && json.data && json.data.content) {%>
    <%var data = json.data.content %>
    <%for(var i in data) {%>
    <tr>
        <td class="col-xs-6">
            <img class="article-img" src="<%=data[i].thumbUrl%>">

            <div class="img-detail">
                <a href="${BASE_PATH}/shop/wechat/article-view?shopAricleRelId=<%=data[i].relId%>" target="_blank"><h2 class="js-show-tips"><%=data[i].articleTitle%></h2></a>

                <p><i class="detail">阅读：<%=data[i].readCount || '0'%></i>
                    <i class="detail">送达：<%=data[i].targetCount || '0'%></i></p>

            </div>
        </td>
        <td><%=data[i].sendTimeStr%></td>
        <td><%if(data[i].sendStatus == 0) {%><%='未发送'%>
            <%}else{%><%='已发送'%><%}%></td>
        <td>
            <%if(data[i].sendStatus == 0) {%>
            <a class="yqx-btn yqx-btn-3 send" href="${BASE_PATH}/shop/wechat/article-view?shopAricleRelId=<%=data[i].relId%>">查看发送</a>
            <%}else{%>
            <a class="yqx-btn yqx-btn-3 send" href="${BASE_PATH}/shop/wechat/article-view?shopAricleRelId=<%=data[i].relId%>">查看</a>
            <%}%>
        </td>
    </tr>
    <%}%>
    <%} else {%>
    <h4 class="no-data">
        暂无数据
    </h4>
    <%}%>
</script>
<script src="${BASE_PATH}/static/js/page/wechat/article-list.js?a3415fc5880d51aa08d266400d8f8601"></script>
<#include "yqx/layout/footer.ftl" >
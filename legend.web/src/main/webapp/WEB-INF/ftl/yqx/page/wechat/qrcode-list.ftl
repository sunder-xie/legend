<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-base.css?9f840e9f3c3f39c54bcbe9b7350edbe9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/qrcode-list.css?b01635d7a2b44bd87043086303b6adac"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline clearfix">二维码
                <a class="yqx-btn yqx-btn-default help fr" href="${BASE_PATH}/shop/help?id=91">
                    <i class="question"></i><i>帮助中心</i>
                </a>
            </h1>
        </div>
        <div class="order-body clearfix" id="qrcodeFill">

        </div>
        <div id="qrcodePage" class="yqx-page"></div>
    </div>
</div>
<script id="qrcodeTpl" type="text/template">
    <%if(json && json.success && json.data
    && json.data.content
    && json.data.content.length) {%>
    <%var data = json.data.content %>
    <%for(var i in data) {%>
    <div class="qrcode-box">
        <h2 class="js-show-tips"><%=data[i].qrcodeName%></h2>

        <div class="qr-body">
            <img class="qr-viewer js-preview" src="<%=data[i].qrcodeJoinTplUrl%>">
        </div>
        <div class="qr-footer">
            <a href="<%=data[i].qrcodeJoinTplUrl%>" class="js-download" download>
                <img class="icon" src="${BASE_PATH}/static/img/page/wechat/download-icon.png">

                <h3>下载</h3>
            </a>
        </div>
    </div>
    <%}%>
    <%} else {%>
    <div class="no-data">
        <h2>暂无二维码</h2>
    </div>
    <%}%>
</script>
<script src="${BASE_PATH}/static/js/page/wechat/qrcode-list.js?5f44529cf98323e8e6f7a67b016ca615"></script>
<#include "yqx/layout/footer.ftl" >
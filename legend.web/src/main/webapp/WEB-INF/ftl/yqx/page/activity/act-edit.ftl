<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/activity/act-edit.css?8f77d9f34d53076370a5675c809dca5c">
<div class="yqx-wrapper clearfix">
    <div class="left-menu" style="float: left;">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="aside-main">
        <h3 class="Z-title">客户营销  > <a href="${BASE_PATH}/marketing/ng/analysis/promotion">门店推广</a> >  <a href="${BASE_PATH}/shop/activity">朋友圈推广</a> > <i>编辑</i></h3>
        <form name="searchForm" class="container" id="searchForm">
            <div class="show-grid">
                <input hidden name="id" value="${marketingCase.id}">
                <input hidden name="status" value="${marketingCase.status}">
                <input hidden name="shopId" value="${marketingCase.shopId}">
                <input hidden name="templateId" value="${marketingCase.templateId}">
                <input hidden name="serviceNum" value="${marketingCase.serviceNum}">
                <input hidden name="templateUrl" value="${marketingCase.templateUrl}">

                <div class="form-item col-12">
                    <label>活动名称</label><div class="form-item col-11">
                        <input class="yqx-input" value="${marketingCase.title}" disabled>
                    </div>
                </div>
            </div>
        <#if marketingCase.marketingColumnConfig.name == 1 || marketingCase.marketingColumnConfig.mobile == 1>
            <div class="show-grid">
                <#if marketingCase.marketingColumnConfig.name == 1>
                    <div class="form-item col-6">
                        <label>门店名称</label><div class="form-item col-9">
                            <input disabled class="yqx-input" value="${marketingCase.shop.name}">
                        </div>
                    </div>
                </#if><#if marketingCase.marketingColumnConfig.mobile == 1 ><div class="form-item col-6">
                        <label>门店电话</label><div class="form-item col-9">
                            <input class="yqx-input" disabled value="${marketingCase.shop.tel}">
                        </div>
                    </div>
                </#if>
            </div>
        </#if>
        <#if marketingCase.marketingColumnConfig.address == 1>
            <div class="show-grid">
                <label>门店地址</label><div class="form-item col-5">
                    <input disabled class="yqx-input" value="${marketingCase.shop.address}">
                </div>
            </div>
        </#if>
        </form>
        <form class="container list-container hide">
                <div class="show-grid">
<#if marketingCase.marketingColumnConfig.serviceName ==1>
                        <i class="width-320">服务名称</i>
    </#if>
<#if marketingCase.marketingColumnConfig.serviceNote ==1>
                        <i class="width-320">服务描述</i>
</#if>
<#if marketingCase.marketingColumnConfig.servicePrice ==1>
                        <i class="width-80">服务价格</i>
</#if>
                </div>
        <#if marketingCase.marketingColumnConfig.serviceName ==1 || marketingCase.marketingColumnConfig.serviceNote ==1|| marketingCase.marketingColumnConfig.servicePrice == 1>
                <#list marketingCase.serviceInfos as item>
                    <div class="show-grid">
                        <input hidden name="marketingServiceId" value="${item.id}"/>
                        <input hidden name="serviceId" value="${item.serviceId}"/>
                        <input hidden name="type" value="1"/>
                        <input hidden name="maxCol" value="${marketingCase.serviceNum}">
                        <#if marketingCase.marketingColumnConfig.serviceName ==1>
                        <div class="form-item width-320">
                            <input name="serviceName" value="${item.serviceName}" class="yqx-input"/>
                        </div>
                        </#if>
                        <#if marketingCase.marketingColumnConfig.serviceNote ==1>
                        <div class="form-item width-320">
                            <input name="serviceNote" value="${item.serviceNote}" class="yqx-input"/>
                        </div>
                        </#if>
                        <#if marketingCase.marketingColumnConfig.servicePrice ==1>
                        <div class="form-item width-80">
                            <input name="servicePrice" width="80" value="${item.servicePrice}" class="yqx-input"/>
                            </div>
                        </#if>
                            <div class="form-item">
                                <div class="delete-icon js-del-service"></div>
                                </div>
                    </div>
                </#list>
                <div class="show-grid">
                    <input name="serviceId" value="" hidden>
                    <#if marketingCase.marketingColumnConfig.serviceName ==1>
                    <div class="form-item width-320">
                    <input name="serviceName" class="yqx-input">
                    </div>
                    </#if>

                    <#if marketingCase.marketingColumnConfig.serviceNote ==1>
                    <div class="form-item width-320">
                        <input name="serviceNote" class="yqx-input" placeholder="请输入服务描述"/>
                    </div>
                    </#if>

                    <#if marketingCase.marketingColumnConfig.servicePrice ==1>
                    <div class="form-item width-80">
                        <input name="servicePrice" width="80" class="yqx-input" disabled/>
                        </div>
                    </#if>
                    <div class="form-item">
                        <div class="add-icon js-add-service"></div>
                    </div>
                </div>

        </#if>
            </form>
            <div class="show-grid">
                <button class="yqx-btn yqx-btn-2 js-publish">发布</button>
            </div>
        </div>
    </div>
</div>
<script type="text/template" id="serviceTpl">

        <#assign col_width=0>
        <#if marketingCase.marketingColumnConfig.serviceName == 1>
            <#assign col_width=col_width + 223>
        </#if>
        <#if marketingCase.marketingColumnConfig.serviceNote == 1>
            <#assign col_width=col_width + 223>
        </#if>
        <#if  marketingCase.marketingColumnConfig.servicePrice == 1>
            <#assign col_width=col_width + 87>
        </#if>
        <ul class="yqx-downlist-content js-downlist-content" style="width: ${col_width}px;">
            <li class="yqx-downlist-title">
                <#if marketingCase.marketingColumnConfig.serviceName ==1>
                <span class="col-5">服务名称</span>
                </#if>
                <#if marketingCase.marketingColumnConfig.serviceNote ==1>
                <span class="col-5">服务描述</span>
                </#if>
                <#if marketingCase.marketingColumnConfig.servicePrice ==1>
                <span class="col-2">服务价格</span>
                </#if>
            </li>
            <%for(var i=0;i<templateData.length;i++){
            var item = templateData[i];%>
            <li class="js-downlist-item">
                <#if marketingCase.marketingColumnConfig.serviceName ==1>
                <span class="col-5 js-show-tips"><%= item.name%></span>
                </#if>
                <#if marketingCase.marketingColumnConfig.serviceNote ==1>
                <span class="col-5 js-show-tips"><%= item.serviceNote%></span>
                </#if>
                <#if marketingCase.marketingColumnConfig.servicePrice ==1>
                <span class="col-2"><%= item.servicePrice%></span>
                </#if>
            </li>
            <%}%>
        </ul>
</script>
<script id="scancopyTpl" type="text/html">
    <!-- 直接分享模板 -->
    <div class="scan-pop-box">
        <div class="scan-pop-content">
            <img src="<%=data.src%>" width="130" height="130"/>

            <p class="pop-scan-tip">扫一扫 轻松分享</p>

            <div class="pop-copy-box">
                <label for="copy-input">分享地址：</label><input
                    id="copy-input" class="copy-input yqx-input" type="text" value="<%=data.url%>" disabled/>

                <div class="btn-copy js-copy">复制
                </div>
            </div>
        </div>
    </div>
</script>
<#include "yqx/layout/footer.ftl">
<#-- 复制 fallback -->
<script src="${BASE_PATH}/resources/script/libs/zclip/jquery.zclip.min.js?34bc3e8a9f22d426127c21e43ba1039f"></script>
<script src="${BASE_PATH}/static/js/page/activity/act-edit.js?09a93ae7b049c470e9846ef08476a2c3"></script>

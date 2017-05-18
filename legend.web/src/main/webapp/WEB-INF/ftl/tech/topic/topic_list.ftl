<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/technology_center/topic_common.css?773aa33c606d7b1e643b49631e5c5909" />
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/technology_center/technology_special.css?7161b4f4f3f81394fd419320d3865581"/>
<div class="wrapper">
    <div class="search_tech-box">
        <label>汽车维修技术搜索：</label>
        <input type="text" id="search_keywords" class="search_tech_keyword txt" placeholder="请输入关键词..."/>
        <button type="button" class="search_tech-btn" id="search_book_btn"></button>
    </div>
<#include "tech/left_menu.ftl" >
<#include "tech/recommend.ftl" >
</div>
<div class="right">
    <div class="container">
    <#if topicList>
        <#if topicList?size==1>
            <#list topicList as topic>
                <div class="innerbox">
                    <div class="scroll-left img-box">
                        <img src="${topic.pic}">
                    </div>
                    <div class="scroll-right">
                        <h2> <a target="_blank" href="${BASE_PATH}/shop/tech/topic/info?id=${topic.id}">${topic.title}</a></h2>
                        <div class="lable">
                            <#if topic.keywordsList>
                                <#list topic.keywordsList as keywords>
                                    <span class="lable-name">${keywords}</span>
                                </#list>
                            </#if>
                        </div>
                        <p>
                            <#if topic.summary>
                                <#if topic.summary?length gt 70>
                                ${topic.summary?substring(0,70)}...
                                <#else>
                                ${topic.summary}
                                </#if>
                            </#if>
                        </p>
                    </div>
                </div>
            </#list>
        <#else>
            <div id="slides">
                    <#list topicList as topic>
                        <div class="innerbox">
                            <div class="scroll-left img-box">
                                <img src="${topic.pic}">
                            </div>

                            <div class="scroll-right">
                                <h2> <a target="_blank" href="${BASE_PATH}/shop/tech/topic/info?id=${topic.id}">${topic.title}</a></h2>
                                <div class="lable">
                                    <#if topic.keywordsList>
                                        <#list topic.keywordsList as keywords>
                                            <span class="lable-name">${keywords}</span>
                                        </#list>
                                    </#if>
                                </div>
                                <p>
                                    <#if topic.summary>
                                        <#if topic.summary?length gt 70>
                                        ${topic.summary?substring(0,70)}...
                                        <#else>
                                        ${topic.summary}
                                        </#if>
                                    </#if>
                                </p>
                            </div>
                        </div>
                    </#list>
                </div>
        </#if>
    <#else>
        <div class="innerbox">
            <div class="scroll-left">
                <img src="../../resources/images/technology_center/scrollimg.jpg">
            </div>
            <div class="scroll-right">
                <h2>奔驰四轮定位实战案例</h2>
                <div class="lable">
                    <span class="lable-name">奔驰</span>
                    <span class="lable-name">案例</span>
                    <span class="lable-name">四轮定位</span>
                </div>
                <p>发动机的装合包括发动机、各组件的装配和发动机总装配两部分。安装的步骤随发发动机的装合包括发动机、各组件的装配和发动机总装配两部分。安装的步骤随发动机...</p>
            </div>
        </div>
    </#if>
    </div>

    <form name="searchForm" id="searchForm" action="${BASE_PATH}/shop/tech/topic/list">
        <input type="hidden" name="page" value="1">
        <input type="hidden" name="search_catId" id="search_catId"/>
        <div class="center-box">
            <div class="tabs">
                <ul class="my-tabs">
                    <input type="hidden" name="search_type" class="search_type" value=" <#if defaultType==2>2<#else>1</#if>">
                    <li class="topicType default" value="<#if defaultType==2>2<#else>1</#if>"><#if defaultType==2>基本资料<#else>维修案例</#if></li>
                </ul>
                <div class="tab-box">
                    <ul id="topicCatContent">
                    <#list topicCatList as topicCat>
                        <li class="topicCat" value="${topicCat.id}">${topicCat.name}</li>
                    </#list>
                    </ul>
                    <script type="text/html" id="topicCatContentTpl">
                        <%if(templateData!=null){ var topicCatSize = templateData.length;%>
                        <% for(var z=0;z<topicCatSize;z++){%>
                        <% var topicCat = templateData[z]%>
                        <li class="topicCat" value="<%=topicCat.id%>"><%= topicCat.name%></li>
                        <%}%>
                        <%}%>
                    </script>
                </div>
                <div class="more">
                    <span class="more-line"></span>
                    <a href="javascript:void(0)" class="more-down"><span class="arrow-down">更多</span></a>
                </div>
            </div>
            <div class="center-img" id="content">

            </div>
            <script type="text/html" id="contentTemplate">
                <%if(templateData!=null){ var topicSize = templateData.length;%>
                <% for(var i=0;i<topicSize;i++){%>
                <% var topic = templateData[i]%>
                <a href="${BASE_PATH}/shop/tech/topic/info?id=<%=topic.id%>" target="_blank">
                    <div class="content">
                        <div class="img-box"><img src="<%=topic.pic%>"></div>
                        <p><%if(topic.title.length > 16){%>
                            <%= topic.title.substring(0,16)%>...
                            <%}else{%>
                            <%= topic.title%>
                            <%}%>
                        </p>
                        <div class="lable">
                            <%if(topic.keywordsList!=null){var keywordsSize= topic.keywordsList.length;%>
                            <%if(keywordsSize==0){%>
                                <span class="lable-clear">&nbsp;</span>
                            <%}else{%>
                            <%for(var j=0;j<keywordsSize;j++){%>
                                <span class="lable-name"><%=topic.keywordsList[j]%></span>
                            <%}%>
                            <%}%>
                            <%}%>
                        </div>
                    </div>
                </a>
                <%}%>
                <%}%>
            </script>
        </div>
    </form>
</div>
</div>
<div id="pageDiv" class="text-center">
    <div class="clear"></div>
</div>


<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.slides.min.js"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/pagination.js?3f20a000af0278d601fa443295a4a7a3"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/tech/topic/topic_list.js?4a3ec2ccad536216b29dab3a8c1ee90c"></script>

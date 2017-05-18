<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/technology_center/topic_common.css?773aa33c606d7b1e643b49631e5c5909"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/technology_center/search_list.css?e283bcfa90345e29ceb9bf77657c074c"/>

<div class="wrapper">
    <form name="searchForm" id="searchForm" action="${BASE_PATH}/shop/tech/search/list" onsubmit="return false">
        <div class="search_tech-box">
            <label>汽车维修技术搜索：</label>
            <input type="hidden" name="page" value="1"/>
            <input type="text" name="search_keywords" class="search_tech_keyword txt" placeholder="请输入关键词..." value="${keywords}"/>
            <button type="button" class="search_tech-btn"></button>
        </div>
    <#include "tech/left_menu.ftl" >
    <#include "tech/recommend.ftl" >
</div>
<div class="right">
    <div class="center-box clearfix">
        <div id="content">
        </div>
        <script id="contentTemplate" type="text/html">
            <ul class="clearfix">
                <%for(var index in templateData){%>
                <%item = templateData[index]%>
                <li>
                    <div class="item_box">
                        <div class="img_box">
                            <%
                                var url = "";
                                if(item.libraryType == "book"){
                                    url = "${BASE_PATH}/shop/tech/book/info?id="+item.id;
                                }else if(item.libraryType == "topic"){
                                    url = "${BASE_PATH}/shop/tech/topic/info?id="+item.id;
                                }

                                var imgUrl = "";
                                if(item.cover == "" || item.cover == null){
                                    imgUrl = "${BASE_PATH}/resources/images/technology_center/search_default.jpg";
                                }else{
                                    imgUrl = item.cover;
                                }
                            %>
                            <a target="_blank" href="<%=url%>"><img src="<%=imgUrl%>" height="180"></a>
                        </div>
                        <div class="txt1" title="<%=item.title%>">
                            <%=$substr(item.title,15)%>
                        </div>
                        <div class="tags_list">
                            <%if(item.libraryType == "book"){%>
                                <span><%=item.carBrand%></span>
                                <span><%=item.carSeries%></span>
                                <span><%=item.carYear%></span>
                            <%}else if(item.libraryType == "topic"){
                                if(item.keywords != null && item.keywords!=""){
                                    var arr = item.keywords.split(",");
                                    for(var i=0;i<arr.length;i++){
                            %>
                                       <span><%=arr[i]%></span> 
                            <%
                                    }
                                }
                            %>
                            <%}%>
                        </div>
                    </div>
                </li>
                <%}%>
            </ul>
        </script>
        <div id="pageDiv" class="text-center qxy_page">
            <div class="qxy_page_inner"></div>
        </div>
    </div>
</div>
</form>
</div>



<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/paging.js?c4ece14e0f4c534b806efc96ae712410"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/tech/book/search_list.js?7350e82d2355047bbb0f690fe02ed354"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/tech/book/book_area.js?622317b809ced84115b98269594ff0d1"></script>

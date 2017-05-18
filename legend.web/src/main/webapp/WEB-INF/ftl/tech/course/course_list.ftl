<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/technology_center/topic_common.css?773aa33c606d7b1e643b49631e5c5909"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/technology_center/technology_train.css?c106aba99453b7065ead21d5f0bcff4b"/>
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
<div class="center-box">
    <div class="tabs">
        <ul class="my-tabs">
            <li <#if courseTab=="time">class="default"</#if> onclick="javascript:window.location.href='${BASE_PATH}/shop/tech/course?flag=1'">按日期</li>
            <li <#if courseTab=="cat">class="default"</#if> onclick="javascript:window.location.href='${BASE_PATH}/shop/tech/course?flag=2'">按课程</li>
        </ul>
        <form name="searchForm" id="searchForm" action="${BASE_PATH}/shop/tech/course/list">
            <input type="hidden" name="page" value="1">
            <input type="hidden" name="search_searchTime" value="" id="search_searchTime">
            <input type="hidden" name="search_catId" value="" id="search_catId">
        <div class="catList">
        <#if flag==1>
            <div class="tab-box" style="border-bottom: none">
                <ul style="float: left;width: 710px">
                    <#list dateList as item>
                        <li class="chooseTime" date-str="${item.date}">${item.dateStr}</li>
                    </#list>
                </ul>
            </div>
        <#else>
        <#--</div>-->
        <#--<div class="catList">-->
            <#list catList as cat>
                <#if cat_index == catList?size-1>
                    <div class="tab-box" style="border-bottom: none">
                        <div class="total-title">${cat.name}</div>
                        <ul>
                            <#list cat.childCatList as childCat>
                                <li class="courseCat" child-catId="${childCat.id}">${childCat.name}</li>
                            </#list>
                        </ul>
                    </div>
                <#else>
                    <div class="tab-box">
                        <div class="total-title">${cat.name}</div>
                        <ul>
                            <#list cat.childCatList as childCat>
                                <li class="courseCat" child-catId="${childCat.id}">${childCat.name}</li>
                            </#list>
                        </ul>
                    </div>
                </#if>
            </#list>
        </#if>
        </div>
            <div class="more">
                <span class="more-line"></span>
                <a href="javascript:void(0)" class="more-down"><span class="arrow-down">更多</span></a>
            </div>
        </form>
    </div>
    <div class="right-center">
        <div id="content">

        </div>
        <script type="text/html" id="contentTemplate">
            <%if(templateData!=null){ var courseSize = templateData.length;%>
            <% for(var i=0;i<courseSize;i++){%>
            <% var course = templateData[i];%>
            <% var count=0;%>
            <%if(course.courseDetailList!=null){%>
            <% count = course.courseDetailList.length;%>
            <%}%>
            <div class="content">
                <div class="content-left">
                    <a href="${BASE_PATH}/shop/tech/course/detail?id=<%=course.id%>">
                    <img src="<%= course.pic%>" class="courseImg">
                    <p>
                        <%if(course.title.length>8){%>
                        <%= course.title.substring(0,8)%>...
                        <%}else{%>
                        <%=course.title%>
                        <%}%>
                        (<%= count%>课程)
                    </p>
                    </a>
                </div>
                <!--轮播部分-->
                <div class="content-right" id="lunbo<%=i+1%>" class="lunbo">
                    <div class="disleftBtn <%if(count<=3){%>visible<%}%>"></div>
                    <div class="container">
                        <div class="inner">
                            <%var childList = course.courseDetailList;%>
                            <%if(childList!=null){var childSize = childList.length;%>
                            <%for(var j=0;j<childSize;j++){%>
                            <%var courseDetail = childList[j];%>
                                <div class="innerbox">
                                    <a href="${BASE_PATH}/shop/tech/course/detail?id=<%=course.id%>">
                                        <div class="imgbox"><img src="<%= courseDetail.pic%>" class="detailImg"></div>
                                        <p<%if(courseDetail.outDate){%> class="outDate"<%}%>>
                                            <%if(courseDetail.title.length>7){%>
                                                <%= courseDetail.title.substring(0,7)%>...
                                            <%}else{%>
                                                <%=courseDetail.title%>
                                            <%}%>
                                        </p>
                                        <p class="detailDate<%if(courseDetail.outDate){%> outDate<%}else{%><%if(courseDetail.checkDate==0){%> orange<%}%><%}%>"><%= courseDetail.dateStr%></p>
                                    </a>
                                </div>
                            <%}%>
                            <%}%>
                        </div>
                    </div>
                    <div class="rightBtn <%if(count<=3){%>visible<%}%>"></div>
                </div>
            </div>
            <%}%>
            <%}%>
        </script>
    </div>
    <div id="pageDiv" class="text-center">
        <div class="clear"></div>
    </div>
</div>
</div>
</div>
<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.slides.min.js"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/pagination.js?3f20a000af0278d601fa443295a4a7a3"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/tech/course/course_list.js?aa00211d68c7e9b1150886b69d869544"></script>

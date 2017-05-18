<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/technology_center/topic_common.css?773aa33c606d7b1e643b49631e5c5909"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/technology_center/train_detail.css?96e6cfa8954f56f144680beda65f5702"/>
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
            <div class="tabs-left course-tail"><img src="${course.pic}"></div>
            <div class="text">
                <h2>${course.title}(<#if course.courseDetailList>${course.courseDetailList?size}<#else>0</#if>课程)</h2>
                ${course.content}
            </div>
        </div>
        <div class="right-center">
            <#list course.courseDetailList as courseDetail>
                <div class="content">
                    <input type="hidden" class="detailId" value="${courseDetail.id}"/>
                    <input type="hidden" class="id" value="${course.id}"/>
                    <div class="content-left">
                        <img src="${courseDetail.pic}">
                    </div>
                    <div class="content-center">
                        <h2>${courseDetail.dateStr}<span class="splite">|</span> <span class="blue">${course.catName}</span></h2>
                        <div class="content-text">${courseDetail.content}</div>
                        <div class="location">${courseDetail.address}</div>
                    </div>
                    <div class="content-right">
                        <#if courseDetail.outDate>
                            <button type="button" href="#" class="attend disattend" disabled="disabled">报名参加</button>
                            <p class="isattend grey">${courseDetail.limitCount}人班 | 已过期<#if courseDetail.courseShopRel> | 已报名${courseDetail.courseShopRel.joinCount}人</#if></p>
                        <#elseif courseDetail.joinCount==courseDetail.limitCount>
                            <#if courseDetail.courseShopRel>
                                <button type="button" href="#" class="attend">重新报名</button>
                                <p class="isattend grey">${courseDetail.joinCount}人/${courseDetail.limitCount}人班<#if courseDetail.courseShopRel> | 我已报${courseDetail.courseShopRel.joinCount}人</#if></p>
                            <#else>
                                <button type="button" href="#" class="attend disattend" disabled="disabled">报名参加</button>
                                <p class="isattend grey">${courseDetail.joinCount}人/${courseDetail.limitCount}人班<#if courseDetail.courseShopRel> | 我已报${courseDetail.courseShopRel.joinCount}人</#if></p>
                            </#if>
                        <#else>
                            <button type="button" href="#" class="attend"><#if courseDetail.courseShopRel>重新报名<#else>报名参加</#if></button>
                            <p class="isattend">${courseDetail.joinCount}人/${courseDetail.limitCount}人班 <#if courseDetail.courseShopRel> | 我已报${courseDetail.courseShopRel.joinCount}人</#if></p>
                        </#if>
                    </div>
                </div>
            </#list>
        </div>
    </div>
</div>
<div class="clear">

</div>
</div>
<script type="text/html" id="attend-popup">
    <div class="attend-popup">
        <h1>报名申请</h1>
        <div class="attend-number">
            报名人数：
            <span class="number-box">
                <span class="reduce">-</span>
                <input class="number J_input_limit" data-limit_type="number" value="2" maxlength="1"></input>
                <span class="add">+</span>
            </span>
        </div>
        <button type="button" class="sure-btn">确认报名</button>
        <#--<input type="button" value="确认报名" class="sure-btn"/>-->
    </div>
</script>
<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/tech/course/course_detail.js?3d770d565befb8a72b344874a72bb8c5"></script>

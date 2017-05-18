<#include "layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/resources/css/technology_center/special_detail.css?20150429"/>
<div class="wrapper">
    <div class="search_tech-box">
        <label>汽车维修技术搜索：</label><input type="text"
                                       id="search_keywords" class="search_tech_keyword txt" placeholder="请输入关键词..."/><#--
        -->
        <button type="button" class="search_tech-btn" id="search_book_btn"></button>
    </div>
    <div class="title">
        <span class="title-china">技术中心</span>
        <span class="title-english">Technology Center</span>
    </div>
    <div class="content">
        <h1>${topic.title}</h1>
        <div class="content-detail">
            ${topic.content}
        </div>
        <div class="praise-box">
            <button type="button" class="like-btn" data-id="${topic.id}"><span class="like praise number">${topic.likeNum}</span></button>
            <button type="button" class="unlike-btn" data-id="${topic.id}"><span class="unlike praise number">${topic.unlikeNum}</span></button>
        </div>
    </div>
    <div class="banner">
        <a class="banner-box" href="${BASE_PATH}/shop/tech/topic">
            <img src="${BASE_PATH}/resources/images/technology_center/return.png" alt=""/>
            <div>返回</div>
        </a>
        <a class="banner-box" href="#">
            <img src="${BASE_PATH}/resources/images/technology_center/totop.png" alt=""/>
            <div>顶部</div>
        </a>
    </div>
</div>



<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/tech/topic/topic_repair_info.js?4459a42f4c39a5e742f17f905d60e6a8"></script>

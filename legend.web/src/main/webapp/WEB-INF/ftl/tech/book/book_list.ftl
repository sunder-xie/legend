<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/technology_center/topic_common.css?773aa33c606d7b1e643b49631e5c5909"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/technology_center/data_maintenance.css?7f7e83f43944583a3603351cdf8f5ff8"/>

<div class="wrapper">
    <form name="searchForm" id="searchForm" action="${BASE_PATH}/shop/tech/book/list">
        <div class="search_tech-box">
            <label>汽车维修技术搜索：</label>
            <input type="hidden" name="page" value="1"/>
            <input type="text" name="search_keywords" class="search_tech_keyword txt" placeholder="请输入关键词..." value="${keywords}"/>
            <button type="button" class="search_tech-btn"></button>
        </div>
        <#include "tech/left_menu.ftl" >
            <div class="choice-car">
                <div class="list-text">选择车型</div>
                <div class="list-detail">
                    <div class="name">汽车厂家</div>
                    <input type="hidden" name="search_carBrand" id="search_carBrand">
                    <select id="level2" class="check-type"
                            child="level3,level4" onchange="areaChangeCallback(this);"></select>
                </div>
                <div class="list-detail">
                    <div class="name">车型系列</div>
                    <input type="hidden" name="search_carSeries" id="search_carSeries">
                    <select id="level3" class="check-type"
                            child="level4" onchange="areaChangeCallback(this);"></select>
                </div>
                <div class="list-detail">
                    <div class="name">汽车年款</div>
                    <input type="hidden" name="search_carYear" id="search_carYear">
                    <select id="level4" class="check-type"
                            onchange="areaChangeCallback(this);"></select>
                </div>
                <div class="comform"><a href="javascript:void(0);" id="search_tech_btn">提交选择</a></div>
            </div>

        <#include "tech/recommend.ftl" >
</div>
<div class="right">
    <div class="scroll">
        <ul>
            <li class="scroll-box default active">
                <div class="person-box">
                    <div class="scroll-img">
                        <a href="javascript:void(0);"><img src="${BASE_PATH}/resources/images/technology_center/expert_list/lhl.jpg"></a>
                    </div>
                    <div class="scroll-msg">
                        <div class="name">罗河龙</div><div class="job">汽车维修专家技师</div><div class="experience">从事过林肯、福特、海马、SGMW等车型，有20多年从业经验</div>
                    </div>
                </div>
            </li>
            <li class="scroll-box">
                <div class="person-box">
                    <div class="scroll-img">
                        <a href="javascript:void(0);"><img src="${BASE_PATH}/resources/images/technology_center/expert_list/gj.jpg"></a>
                    </div>
                    <div class="scroll-msg">
                        <div class="name">郭金</div><div class="job">汽车专家级技师</div><div class="experience">从事过本田、大众等车型，有10多年从业经验</div>
                    </div>
                </div>
            </li>
            <li class="scroll-box">
                <div class="person-box">
                    <div class="scroll-img">
                        <a href="javascript:void(0);"><img src="${BASE_PATH}/resources/images/technology_center/expert_list/ljw.jpg"></a>
                    </div>
                    <div class="scroll-msg">
                        <div class="name">刘文江</div><div class="job"> 宝马专家级认证技师、二级内训师</div><div class="experience">从事别克，宝马等车型有10多年从业经验。</div>
                    </div>
                </div>
            </li>
            <li class="scroll-box">
                <div class="person-box">
                    <div class="scroll-img">
                        <a href="#"><img src="${BASE_PATH}/resources/images/technology_center/expert_list/hj.jpg"></a>
                    </div>
                    <div class="scroll-msg">
                        <div class="name">黄健</div><div class="job">宝马专家级认证技师</div><div class="experience">从事丰田，宝马等车型，有10多年从业经验。</div>
                    </div>
                </div>
            </li>
        </ul>
        <a href="#" class="more"><img src="${BASE_PATH}/resources/images/technology_center/more.png"></a>
    </div>
    <div class="center-box">
        <div id="content"></div>
        <script id="contentTemplate" type="text/html">
            <%for(var index in templateData){%>
            <%item = templateData[index]%>
            <div class="list-box">
                <div class="img-box"><a target="_blank" href="${BASE_PATH}/shop/tech/book/info?id=<%=item.id%>"><img src="<%=item.cover%>" width="80" height="110"></a></div>
                <div class="text-box">
                    <div class="text-title">
                        <a target="_blank" href="${BASE_PATH}/shop/tech/book/info?id=<%=item.id%>"><span class="name"><%=item.title%></span></a>
                    </div>
                    <div class="text">
                        <a target="_blank" href="${BASE_PATH}/shop/tech/book/info?id=<%=item.id%>">
                            <span class="type"></span>
                        </a>
                    <#--<div class="text"><a href="${BASE_PATH}/shop/tech/book/info?id=<%=item.id%>"><%=item.summary%></a>-->
                    </div>
                    <div class="text">
                        <a target="_blank" href="${BASE_PATH}/shop/tech/book/info?id=<%=item.id%>">
                            <span class="type"><%=item.carBrand%></span>
                        </a>
                    </div>
                    <div class="text">
                        <a target="_blank" href="${BASE_PATH}/shop/tech/book/info?id=<%=item.id%>">
                            <span class="type"> <%=item.carSeries%> <%=item.carYear%></span>
                        </a>
                    </div>
                    <div class="zan">
                        <a href="javascript:void(0)" class="doLike" data-id="<%=item.id%>">
                            <span class="zan-up"></span>
                            <span class="number"><%=item.likeNum%></span>
                        </a>
                        <span class="line"></span>
                        <a href="javascript:void(0)" class="doUnlike" data-id="<%=item.id%>">
                            <span class="zan-down"></span>
                            <span class="number"><%=item.unlikeNum%></span>
                        </a>
                    </div>
                </div>
            </div>
            <%}%>
        </script>
        <div class="clear"></div>
        <div id="pageDiv" class="text-center" style="margin-left: 10px">
            <div class="clear"></div>
        </div>
    </div>
</div>
    </form>
</div>


<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/pagination.js?3f20a000af0278d601fa443295a4a7a3"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/tech/book/book_list.js?fe045a96e48c8bb308aefc7fec94a3b7"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/tech/book/book_area.js?622317b809ced84115b98269594ff0d1"></script>

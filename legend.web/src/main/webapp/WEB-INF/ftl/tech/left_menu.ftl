<div class="title">
    <span class="title-china">淘汽知道</span>
    <span class="title-english">Tqmall Knows</span>
</div>
<div class="left">
<div class="left-menu">
    <a href="${BASE_PATH}/shop/tech/book">
        <div class="menu-item <#if techTab="book">active</#if>">
            <span class="item-img1"></span>
            <span class="tab">维修资料库</span>
        <#if techTab="book"><span class="arrow"></span></#if>
        </div>
    </a>
    <a href="${BASE_PATH}/shop/tech/topic?type=2">
        <div class="menu-item <#if defaultType="2">active</#if>">
            <span class="item-img2"></span>
            <span class="tab">技术专题</span>
        <#if defaultType="2"><span class="arrow"></span></#if>
        </div>
    </a>
    <a href="${BASE_PATH}/shop/tech/topic">
        <div class="menu-item <#if defaultType="1">active</#if>">
            <span class="item-img4"></span>
            <span class="tab">维修案例</span>
        <#if defaultType="1"><span class="arrow"></span></#if>
        </div>
    </a>
    <a href="${BASE_PATH}/shop/tech/course">
        <div class="menu-item no-border <#if techTab="course">active</#if>">
            <span class="item-img3"></span>
            <span class="tab">技能培训</span>
        <#if techTab="course"><span class="arrow"></span></#if>
        </div>
    </a>
</div>
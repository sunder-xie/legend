<style>
    .aside {
        float: left;
        width: 140px;
    }
    /* 左侧导航栏 */
    .nav {
        /*padding: 0 18px;*/
        font-size: 12px;
        color: #666;
        background: #fff;
        border: 1px solid #e8e8e8;
    }
    .nav-item dt, .nav-item dd, .nav-title {
        padding: 0 18px;
    }
    .nav-item dt, .nav-title {
        line-height: 43px;
        font-size: 14px;
    }
    .nav-title {
        color: #062c5f;
        border-bottom: 1px solid #e8e8e8;
    }
    .nav-title a {
        font-weight: 700;
    }
    .nav-item {
        border-bottom: 1px solid #e8e8e8;
    }
    .nav-item:last-child {
        border-bottom: none;
    }
    .nav-item dt {
        font-weight: 700;
        color: #535353;
    }
    .nav-item dd {
        line-height: 30px;
        color: #666;
    }
    .nav-item dd.nav_cur {
        padding-left: 15px;
        background: #f0f0f0;
        border-left: 3px solid #8eb210;
    }
    .nav-item dt.nav_cur a, .nav-title.nav_cur a {
        font-weight: 900;
        color: #000;
    }
    .notice {
        padding: 10px 20px;
    }
    .notice img {
        width: 20px;
        height: 20px;
        padding-right: 10px;
        vertical-align: middle;
    }
    a{ color: #333;}
</style>
<div class="aside">
    <div class="nav">
        <p class="nav-title<#if actTab == 'main'> nav_cur</#if>"><a href="${BASE_PATH}/shop/activity/main">活动报名</a></p>
        <dl class="nav-item">
            <dt<#if actTab == 'bill_list' || actTplId> class="nav_cur"</#if>><a href="javascript:void(0);">活动快速核销</a></dt>
            <div class="act-tab"></div>
            <dd<#if actTab == 'bill_list'> class="nav_cur"</#if>><a href="${BASE_PATH}/shop/activity/bill/list">核销单列表</a></dd>
        </dl>
        <dl class="nav-item">
            <dt <#if actTab == 'achievement'> class="nav_cur"</#if>><a href="${BASE_PATH}/shop/activity/achievement">活动业绩</a></dt>
        </dl>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        seajs.use(["ajax", "art"], function (ax, at) {
            $.ajax({
                url: BASE_PATH + '/shop/activity/shop_act/list',
                success: function (result) {
                    if (result.success) {
                        var html = at('actContTemplate',{json:result.data});
                        $(".act-tab").append(html).css('display', 'block');
                    }
                    else {
                        $(".act-tab").css('display', 'none');
                    }
                }
            });
        });
    });
</script>
<script type="text/html" id="actContTemplate">
    <%if(json){%>
    <% for(var i = 0;i < json.length; i++){%>
    <% var shopActivity = json[i]%>
    <#if actTplId>
    <% var actTplId = ${actTplId};%>
    </#if>
    <dd<%if(shopActivity.actTplId == actTplId){%> class="nav_cur"<%}%>><a href="${BASE_PATH}/shop/activity/join?acttplid=<%= shopActivity.actTplId%>"><%= shopActivity.byName%></a></dd>
    <%}%>
    <%}%>
</script>
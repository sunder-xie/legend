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
    <#--.nav-item dt:before, .nav-title:before {-->
        <#--display: inline-block;-->
        <#--content: "";-->
        <#--width: 18px;-->
        <#--height: 18px;-->
        <#--margin-right: 7px;-->
        <#--background: url("${BASE_PATH}/resources/images/drainage_activities/sprite.png") no-repeat;-->
        <#--vertical-align: middle;-->
    <#--}-->
    .nav-title {
        color: #062c5f;
        border-bottom: 1px solid #e8e8e8;
    }
    .nav-title a {
        font-weight: 700;
    }
    /*.nav-title:before {*/
        /*background-position: 0 0;*/
    /*}*/
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
    /*.nav-voucher dt:before {*/
        /*background-position: 0 -36px;*/
    /*}*/
    /*.nav-payment dt:before {*/
        /*background-position: 0 -72px;*/
    /*}*/
    /*.nav-more dt:before {*/
        /*background-position: 0 -105px;*/
    /*}*/
    .notice {
        padding: 10px 20px;
    }
    .notice img {
        width: 20px;
        height: 20px;
        padding-right: 10px;
        vertical-align: middle;
    }
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
    $(function ($) {
        seajs.use(["ajax", "artTemplate"], function (ajax, art) {
            ajax.get({
                url: BASE_PATH + '/shop/activity/shop_act/list',
                success: function (result) {
                    if (result.data) {
                        var html = art.render('actContTemplate', {'templateData': result.data});
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
    <%if(templateData){%>
    <% var shopActivitySize = templateData.length;%>
    <% for(var i = 0;i < shopActivitySize; i++){%>
    <% var shopActivity = templateData[i]%>
    <#if actTplId>
    <% var actTplId = ${actTplId};%>
    </#if>
    <dd<%if(shopActivity.actTplId == actTplId){%> class="nav_cur"<%}%>><a href="${BASE_PATH}/shop/activity/join?acttplid=<%= shopActivity.actTplId%>"><%= shopActivity.byName%></a></dd>
    <%}%>
    <%}%>
</script>
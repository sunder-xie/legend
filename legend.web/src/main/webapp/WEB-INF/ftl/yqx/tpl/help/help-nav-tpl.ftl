<!-- 样式start -->
<style>
    .guide {
        width: 100%;
        line-height: 45px;
        overflow: hidden;
    }
    .help_tit {
        font-size: 18px;
        font-weight: bold;
        color: #1f2a66;
    }
    .help_tit label {
        color:#bebebe;
        font-style: italic;
    }
    .sidebar {
        width:220px;
        float:left;
    }
    .firstNav {
        cursor:pointer;
    }
    .firstNav>li {
        line-height: 30px;
        padding:15px 0;
    }
    .bottomBorder {
        border-bottom:1px solid #e8e8e8;
    }
    .fItem {
        line-height: 40px;
        padding-left: 15px;
        font-size:14px;
        color:#333;
        font-weight: bold;
        background:url("${BASE_PATH}/static/img/page/help/extend.png") no-repeat 185px center;
    }
    .nItem {
        line-height: 40px;
        padding-left: 15px;
        font-size:14px;
        color:#333;
        font-weight: bold;
    }
    .contract {
        background:url("${BASE_PATH}/static/img/page/help/contract.png") no-repeat 185px center;
    }
    .sItem {
        height: 20px;
        padding-left:15px;
        line-height: 20px;
        margin-bottom: 5px;
        overflow: hidden;
    }
    .tItem {
        padding-left: 30px;
        height: 20px;
        line-height: 20px;
        margin-bottom: 5px;
        overflow: hidden;
    }
    .secondNav li a:hover{color: #666;}
    .title_first p {
        padding-left: 12px;
        color: #111;
        background-color: #f9f9f9;
        border-left: 3px solid transparent;
    }

    .current p {
        padding-left: 12px;
        color: #111;
        border-left: 3px solid #3a7ffc;
        background-color: #f9f9f9;
    }
    .current .tItem {
        padding-left: 27px;
    }
    .top {
        display: block;
        width:43px;
        height:43px;
        position:fixed;
        right:60px;
        bottom:60px;
        background:#fff url("${BASE_PATH}/static/img/page/help/top.png") no-repeat center;
    }
    .childCat{
        font-weight:bold;
    }
    a p:hover{
        background-color: #eee;
    }
    a{
        color: black;
    }
</style>
<!-- 样式end -->

<div class="guide"><span class="help_tit">帮助&nbsp;<label>help</label></span></div>
<div class="sidebar" id="sidebar">
    <ul class="firstNav js-menu-content">

    </ul>
</div>
<div class="clear"></div>
<a id="to-top" class="top" href="#"></a>

<!-- menu start -->
<script id="menuTpl" type="text/html">
    <li class="bottomBorder">
        <p>
            <a class="nItem" href="${BASE_PATH}/shop/help/notice">系统公告</a>
        </p>
    </li>
    <li class="bottomBorder">
        <p>
            <a class="nItem" href="${BASE_PATH}/shop/help/feedback/feedback-list">历史反馈问题</a>
        </p>
    </li>
    <#if articleDetail>
    <%var articleDetailId = ${articleDetail.id};%>
    </#if>
    <%if(data && data.length>0){%>
        <%for(var i=0;i<data.length;i++){%>
        <%var menu = data[i];%>
        <%var articleList = menu.articleList;%>
        <%var childCatList = menu.childCatList;%>
        <li class="bottomBorder">
            <p class="fItem"><%= menu.title%></p>
            <ul class="secondNav">
                <%if(articleList){%>
                    <%for(var j=0;j<articleList.length;j++){%>
                    <%var article = articleList[j];%>
                    <li<%if(article.id == articleDetailId){%> class="current"<%}%>><a
                        href="${BASE_PATH}/shop/help?type=${type}&id=<%= article.id%>"><p class="sItem"><%= article.title%></p></a></li>
                    <%}%>
                <%}%>
                <%if(childCatList){%>
                    <%for(var l=0;l<childCatList.length;l++){%>
                    <%var childCat = childCatList[l];%>
                    <%var childArticleList = childCat.articleList;%>
                    <li><p class="sItem childCat"><%= childCat.title%></p>
                        <ul class="thirdNav">
                            <%if(childArticleList){%>
                                <%for(var m=0;m<childArticleList.length;m++){%>
                                <%var childArticle = childArticleList[m];%>
                                <li<%if(childArticle.id == articleDetailId){%> class="current"<%}%>><a
                                    href="${BASE_PATH}/shop/help?type=${type}&id=<%= childArticle.id%>"><p class="tItem"><%= childArticle.title%></p></a></li>
                                <%}%>
                            <%}%>
                        </ul>
                    </li>
                    <%}%>
                <%}%>
            </ul>
        </li>
        <%}%>
    <%}%>
</script>
<!-- menu end -->

<script>
    $(function(){
        //获取文章列表
        seajs.use([
            "ajax",
            "art"
        ],function(ax,at){
            $.ajax({
                type: "GET",
                url: BASE_PATH+"/shop/help/get-menu",
                success:function(result){
                    if (result.success != true) {
                        taoqi.failure(result.errorMsg);
                        return false;
                    }else{
                        var html = at('menuTpl',{"data":result.data});
                        $(".js-menu-content").html(html);
                    }
                }
            });

            //一级导航栏
            $('.sidebar').on("click", '.fItem', function () {
                $(this).parent().find('.secondNav').toggle(500);
                $(this).toggleClass("contract");
            });

            //二级导航栏
            $('.sidebar').on("click", '.sItem', function () {
                if ($(this).parent().find('.thirdNav').length > 0) {
                    $(this).parent().find('.thirdNav').toggle(500);
                } else {
                    $('.secondNav').find('li').removeClass("current");
                    $(this).parent().addClass("current");
                }
            });

            //三级导航栏
            $('.sidebar').on("click", '.tItem', function () {
                $('.secondNav').find('li').removeClass("current");
                $(this).parent().addClass("current");
            });

            $('.sidebar').on("click", '.tItem', function () {
                $('.secondNav').find('li').removeClass("current");
                $(this).parent().addClass("current");
            });

            //回到顶部开始
            $(window).scroll(function (e) {
                //若滚动条离顶部大于100元素
                if ($(window).scrollTop() > 0) {
                    $("#to-top").show();//以1秒的间隔渐显id=gotop的元素
                } else {
                    $("#to-top").hide();//以1秒的间隔渐隐id=gotop的元素
                }
            });
            //点击回到顶部的元素
            $("#to-top").click(function (e) {
                //以1秒的间隔返回顶部
                $('html,body').animate({scrollTop: '0px'}, 800);
            }).mouseover(function (e) {
                $(this).find("a").addClass("hover");
            }).mouseout(function (e) {
                $(this).find("a").removeClass("hover");
            });
            //回到顶部结束
        });
    });
</script>
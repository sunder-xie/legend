
$(document).ready(function ($) {
    var page = $("#page").val();
    getPageData(page);
    $(function () {
        (function init() {
            $('.sf_current .second_nav').show();
        })();
        //一级菜单
        $('.first_nav_tit').on("click", function () {

            $('.first_nav.sf_current').find(".second_nav").hide(500);
            $('.first_nav.sf_current').removeClass("sf_current");
            $(this).parent().find(".second_nav").show(500);
            $(this).parent().addClass("sf_current");
            getPageData($(this).data('page'));
        });
        $('.first_nav_tit_noarrow').on("click", function () {
            $('.first_nav.sf_current').find(".second_nav").hide(500);
            $('.first_nav.sf_current').removeClass("sf_current");
            $(this).parent().find(".second_nav").show(500);
            $(this).parent().addClass("sf_current");
            getPageData($(this).data('page'));
        });
        //二级菜单
        $('.second_nav li').on("click", function () {
            $('.second_nav li').removeClass("ss_current");
            $(this).addClass("ss_current");
            getPageData($(this).data('page'));
        });
    });


    $("#search_book_btn").click(function () {
        var keywords = $("#search_keywords").val();
        window.location.href = BASE_PATH+"/shop/tech/search?keywords="+keywords;
    });

    $(".search_tech_keyword").keyup(function(e){
        if(e.keyCode == 13){
            var keywords = $("#search_keywords").val();
            window.location.href = BASE_PATH+"/shop/tech/search?keywords="+keywords;
        }
    });

    $(".like-btn").click(function(){
        zanFun.zan($(this));
    });
    $(".unlike-btn").click(function(){
        zanFun.unZan($(this));
    });

    var zanFun = {
        zanclick: false,
        //unZanclick : false,
        zan: function (obj) {
            var dataId = obj.attr("data-id");
            if (!zanFun.zanclick) {
                $.ajax({
                    type: "GET",
                    url: BASE_PATH + "/shop/tech/book/doLike",
                    data : {id:dataId},
                    cache:false,
                    success: function (data, textStatus) {
                        if (data.success) {
                            var childNode = obj.find(".number"),
                                number = parseInt(childNode.text());
                            childNode.text(++number);
                            zanFun.zanclick = true;
                            return;
                        }
                        else {
                            return;
                        }
                    }
                })
            }

        },
        unZan: function (obj) {
            var dataId = obj.attr("data-id");
            if (!zanFun.zanclick) {
                $.ajax({
                    type: "GET",
                    url: BASE_PATH + "/shop/tech/book/doUnlike",
                    data : {id:dataId},
                    cache:false,
                    success: function (data, textStatus) {
                        if (data.success) {
                            var childNode = obj.find(".number"),
                                number = parseInt(childNode.text());
                            childNode.text(++number);
                            zanFun.zanclick = true;
                            return;
                        }
                        else {
                            return;
                        }
                    }
                })
            }
        }
    }

    $(".prev-btn").unbind("click").click(function(){
        getPageData(parseInt($("#page").val(),10)-1);
    });
    $(".next-btn").unbind("click").click(function(){
        getPageData(parseInt($("#page").val(),10)+1);
    });
    $(".redirect-btn").unbind("click").click(function(){
        getPageData(parseInt($(".pageNum").val(), 10));
    });

    $("#bookPreviewFull").click(function(){
        var search_bookId = $("#search_bookId").val();
        var page = $("#page").val();
        var url = BASE_PATH+"/shop/tech/book/preview_full?id="+search_bookId+"&page="+page;
        window.open(url);
    });

});


function getPageData(page) {
    if(page<1){
        page =1;
    }
    $("#page").val(page);
    $(".pageNum").val(page);
    var search_bookId = $("#search_bookId").val();
    var loading = taoqi.loading();
    $.ajax({
        type: 'get',
        dataType: 'json',
        url: BASE_PATH + '/shop/tech/book/page_list',
        data: {
            search_bookId: search_bookId,
            page: page
        },
        cache: false,
        success: function (result) {
            taoqi.close(loading);
            if(result.success) {
                if(page <1){
                    taoqi.error("页码必须从1开始");
                    return;
                }else if(page > result.data.totalPages ){
                    taoqi.error("已经是最后一页");
                    return;
                }
                $(".totalPage").text(result.data.totalPages);
                var templateHtml = template.render('contentTemplate', {'templateData': result.data.content});
                $('#content').html(templateHtml);
            }
        },error: function(XmlHttpRequest, textStatus, errorThrown){
            taoqi.close(loading);
            taoqi.error("数据异常");
        }
    });



}
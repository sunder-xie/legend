$(document).ready(function ($) {

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
                    url: BASE_PATH + "/shop/tech/topic/doLike",
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
                    url: BASE_PATH + "/shop/tech/topic/doUnlike",
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

    $(function() {
        window.onscroll = function(){    //右侧返回顶部按钮永远置于页面顶端
            var scrollHeight = window.scrollY;
            var $banner = $(".banner");
            if(scrollHeight > 10){
                $banner.css({'-webkit-transform':'translateY('+(scrollHeight-10)+'px)'});
            }
            else{
                $banner.css({'-webkit-transform':'translateY(0px)'});
            }
        }
        $(".return").click(function(){
            $('body').scrollTop(0);
        })
    });

});


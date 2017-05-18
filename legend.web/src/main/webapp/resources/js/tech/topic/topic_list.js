$(document).ready(function ($) {
    $("#searchForm").submit(function () {
        moreData($(this), "", false,false);
        return false;
    });

    $("#searchForm").submit();

    $(".search_tech_keyword").keyup(function(e){
        if(e.keyCode == 13){
            var keywords = $(".search_tech_keyword").val();
            window.location.href = BASE_PATH+"/shop/tech/search?keywords="+keywords;
        }
    });
    
    $("#search_book_btn").click(function () {
        var search_keywords = $("#search_keywords").val();
        window.location.href = BASE_PATH + "/shop/tech/search?keywords=" + search_keywords;
    });

//    $(function(){
//        //图片轮播效果
//        var isAnimating = false;
//        $(".scroll-box").on("mousemove",function(){
//            if($(this).hasClass('active')){
//                return ;
//            }
//            if(isAnimating){
//                return;
//            }
//            isAnimating = true;
//            $('.scroll .active').animate({width:'72px'},400).find('.person-box').animate({marginLeft:'-35px'},400).end().removeClass('active');
//            $(this).find('.person-box').animate({marginLeft:'0px'},400);
//            $(this).animate({width:'274px'},400,function(){
//                isAnimating = false;
//            });
//            $(this).addClass('active');
//
//        });
//
//    });

    $(document).on('click','.topicCat',function(){
        var catId = $(this).val();
        $("#search_catId").val(catId);
        $("#searchForm").submit();
    });

    $(document).on('click','.topicType',function(){
        var type = $(this).val();
        window.location.href = BASE_PATH + "/shop/tech/topic?type=" + type;
    });



    $(function(){
        (function showDown(){
            var tabBox = $(".tab-box"),
                tabBoxHeight = tabBox.find("ul").height();
            $(".more-down").on("click",function(){
                if(tabBoxHeight > 100){
                    if($(".more").find('.arrow-up').length){
                        $(".more .arrow-down").removeClass("arrow-up");
                        tabBox.animate({"height":"100px"});
                    }
                    else{
                        $(".more .arrow-down").addClass("arrow-up");
                        tabBox.animate({"height":(tabBoxHeight+20)});
                    }
                }
            })
            $(".tab-box").on("click","li",function(){
                $(".tab-box li").removeClass("check");
                $(this).addClass("check");
            })
        })()
    })

    $(function() {
        $('#slides').slidesjs({
            width: 228,
            height: 152,
            play: {
                active: true,
                auto: true,
                interval: 4000,
                swap: true
            }
        });
    });
});
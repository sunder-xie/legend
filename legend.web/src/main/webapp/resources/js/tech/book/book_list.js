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

    $(document).on('click', '.search_tech-btn', function () {
        //$("#searchForm").submit();
        var keywords = $(".search_tech_keyword").val();
        window.location.href = BASE_PATH+"/shop/tech/search?keywords="+keywords;
    });

    $(document).on('click', '#search_tech_btn', function () {
        var level2 = $.trim($("#level2").val());
        var level3 = $.trim($("#level3").val());
        var level4 = $.trim($("#level4").val());
        if(level2!=''){
            level2 = $.trim($("#level2").find("option:selected").text());
        }
        if(level3!=''){
            level3 = $.trim($("#level3").find("option:selected").text());
        }
        if(level4!=''){
            level4 = $.trim($("#level4").find("option:selected").text());
        }
        $("#search_carBrand").val(level2);
        $("#search_carSeries").val(level3);
        $("#search_carYear").val(level4);
        $("#searchForm").submit();
    });

    $(function(){
        //图片轮播效果
        var isAnimating = false;
        $(".scroll-box").on("mousemove",function(){
            if($(this).hasClass('active')){
                return ;
            }
            if(isAnimating){
                return;
            }
            isAnimating = true;
            $('.scroll .active').animate({width:'140px'},400).find('.person-box').end().removeClass('active');
            $(this).find('.person-box').animate({marginLeft:'0px'},400);
            $(this).animate({width:'288px'},400,function(){
                isAnimating = false;
            });
            $(this).addClass('active');

        })

        //赞和不赞
        $(document).on("click",".doLike",function(){
            zanFun.zan($(this));
        });
        $(document).on("click",".doUnlike",function(){
            zanFun.unZan($(this));
        });
        var zanFun = {
            zanclick : false ,
            //unZanclick : false,
            zan : function(obj){
                var dataId = obj.attr("data-id");
                if(!zanFun.zanclick){
                    $.ajax({
                        type : "GET",
                        url : BASE_PATH+"/shop/tech/book/doLike",
                        data : {id:dataId},
                        cache:false,
                        success : function(data, textStatus){
                            if(data.success){
                                var childNode = obj.find(".number"),
                                    number = parseInt(childNode.text());
                                childNode.text(++number);
                                zanFun.zanclick = true;
                                return;
                            }
                            else{
                                return;
                            }
                        }
                    })
                }

            },
            unZan : function(obj){
                var dataId = obj.attr("data-id");
                if(!zanFun.zanclick){
                    $.ajax({
                        type : "GET",
                        url : BASE_PATH+"/shop/tech/book/doUnlike",
                        data : {id:dataId},
                        cache:false,
                        success : function(data, textStatus){
                            if(data.success){
                                var childNode = obj.find(".number"),
                                    number = parseInt(childNode.text());
                                childNode.text(++number);
                                zanFun.zanclick = true;
                                return;
                            }
                            else{
                                return;
                            }
                        }
                    })
                }
            }
        }
    })

});
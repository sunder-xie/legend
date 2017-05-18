$(document).ready(function ($) {
    $("#searchForm").submit(function () {
        moreData($(this), "", false,function(){
            new LunBo('#lunbo1').init();
            new LunBo('#lunbo2').init();
            new LunBo('#lunbo3').init();
            new LunBo('#lunbo4').init();
            new LunBo('#lunbo5').init();
            new LunBo('#lunbo6').init();
            new LunBo('#lunbo7').init();
            new LunBo('#lunbo8').init();
            new LunBo('#lunbo9').init();
            new LunBo('#lunbo10').init();
            var checkTime = $(".check").text();
            $(".container").each(function(){
                $(this).find(".detailDate").each(function(){
                    var detailDate = $(this).text();
                    if(checkTime==detailDate){
                        $(this).addClass("orange");
                    }
                });
            });
        });
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

    $(document).on('click','.courseCat',function(){
        var catId = $(this).attr("child-catId");
        $("#search_catId").val(catId);
        $("#searchForm").submit();
    });

    $(document).on('click','.chooseTime',function(){
        var searchTime = $(this).attr("date-str");
        $("#search_searchTime").val(searchTime);
        $("#searchForm").submit();
    });


    $(function(){
        var tabBox = $(".tab-box");
        var catList = $(".catList");
        var minHeight = catList.css("minHeight");
        var maxHeight = catList.css("maxHeight");
        (function showDown(){

//            var catHeight = 0;
//            if(catList.css("minHeight")){
//                catHeight = catList.css("minHeight");
//            }
//            else{
//                catHeight = catList.css("maxHeight");
//            }
            $(".more-down").on("click",function(){
                if($(".more").find('.arrow-up').length){
                    for(var i = 0 ;i < tabBox.length; i++){
                        tabBoxHeight = tabBox.eq(i).find("ul").height();
                        if(tabBoxHeight > 100){
                            tabBox.eq(i).animate({"height":"81px"});
                        }
                    }
                    catList.animate({"maxHeight":maxHeight});
                    $(".more .arrow-down").removeClass("arrow-up");
                }
                else{
                    var height=0;
                    for(var i = 0 ;i < tabBox.length; i++){
                        var tabBoxHeight = tabBox.eq(i).find("ul").height();
                        height+=tabBoxHeight;
                        if(tabBoxHeight > 100){
                            tabBox.eq(i).animate({"height":(tabBoxHeight+20)});
                        }
                    }
                    catList.animate({"maxHeight":height+30*tabBox.length});
//                    catList.animate({"minHeight":minHeight});
                    $(".more .arrow-down").addClass("arrow-up");
                }
//                for(var i = 0 ;i < tabBox.length; i++){
//                    var tabBoxHeight = tabBox.eq(i).find("ul").height();
//                    if(tabBoxHeight > 100){
//                        if($(".more").find('.arrow-up').length){
//                            $(".more .arrow-down").removeClass("arrow-up");
//                            tabBox.eq(i).animate({"height":"97px"});
//                        }
//                        else{
//                            $(".more .arrow-down").addClass("arrow-up");
//                            tabBox.eq(i).animate({"height":(tabBoxHeight+20)});
//                        }
//                    }
//                }
            })
            $(".tab-box").on("click","li",function(){
                $(".tab-box li").removeClass("check");
                $(this).addClass("check");
            })
        })()




});

    // 图片轮播部分
    function LunBo(selector){
        this.selector = $(selector);
        this.currentIndex = 0;
        this.innerBoxLength = this.selector.find('.innerbox').length;
    }

    LunBo.prototype = {
        init: function(){
            this.leftClick();
            this.rightClick();
        },
        leftClick: function(){
            var that = this;
            this.selector.find(".disleftBtn").on("click",function(){
                if(that.currentIndex == 0){
                    return;
                }
                that.currentIndex--;
//                console.log(that.currentIndex);
                var left = that.currentIndex * -126;
                $(this).siblings(".container").find(".inner").animate({"left":left+"px"});
                $(this).siblings(".rightBtn").removeClass("disrightBtn");
                if(that.currentIndex == 0){
                    $(this).removeClass("leftBtn");
                }
            })

        },
        rightClick: function(){
            var that = this;
            this.selector.find(".rightBtn").on("click",function(){
                if(that.currentIndex == that.innerBoxLength-3){
                    return;
                }
                that.currentIndex++;
                var left = that.currentIndex * -126;
                $(this).siblings(".container").find(".inner").animate({"left":left+"px"});
                $(this).siblings(".disleftBtn").addClass("leftBtn");
                if(that.currentIndex == that.innerBoxLength - 3){
                    $(this).addClass("disrightBtn");
                }
            })
        }
    }


});
$(function() {
    $(".tq_store,.tq_store_shadow").hover(function(){
        $(".tq_store").css({zIndex:3});
        $(".tq_store_shadow,.tq_arrow_shadow").css({zIndex:-1}).hide();
        $(".tq_customer").css({zIndex:1});
        $(".tq_customer_shadow").css({zIndex:2}).fadeIn(250);
    });
    $(".tq_customer,.tq_customer_shadow").hover(function(){
        $(".tq_customer").css({zIndex:3});
        $(".tq_customer_shadow").css({zIndex:-1}).hide();
        $(".tq_store").css({zIndex:1});
        $(".tq_store_shadow,.tq_arrow_shadow").css({zIndex:2}).fadeIn(250);
    });

    $(window).scroll(function(){
        var body_top = $("body").scrollTop();
        if(body_top >= 700){
            $(".navigation_top").show();
        }else{
            $(".navigation_top").hide();
        }
    });



    //banner
    seajs.use('banner',function(ban){
        ban.init({
            //图片盒子
            bannerBox: $('.home-banner'),
            //图片切换按钮盒子
            btnBox: $('.banner-btn'),
            //显示方式（1、从右向左滚动显示 2、逐个显示其它隐藏）
            displayModes:2
        });
    });

});
/**
 * Created by lihaizhong on 2015/3/19.
 */

$(document).ready(function () {
    //join_advantage 合作优势  START
    //$('.join_adv_list li').on("mouseover", function () {
    //    $(this).siblings("li").removeClass("join_adv_sel");
    //    $(this).siblings("li").find(".join_adv_detail").css("display", "none");
    //    $(this).addClass("join_adv_sel");
    //    $(this).find(".join_adv_detail").css("display", "block");
    //});
    //join_advantage 合作优势  END


    var $joinAdvList_li = $('.join_adv_list li'),
        $telephone = $('.join_hotline_part .telephone');
    $joinAdvList_li.on("mouseenter",function(){
        $(this).find(".join_adv_detail").stop().animate({"top":0},"slow");
    });
    $joinAdvList_li.on("mouseleave",function(){
        $(this).find(".join_adv_detail").stop().animate({"top":"305px"},"fast");
    });
    //显示热线电话 START
    $telephone.on("mouseover", function() {
        $('.join_hotline').css("display", "block");
    });
    $telephone.on("mouseout", function() {
        $('.join_hotline').css("display", "none");
    });
    //显示热线电话 END

    //右侧页内导航 START
    $('.top_nav li').on("click", function () {
        $(this).siblings("li").removeClass("cur");
        if ($(this).hasClass("top")) {
            $('.top_nav li').eq(0).addClass("cur");
        } else {
            $(this).addClass("cur");
        }
    });
    function nav() {
        var box = $('.merchants-box').not(".hide");
        var curTop = $(window).scrollTop();
        if( curTop < box.eq(0).offset().top+800) {
            $('.top_nav li').removeClass("cur");
            $('._join_mode').addClass("cur");
            return;
        } else {
            box.each(function () {
                if (curTop >= $(this).offset().top) {
                    $('.top_nav li').removeClass("cur");
                    var _li = "._" + $(this).attr("id");
                    $(_li).addClass("cur");
                }
            });
        }
    }
    $(document).scroll(function () {
        nav();
    });
    //右侧页内导航  END

    //乘用车和商用车切换
    $(".tab .taber").click(function(){
        $(this).siblings().removeClass("active");
        $(this).addClass("active");
        if($(this).index() == 0){
            $(this).find("img").attr("src",BASE_PATH+"/resources/images/portal/car_h.png");
            $(this).siblings().find("img").attr("src",BASE_PATH+"/resources/images/portal/truck.png");
            //$("#join_mode").removeClass("hide");
            //$("#join_mode_other").addClass("hide");
            $("#content").html($("#passengerVehicleTpl").html());
        }
        else{
            $(this).find("img").attr("src",BASE_PATH+"/resources/images/portal/truck_h.png");
            $(this).siblings().find("img").attr("src",BASE_PATH+"/resources/images/portal/car.png");
            //$("#join_mode").addClass("hide");
            //$("#join_mode_other").removeClass("hide");
            $("#content").html($("#commercialVehicleTpl").html());
        }
    });
    /*
    * 初始化
    */
    (function init() {
        $("#content").html($("#passengerVehicleTpl").html());
        nav();
    })();
});

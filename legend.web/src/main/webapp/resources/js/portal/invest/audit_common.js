/**
 * Created by Sky on 2015/3/19.
 */

$(function() {
// audit_info侧边栏动画效果  START=======
//    $('.info_b').on("click", function() {   //页面导航
//        $('.info_b').removeClass("ib_current");
//        $(this).addClass("ib_current");
//    });

    //左边菜单当前页样式定位 change by ch 20150616-----
    var href = location.href;
    var pageName = href.substr(href.lastIndexOf("/")+1);
    $(".info_b[data-pageName='"+pageName+"']")
        .addClass("ib_current")
        .siblings()
        .removeClass("ib_current");
    //----------------------------------------------


// audit_info侧边栏动画效果  END=======
});
/**
 * Created by lihaizhong on 2015/2/10.
 */

$(function () {
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

    //相册效果渲染
    if ($('#galleria') && $('#galleria').length > 0) {
        Galleria.loadTheme(BASE_PATH + '/resources/js/lib/galleria/themes/classic/galleria.classic.js');
        Galleria.configure({
            transition: 'fade',
            //imageCrop: true,
            lightbox: true,
            autoplay: 3000
        });
        Galleria.run('#galleria');
    }

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

    // 这边加入温爷插件
    var query = function (url, tplId, success, renderId) {
        seajs.use([
                "ajax",
                "artTemplate",
                "table",
                "paging"
            ],
            function (ajax, template, table) {
                $('#' + renderId).html('');

                ajax.post({
                    url: url,
                    // data:formData(),
                    success: function (json) {
                        if (!json.success) {
                            alert(json.errorMsg, 5);
                            return;
                        } else {
                            success(json.data, template, tplId, renderId);
                        }
                    }
                });
            }
        );
    }


    var notice_render = function (data, template, tplId, renderId) {
        var html = template(tplId, {"templateData": data.content});
        $("#" + renderId).html("");
        $("#" + renderId).append(html);
    }

    //公告分页的js

    var getPageData = function (pageNum) {
        var param = {
            page: pageNum
        };
        seajs.use(["ajax", "artTemplate"], function (ajax) {
            ajax.post({
                url: BASE_PATH + '/shop/help/notice_list',
                data: param,
                success: function (json) {
                    if (!json.success) {
                        return;
                    } else {
                        var totalPages = json.data.totalPages;
                        $("#totalPages").attr("value", totalPages);
                        $("#totalPages").html("");
                        $("#totalPages").append(totalPages);
                        //当前的页
                        var currentPage = pageNum;
                        if( totalPages === 0 ){
                            currentPage = 0;
                        }
                        $("#currentPage").attr("value", currentPage);
                        $("#currentPage").html("");
                        $("#currentPage").append(currentPage);
                        $("input[name=pageNum]").attr("value", currentPage);
                        //渲染视图
                        var html = template("noticeData", {"templateData": json.data.content});
                        $("#notice").html("");
                        $("#notice").append(html);
                    }

                }
            });
        });
    }

    $("input[name=pageAction]").on("click", function () {
        if ($("input[name=pageNum]").attr("value") > $("#totalPages").attr("value"))
            return;
        getPageData($("input[name=pageNum]").attr("value"));
    });

    //上一页
    $("#prePage").on("click", function () {
        if ($("#currentPage").attr("value") <= 1)
            return;
        getPageData($("#currentPage").attr("value") - 1);
    });

    //下一页
    $("#nextPage").on("click", function () {
        if ($("#currentPage").attr("value") == $("#totalPages").attr("value"))
            return;
        getPageData($("#currentPage").attr("value") + 1);
    });

    //执行第一次请求 要判断是否执行
    if ($('#provSelect1').val() === 'notice') {
        getPageData(1);
    }
});

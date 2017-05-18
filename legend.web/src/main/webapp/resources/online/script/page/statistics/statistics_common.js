/*------------------------------------
 统计报表增加左右按钮
 --------------------------------------*/
$(function () {
    //ie8以下将不能使用此功能。
    if(util.isLowIE().isLowIE && util.isLowIE().version > 7){
        return;
    }
    var qxy_right = $(".qxy_right");
    var html = '<a href="javascript:;" class="go_left_or_right go_left"></a><a href="javascript:;" class="go_left_or_right go_right"></a>';
    $("script[type='text/html']", qxy_right).append(html);

    var cont_offset = qxy_right.offset(),
        cont_width = qxy_right.width(),
        btn_left = cont_offset.left - 15,
        btn_top = cont_offset.top + 80;
    $(".go_left").css({"top": btn_top, "left": btn_left});
    $(".go_right").css({"top": btn_top, "left": btn_left + cont_width - 20});


    var scrollLeft = 0,
        speed = 50;//鼠标点击时移动滚动条的步长

    //鼠标向右
    $("body").on("click", ".go_right", function () {
        scrollLeft += speed;
        var table_parent = $(".go_left").parent();
        var table_width = $("table", table_parent).width() - table_parent.width();
        if (scrollLeft > table_width) {
            scrollLeft = table_width;
        }
        //console.log("向右  ： scrollLeft---"+scrollLeft+"    "+ "table_width----"+table_width);
        $(".stats_list").scrollLeft(scrollLeft);
    });
    //鼠标向左
    $("body").on("click", ".go_left", function () {
        scrollLeft -= speed;
        var table_parent = $(".go_left").parent();
        var table_width = $("table", table_parent).width() - table_parent.width();
        if (scrollLeft < 0) {
            scrollLeft = 0;
        }
        //console.log("向左  ： scrollLeft---"+scrollLeft+"    "+ "table_width----"+table_width);
        $(".stats_list").scrollLeft(scrollLeft);
    });
    //拖动横向滚动条
    $(".stats_list").scroll(function () {
        scrollLeft = $(this).scrollLeft();
    });
    //键盘事件
    $("body").on("keyup", function (e) {
        if (e.keyCode == 37) {
            //向左
            $(".go_left").trigger("click");
        } else if (e.keyCode == 39) {
            //向右
            $(".go_right").trigger("click");
        }
    });
});
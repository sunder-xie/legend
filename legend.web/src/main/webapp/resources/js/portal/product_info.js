/**
 * Created by lixiao on 15-3-16.
 */
$(document).ready(function ($) {
    $(".storeNav .item").click(
        function () {
            $("#product_title").text($(this).text());
            $(this).siblings().removeClass("current");
            $(this).addClass("current");
            var tabId = $(this).attr("for_table");
            $("#" + tabId).show();
            $("#" + tabId).siblings().hide();

        }
    );

    var productType = $("#productType").val();
    if (productType != null && productType != '') {
        var $product_li = $("#" + productType);
        $("#product_title").text($product_li.text());
        $product_li.siblings().removeClass("current");
        $product_li.addClass("current");
        var tabId = $product_li.attr("for_table");
        $("#" + tabId).show();
        $("#" + tabId).siblings().hide();


    }
});
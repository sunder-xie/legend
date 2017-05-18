jQuery(function ($) {

    var channel = $(".tab .tab-item:first").data("channel");
    getData( channel );

    //tab切换的时候
    $(".tab .tab-item").click(function () {
        $(this).siblings(".active").removeClass("active");
        $(this).addClass("active");
        var channel = $(this).data("channel");
        getData(channel);
    });

    $(".click-hide").click(function(){
        $(".active").slideUp();
        return false;
    });
    $(".banner").lunbo({
        interval:4500
    });


});

function getData(channel){
    seajs.use(["ajax", "dialog", "artTemplate"], function (ajax, dg, art) {
        ajax.get({
            url: BASE_PATH + "/shop/activity/getServiceListByChannel",
            data: {
                channel: channel
            },
            success: function (json) {
                if (json.success) {
                    var html = art('contentTpl', {templateData: json.data});
                    $("#content").html(html);
                } else {
                    dg.info(json.errorMsg, 3);
                }
            }
        });
    });
}


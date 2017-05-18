/**
 * Created by sky on 15/8/7.
 */

$(function () {
    seajs.use([
        'ajax',
        'art'], function (ajax, art) {
        var list = 'list';

        $.ajax({url: BASE_PATH + '/shop/activity/list'})
            .done(function (json) {
                var html = art('listTpl', {json:json});

                $('#listFill').append( $(html) );
            });

        $(document).on("mouseover", ".model-list li", function (e) {
            //鼠标移入模板时展示二维码动作
            var s = e.target;
            // 排除按钮
            if (!$(s).hasClass("code-hover"))
                return;

            $(this).find(".scan-show").slideDown(500);
        }).on("mouseleave", ".model-list li", function () {
            //鼠标移出模板时隐藏二维码动作
            $(this).find(".scan-show").slideUp(500);
        });
    });
});

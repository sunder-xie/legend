$(function () {
    var paging, pageSize = 10;
    var defaultImgUrl = BASE_PATH + '/static/img/page/home/default/news.jpg';

    seajs.use(['ajax', 'dialog'], function(ajax, dialog){
        dialog.titleInit();
    });
    $('.js-type').on('click', function () {
        var newsTypeId = $(this).data('typeId');
        var current = $('.article-tab-box .current-tab');

        if(current[0] != this) {
            current.removeClass('current-tab');
            $(this).addClass('current-tab');
        }

        $.ajax({
            url: BASE_PATH + "/home/news/list",
            type: 'GET',
            data: {
                newsTypeId: newsTypeId,
                size: pageSize
            },
            success: function (data) {
                if (data.success) {
                    paging = new Paging(data.data.totalPages, send.bind(null, newsTypeId), 4, '.yqx-page');
                    renderHTML(data.data.content);
                }
            }

        });
    });

    $('.js-type').eq(0).click();

    function send(id, curr) {
        $.ajax({
            url: BASE_PATH + "/home/news/list",
            type: 'get',
            data: {
                newsTypeId: id,
                page: curr,
                size: pageSize
            },
            success: function (data) {
                if (data.success)
                    renderHTML(data.data.content);
            }
        })
    }

    function renderHTML(data) {
        seajs.use('art', function (art) {
            art.helper("$", $);
            var html = art('newsList', {data: data, console: console, defaultImgUrl: defaultImgUrl});

            $('.article-content').empty().append($(html));
        })
    }
});
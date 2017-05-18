$(function () {
    var pageSize = 10, paging;
    var defaultImgUrl = BASE_PATH + '/static/img/page/home/default/products.jpg';

    seajs.use(['ajax', 'dialog'], function(ajax, dialog){
        dialog.titleInit();
    });
    getHotProduct();
    send();

    function send(curr) {
        $.ajax({
            url: BASE_PATH + "/home/productalert/list",
            type: 'get',
            data: {
                page: curr,
                size: pageSize
            },
            success: function (data) {
                if (data.success) {
                    renderHTML(data.data.content);
                    if(!paging) {
                        paging = new Paging(data.data.totalPages, send, 4, '.yqx-page');
                    }
                }
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

    function getHotProduct() {
        $.ajax({
            url: BASE_PATH + '/home/productalert/top',
            success: function (json) {
                var html = '', e, link;
                if(json.success) {
                    for(var i in json.data) {
                        e = json.data[i];
                        if (e.targetUrl) {
                            link = e.targetUrl;
                        } else {
                            link = BASE_PATH + '/home/products/products-detail?itemid=' + e.id;
                        }

                        html += '<li>' +
                            '<a target="_blank" href="' + link + '">' + e.itemTitle + '</a></li>';
                    }

                    $(html).appendTo( $('#hotProducts') );
                }
            }
        })
    }
});
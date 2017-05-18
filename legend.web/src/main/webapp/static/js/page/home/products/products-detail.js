$(function () {
    getHotProduct();

    seajs.use('dialog', function (dialog) {
        dialog.titleInit();
    });

    function getHotProduct() {
        $.ajax({
            url: BASE_PATH + '/home/productalert/top',
            success: function (json) {
                var html = '', e;
                var link = '';

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
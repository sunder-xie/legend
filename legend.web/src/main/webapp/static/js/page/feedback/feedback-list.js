$(function () {
    
    seajs.use([
        'ajax', 
        'table',
        'art'],
    function (ajax, table, art) {
        var desc = 'list';

        art.helper('$', $);
        table.init({
            url: BASE_PATH + '/shop/help/feedback/list',
            tplid: desc + 'Tpl',
            fillid: desc + 'Fill',
            pageid: desc + 'Page',
            formid: desc + 'Form'
        });

        $('.js-search-content').on('keydown', function (e) {
            if(e.key.toLowerCase() == 'enter' || e.which == 13) {
                $(this).parent().find('.js-search-btn').click();
            }
        });

        $(document).on('click.feedback', '.js-continue-feedback', function () {

            window.feedbackId = $(this).data('id');
        })
            .on('click', '.js-pic-view', function () {
                var src = $(this).attr('data-src');
                var img = $('<img src=' + src +'>').addClass('hide').appendTo(document.body);
                img.on('load', function () {
                    util.imgZoomBigger.call(this, {maxWidth: 600, maxHeight: 400});
                });
            })

    });
});
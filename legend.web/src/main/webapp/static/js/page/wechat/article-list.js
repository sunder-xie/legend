/** created by ende 2016年06月14日11:14:07 **/
$(function() {
    var url = BASE_PATH + '/shop/wechat/op/qry-article-list';
    var target = 'article';

    seajs.use(['table', 'ajax', 'dialog'], function(table, ajax, dialog) {
        table.init({
            fillid: target + 'Fill',
            pageid: target + 'Page',
            tplid : target + 'Tpl',
            url   : url,
            formid: target + 'Form'
        })

        ;dialog.titleInit();
    });

    // 查询标签点击事件,添加一个input，
    // 然后触发 查询事件
    $('.js-search-input').on('click', function() {
        var name = $(this).data('name');
        var val  = $(this).data('value');
        if(val === undefined) {
            val = '';
        }
        $('input[name="' + name + '"]').val(val);

        $('.selected').removeClass('selected');
        $(this).addClass('selected');

        $('.js-search-btn').click();
    });
});
// created by wry 2016年06月15日
$(function () {
    var url = BASE_PATH + '/shop/wechat/op/qry-replymsg-list';
    var delUrl = BASE_PATH + '/shop/wechat/op/delete-replymsg';
    var target = 'msg';

    seajs.use(['table', 'ajax', 'select', 'dialog', 'formData', 'art'],
        function (table, ajax, select, dialog, formData, art) {
        select.init({
            dom: '.js-reply-status',
            showKey: 'key',
            showValue: 'name',
            pleaseSelect: true,
            data: [{
                key: 0,
                name: '用户消息回复'
            }, {
                key: 1,
                name: '关键字回复'
            }, {
                key: 2,
                name: '被添加回复'
            }]
        });

        table.init({
            fillid: target + 'Fill',
            pageid: target + 'Page',
            tplid: target + 'Tpl',
            url: url,
            formid: target + 'Form'
        });

        $(document).on('click', '.js-detail', function () {
            var content;
            // tr 的数据
            var data = formData.get( $(this).parent().parent() );

            content = art('detail', {
                data: data
            });
            dialog.open({
                area: ['500px'],
                content: content
            });
        })
            .on('click', '.js-del', function () {
                var id = $(this).data('id');
                var self = this;

                if(id) {
                    dialog.confirm('确定删除这条自动回复么？', function () {
                        $.ajax({
                            url: delUrl,
                            type: 'POST',
                            data: {
                                msgId: id
                            },
                            success: function (json) {
                                if (json && json.success) {
                                    $(self).parent().parent().remove();
                                    dialog.success('删除成功', function () {
                                        resetNo($(self).parents('table').find('tr'));
                                    });

                                } else {
                                    dialog(json.errorMsg || '删除失败');
                                }
                            }
                        });
                    });
                }
            });
    });

    $(document).on('click', '.detail-img img', util.imgZoomBigger);

    function resetNo($obj) {
        var count = 1;

        $obj.each(function () {
            $(this).find('.no').text(count);
            count += 1;
        });
    }
});
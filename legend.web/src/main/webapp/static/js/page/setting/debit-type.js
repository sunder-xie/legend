/*
 * create by zmx 2016/12/05
 * 收款类型设置页面
 */

$(function () {
    var doc = $(document);
    seajs.use([
        'dialog',
        'check'
    ], function (dialog, ck) {
        ck.init();
        dialog.titleInit();
        //新增付款类型
        doc.on('click', '.js-pay-btn', function () {
            $('.add-pay-type').show();
            $('.js-pay-btn').hide();
        });
        seajs.use([
            'dialog',
            'art'
        ], function (dg, at) {
            //页面数据填充
            $.ajax({
                url: BASE_PATH + '/shop/setting/debit-type/list',
                success: function (result) {
                    if (result.success) {
                        var html = at('listTpl', {json: result});
                        $('#listCon').html(html);
                    }
                }
            });

            //更改开启状态
            doc.on('click', '.js-on-btn', function () {
                var showStatus;
                var id = $(this).parents('.on-off').data("id");
                if( $(this).hasClass('off-btn')){
                    showStatus = 1;
                    $(this).removeClass('off-btn').parents('.on-off').css('background', '#607b0a');
                } else {
                    showStatus = 0;
                    $(this).addClass('off-btn').parents('.on-off').css('background', '#fd461e');
                }
                $.ajax({
                    url: BASE_PATH + '/shop/setting/debit-type/change-status',
                    data: {
                        id:id,
                        showStatus:showStatus
                    },
                    success: function (result) {
                        if (!result.success) {
                            dg.fail(result.errorMsg);
                            return;
                        }
                    }
                });
            });

            //保存新增付款类型
            doc.on('click', '.js-pay-save', function () {
                var name = $.trim($(".pay-type-name").val());
                if (!ck.check()) {
                    return;
                }
                $.ajax({
                    post: 'post',
                    url: BASE_PATH + '/shop/setting/debit-type/save',
                    data: {
                        name: name
                    },
                    success: function (result) {
                        if (result.success) {
                            var html = $('#payTypeTpl').html();
                            $('.pay-type-box').append(html);
                            $('.pay-type-box').find('.add-pay-type-name').next('.on-off').data('id',result.data);
                            $('.pay-type-box').find('.add-pay-type-name').next('.on-off').data('showStatus',true);
                            $('.pay-type-box').find('.add-pay-type-name').text(name).removeClass('add-pay-type-name');
                            $('.pay-type-name').val('');
                            $('.js-pay-btn').show();
                            $('.add-pay-type').hide();

                        } else {
                            dg.fail(result.errorMsg)
                        }
                    }
                });
            });
        });

        //取消保存新增收款类型
        doc.on('click', '.js-pay-cancel', function () {
            $('.add-pay-type').hide().find('.pay-type-name').val('');
            $('.js-pay-btn').show();
        });
    });
});
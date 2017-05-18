/**
 * Created by ende on 16/6/17.
 */
$(function () {
    var url = BASE_PATH + '/shop/wechat/op/save-wifi';
    var saveClick = true;

    seajs.use(['check', 'ajax', 'dialog', 'formData'], function (check, ajax, dialog, formData) {
        check.init();
        $('.js-save').on('click', function () {
            var data;

            if(!check.check() || !saveClick) {
                return;
            }
            saveClick = false;
            data = formData.get('.order-body');

            $.ajax({
                url: url,
                data: JSON.stringify(data),
                type: 'post',
                contentType: 'application/json',
                dataType: 'json',
                success: function(json) {
                    if(json && json.success) {
                        dialog.success('保存成功', function() {
                            location.reload(true);
                        });
                    } else {
                        dialog.fail(json.errorMsg || '保存失败');
                    }
                    saveClick = true;
                },
                error: function() {
                    saveClick = true;
                    dialog.fail('保存失败');
                }
            });
        });

        $('.js-back').on('click', function () {
            history.back(-1);
        });
    });
});
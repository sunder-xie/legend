/**
 * Created by zmx on 16/12/7.
 * 提醒设置
 */

$(function () {
    seajs.use([
        'formData',
        'check',
        'dialog'
    ], function (fd, ck, dg) {

        ck.init();

        $(document).on('click', '.js-save', function () {
            var data = fd.get('#formData');
            if (ck.check("#formData")) {
                $.ajax({
                    url: BASE_PATH + '/shop/conf/notice/update',
                    data: data,
                    type: 'POST',
                    success: function (result) {
                        if (result.success) {
                            dg.success('保存成功', function () {
                                window.location.href = BASE_PATH + "/shop/conf/notice";
                            })
                        } else {
                            dg.fail(result["errorMsg"]);
                        }
                    }
                })
            }
        })
    })
});
/**
 * ch 2016-04-18
 * 系统公告弹框
 */
$(function () {
    var t = new Date().getDate();
    var last = new Date(localStorage.publicNoticeLastShowDate)
        .getDate();

    // 一天只显示一次
    if (t === last) {
        return;
    } else {
        localStorage.publicNoticeLastShowDate = new Date();
    }
    $.ajax({
        url: BASE_PATH + "/shop/help/latest_notice",
        cache: false
    }).done(function (result) {
        if (result.success && result.data) {
            var html;

            localStorage.publiceNoticeShowed = false;
            try {
                html = template("publicNoticeDialogContentTpl", result);

                openDialog(html);
            } catch (e) {
                seajs.use('art', function (art) {
                    html = art("publicNoticeDialogContentTpl", result);

                    openDialog(html);
                });
            }

        } else {
            localStorage.publiceNoticeShowed = true;
        }
    });

    var openDialog = function (html) {
        var pageii, layerFn;
        var obj = {
            type: 1,
            title: false,
            area: ['685px', '580px'],
            border: [0],
            closeBtn: [0, false],
            shift: 'top',
            bgcolor: '',
            page: {
                html: html
            },
            content: html
        };
        if (!$.layer) {
            // 新版本的，另要用css隐藏 layer的右上角的按钮
            seajs.use('layer', function (layer) {
                pageii = layer.open(obj);
            });
        } else {
            // 旧版本的
            pageii = $.layer(obj);
        }
        //关闭
        $(document).on('click', '.notice-close-btn, .public_notice_dialog_btns a', function () {
            localStorage.publiceNoticeShowed = true;
            layer.close(pageii);
        });
    }

    // openDialog();
});

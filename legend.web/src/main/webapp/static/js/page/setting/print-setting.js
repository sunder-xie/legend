$(function () {
    var baseUrl = BASE_PATH + '/shop/print-config';

    $.ajax({
        url: BASE_PATH + '/shop/conf/get-print-version',
        success: function (json) {
            if(json.success) {
                var btn = $('.js-change-version');

                if(json.data == 'new') {
                    btn.text('切换为老版本');
                } else {
                    btn.text('切换为新版本').addClass('yqx-btn-2');
                }
                btn.removeClass('hide');
            }
        }
    });
    seajs.use([
        'dialog'
    ], function (dialog) {
        $('.js-print-guide').hover(function () {
            dialog.tips(this, '<ul>\
            <li>1.点击开始，选择打印机和传真</li>\
            <li>2.在空白处点击鼠标右键选择服务器属性</li>\
            <li>3.在创建新格式前勾选后填入新的表格名，如出库单等</li>\
            <li>4.在格式描述里纸张大小填写您所使用的打印纸的<p>　每份的尺寸，单位是厘米。</p></li>\
            <li>5.设置好后点击确定</li>\
            <li>6.右键点击打印机选择属性</li>\
            <li>7.在常规项中的下方点击打印首选项</li>\
            <li>8.然后点击高级选项，在纸张/输出的纸张规格中<p>　选择刚添加的纸型名称，点击确定，再次确定</p></li>\
            <li>9.点击设备设置，在滚动进纸器中选择刚添加的纸型名称，点击确定。</li>\
            ', {
                time: 0
            })
        }, function () {
            dialog.close();
        });

        $('.js-guide').click(function () {
            window.Components.receiptGuideDialog(true);
        });

        $('.js-edit').click(function () {
            var box = $(this).parents('.choose-box');
            var status = box.find('.status-box');
            var data = box.data();

            $.ajax({
                url: baseUrl + '/chang-open-status',
                data: data,
                success: function (json) {
                    if (json.success) {
                        if (status.hasClass('opened')) {
                            status.removeClass('opened')
                                .find('i').text('未开启');
                        } else {
                            status.addClass('opened')
                                .find('i').text('已开启');
                        }
                    } else {
                        dialog.fail(json.errorMsg);
                    }
                }
            })
        }); // .js-edit end

        $('.js-change-version').on('click', function () {
            var that = $(this);
            $.ajax({
                url: BASE_PATH + '/shop/conf/change-print-version',
                success: function (json) {
                    seajs.use('dialog', function (dialog) {
                        if (!json.success) {
                            dialog.fail(json.errorMsg || '切换失败');
                        } else {
                            dialog.success('切换成功');
                            if(json.data == 'new') {
                                $(that).text('切换为老版本').removeClass('yqx-btn-2');
                            } else if(json.data == 'old') {
                                $(that).text('切换为新版本').addClass('yqx-btn-2');
                            }
                        }
                    });
                }
            });
        });
    }); //seajs end

    $('.js-back').on('click', function () {
        if(document.referrer) {
            window.location.href = document.referrer;
        } else {
            util.goBack();
        }
    })
});
$(function () {
    seajs.use([
        'art',
        'dialog'
    ], function (art, dialog) {
        var _dialogId,
            _staff = null;

        dialog.titleInit();
        getStaff();

        $(document).on('click', '.js-del-staff', function () {
            $(this).parents('.staff-btn').remove();
        });

        $('.choose-staff-dialog')
            .on('click', '.js-cancel', function () {
                dialog.close(_dialogId);
            })
            .on('change', '.js-staff', function () {
                var id = $(this).data('id');
                var box = $('.aside-main .staff-list');

                if(this.checked) {
                    box.find('.js-add-staff').before(
                        '<i class="staff-btn btn" data-user-id="' + $(this).data('id') +
                        '">' + $(this).data('name')
                        + '<i title="删除" class="staff-del-btn js-del-staff"></i></i>'
                    );
                } else {
                    box.find('.staff-btn[data-user-id="' + id + '"]').remove();
                }
            })
            .on('input', '.js-staff-name', function () {
                var name = $.trim( $(this).val() );

                $(this).parents('.yqx-dialog').find('.staff-list li')
                    .each(function () {
                        if($.trim($(this).text()).indexOf(name) === -1) {
                            $(this).addClass('hide');
                        } else {
                            $(this).removeClass('hide');
                        }
                    });
            })
            .on('click', '.js-confirm', function () {
                dialog.close(_dialogId);
            });

        // 一键分配
        $('.js-one').click(function () {
            var choseUserIds = [];
            var num = $.trim( $('#needAllotNum').text() );

            $('.staff-list .staff-btn').each(function () {
                choseUserIds.push($(this).data('userId'));
            });
            if (choseUserIds.length == 0) {
                dialog.warn('请至少选择一位员工');
                return;
            }

            if(num == 0) {
                dialog.warn('当前无可分配的客户');
                return;
            }
            var data= {
                choseUserIds:choseUserIds
            };
            dialog.confirm('确认一键分配<strong>' + num
                + '位客户 </strong>给' + choseUserIds.length + '位员工吗？',
            function () {
                $.ajax({
                    type: 'post',
                    url: BASE_PATH + '/marketing/gather/allot/normal-allot-list/allot',
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    success: function (json) {
                        if (json.success && json.data) {
                            dialog.success('一键分配成功');
                            setTable(json.data);
                            getAllotStatistic();
                        } else {
                            dialog.fail(json.message || '分配失败');
                        }
                    }
                });
            });
        });

        $('.js-add-staff').click(function () {
            var d = $('.choose-staff-dialog').removeClass('hide');
            var html = '';

            if (_staff) {
                for (var i in _staff) {
                    html += '<li><label class="js-show-tips"><input type="checkbox"' +
                            ' data-name="' + _staff[i].userName +
                        '" class="js-staff" data-id="' + _staff[i].userId
                        + '">' + _staff[i].userName + '</label>';
                }

                d.find('.staff-list').append(html);
                _staff = null;
            }

            $('.staff-list .staff-btn').each(function () {
                var id = $(this).data('userId');
                d.find('input[data-id="' + id + '"]')
                    .prop('checked', true);
            });

            _dialogId = dialog.open({
                content: d,
                end: function () {
                    $('.choose-staff-dialog').find('.staff-list li')
                        .removeClass('hide')
                        .end().find('input').prop('checked', false)
                        .end().find('.js-staff-name').val('')
                }
            });
        });

        function getStaff() {
            $.get(BASE_PATH + '/marketing/gather/api/get-manager', function (json) {
                if (json.success && json.data) {
                    _staff = json.data
                }
            });
        }

        function setTable(data) {
            var html = art('tableTpl', {
                data: data
            });
            $('#fill').empty();
            $('#fill').append(html);
        }
        function getAllotStatistic() {
            $.get(BASE_PATH + '/marketing/gather/allot/normal-allot-list/get-statistic', function (json) {
                if (json && json.success) {
                   var freeSummary = json.data;
                    $(".js-active").text(freeSummary.active);
                    $(".js-lost").text(freeSummary.lost);
                    $(".js-lazy").text(freeSummary.lazy);
                    $(".js-sum").text(freeSummary.sum);
                } else {
                    dialog.fail(json.message);
                }
            })
        }
    });
});
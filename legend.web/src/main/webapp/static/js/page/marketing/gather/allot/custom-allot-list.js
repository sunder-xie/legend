/**
 * Created by ende on 16/12/14.
 */
$(function () {
    var _staff;
    var _filter = {
            // 行为属性
            '消费金额': ['未到店天数', '车牌', '车型', '车主电话', '系统属性', '客户单位'],
            '单车产值': ['未到店天数', '车牌', '车型', '车主电话', '系统属性', '客户单位'],
            '消费次数': ['未到店天数', '车牌', '车型', '车主电话', '系统属性', '客户单位'],
            '未到店天数': ['行为属性', '车牌', '车型', '车主电话', '系统属性', '客户单位'],

            // 客户属性
            '车辆级别': ['车牌', '车型', '车主电话', '客户标签', '客户单位'],
            '行驶里程': ['车牌', '车型', '车主电话', '客户标签', '客户单位'],
            '会员卡类型': ['车牌', '车型', '车主电话', '客户标签', '客户单位'],
            '车牌': 'all',
            '车型': 'all',
            '车主电话': ['车牌', '车型', '客户标签', '客户单位'],
            '客户单位': 'all',

            // 系统属性
            '新老客户': ['行为属性', '车牌', '车型', '车主电话', '客户标签', '活跃度', '客户单位'],
            '客户标签': 'all',
            '活跃度': ['行为属性', '车牌', '车型', '车主电话', '客户标签', '新老客户', '客户单位']
        },

        _cate = {
            '系统属性': ['新老客户', '客户标签', '活跃度'],
            '客户属性': ['车辆级别', '行驶里程', '会员卡类型', '车牌',
                '车型', '车主电话', '客户单位'],
            '行为属性': ['消费金额', '单车产值', '消费次数', '未到店天数'],
            all: ['系统属性', '客户属性', '行为属性']
        };

    getStaff();

    $('.js-filter').on('click', function () {
        if (!$(this).prop('disabled')) {
            $(this).toggleClass('active');
            setFilter.call(this);

            var target = $(this).data('target').split('&');

            if ($(this).hasClass('active')) {
                for (var i in target) {
                    $(target[i]).removeClass('hide')
                        .find('select').removeAttr('no_submit');
                }
            } else {
                for (var i in target) {
                    $(target[i]).addClass('hide')
                        .find('input').val('')
                        .end()
                        .find('select').attr('no_submit', true);
                }

            }

            var len = $('.js-filter.active').length;

            if (len == 0) {
                $('.tjsj').removeClass('hide');
            } else if (len == 1) {
                if (!target[1] || target[1] !== '.tjsj') {
                    $('.tjsj').addClass('hide');
                }
            }
        }
    });

    $('.js-reset').click(function () {
        $(this).parents('.filter-prop')
            .find('.js-filter.active').click();
    });

    $('.js-reset-condition').click(function () {

        $(this).parents('.filter-condition')
            .find('input').val('')
            .end().find('select').each(function () {
                $(this).find('option').eq(0).prop('selected', true);
            });
        $(".js-search-allot").val("true");
    });

    $('.js-self-define').on('change', function () {
        var val = this.value;
        var target = $(this).parent('li').find('.self-define');

        if (val == '' && $(this).find('option:selected').text() === '自定义') {
            target.removeClass('hide')
        } else {
            target.addClass('hide');
        }
    });

    $('.js-xfje').on('change', setSelectValue);
    $('.js-dccz').on('change', setSelectValue);
    $('.js-xslc').on('change', setSelectValue);


    seajs.use(['table', 'art', 'date', 'dialog', 'select'], function (table, art, date, dialog, st) {
        var _dialogId;

        dialog.titleInit();

        date.dpStartEnd({
            start: 'startTime',
            end: 'endTime'
        });

        //初始化客户标签
        st.init({
            url: BASE_PATH + '/shop/customer/tag/value_list',
            canInput: true,
            dom: 'input[name="search_customerTag"]'
        });

        art.helper('isContained', function (data) {
            var _filter = getFilterTarget();

            for (var i = 0; i < data.length; i++) {
                if (_filter.indexOf(data[i]) !== -1) {
                    return true;
                }
            }
        });

        var _list = table.init({
            url: BASE_PATH + '/marketing/ng/center/accurate/list',
            fillid: 'fill',
            pageid: 'page',
            tplid: 'tpl',
            formid: 'form',
            callback: function (a, json) {
                $('.need-allot-num').text(json.data ? json.data.totalElements || 0 : 0);
            }
        });

        $(document)
            .on('click', '.js-del-staff', function () {
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
                var name = $.trim($(this).val());

                $(this).parents('.yqx-dialog').find('.staff-list li')
                    .each(function () {
                        if ($.trim($(this).text()).indexOf(name) === -1) {
                            $(this).addClass('hide');
                        } else {
                            $(this).removeClass('hide');
                        }
                    });
            })
            .on('click', '.js-confirm', function () {
                dialog.close(_dialogId);
            })
            .on('click', '.yqx-dialog .js-cancel', function () {
                dialog.close(_dialogId);
            });

        $('.allot-result-dialog .js-confirm').click(function () {
            dialog.close();
            _list(1);
            getAllotStatistic();
        });

        $('.js-pre-page li').click(function () {
            var val = $(this).text();

            $(this).parents('.pre-page')
                .find('.page-input').val(val)
                .focus();

            $('.filter-condition input[name="size"]').val($(this).data('value'));

            _list(1);
        });

        // 平均分配
        $('.js-allot').click(function () {
            var data = {
                choseUserIds: []
            };

            $('.staff-list .staff-btn').each(function () {
                data.choseUserIds.push($(this).data('userId'));
            });

            seajs.use(['formData', 'dialog'], function (formData, dialog) {
                var t = {};
                var num = $.trim( $('.need-allot-num').text() );
                if (data.choseUserIds.length == 0) {
                    dialog.warn('请至少选择一位员工');
                    return;
                }

                if(num == 0) {
                    dialog.warn('当前无可分配客户');
                    return;
                }
                dialog.confirm('确定平均分配这<strong>'
                    + num
                    + '位客户</strong>，给这' + data.choseUserIds.length
                    + '位员工吗？', function () {

                    $.extend(data, formData.get('#form'));

                    for (var key in data) {
                        if (key.indexOf('search_') > -1) {
                            t[key.replace('search_', '')] = data[key];
                        } else {
                            t[key] = data[key];
                        }
                    }

                    data = t;
                    $.ajax({
                        url: BASE_PATH + '/marketing/gather/allot/custom-allot-list/custom-allot',
                        type: 'post',
                        data: JSON.stringify(data),
                        contentType: 'application/json',
                        success: function (json) {
                            if (json.success) {
                                var box = $('.allot-result-dialog');
                                var count = 0;
                                var html = '';

                                for (var i in json.data) {
                                    html += '<tr>'
                                        + '<td>' + json.data[i].userName + '</td>'
                                        + '<td>' + json.data[i].totalAllot + '</td>'
                                        + '</tr>';

                                    count += json.data[i].totalAllot;
                                }

                                box.find('.num').text(count);

                                box.find('.yqx-table tbody').empty().append(html);

                                dialog.open({
                                    content: box.removeClass('hide')
                                });
                            } else {
                                dialog.fail(json.message || '平均分配失败');
                            }
                        }
                    }); // ajax end
                }); // confirm end
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
                        + '">' + _staff[i].userName + '</label></li>';
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
                    d.find('input').prop('checked', false)
                        .end().find('.js-staff-name').val('')
                        .end().find('li').removeClass('hide');
                }
            });
        });
    });

    function setFilter() {
        var that = this;
        var data = getDisabledData();

        clearFilter();

        $('.js-filter').each(function () {
            var key = this.value;

            if (data[key] && this !== that && !$(this).hasClass('active')) {
                $(this).prop('disabled', true);
            }
        });
    }

    function clearFilter() {
        $('.js-filter').prop('disabled', false);
    }

    function getDisabledData() {
        var ret = {},
            t, key = [],
            a,
            temp;

        $('.js-filter.active').each(function () {
            key.push(this.value);
        });

        for (var i in key) {
            t = _filter[key[i]];

            if (t == 'all') {
                t = _cate.all;
            }
            for (var j in t) {

                if ((temp = _cate[t[j]])) {
                    for (a in temp) {
                        ret[temp[a]] = true;
                    }
                } else {
                    ret[t[j]] = true;
                }
            }
        }

        return ret;
    }

    function getFilterTarget() {
        var ret = [];

        $('.js-filter.active').each(function () {
            var key = $(this).data('target').replace('.', '');

            ret.push(key);
        });

        return ret;
    }

    function setSelectValue() {
        var value = $(this).val().split('-'), min, max;
        var input = $(this).parent().find('.yqx-input[name]');

        min = value[0] != undefined ? value[0] : '';
        max = value[1] != undefined ? value[1] : '';
        input.eq(0).val(min);
        input.eq(1).val(max);
    }

    function getStaff() {
        $.get(BASE_PATH + '/marketing/gather/api/get-manager', function (json) {
            if (json.success && json.data) {
                _staff = json.data
            }
        });
    }

    function getAllotStatistic() {
        $.get(BASE_PATH + '/marketing/gather/allot/normal-allot-list/get-statistic', function (json) {
            if (json && json.success) {
                var freeSummary = json.data;
                $(".total-allot-num").text(freeSummary.sum);
            } else {
                dialog.fail(json.message);
            }
        })
    }
});
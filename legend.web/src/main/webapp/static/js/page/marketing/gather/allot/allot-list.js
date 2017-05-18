/**
 * Create by zmx 2016/12/15
 * 分配客户列表页
 **/
$(function () {
    var _link = {
        base: BASE_PATH + '/marketing/gather/allot/customer-allot-list'
    };
    // 第一次查询列表
    var _first = true;
    seajs.use([
        'dialog',
        'art'
    ], function (dialog, art) {
        var _rebindData, _dialogId;
        var _chart;
        var _name;

        getStaff();
        getAllotStatistic();

        dialog.titleInit();

        $(document)
            .on('click', '.js-t-off-bind', function () {
                var data = {
                    userId: $(this).data('userId')
                };

                dialog.confirm('确定将<strong>' + $(this).parents('tr').find('.name').text() +
                    '</strong>的归属客户<strong>全部</strong>解绑吗？', function () {
                    offBind(data, function () {
                        location.reload();
                    });
                });
            })
            .on('click', '.js-t-re-bind', function () {
                var d = $('.choose-staff-dialog').removeClass('hide');
                _name = $(this).parents('tr').find('.name').text();
                _rebindData = {
                    oldUserId: $(this).data('userId')
                };

                _dialogId = dialog.open({
                    end: function () {
                        d.find('input').prop('checked', false)
                            .end().find('.js-staff-name').val('')
                            .end().find('li').removeClass('hide');
                    },
                    content: d
                });
            });

        $('.allot-dialog').on('click', '.js-close', function () {
            dialog.close();
        });

        $('.choose-staff-dialog')
            .on('click', '.js-cancel', function () {
                dialog.close(_dialogId);
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
                var t = $(this).parents('.yqx-dialog')
                    .find('.js-staff:checked');

                _rebindData.newUserId = t.data('id');

                if (_rebindData.newUserId == null) {
                    dialog.warn('请选择一位员工');
                    return;
                }
                dialog.confirm('确定将<strong>' + _name +
                    '</strong>的归属客户全部调整给<strong>' + t.parent().text()
                    + '</strong>吗？', function () {
                    _name = '';
                    reBind(_rebindData, _dialogId, function () {
                        location.reload();
                    });
                });
            })
            .on('change', '.js-staff', function () {
                var box = $(this).parents('.yqx-dialog');
                var that = this;

                if (this.checked) {
                    box.find('.js-staff').each(function () {
                        if (this !== that) {
                            this.checked = false;
                        }
                    });
                }
            });

        function offBind(data, fn) {
            $.ajax({
                url: BASE_PATH + '/marketing/gather/allot/allot-list/unbinding-all',
                type: 'post',
                data: data,
                success: function (json) {
                    if (json.success) {
                        dialog.success(json.message || '解绑成功', fn);
                    } else {
                        dialog.fail(json.message || '保存失败')
                    }
                }
            })
        }

        function reBind(data, dialogId, fn) {
            $.ajax({
                url: BASE_PATH + '/marketing/gather/allot/allot-list/change-binding-all',
                type: 'post',
                data: {
                    oldUserId : data.oldUserId,
                    newUserId : data.newUserId
                },
                success: function (json) {
                    if (json.success) {
                        dialog.success(json.message || '客户调整成功', fn);

                        dialog.close(dialogId);
                    } else {
                        dialog.fail(json.message || '保存失败')
                    }
                }
            });
        }

        function getAllotStatistic() {
            $.get(BASE_PATH + '/marketing/gather/allot/allot-list/get-statistic', function (json) {

                if (json && json.success) {
                    if(json.data && json.data.length) {
                        setTable(json.data);

                        _chart = setBar(convertEChartsData(json.data));

                        _chart.on('click', function (params) {
                            var name = params.name;

                            if (_link[name]) {
                                location.href = _link.base + '?userId=' + _link[name];
                            }
                        });
                    } else if(_first) {
                        dialog.open({
                            content: $('.allot-dialog')
                        });

                        _first = false;
                    }
                } else {
                    dialog.fail(json.message);
                }
            })
        }

        function setTable(data) {
            var html = art('tableTpl', {
                data: data
            });

            $('#fill').append(html);
        }
    });

    function setBar(data) {
        var echart = echarts.init($('#bar')[0]);
        var option = {
            grid: {
                bottom: 35
            },
            yAxis: {
                // 标签
                axisLabel: false,
                // grid 的分隔线
                splitLine: {
                    lineStyle: {
                        color: '#e9e9e9',
                        type: 'dashed'
                    }
                },
                splitNumber: 3,
                // 刻度
                axisTick: false,
                axisLine: false
            },

            xAxis: {
                // grid 的分隔线
                splitLine: false,
                // 刻度
                axisTick: false,
                type: 'category',
                axisLine: {
                    lineStyle: {
                        color: '#e9e9e9'
                    }
                },
                axisLabel: {
                    interval: 0,
                    textStyle: {
                        color: '#000'
                    }
                },
                data: data.name
            },
            series: [{
                type: 'bar',
                barWidth: 13,
                barMinHeight: 5,
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        textStyle: {
                            color: '#000'
                        }
                    }
                },
                data: data.value
            }],
            color: ['#607b0a', '#607b0a', '#607b0a', '#607b0a',
                '#232e49', '#232e49', '#232e49', '#232e49',
                '#f29530', '#f29530', '#f29530']
        };

        if(data.value.length > 12) {
            option.dataZoom = [
                {
                    type: 'slider',
                    show: true,
                    xAxisIndex: [0],
                    start: 1,
                    end: 60
                }
            ];

            option.grid.bottom = 60;
        }

        echart.setOption(option);

        return echart;
    }

    function convertEChartsData(data) {
        var ret = {
            name: [],
            value: []
        };
        var name;
        for (var i in data) {
            if (data[i].customerNumber > 0) {
                name = data[i].userName;

                if(name.length > 4) {
                    name = name.slice(0, 4) + '..';
                }
                ret.name.push(name);
                ret.value.push(data[i].customerNumber);

                _link[name] = data[i].userId;
            }
        }

        return ret;
    }

    function getStaff() {
        $.get(BASE_PATH + '/marketing/gather/api/get-manager', function (json) {
            if (json.success && json.data) {
                var html = '', t = json.data;
                var d = $('.choose-staff-dialog');

                for (var i in t) {
                    html += '<li><label class="js-show-tips"><input type="checkbox" class="js-staff" data-id="' + t[i].userId
                        + '">' + t[i].userName + '</label></li>';
                }

                d.find('.staff-list').append(html);
            }
        });
    }
});
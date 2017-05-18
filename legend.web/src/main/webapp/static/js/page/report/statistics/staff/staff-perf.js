/**
 *
 * Created by ende on 16/12/23.
 */
// 员工绩效
$(function () {
    if(util.getPara('hasBack') == 'true') {
        addBackBtn();
    }

    seajs.use(['dialog', 'art', 'table', 'date'],
        function (dialog, art, table, date) {
            var d = new Date();
            var month = d.getFullYear() + '-' + (d.getMonth() + 1);
            var _detailTimer, _last,
                isYBD = $('#isYBD').val() == 'true';

            hasDefaultSetting(month);

            date.datePicker('.js-date', {
                dateFmt: 'yyyy-MM',
                maxDate: 'yyyy-MM'
            });

            table.init({
                url: BASE_PATH + '/shop/report/gather/staff/perf/collect_user',
                formid: 'form',
                fillid: 'fill',
                tplid: 'tpl'
            });

            $('.js-rule-detail').hover(function () {
                var that = this, url;
                var box = $('.rule-detail-tips');
                var month = $('.js-date').val();

                box.remove();
                if (_last == month) {
                    box.eq(0).appendTo($(this)).show();

                    if (_detailTimer) {
                        clearTimeout(_detailTimer);
                        _detailTimer = null;
                    }
                    return;
                }

                if (isYBD) {
                    url = BASE_PATH + '/shop/report/gather/staff/perf/get_all_config'
                }

                _last = month;
                $.get(url || BASE_PATH + '/shop/report/staff/perf/get_all_config',
                    {
                        month: month
                    },
                    function (result) {
                        if (result.success) {
                            var html = art('ruleDetailTpl', {
                                data: convertConfig(result.data, month)
                            });

                            $(that).append(html);

                            var box = $(that).find('.rule-detail-tips');
                            var width = box[0].offsetWidth,
                                t;

                            if (width > 418) {
                                width = 418;
                            }

                            box.css({
                                'word-break': 'break-all',
                                width: width
                            });
                            t = -20 + width + box.offset().left - document.body.clientWidth;

                            if (t > 0) {
                                box.css({
                                    right: t
                                });

                                box.find('.angle').css('right', -t);
                            }
                        } else {
                            dialog.fail(result.message || '获取当月绩效规则失败');
                        }
                    });
            }, function () {
                _detailTimer = setTimeout(function () {
                    $('.rule-detail-tips').hide();
                }, 300);
            });


            function hasDefaultSetting(month) {
                var url = BASE_PATH + '/shop/report/staff/perf/check_config';

                if (isYBD) {
                    url = BASE_PATH + '/shop/report/gather/staff/perf/check_config'
                } else {
                    month = null;
                }

                $.ajax({
                    url: url,
                    data: {
                        month: month
                    },
                    success: function (json) {
                        if (!json.data) {
                            dialog.warn('当前无绩效设置，请联系管理员');
                        }
                    }
                });
            }
        });

    function convertConfig(data, month) {
        var ret = {
            month: month
        };
        var t;

        for (var i in data) {
            t = data[i];
            if (t.configType == 0) {
                if (t.percentageType == 0) {
                    ret.servicePer = t.percentageRate;
                    ret.serviceMethod = t.percentageMethod;
                } else {
                    if (!ret.serviceRules) {
                        ret.serviceRules = [];
                    }
                    ret.serviceRules.push({
                        name: t.relName,
                        value: t.percentageRate >= 0 && t.percentageRate !== null && !t.percentageAmount
                            ? t.percentageRate + '%'
                            : t.percentageAmount + '元/单'
                    });
                }
            } else if (t.configType == 1) {
                if (t.percentageType == 0) {
                    ret.goodsPer = t.percentageRate;
                    ret.goodsMethod = t.percentageMethod;
                } else {
                    if (!ret.goodsRules) {
                        ret.goodsRules = [];
                    }
                    ret.goodsRules.push({
                        name: t.relName,
                        value: t.percentageRate && t.percentageRate !== null && !t.percentageAmount
                            ? t.percentageRate + '%'
                            : t.percentageAmount + '元/' + t.measureUnit
                    });
                }
            } else if (t.configType == 2) {
                ret.servicerPer = t.percentageRate;
                ret.servicerMethod = t.percentageMethod;
            } else if (t.configType == 3) {
                ret.gather = t;
            }
        }

        return ret;
    }

    function addBackBtn() {
        var html = '<button class="yqx-btn-1 yqx-btn js-go-back fr" style="margin-top: -7px;">返回</button>';

        $('.headline').css('margin-bottom', 20).append(html);

        $('.js-go-back').click(util.goBack);
    }
});
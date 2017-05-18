$(function () {
    seajs.use(['art', 'dialog', 'ajax'], function (art, dialog, ajax) {
        var isYBD = $('#isYBD').val() == 'true' ? true : false;
        var date = new Date();
        var month = date.getFullYear() + '-' + (date.getMonth() + 1);

        art.helper('log', console.log)

        dialog.load();
        checkConfig(month);

        function getGatherConfig(month) {
            $.ajax({
                url: BASE_PATH + '/shop/report/gather/staff/perf/get_all_config',
                data: {
                    month: month
                },
                success: function (result) {
                    dialog.close();
                    if (result.success) {
                        var html = art('ruleDetailTpl', {
                            data: convertConfig(result.data, month)
                        });

                        $('.rule-box').append(html);
                    } else {
                        dialog.fail(result.message);
                    }
                }
            });
        }

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

        function checkConfig(month) {
            var url;
            if (isYBD) {
                url = BASE_PATH + '/shop/report/gather/staff/perf/check_config';

                $.ajax({
                    url: url,
                    data: {
                        month: month
                    },
                    success: function (json) {
                        if (json.data) {
                            $('.rule-box').removeClass('hide');
                            getGatherConfig(month);
                        } else {
                            $('.empty-box').removeClass('hide');
                            dialog.close();
                        }
                    }
                });
            }
        }
    });
});
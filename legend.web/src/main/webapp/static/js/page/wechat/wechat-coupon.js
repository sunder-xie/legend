$(function () {
    //echarts
    var myChart = echarts.init(document.getElementById('echart'));
    var originObj = {};
    var isBegin = false;

    seajs.use(['select', 'date', 'art', 'dialog', 'check', 'formData'], function (select, date, art, dialog, check, formData) {
        //查询是否开启优惠券
        $.ajax({
            url: BASE_PATH + "/shop/wechat/op/qry-coupon-setting",
            success: function (result) {
                var data = result.data;
                if (result.success) {
                    $('.order-body-wrapper').show();
                    if (!data || data.configStatus == 0) {
                        toggleShow();
                        isBegin = false;
                    } else if (data && data.configStatus == 1) {
                        originObj = data;
                        isBegin = true;
                        $('#couponType').val(data.couponTypeName);
                        $('#couponTypeId').val(data.couponTypeId);
                        $('#couponNum').val(data.originNumber);
                        $('#id').val(data.id);
                        if (data.isLongTime == 0) {
                            $('input[name="isLongTime"]:eq(1)').prop('checked', true).trigger('change');
                            $('#startTime').val(data.startTimeStr);
                            $('#endTime').val(data.endTimeStr);
                        }
                    }
                    //初始化图表
                    getRenderEchart({});
                } else {
                    dialog.fail(result.errorMsg || "获取信息失败");
                }
            }
        });


        //异步获取下拉列表数据
        select.init({
            dom: '#couponType',
            url: BASE_PATH + '/account/coupon/search?couponType=1',
            showKey: "id",
            showValue: "couponName",
            noDataCallback: function () {
                dialog.msg("您还没有卡券，点击新增优惠券去设置吧~", 1);
            }
        });
        select.init({
            dom: '#couponType1',
            url: BASE_PATH + '/shop/wechat/op/qry-coupon-his-setting',
            showKey: "couponTypeId",
            showValue: "couponTypeName",
            allSelect: true
        });
        // 配置的日期
        date.dpStartEnd({
            start: 'startTime',
            end: 'endTime',
            startSettings: {
                dateFmt: 'yyyy-MM-dd',
                minDate: '%y-%M-%d',
                maxDate: '#F{$dp.$D(\'endTime\')}'
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd',
                minDate: '#F{$dp.$D(\'startTime\')||\'%y-%M-%d\'}'
            }
        });
        date.dpStartEnd({
            start: 'startTime1',
            end: 'endTime1',
            startSettings: {
                dateFmt: 'yyyy-MM-dd',
                maxDate: '#F{$dp.$D(\'endTime1\')}'
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd',
                minDate: '#F{$dp.$D(\'startTime1\')}'
            }
        });

        //初始化数据校验
        var couponMinValue = function (val, v) {
            var msg = [
                "",
                "数量不能小于" + v,
                "请输入数字"
            ];
            if (!$.isNumeric(val)) {
                return {msg: msg[2], result: false};
            }
            if (Number(val) >= v) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        }
        var couponMaxValue = function (val, v) {
            var msg = [
                "",
                "数量不能大于" + v,
                "请输入数字"
            ];
            if (!$.isNumeric(val)) {
                return {msg: msg[2], result: false};
            }
            if (Number(val) <= v) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        }

        check.helper('couponMinValue', couponMinValue);
        check.helper('couponMaxValue', couponMaxValue);
        check.init();

        $(document)
        //关闭送券功能
            .on('click', '#toggle-true', function () {
                if ($.isEmptyObject(originObj)) {
                    toggleShow();
                } else {
                    dialog.confirm("关闭“关注送券”功能会结束当前优惠券的派发，是否继续？", function () {
                        var data = formData.get('.js-setting', true);
                        data.configStatus = 0;
                        saveSetting(data);
                    });
                }
            })

            //开启送券功能
            .on('click', '#toggle-false', function () {
                toggleShow();
                $('.js-setting').find('input').not('input:radio').val("");
                $('input:radio').eq(0).prop('checked', true);
                originObj = {};
            })

            //送券时间radio切换
            .on('change', 'input[name="isLongTime"]', function () {
                if ($('input[name="isLongTime"]:checked').val() == 0) {
                    $('.give-time').show();
                } else {
                    $('.give-time').hide();
                }
            })
            //优惠券发放设置
            .on('click', '.js-save', function () {
                if ($('input[name="isLongTime"]:checked').val() == 1) {
                    $('.time-select').val("");
                }
                var data = formData.get('.js-setting', true);
                if (!check.check(null, false)) {
                    return;
                }
                if (!$.isEmptyObject(originObj)) {
                    if ($('#couponTypeId').val() != originObj.couponTypeId) {
                        dialog.confirm("更换卡券会结束当前优惠券的派发，是否继续？", function () {
                            data.configStatus = 1;
                            saveSetting(data);
                        });
                    } else {
                        if ($('#couponTypeId').val() == originObj.couponTypeId && $('#couponNum').val() == originObj.originNumber
                            && $('#startTime').val() == originObj.startTimeStr && $('#endTime').val() == originObj.endTimeStr) {
                            dialog.msg("卡券设置未发生变动，无需保存", 1);
                        } else {
                            data.configStatus = 1;
                            saveSetting(data);
                        }
                    }
                } else {
                    data.configStatus = 1;
                    saveSetting(data);
                }

            })
            //优惠券统计数据查询
            .on('click', '.js-search', function () {
                var param = formData.get('.chooseBox', true);
                return getRenderEchart(param);
            })
            //查看详情
            .on('click', '.js-detail', function () {
                var qryParams = formData.get('#searchForm');
                var qryParamsStr = '';
                $.each(qryParams, function (index, element) {
                    qryParamsStr += (index + '=' + element + '&');
                });
                window.location = BASE_PATH + '/shop/wechat/wechat-coupon-list?' + qryParamsStr;
            })
        // 跳转短信群发页面
            .on('click', '.js-sms-btn', function () {
                if (isBegin) {
                    location.href = BASE_PATH + '/shop/wechat/sms-info';
                } else {
                    dialog.warn('请先开启活动！');
                }
            });

        function saveSetting(param) {
            $.ajax({
                type: "POST",
                contentType: 'application/json',
                url: BASE_PATH + "/shop/wechat/op/save-coupon-setting",
                data: JSON.stringify(param),
                dataType: "json",
                success: function (result) {
                    if (result.success) {
                        dialog.success('设置成功', function () {
                            location.reload();
                        });
                    } else {
                        dialog.fail(result.errorMsg);
                    }
                }
            });
        }

        function toggleShow() {
            $('.switch, .toggle').toggle();
            $('#intro').toggleClass('right-box');
        }

        function renderEchart(value) {

            // 指定图表的配置项和数据
            var option = {
                title: {
                    show: true,
                    text: '单位:张',
                    top: '5%',
                    left: '16%',
                    textStyle: {
                        fontSize: '12px',
                        color: '#999'
                    }
                },
                grid: {
                    left: '8%',
                    right: '8%',
                    bottom: '5%',
                    top: '25%',
                    containLabel: true
                },
                xAxis: {
                    data: ['发放券总数', '已使用券总数', '未使用券总数'],
                    splitLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#999'
                        }
                    }
                },
                yAxis: {
                    splitLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        textStyle: {
                            color: '#999'
                        }
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#999'
                        }
                    },
                    minInterval: 1

                },
                series: [{
                    type: 'bar',
                    data: [{
                        value: value[0],
                        itemStyle: {
                            normal: {
                                color: '#4bc8f4'
                            }
                        },
                        label: {
                            normal: {
                                show: true,
                                position: 'top',
                                textStyle: {
                                    color: '#333'
                                }
                            }
                        }
                    }, {
                        value: value[1],
                        itemStyle: {
                            normal: {
                                color: '#4bf4da'
                            }
                        },
                        label: {
                            normal: {
                                show: true,
                                position: 'top',
                                textStyle: {
                                    color: '#333'
                                }
                            }
                        }
                    }, {
                        value: value[2],
                        itemStyle: {
                            normal: {
                                color: '#c1d1ce'
                            }
                        },
                        label: {
                            normal: {
                                show: true,
                                position: 'top',
                                textStyle: {
                                    color: '#333'
                                }
                            }
                        }
                    }],
                    barWidth: '35'
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
            $('.first+span').text(value[0]);
            $('.second+span').text(value[1]);
            $('.third+span').text(value[2]);

        }

        function getRenderEchart(param) {
            $.ajax({
                type: "GET",
                //global:false,//不加载loading
                url: BASE_PATH + "/shop/wechat/op/qry-coupon-statis",
                data: param,
                async: true,
                success: function (result) {
                    if (result.success) {
                        if (!result.data) {
                            dialog.info("暂无数据！", 1);
                        } else {
                            var value = [];
                            value[0] = result.data.sentNumber;
                            value[1] = result.data.usedNumber;
                            value[2] = result.data.unusedNumber;
                            renderEchart(value);
                        }
                    } else {
                        dialog.fail(result.errorMsg || " 获取统计数据失败!");
                    }
                }
            });
        }

    });// seajs
})







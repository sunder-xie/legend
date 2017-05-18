$(function () {
    var baseUrl = BASE_PATH + '/shop/report/business/month/';
    var $wrapper = $('.yqx-wrapper');
    var extend = $.extend;
    // echart 初始化
    var chartsDataSetting = {
        tag: {
            operate: ['operateIn', 'operateOut', 'operateTrend'],
            card:    ['memberCard', 'comboCard', 'couponCard']
        },
        dom: {
            operateIn:  'operateInChart',
            operateOut:  'operateOutChart',
            operateTrend: 'operateTrendChart',
            purchase: 'purchaseChart',
            service:  'serviceChart',
            visit:    'visitChart',
            goods:    'goodsChart',
            memberCard: 'memberCardChart',
            comboCard: 'comboCardChart',
            couponCard: 'couponCardChart',
            stock:    'stockChart',
            brand:    'brandChart'
        },
        type: {
            pie: 'service, goods, purchase',
            bar: 'visit, stock',
            line: 'operateTrend',
            yBar: 'brand, operateIn, operateOut, memberCard, comboCard, couponCard,'
        },
        url: {
            // 图表的请求 url
            // 部分文本夹杂在 图表里
            charts: {
                // 回访
                visit: {
                    url: baseUrl + 'visitant',
                    state: false
                },
                // 库存统计
                stock: {
                    url: baseUrl + 'warehouse_goods_cate_amount'
                },
                goods: {
                    url: baseUrl + 'goods_cat_pie_rank'
                },
                purchase: {
                    url: baseUrl + 'purchase_top'
                },
                operateIn: {
                    url: baseUrl + 'paidin'
                },
                operateOut: {
                    url:  baseUrl + 'paidout'
                },
                // 营业趋势
                operateTrend: {
                    url: {
                        month: [baseUrl + 'business_month_trend', baseUrl + 'paidin_month_trend'],
                        year: [baseUrl + 'business_year_trend', baseUrl + 'paidin_year_trend']
                    }
                },
                // 车型
                brand: {
                    url: baseUrl + 'car_brand_top10'
                },
                memberCard: {
                    url: baseUrl + 'member_strip'
                },
                couponCard: {
                    url: baseUrl + 'coupon_strip'
                },
                comboCard: {
                    url: baseUrl + 'combo_strip'
                },
                // 服务
                service: {
                    url: baseUrl + 'service_cat_pie'
                }
            },
            // 文本的请求
            text: {
                operate: {
                    url: baseUrl + 'overview',
                    state: false
                },
                // 配件类型库存统计的总计部分
                stock: {
                    url: [baseUrl + 'warehouse_goods_cate_total'
                        // 当前库存总金额
                        ,baseUrl + 'warehouse_all_inventory_amount']
                }
            },
            // 表格数据的url
            table: {
                service: {
                    service: {
                        url: baseUrl + 'service_cat_rank',
                        state: false
                    },
                    service2: {
                        url: baseUrl + 'service_rank'
                    }
                },
                stock: {
                    stock: {
                        url: baseUrl + 'warehouse_goods_cate_stat'
                    },
                    stock2: {
                        url: baseUrl + 'warehouse_goods_stat'
                    }
                },
                card: {
                    memberCard: {
                        url: baseUrl + 'member_list'
                    },
                    couponCard: {
                        url: baseUrl + 'coupon_list'
                    },
                    comboCard: {
                        url: baseUrl + 'combo_list'
                    }
                },
                goods: {
                    contentType: 'json',
                    goods: {
                        url: baseUrl + 'goods_cat_rank'
                    },
                    goods2: {
                        url: baseUrl + 'goods_rank'
                    }
                },
                purchase: {
                    contentType: 'json',
                    type: 'post',
                    purchase: {
                        url: baseUrl + 'category_purchase'
                    },
                    purchase2: {
                        url: baseUrl + 'purchase'
                    }
                },
                brand: {
                    url: baseUrl + 'series_rank'
                }
            }
        }, // url end
        // 各个图表的 vo
        // {name, value} 的外层对象名
        vo: {
            purchase: 'topK',
            operateIn: 'businessPaidInAmountVoList',
            operateOut: 'businessPaidOutAmountVoList',
            operateTrend: 'datePointVoList'
        },
        key: {
            stock: {
                name: 'goodsCateName',
                seriesName: ['期初金额', '期末金额'],
                value: ['beginGoodsAmount', 'endGoodsAmount']
            },
            goods: {
                name: 'catName',
                value: 'saleAmount'
            },
            operateIn: {
                name: 'businessTagName',
                value: 'paidAmount'
            },
            operateOut: {
                name: 'businessTagName',
                value: 'paidAmount'
            },
            brand: {
                name: 'carBrand',
                value: 'receptionNumber'
            },
            purchase: {
                name: 'categoryName',
                value: 'purchaseAmount'
            },
            operateTrend: {
                name: 'date',
                value: 'amount'
            },
            service: {
                name: 'serviceCatName',
                value: 'saleNum'
            }
        }// key end
    };
    var charts = chartsInit(chartsDataSetting.dom),
    // 类型配置
        chartsType = chartsDataSetting.type,
        chartsTag  = chartsDataSetting.tag,
    // url
        textUrl = chartsDataSetting.url.text,
        tableUrl = chartsDataSetting.url.table,
        chartsUrl = chartsDataSetting.url.charts,
        // 各个图表的 VO
        voList = chartsDataSetting.vo,
        // 各个图表的键值
        keyList = chartsDataSetting.key;

    // 系列名称
    // 其余的部分都在返回的数据里了
    var chartSeriesName = {
        operateTrend: ['营业额', '收款金额']
    };

    var chartsSeriesDesc = {
        '老客户': '当月第二次以上到店消费的客户，含第二次，以确认账单为准，统计车牌',
        '新客户': '当月第一次到店消费的客户，以确认账单为准，统计车牌',
        '充值':   '当月充值总金额',
        'memberCard充值收款': '当月会员卡充值收款总额',
        '收款金额':     '当月充值总金额',
        '会员卡支付':   '当月用会员卡支付的金额',
        '会员卡特权抵扣':  '当月使用会员卡折扣抵扣的金额',
        'comboCard收款': '当月发放计次卡收款金额',
        '抵扣工时总额':  '当月使用计次卡抵扣的服务金额',
        '抵扣总金额':    '当月各类优惠券抵扣金额之和'
    };
    // 跳转的链接及参数
    // time 另做处理
    var links = {
        '会员卡': {
            url: BASE_PATH + '/shop/stats/card/coupon-recharge?',
            param: {
                time: ['startTime', 'endTime'],
                tab: 0,
                refer:'month'
            }
        },
        '充值': {
            url: BASE_PATH + '/shop/stats/card/coupon-recharge?',
            param: {
                time: ['startTime', 'endTime'],
                tab: 0,
                consumeTypeId:1,
                consumeTypeName: '会员卡充值',
                refer:'month'

            }
        },
        'memberCard充值收款': {
            url: BASE_PATH + '/shop/stats/card/coupon-recharge?',
            param: {
                time: ['startTime', 'endTime'],
                tab: 0,
                consumeTypeId:1,
                consumeTypeName: '会员卡充值',
                refer:'month'
            }
        },
        '会员卡支付': {
            url: BASE_PATH + '/shop/stats/card/coupon-consume?',
            param: {
                time: ['startTime', 'endTime'],
                tab: 0,
                refer:'month'
            }
        },
        '会员卡特权抵扣': {
            url: BASE_PATH + '/shop/stats/card/coupon-consume?',
            param: {
                time: ['startTime', 'endTime'],
                tab: 0,
                refer:'month'
            }
        },
        '退卡退款': {
            url: BASE_PATH + '/shop/stats/card/coupon-recharge?',
            param: {
                time: ['startTime', 'endTime'],
                tab: 0,
                consumeTypeId: 6,
                consumeTypeName: '会员卡退卡',
                refer:'month'
            }
        },
        '计次卡': {
            url: BASE_PATH + '/shop/stats/card/coupon-recharge?',
            param: {
                time: ['startTime', 'endTime'],
                tab: 1,
                refer:'month'
            }
        },
        '收款金额': {
            url: BASE_PATH + '/shop/stats/card/coupon-recharge?',
            param: {
                time: ['startTime', 'endTime'],
                tab: 1,
                refer:'month'
            }
        },
        '抵扣工时总额': {
            url: BASE_PATH + '/shop/stats/card/coupon-consume?',
            param: {
                time: ['startTime', 'endTime'],
                tab: 1,
                refer:'month'
            }
        },
        '优惠券': {
            url: BASE_PATH + '/shop/stats/card/coupon-recharge?',
            param: {
                time: ['startTime', 'endTime'],
                tab: 2,
                refer:'month'
            }
        },
        'comboCard收款': {
            url: BASE_PATH + '/shop/stats/card/coupon-recharge?',
            param: {
                time: ['startTime', 'endTime'],
                tab: 1,
                refer:'month'
            }
        },
        '抵扣总金额': {
            url: BASE_PATH + '/shop/stats/card/coupon-consume?',
            param: {
                time: ['startTime', 'endTime'],
                tab: 2,
                refer:'month'
            }
        },
        '快修快保单':  {
            url: BASE_PATH + '/shop/stats/order_payment?',
            param: {
                time: ['sPayTime', 'ePayTime'],
                orderTag: 3,
                orderTagName: "快修快保",
                refer:'month'
            }
        },
        '综合维修单': {
            url: BASE_PATH + '/shop/stats/order_payment?',
            param: {
                time: ['sPayTime', 'ePayTime'],
                orderTag: 1,
                orderTagName: "综合维修",
                refer:'month'
            }
        },
        '洗车单': {
            url: BASE_PATH + '/shop/stats/order_payment?',
            param: {
                time: ['sPayTime', 'ePayTime'],
                orderTag: 2,
                orderTagName: "洗车单",
                refer:'month'
            }
        },
        '收款单': {},
        '销售单': {
            url: BASE_PATH + '/shop/stats/order_payment?',
            param: {
                time: ['sPayTime', 'ePayTime'],
                orderTag: 5,
                orderTagName: "销售",
                refer:'month'
            }
        },
        // 以下是 a 标签的部分
        '营业总额': {
            url: BASE_PATH + '/shop/stats/order_info_detail?',
            param: {
                time: ['payStartTime', 'payEndTime'],
                orderStatus: 'DDYFK',
                clear: true,
                refer:'month'
            }
        },
        '采购总额': {
            url: BASE_PATH + '/shop/stats/warehouse-info/in?',
            param: {
                time: ['startTime', 'endTime'],
                status: 'LZRK',
                clear: true,
                refer:'month'
            }
        },
        '服务总车辆': {
            url: BASE_PATH + '/shop/stats/order_info_detail?',
            param: {
                time: ['payStartTime', 'payEndTime'],
                orderStatus: 'DDYFK',
                clear: true,
                refer:'month'
            }
        },
        '新建车辆': {
            url: BASE_PATH + '/shop/stats/new_customer/ng?',
            param: {
                time: ['startTime', 'endTime'],
                refer:'month'
            }
        },
        '老客户': {
            url: BASE_PATH + '/marketing/ng/analysis/type?',
            param: {
                time: ['search_sTime', 'search_eTime'],
                tag: 'older',
                refer:'month'
            }
        },
        '新客户': {
            url: BASE_PATH + '/marketing/ng/analysis/type?',
            param: {
                time: ['search_sTime', 'search_eTime'],
                tag: 'newer',
                refer:'month'
            }
        }
    };
    // 字段的名称
    var chartCategoryName = {
        visit: {
            newCustomerNumber: '新客户',
            oldCustomerNumber: '老客户'
        },
        memberCard: {
            rechargeAmount: '充值',
            payAmount: '充值收款',
            cardPayAmount: '会员卡支付',
            discountAmount: '会员卡特权抵扣',
            retreatCardAmount: '退卡退款'
        },
        comboCard: {
            reciveAmount: '收款',
            discountAmount: '抵扣工时总额'
        },
        couponCard: {
            discountAmount: '抵扣总金额'
        }
    };
    // 饼图的大小及位置配置
    var pie = {
        // 半径
        radius: {
            default: '50%',
            operate: '50%',
            service: '65%',
            // 环形
            goods: ['50%', '60%']
        },
        // 位置, [x, y]
        center: {
            default: ['50%', '50%'],
            purchase: ['50%', '50%'],
            operate: [
                ['25%', '55%'],
                ['75%', '55%']
            ],
            service: ['55%', '53%'],
            goods: ['55%', '55%']
        }
    };
    // 颜色的配置
    var chartColor = {
        pie: [
            ['#ffb637', '#4cb9fe', '#9fc527', '#cacac9', '#fded40', '#4df2fc', '#fe7545'],
            ['#4cb9fe', '#cacac9']
        ],
        bar: ['#9fc527', '#52a30b'],
        yBar: '#9fc527',
        font: '#333',
        axis: ['#dcdcdc', '#999999'],
        line: ['#4cb9fe', '#9fc527']
    }, axisLine = {
        lineStyle: {
            color: chartColor.axis[0]
        }
    };
    // 其他的
    var baseSetting = {
        // 坐标轴设置
        grid: {
            default: {
                top: 0,
                left: 80,
                bottom: 0,
                right: 70
            },
            stock: {
                containLabel: true,
                 top: 40,
                left: 20,
                bottom: 10,
                right: 20
            },
            visit: {
                width: 440,
                top: 30,
                bottom: 50,
                left: 'center'
            },
            card: {
                top: 0,
                left: 120,
                bottom: 0,
                right: 60
            },
            brand: {
                top: 0,
                left: 70,
                bottom: 0,
                right: 30
            },
            operateTrend: {
                left: 60,
                top: 50,
                bottom: 70,
                right: 10
            }
        },
        // 柱状图单个的宽度
        barSize: {
            barWidth: 60
        },
        // y 轴柱状图的值显示的位置
        yBarLabel: {
            normal: {
                position: 'right'
            }
        },
        yBarSize: {
            barWidth: 15
        },
        // y 轴值的显示
        topLabel: {
            normal: {
                show: true,
                position: 'top',
                textStyle: {
                    color: chartColor.font,
                    fontFamily: 'Arial'
                }
            }
        },
        // 不显示坐标轴的
        noAxis: {
            type: 'value',
            // 轴线
            axisLine: false,
            // 标签
            axisLabel: false,
            // grid 的分隔线
            splitLine: false,
            // 刻度
            axisTick: false
        },
        // 类目轴
        axisCategory: {
            type: 'category',
            splitLine: false,
            axisTick: false,
            axisLine: axisLine,
            data: []
        },
        // 全局文本
        textStyle: {
            color: chartColor.font
        }
    };

    var seriesSetting = {
        bar: extend(true, {
            type: 'bar',
            label: baseSetting.topLabel
        }, baseSetting.barSize),
        yBar: extend({
            type: 'bar',
            label: extend(true, {}, baseSetting.topLabel, {normal :{show:true, position: 'right'}}),
            barMinHeight: 7,
            barGap: 20
        }, baseSetting.yBarSize)
    };
    // 各 chart 的基本设置，数据之后填充
    var optionSetting = {
        pie: {
            tooltip: {
                trigger: 'item',
                formatter: function (params) {
                    var name = params.name.replace(/\d+.\d+%$/, '');

                    return name + '：' + params.value;
                }
            },
            series: []
        },
        bar: {
            yAxis: baseSetting.noAxis,
            xAxis: baseSetting.axisCategory,
            tooltip: {
                trigger: 'item',
                formatter: function (params) {
                    var desc = chartsSeriesDesc;
                    var name = params.name;
                    var _name = params.seriesName + name;

                    return desc[name] || desc[_name] || '';
                }
            },
            series: [],
            textStyle: baseSetting.textStyle,
            color: chartColor.bar
        },
        yBar: {
            yAxis: extend(true, baseSetting.axisCategory, {
                axisLine: {
                    lineStyle: {
                        color: chartColor.axis[1]
                    }
                }
            }),
            xAxis: baseSetting.noAxis,
            series: [],
            textStyle: baseSetting.textStyle
        },
        line: {
            tooltip: {
                trigger: 'axis'
            },
            yAxis: {
                type: 'value',
                splitLine: false,
                axisTick: false,
                axisLine: axisLine
            },
            xAxis: {
                type: 'category',
                splitLine: false,
                axisTick: false,
                axisLine: axisLine,
                data: []
            },
            textStyle: baseSetting.textStyle,
            series: []
        }
    };
    var dateSelectors = [
        '.js-date-month'
    ];

    var date = $('.yqx-wrapper .date'),
        // 营业趋势的选项
        operateTrend = 'month';
    // 无数据时，其图片显示的位置
    var noDataImgPosition = {
        operateIn: {
            left: 170,
            top: 35
        },
        operateOut: {
            left: 136,
            top: 30
        }
    };
    var noData = $('#noData').remove()
        .clone().removeClass('hide').removeAttr('id');

    var isBPShare = $('#isBPShare').val() === 'true' ? true : false;
    // 钣喷共享服务
    var _shareService = null;
    /*--------------------- seajs.use start ---------------------*/
    seajs.use([
        'date',
        'table',
        'dialog',
        'ajax',
        'art',
    ], function (date, table, dialog, ajax, art) {
        var old = $('input.date').val();

        date.datePicker('input.date', {
            dateFmt: 'yyyy-MM',
            maxDate: 'yyyy-MM'
        });

        $('input.date').on('blur', function () {
            var date = new Date( $(this).val() );
            var desc = $('.current-item.tab-item').data('desc');
            var flag = true;
            var now = new Date();

            if(desc == 'stock') {
                seajs.use('dialog', function (dialog) {
                    if (date.getMonth() <= 8 && date.getFullYear() <= 2016) {
                        dialog.warn('请选择2016年9月份之后的日期');
                        flag = false;
                    } else if (now.getMonth() - date.getMonth() > 2
                        || date.getFullYear() != now.getFullYear()
                    ) {
                        dialog.warn('请选择最近两个月的时间');
                        flag = false;
                    }
                });
            }
            if(!flag) {
                $(this).val(old);
            } else {
                old = $(this).val();
            }
        });

        dialog.titleInit();

        art.helper('toPrice', toPrice)
    });

    // 初始时更新链接及其参数
    updateLink();
    /*--------------------- seajs.use end ---------------------*/

    /*--------------------- dom 事件 start ----------------------*/
    // 营业趋势，年/月
    $('.js-trend-select').on('click', function () {
        var target = $(this).data('target');
        var data;

        var current = $(this).parent().find('.current-trend');

        if(current[0] == this) {
            return;
        }
        current.removeClass('current-trend');

        $(this).addClass('current-trend');

        if(target == 'month') {
            data = {
                month: date.val()
            }
        } else {
            data = {
                year: new Date(date.val()).getFullYear()
            }
        }

        operateTrend = target;

        chartsUrl.operateTrend.state = false;
        getData('operateTrend', data, true);
    });
    // tab 事件
    $('.js-tab-item').on('click', function () {
        var target = $(this).data('target');
        var desc = $(this).data('desc');
        var data = {month: date.val()};
        var flag = true, _date = new Date( date.val() );
        var now = new Date();
        var t, i;

        $wrapper.find('.current-item').removeClass('current-item')
            .end().find('.current-chart').removeClass('current-chart').addClass('hide');

        $(this).addClass('current-item');
        $(target).addClass('current-chart').removeClass('hide');
        // 重置一些元素的状态
        resetStatus();

        if(desc == 'stock') {
            seajs.use('dialog', function (dialog) {
                if (_date.getMonth() <= 8 && _date.getFullYear() <= 2016) {
                    dialog.warn('请选择2016年9月份之后的日期');
                    date.val(now.getFullYear() + '-' + (now.getMonth() + 1));
                    flag = false;
                }
            });
        }

        if(!flag) {
            return;
        }

        if(t = chartsTag[desc]) {
            for(i in t) {
                getData(t[i], data, !chartsUrl[ t[i] ].state);
            }
        } else if(chartsUrl[desc]) {
            getData(desc, data, !chartsUrl[desc].state);
        }

        if(desc == 'operate') {
            getTotalSurfaceNum(data);
        }

        if(desc == 'service' && isBPShare) {
            tableUrl.service.service.url = null;
            $('.js-service-tab span').eq(0).click();
        }

        if(textUrl[desc] && !textUrl[desc].state) {
            getTextData(textUrl[desc], data, desc);
        }

        // 更新和查询日期有关的文字
        // 如：8月服务总车辆
        updateDateText( date.val() );
        // 表格
        setTable(desc);
    })
        // 初始查询
        .eq(0).click();

    // 配件销售统计，按标准分类/按自定义分类
    $('.goods-main .js-goods-sub').on('click', function () {
        var current = $('.goods-main .current-sub-tab');

        if(current[0] != this) {
            current.removeClass('current-sub-tab');
            $(this).addClass('current-sub-tab');
        }

        $('.search-form .js-search-btn').trigger('click.chart');
    });

    // 快速筛选
    $('.js-quick-select').on('click', function () {
        var index = +$(this).data('target');
        var date = new Date();

        date.setMonth(date.getMonth() + index);

        var month = date.getMonth() + 1;

        if(month < 10) {
            month = '0' + month;
        }

        $('.date').val( date.getFullYear() + '-' + month );

        $('.search-form .js-search-btn').trigger('click.chart');
    });
    $('.js-search-btn').on('click.chart', resetQuery);

    $('.js-second-search').on('click', function () {
        var target = $(this).data('target').split('|');
        var t = tableUrl;

        for(var i in target) {
            t = t[ target[i] ];
        }

        // 二次选择单独一个表刷新
        t.send(1);
    });

    $('.js-service-tab span').on('click', function () {
        $(this).addClass('service-current').siblings().removeClass('service-current');
        seajs.use('table', function (table) {
            if(!_shareService) {
                _shareService = table.init({
                    fillid: 'serviceFill',
                    pageid: 'servicePage',
                    tplid: 'serviceTpl',
                    pageTplId: 'pageTpl',
                    url: BASE_PATH + '/shop/report/business/month/spray_service_cat_rank',
                    data: function () {
                        return {
                            month: date.val(),
                            shared: $('.js-service-tab .service-current').data('shared')
                        }
                    },
                    callback: function () {
                        var tabIndex = $('.service-current').index('.service-tab span');
                        if( tabIndex == 1){
                            $('.paint-num').hide();
                        }else{
                            $('.paint-num').show();
                        }
                    }
                });
            } else {
                _shareService(1);
            }

        });
        var data = {
            month: date.val(),
            shared: $('.js-service-tab .service-current').data('shared')
        };
        getTotalSpraySaleAmount(data);

    });

    /*--------------------- dom 事件 end ----------------------*/
    /*--------------------- echarts 事件 start ----------------------*/
    charts.operateIn.on('click', echartClick);
    charts.operateOut.on('click', echartClick);
    charts.visit.on('click', echartClick);

    chartsTag.card.forEach(function (e) {
        charts[e].on('click', echartClick);
    });

    /*--------------------- echarts 事件 end ----------------------*/
    /*--------------------- 函数 start --------------------------*/

    function echartClick (params) {
        var name = params.name;
        var _name = params.seriesName + name, t;
        name = name.match(/[\u4E00-\u9FA5]+/g, '');

        if(t = links[name] || links[_name]) {
            window.open(t.url + $.param( setParamTimeStr(t.param) ));
        }

    }

    // 柱形图 option 配置
    function setBarData(data, name, type, vertical) {
        var option, gridType = name, axis;
        var j = 0, series, t;
        var barData, sort = false;

        if(!baseSetting.grid[gridType] && name.indexOf('Card') > -1) {
            gridType = 'card';
        }

        // 水平的
        if(!vertical) {
            axis = 'xAxis';
        } else {
            // 竖立的，即 y 轴的
            type = 'yBar';
            axis = 'yAxis';
            sort = true;
        }

        if(!chartCategoryName[name]) {
            barData = convertData(voList[name] ? data[ voList[name] ] : data, keyList[name], sort, type);
        } else {
            t = []; j = [];
            // name 和 value 分为两个数组
            // 一一对应
            for(var i in chartCategoryName[name]) {
                t.push( chartCategoryName[name][i] );
                j.push( data[i] );
            }
            barData = {
                name: t,
                value: j
            };
        }
        series = extend(true, {}, seriesSetting[type]);
        option = extend(true, {
            grid: baseSetting.grid[gridType] || baseSetting.grid.default
        }, optionSetting[type]);

        if(name == 'memberCard') {
            $.extend(option.xAxis, {
                axisLabel:{
                    interval:0
                }
            });
        }

        if( name == 'stock' ) {
            series.label = false;
            option.legend = {
                right: 10,
                top: 0,
                data: ['期初金额', '期末金额']
            };
            loadyAxisAndTitle(option, undefined, 1000);
        }

        if( !hasData(barData, name, type) ) {
            return {};
        }

        j = 0;
        // 按类别名
        if(!barData.seriesMulti) {
            option.series.push(series);
            series.data = [];
            for (var key in barData.name) {
                t = barData.name[key];
                // 类别名
                option[axis].data.push(
                    t
                );
                // 对应数据
                series.data.push(
                    setSeries(chartColor[type], barData.value[key], '', j, type)
                );

                j += 1;
            }
            series.name = name;
        } else {
            // 比如库存分析
            // demo: http://echarts.baidu.com/demo.html#bar-stack
            series.barWidth = 15;
            option.xAxis.data = barData.name;
            option.xAxis.axisLabel = {
                interval: 0,
                formatter: function (value) {
                    var t = value;

                    if(value.length > 6) {
                        t = value.slice(0, 6) + '\n' + value.slice(6);
                    }
                    return t;
                }
            };
            option.tooltip ={
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            };
            for(key in barData.value) {
                if(key == 'length') {
                    continue;
                }
                t = barData.value[key];
                option.series.push(
                    $.extend(true, {
                        name: key,
                        data: t
                    }, series)
                )
            }
        }

        return option;
    }

    // 按 name 获取数据
    function setSeries(color, value, name, i, type) {
        var v =  typeof value == 'object' ? value[name] || value[i] : value;
        var colorStr = typeof color == 'object' && color.length ? color[i] : color;
        var label = type === 'yBar' ? baseSetting.yBarLabel : {position: 'top'};

        name = name || [];

        return {
            value: v,
            name: name,
            itemStyle: {
                normal: {
                    color: colorStr
                }
            },
            label: label
        }
    }

    // 配置 饼图
    function setPieData(data, name, type, index, clearOption) {
        var option;
        var center = index != null ? pie.center[name][index] : pie.center[name] || pie.center.default;
        var pieData, t;

        if(!clearOption) {
            option = charts[name].getOption()
        } else {
            option = extend(true, {}, optionSetting[type])
        }

        if(voList[name]) {
            t = data[ index ? voList[name][index] : voList[name] ];
        } else {
            t = data;
        }

        pieData = convertData(t, keyList[name]);

        if( !hasData(pieData, name, type, index) ) {
            return option;
        }
        // 会员卡 -> 会员卡\n30%
        pieData.name = convertPieName(pieData, name);

        option.series.push({
            type: type,
            radius: pie.radius[name] || pie.radius.default,
            center: center,
            labelLine: {
                normal: {
                    length: 10
                }
            },
            data: pieData.name.map(
                setSeries.bind(null, chartColor[type][0], pieData.value)
            )
        });
        return option;
    }

    // 折线图配置
    function setLineData(data, name, type, clearOption, index) {
        var series, option;

        var lineData = convertData(data[ voList[name] ], keyList[name], false, type);

        if(!clearOption) {
            option = charts[name].getOption();
        } else {
            option = extend(true, {}, optionSetting[type]);
        }
        series = option.series;

        // 营业趋势的时间文字如 (2016-01-10 - 2016-01-31)
        if(name == 'operateTrend' && lineData.name.length > 1) {
            setTextData({
                trendTime: '（' + convertTime(lineData.name[0]) + ' - '
                + convertTime(lineData.name[ lineData.name.length - 1 ]) + '）'
            });
        }

        series.push({
            type: type,
            lineStyle: {
                normal: {
                    color: chartColor[type][index]
                }
            },
            itemStyle: {
                normal: {
                    color: chartColor[type][index]
                }
            },
            data: lineData.value,
            name: chartSeriesName[name][index]
        });

        if(clearOption) {
            option.xAxis.data = lineData.name;
        }
        option.series = series;
        option.grid   = baseSetting.grid[name] || baseSetting.grid.default;

        return option;
    }

    function setOption(data, name, index, clearOption) {
        if (clearOption) {
            charts[name].clear();
        }
        if (chartsType.pie.indexOf(name) > -1) {
            return setPieData(data, name, 'pie', index, clearOption);
        }
        if (chartsType.line.indexOf(name) > -1) {
            return setLineData(data, name, 'line', clearOption, index);
        }
        if (chartsType.bar.indexOf(name) > -1) {
            return setBarData(data, name, 'bar', false, clearOption);
        }
        if (chartsType.yBar.indexOf(name) > -1) {
            return setBarData(data, name, 'bar', true, clearOption);
        }
    }

    // 获取数据
    function getData(name, data, clearOption) {
        var request = chartsUrl[name],
            url = request ? request.url : null;
        var type = {};

        if(name == 'operateTrend') {
            url = request.url[operateTrend];
        }
        // 已请求过的, 和无效的
        if( (request && request.state && !clearOption) || !url || !request) {
            return;
        }

        if(request.contentType === 'json') {
            type.contentType = 'application/json'
            data = JSON.stringify(data);
        }
        if(name == 'goods') {
            data.tag = $('.goods-chart .current-sub-tab').data('value');
        }

        url = [].concat(url);

        for(var i in url) {
            // 异步的情况，保持 i 不变
            (function (i) {
                $.ajax(
                    extend({
                        url: url[i],
                        data: data,
                        success: function (json) {
                            if (json && json.success) {
                                var option = setOption(json.data, name, url.length > 1 ? i : null, clearOption);

                                clearOption = false;
                                if (i == url.length - 1) {
                                    request.state = true;
                                }

                                charts[name].resize();
                                charts[name].setOption(option);

                                if (name == 'operateTrend') {
                                    setTextData(json.data, i);
                                } else if (name == 'operateOut'
                                    || name == 'operateIn') {

                                    if(name === 'operateOut') {
                                        // '会员卡退款退卡'
                                        charts[name].setOption({
                                            grid: {
                                                left: 93
                                            }
                                        })
                                    }
                                    // 有后缀的
                                    setTextData(json.data, name);
                                } else
                                    setTextData(json.data);
                            } else {
                                seajs.use('dialog', function (dialog) {
                                    dialog.fail(json.errorMsg)
                                });
                            }
                        }
                    }, type)
                );
            })(i);
        }
    }

    // 设置表格
    function setTable(name) {
        var urls, ajax, dataType, totalElements, data, pageType = null;
        var callback = null;

        if(!tableUrl[name]) {
            return;
        }
        if(typeof tableUrl[name].url == 'string') {
            urls = {};
            urls[name] = tableUrl[name]
        } else {
            urls = tableUrl[name];
        }

        if(name == 'brand' || urls.contentType == 'json') {
            ajax = {
                contentType: 'application/json'
            };
            ajax.type = urls.type || 'get';
            if(name == 'brand') {
                totalElements = 'totalSize';
                ajax.type = 'post';
            }
            if(name == 'purchase') {
                totalElements = 'totalNum';
            }
            dataType = 'json';
        }

        if(name == 'goods') {
            ajax.type = 'post';
        }
        seajs.use('table', function(table) {
            for(var key in urls) {
                data = {size: 10};
                if(!urls[key]
                    || typeof urls[key] != 'object'
                    || urls[key].state) {
                    continue;
                }
                if(key == 'goods') {
                    data.tag = $('.goods-chart .current-sub-tab').data('value');
                    pageType = 'new';
                }

                if(key == 'stock2') {
                    callback = function () {
                        window.Components.freezeTable($('#stock2Table'))
                    }
                }

                urls[key].state = true;
                // 保存该表格的刷新函数
                urls[key].send = table.init({
                    url: urls[key].url,
                    pageid: key + 'Page',
                    fillid: key + 'Fill',
                    tplid:  key + 'Tpl',
                    formid: 'searchForm',
                    relativeForm: key + 'Form',
                    pageTplId: 'pageTpl',
                    enabledHash: false,
                    pageType: pageType,
                    data: data,
                    ajax: ajax,
                    totalElements: totalElements,
                    dataType: dataType,
                    callback: callback
                })
            }
        });
    }

    function getTextData(request, data, desc) {
        var url = request.url;

        if(typeof url === 'string') {
            url = [url];
        }
        for(var i in url) {
            $.ajax({
                url: url[i],
                data: data,
                success: function (json) {
                    if (json && json.success) {
                        request.state = true;

                        // 库存统计的总计部分
                        if (desc === 'stock' && json.data) {
                            if(typeof json.data == 'object') {
                                $('.stock-total').removeClass('hide');
                            } else if(typeof json.data == 'number') {
                                $('#stockTotal').text( toPrice(json.data) );
                            }
                        }
                        setTextData(json.data);
                    } else {
                        seajs.use('dialog', function (dialog) {
                            dialog.fail(json.errorMsg)
                        });
                    }
                }
            });
        }
    }

    function setTextData(data, afterfix) {
        var t, str;
        for(var i in data) {
            t = afterfix != null ? i + afterfix : i;
            str = data[i];

            if(t == 'ranking') {
                if(data[i] == 0) {
                    str = '> 1000 加油!';
                } else {
                    str = '第' + data[i] + '名';
                }
            }

            if(i != 'receivedCarNumber'
                && i != 'receptionNumber'
                && i != 'newCarNumber') {
                if ($.isNumeric(str)) {
                    str = toPrice(str);
                }
            }

            $('#' + t).text( str );
        }
    }

    function convertData(data, keys, sort, type) {
        var obj = {
            name: [],
            value: []
        };
        var count = 1, t;

        if(!data || data.length == 0) {
            return {};
        }
        if(typeof keys.value === 'string') {
            if (sort) {
                data.sort(function (a, b) {
                    return a[keys.value] - b[keys.value];
                })
            }
            if (type != 'line') {
                // 过滤掉0的数据
                data = data.filter(function (e) {
                    return e[keys.value] != 0;
                });
            }

            data.forEach(function (e) {
                obj.name.push(e[ keys.name ]);
                obj.value.push(e[ keys.value ])
            });
        }

        if(keys.seriesName
            && typeof keys.value == 'object'
            && keys.value.length == keys.seriesName.length) {
            obj.value = {};
            data.forEach(function (e) {
                obj.name.push(e[ keys.name ]);

                keys.seriesName.forEach(function (seriesName, i) {
                    t = (+e[ keys.value[i] ]).toFixed(2);
                    if(!obj.value[seriesName]) {
                        obj.value[seriesName] = [t];
                    } else {
                        obj.value[seriesName].push(t);
                    }
                    count ++;
                });
            });
            // 为了通过 hasData
            obj.value.length = count;

            obj.seriesMulti = true;
        }

        return obj;
    }

    function convertPieName(data, name) {
        if(data.value.length == 0) {
            return data.name;
        }
        var total = data.value.reduce(function (a, b) {
            return a + b;
        });

        var percent = data.value.map(function (e) {
            return ((e / total).toFixed(4) * 100).toFixed(2) + '%';
        });

        return data.name.map(function (e, i) {
            var t = e;
            if(e && e.length > 6 && name == 'purchase') {
                t = e.slice(0, 6) + '\n' + e.slice(6);
            }
            return t + '\n' + percent[i];
        })
    }

    // 查询时重置请求
    // 重置所有的状态
    // 当前tab的重新查询
    function resetQuery(e) {
        var j, i;
        var desc = $('.tab-item.current-item').data('desc');
        var date = new Date( $('.date').val() );
        var now = new Date();
        var flag = true;

        if(desc == 'stock') {
            seajs.use('dialog', function (dialog) {
                if (date.getMonth() <= 8 && date.getFullYear() <= 2016) {
                    dialog.warn('请选择2016年9月份之后的日期');
                    flag = false;
                } else if (now.getMonth() - date.getMonth() > 2
                    || date.getFullYear() != now.getFullYear()
                ) {
                    dialog.warn('请选择最近两个月的时间');
                    flag = false;
                }
            });
        }

        if(flag) {
            // 图标的url状态重置
            for (i in chartsDataSetting.url) {
                for (j in chartsDataSetting.url[i]) {
                    chartsDataSetting.url[i][j].state = false;
                }
            }

            // 表格的状态重置
            for (i in tableUrl) {
                if (tableUrl[i].url) {
                    tableUrl[i].state = false;
                    continue;
                }
                for (j in tableUrl[i]) {
                    tableUrl[i][j].state = false;
                }
            }

            // 更新链接的参数
            updateLink();
            // 重置当前 tab
            resetCurrent();
        }
        // 防止冒泡
        e.stopPropagation();
        e.stopImmediatePropagation();
    }

    function resetCurrent() {
        $('.tab-box .current-item').click();
    }

    function resetStatus() {
        // 营业趋势 年/月
        $('.current-trend').removeClass('current-trend');
        $('.js-trend-select[data-target="month"]').addClass('current-trend');
        operateTrend = 'month';
    }

    // 更新对应的日期数据
    function updateDateText(dateStr) {
        var date = new Date(dateStr);
        var m = date.getMonth() + 1;
        for(var i in dateSelectors) {
            $( dateSelectors[i] ).text( m + '月' )
        }
    }

    // 2015-01-02 to 2015.01.02
    function convertTime(timeStr) {
        var a = timeStr.split('-');

        return a.map(function (e) {
            if(e < 10) {
                return '0' + (+e);
            } else
                return e
        }).join('.');
    }

    // 更新链接带的参数
    function updateLink() {
        $('.js-link').each(function () {
            var name = $(this).data('target');
            var href = links[name].url;
            var paramStr = $.param( setParamTimeStr(links[name].param) );

            $(this).attr('href', href + paramStr);
        })
    }

    function setParamTimeStr(param) {
        var t = param.time, timeStr = $('.date').val();
        var time = new Date( $('.date').val() ), i;
        var start = timeStr + '-01', end;
        var re = $.extend(true, {}, param), day;

        if( isThisMonth(time) ) {
            day = new Date().getDate();
            if(day < 10) {
                day = '0' + day;
            }
            end = timeStr + '-' + day;
        } else {
            // 获取 time 当月的最后一天
            i = time;
            i.setMonth( i.getMonth() + 1);
            i.setDate(0);
            day = i.getDate();


            end = timeStr + '-' + day;
        }
        re[ t[0] ] = start;
        re[ t[1] ] = end;

        delete re.time;

        return re;
    }

    function isThisMonth(time) {
        var now = new Date();

        if(time.getFullYear() == now.getFullYear()) {
            if(time.getMonth() == now.getMonth()) {
                return true;
            }
        }

        return false;
    }

    // 判断是否有数据
    function hasData(data, name, type, index) {
        var ret = false;
        if(data && typeof data == 'object') {
            if(data.value && data.value.length > 0) {
                if(data.seriesMulti) {
                    return true;
                }
                ret = data.value.some(function (e) {
                    return e > 0;
                });
            }
        }

        // 无数据时显示
        showChartFallBack(name, !ret);
        return ret;
    }

    // 无数据时显示
    function showChartFallBack(name, show) {
        var ele = charts[name].getDom();
        var position;
        
        if(show) {
            position = noDataImgPosition[name];
            if(!position) {
                // 68, 92 为 .no-data 的宽高
                position = {
                    left: ele.clientWidth / 2 - 68 / 2,
                    top:  ele.clientHeight / 2 - 92 / 2
                }
            }

            if($(ele).find('.no-data').length == 0) {
                $(ele).append(noData.clone().css(position));
            }
        } else {
            $(ele).find('.no-data').remove();
        }
    }

    function chartsInit(selectors) {
        var ret = {};
        for(var i in selectors) {
            ret[i] = echarts.init( document.getElementById(selectors[i]) );
        }

        return ret;
    }

    // 设置 echarts 与旁边的表格高度保持一致
    function resizeEChartsWidthTable(key) {
        var table = $('#' + key + 'Fill').parent();
        var chart = charts[key];
        var h = table[0].clientHeight;

        if(!key || !chart) {
            return;
        }

        if(h < 128) {
            return;
        }

        h = h > 400 ? 400 : h;

        $( chart.getDom() ).css('height', h);
        chart.resize();
    }

    // 添加title和 y 坐标轴
    function loadyAxisAndTitle(option, title, interval) {
        option.yAxis.axisLine = {
            show: true
        };
        option.yAxis.axisLabel = {
            show: true
        };
        option.minInterval = interval;

        if(title) {
            option.title = {
                text: title,
                right: '35%',
                top: 0
            }
        }
    }
    function toPrice (price) {
        var reg = /\B(?=(\d{3})+(?!\d))/g;
        var t = price != null && !isNaN(price) ? (+price).toFixed(2) : '';

        t = t.toString().split('.');
        t[0] = t[0].replace(reg, ',');

        if (t[1]) {
            t[0] += '.' + t[1];
        }

        return t[0];
    }

    // 面漆总数
    function getTotalSurfaceNum(data) {
        if(isBPShare) {
            $.ajax({
                url: BASE_PATH + '/shop/report/business/month/surface_num',
                data: data,
                global: false,
                success: function (result) {
                    if (result.success) {
                        $('#totalSurfaceNum').text(result.data);
                    }
                }
            });
        }
    }

    // 钣喷销售额合计
    function getTotalSpraySaleAmount(data) {
        if(isBPShare) {
            $.ajax({
                url: BASE_PATH + '/shop/report/business/month/spray_sale_amount',
                data: data,
                global: false,
                success: function (result) {
                    if (result.success) {
                        $('#totalSpraySaleAmount').text(result.data);
                    }
                }
            });
        }
    }
    /*--------------------- 函数 end --------------------------*/
});
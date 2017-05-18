/**
 * Created by sky on 16/4/19.
 */
$(function() {
    // 服务销量趋势图
    tendencyCharts({
        domId: 'saleTendencyChart',
        xAxisData: ['3-1', '3-2', '3-3', '3-4', '3-5', '3-6', '3-7', '3-8', '3-9', '3-10', '3-11', '3-12', '3-13', '3-14'],
        data: [100, 200, 150, 100, 150, 200, 200, 180, 160, 170, 20, 150, 100, 250],
        name: '营业额（元）'
    });

    barCharts({
        domId: 'saleRatioChart',
        xAxisData: ['保养', '洗车', '检修', '钣喷', '救援', '其他'],
        data: [2300, 30, 100, 1700, 1500, 2400, 15]
    });

    pieCharts({
        domId: 'timesRatioChart',
        pies: ['保养', '洗车', '美容', '检修', '钣喷', '救援', '其他'],
        data: [
            {value: 18, name: '保养'},
            {value: 12, name: '洗车'},
            {value: 6, name: '美容'},
            {value: 20, name: '检修'},
            {value: 40, name: '钣喷'},
            {value: 0, name: '救援'},
            {value: 4, name: '其他'}
        ]
    });
});

/* 柱状图 */
function barCharts(options) {
    var chartInstance, option;

    chartInstance = echarts.init( document.getElementById(options.domId) );
    option = {
        tooltip: {
            trigger: 'item'
        },
        grid: {
            borderWidth: 0,
            x: 0,
            y: 80,
            x2:0,
            y2: 40
        },
        xAxis: [
            {
                type: 'category',
                axisLabel: {
                    textStyle: {
                        color: '#333',
                        fontSize: '12px'
                    }
                },
                axisLine: {
                    lineStyle: {
                        color: '#ddd',
                        width: 5
                    }
                },
                axisTick: {
                    show: false
                },
                splitLine: {
                    show: false
                },
                data: options.xAxisData
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLine: {
                    show: false
                },
                splitLine: {
                    lineStyle: {
                        color: '#ddd'
                    }
                }
            }
        ],
        series: [
            {
                name: options.name || '',
                type: 'bar',
                barWidth: 25,
                itemStyle: {
                    normal: {
                        color: '#9fc527',
                        label: {
                            show: true,
                            formatter: '{c}',
                            textStyle: {
                                color: '#333'
                            }
                        }
                    }
                },
                data: options.data
            }
        ]
    };

    chartInstance.setOption(option);
    return chartInstance;
}

/* 饼图 */
function pieCharts(options) {
    var chartInstance, option, i, pies = [];

    chartInstance = echarts.init( document.getElementById(options.domId) );
    for(i = 0; i < options.pies.length; i++) {
        pies.push({
            icon: 'bar',
            name: options.pies[i]
        });
    }
    option = {
        legend: {
            orient: 'vertical',
            x: 'right',
            y: 'center',
            itemWidth: 15,
            itemHeight: 15,
            data: pies
        },
        tooltip: {
            trigger: 'item'
        },
        color: ['#ffb637', '#fe7545', '#4df2fc', '#fded40', '#9fc527', '#fb3444', '#cacac9'],
        series: [
            {
                name: options.name || '',
                type: 'pie',
                radius: '55%',
                center: ['42%', '50%'],
                itemStyle: {
                    normal: {
                        label: {
                            formatter: function(data) {
                                return Number(data.percent) + '%';
                            },
                            textStyle: {
                                color: '#333'
                            }
                        },
                        labelLine: {
                            length: 1,
                            lineStyle: {
                                color: '#ddd'
                            }
                        }
                    }
                },
                data: options.data
            }
        ]
    };

    chartInstance.setOption(option);
    return chartInstance;
}
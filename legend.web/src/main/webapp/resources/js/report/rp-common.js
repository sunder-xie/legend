/**
 * Created by sky on 16/4/15.
 */

template.helper('thousandSep', function(price) {
    var _result = '', _int, _dec;
    if( !isNaN(price) && (price = price.toString()).length > 3 ) {
        price = price.split('.');
        _int = price[0];
        _dec = price[1] ? '.' + price[1] : '';
        while (_int.length > 3) {
            _result = ',' + _int.slice(-3) + _result;
            _int = _int.slice(0, _int.length - 3);
        }
        return _int + _result + _dec;
    }
    return price;
});


/* 趋势图 start */
function tendencyCharts(options) {
    /* 使用插件echarts 趋势图  */
    var chartInstance, axis, option;

    chartInstance = echarts.init( document.getElementById(options.domId) );
    axis = {
        axisLabel: {
            textStyle: {
                color: '#333',
                fontSize: '12px'
            }
        },
        axisLine: {
            lineStyle: {
                color: '#ddd',
                width: 1,
                type: 'solid'
            }
        },
        axisTick: {
            show: false
        },
        splitLine: {
            show: false
        }
    };
    option = {
        legend: {
            data: [options.name],
            setColor: '#666'
        },
        tooltip: {
            trigger: 'item'
        },
        grid: {
            x: 35,
            y: 45,
            x2: 0,
            y2: 40,
            borderWidth: 0
        },
        xAxis: [
            (function () {
                return $.extend({}, axis, {
                    type: 'category',   // 默认
                    data: options.xAxisData
                });
            }())
        ],
        yAxis: [
            (function () {
                return $.extend({}, axis, {
                    type: 'value'   // 默认
                });
            }())
        ],
        series: [
            {
                name: options.name,
                type: 'line',
                data: options.data,
                symbol: 'circle',
                symbolSize: 5,
                itemStyle: {
                    normal: {
                        width: 2,
                        color: '#9fc527',
                        lineStyle: {
                            color: '#ddd',
                            width: 1
                        }
                    },
                    emphasis: {
                        label: {show: true}
                    }
                }
            }
        ]
    };
    chartInstance.setOption(option);
    return chartInstance;
}
/* 趋势图 end */

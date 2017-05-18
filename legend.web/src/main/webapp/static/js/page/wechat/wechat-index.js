/**
 *
 * Created by ende on 16/6/16.
 */
$(function () {
    var data = [
        {
            val: [
                $('input[name="target_user"]').val(),
                $('input[name="ori_page_read_user"]').val()
            ],
            text: [
                '文章送达数',
                '文章阅读数'
            ]
        },
        {
            val: [
                $('input[name="share_count"]').val(),
                $('input[name="add_to_fav_count"]').val()
            ],
            text: [
                '文章转发次数',
                '文章收藏次数'
            ]
        }
    ];

    var interval = [];

    var myChart = [
        echarts.init(document.getElementById('echart-1')),
        echarts.init(document.getElementById('echart-2'))
    ];

    // 指定图表的配置项和数据
    var option = [{
        title: {
            show: true,
            text: '单位:人',
            top: '0',
            left: '22%',
            textStyle: {
                fontSize: '12px',
                color: '#999'
            }
        },
        grid: {
            left: '15%',
            right: '15%',
            bottom: '5%',
            top: '19%',
            containLabel: true
        },
        xAxis: {
            data: data[0].text,
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
            }
        },
        series: [{
            type: 'bar',
            data: [{
                value: data[0].val[0],
                itemStyle: {normal: {
                    color: '#c96462'
                }},
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
                value: data[0].val[1],
                itemStyle: {
                    normal: {
                        color: '#f48331'
                    }
                },
                label: {
                    normal : {
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
    }, {
        title: {
            text: '单位:次',
            top: '0',
            left: '22%',
            textStyle: {
                fontSize: '12px',
                color: '#999'
            }
        },
        grid: {
            left: '15%',
            right: '15%',
            bottom: '5%',
            top: '19%',
            containLabel: true
        },
        xAxis: {
            data: data[1].text,
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
            }
        },
        series: [{
            type: 'bar',
            data: [{
                value: data[1].val[0],
                itemStyle: {normal: {
                    color: '#4bc8f4'
                }},
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
                value: data[1].val[1],
                itemStyle: {normal: {
                    color: '#4bf4da'
                }},
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        textStyle: {
                            color: '#333'
                        }
                    }
                }
            }
            ],
            barWidth: '35'
        }]
    }];

    // 使用刚指定的配置项和数据显示图表。
    myChart[0].setOption(option[0]);
    myChart[1].setOption(option[1]);
});

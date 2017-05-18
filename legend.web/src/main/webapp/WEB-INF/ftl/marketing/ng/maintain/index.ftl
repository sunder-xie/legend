<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/marketing/maintain/index.css?1a840db2ef07f3c8d8712b921c4e1be7"/>
<div class="wrapper">
    <#include "marketing/ng/left_nav.ftl"/>
    <div class="Z-right">
        <h3 class="Z-title">客户营销  >  <a href="${BASE_PATH}/marketing/ng/maintain/center">提醒中心</a> > <i>提醒效果</i></h3>
        <input type="hidden" id="isYBD" value="${YBD}">
        <ul class="Z-maintain">
            <li class="increase-total"><i></i><span class="increase-total-title">累计增收</span><span class="increase-total-num">${customerMaintainDTO.totalAddedAmount?default(0)}<em>元</em></span></li>
            <li class="remind-total"><i></i><span class="remind-total-title">累计处理提醒数</span><span class="remind-total-num">${customerMaintainDTO.noteHandledCount?default(0)}<em>条</em></span></li>
            <li class="active-total"><i></i><span class="active-total-title">激活客户总数</span><span class="active-total-num">${customerMaintainDTO.activatedCustomerCount?default(0)}<em>位</em></span></li>
        </ul>
        <div class="Z-maintain-report">
            <ul class="Z-type-legend">
                <li class="remind-num"><span></span>&nbsp;处理提醒数(条)</li>
                <li class="increase-num"><span></span>&nbsp;增收(元)</li>
                <li class="active-num"><span></span>&nbsp;激活客户(位)</li>
            </ul>
            <ul class="Z-report-list">
                <li id="report-1" class="report-1"></li>
                <i class="no-data-1"></i>
                <i class="Z-maintain-tag1"></i>
                <li id="report-2" class="report-2"></li>
                <i class="no-data-2"></i>
                <i class="Z-maintain-tag2"></i>
                <li id="report-3" class="report-3"></li>
                <i class="no-data-3"></i>
                <i class="Z-maintain-tag3"></i>
                <div class="report-month">
                    <span>${last_month}月份</span>
                    <span>${next_month}月份</span>
                    <span>${current_month}月份</span>
                </div>
            </ul>
            <div class="Z-maintain-notice"><i></i><span>&nbsp;提醒处理后30天内到店的视为有效激活客户，其消费金额视为增收</span></div>
        </div>

        <div class="analysis_box">
            <div class="Detail_datebox">
                <div class="date">
                    <div class="mb-10">
                        <input class="param" id="startTime" name="sTime" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}'})" type="text"/> -
                        <input class="param" id="endTime" name="eTime"  onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}'})" type="text"/>
                    </div>
                </div>

                <div class="date_sel">
                    <a data-format="Y" href="javascript:;">今年</a>、
                    <a data-format="LM" href="javascript:;">上月</a>、
                    <a data-format="M" href="javascript:;">本月</a>、
                    <a href="javascript:;">全部</a>
                </div>
                <input class="date_btn" type="button" id="searchBtn" value="查询">
            </div>
        </div>
        <div class="DetailTab">
            <span  class="hover js-tab" data-index="1">提醒类型</span>
            <span data-index="2" class="js-tab">提醒方式</span>
        </div>
        <div class="tableContent" id="tableContent">

        </div>
    </div>
</div>
<script type="text/html" id="remindTable">
    <table>
        <thead>
        <tr class="tabcon_title">
            <th>提醒类型</th>
            <th>提醒数量</th>
            <th>到店客户数</th>
            <th>消费总数</th>
            <th>转化率</th>
        </tr>
        </thead>
        <% if (success) { %>
        <tbody>
        <% if (data) { %>
        <% for (var i = 0; i < data.length; i++) { %>
        <% var item = data[i]; %>
        <tr class="tabcon_list">
            <td><%= item.noteTypeStr %></td>
            <td><%= item.noteCount %></td>
            <td><%= item.customerCount %></td>
            <td><%= item.effectAmount %></td>
            <td><%= item.takeRates %>%</td>
        </tr>
        <%}}%>
        </tbody>
    </table>
    <%}%>
</script>

<script id="tableTpl" type="text/html">
    <table>
        <thead>
        <tr class="tabcon_title">
            <th>提醒方式</th>
            <th>提醒数量</th>
            <th>到店客户</th>
            <th>增收</th>
            <th>转化率</th>
        </tr>
        </thead>

        <% if (success) { %>

        <tbody>
        <% if (data) { %>
        <% for (var i = 0; i < data.length; i++) { %>
        <% var item = data[i]; %>
        <tr class="tabcon_list">
            <td><%= item.noteWayStr %></td>
            <td><%= item.noteCount %></td>
            <td><%= item.customerCount %></td>
            <td><%= item.effectAmount %></td>
            <td><%= item.takeRates %>%</td>
        </tr>
        <% }} %>
        </tbody>

        <% } %>
    </table>
</script>

<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/echarts/echarts.js?f3da14a808d4e3fdfd3988e0e6de055f"></script>
<script>
    jQuery(function(){
        var $doc = $(document);
        var total1 = ${last_month_data.customer} + ${last_month_data.handleCount} + ${last_month_data.amount};
        var total2 = ${next_month_data.customer} + ${next_month_data.handleCount} + ${next_month_data.amount};
        var total3 = ${current_month_data.customer} + ${current_month_data.handleCount} + ${current_month_data.amount};
        var tabIndex = 1, tpl, searchData;
        //报表部分
        require.config({
            paths: {
                echarts: BASE_PATH + '/resources/js/echarts'
            }
        });
        require(
                [
                    'echarts',
                    'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
                    'echarts/chart/pie',
                    'echarts/chart/funnel',
                    'echarts/chart/line'
                ],
                function (ec) {
                    // 基于准备好的dom，初始化echarts图表
                    var myCharts1 = ec.init(document.getElementById('report-1'));
                    var myCharts2 = ec.init(document.getElementById('report-2'));
                    var myCharts3 = ec.init(document.getElementById('report-3'));
                    var labelLeft = {normal: {label : {position: 'left'}}};
                    if(total1){
                        option1 = {
                            backgroundColor:'#f9f9f9',
                            xAxis: [
                                {
                                    show: false,
                                    type: 'value',
                                }
                            ],
                            grid:{
                                x:50,
                                y:10,
                                x2:20,
                                y2:10,
                                borderWidth:0,
                                width:130,
                                height:90
                            },
                            yAxis: [
                                {
                                    type: 'category',
                                    axisLine: {show: true, lineStyle: {color: '#ddd'}},
                                    axisLabel: {show: false},
                                    axisTick: {show: false},
                                    splitLine: {show: false},
                                    data: [${last_month_data.customer}, ${last_month_data.handleCount}, ${last_month_data.amount}]
                                }
                            ],
                            series: [
                                {
                                    name: '生活费',
                                    type: 'bar',
                                    stack: '总量',
                                    itemStyle: {
                                        normal: {
                                            color: function (params) {
                                                // build a color map as your need.
                                                var colorList = [
                                                    '#4bc8f4 ', '#fad4ac ', '#d7e4ae '
                                                ];
                                                return colorList[params.dataIndex]
                                            },
                                            barBorderRadius: 0,
                                            label: {
                                                show: true,
                                                formatter: '{b}'
                                            }
                                        }
                                    },
                                    barCategoryGap: '20%',
                                    data: [
                                    ${last_month_data.customer},
                                        {value: ${last_month_data.handleCount}, itemStyle: labelLeft},
                                    ${last_month_data.amount}
                                    ]
                                }
                            ]
                        };
                        myCharts1.setOption(option1);
                    }else{
                        $(".no-data-1").show();
                    }
                    if(total2){
                        option2 = {
                            backgroundColor:'#f9f9f9',
                            xAxis: [
                                {
                                    show: false,
                                    type: 'value',
                                }
                            ],
                            grid:{
                                x:50,
                                y:10,
                                x2:20,
                                y2:10,
                                borderWidth:0,
                                width:130,
                                height:90
                            },
                            yAxis: [
                                {
                                    type: 'category',
                                    axisLine: {show: true, lineStyle: {color: '#ddd'}},
                                    axisLabel: {show: false},
                                    axisTick: {show: false},
                                    splitLine: {show: false},
                                    data: [${next_month_data.customer}, ${next_month_data.handleCount}, ${next_month_data.amount}]
                                }
                            ],
                            series: [
                                {
                                    name: '生活费',
                                    type: 'bar',
                                    stack: '总量',
                                    itemStyle: {
                                        normal: {
                                            color: function (params) {
                                                // build a color map as your need.
                                                var colorList = [
                                                    '#4bc8f4 ', '#fad4ac ', '#d7e4ae '
                                                ];
                                                return colorList[params.dataIndex]
                                            },
                                            barBorderRadius: 0,
                                            label: {
                                                show: true,
                                                formatter: '{b}'
                                            }
                                        }
                                    },
                                    barCategoryGap: '20%',
                                    data: [
                                    ${next_month_data.customer},
                                        {value: ${next_month_data.handleCount}, itemStyle: labelLeft},
                                    ${next_month_data.amount}
                                    ]
                                }
                            ]
                        };
                        myCharts2.setOption(option2);
                    }else{
                        $(".no-data-2").show();
                    }
                    if(total3){

                        option3 = {
                            backgroundColor:'#f9f9f9',
                            xAxis: [
                                {
                                    show: false,
                                    type: 'value',
                                }
                            ],
                            grid:{
                                x:50,
                                y:10,
                                x2:20,
                                y2:10,
                                borderWidth:0,
                                width:130,
                                height:90
                            },
                            yAxis: [
                                {
                                    type: 'category',
                                    axisLine: {show: true, lineStyle: {color: '#ddd'}},
                                    axisLabel: {show: false},
                                    axisTick: {show: false},
                                    splitLine: {show: false},
                                    data: [${current_month_data.customer}, ${current_month_data.handleCount}, ${current_month_data.amount}]
                                }
                            ],
                            series: [
                                {
                                    name: '生活费',
                                    type: 'bar',
                                    stack: '总量',
                                    itemStyle: {
                                        normal: {
                                            color: function (params) {
                                                // build a color map as your need.
                                                var colorList = [
                                                    '#4bc8f4 ', '#fad4ac ', '#d7e4ae '
                                                ];
                                                return colorList[params.dataIndex]
                                            },
                                            barBorderRadius: 0,
                                            label: {
                                                show: true,
                                                formatter: '{b}'
                                            }
                                        }
                                    },
                                    barCategoryGap: '20%',
                                    data: [
                                    ${current_month_data.customer},
                                        {value: ${current_month_data.handleCount}, itemStyle: labelLeft},
                                    ${current_month_data.amount}
                                    ]
                                }
                            ]
                        };
                        myCharts3.setOption(option3);
                    }else{
                        $(".no-data-3").show();
                    }
                    window.onresize = myCharts1.resize;
                    window.onresize = myCharts2.resize;
                    window.onresize = myCharts3.resize;
                }
        );
        //当月客情类型汇总
        var appointment = ${customerMaintainDTO.appointNoteUnhandledCount?default(0)};
        var maintain = ${customerMaintainDTO.keepupNoteUnhandledCount?default(0)};
        var insurance = ${customerMaintainDTO.insuranceNoteUnhandledCount?default(0)};
        var inspection = ${customerMaintainDTO.inspectionNoteUnhandledCount?default(0)};
        var birthday = ${customerMaintainDTO.birthdayNoteUnhandledCount?default(0)};
        var visit = ${customerMaintainDTO.visitNoteUnhandledCount?default(0)};
        var lost = ${customerMaintainDTO.customerLostNoteUnhandledCount?default(0)};
        var appointmentPercent = $(".appointment i");
        var maintainPercent = $(".maintain-expire i");
        var insurancePercent = $(".insurance-expire i");
        var inspectionPercent = $(".yearly-inspection-expire i");
        var birthdayPercent = $(".birthday i");
        var visitPercent = $(".visit i");
        var lostPercent = $(".lost i");
        //是否样板店
        var isYBD = $('#isYBD').val() == 'true';
        var baseUrl = isYBD ? BASE_PATH + '/marketing/gather/plan?' : BASE_PATH + '/marketing/ng/maintain/center?';

        var total = appointment + maintain + insurance + inspection + birthday + visit + lost;
        appointmentPercent.css("width",appointment/total*$(".appointment-percent").width());
        maintainPercent.css("width",maintain/total*$(".maintain-percent").width());
        insurancePercent.css("width",insurance/total*$(".insurance-percent").width());
        inspectionPercent.css("width",inspection/total*$(".inspection-percent").width());
        birthdayPercent.css("width",birthday/total*$(".birthday-percent").width());
        visitPercent.css("width",visit/total*$(".visit-percent").width());
        lostPercent.css("width",lost/total*$(".lost-percent").width());

        $('.js-remind-link li').on('click', function () {
            var tag = $(this).data('tag');
            var t = tag === 'appointment' && isYBD ?  BASE_PATH + '/shop/appoint/appoint-list?' : baseUrl;

            location.href = t + 'tag=' + tag
                + '&tab=0' + '&refer=maintain';
        });



        var $document = $(document),
                loadId;

        /**** ?Ajax全局设置（该设置可以通过 global: false 取消） ****/

        $document.ajaxStart(function() {
            loadId = taoqi.loading();
        }).ajaxStop(function() {
            taoqi.close(loadId);
            loadId = undefined;
        });

        /**** ?Model模块 ****/

        var Model = {
            // 查询数据接口
            search: function(startTime, endTime, success) {
                var url = "";
                // 选择接口
                switch (tabIndex) {
                    // 工作记录
                    case 1: url = BASE_PATH + '/marketing/ng/maintain/type_analysis/list';break;
                    // 收益记录
                    case 2: url = BASE_PATH + '/marketing/ng/maintain/effect/list';break;
                }


                $.ajax({
                    url: url,
                    data: {
                        search_sTime: startTime,
                        search_eTime: endTime
                    },
                    dataType: "json",
                    success: function(result) {
                        success && success(result);
                    }
                });
            }
        };

        /*
         * 格式化时间
         * @param $('.date_sel a').data("format");
         * return ['startTime', 'endTime']
         */
        function timeFormat() {
            var $this = $(this),
                    format = $this.data("format"),
                    date = new Date(), _date, time = [], last, next;

            switch (format) {
                // 今年
                case "Y":
                    last = date.getFullYear() + "-1-1";
                    next = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                    break;
                // 本月
                case "M":
                    last = date.getFullYear() + "-" + (date.getMonth() + 1) + "-1";
                    next = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                    break;
                // 上月
                case "LM":
                    _date = new Date(date.setDate(0));  // 这个月的第0天即为上个月的最后一天
                    last = _date.getFullYear() + "-" + (date.getMonth() + 1) + "-1";
                    next = _date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + _date.getDate();
                    break;
                // 所有
                default:
                    last = next = "";
            }

            time.push(last, next);
            return time;
        }


        // 渲染页面
        function renderContent(startTime, endTime) {
            Model.search(startTime, endTime, function(result) {
                var html;
                if(result.success) {
                    html = template(tpl, result);
                    $('#tableContent').html(html);
                } else {
                    layer.msg(result.errorMsg);
                }
            });
        }


        // tab切换
        $doc.on('click', '.DetailTab span', function() {
            var $this = $(this);
            $this.addClass("hover").siblings().removeClass("hover");
            // 设置tabIndex类型(切换tab时，调用不同接口有用)
            tabIndex = $this.data('index');
            // 清空数据
            $('#tableContent').html("");
            $('#searchBtn').trigger('click');
        });

        $document
            // 查询列表
                .on('click', '#searchBtn', function() {
                    var startTime = $('#startTime').val(),
                            endTime = $('#endTime').val();

                    // 选择模板
                    switch (tabIndex) {
                        case 1: tpl = 'remindTable';break;
                        case 2: tpl = 'tableTpl';break;
                    }

                    renderContent(startTime, endTime);
                })

            // 查询tag -- 查询数据(今年、上月、本月、所有)
                .on('click', '.date_sel a', function() {
                    var $this = $(this), time;

                    $this.addClass("hover").siblings().removeClass("hover");
                    time = timeFormat.apply(this);
                    $('#startTime').val(time[0]);
                    $('#endTime').val(time[1]);

                    $('#searchBtn').trigger('click');
                })

        $('#searchBtn').trigger('click');
    })

</script>

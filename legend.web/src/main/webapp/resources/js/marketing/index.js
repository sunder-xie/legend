/**
 * Created by zqq on 16/3/1.
 */
jQuery(function(){
    var $doc = $(document),
        $numPercent = $('#num-percent'),
        $PurchasePercent = $("#purchase-percent"),
        $numWashed = $("#num-washed");

    if(localStorage.guideDisplay === 'false') {
        hideProcess();
    } else {
        $('.guide-box').show();
        $('.js-guide').hide();
        $('.wrapper').css('margin-top','10px');
    }

    if( $numPercent.data("low") || $numPercent.data("middle") || $numPercent.data("high") ) {
        //报表
        require.config({
            paths: {
                echarts: BASE_PATH+'/resources/js/echarts'
            }
        });

        // 使用
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
                var myCharts1 = ec.init(document.getElementById('num-percent'));
                var option1 = {
                    title: {
                        text: '数量比例',
                        x: 'center',
                        y: 'center',
                        textStyle: {
                            fontSize: 12,
                            fontFamily: 'MicrosoftYahei'
                        }
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)",
                        showDelay: 200
                    },
                    series: [
                        {
                            name: '数量比例',
                            type: 'pie',
                            radius: ['38%', '48%'],
                            itemStyle:{
                                normal:{
                                    label:{
                                        formatter:"{b}:{d}%"
                                    },
                                    labelLine: {
                                        show: true,
                                        length: 1
                                    }
                                }
                            },
                            data: [
                                {
                                    value: $numPercent.data("low"),
                                    name: '低端',
                                    itemStyle: {normal: {color: '#5e9cd3',label:{textStyle:{color:'#5e9cd3'}},labelLine:{lineStyle:{color:'#5e9cd3'}}}}
                                },
                                {
                                    value: $numPercent.data("middle"),
                                    name: '中端',
                                    itemStyle: {normal: {color: '#fea23f',label:{textStyle:{color:'#fea23f'}},labelLine:{lineStyle:{color:'#fea23f'}}}}
                                },
                                {
                                    value: $numPercent.data("high"),
                                    name: '高端',
                                    itemStyle: {normal: {color: '#9fc527',label:{textStyle:{color:'#9fc527'}},labelLine:{lineStyle:{color:'#9fc527'}}}}
                                }
                            ]
                        }
                    ]
                };

                var myCharts2 = ec.init(document.getElementById('purchase-percent'));
                var option2 = {
                    title: {
                        text: '消费比例',
                        x: 'center',
                        y: 'center',
                        textStyle: {
                            fontSize: 12,
                            fontFamily: 'MicrosoftYahei'
                        }
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)",
                        showDelay: 400
                    },
                    series: [
                        {
                            name: '消费比例',
                            type: 'pie',
                            radius: ['38%', '48%'],
                            itemStyle:{
                                normal:{
                                    label:{
                                        formatter:"{b}:{d}%"
                                    },
                                    labelLine: {
                                        show: true,
                                        length: 1
                                    }
                                }
                            },
                            data: [
                                {
                                    value: $PurchasePercent.data("low"),
                                    name: '低端',
                                    itemStyle: {normal: {color: '#5e9cd3',label:{textStyle:{color:'#5e9cd3'}},labelLine:{lineStyle:{color:'#5e9cd3'}}}}
                                },
                                {
                                    value: $PurchasePercent.data("middle"),
                                    name: '中端',
                                    itemStyle: {normal: {color: '#fea23f',label:{textStyle:{color:'#fea23f'}},labelLine:{lineStyle:{color:'#fea23f'}}}}
                                },
                                {
                                    value: $PurchasePercent.data("high"),
                                    name: '高端',
                                    itemStyle: {normal: {color: '#9fc527',label:{textStyle:{color:'#9fc527'}},labelLine:{lineStyle:{color:'#9fc527'}}}}
                                }
                            ]
                        }
                    ]
                };

                if( document.getElementById('num-washed') ){
                    // 基于准备好的dom，初始化echarts图表
                    var myCharts3 = ec.init(document.getElementById('num-washed'));
                    var option3 = {
                        title: {
                            text: '数量比例',
                            x: 'center',
                            y: 'center',
                            textStyle: {
                                fontSize: 12,
                                fontFamily: 'MicrosoftYahei'
                            }
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b}: {c} ({d}%)",
                            showDelay: 200
                        },
                        series: [
                            {
                                name: '数量比例',
                                type: 'pie',
                                radius: ['38%', '48%'],
                                itemStyle:{
                                    normal:{
                                        label:{
                                            formatter:"{b}:{d}%"
                                        },
                                        labelLine: {
                                            show: true,
                                            length: 1
                                        }
                                    }
                                },
                                data: [
                                    {
                                        value: $numWashed.data("low"),
                                        name: '低端',
                                        itemStyle: {normal: {color: '#5e9cd3',label:{textStyle:{color:'#5e9cd3'}},labelLine:{lineStyle:{color:'#5e9cd3'}}}}
                                    },
                                    {
                                        value: $numWashed.data("middle"),
                                        name: '中端',
                                        itemStyle: {normal: {color: '#fea23f',label:{textStyle:{color:'#fea23f'}},labelLine:{lineStyle:{color:'#fea23f'}}}}
                                    },
                                    {
                                        value: $numWashed.data("high"),
                                        name: '高端',
                                        itemStyle: {normal: {color: '#9fc527',label:{textStyle:{color:'#9fc527'}},labelLine:{lineStyle:{color:'#9fc527'}}}}
                                    }
                                ]
                            }
                        ]
                    };
                }


                myCharts1.setOption(option1);
                myCharts2.setOption(option2);
                if( document.getElementById('num-washed') ){
                    myCharts3.setOption(option3);
                }

                var ecConfig = require('echarts/config');

                function eConsole(param) {
                    if ('低端' == param.name) {
                        window.location.href = BASE_PATH + "/marketing/ng/analysis/level?tag=low";
                    }else if('中端' == param.name) {
                        window.location.href = BASE_PATH + "/marketing/ng/analysis/level?tag=middle";
                    }else if('高端' == param.name) {
                        window.location.href = BASE_PATH + "/marketing/ng/analysis/level?tag=high";
                    }
                }

                function chartsConsole(param) {
                    if ('低端' == param.name) {
                        window.location.href = BASE_PATH + "/marketing/ng/analysis/lost?tag=low";
                    }else if('中端' == param.name) {
                        window.location.href = BASE_PATH + "/marketing/ng/analysis/lost?tag=middle";
                    }else if('高端' == param.name) {
                        window.location.href = BASE_PATH + "/marketing/ng/analysis/lost?tag=high";
                    }
                }
                myCharts1.on(ecConfig.EVENT.CLICK, eConsole);
                myCharts2.on(ecConfig.EVENT.CLICK, eConsole);
                if( document.getElementById('num-washed') ) {
                    myCharts3.on(ecConfig.EVENT.CLICK, chartsConsole);
                }
            }
        );
    }else{
        $numPercent.hide();
        $PurchasePercent.hide();
        $(".custom-level-status").hide();
        $(".custom-level-detail").hide();
        $("#num-washed").hide();
        $(".num-washed-info").hide();
        $('.custom-level-analysis .no-data').show();
        $(".custom-level-no-data").show();
        $(".custom-level-import").show();
    }


    //计算比例
    function percent(p1,p2,p3,total,s1,s2,s3,obj){
        //debugger;
        if(p1||p2){
            var percent1 = p1/total * obj.height();
            var percent2 = p2/total * obj.height();
            s1.css('height',percent1);
            s2.css('height',percent2);
        }
        if(p3&&s3){
            var percent3 = p3/total * obj.height();
            s3.css('height',percent3);
        }
    }
    //新老客户数
    var customPercent = $('#custom-percent');
    var newer = customPercent.data('newer');
    var older = customPercent.data('older');
    var total = newer + older;
    if(total) {
        var newerPercent = (newer / total * 100).toFixed(2);
        var olderPercent = (older / total * 100).toFixed(2);
    }
        $(".newer-percent").text(newerPercent?newerPercent:0);
        $(".older-percent").text(olderPercent?olderPercent:0);
        var seletorNewer = $('#custom-percent-newer');
        var seletorOlder = $('#custom-percent-older');
        percent(newer,older,null,total,seletorNewer,seletorOlder,null,customPercent);
        $doc.on("click",'#custom-percent-newer',function(){
            window.location.href=BASE_PATH + "/marketing/ng/analysis/type?tag=newer"
        }).on("click",'#custom-percent-older',function(){
            window.location.href=BASE_PATH + "/marketing/ng/analysis/type?tag=older"
        });
        //计算客户活跃度比例
        var activityPercent = $('#custom-activity-percent');
        var activity = activityPercent.data('activity');
        var dormant = activityPercent.data('dormant');
        var loss = activityPercent.data('loss');
        var totalActivity = activity + dormant + loss;
        if(totalActivity){
            var activePercent = (activity/totalActivity*100).toFixed(2);
            var dormantPercent = (dormant/totalActivity*100).toFixed(2);
            var lossPercent = (loss/totalActivity*100).toFixed(2);
        }
    if(newerPercent < 25){
        if(lossPercent > 50){
            $(".custom-type-status").text("亲，您的新客户比例过低；流失客户比例过高！");
        }else{
            $(".custom-type-status").text("亲，您的新客户比例过低！");
        }
    }else if(olderPercent < 50){
        if(lossPercent > 50){
            $(".custom-type-status").text("亲，您的老客户比例过低；流失客户比例过高！");
        }else{
            $(".custom-type-status").text("亲，您的老客户比例过低！");
        }
    }else if(lossPercent > 50){
        $(".custom-type-status").text("亲，您的流失客户比例过高！");
    }else{
        $(".custom-type-status").text("亲，您的客户比例非常健康！");
    }
        $(".activity-percent").text(activePercent?activePercent:0);
        $(".dormant-percent").text(dormantPercent?dormantPercent:0);
        $(".loss-percent").text(lossPercent?lossPercent:0);
        var seletorActivity = $('#custom-percent-activity');
        var seletorDormant = $('#custom-percent-dormant');
        var seletorLoss = $('#custom-percent-loss');
        percent(activity,dormant,loss,totalActivity,seletorActivity,seletorDormant,seletorLoss,activityPercent);
        $doc.on("click",'#custom-percent-activity',function(){
            window.location.href=BASE_PATH + "/marketing/ng/analysis/type/activity?tag=activity";
        }).on("click",'#custom-percent-dormant',function(){
            window.location.href=BASE_PATH + "/marketing/ng/analysis/type/activity?tag=dormant";
        }).on("click",'#custom-percent-loss',function(){
            window.location.href=BASE_PATH + "/marketing/ng/analysis/type/activity?tag=loss";
        });
    if(!total&&!totalActivity){
        $(".custom-new-old").hide();
        $(".custom-activity-level").hide();
        $(".custom-type-status").hide();
        $(".custom-type-detail").hide();
        $(".custom-type-analysis .no-data").show();
        $(".custom-type-no-data").show();
        $(".custom-type-bill").show();
    }

    $('.js-pack').on('click', function() {
        var guideBox = $('.guide-box');
        if( guideBox.is(':visible') ) {
            guideBox.slideUp();
            hideProcess(true);
            // 保存状态
            localStorage.guideDisplay = 'false';
        } else {
            guideBox.slideDown();
            $('.js-guide').hide();
            $('.wrapper').css('margin-top','10px');
            localStorage.guideDisplay = 'true';
        }
    });

    function hideProcess(t) {
        var guideBox = $('.guide-box');
        if(guideBox.is(':hidden') && !t) {
            guideBox.hide();
        }
        $('.js-guide').show();
        $('.wrapper').css('margin-top','80px');
    }

    $(document).on('click','.js-guide',function(){
        $('.guide-box').slideDown(500);
        $('.wrapper').css('margin-top','10px');
        $(this).hide();
        localStorage.guideDisplay = 'true';
    });

});
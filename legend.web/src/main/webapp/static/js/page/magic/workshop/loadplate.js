/*
 *  weilu 2016/07/09
 *  负载看板设置
 */
$(function(){
    var doc = $(document),
        init_data = JSON.parse(window.localStorage.getItem('initData')),
        initTime = JSON.parse(window.localStorage.getItem('initTime')),
    //容错————变量提升
        init_start = initTime && initTime.openTime ? Math.floor(initTime.openTime.replace(":",".")) : 0,
        init_break_start = initTime && initTime.noonBreakStartTime ? initTime.noonBreakStartTime : 0,
        init_break_end = initTime && initTime.noonBreakEndTime ? initTime.noonBreakEndTime : 0,
        init_end = initTime && initTime.closeTime ? Math.ceil(initTime.closeTime.replace(":",".")) : 0,
        init_during = init_end - init_start + 1;
    //获取时间初始数据
    function init_time(){
        initTime = JSON.parse(window.localStorage.getItem('initTime')),
            init_start = initTime && initTime.openTime ? Math.floor(initTime.openTime.replace(":",".")) : 0,
            init_break_start = initTime && initTime.noonBreakStartTime ? initTime.noonBreakStartTime : 0,
            init_break_end = initTime && initTime.noonBreakEndTime ? initTime.noonBreakEndTime : 0,
            init_end = initTime && initTime.closeTime ? Math.ceil(initTime.closeTime.replace(":",".")) : 0,
            init_during = init_end - init_start + 1;
    }
    //初始化
    seajs.use(['art','dialog', 'ajax'],function(at, dg, ajax){
        /*
         * func: 时间戳转换为时间
         * date: 需要转换的时间
         * format: 需要转换成的格式
         * */
        at.helper('time',function(date, format){
            if(!date){
                return '--';
            }
            var date = new Date(date);
            var map = {
                "M": date.getMonth() + 1, //月份
                "d": date.getDate(), //日
                "h": date.getHours(), //小时
                "m": date.getMinutes(), //分
                "s": date.getSeconds(), //秒
                "q": Math.floor((date.getMonth() + 3) / 3), //季度
                "S": date.getMilliseconds() //毫秒
            };
            format = format.replace(/([yMdhmsqS])+/g, function(all, t){
                var v = map[t];
                if (v !== undefined) {
                    if (all.length > 1) {
                        v = '0' + v;
                        v = v.substr(v.length - 2);
                    }
                    return v;
                }
                else if (t === 'y') {
                    return (date.getFullYear() + '').substr(4 - all.length);
                }
                return all;
            });
            return format;
        });
        /*
         * func: 间隔时间间隔宽度
         * tl: 上次结束时间
         * ts: 本次开始时间
         * tw: 时间轴每小时宽度
         *   || 0 : 排错，后台无值返回null 计算出问题，莫问为0
         * 算法：
         *   1. tl <= today19 :    a. ts < today19  正常  b. today19 < ts < tomorrow8  0   c. ts >= tomorrow8    隔夜-13h
         *   2. today19 < tl < tomorrow8:    a. ts < 8  0     b.ts >= 8 隔夜-13h
         *   3. tl >= tomorrow8:    a.  第二天第一条 (通过ts - tl 的值做判断)  减第二天八点  b. 第二天后面的正常
         *
         *
         * ***************改版：******************
         * 大于一天  减一夜间隔
         * 小于一天  直接计算
         * */
        at.helper('gapTime',function(tl,ts,tw){
            tl = tl || 0;
            ts = ts || 0;
            tw = tw || 0;
            var today = new Date(),
            //tomorrow8 = new Date(today.getFullYear(),(today.getMonth()),(today.getDate() + 1),init_during,0,0).getTime(),
                today19 = new Date(today.getFullYear(),(today.getMonth()),(today.getDate()),init_end,0,0).getTime(),
                hour13 = (24 - init_during) * 3600000,
                hasNight = (ts - tl - hour13) * tw / 3600000,
                noNight = (ts - tl) * tw / 3600000,
            //tomorrowNoNight = (ts - tomorrow8) * tw / 3600000,
            //tomorrow0 = ts - tl >= hour13 ? 1 : 0,
            //gaptime = tl <= today19 ? ( ts < today19 ? noNight : ts >= today19 && ts < tomorrow8 ? 0 : hasNight) : (tl > today19 && tl < tomorrow8 ? (ts < tomorrow8 ? 0 : hasNight) : (tomorrow0 == 1 ? tomorrowNoNight : noNight));
                gaptime = ts >= today19 ? hasNight : noNight;
            return gaptime;
        });
        /*
         * func: 持续时间宽度
         * ts: 本次开始时间
         * te: 本次结束时间
         * tw: 时间轴每小时宽度
         * wkHour: 一天工时
         * gapHour: 一晚时间间隔
         * nightdurtime: 减去隔夜时间
         * */
        at.helper('duringTime',function(ts,te,tw){
            te = te || 0;
            ts = ts || 0;
            tw = tw || 0;
            var durtime = (te - ts) / 3600000,
                wkHour = init_during,
                gapHour = 24 - wkHour,
                nightdurtime = (te - ts) / 3600000 - gapHour;
            return durtime > gapHour ? nightdurtime * tw : durtime * tw;
        });
        /*
         * func: 获取最大时间  去除休息间隔
         * t1: 快修线结束时间
         * t2: 事故线结束时间
         * t3: 快喷线结束时间
         * wkHour: 一天工时
         * 13: 一晚时间间隔（19：00——8：00）
         * */
        function maxTime(t1,t2,t3){
            t1 = t1 || 0;
            t2 = t2 || 0;
            t3 = t3 || 0;
            var wkHour = init_during,
                gapHour = 24 - wkHour;
            var max = Math.max(t1,t2,t3),
                today = new Date(),
                start = new Date(today.getFullYear(),(today.getMonth()),today.getDate(),init_start,0,0).getTime(),
                gap = Math.round((max - start) / 3600000);
            return gap > wkHour ? (gap - wkHour - gapHour > 0 ? gap - gapHour : wkHour ) : gap;
        };
        /*
         * func: 获取上班相应各时间
         * item: 1. 每天工作小时数 2. 开始时间 3. 休息开始时间   4.休息结束时间    5.下班时间
         *
         * */

        at.helper('working',function(item){
            var ts = item == 1 ?init_during : item == 2 ? init_start : item == 3 ?
                init_break_start : item == 4 ?
                init_break_end : item == 5 ?
                init_end : '';
            return ts;

        });

        /*
         * func: 今天日期
         *
         * */
        at.helper('today',function(){
            var today = new Date();
            return today.getFullYear() +'-' + (today.getMonth() + 1) + '-' + today.getDate();
        });
        /*
         * func: 明天日期
         *
         * */
        at.helper('tomorrow',function(){
            var today = new Date(),
                tomorrow =  new Date(today.getFullYear(), (today.getMonth() + 1), (today.getDate() + 1));
            return tomorrow.getFullYear() +'-' + tomorrow.getMonth() + '-' + tomorrow.getDate();
        });
        /*
         * func: 车牌配色盘
         *
         * */
        at.helper('colorPlate',function(str){
            return str.charAt(str.length - 1);
        });


        /*
         * func: 填充页面
         *
         * */
        function init(result, status){
            var json = result.data,
                t1 = json && json.loadPlateVO[0] && json.loadPlateVO[0]['plateVOs'] && json.loadPlateVO[0]['plateVOs']['planTime'] ? json.loadPlateVO[0]['plateVOs']['planTime'] : 0,
                t2 = json && json.loadPlateVO[1] && json.loadPlateVO[1]['plateVOs'] && json.loadPlateVO[1]['plateVOs']['planTime'] ? json.loadPlateVO[1]['plateVOs']['planTime'] : 0,
                t3 = json && json.loadPlateVO[2] && json.loadPlateVO[2]['plateVOs'] && json.loadPlateVO[2]['plateVOs']['planTime'] ? json.loadPlateVO[2]['plateVOs']['planTime'] : 0,
                timeLine = parseInt( maxTime(t1, t2 ,t3), 10),
                $main = $(".main"),
                mainWidth = status ? ($main.width() - 15) : $main.width(),
                timeWidth,
                today = new Date(),
                eightOclock = new Date(today.getFullYear(),(today.getMonth()),today.getDate(),init_start,0,0).getTime(),
                timeHtml = at('timeTpl',{"timeLine": timeLine,"mainWidth": mainWidth});
            var wait = function(dtd){
                var dtd = $.Deferred();
                $main.html(timeHtml);
                timeWidth = $(".start").width();
                dtd.resolve();
                return dtd.promise();
            };
            $.when(wait()).done(function(){
                var html = at('mainTpl',{"json": result.data,"timeWidth": timeWidth, "eightOclock": eightOclock});
                $("#loadplate").html(html);
            });
        }
        /*
         * func: 获取本地数据
         *
         * */
        if(init_data){
            init(init_data, 1);
        }

        /*
         * func: 时间接口数据
         *
         * */

        var ajax1 = $.ajax({
                url: BASE_PATH + "/workshop/loadplate/worktime"
            }),
            ajax2 = $.ajax({
                url: BASE_PATH + "/workshop/loadplate/loadplate-load",
                data: {
                    type : 1
                }
            });
        $.when(ajax1,ajax2).done(function(rs1, rs2){
            if(rs1[0]){
                window.localStorage.setItem('initTime', JSON.stringify(rs1[0].data));
                init_time();
            }
            if(rs2[0] && rs2[0].data && rs2[0].data.loadPlateVO && rs2[0].data.loadPlateVO.length){
                window.localStorage.setItem('initData', JSON.stringify(rs2[0]));
                init(rs2[0]);
            }
        }).fail(function(){
            dg.fail('fial');
        });

        doc.on("click",".toggle",function(){
            var $t = $(".slide"),
                $s = $(".aside"),
                $p = $(".plate-con"),
                $pc = $(".plate-con").not(".plate-content"),
                $m = $(".main");
            if($t.hasClass("on") && $(this).hasClass("on")){
                $p.hide();
                $s.animate({"width": 30});
                $m.animate({"marginLeft": 30},function(){
                    init(init_data);
                });
                $t.removeClass("on");
            }else if(!$t.hasClass("on")){
                $s.animate({"width": 160},function(){
                    $pc.show();
                });
                $m.animate({"marginLeft": 160},function(){
                    init(init_data);
                });
                $t.addClass("on");
            }
        });
    });
});
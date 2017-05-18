$(function () {
    $("#createTimeStart").val(getDateStr(-7));
    $("#createTimeEnd").val(getDateStr(0));

    seajs.use(['ajax', 'dialog', 'table', 'select', 'date', 'art', 'formData'],
        function (ajax, dialog, table, select, dp, art, formData) {
            // 1: 正常； 2: 迟到；3:早退；4: 未打卡;
            art.helper('getSignStatus', function(signInStatus, signOutStatus) {
                var msg = [];

                if (signInStatus === 1 && signOutStatus === 1) {
                    msg.push('正常');
                } else if (signInStatus === 4 || signOutStatus === 4) {
                    msg.push('未打卡');
                } else {
                    if (signInStatus === 2) {
                        msg.push('迟到');
                    }
                    if(signOutStatus === 3) {
                        msg.push('早退');
                    }
                }
                return msg.join(',');
            });

            dp.dpStartEnd({
                startSettings: {
                    maxDate: '#F{$dp.$D(\'createTimeEnd\')}'
                },
                endSettings: {
                    minDate: '#F{$dp.$D(\'createTimeStart\')}'
                },
                start :'createTimeStart',
                end: 'createTimeEnd'
            });

            select.init({
                dom: '.js-staff',
                pleaseSelect: true,
                canInput: true,
                url: BASE_PATH + '/shop/stats/staff/attendance/getshopemployee'
            });

            table.init({
                url: BASE_PATH + '/shop/stats/staff/attendance/get_stats_amount/get',
                fillid: 'infoFill',
                tplid: 'infoTpl',
                pageid: 'infoPage',
                formid: 'searchForm'
            });

            // 密码登录导出
            exportSecurity.tip({'title':'导出报表信息'});
            exportSecurity.confirm({
                dom: '#excelBtn',
                title: '经营报表—员工考勤统计',
                callback: function(json){
                    var url = BASE_PATH + '/shop/stats/staff/attendance/get_stats_amount/export_ng?';
                    url += getParaFromForm();
                    window.location.href = url;
                }
            });


            $('.js-print-btn').click(function (e) {
                var url = BASE_PATH + '/shop/stats/staff/attendance/print_ng?';

                url += getParaFromForm();

                util.print(url);
                e.preventDefault();
            });

            function getParaFromForm() {
                var str = '';
                //过滤参数初始化
                var data = formData.get('.search-form');

                var time1 = new Date(data.search_startTime);
                var time2 = new Date(data.search_startTime);
                if (time1 == null) {
                    dialog.warn("开始时间不能为空");
                }
                if (time2 == null) {
                    dialog.warn("结束时间不能为空");
                }

                for(var i in data) {
                    str = str + '&' + i + '=' + data[i];
                }
                return str;
            }
        });

    function getDateStr(AddDayCount) {
        var dd = new Date();
        dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
        var y = dd.getFullYear();
        var m = dd.getMonth() + 1;//获取当前月份的日期
        var d = dd.getDate();
        m = m < 10 ? "0" + m : m;
        d = d < 10 ? "0" + d : d;
        return y + "-" + m + "-" + d;
    }


});
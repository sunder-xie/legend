/**
 * Created by zz on 2016/6/22.
 */
$(function(){
     Date.prototype.getFullMonth = function (offset) {
        offset = offset || 0;
        var month = this.getMonth() + 1 + offset;
        if(month < 10) {
            return '0' + month;
        }
        return month + '';
    };

    // 天数的偏移
    Date.prototype.getFullString = function (offset, monthOffset) {
        var date = new Date(this);
        if(monthOffset != null) {
            date.setMonth( date.getMonth() + monthOffset );
        }
        if(offset != null) {
            date.setDate( date.getDate() + offset);
        }

        return date.getFullYear() + '-' + date.getFullMonth() + '-'
            + date.getDate();
    };

    var startTime = $('#startDate').val();
    var endTime = $('#endDate').val();

    if (!(startTime && endTime)) {
        var todayStr = new Date().getFullString();

        $('#startDate').val(todayStr);
        $('#endDate').val(todayStr);

        startTime = todayStr;
        endTime = todayStr;
    }
    getNumberData({
        startTime: startTime,
        endTime: endTime
    });

    //表格模块初始化
    seajs.use(['table', 'date'],function(tb, date) {
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/account/member/rechargeFlow/list',
            //表格数据目标填充id，必需
            fillid: 'tableTest',
            //分页容器id，必需
            pageid: 'pagingTest',
            //表格模板id，必需
            tplid: 'tableTestTpl',
            //关联查询表单id，可选
            formid: 'formId',
            //渲染表格数据完后的回调方法,可选
            callback: function () {
            }
        });

        // 时间
        date.dpStartEnd({
            start: 'startDate',
            end: 'endDate',
            startSettings: {
                dateFmt: 'yyyy-MM-dd',
                maxDate: '#F{$dp.$D(\'endDate\')||\'%y-%M-%d\'}'
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd',
                minDate: '#F{$dp.$D(\'startDate\')}',
                maxDate: '%y-%M-%d'
            }
        });
    });

    //撤销
    $(document).on('click', '.js-revocation', function(){
        var id = $(this).data('id');
        var self = this;
        if($(this).hasClass('disabled')) {
            return;
        }
        seajs.use('dialog',function(dg){
            $.ajax({
                url: BASE_PATH + "/account/member/reverseRecharge",
                data: {
                    id: id
                },
                success: function (result) {
                    if (result.success) {
                        dg.success("撤销成功");
                        $(self).removeClass('blue').addClass('.disable');
                        location.reload();
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });

    })
    $('.js-date').on('click', function () {
        var offset = +$(this).data('offset') || 0;
        var offsetStr = $(this).data('offset');
        var target = $(this).data('target').split('|');
        var str;

        // 本周
        if(offsetStr === 'week') {
            offset = -new Date().getDay();
        }
        // 本月
        if(offsetStr === 'month') {
            offset = -new Date().getDate() + 1;
        }
        str = getDateStr(offset);

        target.forEach(function (e, i) {
            $(e).val(str[i]);
        });

        $('.js-search-btn').click();
    });

    seajs.use('formData', function (formData) {
        $('.js-search-btn').on('click', function () {
            var data = formData.get('.search-form');
            getNumberData(data);
        });
    });

    $(document).on('click', '.js-print', function () {
        util.print(BASE_PATH + '/account/member/rechargePrint?id=' + $(this).data('id'));
    })

    function getDateStr(offset) {
        var today = new Date();
        var todayStr = today.getFullString();
        var re;
        if(typeof offset === 'number' && offset === offset) {
            re = today.getFullString(offset);
        } else {
            return '';
        }

        return [re, todayStr];
    }

    seajs.use('dialog', function(dg) {
        dg.titleInit();
    });

    function getNumberData(data) {
        data = data || {};
        $.ajax({
            url: BASE_PATH + '/account/member/rechargeSummary',
            data: data,
            success: function (json) {
                if (json && json.success) {
                    $('#moneyAmount').text(json.data.summaryAmount);
                    $('#customerNum').text(json.data.summaryCount);
                }
            }

        });
    }
})


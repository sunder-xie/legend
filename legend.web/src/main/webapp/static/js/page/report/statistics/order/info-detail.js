/**
 * Created by th on 16/5/19.
 * 工单明细表
 */

$(function () {
    var istqmall = $("#istqmall").val();
    seajs.use(['ajax', 'dialog', 'formData', 'table', 'layer', 'select', 'date'],
        function (ajax, dialog, formData, table, layer, select, dp) {
            /* 加载表单展示 */
            var orderTag, orderStatus;
            if (istqmall == "true") {
                orderTag = [{
                    key: '',
                    value: '请选择'
                }, {
                    key: 2,
                    value: '洗车单'
                }, {
                    key: 3,
                    value: '快修快保'
                }, {
                    key: 5,
                    value: '销售'
                }];

                orderStatus = [{
                    key: '',
                    value: '请选择'
                }, {
                    key: 'CJDD',
                    value: '待结算'
                }, {
                    key: 'DDYFK',
                    value: '已结算'
                }];
            } else {
                orderTag = [{
                    key: '',
                    value: '请选择'
                }, {
                    key: 1,
                    value: '综合维修单'
                }, {
                    key: 2,
                    value: '洗车单'
                }, {
                    key: 3,
                    value: '快修快保'
                }, {
                    key: 4,
                    value: '引流活动'
                }, {
                    key: 5,
                    value: '销售'
                }];

                orderStatus = [{
                    key: '',
                    value: '请选择'
                }, {
                    key: 'CJDD',
                    value: '待报价'
                }, {
                    key: 'DDBJ',
                    value: '已报价'
                }, {
                    key: 'FPDD',
                    value: '已派工'
                }, {
                    key: 'DDSG',
                    value: '修理中'
                }, {
                    key: 'DDWC',
                    value: '已完工'
                }, {
                    key: 'DDYFK',
                    value: '已结算'
                }];
            }
            var params = {
                time: [util.getPara('startTime'), util.getPara('endTime')],
                payTime: [util.getPara('payStartTime'), util.getPara('payEndTime')],
                orderStatus: util.getPara('orderStatus'),
                clear: util.getPara('clear')
            };

            var $document = $(document);
            var Model = {
                main: function (success) {
                    var url = BASE_PATH + "/report/get_config/order_info_detail";
                    $.ajax({
                        url: url,
                        dataType: 'json',
                        success: function (result) {
                            if (result.success) {
                                var datas = result.data;
                                for (var i = 0; i < datas.length; i++) {
                                    var data = datas[i];
                                    var display = data.display;
                                    if (!display) {
                                        $(".dropdown-menu input[data-ref=" + data.field + "]").removeAttr('checked');
                                    }
                                }
                                success && success(result);
                            } else {
                                dialog.warn(result.errorMsg, 2, 3);
                            }
                        }
                    });
                }
            };
            dialog.titleInit();

            select.init({
                dom: '.js-select-order-tag',
                showValue: 'value',
                showKey: 'key',
                data: orderTag
            });
            select.init({
                dom: '.js-select-order-status',
                showValue: 'value',
                showKey: 'key',
                selectedKey: params.orderStatus,
                data: orderStatus
            });

            dp.dpStartEnd({
                startSettings: {
                    maxDate: '#F{$dp.$D(\'createTimeEnd\')}'
                },
                endSettings: {
                    minDate: '#F{$dp.$D(\'createTimeStart\')}'
                },
                start: 'createTimeStart',
                end: 'createTimeEnd'
            });
            dp.dpStartEnd({
                start: 'payTimeStart',
                end: 'payTimeEnd',
                startSettings: {
                    maxDate: '#F{$dp.$D(\'payTimeEnd\')}'
                },
                endSettings: {
                    minDate: '#F{$dp.$D(\'payTimeStart\')}'
                }
            });

            $('.js-search-btn').on('click', function (e) {
                var start = $.trim($('input[name="search_createTimeStart"]').val());
                var end = $.trim($('input[name="search_createTimeEnd"]').val());
                var start2 = $.trim($('input[name="search_payTimeStart"]').val());
                var end2 = $.trim($('input[name="search_payTimeEnd"]').val());
                var ret = true;

                if ((start == '' && end != '') || (start == '' && start2 == '' && end2 == '')) {
                    dialog.warn('请选择开单开始时间');
                    ret = false;
                } else if (start != '' && end == '') {
                    dialog.warn('请选择开单结束时间');
                    ret = false;
                } else if (start2 == '' && end2 != '') {
                    dialog.warn('请选择结算开始时间');
                    ret = false;
                } else if (start2 != '' && end2 == '') {
                    dialog.warn('请选择结算结束时间');
                    ret = false;
                }

                if (!ret) {
                    e.stopImmediatePropagation();
                }
                total();
            });

            /* 报表展示 start */
            // 列表字段显示
            $('.js-list-option').click(function () {
                var target = $($(this).data('target'));

                if (target.hasClass('hide')) {
                    target.removeClass('hide');
                    plusTominus(this);
                } else {
                    target.addClass('hide');
                    minusToplus(this);
                }
            });
            /* 表单展示事件 start */
            $document.on('click', '.dropdown-menu input[type=checkbox]', function () {
                displayReport.call(this);
                var datas = [];
                //组装配置bean传给后台
                $(".dropdown-menu input[type=checkbox]").each(function () {
                    var $this = $(this)
                    data = {
                        "field": $this.data('ref'),
                        "name": $this.parent().text(),
                        "display": $this.is(':checked')
                    }
                    datas.push(data);
                });
                var url = BASE_PATH + "/report/save_config/order_info_detail";
                $.ajax({
                    url: url,
                    dataType: 'json',
                    global: false,
                    data: {
                        confValue: JSON.stringify(datas)
                    },
                    success: function (result) {
                    }
                });
            });


            $.ajax({
                url: BASE_PATH + '/shop/stats/staff/attendance/getshopemployee',
                type: "get",  //post请求
                success: function (json) {
                    var $arr = ['.js-select-receiver', '.js-select-worker', '.js-select-saler'];
                    //请求成功之后填充数据
                    //可以把data看成一个数组
                    $arr.forEach(function (e) {
                        select.init({
                            dom: e,
                            showValue: 'name',
                            showKey: 'id',
                            pleaseSelect: true,
                            data: json.data
                        });
                    });
                }
            });

            // 密码登录导出
            exportSecurity.tip({'title':'导出报表信息'});
            exportSecurity.confirm({
                dom: '#excelBtn',
                title: '经营报表—工单流水表',
                callback: function(json){
                    var url = BASE_PATH + '/shop/stats/order_info_detail/get_excel?a=1';

                    //过滤参数初始化
                    var data = formData.get('.search-form');
                    var str = '';

                    var time1 = new Date(data.search_createTimeStart);
                    var time2 = new Date(data.search_createTimeEnd);
                    var time5 = new Date(data.search_payTimeStart);
                    var time6 = new Date(data.search_payTimeEnd);
                    var monthDiff = time2.getMonth() - time1.getMonth();
                    var monthDiff2 = time5.getMonth() - time6.getMonth();
                    if (monthDiff > 2) {
                        dialog.warn("开单查询时间不能超过2个月");
                        return
                    }

                    if (monthDiff2 > 2) {
                        dialog.warn("结算查询时间不能超过2个月");
                        return
                    }

                    for(var i in data) {
                        str = str + '&' + i + '=' + data[i];
                    }
                    url = url + str;
                    window.location.href = url;
                }
            });

            //弹框
            $document.on('click', '.js-dialog', function () {
                $.layer({
                    type: 1,
                    title: false,
                    area: ['auto', 'auto'],
                    border: [0], //去掉默认边框
                    shade: [0.5, '#000'],
                    shadeClose: false,
                    bgcolor: '#fff',
                    closeBtn: [1, true], //去掉默认关闭按钮
                    shift: 'top',
                    page: {
                        html: $('#ins-dialog').html()
                    }
                })
            })

            /** function start **/
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

            function renderHTML(data) {
                data = data || {};
                table.init({
                    url: BASE_PATH + '/shop/stats/order_info_detail/list/list',
                    fillid: 'infoFill',
                    tplid: 'infoTpl',
                    pageid: 'infoPage',
                    formid: 'searchForm',
                    data: data,
                    fullString: true,
                    callback: function (html) {
                        var $table = $(html);
                        /* 列表项目根据配置更改 start */
                        Model.main(function () {
                            $('.dropdown').find("input").each(function () {
                                displayReport.call(this, $table);
                            });

                            $('#infoFill').empty().append($table)
                                // 初次载入时避免出现一条边框
                                .parent().removeClass('hide');
                        });
                        /* 列表项目根据配置更改 end */
                    }
                });
            }

            function displayReport(table) {
                var $this = $(this),
                    ref = $this.data('ref'),
                    $ref = table ? table.find('.' + ref) : $('.' + ref),
                    checked = $this.prop('checked');
                checked ? $ref.removeClass('hide') : $ref.addClass('hide');
            }

            (function initialize() {
                if (params.clear == "true") {
                    $("#createTimeStart").val(params.time[0]);
                    $("#createTimeEnd").val(params.time[1]);

                    $('#payTimeStart').val(params.payTime[0]);
                    $('#payTimeEnd').val(params.payTime[1]);
                } else {
                    $("#createTimeStart").val(getDateStr(-7));
                    $("#createTimeEnd").val(getDateStr(0));
                }

                $.ajax({
                    url: BASE_PATH + '/shop/stats/staff/attendance/getshopemployee',
                    type: "get",
                    success: function (json) {
                        var $arr = ['.js-select-receiver', '.js-select-worker', '.js-select-saler'];
                        //请求成功之后填充数据
                        //可以把data看成一个数组
                        $arr.forEach(function (e) {
                            select.init({
                                dom: e,
                                showValue: 'name',
                                showKey: 'id',
                                pleaseSelect: true,
                                data: json.data
                            });
                        });
                    }
                });

                /* 列表项目根据配置更改 start */
                renderHTML();
            })();
            total();
            function total(){
                //新加总计
                var data = formData.get('#searchForm');
                $.ajax({
                    url:BASE_PATH + '/shop/stats/order_info_detail/orderTotal',
                    data:data,
                    success:function(result){
                        if(result.success){
                            var total = $('.total-box');
                            total.find('.payAmount').text((result.data.totalAmount).toFixed(2));
                            total.find('.grossAmount').text((result.data.payAmount).toFixed(2));
                            total.find('.signAmount').text((result.data.grossAmount).toFixed(2));
                        }
                    }
                })
            }
        });



    function plusTominus(e) {
        $(e).find('.icon-plus')
            .removeClass('icon-plus')
            .addClass('icon-minus');
    }

    function minusToplus(e) {
        $(e).find('.icon-minus')
            .removeClass('icon-minus')
            .addClass('icon-plus');
    }


});

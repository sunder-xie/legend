$(function () {
    var params = {
        time: [util.getPara('startTime'), util.getPara('endTime')],
        status: util.getPara('status'),
        clear: util.getPara('clear')
    };

    var urls = {
        in: {
            'list': BASE_PATH + '/shop/stats/warehouse-info/in/list/list',
            'export': BASE_PATH + '/shop/stats/warehouse-info/in/export?',
            'total': BASE_PATH + '/shop/stats/warehouse-info/in/getTotalInInfo'
        },
        out: {
            'list': BASE_PATH + '/shop/stats/warehouse-info/out/list/list',
            'export': BASE_PATH + '/shop/stats/warehouse-info/out/export?',
            'total': BASE_PATH  + '/shop/stats/warehouse-info/out/getTotalOutInfo'
        }
    };
    var tableDispaly = {};

    tableDispaly.in = new TableDisplay(['warehouse-in'], 'report.warehouse.');
    tableDispaly.out = new TableDisplay(['warehouse-out'], 'report.warehouse.');
    var event = {
        in: tableDispaly.in.checkBoxEvent.bind(null, tableDispaly.in),
        out: tableDispaly.out.checkBoxEvent.bind(null, tableDispaly.out)
    };
    var prefix = 'in', outTableLoad = false;


    /* 表单展示事件 */
    $('.dropdown-menu input').on('click', function (e) {
        var t = $(this).data('ref');
        var parent = $(this).parents('.dropdown-menu');
        var url,allData;
        event[prefix].call(this, e);
        totalSet(t, $(this).prop('checked'));

        if(parent.hasClass('dropdown-menu-in')){
            url = BASE_PATH + "/report/save_config/warehouse_in";
            var datasIn = [];
            //组装配置bean传给后台
            $(".dropdown-menu-in input[type=checkbox]").each(function () {
                var $this = $(this);
                data = {
                    "field": $this.data('ref'),
                    "display": $this.is(':checked')
                };
                datasIn.push(data);
                allData = JSON.stringify(datasIn)
            });
        }else if(parent.hasClass('dropdown-menu-out')){
            url = BASE_PATH + "/report/save_config/warehouse_out";
            var datasOut = [];
            //组装配置bean传给后台
            $(".dropdown-menu-out input[type=checkbox]").each(function () {
                var $this = $(this);
                data = {
                    "field": $this.data('ref'),
                    "display": $this.is(':checked')
                };
                datasOut.push(data);
                allData = JSON.stringify(datasOut)
            });
        }
        $.ajax({
            url: url,
            dataType: 'json',
            global: false,
            data: {
                confValue: allData
            },
            success: function (result) {

            }
        });
    });

    if (params.clear == "true") {
        $("#createTimeStart").val(params.time[0]);
        $("#createTimeEnd").val(params.time[1]);
    } else {
        $("#createTimeStart").val(getDateStr(-7));
        $("#createTimeEnd").val(getDateStr(0));
    }
    $("#createTimeStart2").val(getDateStr(-7));
    $("#createTimeEnd2").val(getDateStr(0));

    seajs.use([
        'dialog',
        'table',
        'date',
        'select',
        'formData',
        'art'
    ], function (dialog, table, dp, select, formData, art) {
        var selectUrls = {
            supplier: BASE_PATH + '/shop/setting/supplier/get_supplier_list',
            employee: BASE_PATH + '/shop/stats/staff/attendance/getshopemployee'
        };
        var orderType = {
            in: [
                {name: '蓝字入库', id: 'LZRK'},
                {name: '红字入库', id: 'HZRK'},
            ],
            out: [
                {name: '蓝字出库', id: 'LZCK'},
                {name: '红字出库', id: 'HZCK'},
            ]
        };


        select.init({
            url: selectUrls.supplier,
            showKey: 'id',
            showValue: 'supplierName',
            dom: '.js-supplier',
            canInput: true,
            isClear: true
        });

        select.init({
            url: selectUrls.employee,
            pleaseSelect: true,
            dom: '.js-employee'
        });

        select.init({
            data: orderType.in,
            pleaseSelect: true,
            selectedKey: params.status,
            dom: '.js-order-type-in'
        });

        select.init({
            data: orderType.out,
            pleaseSelect: true,
            dom: '.js-order-type-out'
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

        dp.dpStartEnd({
            startSettings: {
                maxDate: '#F{$dp.$D(\'createTimeEnd2\')}'
            },
            endSettings: {
                minDate: '#F{$dp.$D(\'createTimeStart2\')}'
            },
            start :'createTimeStart2',
            end: 'createTimeEnd2'
        });

        table.init({
            url: urls[prefix].list,
            fillid: prefix + 'ListFill',
            pageid: prefix + 'ListPage',
            tplid:  prefix + 'ListTpl',
            formid: prefix + 'SearchForm',
            ajax: {
                type: 'post'
            },
            dataType: 'json',
            callback: function (html) {
                getTotal(true, 'post');

                $.ajax({
                    url: BASE_PATH + "/report/get_config/warehouse_in",
                    dataType: 'json',
                    success: function (result) {
                        if (result.success) {
                            var datas = result.data;
                            var $dropdown = $('.dropdown-menu-in');
                            for (var i = 0; i < datas.length; i++) {
                                var data = datas[i];
                                if (!data.display) {
                                    $dropdown.find("input[data-ref=" + data.field + "]").removeAttr('checked');
                                }
                            }
                            var $table = $(html);
                            $('.dropdown-menu-in').find("input").each(function () {
                                var $this = $(this),
                                    ref = $this.data('ref'),
                                    $ref = $table ? $this.parents('.container').next().find('.' + ref) : $('.' + ref),
                                    checked = $this.prop('checked');
                                checked ? $ref.removeClass('hide') : $ref.addClass('hide');
                            });
                        } else {
                            seajs.use('dialog',function(dialog){
                                dialog.warn(result.errorMsg);
                            });
                        }
                    }
                });
                tableDispaly[prefix].tableDisplayInit(null, totalSet.bind(null, prefix));

            }
        });

        $('.js-search-btn').on('click', function () {
            var form = $(this).parents('.search-form');
            if( !form.find('input[name=startTime]').val() || !form.find('input[name=endTime]').val() ) {
                dialog.warn('请选择开始时间和结束时间');
                return false;
            }
        });

        // 导出
        // 密码登录导出
        exportSecurity.tip({'title':'导出报表信息'});
        exportSecurity.confirm({
            dom: '.js-excel',
            title: '经营报表—出入库明细表',
            callback: function() {
                var url = urls[prefix].export;
                var t =  getParaFromForm.call(this);

                if(t) {
                    url += t;
                }

                window.location.href = url;
            }
        });

        $('.js-tab-item').on('click', function () {
            var target = $(this).data('target');
            prefix = $(this).data('desc');

            if(!outTableLoad && $(this).data('desc') == 'out') {
                table.init({
                    url: urls[prefix].list,
                    fillid: prefix + 'ListFill',
                    pageid: prefix + 'ListPage',
                    tplid: prefix + 'ListTpl',
                    formid: prefix + 'SearchForm',
                    ajax: {
                        type: 'post'
                    },
                    dataType: 'json',
                    callback: function (html) {
                        getTotal(true, 'post');
                        $.ajax({
                            url: BASE_PATH + "/report/get_config/warehouse_out",
                            dataType: 'json',
                            success: function (result) {
                                if (result.success) {
                                    var datas = result.data;
                                    var $dropdown = $('.dropdown-menu-out');
                                    for (var i = 0; i < datas.length; i++) {
                                        var data = datas[i];
                                        if (!data.display) {
                                            $dropdown.find("input[data-ref=" + data.field + "]").removeAttr('checked');
                                        }
                                    }
                                    var $table = $(html);
                                    $('.dropdown-menu-out').find("input").each(function () {
                                        var $this = $(this),
                                            ref = $this.data('ref'),
                                            $ref = $table ? $this.parents('.container').next().find('.' + ref) : $('.' + ref),
                                            checked = $this.prop('checked');
                                        checked ? $ref.removeClass('hide') : $ref.addClass('hide');
                                    });
                                } else {
                                    seajs.use('dialog',function(dialog){
                                        dialog.warn(result.errorMsg);
                                    });
                                }
                            }
                        });
                        tableDispaly[prefix].tableDisplayInit(null, totalSet.bind(null, prefix));

                    }
                });
            }


            $('.yqx-wrapper .current-item').removeClass('current-item');

            $(this).addClass('current-item');

            $('.current-content').removeClass('current-content')
                .addClass('hide');

            $(target).addClass('current-content')
                .removeClass('hide');
        });


        $('.js-supplier').on('input', function () {
            var arr = $(this).parent().find('.yqx-select-options dd');
            var val = $.trim($(this).val()).toLowerCase();

            arr.each(function () {
                var text = $(this).text().toLowerCase();

                if (text.indexOf(val) > -1) {
                    $(this).removeClass('hide');
                } else {
                    $(this).addClass('hide');
                }
            });

            $(this).siblings('input').val('');
        });

        function getParaFromForm() {
            var str = '';
            //过滤参数初始化
            var data = formData.get( $(this).parents('.container').find('.search-form') );

            if (!data.startTime) {
                dialog.warn("开始时间不能为空");
                return false;
            }
            if (!data.endTime) {
                dialog.warn("结束时间不能为空");
                return false;
            }

            for(var i in data) {
                str = str + '&' + i + '=' + data[i];
            }
            return str;
        }

        function getTotal(toJSON, type) {
            var data = {
                data: formData.get('#' + prefix + 'SearchForm')
            };

            var ajax = {
                url: urls[prefix].total,
                success: function (json) {
                    var html = art(prefix + 'TotalTpl', {json:json});

                    $('#' + prefix + 'TotalFill').html(html);
                }
            };

            if(toJSON) {
                data.contentType = 'application/json';
                data.data = JSON.stringify(data.data);
            }

            data.type = type || 'get';

            $.ajax(
                $.extend(ajax, data)
            )

        }
    });

    function totalSet(prefix, desc, state) {
        var parent = $('.warehouse-' + prefix + '-content');
        try {
            colspanControl(parent.find('.js-total-leadTh'), desc, state);
            colspanControl(parent.find('.js-total-endTh'), desc, state);
        } catch (e) {

        }
    }

    // 跨列的 th colspan 控制
    function colspanControl(ele, desc, state) {
        var th = ele,
            max = +th.data('max'),
            target = th.data('target'),
            colspan = +th.attr('colspan');

        if (target.indexOf(desc) > -1) {
            // 选中的时候
            if (state && colspan < max) {
                colspan += 1;
            }
            // 不选中的时候
            else if( !state ){
                colspan -= 1;
            }
            if(colspan == 0) {
                th.hide();
            } else {
                th.show();
            }
            th.attr('colspan', colspan)
        }
    }

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
})
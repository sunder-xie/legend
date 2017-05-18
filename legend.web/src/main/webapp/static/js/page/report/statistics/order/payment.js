/**
 * Created by wjc on 16/5/19.
 * 工单结算收款表
 */

$(function () {
    var istqmall = $("#istqmall").val();
    seajs.use(['ajax', 'dialog', 'formData', 'table', 'layer', 'select', 'date'],
        function (ajax, dialog, formData, table, layer, select, dp) {
            var $document = $(document);
            var orderTag
            var orderStatus
            if(istqmall == "true"){
                orderTag = [{
                    key: '',
                    value: '请选择'
                },  {
                    key: 2,
                    value: '洗车单'
                }, {
                    key: 3,
                    value: '快修快保'
                }, {
                    key: 5,
                    value: '销售'
                }];
            }else{
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
            }
            var Model = {
                main: function (success) {
                    var url = BASE_PATH + "/report/get_config/order_payment";
                    $.ajax({
                        url: url,
                        dataType: 'json',
                        success: function (result) {
                            if (result.success) {
                                var datas = result.data;
                                var $dropdown = $('.dropdown-menu');
                                for (var i = 0; i < datas.length; i++) {
                                    var data = datas[i];
                                    if (!data.display) {
                                        $dropdown.find("input[data-ref=" + data.field + "]").removeAttr('checked');
                                    }
                                }

                                success && success(result);
                            } else {
                                dialog.warn(result.errorMsg);
                            }
                        }
                    });
                }
            }
        var ssTime = $("#ssTime").val();
            if(null == ssTime||""==ssTime){
                $("#sPayTime").val(getDateStr(-7));
                $("#ePayTime").val(getDateStr(0));

            }

            dialog.titleInit();
            renderHTML();
            dp.dpStartEnd({
                startSettings: {
                    maxDate: '#F{$dp.$D(\'ePayTime\')}'
                },
                endSettings: {
                    minDate: '#F{$dp.$D(\'sPayTime\')}'
                },
                start: 'sPayTime',
                end: 'ePayTime'
            });

            select.init({
                dom: '.js-server-select'
            });

            select.init({
                dom: '.js-status-select',
                showKey: 'key',
                showValue: 'value',
                data: [{
                    key: '',
                    value: '请选择'
                }, {
                    key: '0',
                    value: '实付'
                }, {
                    key: '1',
                    value: '坏账'
                }]
            });

            select.init({
                dom: '.js-select-order-tag',
                showValue: 'value',
                showKey: 'key',
                data: orderTag
            });

            $('.js-search-btn').on('click', function (e) {
                var start = $.trim($('input[name="search_sPayTime"]').val());
                var end = $.trim($('input[name="search_ePayTime"]').val());

                if(start == '') {
                    dialog.warn('请选择结算开始时间');
                    e.stopImmediatePropagation();
                    return;
                }
                if(end == '') {
                    dialog.warn('请选择结算结束时间');
                    e.stopImmediatePropagation();
                }
                orderTotal();
            });

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

            // 密码登录导出
            exportSecurity.tip({'title':'导出报表信息'});
            exportSecurity.confirm({
                dom: '#excelBtn',
                title:'经营报表—工单结算收款表',
                callback: function(json){
                    var url = BASE_PATH + '/shop/stats/order_payment/get_excel?';
                    var str = '';
                    //过滤参数初始化
                    var data = formData.get('.search-form');

                    var time1 = new Date(data.search_sPayTime);
                    var time2 = new Date(data.search_ePayTime);
                    var timeDiff = time2.getMonth() - time1.getMonth();
                    if (timeDiff > 2) {
                        dialog.warn("结算查询时间不能超过2个月");
                        return
                    }

                    for(var i in data) {
                        str = str + '&' + i + '=' + data[i];
                    }
                    url += str;

                    window.location.href = url;
                }
            });

            //弹框
            $document.on('click', '.js-dialog', function () {
                layer.open({
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


            /* 表单展示事件 start */
            $('.dropdown-menu input[type=checkbox]').on('click', function () {
                displayReport.call(this);
                var datas = [];
                //组装配置bean传给后台
                $(".dropdown-menu input[type=checkbox]").each(function () {
                    var $this = $(this)
                    data = {
                        "field": $this.data('ref'),
                        "display": $this.is(':checked')
                    }
                    datas.push(data);
                });
                var url = BASE_PATH + "/report/save_config/order_payment";
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
            /** function start **/
            function getDateStr(AddDayCount) {
                var dd = new Date(),
                    y, m, d;

                dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
                y = dd.getFullYear();
                m = dd.getMonth() + 1;//获取当前月份的日期
                d = dd.getDate();

                m = m < 10 ? "0" + m : m;
                d = d < 10 ? "0" + d : d;

                return y + "-" + m + "-" + d;
            }

            /* 报表展示 start */
            function displayReport(table) {
                var $this = $(this),
                    ref = $this.data('ref'),
                    $ref = table ? table.find('.' + ref) : $('.' + ref),
                    checked = $this.prop('checked');
                checked ? $ref.removeClass('hide') : $ref.addClass('hide');
            }

            function renderHTML(data) {
                data = data || {};
                table.init({
                    url: BASE_PATH + '/shop/stats/order_payment/list',
                    fillid: 'paymentFill',
                    tplid: 'paymentTpl',
                    pageid: 'paymentPage',
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

                            $('#paymentFill').empty().append($table)
                                // 初次载入时避免出现一条边框
                                .parent().removeClass('hide');
                        });
                        /* 列表项目根据配置更改 end */
                    }
                });
            }


            /* 报表展示 end */

            orderTotal();
            //新加总计
            function orderTotal(){
                var totalData = formData.get('#searchForm');
                $.ajax({
                    url:BASE_PATH + '/shop/stats/order_payment/total',
                    data:totalData,
                    success:function(result){
                        if(result.success){
                            var total = $('.total-box');
                            total.find('.payAmount').text((result.data.payAmount).toFixed(2));
                            total.find('.grossAmount').text((result.data.grossAmount).toFixed(2));
                            total.find('.signAmount').text((result.data.signAmount).toFixed(2));
                            total.find('.badAmount').text((result.data.badAmount).toFixed(2));
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

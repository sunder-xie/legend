$(function () {
    var base = '/shop/report/business/daily';
    var card = {};
    var numberToWeek = {
        '1' : '上周一',
        '2' : '上周二',
        '3' : '上周三',
        '4' : '上周四',
        '5' : '上周五',
        '6' : '上周六',
        '0' : '上周日'
    };

    // 快速筛选
    $('.js-quick-select').each(function () {
        var t = getOffsetDay(+this.dataset.target);

        if(t.str) {
            $(this).text(t.str);
        }

        $(this).attr('data-date',
            t.date.toISOString().substr(0, 10));
    });

    seajs.use([
        'art',
        'dialog',
        'ajax',
        'date',
        'table'
    ], function (art, dialog, ajax, date, table) {
        dialog.titleInit();
        var str = 'order', descs = ['card', 'purchase', 'revenue', 'customer'];
        var orderLoad = false;
        var urls = {
            show: {
                card: BASE_PATH + base + '/account-trade',
                purchase: BASE_PATH + base + '/purchase-sale',
                revenue: BASE_PATH + base + '/revenue',
                customer: BASE_PATH + base + '/customer-statistics'
            },
            table: {
                account: BASE_PATH + base + '/business-statistics',
                order: BASE_PATH + base + '/order-list'
            }
        };
        for (var i in descs) {
            getData(descs[i], {dateStr: $('.yqx-input.date').val()});
        }

        art.helper('getCard', function () {
            return card;
        });

        date.datePicker('.date', {
            dateFmt:'yyyy-MM-dd',
            maxDate: '%y-%M-%d'
        });

        $('.js-slide-up').on('click', function () {
            var target = $(this).data('target');

            if(target.indexOf('.order-info') > -1 && !orderLoad) {
                orderLoad = true;
                table.init({
                    url: urls.table.order,
                    pageid: str + 'Page',
                    fillid: str + 'Fill',
                    tplid: str + 'Tpl',
                    formid: 'searchForm'
                });
            }
            if( $(target).is(':hidden') ) {
                $(this).text('收起');
                $(target).slideDown();
            } else {
                $(this).text('展开');
                $(target).slideUp();
            }

        });

        $('.js-search-btn').on('click', function () {
            var val = $('.yqx-input.date').val();

            getAccountData( {dateStr: val} );

            $('.module-statistic .module-box').empty();

            for (var i in descs) {
                getData(descs[i], {dateStr: val});
            }
        });

        // 快速筛选
        $('.js-quick-select').on('click', function () {
            $('.date').val( this.dataset.date );

            $('.search-form .js-search-btn').trigger('click');
        });

        function getAccountData(data) {
            $.ajax({
                url: urls.table.account,
                data: data,
                success: function (json) {
                    var html = art('accountTpl', {json: json});

                    $(html).appendTo( $('.account-detail table').find('tbody').empty().end() );

                    if( $('.account-detail .table-fill-line').length == 0 ) {
                        $('<div class="table-fill-line"></div>')
                            .appendTo($('.account-detail .table-box'));
                    }
                }
            })
        }

        function getData(desc, data) {
            var global = false, timer = 300;
            if (desc == 'revenue') {
                global = true;
                timer = 0;
            }
            setTimeout(function () {
                $.ajax({
                    url: urls.show[desc],
                    data: data,
                    global: global,
                    success: function (json) {
                        if (json && json.success) {
                            var $e = $(art(desc + 'Tpl', {json: json}));

                            if (desc == 'card') {
                                card = json.data;
                                getAccountData( {dateStr: $('.yqx-input.date').val()} );
                            }

                            // 按 data-index 顺序排列
                            setIndex($e, $('.module-statistic .module-box'));
                        }
                    }
                })
            }, timer);
        }

        // 营业额跳转工单流水表
        $(document).on('click', '.js-report-order-pay', function () {
            date = $("input[name='dateStr']").val();
            $(this).attr('href', BASE_PATH + "/shop/stats/order_info_detail?refer=daily&payStartTime=" + date + "&payEndTime=" + date + "&orderStatus=DDYFK&clear=true");
        });
        // 采购跳转入库明细报表
        $(document).on('click', '.js-warehouse-in', function () {
            date = $("input[name='dateStr']").val();
            $(this).attr('href', BASE_PATH + "/shop/stats/warehouse-info/in?refer=daily&startTime=" + date + "&endTime=" + date + "&status=LZRK&clear=true");

        });
        // 实付跳转财务付款流水
        $(document).on('click', '.js-settle-pay', function () {
            date = $("input[name='dateStr']").val();
            $(this).attr('href', BASE_PATH + "/shop/settlement/pay/pay-flow?refer=daily&startTime=" + date + "&endTime=" + date);

        });
        // 配件销量跳转销售明细表－配件销售明细
        $(document).on('click', '.js-report-sale-goods', function () {
            date = $("input[name='dateStr']").val();
            $(this).attr('href', BASE_PATH + "/shop/stats/order/goods?refer=daily&orderSettleStartTime=" + date + "&orderSettleEndTime=" + date);
        });
        // 充值跳转卡券充值表－优惠券
        $(document).on('click', '.js-account-coupon', function () {
            date = $("input[name='dateStr']").val();
            $(this).attr('href', BASE_PATH + "/shop/stats/card/coupon-recharge?refer=daily&tab=2&startTime=" + date + "&endTime=" + date);
        });
        // 充值跳转客户管理－充值记录
        $(document).on('click', '.js-account-card-recharge', function () {
            date = $("input[name='dateStr']").val();
            $(this).attr('href', BASE_PATH + "/account/flow?refer=daily&startTime=" + date + "&endTime=" + date);
        });
        // 充值跳转卡券充值表－计次卡
        $(document).on('click', '.js-account-combo', function () {
            date = $("input[name='dateStr']").val();
            $(this).attr('href', BASE_PATH + "/shop/stats/card/coupon-recharge?refer=daily&tab=1&startTime=" + date + "&endTime=" + date);
        });
        // 充值跳转卡券充值表－会员卡
        $(document).on('click', '.js-account-card', function () {
            date = $("input[name='dateStr']").val();
            $(this).attr('href', BASE_PATH + "/shop/stats/card/coupon-recharge?refer=daily&tab=0&consumeTypeId=5&consumeTypeName=会员卡办卡&startTime=" + date + "&endTime=" + date);
        });
        // 退卡跳转卡券充值表－会员卡
        $(document).on('click', '.js-account-card-retreat', function () {
            date = $("input[name='dateStr']").val();
            $(this).attr('href', BASE_PATH + "/shop/stats/card/coupon-recharge?refer=daily&tab=0&consumeTypeId=6&consumeTypeName=会员卡退卡&startTime=" + date + "&endTime=" + date);
        });
        // 接车跳转工单流水表
        $(document).on('click', '.js-report-order-create', function () {
            date = $("input[name='dateStr']").val();
            $(this).attr('href', BASE_PATH + "/shop/stats/order_info_detail?refer=daily&startTime=" + date + "&endTime=" + date + "&clear=true");
        });
    });

    function setIndex($e, $eles) {
        var index = $e.data('index');

        $eles.eq(index).append($e);
    }

    function getOffsetDay(offset) {
        var now = new Date();
        var t, d = now.getDate();
        var ret = {};

        t = now;
        t.setDate(d + offset);

        // 上个星期
        if(offset === -7) {
            ret.str = numberToWeek[ t.getDay() ];
        }

        ret.date = t;

        return ret;
    }
});
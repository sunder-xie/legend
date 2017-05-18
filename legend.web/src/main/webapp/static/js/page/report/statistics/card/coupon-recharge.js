$(function () {
    var url = BASE_PATH + '/shop/stats/card/coupon-recharge/list';
    var base = BASE_PATH + '/shop/stats/card/coupon-recharge/';
    var type = {
        coupon: 1,
        member: 3,
        combo: 2
    };
    var currentType, tab = util.getPara('tab');

    tab = tab != '' && tab ? tab : 0;
    var start1 = $("#start1").val();
    var start2 = $("#start2").val();
    var start3 = $("#start3").val();
    if (!(start1 || start2 || start3)) {
        $('input[name="search_sTime"]').val(getDateStr(-7));
        $('input[name="search_eTime"]').val(getDateStr(0));
    }
    seajs.use([
        'table',
        'select',
        'ajax',
        'dialog',
        'formData',
        'date'
    ], function (table, select, ajax, dialog, formData, date) {
        var tableDispaly;

        tableDispaly = new TableDisplay(['member', 'combo', 'coupon'], 'report.card.recharge.');
        var event = tableDispaly.checkBoxEvent.bind(null, tableDispaly);

        // tab标签组
        var tabMap = {
            'member': '经营报表—会员卡充值记录表',
            'combo': '经营报表—计次卡充值记录表',
            'coupon': '经营报表—优惠券充值记录表'
        };

        // 密码登录导出
        exportSecurity.tip({'title':'导出报表信息'});
        exportSecurity.confirm({
            dom: '.js-excel',
            title: '经营报表—会员卡充值记录表',
            callback: function(json){
                var data = formData.get($(this).parents('.content'));
                var target = $(this).data('target');
                var url = BASE_PATH + '/shop/stats/card/coupon-recharge/get_excel';
                var iframe = $('<iframe class="hide"></iframe>'), str = 'search_isExcel=1';

                // 参数拼接
                for (var i in data) {
                    str = str + '&' + i + '=' + data[i];
                }

                str += '&' + 'search_tradeType=' + currentType;
                iframe.attr('src', url + '?' + str);
                $(document.body).append(iframe);
            }
        });


        $('.js-tab-item').on('click', function () {
            var target = $(this).data('target');
            var tabIndex = $(this).data('tab');
            var desc = $(this).data('desc');
            var current = $('.current-item');

            exportSecurity["exportBrief"] = tabMap[desc] || "经营报表—会员卡充值记录表";

            current.removeClass('current-item');
            $(this)
                .parents('.content').addClass('hide');

            $(target).removeClass('hide')
                .find('.tab-item').eq(tabIndex)
                .addClass('current-item');

            tableInit(desc);
        });

        $('.js-tab-item').eq(tab).click();

        // 时间选择
        for (var i = 1; i < 4; i++) {
            var start = 'start' + i;
            var end = 'end' + i;
            date.dpStartEnd({
                start: start,
                end: end
            });
        }

        /* 表单展示事件 */
        $('.dropdown-menu input').on('click',
            event
        );

        select.init({
            url: base + '/getSuiteInfo',
            dom: '.js-suite-info',
            isPleaseSelect: true,
            showKey: 'id',
            showValue: 'suiteName'
        });

        select.init({
            url: base + '/getCouponInfo',
            pleaseSelect: true,
            dom: '.js-coupon-info',
            showKey: 'id',
            showValue: 'couponName'
        });

        select.init({
            url: base + '/getComboInfo',
            dom: '.js-combo-info',
            pleaseSelect: true,
            showKey: 'id',
            showValue: 'comboName'
        });

        select.init({
            url: base + '/getMemberCardInfo',
            dom: '.js-member-info',
            pleaseSelect: true,
            showKey: 'id',
            showValue: 'typeName'
        });

        select.init({
            data: [{
                id: 5,
                name: '会员卡办卡'
            },{
                id: 1,
                name: '会员卡充值'
            },{
                id: 6,
                name: '会员卡退卡'
            }, {
                id: 8,
                name: '会员卡导入'
            }],
            pleaseSelect: true,
            dom: '.js-member-type',
            showKey: 'id',
            showValue: 'name'
        });

        dialog.titleInit();

        function tableInit(desc) {
            currentType = type[desc];
            table.init({
                formid: desc + 'Form',
                fillid: desc + 'Fill',
                pageid: desc + 'Page',
                tplid: desc + 'Tpl',
                ajax: {
                    contentType: 'application/json'
                },
                data: {
                    search_tradeType: type[desc],
                    refer:type[desc]
                },
                callback: function () {
                    desc === 'member' && cardTotal();
                    tableDispaly.tableDisplayInit([desc]);
                },
                url: url
            });
        }


        $('.js-search-btn').on('click', function (e) {
            var parent = $(this).parents('.search-form');
            var start = $.trim(parent.find('input[name="sTime"]').val());
            var end = $.trim(parent.find('input[name="eTime"]').val());
            var ret = true;

            if (start == '' && end != '') {
                dialog.warn('请选择开始时间');
                ret = false;
            } else if (start != '' && end == '') {
                dialog.warn('请选择结束时间');
                ret = false;
            }

            if (ret == false) {
                e.stopImmediatePropagation();
            }
            var tabIndex = $('.current-item').data('tab');
            if( tabIndex == 0){
                cardTotal();
            }
        });

        function cardTotal(){
            //新加总计;
            var data = formData.get('#memberForm');
            data.search_tradeType = 3;
            $.ajax({
                type:"get",
                url:BASE_PATH + '/shop/stats/card/coupon-recharge/totalMember',
                data: data,
                success:function(result){
                    if(result.success){
                        var total = $('.total-box');
                        //充值金额
                        total.find('.recharge').text((result.data.rechargeAmount).toFixed(2));
                    }
                }
            })
        }
    });
});
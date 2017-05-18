/**
 *
 * Created by ende on 16/7/12.
 */
$(function () {
    // 初始时从 localStorage 获取json 字符串
    // 转为 json 对象
    var tableDisplay = {
        shopOrderGoodsDetail: JSON.parse(localStorage.shopOrderGoodsDetail || '{}'),
        shopOrderServicesDetail: JSON.parse(localStorage.shopOrderServicesDetail || '{}')
    };
    var exportsUrl = {
        ".goods-content": BASE_PATH + '/shop/stats/order/goods/export',
        ".services-content": BASE_PATH + '/shop/stats/order/services/export',
    };
    var istqmall = $("#istqmall").val();

    var orderStatus;
    var _params = {
        payStartTime: util.getPara('payStartTime'),
        payEndTime: util.getPara('payEndTime')
    };

    if(istqmall == "true"){
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
    }else{
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

    if($("#start-time").val()== "" && $("#start-pay-time").val() == ""){
        $("input[name=orderCreateEndDate]").val(getDateStr(0));

        $("input[name=orderCreateStartDate]").val(getDateStr(-7));
    }

    if(!_params.startTime && _params.payStartTime) {
        $('input[name="orderSettleStartDate"]').val( _params.payStartTime );
        $('input[name="orderSettleEndDate"]').val( _params.payEndTime );

        $("input[name=orderCreateEndDate]").val('');
        $("input[name=orderCreateStartDate]").val('');
    }

    tableDisplayInit(tableDisplay.shopOrderGoodsDetail, 'goods');
    tableDisplayInit(tableDisplay.shopOrderServicesDetail, 'services');

    $('.js-more-options').on('click', function () {
        var target = $(this).data('target');

        if ($(target).hasClass('hide')) {
            $(target).removeClass('hide');

            plusTominus(this);
            $('.goods-content')
                .find('.search-form .margin-bt-70')
                .removeClass('margin-bt-70');
        } else {
            $(target).addClass('hide');

            minusToplus(this);
            $('.goods-content')
                .find('.search-form .show-grid')
                .eq(1).addClass('margin-bt-70');
        }
    });

    $('.js-reset').on('click', function () {
        var inputs = $('.content input');
        inputs.each(function () {
            $(this).val('');
        });

        $(this).parents('.content').find('.js-search-btn').click();
    });

    seajs.use(['formData', 'ajax', 'table', 'dialog', 'select', 'date'],
        function (formData, ajax, table, dialog, select, date) {
        var goodsCatType = ['.js-cat-1', '.js-cat-2', '.js-cat-3'],
            manager = ['.js-worker', '.js-receiver', '.js-goods-receiver', '.js-saler', '.js-worker-2',
                '.js-receiver-2'];
        var servicesFirst = true;
        var catTypeData = {};
        var catTypeSelect = [];
        var n = null;

        var selectUrl = {
            manager: BASE_PATH + '/shop/manager/get_manager',
            catType: BASE_PATH + '/shop/goods_category/std/get_by_key',
            brand: BASE_PATH + '/shop/goods_brand/shop_list',
            serviceCat: BASE_PATH + '/shop/shop_service_cate/get_by_name'
        };

        dialog.titleInit();

        manager.forEach(function (e) {
            select.init({
                url: selectUrl.manager,
                dom: e,
                showKey: 'id',
                showValue: 'name',
                pleaseSelect: true
            });
        });

        select.init({
            dom: '.js-select-order-status',
            showValue: 'value',
            showKey: 'key',
            data: orderStatus
        });

        // 配件类型
        // 是数据全部返回的
        // 不是一级一级去获取，而是将获取的数据保存
        catTypeSelect[2] = select.init({
            dom: goodsCatType[2],
            showKey: 'id',
            showValue: 'name',
            data: []
        });

        catTypeSelect[1] = select.init({
            dom: goodsCatType[1],
            showKey: 'id',
            showValue: 'name',
            data: [],
            callback: function (key, value) {
                var index = $(this).data('index');
                $(goodsCatType[2]).val('');

                catTypeData.tt = catTypeData.t.list[index];

                catTypeSelect[2].showOptions(n, n, n, catTypeData.tt.list, false);

            }
        });

        catTypeSelect[0] = select.init({
            url: selectUrl.catType,
            dom: goodsCatType[0],
            showKey: 'id',
            showValue: 'name',
            retData: catTypeData,
            callback: function (target, key) {
                var index = $(this).data('index');
                $(goodsCatType[1]).val('');
                $(goodsCatType[2]).val('');

                catTypeData.t = catTypeData.data[index];

                catTypeSelect[1].showOptions(n, n, n, catTypeData.t.list, false);

            }
        });

        select.init({
            url: selectUrl.brand,
            dom: '.js-goods-brand',
            showKey: 'id',
            showValue: 'brandName',
            pleaseSelect: true
        });

        select.init({
            url: selectUrl.serviceCat,
            dom: '.js-service-cat',
            showKey: 'id',
            showValue: 'name',
            pleaseSelect: true
        });

        table.init({
            formid: 'goodsForm',
            fillid: 'goodsFill',
            pageid: 'goodsPage',
            tplid: 'goodsTpl',
            url: BASE_PATH + '/shop/stats/order/goods/list',
            dataType: 'json',
            ajax: {
                contentType: 'application/json',
                type: 'post'
            },
            data: {
                pageSize: 10
            },
            totalPages: 'totalPage',
            totalElements: 'totalSize',
            callback: function () {
                tableDisplayInit(tableDisplay.shopOrderGoodsDetail, 'goods');
            }
        });

        $('.js-search-btn').on('click', function (e) {
            var parent = $(this).parents('.search-form');
            var start = $.trim( parent.find('input[name="orderCreateStartDate"]').val() );
            var end = $.trim( parent.find('input[name="orderCreateEndDate"]').val() );
            var start2 = $.trim( parent.find('input[name="orderSettleStartDate"]').val() );
            var end2 = $.trim( parent.find('input[name="orderSettleEndDate"]').val() );
            var ret = true;

            if(start == '' && end != ''){
                dialog.warn('请选择开单开始时间');
                ret = false;
            } else if(start != '' && end == '') {
                dialog.warn('请选择开单结束时间');
                ret = false;
            } else if(start2 == '' && end2 != '') {
                dialog.warn('请选择结算开始时间');
                ret = false;
            } else if(start2 != '' && end2 == '') {
                dialog.warn('请选择结算结束时间');
                ret = false;
            }

            if(ret == false) {
                e.stopImmediatePropagation();
            }

            var tabIndex = $('.current-item').data('tab');
            if(tabIndex == 0){
                goodsTotal();
            }else if( tabIndex == 1){
                serviceTotal();
            }
        });

        $('.tab-item').on('click', function () {
            var target = $(this).data('target');
            var tabIndex = $(this).data('tab');
            var current = $('.current-item');
            currentContent =target;

            current.removeClass('current-item');
            $(this)
                .parents('.content').addClass('hide');

            $(target).removeClass('hide')
                .find('.tab-item').eq(tabIndex)
                .addClass('current-item');

            if(tabIndex == 0){
                goodsTotal();
            }else if( tabIndex == 1){
                serviceTotal();
            }
            // 第一次切换到服务明细的时候再请求
            if(servicesFirst && target.indexOf('service') > -1) {
                servicesFirst = false;
                table.init({
                    formid: 'servicesForm',
                    fillid: 'servicesFill',
                    pageid: 'servicesPage',
                    tplid: 'servicesTpl',
                    url: BASE_PATH + '/shop/stats/order/services/list',
                    dataType: 'json',
                    ajax: {
                        contentType: 'application/json',
                        type: 'post'
                    },
                    data: {
                        pageSize: 10
                    },
                    totalPages: 'totalPage',
                    totalElements: 'totalSize',
                    callback: function () {
                        tableDisplayInit(tableDisplay.shopOrderServicesDetail, 'services');
                    }
                });
            }
        });

        // 时间选择
        for (var i = 0; i < 4; i++) {
            var start = 'start' + (i > 0 ? i : '');
            var end = 'end' + (i > 0 ? i : '');
            date.dpStartEnd({
                start: start,
                end: end
            });
        }

        // 当前contend
        var currentContent =".goods-content";
        // 密码登录导出
        exportSecurity.tip({'title':'导出报表信息'});
        exportSecurity.confirm({
            dom: '.js-excel',
            title: '经营报表—销售明细表',
            callback: function(json){
                var data = formData.get($(currentContent));
                var url = exportsUrl[currentContent];
                var iframe = $('<iframe></iframe>'), str = '';

                // 参数拼接
                for (var i in data) {
                    str = str + '&' + i + '=' + data[i];
                }
                iframe.attr('src', url + '?' + str);
                $(document.body).append(iframe);
            }
        });

        // 列表字段设置选择框 显示
        $('.js-list-option').click(function () {
            var target = $(this).parents('.content')
                .find( $(this).data('target') );

            if (target.hasClass('hide')) {
                plusTominus(this);
                target.removeClass('hide');
            } else {
                minusToplus(this);
                target.addClass('hide');
            }
        });

        /* 表单展示事件 */
        $('.dropdown-menu input[type=checkbox]').on('click', function () {
            var table = $(this).parents('.content').find('table');
            var local = table.data('localStorage'),
                field = $(this).data('ref');

            displayReport.call(this, table);

            tableDisplay[local][field] = $(this).is(':checked');
            // 保存
            localStorage[local]        = JSON.stringify(tableDisplay[local]);
        });

            goodsTotal();

            function goodsTotal(){
                //新加总计（配件销售明细）
                var data = formData.get('#goodsForm');
                $.ajax({
                    type:"post",
                    url:BASE_PATH + '/shop/stats/order/goods/orderGoodsTotal',
                    data: JSON.stringify(data), 
                    dataType: 'json', 
                    contentType: "application/json",
                    success:function(result){
                        if(result.success){
                            var total = $('.goods-box');
                            //销售总额
                            total.find('.gross-sales').text((result.data.payAmount).toFixed(2));
                            //成本
                            total.find('.cost').text((result.data.goodsAmount).toFixed(2));
                            //毛利
                            total.find('.gross-profit').text((result.data.grossAmount).toFixed(2));
                        }
                    }
                })
            }

            function serviceTotal(){
                //新加总计（配件销售明细）
                var data = formData.get('#servicesForm');
                $.ajax({
                    type:"post",
                    url:BASE_PATH + '/shop/stats/order/services/orderServiceTotal',
                    data: JSON.stringify(data),
                    dataType: 'json',
                    contentType: "application/json",
                    success:function(result){
                        if(result.success){
                            var total = $('.service-box');
                            ////工时费
                            total.find('.total-hours-price').text((result.data.data.payAmount).toFixed(2));
                            //工时
                            total.find('.total-hours').text((result.data.data.costAmount).toFixed(2));
                            //优惠
                            total.find('.preferential').text((result.data.data.discountAmount).toFixed(2));
                        }
                    }
                })
            }

    });
    // 列表字段设置初始化
    function tableDisplayInit(data, desc) {
        var i;
        for(i in data) {
            if(!data[i]) {
                $('.' + desc + '-content .dropdown')
                    .find('input[data-ref="' + i + '"]')
                    .removeAttr('checked');
                $('.' + desc + '-content table')
                    .find('.' + i).addClass('hide');
            }
        }
    }

    /* 显示隐藏对应的列 */
    function displayReport(table) {
        var $this = $(this),
            ref = $this.data('ref'),
            $ref = table ? table.find('.' + ref) : $('.' + ref),
            checked = $this.prop('checked');
        checked ? $ref.removeClass('hide') : $ref.addClass('hide');
    }

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

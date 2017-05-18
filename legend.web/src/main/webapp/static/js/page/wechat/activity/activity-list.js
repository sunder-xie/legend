
$(function(){
    var page,
        myDialog;
    var modules = [
        {module: 'service', parentIdName: 'actId', childIdName: 'serviceId', reportUrl: '/shop/wechat/op/qry-activity-appoint-data',
            reportTpl: 'serviceReportTpl', detailUrl: '/shop/wechat/op/qry-act-appoint-list', detailTpl:'serviceDetailTpl' },
        {module: 'game', parentIdName: 'gameId', childIdName: 'couponInfoId', reportUrl: '/shop/wechat/op/qry-game-activity-coupon-list',
            reportTpl: 'gameReportTpl', detailUrl: '/shop/wechat/op/qry-game-coupon-user-list', detailTpl: 'gameDetailTpl'},
        {module: 'discount', parentIdName: 'actId', childIdName: 'serviceId', reportUrl: '/shop/wechat/op/qry-bargin-appoint-data',
            reportTpl: 'discountReportTpl', detailUrl: '/shop/wechat/op/qry-bargin-appoint-list', detailTpl: 'discountDetailTpl'},
        {module: 'groupBuy', parentIdName: 'actId', childIdName: 'serviceId', reportUrl: '/shop/wechat/op/qry-activity-groupbuy-share-data',
            reportTpl: 'groupBuyReportTpl', detailUrl: '/shop/wechat/op/qry-act-groupbuy-user-detail-list', detailTpl: 'groupBuyDetailTpl'},
        {module: 'discountCoupon', parentIdName: 'actId', childIdName: 'couponTplId', reportUrl: '/shop/wechat/op/qry-bargin-coupon-data',
            reportTpl: 'discountCouponReportTpl', detailUrl: '/shop/wechat/op/qry-bargin-coupon-users', detailTpl: 'discountCouponDetailTpl'}
    ];
    if(history.state){
        $('#activityType').val(history.state.activityType);
        $('#isJoin').val(history.state.isJoin);
    }
    seajs.use(['table', 'dialog', 'select', 'check', 'art', 'date'], function (table, dialog, select, check, art, date) {
        dialog.titleInit();
        check.init();
        // 下拉选择
        select.init({
            dom: '.js-status',
            showKey: 'key',
            showValue: 'name',
            data: [{
                key: 0,
                name: '全部'
            }, {
                key: 1,
                name: '未开始'
            }, {
                key: 2,
                name: '正在进行'
            }, {
                key: 3,
                name: '已结束'
            }]
        });
        table.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/wechat/op/qry-activity-list',
            //表格数据目标填充id，必需
            fillid: 'tableList',
            //分页容器id，必需
            pageid: 'paging',
            //表格模板id，必需
            tplid: 'tableTpl',
            //关联查询表单id，可选
            formid: 'searchForm',
            isfirstfill: true,
            //渲染表格数据完后的回调方法,可选
            callback : function(){
                var activityType =  $('#activityType').val(),
                    isJoin = $('#isJoin').val();
                history.replaceState({activityType: $('#activityType').val(), isJoin: $('#isJoin').val()}, null, location.href);
                $('.js-tab-item').removeClass('current');
                $('.head-tab').eq(activityType-1).addClass('current');
                $('.activity-tab-item').eq(isJoin-1).addClass('current');
                $.each($('.preview-qrcode-small'), function(){
                    var $this = $(this);
                    if($this.data('url')){
                        $this.qrcode({
                            width: 60,
                            height: 60,
                            text: $this.data('url')
                        });
                    }
                });
                $.each($('.preview-qrcode'), function(){
                    var $this = $(this);
                    if($this.data('url')){
                        $this.qrcode({
                            width: 200,
                            height: 200,
                            text: $this.data('url')
                        });
                    }
                })

            }

        });

        // 配置的日期
        date.dpStartEnd({
            start: 'startTime',
            end: 'endTime',
            startSettings: {
                dateFmt: 'yyyy-MM-dd',
                maxDate: '#F{$dp.$D(\'endTime\')}'
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd',
                minDate: '#F{$dp.$D(\'startTime\')}'
            }
        });

        $(document)
            //table的报表查看按钮
            .on('click','.js-report',function(){
                var $this = $(this);
                if($this.hasClass('yqx-btn-disable')){
                    return;
                }
                var id = $this.data('id'),
                    module = modules[$this.data('type')-1],
                    data = {};

                data[module.parentIdName] = id;

                $.ajax({
                    url: BASE_PATH + module.reportUrl,
                    data: data,
                    dataType: 'json',
                    success: function (json) {
                        if (json && json.success) {
                            var html = art(module.reportTpl, $.extend({json: json}, {parentId: id}));
                            dialog.open({
                                area: ['650px', '480px'],
                                content: html
                            });
                        } else {
                            dialog.fail(json.errorMsg || '查询失败');
                        }
                    },
                    error: function () {
                        dialog.fail('查询失败');
                    }
                })
            })
            //table的查看按钮
            .on('click', '.js-view', function(){
                var $this = $(this);
                if($this.hasClass('yqx-btn-disable')){
                    return false;
                }
                //如果是砍价和拼团
                if($this.data('type') == 3 || $this.data('type') == 4 || $this.data('type') == 5){
                    var actTplId = $this.data('tplId');
                    $.ajax({
                        url: BASE_PATH + '/shop/wechat/op/get-join-activity-limit?actTplId=' + actTplId,
                        dataType: 'json',
                        success: function (json) {
                            if (json && json.success && json.data) {
                                var code = json.data.code;
                                if(code == '0'){
                                    location.href = BASE_PATH + '/shop/wechat/activity-detail?actTplId=' + actTplId;
                                } else {
                                    var html = art('bounceTpl', {data: json.data});
                                    myDialog = dialog.open({
                                        area: ['400px', 'auto'],
                                        content: html
                                    });
                                }

                            } else {
                                dialog.fail(json.errorMsg || '查询失败');
                            }
                        }
                    })
                }
            })
            //浮层的查看按钮
            .on('click', '.js-customer-detail', function(){
                var $this = $(this);
                if($this.hasClass('no-count')){
                    return;
                }
                page.init.call($this[0]);
            })

            //分享朋友圈
            .on('click', '.js-share', function(){
                if($(this).hasClass('yqx-btn-disable')){
                    return;
                }

                var actId = $(this).data('id');
                $.ajax({
                    url: BASE_PATH + '/shop/wechat/op/get-activity-url?isFormal=1&actId=' + actId,
                    dataType: 'json',
                    contentType: 'application/json',
                    type:'POST',
                    success: function (json) {
                        if (json && json.success) {
                            var url = json.data;
                            var html = art('qrcodeTpl', {url: url});
                            myDialog = dialog.open({
                                area: ['590px', '280px'],
                                content: html
                            });
                            //Render in canvas
                            $("#qrcodeView div").qrcode({
                                width: 170,
                                height: 170,
                                text: url
                            });
                            //复制链接
                            $("#copyUrl").zclip({
                                path: BASE_PATH + '/static/third-plugin/zclip/ZeroClipboard.swf',
                                copy:function(){
                                    return $('#linkAddress').val();
                                },
                                afterCopy: function(){
                                    dialog.msg('复制成功');
                                }
                            });
                        } else {
                            dialog.fail(json.errorMsg || '请求失败');
                        }
                    },
                    error: function () {
                        dialog.fail('请求失败');
                    }
                })

             })
            .on({
                'mouseover': function () {
                    $(this).find('.preview-qrcode').show();
                },
                'mouseout': function () {
                    $(this).find('.preview-qrcode').hide();
                }
            },'.preview-qrcode-td');

        page = (function(){
            var args = {
                current: 0,
                lastPage: true,
                data: {},
                url: '',
                tpl: ''
            };
            var func = {
                go: function(p){
                    $.ajax({
                        url: BASE_PATH + args.url + '?page='+p,
                        data: args.data,
                        dataType: 'json',
                        success: function (json) {
                            if (json && json.success) {
                                var html = art(args.tpl, {json: json});
                                $('.customerDetail').html(html);
                                args.current = p;
                                if(json.data){
                                   args.lastPage = json.data.lastPage;
                                }
                                func.buttonCss();
                            } else {
                                dialog.fail(json.errorMsg || '查询失败');
                            }
                        },
                        error: function () {
                            dialog.fail('查询失败');
                        }
                    })

                },
                bindEvent: function(){
                    $(document)
                        .off('click.report', '.js-pre-page').on('click.report', '.js-pre-page', function(){
                            if($(this).hasClass('disable')){
                                return;
                            }
                            func.go(args.current - 1);
                        })
                        .off('click.report', '.js-next-page').on('click.report', '.js-next-page', function(){
                            if($(this).hasClass('disable')){
                                return;
                            }
                            func.go(args.current + 1);
                        })
                },
                buttonCss: function(){
                    if(args.current == 1){
                        $('.js-pre-page').addClass('disable');
                    }
                    if(args.lastPage){
                        $('.js-next-page').addClass('disable');
                    }
                }
            };

            return{
                init: function(){
                    var type = $('#type').val(),
                        module = modules[type-1];

                    args.url = module.detailUrl;
                    args.tpl = module.detailTpl;
                    args.data[module.parentIdName] = $('#parentId').val();
                    args.data[module.childIdName] = $(this).data('id');
                    func.bindEvent();
                    func.go(1);
                }
            }
        })();

    });

    $(document)
        .on('click', '.js-tab-item', function () {
        var $this = $(this),
            value = $this.data('value'),
            target = $this.data('target');
        $('#'+target).val(value);

        $('.js-search-btn').trigger('click');
    })


});


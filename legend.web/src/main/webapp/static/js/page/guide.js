/**
 * Created by ende on 16/5/10.
 */

$(function () {
    var isYBD = $('#isYBD').val() == 'true';
    var base = {
        counts: BASE_PATH + '/guide/count?noteType=',
        market: BASE_PATH + '/marketing/ng/maintain/list?noteType=',
        lists: BASE_PATH + '/guide',
        permission: BASE_PATH + '/shop/func/check_func_list',
        stockWarning: BASE_PATH + '/guide/stockwarning-list',
        singlePermission: BASE_PATH + '/shop/func/check_func'
    };
    var countsAjaxUrls = {
        reception: base.counts + 1,
        newOrder: base.counts + 2,
        settle: base.counts + 3,
        visit: base.counts + 4,
        stockIn: base.counts + 5,
        goodsShortage: base.counts + 6,
        goodsOut: base.counts + 7
    };
    var btnPermissionFuncName = {
        reception: ['接待首页', '派工首页', '接车维修首页'],
        newOrder: ['接待首页', '派工首页', '接车维修首页'],
        settle: ['结算首页', '财务首页'],
        visit: ['提醒中心'],
        stockIn: ['仓库首页'],
        goodsShortage: ['仓库首页'],
        goodsOut: ['仓库首页']
    };

    var detailUrls = {
        appoint: {
            permission: ['接待首页', '派工首页'],
            tqmall_permission: ['接车维修'],
            detail: base.lists + '/appoint-list',
            link: BASE_PATH + '/shop/appoint/appoint-detail?appointId='
        },
        orderlist: {
            permission: ['派工首页', '接待首页'],
            tqmall_permission: ['接车维修'],
            detail: base.lists + '/order-list',
            link: BASE_PATH + '/shop/order/detail?orderId='
        },
        maintain: {
            permission: ['提醒中心'],
            detail: base.market + '1',
            link: BASE_PATH + '/marketing/ng/maintain/center?target=maintain&id='
        },
        insurance: {
            permission: ['提醒中心'],
            detail: base.market + '2',
            link: BASE_PATH + '/marketing/ng/maintain/center?target=insurance-expire&id='
        },
        auditing: {
            permission: ['提醒中心'],
            detail: base.market + '3',
            link: BASE_PATH + '/marketing/ng/maintain/center?target=yearly-inspection-expire&id='
        },
        stockWarning: {
            permission: ['仓库首页'],
            detail: base.stockWarning
        }
    };

    var countsText = {
        reception: [
            '今日已预检',
            '辆车'
        ],
        newOrder: [
            '今日已开',
            '个工单'
        ],
        settle: [
            '今日已结清',
            '单'
        ],
        visit: [
            '今日需回访',
            '位客户'
        ],
        stockIn: [
            '今日已入库',
            '类配件'
        ],
        goodsShortage: [
            '今日在售配件',
            '类'
        ],
        goodsOut: [
            '今日已领料配件',
            '类'
        ]
    };
    var listDescs = {
        appoint: [
            'appointTimeFormat', 'license', 'customerName', 'mobile',
            'appointContent', 'statusName'
        ],
        orderlist: [
            'carLicense', 'orderStatusName', 'carInfo', 'customerName',
            'customerMobile', 'createTimeStr', 'orderAmount'
        ],
        maintain: [
            'carLicense', 'noteTimeStr', 'customerName', 'mobile',
            'carModel', 'mileage', 'quickVisit'
        ],
        insurance: [
            'carLicense', 'noteTimeStr', 'customerName', 'mobile',
            'carModel', 'lastPayTimeStr', 'quickVisit'
        ],
        auditing: [
            'carLicense', 'noteTimeStr', 'customerName', 'mobile',
            'carModel', 'lastPayTimeStr', 'quickVisit'
        ]
    };
    var isTqmall = $('#isTqmall').val() == 'false' || !$('#isTqmall').val() ? false : true;
    var countData = {},
        permissionData = {},
        permissionBtn = {join: false};
    var warnHTML = {
        nodata: '<i class="warn">暂无提醒</i>',
        permission: ['<i class="warn">', '</i>']
    }, countHTML = ['<span class="red">','</span>'], loadding;
    var warnText = {
        btn  : ['亲，此账号还未分配“', '”的权限。'],
        tqmall: '您的账号没有登陆此入口权限，请联系管理员进行开通',
        list : ['亲，此账号还未分配查看“', '”的权限。']
    }, descToText = {
        appoint: '预约提醒',
        orderlist: '工单提醒',
        maintain : '保养提醒',
        insurance: '保险提醒',
        auditing : '年检提醒',
        tqmall_appoint: '接车维修',
        tqmall_orderlist: '接车维修',
        stockWarning:'库存预警',
        btn_reception : '接车维修',
        btn_newOrder : '维修开单',
        btn_settle    : '结算',
        btn_visit     : '客户回访',
        btn_stockIn   : '采购入库',
        btn_goodsShortage : '库存查询',
        btn_goodsOut  : '领料出库'
    };

    if(localStorage.processDisplay === 'false') {
        hideProcess();
    } else {
        $('.process').removeClass('hide');
    }
    // 更新的时间
    var guideDate = '2016-07-26';
    // 引导是否显示
    var helpTimer = setInterval(function() {
        if (localStorage.publiceNoticeShowed == 'true') {
            var right = +$('.yqx-header .header-entry')[0].offsetLeft;
            var headerToRight = ($('.yqx-header')[0].clientWidth - 980)/2;
            $('.yqx-help-box .yqx-home-help').css('left', right + headerToRight - 127);
            $('.yqx-help-box').removeClass('hide');

            clearInterval(helpTimer);
        }
    }, 600);

    seajs.use(['art', 'dialog'], function (art, dialog) {
        dialog.titleInit();

        art.helper('isTqmall', function () {
            return isTqmall;
        });

        $('.js-count').hover(function () {
            var desc = $(this).attr('data-desc');
            var $title = $(this).find('.title');

            getBtnPermission(desc, 0, btnPermissionFuncName, isTqmall);
            if ($title.text()) {
                $title.show();
            }
            if(isTqmall && desc == 'visit'){
                return;
            }
            getCountData(desc, function() {
                var html = countsText[desc][0] + countHTML[0]
                    + countData[desc]
                    + countHTML[1] + countsText[desc][1];
                $title[0].innerHTML = html;
            });

        }, function () {
            $(this).find('.title').hide();
        });

        /**
         * 点击引导图标跳转
         */
        $('.js-count').click(function () {
            var desc = $(this).attr('data-desc');
            var version =$(this).data('version');
            // tqmall 权限判断
            if (isTqmall && permissionBtn[desc] === false) {
                if(desc == 'join') {
                    window.location.href = BASE_PATH + '/home/join?refer=guide#客户营销';
                } else {
                    dialog.warn(warnText.tqmall);
                }
                return;
            } else if (permissionBtn[desc] === false) {
                dialog.warn(warnText.btn[0] + descToText['btn_' + desc] + warnText.btn[1]);
                return;
            } else if(permissionBtn[desc] !== true) {
                return;
            }
            var refer = util.getPara("refer");
            if(refer == 'yunxiu-dialog'){
                refer = 'yunxiu-dailog-guide';
            } else {
                refer = 'guide';
            }
            if (desc == 'reception') {
                window.location.href = BASE_PATH + '/shop/reception?refer=' + refer;
            } else if (desc == 'newOrder') {
                window.location.href = BASE_PATH + '/shop/order/order-list?refer=' + refer;
            } else if (desc == 'settle') {
                window.location.href = BASE_PATH + '/shop/settlement/debit/order-list?refer=' + refer;
            } else if (desc == 'visit') {
                if(isYBD){
                    window.location.href = BASE_PATH + '/marketing/gather/plan?refer=' + refer + '&tag=visit&tab=0';
                } else {
                    window.location.href = BASE_PATH + '/marketing/ng/maintain/center?refer=' + refer + '&target=visit';
                }
            }else if (desc == 'stockIn') {
                window.location.href = BASE_PATH + '/shop/warehouse/in/in-list?refer=' + refer;
            } else if (desc == 'goodsShortage') {
                window.location.href = BASE_PATH + '/shop/warehouse/stock/stock-query?refer=' + refer;
            }else if (desc == 'goodsOut') {
                window.location.href = BASE_PATH + '/shop/warehouse/out/out-list?refer=' + refer;
            }

        });

        /**
         * 查看更多
         */
        $('.js-list-more').click(function () {
            var desc = $(this).attr('data-desc');
            if (desc == 'appoint') {
                //预约提醒
                window.location.href = BASE_PATH + '/shop/appoint/appoint-list?refer=guide';
            } else if (desc == 'order') {
                //工单提醒
                window.location.href = BASE_PATH + '/shop/order/order-list?refer=guide';
            } else if (desc == 'maintain') {
                //保养提醒
                if(isYBD){
                    window.location.href = BASE_PATH + '/marketing/gather/plan?refer=guide&tag=maintain&tab=0';
                } else {
                    window.location.href = BASE_PATH + '/marketing/ng/maintain/center?refer=guide&target=maintain';
                }
            } else if (desc == 'insurance') {
                //保险提醒
                if(isYBD){
                    window.location.href = BASE_PATH + '/marketing/gather/plan?refer=guide&tag=insurance&tab=0';
                } else {
                    window.location.href = BASE_PATH + '/marketing/ng/maintain/center?refer=guide&target=insurance-expire';
                }
            } else if (desc == 'auditing') {
                //年检提醒
                if(isYBD){
                    window.location.href = BASE_PATH + '/marketing/gather/plan?refer=guide&taget=inspection&tab=0';
                } else {
                    window.location.href = BASE_PATH + '/marketing/ng/maintain/center?refer=guide&target=yearly-inspection-expire';
                }

            } else if (desc == 'stockWarning') {
                //库存预警
                window.location.href = BASE_PATH + '/shop/warehouse/stock/stock-warning';
            }
        });

        // 去采购
        $(document).on('click', '.guide-table .js-to-purchasing', function (e) {
            e.stopPropagation();
        });

        // 点击列表跳转
        $(document).on('click', '.js-tr', function () {
            var id = $(this).attr('data-id');
            // tag 用于样板店调整
            var tag = $('.tab-item.current-item').data('tag');
            var desc = $(this).parents('div').attr('data-desc');
            var data = $(this).data();

            data.tab = 0;
            data.tag = tag;
            data.visitDialog = true;

            if(isYBD && tag) {
                location.href = BASE_PATH + '/marketing/gather/plan?'
                    + $.param(data)
                    + '&refer=guide';
            } else if (id && desc && detailUrls[desc] && detailUrls[desc].link) {
                location.href = detailUrls[desc].link + id + '&refer=guide';
            }
        });

        $(document).on('click', '.stock-warning .js-tr', function (e) {
            var id = $(this).data('id');
            window.location.href = BASE_PATH + "/shop/goods/goods-toedit?goodsid=" + id;
            e.stopImmediatePropagation();
        });

        // 隐藏banner
        $(document).on('click', '.js-banner-del', function(e) {
            $(this).remove();

            $('.head-banner').slideUp();
            // 调整 content 和 footer 的间距
            if($('.process').is(':hidden')) {
                $(document.body).css('height', '386px');
            }

            // 防止点到 link
            e.preventDefault();
            e.stopPropagation();
        });

        // tab 切换
        $('.js-tab').on('click', function () {
            var desc = $(this).data('desc');
            var nowClass = 'current-item';
            var nowTable = 'table-current', global = $(this).index('.tab-item') == 0 ? false : true;

            if (!$(this).hasClass(nowClass)) {
                $('.' + nowClass).removeClass(nowClass);
                $(this).addClass(nowClass);

                $('.' + nowTable).removeClass(nowTable)
                    .addClass('hide');
                $('.' + desc).removeClass('hide').addClass(nowTable)
            }

            if($('.' + desc).find('tbody tr').length) {
                return;
            }
            getListPermission(desc, dealListPermission.bind(this, desc), global);
        });

        $(document)
            .ajaxStart(function () {
                //请求发送之前加载loadding动画
                loadding && dialog.close(loadding);
                loadding = dialog.load();
            })
            .ajaxStop(function () {
                loadding && dialog.close(loadding);
            });

        $('.js-process').on('click', function() {
            var $process = $('.process');

            if($process.css('display') !== 'none') {
                $process.slideUp();
                // 和隐藏.process相关的动作
                hideProcess(true);

                // 保存状态
                localStorage.processDisplay = 'false';
            } else {
                $process.slideDown();

                $(document.body).css('height', '100%');
                $('.process-show').addClass('hide');
                localStorage.processDisplay = 'true';
            }
        });

        $(window).on('resize', function() {
            var height = $('.header')[0].clientHeight;

            $('.yqx-wrapper').css('padding-top', height + 'px');
        });

        $('.js-tab').eq(0).click();

    });

    /************ 函数部分 start **************/
    // t：slideUp时不至于直接隐藏.process，使得没有动画效果
    function hideProcess(t) {
        var bodyHeight = document.body.clientHeight;
        var $process = $('.process');
        var processHeight = $process[0].clientHeight;

        if($process.css('display') !== 'none' && !t) {
            $process.hide();
        }
        // 减小一些和footer的间距
        $(document.body).css('height', bodyHeight - processHeight + 'px');
        // 显示右侧业务流程
        $('.process-show').removeClass('hide');
    }
    function dealListPermission(desc) {
        var desc_2 = isTqmall ? 'tqmall_' + desc : desc;
        // 判断权限
        if (!permissionData[desc]) {
            $('.' + desc).find('.tablebottom').remove();
            if($('.' + desc).find('.warn').length) {
                return;
            }
            $('.' + desc).append($(
                warnHTML.permission[0] +
                    warnText.list[0] + descToText[desc_2] + warnText.list[1]
                + warnHTML.permission[1]
            ));
            return;
        }

        $.ajax({
            url: detailUrls[desc].detail,
            data: {
                size: 8,
                page: 1
            },
            success: function (json) {
                if (!json) {
                    return;
                }
                var data = json.data;
                var len = data.length
                    || (data.list && data.list.length)
                    || (data.content && data.content.length) || 0;
                var table = $('.' + desc).find('.guide-table');

                var tpl = 'tableTpl';

                if (json && json.success
                    && data && len) {
                    var arr = data.length ? data
                        : (data.list && data.list.length ? data.list : data.content);
                    var arr_id;

                    var html;
                    seajs.use('art', function (art) {
                        if(desc == 'stockWarning') {
                            tpl = desc + 'Tpl';

                            if(json.data && json.data.content && json.data.content.length) {
                                arr_id = json.data.content.map(function (e) {
                                    return e.id;
                                });
                            }

                            html = art(tpl, {json: json});
                        } else {
                            html = art(tpl, {
                                arr: arr,
                                descs: listDescs[desc],
                                // 检测今日到店的函数
                                getFormatDate: getFormatDate
                            });
                        }
                    });

                    table.find('tbody').empty();
                    table.append($(html));

                    if(desc == 'stockWarning' && arr_id) {
                        getStockWarningPrice(arr_id);
                    }

                    table.parent().find('.tablebottom .hide').removeClass('hide');

                    // 有`今日到店`的话加长
                    if (table.find('.has-tip').length) {
                        table.find('.width-time').css('width', '210px');
                    }

                } else {
                    if($('.' + desc).find('.warn').length) {
                        return;
                    }
                    $('.' + desc).find('.tablebottom').remove();
                    $('.' + desc).append($(warnHTML.nodata));
                }
            },
            error: function () {
                if($('.' + desc).find('.warn').length) {
                    return;
                }
                $('.' + desc).find('.tablebottom').remove();
                $('.' + desc).append($(warnHTML.nodata));
            }
        });
    }

    // 列表权限
    function getListPermission(name, fn, global) {
        var data = [];

        if (permissionData[name] === false || permissionData[name] === true) {
            fn && fn();
            return;
        }
        if(isTqmall) {
            data = detailUrls[name].tqmall_permission;
        } else {
            data = detailUrls[name].permission;
        }

        if(!data) {
            return;
        }
        $.ajax({
            url: base.permission,
            data: {
                funcNameList: data.join(',')
            },
            global: global || false,
            success: function (json) {
                permissionData[name] = json.success;
                setSamePermission(name, 'btn');
                fn && fn();
            },
            error: function () {
                permissionData[name] = false;
            }
        });
    }

    // 流程按钮权限
    function getBtnPermission(name, i, data) {
        var orginName = name;
        var permission = permissionBtn;

        data = data || btnPermissionFuncName;

        if (permission[orginName] === true || permission[orginName] === false) {
            return;
        }

        $.ajax({
            url: base.permission,
            data: {
                funcNameList: data[name].join(',')
            },
            global: false,
            success: function (json) {
                if (json) {

                    switch(orginName) {
                        case 'reception':
                        case 'newOrder':
                            permission.newOrder = permission.reception = json.success;
                            break;
                        case 'stockIn':
                        case 'goodsShortage':
                        case 'goodsOut':
                            permission.stockIn = permission.goodsShortage = permission.goodsOut = json.success;
                            break;
                        default:
                            permission[orginName] = json.success;
                            break;
                    }

                    setSamePermission(orginName, 'list');
                }
            },
            error: function () {
                permission[orginName] = false;
            }
        });

    }

    function setSamePermission(name, type) {
        // btn 设置 list 权限
        if (type === 'list') {
            switch (name) {
                case 'reception':
                case 'newOrder':
                    permissionData.orderlist = permissionBtn[name];
                    permissionData.appoint = permissionBtn[name];
                    break;
                default:
                    break;
            }
        }
        if (type === 'btn') {
            switch (name) {
                case 'orderlist':
                case 'appoint':
                    permissionBtn.orderlist = permissionData[name];
                    permissionBtn.appoint = permissionData[name];
                    break;
                default:
                    break;
            }
        }
    }

    function getCountData(desc, fn) {
        if(!countsAjaxUrls[desc]) {
            return;
        }

        $.ajax({
            url: countsAjaxUrls[desc],
            global: false,
            success: function (json) {
                if (json && json.success) {
                    countData[desc] = json.data;
                    delete countsAjaxUrls[desc];

                    fn && fn();
                }
            }
        })

    }

    function getFormatDate(data) {
        var today = data ? new Date(data) : new Date();
        var day = today.getDate();
        var month = today.getMonth() + 1;
        var year = today.getFullYear();

        return year + '-' + month + '-' + day;
    }

    function getStockWarningPrice(itemIds) {
        $.ajax({
            type: 'get',
            url: BASE_PATH + '/shop/warehouse/stock/stock-warning-inventoryprice',
            data: {itemids: itemIds.join(",").toString()},
            success: function (result) {
                if (result.success) {
                    var goodses = result["data"];
                    for (var goodsIndex in goodses) {
                        var goods = goodses[goodsIndex];
                        var goodsId = goods["goodsId"];
                        var tqmallGoodsId = goods["tqmallGoodsId"];
                        if (typeof(tqmallGoodsId) != 'undefined' && tqmallGoodsId != "" && tqmallGoodsId != null ) {
                            var tqmallPrice = goods["tqmallPrice"];
                            var row = $("#TR_" + goodsId);
                            if(typeof(tqmallPrice) !='undefined' && tqmallPrice !="" && tqmallPrice !="null"){
                                tqmallPrice = '&yen;' + tqmallPrice;
                            }else{
                                tqmallPrice = '--';
                            }

                            var appendHtml = '<p class="money-font has-next-link">' + tqmallPrice + '</p><p><a href="http:\/\/www.tqmall.com\/Goods\/detail.html?id='
                                + tqmallGoodsId + '" class=\"green procurement js-to-purchasing\" target=\"_blank\">去采购》</a></p>';

                            $('.js-inventory-price', row).html(appendHtml);
                        }
                    }
                } else {
                    return false;
                }
            }
        });
    }

});
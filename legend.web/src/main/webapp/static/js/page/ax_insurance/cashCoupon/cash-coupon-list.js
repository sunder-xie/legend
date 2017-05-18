$(function() {
    seajs.use(['art', 'dialog', 'select', 'paging'], function(template, dg, select, pg) {
        var selectVal, orderTpl, nowCashInfo, successTpl;
        template.helper("timeFormat", function( timestamp, format ) {
            var  date;
            if(arguments.length ==1) {
                format = timestamp;
                date = new Date();
            } else {
                date = new Date(timestamp);

            }
            var map = {
                Y: date.getFullYear(),
                M: date.getMonth() + 1,                     // 月份
                D: date.getDate(),                          // 日
                d: date.getDay(),                           // 周
                h: date.getHours(),                         // 时
                m: date.getMinutes(),                       // 分
                s: date.getSeconds(),                       // 秒
                q: Math.floor( (date.getMonth()+3)/3 ),     // 季度
                S: date.getMilliseconds()                   // 毫秒
            };
            return format.replace(/([YMDdhms])+/g, function(v, s) {
                var val = map[s];
                if(s === "Y") {
                    return ("" + val).substr(4 - v.length);
                } else if( val !== undefined ) {
                    val = "0" + val;
                    return v.length > 1 ? val.substr(val.length - 2) : val;
                }
                return v;
            });
        });
        template.helper('parseStatus', function(id) {
            var str;
            switch (id) {
                case 0:
                    str = '未用';
                    break;
                case 1:
                    str = '已使用';
                    break;
                case 2:
                    str = '已过期';
                    break;
                case 3:
                    str = '已结算';
                    break;
                default:
                    str = '未知';
            }
            return str;
        });
        template.helper('stringify', function(data) {
            return JSON.stringify(data);
        });
        select.init({
            dom: '.coupon-state',
            data: [
                {
                    id: null,
                    name: '全部'
                },
                {
                    id: 0,
                    name: '未用'
                },
                {
                    id: 1,
                    name: '已使用'
                },
                {
                    id: 2,
                    name: '已过期'
                },
                {
                    id: 3,
                    name: '已结算'
                }
            ],
            callback: function(val, des) {
                selectVal = val;
                $('.condition-active').removeClass('condition-active');
                renderHTML(1);
            }
        });
        getInofo();
        renderHTML(0);
        $(document)
            .on('click', '.condition-btn', function() {
                selectVal = undefined;
                if($(this).hasClass('condition-active')) {
                    $('.condition-active').removeClass('condition-active');
                } else {
                    $('.condition-active').removeClass('condition-active');
                    $(this).addClass('condition-active');
                }
                $('.coupon-state').val('全部');
                renderHTML(2);
            })
            .on('click', '.use-cash-coupon', function() {
                var $target = $(this), url = 'getCashCouponFormByShopId';
                $.ajax(url).done(function(res) {
                    if(res.success) {
                        if(parseCash($target)) {
                            orderTpl = dg.open({
                                content: template('couponOrderTpl', res),
                                offset: '200px'
                            });
                        } else {
                            dg.fail('解析现金券信息失败');
                        }

                    }else{

                        dg.fail(res.errorMsg);
                    }
                })
            })
            .on('change', '.like-checkbox', function() {
                $('.like-checkbox').not($(this)).prop('checked', false);
                $('.choice-use').prop('disabled', !$(this).prop('checked'));
            })
            .on('click', '.order-tr', function() {
                if($(event.target).hasClass('like-checkbox')) return;
                $(this).find('.like-checkbox').trigger('click');
            })
            .on('click', '.choice-close', function() {
                orderTpl && dg.close(orderTpl);
            })
            .on('click', '.choice-use', function() {
                var url = 'useCashCoupon', $oneCheck = $('.like-checkbox:checked');
                if($oneCheck.length != 1) {
                    return dg.fail('请选择一张现金券');
                }
                $.get(url, {
                    orderSn: $oneCheck.val(),
                    cashCouponSn: nowCashInfo.cashCouponSn,
                    cashCouponFee: nowCashInfo.faceAmount
                }).done(function(res) {
                    if(res.success) {
                        dg.close(orderTpl);
                        showSuccessTpl();
                    } else {
                        dg.fail(res.message);
                    }
                })
            })
            .on('click', '.cash-jump-btn', function() {
                if($(this).hasClass('purchase')) {
                    open(BASE_PATH + '/home/avoid/tqmall/index');
                } else {
                    open('cashRebateCount')
                }
            })
        function showSuccessTpl() {
            successTpl = dg.open({
                offset: '200px',
                content: template('cashSuccessTpl', nowCashInfo)
            });
            getInofo();
            renderHTML(0);
        }
        function parseCash($tar) {
            return nowCashInfo = $tar.closest('tr').data('item'),nowCashInfo.cashCouponSn ? true : false;
        }
        function getInofo(back) {
            var url = 'getShopCashCouponInfo';
            $.ajax(url).done(function(res) {
                if(res.success) {
                    for(var i in res.data) {
                        $('.' + i).text(res.data[i]||0);
                    }
                } else {
                    dg.fail(res.errorMsg);
                }
            });
        }
        function renderHTML (type, page, back) {
            var url = 'queryCashCouponPage';
            $.ajax(url, {
                data: getData(type, page||1)
            }).done(function(res) {
                if(res.success) {
                    $('.cash-table tbody').html(template('cashCouponListTpl', res.data));
                    paging(res.data);
                    back && back(res.data);
                } else {
                    dg.fail(res.errorMsg);
                }
            })
        }
        function getData(type, p) {
            var d = {
                page: p
            };
            if(type == 2) {
                if($('.condition-active').length) {
                    d.searchDayType = $('.condition-active').data('val');
                }
            } else if(type == 1) {
                d.cashCouponStatus = selectVal;
            } else {
                if($('.condition-active').length) {
                    d.searchDayType = $('.condition-active').data('val');
                } else {
                    selectVal != undefined? d.cashCouponStatus = selectVal : '';
                }
            }
            return d;
        }
        function paging(data) {
            pg.init({
                dom: $('#pagingBox'),
                itemSize: data.totalElements,
                pageCount: data.totalPages,
                current: data.number,
                backFn: function (p) {
                    renderHTML(0, p);
                }
            });
        }
    })
})
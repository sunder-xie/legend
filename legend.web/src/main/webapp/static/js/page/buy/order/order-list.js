/**
 * Created by sky on 16/11/27.
 */

$(function () {
    seajs.use(['art', 'paging', 'dialog', 'ajax'], function (tpl, pg, dg) {

        tpl.helper('getFlag', function (flag) {
            var base = BASE_PATH + '/static/img/common/buy/{0}.png',
                imgSrcs = {
                    FINANCEHD: 'financehd',
                    YXCGJHD: 'yxcgjhd',
                    FUCHSHDYXDD: 'fuchshdyxdd',
                    HHDD: 'hhdd',
                    SPRAY: 'spray',
                    AEXCHANGEA: 'hhdd'
                },
                thisSrc = 'tqorder', key;

            for (key in imgSrcs) {
                if (!imgSrcs.hasOwnProperty(key)) {
                    continue;
                }
                if (flag == key) {
                    thisSrc = imgSrcs[key];
                    break;
                }
            }
            

            return base.format(thisSrc);
        });

        tpl.helper('$toString', function (data) {
            try {
                return JSON.stringify(data);
            } catch (ex) {
                return data;
            }
        });

        dg.titleInit();

        var exchangeDg,
            model = {
                orderList: function () {
                    return $.ajax({
                        url: BASE_PATH + '/shop/buy/my_order_info',
                        data: {
                            page: requestListData.page,
                            search_orderFlag: requestListData.orderFlag,
                            search_status: requestListData.status
                        }
                    });
                },
                getWaitingInWarehouse: function (orderId) {
                    return $.ajax({
                        url: BASE_PATH + '/shop/buy/batch_insert_list',
                        data: {
                            orderIds: orderId
                        }
                    });
                },
                confirmReceived: function (data) {
                    return $.ajax({
                        url: BASE_PATH + '/shop/buy/confirm_revice',
                        data: data
                    });
                },
                batchInWarehouse: function (data) {
                    return $.ajax({
                        url: BASE_PATH + '/shop/goods/batch_add_with_attr_car/ng',
                        data: JSON.stringify(data),
                        type: 'POST',
                        contentType: 'application/json'
                    });
                }
            };

        /* ^列表信息 */
        var requestListData = {
            status: 'ALL',
            page: 1,
            orderFlag: ''
        };

        // 数据初始化
        function requestListDataRest(status) {
            requestListData.page = 1;
            if (status) {
                requestListData.status = status;
            }
        }

        // 列表请求
        function requestList(callback) {
            model.orderList().done(function (result) {
                var html;
                if (result.success) {
                    html = tpl('orderListTpl', result);
                    $('#orderListBox').html(html);
                    paging(result.data);
                    callback && callback(result);
                } else {
                    dg.fail(result.errorMsg);
                }
            });
        }

        // 列表分页
        function paging(data) {
            pg.init({
                dom: $('#pagingBox'),
                itemSize: data.totalElements,
                pageCount: data.totalPages,
                current: data.number + 1,
                backFn: function (p) {
                    requestListData.page = p;
                    requestList();
                }
            });
        }

        /* $列表信息 */

        /* ^批量入库 */
        function batchInWarehouse(data, uid, orderSn) {
            var batchData = [];
            // 批量数据处理
            if (data && data.length) {
                $.each(data, function (i, item) {
                    var goodsAttrList = item.goodsAttrList,
                        carModelList = item.carModelList,
                        batchDataItem = {
                            tqmallGoodsId: item.goodsId,
                            name: item.goodsName,
                            measureUnit: item.minMeasureUnit,
                            origin: item.original,
                            cat1Id: item.cat1Id,
                            cat1Name: item.cat1Name,
                            cat2Id: item.cat2Id,
                            cat2Name: item.cat2Name,
                            format: item.goodsFormat,
                            goodsSn: item.goodsSn,
                            goodsStatus: 0,
                            tqmallGoodsSn: item.goodsSn,
                            tqmallStatus: 1,
                            brandId: item.brandId,
                            brandName: item.brandName,
                            imgUrl: item.goodsImg,
                            stdCatId: item.catId
                        };

                    if (goodsAttrList && goodsAttrList.length) {
                        $.each(goodsAttrList, function (i, subItem) {
                            subItem.attrName = subItem.name;
                            subItem.attrValue = subItem.value;
                            delete subItem.name;
                            delete subItem.value;
                        });

                        batchDataItem.goodsAttrRelList = JSON.stringify(goodsAttrList);
                    }

                    if (carModelList && carModelList.length) {
                        $.each(carModelList, function (i, subItem) {
                            subItem.carBrandId = subItem.id;
                            subItem.carBrandName = subItem.name;
                            delete subItem.id;
                            delete subItem.name;
                        });

                        batchDataItem.goodsCarList = JSON.stringify(carModelList);
                    }

                    batchData.push(batchDataItem);
                });
            }


            model.batchInWarehouse(batchData)
                .done(function (result) {
                    if (result.success) {
                        location.href = BASE_PATH + "/shop/warehouse/in/tqmall/stock?uid=" + uid + "&orderSn=" + orderSn + "&refer=orderlist";
                    } else {
                        dg.fail(result.errorMsg);
                    }
                });
        }

        /* $批量入库 */

        /* ^初始化 */
        (function initialize() {
            requestListDataRest();
            requestList();
        })();
        /* $初始化 */

        $(document)
        // 根据订单状态获取列表信息
            .on('click', '#conditionList li', function () {
                var $this = $(this),
                    status = $this.data('condition');
                requestListDataRest(status);
                requestList(function () {
                    $this.addClass('active')
                        .siblings('.active').removeClass('active');
                });
            })
            // 换货清单
            .on('click', '.js-exchange-list-btn', function () {
                var result = $(this).data('refundGoods');
                exchangeDg = dg.dialog({
                    wrapClass: 'dialog-exchange-list-wrap',
                    area: ['500px', 'auto'],
                    content: tpl('exchangeListTpl', {data: JSON.parse(result)})
                });
            })
            // 关闭换货清单弹窗
            .on('click', '.js-exclose-btn', function () {
                dg.close(exchangeDg);
            })
            // 签收入库
            .on('click', '.js-put-in-btn', function () {
                var $this = $(this),
                    flag = $this.siblings('input[name=flag]').val(),
                    uid = $this.siblings('input[name=uid]').val(),
                    orderSn = $this.siblings('input[name=orderSn]').val(),
                    orderId = $this.siblings('input[name=orderId]').val();
                // 若配件已经存在，直接跳转采购入库页面
                if (!+flag) {
                    location.href = BASE_PATH + '/shop/warehouse/in/tqmall/stock?uid={0}&orderSn={1}&refer=orderList'.format(uid, orderSn);
                    return;
                }

                model.getWaitingInWarehouse(orderId)
                    .done(function (result) {
                        if (result.success) {
                            batchInWarehouse(result.data, uid, orderSn);
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    });
            })
            // 确认收货
            .on('click', '.js-received-btn', function () {
                var $this = $(this), data = $this.data();

                dg.confirm('是否确认收货？', function () {
                    model.confirmReceived(data)
                        .done(function (result) {
                            if (result.success) {
                                dg.success('确认收货成功');
                                // 配货中数目
                                var $phz = $('#PHZ i'),
                                // 已签收数目
                                    $yqs = $('#YQS i');

                                $phz.text(+$phz.text() - 1);
                                $yqs.text(+$yqs.text() + 1);
                                requestList();
                            } else {
                                dg.fail(result.errorMsg);
                            }
                        });
                });
            });

    });
});

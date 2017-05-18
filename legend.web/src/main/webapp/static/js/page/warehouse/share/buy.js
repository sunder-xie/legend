$(function () {
    var baseUrl = BASE_PATH + '/shop/warehouse/share';

    seajs.use([
        'table',
        'art',
        'dialog'
    ], function (table, art, dialog) {
        table.init({
            url: baseUrl + '/getShareGoodsList',
            formid: 'onSaleListForm',
            pageid: 'onSaleListPage',
            fillid: 'onSaleListFill',
            tplid: 'onSaleListTpl',
            data: {
                size: 10
            },
            callback: function () {
                setUnitWidth($('#onSaleListFill'), function (max) {
                    var saleNum = $('.yqx-table th.sale-num');

                    saleNum.css('padding-right', max + 7);
                });
            }
        });

        // 车型信息的拼接
        art.helper('getCarInfo', function (str) {
            var ret = '', t;
            try {
                // 假如是 json 串
                var data = JSON.parse(str);

                if(data == '0') {
                    ret = '通用';
                } else if(typeof data == 'object' && data.length) {
                    for (var i in data) {
                        if (data[i].carBrandName) {
                            // '|'分隔
                            if (ret != '') {
                                ret += '|';
                            }
                            // 品牌，车系，别名
                            t = [data[i].carBrandName, data[i].carSeriesName, data[i].carAlias];
                            ret += t.filter(function (e) {
                                if (e) {
                                    return e;
                                }
                            }).join(' ');
                        }
                    }
                }
            } catch(e) {
                ret = str;

                if(ret == '0') {
                    ret = '通用';
                }
            }

            return ret;
        });

        $(document).on('click', '.js-detail', function (e) {
            var id = $(this).data('id');
            toDetail(id);

            e.stopImmediatePropagation();
        })
            .on('click', '.js-detail-tr', function () {
                var id = $(this).find('.js-detail').data('id');

                toDetail(id);
            });

        $('.js-detail-back').on('click', function () {
            $('.detail-main').addClass('hide');
            $('.warehouse').removeClass('hide');
        });

        $('.js-region').on('click', function () {
            var current = $('.selected.region');

            if(current[0] == this) {
                return;
            }

            current.removeClass('selected');
            $(this).addClass('selected');

            $(this).parents('.buy-form').find('input[name=region]')
                .val( $(this).data('region') );

            $('.buy-form .js-search-btn').trigger('click');
        });

        // 查看详情
        function toDetail(id) {
            $.ajax({
                url: baseUrl + '/goods/detail',
                data: {
                    id: id
                },
                success: function (json) {
                    if (json.success) {
                        var html = art('detailTpl', {item: json.data});

                        $('.detail-main')
                            .find('.detail-box').html(html)
                            .end().removeClass('hide');

                        var distance = setShopRange('map', json.data);

                        // 距离
                        if(distance !== undefined) {
                            $('#distance').removeClass('hide')
                                .find('.text').text(distance);
                        }

                        $('.detail-main .detail-btn').removeClass('hide');
                        $('.warehouse').addClass('hide');
                    } else {
                        dialog.fail(json.message || '查看失败');
                    }
                }
            });
        }
    });
    function setUnitWidth(box, fn) {
        var max = 0;
        box.find('.measure-unit').each(function () {
            var width = this.offsetWidth;

            if(width > max) {
                max = width;
            }
        }).css({
            width: max,
            textAlign: 'left'
        });

        if(fn && typeof fn == 'function') {
            fn(max);
        }
    }

    // baidu 地图设置
    function setShopRange(id, data) {
        try {
            if (data.longitude !== null && data.latitude !== null) {
                $('#'+id)
                    .removeClass('hide')
                    .parents('tr').addClass('has-map');

                var map = new BMap.Map(document.getElementById(id));
                var shop = new BMap.Point(data.longitude, data.latitude);

                var size = new BMap.Size(-24, -19);
                var mark = new BMap.Marker(shop);
                var markLabel = new BMap.Label(data.contactName);

                markLabel.setOffset(size);
                mark.setLabel(markLabel);

                // 启用地图拖拽
                map.enableDragging();
                // 启用滚轮放大缩小
                map.enableScrollWheelZoom();
                // 缩放级别：4，国
                map.setMinZoom(4);

                map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_ZOOM}));

                map.centerAndZoom(shop, 12);
                map.addOverlay(mark);

                if (data.selfLongitude !== null && data.selfLatitude != null) {
                    var self = new BMap.Point(data.selfLongitude, data.selfLatitude);
                    map.addOverlay(new BMap.Marker(self));

                    return ( map.getDistance(shop, self) / 1000 ).toFixed(1);
                }
            }
        } catch(e) {
            return undefined;
        }
    }
});
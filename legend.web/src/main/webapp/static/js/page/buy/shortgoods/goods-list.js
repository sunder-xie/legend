/**
 * Created by sky on 16/11/28.
 */

$(function () {
    seajs.use(['art', 'dialog', 'select', 'formData', 'table', 'ajax'], function (tpl, dg, st, fd, tb) {
        dg.titleInit();

        function closeGoodsType() {
            $('.goods_type_input').siblings('.goods_type_part_wind').remove();
        }

        st.init({
            dom: '.js-goods-brand',
            url: BASE_PATH + '/shop/goods_brand/inwarehosue/list',
            showValue: 'brandName',
            canInput: true,
            beforeSelectClick: closeGoodsType
        });

        st.init({
            dom: '.js-goods-status',
            data: [
                {
                    id: '',
                    name: '显示所有配件'
                },
                {
                    id: '1,3',
                    name: '显示淘汽配件'
                },
                {
                    id: '0,2,4,5',
                    name: '显示非淘汽配件'
                }
            ],
            beforeSelectClick: closeGoodsType,
            selectedKey: ''
        });

        tb.init({
            url: BASE_PATH + '/shop/warehouse/stock/stockwarning/search/json',
            fillid: 'searchListBody',
            pageid: 'pagingBox',
            tplid: 'goodsListTpl',
            formid: 'searchForm',
            callback: function () {
                // 获取采购价
                var itemIds = [];
                $.each($('input[name="itemId"]'), function () {
                    itemIds.push(this.value)
                });
                if (itemIds.length > 0) {
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

                                    // 当前行
                                    var row = $("#TR_" + goodsId);
                                    // 采购价
                                    if (tqmallGoodsId) {
                                        var tqmallPrice = goods["tqmallPrice"];
                                        if (typeof(tqmallPrice) != 'undefined' && tqmallPrice != "" && tqmallPrice != "null") {
                                            tqmallPrice = '&yen;' + tqmallPrice;
                                        } else {
                                            tqmallPrice = "暂无报价";
                                        }
                                        $('td[name="inventoryPrice"]', row).html(tqmallPrice);
                                    }else{
                                        $('td[name="inventoryPrice"]', row).html("暂无报价");
                                    }
                                }
                            } else {
                                return false;
                            }
                        }
                    });
                }
            }
        });

        $(document)
            // 搜索淘汽配件库
            .on('click', '.js-search-goods-lib', function () {
                var q = $('input[name="search_likeKeyWords"]').val(),
                    brandId = $('input[name="search_brandId"]').val(),
                    catSecId = $('input[name="search_stdCatId"]').val(),
                    url;

                catSecId == null && (catSecId = '');

                if (q != '') {
                    url = 'http://www.tqmall.com/Search.html?q={0}&catSecId={1}&brandId={2}&source=legend'.format(q, catSecId, brandId);
                } else {
                    url = 'http://www.tqmall.com/List.html?catSecId={0}&brandId={1}&source=legend'.format(catSecId,brandId);
                }

                window.open(url, '_blank');
            });
    });
});

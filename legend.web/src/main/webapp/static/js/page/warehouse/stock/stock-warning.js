/**
 * 库存预警
 * zmx 2016-08-08
 */

$(function () {
    var doc = $(document);
    seajs.use([
        'select',
        'table',
        'dialog'
    ], function (st, tb, dg) {

        // 配件品牌
        st.init({
            dom: '.js-brand',
            url: BASE_PATH + '/shop/goods_brand/inwarehosue/list',
            showKey: "id",
            pleaseSelect: true,
            showValue: "brandName",
            canInput:true
        });

        // 预警数量
        st.init({
            dom: '.js-warning',
            data: [{id: "1-10", name: "1-9"},
                {id: "10-20", name: "10-19"},
                {id: "20-30", name: "20-29"},
                {id: "30-50", name: "30-49"},
                {id: "50", name: "50及以上"}],
            selectedKey: 1,
            showKey: "id",
            pleaseSelect: true,
            showValue: "name"
        });

        //溢出隐藏
        dg.titleInit();

        //表格
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/warehouse/stock/stockwarning/search/json',
            //表格数据目标填充id，必需
            fillid: 'tableCon',
            //分页容器id，必需
            pageid: 'pagingCon',
            //表格模板id，必需
            tplid: 'tableTpl',
            //关联查询表单id，可选
            formid: 'listForm',
            enableSearchCache: true,
            beforeSetSearchCache: function (data, formId) {
                // 自定义配件的时候，其配件类型查询条件 name 修改
                var isCustom = false;
                for(var i in data) {
                    if(data[i]) {
                        if (data[i].name == 'customCat' && data[i].value == 'true') {
                            isCustom = true;
                            break;
                        }
                    }
                }

                if(isCustom) {
                    $('#search_catId').attr('name', 'search_catId');
                }
            },
            data: {
                size: 12
            },
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
                            var unit = '';
                            if (result.success) {
                                var goodses = result["data"];
                                for (var goodsIndex in goodses) {
                                    var goods = goodses[goodsIndex];
                                    var goodsId = goods["goodsId"];
                                    var tqmallGoodsId = goods["tqmallGoodsId"];
                                    var suggestGoodsNumber = goods["suggestGoodsNumber"];
                                    var averageNumber = goods["averageNumber"];

                                    // 当前行
                                    var row = $("#TR_" + goodsId);
                                    // 单位
                                    unit = ' ' + (row.data('unit') || '');

                                    // 采购价
                                    if (tqmallGoodsId) {
                                        var tqmallPrice = goods["tqmallPrice"];
                                        if (typeof(tqmallPrice) != 'undefined' && tqmallPrice != "" && tqmallPrice != "null") {
                                            tqmallPrice = '&yen;' + tqmallPrice;
                                        } else {
                                            tqmallPrice = "";
                                        }

                                        var appendHtml = '<p class="money-font">' + tqmallPrice + '<\/p><p><a href="http:\/\/www.tqmall.com\/Goods\/detail.html?id='
                                            + tqmallGoodsId + '" class=\"green procurement js-toPurchasing\" target=\"_blank\">去采购》<\/a><\/p>';
                                        $('td[name="inventoryPrice"]', row).html(appendHtml);
                                    }

                                    // 建议采购
                                    if ($.isNumeric(suggestGoodsNumber) && Number(suggestGoodsNumber) > 0) {
                                        $('td[name="suggestGoodsNumber"]', row).html(suggestGoodsNumber + unit)
                                    } else {
                                        $('td[name="suggestGoodsNumber"]', row).html("不采购")
                                    }
                                    $('td[name="averageNumber"]', row).html(averageNumber + unit);
                                }
                            } else {
                                return false;
                            }
                        }
                    });
                }
            }
        });

        // 采购
        $(document).on('click', '.js-caigou', function () {
            // 获取选中配件ID
            var goodsIds = [];
            $.each($(':checkbox[name="checkId"]'), function () {
                if (this.checked) {
                    goodsIds.push(this.value)
                }
            });

            if (goodsIds.length > 0) {
                window.location.href = BASE_PATH + "/shop/warehouse/in/in-edit/blue?goodsIds=" + goodsIds;
            } else {
                dg.warn("请选择需要采购的配件");
            }

        });


        //全选
        doc.on('click', '.js-select-all', function () {
            var trAll = $(this);
            if (trAll.is(':checked')) {
                //全选
                $(".line-select").prop("checked", true);
            } else {
                //全取消
                $(".line-select").prop("checked", false);
            }
        });

        doc.on('click', '.line-select', function (event) {
            event.stopPropagation();
            if (!($(this).is(':checked'))) {
                $('.js-select-all').prop("checked", false);
            }
        });

        doc.on('click', '.detail-list', function () {
            var goodsId = $(this).data('itemid');
            $.ajax({
                url: BASE_PATH + "/shop/goods/ispaint",
                type: 'GET',
                data: {
                    goodsId: goodsId
                },
                success: function (result) {
                    if (result.success) {
                        var isPaint = result["data"];
                        if (isPaint) {
                            window.location.href = BASE_PATH + "/shop/goods/editBPGoodsPage?id=" + goodsId;
                        } else {
                            window.location.href = BASE_PATH + "/shop/goods/goods-toedit?goodsid=" + goodsId;
                        }
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        });

        doc.on('click', '.js-toPurchasing', function (event) {
            event.stopPropagation();
        });

        doc.on('click','.js-in-edit',function(ev){
            ev.stopPropagation();
        });

        // 导出excel
        $(document).on('click', '.export-excel', function () {
            // gather query param
            var queryParamArray = gatherQueryParam();

            // 分类
            var catIdName = $("#search_catId").attr("name");

            window.location.href = BASE_PATH + "/shop/warehouse/stock/stockwarning_export?"
                + catIdName + "=" + queryParamArray[catIdName]
                + "&search_brandId=" + queryParamArray["search_brandId"]
                + "&search_carInfoLike=" + queryParamArray["search_carInfoLike"]
                + "&search_goodsNameLike=" + queryParamArray["search_goodsNameLike"]
                + "&search_shortageNumberScope=" + queryParamArray["search_shortageNumberScope"]
                + "&page=" + $("#search_page").val();
        });


        // 获取搜索条件
        function gatherQueryParam() {
            // 配件类型
            var catId = $("#search_catId").val();
            // 配件品牌
            var brandId = $('input[name="search_brandId"]').val();
            // 车型
            var carInfoLike = $('input[name="search_carInfoLike"]').val();
            // 预警数量范围
            var search_shortageNumberScope = $('input[name="search_shortageNumberScope"]').val();
            // 配件名称
            var goodsNameLike = $('input[name="search_goodsNameLike"]').val();
            var data = {
                search_brandId: brandId || '',
                search_carInfoLike: carInfoLike || '',
                search_goodsNameLike: goodsNameLike || '',
                search_shortageNumberScope: search_shortageNumberScope || ''
            };

            var catIdName = $("#search_catId").attr("name");
            data[catIdName] = catId;

            return data;
        }

    });
});
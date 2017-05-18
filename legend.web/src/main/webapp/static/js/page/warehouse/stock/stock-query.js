/**
 * 库存盘点
 * zmx 2016-08-08
 */

$(function () {
    var doc = $(document);
    // 选择的配件
    var selectedGoods = {};

    seajs.use([
        'select',
        'table',
        'dialog',
        'formData',
        'art'
    ], function (st, tb, dg, fd, at) {

        // 配件品牌
        st.init({
            dom: '.js-brand',
            url: BASE_PATH + '/shop/goods_brand/inwarehosue/list',
            showKey: "id",
            pleaseSelect: true,
            showValue: "brandName",
            canInput:true
        });
        // 仓库货位
        st.init({
            dom: '.js-depot',
            url: BASE_PATH + '/shop/goods/get_depot_list',
            showValue: "name",
            canInput:true
        });

        // 上架状态
        st.init({
            dom: '.js-sale',
            data: [{id: "", name: "全部"}, {id: "1", name: "上架"}, {id: "0", name: "下架"}],
            showKey: "id",
            showValue: "name"
        });

        // 库存状态
        st.init({
            dom: '.js-stock',
            data: [{id: "1", name: "非0库存"}, {id: "0", name: "0库存"}],
            showKey: "id",
            pleaseSelect: true,
            showValue: "name"
        });

        //超出隐藏
        dg.titleInit();
        //表格
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/goods/search/json',
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
                // 已选中的配件
                $('#tableCon .detail-list').each(function () {
                    var id = $(this).data('itemid');

                    if (selectedGoods[id]) {
                        $(this).find('.js-goods').prop('checked', true);
                    }
                });

                // 上下架状态
                var saleStatus = $('input[name="search_onsaleStatus"]').val();
                if (saleStatus == "0") {
                    $(".js-select-all").attr("disabled", true);
                }
                $('#tableCon .js-money-font').each(function () {
                    var val = +$(this).text();
                    $(this).text(val.priceFormat());
                });
            }
        });

        // 获取搜索条件
        function gatherQueryParam() {
            var catId = $("#search_catId").val();
            var brandId = $('input[name="search_brandId"]').val();
            var carInfoLike = $('input[name="search_carInfoLike"]').val();
            var goodsNameLike = $('input[name="search_goodsNameLike"]').val();
            var goodsFormatLike = $('input[name="search_goodsFormatLike"]').val();
            var depotLike = $('input[name="search_depotLike"]').val();
            var zeroStockRange = $('input[name="search_zeroStockRange"]').val();
            var onsaleStatus = $('input[name="search_onsaleStatus"]').val();
            var data = {
                search_brandId: brandId || '',
                search_carInfoLike: carInfoLike || '',
                search_goodsNameLike: goodsNameLike || '',
                search_goodsFormatLike: goodsFormatLike || '',
                search_depotLike: depotLike || '',
                search_zeroStockRange: zeroStockRange || '',
                search_onsaleStatus: onsaleStatus || ''
            };

            var catIdName = $("#search_catId").attr("name");
            data[catIdName] = catId;

            return data;
        }

        // 配件合并
        $(document).on('click', '.merge', function () {
            // 获取选中配件ID
            var goodsIds = getSelectedIdByArray();

            if (goodsIds.length > 1) {
                var goodsIdsStr = goodsIds.join(',');

                $.ajax({
                    url: BASE_PATH + "/shop/goods/getGoodsListByIds",
                    type: 'GET',
                    dataType: 'JSON',
                    data: {
                        goodsIds: goodsIdsStr
                    },
                    success: function (result) {
                        if (result.success) {
                            var html = at('combineTpl', result);
                            dg.open({
                                area: ['570px', 'auto'],
                                content: html
                            })
                            $("#oldGoodsIds").val(goodsIdsStr);
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            } else {
                dg.warn("请选择需要合并的配件, 至少两个配件");
            }

        });

        // 配件合并
        $(document).on('click', '.js_merge', function () {
            var oldGoodsIds = $("#oldGoodsIds").val();
            var newGoodsId = $("#newGoodsId").val();
            var mergeTargetName = $("#newGoodsId").find("option:selected").text();
            var data = {
                oldGoodsIds: oldGoodsIds,
                newGoodsId: newGoodsId
            }
            var confirmTip = "列表所有配件, 将合并到目标配件<B>(" + mergeTargetName + ")<\/B> 中 <br\/><br\/>您确定要合并么？";
            dg.confirm(confirmTip, function () {
                $.ajax({
                    url: BASE_PATH + "/shop/goods/do_merge_goods",
                    type: 'POST',
                    dataType: 'JSON',
                    data: data,
                    success: function (result) {
                        if (result.success) {
                            dg.success("操作成功");
                            window.location.hash = 1;
                            window.location.reload();
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            }), function () {
                return false;
            };
        });

        // 出库
        $(document).on('click', '.outstock', function () {
            // 获取选中配件ID
            var goodsIds = getSelectedIdByArray();

            if (goodsIds.length > 0) {
                window.location.href = BASE_PATH + "/shop/warehouse/out/out-other?goodsIds=" + goodsIds;
            } else {
                window.location.href = BASE_PATH + "/shop/warehouse/out/out-other"
            }
        });

        // 入库
        $(document).on('click', '.instock', function () {
            // 获取选中配件ID
            var goodsIds = getSelectedIdByArray();

            if (goodsIds.length > 0) {
                window.location.href = BASE_PATH + "/shop/warehouse/in/in-edit/blue?goodsIds=" + goodsIds;
            } else {
                window.location.href = BASE_PATH + "/shop/warehouse/in/in-edit/blue";
            }
        });

        // 库存盘点
        $(document).on('click', '.toinventory', function () {
            // 获取选中配件ID
            var goodsIds = getSelectedIdByArray();

            if (goodsIds.length > 0) {
                window.location.href = BASE_PATH + "/shop/warehouse/stock/stock-inventory-add?goodsids=" + goodsIds;
            } else {
                window.location.href = BASE_PATH + "/shop/warehouse/stock/stock-inventory-add"
            }
        });

        // 导出
        exportSecurity.tip({'title':'导出库存信息'});
        exportSecurity.confirm({
            dom: '.export-excel',
            title: '库存查询—配件库存',
            callback: function(json){
                // gather query param
                var queryParamArray = gatherQueryParam();

                // 分类
                var catIdName = $("#search_catId").attr("name");

                window.location.href = BASE_PATH + "/shop/goods/search_export?"
                    + catIdName + "=" + queryParamArray[catIdName]
                    + "&search_brandId=" + queryParamArray["search_brandId"]
                    + "&search_carInfoLike=" + queryParamArray["search_carInfoLike"]
                    + "&search_goodsNameLike=" + queryParamArray["search_goodsNameLike"]
                    + "&search_goodsFormatLike=" + queryParamArray["search_goodsFormatLike"]
                    + "&search_depotLike=" + queryParamArray["search_depotLike"]
                    + "&search_zeroStockRange=" + queryParamArray["search_zeroStockRange"]
                    + "&search_onsaleStatus=" + queryParamArray["search_onsaleStatus"];
            }
        });

        //全选
        doc.on('click', '.js-select-all', function () {
            var that = this;
            $('.js-goods').prop('checked', this.checked);

            $('.detail-list').each(function () {
                var id = $(this).data('itemid');

                selectedGoods[id] = that.checked;
            });
        });

        // 选择当前配件
        doc.on('click', '.js-goods', function (event) {
            var id = $(this).parents('tr').data('itemid');
            if (!this.checked) {
                $('.js-select-all').prop("checked", false);
            }
            selectedGoods[id] = this.checked;

            event.stopPropagation();
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

        function getSelectedIdByArray() {
            var ret = [];

            for (var id in selectedGoods) {
                if (selectedGoods[id]) {
                    ret.push(id);
                }
            }

            return ret;
        }
    });
});





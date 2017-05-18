/*
 * create by zmx 2017/1/5
 * 配件资料
 */

$(function () {
    var doc = $(document);

    seajs.use([
        'select',
        'table',
        'dialog'
    ], function (st, tb, dg) {
        dg.titleInit();
        // 配件品牌
        st.init({
            dom: '.js-brand',
            url: BASE_PATH + '/shop/goods_brand/inwarehosue/list',
            showKey: "id",
            showValue: "brandName",
            pleaseSelect: true,
            canInput: true
        });

        // 上架状态
        st.init({
            dom: '.js-sale',
            data: [{id: "2", name: "全部"}, {id: "1", name: "上架"}, {id: "0", name: "下架"}],
            showKey: "id",
            showValue: "name"
        });
        //库位
        st.init({
            dom: '.js-depot',
            url: BASE_PATH + '/shop/goods/get_depot_list',
            showValue: "name",
            canInput:true
        });

        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/goods/search/json',
            //表格数据目标填充id，必需
            fillid: 'tableListCon',
            //分页容器id，必需
            pageid: 'pagingCon',
            //表格模板id，必需
            tplid: 'tableTpl',
            //关联查询表单id，可选
            formid: 'formData',
            //记住搜索条件
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
            }
        });

        // 跳转淘汽配件采购页
        doc.on('click', '.js-tqmall-list', function () {
            window.location.href = BASE_PATH + "/shop/goods/goods-tqmall-list";
        });

        // 跳转配件新增页
        doc.on('click', '.js-add', function () {
            window.location.href = BASE_PATH + "/shop/goods/goods-toadd";
        });

        // 钣喷中心-油漆资料新增页面
        doc.on('click', '.js-paint-add', function () {
            window.location.href = BASE_PATH + "/shop/goods/toPaintGoodsAdd";
        });

        doc.on('click','.js-edit-tr',function(){
            var goodsId = $(this).data('itemId');
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

        // 配件上架
        doc.on('click', '.js-upshelf', function (event) {
            event.stopPropagation();
            var goodsId = $(this).parents('tr').data('itemId');
            $.ajax({
                url: BASE_PATH + "/shop/goods/upshelf",
                type: 'get',
                data: {
                    goodsid: goodsId
                },
                success: function (result) {
                    if (result.success) {
                        dg.success("上架成功");
                        window.location.reload();
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        });

        // 配件下架
        doc.on('click', '.js-downshelf', function (event) {
            event.stopPropagation();
            var goodsId = $(this).parents('tr').data('itemId');
            dg.confirm("您确认下架该配件嘛?", function () {
                $.ajax({
                    url: BASE_PATH + "/shop/goods/downshelf",
                    type: 'get',
                    data: {
                        goodsid: goodsId
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success("配件下架成功", function () {
                                window.location.reload();
                            });
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            });
        });

        // 配件编辑
        doc.on('click', '.js-edit', function (event) {
            event.stopPropagation();
            var goodsId = $(this).parents('tr').data('itemId');

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

        // 配件删除
        doc.on('click', '.js-delete', function (event) {
            event.stopPropagation();
            var goodsId = $(this).parents('tr').data('itemId');
            dg.confirm("您确认删除该配件嘛?", function () {
                $.ajax({
                    url: BASE_PATH + "/shop/goods/delete",
                    type: 'get',
                    data: {
                        id: goodsId
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success("配件删除成功", function () {
                                window.location.reload();
                            });
                        } else {
                            var code = result.code;
                            var errorMsg = result.errorMsg;
                            if (code == 10000) {
                                errorMsg = errorMsg + "<br/>该配件不能删除，你可以选择下架操作";
                                dg.warn(errorMsg);
                            } else {
                                dg.fail(errorMsg);
                            }
                        }
                    }
                });
            });
        });
    })


});
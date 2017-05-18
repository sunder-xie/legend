/*
 * create by zmx 2017/1/5
 * 配件资料
 */

$(function () {
    seajs.use([
        'check',
        'select',
        'table',
        'dialog',
        'art',
        "formData"
    ], function (check, st, tb, dg, at, fd) {
        var tqmallGoodsId;
        dg.titleInit();

        at.helper('$jsonToString', function (v) {
            return JSON.stringify(v)
        });

        var _currentTarget = "common";
        var _formActionUrl = BASE_PATH + "/shop/goods/common/save";
        var _formActionUrls = {
            "paint": BASE_PATH + "/shop/goods/paint/save",
            "common": BASE_PATH + "/shop/goods/common/save"
        };

        // 配件品牌
        st.init({
            dom: '.js-brand',
            url: BASE_PATH + '/shop/goods_brand/list',
            showKey: "id",
            showValue: "brandName",
            pleaseSelect: true,
            canInput: true
        });

        // 适配品牌
        $.ajax({
            url: BASE_PATH + "/shop/car_category/brand_letter",
            success: function (json) {
                if (json.success) {
                    //回填上次级别设置的数据。
                    var html = at("allBrandTpl", {"templateData": json.data});
                    $("#allBrand").html(html);
                    handleChoosenSelect("#allBrand");
                }
            }
        });

        //搜索插件
        function handleChoosenSelect(selectorName, width) {
            $(selectorName).each(function () {
                if ($(this).find('option').length > 1) {
                    $(this).chosen({
                        search_contains: true,
                        display_width: width, //默认是220px
                        allow_single_deselect: $(this).attr("data-with-diselect") === "1" ? true : false,
                        no_results_text: '没有搜索到结果'
                    });
                }
            });
        }



        //油漆等级
        st.init({
            dom: '.js-paint-level',
            data: [{
                id: "0",
                name: "油性漆"
            }, {
                id: "1",
                name: "水性漆"
            }],
            pleaseSelect: true
        });

        //油漆等级
        st.init({
            dom: '.js-paint-type',
            data: [{
                id: "0",
                name: "素色"
            }, {
                id: "1",
                name: "银粉"
            }, {
                id: "2",
                name: "珍珠"
            }, {
                id: "3",
                name: "金属"
            }, {
                id: "4",
                name: "弱色系"
            }],
            pleaseSelect: true
        });

        var _searchData;
        if($("input[name='search_goodsId']").val() != ''){
            _searchData = {
                search_goodsId :  $("input[name='search_goodsId']").val()
            }
        }

        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/goods/tqmall/search/json',
            //表格数据目标填充id，必需
            fillid: 'tableListCon',
            //分页容器id，必需
            pageid: 'pagingCon',
            //表格模板id，必需
            tplid: 'tableTpl',
            //关联查询表单id，可选
            formid: 'formData',
            //其他属性，如淘汽采购过来的配件id
            data: _searchData,
            //记住搜索条件
            enableSearchCache: true
        });

        // 编辑
        $(document).on('click', '.js-edit', function () {
            var legendGoodsId = $(this).parents('tr').data('referLegendId');
            $.ajax({
                url: BASE_PATH + "/shop/goods/ispaint",
                type: 'GET',
                data: {
                    goodsId: legendGoodsId
                },
                success: function (result) {
                    if (result.success) {
                        var isPaint = result["data"];
                        if (isPaint) {
                            window.location.href = BASE_PATH + "/shop/goods/editBPGoodsPage?id=" + legendGoodsId;
                        } else {
                            window.location.href = BASE_PATH + "/shop/goods/goods-toedit?goodsid=" + legendGoodsId;
                        }
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        });

        // 新增配件弹出框
        var dialogIndex;
        var _current_id;
        $(document).on('click', '.js-add', function () {
            // 淘汽配件
            tqmallGoodsId = $(this).parents('tr').data('itemId');
            var commonGoodsTemplate = $('#addInfoTpl').html();
            var paintGoodsTemplate = $('#addBPInfoTpl').html();

            //增加油漆
            $.ajax({
                url: BASE_PATH + "/shop/goods/isBPPrintGood",
                type: 'GET',
                data: {
                    goodsId: tqmallGoodsId
                },
                success: function (result) {
                    if (result.success) {
                        _current_id = tqmallGoodsId;
                        if (result.data) {
                            //弹出油漆配件
                            _currentTarget = "paint";
                            _formActionUrl = _formActionUrls["paint"];
                            dialogIndex = dg.open({
                                area: ['440px', '490px'],
                                content: paintGoodsTemplate
                            });
                        } else {
                            //弹出一般配件
                            _currentTarget = "common";
                            _formActionUrl = _formActionUrls["common"];
                            dialogIndex = dg.open({
                                area: ['400px', '240px'],
                                content: commonGoodsTemplate
                            });
                        }
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            })
        });

        // 删除
        $(document).on('click', '.js-delete', function () {
            var goodsId = $(this).parents('tr').data('referLegendId');
            dg.confirm("您确认删除已采购的配件嘛?", function () {
                $.ajax({
                    url: BASE_PATH + "/shop/goods/delete",
                    type: 'GET',
                    data: {
                        'id': goodsId
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success("配件删除成功");
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



        // 弹出框-保存配件
        $(document).on("click", ".js-dialog-save", function () {
            if (!check.check('.addGoodsDIV')) {
                return;
            }
            var addGoodsDiv = $('.addGoodsDIV');
            // 淘汽配件基本属性
            var formData = fd.get("#tqmallgoods_" + tqmallGoodsId);
            // 配件属性
            var goodsAttrRelListStr = formData["goodsAttrRelList"];
            if (goodsAttrRelListStr && goodsAttrRelListStr != "null") {
                var goodsAttrRelList = JSON.parse(goodsAttrRelListStr);
                var goodsAttrRelListTransfer = [];
                $.each(goodsAttrRelList, function (i, e) {
                    goodsAttrRelListTransfer.push({id: e.id, attrName: e.name, attrValue: e.value});
                });
                formData['goodsAttrRelList'] = JSON.stringify(goodsAttrRelListTransfer);
            }
            // 适用车型
            var goodsCarListStr = formData["goodsCarList"];
            goodsCarListStr = goodsCarListStr.replace(/id/g, "carBrandId");
            goodsCarListStr = goodsCarListStr.replace(/name/g, "carBrandName");
            formData["goodsCarList"] = goodsCarListStr;

            //自定义配件类型
            var customCat = $("input[name='customCat']").val();
            if (customCat != "true") {
                $("input[name='catId']").attr("name", "stdCatId");
                formData["stdCatId"] = formData["catId"];
                delete formData.catId;
            }

            var price = addGoodsDiv.find('input[name="price"]').val();
            var depot = addGoodsDiv.find('input[name="depot"]').val();
            formData["price"] = price;
            formData["depot"] = depot;
            // 钣喷-油漆: 追加油漆属性
            if (_currentTarget === "paint") {
                formData["netWeight"] = addGoodsDiv.find('input[name="netWeight"]').val();
                formData["bucketWeight"] = addGoodsDiv.find('input[name="bucketWeight"]').val();
                formData["stirWeight"] = addGoodsDiv.find('input[name="stirWeight"]').val();
                formData["paintLevel"] = addGoodsDiv.find('input[name="paintLevel"]').val();
                formData["paintType"] = addGoodsDiv.find('input[name="paintType"]').val();
            }

            $.ajax({
                type: 'POST',
                url: _formActionUrl,
                data: formData,
                success:function(result){
                    if (result.success) {
                        dg.success("添加成功", function () {
                            window.location.reload();
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        });
        $(document).on("click", ".js-dialog-cancle", function () {
            dialogIndex && dg.close(dialogIndex);
        });
    })
});

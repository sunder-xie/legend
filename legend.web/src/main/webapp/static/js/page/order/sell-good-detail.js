$(function () {
    var orderId = $("#orderId").val();
    var goodsData = [];

    seajs.use('dialog', function(dg) {
        dg.titleInit();

        // 删除
        $(".js-delete").click(function() {
            dg.confirm("您确定要把该工单删除吗?",function(){
                $.ajax({
                    type: 'post',
                    url: BASE_PATH + '/shop/order/delete',
                    data: {
                        orderId: orderId
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success("操作成功", function () {
                                window.location.href = BASE_PATH + "/shop/order/order-list";
                            });
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            },function(){
                return false;
            });
        });

        // 转入库
        inWarehouse({
            dom: '.js-in-warehouse',
            list: '.goods-datatr',
            goodsData: goodsData
        });

        ////打印
        //$(".js-print-order").click(function () {
        //    var href = $(this).data('href');
        //    util.print(href + '?orderId=' + orderId);
        //});

        // 结算
        $(".js-settle").click(function () {
            var funcList = "结算首页,财务首页";
            var func = util.checkFuncList(funcList);
            if(!func){
                return false;
            }
            $.ajax({
                url: BASE_PATH + "/shop/warehouse/out/isstockout",
                data: {orderid: orderId},
                success: function (json) {
                    if (json.success) {
                        if (json["data"]) {
                            dg.fail("提交失败，库存不足请转入库<br\/>" + 3 + "秒后自动弹出转入库页面", function () {
                                var _repeat = {}, _existed = {};
                                // 清空原来的数据
                                goodsData.splice(0);

                                $('.goods-datatr').each(function () {
                                    var $tr = $(this);
                                    var goodsId = $tr.data("id");
                                    var goodsNumber = Number($tr.data("number"));
                                    var stock = Number($tr.data("stock"));

                                    if (_repeat[goodsId] === undefined) {
                                        _repeat[goodsId] = goodsNumber;
                                    } else {
                                        stock -= _repeat[goodsId];

                                        _repeat[goodsId] += goodsNumber;
                                    }
                                    if (goodsNumber > stock) {
                                        if (_existed[goodsId] === undefined) {
                                            var data = {
                                                goodsId: goodsId,
                                                goodsFormat: $tr.data("format"),
                                                goodsName: $tr.data("name"),
                                                goodsNumber: goodsNumber
                                            };

                                            // 保存索引
                                            _existed[goodsId] = goodsData.length;

                                            goodsData.push(data);
                                        } else {
                                            goodsData[_existed[goodsId]].goodsNumber = _repeat[goodsId];
                                        }
                                    }
                                });
                                $(".js-in-warehouse").click();
                            });
                        } else {
                            window.location.href = BASE_PATH + '/shop/settlement/debit/speedily-confirm-bill?orderId=' + orderId;
                        }
                    } else {
                        dg.fail(json.errorMsg);
                    }

                }
            });
        });
    });


    // 编辑
    $(".js-edit").on("click", function () {
        window.location.href = BASE_PATH + "/shop/order/sell-good?orderId=" + orderId;
    });

    //打印
    $('.js-print').click(function () {
        var _url = $(this).data("href");
        util.print(BASE_PATH + _url +'?orderId=' + orderId);
    });

    // 复制
    $(".js-copy").on("click", function () {
        window.location.href = BASE_PATH + "/shop/order/sell-good-copy?copyOrderId=" + orderId;
    });

    // 作废
    $(document).on('click', '.js-invalid', function () {
        seajs.use(["dialog"], function (dg) {
            dg.confirm("您确定要把该工单无效吗?", function () {
                $.ajax({
                    type: 'post',
                    url: BASE_PATH + '/shop/order/invalid',
                    data: {
                        orderId: orderId
                    },
                    success: function (json) {
                        if (json["success"]) {
                            dg.success("操作成功", function () {
                                window.location.reload();
                            });
                        } else {
                            dg.fail(json["errorMsg"]);
                        }
                    }
                });
            });
        });
    });

    // 返回
    $(".js-return").on("click", function () {
        util.goBack();
    });

    //校验删除按钮是否有权限
    if($(".yqx-btn").hasClass('js-delete')){
        util.checkFunc("工单删除",".js-delete");
    }
    //校验无效按钮是否有权限
    if($(".yqx-btn").hasClass('js-invalid')){
        util.checkFunc("工单无效",".js-invalid");
    }
});
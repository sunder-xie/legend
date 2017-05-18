/** * Created by zsy on 16/4/20.
 * 综合维修单详情
 */
$(function () {
    var orderId = $("#orderId").val();
    var orderSn = $("#orderSn").val();
    var orderStatus = $("#orderStatus").val();
    var proxyType = $("#proxyType").val();//委托状态，委托的工单可以不选维修工

    // 结算
    $(".js-common-settle").click(function () {
        window.location.href = BASE_PATH + '/shop/settlement/order-settle?orderId=' + orderId;
    });

    // 编辑
    $(".js-common-edit").on("click", function () {
        window.location.href = BASE_PATH + "/shop/order/common-edit?orderId=" + orderId;
    });

    // 复制
    $(".js-common-copy").on("click", function () {
        window.location.href = BASE_PATH + "/shop/order/common-add?copyOrderId=" + orderId;
    });

    seajs.use([
        'art',
        'dialog'
    ], function (at, dg) {

        var doc = $(document);

        dg.titleInit();

        //打印 弹窗
        doc.on('click', '.js-print', function () {
            var html = at('print-dialog-tpl')();
            dg.open({
                area: ['300px', '260px'],
                content: html
            });
                if(orderStatus != 'CJDD'){
                $(".js-bj-print").removeClass("display-none");
            }
            return false;
        });

        //车间打印 弹窗
        doc.on('click', '.js-magic-print', function () {
            var html = at('print-dialog-tpl')();
            dg.open({
                area: ['300px','350px'],
                content: html
            });
            if(orderStatus != 'CJDD'){
                $(".js-bj-print").removeClass("display-none");
            }
            return false;
        });

        //派工单打印
        doc.on('click', '.js-common-print', function () {
            util.print(BASE_PATH + '/shop/order/order-print?orderId=' + orderId);
        });
        //简化版派工单打印
        doc.on('click', '.js-simple-print', function () {
            util.print(BASE_PATH + '/shop/order/simple-order-print?orderId=' + orderId);
        });
        //试车单打印
        doc.on('click', '.js-trialrun-print', function () {
            util.print(BASE_PATH + '/shop/order/trialrun-print?orderId=' + orderId);
        });
        //打印报价
        doc.on('click', '.js-bj-print', function () {
            util.print(BASE_PATH + '/shop/warehouse/out/pricing_print?id=' + orderId);
        });
        //综合维修单打印--包含外观检查信息
        doc.on('click', '.js-common-order-print', function () {
            util.print(BASE_PATH + '/shop/order/common-order-print?type=0&orderId=' + orderId);
        });
        // 简化版综合维修单打印
        doc.on('click', '.js-simple-common-order-print', function () {
            util.print(BASE_PATH + '/shop/order/common-order-print?type=1&orderId=' + orderId);
        });

        //无效
        $(".js-common-invalid").click(function () {

            if (orderStatus == 'WXDD') {
                dg.warn("该工单已经被置为‘无效’，<br\/>无须再次进行‘无效’操作");
                return false;
            }

            dg.confirm("您确定要把该工单无效吗?", function () {
                $.ajax({
                    type: 'POST',
                    url: BASE_PATH + '/shop/order/invalid',
                    data: {
                        orderId: orderId
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success("综合维修单无效成功", function () {
                                window.location.reload();
                            });
                        } else {
                            dg.fail(result.errorMsg);

                        }
                    }
                });
            });
        });

        // 删除
        $(".js-common-delete").click(function () {
            dg.confirm("您确定要把该工单删除吗?", function () {
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
            }, function () {
                return false;
            });
        });

        // 重新派工
        $(".js-common-rePaiGong").on("click", function () {
            dg.confirm("你确定要把该工单派工吗？", function () {
                $.ajax({
                    url: BASE_PATH + '/shop/order/order_track/tasking',
                    type: 'POST',
                    data: {
                        orderId: orderId
                    },
                    success: function (data) {
                        if (data["success"] == true) {
                            dg.success("操作成功", function () {
                                window.location.reload();
                            });
                        } else {
                            dg.fail(data["errorMsg"]);
                        }
                    }
                });
            }, function () {
                return false;
            });
        });
    });

    // 继续开单
    $(".js-return").on("click", function () {
        util.goBack();
    });

    seajs.use([
        "ajax",
        'dialog',
        'art',
    ], function (ajax, dg, at) {

        // 维修工校验
        function checkWorkerIsExist() {
            // 校验基本服务
            var basicServiceDiv = $("#basicServiceRow");
            var basicServiceRows = $(".form_item", basicServiceDiv);
            for (var bs = 0; bs < basicServiceRows.length; bs++) {
                var currentObj = basicServiceRows[bs];
                var workerIds = $('input[name="workerIds"]', currentObj).val();

                // 是否指派维修工
                if (typeof(workerIds) === "undefined" || workerIds == "") {
                    return {"code": "error", "status": "101"};
                }
            }

            return {"code": "success"};
        };

        $('.js-common-wangongBtn').click(function () {
            var resp;
            var workshop = $(this).data("workshop");
            if (proxyType == 1 || proxyType == 2 || workshop == 1) {
                //委托工单、受托工单可不选维修工
                resp = {"code": "success"};
            }else{
                resp = checkWorkerIsExist();
            }
            if (resp["code"] != "success") {
                var errorMsg = "";
                var errorCode = resp["status"];
                if (errorCode == 101) {
                    errorMsg = "完工失败！<br/>存在服务项目未指派维修工，<br/>请指派维修工，保存工单后再进行完工";
                }
                dg.warn(errorMsg, function () {
                    window.location.href = BASE_PATH + "/shop/order/common-edit?orderId=" + orderId;
                });

                return false;
            } else {
                $.ajax({
                    type: 'get',
                    url: BASE_PATH + '/shop/order/getRealOrderGoodsListByOrderId',
                    data: {
                        orderId: orderId,
                        orderStatus: "FPDD"
                    },
                    success: function (result) {
                        if (result["success"] == true) {
                            if (result["data"]["code"] == true) {
                                $.ajax({
                                    type: 'post',
                                    url: BASE_PATH + '/shop/order/order_track/finish',
                                    data: {
                                        orderSn: orderSn,
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
                            } else {
                                // update orderStatus =DDWC
                                $.ajax({
                                    type: 'get',
                                    url: BASE_PATH + '/shop/order/getRealOrderGoodsListByOrderId',
                                    data: {
                                        orderId: orderId
                                    },
                                    success: function (result) {
                                        if (!result.success) {
                                            dg.fail(result["errorMsg"]);
                                        } else {
                                            // 2016-04-20 物料出库单列表
                                            var html = at('moreWarehouseTpl', {result: result});
                                            dg.open({
                                                area: ['900px', 'auto'],
                                                content: html
                                            });
                                        }
                                    }
                                });
                            }
                        } else {
                            dialog.info(result["errorMsg"], 3);
                        }
                    }
                });
            }

        });

        // 派工
        $('.js-common-paigongBtn').click(function () {

            dg.confirm("您确定要把该工单派工吗?", function () {
                $.ajax({
                    type: 'post',
                    url: BASE_PATH + '/shop/order/order_track/tasking',
                    data: {
                        orderSn: orderSn,
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
        // 车间派工
        $('.js-magic-common-paigongBtn').click(function () {
            dg.confirm("您确定要把该工单派工吗?", function () {
                window.location.href = BASE_PATH + "/workshop/workOrder/toWorkOrderCreate?orderId=" + orderId;
            });
        });

    });

    //校验删除按钮是否有权限
    if($(".yqx-btn").hasClass('js-common-delete')){
        util.checkFunc("工单删除",".js-common-delete");
    }
    //校验无效按钮是否有权限
    if($(".yqx-btn").hasClass('js-common-invalid')){
        util.checkFunc("工单无效",".js-common-invalid");
    }
    // 委托
    $(".js-create-proxy").click(function(){
        window.location.href = BASE_PATH + "/proxy/toTranslateProxy?orderId=" + orderId;
    });

    $(document).on('click', '.js-print-order', function () {
        var orderId = util.getPara('orderId');
        var href = $(this).data('href');
        util.print(href + '?orderId=' + orderId);
    });
});
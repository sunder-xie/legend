$(function(){
    var orderId = $("#orderId").val();
    var goodsData = [];

    // 编辑
    $(".js-speedily-edit").on("click", function() {
        window.location.href = BASE_PATH + "/shop/order/speedily?orderId=" + orderId;
    });

    //打印
    $('.js-print').click(function () {
        util.print(BASE_PATH + '/shop/settlement/simple-settle-print?orderId=' + orderId);
    });
    //打印
    $(".js-print-new").click(function () {
        var href = $(this).data('href');
        var target = $(this).data('target');

        if(target === 'receipt' && window.Components.receiptGuideDialog(href + '?orderId=' + orderId)) {
            return;
        }
        util.print(href + '?orderId=' + orderId);
    });

    // 复制
    $(".js-speedily-copy").on("click", function() {
        window.location.href = BASE_PATH + "/shop/order/speedily?copyOrderId=" + orderId;
    });

    seajs.use("dialog",function(dg){

        dg.titleInit();
        //无效
        $(".js-speedily-invalid").click(function() {
            var orderStatus = $("#orderStatus").val();

            if(orderStatus =='WXDD'){
                dg.warn("该工单已经被置为‘无效’，<br\/>无须再次进行‘无效’操作");
                return false;
            }

            dg.confirm("您确定要把该工单无效吗?",function(){
                $.ajax({
                    type: 'post',
                    url: BASE_PATH + '/shop/order/invalid',
                    data: {
                        orderId: orderId
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success("操作成功", function () {
                                window.location.reload();
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

        // 删除
        $(".js-speedily-delete").click(function() {
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

        // 维修工校验
        function checkWorkerIsExist() {
            // 校验基本服务
            var basicServiceDiv = $("#basicServiceRow");
            var basicServiceRows = $(".form_item", basicServiceDiv);
            for (var bs = 0; bs < basicServiceRows.length; bs++) {
                var currentObj = basicServiceRows[bs];
                var workerIds = $('input[name="workerIds"]', currentObj).val();

                // 是否指派维修工
                if (typeof(workerIds) === "undefined" || workerIds == "" || workerIds == "0") {
                    return {"code": "error", "status": "101"};
                }
            }

            return {"code": "success"};
        };

        // 完工
        $(".js-speedily-finish").click(function() {
            var resp = checkWorkerIsExist();
            if (resp["code"] != "success") {
                var errorMsg = "";
                var errorCode = resp["status"];
                if (errorCode == 101) {
                    errorMsg = "完工失败！<br/>存在服务项目未指派维修工，<br/>请指派维修工，保存工单后再进行完工";
                }
                dg.warn(errorMsg, function () {
                    window.location.href = BASE_PATH + "/shop/order/speedily?orderId=" + orderId;
                });

                return false;
            } else {
                $.ajax({
                    type: 'get',
                    url: BASE_PATH + '/shop/order/updateorderstatus',
                    data: {
                        orderId: orderId,
                        orderStatus: "DDWC"
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
            }
        });

        // 转入库
        inWarehouse({
            dom: '.js-in-warehouse',
            list: '.goods-datatr',
            goodsData: goodsData
        });

        // 结算
        $(".js-speedily-settle").click(function() {
            var funcList = "结算首页,财务首页";
            var func = util.checkFuncList(funcList);
            if(!func){
                return false;
            }
            var resp = checkWorkerIsExist();
            if (resp["code"] != "success") {
                var errorMsg = "";
                var errorCode = resp["status"];
                if (errorCode == 101) {
                    errorMsg = "存在服务项目没有选择维修工, 是否继续进行收款？";
                }
                dg.confirm(errorMsg,function(){
                    settle(dg,orderId,goodsData);
                },function(){
                    window.location.href= BASE_PATH + "/shop/order/speedily?orderId="+orderId;
                });
            }else{
                settle(dg,orderId,goodsData);
            }

        });
    });

    // 返回
    $(".js-speedily-back").click(function(){
        util.goBack();
    });

    //校验删除按钮是否有权限
    if($(".yqx-btn").hasClass('js-speedily-delete')){
        util.checkFunc("工单删除",".js-speedily-delete");
    }
    //校验无效按钮是否有权限
    if($(".yqx-btn").hasClass('js-speedily-invalid')){
        util.checkFunc("工单无效",".js-speedily-invalid");
    }
});

function settle(dg, orderId, goodsData) {
    $.ajax({
        url: BASE_PATH + "/shop/warehouse/out/isstockout",
        data: {orderid: orderId},
        success: function (json) {
            if (json.success) {
                if (json["data"]) {
                    dg.fail("提交失败，库存不足请转入库<br\/>" + 3 + "秒后自动弹出转入库页面", function () {
                        seajs.use('formData', function (fd) {
                            var _repeat = {}, _existed = {};
                            goodsData.splice(0);
                            $('.goods-datatr').each(function () {
                                var $tr = $(this);
                                var goodsId = $tr.data("id");
                                var goodsNumber = Number($tr.data("number"));
                                var stock = Number($tr.data("stock"));

                                if(_repeat[goodsId] === undefined) {
                                    _repeat[goodsId] = goodsNumber;
                                } else {
                                    stock -= _repeat[goodsId];

                                    _repeat[goodsId] += goodsNumber;
                                }
                                if (goodsNumber > stock) {
                                    if(_existed[goodsId] === undefined) {
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
                                        goodsData[ _existed[goodsId]].goodsNumber = _repeat[goodsId];
                                    }
                                }
                            });

                            $(".js-in-warehouse").trigger("click");
                        });
                    });

                } else {
                    window.location.href = BASE_PATH + '/shop/settlement/debit/speedily-confirm-bill?orderId=' + orderId;
                }
            } else {
                dg.fail(json.errorMsg);
            }

        }
    });
}
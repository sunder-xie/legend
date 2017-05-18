/**
 * zmx 2016-05-16
 * 新建委托单JS
 */

$(function() {
    var $doc = $(document);

    seajs.use('dialog', function(dg) {
        dg.titleInit();
    });
    //计算价格
    function total() {
        var numtotal = 0,
            pricetotal = 0,
            priceCount = 0;

        $(".yqx-table tr").each(function () {
            var thisNum = $(this).find(".num").text(),
                thisPrice = $(this).find(".price").text();

            //委托单金额
            pricetotal = Number(thisPrice);
            priceCount += pricetotal;

            $(".js-numtotal").text(thisNum);
            $(".js-moneytotal").text(priceCount.toFixed(2));
        })
    }

    total();

    //返回按钮
    $doc.on('click', '.js-goback', function () {
        util.goBack();
    });

    seajs.use([
        'dialog'
    ], function (dg) {
        //无效按钮
        $doc.on("click", ".js-invalid", function () {
            var proxyId = $(".js-proxyid").data('proxyId');
            dg.confirm('是否确认无效该委托单',function(){
                $.ajax({
                    type: 'get',
                    url: BASE_PATH + "/proxy/cancelProxy",
                    data: {
                        proxyOrderId: proxyId
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success("操作成功", function () {
                             window.location.href = BASE_PATH + "/proxy/detail?proxyOrderId="+ proxyId;
                             });
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            },function(){
                layer.close();
            });
        });

        //编辑按钮
        $doc.on('click','.js-edit',function(){
            var proxyId = $(".js-proxyid").data('proxyId');
            var orderId = $(".js-orderid").data('orderId');
            window.location.href = BASE_PATH + "/proxy/toTranslateProxy?orderId="+orderId+"&proxyId="+ proxyId;
        });

        //转工单按钮
        $doc.on('click','.js-translate-order',function(){
            var proxyId = $(".js-proxyid").data('proxyId');
            window.location.href = BASE_PATH + "/shop/order/common-add?proxyId="+ proxyId;
        });


        //查看委托单对应工单详情
        $doc.on('click','.js-show-order-info',function(){
            var proxyOrderId = $(".js-proxy-order-id").data('proxyOrderId');
            window.location.href = BASE_PATH + "/shop/order/detail?orderId="+ proxyOrderId;
        });
        //查看委托单对应委托方工单详情
        $doc.on('click','.js-show-proxy-order-info',function(){
            var proxyOrderId = $(".js-orderid").data('orderId');
            window.location.href = BASE_PATH + "/shop/order/detail?orderId="+ proxyOrderId;
        });

        //接单按钮
        $doc.on('click','.js-accept-order',function(){
            var proxyId = $(".js-proxyid").data('proxyId');
            /*$.ajax({
                type: 'get',
                url: BASE_PATH + "/proxy/acceptProxy",
                data: {
                    proxyOrderId: proxyId
                },
                success: function (result) {
                    if (result.success) {
                        dg.success("操作成功", function () {
                            window.location.href = BASE_PATH + "/proxy/detail?proxyOrderId="+ proxyId;
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });*/
            window.location.href = BASE_PATH + "/proxy/toReceiveOrder?proxyId="+ proxyId;
        });


        //交车按钮
        $doc.on('click','.js-back-car',function(){
            var proxyId = $(".js-proxyid").data('proxyId');
            dg.confirm('是否确认交车',function(){
                $.ajax({
                    type: 'get',
                    url: BASE_PATH + "/proxy/backCar",
                    data: {
                        proxyOrderId: proxyId
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success("操作成功", function () {
                                window.location.href = BASE_PATH + "/proxy/detail?proxyOrderId="+ proxyId;
                            });
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            },function(){
                layer.close();
            });

        });


    });

});

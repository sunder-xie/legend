/**
 * Created by zsy on 16/4/16.
 * 洗车单详情页
 */
$(function(){
    var orderId = $("#orderId").val();

    //结算
    $(".js-carwash-settle").click(function() {
        window.location.href = BASE_PATH + '/shop/settlement/order-settle?orderId=' + orderId;
    });

    //完善页面
    $(".js-carwash-perfect").click(function(){
        window.location.href = BASE_PATH + '/shop/order/carwash-perfect?orderId=' + orderId;
    });

    //打印
    $(".js-print").click(function(){
        util.print(BASE_PATH + '/shop/settlement/simple-settle-print?orderId=' + orderId);
    });
    //打印
    $(".js-print-new").click(function () {
        var href = $(this).data('href');
        var target = $(this).data('target');

        if(target === 'receipt'
            && window.Components.receiptGuideDialog(href + '?orderId=' + orderId)) {
            return;
        }
        util.print(href + '?orderId=' + orderId);
    });

    seajs.use("dialog",function(dg){

        dg.titleInit();
        //无效
        $(".js-carwash-invalid").click(function() {
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
                            dg.success("洗车单无效成功", function(){
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
        $(".js-carwash-delete").click(function() {
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
            });
        });
    });

    //返回
    $(".js-carwash-back").click(function(){
        util.goBack();
    });

    //校验删除按钮是否有权限
    if($(".yqx-btn").hasClass('js-carwash-delete')){
        util.checkFunc("工单删除",".js-carwash-delete");
    }
    //校验无效按钮是否有权限
    if($(".yqx-btn").hasClass('js-carwash-invalid')){
        util.checkFunc("工单无效",".js-carwash-invalid");
    }
});
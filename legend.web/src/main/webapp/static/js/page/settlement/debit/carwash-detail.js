/**
 * zmx  2016-06-07
 * 洗车单详情 页面
 */

$(function(){
    var $doc = $(document);

    seajs.use([
        'dialog',
        'select',
        'table',
        'art'
    ],function(dg,st,tb,at){

        dg.titleInit();
        var orderId = $('#orderId').val();
        var carId = $('#customerCarId').val();


        //校验挂账工单按钮是否显示
        $.ajax({
            url: BASE_PATH + "/shop/order/get-sign-order",
            data:{
                customerCarId: carId
            },
            success:function(result){
                if(result.success && result.data.length > 0){
                    $('.sign-history').removeClass('display-none');
                }
            }
        });

        //挂账工单点击跳转到详情页
        $doc.on('click','.js-linkToDetail',function(){
            var id = $(this).attr('data-content');
            window.open(BASE_PATH+"/shop/settlement/debit/order-detail?orderId="+id);
        });

        //打印
        $(".js-print-new").click(function () {
            var href = $(this).data('href');
            var target = $(this).data('target');

            if(target === 'receipt'
                && window.Components.receiptGuideDialog(href + '?orderId=' + orderId)) {
                return;
            }
            if(!href) {
                href = BASE_PATH + '/shop/settlement/shop-settle-print';
            }
            util.print(href + '?orderId=' + orderId);
        });

        //打印
        $doc.on('click', '.js-print', function () {
            var html = at('print-dialog-tpl')();
            dg.open({
                area: ['300px', '160px'],
                content: html
            });
        });

        //普通打印
        $doc.on('click', '.js-common-print', function () {
            util.print(BASE_PATH+  '/shop/settlement/settle-print?orderId='+orderId);
        });
        //简化版打印
        $doc.on('click', '.js-simple-print', function () {
            util.print(BASE_PATH+  '/shop/settlement/simple-settle-print?orderId='+orderId);
        });


        // 收款
        $doc.on('click', '.js-debit', function () {
            window.location.href = BASE_PATH + "/shop/settlement/debit/order-debit?refer=order-detail&orderId=" + orderId;
        });

        //无效
        $doc.on('click', '.js-invalid', function () {
            var orderStatus = $("#orderStatus").val();
            if(orderStatus =='WXDD'){
                dg.warn("该工单已经被置为‘无效’，<br\/>无须再次进行‘无效’操作");
                return false;
            }
            dg.confirm("您确定要把该工单无效吗?", function () {
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
            }, function () {
                return false;
            });
        });

        //删除
        $doc.on('click', '.js-delete', function () {
            dg.confirm("您确定要把该工单删除吗?", function () {
                $.ajax({
                    type: 'POST',
                    url: BASE_PATH + '/shop/order/delete',
                    data: {
                        orderId: orderId
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success("操作成功", function () {
                                window.location.href = BASE_PATH + "/shop/settlement/debit/order-list";
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

    });

    //总数提示
    $doc.on('mouseover', '.js-tag', function () {
        $(this).next('.total-hide').removeClass('hide');
    });
    $doc.on('mouseleave', '.js-tag', function () {
        $(this).next('.total-hide').addClass('hide');
    });
    //返回按钮
    $doc.on('click','.js-goback',function(){
        window.location.href = BASE_PATH + "/shop/settlement/debit/order-list";
    });

    //校验删除按钮是否有权限
    if($(".yqx-btn").hasClass('js-delete')){
        util.checkFunc("工单删除",".js-delete");
    }
    //校验无效按钮是否有权限
    if($(".yqx-btn").hasClass('js-invalid')){
        util.checkFunc("工单无效",".js-invalid");
    }


    $('.btn-box').find('button').eq(0).addClass('yqx-btn-2');

});
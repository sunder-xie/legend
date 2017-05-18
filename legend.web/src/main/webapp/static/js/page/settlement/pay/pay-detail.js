/**
 * zmx  2016-06-01
 * 收款单详情
 */

$(function () {
    var $doc = $(document);

    //返回按钮
    $doc.on('click', '.js-goback', function () {
        util.goBack();
    });

    $('.js-print').click(function () {
        var data = {
            id: util.getPara('id')
        };

        util.print(BASE_PATH + '/shop/settlement/pay/print/payment?' + $.param(data))
    });

    seajs.use('dialog', function (dg) {
        dg.titleInit();

        $doc.on('click', '.js-invalid', function () {
            dg.confirm("您确定要把该付款单无效吗?", function () {
                var id = $('.js-invalid').data("id");
                $.ajax({
                    url: BASE_PATH + '/shop/settlement/pay/pay-invalid',
                    data: {
                        id: id
                    },
                    type: 'POST',
                    success: function (data) {
                        if (data.success) {
                            dg.success("无效成功", function () {
                                window.location.href = BASE_PATH + "/shop/settlement/pay/pay-flow"
                            });
                        } else {
                            dg.fail("无效失败");
                        }
                    }
                });
            });
        });

        $doc.on('click', '.js-delete', function () {
            var id = $(this).data("id");
            dg.confirm("您确定要删除该付款单吗?", function () {
                $.ajax({
                    url: BASE_PATH + '/shop/settlement/pay/pay-delete',
                    data: {
                        id: id
                    },
                    type: 'POST',
                    success: function (data) {
                        if (data.success) {
                            dg.success("删除成功", function () {
                                window.location.href = BASE_PATH + "/shop/settlement/pay/pay-flow"
                            });
                        } else {
                            dg.fail("删除失败");
                        }
                    }
                });
            });
        });
    });
    //校验无效按钮是否有权限
    if($(".yqx-btn").hasClass('js-invalid')){
        util.checkFunc("工单无效",".js-invalid");
    }

});
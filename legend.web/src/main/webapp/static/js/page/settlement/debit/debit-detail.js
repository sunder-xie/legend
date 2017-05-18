/**
 * zmx  2016-06-01
 * 收款单详情
 */

$(function () {
    var $doc = $(document);

    var billId = $('input[name="billId"]').val();
    var billName = $('input[name="billName"]').val();

    //返回按钮
    $doc.on('click', '.js-goback', function () {
        util.goBack();
    });
    seajs.use('dialog', function (dg) {
        dg.titleInit();
    });

    $('.js-print').click(function () {
        var data = {
            billId: util.getPara('billId'),
            flowId: util.getPara('flowId')
        };

        util.print(BASE_PATH + '/shop/settlement/debit/print/debit?' + $.param(data))
    });

    // 无效
    $doc.on('click', '.js-invalid', function () {
        seajs.use('dialog', function (dg) {
            dg.confirm("您确定要把该收款单无效吗?", function () {
                $.ajax({
                    url: BASE_PATH + '/shop/settlement/debit/invalid',
                    type: 'post',
                    data: {billId:billId,billName:billName},
                    success: function (result) {
                        if (result.success) {
                            dg.success("操作成功", function () {
                                window.location.href = BASE_PATH + "/shop/settlement/debit/flow-list";
                            });
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            },function (){
                return false;
            });
        });
    });

    //校验无效按钮是否有权限
    if($(".yqx-btn").hasClass('js-invalid')){
        util.checkFunc("工单无效",".js-invalid");
    }

});
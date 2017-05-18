/**
 * Created by zz on 2016/6/20.
 */
$(function () {
    //收款方式下拉
    seajs.use('select', function(st){
        st.init({
            dom: '.js-payment-select',
            url: BASE_PATH + '/shop/payment/get_payment',
            showKey: "id",
            showValue: "name"
        });
    });

    var _cardId = $('.js-card').eq(0).find('.card-id').val();

    $(document).on('click','.js-card',function(){
        $(this).addClass('hover').siblings().removeClass('hover');
        _cardId = $(this).find('.card-id').val();
    });

    //提交
    $(".submit").on("click",function(){
        var flag = true;
        seajs.use('check', function(ck) {
            if(!ck.check()) {
                flag =  false;
            }
        });
        if(!flag) {
            return;
        }
        var data={};
        data.amount=$("input[name='amount']").val();
        data.payAmount=$("input[name='payAmount']").val();
        data.amount=$("input[name='amount']").val();
        data.paymentId = $("input[name='paymentId']").val();
        data.paymentName = $("input[name='paymentName']").val();
        data.remark = $("textarea[name='remark']").val();
        data.cardId = _cardId;
        $.ajax({
            url:BASE_PATH + "/account/member/reCharge",
            dataType: 'json',
            contentType : 'application/json',
            data: JSON.stringify(data),
            type:"POST",
            success:function(result){
                seajs.use(['dialog'],function(dg){
                    if(result.success){
                        dg.close();
                        dg.confirm('充值成功！是否打印回执单', function() {
                           util.print(BASE_PATH + '/account/member/rechargePrint?id=' + result.data.id, {
                               afterPrint: function () {
                                   window.location = BASE_PATH + "/account";
                               }
                           });
                        }, function () {
                            window.location = BASE_PATH + "/account";
                        }, ['打印', '取消']);
                    }else{
                        dg.fail(result.errorMsg);
                    }
                });

            }
        });
    });

    seajs.use('check', function(ck){
        ck.init();
    });
});
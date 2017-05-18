/**
 * Created by zmx on 2017/3/1.
 */
$(function () {
    var _cardId = $('.js-card').eq(0).find('.card-id').val();
    //收款方式下拉
    seajs.use('select', function(st){
        st.init({
            dom: '.js-payment-select',
            url: BASE_PATH + '/shop/payment/get_payment',
            showKey: "id",
            showValue: "name"
        });
    });

    $(document).on('click','.js-card',function(){
        $(this).addClass('hover').siblings().removeClass('hover');
        _cardId = $(this).find('.card-id').val();
    });

    $(document).on('click','.js-retreat',function(){
        seajs.use(['check','dialog','formData'], function(ck,dialog,fd) {
            if(!ck.check()) {
                return;
            }
            var data = fd.get('.upgrade-info');
            data.cardId = _cardId;
            $.post(BASE_PATH + '/account/member/back', data, function (json) {
                if(json.success) {
                    dialog.success('退卡成功', function () {
                        util.goBack();
                    });
                } else {
                    dialog.fail(json.errorMsg || '退卡失败');
                }
            });
        })
    });
});
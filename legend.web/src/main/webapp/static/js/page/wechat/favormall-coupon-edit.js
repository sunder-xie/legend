/**
 * Created by zz on 2016/9/7.
 */
seajs.use(['select','check','dialog'], function(st,ck,dg){
    ck.init();
    st.init({
        dom: '.js-choose-coupon',
        url: BASE_PATH + '/account/coupon/search?couponType=1',
        showKey: "id",
        showValue: "couponName",
        callback:function(){
            var couponTypeId = $('input[name="couponTypeId"]').val();
            var couponTypeName = $('.js-choose-coupon').val();
            $.ajax({
                url: BASE_PATH + "/shop/wechat/op/isExist-favormall-config",
                data: {
                    configType :2,
                    configId:couponTypeId
                },
                dataType: "json",
                success: function (result) {
                    if (result.success &&result.data) {
                        dg.warn("优惠券["+couponTypeName+"]已经配置在卡券商城中，请选择其他优惠券，或者取消配置中的优惠券");
                        $('.js-choose-coupon').val('');
                        $('input[name="couponTypeId"]').val('');
                    }
                }
            });
        }
    });
    $(document).on('click','#js-save',function(){
        if (!ck.check(null, false)) {
            return;
        }
        var couponTypeId = $('input[name="couponTypeId"]').val();
        var givingNumber = $('.giving-number').val();
        var data ={couponTypeId:couponTypeId, givingNumber:givingNumber,givingStatus:1}
        $.ajax({
            url:BASE_PATH + '/shop/wechat/op/save-favormall-coupon',
            data: JSON.stringify(data),
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            success:function(result){
                if(result.success){
                    dg.success('操作成功!',function(){
                        location.href = BASE_PATH + '/shop/wechat/favormall-list?flag=1';
                    });
                }else{
                    dg.fail(result.message);
                }
            }
        });
    })
});
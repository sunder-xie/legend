/**
 * Created by zz on 2016/10/26.
 */
seajs.use('dialog',function(dg){
    //点击开启
    $(document).on('click','.js-open',function(){
        if($(this).hasClass('on')){
            return;
        }
        $.ajax({
            url:BASE_PATH + '/shop/config/payment/has-open-payment',
            success:function(result){
                if(result.success){
                    if(result.data == true){
                        openWechatPayment('open');
                    } else {
                        dg.confirm('开通微信支付才能激活此功能',function(){
                            window.location = BASE_PATH + '/shop/settlement/online/online-payment';
                        },function(){
                            return false;
                        });
                    }
                }
            }
        });
    });
    //点击关闭
    $(document).on('click','.js-close',function(){
        if($(this).hasClass('on')){
            return;
        }
        openWechatPayment('close');
    });
    //支付方式开启/关闭
    function openWechatPayment(statement){
        $.ajax({
            type:"post",
            url:BASE_PATH + '/shop/config/payment/set-payment/wechat/?confValue=' + statement,
            success:function(result){
                if(result.success){
                    dg.success(statement == 'open'?'开启成功':'关闭成功');
                    window.location.reload();
                } else {
                    dg.fail(statement == 'open'?'开启失败':'关闭失败');
                }
            }
        });
    }
});

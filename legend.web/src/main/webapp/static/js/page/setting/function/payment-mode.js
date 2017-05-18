/**
 * 支付方式开关设置
 */
$(function(){

    seajs.use(["dialog"], function (dg) {
        var doc = $(document);
        //开关
        doc.on('click','.js-on-btn',function(){
            var $this = $(this);
            if($this.hasClass('off-btn') ){
                //由关到开
                //先判断门店有没有成功申请相应的支付功能
                $.ajax({
                    url:BASE_PATH + '/shop/conf/payment-mode/has-open-payment',
                    data: {
                        applyType: $this.data('applytype')
                    },
                    success:function(result){
                        if(result.success){
                            if(result.data == true){
                                var data={
                                    confKey: $this.data('confkey'),
                                    confValue: 'open'
                                };
                                save(data,$this);
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
            }else{
                //由开到关
                var data={
                    confKey: $this.data('confkey'),
                    confValue: 'close'
                };
                save(data,$this);
            }
        });

        function save(data,$this){
            $.ajax({
                type:"post",
                url:BASE_PATH + '/shop/conf/payment-mode/save',
                data:data,
                success:function(result){
                    if(result.success){
                        dg.success('操作成功');
                        //样式切换
                        $this.toggleClass('off-btn').parents('.on-off').toggleClass('red-color');
                    } else {
                        dg.fail('操作失败');
                    }
                }
            });
        }
    });
});

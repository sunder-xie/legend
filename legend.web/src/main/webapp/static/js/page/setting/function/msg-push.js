/**
 * Created by zmx 2016/12/06
 * 消息推送设置页面样式
 */

$(function(){
    var doc = $(document);

    //开关
    doc.on('click','.js-on-btn',function(){
        var confValue;
        if ($(this).hasClass('off-btn')) {
            //由关到开
            confValue = 0;
        } else {
            //由开到关
            confValue = 1;
        }
        var data={
            confKey: $(this).data('confkey'),
            confValue: confValue
        };
        save(data,$(this));
    });

    function save(data,$this){
        seajs.use(["dialog"], function (dg) {
            $.ajax({
                method:'post',
                url: BASE_PATH + '/shop/conf/msg-push/save',
                data: data,
                success: function (result) {
                    if (result.success) {
                        dg.success('操作成功');
                        //样式切换
                        $this.toggleClass('off-btn').parents('.on-off').toggleClass('red-color');
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });
    }

});

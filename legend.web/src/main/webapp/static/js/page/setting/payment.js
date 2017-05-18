/**
 * Created by zmx on 16/11/7.
 * 设置-业务类型
 */
$(function () {
    var doc = $(document);

    seajs.use([
        "ajax",
        "dialog",
        "check"
    ], function (ajax, dg, ck) {
        dg.titleInit();

        //开关
        doc.on('click', '.js-on-btn', function () {
            var showStatus;
            if ($(this).hasClass('off-btn')) {
                showStatus = 1;
                $(this).removeClass('off-btn').parents('.on-off').removeClass('background-red').addClass('background-green');
            } else {
                showStatus = 0;
                $(this).addClass('off-btn').parents('.on-off').removeClass('background-green').addClass('background-red');
            }
            var id = $(this).data("id");
            var payment = {
                id: id,
                showStatus: showStatus
            }
            $.ajax({
                type: 'POST',
                dataType: "json",
                contentType: "application/json",
                url: BASE_PATH + '/shop/setting/payment/update',
                data: JSON.stringify(payment),
                success: function (result) {
                    if (!result.success) {
                        dg.warn(result.errorMsg);
                        return false;
                    } else {
                    }
                }
            });
        });


        //他人卡券优惠开关
        doc.on('click','.js-change-guest',function(){
            var $this = $(this);
            if($(this).hasClass('off-btn') ){
                //由关到开
                $(this).addClass('off-btn').parents('.on-off').addClass('background-red');
                dg.confirm("您确定要开启吗？",function(){
                    save({value:'yes'},$this);
                });
            }else{
                //由开到关
                $(this).removeClass('off-btn').parents('.on-off').removeClass('background-red');
                dg.confirm("您确定要关闭吗？",function(){
                    save({value:'no'},$this);
                });
            }
        });

        function save(data,$this){
            $.ajax({
                type: 'POST',
                url: BASE_PATH + "/shop/conf/change-guest-conf",
                data: data,
                success: function (result) {
                    if (result.success) {
                        dg.success("操作成功");
                        //样式切换
                        $this.toggleClass('off-btn').parents('.on-off').toggleClass('background-red');
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        }
    });
});
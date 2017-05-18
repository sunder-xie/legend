/**
 * 委托开关设置
 */
$(function(){
    var doc = $(document);

    seajs.use(['dialog'], function (dg) {
        //开关
        doc.on('click','.js-on-btn',function(){
            var $this = $(this);
            if($(this).hasClass('off-btn') ){
                //由关到开
                $(this).addClass('off-btn').parents('.on-off').addClass('red-color');
                dg.confirm("您确定要加入委托吗？<br/><span style='color:red'>加入后请重新登录才有效</span>",function(){
                    save({joinStatus:1},$this);
                },function(){
                    return false;
                });
            }else{
                //由开到关
                $(this).removeClass('off-btn').parents('.on-off').removeClass('red-color');
                dg.confirm("您确定要退出委托吗？<br/><span style='color:red'>退出后请重新登录才有效</span>",function(){
                    save({joinStatus:0},$this);
                },function(){
                    return false;
                });
            }
        });


        function save(data,$this){
            $.ajax({
                type: 'POST',
                url: BASE_PATH + "/shop/conf/share-join/save",
                data: data,
                success: function (result) {
                    if (result.success) {
                        dg.success("操作成功");
                        //样式切换
                        $this.toggleClass('off-btn').parents('.on-off').toggleClass('red-color');
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        }
    });
});

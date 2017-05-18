/**
 * Created by zmx on 16/12/7.
 * 上下班设置
 */

$(function(){
    var doc = $(document);
    seajs.use(['dialog','formData'],function(dg,formData){
        // 时间
        $(".clockpicker").clockpicker();
        //时间
        $(document).on('click', '.clockpicker', function (e) {
            var clock = $(this).find('.clockpicker-popover');

            if (!clock.length) {
                clock = $('body > .clockpicker-popover');
                $(this).append(clock);
            }
            clock.css({
                top: 'initial',
                left: '0',
                position: 'absolute'
            });
            e.stopImmediatePropagation();
        });

        $(document).on('blur','input',function(){
            var val = $(this).val();
            var regTime = /^([0-2][0-9]):([0-5][0-9])$/;
            if( regTime.test(val) ){
                $(this).val( val + ':00');
            }
        });

        doc.on('click','.js-save',function(){
            var signInfoConfig = formData.get('#signInfoConfigDiv', true);
            $.ajax({
                method:'post',
                dataType: 'json',
                contentType: 'application/json',
                url:BASE_PATH + '/shop/conf/work-check-on/save',
                data:JSON.stringify(signInfoConfig),
                success:function(result){
                    if(result.success){
                        dg.success('保存成功')
                    }else{
                        dg.fail(result.message);
                    }
                }
            })
        });
    })
});
/**
 * 核销服务券
 * Created by zmx on 16/9/18.
 */
$(function() {
    var $doc = $(document);

    //增加
    seajs.use('dialog',function(dg){
        $doc.on('click', '.js-add', function () {
            var numText = $(this).siblings('input');
            var i = numText.val();
            var times = $(this).parents('tr').find('.times').text();
            i++;
            numText.val(i);
            $(this).siblings('.js-sub').removeAttr('disabled');

            if(i > times){
                dg.fail('使用次数不能大于剩余次数');
                numText.val(times);
                $(this).attr('disabled','true');
            }
        });

        $doc.on('blur', '.js-consume-times', function () {
            var numText = $(this).val();
            var times = $(this).parents('tr').find('.times').text();
            if(numText > times){
                dg.fail('使用次数不能大于剩余次数');
                $(this).val(times)
            }
        });
    });


    //减少
    $doc.on('click', '.js-sub', function () {
        var numText = $(this).siblings('input');
        var i = numText.val();
        $(this).siblings('.js-add').removeAttr('disabled');
        if(i <= 0){
            $(this).attr('disabled','true')
        }else{
            i--;
            numText.val(i);
        }
    });


    //取消
    $doc.on('click','.js-return',function(){
        window.location.href = BASE_PATH +'/insurance/anxin/settle/service-list'
    });

    seajs.use(['ajax','dialog'],function(ax,dg){

        $doc.on('input', '.js-consume-times', function () {
            var val = $(this).val();
            val = checkFloat(val, 0);
            if(val == ''){
                $(this).val("0");
            }
        });

        //发送验证码
        $doc.on('click','.js-send',function(){
            var mobile = $('.phone-num').text();

            if ($(this).is(':disabled')) {
                return;
            }
            $(this).attr("disabled", true);
            var t = 60;
            var _this = this;
            $(this).text('重获手机验证码({0})'.format(t));
            var tid = setInterval(function () {
                if (--t) {
                    $(_this).text('重获手机验证码({0})'.format(t));
                } else {
                    clearInterval(tid);
                    $(_this).text('发送手机验证码');
                    $(_this).removeAttr('disabled');
                }
            }, 1000);

            $.ajax({
                type:'post',
                url:BASE_PATH + '/insurance/anxin/settle/consume-service/send',
                data:{
                    mobile:mobile
                },
                success:function(result){
                    if(result.success){
                        dg.success('亲，验证码获取成功，请接收验证码短信!');

                    }else{
                        dg.fail(result.errorMsg)
                    }
                }
            })
        });

        //提交
        $doc.on('click','.js-submit',function(){
            var consumeServiceBo ={
                // mobile:$('.phone-num').text(),
                // code:$('.captcha').val()
            };
            var consumeServiceParamBoList=[];
            var flag = true;
            $('.js-volist').each(function() {
                var $this = $(this);
                var serviceItemId = $this.find('.service-item-id').val(),
                    consumeTimes = Number($this.find('.consume-times').val()),
                    consumedUserPackageId = $this.find('.package-id').val(),
                    itemName = $this.find('.item-name').text(),
                    remainServiceTimes = Number($this.find('.times').text());
                if(!consumeTimes || consumeTimes == 0){
                    return;
                }
                if(consumeTimes > remainServiceTimes){
                    flag = false;
                    return;
                }
                consumeServiceParamBoList.push({
                    serviceItemId:serviceItemId,
                    consumeTimes:consumeTimes,
                    consumedUserPackageId:consumedUserPackageId,
                    itemName:itemName
                });

            });
            if(!flag){
                dg.warn("使用次数不能大于剩余次数");
                return false;
            }
            consumeServiceBo['consumeServiceParamBoList'] = consumeServiceParamBoList;

            $.ajax({
                type:'post',
                url:BASE_PATH +'/insurance/anxin/settle/consume-service/settle',
                data: JSON.stringify(consumeServiceBo),
                contentType: 'application/json',
                success:function(result){
                    if(result.success){
                        dg.success('核销成功',function(){
                            window.location.href = BASE_PATH + "/insurance/anxin/settle/service-list";
                        });
                    }else{
                        dg.fail(result.errorMsg)
                    }
                }
            })
        })
    });
});

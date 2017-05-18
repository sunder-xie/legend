/**
 *
 * Created by wushaui on 16/07/24.
 */
$(function() {
    seajs.use(['ajax','dialog'],
        function (aj,dialog) {
            $('.js-apply').on('click', function() {
                $.ajax({
                    url: BASE_PATH + '/shop/wechat/op/apply-use',
                    type: 'POST',
                    dataType: 'json',
                    success: function(json) {
                        if(json && json.success) {
                            dialog.success('提交成功', function() {
                                location.href = BASE_PATH + '/shop/wechat';
                            });
                        } else {
                            dialog.fail(json.errorMsg || '提交失败');
                        }
                    },
                    error: function() {
                        dialog.fail('提交失败');
                    }
                });
            });
        });
    // 协议相关的
    $('.js-agreement').on('click', function () {
        $('.wechat-agreement-box, .wechat-agreement-back').removeClass('hide');
    });

    $('.js-agree-agreement').on('click', function () {
        $('.agree').prop('checked', 'checked');
        if (! $('.agree').prop('checked')) {
            $('.js-apply').attr('disabled', 'disabled');
        } else {
            $('.js-apply').removeAttr('disabled');
        }
        $('.wechat-agreement-box, .wechat-agreement-back').addClass('hide');
    });

    $('.wechat-agreement-back, .js-agreement-close').on('click', function () {
        $('.wechat-agreement-box, .wechat-agreement-back').addClass('hide');
    });

    $('.agree').on('click', function () {
        if (! $(this).prop('checked')) {
            $('.js-apply').attr('disabled', 'disabled');
        } else {
            $('.js-apply').removeAttr('disabled');
        }
    });
    // 协议相关的 end
    $('.js-register').on('click',function(){
        window.open("https://mp.weixin.qq.com/","_blank");
    })
});

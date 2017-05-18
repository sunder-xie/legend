$(function(){
    seajs.use([
        'dialog'
    ], function(dg){
        var timer = null;
        var html;
        //图片验证码
        $(document).on('click','.js-check',function(){
            $('.js-check').fadeOut().attr('src',"/legend/imageServlet"+ '?'+ Math.floor(Math.random() * 100)).fadeIn();
        });
        //手机验证码
        $(document).on('click','.js-code',function(){
            var t = 60;
            var mobile = $.trim($('.js-mobile').val());

            $.ajax({
                type:'post',
                url:BASE_PATH + '/portal/shop/sell/send-mobile-core',
                data:{
                    mobile:mobile
                },
                success:function(result){
                    if(result.success){
                        dg.warn('亲，验证码获取成功，请接收验证码短信，短信可能被当做垃圾短信拦截，如果未查收到短信，请到垃圾短信收件箱中查找!');
                        timer = setInterval(function(){
                            t--;
                            if( t == 0 ){
                                clearInterval(timer);
                                $('.js-code').text('获取验证码').removeClass('code-btn-disabled').prop('disabled',false);
                            }else{
                                $('.js-code').text('重新获取'+ t + 's').addClass('code-btn-disabled').prop('disabled',true);
                            }
                        },1000)
                    }else{
                        dg.fail(result.message)
                    }
                }
            });
        });

        //登录
        $(document).on('click','.js-login',function(){
            var mobile = $.trim($('.js-mobile').val());
            var mobileCode = $('.captcha').val();
            var validateCode = $('.check-num').val();

            if(mobile == ''){
                dg.fail('请输入正确的手机号码');
                return false;
            }
            if(validateCode ==''){
                dg.fail('请输入正确的检验码');
                return false;
            }
            if(mobileCode ==''){
                dg.fail('请输入正确的验证码');
                return false;
            }
            $.ajax({
                url:BASE_PATH + '/index/sellShop/loginCheck',
                data:{
                    mobile:mobile,
                    validateCode:validateCode,
                    mobileCode:mobileCode
                },
                success:function(result){
                    if(result.success){
                        window.location.href = BASE_PATH + '/portal/shop/sell/select/version';
                    }else{
                        var code = result.code;
                        if(code != '' && code == 10004){
                            html = $('#certificationDialog').html();
                            dg.open({
                                area:['335px','295px'],
                                content:html
                            });
                        }else{
                            dg.fail(result.message)
                        }
                    }
                }
            });
        })
    });
});
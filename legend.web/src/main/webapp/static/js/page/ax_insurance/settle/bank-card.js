/**
 * Created by zmx on 2016/11/30.
 */
$(function(){
    var $doc = $(document);
    seajs.use([
        'formData',
        'select',
        'dialog',
        'ajax',
        'check'
    ],function(fd,st,dg,aj,ck) {
        //验证
        ck.init();

        //获取省
        st.init({
            dom: '.js-province',
            url: BASE_PATH + '/index/region/sub?pid=1',
            showKey: "id",
            showValue: "regionName",
            callback:function(showKey){
                $('.js-city').val('');
                $('.js-city').next('.yqx-select-options').remove();

                //获取地区
                st.init({
                    dom: '.js-city',
                    url: BASE_PATH + '/index/region/sub?pid='+ showKey,
                    showKey: "id",
                    showValue: "regionName"
                });
            }
        });

        //开户行
        st.init({
            dom: '.js-bank',
            url: BASE_PATH + '/shop/finance/account/get_bank_list',
            showKey: "id",
            showValue: "name"
        });

        $doc.on('input','input[name="account"]',function(){
           var text;
            text = $(this).val().replace(/\s/ig,'');
            $(this).val(text)
        });

        //验证码
        $doc.on('click','.js-captcha',function(){
            var num =60,
                timer=null,
                reg = /^1[3|4|5|7|8][0-9]\d{8}$/,
                mobile = $('input[name="mobile"]').val();

            if(!reg.test(mobile)){
                return;
            }
            $.ajax({
                type:'post',
                url:BASE_PATH + '/insurance/anxin/settle/bank-card/code',
                data:{
                    mobile:mobile
                },
                success:function(result){
                    if(result.success){
                        timer = setInterval(function(){
                            num--;
                            if( num == 0){
                                $('.js-captcha').text('发送验证码').prop('disabled',false);
                                clearInterval(timer);
                            }else{
                                $('.js-captcha').text('重新发送' + num +'s').prop('disabled',true);
                            }
                        },1000);
                    }
                }
            })
        });

        //返回按钮
        $doc.on('click', '.js-goback', function () {
            util.goBack();
        });
        //提交数据
        $doc.on('click', '.js-save-btn', function () {
            var financeAccount = JSON.stringify(fd.get("#bankInfo"));
            if (!ck.check()) {//作用域为整个页面的验证
                return;
            }
            $.ajax({
                url: BASE_PATH+'/insurance/anxin/settle/bank-card/save',
                contentType:"application/json",
                type: 'POST',
                data:financeAccount,
                dataType: 'json',
                success:function(result){
                    if(result.success){
                        dg.success("提交成功", function () {
                            window.location.href = BASE_PATH+"/insurance/anxin/settle/bank-card-detail?id="+result.data+"&refer=bank-card";
                        });
                    }else{
                        dg.fail(result.errorMsg);
                    }
                }
            });
        });

    });
});
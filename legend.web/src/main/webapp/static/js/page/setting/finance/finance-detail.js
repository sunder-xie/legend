/*
 * create by zmx 2017/1/5
 * 银行卡信息
 */

$(function(){
    var doc = $(document);
    var timer = null;
    seajs.use([
        'check',
        'select',
        'dialog',
        'formData'
    ],function(ck,st,dg,fd){
        //验证
        ck.init();

        st.init({
            dom:'.js-bank'
        });
        var provinceId = $('input[name="bankProvinceId"]').val();
        //选择省
        st.init({
            dom: '.js-province',
            url: BASE_PATH + '/index/region/sub?pid=1',
            showKey: "id",
            showValue: "regionName",
            callback:function(showKey){
                $('.js-city').val('');
                $('input[name="bankCityId"]').val('');
                $('.city-box').find('.yqx-select-options').remove();
                $('input[name="bankProvinceId"]').val(showKey);
                provinceId = showKey;
            }
        });

        //选择市
        st.init({
            dom: '.js-city',
            url: BASE_PATH + '/index/region/sub',
            params: function (){
                return {
                    pid: provinceId
                }
            },
            showKey: "id",
            showValue: "regionName",
            callback:function(showKey){
                $('input[name="bankCityId"]').val(showKey)
            }
        })


        //发送验证码
        doc.on('click','.js-getCode',function(){
            var mobile = $('input[name="mobile"]').val();
            var t = 60;
            var isShop = $("#isShop").val();
            timer = setInterval(function(){
                t--;
                if( t == 0 ){
                    $('.js-getCode').text('获取验证码').prop('disabled',false);
                    clearInterval(timer);
                } else{
                    $('.js-getCode').text('重新获取' + t +'s').prop('disabled',true);
                }
            },1000);

            $.ajax({
                dataType: 'json',
                url: BASE_PATH + '/shop/settlement/activity/get-code',
                type:'POST',
                data: {
                    isShop: isShop,
                    mobile : mobile
                },
                success:function(result){
                    if(result.success){
                        dg.success("亲，验证码获取成功，请接收验证码短信！短信可能被当做垃圾短信拦截，如果未收到短信，请到垃圾短信收件箱中查找！")
                    }else{
                        dg.fail(result.errorMsg)
                    }
                }
            })
        });

        //返回按钮
        doc.on('click','.js-goBack',function(){
            var flag = $("#isShop").val();
            if (flag == 'true') {
                location.href = BASE_PATH + "/shop/setting/edit-detail";
            } else {
                location.href = BASE_PATH + "/shop/setting/user-info/user-info";
            }
        });

        //保存按钮
        doc.on('click','.js-save',function(){
            var financeAccount = fd.get('#formData');
            if(!ck.check()){
                return;
            }
            var isShop = $("#isShop").val();
            var password = $("#password").val();
            var financeAccountVo = {
                financeAccount : financeAccount,
                isShop: isShop,
                password:password
            }
            $.ajax({
                type:"POST",
                url:BASE_PATH +'/shop/setting/finance/bank-bind',
                data: JSON.stringify(financeAccountVo),
                dataType: 'json',
                contentType: "application/json",
                success:function(result){
                    if(result.success){
                        dg.success('保存成功',function(){
                            window.location.reload();
                        });
                    }else{
                        dg.fail(result.message)
                    }
                }
            })
        })
    })
});
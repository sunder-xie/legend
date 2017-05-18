/**
 * sven  2016-06-11
 * 新增付款单
 */

$(function(){
    var $doc = $(document);
    seajs.use([
        'formData',
        'select',
        'dialog',
        'ajax',
        'check'
    ],function(fd,st,dg,aj,ck){
        //验证
        ck.init();

        //付款类型
        st.init({
            dom: '.js-pay-type',
            url: BASE_PATH + '/shop/settlement/pay/get-pay-type?action=true',
            showKey: "id",
            showValue: "typeName"
        });
        //付款方式下拉列表
        st.init({
            dom: '.js-method',
            url: BASE_PATH + '/shop/payment/get_payment',
            showKey: "id",
            showValue: "name"
        });
        $doc.on('click', '.js-save-btn', function () {
            var payForm = JSON.stringify(fd.get("#payForm"));
            if (!ck.check()) {//作用域为整个页面的验证
                return;
            }
            $.ajax({
                url: BASE_PATH+'/shop/settlement/pay/pay-add/save',
                data:{payForm : payForm},
                type: 'POST',
                success:function(data){
                    if(data.success){
                        dg.success("保存成功", function () {
                            window.location.href = BASE_PATH+"/shop/settlement/pay/pay-flow"
                        });
                    }else{
                        dg.fail(data.errorMsg);
                    }
                }
            });
        });
    })
});
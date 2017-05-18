/**
 * zmx  2016-06-01
 * 新建收款单
 */

$(function(){
    var $doc = $(document);

    seajs.use([
        'formData',
        'dialog',
        'select',
        'check',
        'date'
    ],function(fd,dg,st,ck,date){
        // 验证
        ck.init();
        dg.titleInit();

        // 业务类型select下拉列表初始化。
        st.init({
            dom: ".js-debit-type",
            url: BASE_PATH + '/shop/settlement/debit/type/list?needFilter=true',
            showKey: "id",
            showValue: "typeName"
        });

        // 付款方式select下拉列表初始化。
        st.init({
            dom: ".js-payment-type",
            url: BASE_PATH + '/shop/payment/get-order-payment',
            showKey: "id",
            showValue: "name"
        });

        // 收款日期
        date.datePicker('.js-gmtCreateStr', {
            maxDate: '%y-%M-%d',
            dateFmt: 'yyyy-MM-dd HH:mm'
        });

        $doc.on('input', '.js-pay-amount', function () {
            var val = $(this).val();
            val = checkFloat(val, 2);
            if(val == '.') {
                val = 0;
            }
            $(this).val(val);
        });

        //提交按钮
        $doc.on('click','.js-submit',function(){
            //先校验信息。
            var result = ck.check();
            if (!result) {
                return;
            }
            var data = fd.get('#formData');
            $.ajax({
                url: BASE_PATH + '/shop/settlement/debit/save-bill',
                type:'post',
                data:data,
                success:function(result){
                    if(result.success){
                        dg.success("提交成功", function () {
                            window.location.href = BASE_PATH + "/shop/settlement/debit/flow-list?refer=debit-bill-add";
                        });
                    }else{
                        dg.fail(result.errorMsg);
                    }
                }
            })
        });

    });



});
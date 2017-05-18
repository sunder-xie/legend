/**
 * zmx  2016-06-02
 * 付款 页面
 */

$(function () {
    var $doc = $(document);
    seajs.use([
        'select',
        'table',
        'check',
        'dialog'
    ], function (st, tb, ck, dg) {
        //验证
        ck.init();

        //付款方式下拉列表
        st.init({
            dom: '.js-method',
            url: BASE_PATH + '/shop/payment/get_payment',
            showKey: "id",
            showValue: "name"

        });
        var ids = $('#ids').val();

        //表格填充
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/settlement/pay/pay-supplier-behavior/list',
            //表格数据目标填充id，必需
            fillid: 'payTable',
            //表格模板id，必需
            tplid: 'payTableTpl',
            //扩展参数,可选
            data: {
                ids: ids
            },
            //渲染表格数据完后的回调方法,可选
            callback: null
        });
        $doc.on('click', '.js-save-btn', function () {
            if (!ck.check()) {//作用域为整个页面的验证
                return ;
            }
            var ids = $('#ids').val();
            var amount =  parseFloat($('#amount').val());
            var totalAmount = parseFloat($('#totalAmount').html());
            var flag =check(totalAmount,amount);
            if(!flag){
                return ;
            }
            var supplierId = $('input[name="search_supplierId"]').val();
            var supplierName = $('input[name="search_supplierName"]').val();
            $.ajax({
                url: BASE_PATH + '/shop/settlement/pay/pay-supplier-behavior/operate',
                data: {
                    paymentId: $('#paymentId').val(),
                    paymentName: $('#paymentName').val(),
                    amount: amount,
                    remark: $('#remark').val(),
                    ids: ids
                },
                type: 'POST',
                success: function (data) {
                    if (data.success) {
                        dg.success("付款成功", function () {
                            window.location.href = BASE_PATH + "/shop/settlement/pay/pay-supplier-list?supplierId="+supplierId+"&supplierName="+supplierName+"&refer=pay-supplier-operator";
                        });
                    } else {
                        dg.fail(data.errorMsg);
                    }
                }
            });

        });
        function check(totalAmount,amount){

            if (totalAmount !=0 && amount == 0) {
                dg.warn("输入的金额必须在0和"+totalAmount+"之间");
                return false;
            }
            if ((totalAmount < 0 && amount > 0) ) {
                dg.warn("输入的金额必须在"+totalAmount+"与0之间");
                return false;
            }
            if(totalAmount > 0 && amount < 0){
                dg.warn("输入的金额必须在0与"+totalAmount+"之间");
                return false;
            }
            if (totalAmount < 0) {
                if(totalAmount > amount ){
                    dg.warn("输入的金额必须在"+totalAmount+"与0之间");
                    return false;
                }
            }
            if (totalAmount >0) {
                if(totalAmount < amount ){
                    dg.warn("输入的金额必须在0与"+totalAmount+"之间");
                    return false ;
                }
            }

            return true;
        }
        //输入的值判断
        $doc.on('blur', '.js-pay', function () {
            if (!ck.check("#amount") || $('#amount').val() == "") {//作用域为整个页面的验证
                return ;
            }
            var amount = parseFloat($('#amount').val());
            var totalAmount = parseFloat($('#totalAmount').html());
           var flag =  check(totalAmount,amount);
            if(flag){
                var unpaidAmount = totalAmount - amount;
                $('#unpaidAmount').html(unpaidAmount.toFixed(2));
                $('#paidAmount').html(amount.toFixed(2));
            }
        });
    });

    //表格展开收缩
    $doc.on('click', '.js-tableShow', function () {
        var btnText = $(this).find('span').text();
        $('.show-table').slideToggle(500);
        if (btnText == '展开') {
            $(this).find('span').text('收缩');
            $(this).find('i').removeClass('icon-angle-down').addClass('icon-angle-up');
        } else {
            $(this).find('span').text('展开');
            $(this).find('i').removeClass('icon-angle-up').addClass('icon-angle-down');
        }
    });
    //返回按钮
    $doc.on('click', '.js-goback', function () {
            util.goBack();
    });
});
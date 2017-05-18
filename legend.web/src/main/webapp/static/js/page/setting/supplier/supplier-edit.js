/*
 * create by zmx 2016/12/05
 * 新增/编辑供应商页面
 */
$(function () {
    var doc = $(document);
    seajs.use([
        'check',
        'dialog',
        'formData',
        'select'
    ], function (ck, dg, fd, st) {
        ck.init();
        //
        st.init({
            dom: '.js-category',
            url: BASE_PATH + '/shop/setting/supplier/get_supplier_category',
            showKey: "code",
            showValue: "name"
        });
        st.init({
            dom: '.js-pay-method',
            url: BASE_PATH + '/shop/setting/supplier/get_pay_method',
            showKey: "code",
            showValue: "name"
        });
        //开票类型
        doc.on('click', '.js-receipt li', function () {
            $(this).addClass('receipt-current').siblings().removeClass('receipt-current');
            var invoiceType = $('.receipt-current').data("invoiceType");
            $('input[name="invoiceType"]').val(invoiceType);
        });
        function getInvoiceType(){
            var invoiceType = $('input[name="invoiceType"]').val();
            $('.js-receipt li').eq(invoiceType).addClass('receipt-current');
        };
        getInvoiceType();


        //保存
        doc.on('click', '.js-save', function () {
            if (!ck.check()) {
                return;
            }
            var supplier = JSON.stringify(fd.get(".js-supplier"));
            $.ajax({
                url: BASE_PATH + '/shop/setting/supplier/save',
                contentType: 'application/json',
                dataType: 'JSON',
                data: supplier,
                type: 'POST',
                success: function (data) {
                    if (data.success) {
                        dg.success("保存成功", function () {
                            window.location.href = BASE_PATH + "/shop/setting/supplier/supplier-list"
                        });
                    } else {
                        dg.fail(data.message);
                    }
                }
            });
        });

        //返回
        doc.on('click', '.js-return', function () {
            util.goBack();
        });

    });
});
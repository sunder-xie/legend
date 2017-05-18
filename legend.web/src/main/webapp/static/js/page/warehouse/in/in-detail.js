$(function () {
    seajs.use('dialog', function (dialog) {
        var id = $('#id').val();
        var $doc = $(document);

        dialog.titleInit();

        $('.form-footer.btn-group .fl').find('button')
            .eq(0).addClass('yqx-btn-2').removeClass('yqx-btn-1');
        //付款
        $doc.on('click', '.js-pay', function () {
            var supplierId = $(this).data('supplierId');
            var supplierName = $(this).data('supplierName');
            window.location.href = BASE_PATH + "/shop/settlement/pay/pay-supplier-list?supplierId=" + supplierId + "&supplierName=" + supplierName + "&refer=in-detail";
        });

        $doc.on('click', '.js-edit', function () {
            window.location.href = BASE_PATH + "/shop/warehouse/in/in-edit/draft?id=" + id + "&refer=in-detail";
        });

        //退货
        $doc.on('click', '.js-stock-refund', function () {
            window.location.href = BASE_PATH + "/shop/warehouse/in/in-red?id=" + id + "&refer=in-detail";
        });
        //作废
        $doc.on('click', '.js-abolish', function () {
            var status = $(this).data('status');
            if(status == 'LZRK'){
                dialog.confirm('确认作废该入库单及其所有退货记录?', function () {
                    abolish();
                });
                return;
            }
            dialog.confirm('确认作废?', function () {
                abolish();
            });
        });
        //作废
        function abolish() {
            $.ajax({
                url: BASE_PATH + '/shop/warehouse/in/abolish',
                data: {
                    id: id
                },
                type: 'POST',
                success: function (data) {
                    if (data.success) {
                        dialog.success("作废成功", function () {
                            window.location.href = BASE_PATH + "/shop/warehouse/in/in-detail?id=" + id + "&refer=in-detail";
                        });
                    } else {
                        dialog.fail(data.errorMsg);
                    }
                }
            });
        }
        //打印
        $doc.on('click', '.js-print', function () {
            util.print(BASE_PATH + "/shop/warehouse/in/print?id=" + id + "&refer=in-detail");
        });
        //删除
        $doc.on('click', '.js-delete', function () {
            dialog.confirm('确认删除?', function () {
                $.ajax({
                    url: BASE_PATH + '/shop/warehouse/in/delete',
                    data: {
                        id: id
                    },
                    type: 'POST',
                    success: function (data) {
                        if (data.success) {
                            dialog.success("删除成功", function () {
                                window.location.href = BASE_PATH + "/shop/warehouse/in/in-list?status=DRAFT&refer=in-detail";
                            });
                        } else {
                            dialog.fail(data.errorMsg);
                        }
                    }
                });
            });
        });
        //转入库
        $doc.on('click', '.js-stock', function () {
            $.ajax({
                url: BASE_PATH + '/shop/warehouse/in/in-detail/draft/stock',
                data: {
                    id: id
                },
                type: 'POST',
                success: function (data) {
                    if (data.success) {
                        dialog.success("入库成功", function () {
                            window.location.href = BASE_PATH + "/shop/warehouse/in/in-detail?id=" + id + "&refer=in-detail";
                        });
                    } else {
                        dialog.fail(data.errorMsg);
                    }
                }
            });
        });

        $('.js-back').on('click', util.goBack);

    });

});
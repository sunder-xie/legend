/**
 * 出库单详情
 * zmx 2016-08-25
 */
$(function () {
    seajs.use([
        'select',
        'dialog',
        'formData',
        'check'
    ], function (st, dg, formData, ck) {
        dg.titleInit();
        //验证
        ck.init();
        //领料人下拉列表
        st.init({
            dom: '.js-picking',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });

        //出库人下拉列表
        st.init({
            dom: '.js-out-person',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });

        //出库类型下拉列表
        st.init({
            dom: '.js-out-type',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });
        //删除作废单
        $(document).on('click', '.js-delete', function () {
            var id = $(this).data("id");
            dg.confirm('确认删除?', function () {
                $.ajax({
                    url: BASE_PATH + "/shop/warehouse/out/delete",
                    data: {
                        id: id
                    },
                    type: "POST",
                    success: function (data) {
                        if (data.success) {
                            dg.success("删除成功", function () {
                                window.location.href = BASE_PATH + "/shop/warehouse/out/out-list";
                            });
                        } else {
                            dg.fail(data.errorMsg);
                        }
                    }
                });
            });
        });
        //作废
        $(document).on('click', '.js-abolish', function () {
            var id = $(this).data("id");
            var status = $(this).data('status');
            if (status == 'LZCK') {
                dg.confirm('确认作废该出库单及其所有退货记录?', function () {
                    abolish(id);
                });
                return;
            }
            dg.confirm('确认作废?', function () {
                abolish(id);
            });
        });
        //作废
        function abolish(id) {
            $.ajax({
                url: BASE_PATH + "/shop/warehouse/out/abolish",
                data: {
                    id: id
                },
                type: "POST",
                success: function (data) {
                    if (data.success) {
                        dg.success("作废成功", function () {
                            window.location.href = BASE_PATH + "/shop/warehouse/out/out-detail?id=" + id;
                        });
                    } else {
                        dg.fail(data.errorMsg);
                    }
                }
            });
        }

        //退货
        $(document).on('click', '.js-refund', function () {
            var id = $(this).data("id");
            window.location.href = BASE_PATH + "/shop/warehouse/out/out-refund?id=" + id;
        });
        //打印
        $(document).on('click', '.js-print', function () {
            var id = $(this).data("id");
            util.print(BASE_PATH + "/shop/warehouse/out/out-print?id=" + id);
        });

        //返回
        $(document).on('click', '.js-return', function () {
            util.goBack();
        });

        $('.js-save').on('click', function () {
            if (!ck.check()) {//作用域为整个页面的验证
                return;
            }
            var id = $('#warehouseOutId').val();
            var data = [];
            $('#list tbody tr').each(function () {
                data.push(
                    formData.get($(this))
                )
            });
            $.ajax({
                url: BASE_PATH + "/shop/warehouse/out/out-detail/update",
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(data),
                type: "POST",
                success: function (data) {
                    if (data.success) {
                        dg.success("保存成功", function () {
                            window.location.href = BASE_PATH + "/shop/warehouse/out/out-detail?id=" + id;
                        });
                    } else {
                        dg.fail(data.errorMsg);
                    }
                }
            });
        })
    });
});
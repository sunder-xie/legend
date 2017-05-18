$(function () {
    seajs.use([
        'table',
        'select',
        'date',
        'dialog',
        'formData',
        'check'
    ], function (tb, st, dt, dg, fd, ck) {
        dg.titleInit();
        ck.init();
        var doc = $(document);
        // 配置的日期
        dt.datePicker('.js-outTime', {
            dateFmt: 'yyyy-MM-dd HH:mm'
        });

        // 领料人
        st.init({
            dom: '.js-select-user',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name",
            canInput: true
        });

        //初始化出库类型的下拉数据
        st.init({
            dom: '.js-out-type',
            url: BASE_PATH + '/shop/warehouse/out/get_out_type',
            showKey: "name",
            showValue: "message",
            selectedKey: "GDCK"
        });


        //保存其他入库
        $(document).on('click', '.js-save', function () {
            var result = ck.check();
            if (!result) {
                return;
            }
            var warehouseOut = fd.get('.js-stock'),
                warehouseOutDetail = [];
            var flag = true, errList = [];
            $('.js-detail').each(function (i) {
                var goodsCount = +$(this).find('input[name="goodsCount"]').val();
                var orderCount = +$(this).find('[name="orderCount"]').val();
                var outNumber  = +$(this).find('[name="outNumber"]').val();
                if ( (orderCount - outNumber) < goodsCount ) {
                    errList.push(i+1);
                    flag = false;
                }
                if (goodsCount != 0) {
                    warehouseOutDetail.push(fd.get($(this)));
                }
            });
            if (!flag) {
                dg.warn('第{0}行配件 出库数量不能大于可出库数量（开单数量 - 已出数量），请修改'.format(errList.join(',')));
                return;
            }
            if (!warehouseOutDetail.length) {
                dg.warn('出库配件不能为空');
                return;
            }
            $.ajax({
                url: BASE_PATH + "/shop/warehouse/out/stock/out",
                dataType: 'json',
                data: {
                    warehouseOutBo: JSON.stringify(warehouseOut),
                    warehouseOutDetailBoList: JSON.stringify(warehouseOutDetail)
                },
                type: 'POST',
                success: function (data) {
                    if (data.success) {
                        dg.success("出库成功", function () {
                            window.location.href = BASE_PATH + "/shop/warehouse/out/out-detail?id=" + data.data + "&refer=in-edit";
                        });
                    } else {
                        dg.fail(data.errorMsg);
                    }
                }
            });
        });

        // 返回
        doc.on('click', '.js-back', function () {
            util.goBack();
        });

    });
});
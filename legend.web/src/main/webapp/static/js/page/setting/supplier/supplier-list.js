/*
 * create by zmx 2016/12/02
 * 供应商资料页面
 */
$(function () {
    var doc = $(document);
    var mergeDialog = null;
    seajs.use([
        'table',
        'select',
        'dialog',
        'formData',
        'check'
    ], function (tb, st, dg, fd, ck) {
        dg.titleInit();
        var tableList = tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/setting/supplier/supplier-list/data',
            //表格数据目标填充id，必需
            fillid: 'tableCon',
            //分页容器id，必需
            pageid: 'pagingCon',
            //表格模板id，必需
            tplid: 'tableTpl',
            //关联查询表单id，可选
            formid: 'formData',
            //渲染表格数据完后的回调方法,可选
            callback: null
        });

        //分类下拉列表
        st.init({
            dom: '.js-category',
            url: BASE_PATH + '/shop/setting/supplier/get_supplier_category',
            showKey: "code",
            showValue: "name",
            pleaseSelect: true
        });

        //付款方式下拉列表
        st.init({
            dom: '.js-payway',
            url: BASE_PATH + '/shop/setting/supplier/get_pay_method',
            showKey: "code",
            showValue: "name",
            pleaseSelect: true
        });

        //开票类型下拉列表
        st.init({
            dom: '.js-type',
            url: BASE_PATH + '/shop/setting/supplier/get_invoice_type',
            showKey: "code",
            showValue: "name",
            pleaseSelect: true
        });

        //request供应商下拉列表
        st.init({
            dom: '.js-supplier-request',
            url: BASE_PATH + '/shop/setting/supplier/get_supplier_list',
            showKey: "id",
            showValue: "supplierName",
            canInput: true
        });
        //request供应商下拉列表
        st.init({
            dom: '.js-supplier-result',
            url: BASE_PATH + '/shop/setting/supplier/get_supplier_list',
            showKey: "id",
            showValue: "supplierName",
            canInput: true
        });

        //编辑
        doc.on('click', '.js-edit', function (e) {
            e.stopPropagation();
            var id = $(this).parents('tr').data('id');
            window.location.href = BASE_PATH + '/shop/setting/supplier/supplier-edit?id=' + id + '&refer=supplier-list';
        });
        //编辑
        doc.on('click', '.js-edit-tr', function () {
            var id = $(this).data('id');
            window.location.href = BASE_PATH + '/shop/setting/supplier/supplier-edit?id=' + id + '&refer=supplier-list';
        });

        //删除
        doc.on('click', '.js-delete', function (e) {
            e.stopPropagation();
            var id = $(this).parents('tr').data('id');
            dg.confirm('确定要删除数据吗？', function () {
                $.ajax({
                    url: BASE_PATH + '/shop/setting/supplier/delete',
                    data: {
                        id: JSON.stringify(id)
                    },
                    type: 'POST',
                    success: function (result) {
                        if (result.success) {
                            dg.success("删除成功", function () {
                                tableList(1);
                            });
                        } else {
                            dg.fail(result.message)
                        }
                    }
                });
            });
        });

        //新增供应商
        doc.on('click', '.js-add-supplier', function () {
            window.location.href = BASE_PATH + '/shop/setting/supplier/supplier-edit?refer=supplier-list';
        });
        //合并供应商
        doc.on('click', '.js-merge', function () {
            var html = $('#mergeTpl').html();
            mergeDialog = dg.open({
                area: ['560px', '350px'],
                content: html
            })
        });
        //取消
        doc.on('click', '.js-remove', function () {
            dg.close(mergeDialog);
        });
        //合并
        doc.on('click', '.js-merge-save', function () {
            var requestId = $('input[name="requestId"]').val();
            var resultId = $('input[name="resultId"]').val();
            if (requestId == resultId) {
                dg.warn("请选择不同的供应商合并");
                return;
            }
            if (!ck.check('#dialogRequired')) {
                return;
            }
            dg.confirm('确定要合并供应商吗？', function () {
                $.ajax({
                    url: BASE_PATH + '/shop/setting/supplier/merge',
                    data: {
                        requestId: requestId,
                        resultId: resultId
                    },
                    type: 'POST',
                    success: function (result) {
                        if (result.success) {
                            dg.close();
                            dg.success("合并成功", function () {
                                tableList(1);
                            });
                        } else {
                            dg.fail(result.message)
                        }
                    }
                });
            });
        });
    })
});
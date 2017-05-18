/*
 * create by zmx 2016/11/14
 * 油漆盘点单详情
 */
$(function () {
    var doc = $(document);

    //返回按钮
    doc.on('click', '.js-return', function () {
        window.location.href = BASE_PATH + '/paint/inventory/toInventoryPaintList';
    });

    //打印按钮
    doc.on('click', '.js-print', function () {
        var id = $('.record_id').val();
        util.print(BASE_PATH + '/paint/inventory/toInventoryPaintPrint?id=' + id);
    });

    //导出按钮
    doc.on('click', '.js-export-inventoryPaint', function () {
        var id = $('.record_id').val();
        window.location.href = BASE_PATH + '/paint/inventory/inventoryPaintDtlExport?id=' + id;
    });
});
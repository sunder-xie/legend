/**
 * 库存盘点详情
 * zmx 2016-08-08
 */

$(function () {
    var doc = $(document);
    seajs.use([
        'dialog'
    ], function (dg) {
        //溢出隐藏
        dg.titleInit();

        //  返回
        doc.on('click', '.js-return', function () {
            window.document.location.href = BASE_PATH + "/shop/warehouse/stock/stock-inventory";
        });

        // 导出
        doc.on('click', '.js-export', function () {
            // 记录ID
            var itemId = $('input[name="recordId"]').val();
            window.location.href =BASE_PATH + "/shop/warehouse/stock/inventory-export?id=" + itemId ;
        });

        // print
        doc.on('click', '.js-print', function () {
            // 记录ID
            var itemId = $('input[name="recordId"]').val();
            util.print(BASE_PATH + "/shop/warehouse/stock/inventory_print?id=" + itemId);
        });

        // 编辑页面
        doc.on('click', '.js-edit', function () {
            // 记录ID
            var itemId = $('input[name="recordId"]').val();
            window.document.location.href = BASE_PATH + "/shop/warehouse/stock/stock-inventory-edit?itemid=" + itemId;
        });

        // generate
        doc.on('click', '.js-generate', function () {

            // 记录ID
            var itemId = $('input[name="recordId"]').val();

            $.ajax({
                type: 'get',
                data: {'itemid': itemId},
                url: BASE_PATH + '/shop/warehouse/stock/stock-inventory-toformal'
            }).done(function (json) {
                if (json.success) {
                    dg.success("生成盘点单成功", function () {
                        window.document.location.reload();
                    });
                } else {
                    var code = json["code"];
                    var errormsg = "";
                    if (code === 'deleted') {
                        errormsg = ["被盘点的配件不存在，请删除后，重新生成盘点单", "零件号：" + json["errorMsg"]].join("<br/>");
                        dg.fail(errormsg);
                    } else {
                        errormsg = json["errorMsg"];
                    }
                    dg.fail(errormsg);
                }
            });
        });

    });
});
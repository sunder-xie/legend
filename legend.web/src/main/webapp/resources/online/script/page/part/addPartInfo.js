/**
 * create by wjc 2015.05.27
 */
$(function () {
    //添加配件
    $("body").on("click", ".opt_add a", function () {
        var parent = $(this).parents(".opt_add");
        parent.addClass("hide");
        parent.siblings(".opt_save").removeClass("hide");
    });
    //取消添加配件
    $("body").on("click", ".qxy_part_cancel", function () {
        var parent = $(this).parents(".opt_save");
        parent.addClass("hide");
        parent.siblings(".opt_add").removeClass("hide");
    });
    $(document).on("click", ".qxy_part_save", function () {
        var $this = $(this);
        var formid = $this.parents("form").attr("id");
        seajs.use("check", function (check) {
            if (!check.check(formid)) {
                return;
            }
            var extData = {};
            extData['goodsAttrRelList'] = '[]';
            var attrListStr = $("#" + formid).find('input[name="goodsAttrRelList"]').val();
            if (attrListStr && attrListStr != "null") {
                attrList = JSON.parse(attrListStr);
                attrExList = [];
                $.each(attrList, function (i, e) {
                    attrExList.push({id: e.id, attrName: e.name, attrValue: e.value});
                })
                extData['goodsAttrRelList'] = JSON.stringify(attrExList);
            }
            util.submit({
                formid: formid,
                extData: extData,
                method: "get2",
                callback: function (json, dialog) {
                    if (json.success) {
                        dialog.info("添加成功", 1);
                        seajs.use("table", function (table) {
                            table.fill("addPart_search_table");
                        });
                    } else {
                        dialog.info(json.errorMsg, 3);
                    }
                }
            });
        });
    });
});

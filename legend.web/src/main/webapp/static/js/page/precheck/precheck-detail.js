/**
 * zmx 2016-04-08
 * 预检单详情页面。
 */
$(function () {
    var doc = $(document);



    seajs.use([
        'dialog',
        'ajax'
    ], function(dg, ax) {
        dg.titleInit();
        //删除按钮
        doc.on("click",".js-precheck-delete",function() {
            var precheckId = $('#precheck_id').val();
            dg.confirm("您确定要删除吗?", function () {
                $.ajax({
                    type: 'post',
                    url: BASE_PATH + "/shop/precheck/precheck-delete",
                    data: {
                        precheckId: precheckId
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success("删除成功!", function () {
                                window.location.href = BASE_PATH + "/shop/precheck/precheck";
                            });
                        } else {
                            dg.fail("预检单删除失败!");
                        }
                    }
                });
            });
        });

    });

    doc.on('click','.js-return',function(){
        util.goBack();
    })

    doc.on('click','.js-print',function(){
        var precheckId = $('#precheck_id').val();
        util.print(BASE_PATH + "/shop/precheck/precheck-print?id="+precheckId+"&refer=precheck-detail");

    })




});


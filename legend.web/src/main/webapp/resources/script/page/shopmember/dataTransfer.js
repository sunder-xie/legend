$(function () {
    $("#empty").click(function () {
        ajaxFn("/member/transfer/empty");
    })

    $("#transfer").click(function(){
        ajaxFn("/member/transfer/execute");
    })

    var ajaxFn = function(url){
        seajs.use(["ajax", "dialog"], function (ajax, dialog) {
            ajax.post({
                url: BASE_PATH + url,
                success: function (json) {
                    if (json && json.success) {
                        dialog.info(json.data, 1);
                    } else {
                        dialog.info(json.errorMsg, 0);
                    }
                }
            });
        });
    }
})
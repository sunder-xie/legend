var SmartAjax = function () {
    var sendAjaxRequest = function(param){
        // 拼凑参数
        var reqParam = AjaxParamProcessor.process(param);
        $.ajax(reqParam);
    };

    return {
        get: function (param) {
            sendAjaxRequest(param);
        },
        post: function (param) {
            param.type = 'POST';
            this.get(param);
        }
    }
}();

// 拼凑参数
var AjaxParamProcessor = {
    process: function(param) {
        var reqParam = $.extend({
            url: '',
            type: 'GET',
            dataType: 'json',
            data: null,
            async: true,
            cache: true,
            contentType: 'application/x-www-form-urlencoded',
            timeout: 60000,
            success: function () {
            }
        }, param);

        return reqParam;
    }
};
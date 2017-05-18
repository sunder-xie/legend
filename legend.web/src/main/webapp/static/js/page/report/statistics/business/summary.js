$(function () {
    var historyDialog = $('.history-dialog')
        .remove().removeClass('hide')[0];
    var _reportDate = $("#reportDate").val();

    seajs.use([
        'dialog'
    ], function (dialog) {

        exportSecurity.tipAndConfirm({
            title: '管理报表—门店经营分析报告',
            dom: '.js-preview',
            async: false,
            callbackType: 'url',
            callback: function (json) {
                var reportDate = $(this).data('reportDate') || _reportDate;
                return BASE_PATH + '/shop/report/business/month/preview?month=' + reportDate;
            }
        });

        exportSecurity.tipAndConfirm({
            title: '管理报表—门店经营分析报告',
            dom: '.js-download',
            async: false,
            callbackType: 'url',
            callback: function (json) {
                var reportDate = $(this).data('reportDate') || _reportDate;
                return BASE_PATH + '/shop/report/business/month/download?month=' + reportDate;
            }
        });


        $('.js-show-history').click(function () {

            dialog.open({
                area: ['460px'],
                content: historyDialog.outerHTML
            });
        });
    }); // seajs

});// onReady
/**
 * zsy  2016-04-14
 * 综合维修单详情
 */

$(function(){
    var $document = $(document);
    var orderId = $("#orderId").val();
    seajs.use([
        'art',
        'dialog'
    ], function(at, dg) {
        //打印 弹窗
        $document.on('click','.js-print',function(){
            var html = at('print-dialog-tpl')();
            dg.open({
                area: ['300px','250px'],
                content: html
            });
            return false;
        });

        //普通打印
        $document.on('click','.js-common-print',function(){
            window.open(BASE_PATH + '/shop/order/common-print?id=' + orderId);
        });
        //简化版打印
        $document.on('click','.js-simple-print',function(){
            window.open(BASE_PATH + '/shop/order/common-simpleprint?id=' + orderId);
        });
        //试车单打印
        $document.on('click','.js-trialrun-print',function(){
            window.open(BASE_PATH + '/shop/order/trialrun-print?id=' + orderId);
        });
    })
});
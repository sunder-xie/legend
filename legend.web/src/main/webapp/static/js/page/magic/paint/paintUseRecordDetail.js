/*
 * create by zmx 2016/11/14
 * 使用记录详情
 */

$(function(){
    var doc = $(document);
    //返回按钮
    doc.on('click','.js-return',function(){
        window.location.href = BASE_PATH + '/shop/paint/record/toPaintUseRecordList';
    });
    //导出按钮
    doc.on('click','.js-export',function(){
        var useRecordId = util.getPara('id');
        window.location.href = BASE_PATH + '/shop/paint/record/exportPaintRecord/'+ useRecordId;
    });
    //打印按钮
    doc.on('click','.js-print',function(){
        var useRecordId = util.getPara('id');
        util.print(BASE_PATH + '/shop/paint/record/printPaintUseRecord?id='+ useRecordId);
    })

});
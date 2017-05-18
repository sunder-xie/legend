/*
 *  zmx 2016/10/14
 *  钣喷受托数据统计详情
 */
$(function(){
    var doc = $(document);
    seajs.use([
        'dialog',
        'art'
    ],function(dg,at){
        dg.titleInit();

        //表格数据填充
        var data = {
            shopId:$('.shop-id').val(),
            proxyStartTime:$('.start-time').text(),
            proxyEndTime:$('.end-time').text()
        };
        $.ajax({
            type:'post',
            url:BASE_PATH + '/proxy/report/proxyReportDtl',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: "application/json",
            success:function(result){
                if(result.success){
                    var html = at('tableTpl',{json:result});
                    $('#tableCon').html(html);
                }else{
                    dg.fail(result.message)
                }
            }
        });
    })
});

//导出
$(document).on('click','.export-excel',function(){
    var shopId = $('.shop-id').val(),
        startTime = $('.start-time').text(),
        endTime = $('.end-time').text();

    window.location.href = BASE_PATH + '/proxy/report/exportProxyReportDtl?startTime='+startTime+'&endTime='+endTime+'&shopId='+shopId;
});
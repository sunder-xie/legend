/**
 * Created by zmx on 2016/10/14.
 * 中断车辆详情
 */
$(function(){
    var doc = $(document);

    //恢复按钮
    seajs.use([
        'dialog'
    ],function(dg){
        doc.on('click','.js-recovery',function(){
            var workOrderId = $(this).parents('.table-content').find('#work-order-id').val();
            var processId = $(this).parents('.table-content').find('#process-id').val();
            $.ajax({
                url: BASE_PATH + "/workshop/workOrder/interruptRecovery",
                type:'post',
                data:{
                    workOrderId: workOrderId,
                    processId:processId
                },
                success:function(result){
                    if(result.success){
                        dg.success('恢复成功');
                        window.location.href = BASE_PATH + "/workshop/workOrder/toWorkOrderBreakList"
                    }else{
                        dg.fail(result.message);
                    }
                }
            });
        });

        //完工按钮
        doc.on('click','.js-finished',function(){
            var workOrderId = $(this).parents('.table-content').find('#work-order-id').val();
            var processId = $(this).parents('.table-content').find('#process-id').val();
            $.ajax({
                url: BASE_PATH + "/workshop/workOrder/interruptedCompletion",
                type:'post',
                data:{
                    workOrderId: workOrderId,
                    processId:processId
                },
                success:function(result){
                    if(result.success){
                        dg.success('提前完工成功');
                        window.location.href = BASE_PATH + "/workshop/workOrder/toWorkOrderBreakList"
                    }else{
                        dg.fail(result.message);
                    }
                }
            });
        });

        //跳转到施工单
        doc.on('click','.js-order-num',function(){
            var id = $('.order-id').val();
            window.location.href = BASE_PATH + '/workshop/workOrder/toWorkOrderDetail?id='+ id;
        })

        //返回
        doc.on('click','.js-return',function(){
            util.goBack();
        })
    });


});
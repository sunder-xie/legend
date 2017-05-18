$(function(){
   var doc = $(document);

    seajs.use([
        'dialog',
        'art',
        'ajax'
    ],function(dg,at,ax){

        //保存按钮
        doc.on('click','.js-save',function(){
            var Data={
                id:$('.workOrder-id').val(),
                lineType:2
            };
            var workOrderProcessRelDTOList=[];
            $('.accident-list').each(function() {
                var processManagerId = $(this).find('.processManagerId').val(),
                    processId = $(this).find('.processId').val(),
                    processName = $(this).find('.processName').text(),
                    workTime = $(this).find('.workTime').val(),
                    teamId = $(this).find('.teamId').val(),
                    teamName = $(this).find('.teamName').val(),
                    operatorId = $(this).find('.operatorId').val(),
                    operator = $(this).find('.operator').val(),
                    planStartTime = $(this).find('.planStartTime').text(),
                    planEndTime = $(this).find('.planEndTime').text(),
                    processSort = $(this).find('.processSort').val();

                workOrderProcessRelDTOList.push({
                    id:processManagerId,
                    processId: processId,
                    processName: processName,
                    workTime:workTime,
                    teamName:teamName,
                    teamId:teamId,
                    planStartTimeStr:planStartTime,
                    planEndTimeStr:planEndTime,
                    processSort:processSort
                });

                Data['workOrderProcessRelDTOList'] = workOrderProcessRelDTOList;
            });
            $.ajax({
                type: 'post',
                url: BASE_PATH + "/workshop/workOrder/addOrUpdateWorkOrder",
                data: JSON.stringify(Data),
                contentType: 'application/json',
                success: function (result) {
                    if (result.success) {
                        dg.success('保存成功',function(){
                            window.location.href= BASE_PATH + "/workshop/workOrder/toWorkOrderDetail?id="+result.data;
                        })
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });

        //返回按钮
        doc.on('click','.js-goback',function(){
            util.goBack();
        });


        //重新计算
        doc.on('blur','.js-calculation',function(){
            calculation();
        });

        function calculation(){
            var lineId = $('.lineId').val();
            var Data={
                lineId:lineId
            };

            var processWorkTimeVoList = [];
            var processManagerRelVoList = [];
            $('.accident-list').each(function() {
                var processId = $(this).find('.processId').val(),
                    workTime = $(this).find('.workTime').val(),
                    teamId = $(this).find('.teamId').val(),
                    teamName = $(this).find('.teamName').val(),
                    operatorId = $(this).find('.operatorId').val(),
                    operatorName = $(this).find('.operatorName').val(),
                    processName = $(this).find('.processName').text(),
                    processManagerId = $(this).find('.processManagerId').val(),
                    processSort = $(this).find('.processSort').val(),
                    shopId = $('.shopId').val();

                processWorkTimeVoList.push({
                    processId: processId,
                    workTime: workTime
                });
                processManagerRelVoList.push({
                    shopId:shopId,
                    id:processManagerId,
                    teamId:teamId,
                    teamName:teamName,
                    operatorId:operatorId,
                    operator:operatorName,
                    workTime:workTime,
                    processId: processId,
                    processName:processName,
                    processSort:processSort

                })

            });
            Data['processWorkTimeVoList'] = processWorkTimeVoList;
            Data['processManagerRelVoList'] = processManagerRelVoList;

            $.ajax({
                type:'post',
                contentType:'application/json',
                url: BASE_PATH + "/workshop/process/accident/calculate",
                data:JSON.stringify(Data),
                success: function (result) {
                    var html = at('tableTpl',{json:result});
                    $('#tableCon').html(html)
                }
            });

        };
    });

});
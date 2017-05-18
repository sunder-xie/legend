/*
 *  zmx 2016/07/04
 *  维修工单查询
 */

$(function(){
    var doc = $(document);
    seajs.use([
        'dialog',
        'table',
        'select',
        'art',
        'check'
    ],function(dg,tb,st,at,ck){

        //超出提示
        dg.titleInit();
        //初始化隐藏保存按钮
        $('.js-save－type').hide();

        ck.init();

        //tab表格填充
        function table(tableConTpl){
            var lineId = $('.current-item').data('lineId');
            $.ajax({
                url:BASE_PATH + '/workshop/loadplate/loadplate-line',
                data:{
                    lineId:lineId
                },
                success:function(result){
                    if(result.success){
                        var html = at('repair',{json:result});
                        $('#tableCon').html(html);
                    }
                }
            });
        }

        //tab切换
        doc.on('click','.tab-item',function(){
            var $this = $(this);
            $this.addClass('current-item').siblings().removeClass('current-item');
            var type = $('.current-item').data('type');
            table('repair');
            //按钮显示
            $('.js-work-btn').show();
            //隐藏自动派工
            $('.dispatching').hide();
            //隐藏按钮
            $('.js-save－type').hide();

            team();
        });

        //班组下拉列表
        var hidTeamId,hidTeamName,hidOperatorId,hidOperatorName;
        function team(){
            var shopId = $(".shopId").val();
            var lineId = $('.current-item').data('lineId');
            var type = $('.current-item').data('type');

            st.init({
                scope: '.jet-list',
                dom: '.js-team',
                url: BASE_PATH + '/workshop/workOrder/getTeamListByShopIdAndLineId?shopId='+shopId+'&lineId='+lineId,
                showKey: "id",
                showValue: "name",
                isClear:true,
                callback:function(showKey,showValue){
                    hidTeamId = showKey;
                    hidTeamName = showValue;
                    var $scope = $(this).parents('.jet-list');
                    var lineProcessId = $scope.find('.lineProcessId').val();
                    $('.other-team').val(hidTeamName);
                    $scope.find('.teamId-p').val(showKey);
                    $('.form-opereator').find('.yqx-select-options').remove();
                    if(type == 4){
                        calculation();
                    }
                    //技师下拉列表
                    st.init({
                        scope: $scope,
                        dom: '.js-operator',
                        url: BASE_PATH + '/workshop/workOrder/getShopManagerByTeamId?teamId='+hidTeamId+'&lineProcessId='+lineProcessId,
                        showKey: "managerId",
                        showValue: "managerName",
                        isClear:true,
                        callback:function(showKey,showValue){
                            hidOperatorId = showKey;
                            hidOperatorName = showValue;
                            $(this).parents('tr').find('.operatorId-p').val(showKey);
                            //重新计算
                            calculation();
                        }
                    });
                },
                noDataCallback:function(result){
                    dg.fail('请先进行排工')
                }
            });
        }

        //自动派工按钮
        var dispatTpl = '';
        doc.on('click','.js-dispat',function(){
            var type = $('.current-item').data('type');
            if(type == 1){
                //快修线
                $('.dispatching').show();
                $('.js-save－type').show();
                //快修线 自动派工模板
                dispat();

            }else if(type == 2){
                //事故线
                $('.dispatching').show();
                $('.js-save－type').show();
                $('.js-work-btn').show();
                //事故线 自动派工模板
                accident();
            }else if(type == 3){
                //快喷线
                $('.dispatching').show();
                $('.js-save－type').show();
                //快喷线 自动派工模板
                jet();
            }else if(type == 4){
                //事故线
                $('.dispatching').show();
                $('.js-save－type').show();
                $('.js-work-btn').show();
                //事故线 自动派工模板
                smallAccident();
            }
        });

        //撤销派工按钮
        doc.on('click','.js-revoke',function(){
            $('.dispatching').hide();
            $('.js-save－type').hide();
        });

        //获取tab
        tab();
        function tab(){
            var type = $('.current-item').data('type');
            $.ajax({
                url: BASE_PATH + "/workshop/workOrder/getProductLineByShopId",
                success: function (result) {
                    if (result.success) {
                        //模板填充
                        var html = at('tabTpl',{json:result});
                        $("#tabCon").html(html);
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        };


        //保存按钮
        doc.on('click','.js-save－type',function(){
            var type = $('.current-item').data('type');
            var lineId = $('.current-item').data('lineId');
            var lineName = $('.current-item').data('lineName');
            var teamVal,operatorVal;
            //获取详情数据
            var Data={
                shopId:$('.shopId').val(),
                orderId:$('.orderId').val(),
                orderSn:$('.orderSn').val(),
                customerCarId:$('.customerCarId').val(),
                carLicense:$('.carLicense').text(),
                carInfo:$('.carInfo').text(),
                carColor:$('.carColor').text(),
                expectTimeStr:$('.expectTime').text(),
                contactName:$('.contactName').text(),
                contactMobile:$('.contactMobile').text(),
                serviceSa:$('.serviceSa').text(),
                remark:$('.postscript').text(),
                lineId:lineId,
                lineName:lineName,
                lineType:type
            };

            //获取表格数据
            var workOrderProcessRelDTOList=[];
            if(type == 1){
                //快修线
                $('.repair-list').each(function() {
                    var shopId = $('.shopId').val(),
                        processId = $(this).find('.processId').val(),
                        processName = $(this).find('.processName').text(),
                        processSort = $(this).find('.processSort').val(),
                        workTime = $(this).find('.workTime').val(),
                        teamId = $(this).find('.teamId').val(),
                        teamName = $(this).find('.teamName').val(),
                        operatorId = $(this).find('.operatorId').val(),
                        operator = $(this).find('.operator').val(),
                        planStartTime = $(this).find('.planStartTime').text(),
                        planEndTime = $(this).find('.planEndTime').text();

                    workOrderProcessRelDTOList.push({
                        shopId:shopId,
                        processId: processId,
                        processName: processName,
                        processSort:processSort,
                        workTime:workTime,
                        teamName:teamName,
                        teamId:teamId,
                        operatorId:operatorId,
                        operator:operator,
                        planStartTimeStr:planStartTime,
                        planEndTimeStr:planEndTime
                    });
                });

            }else if(type == 2){
                //事故线
                $('.accident-list').each(function() {
                    var shopId = $('.shopId').val(),
                        processId = $(this).find('.processId').val(),
                        processName = $(this).find('.processName').text(),
                        processSort = $(this).find('.processSort').val(),
                        workTime = $(this).find('.workTime').val(),
                        teamId = $(this).find('.teamId').val();

                    //判断班组是否为空
                    teamVal = $(this).find('.js-team').val();
                    if(teamVal == ''){
                        dg.warn('请选择班组');
                        return false;
                    };

                    workOrderProcessRelDTOList.push({
                        shopId:shopId,
                        processId: processId,
                        processName: processName,
                        processSort:processSort,
                        workTime:workTime,
                        teamName:hidTeamName,
                        teamId:hidTeamId
                    });
                });
                if(teamVal == ''){
                    return;
                };
            }else if(type == 3){
                //快喷线
                $('.jet-list').each(function() {
                    var shopId = $('.shopId').val(),
                        processId = $(this).find('.processId').val(),
                        processName = $(this).find('.processName').text(),
                        processSort = $(this).find('.processSort').val(),
                        workTime = $(this).find('.workTime').val(),
                        teamId = $(this).find('.teamId-p').val(),
                        teamName = $(this).find('.teamName-p').val(),
                        operatorId = $(this).find('.operatorId-p').val(),
                        operator = $(this).find('.operatorName-p').val(),
                        planStartTime = $(this).find('.planStartTime').text(),
                        planEndTime = $(this).find('.planEndTime').text();

                    //判断班组是否为空
                    teamVal = $(this).find('.js-team').val();
                    operatorVal = $(this).find('.js-operator').val();
                    if(teamVal == ''||operatorVal == ''){
                        dg.warn('请完整填写派工信息');
                        return false;
                    };

                    workOrderProcessRelDTOList.push({
                        shopId:shopId,
                        processId: processId,
                        processName: processName,
                        processSort:processSort,
                        workTime:workTime,
                        teamName:teamName,
                        teamId:teamId,
                        operatorId:operatorId,
                        operator:operator,
                        planStartTimeStr:planStartTime,
                        planEndTimeStr:planEndTime
                    });
                });
                if(teamVal == '' || operatorVal == ''){
                    return;
                };

            } else if(type == 4){
                //小钣金事故线
                $('.small-accident-list').each(function() {
                    var shopId = $('.shopId').val(),
                        processId = $(this).find('.processId').val(),
                        processName = $(this).find('.processName').text(),
                        processSort = $(this).find('.processSort').val(),
                        workTime = $(this).find('.workTime').val(),
                        teamName = $(this).find('.teamName').val(),
                        planStartTime = $(this).find('.planStartTime').text(),
                        planEndTime = $(this).find('.planEndTime').text(),


                    //判断班组是否为空
                    teamVal = $(this).find('.js-team').val();
                    if(teamVal == ''){
                        dg.warn('请选择班组');
                        return false;
                    };

                    workOrderProcessRelDTOList.push({
                        shopId:shopId,
                        processId: processId,
                        processName: processName,
                        processSort:processSort,
                        workTime:workTime,
                        teamId:hidTeamId,
                        teamName:teamName,
                        planStartTimeStr:planStartTime,
                        planEndTimeStr:planEndTime
                    });
                });
            };
            Data['workOrderProcessRelDTOList'] = workOrderProcessRelDTOList;

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



        //快修线自动派工
        function dispat(){
            var lineId = $('.current-item').data('lineId');
            $.ajax({
                type:'get',
                url: BASE_PATH + "/workshop/workOrder/setAutoChooseParam",
                data:{
                    lineId:lineId
                },
                success: function (result) {
                    $.ajax({
                        type:'post',
                        contentType:'application/json',
                        url: BASE_PATH + "/workshop/process/autoChoose",
                        data:JSON.stringify(result.data),
                        success: function (result) {
                            if(result.success){
                                var html = at('dispatchingTpl',{json:result});
                                $('#dispatchingCon').html(html)
                            }else{
                                dg.fail(result.message);
                            }

                        }
                    });
                }
            });
        }


        //事故线自动派工
        function accident(){
            var lineId = $('.current-item').data('lineId');
            $.ajax({
                type:'get',
                contentType:'application/json',
                url: BASE_PATH + "/workshop/workOrder/getPLineProcessRelList",
                data:{
                    lineId:lineId
                },
                success: function (result) {
                    var html = at('accidentTpl',{json:result});
                    $('#dispatchingCon').html(html)

                    $('.processName').each(function(){
                       var processName = $.trim($(this).text());
                       if( processName =='钣金'){
                           $(this).parents('.accident-list').find('.workTime').attr('disabled',true);
                       }
                    });
                }
            });
        }
        //小钣金事故线自动派工
        function smallAccident(){
            var lineId = $('.current-item').data('lineId');
            $.ajax({
                type:'get',
                contentType:'application/json',
                url: BASE_PATH + "/workshop/workOrder/getPLineProcessRelList",
                data:{
                    lineId:lineId
                },
                success: function (result) {
                    var html = at('amallAccidentTpl',{json:result});
                    $('#dispatchingCon').html(html)

                    $('.processName').each(function(){
                        var processName = $.trim($(this).text());
                    });
                }
            });
        }


        //快喷线自动派工
        function jet(){
            var lineId = $('.current-item').data('lineId');
            $.ajax({
                type:'get',
                contentType:'application/json',
                url: BASE_PATH + "/workshop/workOrder/getPLineProcessRelList",
                data:{
                    lineId:lineId
                },
                success: function (result) {
                    var html = at('jetTpl',{json:result});
                    $('#dispatchingCon').html(html)
                }
            });
        }

        doc.on('blur','.js-workTime',function(){
            calculation();
        });

        doc.on('blur','.js-repair-workTiem',function(){
            calculation();
        });
        doc.on('blur','.js-work-tiem',function(){
            var teamId = $('.teamId').val();
            if( !(teamId == '')){
                calculation();
            }
        });


        //重新计算按钮
        doc.on('click','.js-calculation',function(){
            calculation();
        });

        //重新计算
        function calculation(){
            var operator;
            var lineId = $('.current-item').data('lineId');
            var type = $('.current-item').data('type');
            var Data={
                lineId:lineId
            };
            if( type == 1){
                //快修线数据
                var processWorkTimeVoList = [];
                var processManagerRelVoList = [];
                $('.repair-list').each(function() {
                    var processId = $(this).find('.processId').val(),
                        workTime = $(this).find('.workTime').val();


                    processWorkTimeVoList.push({
                        processId: processId,
                        workTime: workTime
                    });
                });
                Data['processWorkTimeVoList'] = processWorkTimeVoList;
                Data['processManagerRelVoList'] = processManagerRelVoList;

                $.ajax({
                    type:'post',
                    contentType:'application/json',
                    url: BASE_PATH + "/workshop/process/autoChoose",
                    data:JSON.stringify(Data),
                    success: function (result) {
                        var html = at('dispatchingTpl',{json:result});
                        $('#dispatchingCon').html(html)
                    }
                });
            }else if(type == 3){
                //快喷线数据
                var processWorkTimeVoList = [];
                var processManagerRelVoList = [];
                $('.jet-list').each(function() {
                    var processId = $(this).find('.processId').val(),
                        workTime = $(this).find('.workTime').val(),
                        processSort = $(this).find('.processSort').val(),
                        processName = $(this).find('.processName').text(),
                        teamId = $(this).find('.teamId-p').val(),
                        teamName = $(this).find('.teamName-p').val(),
                        operatorId = $(this).find('.operatorId-p').val(),
                        operatorName = $(this).find('.operatorName-p').val();

                    processWorkTimeVoList.push({
                        processId: processId,
                        workTime: workTime
                    });
                    processManagerRelVoList.push({
                        teamId:teamId,
                        teamName:teamName,
                        operatorId:operatorId,
                        operator:operatorName,
                        workTime:workTime,
                        processId: processId,
                        processSort:processSort,
                        processName:processName,
                        shopId:$(".shopId").val(),
                        lineId:lineId,
                        lineName:$('.current-item').data('lineName')
                    });



                    //判断技师是否为空
                    operator = $(this).find('.js-operator').val();
                    if(operator == ''){
                        return false;
                    }

                });
                if(operator == ''){
                    return;
                };
                Data['processWorkTimeVoList'] = processWorkTimeVoList;
                Data['processManagerRelVoList'] = processManagerRelVoList;

                $.ajax({
                    type:'post',
                    contentType:'application/json',
                    url: BASE_PATH + "/workshop/process/quick-paint/calculate",
                    data:JSON.stringify(Data),
                    success: function (result) {
                        var html = at('jetTpl',{json:result});
                        $('#dispatchingCon').html(html)
                    }
                });
            }else if(type == 4) {
                //小钣金事故线
                var lineId = $('.lineId').val();
                var Data={
                    lineId:lineId
                };

                var processWorkTimeVoList = [];
                var processManagerRelVoList = [];
                $('.small-accident-list').each(function() {
                    var processId = $(this).find('.processId').val(),
                        workTime = $(this).find('.workTime').val(),
                        teamName = $(this).find('.teamName').val(),
                        processName = $(this).find('.processName').text(),
                        processSort = $(this).find('.processSort').val(),
                        shopId = $('.shopId').val();

                    processWorkTimeVoList.push({
                        processId: processId,
                        workTime: workTime
                    });

                    processManagerRelVoList.push({
                        lineId:lineId,
                        shopId:shopId,
                        teamId:hidTeamId,
                        teamName:teamName,
                        workTime:workTime,
                        processId: processId,
                        processName:processName,
                        processSort:processSort
                    })
                });
                Data['processWorkTimeVoList'] = processWorkTimeVoList;
                Data['processManagerRelVoList'] = processManagerRelVoList;

                $.ajax({
                    type: 'post',
                    contentType: 'application/json',
                    url: BASE_PATH + "/workshop/process/accident/calculate",
                    data: JSON.stringify(Data),
                    success: function (result) {
                        var html = at('amallAccidentTpl', {json: result});
                        $('#dispatchingCon').html(html);
                        $('.js-team').val(hidTeamName);
                        $('.teamId').val(hidTeamId);
                    }
                });
            }
        }
    });


    //返回按钮
    doc.on('click','.js-goback',function(){
        util.goBack();
    });

});
/*
 * zmx 2016-07-04
 * 中断管理列表页面
 */

$(function(){
   var doc = $(document);

    seajs.use([
       'select',
       'table',
        'art',
        'dialog',
        'art'
    ],function(st,tb,at,dg,art){
        //中断总时长
        art.helper('Date', function (time) {
            return new Date(time);
        });

        //工序下拉列表
        st.init({
            dom: '.js-procedure',
            url: BASE_PATH + '/workshop/workOrder/getProcessNameList',
            showKey: "id",
            showValue: "name",
            isClear: true
        });
        //施工人员下拉列表
        st.init({
            dom: '.js-person',
            url: BASE_PATH + '/workshop/workOrder/getRealOperatorList',
            showKey: "id",
            showValue: "name",
            isClear: true,
        });

        //中断管理列表
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/workshop/workOrder/selectBreakWorkOrderProcess',
            //表格数据目标填充id，必需
            fillid: 'breakTable',
            //分页容器id，必需
            pageid: 'breakPaging',
            //表格模板id，必需
            tplid: 'breakTableTpl',
            //扩展参数,可选
            data: {},
            //关联查询表单id，可选
            formid: 'search',
            tplObject: {Date: Date},
            //渲染表格数据完后的回调方法,可选
            callback : null,
            totalElements: 'totalNum'
        });


        //恢复按钮
        doc.on('click','.js-recovery',function(){
            var listId = $(this).parents('tr').find('#listId').val();
            var workOrderId = $(this).parents('tr').find('#workOrderId').val();
            var processId = $(this).parents('tr').find('#processId').val();
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
            var workOrderId = $(this).parents('tr').find('#workOrderId').val();
            var processId = $(this).parents('tr').find('#processId').val();
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
        doc.on('click','.js-order-number',function(){
            var id = $(this).parents('tr').data('id');
            window.location.href = BASE_PATH + '/workshop/workOrder/toWorkOrderDetail?id='+ id;
        });

        //跳转到详情页面
        doc.on('click','.js-detail',function(){
            var id = $(this).parents('tr').data('id');
            window.location.href = BASE_PATH + '/workshop/workOrder/toWorkOrderBreakListDtl?id='+ id;
        });
    });


});
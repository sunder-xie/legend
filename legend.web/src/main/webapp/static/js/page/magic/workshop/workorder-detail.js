/**
 * Created by zz on 2016/7/4.
 */
$(function(){
    var doc = $(document);

    seajs.use([
        'art',
        'dialog'
    ],function(at,dg){
        dg.titleInit();

        //施工中断
        doc.on('click','.js-break',function(){
            var html = at('recoveryDialog',{});
            dg.open({
                area:['500px','310px'],
                content:html
            });
            var workOrderId = $('.work-id').val();
            $.ajax({
                url: BASE_PATH + '/workshop/workOrder/getWorkOrderProcessRel?workOrderId='+workOrderId,
                success:function(result){
                    if(result.success){
                        $('.break-process').text(result.data.processName);
                        $('.operator').text(result.data.realOperator);
                        $('input[name="workOrderId"]').val(result.data.workOrderId);
                        $('input[name="processId"]').val(result.data.processId);
                    }
                }
            });
        })

        var prepare = null;
        doc.on('click','.js-prepare',function(){
            var html = at('paintPrepare',{});
             prepare = dg.open({
            area:['500px','auto'],
                content:html
            });
            var key = localStorage.getItem(key);
            $('input[value="'+key+'"]').attr('checked','true').siblings().removeAttr('checked','false');
        });

        doc.on('click','.js-paint-cancel',function(){
            dg.close(prepare);
        });

        var replacement = null;
        doc.on('click','.js-replacementPrepare',function(){
            var html = at('replacementPrepare',{});
            replacement = dg.open({
                area:['500px','auto'],
                content:html
            });
            var key1 = localStorage.getItem(key1);
            $('input[value="'+key1+'"]').attr('checked','true').siblings().removeAttr('checked','false');
        });
        doc.on('click','.js-replace-cancel',function(){
            dg.close(replacement);
        });

        doc.on('click','.js-print',function(){
            var id = $('.work-id').val();
            util.print(BASE_PATH + '/workshop/workOrder/workOrderPrint?id='+id);
        });

        //中断原因下拉
        seajs.use('select', function(st){
            st.init({
                dom: '.js-reason',
                showKey: "key",
                showValue: "value",
                data: [{
                    key: '返工',
                    value: '返工'
                },{
                    key: '缺件',
                    value: '缺件'
                },{
                    key: '插队',
                    value: '插队'
                },{
                    key: '其他',
                    value: '其他'
                }],
                callback:function(a,key){
                    if(key != '其他'){
                        $('input[name="remark"]').val("").removeAttr('data-v-type');
                    }else{
                        $('input[name="remark"]').val("").attr('data-v-type','required');
                    }
                }
            });
        });

        //中断弹框中断按钮
        seajs.use('check', function(ck) {
            doc.on('click', '.dialog-break', function () {
                var workId = $('.work-id').val();
                if (!ck.check()) {
                    return false;
                }
                var workOrderId = $('input[name="workOrderId"]').val();
                var processId = $('input[name="processId"]').val();
                var breakReason = $('input[name="breakReason"]').val();
                var breakRemark = $('input[name="remark"]').val();
                $.ajax({
                    url: BASE_PATH + '/workshop/workOrder/interruptedOperator',
                    type: 'post',
                    data: {
                        workOrderId: workOrderId,
                        processId: processId,
                        breakReason: breakReason,
                        breakRemark:breakRemark
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success('中断成功');
                            window.location.href = BASE_PATH + "/workshop/workOrder/toWorkOrderDetail?id="+workId;
                            $('.js-break').addClass('grey').attr('disabled','true');
                        } else {
                            dg.fail(result.message);
                        }
                    }
                })
            });
        });

        seajs.use('dialog', function(dg) {
            dg.titleInit();
        });
    });

    //备件准备提交按钮
    seajs.use('dialog', function(dg) {
        doc.on('click', '.js-replace-submit', function () {
            var workId = $('.work-id').val();
            var checkRadio = $('input[name="status1"]:checked').val();

            $.ajax({
                url: BASE_PATH + '/workshop/workOrder/updatePaintPrepareOrAccessoriesPrepare',
                type: 'post',
                data: {
                    id: workId,
                    accessoriesPrepare: checkRadio
                },
                success: function (result) {
                    if (result.success) {
                        dg.success('操作成功', function () {
                            var key1;
                            localStorage.setItem(key1,checkRadio);
                            window.location.href = BASE_PATH + "/workshop/workOrder/toWorkOrderDetail?id="+workId;
                        });
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });
    });

    //面漆准备提交按钮
    seajs.use('dialog', function(dg) {
        doc.on('click', '.js-paint-submit', function () {
            var workId = $('.work-id').val();
            var checkRadio = $('input[name="status"]:checked').val();

            $.ajax({
                url: BASE_PATH + '/workshop/workOrder/updatePaintPrepareOrAccessoriesPrepare',
                type: 'post',
                data: {
                    id: workId,
                    paintPrepare: checkRadio
                },
                success: function (result) {
                    if (result.success) {
                        dg.success('操作成功', function () {
                            var key;
                            localStorage.setItem(key,checkRadio);
                            window.location.href = BASE_PATH + "/workshop/workOrder/toWorkOrderDetail?id="+workId;
                        });
                    } else {
                        dg.fail(result.message);
                    }
                }
            })
        });
    });


    //排工按钮
    seajs.use('dialog', function(dg) {
        doc.on('click', '.js-schedule', function () {
            var workId = $('.work-id').val();
            var status = $('.work-order-status').val();
            if (status != 'SGWC' && status != '1'){
                dg.warn("钣金未完工，不能排工！");
                return;
            }
            window.location.href = BASE_PATH + "/workshop/workOrder/toUpdateWorkOrder?id=" + workId;
        });
    })
});
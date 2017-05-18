/*
 * create by zmx 2016/11/14
 * 新建使用记录
 */
$(function(){
    var doc= $(document);

    seajs.use([
        'select',
        'check',
        'date',
        'formData',
        'dialog'
    ], function(st,ck,dp,fd,dg){
        //验证
        ck.init();

        // 未配置的日期
        dp.datePicker();

        //领料人
        st.init({
            dom: '.js-packing',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });

        //油漆等级
        st.init({
            dom: '.js-paint-level',
            showKey: "key",
            showValue: "value",
            data: [{
                key: '1',
                value: '油性漆'
            },{
                key: '2',
                value: '水性漆'
            }]
        });

        //油漆类型
        st.init({
            dom: '.js-paint-type',
            showKey: "key",
            showValue: "value",
            data: [{
                key: '1',
                value: '素色'
            },{
                key: '2',
                value: '银粉'
            },{
                key: '3',
                value: '珍珠'
            },{
                key: '4',
                value: '金属'
            },{
                key: '5',
                value: '弱色系'
            }]
        });

        //删除行
        doc.on('click','.js-delete',function(){
            $(this).parents('tr').remove();
        });

        //返回按钮
        doc.on('click','.js-return',function(){
            util.goBack();
        });

        //添加出库油漆
        doc.on('click','.js-add-paint',function(){
            var html = $('#tableTpl').html();
            $('#tableCon').append(html);
        });

        //提交按钮
        doc.on('click','.js-confirm',function() {
            if (!ck.check()) {
                return false;
            }
            //获取基本信息
            var paintUseRecordVo = fd.get('#formData');
            //备注
            paintUseRecordVo.recordRemark = $('textarea[name="recordRemark"]').val();
            //获取出库油漆列表数据
            var paintRecordDetailVoList = [];
            $('.table-list').each(function(){
                var listData = fd.get($(this),'#tableCon');
                paintRecordDetailVoList.push(listData);
            });
            paintUseRecordVo['paintRecordDetailVoList'] = paintRecordDetailVoList;
            $.ajax({
                type:'post',
                url:BASE_PATH + '/shop/paint/record/paintUseRecordAdd',
                data: JSON.stringify(paintUseRecordVo), 
                dataType: 'json', 
                contentType: "application/json",
                success:function(result){
                    if(result.success){
                        dg.success('提交成功');
                        window.location.href = BASE_PATH + '/shop/paint/record/toPaintUseRecord?id='+result.data;
                    }else{
                        dg.fail(result.errorMsg)
                    }
                }
            })
        })
    });
});
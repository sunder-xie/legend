/*
 * create by zmx 2016/11/15
 * 油漆使用记录
 */

$(function (){
    var doc = $(document);
    seajs.use([
        'table',
        'dialog',
        'date'
    ],function(tb,dg,dp){
        //溢出隐藏
        dg.titleInit();

        // 开始结束日期
        dp.datePicker();

        //表格模块初始化
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/paint/record/getPaintUseRecords',
            //表格数据目标填充id，必需
            fillid: 'tableCon',
            //分页容器id，必需
            pageid: 'paging',
            //表格模板id，必需
            tplid: 'tableTpl',
            fallback: true,
            //扩展参数,可选
            data: {size: 12},
            //关联查询表单id，可选
            formid: 'formData'
        });
        //查看
        doc.on('click','.js-view',function(){
            var listId = $(this).parents('tr').data('list-id');
            window.location.href = BASE_PATH + '/shop/paint/record/toPaintUseRecord?id='+ listId;
        });
        //新增使用记录按钮
        doc.on('click','.js-add-inventory',function(){
            window.location.href = BASE_PATH + '/shop/paint/record/toPaintUseRecordAdd';
        })
    });
});
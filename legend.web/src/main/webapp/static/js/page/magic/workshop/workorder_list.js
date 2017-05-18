/**
 * Created by zz on 2016/7/4.
 */
seajs.use('table',function(tb){
    tb.init({
        //表格数据url，必需
        url: BASE_PATH + '/workshop/workOrder/selectWorkOrderByPage',
        //表格数据目标填充id，必需
        fillid: 'tableTest',
        //分页容器id，必需
        pageid: 'pagingTest',
        //表格模板id，必需
        tplid: 'tableTestTpl',
        //扩展参数,可选
        data: {},
        //关联查询表单id，可选
        formid: 'search',
        //渲染表格数据完后的回调方法,可选
        callback : null
    });
});
seajs.use('date', function(dp) {
    // 开始结束日期
    dp.dpStartEnd({
        start:'startDate',
        end:'endDate'
    });
    dp.datePicker('#startDate1', {
        minDate: '%y-%M-%d'
    });
    dp.datePicker('#endDate1', {
        minDate: '%y-%M-%d'
    });
});

seajs.use('select', function(st){
    st.init({
        dom: '.js-status',
        showKey: "key",
        showValue: "value",
        data: [{
            value:'请选择'
        },{
            key: 'DSG',
            value: '待施工'
        },{
            key: 'YWG',
            value: '已完工'
        },{
            key: 'SGZ',
            value: '施工中'
        },{
            key: 'SGZD',
            value: '施工中断'
        }]
    });
});
seajs.use('select', function(st){
    st.init({
        dom: '.js-accessories',
        showKey: "key",
        showValue: "value",
        data: [{
          value:'请选择'
        },{
            key: 1,
            value: '准备就绪'
        },{
            key: 2,
            value: '部分就绪'
        },{
            key: 0,
            value: '尚未就绪'
        }]
    });
});
seajs.use('select', function(st){
    st.init({
        dom: '.js-paint',
        showKey: "key",
        showValue: "value",
        data: [{
            value:'请选择'
        },{
            key: 1,
            value: '准备就绪'
        },{
            key: 0,
            value: '尚未就绪'
        }]
    });
});

seajs.use('dialog', function(dg) {
    dg.titleInit();
});


//跳转到详情
$(document).on('click','.detail-page',function(){
    var contentId = $(this).data('contentId');
    window.location.href = BASE_PATH + '/workshop/workOrder/toWorkOrderDetail?id='+contentId
});

//导出
$(document).on('click','.export-excel',function(){
    window.location.href = BASE_PATH + '/workshop/workOrder/exportWorkOrderList';
});
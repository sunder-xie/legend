/**
 * 服务券列表页
 * Created by zmx on 16/9/18.
 */

$(function(){
    var $doc = $(document);

    seajs.use([
        'table',
        'dialog',
        'date'
    ],function(tb,dg,dp){
        dg.titleInit();
        //日历
        dp.dpStartEnd({
            start: 'start1',
            end: 'end1',
            startSettings: {
                dateFmt: 'yyyy-MM-dd HH:mm:ss',
                maxDate: '#F{$dp.$D(\'end1\')}'
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd HH:mm:ss',
                minDate: '#F{$dp.$D(\'start1\')}'
            }
        });

        //表格
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/insurance/anxin/settle/service-list/list',
            //表格数据目标填充id，必需
            fillid: 'orderListContent',
            //分页容器id，必需
            pageid: 'orderListPage',
            //表格模板id，必需
            tplid: 'orderListTpl',
            //扩展参数,可选
            data: {},
            //关联查询表单id，可选
            formid: 'searchForm',
            //渲染表格数据完后的回调方法,可选
            callback : null
        });
        $doc.on('click','.js-cancel-btn',function(){
            var orderId = $(this).parents('tr').data('id');
            window.location.href = BASE_PATH + '/insurance/anxin/settle/consume-service?id='+ orderId
        })
    });
});

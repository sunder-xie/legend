/**
 * 2016-04-10 zsy
 * 历史导入的工单列表逻辑
 */

$(function(){
    seajs.use([
        'table',
        'select',
        'date'
    ], function(tb, st, dp){

        var cardOrTable = localStorage.getItem('cardOrTable'),
            listUrl = BASE_PATH + '/shop/order/history/list',
            doc = $(document);

        function tableFill(url){
            //表格渲染
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'orderListTable',
                //分页容器id，必需
                pageid: 'orderListTablePage',
                //表格模板id，必需
                tplid: 'orderListTableTpl',
                //扩展参数,可选
                data: {size: 12},
                //默认第一次加载
                isfirstfill:false,
                //关联查询表单id，可选
                formid: 'orderListForm',
                //渲染表格数据完后的回调方法,可选
                callback : null
            });
        }

        //表格渲染
        tableFill(listUrl);
        //日历绑定
        dp.dpStartEnd({
            start: 'start1',
            end: 'end1',
            startSettings: {
                dateFmt: 'yyyy-MM-dd',
                maxDate: '#F{$dp.$D(\'end1\')||\'%y-%M-%d\'}'
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd',
                minDate: '#F{$dp.$D(\'start1\')}',
                maxDate: '%y-%M-%d'
            }
        });
    });
});
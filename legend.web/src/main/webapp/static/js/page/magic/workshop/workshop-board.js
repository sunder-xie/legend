/*
 *  zmx 2016/07/04
 *  生产进度看板
 */

$(function(){
    seajs.use([
        'table'
    ],function(tb){
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/settlement/list/list?page=1&search_orderStatuss=DDWC&search_payStatus=0',
            //表格数据目标填充id，必需
            fillid: 'tableCon',
            //分页容器id，必需
            pageid: '',
            //表格模板id，必需
            tplid: 'tableTpl',
            //扩展参数,可选
            data: {},
            //关联查询表单id，可选
            formid: null,
            //渲染表格数据完后的回调方法,可选
            callback : null
        });
    });
});

$(function () {
    seajs.use(['date','table','art'], function (dp,tb,at) {
        App.init();

        //初始化模板
        Smart.init_art(at);

        // 初始化搜索框
        init_search_data(dp);

        // 异步加载页面数据
        get_rest_table(tb);

        // 点击事件
        init_click(tb);

    })
});


function init_search_data(dp){
    //日历
    dp.dpStartEnd({
        start: 'search_settle_start_time',
        end: 'search_settle_end_time',
        startSettings: {
            dateFmt: 'yyyy-MM-dd HH:mm:ss',
            maxDate: '#F{$dp.$D(\'search_settle_end_time\')}'
        },
        endSettings: {
            dateFmt: 'yyyy-MM-dd HH:mm:ss',
            minDate: '#F{$dp.$D(\'search_settle_start_time\')}'
        }
    });

    //下拉框
    $("#choose_settle_project,#choose_cooperation").select2();
}


function get_rest_table(tb){
    //仅收入
    init_table(tb,"/legend/insurance/settle/console/getDetailData","package_table","pagingColumn","packageTableTpl","orderSearchForm");
}

function init_click(tb){
    $(document).on("click","search_button",function(){
        get_rest_table(tb);
    });
}





function init_table(tb,url,table_id,page_id,template_id,searchForm_id){
    tb.init({
        //表格数据url，必需
        url: url,
        //表格数据目标填充id，必需
        fillid: table_id,
        //分页容器id，必需
        pageid: page_id,
        //表格模板id，必需
        tplid: template_id,
        //如果模板需要自定义参数,可选
        tpldata: {},
        //扩展参数,可选
        data: {},
        //关联查询表单id，可选
        formid: searchForm_id,
        // 是否开启hash功能
        enabledHash: false,
        //渲染表格数据完后的回调方法,可选
        callback : null
    });
}
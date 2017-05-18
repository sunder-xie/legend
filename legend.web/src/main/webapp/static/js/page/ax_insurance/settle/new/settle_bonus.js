
$(function () {
    seajs.use(['date','table','art'], function (dp,tb,at) {
        App.init();

        //初始化模板
        Smart.init_art(at);

        // 初始化搜索框
        init_search_data(dp);

        // 异步加载页面数据
        get_settle_rest_table(tb);

        // 点击事件
        init_click(tb);

        //总额
        //SmartAjax.get({
        //    url:'/legend/insurance/settle/console/getBonusAmount',
        //    success:function(result){
        //        if(result !=null){
        //            $("#settle_sum_amount").text(result.data);
        //        }
        //    }
        //});
    })
});


function init_search_data(dp){
    //收入 日历
    init_data_start_end(dp,"search_settle_start_time","search_settle_end_time");
    //支出日期
    init_data_start_end(dp,"search_expend_start_time","search_expend_end_time");
    //收入日期
    init_data_start_end(dp,"search_return_start_time","search_return_end_time");

    //下拉框
    $("select").select2();
}




function init_click(tb){
    //顶部菜单栏的切换
    $(document).on("click",".tab_page_a",function(){
        if($(this).parent().hasClass("active")){
            // 自身点击
            return false;
        }
        // 菜单栏选中更改、
        $(".tab_page_a").parent().removeClass("active");
        $(this).parent().addClass("active");

        //页面展示
        var tab_pane_id = $(this).data("type");
        $(".tab_bonus").removeClass("active");
        $("#"+tab_pane_id).addClass("active");
        //数据展示，模拟按钮电锯
        $("#"+tab_pane_id+"_search_button").trigger("click");
    });

    //收入
    $(document).on("click","#settle_tab_search_button",function(){
        get_settle_rest_table(tb);
    });
    //支出
    $(document).on("click","#expend_tab_search_button",function(){
        init_table(tb,"/legend/insurance/settle/console/getBonusExtendReturnData","bonus_expend_table","expend_tab_pagingColumn","expendTableTpl","expendSearchForm");
    });
    //返还
    $(document).on("click","#return_tab_search_button",function(){
        init_table(tb,"/legend/insurance/settle/console/getBonusExtendReturnData","bonus_return_table","return_tab_pagingColumn","returnTableTpl","returnSearchForm");
    });
}

function get_settle_rest_table(tb){
    //仅收入
    init_table(tb,"/legend/insurance/settle/console/getDetailData","bonus_table","settle_tab_pagingColumn","settleTableTpl","orderSearchForm");
}


function init_data_start_end(dp,start_obj_id,end_obj_id){
    dp.dpStartEnd({
        start: start_obj_id,
        end: end_obj_id,
        startSettings: {
            dateFmt: 'yyyy-MM-dd HH:mm:ss',
            maxDate: '#F{$dp.$D(\''+end_obj_id+'\')}'
        },
        endSettings: {
            dateFmt: 'yyyy-MM-dd HH:mm:ss',
            minDate: '#F{$dp.$D(\''+start_obj_id+'\')}'
        }
    });
};


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
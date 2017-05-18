
$(function () {
    seajs.use([
        'table',
        'dialog',
        'art'
    ], function (tb, dg, at){
        // 初始化click
        init_click(tb,at);
        // 初始化分页数据
        init_data("recharge",tb,at);
    });
});

function init_click(tb,at){
    var doc_obj = $(document);
    doc_obj.on("click",".tab_page_a",function(){
        $(".tab_page_a").parent().removeClass("active");
        $(this).parent().addClass("active");

        init_data($(this).data("type"),tb,at);
    })
}

function init_data(type_name,tb,at){
    Smart.init_art(at);
    if(type_name == "recharge"){
        init_table(tb,"/legend/smart/bihu/usedView/getRechargeList","tableContent","pagingColumn","rechargeTableTemplate");
    }else{
        init_table(tb,"/legend/smart/bihu/usedView/consume/list","tableContent","pagingColumn","consumeTableTemplate");
    }
}

function init_table(tb,url,table_id,page_id,template_id){
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
        formid: null,
        // 是否开启hash功能
        enabledHash: false,
        //渲染表格数据完后的回调方法,可选
        callback : null
    });
}
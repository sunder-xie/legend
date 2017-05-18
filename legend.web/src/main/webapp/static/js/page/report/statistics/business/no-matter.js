$(function(){
    seajs.use(['art','dialog'],function(at,dg){
        var searchMonth = $('input[name="month"]').val();
        //非物料成本
        $.ajax({
            url:BASE_PATH + '/bp/cost/nonGoodCost',
            data:{
                searchMonth:searchMonth
            },
            success:function(result){
                if(result.success){
                    var html = at('tableTpl',{json:result});
                    $('#tableCon').html(html)
                }else{
                    dg.fail(result.message)
                }
            }
        });
    });
});
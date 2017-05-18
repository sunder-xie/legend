$(function(){
    seajs.use(['art','dialog'],function(at,dg){
        var searchMonth = $('input[name="month"]').val();
        //辅料成本
        $.ajax({
            url:BASE_PATH + '/bp/cost/accessiesCost',
            data:{
                searchMonth:searchMonth
            },
            success:function(result){
                if(result.success){
                    var html = at('accessiesCostTpl',{json:result});
                    $('#accessiesCostCon').html(html)
                }else{
                    dg.fail(result.message)
                }
            }
        });

        $(document).on('click','.js-search-btn',function(){
            var searchMonth = $('input[name="month"]').val();
            $.ajax({
                url:BASE_PATH + '/bp/cost/paintCost',
                data:{
                    searchMonth:searchMonth
                },
                success:function(result){
                    if(result.success){
                        var html = at('paintCostTpl',{json:result});
                        $('#paintCostCon').html(html)
                    }else{
                        dg.fail(result.message)
                    }
                }
            });
        });

        function paintTable(){
            //油漆成本
            $.ajax({
                url:BASE_PATH + '/bp/cost/paintCost',
                data:{
                    searchMonth:searchMonth
                },
                success:function(result){
                    if(result.success){
                        var html = at('paintCostTpl',{json:result});
                        $('#paintCostCon').html(html)
                    }else{
                        dg.fail(result.message)
                    }
                }
            });
        }


    });
});
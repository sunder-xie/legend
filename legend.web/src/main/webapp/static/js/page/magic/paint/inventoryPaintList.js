/*
 * create by zmx 2016/11/14
 * 油漆盘点列表页
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
        dp.dpStartEnd();
        //表格模块初始化
        getTable();
        doc.on('click','.list-tab',function(){
            var status = $(this).data('status');
            $('input[name="search_status"]').val(status);
            $('.list-tab.current-tab').removeClass('current-tab');
            $(this).addClass('current-tab');
            $('.js-search-btn').click();
        });
        $('.list-tab').eq(0).click();
        //表格模块初始化
        function getTable(){
            tb.init({
                //表格数据url，必需
                url: BASE_PATH + '/paint/inventory/list',
                //表格数据目标填充id，必需
                fillid: 'tableCon',
                //分页容器id，必需
                pageid: 'paging',
                //表格模板id，必需
                tplid: 'tableTpl',
                //关联查询表单id，可选
                formid: 'formData',
                //扩展参数,可选
                data: {
                    size: 12
                }
            });
        }

        doc.on('click','.js-delete',function(){
            var id =  $(this).parents('tr').data('id');
            dg.confirm('确定要删除吗？',function(){
                $.ajax({
                    type:'get',
                    url:BASE_PATH + '/paint/inventory/deletePaintInventory',
                    data:{
                        id:id
                    },
                    success:function(){
                        getTable();
                    }
                })
            })
        });


        doc.on('click','.js-add-new-inventory',function(){
            window.location.href = BASE_PATH + '/paint/inventory/toInventoryPaint'
        });

        doc.on('click','.js-edit',function(){
           var id =  $(this).parents('tr').data('id');
           window.location.href = BASE_PATH + '/paint/inventory/toUpdateInventoryPaint?id=' + id
        });

        doc.on('click','.js-look',function(){
            var id =  $(this).parents('tr').data('id');
            window.location.href = BASE_PATH + '/paint/inventory/toInventoryPaintDtl?id=' + id
        });

    });
});
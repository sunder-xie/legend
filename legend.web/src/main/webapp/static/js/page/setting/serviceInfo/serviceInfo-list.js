/*
 * create by zmx 2017/1/5
 * 服务资料
 */

$(function(){
   var doc = $(document);

    seajs.use(['table', 'select','dialog'],function(tb,st,dg){
        dg.titleInit();
        //服务类别下拉列表
        st.init({
            dom:".js-service-category",
            isClear: true
        });

        //车辆级别下拉列表
        st.init({
            dom:".js-car-level",
            isClear: true
        });

        //服务类型下拉列表
        st.init({
            dom:".js-service-type",
            isClear: true
        });

        //服务标签下拉列表
        st.init({
            dom:".js-service-flags",
            isClear: true
        });

        //表格
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/setting/serviceInfo/getServiceInfoList',
            //表格数据目标填充id，必需
            fillid: 'tableListCon',
            //分页容器id，必需
            pageid: 'pagingCon',
            //表格模板id，必需
            tplid: 'tableTpl',
            //关联查询表单id，可选
            formid: 'formData'
        });

        doc.on('click','.js-tr-edit',function(){
            var link = $(this).find('.js-edit').attr('href');
            window.location.href = link;
        });
        //删除
        doc.on('click','.js-delete',function(event){
            event.stopPropagation();
            var data = $(this).parents('tr').data('id');
            dg.confirm('确定要删除吗？',function(){
                $.ajax({
                    type:'post',
                    url:BASE_PATH + '/shop/setting/serviceInfo/deleteShopServiceInfo',
                    data: {
                        id:data
                    },
                    success:function(result){
                        if(result.success){
                            dg.success('服务删除成功');
                            window.location.reload()
                        }else{
                            dg.fail(result.message)
                        }
                    }
                })
            })

        })
    })
});
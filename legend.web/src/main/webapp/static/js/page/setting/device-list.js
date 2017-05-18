/*
 * create by zmx 2017/1/6
 * App授权设置
 */

$(function(){
    var doc = $(document);
    seajs.use([
        'table',
        'select',
        'dialog'
    ],function(tb,st,dg){
        dg.titleInit();
        //表格
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/security-login/device-search',
            //表格数据目标填充id，必需
            fillid: 'tableListCon',
            //分页容器id，必需
            pageid: 'pagingCon',
            //表格模板id，必需
            tplid: 'tableTpl',
            //关联查询表单id，可选
            formid: 'formData'
        });

        //状态下拉列表
        st.init({
            dom: '.js-state',
            url: BASE_PATH + '/shop/security-login/get-device-status',
            showKey: "code",
            showValue: "name",
            pleaseSelect: true
        });

        doc.on('click','.js-goBack',function(){
            window.location.href = BASE_PATH +'/shop/security-login/level-setting';
        });

        doc.on('click','.js-delete',function(){
            var type = $(this).data("type");
            var id = $(this).parents('tr').data('id');

            if(type == "delete"){
                dg.confirm('确定要解绑吗？',function(){
                    $.ajax({
                        type:"post",
                        url:BASE_PATH + '/shop/security-login/devices/'+ type +'/' + id,
                        success:function(result){
                            if(result.success){
                                dg.success(result.data);
                                window.location.reload();
                            }else{
                                dg.error(result.message);
                            }
                        }
                    })
                });
            }else{
                $.ajax({
                    type:"post",
                    url:BASE_PATH + '/shop/security-login/devices/'+ type +'/' + id,
                    success:function(result){
                        if(result.success){
                            dg.success(result.data);
                            window.location.reload();
                        }else{
                            dg.error(result.message);
                        }
                    }
                })
            }
        })
    })
});
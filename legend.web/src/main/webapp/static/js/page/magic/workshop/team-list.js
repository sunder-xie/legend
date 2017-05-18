/*
 *  zmx 2016/07/04
 *  班组管理
 */
$(function(){
    var doc = $(document);

    seajs.use([
        'table',
        'dialog',
        'art'
    ],function(tb,dg,at){

        dg.titleInit();

        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/workshop/team/list',
            //表格数据目标填充id，必需
            fillid: 'teamTable',
            //分页容器id，必需
            pageid: '',
            //表格模板id，必需
            tplid: 'teamTableTpl',
            //扩展参数,可选
            data: {},
            //关联查询表单id，可选
            formid: null,
            //渲染表格数据完后的回调方法,可选
            callback : null
        });
        var delDialog = null,teamId = null,shopId =null,parent = null;
        doc.on('click','.js-delete',function(){
            teamId = $(this).data('id');
            shopId = $(this).data('shopId');
            parent = $(this).parents('tr');
            var html = at('deleteDialog',{});
            var deleteDialog = dg.open({
                area:['500px','300px'],
                content:html
            });

        });

        doc.on('click','.js-list-delete',function(){
            $.ajax({
                type: 'get',
                url: BASE_PATH + "/workshop/team/del",
                data: {
                    teamId:teamId,
                    shopId:shopId
                },
                success: function (result) {
                    if (result.success) {
                        dg.success('删除成功',function(){
                            dg.close(delDialog);
                            parent.remove();
                        })
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });

        //取消弹窗
        doc.on('click','.js-chanel',function(){
            dg.close(delDialog);
        });

        //编辑按钮
        doc.on('click','.js-edit',function(){
            var teamId = $(this).data('id');
            window.location.href = BASE_PATH + "/workshop/team/editpage?teamId="+teamId;
        });

        //添加班组跳转
        doc.on('click','.js-addteam',function(){
            window.location.href = BASE_PATH + "/workshop/team/addpage"
        });
    });


});
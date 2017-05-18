/*
 *  weilu 2016/07/09
 *  面漆种类设置
 */
$(function(){
    var doc = $(document);

    //表格模块初始化
    seajs.use(['art','dialog','table', 'select', 'check'],function(at, dg, tb, st, ck){
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/workshop/paintspecies/list',
            //表格数据目标填充id，必需
            fillid: 'tablePaint',
            //分页容器id，必需
            pageid: 'pagingPaint',
            //表格模板id，必需
            tplid: 'tablePaintTpl',
            //扩展参数,可选
            data: {},
            //关联查询表单id，可选
            formid: null,
            //渲染表格数据完后的回调方法,可选
            callback : null
        });

        doc.on("click",".add",function(){
            var html = at('addDialog',{});
            dg.open({
                area:['500px','346px'],
                content:html
            });
            //下拉列表渲染
            st.init({
                dom: ".paint-level-select",
                url: BASE_PATH + '/workshop/paintlevel/selectlist',
                showKey: "code",
                pleaseSelect: true,
                showValue: "name"
            });
        }).on("click",".edit",function(){
            var eid = $(this).data('eid');
            $.ajax({
                type: 'POST',
                url: BASE_PATH + "/workshop/paintspecies/detail",
                data: {
                    id : eid
                },
                success: function (result) {
                    if (result.success) {
                        var html = at('editDialog',{"json": result.data});
                        dg.open({
                            area:['500px','346px'],
                            content:html
                        });
                        //下拉列表渲染
                        st.init({
                            dom: ".paint-level-select",
                            url: BASE_PATH + '/workshop/paintlevel/selectlist',
                            showKey: "code",
                            pleaseSelect: true,
                            showValue: "name"
                        });
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
            return false;
        }).on("click",".remove",function(){
            var eid = $(this).data('eid');
            $.ajax({
                type: 'POST',
                url: BASE_PATH + "/workshop/paintspecies/paintspecies-del",
                data: {
                    id : eid
                },
                success: function (result) {
                    if (result.success) {
                        dg.msg('删除成功!');
                        tb.init({
                            //表格数据url，必需
                            url: BASE_PATH + '/workshop/paintspecies/list',
                            //表格数据目标填充id，必需
                            fillid: 'tablePaint',
                            //分页容器id，必需
                            pageid: 'pagingPaint',
                            //表格模板id，必需
                            tplid: 'tablePaintTpl',
                            //扩展参数,可选
                            data: {},
                            //关联查询表单id，可选
                            formid: null,
                            //渲染表格数据完后的回调方法,可选
                            callback : null
                        });
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
            return false;
        }).on("click",".save",function(){
            ck.init();
            if(!ck.check()){
                return false;
            }
            var $t = $(this);
            var paintSpeciesVO = {
                paintLevelName: $("input[name='paintLevelName']").val(),
                price: $("input[name='price']").val(),
                name: $("input[name='sname']").val(),
                remark: $(".yqx-textarea").val()
            };
            if($t.data('eid')){
                paintSpeciesVO.id = $t.data('eid');
            }
            $.ajax({
                type: 'POST',
                url: BASE_PATH + "/workshop/paintspecies/edit",
                contentType : 'application/json',
                dataType : 'json',
                data: JSON.stringify(paintSpeciesVO),
                success: function (result) {
                    if (result.success) {
                        dg.close();
                        dg.msg('保存成功!');
                        tb.init({
                            //表格数据url，必需
                            url: BASE_PATH + '/workshop/paintspecies/list',
                            //表格数据目标填充id，必需
                            fillid: 'tablePaint',
                            //分页容器id，必需
                            pageid: 'pagingPaint',
                            //表格模板id，必需
                            tplid: 'tablePaintTpl',
                            //扩展参数,可选
                            data: {},
                            //关联查询表单id，可选
                            formid: null,
                            //渲染表格数据完后的回调方法,可选
                            callback : null
                        });
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
            return false;
        }).on("click",".js-cancel",function(){
            dg.close();
        });
    });

});
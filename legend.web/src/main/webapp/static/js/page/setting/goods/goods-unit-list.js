/*
 * create by zsy 2017/5/12
 * 配件单位
 */

$(function () {
    var doc = $(document);

    seajs.use(['table', 'dialog', 'formData', 'ajax'], function (tb, dg, fd, ax) {
        dg.titleInit();

        //表格
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/goods_unit/get-page',
            //表格数据目标填充id，必需
            fillid: 'tableListCon',
            //分页容器id，必需
            pageid: 'pagingCon',
            //表格模板id，必需
            tplid: 'tableTpl',
            //关联查询表单id，可选
            formid: 'formData'
        });

        //全选
        doc.on('click','.js-checkbox-all',function(){
            $('.js-checkbox').prop('checked',$('.js-checkbox-all').prop('checked'));
        });

        //删除
        doc.on('click', '.js-delete', function () {
            var id = $(this).data('id');
            var idList = [];
            idList.push(id);
            deleteUnit(dg,idList);
        });

        //批量删除
        doc.on('click', '.js-batch-delete', function () {
            var idList = [];
            $(".js-checkbox").each(function () {
                if ($(this).is(':checked')) {
                    var id = $(this).data('id');
                    idList.push(id);
                }
            });
            if (idList.length == 0) {
                dg.warn("请选择需要删除的单位");
                return false;
            }
            deleteUnit(dg, idList);
        });

        //全部删除
        doc.on('click', '.js-delete-all', function () {
            dg.confirm('确定要全部删除吗？', function () {
                $.ajax({
                    type: 'POST',
                    url: BASE_PATH + '/shop/goods_unit/delete',
                    success: function (result) {
                        if (result.success) {
                            dg.success("配件单位全部删除成功",function(){
                                window.location.reload();
                            });
                        } else {
                            dg.fail(result.message)
                        }
                    }
                });
            });
        });
    });

    function deleteUnit(dg, idList) {
        dg.confirm('确定要删除吗？', function () {
            $.ajax({
                type: 'POST',
                url: BASE_PATH + '/shop/goods_unit/deleteByIds',
                data: {
                    ids: idList
                },
                success: function (result) {
                    if (result.success) {
                        dg.success("配件单位删除成功",function(){
                            window.location.reload();
                        });
                    } else {
                        dg.fail(result.message)
                    }
                }
            });
        });
    }
});
/*
 * create by zmx 2017/1/5
 * 服务资料
 */

$(function () {
    var doc = $(document);
    var typeDialog = null;
    var keyWord = true;

    seajs.use(['table', 'dialog', 'check', 'formData'], function (tb, dg, ck, fd) {
        dg.titleInit();
        ck.init();

        //表格
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/goods_category/search',
            //表格数据目标填充id，必需
            fillid: 'tableListCon',
            //分页容器id，必需
            pageid: 'pagingCon',
            //表格模板id，必需
            tplid: 'tableTpl',
            //关联查询表单id，可选
            formid: 'formData'
        });
        //编辑
        doc.on('click', '.js-edit', function () {
            var thisInput = $(this).parents('tr').find('.form-item');
            var thisText = $(this).parents('tr').find('.form-label');
            thisInput.show();
            thisText.hide();
            thisInput.find('input[name="catName"]').val(thisText.text().trim());
            thisInput.find('input[name="catName"]').focus();
        });
        //保存类别名称（失焦事件）
        doc.on('blur','.js-type-name',function(){
            if(keyWord){
                saveTypeName($(this));
            }
        });
        //保存类别名称（回车事件）
        doc.on('keydown','.js-type-name',function(event){
            if(event.keyCode == 13){
                saveTypeName($(this));
                keyWord = false
            }else{
                keyWord = true
            }
        });
        //保存类别名称函数
        function saveTypeName($this) {
            //.检查
            if (!ck.check()) {
                return;
            }
            //.提交到后台
            var goodscategory = fd.get($this.parents('tr'));
            doSave(goodscategory, function () {
                //.样式切换
                var thisInput = $this.parents('tr').find('.form-item');
                var thisText = $this.parents('tr').find('.form-label');
                var name = $this.val();
                thisText.text(name);
                thisInput.hide();
                thisText.show();
            });
        }

        //删除
        doc.on('click', '.js-delete', function () {
            var id = $(this).parents('tr').data('id');
            dg.confirm('确定要删除吗？', function () {
                $.ajax({
                    type: 'post',
                    url: BASE_PATH + '/shop/goods_category/goods-category-delete',
                    data: {id: id},
                    success: function (result) {
                        if (result.success) {
                            dg.success(result.data);
                            window.location.reload()
                        } else {
                            dg.fail(result.message)
                        }
                    }
                });
            });
        });

        //弹窗
        doc.on('click', '.js-new-type', function () {
            var html = $('#newTypeDialog').html();
            typeDialog = dg.open({
                area: ['400px', '200px'],
                content: html
            })
        });

        doc.on('click', '.js-cancel', function () {
            dg.close(typeDialog)
        });

        doc.on('click', '.js-confirm', function () {
            if (!ck.check()) {
                return;
            }
            var goodscategory = fd.get('.js-goodscategory-form');
            doSave(goodscategory, function () {
                window.location.href = BASE_PATH + '/shop/goods_category/goods-category-list';
            });
        });

        function doSave(saveData, callback) {
            $.ajax({
                type: 'post',
                url: BASE_PATH + '/shop/goods_category/goods-category-save',
                data: JSON.stringify(saveData),
                dataType: 'json',
                contentType: "application/json",
                success: function (result) {
                    if (result.success) {
                        dg.success(result.data,callback);
                    } else {
                        dg.fail(result.message);
                        $('.js-form-item').hide();
                        $('.form-label').show();
                    }
                }
            });
        }

    })
});
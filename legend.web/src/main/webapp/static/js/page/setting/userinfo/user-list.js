/*
 * create by zmx 2017/1/5
 * 人员管理
 */

$(function () {
    seajs.use([
        'table',
        'select',
        'dialog'
    ], function (tb, st, dg) {
        dg.titleInit();
        //岗位下拉列表
        st.init({
            dom: ".js-roles",
            url: BASE_PATH + '/shop/setting/roles/get-roles-list',
            showKey: "id",
            showValue: "name",
            pleaseSelect: true,
            callback: function (showKey) {
                $('input[name="roleId"]').val(showKey);
            }
        });

        //表格
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/setting/user-info/user-list/get-list',
            //表格数据目标填充id，必需
            fillid: 'tableListCon',
            //分页容器id，必需
            pageid: 'pagingCon',
            //表格模板id，必需
            tplid: 'tableTpl',
            //关联查询表单id，可选
            formid: 'formData'
        });

        //删除按钮
        $(document).on('click', '.js-delete', function () {
            var $pare = $(this).parents('tr'),
                accountId = $pare.data('id'),
                rolesId = $pare.data('rolesId');

            Components.deleteStaff({
                accountId: accountId,
                rolesId: rolesId,
                success: function () {
                    $('.js-search-btn', '#formData').click();
                }
            });
            return false;
        });

        
        $(document)
            // 编辑按钮
            .on('click', '.js-edit', function () {
                var managerId = $(this).parents('tr').data('id');
                window.location.href = BASE_PATH + '/shop/setting/user-info/user-edit?managerId=' + managerId;
            })
            .on('click', '.js-edit-link', function () {
                var managerId = $(this).data('id');
                window.location.href = BASE_PATH + '/shop/setting/user-info/user-edit?managerId=' + managerId;
            })
            // 添加按钮
            .on('click', '.js-add-user', function () {
                window.location.href = BASE_PATH + '/shop/setting/user-info/user-add';
            });
    })
});
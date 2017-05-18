<#-- Created by sky on 2017/1/11. -->
<style data-ref-tpl="delete-staff-tpl">
    #userListDialog .dialog-content {
        text-align: center;
        line-height: 25px;
    }

    #userListDialog .dialog-content strong{
        font-weight: bold;
    }

    #userListDialog .dialog-btn{
        border-top: 1px solid #cacaca;
        text-align: center;
        padding: 10px;
    }
</style>

<script type="text/html" data-ref-tpl="delete-staff-tpl" id="userListDialogTpl">
    <div class="yqx-dialog" id="userListDialog">
        <div class="dialog-title">提示</div>
        <div class="dialog-content">
            该员工有 <strong><%= data %></strong> 个归属客户，是否进行调整后删除。
            <p><strong>直接删除后，归属客户将变为未分配客户</strong></p>
        </div>
        <div class="dialog-btn">
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-del-staff"
                    data-manager-id="<%= data.managerId %>"
                    data-role-id="<%= data.roleId %>">直接删除
            </button>
            <a href="${BASE_PATH}/marketing/gather/allot/allot-list" class="yqx-btn yqx-btn-3 yqx-btn-small">调整</a>
        </div>
    </div>
</script>

<script data-ref-tpl="delete-staff-tpl">
    $(function () {
        seajs.use(['art', 'dialog', 'ajax'], function (tpl, dg) {
            var dialogId,
                args,
                sd = {
                    managerId: null,
                    rolesId: null
                },
                model = {
                checkBalance: function (accountId) {
                    return $.ajax({
                        url: BASE_PATH + '/shop/roles_func/check_balance',
                        data: {
                            managerId: accountId
                        }
                    });
                },
                checkUserAllot: function (managerId) {
                    return $.ajax({
                        url: BASE_PATH + '/shop/roles_func/check-user-allot',
                        data: {
                            managerId: managerId
                        }
                    });
                },
                deleteStaff: function (managerId, rolesId) {
                    var data = {
                        managerId: managerId,
                        roleId: rolesId
                    };

                    return $.ajax({
                        type: 'POST',
                        url: BASE_PATH + '/shop/setting/user-info/delete',
                        contentType: 'application/json',
                        data: JSON.stringify(data)
                    });
                }
            };

            /**
             * 员工删除
             * */
            function deleteStaff() {
                model.deleteStaff(sd.managerId, sd.rolesId)
                        .done(function (result) {
                            if (result.success) {
                                dg.success('操作成功！', function () {
                                    args.success && args.success(result.data);
                                });
                            } else {
                                dg.fail(result.message);
                            }

                            sd.managerId = sd.roles = null;
                        });
            }

            /**
             * 查询员工是否有分配的客户
             */
            function checkUserAllot() {
                model.checkUserAllot(sd.managerId)
                        .done(function (result) {
                            if (result.success) {

                                if (result.data > 0) {
                                    dialogId = dg.open({
                                        area: ['320px', 'auto'],
                                        content: tpl('userListDialogTpl', result)
                                    })
                                } else {
                                    deleteStaff();
                                }
                            }
                        });
            }

            /**
             * export 检查是否有返利
             * @param accountId
             * @param rolesId
             */
            function checkBalance (accountId, rolesId) {
                sd.managerId = accountId;
                sd.rolesId = rolesId;
                model.checkBalance(accountId)
                        .done(function (result) {
                            if (result.success) {
                                dg.confirm('确定要删除吗？', function () {
                                    //判断是否有客户绑定信息
                                    checkUserAllot(sd.managerId, sd.rolesId);
                                });
                            } else if (result.errorMsg) {
                                // 有返利的情况
                                dg.confirm(result.errorMsg + ' 确认要删除吗？', function () {
                                    //判断是否有客户绑定信息
                                    checkUserAllot(sd.managerId, sd.rolesId);
                                });
                            } else {
                                dg.fail(result.errorMsg || '删除出错，请稍后再试。')
                            }
                        });
            }

            $(document).on('click', '#userListDialog .js-del-staff', function () {
                if (sd.managerId != null && sd.rolesId != null) {
                    deleteStaff();
                    dg.close(dialogId);
                }
            });

            /*** checkBalance => checkUserAllot => deleteStaff ***/
            Components.deleteStaff = function (options) {
                args = $.extend({}, {
                    accountId: '',
                    rolesId: '',
                    success: null
                }, options);
                checkBalance(args.accountId, args.rolesId);
            };
        });
    })
</script>


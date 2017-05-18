/*
 * create by zmx 2017/1/5
 * 岗位管理
 */

$(function () {
    seajs.use(['art', 'dialog', 'treeView', 'formData', 'ajax'], function (tpl, dg, tv, fd) {
        var doc = $(document),
            model = {
                /**
                 * 获取角色列表
                 * @returns {*}
                 */
                getRoles: function () {
                    return $.ajax({
                        url: BASE_PATH + '/shop/setting/roles/get-roles'
                    });
                },
                /**
                 * 更新岗位权限列表
                 * @param rolesId
                 * @param funcIdsStr
                 * @param parentId
                 * @returns {*}
                 */
                updateRolesFunc: function (rolesId, funcIdsStr, parentId) {
                    var data = {
                        rolesId: rolesId,
                        funcIdsStr: funcIdsStr,
                        parentId: parentId
                    };

                    return $.ajax({
                        url: BASE_PATH + '/shop/setting/roles/update-roles-func',
                        data: JSON.stringify(data),
                        type: 'post',
                        contentType: 'application/json'
                    });
                },
                /**
                 * 根据rolesId获取右侧岗位人员信息
                 * @param rolesId
                 * @returns {*}
                 */
                getRolesUser: function (rolesId) {
                    return $.ajax({
                        url: BASE_PATH + '/shop/setting/user-info/user-list/get-roles-user',
                        global: false,
                        data: {
                            rolesId: rolesId
                        },
                        dataType: 'json'
                    });
                },
                /**
                 * 根据rolesId，parentId获取岗位权限信息
                 * @param rolesId
                 * @param parentId
                 * @returns {*}
                 */
                getAllFunc: function (rolesId, parentId) {
                    var data = {
                        rolesId: rolesId,
                        parentId: parentId
                    };

                    return $.ajax({
                        url: BASE_PATH + '/shop/setting/roles/get-all-func',
                        global: false,
                        data: data,
                        dataType: 'json'
                    });
                },
                /**
                 * 添加岗位
                 * @param name
                 * @param parentId
                 * @param levelId
                 * @param pvgRoleId
                 * @returns {*}
                 */
                additionRole: function (name, parentId, levelId, pvgRoleId) {
                    var data = {
                        name: name,
                        parentId: parentId,
                        levelId: levelId,
                        pvgRoleId: pvgRoleId
                    };

                    return $.ajax({
                        url: BASE_PATH + '/shop/setting/roles/add-roles',
                        data: JSON.stringify(data),
                        type: 'post',
                        contentType: 'application/json'
                    });
                },
                /**
                 * 编辑岗位
                 * @param rolesId
                 * @param rolesName
                 * @returns {*}
                 */
                editRole: function (rolesId, rolesName) {
                    var data = {
                        rolesId: rolesId,
                        rolesName: rolesName
                    };

                    return $.ajax({
                        url: BASE_PATH + '/shop/setting/roles/edit-roles',
                        data: data,
                        type: 'post'
                    });
                },
                /**
                 * 删除岗位
                 * @param rolesId
                 * @returns {*}
                 */
                deleteRole: function (rolesId) {
                    return $.ajax({
                        url: BASE_PATH + '/shop/setting/roles/del-roles',
                        data: {
                            rolesId: rolesId
                        },
                        type: 'post'
                    });
                }
            },
            globalData = {
                rolesId: '',
                parentId: '',
                pvgRoleId: ''
            },
            ajaxObject = {},
            currRolesStaffNum = null,
            addRolesDialogId,
            tvL,
            $allFunc = $('#allFunc'),
        // 记录触发状态变化的元素级别
            targetChange,
            editObj,
            ann;
        
        dg.titleInit();

        tpl.helper('treeView', function (data, template, idx) {
            var $$li = document.createElement('li');
            $$li.id = 'allFunc' + idx;
            tv.init({
                wrap: $$li,
                template: template,
                data: data,
                showIcon: false
            }).buildTree();
            return $$li;
        });

        /**
         * 更新角色列表
         */
        function initRoleList() {
            model.getRoles()
                .done(function (result) {
                    if (result.success) {
                        tvL = tv.init({
                            wrap: '#treeViewWrap',
                            data: result.data,
                            template: '#rolesTpl',
                            level: 5,
                            showLevel: 5,
                            afterCreateBranchCallback: function (level, el) {
                                level === 1 && $(el).removeClass('yunui-tv-icon');
                            },
                            selectedCallback: function (e, level, el) {
                                var $treeView = $('#treeViewWrap'),
                                    $treeItem = $(el).find('.js-treeview-item');

                                globalData.rolesId = $treeItem.data('id');
                                globalData.parentId = $treeItem.data('parentId');
                                globalData.pvgRoleId = $treeItem.data('pvgRoleId');

                                if (e.currentTarget !== e.target) {
                                    if (!$treeItem.hasClass('current')) {
                                        // 下拉菜单状态切换
                                        $treeView.find('.js-treeview-item.current').removeClass('current');
                                        $treeItem.addClass('current');

                                        // 重新请求前先把原来的请求关闭
                                        ajaxObject.getRolesUser && ajaxObject.getRolesUser.abort();
                                        ajaxObject.getAllFunc && ajaxObject.getAllFunc.abort();
                                        // 请求 员工信息 和 岗位权限
                                        $.when(
                                            (ajaxObject.getRolesUser = model.getRolesUser(globalData.rolesId)),
                                            (ajaxObject.getAllFunc = model.getAllFunc(globalData.rolesId, globalData.parentId))
                                        ).done(function (response1, response2) {
                                            var result1 = response1[0],
                                                result2 = response2[0],
                                                html;

                                            // 渲染员工信息模块
                                            if (result1.success) {
                                                html = tpl('rolesUserInfoTpl', result1);
                                                $('#rolesUserInfo').html(html);
                                                currRolesStaffNum = result1.data instanceof Array ? result1.data.length : 0;
                                            } else {
                                                currRolesStaffNum = null;
                                                dg.fail(result1.message);
                                            }

                                            // 渲染岗位权限模块
                                            if (result2.success) {
                                                html = tpl('allFuncTpl', result2);
                                                $('#allFunc').html(html);
                                                initAllFunc();
                                            } else {
                                                dg.fail(result2.message);
                                            }
                                        });
                                    }

                                    //// 阻止TreeView视图更新 ////
                                    // 阻止树枝状态切换
                                    // 阻止图标更新
                                    return false;
                                }
                            },
                            finishedCallback: function (tree) {
                                tree && $(tree).find('.js-treeview-level-1').eq(0).click();
                            }
                        }).buildTree();
                    } else {
                        dg.fail(result.message);
                    }
                });
        }

        // 初始化岗位权限
        function initAllFunc() {
            var allSelected = true;
            $('.js-select-item, .js-select-sub-item', $allFunc).each(function () {
                var checked = $(this).prop('checked');
                if (checked === false) {
                    allSelected = false;
                    return false;
                }
            });

            $('.js-select-all', $allFunc).prop('checked', allSelected);
        }

        ////^ 设置 ////
        // 关闭设置下拉列表
        function settingDropdownClose() {
            $('.js-dropdown:visible').hide();
        }

        // 添加下级
        function addNode($$item) {
            return function (name, parentId, level, pvgRoleId) {
                model.additionRole(name, parentId, level, pvgRoleId)
                    .done(function (result) {
                        dg.close(addRolesDialogId);
                        if (result.success) {
                            tvL.addNode($$item, result.data);
                        } else {
                            dg.fail(result.message);
                        }
                    });
                ann = null;
            }
        }

        function addNodeDialog($$item, parentId, level, pvgRoleId, title) {
            ann = addNode($$item);

            dg.dialog({
                area: ['405px', '200px'],
                content: tpl('addRolesDialogTpl', {
                    title: title,
                    level: level,
                    parentId: parentId,
                    pvgRoleId: pvgRoleId
                })
            });
        }

        editObj = {
            createInput: function (value) {
                var input = document.createElement('input');
                input.className = 'yqx-input treeview-edit-control';
                input.value = value;
                return input;
            },
            deleteInput: function ($$input) {
                $$input.onblur = null;
                $$input.onkeydown = null;
                $$input.onkeyup = null;
                $$input.parentNode.removeChild($$input);
                $$input = null;
            },
            bindEvent: function ($$input) {
                var keyDownValue;
                $$input.onblur = function () {
                    editObj.deleteInput($$input);
                };

                $$input.onkeydown = function (e) {
                    if (e.keyCode === 13) {
                        keyDownValue = this.value;
                    }
                };

                $$input.onkeyup = function (e) {
                    var $$this = this;

                    editObj.trimBlank.call($$this);
                    if (e.keyCode === 13 && keyDownValue === $$this.value) {
                        editObj.saveFn.call($$this);
                    }
                };

                $$input.onblur = function () {
                    var $$this = this;

                    editObj.trimBlank.call($$this);
                    editObj.saveFn.call($$this);
                };
            },
            trimBlank: function () {
                // 去除所有空白
                var blankReg = /\s+/gi,
                    value = this.value;
                this.value = value.replace(blankReg, '');
            },
            // bindEvent js-dropdown-edit
            editFn: function ($$scope) {
                // 创建编辑输入框
                var name = $$scope.querySelector('.js-treeview-content').innerText,
                    $$input = editObj.createInput(name);
                if ($$scope) {
                    $$scope.appendChild($$input);
                    editObj.bindEvent($$input);
                    // 内容全选中
                    $$input.select();
                }
            },
            saveFn: function () {
                // 保存所有内容
                var $$this = this,
                    $$content = $$this.parentNode.querySelector('.js-treeview-content'),
                    name,
                    preName = $$content.innerText;
                check: {
                    name = $$this.value;
                    if (name === '' || name === preName) {
                        editObj.deleteInput($$this);
                        break check;
                    }

                    if (name.length > 15) {
                        dg.warn('岗位名称长度不能超过15字！');
                        break check;
                    }
                    model.editRole(globalData.rolesId, name)
                        .done(function (result) {
                            if (result.success) {
                                $$content.innerText = name;

                                editObj.deleteInput($$this);
                            } else {
                                dg.fail(result.message);
                            }
                        });
                }
                return false;
            }
        };

        doc
        // 关闭设置下拉菜单
            .on('click', function () {
                settingDropdownClose();
            })
            // 打开设置下拉菜单
            .on('click', '.js-treeview-setting', function () {
                var $this = $(this),
                    dropdown = $this.find('.js-dropdown');
                if (dropdown.length) {
                    dropdown.show();
                } else {
                    // 创建dropdown
                    dropdown = $('<ul class="dropdown js-dropdown"></ul>')
                        .append(
                            '<li class="js-dd-edit">修改</li>' +
                            '<li class="js-dd-del">删除</li>'
                        );

                    dropdown.prepend(
                        '<li class="js-dd-add-flat">添加平级</li>' +
                        '<li class="js-dd-add-lower">添加下级</li>'
                    );
                    $this.append(dropdown);
                }
                return false;
            })
            // 选中设置--添加平级
            .on('click', '.js-dd-add-flat', function () {
                var $this = $(this),
                    $item = $this.closest('.js-treeview-item'),
                    level = $item.data('levelId'),
                    parentId = $item.data('parentId'),
                    pvgRoleId = $item.data('pvgRoleId'),
                    $$scope = $item.closest('ul').siblings('.yunui-tv-item')[0];

                addNodeDialog($$scope, parentId, level, pvgRoleId, '添加平级');
                settingDropdownClose();
                return false;
            })
            // 选中设置--添加下级
            .on('click', '.js-dd-add-lower', function () {
                var $this = $(this),
                    $item = $this.closest('.js-treeview-item'),
                    level = $item.data('levelId'),
                    // 当前层级的id就是下级层级的parentId
                    id = $item.data('id'),
                    pvgRoleId = $item.data('pvgRoleId');

                addNodeDialog($item[0], id, ++level, pvgRoleId, '添加下级');
                settingDropdownClose();
                return false;
            })
            // 选中设置--编辑
            .on('click', '.js-dd-edit', function () {
                var $scope = $(this).closest('.js-treeview-item');
                editObj.editFn($scope[0]);
                settingDropdownClose();
                return false;
            })
            // 选中设置--删除
            .on('click', '.js-dd-del', function () {
                var $$this = this;
                if (currRolesStaffNum !== null) {
                    if (currRolesStaffNum === 0) {
                        dg.confirm('确认删除？', function () {
                            model.deleteRole(globalData.rolesId)
                                .done(function (result) {
                                    if (result.success && result.data) {
                                        dg.success('删除成功！');
                                        tvL.removeNode($$this);
                                        $('#rolesUserInfo, #allFunc').empty();
                                        $('.js-treeview-level-1').eq(0).click();
                                    } else {
                                        dg.fail(result.message || '删除失败！');
                                    }
                                    settingDropdownClose();
                                })
                        });
                    } else {
                        dg.warn('此岗位有员工，请先删除员工！');
                        settingDropdownClose();
                    }
                }
                return false;
            });

        doc
            .on('click', '.js-add-roles-ok', function () {
                var $$this = this,
                    data = fd.get('#addDialog');

                ann(data.name, data.parentId, data.level, data.pvgRoleId);
            })
            .on('click', '.js-add-roles-cancel', function () {
                dg.close(addRolesDialogId);
            });
        ////$ 设置 ////

        ////^ 员工信息 ////
        doc
        // 员工信息--换岗
            .on('click', '.js-change', function () {
                location.href = BASE_PATH + '/shop/setting/user-info/user-edit?managerId=' + $(this).data('managerId');
            })
            // 员工信息--删除
            .on('click', '.js-del', function () {
                var $this = $(this),
                    accountId = $this.data('id'),
                    rolesId = $this.data('rolesId');

                Components.deleteStaff({
                    accountId: accountId,
                    rolesId: rolesId,
                    success: function () {
                        $this.parents('tr').remove();
                    }
                });
                return false;
            })
            .on('click', '.js-add-user', function () {
                //添加员工
                Components.addUserDialog({
                    data: {
                        userRoleId: globalData.rolesId,
                        pvgRoleId: globalData.pvgRoleId
                    },
                    success: function (data) {
                        var html = tpl('rolesUserInfoTpl', {data: data});
                        $('#rolesUserInfo').html(html);
                    }
                });
            });
        ////$ 员工信息 ////

        ////^ 岗位权限 ////



        //= 选中状态 start =//
        // 利用了change的事件冒泡
        $allFunc.on('change', '.js-select-all', function () {
            var checked,
                $children,
                tc = targetChange;

            // 设置当前状态，在子级的change事件中有用
            targetChange = 0;

            // 如果不是子级“冒泡”上来的，则触发
            if (tc !== 1 && tc !== 2) {
                checked = $(this).prop('checked');
                $children = $('.js-select-item', '#allFunc');

                $children.prop('checked', checked);
                $children.change();
            }

            // 初始化状态
            targetChange = null;
        });

        $allFunc.on('change', '.js-select-item', function () {
            var $this = $(this),
                $parent,
                checked = $this.prop('checked'),
                allSelected = true,
                tc = targetChange;

            targetChange = 1;

            if (tc !== 2) {
                // 遍历子级，选择或者取消选择所有子级的勾选
                $this.closest('li').find('.js-select-sub-item').prop('checked', checked);
            }

            if (tc !== 0) {
                // 遍历所有同级，判断同级是否全部选择
                $this.closest('.permission').find('.js-select-item').each(function () {
                    if ($(this).prop('checked') === false) {
                        allSelected = false;
                        return false;
                    }
                });

                // 更新父级，将最终结果反映给父级
                $parent = $('.js-select-all', $allFunc);
                $parent.prop('checked', allSelected);
                $parent.change();
            }

        });

        $allFunc.on('change', '.js-select-sub-item', function () {
            var $this = $(this),
                $parent,
                allSelected = true,
                tc = targetChange;

            targetChange = 2;

            if (tc !== 1) {
                $this.closest('.permission').find('.js-select-sub-item').each(function () {
                    if ($(this).prop('checked') === false) {
                        allSelected = false;
                        return false;
                    }
                });

                // 修改父级状态
                $parent = $this.closest('.permission').parent().find('.js-select-item');
                $parent.prop('checked', allSelected);
                $parent.change();
            }
        });
        //= 选中状态 red =//

        $allFunc.on('click', '.js-list-control', function () {
            var $this = $(this),
                $icon = $this.find('.js-icon-change');

            if ($this.hasClass('open')) {
                $this.removeClass('open');
                $icon.removeClass('icon-angle-up').addClass('icon-angle-down');
                $this.parent().siblings('.permission').hide();
            } else {
                $this.addClass('open');
                $icon.removeClass('icon-angle-down').addClass('icon-angle-up');
                $this.parent().siblings('.permission').show();
            }
        });

        doc.on('click', '.js-save-perm', function () {
            var funcIds = [];

            $('.js-select-item, .js-select-sub-item').each(function () {
                if ($(this).prop('checked') === true) {
                    funcIds.push(this.value);
                }
            });



            model.updateRolesFunc(globalData.rolesId, funcIds.join(','), globalData.parentId)
                .done(function (result) {
                    if (result.success) {
                        dg.success('保存成功！');
                    } else {
                        dg.fail(result.errorMsg);
                    }
                });
        });
        ////$ 岗位权限 ////

        ////^ 初始化 ////
        (function init() {
            // 设置角色列表
            initRoleList();
        })();
        ////$ 初始化 ////

    });
});
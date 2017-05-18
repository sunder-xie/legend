/**
 * Create by zsy 2016/12/16
 * 客户归属列表页
 **/
$(function () {
    seajs.use([
        'table',
        'dialog',
        'select',
        'art',
        'ajax'
    ], function (tb, dialog, st, art) {
        var _selectedStaff = {},
            _rebindData = {
                oldUserId: null,
                newUserId: null,
                customerCarIds: null
            };

        var _dialogId;

        dialog.titleInit();
        getStaff();

        art.helper('isSelect', isSelect);
        //下拉列表
        st.init({
            dom: '.js-select-staff',
            url: BASE_PATH + '/marketing/gather/api/get-manager',
            showKey: "userId",
            // 过滤未分配的员工
            params: {
                isAllot: true
            },
            selectedKey: +$('input[name="search_userIds"]').val(),
            canInput: true,
            pleaseSelect: true,
            showValue: "userName"
        });

        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/marketing/gather/allot/customer-allot-list/get-list',
            //表格数据目标填充id，必需
            fillid: 'tableCon',
            //分页容器id，必需
            pageid: 'paging',
            //表格模板id，必需
            tplid: 'tableTpl',
            //关联查询表单id，可选
            formid: 'formData'
        });

        $('.js-search-btn').click(function () {
            _rebindData = {};
            _selectedStaff = {};
        });

        $(document)
            .on('click', '.js-t-re-bind', function () {
                _rebindData = {
                    customerCarIds: [$(this).data('id')],
                    oldUserIds: [$(this).data('userId')]
                };

                _dialogId = dialog.open({
                    content: $('.choose-staff-dialog').removeClass('hide')
                });
            })
            .on('click', '.js-t-off-bind', function () {
                var customerCarIds = [];
                customerCarIds.push($(this).data('id'));
                var data = {
                    customerCarIds: customerCarIds.join(',')
                };

                dialog.confirm('确定将<strong>' + $(this).parents('tr').find('.name').text() + '</strong>的归属客户<strong>'
                        + $(this).parents('tr').find('.license').text()
                    + '</strong>解绑吗？', function () {

                    offBind(data, function () {
                        location.reload();
                    });
                });
            });

        $('.js-re-bind').on('click', function () {
            _rebindData = getBindData(_selectedStaff);

            _dialogId = dialog.open({
                content: $('.choose-staff-dialog').removeClass('hide'),
                end: function () {
                    $('.choose-staff-dialog').find('.staff-list li')
                        .removeClass('hide')
                        .end().find('.js-staff-name').val('')
                }
            });
        });

        $('.js-off-bind').on('click', function () {
            var data = getBindData(_selectedStaff);

            dialog.confirm('确定将这<strong>' + data.customerCarIds.length + '</strong>位客户全部解绑吗？',
                function () {
                var offBindData = {
                    customerCarIds:data.customerCarIds.join(',')
                }
                offBind(offBindData, function () {
                    location.reload();
                });
            });
        });

        $('.choose-staff-dialog')
            .on('click', '.js-cancel', function () {
                dialog.close(_dialogId);
            })
            .on('input', '.js-staff-name', function () {
                var name = $.trim( $(this).val() );

                $(this).parents('.yqx-dialog').find('.staff-list li')
                    .each(function () {
                        if($.trim($(this).text()).indexOf(name) === -1) {
                            $(this).addClass('hide');
                        } else {
                            $(this).removeClass('hide');
                        }
                    });
            })
            .on('click', '.js-confirm', function () {
                var t = $(this).parents('.yqx-dialog')
                    .find('.js-staff:checked');

                var newUserId = t.data('id');

                if (_rebindData.customerCarIds) {
                    dialog.confirm('确定将这<strong>' + _rebindData.customerCarIds.length
                        + '</strong>条数据都调整给<strong>' + t.parent().text() + '</strong>吗？',function () {
                        var redBindData = {
                            customerCarIds:_rebindData.customerCarIds.join(','),
                            oldUserIds:_rebindData.oldUserIds.join(','),
                            newUserId:newUserId
                        }
                        reBind(redBindData, _dialogId, function () {
                            location.reload();
                        });
                    });
                }
            })
            .on('change', '.js-staff', function () {
                var box = $(this).parents('.yqx-dialog');
                var that = this;

                if (this.checked) {
                    box.find('.js-staff').each(function () {
                        if (this !== that) {
                            this.checked = false;
                        }
                    });
                }
            });


        //全选
        $(document).on('click', '.js-all-select', function () {
            var checked = this.checked;

            $('.yqx-table .js-select').each(function () {
                if(this.checked != checked) {
                    $(this).click();
                }
            });
        });
        //单选
        $(document).on('change', '.js-select', function () {
            var select = $('.js-select').length;
            var checkedSelect = $('.js-select:checked').length;
            var id = $(this).data('customerCarId');

            if (select == checkedSelect) {
                $('.js-all-select').prop('checked', true);
            } else {
                $('.js-all-select').prop('checked', false);
            }

            if(this.checked) {
                _selectedStaff[id] = $(this).data();
            } else {
                _selectedStaff[id] = false;
            }
        });
        //返回按钮
        $(document).on('click', '.js-back', util.goBack);

        function getStaff() {
            $.get(BASE_PATH + '/marketing/gather/api/get-manager', function (json) {
                if (json.success && json.data) {
                    var html = '', t = json.data;
                    var d = $('.choose-staff-dialog');

                    for (var i in t) {
                        html += '<li><label class="js-show-tips"><input type="checkbox" class="js-staff" data-id="' + t[i].userId
                            + '">' + t[i].userName + '</label>';
                    }

                    d.find('.staff-list').append(html);
                }
            });
        }

        function reBind(data, dialogId, fn) {
            $.ajax({
                url: BASE_PATH + '/marketing/gather/allot/allot-list/change-binding',
                type: 'post',
                data: data,
                success: function (json) {
                    if (json.success) {
                        dialog.success(json.message || '客户调整成功', fn);

                        dialog.close(dialogId);
                    } else {
                        dialog.fail(json.message || '保存失败')
                    }
                }
            });
        }

        function offBind(data, fn) {
            $.ajax({
                url: BASE_PATH + '/marketing/gather/allot/allot-list/unbinding',
                type: 'post',
                data: data,
                success: function (json) {
                    if (json.success) {
                        dialog.success(json.message || '解绑成功', fn);
                    } else {
                        dialog.fail(json.message || '保存失败')
                    }
                }
            })
        }

        function getBindData(data) {
            var ret = {
                customerCarIds: [],
                oldUserIds: []
            };

            for(var i in data) {
                if(data[i]) {
                    ret.customerCarIds.push(data[i].customerCarId);
                    ret.oldUserIds.push(data[i].userId);
                }
            }

            return ret;
        }

        function isSelect(id, data) {
            var flag = true;

            if(id == 'all' && data) {
                for(var i in data) {
                    if( !_selectedStaff[ data[i].customerCarId ] ) {
                        flag = false;
                        break;
                    }
                }

                if(!i) {
                    flag = false;
                }
            } else {
                flag = !!_selectedStaff[id];
            }

            return flag;
        }
    });
});
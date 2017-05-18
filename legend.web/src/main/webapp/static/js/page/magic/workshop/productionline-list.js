/*
 *  zqq 2016/07/11
 *  生产线管理
 */
$(function () {
    var doc = $(document);
    seajs.use([
        'table',
        'dialog',
        'art',
        'select',
        'check'
    ], function (tb, dg, at, st, ck) {
        var doc = $(document);
        ck.init();
        //排班管理弹窗
        var schedulingDialog = null;
        doc.on('click', '.Z-manager-btn', function () {
            var _this = $(this);
            if (_this.data('eid')) {
                var data = {
                    lineId: _this.data('eid')
                };
                $.ajax({
                    url: BASE_PATH + '/workshop/LineProcessManager/managerList',
                    type: 'GET',
                    data: data,
                    dataType: 'json',
                    contentType: 'application/json',
                    success: function (result) {
                        if (result.success) {
                            var html = at('schedulingDialog', {'json': result});
                            schedulingDialog = dg.open({
                                area: ['530px', '350px'],
                                content: html
                            });
                            $('.form-item-box').each(function(){
                                var teamLength = $(this).find('.form-btn-box:visible').length;
                                if( teamLength <= 1){
                                    $(this).find('.Z-scheduling-del').hide();
                                }
                            })

                        } else {
                            dg.fail(result.message);
                        }
                    }
                })
            } else {
                dg.fail("没有对应的生产线！");
            }
        });




        //function team(){
        //    st.init({
        //        dom: '.js-team',
        //        url: BASE_PATH + '/workshop/team/list',
        //        showKey: "id",
        //        showValue: "name",
        //        isClear:true,
        //        callback:function(showKey,showValue){
        //            //技师下拉列表
        //            st.init({
        //                dom: '.js-operator',
        //                url: BASE_PATH + '/workshop/team/managerByTeamId?teamId'+showKey,
        //                showKey: "managerId",
        //                showValue: "managerName",
        //                isClear:true,
        //            });
        //        }
        //    });
        //}

            //点击班组下拉列表
            $("body").on("click", '.item-type-down', function () {
                $('.yqx-select-options').hide();
                var _that = $(this);
                if (_that.hasClass('icon-angle-down')) {
                    $.ajax({
                        url: BASE_PATH + '/workshop/team/list',
                        type: 'GET',
                        success: function (result) {
                            if (result.success) {
                                var html = '';
                                var content = result.data;
                                for (var i = 0; i < content.length; i++) {
                                    html += "<dl><dd class='yqx-select-option js-show-tips' title=" + content[i].name + " data-itemid=" + content[i].id + ">" + content[i].name + "</dd></dl>"
                                }
                                _that.prev().append(html);
                            }
                        }
                    });
                    _that.prev().show();
                    $('.item-type-down').attr('class', 'fa item-type-down icon-angle-down');
                    $('.member-type-down').attr('class', 'fa member-type-down icon-angle-down');
                    _that.attr('class', 'fa item-type-down icon-angle-up');
                } else {
                    $('.yqx-select-options').hide();
                    _that.attr('class', 'fa item-type-down icon-angle-down');
                }
                $('.form-item dl').remove();
                return false;
            })
            //点击组员下拉列表
            $("body").on('click', '.member-type-down', function () {
                var _that = $(this);
                var teamId = _that.parents(".form-btn-box").find(".item-type-select").data('teamid');
                if (!teamId) return false;
                $('.yqx-select-options').hide();
                if (_that.hasClass('icon-angle-down')) {
                    $.ajax({
                        url: BASE_PATH + '/workshop/team/managerByTeamId',
                        type: 'GET',
                        data: {'teamId': teamId},
                        success: function (result) {
                            if (result.success) {
                                var html = '';
                                var content = result.data;
                                for (var i = 0; i < content.length; i++) {
                                    html += "<dl><dd class='yqx-select-option js-show-tips' title=" + content[i].managerName + " data-managerid=" + content[i].managerId + ">" + content[i].managerName + "</dd></dl>"
                                }
                                _that.prev().append(html);
                            }
                        }
                    });
                    _that.prev().show();
                    $('.item-type-down').attr('class', 'fa item-type-down icon-angle-down');
                    $('.member-type-down').attr('class', 'fa member-type-down icon-angle-down');
                    _that.attr('class', 'fa member-type-down icon-angle-up');
                } else {
                    $('.yqx-select-options').hide();
                    _that.attr('class', 'fa member-type-down icon-angle-down');
                }
                $('.form-item dl').remove();
                    return false;
            })


            //点击班组下拉列表将内容赋值到文本框中
        doc.on('click', '.yqx-select-option ', function () {
                var _that = $(this);
                _that.parents('.form-item').find('.item-type-select').val(_that.text());
                _that.parents('.form-item').find('.item-type-select').data('teamid', _that.data('itemid'));
                $('.item-type-down').attr('class', 'fa item-type-down icon-angle-down');
                $('.member-type-down').attr('class', 'fa member-type-down icon-angle-down');
                _that.parents('.yqx-select-options').hide();
                _that.parents(".form-btn-box").find(".item-members-select").val('');
            })
            //点击组员下拉列表将内容赋值到文本框中
        doc.on('click', '.yqx-select-option ', function () {
                var _that = $(this);
                _that.parents('.form-item').find('.item-members-select').val(_that.text());
                _that.parents('.form-item').find('.item-members-select').data('managerid', _that.data('managerid'));
                $('.item-type-down').attr('class', 'fa item-type-down icon-angle-down');
                $('.member-type-down').attr('class', 'fa member-type-down icon-angle-down');
                _that.parents('.yqx-select-options').hide();
                //判断选择的排班人员是否有重复
                //1.判断工序id，如果相同
                //2.判断班组id，如果相同
                //3.判断人员id，如果相同
                //4.判断纪录id，如果相同，则不做操作，否则做重复处理
                $('.item-type-select').each(function(){
                    if(_that.parents(".form-btn-box").data('lineprocessid') == $(this).parents(".form-btn-box").data('lineprocessid')){
                        if(_that.parents(".form-btn-box").find(".item-type-select").data('teamid') == $(this).data('teamid')){
                            if(_that.data('managerid') == $(this).parents(".form-btn-box").find(".item-members-select").data('managerid')){
                                if(_that.parents(".form-btn-box").data('lineProcessManagerId') != $(this).parents(".form-btn-box").data("lineProcessManagerId")){
                                    _that.parents('.form-item').find('.item-members-select').val('');
                                    _that.parents('.form-item').find('.item-members-select').data('managerid', '');
                                    dg.fail("排班人员不能重复！");
                                    return false;
                                }
                            }
                        }
                    }
                });
            })
            //点击排班管理新增按钮
        doc.on('click', '.Z-scheduling-add', function () {
                var lineProcessId = $(this).prev('.form-item-box').find(".form-btn-box").eq(0).data('lineprocessid');
                var html = at('teamSelectQuick',{lineProcessId: lineProcessId});
                $(this).siblings('.form-item-box').append(html);
                $(this).prev('.form-item-box').children('.form-btn-box:visible').find('.Z-scheduling-del').eq(0).show();
            })
            //点击排班管理保存按钮
        doc.on('click', '.Z-save', function () {
                var arr = [];
                var data = {};
                $('.form-btn-box').each(function () {
                    data = {
                        "id": $(this).data('lineProcessManagerId'),
                        "lineProcessId": $(this).data('lineprocessid'),
                        "managerId": $(this).find('.item-members-select').data('managerid'),
                        "managerName": $(this).find('.item-members-select').val(),
                        "teamId": $(this).find('.item-type-select').data('teamid'),
                        "teamName": $(this).find('.item-type-select').val(),
                        "isDeleted":$(this).find('input[name="isDeleted"]').val()
                    };
                    arr.push(data);
                });
                //如果提交的排班列表中temid或者managerid为空，中断执行
                for (var i = 0; i < arr.length; i++) {
                    if (!arr[i].teamId || !arr[i].managerId) {
                        dg.fail("请选择班组或人员！");
                        return false;
                    }
                }
                $.ajax({
                    url: BASE_PATH + '/workshop/LineProcessManager/batchSaveOrUpdate',
                    type: 'POST',
                    data: {'managerListStr': JSON.stringify(arr)},
                    success: function (result) {
                        if (result.success) {
                            dg.success("保存成功！");
                            dg.close(schedulingDialog);
                        } else {
                            dg.fail(result.message);
                        }
                    }
                })
            });

        //排班删除
        doc.on('click', '.Z-scheduling-del', function () {
            var box = $(this).parents('.form-item-box');
            var lineProcessManagerId = $(this).parents(".form-btn-box").data('lineProcessManagerId');
            if( lineProcessManagerId == ''){
                $(this).parents('.form-btn-box').remove();
            }else{
                debugger;
                $(this).parents('.form-btn-box').hide();
                $(this).parents('.form-btn-box').find('input[name="isDeleted"]').val('Y');
            };
            var teamLength = box.find('.form-btn-box:visible').length;
            if( teamLength <= 1){
                box.find('.Z-scheduling-del').hide();
            };
        });

    });
});
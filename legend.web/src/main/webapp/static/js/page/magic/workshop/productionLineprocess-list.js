/*
 *  zmx 2016/08/18
 *  生产线管理
 */

$(function () {
    var doc = $(document);

    seajs.use([
        'table',
        'dialog',
        'art',
        'select'
    ],function(tb,dg,at,st){
        dg.titleInit();

        //生产线表格填充
        function table(){
            $.ajax({
                url: BASE_PATH + '/workshop/productionline/list',
                success:function(result){
                    if(result.success){
                        var html=at('productionTableTpl',{json:result})
                        $('#productionTableCon').html(html);
                    }else{
                        dg.fail(result.message);
                    }
                }
            })
        }
        table();


        //事故线 排班按钮
        var schedulingDialog;
        doc.on('click','.js-scheduling',function(){
            var lineId = $(this).data("eid");
            $.ajax({
               type:"get",
               url:BASE_PATH + '/workshop/LineProcessManager/managerList',
               data:{
                   lineId:lineId
               },
               success:function(result){
                    if(result.success){
                        var html = at('schedulingDialogAccident',{json:result});
                        //排班弹窗
                        schedulingDialog = dg.open({
                            area:['530px','350px'],
                            content:html,
                        })
                        //第一组可编辑
                        $('.form-item-box').eq(0).find('.teamName').removeAttr('disabled');
                        $('.form-item-box').eq(0).find('.teamName').addClass('js-teamName');
                        $('.form-item-box ').eq(0).find('.teamName').removeAttr('disabled');

                        //删除其它组里的a标签
                        $('.form-item-box').not(':first').find('a').remove();
                        $('.js-add-team').not(':first').remove();
                        onlyTeam();
                    }else{
                        dg.fail(result.message)
                    }
               }
            });
        });

        //排班弹窗（选择班组）
        st.init({
            dom: '.js-teamName',
            url: BASE_PATH + '/workshop/team/list',
            showKey: "id",
            showValue: "name",
            callback:function(showKey,showValue){
                var index = $(this).parents('.form-item').index('.form-item');
                $('.form-item-box').each(function(){
                    //自动填充其它班组
                    $(this).find('.teamName').eq(index).val(showValue);
                    $(this).find('input[name="teamId"]').eq(index).val(showKey);
                    $(this).find('input[name="teamName"]').eq(index).val(showValue);
                })

            }
        });

        //弹窗（增加班组）
        doc.on('click','.js-add-team',function(){
            $('.form-item-box').each(function(){
                var lineProcessId = $(this).find('input[name="lineProcessId"]').val();
                var html = at('teamSelect',{lineProcessId: lineProcessId});
                $(this).append(html);
            });
            //第一组可编辑
           $('.form-item-box').eq(0).find('.teamName').removeAttr('disabled');
           $('.form-item-box').eq(0).find('.teamName').addClass('js-teamName');
            //删除其它组a标签
           $('.form-item-box').not(':first').find('a').remove();
           $('.form-item-box').eq(0).find('.js-remove-team').show();

        });

        //弹窗（删除班组）
        doc.on('click','.js-remove-team',function(){
            var index = $(this).parents('.form-btn-box').index('.form-btn-box');
            var lineProcessManagerId = $(this).siblings('input[name="lineProcessManagerId"]').val();
            $('.form-item-box').each(function(){
                if( lineProcessManagerId == '' ){
                    $(this).find('.form-btn-box').eq(index).remove();
                }else{
                    $(this).find('.form-btn-box').eq(index).hide();
                    $(this).find('.form-btn-box').eq(index).find('input[name="isDeleted"]').val('Y');
                }
            });
            onlyTeam();
        });

        //是否只存在一个排班班组
        function onlyTeam(){
            var teamLength = $('.form-item-box').eq(0).find('.form-btn-box:visible').length;
            if( teamLength <= 1){
                $('.js-remove-team').hide();
            }
        }


        //排班保存按钮
        doc.on('click','.js-scheduling-save',function(){
            //判断班组是否重复
            var prevId;
            var flag = $('.form-item-box').eq(0).find('input[name="teamName"]').toArray().some(function(e, i){
                var id;
                if(i == 0) {
                    prevId = $(e).val()
                } else
                 id = $(e).val();
                return id == prevId
            });
            if(!flag){
                var lineProcessManagerVOList = [];
                $('.form-btn-box').each(function(){
                    var id = $(this).find('input[name="lineProcessManagerId"]').val(),
                        lineProcessId = $(this).find('input[name="lineProcessId"]').val(),
                        teamId = $(this).find('input[name="teamId"]').val(),
                        teamName = $(this).find('input[name="teamName"]').val(),
                        isDeleted = $(this).find('input[name="isDeleted"]').val()
                    lineProcessManagerVOList.push({
                        id:id,
                        lineProcessId:lineProcessId,
                        teamId:teamId,
                        teamName:teamName,
                        isDeleted:isDeleted
                    })
                });

                $.ajax({
                    url: BASE_PATH + '/workshop/LineProcessManager/batchSaveOrUpdate',
                    type: 'POST',
                    data: {'managerListStr': JSON.stringify(lineProcessManagerVOList)},
                    success: function (result) {
                        if (result.success) {
                            dg.success("保存成功！");
                            dg.close(schedulingDialog);
                        } else {
                            dg.fail(result.message);
                        }
                    }
                })
            }else{
                dg.fail("排班人员不能重复！");
                return false;
            }
        });

        //删除生产线按钮
        doc.on('click','.js-del',function(){
            var eid = $(this).data("eid");
            dg.confirm('确定要删除吗？',function(){
                    $.ajax({
                        type: 'POST',
                        url: BASE_PATH + "/workshop/productionline/delete",
                        data: {
                            id: eid
                        },
                        success: function (result) {
                            if (result.success) {
                                table();
                                dg.success('删除成功!');
                            } else {
                                dg.fail(result.message);
                            }
                        }
                    });
                })
        });

        //编辑按钮
        doc.on('click','.js-edit',function(){
            var eid = $(this).data("eid");
            window.location.href=BASE_PATH +"/workshop/productionline/productionline-edit?lineId="+eid;
        });

        //新增生产线
        doc.on('click','.js-add-productionLine',function(){
            window.location.href=BASE_PATH +"/workshop/productionline/productionline-add"
        });
    });
});
/**
 * Created by jason on 15/8/7.
 */

//template.config("escape", false);

$(function(){

    // 全局加载...
    var loading;
    $.ajaxSetup({
        beforeSend: function() {
            loading = taoqi.loading();
        },
        complete: function() {
            taoqi.close(loading);
        }
    });

    // init
    $('.counter').counter();

    $(document).on('click','.activityType',function(){
        $(this).is('.typeChecked')?$(this).removeClass('typeChecked'):$(this).addClass('typeChecked');
    });

    var editor = new UE.ui.Editor();
    editor.render('detailDesc');

    editor.ready(function() {

        var _con = document.getElementById("editorExample"),
            _body = document.getElementById('baidu_editor_0').contentWindow.document.body,
            _isEdit = document.getElementById("isEdit"),
            isEdit = function () {// 内容是否已经被填充
                if(_isEdit.value == 0 ) {
                    editor.setContent(_con.innerHTML);
                }
            };

        isEdit();

        $(_body)
            .on('focus', function() {

                // 如果内容未被填充，清空案例
                if( _isEdit.value == 0 ) {
                    editor.setContent("");
                }
            })
            .on('blur', function() {

                // 根据内容修改编辑状态
                _isEdit.value = editor.getContent() !== "" ? 1 : 0;
                isEdit();
            });
    });

    var queryList=['id','actName','actDesc', 'startTime','actStatus'];

    //保存
    $(document).on('click','.save',function(){
        var loading;
        //验证数据
        if(!validaData()) {
            return false;
        }
        $.ajaxSetup({
            contentType:'application/json',
            beforeSend: function() {
                loading = taoqi.loading("正在保存，请稍后...");
            },
            complete: function() {
                taoqi.close(loading);
            }
        });
        $.post('add/ng',makeDate(),function(result){
            if (result.success != true) {
                layer.msg(result.errorMsg,3,3);
                return false;
            } else {
                layer.msg('保存活动成功!',2,1,function(){
                    $('#actId').val(result.data);
                    var id = $('#actId').val();
                    taoqi.loading();
                    window.location.href = BASE_PATH+"/shop/cz_app/activity/edit_act?actId="+id;
                });
            }
        });
        return false;
    });
    //编辑
    $(document).on('click','.edit',function(){
        var loading;
        //验证数据
        if(!validaData()) {
            return false;
        }

        $.ajaxSetup({
            contentType:'application/json',
            beforeSend: function() {
                loading = taoqi.loading("正在保存，请稍后...");
            },
            complete: function() {
                taoqi.close(loading);
            }
        })
        $.post('edit/ng',makeDate(),function(result){
            if (result.success != true) {
                layer.msg(result.errorMsg,3,3);
                return false;
            } else {
                taoqi.loading();
                layer.msg('保存活动成功!',2,1,function(){
                    history.go(0);
                });
            }
        });
        return false;
    });

    //活动编辑页发布活动
    $(document).on('click','.open',function(){
        var loading;
        //验证数据
        if(!validaData()) {
            return false;
        }

        $.ajaxSetup({
            contentType:'application/json',
            beforeSend: function() {
                loading = taoqi.loading("正在发布，请稍后...");
            },
            complete: function() {
                taoqi.close(loading);
            }
        })

        layer.confirm("您确定要发布该活动吗?",function(){
            $.post('open/ng',makeDate(),function(result){
                if (result.success != true) {
                    layer.msg(result.errorMsg,3,3);
                    return false;
                } else {
                    taoqi.loading();
                    layer.msg('发布活动成功，活动审核需1-2个工作日!',2,1,function(){
                        history.go(0);
                    });
                }
            });
        },function (){
            return false;
        });
        return false;
    });

    //活动列表页发布活动
    $(document).on('click','.release',function(){
        var id = $('#id').val();//活动ID
        layer.confirm("您确定要发布该活动吗?",function(){
            $.ajax({
                type : "GET",
                url : BASE_PATH + "/shop/cz_app/activity/release_act/ng",
                data : {
                    actId : id
                },
                success: function (result){
                    if (result.success != true) {
                        layer.msg(result.errorMsg,3,3);
                        return false;
                    } else {
                        layer.msg(result.data,2,1,function(){
                            history.go(0);
                        });
                    }
                }
            });
        },function (){
            return false;
        });
    });

    // 上传图片
    $("#uploadBanner").fileupload({
        url: BASE_PATH + "/index/oss/upload_img",
        type: "post",
        /*formData: {

        },      // 额外的参数*/
        singleFileUploads: true,
        //autoUpload: true,  // 默认为true
        done: function(e, data) {
            //debugger;
            // data.result
            // data.textStatus;
            // data.jqXHR;
            //debugger;
            var $img = $(this).siblings(".bannerImg");
            if(data.result.success) {

                $img.show().attr("src", data.result.data.original);
                $('.img_del_btn').show();
            } else {
                taoqi.error(data.result.errorMsg);
            }
        },
        fail: function(e, data) {
            // data.errorThrown
            // data.textStatus;
            // data.jqXHR;
            taoqi.error("网络异常,请重试！");
        },
        /*always: function(e, data) {
            // data.result
            // data.textStatus;
            // data.jqXHR;
        }*/
    });

    $(document).on('click', '.img_del_btn', function() {

        $(this).hide()
            .siblings(".bannerImg").hide().attr("src", "");
    });

    //预览效果
    $(document).on('click', '.preview', function() {

        var id = $("#id").val();
        if(id === undefined) {
            taoqi.info("请先保存信息！");
        } else {
            taoqi.activityPreview(id);
        }
    });

    //下线活动
    $(document).on('click','.offline',function(){
        var id = $('#id').val();//活动ID
        layer.confirm("您确定要下线该活动吗?",function(){
            $.ajax({
                type : "GET",
                url : BASE_PATH + "/shop/cz_app/activity/off_act/ng",
                data : {
                    actId : id
                },
                success: function (result){
                    if (result.success != true) {
                        layer.msg(result.errorMsg,3,3);
                        return false;
                    } else {
                        layer.msg(result.data,2,1,function(){
                            history.go(0);
                        });
                    }
                }
            });
        },function (){
            return false;
        });
    });

    //删除活动
    $(document).on('click','.delete',function(){
        var id = $('#id').val();//活动ID
        layer.confirm("您确定要删除该活动吗?",function(){
            $.ajax({
                type : "GET",
                url : BASE_PATH + "/shop/cz_app/activity/del_act/ng",
                data : {
                    actId : id
                },
                success: function (result){
                    if (result.success != true) {
                        layer.msg(result.errorMsg,3,3);
                        return false;
                    } else {
                        layer.msg(result.data,2,1,function(){
                            window.location.href = BASE_PATH+"/shop/cz_app/activity/act_list";

                        });
                    }
                }
            });
        },function (){
            return false;
        });
    });

    //组装数据
    function makeDate(){
        var data={},
            list = [],
            endTime = $.trim($("#endTime").val());
        for(var i=0;i<queryList.length;i++){
            if($('#'+queryList[i]).val()){
                data[queryList[i]]=$('#'+queryList[i]).val();
            }
        }
        $.each($('.typeChecked'),function(){
            var d={cateId:$(this).data('id'),cateName:$(this).html()};
            list.push(d);
        });
        data['endTime'] = (endTime == '永久') ? null : endTime;
        data['imgUrl'] = $("#bannerImg").attr("src");
        data['detailDesc']=editor.getContent();
        data['actCateList']=list;
        return JSON.stringify(data);
    }

    function validaData() {
        if(!$.trim($('#actName').val())){
            layer.msg("请填写活动名称!",2,3);
            return false;
        } else if($.trim($('#actName').val()).length > 20){
            layer.msg("活动名称不能超过20个字!",2,3);
            return false;
        } else if(!$.trim($('#actDesc').val())){
            layer.msg("请填写活动简介!",2,3);
            return false;
        } else if($.trim($('#actDesc').val()).length > 40){
            layer.msg("活动名称不能超过40个字!",2,3);
            return false;
        } else if(!$('#startTime').val()){
            layer.msg("请选择活动开始时间!",2,3);
            return false;
        } else if(!$('.typeList').find('.typeChecked').length){
            layer.msg("请选择活动分类!",2,3);
            return false;
        } else if($('#isEdit').val() == 0 ){
            layer.msg("请填写活动首页内容!",2,3);
            return false;
        }
        return true;
    }
});

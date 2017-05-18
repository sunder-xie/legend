/**
 * Created by QXD on 2014/11/10.
 */
$(function () {
    //保存按钮
    $("#update").click(function () {
        var isAdminVal = $("#isAdminVal").val();
        if (isAdminVal != 1) {
            if ($.trim($("#accountIdReg").val()) == ""
                || $.trim($("#nameReg").val()) == ""
                || $.trim($("#mobileReg").val()) == ""
                || $.trim($("#rolesReg").val()) == ""
            ) {
                taoqi.failure("信息不完整 *为必填项");
                return;
            }
            var supplierObj = {
                id: $.trim($("#accountIdReg").val()),
                name: $.trim($("#nameReg").val()),
                mobile: $.trim($("#mobileReg").val()),
                roleId: $.trim($("#rolesReg").val())
            };

            var save = taoqi.loading("保存信息中");
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: BASE_PATH + '/shop/member/user_info_change',
                data: supplierObj,
                success: function (data) {
                    layer.close(save);
                    if (data.success != true) {
                        taoqi.error(data.errorMsg);
                        return;
                    } else {
                        taoqi.success("操作成功");
                    }
                },
                error: function (a, b, c) {
//                    console.log(a,b,c);
                }
            });
        } else {
            if (
                $.trim($("#accountIdReg").val()) == ""
                || $.trim($("#accountLoginIdReg").val()) == ""
                || $.trim($("#accountReg").val()) == ""
                || $.trim($("#passwordReg").val()) == ""
                || $.trim($("#nameReg").val()) == ""
                || $.trim($("#mobileReg").val()) == ""
                || $.trim($("#rolesReg").val()) == ""
            ) {
                taoqi.failure("信息不完整 *为必填项");
                return;
            }
            //先校验手机号是否存在
            var oldMobile = $.trim($("#js-old-mobile").val());
            var mobile = $.trim($("#mobileReg").val());
            if(oldMobile != mobile){
                //修改了手机号，需要校验下手机号是否在其他店存在
                $.ajax({
                    type: 'GET',
                    dataType: 'json',
                    url: BASE_PATH + '/shop/roles_func/check-mobile',
                    data: {
                        mobile:mobile
                    },
                    success: function (data) {
                        if (data.success != true) {
                            taoqi.error(data.errorMsg);
                            return;
                        } else {
                            var boolean = data.data;
                            if(boolean){
                                taoqi.ask("此手机号码存在于其他门店，是否继续操作？",function(){
                                    updateUser();
                                },function(){
                                    return false;
                                });
                            } else {
                                updateUser();
                            }
                        }
                    }
                });
            } else {
                //未修改手机号
                updateUser();
            }
        }
    });

    function updateUser(){
        var pvgRoleIds = [];
        $(":checkbox[name='pvgRoleId']").each(function () {
            if ($(this).prop('checked') == true) {
                pvgRoleIds.push($(this).val());
            }
        });
        if (pvgRoleIds == null || pvgRoleIds == '') {
            taoqi.failure("请选择APP 角色");
            return;
        }

        var online = $('#job').is(':checked');
        var workStatus = 1;
        if (online) {
            workStatus = 1;
        } else {
            workStatus = 0;
        }
        //时间校验
        var flag=true;
        $('.js-time-ico').each(function(){
            var str = $.trim($(this).val());
            var reg=/^((20|21|22|23|[0-1]\d)\:[0-5][0-9])(\:[0-5][0-9])?$/;
            if(!reg.test(str)){
                flag = false;
            }

        });
        if(!flag){
            taoqi.failure("您输入的时间不正确");
            return;
        }

        var supplierObj = {
            accountIdReg: $.trim($("#accountIdReg").val()),
            accountLoginIdReg: $.trim($("#accountLoginIdReg").val()),
            accountReg: $.trim($("#accountReg").val()),
            passwordReg: $.trim($("#passwordReg").val()),
            nameReg: $.trim($("#nameReg").val()),
            mobileReg: $.trim($("#mobileReg").val()),
            rolesReg: $.trim($("#rolesReg").val()),
            pvgRoleIds: pvgRoleIds.join(','),
            teamId: $.trim($('#teamId').val()),
            teamName: $.trim($('#teamId option:selected').text()),
            processId: $.trim($('#processId').val()),
            processName: $.trim($('#processId option:selected').text()),
            workStatus: workStatus,
            extId:$.trim($('#cardNum').data('extId')),
            startTime: $.trim($("input[name='startTime']").val()),
            endTime: $.trim($("input[name='endTime']").val())
        };
        var save = taoqi.loading("保存信息中");

        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: BASE_PATH + '/shop/member/user_info_change_by_admin',
            data: supplierObj,
            success: function (data) {
                layer.close(save);
                if (data.success != true) {
                    taoqi.error(data.errorMsg);
                    return;
                } else {
                    taoqi.success("操作成功");
                }
            },
            error: function (a, b, c) {
//                        console.log(a,b,c);
            }
        });
    }
    //工牌卡样式
    $(document).on('click', '.js-dialog-pic', function () {
        var content = $('#Badge').html();
        $.layer({
            type: 1,
            shade: [0],
            area: ['320px', '560px'],
            title: false,
            border: [0],
            page: {
                dom: '#Badge',
                html: content
            }
        });

        setTimeout(function () {
            var $$cardBox = document.getElementById('cardBox'),
                w = $$cardBox.clientWidth,
                h = $$cardBox.clientHeight;

            var canvas = document.createElement('canvas');
            canvas.width = 2 * w;
            canvas.height = 2 * h;
            canvas.style.width = w + "px";
            canvas.style.height = h + "px";
            var ctx = canvas.getContext('2d');
            
            ctx.scale(2, 2);
            ctx.translate(-1, -1);
            
            html2canvas($$cardBox, {
                canvas: canvas
            }).then(function (canvas) {
                var image = canvas.toDataURL("image/png")
                    .replace("image/png", "image/octet-stream");
                $('#cardDownload').attr('href', image);
            });
        }, 600);

    });

    $(document).on('click','.js-delete',function(){
        var type = $(this).data('type'),
            id = $(this).parents('li').data('id');
        layer.confirm('确定要解绑吗？',function(){
            $.ajax({
                type:"post",
                url:BASE_PATH + '/shop/manager/devices/'+ type +'/' + id,
                success:function(result){
                    if(result.success){
                        taoqi.success(result.data);
                        window.location.reload();
                    }else{
                        taoqi.error(result.message);
                    }
                }
            })
        });

    })
    $(".clockpicker").clockpicker();

    //时间校验
    $(document).on('blur','.js-time-ico',function(){
        var str = $.trim($(this).val());
        if(str.length!=0){
            var reg=/^((20|21|22|23|[0-1]\d)\:[0-5][0-9])(\:[0-5][0-9])?$/;
            if(!reg.test(str)){
                taoqi.failure("您输入的时间不正确");
            }
        }
    });

});

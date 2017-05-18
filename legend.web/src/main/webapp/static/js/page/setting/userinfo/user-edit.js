/*
 * create by zmx 2017/1/5
 * 人员资料编辑
 */

$(function(){
    var doc = $(document);
    seajs.use([
        'date',
        'dialog',
        'select',
        'formData',
        'check'
    ],function(dp,dg,st,fd,ck){
        //验证
        ck.init();

        // 开始结束日期
        dp.dpStartEnd();

        //返回按钮
        $(document).on('click','.js-goBack',function(){
            util.goBack();
        });

        //岗位下拉列表
        st.init({
            dom:".js-roles",
            url: BASE_PATH + '/shop/setting/roles/get-roles-list',
            showKey: "id",
            showValue: "name",
            pleaseSelect:true,
            callback:function(showKey){
                $('input[name="roleId"]').val(showKey);
            }
        });

        //班组下拉列表
        st.init({
            dom:".js-team",
            showKey: "teamId",
            callback:function(showKey){
                $('input[name="teamId"]').val(showKey);
            }
        });

        //岗位下拉列表
        st.init({
            dom:".js-process",
            showKey: "processId",
            callback:function(showKey){
                $('input[name="processId"]').val(showKey);
            }
        });

        //时间
        $(document).on('click', '.clockpicker', function (e) {
            var clock = $(this).find('.clockpicker-popover');
            if (!clock.length) {
                clock = $('body > .clockpicker-popover');
                $(this).append(clock);
            }
            clock.css({
                top: 'initial',
                left: '0',
                position: 'absolute'
            });
            e.stopImmediatePropagation();
        });

        //时间校验
        $(document).on('blur', '.js-time-ico', function () {
            var str = $.trim($(this).val());
            if (str.length != 0) {
                var reg = /^((20|21|22|23|[0-1]\d)\:[0-5][0-9])(\:[0-5][0-9])?$/;
                if (!reg.test(str)) {
                    dg.fail("您输入的时间不正确");
                }
            }
        });

        $(".clockpicker").clockpicker();

        //工牌卡
        $(document).on('click', '.js-dialog-pic', function () {
            var content = $('#Badge').html();
            dg.open({
                area: ['320px', '560px'],
                content:content
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


        doc.on('click','.js-save',function(){
            var isAdminVal = $("#isAdminVal").val();
            var supplierObj = fd.get('#formData');
            if( !ck.check()){
                return;
            }
            if (isAdminVal != 1) {
                console.log(supplierObj);
                $.ajax({
                    type: 'POST',
                    url: BASE_PATH + '/shop/setting/user-info/change',
                    data: JSON.stringify(supplierObj),
                    dataType: 'json',
                    contentType: "application/json",
                    success: function (result) {
                        if (result.success) {
                            dg.success(result.data);
                        } else {
                            dg.fail(result.message);
                        }
                    }
                });
            } else {
                //先校验手机号是否存在
                var oldMobile = $('#oldMobile').val();
                var mobile = $('input[name="mobile"]').val();
                if(oldMobile != mobile){
                    //修改了手机号，需要校验下手机号是否在其他店存在
                    $.ajax({
                        type: 'GET',
                        dataType: 'json',
                        url: BASE_PATH + '/shop/roles_func/check-mobile',
                        data: {
                            mobile:mobile
                        },
                        success: function (result) {
                            if (result.success) {
                                var boolean = result.data;
                                if(boolean){
                                    dg.confirm("此手机号码存在于其他门店，是否继续操作？",function(){
                                        updateUser();
                                    },function(){
                                        return false;
                                    });
                                } else {
                                    updateUser();
                                }
                            } else {
                                dg.fail(result.message);
                            }
                        }
                    });
                } else {
                    //未修改手机号
                    updateUser();
                }
            }

        });

        /*设备解绑*/
        doc.on('click','.js-delete',function(){
            var type = $(this).data('type'),
                id = $(this).data('id');
            dg.confirm('确定要解绑吗？',function(){
                $.ajax({
                    type:"post",
                    url:BASE_PATH + '/shop/security-login/devices/'+ type +'/' + id,
                    success:function(result){
                        if(result.success){
                            dg.success(result.data);
                            window.location.reload();
                        }else{
                            dg.error(result.message);
                        }
                    }
                })
            });

        })

        function updateUser(){
            var pvgRoleIds = [];
            $(".pvgRoleId").each(function () {
                if ($(this).prop('checked') == true) {
                    pvgRoleIds.push($(this).val());
                }
            });
            if (pvgRoleIds == null || pvgRoleIds == '') {
                dg.warn("请选择APP 角色");
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
                dg.warn("您输入的时间不正确");
                return;
            }

            var supplierObj = fd.get('#formData');
            supplierObj.pvgRoleIds = pvgRoleIds.join(',');
            supplierObj.workStatus = workStatus;
            supplierObj.extId = $("#cardNum").data("extId");
            $.ajax({
                type: 'POST',
                url: BASE_PATH + '/shop/setting/user-info/change-admin',
                data: JSON.stringify(supplierObj),
                dataType: 'json',
                contentType: "application/json",
                success: function (result) {
                    if (result.success) {
                        dg.success(result.data);
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        }
    });

});

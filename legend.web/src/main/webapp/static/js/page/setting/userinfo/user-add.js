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
                $('input[name="userRoleId"]').val(showKey);
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

        doc.on('click','.js-save',function(){
            if( !ck.check()){
                return;
            }
            //先校验手机号是否存在
            var mobile = $('input[name="userMobile"]').val();
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
                                addUser();
                            },function(){
                                return false;
                            });
                        } else {
                            addUser();
                        }
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });

        function addUser(){
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
            $.ajax({
                type: 'POST',
                url: BASE_PATH + '/shop/setting/user-info/add',
                data: JSON.stringify(supplierObj),
                dataType: 'json',
                contentType: "application/json",
                success: function (result) {
                    if (result.success) {
                        dg.success(['添加', 2, '秒后返回列表页'],function(){
                            window.location.href = BASE_PATH + '/shop/setting/user-info/user-list';
                        });
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        }
    });

});

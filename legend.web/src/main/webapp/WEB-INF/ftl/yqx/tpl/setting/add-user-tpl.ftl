<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/third-plugin/clockpicker/jquery-clockpicker.min.css"/>
<style>
    .yqx-dialog .show-grid{
        margin-top: 10px;
        margin-bottom: 0;
    }
    .show-grid .col-6{
       display: inline-block;
        width: 50%;
    }
    .show-grid .form-label{
        width: 70px;
        text-align: right;
    }
    .show-grid .form-item{
        width: 210px;
    }
    .show-grid .w-98{
        width: 98px;;
    }
    .show-grid .tips{
        width: 210px;
        line-height: 18px;
        margin-left: 85px;
        color: #999;
    }
    .show-grid label input{
        margin-right: 4px;
    }
    .dialog-btn{
        border-top: 1px solid #cacaca;
        margin-top: 10px;
        padding: 10px;
        text-align: center;
    }
    .show-grid .login-time{
        line-height: 30px;
        border: 1px solid #bfbfbf;
        border-radius: 3px;
        padding: 0 5px;
        background: #fff;
        width: 120px;
    }
    .show-grid .team-width{
        width: 150px;
    }
    .popover{
       z-index: 99;
        border: 1px solid #cacaca;
    }
    .layui-layer-page .layui-layer-content{
        overflow: visible;
    }

    #userDialogData .yqx-select-options {
        display: none;
        width: 169px;
    }

</style>
<script type="text/html" id="addUserBox" data-ref-tpl="">
    <div class="add-user-box">
        <div class="yqx-dialog">
            <div class="dialog-title">
                添加员工账号
            </div>
            <div class="form-box" id="userDialogData">
                <input type="hidden" name="userRoleId" id="userRoleId">
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            账号:
                        </div>
                        <div class="form-item w-98">
                            <input type="text" name="preUserAccount" id="preUserAccount" class="yqx-input yqx-input-small" disabled>
                        </div>
                        -
                        <div class="form-item w-98">
                            <input type="text" name="userAccount" class="yqx-input yqx-input-small" data-v-type="required">
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            姓名:
                        </div>
                        <div class="form-item">
                            <input type="text" name="userName" class="yqx-input yqx-input-small" data-v-type="required" autocomplete="new-password">
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            密码:
                        </div>
                        <div class="form-item">
                            <input type="password" name="userPassword" class="yqx-input yqx-input-small" data-v-type="required" autocomplete="new-password">
                        </div>
                        <p class="tips">注：密码长度6~12位,密码必须包含数字和字母，字母不区分大小写</p>
                    </div>
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            联系电话:
                        </div>
                        <div class="form-item">
                            <input type="text" name="userMobile" class="yqx-input yqx-input-small" data-v-type="required">
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        登录时间:
                    </div>
                    <div class="form-item login-time">
                        <div class="field_box clockpicker" data-placement="bottom" data-align="top" data-autoclose="true">
                            <input type="text" class="qxy_input time-ico js-time-ico" placeholder="选择开始时间" name="startTime" value="00:00:00" data-v-type="required">
                        </div>
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                    至
                    <div class="form-item login-time">
                        <div class="field_box clockpicker" data-placement="bottom" data-align="top" data-autoclose="true">
                            <input type="text" class="qxy_input time-ico js-time-ico" placeholder="选择结束时间" name="endTime" value="23:59:59" data-v-type="required">
                        </div>
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                </div>
            <#if SESSION_SHOP_WORKSHOP_STATUS == 1 && BPSHARE == 'true'>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label">
                            班组:
                        </div>
                        <div class="form-item team-width">
                            <input type="hidden" name="teamId" value="0">
                            <input type="text" name="teamName" class="yqx-input yqx-input-icon yqx-input-small js-team" value="无">
                            <div class="yqx-select-options">
                                <dl class="js-team-content"></dl>
                            </div>
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                        <a  href="${BASE_PATH}/workshop/team/listpage" class="yqx-btn yqx-btn-1 yqx-btn-small yqx-btn-micro ">管理班组</a>
                    </div>
                    <div class="col-6">
                        <div class="form-label">
                            工序:
                        </div>
                        <div class="form-item">
                            <input type="hidden" name="processId" value="0">
                            <input type="text" name="processName" class="yqx-input yqx-input-icon yqx-input-small js-process" value="无">
                            <div class="yqx-select-options">
                                <dl class="js-process-content"></dl>
                            </div>
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>

                        <p class="tips" style="width: 250px;">注：快修组必填，事故组、快喷组员工可不填</p>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label">
                        在岗:
                    </div>
                    <div class="form-item time">
                        <span><input type="checkbox" id="job" class="job" checked="checked"> 在岗</span>
                    </div>
                </div>
            </#if>
                <div class="show-grid js-role-content">

                </div>
                <div class="dialog-btn">
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-user-save">保存</button>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small js-close">取消</button>
                </div>
            </div>
        </div>
    </div>
</script>

<script type="text/html" id="teamTpl">
    <%if(templateData && templateData.teamVOList){%>
    <%var teamVOListLength = templateData.teamVOList.length;%>
    <%for(var i=0; i < teamVOListLength; i++){%>
    <%var item = templateData.teamVOList[i];%>
    <dd class="yqx-select-option" data-key="<%= item.id%>"<%if(item.id == 0){%> selected<%}%>><%= item.name%></dd>
    <%}%>
    <%}%>
</script>

<script type="text/html" id="processTpl">
    <%if(templateData && templateData.processDTOList){%>
    <%var processListLength = templateData.processDTOList.length;%>
    <%for(var i=0; i < processListLength; i++){%>
    <%var item = templateData.processDTOList[i];%>
    <dd class="yqx-select-option" data-key="<%= item.id%>"<%if(item.id == 0){%> selected<%}%>><%= item.name%></dd>
    <%}%>
    <%}%>
</script>

<script type="text/html" id="appRoleTpl">
    <div class="form-label form-label-must">
        APP角色:
    </div>
    <%if(templateData && templateData.pvgRoleVoList){%>
    <%var pvgRoleListLength = templateData.pvgRoleVoList.length;%>
    <%for(var i=0; i < pvgRoleListLength; i++){%>
    <%var item = templateData.pvgRoleVoList[i];%>
    <label><input type="checkbox" class="pvgRoleId" value="<%= item.id%>"<%if(item.exist || item.id == pvgRoleId) {%> checked="checked"<%}%>/><%= item.roleName%></label>
    <%}%>
    <%}%>
</script>

<script src="${BASE_PATH}/static/third-plugin/clockpicker/jquery-clockpicker.min.js"></script>
<script>

    Components.addUserDialog = function (option) {
        var defaultOpt = {
            data: {
                userRoleId: '',
                pvgRoleId: ''
            },
            success: null
        },
        args = $.extend({}, defaultOpt, option),
        $doc = $(document),
        _staffId;

        seajs.use([
            'dialog',
            'formData',
            'select',
            'check',
            'art'
        ], function (dg, fd, st, ck, art) {
            var model = {
                checkMobile: function (params) {
                    return $.ajax({
                        url:BASE_PATH + '/shop/roles_func/check-mobile',
                        data: params
                    });
                },
                addStaff: function (params) {
                    return $.ajax({
                        type:'post',
                        url:BASE_PATH + '/shop/setting/user-info/add',
                        data: JSON.stringify(params),
                        contentType: "application/json"
                    });
                },
                userAddInfo: function (pvgRoleId) {
                    var data = {
                        pvgRoleId: pvgRoleId
                    };

                    return $.ajax({
                        url:BASE_PATH + '/shop/setting/user-info/user-add-info',
                        data: data
                    });
                }
            };

            function closeDialog() {
                dg.close(_staffId);
                _staffId = undefined;
            }

            //添加用户
            function addUser(params) {
                model.addStaff(params)
                        .done(function (result) {
                            if (result.success) {
                                closeDialog();
                                dg.success("操作成功", function () {
                                    args.success && args.success(result.data);
                                });
                            } else {
                                dg.fail(result.message);
                            }
                        });
            }

            //获取添加员工信息
            function setUserAddInfo() {
                //TODO 左侧导航栏的pvgRoleId
                var pvgRoleId = args.data.pvgRoleId;
                //设置钣喷等信息
                model.userAddInfo(pvgRoleId)
                        .done(function (result) {
                            if(result.success){
                                var data = {templateData: result.data, pvgRoleId: pvgRoleId};
                                var userAddInfoVo = result.data;
                                $("#preUserAccount").val(userAddInfoVo.shopAbbr);
                                if(userAddInfoVo.teamVOList){
                                    var html = art('teamTpl', data);
                                    $(".js-team-content").html(html);
                                }
                                if(userAddInfoVo.processDTOList){
                                    var processHtml = art('processTpl', data);
                                    $(".js-process-content").html(processHtml);
                                }
                                if(userAddInfoVo.pvgRoleVoList){
                                    var pvgRoleHtml = art('appRoleTpl', data);
                                    $(".js-role-content").html(pvgRoleHtml);
                                }
                            }else{
                                dg.fail(result.message);
                            }
                        });
            }

            //时间
            $doc
                    .off('click', '.clockpicker')
                    .on('click', '.clockpicker', function (e) {
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
            $doc
                    .off('blur', '.js-time-ico')
                    .on('blur', '.js-time-ico', function () {
                        var time = $.trim($(this).val()),
                            reg = /^((20|21|22|23|[0-1]\d)\:[0-5][0-9])(\:[0-5][0-9])?$/;
                        if (time.length && !reg.test(time)) {
                            dg.warn("您输入的时间不正确");
                        }
                    });

            //关闭弹窗
            $doc
                    .off('click', '.js-close')
                    .on('click', '.js-close', closeDialog);

            //保存
            $doc
                    .off('click', '.js-user-save')
                    .on('click', '.js-user-save', function () {
                        var params = fd.get('#userDialogData'),
                            pvgRoleIds = [],
                            errMsg = '';

                        check: {
                            if(!ck.check()){
                                break check;
                            }
                            //时间校验
                            var flag = true;
                            $('.js-time-ico').each(function () {
                                var time = $.trim($(this).val());
                                var reg = /^(?:20|21|22|23|[0-1]\d)(?:\:[0-5][0-9])(?:\:[0-5][0-9])?$/;
                                if (!reg.test(time)) {
                                    return (flag = false);
                                }
                            });
                            if (!flag) {
                                errMsg = '您输入的时间不正确';
                                break check;
                            }
                            $(":checkbox[class='pvgRoleId']").each(function () {
                                if ($(this).prop('checked') == true) {
                                    pvgRoleIds.push($(this).val());
                                }
                            });
                            if (!pvgRoleIds.length) {
                                errMsg = '请选择APP角色';
                                break check;
                            }
                        }

                        if (errMsg !== '') {
                            dg.warn(errMsg);
                            return;
                        }

                        var online = $('#job').is(':checked');
                        var workStatus = online ? 1 : 0;
                        params.pvgRoleIds = pvgRoleIds.join(',');
                        params.workStatus = workStatus;

                        var mobileParams = {
                            mobile: $('input[name="userMobile"]').val()
                        };
                        model.checkMobile(mobileParams)
                                .done(function (result) {
                                    if (result.success) {
                                        var boolean = result.data;
                                        if (boolean) {
                                            dg.confirm("此手机号存在于其他门店，是否继续添加？", function () {
                                                addUser(params);
                                            });
                                        } else {
                                            addUser(params);
                                        }
                                    } else {
                                        dg.fail(result.errorMsg);
                                    }
                                });
            });

            (function init() {
                _staffId = dg.open({
                    area: ['740px', 'auto'],
                    content: $('#addUserBox').html()
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

                // 初始化校验
                ck.init();

                $(".clockpicker").clockpicker();

                $('#userRoleId').val(args.data.userRoleId);

                setUserAddInfo();
            })();
        });
    }

</script>

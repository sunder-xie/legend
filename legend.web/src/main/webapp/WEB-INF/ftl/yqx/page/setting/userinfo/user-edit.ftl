<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/userinfo/user-edit.css?700454ac6bc97114acdb05d031199fa5"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/style/page/member/bootstrap-clockpicker.min.css"/>

<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">员工账号<small>－人员资料编辑</small></h3>
        </div>
        <input type="hidden" id="isAdminVal" value="${isAdminVal}"/>
        <div class="content" id="formData">
            <input type="hidden" name="managerId" id="managerId" value="${shopManagerInfo.managerId}"/>
            <input type="hidden" name="managerLoginId" id="managerLoginId" value="${shopManagerInfo.managerLoginId}"/>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    账号:
                </div>
                <div class="form-item w-210">
                    <input type="text" name="account" id="account" class="yqx-input yqx-input-small" value="${shopManagerInfo.account}" placeholder="" disabled data-v-type="required">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    密码:
                </div>
                <div class="form-item w-210">
                    <input type="password" name="password" id="password" class="yqx-input yqx-input-small" value="${shopManagerInfo.password}" placeholder="" <#if isAdminVal != 1> disabled="disabled" </#if> data-v-type="required">
                </div>
            <#if isAdminVal != 1 >
                <a class="yqx-btn yqx-btn-1 yqx-btn-small"  href="${BASE_PATH}/shop/member/change">修改密码</a>
            <#else>

            </#if>
                <span style="color:red;font-size: 12px;">注：密码长度6~12位,密码必须包含数字和字母，字母不区分大小写</span>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    姓名:
                </div>
                <div class="form-item">
                    <input type="text" name="name" id="name" class="yqx-input yqx-input-small" value="${shopManagerInfo.name}" placeholder="" data-v-type="required">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    联系电话:
                </div>
                <div class="form-item">
                    <input type="text" name="mobile" class="yqx-input yqx-input-small" value="${shopManagerInfo.mobile}" placeholder="" <#if isAdminVal != 1 || isAdmin == 1> disabled="disabled" </#if> data-v-type="required">
                    <input type="hidden" id="oldMobile" value="${shopManagerInfo.mobile}"/>
                </div>
            <#if isAdmin == 1><span style="color:red;font-size: 12px;">注：如需更换手机号，请联系客户修改相关信息</span></#if>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    岗位:
                </div>
                <div class="form-item">
                    <input type="hidden" name="roleId" value="${shopManagerInfo.roleId}">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-roles" value="${shopManagerInfo.rolesName}" placeholder="员工岗位" data-v-type="required" <#if isAdminVal != 1> disabled="disabled" </#if> >
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <#if isAdminVal != 0>
                    <a href="${BASE_PATH}/shop/setting/roles/roles-list" class="yqx-btn yqx-btn-2 yqx-btn-small">岗位管理</a>
                <#else>

                </#if>
            </div>
            <#--钣喷中心的-->
            <#if SESSION_SHOP_WORKSHOP_STATUS == 1 && BPSHARE == 'true'>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    工牌号:
                </div>
                <div class="form-item">
                    <input type="text" id="cardNum" class="yqx-input yqx-input-small form-control" data-ext-id="${shopManagerExtVO.id}" value="${shopManagerExtVO.cardNum}"  data-v-type="required" disabled>
                </div>
                <a class="yqx-btn yqx-btn-1 yqx-btn-small  js-dialog-pic"  href="javascript:;">工牌卡样式</a>
            </div>
            <div class="show-grid">
                <div class="form-label">
                    班组:
                </div>
                <div class="form-item">
                    <input type="hidden" name="teamId" value="${shopManagerExtVO.teamId}">
                    <input type="text" name="teamName" class="yqx-input yqx-input-small form-control js-team" data-ext-id="${shopManagerExtVO.teamId}" value="${shopManagerExtVO.teamName}" data-v-type="required" <#if isAdminVal != 1> disabled="disabled" </#if>>
                    <span class="fa icon-angle-down icon-small"></span>
                    <div class="yqx-select-options" style="width: 140px; display: none;">
                        <dl>
                            <#list teamVOs as item>
                            <dd class="yqx-select-option" data-key="${item.id}" <#if shopManagerExtVO.teamId == item.id> selected </#if> > ${item.name}</dd>
                            </#list>
                        </dl>
                    </div>
                </div>
                <a class="yqx-btn yqx-btn-1 yqx-btn-small"  href="${BASE_PATH}/workshop/team/listpage">管理班组</a>
            </div>
            <div class="show-grid">
                <div class="form-label">
                    工序:
                </div>
                <div class="form-item">
                    <input type="hidden" name="processId" value="${shopManagerExtVO.processId}">
                    <input type="text" name="processName" class="yqx-input yqx-input-small form-control js-process" data-ext-id="${shopManagerExtVO.processId}" value="${shopManagerExtVO.processName}" data-v-type="required" <#if isAdminVal != 1> disabled="disabled" </#if>>
                    <span class="fa icon-angle-down icon-small"></span>
                    <div class="yqx-select-options" style="width: 140px; display: none;">
                        <dl>
                        <#list processList as item>
                            <dd class="yqx-select-option" data-key="${item.id}" <#if shopManagerExtVO.processId == item.id> selected </#if>> ${item.name}</dd>
                        </#list>
                        </dl>
                    </div>
                </div>
                <span style="color:red;">注：快修组必填，事故组、快喷组员工可不填</span>
            </div>
            <div class="show-grid">
                <div class="form-label">

                </div>
                <div class="form-item">
                    <input type="checkbox" name="" value=""  id="job" <#if shopManagerExtVO.workStatus ==1> checked </#if> />在岗
                </div>
            </div>
            </#if>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    APP角色:
                </div>
                <div class="role-width">
                    <#list pvgRoleVoList as pvgRoleVo>
                        <input type="checkbox" class="pvgRoleId" value="${pvgRoleVo.id}"<#if isAdminVal != 1> disabled="disabled"</#if>
                            <#if pvgRoleVo.exist> checked</#if>/> ${pvgRoleVo.roleName}
                    </#list>
                </div>
            </div>
            <#if shopManagerInfo.isAdmin != 1>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    登录时间:
                </div>
                <div class="form-item login-time">
                    <div class="field_box clockpicker" data-placement="bottom" data-align="top" data-autoclose="true">
                        <input type="text" class="qxy_input time-ico js-time-ico" placeholder="选择开始时间" name="startTime" data-v-type="required" <#if isAdminVal != 1> disabled="disabled" </#if> value="<#if whiteAddress??> ${whiteAddress.loginBeginTime} <#else> 00:00:00 </#if>">
                    </div>
                    <span class="fa icon-calendar icon-small"></span>
                </div>
                至
                <div class="form-item login-time">
                    <div class="field_box clockpicker" data-placement="bottom" data-align="top" data-autoclose="true">
                        <input type="text" class="qxy_input time-ico js-time-ico" placeholder="选择结束时间" name="endTime" data-v-type="required" <#if isAdminVal != 1> disabled="disabled" </#if> value="<#if whiteAddress??> ${whiteAddress.loginEndTime} <#else> 23:59:59 </#if>">
                    </div>
                    <span class="fa icon-calendar icon-small"></span>
                </div>
            </div>
            </#if>
            <div class="form-label">
                <label>商家设备:</label>
            </div>
            <#if (managerDevices??)&&(managerDevices?size>0)>
                <#list managerDevices as device>
                    <span class="manager-device">${device.phoneBrand}</span>
                    <#if isAdminVal == 1><a href="javascript:;" class="delete js-delete" data-type="delete" data-id="${device.id}">解绑</a></#if>
                </#list>
            <#else >
                <div class="form-item">
                    无
                </div>
            </#if>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save">保存</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goBack">返回</button>
            </div>
        </div>
    </div>
</div>

<!--工牌打印-->
<script type="text/html" id="Badge">
    <div class="download-pattern">
        <div class="card-box" id="cardBox">
            <div class="car-number">${shopManagerExtVO.cardNum}</div>
            <div class="shape">
                <img src="${BASE_PATH}/pub/createImg?text=${shopManagerExtVO.cardNum}"/>
            </div>
            <div class="small-car-num">${shopManagerExtVO.cardNum}</div>
        </div>
    </div>
    <div class="card-wrapper">
        <div class="card-box">
            <div class="car-number">${shopManagerExtVO.cardNum}</div>
            <div class="shape">
                <img src="${BASE_PATH}/pub/createImg?text=${shopManagerExtVO.cardNum}"/>
            </div>
            <div class="small-car-num">${shopManagerExtVO.cardNum}</div>
        </div>
        <div class="card-tips">打印员工工牌时，请根据实际工牌大小，调整缩放比例</div>
        <div class="card-btns">
            <a href="javascript:;" download="工牌_${shopManagerExtVO.cardNum}.jpg" class="download-btn" id="cardDownload">下载</a>
        </div>
    </div>
</script>

<script src="${BASE_PATH}/static/js/page/setting/userinfo/user-edit.js?86f807cb64591c1747f7c648cef62933"></script>
<script src="${BASE_PATH}/resources/script/page/member/bootstrap-clockpicker.min.js?d25d9e41f486972994261114f443e372"></script>
<script src="${BASE_PATH}/static/js/page/setting/userinfo/html2canvas.min.js?4e89da462f4bed4e98ce1a76dbcdf75b"></script>
<#include "yqx/layout/footer.ftl">

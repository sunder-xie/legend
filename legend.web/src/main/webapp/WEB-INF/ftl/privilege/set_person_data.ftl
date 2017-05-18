<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/set-common.css?77d397fcba7a83c9bd59f8d7706a1e43"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/set_person_data.css?b25ab1c2ae095396c9822c74e19610d9"/>
<script type="text/javascript" src="${BASE_PATH}/resources/js/privilege/set_person_data.js?d2825628eb7b7600ec435c48ab5f3c6e"></script>
<script src="${BASE_PATH}/static/js/page/setting/userinfo/html2canvas.min.js?4e89da462f4bed4e98ce1a76dbcdf75b"></script>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/style/page/member/bootstrap-clockpicker.min.css"/>

<div class="wrapper">

    <div class="action-btn">
        <a class="btn_cmn btn_pink" name="update" id="update">保存</a>
        <a onclick="history.go(-1)" class="btn_cmn btn_pink">返回</a>
    </div>


    <div class="base-data">
        <div class="head">
            <span>个人资料</span>
        </div>
        <ul class="data-set" id="zcw-data">
            <input type="hidden" name="accountIdReg" id="accountIdReg" value="${shopManagerInfo.accountIdReg}"/>
            <input type="hidden" name="accountLoginIdReg" id="accountLoginIdReg" value="${shopManagerInfo.accountLoginIdReg}"/>
            <input type="hidden" name="isAdminVal" id="isAdminVal" value="${isAdminVal}"/>
            <li>
                <label>帐　　号: <span class="red-star">*</span></label>
                <input type="text" class="form-control" name="accountReg" id="accountReg" value="${shopManagerInfo.accountReg}" disabled="disabled"/>
            </li>
            <li>
                <label>密　　码: <span class="red-star">*</span></label>
                <input  class="form-control" <#if isAdminVal != 1> disabled="disabled" </#if> type="password" id ="passwordReg" value = "${shopManagerInfo.passwordReg}" autocomplete="off"/>
            <#if isAdminVal != 1 ><a class="btn_cmn btn_gray mar-l-20"  href="${BASE_PATH}/shop/member/change">修改密码</a><#else></#if><span style="color:red;font-size: 12px;">注：密码长度6~12位,密码必须包含数字和字母，字母不区分大小写</span>
            </li>
            <li>
                <label>姓　　名: <span class="red-star">*</span></label>
                <input type="text" class="form-control" name="nameReg" id="nameReg" value="${shopManagerInfo.nameReg}"/>
            </li>
            <li>
                <label>联系电话: <span class="red-star">*</span></label>
                <input type="text" class="form-control" name="mobileReg" id="mobileReg" value="${shopManagerInfo.mobileReg}" <#if isAdminVal != 1 || isAdmin == 1> disabled="disabled" </#if>/>
            <#if isAdmin == 1><span style="color:red;font-size: 12px;">注：如需更换手机号，请联系客户修改相关信息</span></#if>
                <input type="hidden" id="js-old-mobile" value="${shopManagerInfo.mobileReg}"/>
            </li>
        <#if shopManagerInfo.isAdminReg != 1>
            <li>
                <label>登录时间: <span class="red-star">*</span></label>
                <div class="field_box clockpicker" data-placement="bottom" data-align="top" data-autoclose="true">
                    <input type="text" class="qxy_input js-time-ico" placeholder="选择开始时间" name="startTime" v_type='{"required":true}' <#if isAdminVal != 1> disabled="disabled" </#if> value="<#if whiteAddress??> ${whiteAddress.loginBeginTime} <#else> 00:00:00 </#if>">
                </div>
                <div class="field_box clockpicker" data-placement="bottom" data-align="top" data-autoclose="true">
                    <input type="text" class="qxy_input js-time-ico" placeholder="选择结束时间" name="endTime" v_type='{"required":true}' <#if isAdminVal != 1> disabled="disabled" </#if> value="<#if whiteAddress??> ${whiteAddress.loginEndTime} <#else> 23:59:59 </#if>">
                </div>
            </li>
        </#if>
            <li>
                <label>岗　　位: <span class="red-star">*</span></label>
                <select name="rolesReg" id="rolesReg"   class="form-control" <#if isAdminVal != 1> disabled="disabled" </#if> >
                <#list rolesList as item>
                    <option value="${item.id}" <#if shopManagerInfo.rolesReg == item.id> selected </#if> >${item.name}</option>
                </#list>
                </select>

            <#if isAdminVal != 0><a class="btn_cmn btn_gray mar-l-20"  href="${BASE_PATH}/shop/setting/roles/roles-list">管理岗位</a><#else></#if>
            </li>
        <#if isUseWorkShop == true && isShare == true>
            <li>
                <label>工牌号: <span class="red-star">*</span></label>
                <input type="text" class="form-control" name="cardNum" id="cardNum" data-ext-id="${shopManagerExtVO.id}" value="${shopManagerExtVO.cardNum}" disabled="disabled"/>
                <a class="btn_cmn btn_gray mar-l-20 js-dialog-pic"  href="javascript:;">工牌卡样式</a>
            </li>
            <li>
                <label>班　　组:</label>
                <select name="teamId" id="teamId"   class="form-control" <#if isAdminVal != 1> disabled="disabled" </#if> >
                    <#list teamVOs as item>
                        <option value="${item.id}" <#if shopManagerExtVO.teamId == item.id> selected </#if> >${item.name}</option>
                    </#list>
                </select>
                <#if isAdminVal == 1><a class="btn_cmn btn_gray mar-l-20"  href="${BASE_PATH}/workshop/team/listpage">管理班组</a></#if>
            </li>
            <li>
                <label>工　　序:</label>
                <select name="processId" id="processId"   class="form-control" <#if isAdminVal != 1> disabled="disabled" </#if> >
                    <#list processList as item>
                        <option value="${item.id}" <#if shopManagerExtVO.processId == item.id> selected </#if> >${item.name}</option>
                    </#list>
                </select>
                <span style="color:red;">注：快修组必填，事故组、快喷组员工可不填</span>
            </li>
            <li>
                <input type="checkbox" name="" value=""  id="job"<#if shopManagerExtVO.workStatus ==1> checked</#if><#if isAdminVal != 1> disabled</#if>/> &nbsp;在岗
            </li>
        </#if>


            <li>
                <label>APP角色: <span class="red-star">*</span></label>
                <input type="checkbox" name="pvgRoleId" value="2" <#if isAdminVal != 1> disabled="disabled" </#if> <#list pvgUserOrgList as pvgUserOrg><#if pvgUserOrg.pvgRoleId == 2> checked </#if></#list>/>店长&nbsp;&nbsp;
                <input type="checkbox" name="pvgRoleId" value="3" <#if isAdminVal != 1> disabled="disabled" </#if> <#list pvgUserOrgList as pvgUserOrg><#if pvgUserOrg.pvgRoleId == 3> checked </#if></#list> />客服&nbsp;&nbsp;
                <input type="checkbox" name="pvgRoleId" value="4" <#if isAdminVal != 1> disabled="disabled" </#if> <#list pvgUserOrgList as pvgUserOrg><#if pvgUserOrg.pvgRoleId == 4> checked </#if></#list>/>仓管&nbsp;&nbsp;
                <input type="checkbox" name="pvgRoleId" value="5" <#if isAdminVal != 1> disabled="disabled" </#if> <#list pvgUserOrgList as pvgUserOrg><#if pvgUserOrg.pvgRoleId == 5> checked </#if></#list>/>技师&nbsp;&nbsp;
                <input type="checkbox" name="pvgRoleId" value="6" <#if isAdminVal != 1> disabled="disabled" </#if> <#list pvgUserOrgList as pvgUserOrg><#if pvgUserOrg.pvgRoleId == 6> checked </#if></#list>/>财务&nbsp;&nbsp;
            </li>
        <#if (managerDevices??)&&(managerDevices?size>0)>
            <#list managerDevices as device>
                <li data-id="${device.id}">
                    <label>商家设备:</label><span>${device.phoneBrand}</span>
                    <#if isAdminVal == 1><a href="javascript:;" class="delete js-delete" data-type="delete">解绑</a></#if>
                </li>
            </#list>
        <#else>
            <li>
                <label>商家设备:</label>无
            </li>
        </#if>
        </ul>
    </div>
</div>
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
<script src="${BASE_PATH}/resources/script/page/member/bootstrap-clockpicker.min.js?d25d9e41f486972994261114f443e372"></script>
<#include "layout/footer.ftl" >


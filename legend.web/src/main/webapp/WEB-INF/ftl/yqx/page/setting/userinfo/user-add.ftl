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
            <h3 class="headline">员工账号<small>－人员资料新增</small></h3>
        </div>
        <div class="content" id="formData">
            <div class="show-grid">
                <div class="form-label form-label-must">
                    账号:
                </div>
                <div class="form-item w-210">
                    <input type="text" name="preUserAccount" class="yqx-input yqx-input-small" value="${shopAbbr}" disabled data-v-type="required">
                </div>
                -
                <div class="form-item w-210">
                    <input type="text" name="userAccount" class="yqx-input yqx-input-small" data-v-type="required" autocomplete="new-password">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    密码:
                </div>
                <div class="form-item w-210">
                    <input type="password" name="userPassword" class="yqx-input yqx-input-small" placeholder="" data-v-type="required" autocomplete="new-password">
                </div>
                <span style="color:red;font-size: 12px;">注：密码长度6~12位,密码必须包含数字和字母，字母不区分大小写</span>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    姓名:
                </div>
                <div class="form-item">
                    <input type="text" name="userName" class="yqx-input yqx-input-small" placeholder="" data-v-type="required">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    联系电话:
                </div>
                <div class="form-item">
                    <input type="text" name="userMobile" class="yqx-input yqx-input-small" placeholder="" data-v-type="required">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    岗位:
                </div>
                <div class="form-item">
                    <input type="hidden" name="userRoleId" value="">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-roles" placeholder="员工岗位" data-v-type="required">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <a href="${BASE_PATH}/shop/setting/roles/roles-list" class="yqx-btn yqx-btn-2 yqx-btn-small">岗位管理</a>
            </div>
        <#--钣喷中心的-->
        <#if SESSION_SHOP_WORKSHOP_STATUS == 1 && BPSHARE == 'true'>
            <div class="show-grid">
                <div class="form-label">
                    班组:
                </div>
                <div class="form-item">
                    <input type="hidden" name="teamId" value="0">
                    <input type="text" name="teamName" class="yqx-input yqx-input-small form-control js-team" value="无" data-v-type="required">
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
                    <input type="hidden" name="processId" value="0">
                    <input type="text" name="processName" class="yqx-input yqx-input-small form-control js-process" value="无" data-v-type="required">
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
                        <input type="checkbox" class="pvgRoleId" value="${pvgRoleVo.id}"/> ${pvgRoleVo.roleName}
                    </#list>
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
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save">保存</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goBack">返回</button>
            </div>
        </div>
    </div>
</div>

<script src="${BASE_PATH}/static/js/page/setting/userinfo/user-add.js?c6b9dc450394661ab0a555cd6c4cf8c4"></script>
<script src="${BASE_PATH}/resources/script/page/member/bootstrap-clockpicker.min.js?d25d9e41f486972994261114f443e372"></script>

<#include "yqx/layout/footer.ftl">

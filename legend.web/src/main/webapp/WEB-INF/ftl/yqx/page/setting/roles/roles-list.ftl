<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/roles/roles-list.css?40cdf4a6a61b6c7873dddc8c538aaeb7"/>
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
            <h3 class="headline">岗位管理</h3>
        </div>
        <div class="content clearfix">
            <div class="left-aside fl">
                <div class="left-treeview" id="treeViewWrap"></div>
            </div>
            <div class="right-con fl">
                <div class="right-con-inner">
                    <div class="title">
                        <p class="fl">员工信息</p>
                        <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-add-user">新增员工</button>
                    </div>
                    <table class="yqx-table staff-table">
                        <thead>
                        <tr>
                            <th class="text-l">员工名称</th>
                            <th class="text-l">账号</th>
                            <th class="text-r">操作</th>
                        </tr>
                        </thead>
                        <tbody id="rolesUserInfo"></tbody>
                    </table>
                    <div class="title">
                        <p class="fl">岗位权限</p>
                        <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-save-perm">保存</button>
                    </div>
                    <ul class="permission" id="allFunc"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<#-- TreeView 角色列表模板 -->
<script type="text/html" id="rolesTpl">
    <div class="clearfix treeview-item-box js-treeview-item js-treeview-level-<%= level %>" data-level="<%= level %>"
         data-id="<%= templateData.id %>" data-parent-id="<%= templateData.parentId %>"
         data-level-id="<%= templateData.levelId %>" data-pvg-role-id="<%= templateData.pvgRoleId %>">
        <div class="treeview-content fl js-treeview-content js-show-tips" title="<%= templateData.name %>"><%= templateData.name %></div>
        <% if (level !== 1) { %>
        <div class="treeview-setting js-treeview-setting">设置 <i class="icon-angle-down"></i></div>
        <% } %>
    </div>
</script>

<#-- 员工信息模板 -->
<script type="text/html" id="rolesUserInfoTpl">
    <% if (data && data.length) { %>
    <% for (var i = 0; i < data.length; i++) { %>
    <% var item = data[i]; %>
    <tr class="js-change" data-manager-id="<%=item.accountIdReg%>">
        <td class="text-l"><%= item.nameReg %></td>
        <td class="text-l"><%= item.accountReg %></td>
        <td class="text-r">
            <a href="javascript:;" class="yqx-link-3">换岗</a>
            <% if (item.isAdminReg !== 1) { %>
            <a href="javascript:;" class="yqx-link-2 js-del"
               data-id="<%= item.accountIdReg %>" data-roles-id="<%= item.rolesReg %>">删除</a>
            <% } %>
        </td>
    </tr>
    <% } } %>
</script>

<#-- 岗位权限信息模板 -->
<script type="text/html" id="allFuncTpl">
    <% if (data && data.length) { %>
    <li class="all">
        <label><input type="checkbox" class="js-select-all"/>全选</label>
    </li>
    <% for (var i = 0; i < data.length; i++) { %>
    <% var item = data[i]; %>
    <li>
        <div>
            <input type="checkbox" class="js-select-item" value="<%= item.id %>" <%= item.rolesId != null ? 'checked' : '' %>/>
            <% if (item.funcFList && item.funcFList.length) { %>
            <span class="list-control js-list-control"><%= item.name %> <i class="js-icon-change icon-angle-down"></i></span>
            <% } else { %>
            <span class="list-control"><%= item.name %></span>
            <% } %>
        </div>
        <% if (item.funcFList && item.funcFList.length) { %>
        <ul class="permission hide">
            <% for (var j = 0; j < item.funcFList.length; j++) { %>
            <% var subItem = item.funcFList[j]; %>
            <li>
                <input type="checkbox" class="js-select-sub-item" value="<%= subItem.id %>" <%= subItem.rolesId != null ? 'checked' : '' %>/>
                <span><%= subItem.name %></span>
            </li>
            <% } } %>
        </ul>
    </li>
    <% } } %>
</script>

<#-- 添加下级岗位弹窗模板 -->
<script type="text/html" id="addRolesDialogTpl">
    <dg-title><%= title %></dg-title>
    <dg-body>
        <div class="dialog-content-1" id="addDialog">
            <div class="form-row dialog-form-row">
                <div class="form-label">岗位名称</div><div
                    class="form-item dialog-roles-name">
                    <input type="text" class="yqx-input yqx-input-small" name="name" placeholder="请输入岗位名称">
                </div>
                <input type="hidden" name="level" value="<%= level %>">
                <input type="hidden" name="parentId" value="<%= parentId %>">
                <input type="hidden" name="pvgRoleId" value="<%= pvgRoleId %>">
            </div>
            <div class="form-row dialog-form-row text-center">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-add-roles-ok">确认</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-add-roles-cancel">取消</button>
            </div>
        </div>
    </dg-body>
</script>

<#-- 添加员工功能 -->
<#include "yqx/tpl/setting/add-user-tpl.ftl">
<#-- 删除员工功能 -->
<#include "yqx/tpl/setting/delete-staff-tpl.ftl">
<script src="${BASE_PATH}/static/js/page/setting/role/roles-list.js?abdf57326048d8cbbd19d8429606cac4"></script>
<#include "yqx/layout/footer.ftl">

<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/userinfo/user-list.css?01935f9b2af413a41a5469c21f535740"/>
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
            <h3 class="headline">员工账号</h3>
        </div>
        <div class="content">
            <div class="form-box" id="formData">
                <div class="form-item">
                    <input type="text" name="keywords" class="yqx-input yqx-input-small" value="" placeholder="员工姓名/联系电话">
                </div>
                <div class="form-item">
                    <input type="hidden" value="" name="roleId"/>
                    <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-roles" value="" placeholder="员工岗位">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-add-user">新增员工</button>
            </div>
            <!-- 表格容器 start -->
            <div class="table-con">
                <table class="yqx-table yqx-table-hover yqx-table-link">
                    <thead>
                    <tr>
                        <th class="text-l p-left" width="100">员工姓名</th>
                        <th class="text-l" width="100">联系电话</th>
                        <th class="text-l" width="100">岗位</th>
                        <th class="text-l" width="150">账号</th>
                        <th class="text-l" width="150">登录时间</th>
                        <th class="text-l" width="100">App角色</th>
                        <th class="text-c">操作</th>
                    </tr>
                    </thead>
                    <tbody id="tableListCon">
                    </tbody>
                </table>
            </div>
            <!-- 表格容器 end -->

            <!-- 分页容器 start -->
            <div class="yqx-page" id="pagingCon"></div>
            <!-- 分页容器 end -->
        </div>
    </div>
</div>

<script type="text/html" id="tableTpl">
    <%if(json.success && json.data){%>
    <%for(var i=0; i< json.data.content.length; i++){%>
    <%var item = json.data.content[i]%>
    <tr data-id="<%=item.id%>" data-roles-id="<%=item.roleId%>" class="js-edit-link">
        <td class="text-l p-left js-show-tips ellipsis-1" width="82"><%=item.name%></td>
        <td class="text-l js-show-tips ellipsis-1" width="102"><%=item.mobile%></td>
        <td class="text-l js-show-tips ellipsis-1" width="55"><%=item.rolesName%></td>
        <td class="text-l js-show-tips ellipsis-1" width="112"><%=item.account%></td>
        <td class="text-l js-show-tips ellipsis-1" width="150"><%=item.loginBeginTime%> - <%=item.loginEndTime%></td>
        <td class="text-l js-show-tips ellipsis-1" width="200"><%=item.appRoles%></td>
        <td class="text-c">
            <% if( item.isAdmin == 1){%>
            <a href="javascript:;" class="yqx-link-3 js-edit">编辑</a>
            <%}else{%>
            <a href="javascript:;" class="yqx-link-3 js-edit">编辑</a>
            <a href="javascript:;" class="yqx-link-2 js-delete">删除</a>
            <%}%>
        </td>
    </tr>
    <%}}%>
</script>

<#-- 员工删除功能 -->
<#include "yqx/tpl/setting/delete-staff-tpl.ftl">

<script src="${BASE_PATH}/static/js/page/setting/userinfo/user-list.js?ba3093a1bb48d9ec50e72b70cc951548"></script>
<#include "yqx/layout/footer.ftl">

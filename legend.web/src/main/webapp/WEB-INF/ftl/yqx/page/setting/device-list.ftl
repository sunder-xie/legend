<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/device-list.css?f36cebdff23801bc293815789d552937"/>
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
            <h3 class="headline">安全登录设置<small>－App授权设置</small></h3>
        </div>
        <div class="content">
            <div class="form-box" id="formData">
                <div class="form-item">
                    <input type="text" name="keyword" class="yqx-input yqx-input-small" value="" placeholder="员工姓名/联系电话">
                </div>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-state" value="" placeholder="状态">
                    <input type="hidden" name="status" value=""/>
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
            </div>
            <!-- 表格容器 start -->
            <div class="table-con">
                <table class="yqx-table yqx-table-hover yqx-table-link">
                    <thead>
                    <tr>
                        <th class="text-l p-left">员工姓名</th>
                        <th class="text-l">联系电话</th>
                        <th class="text-l">设备ID</th>
                        <th class="text-l" width="50">状态</th>
                        <th class="text-c">操作</th>
                    </tr>
                    </thead>
                    <tbody id="tableListCon">
                    </tbody>
                </table>

                <!-- 分页容器 start -->
                <div class="yqx-page" id="pagingCon"></div>
                <!-- 分页容器 end -->
            </div>
            <!-- 表格容器 end -->
            <button class="yqx-btn yqx-btn-1 yqx-btn-small back js-goBack fr">返回</button>
        </div>
    </div>
</div>

<script type="text/html" id="tableTpl">
    <%if(json.data && json.data.content){%>
    <%for(var i=0;i < json.data.content.length;i++){%>
    <%var item = json.data.content[i];%>
    <tr data-id="<%= item.id%>">
        <td class="text-l js-show-tips ellipsis-1 p-left"><%= item.managerName%></td>
        <td class="text-l js-show-tips ellipsis-1"><%= item.managerMobile%></td>
        <td class="text-l js-show-tips ellipsis-1"><%= item.phoneBrand%> <%= item.deviceId%></td>
        <td class="text-l js-show-tips ellipsis-1 w-150"><%= item.authorizeStatuName%></td>
        <td class="text-c">
            <%if(item.authorizeStatus == 0){%>
            <a href="javascript:;" class="yqx-link-3 js-delete" data-type="agree">同意授权</a>
            <a href="javascript:;" class="yqx-link-2 js-delete" data-type="refuse">拒绝</a>
            <%}else if(item.authorizeStatus == 1){%>
            <a href="javascript:;" class="yqx-link-2 js-delete" data-type="delete">解绑</a>
            <%}%>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>

<script src="${BASE_PATH}/static/js/page/setting/device-list.js?7164827904381d213d97bf86ba7a9535"></script>
<#include "yqx/layout/footer.ftl">

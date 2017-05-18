<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/team-list.css?d27353a2fc13189083c575cf79ca6e5e"/>
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <div class="order-right fl">
        <!-- group start -->
        <div class="yqx-group">
            <!-- group标题 start -->
            <div class="yqx-group-head">
                班组管理
                <i class="group-head-line"></i>
            </div>
            <!-- group标题 end -->
            <!-- group内容 start -->
            <div class="yqx-group-content">
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-addteam">添加班组</button>
                <div class="team-table" id="teamTable">

                </div>
            </div>
        </div>
    </div>
</div>

<!--添加班组列表-->
<script type="text/html" id="teamTableTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>序号</th>
            <th class="text-l">名称</th>
            <th class="text-l">成员</th>
            <th class="text-l">备注</th>
            <th class="text-l">操作</th>
        </tr>
        </thead>
        <%if(json.data){%>
        <%for(var i=0;i<json.data.length;i++){%>
        <%var item = json.data[i]%>
        <tr>
            <td><%=i+1%></td>
            <td class="text-l max-width js-show-tips"><%=item.name%></td>
            <td class="text-l max-width js-show-tips">
                <%if(item.shopManagerExtVOList){%>
                    <%for(var j=0;j<item.shopManagerExtVOList.length;j++){%>
                     <%var subItem=item.shopManagerExtVOList[j]%>
                    <span><%=subItem.managerName%></span>
                     <%}%>
                <%}%>
            </td>
            <td class="text-l max-width js-show-tips"><%=item.remark%></td>
            <td class="text-l">
                <a href="javascript:;" class="edit js-edit" data-id="<%=item.id%>">编辑</a>
                <a href="javascript:;" class="delete js-delete"  data-id="<%=item.id%>" data-shop-id="<%=item.shopId%>">删除</a>
            </td>
        </tr>
        <%}}%>
</script>

<!--删除班组-->
<script type="text/html" id="deleteDialog">
<div class="dialog">
    <div class="dialog-title">
        提示
    </div>
    <div class="dialog-con">
        <div class="inquiry">你确定要删除班组吗？</div>
        <button class="yqx-btn yqx-btn-1 yqx-btn-small js-list-delete">删除</button>
        <button class="yqx-btn yqx-btn-1 yqx-btn-small js-chanel">取消</button>
    </div>
</div>
</script>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/workshop/team-list.js?75af3b76dbd86db516d4ae02b9696b45"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
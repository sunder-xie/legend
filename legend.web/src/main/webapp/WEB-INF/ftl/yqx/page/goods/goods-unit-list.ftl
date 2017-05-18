<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/goods/goods-unit-list.css?2661744b27389e147510972f4a0c5548"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h3 class="headline fl">配件资料<small> — 配件单位管理</small></h3>
        </div>
        <div class="content">
            <div class="form-box" id="formData">
                <div class="form-item w-220">
                    <input type="text" name="nameLike" class="yqx-input yqx-input-small" value="" placeholder="单位">
                </div>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-batch-delete">批量删除</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-delete-all fr">全部删除</button>
            </div>
            <!-- 表格容器 start -->
            <div class="table-con" >
                <table class="yqx-table" id="tableTest">
                    <thead>
                    <tr>
                        <th width="40"><input type="checkbox" class="js-checkbox-all"/></th>
                        <th class="text-l" width="700">单位名称</th>
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
            <a href="${BASE_PATH}/shop/goods/goods-list" class="fr yqx-btn yqx-btn-1">返回</a>
        </div>
    </div>
</div>

<!-- 表格数据模板 start -->
<script type="text/template" id="tableTpl">
    <%if(json.success && json.data.content.length>0){%>
    <%for(var i=0; i< json.data.content.length;i++){%>
    <%var item = json.data.content[i]%>
    <tr>
        <td>
            <input type="checkbox" class="js-checkbox" data-id="<%= item.id%>"/>
        </td>
        <td class="text-l" width="700">
            <div class="form-label">
                <%=item.name%>
            </div>
        </td>
        <td class="text-c">
            <a href="javascript:;" class="yqx-link-2 js-delete" data-id="<%=item.id%>">删除</a>
        </td>
    </tr>
    <%}%>
    <%} else{%>
    <tr>
        <td colspan="6">暂无数据！</td>
    </tr>
    <%}%>
</script>

<script src="${BASE_PATH}/static/js/page/setting/goods/goods-unit-list.js?9f8df1f8c77d4baa662616fde337fb49"></script>
<#include "yqx/layout/footer.ftl">

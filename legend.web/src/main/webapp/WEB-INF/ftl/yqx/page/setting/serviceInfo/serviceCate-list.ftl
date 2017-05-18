<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/serviceInfo/serviceCate-list.css?2661744b27389e147510972f4a0c5548"/>
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
            <h3 class="headline fl">服务资料<small> — 服务类别管理</small></h3>
            <button class="yqx-btn yqx-btn-2 yqx-btn-small fr m-top js-new-type">+新建分类</button>
        </div>
        <div class="content">
            <div class="form-box" id="formData">
                <div class="form-item w-220">
                    <input type="text" name="nameLike" class="yqx-input yqx-input-small" value="" placeholder="类别名称">
                </div>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
            </div>
            <!-- 表格容器 start -->
            <div class="table-con" >
                <table class="yqx-table" id="tableTest">
                    <thead>
                    <tr>
                        <th class="text-l" width="740">类别名称</th>
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
            <a href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-list" class="fr yqx-btn yqx-btn-1">返回</a>
        </div>
    </div>
</div>

<!-- 表格数据模板 start -->
<script type="text/template" id="tableTpl">
    <%if(json.success && !!json.data.content && !!json.data.content.length){%>
    <%for(var i=0; i< json.data.content.length; i++){%>
    <%var item = json.data.content[i]%>
    <tr data-id="<%=item.id%>">
        <input type="hidden" name="id" value="<%=item.id%>">
        <td class="text-l" width="740">
            <div class="form-label">
                <%=item.name%>
            </div>
            <div class="form-item js-form-item hide">
                <input type="text" name="name" class="yqx-input yqx-input-small js-type-name" value="<%=item.name%>" placeholder="" data-v-type="required">
            </div>
        </td>
        <td class="text-c">
            <a href="javascript:;" class="yqx-link-3 js-edit">编辑</a>
            <a href="javascript:;" class="yqx-link-2 js-delete">删除</a>
        </td>
    </tr>
    <%}%>
    <%} else{%>
    <tr>
        <td colspan="6">暂无数据！</td>
    </tr>
    <%}%>
</script>

<script type="text/html" id="newTypeDialog">
    <div class="yqx-dialog">
        <div class="yqx-dialog-header">
            <div class="yqx-dialog-headline">新建服务类别</div>
        </div>
        <div class="yqx-dialog-body js-shopServiceCate-form">
            <div class="form-label">
                类别名称
            </div>
            <div class="form-item">
                <input type="text" name="name" class="yqx-input yqx-input-small" value="" placeholder="" data-v-type="required">
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-confirm">确定</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-cancel">取消</button>
            </div>
        </div>
    </div>
</script>

<script src="${BASE_PATH}/static/js/page/setting/serviceInfo/serviceCate-list.js?7a0ffecb295d7e70277fd7196a3e4846"></script>
<#include "yqx/layout/footer.ftl">

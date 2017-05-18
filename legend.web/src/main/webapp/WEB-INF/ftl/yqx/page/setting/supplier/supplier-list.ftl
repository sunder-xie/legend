<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/supplier/supplier-list.css?b837a523cd4c8fa0567d7c773b621afa"/>
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
            <h3 class="headline">供应商资料</h3>
        </div>
        <div class="form-box" id="formData">
            <div class="show-grid">
                <div class="form-item supplier-text-width">
                    <input type="text" name="conditionLike" class="yqx-input yqx-input-small" value="" placeholder="供应商名称、联系电话、供应商编号、主营业务">
                </div>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-category" value="" placeholder="供应商类别">
                    <input type="hidden" name="category" value=""/>
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-payway" value="" placeholder="付款方式">
                    <input type="hidden" name="payMethod" value=""/>
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-type" value="" placeholder="开票类型">
                    <input type="hidden" name="invoiceType" value=""/>
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn" >查询</button>
            </div>
        </div>
        <div class="btn-box">
            <button class="yqx-btn yqx-btn-4 js-add-supplier">添加供应商资料</button>
            <button class="yqx-btn yqx-btn-3 js-merge">合并供应商</button>
        </div>
        <!-- 表格容器 start -->
        <div id="tableCon" class="table-con"></div>
        <!-- 表格容器 end -->

        <!-- 分页容器 start -->
        <div class="yqx-page" id="pagingCon"></div>
        <!-- 分页容器 end -->
    </div>

</div>

<!-- 表格数据模板 start -->
<script type="text/template" id="tableTpl">
    <table class="yqx-table yqx-table-hover yqx-table-link" id="tableTest">
        <thead>
        <tr data-id="10">
            <th class="text-l" width="290">供应商</th>
            <th class="text-l" width="140">供应商编号</th>
            <th class="text-l" width="90">分类</th>
            <th class="text-l" width="107">付款方式</th>
            <th class="text-l" width="110">开票类型</th>
            <th class="text-c" width="80">操作</th>
        </tr>
        </thead>
        <%if(json.data && json.data.content){%>
        <%for(var i=0;i < json.data.content.length;i++){%>
        <%var item = json.data.content[i];%>
        <tr data-id="<%= item.id%>" class="js-edit-tr">
            <td class="text-l js-show-tips ellipsis-1" width="290"><%= item.supplierName%></td>
            <td class="text-l" width="140"><%= item.supplierSn%></td>
            <td class="text-l" width="90"><%= item.categoryName%></td>
            <td class="text-l" width="107"><%= item.paymentMode%></td>
            <td class="text-l" width="110"><%= item.invoiceTypeName%></td>
            <td class="text-c" width="80">
                <a href="javascript:;" class="yqx-link-3 js-edit">编辑</a>
                <a href="javascript:;" class="yqx-link-2 js-delete">删除</a>
            </td>
        </tr>
        <%}%>
        <%}%>
    </table>
</script>

<script type="text/html" id="mergeTpl">
    <div class="yqx-dialog">
        <div class="dialog-title">合并供应商</div>
        <div class="yqx-dialog-body" id="dialogRequired">
            <div class="show-grid">
                <div class="form-label">
                    把供应商
                </div>
                <div class="form-item dialog-supplier">
                    <input type="text"  class="yqx-input yqx-input-icon yqx-input-small js-supplier-request" value="" placeholder="请选择供应商" data-v-type="required">
                    <input type="hidden" name="requestId"/>
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
                <div class="form-label">
                    合并到供应商
                </div>
                <div class="form-item dialog-supplier">
                    <input type="text"  class="yqx-input yqx-input-icon yqx-input-small js-supplier-result" value="" placeholder="请选择供应商" data-v-type="required">
                    <input type="hidden" name="resultId" data-v/>
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </div>
            <div class="merge-text">
                注意:<p>合并供应商后，供应商的付款记录，入库记录都将要合并；确认后将无法拆分合并的供应商，谨慎操作哦</p>
            </div>

            <div class="dialog-btn">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-merge-save">确认</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-remove">取消</button>
            </div>
        </div>
    </div>
</script>
<script src="${BASE_PATH}/static/js/page/setting/supplier/supplier-list.js?03a9a4e5e0a8776bde5e8d3768b7ba49"></script>
<#include "yqx/layout/footer.ftl">
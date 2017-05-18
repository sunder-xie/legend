<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/stock/stock-check.css"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <!-- 标题 start -->
        <div class="order-list-title clearfix">
            <h3 class="headline fl">库存盘点</h3>
        </div>
        <!-- 标题 end -->
        <!-- 查询条件 start -->
        <div class="form-box" id="listForm">
            <div class="show-grid">
                <div class="form-item">
                    <input type="text" class="yqx-input"
                           placeholder="盘点开始日期">
                    <i class="fa icon-calendar"></i>
                    <input type="hidden"
                           name="supplierId">
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input"
                           placeholder="盘点结束日期">
                    <i class="fa icon-calendar"></i>
                    <input type="hidden" name="purchaseAgent">
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input"
                           placeholder="请输入盘点单号">
                </div>
            </div>
        </div>
    </div>
    <div class="list-tab-box">
        <div class="list-tab current-tab">已保存
        </div><div class="list-tab">已盘点
    </div><div class="list-tab">全部
    </div>
        <div class="tools-bar clearfix fr">
            <a href="javascript:;" class="yqx-link export-excel"><i
                    class="fa icon-plus"></i>盘点单</a>
        </div>
    </div>
    <!-- 查询条件 end -->

    <!-- 数据列表>>表格 start -->
    <div class="order-list-table">
        <table class="yqx-table">
            <thead>
            <tr>
                <th>序号</th>
                <th>盘点单号</th>
                <th>盘点日期</th>
                <th>配件数</th>
                <th>盘亏数量</th>
                <th>盘亏金额</th>
                <th>盘盈数量</th>
                <th>盘盈金额</th>
                <th>开单人</th>
                <th>盘点人</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="listFill">
            </tbody>
        </table>
        <div class="yqx-page" id="listPgae">
        </div>
    </div>
    <!-- 数据列表>>表格  end -->

</div>
<!-- 右侧内容区 end -->
<!-- 表格模板 start -->
<script type="text/html" id="listTpl">
    <% var data = json.data.content;%>
    <%for(var i in data) {%>
    <tr>
        <td><%=i+1%></td>
        <td><%=data[i].%></td>
        <td><%=data[i].%></td>
        <td><%=data[i].%></td>
        <td><%=data[i].%></td>
        <td class="font-money"><%='&yen;' + data[i].%></td>
        <td><%=data[i].%></td>
        <td class="font-money"><%='&yen;' + data[i].%></td>
        <td><%=data[i].%></td>
        <td><%=data[i].%></td>
        <td><%=data[i].%></td>
        <td>
            <button class="js-edit edit-btn">编辑</button><button
                class="js-edit del-btn">删除</button><button
                    class="js-edit view-btn">查看</button>
        </td>
    </tr>
    <%}%>
</script>
<!-- 表格模板 end -->

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/warehouse/stock/stock-check.js"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
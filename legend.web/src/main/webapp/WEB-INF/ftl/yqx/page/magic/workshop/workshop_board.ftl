<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/workshop-board.css?1596be4abf5849f95449d15250c00a4e"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h3 class="headline fl">施工作业扫描</h3>
        </div>
        <div class="content">
            <div class="title">生产进度看板</div>
            <div class="table-box" id="tableCon">

            </div>
            <div class="explain">
                <div class="box explain-box blue"></div>已完工
                <div class="box explain-box green"></div>施工
                <div class="box explain-box grey"></div>待施工
                <div class="box explain-box red"></div>超时
                <div class="box explain-box yellow"></div>中断
            </div>
        </div>

    </div>
</div>

<script type="text/html" id="tableTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>车牌号</th>
            <th>生产线</th>
            <th>计划完工时间</th>
            <th>状态</th>
            <th>钣金</th>
            <th>做底</th>
            <th>中涂</th>
            <th>喷漆</th>
            <th>组装</th>
            <th>抛光</th>
        </tr>
        </thead>
        <tbody>
        <%for(var i=0;i<7;i++){%>
        <tr>
            <td>浙A12345</td>
            <td>快修线</td>
            <td>2016-4-13 16:30</td>
            <td>做底 施工中 张三</td>
            <td><div class="box blue"></div></td>
            <td><div class="box green"></div></td>
            <td><div class="box grey"></div></td>
            <td><div class="box grey"></div></td>
            <td><div class="box red"></div></td>
            <td><div class="box yellow"></div></td>
        </tr>
        <%}%>
        </tbody>
    </table>
</script>



<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/workshop/workshop-board.js?3726d319b39aaf35b6425a0731efe3d9"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
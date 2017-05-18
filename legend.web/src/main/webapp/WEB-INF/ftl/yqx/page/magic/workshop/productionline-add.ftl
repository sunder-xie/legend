<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/productionLineprocess-add.css?669f5d034792085364f7ebdd9053ddd6"/>
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->
    <div class="order-right fl">
        <div class="title">
            生产线管理<span>-新增生产线</span>
        </div>
        <div class="content">
            <div class="show-grid">
                <div class="form-label">
                    名称：
                </div>
                <div class="form-item name-width">
                    <input type="text" name="lineName" class="yqx-input yqx-input-small " value="" placeholder="">
                </div>
                <div class="form-label">
                    类型：
                </div>
                <div class="form-item type-width">
                    <input type="hidden" value="" class="show-key">
                    <input type="text" name="type" class="yqx-input yqx-input-small js-type-select" value="" placeholder="请选择">
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label">
                    备注：
                </div>
                <div class="form-item remarks-width">
                    <input type="text" name="remark" class="yqx-input yqx-input-small" value="" placeholder="">
                </div>
            </div>
            <!--表格数据-->
            <div id="tbaleCon"></div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-save">保存</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
            </div>
        </div>

    </div>
</div>

<!-- 表格数据模板 start -->
<script type="text/template" id="TableTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th></th>
            <th>工序号</th>
            <th>工序名称</th>
            <th>工时（分钟）</th>
            <th>工序牌样式</th>
        </tr>
        </thead>
        <%if(json.data){%>
        <%for(var i=0; i<json.data.length;i++){%>
        <%var item = json.data[i]%>
        <tr class="table-list">
            <input type="hidden" value="<%=item.name%>" class="name"/>
            <input type="hidden" value="<%=item.processId%>" class="processId"/>
            <input type="hidden" value="<%=item.processSort%>" class="processSort"/>
            <input type="hidden" value="<%=item.isDeleted%>" class="isDeleted"/>
            <td>
                <input type="checkbox" name="" class="js-check" checked>
            </td>
            <td class="barCode"><%=item.barCode%></td>
            <td class="processName"><%=item.processName%></td>
            <td><input type="text" value="<%=item.workTime%>" class="time-hour"/></td>
            <td>
                <a href="javascript:;" class="color-green js-card-pic">查看图片</a>
            </td>
        </tr>
        <%}}%>
    </table>
</script>

<!--查看图片模板-->
<script type="text/html" id="cardPic">
    <img src="<%=cardUrl%>" class="card-photo"/>
    <div class="download-box">
        <a href="<%=cardUrl%>" class="yqx-btn yqx-btn-3 yqx-btn-small js-download" download="工牌.jpg">下载</a>
    </div>
</script>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/workshop/productionLineprocess-add.js?6bb50c9045060e8476267cfbfc2a5582"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
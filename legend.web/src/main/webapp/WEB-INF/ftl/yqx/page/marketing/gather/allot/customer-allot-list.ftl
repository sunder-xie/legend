<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/allot/allot-common.css?59e8db8de7ecdcc00ce4f3de8c5464aa"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/allot/customer-allot-list.css?6009a5f899f6a9034dd23bfa7fbf972b"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="aside fl">
    <#include "yqx/page/marketing/left-nav.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="aside-main fr">
        <!-- 标题 start -->
        <h3 class="headline">客户归属</h3>
        <div class="form-box" id="formData">
            <div class="form-item w-110">
                <input type="text" name="search_carLicense"
                       class="yqx-input yqx-input-small" value="" placeholder="车牌">
            </div>
            <div class="form-item w-165">
                <input type="text" name="search_mobile"
                       class="yqx-input yqx-input-small" value="" placeholder="车主电话">
            </div>
            <div class="form-item w-110">
                <input type="text" name="search_customerName"
                       class="yqx-input yqx-input-small" value="" placeholder="车主">
            </div>
            <div class="form-item w-130">
                <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-select-staff" value="" placeholder="员工">
                <input type="hidden" name="search_userIds" value="${userId}">
                <span class="fa icon-angle-down icon-small"></span>
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
        </div>
        <div class="btn-box">
            <button class="yqx-btn yqx-btn-4 yqx-btn-small js-re-bind">调整</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-off-bind">解绑</button>
        </div>
        <div class="table-box">
            <div class="table-con" id="tableCon"></div>
            <!-- 分页容器 start -->
            <div class="yqx-page" id="paging"></div>
            <!-- 分页容器 end -->
            <div class="btn-box-bot clearfix">
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-back">返回</button>
            </div>
        </div>
    </div>

</div>
<!-- 右侧内容区 end -->
</div>
<!-- 表格模板 start -->
<script type="text/html" id="tableTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th class="text-c width-30">
                <input type="checkbox"
                   <%if(json.data && json.data.content && isSelect('all', json.data.content) ) {%><%='checked'%><%}%>
                   class="js-all-select">
            </th>
            <th class="text-l width-40">序号</th>
            <th class="text-l js-show-tips">车主</th>
            <th class="text-l">车主电话</th>
            <th class="text-l">车牌</th>
            <th class="text-l js-show-tips">员工</th>
            <th class="text-c op">操作</th>
        </tr>
        </thead>
        <tbody>
        <% if(json.data && json.data.content.length) {%>
        <%for(var i in json.data.content){%>
        <%var item = json.data.content[i];%>
        <tr>
            <td class="text-c">
                <input type="checkbox" class="js-select"
                <%if(isSelect(item.customerCarId) ) {%><%='checked'%><%}%>
                       data-user-id="<%=item.userId%>"
                       data-customer-car-id="<%=item.customerCarId%>">
            </td>
            <td class="text-l"><%=json.data.number * json.data.size + (+i + 1)%></td>
            <td class="text-l js-show-tips"><%=item.customerName%></td>
            <td class="text-l"><%=item.mobile%></td>
            <td class="text-l license"><%=item.carLicense%></td>
            <td class="text-l name js-show-tips"><%=item.userName%></td>
            <td class="text-c">
                <a href="javascript:;" class="yqx-link-1 js-t-re-bind"
                   data-id="<%=item.customerCarId%>" data-user-id="<%=item.userId%>">调整</a>
                <%if(item.userId >0){%>
                <a href="javascript:;" class="yqx-link-2 js-t-off-bind"
                   data-user-id="<%=item.userId%>"
                   data-id="<%=item.customerCarId%>">解绑</a>
                <%}%>
            </td>
        </tr>
        <%}%>
        <%} else {%>
        <tr>
            <td colspan="7">暂无数据</td>
        </tr>
        <%}%>
        </tbody>
    </table>
</script>
<div class="yqx-dialog choose-staff-dialog hide">
    <div class="dialog-title">客户归属调整</div>
    <div class="dialog-content">
        <div class="clearfix">
            <label class="col-2" style="line-height: 30px;">请选择员工</label>

            <div class="form-item col-5">
                <input class="yqx-input enter-name js-staff-name"
                       placeholder="员工名字">
            </div>
        </div>
        <ul class="clearfix staff-list">
        </ul>
    </div>
    <div class="dialog-footer">
        <button class="js-cancel yqx-btn yqx-btn-1">取消</button><button
            style="margin-left: 10px;"
            class="js-confirm yqx-btn yqx-btn-3">确定</button>
    </div>
</div>
<!-- 表格模板 end -->
<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/marketing/gather/allot/customer-allot-list.js?2437ca383937264ecb8bab9d7158cceb"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
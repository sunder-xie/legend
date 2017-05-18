<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/card.css?b7e57f9bc402289671f029f941d2ffe3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/customer/list.css?bb25e8dbe6e7d366a32d75238d27f041"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">

        <!-- 标题 start -->
        <div class="customer-list-title clearfix">
            <h3 class="headline fl">车辆查询</h3>

            <div class="tools-btn fr">
                <a href="${BASE_PATH}/shop/customer/edit?refer=car-list" class="yqx-btn btn-2"><span class="fa icon-plus"></span>新建车辆</a>
                <a href="${BASE_PATH}/shop/precheck/precheck?refer=customer" class="yqx-btn btn-1"><span class="fa icon-plus">新建预检单</span></a>
            </div>
        </div>
        <!-- 标题 end -->

        <!-- 查询条件 start -->
        <div class="condition-box" id="customerListForm">
            <div class="condition-input">
                <div class="form-item">
                    <input type="text" name="search_searchKey" class="search-input yqx-input yqx-input-small"
                           value="${customerCar.license}" placeholder="车牌/车主电话/车主/联系电话/联系人">
                </div><div class="senior-box hide">
                    <div class="form-item senior-width">
                        <input type="text" name="search_license" class="yqx-input yqx-input-small" value="" placeholder="车牌">
                    </div><div class="form-item senior-width">
                        <input type="text" name="search_mobile" class="yqx-input yqx-input-small" value="" placeholder="车主电话">
                    </div><div class="form-item senior-width">
                        <input type="text" name="search_customerName" class="yqx-input yqx-input-small" value="" placeholder="车主">
                    </div><div class="row-spaces"></div>
                </div><div class="senior-box hide">
                    <div class="form-item senior-width">
                        <input type="text" name="search_company" class="yqx-input yqx-input-small" value="" placeholder="客户单位">
                    </div><div class="form-item senior-width">
                        <input type="text" name="search_vin" class="yqx-input yqx-input-small" value="" placeholder="VIN码">
                    </div><div class="form-item senior-width">
                        <input type="text" name="search_carInfo" class="yqx-input yqx-input-small" value="" placeholder="车型">
                    </div>
                </div><div class="search-btns">
                    <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</a>
                    <a href="javascript:;" class="senior-search js-senior">高级搜索</a>
                </div>
            </div>

            <div class="row-spaces"></div>
        </div>
        <div style="height: 36px;">
            <div class="tools-bar clearfix select-btns">
                <a href="javascript:;" class="yqx-link card-tab js-customer-list-card"><i class="icon-th-large"></i>卡片</a>
                <span>|</span>
                <a href="javascript:;" class="yqx-link card-tab js-customer-list-table"><i class="icon-reorder"></i>列表</a>
            </div>
            <div class="tools-bar clearfix select-btns-right">
            <#if SESSION_USER_IS_ADMIN == 1>
                <a href="javascript:;" class="yqx-link export-excel"><i class="icon-signout"></i>导出Excel</a>
            </#if>
            </div>
        </div>
        <!-- 查询条件 end -->

        <!-- 数据列表>>表格 start -->
        <div class="customer-list-table" id="listTable">

        </div>
        <!-- 数据列表>>表格  end -->

        <!-- 数据列表>>卡片 start -->
        <div class="customer-list-card" id="listCard">

        </div>
        <!-- 数据列表>>卡片 end -->
    </div>
    <!-- 右侧内容区 end -->
</div>


<!-- 表格模板 start -->
<script type="text/html" id="listTableTpl">
   <div class="customer-list-table-inner">
        <div class="table-scroll-box">
            <table class="yqx-table yqx-table-hover yqx-table-link">
                <thead>
                <tr>
                    <th class="td-center">序号</th>
                    <th>车牌</th>
                    <th>车型</th>
                    <th>车主</th>
                    <th>车主电话</th>
                    <th>下次保养时间</th>
                    <th>保险到期时间</th>
                    <th>年审到期时间</th>
                </tr>
                </thead>
                <tbody>
                <%if(json.data && json.data.content){%>
                <%for(var i=0;i
                <json.data.content.length
                        ;i++){%>
                    <%var item = json.data.content[i];%>
                    <tr class="pointer js-car-tr" data-content="<%=item.id%>">
                        <td class="js-card-order td-center"></td>
                        <td><%=item.license %></td>
                        <td><div class="max-text js-show-tips"><%=item.carInfo%></div></td>
                        <td><div class="max-text js-show-tips"><%=item.customerName%></div></td>
                        <td><%=item.mobile%></td>
                        <td><%=item.keepupTimeStr%></td>
                        <td><%=item.insuranceTimeStr%></td>
                        <td><%=item.auditingTimeStr%></td>
                    </tr>
                    <%}%>
                    <%}%>
                </tbody>
            </table>
        </div>
    </div>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="listTablePage"></div>
    <!-- 分页容器 end -->

</script>
<!-- 表格模板 end -->

<!-- 卡片模板 start -->
<script type="text/html" id="listCardTpl">
    <div class="card-box">
        <%if(json.data && json.data.content){%>
        <%for(var i=0;i
        <json.data.content.length
                ;i++){%>
            <%var item = json.data.content[i];%>
            <div class="card-item" data-id="<%= item.id%>">
                <div class="card-info">
                    <div class="card-check-box">
                        <input type="hidden" name="id" value="<%=item.id%>"/>
                        <div>
                            <span class="card-licence"><%=item.license%></span>
                            <div class="card-status">
                                <%=item.orderStatusName%>
                            </div>
                        </div>
                        <div class="card-car-type" title="<%=item.carInfo%>">
                            <%=item.carInfo%>
                        </div>

                    </div>
                    <div class="car-check-body">

                        <p>车主：<i class="card-customer"><%=item.customerName%></i></p>
                        <p>车主电话：<i class="card-customer"><%=item.mobile%></i>
                        </p>

                        <p>下次保养时间：<span class="strong card-time"><%=item.keepupTimeStr ? item.keepupTimeStr : '无';%></span></p>
                    </div>
                </div>
                <div class="card-money cleafix">
                    <div class="card-price">
                        <a href="${BASE_PATH}/shop/customer/car-detail?refer=customer&id=<%=item.id%>" class="yqx-input yqx-input-3 card-btn">详情</a>
                    </div>
                </div>
                <div class="card-status cleafix">
                </div>
            </div>
            <%}%>
            <%}%>
    </div>

    <!-- 分页容器 start -->
    <div class="yqx-page" id="listCardPage"></div>
    <!-- 分页容器 end -->

</script>
<!-- 卡片模板 end -->

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/customer/list.js?cd547238fa39db54aa29d8bdd666c2af"></script>
<!-- 脚本引入区 end -->

<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<#include "yqx/layout/footer.ftl">
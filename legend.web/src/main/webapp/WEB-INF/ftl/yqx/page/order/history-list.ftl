<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/history-list.css?6b65980659867883740a0eca5ca2b948"/>
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
        <div class="history-list-title clearfix">
            <h3 class="headline fl">导入工单查询</h3>
        </div>
        <!-- 标题 end -->
        <!-- 查询条件 start -->
        <div class="condition-box" id="orderListForm">
            <div class="condition-input">
                <div class="senior-box">
                    <div class="form-item senior-width senior-width-3">
                        <input type="text" name="search_carLicenseLike" class="yqx-input yqx-input-small" value="" placeholder="输入车牌">
                    </div><div class="form-item senior-width senior-width-3">
                        <input type="text" name="search_customerMobileLike" class="yqx-input yqx-input-small" value="" placeholder="输入车主电话">
                    </div><div class="form-item senior-width senior-width-3">
                        <input type="text" name="search_customerNameLike" class="yqx-input yqx-input-small" value="" placeholder="输入车主">
                    </div>
                    <div class="form-item senior-width senior-width-3">
                        <input type="text" name="search_orderSn" class="yqx-input yqx-input-small" value="" placeholder="输入工单编号">
                    </div>
                </div>
                <div class="senior-box">
                    <div class="form-item senior-width">
                        <input type="text" name="search_receiverLike" class="yqx-input yqx-input-small" value="" placeholder="输入服务顾问">
                    </div><div class="form-item senior-width">
                        <input type="text" name="search_orderStatusLike" class="yqx-input yqx-input-small" value="" placeholder="输入工单状态">
                    </div><div class="form-item senior-width">
                        <input type="text" name="search_startTime" class="yqx-input yqx-input-small yqx-input-icon" id="start1" placeholder="开单开始日期">
                        <span class="fa icon-calendar"></span>
                    </div>至<div class="form-item senior-width">
                        <input type="text" name="search_endTime" class="yqx-input yqx-input-small yqx-input-icon" id="end1" placeholder="开单结束日期">
                        <span class="fa icon-calendar"></span>
                    </div><div class="search-btns" style="margin-left: 10px;">
                        <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</a>
                    </div>
                </div>
            </div>
        </div>
        <!-- 查询条件 end -->

        <!-- 数据列表>>表格 start -->
        <div class="history-list-table" id="orderListTable"></div>
        <!-- 数据列表>>表格  end -->
    </div>
    <!-- 右侧内容区 end -->
</div>
<!-- 表格模板 start -->
<script type="text/html" id="orderListTableTpl">
    <div class="history-list-table-inner">
        <div class="table-scroll-box">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>开单日期</th>
                    <th>工单编号</th>
                    <th>服务顾问</th>
                    <th>工单类型</th>
                    <th>工单状态</th>
                    <th>车牌</th>
                    <th>车型</th>
                    <th>行驶里程</th>
                    <th>VIN码</th>
                    <th>发动机号</th>
                    <th>车主</th>
                    <th>车主电话</th>
                    <th>联系人</th>
                    <th>联系电话</th>
                    <th>服务项目</th>
                    <th>配件项目</th>
                    <th>服务费合计</th>
                    <th>服务费优惠</th>
                    <th>配件费合计</th>
                    <th>配件费优惠</th>
                    <th>总计</th>
                    <th>优惠</th>
                    <th>应收金额</th>
                    <th>实收金额</th>
                    <th>挂账金额</th>
                    <th>开单人</th>
                    <th>维修工</th>
                    <th>结算人</th>
                    <th>完工日期</th>
                    <th>结算日期</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody>
                <%if(json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr>
                    <td><%=json.data.size*(json.data.number)+i+1%></td>
                    <td><%if(item.orderCreateTime){%><%= item.orderCreateTime.substring(0,10)%><%}%></td>
                    <td><%=item.orderSn%></td>
                    <td><%=item.receiver%></td>
                    <td><%=item.orderType%></td>
                    <td><%=item.orderStatus%></td>
                    <td><%=item.carLicense%></td>
                    <td><%=item.carModel%></td>
                    <td><%=item.mileage%></td>
                    <td><%=item.vin%></td>
                    <td><%=item.engineNo%></td>
                    <td><%=item.customerName%></td>
                    <td><%=item.customerMobile%></td>
                    <td><%=item.contactName%></td>
                    <td><%=item.contactMobile%></td>
                    <td><%=item.serviceName%></td>
                    <td><%=item.goodsName%></td>
                    <td><%=item.serviceAmount%></td>
                    <td><%=item.serviceDiscount%></td>
                    <td><%=item.goodsAmount%></td>
                    <td><%=item.goodsDiscount%></td>
                    <td><%=item.payableAmount%></td>
                    <td><%=item.discountAmount%></td>
                    <td><%=item.actualPayableAmount%></td>
                    <td><%=item.payAmount%></td>
                    <td><%=item.signAmount%></td>
                    <td><%=item.operatorName%></td>
                    <td><%=item.worker%></td>
                    <td><%=item.payName%></td>
                    <td><%if(item.finishTime){%><%= item.finishTime.substring(0,10)%><%}%></td>
                    <td><%if(item.payTime){%><%= item.payTime.substring(0,10)%><%}%></td>
                    <td><%=item.remark%></td>
                </tr>
                <%}%>
                <%}%>
                </tbody>
            </table>
        </div>
        <!-- 分页容器 start -->
        <div class="yqx-page" id="orderListTablePage"></div>
        <!-- 分页容器 end -->
    </div>
</script>
<!-- 表格模板 end -->
<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/order/history-list.js?81e2e7b5d8271c6f110c607774fb687d"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
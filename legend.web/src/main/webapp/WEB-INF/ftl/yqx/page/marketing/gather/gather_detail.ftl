<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/allot/allot-common.css?59e8db8de7ecdcc00ce4f3de8c5464aa"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/gather-detail.css?9c5a1a67c27ee209f7f3d00643441673"/>
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
        <div class="order-head clearfix">
            <h3 class="headline">集客详情</h3>
        </div>
        <!-- 标题 end -->
        <div class="form-box" id="formData">
            <div class="date-select">
                <div class="form-label">
                    选择日期：
                </div>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small time" value="${dateStr}" placeholder="选择时间">
                    <span class="fa icon-calendar icon-small"></span>
                </div>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search">查询</button>
                <div class="fast-time">
                    快速筛选：
                    <a href="javascript:;" class="js-last-month">上月</a>
                    <a href="javascript:;" class="js-this-month">本月</a>
                </div>
            </div>
            <div class="staff-box">
                <h3>员工筛选：</h3>
                <ul>
                <#if SESSION_USER_IS_ADMIN == 1>
                    <li class="js-staff <#if !userId>staff-current</#if>">全部员工</li>
                    <#list userList as user>
                        <li class="js-staff <#if userId && userId == user.userId>staff-current</#if>" data-user-id="${user.userId}">${user.userName}</li>
                    </#list>
                <#else>
                    <li class="staff-current" data-user-id="${userId}">${userName}</li>
                </#if>
                </ul>
            </div>
        </div>
        <div class="detail-box clearfix">
            <div class="detail fl">
                <h3>集客操作</h3>
                <ul id="operationCon">

                </ul>
            </div>
            <div class="detail fl">
                <h3>集客效果</h3>
                <ul id="effictCon">

                </ul>
            </div>

            <div class="detail fl">
                <h3>绩效提成</h3>
                <ul id="deductCon">

                </ul>
            </div>
        </div>
        <div class="table-con">
            <h3>客户列表
                <a href="javascript:;" class="fr export-ec js-export">导出Excel</a>
            </h3>
            <table class="yqx-table">
                <thead>
                    <tr>
                        <th class="text-l">车牌</th>
                        <th class="text-l">车主名称</th>
                        <th class="text-l">车主电话</th>
                        <th class="text-l">客户归属</th>
                        <th class="text-l">集客时间</th>
                        <th class="text-l">集客方案</th>
                        <th class="text-l">操作人</th>
                        <th class="text-l">操作方式</th>
                        <th class="text-l">集客消费时间</th>
                        <th class="text-r">集客消费金额</th>
                    </tr>
                </thead>
                <tbody id="activateCon">

                </tbody>
            </table>
        </div>
        <!-- 分页容器 start -->
        <div class="yqx-page" id="paging"></div>
        <!-- 分页容器 end -->
    </div>
    <!-- 右侧内容区 end -->
</div>
<script type="text/html" id="operationTpl">
    <%if(json.success && json.data){%>
    <li>
    <div class="label">盘活客户</div>
            <div class="process"><p class="bg-green process-width" style="width: <%=json.data.panHuoCustomer.percentage * 100%>%;"></p></div>
    <div class="total"><%=json.data.panHuoCustomer.value%>人次</div>
    </li>
    <li>
    <div class="label">老客户拉新</div>
            <div class="process"><p class="bg-green process-width" style="width: <%=json.data.laXinCustomer.percentage * 100%>%;"</p></div>
    <div class="total"><%=json.data.laXinCustomer.value%>人次</div>
    </li>
    <li>
    <div class="label">发送短信</div>
            <div class="process"><p class="bg-green process-width" style="width: <%=json.data.sms.percentage * 100%>%;"</p></div>
    <div class="total"><%=json.data.sms.value%>条</div>
    </li>
    <li>
    <div class="label">电话回访</div>
            <div class="process"><p class="bg-green process-width" style="width: <%=json.data.phone.percentage * 100%>%;"></p></div>
    <div class="total"><%=json.data.phone.value%>人次</div>
    </li>
    <li>
    <div class="label">微信转发</div>
            <div class="process"><p class="bg-green process-width" style="width: <%=json.data.weChat.percentage * 100%>%;"></p></div>
    <div class="total"><%=json.data.weChat.value%>人次</div>
    </li>
    <li>
    <div class="label">领券客户</div>
            <div class="process"><p class="bg-green process-width" style="width: <%=json.data.receiveCoupon.percentage * 100%>%;"></p></div>
    <div class="total"><%=json.data.receiveCoupon.value%>人次</div>
    </li>
    <%}%>
</script>
<script type="text/html" id="effictTpl">
    <li>
        <div class="label">老客户到店消费</div>
        <div class="process"><p class="bg-red process-width" style="width: <%=json.data.toStoreOldCustomer.percentage * 100%>%;"></p></div>
        <div class="total"><%=json.data.toStoreOldCustomer.value%>人次</div>
    </li>
    <li>
        <div class="label">新客户到店消费</div>
        <div class="process"><p class="bg-red process-width" style="width: <%=json.data.toStoreNewCustomer.percentage * 100%>%;"></p></div>
        <div class="total"><%=json.data.toStoreNewCustomer.value%>人次</div>
    </li>
    <li>
        <div class="label">未到店客户</div>
        <div class="process"><p class="bg-red process-width" style="width: <%=json.data.notToStoreCustomer.percentage * 100%>%;"></p></div>
        <div class="total"><%=json.data.notToStoreCustomer.value%>人次</div>
    </li>
    <li>
        <div class="label">总计消费</div>
        <div class="process"><p class="bg-red process-width" style="width: <%=json.data.totalConsume.percentage * 100%>%;"></p></div>
        <div class="total">&yen;<%=json.data.totalConsume.value%></div>
    </li>
    <li>
        <div class="label">老客户消费</div>
        <div class="process"><p class="bg-red process-width" style="width: <%=json.data.oldCustomerConsume.percentage * 100%>%;"></p></div>
        <div class="total">&yen;<%=json.data.oldCustomerConsume.value%></div>
    </li>
    <li>
        <div class="label">新客户消费</div>
        <div class="process"><p class="bg-red process-width" style="width: <%=json.data.newCustomerConsume.percentage * 100%>%;"></p></div>
        <div class="total">&yen;<%=json.data.newCustomerConsume.value%></div>
    </li>
</script>
<script type="text/html" id="deductTpl">
    <li>
        <div class="label">总计</div>
        <div class="process"><p class="bg-blue process-width" style="width: <%=json.data.total.percentage * 100%>%;"></p></div>
        <div class="total">&yen;<%=json.data.total.value%></div>
    </li>
    <li>
        <div class="label">新客户到店奖励</div>
        <div class="process"><p class="bg-blue process-width" style="width: <%=json.data.toStoreNewCustomerReward.percentage * 100%>%;"></p></div>
        <div class="total">&yen;<%=json.data.toStoreNewCustomerReward.value%></div>
    </li>
    <li>
        <div class="label">业绩归属奖励</div>
        <div class="process"><p class="bg-blue process-width" style="width: <%=json.data.performanceBelongReward.percentage * 100%>%;"></p></div>
        <div class="total">&yen;<%=json.data.performanceBelongReward.value%></div>
    </li>
    <li>
        <div class="label">销售之星</div>
        <div class="process"><p class="bg-blue process-width" style="width: <%=json.data.performanceStar.percentage * 100%>%;"></p></div>
        <div class="total">&yen;<%=json.data.performanceStar.value%></div>
    </li>
    <li>
        <div class="label">维修业绩提成</div>
        <div class="process"><p class="bg-blue process-width" style="width: <%=json.data.repairPerformance.percentage * 100%>%;"></p></div>
        <div class="total">&yen;<%=json.data.repairPerformance.value%></div>
    </li>
    <li>
        <div class="label">销售业绩提成</div>
        <div class="process"><p class="bg-blue process-width" style="width: <%=json.data.salePerformance.percentage * 100%>%;"></p></div>
        <div class="total">&yen;<%=json.data.salePerformance.value%></div>
    </li>
    <li>
        <div class="label">服务顾问业绩提成</div>
        <div class="process"><p class="bg-blue process-width" style="width: <%=json.data.serviceAdvisorPerformance.percentage * 100%>%;"></p></div>
        <div class="total">&yen;<%=json.data.serviceAdvisorPerformance.value%></div>
    </li>
</script>
<script type="text/html" id="activateTpl">
    <%if(json.success && json.data){%>
    <%for(var i=0; i< json.data.content.length; i++){%>
    <%var item = json.data.content[i]%>
    <tr>
        <td class="text-l js-show-tips ellipsis-1"><%=item.carLicense%></td>
        <td class="text-l js-show-tips ellipsis-1"><%=item.customerName%></td>
        <td class="text-l js-show-tips ellipsis-1"><%=item.customerMobile%></td>
        <td class="text-l js-show-tips ellipsis-1"><%=item.userName%></td>
        <td class="text-l js-show-tips ellipsis-1"><%=item.gatherTime%></td>
        <td class="text-l js-show-tips ellipsis-1"><%=item.gatherType%></td>
        <td class="text-l js-show-tips ellipsis-1"><%=item.creatorName%></td>
        <td class="text-l js-show-tips ellipsis-1"><%=item.operateType%></td>
        <td class="text-l js-show-tips ellipsis-1"><%=item.gatherConsumeTime%></td>
        <td class="text-r money-font js-show-tips ellipsis-1">&yen;<%=item.gatherConsumeAmount%></td>
    </tr>
    <%} %>
    <% if(!item) {%>
    <tr class="no-data">
        <td colspan="10">暂无数据</td>
    </tr>
    <%}%>
    <%}%>
</script>


<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/marketing/gather/gather_detail.js?b9fc68dfe2a8c2e13b92b7dfd3bf51f7"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
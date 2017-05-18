<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/allot/allot-common.css?59e8db8de7ecdcc00ce4f3de8c5464aa"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/gather-effect.css?7c33798ed0fbb4f3d8e8fbdf6ac1f530"/>
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
            <h3 class="headline">集客效果</h3>
        </div>
        <!-- 标题 end -->
        <div class="form-box" id="formData">
            <div class="date-select">
                <div class="form-label">
                    选择日期：
                </div>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small time" value="" placeholder="选择时间">
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
                    <li class="js-staff staff-current">全部员工</li>
                    <#list userList as user>
                        <li class="js-staff" data-user-id="${user.userId}">${user.userName}</li>
                    </#list>
                    <#else>
                        <li class="staff-current" data-user-id="${userId}">${userName}</li>
                </#if>
                </ul>
            </div>
        </div>

        <div class="tab">
            <span class="tab-hover js-tab" data-tab="1">盘活客户</span>
            <span class="js-tab" data-tab="2">老客户拉新</span>
        </div>
        <div class="tab-con" id="tabCon">

        </div>
    </div>
    <!-- 右侧内容区 end -->
</div>

<script type="text/html" id="activateTpl">
    <%if(json.success && json.data){%>
    <table class="yqx-table" id="tableTest">
        <thead>
        <tr>
            <th>盘活方式</th>
            <th>盘活客户</th>
            <th>到店客户<span class="c-question-i js-show-tips" data-tips="盘活客户操作后30天内到店客户数"></span></th>
            <th>增收</th>
            <th>转化率</th>
            <th>操作</th>
        </tr>
        </thead>
        <%for(var i=0; i< json.data.length; i++){%>
        <%var item=json.data[i]%>
        <tr>
            <td><%=item.operateTypeName%></td>
            <td><%=item.operateCustomerNum%></td>
            <td><%=item.toStoreCustomerNum%></td>
            <td class="money-font">&yen;<%=item.income%></td>
            <td><%=item.conversionRate%></td>
            <td><a href="javascript:;" class="yqx-link-1 js-details">详情</a></td>
        </tr>
        <%}%>
        <%if(!item)  {%>
        </table>
        <div class="no-data">暂无数据</div>
        <%} else {%>
    </table>
    <%}%>
    <%}%>
</script>

<script type="text/html" id="oldNewTpl">
    <%var item=json.data%>
    <%if(json.success && item){%>
    <table class="yqx-table" id="tableTest">
        <thead>
        <tr>
            <th>发放优惠券老客户</th>
            <th>领券老客户</th>
            <th>领券新客户</th>
            <th>到店消费老客户<span class="c-question-i js-show-tips" data-tips="老客户拉新操作后30天内到店消费老客户数"></span></th>
            <th>到店消费新客户<span class="c-question-i js-show-tips" data-tips="老客户拉新操作后30天内到店消费新客户数"></span></th>
            <th>增收</th>
            <th style="width: 50px;">转化率</th>
            <th style="width: 50px;">操作</th>
        </tr>
        </thead>
        <%if(
        item
        && (item.grantOldCustomerNum != 0
        || item.receiveOldCustomerNum != 0
        || item.receiveNewCustomerNum != 0
        || item.toStoreNewCustomerNum != 0
        || item.toStoreOldCustomerNum != 0
        || item.income != 0)
        )  {%>
        <tr>
            <td><%=item.grantOldCustomerNum%></td>
            <td><%=item.receiveOldCustomerNum%></td>
            <td><%=item.receiveNewCustomerNum%></td>
            <td><%=item.toStoreOldCustomerNum%></td>
            <td><%=item.toStoreNewCustomerNum%></td>
            <td class="money-font">&yen;<%=item.income%></td>
            <td>
                <%if(item.conversionRate){%>
                <%=item.conversionRate%>
                <%}else{%>
                -
                <%}%>
            </td>
            <td><a href="javascript:;" class="yqx-link-1 js-details">详情</a></td>
        </tr>
        <%} else{%>
        </table>
        <div class="no-data">暂无数据</div>
        <%}%>
    </table>
    <%}%>
</script>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/marketing/gather/gathr-effect.js?9869cb67aebfe88e8060970c9e6d1bce"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">
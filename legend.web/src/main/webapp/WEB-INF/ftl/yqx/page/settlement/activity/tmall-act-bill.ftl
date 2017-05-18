<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/settlement/activity/settlement.css?cf9c1f92d5da86199b5725fd3ab0366d"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/settlement/activity/drainage_table.css?2d4abc6759bbcf058ffa41f2e93d1eb9"/>
<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/settlement/settlement-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h3 class="headline fl">天猫服务收入明细账单</h3>
        </div>
        <div class="search-box clearfix" id="searchForm">
            <input type="hidden" name="page" value="1"/>
            <input type="hidden" name="search_shopActId" id="search_shopActId" value="${shopActId}"/>
            <input type="hidden" id="actTplId" value="${actTplId}"><!-- excel导出用-->
            <div class="show-grid no-margin">
                <div class="form-item form-width-car">
                    <input type="text" name="search_keywords" class="yqx-input yqx-input-small" value="" placeholder="查找车主、车主电话、车牌" id="search_keywords">
                </div>
                <div class="form-item form-width">
                    <input type="text" id="search_shopConfirmStatus" class="yqx-input yqx-input-icon yqx-input-small" placeholder="状态">
                    <input type="hidden" name="search_shopConfirmStatus" value=""/>
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item form-width">
                    <input type="text" id="d4311" name="search_auditPassStartTime" class="yqx-input yqx-input-icon yqx-input-small auditPassStartTime" value="${auditPassStartTime}" placeholder="审核通过开始时间">
                    <span class="fa icon-calendar icon-small"></span>
                </div>
                至
                <div class="form-item form-width">
                    <input type="text" id="d4312" name="search_auditPassEndTime" class="yqx-input yqx-input-icon yqx-input-small auditPassEndTime" value="${auditPassEndTime}" placeholder="审核通过结束时间">
                    <span class="fa icon-calendar icon-small"></span>
                </div>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search">搜索</button>
            </div>
        </div>
        <!--操作按钮 start-->
        <div class="btn-box">
            <a href="javascript:;" class="fr bill_export"><i class="icon-signout"></i>导出Excel</a>
            <a href="javascript:;" class="fr" id="batchOk">￥ 批量确认账单</a>
        </div>
        <table class="yqx-table groupSelect">
            <thead>
            <tr>
                <th><input class="selectAll" type="checkbox"/></th>
                <th>审核通过时间</th><!---->
                <th>车牌</th>
                <th>车主</th>
                <th>车主电话</th>
                <th>优惠券</th>
                <th>套餐服务</th>
                <th>结算价</th>
                <th>状态</th>
                <th width="10%">操作</th>
            </tr>
            </thead>
            <tbody id="content"></tbody>
        </table>
        <!-- 分页容器 start -->
        <div class="yqx-page" id="contentPage"></div>
        <!-- 分页容器 end -->
    </div>
    <#include "yqx/page/settlement/activity/bank-bind.ftl" >
</div>
<script type="text/html" id="contentTemplate">
    <%if(json.data && json.data.content){%>
    <% var insuranceBillSize = json.data.content.length;%>
    <% for(var i = 0; i < insuranceBillSize; i++){%>
    <% var insuranceBill = json.data.content[i]%>
    <tr>
        <td><input class="selectItem" type="checkbox" value="<%=insuranceBill.id %>"/></td>
        <td><%= insuranceBill.auditPassTimeStr%></td>
        <td><%= insuranceBill.carLicense%></td>
        <td><%= insuranceBill.customerName%></td>
        <td><%= insuranceBill.customerMobile%></td>
        <td><%= insuranceBill.verificationCode%></td>
        <td><%= insuranceBill.serviceName%></td>
        <td class="money-font">&yen;<%= insuranceBill.settlePrice%></td>
        <td>
            <%if(insuranceBill.shopConfirmStatus==1){%>
            已确定
            <%}else{%>
            未确定
            <%}%>
        </td>
        <td >
            <%if(insuranceBill.shopConfirmStatus==0){%>
                <a class="link_ok" data-id="<%= insuranceBill.id%>" href="javascript:void(0);">确认账单</a>
            <%}%>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>

<!-- 脚本引入区 start -->
<script type="text/javascript" src="${BASE_PATH}/static/js/page/settlement/activity/service-settle-bill.js?a2c8bca8f0da4c5b7018ca92d6f9d5ec"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/settlement/activity/bank-bind.js?1fbec3c3e9c66ac26a9603bf875cc86c"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
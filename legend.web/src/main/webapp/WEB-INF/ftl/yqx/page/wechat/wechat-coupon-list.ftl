<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/wechat-coupon-list.css?75f98094f2fc584f9893eb098b1d51c5">
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <!-- 查询表单区域 start -->
        <div class="row" id="searchForm" style="margin-bottom:10px;">
            <fieldset>
                <i class="fieldset-title transparent">送券时间</i>
                <div class="form-item">
                    <input class="yqx-input" placeholder="送券开始时间" id="startTime" name="search_startTime" value="${searchParams.startTime}">
                    <span class="fa icon-calendar"></span>
                </div>
                至
                <div class="form-item">
                    <input class="yqx-input" placeholder="送券结束时间" id="endTime" name="search_endTime" value="${searchParams.endTime}">
                    <span class="fa icon-calendar"></span>
                </div>
                <i>卡券</i>
                <div class="form-item">
                    <input class="yqx-input" id="couponType" value="全部">
                    <input type="hidden" name="search_couponInfoId">
                <#--<span class="fa icon-angle-down"></span>-->
                </div>
                <button class="yqx-btn js-search-btn yqx-btn-3">查询</button>
            </fieldset>
        </div>
        <table class="yqx-table" id="tableList"></table>
        <!-- 分页容器 start -->
        <div class="yqx-page" id="paging"></div>
        <!-- 分页容器 end -->
    </div>
</div>
<!-- 表格数据模板 start -->
<script type="text/template" id="tableTpl">
    <thead>
        <tr>
            <th width="5%">序号</th>
            <th width="10%">手机号码</th>
            <th>卡券代码</th>
            <th width="20%">卡券类型</th>
            <th width="20%">卡券有效期</th>
            <th>核销时间</th>
            <th>分享次数</th>
            <th>分享带来用户数</th>
        </tr>
    </thead>
    <tbody>
    <%if(json.data.content&&json.data.content.length){%>
    <%var size = json.data.size%>
    <%var number = json.data.number%>
        <%for(var i=0;i< json.data.content.length;i++){%>
            <%var item=json.data.content[i]%>
            <tr>
                <td><%= i+1%></td>
                <td><%= item.mobile%></td>
                <td><%= item.couponCode%></td>
                <td><%= item.couponName%></td>
                <td><%= item.effectiveDateStr%>至<%= item.expireDateStr%></td>
                <td><%= item.usedTimeStr%></td>
                <td><%= item.shareCount%></td>
                <td><%= item.registerCount%></td>
            </tr>
        <%}%>
    <%}else{%>
        <tr>
            <td colspan="8">暂无数据！</td>
        </tr>
    <%}%>
    </tbody>
</script>
<!-- 表格数据模板 end -->
<script src="${BASE_PATH}/static/js/page/wechat/wechat-coupon-list.js?1c00f2c8674fa7e3694142fef90b09be"></script>
<#include "yqx/layout/footer.ftl" >

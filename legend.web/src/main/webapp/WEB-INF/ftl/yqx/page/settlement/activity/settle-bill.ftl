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
            <h3 class="headline fl">引流活动收入汇总账单</h3>
            <#--<div class="tag-box fr">-->
                <#--<div class="tag">-->
                    <#--<div class="ico ico2"></div>-->
                    <#--<div class="tagcon">-->
                        <#--<p class="tag-title">4月活动服务单总数</p>-->
                        <#--<p class="tag-money">&yen;999999</p>-->
                    <#--</div>-->
                <#--</div>-->
            <#--</div>-->
            <#--<div class="tag-box fr">-->
                <#--<div class="tag">-->
                    <#--<div class="ico ico2"></div>-->
                    <#--<div class="tagcon">-->
                        <#--<p class="tag-title">4月活动服务单总市场价</p>-->
                        <#--<p class="tag-money">&yen;99999</p>-->
                    <#--</div>-->
                <#--</div>-->
            <#--</div>-->
            <#--<div class="tag-box fr">-->
                <#--<div class="tag">-->
                    <#--<div class="ico ico2"></div>-->
                    <#--<div class="tagcon">-->
                        <#--<p class="tag-title">4月活动服务单总收款价</p>-->
                        <#--<p class="tag-money">9单</p>-->
                    <#--</div>-->
                <#--</div>-->
            <#--</div>-->
        </div>
        <div class="search-box clearfix">
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-act-type" placeholder="活动类型"/>
                <input type="hidden" name="search_shopActId" class="search_shopActId"/>
                <span class="fa icon-angle-down icon-small"></span>
            </div>
            <div class="form-item">
                <input type="text" id="d4311" name="search_auditPassStartTime" class="yqx-input yqx-input-icon yqx-input-small auditPassStartTime" value="" placeholder="审核通过开始时间">
                <span class="fa icon-calendar icon-small"></span>
            </div>
            至
            <div class="form-item">
                <input type="text" id="d4312" name="search_auditPassEndTime" class="yqx-input yqx-input-icon yqx-input-small auditPassEndTime" value="" placeholder="审核通过结束时间">
                <span class="fa icon-calendar icon-small"></span>
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search">搜索</button>
        </div>
        <div class="activity-table">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>活动类型</th>
                    <th class="audit-time">审核通过时间段</th>
                    <th>活动服务总单数</th>
                    <th>活动服务总市场价</th>
                    <th>活动服务总结算价</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="content"></tbody>
            </table>
        </div>
        </div>
    <#include "yqx/page/settlement/activity/bank-bind.ftl" >
    </div>
</div>
<script type="text/html" id="contentTemplate">
    <%if(templateData){%>
        <% var insuranceBillSize = templateData.length;%>
        <% for(var i = 0;i < insuranceBillSize; i++){%>
            <% var insuranceBillVo = templateData[i]%>
            <%if(i == insuranceBillSize-1){%>
                <tr>
                    <td>合计：</td>
                    <td></td>
                    <td></td>
                    <td><strong class="money-font">&yen;<%= insuranceBillVo.totalServiceAmount%></strong></td>
                    <td><strong class="money-font">&yen;<%= insuranceBillVo.totalSettleAmount%></strong></td>
                    <td></td>
                </tr>
            <%}else{%>
                <tr>
                    <#--<td><input type="checkbox"/></td>-->
                    <td><%= insuranceBillVo.actName%></td>
                    <td><%= insuranceBillVo.auditPassTimeStr%></td>
                    <td><%= insuranceBillVo.serviceNum%>单</td>
                    <td class="money-font">&yen;<%= insuranceBillVo.totalServiceAmount%></td>
                    <td class="money-font">&yen;<%=insuranceBillVo.totalSettleAmount%></td>
                    <td><%if(insuranceBillVo.needSettle){%>
                        <a class="link_ok" data-id="<%= insuranceBillVo.actTplId%>" href="javascript:void(0);">确认账单</a>
                        <%}%>
                        <%if(insuranceBillVo.serviceNum > 0){%>
                        <a href="javascript:void(0);" act-tpl-id="<%= insuranceBillVo.actTplId%>" class="go_detail">查看详情</a>
                        <%}%>
                    </td>
                </tr>
            <%}%>
        <%}%>
    <%}%>
</script>

<!-- 脚本引入区 start -->
<script type="text/javascript" src="${BASE_PATH}/static/js/page/settlement/activity/settle-bill.js?37e70edcc289b67b922adea7648771a4"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/settlement/activity/bank-bind.js?1fbec3c3e9c66ac26a9603bf875cc86c"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">

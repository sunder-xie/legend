<#include "yqx/layout/header.ftl">
<#--样式引入区-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/flow.css?528ac89e7fb356b88cb952d5aec7cd50" type="text/css"/>
<#--样式引入区-->
<div class="yqx-wrapper clearfix">
    <div class="aside">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="aside-main">
        <h3 class="Z-title">客户营销  > <a href="${BASE_PATH}/account">客户查询</a> > <i>充值记录</i></h3>
        <div class="yqx-group">
            <!-- group内容 start -->
            <div class="yqx-group-content clearfix fl-box search-form" id="formId">
                <div class="form-item" style="margin-right: 10px;">
                    <input class="yqx-input" name="mobile" placeholder="车主电话">
                </div>
                    <div class="date-box">
                        <div class="form-item">
                            <input type="text"
                            name="startTime" value="${startTime}"
                                   placeholder="充值开始时间"
                            class="yqx-input yqx-input-small" id="startDate">
                            <span class="fa icon-calendar icon-small"></span>
                        </div>
                        <p style="margin: 0 4px;">至</p>
                        <div class="form-item">
                            <input type="text"
                            name="endTime" value="${endTime}"
                                   placeholder="充值结束时间"
                            class="yqx-input yqx-input-small" id="endDate">
                            <span class="fa icon-calendar icon-small"></span>
                        </div>
                    </div>
                    <i class="date selected js-date"
                            data-offset="0" data-target="#startDate|#endDate">今日</i>
                    <i class="date js-date"
                            data-target="#startDate|#endDate"
                            data-offset="week">本周</i>
                    <i class="date js-date"
                        data-target="#startDate|#endDate"
                        data-offset="month">本月</i>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn fr">查询</button>
            </div>
            <p class="count-sum">合计收款金额：<span class="amount"><span class="money" id="moneyAmount">0</span>元</span>客户数：<span class="customer-num money" id="customerNum">0</span>位</p>
            <!-- group内容 end -->
        </div>

        <div class="yqx-group" id="info">
            <!-- group标题 start -->
            <div class="yqx-group-head">
                客户列表
            </div>
            <!-- group标题 end -->
            <!-- group内容 start -->
            <div class="yqx-group-content">
                <!-- 表格容器 start -->
                <div id="tableTest"></div>
                <!-- 表格容器 end -->

                <!-- 分页容器 start -->
                <div class="yqx-page" id="pagingTest"></div>
                <!-- 分页容器 end -->

            </div>
            <!-- group内容 end -->
        </div>
        <!-- 表格数据模板 start -->
        <script type="text/template" id="tableTestTpl">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>日期</th>
                    <th>车主</th>
                    <th>车主电话</th>
                    <th>会员卡名称</th>
                    <th>充值金额</th>
                    <th>支付方式</th>
                    <th>会员卡余额</th>
                    <th>收款金额</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                </thead>
                <%if(json.data.content&&json.data.content.length){%>
                <%for(var i=0;i< json.data.content.length;i++){%>
                <%var item=json.data.content[i]%>
                <tr>
                    <td><%= item.gmtCreateStr%></td>
                    <td class="text-overflow js-show-tips"><%= item.customerName%></td>
                    <td><%= item.mobile%></td>
                    <td class="text-overflow js-show-tips"><%= item.cardTypeName%></td>
                    <td class="text-r money-font">&yen;<%= item.cardExplain%></td>
                    <td><%= item.paymentName%></td>
                    <td class="text-r1 money-font">&yen;<%= item.cardBalance%></td>
                    <td class="text-r money-font">&yen;<span class="amount"><%= item.payAmount%></span></td>
                    <td class="text-overflow js-show-tips">
                        <%=item.remark%>
                    </td>
                    <td>
                        <button class="blue js-print" data-id="<%=item.id%>">打印</button>
                        <%if(item.isReversed==0){%>
                        <button class="js-revocation blue" data-id="<%=item.id%>">撤销</button>
                        <%}else if(item.isReversed==1){%>
                        <button class="disabled">已撤销</button>
                        <%}%>
                    </td>
                    <input type="hidden" name="id" value="<%=item.id%>">
                </tr>
                <%}%>
                <%}%>
            </table>
        </script>
    </div>
</div>
<#--脚本引入区-->
<script src="${BASE_PATH}/static/js/page/account/flow.js?f9fb81bc99552e879edf98bfea975edf" type="text/javascript"></script>
<#--脚本引入区-->
<#include "yqx/layout/footer.ftl">
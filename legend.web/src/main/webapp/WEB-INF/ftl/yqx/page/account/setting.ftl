
<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/setting.css?f9706d1b931322d1ea7243e708678aea"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">
    <div class="left-menu fl">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="right-content fr">
        <div class="order-list-title clearfix">
            <h3 class="Z-title fl">
                客户管理 > <i>优惠设置</i>
            </h3>
            <div class="tools-btn fr">
                <a href="${BASE_PATH}/account/coupon/create" class="yqx-btn btn-1"><span class="fa icon-plus"></span>新建优惠券</a>
                <a href="${BASE_PATH}/account/coupon/suite/create" class="yqx-btn btn-2"><span class="fa icon-plus"></span>新建优惠券套餐</a>
                <a href="${BASE_PATH}/account/combo/create" class="yqx-btn btn-3"><span class="fa icon-plus"></span>新建计次卡类型</a>
                <a href="${BASE_PATH}/account/member/create" class="yqx-btn btn-4"><span class="fa icon-plus"></span>新建会员卡类型</a>
            </div>
        </div>

        <!--tab start-->
        <div class="tab-box">
            <div class="tab">
                <div class="tab-item current-item">优惠券管理(${couponInfoCount})</div><div class="tab-item">套餐管理(${couponSuiteCount})</div><div class="tab-item">计次卡类型管理(${comboInfoCount})</div><div class="tab-item">会员卡类型管理(${cardInfoCount})</div>
            </div>
            <!-- tab容器 start -->
            <div class="tabcon">
                <div class="voucher-con" id="tabCon">

                </div>
                <!-- tab容器 end -->

                <!--账户使用记录 start-->
                <div class="use-record">
                    <div class="record-title"></div>
                    <!-- 表格容器 start -->
                    <div class="record-table" id="recordTable">

                    </div>
                    <!-- 表格容器 end -->

                    <!-- 分页容器 start -->
                    <div class="yqx-page" id="paging"></div>
                    <!-- 分页容器 end -->
                </div>
                <!--账户使用记录 end-->
            </div>
        </div>
        <!--tab end-->
    </div>
</div>

<!--优惠券模板-->
<script type="text/html" id="DiscountTpl">
    <%if(json.data.length > 0){%>
    <%for(var i=0; i< json.data.length; i++){%>
    <%var item = json.data[i]%>
    <div class="voucher">
        <div class="voucher-title">
            <input type="hidden" value="<%=item.id%>" id="id">
            <div class="voucher-discount fl"><%=item.couponTypeName%></div>
            <div class="voucher-discount fl js-show-tips"><%=item.couponName%></div>
            <a href="javascript:;" class="show-btn fr js-show-btn"><i class="icon-angle-down"></i><span class="a">展开</span></a>
            <%if (item.couponStatus === 1){%>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-delete" data-tab="0">删除</button>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-disable">停用</button>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small disable-btn fr js-enable" disabled="disabled">启用</button>
            <%if(item.grantCount != null && item.grantCount > 0){%>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-edit disable-btn fr" disabled="disabled">编辑</button>
            <%}else{%>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-edit fr">编辑</button>
            <%}%>
            <div class="voucher-discount fr green">已启用</div>
            <%}else if(item.couponStatus === 2){%>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-delete">删除</button>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small disable-btn js-disable fr" disabled="disabled">停用</button>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-enable">启用</button>
            <%if(item.grantCount != null && item.grantCount > 0){%>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-edit disable-btn fr" disabled="disabled">编辑</button>
            <%}else{%>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-edit fr">编辑</button>
            <%}%>
            <div class="voucher-discount fr red">已停用</div>
            <%}%>

        </div>
        <div class="voucher-content">
            <div class="show-grid">
                <div class="voucher-label">
                    抵扣金额：
                </div>
                <% if(item.couponType == 2){ %>
                <div class="voucher-item">
                    在结算时手动输入金额，直接扣减
                </div>
                <%}else{%>
                    <div class="voucher-item">
                        <span class="money-font">&yen;<%=item.discountAmount%></span>
                        <% if(item.amountLimit != 0){ %>
                            （满<span class="money-font">&yen;<%=item.amountLimit%></span>使用）
                        <%}%>
                    </div>
                <%}%>
            </div>
            <div class="show-grid">
                <div class="voucher-label">
                    使用范围：
                </div>
                <div class="voucher-item">
                    <%var str = item.useRangeDescript, overflow = false;%>
                    <%if(str.length > 151) {%>
                    <% overflow = true;%>
                    <% str = str.slice(0, 151) + '...';%>
                    <%}%>
                    <p class="discount-info js-show-tips" <%if(overflow){%><%='data-tips=' + item.useRangeDescript%><%}%>>
                        <%=str%></p>
                </div>
            </div>
            <% if(item.customizeTime == 1){ %>
                <div class="show-grid">
                    <div class="voucher-label">
                        生效时间：
                    </div>
                    <div class="voucher-item">
                        <%=item.effectiveDateStr%>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="voucher-label">
                        过期时间：
                    </div>
                    <div class="voucher-item">
                        <%=item.expireDateStr%>
                    </div>
                </div>
            <% }else if(item.customizeTime == 0){ %>
                <div class="show-grid">
                    <div class="voucher-label">
                        生效时间：
                    </div>
                    <div class="voucher-item">
                        发放后立即生效可用
                    </div>
                </div>
                <div class="show-grid">
                    <div class="voucher-label">
                        过期时间：
                    </div>
                    <div class="voucher-item">
                        发放后<%=item.effectivePeriodDays%>天
                    </div>
                </div>
            <%}%>
            <div class="show-grid">
                <div class="voucher-label">
                    使用限制：
                </div>
                <div class="voucher-item">
                    <%=item.ruleStr%>
                </div>
            </div>
            <div class="show-grid">
                <div class="voucher-label">
                    备注：
                </div>
                <div class="voucher-item">
                    <%=item.remark%>
                </div>
            </div>
            <div class="voucher-bot clearfix">
                <div class="voucher-label">
                    创建时间：
                </div>
                <div class="voucher-item">
                    <%=item.gmtCreateStr%>
                </div>
                <div class="use-situation">已发放<span class="issued"><%=item.grantCount || 0%></span>张 已使用<span class="used"><%=item.usedCount || 0%></span>张 已过期<span class="expired"><%=item.expireCount || 0%></span>张</div>
            </div>
        </div>
    </div>
    <%}%>
    <%}else{%>
    <div class="nodata clearfix">
        <div class="nodata-l fl">
            <p>亲，您还没有优惠券</p>
            <p>赶紧去创建新优惠券吧！</p>
            <a href="${BASE_PATH}/account/coupon/create"  class="yqx-btn yqx-btn-2 yqx-btn-small">新建优惠券</a>
        </div>
        <div class="nodata-r fr">
            <i class="triangle-left"></i>
            <div class="nodata-tips">
                <p>暂无数据</p>
            </div>
        </div>
    </div>
    <%}%>
</script>

<!--套餐管理模板-->
<script type="text/html" id="MeterTpl">
    <%if(json.data.length > 0){%>
    <%for(var i=0; i< json.data.length; i++){%>
    <%var item = json.data[i]%>
    <div class="voucher">
        <input type="hidden" value="<%=item.id%>" id="id">
        <div class="voucher-title">
            <div class="voucher-discount fl">套餐</div>
            <div class="voucher-discount fl js-show-tips"><%=item.suiteName%></div>
            <a href="javascript:;" class="show-btn fr js-show-btn"><i class="icon-angle-down"></i><span>展开</span></a>
            <%if (item.suiteStatus === 1){%>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-delete" data-tab="1">删除</button>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-disable">停用</button>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small disable-btn fr js-enable" disabled="disabled">启用</button>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-edit fr">编辑</button>
                <div class="voucher-discount fr green">已启用</div>
            <%}else if(item.suiteStatus === 2){%>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small disable-btn fr js-delete" disabled="disabled">删除</button>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small disable-btn js-disable fr" disabled="disabled">停用</button>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-enable">启用</button>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-edit fr">编辑</button>
                <div class="voucher-discount fr red">已停用</div>
            <%}%>
            <div class="voucher-discount fr">售价 <span class="money-font">&yen;<%=item.salePrice%></span></div>
        </div>
        <div class="voucher-content">
            <div class="show-grid">
                <div class="voucher-label">
                    优惠券：
                </div>
                <div class="voucher-item">
                    <%if(item.suiteCouponRels){%>
                        <% for(var j=0; j< item.suiteCouponRels.length; j++){ %>
                            <%  var jtem = item.suiteCouponRels[j]%>
                            <div class="item-width"><%=jtem.couponName%>  <%=jtem.couponCount%><span>张</span></div>
                        <%}%>
                    <%}%>
                </div>
            </div>
            <div class="voucher-bot clearfix">
                <div class="voucher-label">
                    创建时间：
                </div>
                <div class="voucher-item">
                   <%=item.gmtCreateStr%>
                </div>
                <div class="use-situation">已发放<span class="issued"><%=item.usedCount || 0%></span>套</div>
            </div>
        </div>
    </div>
   <%}%>
   <%}else{%>
    <div class="nodata clearfix">
        <div class="nodata-l fl">
            <p>亲，您还没有优惠券套餐</p>
            <p>赶紧去创建新优惠券套餐吧！</p>
            <a href="${BASE_PATH}/account/coupon/suite/create"  class="yqx-btn yqx-btn-2 yqx-btn-small">新建优惠券套餐</a>
        </div>
        <div class="nodata-r fr">
            <i class="triangle-left"></i>
            <div class="nodata-tips">
                <p>暂无数据</p>
            </div>
        </div>
    </div>
    <%}%>
</script>

<!--计次卡模板-->
<script type="text/html" id="timesCountTpl">
    <%if(json.data.length > 0){%>
    <%for(var i=0; i< json.data.length; i++){%>
    <%var item = json.data[i]%>
    <div class="voucher">
        <input type="hidden" value="<%=item.id%>" id="id">
        <div class="voucher-title">
            <div class="voucher-discount fl">计次卡</div>
            <div class="voucher-discount fl js-show-tips"><%=item.comboName%></div>
            <a href="javascript:;" class="show-btn fr js-show-btn"><i class="icon-angle-down"></i><span>展开</span></a>
            <%if (item.comboStatus === 1){%>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-delete" data-tab="2">删除</button>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-disable">停用</button>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small disable-btn fr js-enable" disabled="disabled">启用</button>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-edit">编辑</button>
                <div class="voucher-discount fr green">已启用</div>
            <%}else if(item.comboStatus === 2){%>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small disable-btn fr js-delete" disabled="disabled">删除</button>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small disable-btn js-disable fr" disabled="disabled">停用</button>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-enable">启用</button>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-edit">编辑</button>
            <div class="voucher-discount fr red">已停用</div>
            <%}%>
            <div class="voucher-discount fr">售价 <span class="money-font">&yen;<%=item.salePrice%></div>
        </div>
        <div class="voucher-content">
            <div class="service-project clearfix">
                <div class="voucher-label">
                    服务项目：
                </div>
                <div class="voucher-item">
                    <%for (var j=0; j < item.content.length;j++){%>
                    <%var subItem =item.content[j] %>
                    <p><%=subItem.serviceName%>  总次数：<%=subItem.serviceCount%>次</p>
                    <%}%>
                </div>
            </div>
            <% if(item.customizeTime == 1){ %>
            <div class="show-grid">
                <div class="voucher-label">
                    生效时间：
                </div>
                <div class="voucher-item">
                    <%=item.effectiveDateStr%>
                </div>
            </div>
            <div class="show-grid">
                <div class="voucher-label">
                    过期时间：
                </div>
                <div class="voucher-item">
                    <%=item.expireDateStr%>
                </div>
            </div>
            <% }else if(item.customizeTime == 0){ %>
            <div class="service-project clearfix">
                <div class="show-grid">
                    <div class="voucher-label">
                        生效时间：
                    </div>
                    <div class="voucher-item">
                        发放后立即生效可用
                    </div>
                </div>
                <div class="show-grid">
                    <div class="voucher-label">
                        过期时间：
                    </div>
                    <div class="voucher-item">
                        发放后<%=item.effectivePeriodDays%>天
                    </div>
                </div>
            </div>
            <%}%>
            <div class="clearfix">
                <div class="voucher-label">
                    备注：
                </div>
                <div class="voucher-item">
                    <%= item.remark %>
                </div>
            </div>
            <div class="voucher-bot clearfix">
                <div class="voucher-label">
                    创建时间：
                </div>
                <div class="voucher-item">
                    <%=item.gmtCreateStr%>
                </div>
                <div class="use-situation fr">
                    已发放:<span class="issued"><%=item.grantedCount|| 0%></span>套
                </div>
            </div>
        </div>
    </div>
    <%}%>
    <%}else{%>
    <div class="nodata clearfix">
        <div class="nodata-l fl">
            <p>亲，您还没有计次卡类型</p>
            <p>赶紧去创建计次卡类型吧！</p>
            <a href="${BASE_PATH}/account/combo/create"  class="yqx-btn yqx-btn-2 yqx-btn-small">新建计次卡类型</a>
        </div>
        <div class="nodata-r fr">
            <i class="triangle-left"></i>
            <div class="nodata-tips">
                <p>暂无数据</p>
            </div>
        </div>
    </div>
    <%}%>
</script>


<!--会员卡模板-->
<script type="text/html" id="vipTpl">
    <%if(json.data.length > 0){%>
    <%for(var i=0; i< json.data.length; i++){%>
    <%var item = json.data[i]%>
    <div class="voucher member-card">
        <input type="hidden" value="<%=item.id%>" id="id">
        <div class="voucher-title">
            <div class="voucher-discount fl">会员卡</div>
            <div class="voucher-discount fl js-show-tips"><%=item.typeName%></div>
            <a href="javascript:;" class="show-btn fr js-show-btn"><i class="icon-angle-down"></i><span>展开</span></a>
            <%if (item.cardInfoStatus === 1||item.cardInfoStatus==0){%>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-delete" data-tab="3">删除</button>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-disable">停用</button>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small disable-btn fr js-enable" disabled="disabled">启用</button>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-edit">编辑</button>
            <div class="voucher-discount fr green">已启用</div>
            <%}else if(item.cardInfoStatus === 2){%>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small disable-btn fr js-delete" disabled="disabled">删除</button>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small disable-btn js-disable fr" disabled="disabled">停用</button>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-enable">启用</button>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small fr js-edit">编辑</button>
            <div class="voucher-discount fr red">已停用</div>
            <%}%>
            <div class="voucher-discount fr">售价 <span class="money-font">&yen;<%=item.salePrice%></span></div>
        </div>
        <div class="voucher-content">
            <div class="service-project clearfix">
                <div class="voucher-label">
                    会员卡可用余额：
                </div>
                <div class="voucher-item">
                    <span class="money-font">&yen;<%=item.initBalance%></span>
                </div>
            </div>
            <div class="service-project clearfix">
                <div class="voucher-label">
                    折扣：
                </div>
                <div class="voucher-item member-info">
                    <%var str = item.discountDescript, overflow = false;%>
                    <%if(str.length > 151) {%>
                    <% overflow = true;%>
                    <% str = str.slice(0, 151) + '...';%>
                    <%}%>
                    <p class="discount-info js-show-tips"
                        <%if(overflow){%><%='data-tips=' + item.discountDescript%><%}%>
                    ><%=str%></p>
                </div>
            </div>
            <div class="service-project clearfix">
                <div class="voucher-label">
                    有效期：
                </div>
                <div class="voucher-item">
                     <%=item.effectivePeriodDays%>
                    <span>天</span>
                </div>
            </div>
            <div class="voucher-bot clearfix">
                    <div class="voucher-label">
                        创建时间：
                    </div>
                    <div class="voucher-item">
                         <%=item.gmtCreateStr%>
                    </div>
                    <div class="use-situation fr">
                        已发放<span class="issued"><%=item.grantedCount || 0%></span>套
                    </div>
            </div>
        </div>
    </div>
    <%}%>
    <%}else{%>
    <div class="nodata clearfix">
        <div class="nodata-l fl">
            <p>亲，您还没有会员卡类型</p>
            <p>赶紧去创建会员卡类型吧！</p>
            <a href="${BASE_PATH}/account/member/create"  class="yqx-btn yqx-btn-2 yqx-btn-small">新建会员卡类型</a>
        </div>
        <div class="nodata-r fr">
            <i class="triangle-left"></i>
            <div class="nodata-tips">
                <p>暂无数据</p>
            </div>
        </div>
    </div>
    <%}%>
</script>



<!--优惠券表格-->
<script type="text/html" id="DiscountTableTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>日期</th>
            <th>车主</th>
            <th>车主电话</th>
            <th>优惠券变更</th>
            <th>操作人</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.data && json.data.content){%>
        <%for(var i=0;i< json.data.content.length;i++){%>
        <%var item = json.data.content[i]%>
        <tr>
            <td><%=item.gmtCreateStr%></td>
            <td class="text-overflow js-show-tips"><%=item.customerName%></td>
            <td><a target="_blank" href="${BASE_PATH}/account/detail?refer=grant_record&accountId=<%=item.accountId%>"><%=item.mobile%></a></td>
            <td>
                <div class="text-overflow js-show-tips"><%=item.couponExplain%></div>
            </td>
            <td><%=item.operatorName%></td>
            <td>
                <% if(item.isReversed==0){ %>
                    <a href="javascript:;" data-id="<%=item.id%>" class="js-reverse" data-refer="coupon">撤销</a>
                <%}else{%>
                    已撤销
                <%}%>
            </td>
        </tr>
        <%}}%>
        </tbody>
    </table>
</script>

<!--套餐表格-->
<script type="text/html" id="Package">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>日期</th>
            <th>车主</th>
            <th>车主电话</th>
            <th>套餐</th>
            <th class="text-r">收款金额</th>
            <th>操作人</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.data && json.data.content){%>
        <%for(var i=0;i< json.data.content.length;i++){%>
        <%var item = json.data.content[i]%>
        <tr>
            <td><%=item.gmtCreateStr%></td>
            <td  class="text-overflow js-show-tips"><%=item.customerName%></td>
            <td>
                <div class="text-overflow js-show-tips"><a target="_blank" href="${BASE_PATH}/account/detail?refer=grant_record&accountId=<%=item.accountId%>"><%=item.mobile%></a></div>
            </td>
            <td class="text-overflow js-show-tips"><%=item.couponSuiteName%></td>
            <td class="text-r money-font">&yen;<%=item.amount%></td>
            <td><%=item.operatorName%></td>
            <td>
                <% if(item.couponSource==2){ %>
                    已撤销
                <%}else{%>
                    <a href="javascript:;" data-id="<%=item.flowId%>" class="js-reverse" data-refer="coupon_suite">撤销</a>
                <%}%>
            </td>
        </tr>
        <%}}%>
        </tbody>
    </table>
</script>

<!--计次卡表格-->
<script type="text/html" id="timesCountTableTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>日期</th>
            <th>车主</th>
            <th>车主电话</th>
            <th>计次卡</th>
            <th class="text-r">收款金额</th>
            <th>服务顾问</th>
            <th>发卡人</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.data && json.data.content){%>
        <%for(var i=0;i< json.data.content.length;i++){%>
        <%var item = json.data.content[i]%>
        <tr>
            <td><%=item.dateStr%></td>
            <td  class="text-overflow js-show-tips"><%=item.customerName%></td>
            <td><div class="text-overflow js-show-tips"><a target="_blank" href="${BASE_PATH}/account/detail?refer=grant_record&accountId=<%=item.accountId%>"><%=item.phone%></a></div></td>
            <td class="text-overflow js-show-tips"><%=item.comboName%></td>
            <td class="text-r money-font text-overflow js-show-tips">&yen;<%=item.payAmount%></td>
            <td class="ellipsis-1 js-show-tips"><%=item.recieverName%></td>
            <td class="ellipsis-1 js-show-tips"><%=item.creatorName%></td>
            <td>
                <% if(item.isReversed==0){ %>
                    <a href="javascript:;" data-id="<%=item.flowId%>"
                       class="js-reverse" data-target="timesCount">撤销</a>
                    <a href="javascript:;" data-id="<%= item.flowId %>" class="js-print">打印</a>
                <%}else{%>
                    已撤销
                <%}%>
            </td>
        </tr>
        <%}}%>
        </tbody>
    </table>
</script>

<!--会员卡表格-->
<script type="text/html" id="vipTableTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>日期</th>
            <th>车主</th>
            <th>车主电话</th>
            <th>会员卡号</th>
            <th>会员卡类型</th>
            <th class="text-right">收款金额</th>
            <th>服务顾问</th>
            <th>发卡人</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.data && json.data.content){%>
        <%for(var i=0;i< json.data.content.length;i++){%>
        <%var item = json.data.content[i]%>
        <tr>
            <td><%=item.gmtCreateStr%></td>
            <td  class="text-overflow js-show-tips"><%=item.customerName%></td>
            <td><a target="_blank" href="${BASE_PATH}/account/detail?refer=grant_record&accountId=<%=item.accountId%>"><%=item.mobile%></a></td>
            <td><div class="text-overflow js-show-tips"><%=item.cardNumber%></div></td>
            <td><div class="text-overflow js-show-tips"><%=item.cardTypeName%></div></td>
            <td class="text-right money-font text-overflow js-show-tips">&yen;<%=item.payAmount%></td>
            <td class="ellipsis-1 js-show-tips"><%=item.receiverName%></td>
            <td class="ellipsis-1 js-show-tips"><%=item.publisherName%></td>
            <td>
                <% if(item.isReversed==0){ %>
                <a href="javascript:;" data-id="<%=item.flowId%>"
                   class="js-reverse" data-target="vip">撤销</a>
                <%}else{%>
                已撤销
                <%}%>
            </td>
        </tr>
        <%}}%>
        </tbody>
    </table>
</script>

<script type="text/javascript" src="${BASE_PATH}/static/js/page/account/setting.js?b0ba6e0c884d095b3ad40f245bd6f4d9"></script>
<#include "yqx/layout/footer.ftl">
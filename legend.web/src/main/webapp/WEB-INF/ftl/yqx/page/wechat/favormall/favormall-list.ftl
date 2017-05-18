<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/wechat-common.css?0219a05beda33d460b58eb20349af16b"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/favormall-list.css?e5000e97c3dc54eba9359622ddbf5f16"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline">卡券商城</h1>
        </div>
        <div class="order-body">
            <!-- 查询表单区域 start -->
            <div id="searchForm">
                <div class="js-list-tab">
                    <span class="list-tab current-tab" data-type="1">会员卡</span>
                    <span class="list-tab" data-type="2">优惠券</span>
                </div>
                <div class="search-box">
                    <div class="form-item">
                        <input type="text" class="yqx-input yqx-input-small js-status"  placeholder="展示状态">
                        <input type="hidden" name="search_givingStatus">
                        <i class="fa icon-angle-down"></i>
                    </div>
                    <button class="yqx-btn js-search-btn yqx-btn-3 yqx-btn-small">确定</button>
                </div>
            </div>
            <!-- 查询表单区域 end -->

            <#--会员卡介绍/新建 start-->
            <div id="new-member" class="new-card">
                <div class="card-intro fl">
                    <div class="intro-title inline-block">会员卡展示说明：</div>
                    <div class="intro-content inline-block">
                        <div>1.将门店会员卡展示给公众号粉丝，吸引潜在客户到店办理会员卡</div>
                        <div>2.也可在会员卡内预充部分金额，粉丝可直接领取并且到店使用，吸引客户到店消费并且办理会员卡</div>
                    </div>
                </div>
                <a class="yqx-btn yqx-btn-1 fr" href="${BASE_PATH}/shop/wechat/favormall-card-edit">新增</a>
            </div>
            <#--会员卡介绍/新建 end-->

            <#--优惠券介绍/新建 start-->
            <div id="new-coupon" class="new-coupon-card display-none">
                <div class="card-intro fl">
                    <div class="intro-title inline-block">优惠券赠送说明：</div>
                    <div class="coupon-intro-content inline-block">
                        <div>1.将设置的优惠券赠送给粉丝，吸引粉丝领取并到店消费</div>
                        <div>2.粉丝领取优惠券后，可分享到到朋友圈或分享给好友，吸引更多客户关注公众号领取优惠券，促使潜在客户到店消费。</div>
                    </div>
                </div>
                <a class="yqx-btn yqx-btn-1 fr coupon-btn" href="${BASE_PATH}/shop/wechat/favormall-coupon-edit">新增</a>
            </div>
            <#--优惠券介绍/新建 end-->

            <table class="yqx-table" id="tableList"></table>
            <!-- 分页容器 start -->
            <div class="yqx-page" id="paging"></div>
            <!-- 分页容器 end -->
        </div>
    </div>
</div>

<#--会员卡列表 start-->
<script type="text/template" id="member-table-tpl">
    <thead>
    <tr>
        <th>编号</th>
        <th class="th-left">会员卡</th>
        <th class="th-left">已领取/赠送总数</th>
        <th class="th-left">展示状态</th>
        <th class="th-right">充值额度</th>
        <th class="td-operate" width="25%">操作</th>
    </tr>
    </thead>
    <tbody>
    <%if(json.data && json.data.content && json.data.content.length){%>
    <%for(var i=0;i< json.data.content.length;i++){%>
    <%var item=json.data.content[i]%>
    <tr>
        <td><%=i+1%></td>
        <td class="td-left js-show-tips ellipsis-1"><%=item.typeName%></td>
        <%if(item.isGiving == 0){%>
        <td class="td-left">—</td>
        <%}else{%>
        <td class="td-left"><%= item.receiveNumber%>/<%= item.givingNumber%></td>
        <%}%>
        <td class="td-left"><%= item.displayStatus%></td>
        <%if(item.isGiving == 0){%>
        <td class="td-right money-font">—</td>
        <%}else{%>
        <td class="td-right money-font"><span class="rmb-font">¥</span><%= item.givingAmount%></td>
        <%}%>
        <td class="td-operate">
            <div>
                <%if(item.givingStatus!=3){%><a class="js-edit link" href="${BASE_PATH}/shop/wechat/favormall-card-edit?cardCfgId=<%= item.id%>">编辑</a><%}%>
                <a class="js-is-fold link" href="javascript:;">展开</a>
            </div>
        </td>
    </tr>
    <tr>
        <td colspan="6" class="card-detail-box">
            <div class="card-detail"  hidden="hidden">
                <div class="card-tag"></div>
                <div class="member-name"><%if(item.cardInfoExplain){%>此会员卡为【<%= item.cardInfoExplain%>】<%}%></div>
                <div class="member-explain">优惠说明：<div class="explain"><%= item.privilegeDesc%></div></div>
                <div class="service-discount">折扣说明：<%= item.discountDescript%></div>
                <div class="validity">有效期：<%= item.effectivePeriodDays%>天</div>
            </div>
        </td>
    </tr>
    <%}%>
    <%}else{%>
    <tr>
        <td colspan="6">暂无数据！</td>
    </tr>
    <%}%>
    </tbody>
</script>
<#--会员卡列表 end-->

<#--优惠券列表 start-->
<script type="text/template" id="coupon-table-tpl">
    <thead>
    <tr>
        <th>编号</th>
        <th class="th-left">优惠券</th>
        <th class="th-left">已领取/赠送总数</th>
        <th class="th-left">展示状态</th>
        <th class="th-right">抵扣金额</th>
        <th class="td-operate" width="25%">操作</th>
    </tr>
    </thead>
    <tbody>
    <%if(json.data && json.data.content && json.data.content.length){%>
    <%for(var i=0;i< json.data.content.length;i++){%>
    <%var item=json.data.content[i]%>
    <tr>
        <input type="hidden" class="config-id" value="<%= item.id%>">
        <input type="hidden" class="coupon-givingNumber" value="<%= item.givingNumber%>">
        <input type="hidden" class="coupon-couponTypeId" value="<%= item.couponTypeId%>">
        <td><%= i+1%></td>
        <td class="td-left js-show-tips ellipsis-1"><%= item.couponName%></td>
        <td class="td-left"><span class="receive-num"><%= item.receiveNumber%></span>/<%= item.givingNumber%></td>
        <td class="td-left"><%= item.displayStatus%></td>
        <td class="td-right money-font"><span class="rmb-font">¥</span><%= item.discountAmount%></td>
        <td class="td-operate">
            <%if(item.givingStatus !=3){%>
            <a class="js-cancel-send link red-link">取消赠送</a>
            <a class="js-send link">修改数量</a>
            <a class="js-is-fold link" href="javascript:;">展开</a>
            <%}else{%>
            <a class="js-is-fold link" href="javascript:;">展开</a>
            <%}%>
        </td>
    </tr>
    <tr>
        <td colspan="6" class="card-detail-box">
            <div class="card-detail"  hidden="hidden">
                <div class="card-tag"></div>
                <div class="amount-limit"><%= item.amountLimiStr%></div>
                <div class="use-range">使用范围：<%= item.useRangeDescript%>;</div>
                <div class="effective">有效期：<%= item.effectiveStr%></div>
                <div class="compatibleWithStr">使用规则：<%= item.compatibleWithStr%></div>
            </div>
        </td>
    </tr>
    <%}%>
    <%}else{%>
    <tr>
        <td colspan="6">暂无数据！</td>
    </tr>
    <%}%>
    </tbody>
</script>
<#--优惠券列表 end-->

<#--优惠券赠送弹窗-->
<script type="text/template" id="couponSendTpl">
    <div class="collection-bounce">
        <input type="hidden" name="id">
        <input type="hidden" name="receiveNumber">
        <div class="bounce-title">赠送</div>
        <div class="bounce-content">
            <div class="tip mb10">请慎重填写赠送此优惠券的张数</div>
            <div class="card-num mb10">
                <div class="form-label">
                    优惠券数量
                </div>
                <div class="form-item">
                    <input type="text" name="givingNumber" class="yqx-input yqx-input-icon yqx-input-small" data-v-type="required | integer">
                    <span class="fa icon-small">张</span>
                </div>
            </div>
        </div>
        <div class="bounce-foot">
            <button class="yqx-btn yqx-btn-3 js-modifymenu-confirm" data-type="<%=type%>">确定</button>
            <button class="yqx-btn yqx-btn-1 js-cancel">取消</button>
        </div>
    </div>
</script>
<script src="${BASE_PATH}/static/js/page/wechat/favormall-list.js?207568bf016f60a631bc3f4a23fca2b0"></script>
<#include "yqx/layout/footer.ftl" >
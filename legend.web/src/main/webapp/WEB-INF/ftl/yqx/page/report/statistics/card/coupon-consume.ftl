<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/common/report/report-common.css?c58c9a3ee3acf5c0dbc310373fe3c349">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/report/statistics/card/coupon-recharge.css?8fef9018ea83e9fa9603b540ebd0765c">
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/report/left-nav.ftl"/>
    <div class="content fr member-content">
        <div class="content-head">
            <div class="tab-item js-tab-item member-tab current-item"
                 data-desc="member"
                 data-target=".member-content" data-tab="0">会员卡
            </div>
            <div class="tab-item js-tab-item combo-tab"
                 data-desc="combo"
                 data-target=".combo-content" data-tab="1">计次卡
            </div>
            <div class="tab-item js-tab-item coupon-tab"
                 data-desc="coupon"
                 data-target=".coupon-content" data-tab="2">优惠券
            </div>
        </div>
        <div class="container search-form" id="memberForm">
            <div class="show-grid input-width-155">
                <div class="input-group">
                    <label>消费时间</label>

                    <div class="form-item">
                        <input type="text" id="start2" name="search_STime"
                               class="yqx-input yqx-input-small" placeholder="选择开始时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                    <label class="time-middle-text">至</label>

                    <div class="form-item">
                        <input type="text" id="end2" name="search_ETime"
                               class="yqx-input yqx-input-small" placeholder="选择结束时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                </div>
                <div class="input-group">
                    <label>会员卡号</label>

                    <div class="form-item">
                        <input class="yqx-input yqx-input-small"
                               name="search_cardNum"
                                >
                    </div>
                </div>
                <div class="input-group">
                    <label>车牌</label>

                    <div class="form-item">
                        <input class="yqx-input yqx-input-small"
                               name="search_license"
                                >
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="input-group">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-small"
                               placeholder="车主号码"
                               name="search_mobile"
                                >
                    </div>
                </div>
                <div class="input-group">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-small select-input js-member-info js-show-tips"
                               placeholder="会员卡类型"
                                >
                        <input type="hidden"
                               name="search_cardCouponTypeId">
                        <span class="fa icon-small icon-angle-down"></span>
                    </div>
                </div>
                <div class="input-group">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-small"
                               placeholder="工单号"
                               name="search_orderSn"
                                >
                    </div>
                </div>
                <div class="btn-group fr">
                    <button class="js-search-btn yqx-btn yqx-btn-3 yqx-btn-small">查询</button>
                    <button class="js-reset yqx-btn yqx-btn-1 yqx-btn-small">重置</button>
                </div>
            </div>
            <div class="lines"></div>
            <div class="form-options text-right">
                <div class="form-option">
                    <i class="js-list-option" data-target=".dropdown"><span class="icon-plus"></span>列表字段设置</i>
                </div>
                <div class="vertical-line"></div>
                <div class="form-option">
                    <a href="javascript:;" class="js-excel" data-target="services" type="button"><i
                            class="icon-signout"></i>导出excel</a>
                </div>
            </div>
            <div class="dropdown hide">
                <ul class="dropdown-menu" data-desc="member">
                    <li><label><input type="checkbox" data-ref="table-consume-time" checked>消费时间</label></li>
                    <li><label><input type="checkbox" data-ref="table-order-sn" checked>工单号</label></li>
                    <li><label><input type="checkbox" data-ref="table-license" checked>车牌</label></li>
                    <li><label><input type="checkbox" data-ref="table-customer" checked>车主</label></li>
                    <li><label><input type="checkbox" data-ref="table-mobile" checked>车主号码</label></li>
                    <li><label><input type="checkbox" data-ref="table-card-num" checked>会员卡号</label></li>
                    <li><label><input type="checkbox" data-ref="table-coupon-info" checked>会员卡类型</label></li>
                    <li><label><input type="checkbox" data-ref="table-discount-type" checked>会员特权</label></li>
                    <li><label><input type="checkbox" data-ref="table-balance-pay" checked>会员卡余额抵扣</label></li>
                    <li><label><input type="checkbox" data-ref="table-privilege-pay" checked>会员卡特权抵扣</label></li>
                    <li><label><input type="checkbox" data-ref="table-card-balance" checked>会员卡余额</label></li>
                </ul>
            </div>
        </div>
        <div class="table-box">
            <div class="scroll-x">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th class="table-consume-time">消费时间</th>
                        <th class="table-order-sn">工单号</th>
                        <th class="table-license">车牌</th>
                        <th class="table-customer">车主</th>
                        <th class="table-mobile">车主号码</th>
                        <th class="table-card-num">会员卡号</th>
                        <th class="table-coupon-info">会员卡类型</th>
                        <th class="table-discount-type">会员特权</th>
                        <th class="table-balance-pay">会员卡余额抵扣</th>
                        <th class="table-privilege-pay">会员卡特权抵扣</th>
                        <th class="table-card-balance">会员卡余额</th>
                    </tr>
                    </thead>
                    <tbody id="memberFill">
                    </tbody>
                </table>
            </div>
        </div>
        <div class="total-box">
            查询结果：
            会员卡余额抵扣： <span class="money-font vip-deduction"></span>
            会员卡特权抵扣： <span class="money-font deduction"></span>
        </div>
        <div class="yqx-page" id="memberPage"></div>
    </div>
    <div class="content fr combo-content hide">
        <div class="content-head">
            <div class="tab-item js-tab-item member-tab"
                 data-desc="member"
                 data-target=".member-content" data-tab="0">会员卡
            </div>
            <div class="tab-item js-tab-item combo-tab"
                 data-desc="combo"
                 data-target=".combo-content" data-tab="1">计次卡
            </div>
            <div class="tab-item js-tab-item coupon-tab"
                 data-desc="coupon"
                 data-target=".coupon-content" data-tab="2">优惠券
            </div>
        </div>
        <div class="container search-form" id="comboForm">
            <div class="show-grid input-width-155">
                <div class="input-group">
                    <label>消费时间</label>

                    <div class="form-item">
                        <input type="text" id="start2" name="search_STime"
                               class="yqx-input yqx-input-small" placeholder="选择开始时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                    <label class="time-middle-text">至</label>

                    <div class="form-item">
                        <input type="text" id="end2" name="search_ETime"
                               class="yqx-input yqx-input-small" placeholder="选择结束时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                </div>
                <div class="input-group">
                    <label>车主号码</label>

                    <div class="form-item">
                        <input class="yqx-input yqx-input-small"
                               name="search_mobile"
                                >
                    </div>
                </div>
                <div class="input-group">
                    <label>车牌</label>

                    <div class="form-item">
                        <input class="yqx-input yqx-input-small js-show-tips"
                               name="search_license"
                                >
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="input-group">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-small js-combo-info select-input js-show-tips"
                               placeholder="计次卡类型"
                                >
                        <input type="hidden"
                               name="search_cardCouponTypeId">
                        <span class="fa icon-small icon-angle-down"></span>
                    </div>
                </div>
                <div class="btn-group fr">
                    <button class="js-search-btn yqx-btn yqx-btn-3 yqx-btn-small">查询</button>
                    <button class="js-reset yqx-btn yqx-btn-1 yqx-btn-small">重置</button>
                </div>
            </div>
            <div class="lines"></div>
            <div class="form-options text-right">
                <div class="form-option">
                    <i class="js-list-option" data-target=".dropdown"><span class="icon-plus"></span>列表字段设置</i>
                </div>
                <div class="vertical-line"></div>
                <div class="form-option">
                    <a href="javascript:;" class="js-excel" data-target="services" type="button"><i
                            class="icon-signout"></i>导出excel</a>
                </div>
            </div>
            <div class="dropdown hide">
                <ul class="dropdown-menu" data-desc="combo">
                    <li><label><input type="checkbox" data-ref="table-consume-time" checked>消费时间</label></li>
                    <li><label><input type="checkbox" data-ref="table-order-sn" checked>工单号</label></li>
                    <li><label><input type="checkbox" data-ref="table-license" checked>车牌</label></li>
                    <li><label><input type="checkbox" data-ref="table-customer" checked>车主</label></li>
                    <li><label><input type="checkbox" data-ref="table-mobile" checked>车主号码</label></li>
                    <li><label><input type="checkbox" data-ref="table-coupon-info" checked>计次卡类型</label></li>
                    <li><label><input type="checkbox" data-ref="table-service" checked>服务项目</label></li>
                    <li><label><input type="checkbox" data-ref="table-service-hour" checked>工时</label></li>
                    <li><label><input type="checkbox" data-ref="table-service-amount" checked>工时费</label></li>
                    <li><label><input type="checkbox" data-ref="table-consume-count" checked>消费次数</label></li>
                    <li><label><input type="checkbox" data-ref="table-discount-amount" checked>抵扣金额</label></li>
                </ul>
            </div>
        </div>
        <div class="container">
            <div class="scroll-x">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th class="table-consume-time">消费时间</th>
                        <th class="table-order-sn">工单号</th>
                        <th class="table-license">车牌</th>
                        <th class="table-customer">车主</th>
                        <th class="table-mobile">车主号码</th>
                        <th class="table-coupon-info">计次卡类型</th>
                        <th class="table-service">服务项目</th>
                        <th class="table-service-hour">工时</th>
                        <th class="table-service-amount">工时费</th>
                        <th class="table-consume-count">消费次数</th>
                        <th class="table-discount-amount">抵扣金额</th>
                    </tr>
                    </thead>
                    <tbody id="comboFill">

                    </tbody>
                </table>
            </div>
        </div>
        <div class="yqx-page" id="comboPage"></div>
    </div>
    <div class="content fr coupon-content hide">
        <div class="content-head">
            <div class="tab-item js-tab-item member-tab"
                 data-desc="member"
                 data-target=".member-content" data-tab="0">会员卡
            </div>
            <div class="tab-item js-tab-item combo-tab"
                 data-desc="combo"
                 data-target=".combo-content" data-tab="1">计次卡
            </div>
            <div class="tab-item js-tab-item coupon-tab"
                 data-desc="coupon"
                 data-target=".coupon-content" data-tab="2">优惠券
            </div>
        </div>
        <div class="container search-form" id="couponForm">
            <div class="show-grid input-width-155">
                <div class="input-group">
                    <label>消费时间</label>

                    <div class="form-item">
                        <input type="text" id="start3" name="search_STime"
                               class="yqx-input yqx-input-small" placeholder="选择开始时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                    <label class="time-middle-text">至</label>

                    <div class="form-item">
                        <input type="text" id="end3" name="search_ETime"
                               class="yqx-input yqx-input-small" placeholder="选择结束时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                </div>
                <div class="input-group">
                    <label>车主号码</label>

                    <div class="form-item">
                        <input class="yqx-input yqx-input-small js-show-tips"
                               name="search_mobile"
                                >
                    </div>
                </div>
                <div class="input-group">
                    <label>车牌</label>

                    <div class="form-item">
                        <input class="yqx-input yqx-input-small js-show-tips"
                               name="search_license"
                                >
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="input-group">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-small js-coupon-info js-show-tips select-input"
                               placeholder="优惠券"
                                >
                        <span class="fa icon-small icon-angle-down"></span>
                        <input type="hidden" name="search_cardCouponTypeId">
                    </div>
                </div>
                <div class="input-group">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-small"
                               placeholder="工单号"
                               name="search_orderSn"
                        >
                    </div>
                </div>
                <div class="btn-group fr">
                    <button class="js-search-btn yqx-btn yqx-btn-3 yqx-btn-small">查询</button>
                    <button class="js-reset yqx-btn yqx-btn-1 yqx-btn-small">重置</button>
                </div>
            </div>
            <div class="lines"></div>
            <div class="form-options text-right">
                <div class="form-option">
                    <i class="js-list-option" data-target=".dropdown"><span class="icon-plus"></span>列表字段设置</i>
                </div>
                <div class="vertical-line"></div>
                <div class="form-option">
                    <a href="javascript:;" class="js-excel" data-target="services" type="button"><i
                            class="icon-signout"></i>导出excel</a>
                </div>
            </div>
            <div class="dropdown hide">
                <ul class="dropdown-menu" data-desc="coupon">
                    <li><label><input type="checkbox" data-ref="table-consume-time" checked>消费时间</label></li>
                    <li><label><input type="checkbox" data-ref="table-order-sn" checked>工单号</label></li>
                    <li><label><input type="checkbox" data-ref="table-license" checked>车牌</label></li>
                    <li><label><input type="checkbox" data-ref="table-customer" checked>车主</label></li>
                    <li><label><input type="checkbox" data-ref="table-mobile" checked>车主号码</label></li>
                    <li><label><input type="checkbox" data-ref="table-coupon-info" checked>优惠券</label></li>
                    <li><label><input type="checkbox" data-ref="table-coupon-code" checked>优惠券码</label></li>
                    <li><label><input type="checkbox" data-ref="table-consume-count" checked>使用数量</label></li>
                    <li><label><input type="checkbox" data-ref="table-discount-amount" checked>抵扣金额</label></li>
                </ul>
            </div>
        </div>
        <div class="container">
            <div class="scroll-x">
                <table class="yqx-table" data-desc="coupon">
                    <thead>
                    <tr>
                        <th class="table-consume-time">消费时间</th>
                        <th class="table-order-sn">工单号</th>
                        <th class="table-license">车牌</th>
                        <th class="table-customer">车主</th>
                        <th class="table-mobile">车主号码</th>
                        <th class="table-coupon-info">优惠券</th>
                        <th class="table-coupon-code">优惠券码</th>
                        <th class="table-consume-count">使用数量</th>
                        <th class="table-discount-amount">抵扣金额</th>
                    </tr>
                    </thead>
                    <tbody id="couponFill">
                    </tbody>
                </table>
            </div>
        </div>
        <div class="yqx-page" id="couponPage"></div>
    </div>

</div>
<!-- 计次卡 -->
<script id="comboTpl" type="text/template">

    <% var e;%>
    <%if (json.data.content.length == 0) {%>
    <tr>
        <td colspan="11">暂无数据</td>
    </tr>
    <%}%>
    <% for(var i in json.data.content) {%>
    <% e = json.data.content[i];%>
    <tr>
        <td class="table-consume-time"><%=e.consumeTime%></td>
        <td class="table-order-sn"><%=e.orderSn%></td>
        <td class="table-license">
            <%=e.license%>
        </td>
        <td class="table-customer">
            <%=e.customerName%>
        </td>
        <td class="table-mobile"><%=e.mobile%></td>
        <td class="table-coupon-info">
            <%=e.cardCouponInfo || ''%>
        </td>
        <td class="table-service">
            <%=e.serviceName ||'' %>
        </td>
        <td class="table-service-hour">
            <%=e.serviceHour || ''%>
        </td>
        <td class="table-service-amount font-money">
            <%='&yen;' + e.serviceAmount%>
        </td>
        <td class="table-consume-count">
            <%=e.consumeCount ||'' %>
        </td>
        <td class="font-money table-discount-amount"><%='&yen;' + e.discountAmount%></td>
    </tr>
    <%}%>

</script>
<script id="memberTpl" type="text/template">

    <% var e;%>
    <%if (json.data.content.length == 0) {%>
    <tr>
        <td colspan="11">暂无数据</td>
    </tr>
    <%}%>
    <% for(var i in json.data.content) {%>
    <% e = json.data.content[i];%>
    <tr>
        <td class="table-consume-time"><%=e.consumeTime%></td>
        <td class="table-order-sn"><%=e.orderSn%></td>
        <td class="table-license">
            <%=e.license%>
        </td>
        <td class="table-customer">
            <%=e.customerName%>
        </td>
        <td class="table-mobile"><%=e.mobile || ''%></td>
        <td class="table-card-num">
            <%=e.cardNum%>
        </td>
        <td class="table-coupon-info">
            <%=e.cardCouponInfo || ''%>
        </td>
        <td class="table-discount-type"><%=e.discountTypeStr%><%=e.discountRate %>折</td>
        <td class="font-money table-balance-pay"><%='&yen;' + (e.cardBalancePay || '0')%></td>
        <td class="font-money table-privilege-pay">
            <%='&yen;' + e.discountAmount %>
        </td>
        <td class="table-card-balance"><%='&yen;' + e.cardBalance%></td>
    </tr>
    <%}%>
</script>
<script id="couponTpl" type="text/template">

    <% var e;%>
    <%if (json.data.content.length == 0) {%>
    <tr>
        <td colspan="9">暂无数据</td>
    </tr>
    <%}%>
    <% for(var i in json.data.content) {%>
    <% e = json.data.content[i];%>
    <tr>
        <td class="table-consume-time"><%=e.consumeTime%></td>
        <td class="table-order-sn"><%=e.orderSn%></td>
        <td class="table-license">
            <%=e.license%>
        </td>
        <td class="table-customer">
            <%=e.customerName%>
        </td>
        <td class="table-mobile">
            <%=e.mobile%>
        </td>
        <td class="table-coupon-info"><%=e.cardCouponInfo || ''%></td>
        <td class="table-coupon-code"><%=e.couponCode || ''%></td>
        <td class="table-consume-count"><%=e.consumeCount || ''%></td>
        <td class="table-discount-amount font-money"><%='&yen;' + e.discountAmount%></td>
    </tr>
    <%}%>
</script>
<script src="${BASE_PATH}/static/js/common/report/report-common.js?6ba9ca39d54ea0d7587dd452bb409d86"></script>
<script src="${BASE_PATH}/static/js/page/report/statistics/card/coupon-consume.js?971c3713bf4a80c82f7779d62557e81e"></script>
<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<#include "yqx/layout/footer.ftl" >

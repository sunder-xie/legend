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
                    <label>办理时间</label>

                    <div class="form-item">
                        <input type="text" id="start1" name="search_sTime" value="${cardStartTime}"
                               class="yqx-input yqx-input-small" placeholder="选择开始时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                    <label class="time-middle-text">至</label>

                    <div class="form-item">
                        <input type="text" id="end1" name="search_eTime" value="${cardEndTime}"
                               class="yqx-input yqx-input-small" placeholder="选择结束时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                </div>
                <div class="input-group">
                    <label>会员卡号</label>

                    <div class="form-item">
                        <input class="yqx-input yqx-input-small"
                               name="search_cardNumber"
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
                        <input class="yqx-input yqx-input-small js-member-info js-show-tips select-input"
                               placeholder="会员卡类型"
                                >
                        <input type="hidden"
                               name="search_cardCouponTypeId">
                        <span class="fa icon-small icon-angle-down"></span>
                    </div>
                </div>
                <div class="input-group">
                    <div class="form-item">
                        <input class="yqx-input yqx-input-small js-member-type js-show-tips select-input"
                               placeholder="交易类型" value="${consumeTypeName}"
                                >
                        <input type="hidden"
                               name="search_consumeTypeId" value="${consumeTypeId}">
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
                <ul class="dropdown-menu" data-desc="member">
                    <li><label><input type="checkbox" data-ref="table-create-time" checked>办理时间</label></li>
                    <li><label><input type="checkbox" data-ref="table-type-name" checked>交易类型</label></li>
                    <li><label><input type="checkbox" data-ref="table-license" checked>车牌</label></li>
                    <li><label><input type="checkbox" data-ref="table-customer" checked>车主</label></li>
                    <li><label><input type="checkbox" data-ref="table-mobile" checked>车主号码</label></li>
                    <li><label><input type="checkbox" data-ref="table-member-card" checked>会员卡号</label></li>
                    <li><label><input type="checkbox" data-ref="table-card-type" checked>会员卡类型</label></li>
                    <li><label><input type="checkbox" data-ref="table-amount" checked>充值金额</label></li>
                    <li><label><input type="checkbox" data-ref="table-car-balance" checked>卡内余额</label></li>
                    <li><label><input type="checkbox" data-ref="table-pay-amount" checked>收款/退款</label></li>
                    <li><label><input type="checkbox" data-ref="table-operator-name" checked>操作人</label></li>
                </ul>
            </div>
        </div>
        <div class="table-box">
            <div class="scroll-x">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th class="table-create-time">办理时间</th>
                        <th class="table-type-name">交易类型</th>
                        <th class="table-license">车牌</th>
                        <th class="table-customer">车主</th>
                        <th class="table-mobile">车主号码</th>
                        <th class="table-member-card">会员卡号</th>
                        <th class="table-card-type">会员卡类型</th>
                        <th class="table-amount">充值金额</th>
                        <th class="table-car-balance">卡内余额</th>
                        <th class="table-pay-amount">收款/退款</th>
                        <th class="table-operator-name">操作人</th>
                    </tr>
                    </thead>
                    <tbody id="memberFill">
                    </tbody>
                </table>
            </div>
        </div>
        <div class="total-box">
            查询结果：
            充值金额： <span class="money-font recharge"></span>
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
                    <label>办理时间</label>

                    <div class="form-item">
                        <input type="text" id="start2" name="search_sTime" value="${comboStartTime}"
                               class="yqx-input yqx-input-small" placeholder="选择开始时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                    <label class="time-middle-text">至</label>
                    <div class="form-item">
                        <input type="text" id="end2" name="search_eTime" value="${comboEndTime}"
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
                        <input class="yqx-input yqx-input-small js-combo-info js-show-tips select-input"
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
                    <li><label><input type="checkbox" data-ref="table-create-time"" checked>办理时间</label></li>
                    <li><label><input type="checkbox" data-ref="table-license" checked>车牌</label></li>
                    <li><label><input type="checkbox" data-ref="table-customer" checked>车主</label></li>
                    <li><label><input type="checkbox" data-ref="table-mobile" checked>车主号码</label></li>
                    <li><label><input type="checkbox" data-ref="table-combo-name" checked>计次卡类型</label></li>
                    <li><label><input type="checkbox" data-ref="table-count-num" checked>卡内项目数</label></li>
                    <li><label><input type="checkbox" data-ref="table-effective-date" checked>有效期</label></li>
                    <li><label><input type="checkbox" data-ref="table-expire-date" checked>过期时间</label></li>
                    <li><label><input type="checkbox" data-ref="table-pay-amount" checked>收款金额</label></li>
                    <li><label><input type="checkbox" data-ref="table-operator-name" checked>操作人</label></li>
                </ul>
            </div>
        </div>
        <div class="container">
            <div class="scroll-x">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th class="table-create-time">办理时间</th>
                        <th class="table-license">车牌</th>
                        <th class="table-customer">车主</th>
                        <th class="table-mobile">车主号码</th>
                        <th class="table-combo-name">计次卡类型</th>
                        <th class="table-count-num">卡内项目数</th>
                        <th class="table-effective-date">有效期</th>
                        <th class="table-expire-date">过期时间</th>
                        <th class="table-pay-amount">收款金额</th>
                        <th class="table-operator-name">操作人</th>
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
                    <label>发放时间</label>

                    <div class="form-item">
                        <input type="text" id="start3" name="search_sTime" value="${couponStartTime}"
                               class="yqx-input yqx-input-small" placeholder="选择开始时间">
                        <span class="fa icon-small icon-calendar"></span>
                    </div>
                    <label class="time-middle-text">至</label>

                    <div class="form-item">
                        <input type="text" id="end3" name="search_eTime" value="${couponEndTime}"
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
                        <input class="yqx-input yqx-input-small js-suite-info js-show-tips"
                               placeholder="优惠券来源"
                                >
                        <span class="fa icon-small icon-angle-down"></span>
                        <input type="hidden" name="search_suiteId">
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
                    <li><label><input type="checkbox" data-ref="table-create-time" checked>发放时间</label></li>
                    <li><label><input type="checkbox" data-ref="table-license" checked>车牌</label></li>
                    <li><label><input type="checkbox" data-ref="table-customer" checked>车牌</label></li>
                    <li><label><input type="checkbox" data-ref="table-mobile" checked>车主号码</label></li>
                    <li><label><input type="checkbox" data-ref="table-coupon-name" checked>优惠券</label></li>
                    <li><label><input type="checkbox" data-ref="table-coupon-num" checked>数量</label></li>
                    <li><label><input type="checkbox" data-ref="table-effective-date" checked>过期时间</label></li>
                    <li><label><input type="checkbox" data-ref="table-coupon-sorce" checked>来源</label></li>
                    <li><label><input type="checkbox" data-ref="table-operator-name" checked>操作人</label></li>
                </ul>
            </div>
        </div>
        <div class="container">
            <div class="scroll-x">
                <table class="yqx-table" data-desc="coupon">
                    <thead>
                    <tr>
                        <th class="table-create-time">发放时间</th>
                        <th class="table-license">车牌</th>
                        <th class="table-customer">车主</th>
                        <th class="table-mobile">车主号码</th>
                        <th class="table-coupon-name">优惠券</th>
                        <th class="table-coupon-num">数量</th>
                        <th class="table-effective-date">过期时间</th>
                        <th class="table-coupon-sorce">来源</th>
                        <th class="table-operator-name">操作人</th>
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
<script id="comboTpl" type="text/template">

    <% var e;%>
    <%if (json.data.content.length == 0) {%>
    <tr>
        <td colspan="10">暂无数据</td>
    </tr>
    <%}%>
    <% for(var i in json.data.content) {%>
    <% e = json.data.content[i];%>
    <tr>
        <td class="table-create-time"><%=e.rechargeTime%></td>
        <td class="table-license js-show-tips"><%=e.license%></td>
        <td class="table-customer"><%=e.customerName%></td>
        <td class="table-mobile"><%=e.mobile%></td>
        <td class="table-combo-name">
            <%=e.cardCouponType || ''%>
        </td>
        <td class="table-count-num">
            <%=e.countNum||'' %>
        </td>
        <td class="table-effective-date">
            <%=(e.effectDay || '') + '天'%>
        </td>
        <td class="table-expire-date">
            <%=e.expireate || ''%>
        </td>
        <td class="font-money table-pay-amount"><%='&yen;' + e.reciveAmount%></td>
        <td class="table-operator-name"><%=e.operatorName%></td>
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
        <td class="table-create-time"><%=e.rechargeTime%></td>
        <td class="table-type-name"><%=e.tradeTypeName%></td>
        <td class="table-license js-show-tips"><%=e.license;%></td>
        <td class="table-customer"><%=e.customerName;%></td>
        <td class="table-mobile"><%=e.mobile%></td>
        <td class="table-member-card"><%=e.cardNum %></td>
        <td class="table-card-type"><%=e.cardCouponType%></td>
        <td class="font-money table-amount"><%if(e.tradeTypeName !== '会员卡充值') {%><%='--'%><%}else{%><%='&yen;' + e
        .rechargeAmount%><%}%></td>
        <td class="font-money table-car-balance"><%='&yen;' + (e.cardBalance || '0')%></td>
        <td class="font-money table-pay-amount"><%='&yen;' + (e.reciveAmount || '0')%></td>
        <td class="table-operator-name"><%=e.operatorName%></td>
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
        <td class="table-create-time"><%=e.rechargeTime%></td>
        <td class="table-license js-show-tips">
            <%=e.license%>
        </td>
        <td class="table-customer">
            <%=e.customerName%>
        </td>
        <td class="table-mobile"><%=e.mobile%></td>
        <td class="table-coupon-name"><%=e.cardCouponType || ''%></td>
        <td class="table-coupon-num"><%=e.countNum || ''%></td>
        <td class="table-effective-date"><%=e.expireate || ''%></td>
        <td class="table-coupon-sorce"><%=e.source %></td>
        <td class="table-operator-name"><%=e.operatorName%></td>
    </tr>
    <%}%>
</script>
<script src="${BASE_PATH}/static/js/common/report/report-common.js?6ba9ca39d54ea0d7587dd452bb409d86"></script>
<script src="${BASE_PATH}/static/js/page/report/statistics/card/coupon-recharge.js?3fed590baa0deb29f07dabe31868a192"></script>

<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<#include "yqx/layout/footer.ftl" >

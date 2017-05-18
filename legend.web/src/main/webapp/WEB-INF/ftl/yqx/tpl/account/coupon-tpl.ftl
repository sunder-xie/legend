<#-- Created by zmx on 2017/3/7. -->
<style>
    /*券 start*/
    .voucher-box{ border: 1px solid #d2d2d2; background: #f9f9f9; padding: 12px 7px;}
    .voucher{  display:inline-block; width:32%;margin-bottom:10px;position: relative; cursor: pointer; vertical-align: top;}
    .voucher-hide{ display: none;}
    .voucher-tag{ width: 23px; height: 23px; background: url("${BASE_PATH}/static/img/page/settlement/debit/btn-active.png") no-repeat; position: absolute; right: 0; top:113px; display: none;}
    /*优惠券*/
    .voucher-t{ width: 100%; height: 135px; padding: 10px 5px 10px 0; border-left: 1px solid #dadada; border-right: 1px solid #dadada; background:url("${BASE_PATH}/static/img/page/settlement/debit/border.png") repeat-x top #fff;}
    .voucher-hover{background:url("${BASE_PATH}/static/img/page/settlement/debit/border-hover.png") repeat-x top #fff; border-left: 1px solid #8bb30d; border-right: 1px solid #8bb30d;}
    .voucher-hover .voucher-l,.voucher-hover .vocher-title,.voucher-hover .vocher-title span{ color: #8bb30d;}
    .voucher-l{ width: 30%; height: 110px; font-family: arial, verdana, sans-serif; text-align: center;
        line-height: 110px;
        font-size: 26px; vertical-align: top;
        color: #ff1d00;}
    .voucher-l .tag{ position: absolute; top:20px; left: 6px; line-height: 20px;}
    .voucher-l span{ font-size: 14px;}
    .voucher-r{ width: 65%;}
    .vocher-title{ font-size: 12px; color: #ff1d00; line-height: 16px;}
    .vocher-title span{color: #ff8477;}
    .vocher-customer-name{font-size: 12px; color: #444; line-height: 20px; padding-bottom: 10px;}
    .vocher-infor{
        width: 150px;
        height: 54px;
        overflow: hidden;
        line-height: 15px;
        color: #444;
        font-size: 12px;
    }
    .vocher-infor p{ display:block; width:100%; height:18px;font-size: 12px; color: #444; line-height: 18px;}
    .vocher-middle-border {
        width: 1px;
        height: 100%;
        border-left: 1px solid #ddd;
        margin: 3px 0 0 0;
    }
    .validity-period{ color: #888; line-height: 22px;}
    .use-times{height: 45px; background: #fff; border: 1px solid #9fbf3a; border-top: none; padding: 6px; display: none;}
    .use-times .form-item{ width: 100px; }
    /*通用券*/
    .car-tag{ background: url("${BASE_PATH}/static/img/page/settlement/debit/car-tag.png") no-repeat center;}
    .voucher-hover .car-tag{ background: url("${BASE_PATH}/static/img/page/settlement/debit/car-taghover.png") no-repeat center;}
    .voucher-more{ height: 30px; line-height: 30px; text-align: center; border: 1px solid #d2d2d2; background: #fff;cursor: pointer;}
    /*计次卡*/
    .combo-tag{ background: url("${BASE_PATH}/static/img/page/settlement/debit/combo-tag.png") no-repeat center;}
    .voucher-hover .combo-tag{ background: url("${BASE_PATH}/static/img/page/settlement/debit/combo-taghover.png") no-repeat center;}
    /*转赠券*/
    .donation{ line-height: 73px; text-align: center; color: #999; font-size: 36px;}
    .voucher-hover.donation{ color: #8bb30d;}
    .use-times .donation-form{ width: 180px; }
    .no-coupon{ display: inline-block; width: 110px; height: 28px; line-height: 28px;text-align: left;}
    .no-coupon img{vertical-align: middle;margin-right: 10px;}
    .voucher-hover .voucher-l, .voucher-hover .vocher-title, .voucher-hover .vocher-title span {
        color: #8bb30d;
    }
    .other-voucher{margin: 10px;}
    .no-sign {margin-right: 5px; color: #666;}
    .hide{ display: none;}
    /*券 end*/

</style>

<div id="discountContent" class="count-content discount-content"></div>
<#if USE_GUEST_ACCOUNT != 'yes'>
<p class="no-coupon-tips hide"><i class="icon-exclamation-sign no-sign"></i>没有优惠券</p>
</#if>

<#if USE_GUEST_ACCOUNT == 'yes'>
<div class="other-voucher">
    <div class="form-label">
        使用其他客户优惠券:
    </div>
    <div class="form-item">
        <input type="text"
               class="yqx-input yqx-input-small js-guestMobile" value=""
               placeholder="请输入客户电话">
    </div>
    <p class="guest-no-customer hide">该客户无优惠券</p>
</div>
<div id="guestCouponConunt" class="count-content"></div>
</#if>


<#-- 优惠 -->
<script id="discountCouponTpl" type="text/html">
    <%if(json && json.couponCount > 0){%>
    <%var len = 0;%>
    <!--优惠券 注：目前优惠券没做合并，一张优惠券一条记录-->
    <%if(json.sortedCashCouponList && json.sortedCashCouponList.length>0){%>
    <%for(var i in json.sortedCashCouponList){%>
    <% len++;%>
    <%if( len==4 ){%>
    <div class="voucher-hide">
        <%}%>
        <%var cashCouponList = json.sortedCashCouponList[i] %>
        <%var rules = cashCouponList.ruleDesc.split(';')%>
        <div class="voucher js-coupon js-order <%if(cashCouponList.available){%>js-voucher<%} else {%>div-disable<%}%>">
            <div class="voucher-t <%if(cashCouponList.selected){%>voucher-hover<%}%>"<%if(cashCouponList.selected){%> data-cashCoupon="<%=$stringify(cashCouponList)%>"<%}%>>
                <input type="hidden" value="<%=cashCouponList.couponId%>" class="couponId"/>

            <div class="voucher-l fl">
                <span class="tag">&yen;</span>
                <%if(cashCouponList.discount <= 999){%>
                <span style="font-size: 30px;"><%=cashCouponList.discount%></span>
                <%}else if(cashCouponList.discount >999 && cashCouponList.discount <= 9999 ){%>
                <span style="font-size: 24px;"><%=cashCouponList.discount%></span>
                <%}else if(cashCouponList.discount >9999 && cashCouponList.discount <= 99999){%>
                <span style="font-size: 20px;"><%=cashCouponList.discount%></span>
                <%}%>
            </div>
                <div class="vocher-middle-border fl"></div>
                <div class="voucher-r fr">
                    <div class="vocher-title"><%=cashCouponList.couponName%></div>
                    <div class="vocher-customer-name">
                        <span class="vocher-name"><%=cashCouponList.customerName%></span>
                        <span><%=cashCouponList.mobile%></span>
                    </div>
                    <div class="vocher-infor">
                        <p><%=rules[0] || ''%></p>

                        <p><%=rules[1] || ''%></p>

                        <p><%=rules[2] || ''%></p>
                    </div>
                    <div class="validity-period">有效期：<%=cashCouponList.expireDateStr%></div>
                </div>
            </div>
            <div class="voucher-tag"
            <%if(cashCouponList.selected){%> style="display: block;"<%}%>>
        </div>
        <div class="use-times"
        <%if(cashCouponList.selected){%> style="display: block;"<%}%>>
        <div class="form-item fl discount-amount-box">
            <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-discount-amount"
                   value="<%=cashCouponList.finalDiscount%>" placeholder="抵扣金额" disabled="disabled">
            <span class="fa icon-small">元</span>
        </div>
    </div>
    </div>
    <%}}%>
    <!--优惠券 end-->

    <!--计次卡 start-->
    <%if(json.sortedComboList && json.sortedComboList.length>0){%>
    <%for(var k in json.sortedComboList){%>
    <% len++;%>
    <%if( len==4 ){%>
    <div class="voucher-hide">
        <%}%>
        <%var comboList = json.sortedComboList[k] %>
        <div class="voucher js-combo <%if(comboList.available){%>js-voucher<%} else {%>div-disable<%}%>">
            <div class="voucher-t <%if(comboList.selected){%>voucher-hover<%}%>"<%if(comboList.selected){%> data-combo="<%=$stringify(comboList)%>"<%}%>>
            <input type="hidden" value="<%=comboList.comboServiceId%>" class="comboServiceId"/>
                <div class="voucher-l fl combo-tag">

                </div>
                <div class="vocher-middle-border fl"></div>
                <div class="voucher-r fr">
                    <div class="vocher-title"><%=comboList.comboName%><span class="fr">x<%=comboList.count%></span>
                    </div>
                    <div class="vocher-customer-name">
                        <span class="vocher-name"><%=comboList.customerName%></span>
                        <span><%=comboList.mobile%></span>
                    </div>
                    <div class="vocher-infor">
                        <p><%=comboList.serviceName%></p>
                    </div>
                    <div class="validity-period">有效期：<%=comboList.expireDateStr%></div>
                </div>
            </div>
            <div class="voucher-tag"
            <%if(comboList.selected){%> style="display: block;"<%}%>>
        </div>
        <div class="use-times"
        <%if(comboList.selected){%> style="display: block;"<%}%>>
        <div class="form-item fr">
            <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-use-count"
                   value="<%= comboList.useCount || 1%>"
                   placeholder="使用次数" data-v-type="integer">
            <span class="fa icon-small">次</span>
        </div>
        <div class="form-item fl">
            <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-discount-amount"
                   value="<%= comboList.discount%>" placeholder="抵扣金额" disabled="disabled">
            <span class="fa icon-small">元</span>
        </div>
    </div>
    </div>
    <%}}%>
    <!--计次卡 end-->

    <!--通用券-->
    <%if(json.sortedUniversalCouponList && json.sortedUniversalCouponList.length>0){%>
    <%for(var j in json.sortedUniversalCouponList){%>
    <% len++;%>
    <%if( len==4 ){%>
    <div class="voucher-hide">
        <%}%>
        <%var universalList = json.sortedUniversalCouponList[j] %>
        <%var universalRules = universalList.ruleDesc.split(';')%>
        <div class="voucher js-coupon <%if(universalList.available){%>js-voucher<%} else {%>div-disable<%}%>">
            <div class="voucher-t <%if(universalList.selected){%>voucher-hover<%}%>"<%if(universalList.selected){%> data-cashcoupon="<%=$stringify(universalList)%>"<%}%>>
            <input type="hidden" value="<%=universalList.couponId%>" class="couponId"/>
                <div class="voucher-l fl car-tag">

                </div>
                <div class="vocher-middle-border fl"></div>
                <div class="voucher-r fr">
                    <div class="vocher-title"><%=universalList.couponName%></div>
                    <div class="vocher-customer-name">
                        <span class="vocher-name"><%=universalList.customerName%></span>
                        <span><%=universalList.mobile%></span>
                    </div>
                    <div class="vocher-infor">
                        <p><%=universalRules[0] || ''%></p>

                        <p><%=universalRules[1] || ''%></p>
                    </div>
                    <div class="validity-period">有效期：<%=universalList.expireDateStr%></div>
                </div>
            </div>
            <div class="voucher-tag"
            <%if(universalList.selected){%> style="display: block;"<%}%>>
        </div>
        <div class="use-times"
        <%if(universalList.selected){%> style="display: block;"<%}%>>
        <div class="form-item fl discount-amount-box">
            <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-discount-amount"
                   value="<%= universalList.finalDiscount || 0%>" data-v-type="price" placeholder="抵扣金额">
            <span class="fa icon-small">元</span>
        </div>
    </div>
    </div>
    <%}}%>
    <!--通用券 end-->
    <%if( len>3 ){%>
    </div>
    <!--点击查看更多显示 start-->
    <div class="voucher-more js-vouchermore"></div>
    <!--点击查看更多显示 end-->
    <%}%>
    <%}%>
</script>


<script id="guestCouponTpl" type="text/html">
    <%if(json && json.guestCouponCount > 0){%>
    <%var len = 0;%>
    <!--优惠券 注：目前优惠券没做合并，一张优惠券一条记录-->
    <%if(json.sortedGuestCashCouponList && json.sortedGuestCashCouponList.length>0){%>
    <%for(var i in json.sortedGuestCashCouponList){%>
    <% len++;%>
    <%if( len==4 ){%>
    <div class="voucher-hide">
        <%}%>
        <%var cashCouponList = json.sortedGuestCashCouponList[i] %>
        <%var rules = cashCouponList.ruleDesc.split(';')%>
        <div class="voucher js-coupon <%if(cashCouponList.available){%>js-voucher<%} else {%>div-disable<%}%>">
            <div class="voucher-t <%if(cashCouponList.selected){%>voucher-hover<%}%>"<%if(cashCouponList.selected){%> data-cashCoupon=<%= $stringify(cashCouponList)%><%}%>>
            <input type="hidden" value="<%=cashCouponList.couponId%>" class="couponId"/>

                <div class="voucher-l fl">
                    <span class="tag">&yen;</span><%=cashCouponList.discount%>
                </div>
                <div class="vocher-middle-border fl"></div>
                <div class="voucher-r fr">
                    <div class="vocher-title"><%=cashCouponList.couponName%></div>
                    <div class="vocher-customer-name">
                        <span class="vocher-name"><%=cashCouponList.customerName%></span>
                        <span><%=cashCouponList.mobile%></span>
                    </div>
                    <div class="vocher-infor">
                        <p><%=rules[0] || ''%></p>

                        <p><%=rules[1] || ''%></p>

                        <p><%=rules[2] || ''%></p>
                    </div>
                    <div class="validity-period">有效期：<%=cashCouponList.expireDateStr%></div>
                </div>
            </div>
            <div class="voucher-tag"
            <%if(cashCouponList.selected){%> style="display: block;"<%}%>>
        </div>
        <div class="use-times"
        <%if(cashCouponList.selected){%> style="display: block;"<%}%>>
        <div class="form-item fl discount-amount-box">
            <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-discount-amount"
                   value="<%=cashCouponList.finalDiscount%>" placeholder="抵扣金额" disabled="disabled">
            <span class="fa icon-small">元</span>
        </div>
    </div>
    </div>
    <%}}%>
    <!--优惠券 end-->

    <!--计次卡 start-->
    <%if(json.sortedGuestComboList && json.sortedGuestComboList.length>0){%>
    <%for(var k in json.sortedGuestComboList){%>
    <% len++;%>
    <%if( len==4 ){%>
    <div class="voucher-hide">
        <%}%>
        <%var comboList = json.sortedGuestComboList[k] %>
        <div class="voucher js-combo <%if(comboList.available){%>js-voucher<%} else {%>div-disable<%}%>">
            <div class="voucher-t <%if(comboList.selected){%>voucher-hover<%}%>"<%if(comboList.selected){%> data-combo=<%= $stringify(comboList)%><%}%>>
            <input type="hidden" value="<%=comboList.comboServiceId%>" class="comboServiceId"/>
                <div class="voucher-l fl combo-tag">

                </div>
                <div class="vocher-middle-border fl"></div>
                <div class="voucher-r fr">
                    <div class="vocher-title"><%=comboList.comboName%><span class="fr">x<%=comboList.count%></span>
                    </div>
                    <div class="vocher-customer-name">
                        <span class="vocher-name"><%=comboList.customerName%></span>
                        <span><%=comboList.mobile%></span>
                    </div>
                    <div class="vocher-infor">
                        <p><%=comboList.serviceName%></p>
                    </div>
                    <div class="validity-period">有效期：<%=comboList.expireDateStr%></div>
                </div>
            </div>
            <div class="voucher-tag"
            <%if(comboList.selected){%> style="display: block;"<%}%>>
        </div>
        <div class="use-times"
        <%if(comboList.selected){%> style="display: block;"<%}%>>
        <div class="form-item fr">
            <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-use-count"
                   value="<%= comboList.useCount || 1%>"
                   placeholder="使用次数" data-v-type="integer">
            <span class="fa icon-small">次</span>
        </div>
        <div class="form-item fl">
            <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-discount-amount"
                   value="<%= comboList.discount%>" placeholder="抵扣金额" disabled="disabled">
            <span class="fa icon-small">元</span>
        </div>
    </div>
    </div>
    <%}}%>
    <!--计次卡 end-->

    <!--通用券-->
    <%if(json.sortedGuestUniversalCouponList && json.sortedGuestUniversalCouponList.length>0){%>
    <%for(var j in json.sortedGuestUniversalCouponList){%>
    <% len++;%>
    <%if( len==4 ){%>
    <div class="voucher-hide">
        <%}%>
        <%var universalList = json.sortedGuestUniversalCouponList[j] %>
        <%var universalRules = universalList.ruleDesc.split(';')%>
        <div class="voucher js-coupon <%if(universalList.available){%>js-voucher<%} else {%>div-disable<%}%>">
            <div class="voucher-t <%if(universalList.selected){%>voucher-hover<%}%>"<%if(universalList.selected){%> data-cashcoupon=<%= $stringify(universalList)%><%}%>>
            <input type="hidden" value="<%=universalList.couponId%>" class="couponId"/>
                <div class="voucher-l fl car-tag">

                </div>
                <div class="vocher-middle-border fl"></div>
                <div class="voucher-r fr">
                    <div class="vocher-title"><%=universalList.couponName%></div>
                    <div class="vocher-customer-name">
                        <span class="vocher-name"><%=universalList.customerName%></span>
                        <span><%=universalList.mobile%></span>
                    </div>
                    <div class="vocher-infor">
                        <p><%=universalRules[0] || ''%></p>

                        <p><%=universalRules[1] || ''%></p>
                    </div>
                    <div class="validity-period">有效期：<%=universalList.expireDateStr%></div>
                </div>
            </div>
            <div class="voucher-tag"
            <%if(universalList.selected){%> style="display: block;"<%}%>>
        </div>
        <div class="use-times"
        <%if(universalList.selected){%> style="display: block;"<%}%>>
        <div class="form-item fl discount-amount-box">
            <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-discount-amount"
                   value="<%= universalList.finalDiscount || 0%>" data-v-type="price" placeholder="抵扣金额">
            <span class="fa icon-small">元</span>
        </div>
    </div>
    </div>
    <%}}%>
    <!--通用券 end-->
    <%if( len>3 ){%>
    </div>
    <!--点击查看更多显示 start-->
    <div class="voucher-more js-vouchermore"></div>
    <!--点击查看更多显示 end-->
    <%}%>
    <%}%>
</script>

<script type="text/javascript">
    $(function () {
        var $doc = $(document);
        var topic = Components.$broadcast.topic;
        var lastVal = 1,
            lastAmount = 0,
            oldGuestMobile,
            combo,
            universal,
            args;
        seajs.use(['dialog', 'art', 'check', 'ajax'], function (dg, at, ck) {
            at.helper('$stringify', function (json) {
                return JSON.stringify(json);
            });
            //查看更多券
            $doc.on('click', '.js-vouchermore', function () {
                var moreText = $(this).text();
                if (moreText == '点击查看更多') {
                    $(this).siblings('.voucher-hide').show();
                    $(this).text('点击隐藏')
                } else {
                    $(this).siblings('.voucher-hide').hide();
                    $(this).text('点击查看更多')
                }
            });
           //券展开收缩
            $doc.on('click', '.js-voucher', function () {
                var $this = $(this);
                $this.find('.use-times').slideToggle(500);
                $this.find('.voucher-tag').toggle();
                $this.find('.voucher-t').toggleClass('voucher-hover');
                Components.getCouponData();
            });
            //阻止冒泡
            $doc.on('click', '.js-voucher input', function () {
                return false;
            });
            //获取其它客户优惠券
            $doc.on('input', '.js-guestMobile', function () {
                if(!ck.check()){
                    return false;
                }
                var $$this = this,
                    errMsg = '请输入正确的手机号码！',
                    value = $$this.value,
                    mobileReg = /^(?:\+\d{2})?1[1-9]\d{9}$/gi;

                if (!mobileReg.test(value)) {
                    seajs.use('check', function (ck) {
                        ck.showCustomMsg(errMsg, $$this);
                    });
                    $$this.value = value.replace(/\D+/gi, '');
                    return false;
                } else if(value != oldGuestMobile){
                    if (oldGuestMobile) {
                        dg.confirm('更换手机号码将重置卡券信息', function () {
                            Components.cleanCouponCache();
                            //使用他人优惠信息时需要输入他人手机号
                            var discountSelectedBo = {
                                guestMobile: value,
                                selectedCard:null,
                                selectedComboList: null,
                                selectedCouponList: null
                            };
                            //最后请求传参
                            var discountOrderBo = {
                                orderId: $('#orderId').val(),
                                discountSelectedBo: discountSelectedBo
                            }
                            Components.$broadcast.distribute(topic, args.type, discountOrderBo);
                        }, function(){
                            $('.js-guestMobile').val(oldGuestMobile);
                        });
                    } else {
                        Components.getCouponData();
                    }
                }
            });
            //如果输入错误，回填上一次正确的电话号码
            $doc.on('blur','.js-guestMobile',function(){
                var $$this = this,
                        value = $$this.value,
                        mobileReg = /^(?:\+\d{2})?1[1-9]\d{9}$/gi;

                if (!mobileReg.test(value)) {
                    $('.js-guestMobile').val(oldGuestMobile);
                    Components.getCouponData();
                }
            })

            //记录上一次输入值
            $doc.on('focus', '.js-use-count', function () {
                lastVal = $(this).val();
            });
            //计次卡使用次数
            $doc.on('change', '.js-use-count', function () {
                if(!ck.check()){
                    return false;
                }
                var $this = $(this);
                var useNum = $.trim($this.val());
                if (useNum != '' && useNum > 0){
                    $(this).val(useNum);
                } else {
                    $(this).val(1);
                    return false;
                }
                Components.getCouponData(true);
            });

            //选择通用券
            $doc.on('focus', '.js-discount-amount', function () {
                lastAmount = $(this).val();
            });
            $doc.on('blur', '.js-discount-amount', function () {
                if(!ck.check()){
                    return false;
                }
                Components.getCouponData(null,true);
            });
        });

        Components.$broadcast.subscribe(topic, function (result, params) {
            var ischange = false;
            if (result && result.success) {
                //缓存结果数据
                var carLicense = args.carLicense;
                var newGuestMobile = params.discountSelectedBo.guestMobile;
                var newResult = Components.getCache(result, carLicense, oldGuestMobile == newGuestMobile);
                var discountContent = $('#discountContent');
                var guestCouponConunt = $('#guestCouponConunt');
                var countMoreText = discountContent.find('.js-vouchermore').text();
                var guestMoreText = guestCouponConunt.find('.js-vouchermore').text();

                if(newResult.couponCount == 0){
                    $('.no-coupon-tips').show();
                }else{
                    $('.no-coupon-tips').hide();
                }

                if(newResult.guestCouponCount == 0 && newGuestMobile){
                    $('.guest-no-customer').css('display','inline-block');
                }else{
                    $('.guest-no-customer').css('display','none');
                }

                seajs.use('art', function(at) {
                    var html = at('discountCouponTpl', {json: newResult});
                    discountContent.html(html);
                    var gustHtml = at('guestCouponTpl', {json: newResult});
                    guestCouponConunt.html(gustHtml);
                });

                $('.js-guestMobile').val(newGuestMobile);
                if(oldGuestMobile && oldGuestMobile != newGuestMobile){
                    ischange = true;
                }
                oldGuestMobile = newGuestMobile;

                if (countMoreText == '点击查看更多' || countMoreText == '') {
                    discountContent.find('.js-vouchermore').text('点击查看更多');
                    discountContent.find('.js-vouchermore').siblings('.voucher-hide').hide();
                } else {
                    discountContent.find('.js-vouchermore').text('点击隐藏');
                    discountContent.find('.js-vouchermore').siblings('.voucher-hide').show();
                }

                if (guestMoreText == '点击查看更多' || guestMoreText == '') {
                    guestCouponConunt.find('.js-vouchermore').text('点击查看更多');
                    guestCouponConunt.find('.js-vouchermore').siblings('.voucher-hide').hide();
                } else {
                    guestCouponConunt.find('.js-vouchermore').text('点击隐藏');
                    guestCouponConunt.find('.js-vouchermore').siblings('.voucher-hide').show();
                }

            } else {
                if (combo) {
                    $('.js-use-count').val(lastVal);
                }
                if (universal) {
                    $('.js-discount-amount').val(lastAmount);
                }
                if(oldGuestMobile){
                    $('.js-guestMobile').val(oldGuestMobile);
                }
            }

            args.callback(oldGuestMobile, ischange);
        });

        var _default = ({
            type: 1,
            /**
             * 车牌，用于缓存
             */
            carLicense:null,
            callback: function () {
            }
        });

        Components.CouponCard = {
            init: function(options) {
                args = $.extend({}, _default, options);
            },
            getCouponData: Components.getCouponData,
            getVoucherData: Components.getVoucherData,
            getCache: Components.getCache
        };

        Components.getCouponData = function(_combo, _universal) {
            var otherGuestMobile = $('.js-guestMobile').val();
            var orderId = $('#orderId').val();
            //本次结算所使用的计次卡服务信息
            var selectedComboList = [];
            var $scope;
            if(otherGuestMobile == oldGuestMobile){
                $scope = $('#discountContent,#guestCouponConunt');
            } else {
                $scope = $('#discountContent');
            }
            //遍历选中的计次卡
            $scope.find(".js-combo").each(function () {
                var $this = $(this);
                if (!$this.hasClass("div-disable") && $this.find(".voucher-t").hasClass("voucher-hover")) {
                    var $combo = $this.find('.voucher-hover');
                    var comboServiceId = $combo.find('.comboServiceId').val();
                    var count = $.trim($(this).find('.js-use-count').val());
                    var accountComboSelectedVo = {
                        comboServiceId: comboServiceId,
                        count: count
                    };
                    selectedComboList.push(accountComboSelectedVo);
                }
            });

            //本次结算所使用的优惠券信息
            var selectedCouponList = [];
            //遍历选中的优惠券
            $scope.find(".js-coupon").each(function () {
                var $this = $(this);
                if (!$this.hasClass("div-disable") && $this.find(".voucher-t").hasClass("voucher-hover")) {
                    var couponIdBox = $(this).find('.voucher-hover');
                    var couponId = couponIdBox.find('.couponId').val();
                    var couponDiscountAmount = $.trim($(this).find('.js-discount-amount').val());
                    var SelectedCouponBo = {
                        couponId: couponId,
                        couponDiscountAmount: couponDiscountAmount
                    };
                    selectedCouponList.push(SelectedCouponBo);
                }
            });

            //判断券是否有隐藏部分
            var countMoreText = $('#discountContent').find('.js-vouchermore').text();
            var guestMoreText = $('#guestCouponConunt').find('.js-vouchermore').text();
            if (countMoreText == '') {
                $('#discountContent').find('.js-vouchermore').text('点击查看更多')
            }
            if (guestMoreText == '') {
                $('#guestCouponConunt').find('.js-vouchermore').text('点击查看更多')
            }

            //使用他人优惠信息时需要输入他人手机号
            var discountSelectedBo = {
                guestMobile: otherGuestMobile,
                selectedComboList: selectedComboList,
                selectedCouponList: selectedCouponList
            };

            //最后请求传参
            var discountOrderBo = {
                orderId: orderId,
                discountSelectedBo: discountSelectedBo
            };

            combo = _combo;
            universal = _universal;

            Components.$broadcast.distribute(topic, args.type, discountOrderBo);
        };



        //整合数据
        Components.getVoucherData = function(){
            var flowBoList = getOrderDiscountFlow($('#discountContent'));
            var guestFlowBoList = getOrderDiscountFlow($('#guestCouponConunt'));
            var orderDiscountFlowBoList = flowBoList.concat(guestFlowBoList);
            return orderDiscountFlowBoList;
        };
        //获取数据（确认帐单）
        function getOrderDiscountFlow(scope) {
            var orderDiscountFlowBoList = [];
            //遍历选中的计次卡
            scope.find(".js-combo").each(function () {
                var $this = $(this);
                if (!$this.hasClass("div-disable") && $this.find(".voucher-t").hasClass("voucher-hover")) {
                    var combo = $this.find('.voucher-hover').data('combo');
                    //优惠金额
                    var discountAmount = combo.discount;
                    var discountName = combo.comboName;
                    var comboServiceId = combo.comboServiceId;
                    var useCount = combo.useCount;
                    var accountId = combo.accountId;
                    var orderDiscountFlowVo = {
                        accountId: accountId,
                        comboServiceId: comboServiceId,
                        discountType: 10,
                        discountName: discountName,
                        discountAmount: discountAmount,
                        useCount: useCount
                    };
                    orderDiscountFlowBoList.push(orderDiscountFlowVo);
                }
            });
            //遍历选中的优惠券
            scope.find(".js-coupon").each(function () {
                var $this = $(this);
                if (!$this.hasClass("div-disable") && $this.find(".voucher-t").hasClass("voucher-hover")) {
                    var cashcoupon = $this.find('.voucher-hover').data('cashcoupon');
                    var couponId = cashcoupon.couponId;
                    //1优惠券2通用券
                    var couponType = cashcoupon.couponType;
                    //优惠金额
                    var discountAmount = cashcoupon.finalDiscount;
                    //券类型
                    var discountType;
                    if (couponType == 'CASH_COUPON') {
                        //现金券
                        discountType = 8;
                    } else if (couponType == 'UNIVERSAL_COUPON') {
                        //通用券
                        discountType = 9;
                    } else {
                        discountType = 0;
                    }
                    var discountName = cashcoupon.couponName;
                    var discountSn = cashcoupon.couponSn;
                    var accountId = cashcoupon.accountId;
                    var orderDiscountFlowVo = {
                        couponId: couponId,
                        accountId: accountId,
                        discountType: discountType,
                        discountName: discountName,
                        discountSn: discountSn,
                        discountAmount: discountAmount
                    };
                    orderDiscountFlowBoList.push(orderDiscountFlowVo);
                }
            });
            return orderDiscountFlowBoList;
        }

        //缓存数据
        var carLicense,sortedCashCouponList = [],sortedComboList = [],sortedUniversalCouponList = [],
                sortedGuestCashCouponList = [],sortedGuestComboList = [],sortedGuestUniversalCouponList = [];
        /**
         * 缓存券排序信息
         * @param result 后端返回的result对象
         * @param license  车牌，用于缓存
         * @param isNotChangeMobile  手机号是否未改变
         * @returns {{sortedCashCouponList: *, sortedComboList: *, sortedUniversalCouponList: *, sortedGuestCashCouponList: *, sortedGuestComboList: *, sortedGuestUniversalCouponList: *}}
         */
        Components.getCache = function(result, license, isNotChangeMobile){
            var data = result.data,
                    tempSortedCashCouponList,tempSortedComboList,tempSortedUniversalCouponList,
                    tempSortedGuestCashCouponList,tempSortedGuestComboList,tempSortedGuestUniversalCouponList;

            /**
             * 重新给他人券赋值
             */
            function getGuestCoupon() {
                //优惠券
                tempSortedGuestCashCouponList = data.sortedGuestCashCouponList;
                sortedGuestCashCouponList = [];
                $.each(tempSortedGuestCashCouponList || [], function (i, obj) {
                    sortedGuestCashCouponList.push(obj.couponId);
                });
                //计次卡
                tempSortedGuestComboList = data.sortedGuestComboList;
                sortedGuestComboList = [];
                $.each(tempSortedGuestComboList || [], function (i, obj) {
                    sortedGuestComboList.push(obj.comboServiceId);
                });
                //通用券
                tempSortedGuestUniversalCouponList = data.sortedGuestUniversalCouponList;
                sortedGuestUniversalCouponList = [];
                $.each(tempSortedGuestUniversalCouponList || [], function (i, obj) {
                    sortedGuestUniversalCouponList.push(obj.couponId);
                });
            }

            if(!carLicense || carLicense != license){
                //保存id
                //自己的券
                tempSortedCashCouponList = data.sortedCashCouponList;
                sortedCashCouponList = [];
                $.each(tempSortedCashCouponList || [], function(i, obj) {
                    sortedCashCouponList.push(obj.couponId);
                });
                tempSortedComboList = data.sortedComboList;
                sortedComboList = [];
                $.each(tempSortedComboList || [], function(i, obj) {
                    sortedComboList.push(obj.comboServiceId);
                });
                tempSortedUniversalCouponList = data.sortedUniversalCouponList;
                sortedUniversalCouponList = [];
                $.each(tempSortedUniversalCouponList|| [], function(i, obj) {
                    sortedUniversalCouponList.push(obj.couponId);
                });
                //其他人的券
                getGuestCoupon();
                carLicense = license;
            } else {
                tempSortedCashCouponList = getList(data.sortedCashCouponList,sortedCashCouponList);
                tempSortedComboList = getList(data.sortedComboList,sortedComboList);
                tempSortedUniversalCouponList = getList(data.sortedUniversalCouponList,sortedUniversalCouponList);
                if(!isNotChangeMobile){
                    //重新赋值
                    getGuestCoupon();
                } else {
                    tempSortedGuestCashCouponList = getList(data.sortedGuestCashCouponList,sortedGuestCashCouponList);
                    tempSortedGuestComboList = getList(data.sortedGuestComboList,sortedGuestComboList);
                    tempSortedGuestUniversalCouponList = getList(data.sortedGuestUniversalCouponList,sortedGuestUniversalCouponList);
                }
            }
            var couponCount = tempSortedCashCouponList.length +
                              tempSortedComboList.length +
                              tempSortedUniversalCouponList.length;
            var guestCouponCount = tempSortedGuestCashCouponList.length +
                                   tempSortedGuestComboList.length +
                                   tempSortedGuestUniversalCouponList.length;
            var newResult = {
                sortedCashCouponList : tempSortedCashCouponList,
                sortedComboList : tempSortedComboList,
                sortedUniversalCouponList : tempSortedUniversalCouponList,
                sortedGuestCashCouponList : tempSortedGuestCashCouponList,
                sortedGuestComboList : tempSortedGuestComboList,
                sortedGuestUniversalCouponList : tempSortedGuestUniversalCouponList,
                couponCount : couponCount,
                guestCouponCount : guestCouponCount
            };
            return newResult;
        };

        function getList(target, cache){
            var dist = [],len = cache.length;
            if(!len){
                $.each(target,function(){
                    var id = this.couponId || this.comboServiceId;
                    cache.push(id);
                });
                return target;
            }
            $.each(target,function(){
                for (var i = 0; i < len; i++) {
                    var id = this.couponId || this.comboServiceId;
                    if (id == cache[i]) {
                        dist[i] = this;
                        break;
                    }
                }
            });
            return dist;
        }

        Components.cleanCouponCache = function(){
            carLicense = null;
        }
    });

</script>




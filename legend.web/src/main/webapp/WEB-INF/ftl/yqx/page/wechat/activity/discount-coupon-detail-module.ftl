<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/activity/discount-coupon-detail-module.css?c88812ef3a20e92284b76aea23e2437a"/>
<fieldset class="module-vo" id="moduleCouponDiscount">
    <input type="hidden" name="uniqueCode" value="${moduleVo.uniqueCode}">
    <input type="hidden" name="moduleIndex" value="${moduleVo.moduleIndex}">
    <input type="hidden" name="moduleType" value="${moduleVo.moduleType}">
    <i class="fieldset-title coupon-choose-title">选择卡券</i>
    <div class="form-item coupon-scroll-wrapper">
        <div class="coupon-item-collection">
        <#list moduleVo.couponVoList as coupon>
            <div class="coupon-item">
                <div class="coupon-item-content js-coupon-content clearfix">
                    <div class="coupon-rule-description">${coupon.couponRuleDescription}</div>
                    <img class="coupon-img fl" src="${BASE_PATH}/static/img/page/wechat/activity/coupon-img.png" />
                    <div class="item-content-right fr">
                        <div class="coupon-name js-show-tips ellipsis-1">${coupon.couponName}</div>
                        <div class="coupon-explain">
                            卡券说明：${coupon.couponRuleDescriptionDelHtml}
                        </div>
                    </div>
                </div>
                <div class="coupon-instruction">
                    <div class="instruction-item">满：${coupon.satisfyUse}元使用</div>
                    <div class="instruction-item">抵扣：${coupon.deductibleAmount}元</div>
                    <div class="instruction-item">抵扣券面值：${coupon.deductibleAmount}元</div>
                    <div class="instruction-item">最低价：0元</div>
                </div>
                <div class="coupon-operation clearfix">
                    <input type="hidden" class="coupon-id" value="${coupon.id}">
                    <input type="hidden" class="coupon-source" value="${coupon.couponSource}">
                    <input type="hidden" class="received-coupon-count" value="${coupon.receivedCouponCount}">
                    <div class="form-label">
                        数量
                    </div>
                    <div class="form-item coupon-num">
                        <input type="text" class="yqx-input yqx-input-icon js-coupon-num" value="${coupon.couponTotal}" data-v-type="required | integerNotZero" <#if coupon.couponTotal != null>disabled</#if>>
                        <span class="fa">张</span>
                    </div>
                    <button class="yqx-btn fr js-coupon-join <#if coupon.couponTotal == null>yqx-btn-2<#else>yqx-btn-1</#if>"><#if coupon.couponTotal == null>参加<#else>取消参加</#if></button>
                </div>
            </div>
        </#list>
        </div>
    </div>
</fieldset>

<!--活动内容弹窗模板 start-->
<script type="text/template" id="couponContentTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            卡券
        </div>
        <div class="bounce-content-wrapper clearfix">
            <%=#content%>
        </div>
    </div>
</script>
<!--活动内容弹窗模板 end-->
<script src="${BASE_PATH}/static/js/page/wechat/activity/discount-coupon-detail-module.js?242cd722894722f464e81bdaac3a3075"></script>

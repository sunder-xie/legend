<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/favormall-coupon-edit.css?cbd512f5effbe18cde9113dd794fa49e"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="aside-main">
        <div class="order-right">
            <div class="order-head clearfix">
                <h1 class="headline">卡券商城设置<span class="font-normal">-新增优惠券</span></h1>
            </div>
        </div>
        <div class="order-body">
            <div class="choose-coupon">
                <div class="form-label form-label-must w100">
                    选择优惠券
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon js-choose-coupon" data-v-type='required'>
                    <input type="hidden" name="couponTypeId">
                    <span class="fa icon-angle-down"></span>
                </div>
                <div class="card-btn">
                    <a class="yqx-btn yqx-btn-1 yqx-btn-small" href="${BASE_PATH}/account/setting" target="_blank">管理优惠券</a>
                    <a class="yqx-btn yqx-btn-1 yqx-btn-small" href="${BASE_PATH}/account/coupon/create" target="_blank">新增优惠券</a>
                </div>
            </div>
            <div class="send-num">
                <div class="form-label form-label-must w100">
                    赠送数量
                </div>
                <div class="form-item">
                    <input type="text" name="givingNumber" class="yqx-input yqx-input-icon giving-number" data-v-type='required | integer'>
                    <span class="fa">张</span>
                </div>
            </div>
            <button class="yqx-btn yqx-btn-3 save" id="js-save">保存</button>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/wechat/favormall-coupon-edit.js?071194c7dcc5096c4d7c32a51fdbc100"></script>
<#include "yqx/layout/footer.ftl" >
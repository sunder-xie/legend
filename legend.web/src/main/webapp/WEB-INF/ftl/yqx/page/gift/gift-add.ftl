<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/gift/gift-add.css?7bfa53a14d9880a4062803c5438e670b">
<div class="yqx-wrapper clearfix">
    <div class="left-menu" style="float: left;">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="aside-main">
        <h1 class="headline">礼品发放</h1>

        <form name="searchForm" class="container" id="searchForm">
            <div class="show-grid">
                <div class="col-6 form-item license-box">
                    <label class="must">　　车牌</label><div class="form-item">
                    <input class="yqx-input" name="license"
                           placeholder="请输入车牌"
                                              data-v-type="required|licence"
                           ><a href="${BASE_PATH}/shop/customer/edit?refer=gift-add"
                               target="_blank" class="add-customer js-add-customer yqx-btn yqx-btn-3">新增客户</a>
                    </div>
                </div>

                <div class="col-6 form-item">
                    <label>车辆型号</label><input class="yqx-input" name="carModel2" disabled/>
                    <input class="yqx-input" name="byName" hidden value="${customerCar.byName}" width="100"/>
                    <input class="yqx-input" hidden name="customerCarId" value="${customerCar.id}"/>
                </div>

            </div>
            <div class="show-grid">
                <div class="col-6 form-item">
                    <label>车主姓名</label><input class="yqx-input" name="customerName" disabled>
                </div>
                <div class="col-6 form-item">
                    <label>车主电话</label><input class="yqx-input" name="mobile" vType="phone" disabled>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-6 form-item">
                    <label class="must">礼品券码</label><div class="form-item">
                    <input class="yqx-input width-100" name="giftSn" data-v-type="required" placeholder="请输入礼品券码"/>
                    </div>
                </div>
                <div class="form-item col-6">
                    <label class="must">礼品发放人</label>

                    <div class="form-item manager-box">
                        <input class="yqx-input js-manager"
                                data-v-type="required"
                                placeholder="请选择礼品发放人"><input class="yqx-input" type="hidden"
                                                              name="registrantId" value="${SESSION_USER_ID}">
                        <span class="fa icon-angle-down"></span>
                    </div>

                </div>
            </div>

            <div class="show-grid">
                <div class="form-item col-12 remark-box">
                <label>　　备注</label><input class="yqx-input" name="giftNote" value="${gift.giftNote}">
                </div>
            </div>
        </form>
        <div class="show-grid">
            <button class="yqx-btn yqx-btn-2 js-publish">发布</button>
            <button class="yqx-btn yqx-btn-1 js-back">返回</button>
        </div>
    </div>
</div>
<!-- 公用车牌模板 -->
<#include "yqx/tpl/common/car-licence-tpl.ftl">
<script src="${BASE_PATH}/static/js/page/gift/gift-add.js?5bea0256244904d441789cfc66645c13"></script>
<#include "yqx/layout/footer.ftl">

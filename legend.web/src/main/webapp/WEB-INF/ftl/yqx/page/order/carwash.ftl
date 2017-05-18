<!-- create by sky 20160406 新建洗车单 -->
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/carwash.css?c8deb83b6a52906d9e68558e5eed7c58"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <input type="hidden" id="isFirst" value="${isFirst}"/>
        <input type="hidden" id="shopGroupId" value="${SESSION_SHOP_ID}"/>
        <!-- 内容区头部标题区 start -->
        <div class="order-head clearfix">
            <h1 class="headline fl">新建洗车单</h1>
            <!-- 工作进度 start -->
            <div class="order-process clearfix fr">
                <div class="order-step order-step-finish">
                    <div class="order-step-circle">1</div>
                    <p class="order-step-title">开洗车单</p>
                </div>
                <div class="order-step">
                    <div class="order-step-circle">2</div>
                    <p class="order-step-title">洗车单结算</p>
                </div>
            </div>
            <!-- 工作进度 end -->
        </div>
        <!-- 内容区头部标题区 end -->
        <div class="order-content carwash_form">
            <div class="order-group">
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label w62 form-label-must">车牌</div>
                        <div class="form-item w160 yqx-downlist-wrap">
                            <input type="text" class="yqx-input js-carlicense-input"
                                   data-label="车牌" name="carLicense" value="${license}"
                                   data-v-type="required | licence" placeholder="请输入车牌"/>
                            <input type="hidden" name="appointId" value="${appointId}"/>
                            <input type="hidden" name="downPayment" value="${downPayment}" class="js-downPayment"/>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label w62 form-label-must">服务顾问</div>
                        <div class="form-item w160 yqx-downlist-wrap">
                            <input type="text" name="receiverName" data-label="服务顾问"
                                   class="yqx-input yqx-input-icon js-carwash-receiver" value="${SESSION_USER_NAME}"
                                   data-v-type="required" placeholder="请输入服务顾问"/>
                            <input type="hidden" name="receiver" value="${SESSION_USER_ID}"/>
                            <span class="fa icon-angle-down"></span>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label w62 form-label-must">开单日期</div>
                        <div class="form-item w160">
                            <input type="text" class="yqx-input yqx-input-icon js-createTime" data-label="开单日期"
                                   name="createTimeStr" placeholder="请选择开单日期" value="${.now?string("yyyy-MM-dd HH:mm")}"
                                   data-v-type="required"/>
                            <span class="fa icon-calendar"></span>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label w62 form-label-must">洗车工</div>
                        <div class="form-item w160 yqx-downlist-wrap">
                            <input type="text" class="yqx-input yqx-input-icon js-show-tips js-carwash-worker"
                                   data-label="洗车工"
                                   value="<#if isFirst==0>${SESSION_USER_NAME}<#else>${orderServices.workerNames}</#if>"
                                   data-v-type="required" placeholder="请选择洗车工姓名"/>
                            <input type="hidden" name="workerIds"
                                   value="<#if isFirst==0>${SESSION_USER_ID}<#else>${orderServices.workerIds}</#if>"/>
                            <span class="fa icon-angle-down"></span>
                        </div>
                    </div>
                    <div class="col-8">
                        <div class="form-label w62">客户单位</div>
                        <div class="form-item yqx-downlist-wrap w-440">
                            <input type="text" class="yqx-input js-show-tips" name="company" value="${company}"
                                   data-v-type="maxLength:100" maxlength="100" placeholder="请输入客户单位"/>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label w62 form-label-must">服务项目</div>
                    <ul class="service-with single-tag js-in-money">
                    <#list shopServiceList as services>
                        <li class="service-name js-show-tips tag-control miw-135 <#if services_index==0>selected</#if> js-bz-service"
                            data-id="${services.id}"
                            data-type="${services.type}"
                            data-servicesn="${services.serviceSn}"
                            data-servicecatid="${services.categoryId}"
                            data-servicename="${services.name}"
                            data-servicecatname="${services.serviceCatName}"
                            data-service-price="${services.servicePrice}">
                            <span>${services.name}：<strong class="hl-r money">${services.servicePrice}</strong>元</span>
                        </li>
                    </#list>
                        <li class="tag-control tag-customize tag-select miw-140 js-more-service">
                            <div class="form-item m-top">
                                <input type="text" class="service-name js-show-tips js-carwash-type money carwash-center" placeholder="更多洗车"
                                       readonly/>
                                <span class="fa icon-angle-down"></span>
                            </div>
                            <div class="js-id hide"></div>
                            <div class="js-type hide"></div>
                            <div class="js-servicesn hide"></div>
                            <div class="js-servicecatid hide"></div>
                            <div class="js-servicename hide"></div>
                            <div class="js-servicecatname hide"></div>
                            <div class="money js-serviceprice hide"></div>
                        </li>
                        <li class="tag-customize tag-control js-custom-service">
                            <div class="form-item w120">
                                <input type="text" class="customize-input js-custom-money money"
                                       value="${shopServiceList[0].servicePrice}" data-v-type="number" placeholder="自定义价格"
                                       maxlength="8"/>
                                <span class="fa">元</span>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="show-grid border-t">
                    <div class="form-label w62 form-label-must">优惠金额</div>
                    <ul class="single-tag money-single">
                        <li class="tag-customize miw-160 discount-money-box">
                            <div class="form-item w160 js-discount-box">
                                <input type="text" class="yqx-input yqx-input-icon discount-money-input js-discount-money"
                                       value="0" data-v-type="number" placeholder="优惠金额"/>
                                <span class="fa">元</span>
                            </div>
                            <input type="hidden" id="js-discount-memberCardId" value="">
                            <input type="hidden" id="js-discount-cardNumber" value="">
                            <input type="hidden" id="js-discount-cardDiscountReason" value="">
                        </li>
                        <#--<li class="tag-customize miw-160">-->
                            <#--<label>-->
                            <#--<input type="checkbox" class="js-discount discount-input"/>-->
                            <#--<span class="discount-text ellipsis-1 js-show-tips"></span>-->
                            <#--</label>-->
                        <#--</li>-->
                    </ul>
                    <div class="discount-amount-vip-card-box" id="discountAmountVIPCardBox">

                    </div>
                </div>
            </div>
            <div class="order-group">
                <div class="show-grid">
                    <div class="form-label w62 form-label-must">结算方式</div>
                    <ul class="single-tag js-sett-type">
                    <#list paymentList as payment>
                        <#if payment.name =='现金'>
                            <li class="tag-control miw-115 selected"
                                data-id="${payment.id}">
                                <span class="tag-label">${payment.name}</span>
                            </li>
                        </#if>
                    </#list>
                        <li class="tag-control miw-115">
                            <span class="tag-label">会员卡余额</span>
                        </li>
                        <li class="tag-control miw-115 coupon-and-combo">
                            <span class="tag-label">优惠券</span>
                        </li>
                        <li class="tag-control miw-115">
                            <span class="tag-label">其他</span>
                        </li>
                    </ul>
                    <!-- 现金 start -->
                    <div class="sett-type-box"></div>
                    <!-- 现金 end -->
                    <!-- 会员卡余额 start -->
                    <#--<div class="sett-type-box sett-member-card hide">-->
                    <#--</div>-->
                    <div class="sett-type-box hide" id="settMemberCardBox"></div>
                    <!-- 会员卡余额 end -->
                    <!-- 会员卡优惠券 start -->
                    <div class="sett-type-box sett-member-coupon coupon-check-box hide">
                        <#include "yqx/tpl/account/coupon-tpl.ftl">
                    </div>
                    <!-- 会员卡优惠券 end -->
                    <!-- 其他结算方式 start -->
                    <div class="sett-type-box sett-type-other js-sett-type-other hide">
                        <ul class="single-tag">
                        <#list paymentList as payment>
                            <#if payment.name !='现金'>
                                <li class="tag-control miw-115"
                                    data-id="${payment.id}"><span
                                        class="tag-label">${payment.name}</span></li>
                            </#if>
                        </#list>
                        </ul>
                    </div>
                    <!-- 其他结算方式 end -->
                </div>

                <div class="show-grid" style="display: inline-table;">
                    <div class="form-label form-label-remark">备注</div>
                    <div class="form-item form-item-remark">
                        <input class="yqx-input js-show-tip" name="postscript" data-v-type="maxLength:200"
                               placeholder="请输入备注">
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label">应收金额</div>
                    <div class="form-item">
                        <span class="money-font js-cash"><#if shopServiceList[0].servicePrice>#{shopServiceList[0].servicePrice;m2M2}<#else>0.00</#if></span>元
                    </div>
                </div>
                <#if downPayment gt 0.00>
                <div class="show-grid">
                    <div class="form-label">预付定金</div>
                    <div class="form-item">
                        <span class="money-font "> #{downPayment;m2M2}</span>元
                    </div>
                </div>
                </#if>
            </div>
            <div class="order-group">
                <button id="settle" class="yqx-btn mr5 yqx-btn-2">确认收款</button>
                <button id="losses" class="yqx-btn yqx-btn-1">挂账</button>
                <button class="yqx-btn yqx-btn-1 js-return fr">返回</button>
            </div>
        </div>
    </div>
    <!-- 右侧内容区 end -->
</div>

<!-- 第一次进入洗车价格设置 弹窗 start -->
<script type="text/html" id="setting_carwash">
    <div class="setting_carwash">
        <p class="dialog-title">
            洗车价目
        </p>
        <ul class="setting_box">
        <#list shopServiceList as services>
            <li class="clearfix">
                <label>${services.name}:</label>
                <p>
                    <input type="hidden" name="id" value="${services.id}">
                    <input type="text" name="servicePrice" v_type='{"required":true}' label="${services.name}"
                           value="${services.servicePrice}" class="J_input_limit"
                           data-limit_type="price"/><span>元</span>
                </p>
            </li>
        </#list>
        </ul>
        <div class="btn_center dialog-btn">
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-1 setting_carwash_cancel">取消</a>
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-2 qxy_green_btn setting_carwash_submit">提交</a>
        </div>

    </div>
</script>
<#include "yqx/tpl/order/worker-multiple-tpl.ftl">
<!-- 第一次进入洗车价格设置 弹窗 end -->
<#include "yqx/tpl/account/broadcast-tpl.ftl">
<#include "yqx/tpl/account/vip-card-tpl.ftl">
<#include "yqx/tpl/account/code-dialog-tpl.ftl">
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script src="${BASE_PATH}/static/js/page/order/carwash.js?c79a50dd1eaa7be6c455955e07f78fcc"></script>
<!-- 脚本引入区 end -->


<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/appservice/appservice-edit.css?411da357c1368b8165cf62752476bc78"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline">发服务-设置服务
            <#if hasSettingFunc>
                <div class="manage-buttons fr">
                    <a class="yqx-btn yqx-btn-1" href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-edit"><i>新增服务</i></a>
                </div>
            </#if>
            </h1>
        </div>
        <div class="order-body">
            <div class="serviceForm">
                <fieldset>
                    <i class="fieldset-title"><i class="must"></i>选择服务:</i>
                    <div class="form-item yqx-downlist-wrap form-data-width">
                        <input class="yqx-input input-long select-input js-choose-service js-clear" placeholder="请输入" name="serviceName"
                               data-v-type="required" value="${shopServiceInfo.name}" no_submit>
                        <i class="fa icon-angle-down"></i>
                        <input type="hidden" name="isSuit" id="isSuit" value="0" no_submit>
                        <input type="hidden" name="id" id="serviceId" class="js-clear" value="${shopServiceInfo.id}">
                    </div>
                </fieldset>
                <fieldset>
                    <i class="fieldset-title"><i class="must"></i>车主服务类型:</i>
                    <div class="form-item yqx-downlist-wrap form-data-width">
                        <input class="yqx-input select-input js-service-type js-clear" placeholder="请选择" value="${shopServiceInfo.appCateName}" data-v-type="required">
                        <i class="fa icon-angle-down"></i>
                        <input type="hidden" class="js-clear" name="appCateId" id="appCateId" value="${shopServiceInfo.appCateId}">
                    </div>
                </fieldset>
                <fieldset>
                    <i class="fieldset-title"><i class="must"></i>价格显示方式:</i>
                    <div class="form-item">
                        <input class="yqx-input select-input js-price-type js-clear" placeholder="请选择"
                               value="<#if shopServiceInfo.priceType == 1>正常价格数值显示<#elseif shopServiceInfo.priceType == 2>到店洽谈
                               <#elseif shopServiceInfo.priceType == 3>免费</#if>"
                               data-v-type="required">
                        <i class="fa icon-angle-down"></i>
                        <input type="hidden" class="js-clear" name="priceType" id="priceType" value="${shopServiceInfo.priceType}">
                    </div>
                </fieldset>
                <fieldset>
                    <i class="fieldset-title"><i class="must"></i>售价:</i>
                    <div class="form-item">
                        <input class="yqx-input readonly-input js-clear" name="servicePrice" id="servicePrice" data-v-type="required | number"
                               value="${shopServiceInfo.servicePrice}"
                               <#if ((shopServiceInfo.suiteNum &gt; 0) || (shopServiceInfo.priceType == 2) || (shopServiceInfo.priceType == 3))>readonly</#if>
                               <#if shopServiceInfo.suiteNum &gt; 0>data-disable="true"</#if>
                                > 元
                        <span class="gray marL40">此处不支持套餐编辑价格</span>
                    </div>
                </fieldset>
                <fieldset>
                    <i class="fieldset-title"><i class="must"></i>市场价:</i>
                    <div class="form-item">
                        <input class="yqx-input readonly-input js-clear" name="marketPrice" id="marketPrice" value="${shopServiceInfo.marketPrice}"
                               data-v-type="required | number |marketPrice"
                               <#if ((shopServiceInfo.suiteNum &gt; 0) || (shopServiceInfo.priceType == 2) || (shopServiceInfo.priceType == 3))>readonly</#if>
                               <#if shopServiceInfo.suiteNum &gt; 0>data-disable="true"</#if>
                                > 元

                    </div>
                </fieldset>
                <fieldset>
                    <i class="fieldset-title"><i class="must"></i>预付金额:</i>
                    <div class="form-item">
                        <input class="yqx-input readonly-input js-clear" name="downPayment" id="downPayment" value="${shopServiceInfo.downPayment}"
                               data-v-type="required | number"
                        <#if isOpenPay == true && payStatus == 'open'>
                        <#else>
                               readonly
                        </#if>> 元
                    <#if isOpenPay == false>
                        <span class="gray marL40">你未开通微信支付无法设置，点击<a href="${BASE_PATH}/shop/settlement/online/online-payment" target="_blank">开通微信支付</a></span>
                    <#else>
                        <#if payStatus == 'close'>
                            <span class="gray marL40">你未开启微信支付无法设置，点击<a href="${BASE_PATH}/shop/conf/payment-mode" target="_blank">开启微信支付</a></span>
                        </#if>
                    </#if>
                    </div>
                </fieldset>
                <fieldset>
                    <i class="fieldset-title"><i class="must"></i>服务图片:</i>
                    <img class="image" id="service-img" src="${shopServiceInfo.imgUrl}" data-default="${shopServiceInfo.imgUrl}"
                         <#if shopServiceInfo.imgUrl == "">hidden</#if>
                            >
                    <input type="hidden" class="img-path js-clear" name="imgUrl" value="${shopServiceInfo.imgUrl}">
                    <div class="btn-upload">
                        <p>重新上传</p>
                        <i class="upload-icon"></i>
                        <input class="yqx-input js-service-img" type="file" accept="image/*">
                    </div>
                    <div class="upload-text">
                        <a href="javascript:;" class="js-default-img" hidden>我要默认图片</a>
                        <p>支持jpg/gif/png格式,单张大小<5M,最多上传一张图片。</p>
                    </div>
                </fieldset>
                <fieldset>
                    <i class="fieldset-title" id="service-description"><i class="must"></i>服务说明:</i>
                    <div class="form-item">
                        <div class="textarea-box">
                            <div class="textarea-header"></div>
                            <textarea class="textarea-content js-clear" name="serviceNote" data-v-type="required">${shopServiceInfo.serviceNote}</textarea>
                            <div class="textarea-footer">
                                <button class="yqx-btn blue-btn">上传图片
                                    <input class="yqx-input js-decoration-img" type="file" accept="image/*">
                                </button>
                                <span class="gray">支持jpg/gif/png格式,单张大小<5M,最多上传10张图片。</span>
                            </div>
                        </div>
                        <ul class="img-box">
                        <#list thirdServiceInfoList as list>
                            <li class="img-item">
                                <div class="arrows"></div>
                                <img class="image marR10" src="${list.imgUrl}" alt="">
                                <input class="img-path" type="hidden" value="${list.imgUrl}">
                                <i class="img-delete"></i>
                            </li>
                        </#list>
                            <li class="img-item" hidden>
                                <div class="arrows"></div>
                                <img class="image marR10" src="" alt="">
                                <input class="img-path" type="hidden">
                                <i class="img-delete"></i>
                            </li>
                        </ul>
                    </div>
                </fieldset>
            </div>
            <fieldset>
                <i class="fieldset-title"></i>
                <button class="yqx-btn yqx-btn-3 js-save">保存</button>
                <button href="${BASE_PATH}/shop/wechat/appservice/list" class="yqx-btn yqx-btn-1 js-back">返回</button>
            </fieldset>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/third-plugin/jquery-ui.min.js"></script>
<script src="${BASE_PATH}/static/js/common/base/util.js?411c94ad46cf0b9ced08a7f3ce95ed23"></script>
<script src="${BASE_PATH}/static/js/page/wechat/appservice/appservice-edit.js?c8a5367c1f784a797c310dd6cce31148"></script>
<#include "yqx/layout/footer.ftl" >

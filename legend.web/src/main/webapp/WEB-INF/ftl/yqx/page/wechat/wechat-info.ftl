<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-base.css?9f840e9f3c3f39c54bcbe9b7350edbe9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-edit.css?4aa8b2d8fff628e3fd3c9ce0dfbb5858"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/wechat-info.css?cf6c09db3c42d0df269af9093f476741"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline">资料维护<a class="yqx-btn yqx-btn-default help" href="${BASE_PATH}/shop/help?id=87">
                <i class="wechat-icons question"></i>帮助中心
            </a></h1>
        </div>
        <div class="order-body">
            <div class="content">
                <form>
                    <input value="2" name="opSubmitType" type="hidden">
                    <input value="${shopWechatVo.id}" name="id" type="hidden">
                    <div class="qrcode-box">
                        <img class="qrcode-img" src="${shopWechatVo.shopQrcode}">
                        <i class="qrcode-text">微信公众号二维码</i>
                    </div>
                    <fieldset>
                        <i class="fieldset-title">账户名称</i>
                        <input class="yqx-input input-long"
                               value="${shopWechatVo.accountName}"
                               readonly>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">微信公众号</i>
                        <input class="yqx-input"
                               value="${shopWechatVo.accountSn}"
                               readonly
                                >
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">邮箱</i>
                        <div class="form-item">
                            <input class="yqx-input input-long"
                                   value="${shopWechatVo.shopEmail}"
                                   data-v-type="email|maxLength:32"
                                   data-label="邮箱"
                                   name="shopEmail">
                        </div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">手机号码</i>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               value="${shopWechatVo.shopMobile}"
                               data-v-type="required|phone"
                               data-label="手机号码"
                               name="shopMobile">
                            </div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">座机号码</i>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               data-v-type="maxLength:18"
                               data-label="座机号码"
                               value="${shopWechatVo.shopTelephone}"
                               name="shopTelephone">
                            </div>
                    <#--TODO 格式校验-->
                        <label>请输入老板或运营人员的座机号码，包括：区号-电话-分机号</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">邮寄地址</i>
                        <div class="form-item">
                            <input class="yqx-input js-address"
                                   placeholder="选择省份"
                                   id="province" data-target="#city">
                            <input name="shopProvince" type="hidden"
                                   value="${shopWechatVo.shopProvince}">
                            <span class="fa">省</span>
                        </div>
                        <div class="form-item">
                            <input class="yqx-input js-address"
                                   placeholder="选择城市"
                                   id="city" data-target="#district">
                            <input name="shopCity" type="hidden"
                                   value="${shopWechatVo.shopCity}">
                            <span class="fa">市</span>
                        </div>
                        <div class="form-item">
                            <input name="shopDistrict" class="yqx-input js-address"
                                   placeholder="选择县/区"
                                   id="district" data-target="#street">
                            <input name="shopDistrict" type="hidden" value="${shopWechatVo.shopDistrict}">
                            <span class="fa">县/区</span>
                        </div>
                        <div class="form-item">
                            <input class="yqx-input js-address"
                                   placeholder="选择街道"
                                   id="street">
                            <input name="shopStreet" type="hidden"
                                   value="${shopWechatVo.shopStreet}">
                            <span class="fa">街道</span>
                        </div>
                        <label>请输入老板或运营人员所在的地址</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">详细地址</i>
                        <div class="form-item">
                        <input name="shopAddress"
                               data-v-type="maxLength:100"
                               data-label="详细地址"
                               class="yqx-input input-long"
                               value="${shopWechatVo.shopAddress}"
                               >
                            </div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">邮政编码</i>
                        <div class="form-item">
                        <input name="shopPostcode"
                               data-v-type="zip"
                               data-label="邮政编码"
                               value="${shopWechatVo.shopPostcode}"
                               class="yqx-input input-short">
                            </div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">个人微信号</i>
                        <div class="form-item">
                        <input name="perWechatid"
                               data-v-type="maxLength:20"
                               data-label="个人微信号"
                               value="${shopWechatVo.perWechatid}"
                               class="yqx-input">
                            </div>
                        <label>请输入老板或运营人员手机号码绑定的微信号，后续需要扫描验证</label>
                    </fieldset>
                    <fieldset class="time-group">
                        <i class="fieldset-title">上线时间</i>
                        <input class="yqx-input"
                               value="${shopWechatVo.onlineTimeStr}"
                               readonly><i class="fieldset-title">认证时间</i><input class="yqx-input js-certificationTime" name="certificationTime"
                               value="${shopWechatVo.certificationTimeStr}"><i class="fieldset-title">签约时间</i><input class="yqx-input"
                               value="${shopWechatVo.signingTimeStr}"
                               readonly><i class="fieldset-title">到期时间</i><input class="yqx-input"
                               value="${shopWechatVo.expirationTimeStr}"
                               readonly>
                    </fieldset>
                </form>
                <fieldset>
                    <i class="fieldset-title"></i>
                    <button class="yqx-btn yqx-btn-3 js-next">保存</button>
                </fieldset>
            </div>
        </div>
    </div>
</div>

<script src="${BASE_PATH}/static/js/page/wechat/wechat-info.js?709d5928da4553f545dbae87debc6975"></script>
<#include "yqx/layout/footer.ftl" >
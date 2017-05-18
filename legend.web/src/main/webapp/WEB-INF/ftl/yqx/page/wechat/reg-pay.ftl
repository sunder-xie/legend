<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-base.css?9f840e9f3c3f39c54bcbe9b7350edbe9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-pay.css?2e877da15cd1b587de5e70cbcf007213"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline fl">微信公众号注册</h1>

            <div class="order-process clearfix fr">
                <div class="order-step order-step-finish">
                    <div class="order-step-circle">1</div>
                    <p class="order-step-title">支付</p>
                </div>
                <div class="order-step">
                    <div class="order-step-circle">2</div>
                    <p class="order-step-title">相关资料填写及上传</p>
                </div>
                <div class="order-step">
                    <div class="order-step-circle">3</div>
                    <p class="order-step-title">受理中</p>
                </div>
            </div>
        </div>
        <div class="order-body">
            <h2>支付信息</h2>
            <form action="${BASE_PATH}/shop/wechat/op/toPay" method="post">
            <div class="content">
                <i class="check-info">请选择<i class="must"></i></i>
            <#list payTplList as pay>
                <div class="radio-box">
                    <input type="radio" name="payTplId"
                           <#if pay.adpatType == 2>checked</#if>
                           class="pay js-pay-check" value="${pay.id}"><div class="service-pay <#if pay.adpatType == 2>pay-selected</#if>">
                        <div class="pay-head">
                            <h3>
                                <#if pay.adpatType == 1>有已认证服务号<i class="pay-tag tag-active">有认证</i></#if>
                                <#if pay.adpatType == 2>无已认证服务号<i class="pay-tag">无认证</i></#if>
                            </h3>

                        </div>
                        <p class="pay-content">
                        ${pay.tplDescribe}
                        </p>
                    </div>
                </div>
            </#list>
                <div class="info-box">
                    <p>“已认证服务号”如</p><div class="info">
                        <i class="yqx-icon"></i>

                        <div class="info-content">
                            <div class="info-tags">
                                <i class="info-tag tag-info">服务号</i><i class="info-tag tag-active">已认证</i>
                            </div>
                            <p>淘汽云修服务</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="pay-type">
                <div class="radio-box">
                    <i class="check-info">支付方式<i class="must"></i></i>
                    <input type="radio" name="payType" class="type-radio"
                           checked
                           data-target=".offline"  value="2"><label>线下支付</label>
                </div>
                <div class="pay-type-box">
                    <div class="single-tag">
                        <div class="type-select offline selected tag-control">
                            <h4><i class="phone-icon"></i>试用期间，如需开通请联系云修顾问</h4>
                        </div>
                        <div class="file-box">
                           <i class="title">请上传支付凭证</i>
                           <div class="btn-upload">
                               <input class="yqx-input js-file" type="file" accept="image/*" >
                               <input class="img-path" type="hidden" name="payVoucher">
                           </div>
                            <label>
                                <p>转账记录</p>
                                <p>支持.jpg .jpeg .bmp .gif .png格式照片，</p>
                                <p>大小不超过5M</p>
                            </label>
                        </div>

                    </div>
                </div>
            </div>
            <div class="confirm-line">
                <div class="agreement-box">
                    <input type="checkbox" name="" id="agree" class="agree" checked>
                    <a href="javascript:void(0)" title="查看协议详情" class="js-agreement">我已同意协议详情</a>
                </div>
                <button type="submit" class="yqx-btn yqx-btn-3 js-next">下一步</button>
            </div>
                </form>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/wechat/reg-pay.js?27642d2c027d418aff571685decc5c9d"></script>
<#include "yqx/tpl/wechat/wechat-agreement.ftl" >
<#include "yqx/layout/footer.ftl" >
<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-base.css?9f840e9f3c3f39c54bcbe9b7350edbe9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-processing.css?dc395aa09920c5b1e82ed1dac0929f32"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline fl">微信公众号注册</h1>

            <div class="order-process clearfix fr">
                <div class="order-step">
                    <div class="order-step-circle">1</div>
                    <p class="order-step-title">支付</p>
                </div>
                <div class="order-step">
                    <div class="order-step-circle">2</div>
                    <p class="order-step-title">相关资料填写及上传</p>
                </div>
                <div class="order-step order-step-finish">
                    <div class="order-step-circle">3</div>
                    <p class="order-step-title">受理中</p>
                </div>
            </div>
        </div>
        <div class="order-body">
            <div class="content">
                <div class="content-left">
                    <div class="mail-icon">
                        <div class="icon">
                        </div>
                    </div>
                </div><div class="content-right">
                    <h2>审核中</h2>
                    <p>1.如认证资料完整，5-7个工作日完成受理</p>
                    <p>2.受理期间若有微信电话确认，请配合完成</p>
                    <p>3.试用期间，仅开放部分城市，如有疑问，请与云修顾问联系！</p>
                    <p>4.如有其它疑问，请致电云修商家客服热线：<i class="icon-phone"></i>400-9937-288</p>
                    <a class="yqx-btn yqx-btn-default" href="${BASE_PATH}/shop/wechat/reg-edit">编辑资料</a>
                </div>
            </div>
        </div>
    </div>
</div>
<#include "yqx/layout/footer.ftl">

<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-base.css?9f840e9f3c3f39c54bcbe9b7350edbe9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/wx-apply.css?495d4cd4095f9c26221ce16a6e357149"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline fl">淘汽云修"微信公众号"开通流程</h1>
            <div class="order-process clearfix fr">
                <div class="order-step order-step-finish">
                    <div class="order-step-circle">1</div>
                    <p class="order-step-title">申请</p>
                </div>
                <div class="order-step">
                    <div class="order-step-circle">2</div>
                    <p class="order-step-title">审核</p>
                </div>
                <div class="order-step">
                    <div class="order-step-circle">3</div>
                    <p class="order-step-title">授权</p>
                </div>
            </div>
        </div>
        <div class="order-body clearfix">
            <div class="content-box box-left">
                <i class="icon"></i>
                <h3>无服务号
                无认证服务号
                </h3>
                <div class="operating-box">
                    <button class="yqx-btn yqx-btn-3 js-register">去微信公众平台注册</button>
                </div>
                <div class="instruction-box dash">
                    <p>操作说明</p>
                    <p><a href="http://mp.weixin.qq.com/s?__biz=MjM5ODk5NDQ4NQ==&mid=502906595&idx=1&sn=ad7b70758adffc6d03fdd4386b3965aa&scene=1&srcid=0726GoKmIMit1h6QyLB560ec#wechat_redirect" target="_blank" title="查看协议详情">《微信服务号注册及认证流程》</a></p>
                </div>
            </div>
            <div class="content-box box-right">
                <i class="icon"></i>
                <h3>如您已经有通过认证的服务号
                请直接提交“申请开通”
                </h3>
                <div class="operating-box">
                    <button class="yqx-btn yqx-btn-3 js-apply">申请开通</button>
                    <div class="agreement-box">
                        <input type="checkbox" name="" id="agree" class="agree" checked>
                        我已阅读并完全同意<a href="javascript:void(0)" title="查看协议详情" class="js-agreement">《协议》</a>
                    </div>
                </div>
                <span id="span-text">如：</span>
                <div class="example-box dash">
                    <i class="icon"></i>
                    <div class="fl">
                        <span id="service">服务号</span>
                        <span id="approve">已认证</span>
                        <p>淘汽云修服务</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/wechat/apply/wx-apply.js?810c22c155967e8871c5ff83a4f8ebea"></script>
<#include "yqx/tpl/wechat/wechat-agreement.ftl" >
<#include "yqx/layout/footer.ftl" >
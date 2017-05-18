<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-base.css?9f840e9f3c3f39c54bcbe9b7350edbe9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/wx-grant.css?af8cd4918961b3c0f475197375f7a9f9"/>
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
                <div class="order-step order-step-finish">
                    <div class="order-step-circle">2</div>
                    <p class="order-step-title">审核</p>
                </div>
                <div class="order-step order-step-finish">
                    <div class="order-step-circle order-step-finish">3</div>
                    <p class="order-step-title">授权</p>
                </div>
            </div>
        </div>
        <div class="order-body">
        <#if shopWechatVo.shopStatus == 6>
            <div class="content-box fail">
                <h3>授权失败<i class="fail-icon"></i></h3>
                <p>您的本次授权失败，<strong>请确保权限都已经授权</strong></p>
                <p>如有问题请联系您的云修顾问</p>
                <button class="yqx-btn yqx-btn-3 js-grant">授权</button>
            </div>
        <#elseif shopWechatVo.shopStatus == 7>
            <div class="content-box process">
                <h3>数据初始化中<i class="process-icon"></i></h3>
                <p>请刷新，数据初始化结束后，将跳转到微信公众号主页。</p>
                <p style="display:none;" class="js-load-grant-again">刷新3次后，还不跳转，请再次授权。</p>
                <button class="yqx-btn yqx-btn-3 js-refresh">刷新</button>
                <button style="display:none;" class="yqx-btn yqx-btn-2 js-grant-again js-load-grant-again">再次授权</button>
            </div>
        </#if>
            <div class="content-box affirm" hidden="hidden">
                <h3>授权确认<i class="affirm-icon"></i></h3>
                <p>您确认已经授权成功了么？</p>
                <button class="yqx-btn yqx-btn-3 js-yes marR10">已授权</button>
                <button class="yqx-btn yqx-btn-2 js-no">授权中断</button>
            </div>
        </div>
    </div>
</div>
<!--确认弹窗-->
<script type="text/html" id="grantConfirmTpl">
    <div class="dialog">
        <div class="dialog-title">提示</div>
        <div class="dialog-con">
            <div class="dialog-text">即将去微信公众平台进行扫描授权，
                扫描确认后，原有的功能及菜单将被统一替换。</div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-go-confirm marR20">确认</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-go-cancel">取消</button>
            </div>
        </div>
    </div>
</script>
<script src="${BASE_PATH}/static/js/page/wechat/apply/wx-grant.js?460cd0b8f5e5393035dbd6329813c062"></script>
<#include "yqx/layout/footer.ftl" >
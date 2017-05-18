<#include "layout/outheader.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/sell/login.css?982561de0e67e0ac12eef2d43e5875cf"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d" />
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/third-plugin/Font-Awesome-3.2.1/css/font-awesome.min.css" />

<div class="login-bg clearfix">
    <h3 class="yx-title">云修系统，一站式汽修管理解决方案</h3>
    <div class="login-box">
        <div class="login-title">手机号登录</div>
        <div class="login-info">
            <div class="row clearfix">
                <div class="mobile-box input-wrap">
                    <i class="icon mobile-ico"></i>
                    <input type="text" placeholder="请填写手机号码" class="mobile js-mobile"/>
                </div>
            </div>
            <div class="row clearfix">
                <div class="code-box input-wrap">
                    <i class="icon code-ico1"></i>
                    <input type="text" placeholder="请输入检验码" class="code check-num"/>
                </div>
                <img src="${BASE_PATH}/imageServlet" class="code-num js-check"></img>
            </div>
            <div class="row clearfix">
                <div class="input-wrap code-box">
                    <i class="icon code-ico2"></i>
                    <input type="text" placeholder="请输入验证码" class="code captcha"/>
                </div>
                <button class="code-btn js-code">获取验证码</button>
            </div>
            <a href="javascript:;" class="login-btn js-login">登录</a>
            <h3 class="login-tips">如果您还未认证，请马上 <a href="${certificationUrl}" target="_blank">进行认证<i class="attestation-ico"></i></a></h3>
        </div>
    </div>
</div>


<script type="text/html" id="certificationDialog">
    <div class="yqx-dialog">
        <div class="yqx-dialog-header">
            <h3 class="yqx-dialog-headline">提示</h3>
        </div>
        <div class="yqx-body">
            <img src="${BASE_PATH}/static/img/page/sell/attestation.png" class="attestation"/>
            <h3 class="certification-tip">您的帐号还没有进行认证</h3>
            <a href="${certificationUrl}" target="_blank" class="go-certification">马上认证</a>
        </div>
    </div>

</script>
<script src="${BASE_PATH}/static/third-plugin/seajs/sea.js"></script>
<script src="${BASE_PATH}/static/third-plugin/path.config.js?98f845edf92898b7a23a5b384185c04c"></script>
<script src="${BASE_PATH}/static/js/page/sell/login.js?5125e911b0a48c79267a18f7080b75fb"></script>
<#include "yqx/page/sell/footer.ftl">
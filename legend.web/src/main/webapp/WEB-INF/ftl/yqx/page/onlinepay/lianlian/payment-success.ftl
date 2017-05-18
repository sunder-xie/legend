<#include "layout/outheader.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/sell/payment-success.css?63a1065a867d716a266b6e3fa940b528"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d" />
<div class="wrap clearfix">
    <div class="wrap-left fl">
        <h3><img src="${BASE_PATH}/static/img/page/sell/success-ico.png"/>您已成功开通淘汽云修系统！</h3>
        <div class="pay-money">
            <span>付款金额：<em>${order.sellAmount}</em>元</span>
            <span>订单号：${order.sellOrderSn}</span>
        </div>
        <div class="congratulations">恭喜您，可以马上开启淘汽云修之旅！</div>
        <div class="accounts-tip">账号密码已经发送至您的手机号码：${order.buyMobile}</div>
        <div class="services">
            <img src="${BASE_PATH}/static/img/page/sell/success-phone.png"/>
            400客服热线电话 <span>400-9937-288</span> 转<span>2</span>转<span>3</span> 咨询 |
            <img src="${BASE_PATH}/static/img/page/sell/success-qq.png"/>
            <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3056630970&site=qq&menu=yes" class="customer">客服1</a>
            <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3320476090&site=qq&menu=yes" class="customer">客服2</a>
            <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3274979296&site=qq&menu=yes" class="customer">客服3</a>
        </div>
        <div class="services">更多精彩培训，尽在云修学院</div>
        <a href="${BASE_PATH}/index" class="login-btn">马上登录</a>
    </div>
    <div class="wrap-right fl">
        <div class="code"><img src="${BASE_PATH}/static/img/page/sell/qr-code.jpg"/></div>
        <h3>扫码下载App</h3>
        <p>移动办公，一手搞定</p>
    </div>
</div>
<#include "yqx/page/sell/footer.ftl">
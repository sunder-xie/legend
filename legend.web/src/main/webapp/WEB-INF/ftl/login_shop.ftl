<#include "layout/outheader.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/resources/css/login/login.css?b3f2573798cc60c69e8aeeb58cf89581"/>
<link rel="stylesheet" href="${BASE_PATH}/resources/css/easycheck.css"/>

<div class="wrapper clearfix">
    <input type="hidden" class="js-socket-url" value="${socketUrl}">
    <div class="log_l">
        <img src="${BASE_PATH}/resources/images/login_img.jpg" alt=""/>
    </div>
    <div class="log_box" id="loginCon">

    </div>
</div>

<!--帐号登录-->
<script type="text/html" id="accountsTpl">
    <div class="log-pic barcode js-barcode"></div>
    <div class="box_top">
        <div class="box-tips">
            <span class="tips">扫码登录更安全</span><img src="${BASE_PATH}/resources/images/arrow-right.png"/>
        </div>
        <div class="box-title">淘汽云修系统密码登录</div>
    </div>
    <div class="form-box">
        <input type="hidden" class="loginShop" id="loginShop" name="loginShop" value="">
        <form name='loginForm' id='loginForm' action='${BASE_PATH}/loginAction' method='POST'>
            <ul class="box_mid">
                <li class="clearfix username">
                    <label></label>
                    <input type="text" name="username" id="username" tabindex="1" autofocus value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}"  placeholder="用户名或手机号">
                    <input type="hidden" name="shopId" id="shopId"/>
                </li>
                <li class="clearfix password">
                    <label></label>
                    <input type="password" name="password" id="password" tabindex="2"  placeholder="密码">
                </li>
                <li class="clearfix checkcode">
                    <label></label>
                    <input type="text" name='validateCode' id="validateCode" tabindex="3"  placeholder="验证码">
                    <img src="${BASE_PATH}/imageServlet" id="imageF" alt="" title="点击图片更换验证码"/>
                </li>
                <li class="clearfix forget_password">
                    <a href="${BASE_PATH}/index/reset" tabindex="5">忘记密码?</a>
                    <a href="javascript:;" class="change" id="flashImage">换一张</a>
                </li>
                <li class="clearfix">
                    <input type="button" tabindex="4" value="登录" class="login_btn login_c">
                </li>
            </ul>
        </form>
    </div>
</script>

<!--扫码登录-->
<script type="text/html" id="barcodeTpl">
    <div class="log-pic pc-login js-login"></div>
    <div class="box_top">
        <div class="box-tips">
            <span class="tips">密码登录在这里</span><img src="${BASE_PATH}/resources/images/arrow-right.png"/>
        </div>
    </div>
    <div class="barcode-box">
        <div class="barcode-img">
            <img src="" class="js-qr-code">
        </div>
    </div>
    <div class="explain clearfix">
        <div class="explain-ico"></div>
        <div class="explain-right">
            <p class="p-text1">请使用<span>商家版App</span></p>
            <p class="p-text2">扫一扫登录</p>
        </div>
    </div>
</script>

<!--二维码过期-->
<script type="text/html" id="overdueTpl">
    <div class="log-pic pc-login js-login"></div>
    <div class="box_top">
        <div class="box-tips">
            <span class="tips">密码登录在这里</span><img src="${BASE_PATH}/resources/images/arrow-right.png"/>
        </div>
    </div>
    <div class="barcode-box">
        <div class="wrong-ico"></div>
        <div class="overdue">二维码已过期</div>
        <div class="again">请刷新二维码后重新扫码</div>
    </div>
    <input type="button" tabindex="4" value="刷新二维码" class="login_btn js-refresh">
</script>

<!--一个手机号多家门店选择弹框-->
<script type="text/html" id="moreShopTpl">
    <section class="choose-shop-dg">
        <div class="choose-shop-dg-header">
            <h3>门店登录选择</h3>
            <small>一切尽在掌控</small>
        </div>
        <div class="choose-shop-dg-body">
            <%if (templateData) {%>
            <ul class="choose-shop-dg-list">
                <%for (var i = 0; i < templateData.length; i++) {%>
                <%var shop = templateData[i];%>
                <li>
                    <p class="shop-name"><%= shop.name %></p>
                    <button data-id="<%= shop.id %>" class="go-btn js-login-shop">立即进入</button>
                </li>
                <%}%>
            </ul>
            <%}%>
        </div>
    </section>
</script>

<script src="${BASE_PATH}/resources/js/home/register.js?40c19c3cdb15ff6e3bab6e2e66c912ec"></script>
<script src="${BASE_PATH}/resources/js/lib/easy.easycheck.js?e84bbd98eaddec64a6c196cfca8d970e"></script>
<script src="${BASE_PATH}/resources/js/common/popup.js?3a084ca1d606e27e6547c63f5ba59424"></script>
<script src="${BASE_PATH}/resources/script/libs/tips.js?e250bff627ebeab4eead5769c49f9bb6"></script>
<script src="${BASE_PATH}/resources/js/common/cookie.js?4f9d2ea24d0fa234847ecd925a614460"></script>
<script src="${BASE_PATH}/resources/script/libs/login_shop.js?ad29f2ee5bb6f031b9f5a01e4c6c5e2c"></script>
<script>

    $(function($){
        $(document).on('click','#flashImage,#imageF',function(){
            $('#imageF').fadeOut().attr('src',"${BASE_PATH}/imageServlet"+ '?'+ Math.floor(Math.random() * 100)).fadeIn();
        });
    });
</script>

<#include "layout/outfooter.ftl" >
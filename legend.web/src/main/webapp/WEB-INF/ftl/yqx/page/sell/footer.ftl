<style>
    /*登录页公共头和尾*/
    .login_header {
        border-bottom: 1px solid #d7d7d7;
        height: 70px;
    }

    .login_header .header_inner {
        width: 980px;
        margin: 0 auto;
    }

    .login_header .header_inner .logo_box {
        float: left;
    }

    .login_header .header_inner .title_box {
        font-size: 18px;
        font-weight: bolder;
        float: left;
        margin: 30px 0 auto 10px;
    }

    .login_header .header_inner .menu_box {
        float: right;
        margin-top: 35px;
    }

    .login_header .header_inner .menu_box a,
    .login_header .header_inner .menu_box span {
        float: left;
        display: block;
    }

    .login_header .header_inner .menu_box span {
        width: 20px;
        text-align: center;
    }

    .login_header .header_inner .menu_box a:hover {
        color: #4492fd;
        text-decoration: underline;
    }

    .login_footer {
        border-top: 1px solid #d7d7d7;
        height: 80px;
    }

    .login_footer p {
        text-align: center;
        margin-top: 30px;
    }
    /*底部*/
    .login-footer{
        width: 990px;
        height: 52px;
        margin: 0 auto;
        border-top: 1px solid #d7d7d7;
        padding-top: 8px;
    }
    .login-footer .link{
        font-size: 12px;
        color: #666;
        font-family: "宋体", sans-serif, Verdana, Arial, Helvetica, Tahoma;
        padding:0 10px;
        border-right: 1px solid #d7d7d7;
        line-height: 30px;
    }
    .login-footer .link:last-child{
        border-right: none;
    }
    .login-footer .copyright{
        text-align: right;
        color: #666;
        font-size: 12px;
        line-height: 30px;
    }
</style>

<div class="login-footer clearfix">
    <div class="fl">
        <a class="link" target="_blank" href="http://weibo.com/u/5508156246">官方微博</a>
        <a class="link" target="_blank" href="${BASE_PATH}/portal/insurance/tqmall-insurance">淘汽保险</a>
        <a class="link" target="_blank" href="http://www.tqmall.com/Help.html?id=51">联系我们</a>
        <a class="link" target="_blank" href="http://open.tqmall.com">开放平台</a>
        <a class="link" target="_blank" href="http://www.tqmall.com">淘汽档口</a>
    </div>
    <div class="fr copyright">Copyright © 2014-2015 Tqmall.com 淘汽云修版权所有 浙ICP备20120045号</div>
</div>

<script type="text/javascript">
    var h = $(window).height();
    var boxH = $(document).height();
    if (boxH == h){
        $('.login-footer').css({
            'position': 'fixed',
            'bottom': '0',
            'left':'50%',
            'margin-left': '-495px'
        })
    }
</script>
</body>
</html>
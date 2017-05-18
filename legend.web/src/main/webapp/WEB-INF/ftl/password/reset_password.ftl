<#include "layout/outheader.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/login/login.css?b3f2573798cc60c69e8aeeb58cf89581"/>
<script type="text/javascript" src="${BASE_PATH}/resources/js/home/register.js?40c19c3cdb15ff6e3bab6e2e66c912ec"></script>
<div class="wrapper">
    <div class="md_b">
        <div class="md_t clearfix">
            <a class="current" href="${BASE_PATH}/index/reset">门店管理员账号</a>
            <a href="${BASE_PATH}/index/remind">门店子账号</a>
        </div>
        <form name='f' id='f' action='${BASE_PATH}/index/newPassword' method='POST'>
            <div class="md_c">
                <input type="hidden" class="userId" id="id" name="id" value="">
                <p style="text-align: center"> <span style="color:red">${error}</span></p>
                <div class="md_cli"><span>淘汽门店账号:</span><input type="text" name="mobileReg" id="mobileReg" class="log_txt" placeholder="门店管理员账号"/></div>
                <div class="md_cli"><span>验证码:</span><input type="text" name="codeReg" id="codeReg" autocomplete="off" class="log_txt yzmt" placeholder="验证码"/><input type="button" name='flashCode' id="flashCode" value="点击获取验证码" class="but_log"/></div>
            </div>
            <div class="lg_b reset clearfix">
                <input type="submit" value="下一步"/>
                <a href="${BASE_PATH}/index">返回登录</a>
            </div>
        </form>
    </div>
</div>
</script>
<script type="text/javascript">

</script>
<#include "layout/outfooter.ftl" >
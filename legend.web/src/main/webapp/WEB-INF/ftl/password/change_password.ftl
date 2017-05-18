<#include "layout/outheader.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/login/login.css?b3f2573798cc60c69e8aeeb58cf89581"/>
<div class="wrapper">
    <div class="md_b">
        <div class="md_t clearfix">
            <a class="current" href="${BASE_PATH}/index/reset">门店管理员账号</a>
            <a href="${BASE_PATH}/index/remind">门店子账号</a>
        </div>
        <form name='f' id='f' action='${BASE_PATH}/index/changePassword' method='POST'>
            <div class="md_c">
                <input type="hidden" name="id" id="id" value="${login.id}"/>
                <input type="hidden" name="shopReg" id="shopReg" value="${login.shopId}"/>
                <p style="text-align: center"> <span style="color:red" id="error"></span></p>
                <div class="md_cli"><span>淘汽门店账号:</span><input name="mobileReg" id="mobileReg" type="text" value="${login.account}" disabled class="log_txt lock"/></div>
                <div class="md_cli"><span>设置新密码:</span><input name="passwordReg" id="passwordReg" type="password" class="log_txt"/></div>
            </div>
            <div class="lg_b reset clearfix">
                <input type="button" id="changePassword" value="提　交"/><a href="${BASE_PATH}/index">返回登录</a>
            </div>
        </form>
    </div>
</div>
<script>
    $("#changePassword").click(function(){
        var id = $("#id").val();
        var shopReg = $("#shopReg").val();
        var mobileReg = $("#mobileReg").val();
        var passwordReg = $("#passwordReg").val();
        if(passwordReg == ''){
            taoqi.failure("请输入6~12位且包含字母和数字的密码");
            return false;
        }
        var loading = taoqi.loading();
        $.ajax({
            type: 'POST',
            url: BASE_PATH + '/index/changePassword',
            data: {
                id : id,
                shopReg : shopReg,
                mobileReg : mobileReg,
                passwordReg : passwordReg
            },
            success: function (result) {
                if (result.success != true) {
                    var errorMsg = result.errorMsg;
                    taoqi.error(errorMsg);
                    $("#error").html(errorMsg);
                    return false;
                }
                else {
                    window.location.href = BASE_PATH + '/index/success';
                }
            },
            error: function (a, b, c) {
                //console.log(a,b,c);
            }
        });
    });
</script>
<#include "layout/outfooter.ftl" >
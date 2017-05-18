<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/set-common.css?77d397fcba7a83c9bd59f8d7706a1e43"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/change_password.css"/>
<script type="text/javascript"
        src="${BASE_PATH}/resources/js/privilege/password_change.js?5fea918a7aa526a6dd37c18e77bbb448"></script>
<div class="wrapper">
    <div class="base-data cgpb password-set-container">
        <div class="content-title">修改密码</div>
        <ul class="data-set">
            <li><span style="color:red;">注：密码长度6~12位,密码必须包含数字和字母，字母不区分大小写</span></li>
            <li>
                <label>原密码: <span class="red-star">＊</span></label>
                <input type="password" class="form-control" name="oldPassWord" id="oldPassWord"/>
            </li>
            <li>
                <label>新密码: <span class="red-star">＊</span></label>
                <input type="password" class="form-control" name="newPassWord" id="newPassWord"/>
            </li>
            <li>
                <label>重输密码: <span class="red-star">＊</span></label>
                <input type="password" class="form-control" name="newPassWordR" id="newPassWordR"/>
            </li>
            <div class="clear"></div>
        </ul>
        <div class="action_bu">
            <button name="update" id="update">保存</button>
            <button onclick="history.go(-1)">返回</button>
        </div>
    </div>
</div>
<#include "layout/footer.ftl" >

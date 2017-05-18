<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/securityexport.css?269a8e963e236b91997867f0c8c53ea6"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">信息导出加密设置</h3>
        </div>
        <div class="content">
            <div class="tab-box">
                <span class="current-tab js-tab" data-tab="1">信息导出密码设置</span>
                <span class="js-tab" data-tab="2">信息导出操作记录</span>
            </div>
            <div class="tab-con" id="tabCon">

            </div>
        </div>
    </div>
    <!-- 右侧内容区 end -->
</div>

<!--信息导出密码设置-->
<script type="text/html" id="passwordBeforeTpl">
    <div class="form-box">
        <div class="form-label">
            信息导出密码:
        </div>
        <div style="display: inline-block">
            <div class="form-item">
                <input type="password"
                       name="password"
                       class="yqx-input yqx-input-small"
                       placeholder="请输入密码">
            </div>
            <div id="passwordDiv" style="display: inline-block">
                <span id="passwordTip" style="color:red"></span>
            </div>
        </div>
        <p class="password-tips">注:密码长度为6~12位，密码必须包含数字和字母，字母不区分大小写</p>
        <p class="password-tips">信息导出密码不能与云修账号登录密码相同</p>
        <p class="password-tips">信息导出密码在车辆信息导出、工单信息导出、报表导出时使用</p>
        <div class="code-box">
            <div class="form-label">
                验证码:
            </div>
            <div class="form-item code-form-item">
                <input type="text"
                       name="SMSCode"
                       class="yqx-input yqx-input-small"
                       placeholder="请输入验证码">
            </div>
            <input type="hidden" name="mobile" value="${mobile}"/>
            <button class="yqx-btn yqx-btn-1 yqx-btn-micro js-get-code">获取验证码</button>
        </div>
        <div class="save-btn-box">
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save">保存</button>
        </div>
    </div>
</script>

<!--信息导出操作记录-->
<script type="text/html" id="exportListTpl">
    <table class="yqx-table" id="tableTest">
        <thead>
        <tr>
            <th class="text-l col1">序号</th>
            <th class="text-l col2">导出时间</th>
            <th class="text-l col3">操作账号</th>
            <th class="text-l col4">导出内容</th>
        </tr>
        </thead>
        <%
            var data =json.data.content;
            var page =json.data.number;
            var size =json.data.size;
            for (var i = 0; i < data.length; i++) {var item = data[i];
            var item = data[i];
        %>
        <tr>
            <td class="text-l col1"><%=i+1 + (page * size)%></td>
            <td class="text-l col2"><%=item.gmtCreateStr%></td>
            <td class="text-l col3"><%=item.operatorName%></td>
            <td class="text-l col4 js-show-tips"><%=item.operateBrief%></td>
        </tr>
        <% } %>
    </table>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="pagingTest"></div>
    <!-- 分页容器 end -->
</script>

<!--停用导出密码-->
<script type="text/html" id="stopDialogTpl">
    <div class="yqx-dialog">
        <div class="yqx-dialog-header">
            <div class="yqx-dialog-headline">输入验证码</div>
        </div>
        <div class="yqx-dialog-body">
            <div class="dialog-form">
                <div class="form-label">
                    验证码:
                </div>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-small js-dialog-code" value="" placeholder="">
                </div>
                <button class="yqx-btn yqx-btn-1 yqx-btn-micro js-dialog-get-code">获取验证码</button>
            </div>
            <div class="dialog-btn">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-dialog-stop" disabled>停用</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-dialog-cancel">取消</button>
            </div>
        </div>
    </div>
</script>

<script src="${BASE_PATH}/static/js/page/setting/securityexport.js?3937aa28b03078ff011ac6826b5ac62b"></script>
<#include "yqx/layout/footer.ftl">

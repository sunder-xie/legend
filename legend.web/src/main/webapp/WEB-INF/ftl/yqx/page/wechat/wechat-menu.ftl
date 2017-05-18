<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-base.css?9f840e9f3c3f39c54bcbe9b7350edbe9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/wechat-menu.css?e88d087e0bed6594b429cdc8ebf44adf"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline">菜单配置
                <a class="yqx-btn yqx-btn-default help fr clearfix" href="${BASE_PATH}/shop/help?id=92">
                    <i class="question-icon"></i><i>帮助中心</i>
                </a>
            </h1>
        </div>
        <div class="order-body">
            <p class="tips">请选择想展示的功能</p>
        </div>
    </div>
</div>

<script id="menuTpl" type="text/template">
    <#--左边展示的菜单-->
    <div class="menu-box" id="menu">
        <% var flag = true;%>
        <%if(customMenu && customMenu.length){%>
            <%for(var i=0; i < customMenu.length; i++) {%>
                <%if(!customMenu[i]) {%>
                <%continue;%>
                <%}%>
            <div class="menu-header <%if(!flag) {%><%='hide'%><%} else {%><%='current'%><%flag = false;}%>" id="index<%=i%>">
                <input type="hidden" class="firstWindex" value="<%=(i+1)*10+1%>"/>
                <%if(customMenu[i].children && customMenu[i].children.length) {%>
                    <% var children = customMenu[i].children %>
                    <% for(var j=0; j < children.length; j++) {%>
                    <div class="menu-selector">
                        <input type="checkbox" class="js-checkbox" checked disabled>
                        <h4><%=originData.customMenu[ children[j] ].kname%></h4>
                    </div>
                    <div class="menu-edit" hidden="hidden" data-index="<%=children[j]%>">
                        <span>
                            <i class="move-icons up-icon <%if(j==0){%> disable <%}%>"></i>
                            <i class="move-icons down-icon <%if(j==children.length-1){%> disable <%}%>"></i>
                        </span>
                        <input type="text" class="yqx-input editMenu" value="<%=originData.customMenu[ children[j] ].kname%>" data-index="<%=children[j]%>" data-v-type="required | maxLength:11">
                        <a class="js-remove delete-style" data-index="<%=children[j]%>" data-target="#edit<%=i%>">删除</a>
                    </div>
                    <%}%>
                <%}%>
                <div class="angle-box">
                    <div class="angle"></div>
                    <div class="angle-back"></div>
                </div>
                <button class="js-edit yqx-btn yqx-btn-3" data-target="#edit<%=i%>">编辑</button>
            </div>
            <%}%>

            <% flag = true;%>
            <div class="menu-body clearfix">
            <%for(var i=0; i < customMenu.length; i++) {%>
                <%if(!customMenu[i]) continue;%>
                <h3 class="fl js-tab <%if(flag) {%><%='active'%><% flag = false; }%>" data-target="#index<%=i%>" data-default="#edit<%=i%>"><%=customMenu[i].name%></h3>
            <%}%>
            </div>
            <div class="menu-footer" hidden="hidden">
                <button class="js-save yqx-btn yqx-btn-3">保存</button>
                <button class="js-back yqx-btn yqx-btn-default">返回</button>
            </div>
        <%}%>
    </div>
    <#--右边的菜单管理-->
    <div class="menu-manage" >
        <%for(var i=0; i < defaultMenus.length; i++) {%>
        <div class="official-menu" id="edit<%=i%>" style="display: none">
            <h3>云修官方菜单</h3>
            <p>微信官方限制最多只能选择5个功能展示</p>
            <ul class="official-menu-list">
                <% var defaultMenu = defaultMenus[i]%>
                <% for(var j=0; j < defaultMenu.length; j++) {%>
                <li>
                    <i class="js-default-add move-icons left-icon <%if(originData.defaultMenu[ defaultMenu[j] ].defaultMenuIsUsed==1){%> disable choosed<%}%>"
                       data-index="<%=defaultMenu[j]%>"></i>
                    <span><%=originData.defaultMenu[ defaultMenu[j] ].kname%></span>
                </li>
                <%}%>
            </ul>
        </div>
        <%}%>
        <div class="custom-menu" style="display: none">
            <h3>自定义菜单</h3>
            <button class="yqx-btn yqx-btn-3 js-addMenu">添加自定义菜单</button>
            <ul class="custom-menu-list">
                <% if(originData.customNoUseMenu) {%>
                    <% var customNoUseMenu = originData.customNoUseMenu %>
                    <% for(var i=0; i < customNoUseMenu.length; i++) {%>
                    <li class="custom-menu-item">
                        <i class="js-custom-add move-icons left-icon" data-index="<%=i%>"></i>
                        <span><%=customNoUseMenu[i].kname%></span>
                        <a class="js-modify modify-style"  data-index="<%=i%>">修改</a>
                        <a class="js-delete delete-style"  data-index="<%=i%>">删除</a>
                    </li>
                    <%}%>
                <%}%>
            </ul>
        </div>
    </div>

</script>
<#--增加菜单项模板start-->
<script type="text/template" id="addMenuItemTpl">
    <div class="menu-edit"  data-index="<%=index%>">
        <span>
            <i class="move-icons up-icon"></i>
            <i class="move-icons down-icon"></i>
        </span>
        <input type="text" class="yqx-input editMenu" value="<%=value%>" data-index="<%=index%>">
        <a class="js-remove delete-style" data-index="<%=index%>" data-target="<%=target%>" data-from="<%=from%>">删除</a>
    </div>
</script>
<#--增加菜单项模板end-->

<#--增加自定义菜单项模板start-->
<script type="text/template" id="addCustomTpl">
    <li class="custom-menu-item">
        <i class="js-custom-add move-icons left-icon" data-index="<%=index%>"></i>
        <span><%=value%></span>
        <a class="js-modify modify-style" data-index="<%=index%>">修改</a>
        <a class="js-delete delete-style" data-index="<%=index%>">删除</a>
    </li>
</script>
<#--增加自定义菜单项模板end-->

<!--添加或修改菜单弹窗tart-->
<script type="text/template" id="modifyCustomMenuTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            <%if(type == 0) {%> <%='添加自定义菜单'%> <%}else{%> <%='修改自定义菜单'%><%}%>
        </div>
        <div class="bounce-content" id="modifyCustomMenu">
            <fieldset>
                <div class="form-label">输入菜单名称</div>
                <div class="form-item">
                    <input type="text" class="yqx-input input-middle"  value="<%if(data){%><%=data.kname%><%}%>"  name="kname" placeholder="限制11个汉字" data-v-type="required | maxLength:11">
                </div>
            </fieldset>
            <fieldset>
                <div class="form-label">输入地址链接</div>
                <div class="form-item">
                    <input type="text" class="yqx-input input-long"  value="<%if(data){%><%=data.menuUrl%><%}%>" name="menuUrl" placeholder="请输入您的地址链接" data-v-type="required|maxLength:200|httpUrl">
                </div>
                <input type="hidden" name="id" value="<%if(data){%><%=data.id%><%}%>" />
            </fieldset>
        </div>
        <div class="bounce-foot">
            <button class="yqx-btn yqx-btn-3 js-modifyMenu-confirm" data-type="<%=type%>">确定</button>
            <button class="yqx-btn yqx-btn-1 js-modifyMenu-cancel">取消</button>
        </div>

    </div>
</script>
<!--添加菜单模板 end-->

<!--切换tab未保存的弹窗 start-->
<script type="text/template" id="changeConfirmTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            还未保存当前菜单
        </div>
        <div class="bounce-foot">
            <button class="yqx-btn yqx-btn-3 js-tab-confirm">确定</button>
            <button class="yqx-btn yqx-btn-1 js-tab-cancel">取消</button>
        </div>
    </div>
</script>
<!--切换tab未保存的弹窗 end-->
<!--删除菜单弹窗 start-->
<script type="text/template" id="deleteMenuTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            是否确定删除
        </div>
        <div class="bounce-foot">
            <button class="yqx-btn yqx-btn-3 js-delete-confirm" data-id="<%=id%>">确定</button>
            <button class="yqx-btn yqx-btn-1 js-delete-cancel">取消</button>
        </div>
    </div>
</script>
<!--切换tab未保存的弹窗 end-->
<!--删除菜单弹窗 start-->
<script type="text/template" id="saveMenuTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
           发布确认
        </div>
        <div class="bounce-content">
            <p>本次发布将在24小时内对所有用户生效，确认发布？</p>
        </div>
        <div class="bounce-foot">
            <button class="yqx-btn yqx-btn-3 js-save-confirm">确定</button>
            <button class="yqx-btn yqx-btn-1 js-save-cancel">取消</button>
        </div>
    </div>
</script>
<!--切换tab未保存的弹窗 end-->
<script src="${BASE_PATH}/static/js/page/wechat/wechat-menu.js?ea5d44754641a1e079b14954aa9d58b3"></script>
<#include "yqx/layout/footer.ftl">

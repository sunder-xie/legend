<li class="aside-nav-root js-nav-wx-root">微信公众号</li>
<li class="aside-nav-list js-nav-wx-list">
    <dl>
        <dd>
            <a class=' <#if subModule?? && subModule=="wechat-index">current</#if>' href="${BASE_PATH}/shop/wechat">主页</a>
        </dd>
        <dd>
            <a class=' <#if subModule?? && subModule=="article-list">current</#if>' href="${BASE_PATH}/shop/wechat/article-list">文章管理</a>
        </dd>
        <dd>
            <a class=' <#if subModule?? && subModule=="wechat-activity">current</#if>' href="${BASE_PATH}/shop/wechat/activity-list">活动管理</a>
        </dd>
        <dd>
            <a class=' <#if subModule?? && subModule=="wechat-lottery">current</#if>' href="${BASE_PATH}/shop/wechat/activity-lottery-management">抽奖活动</a>
        </dd>
        <dd>
            <a class=' <#if subModule?? && subModule=="wechat-coupon">current</#if>' href="${BASE_PATH}/shop/wechat/wechat-coupon">关注送券</a>
        </dd>
        <dd>
            <a class=' <#if subModule?? && subModule=="wechat-favormall">current</#if>' href="${BASE_PATH}/shop/wechat/favormall-list">卡券商城</a>
        </dd>
        <dd>
            <a class=' <#if subModule?? && subModule=="wechat-rescue-assessment">current</#if>' href="${BASE_PATH}/shop/wechat/rescue-assessment-list">救援定损</a>
        </dd>
        <#--管理员和有设置权限的员工有发布服务权限-->
        <#if SESSION_USER_IS_ADMIN == 1>
            <dd>
                <a class='<#if subModule?? && subModule=="wechat-appservice">current</#if>' href="${BASE_PATH}/shop/wechat/appservice/list">发布服务</a>
            </dd>
        <#else>
            <#if SESSION_USER_ROLE_FUNC?exists>
                <#list SESSION_USER_ROLE_FUNC as temp>
                    <#if temp.name =="设置">
                        <dd>
                            <a class='<#if subModule?? && subModule=="wechat-appservice">current</#if>' href="${BASE_PATH}/shop/wechat/appservice/list">发布服务</a>
                        </dd>
                    </#if>
                </#list>
            </#if>
        </#if>

        <dd>
            <a class=' <#if subModule?? && subModule=="wechat-menu">current</#if>' href="${BASE_PATH}/shop/wechat/wechat-menu">微信菜单配置</a>
        </dd>
        <dd>
            <a class=' <#if subModule?? && subModule=="msg-list">current</#if>' href="${BASE_PATH}/shop/wechat/msg-list">自动回复</a>
        </dd>
        <dd>
            <a class=' <#if subModule?? && subModule=="wifi-manage">current</#if>' href="${BASE_PATH}/shop/wechat/wifi-manage">设置WIFI</a>
        </dd>
        <dd>
            <a class=' <#if subModule?? && subModule=="qrcode-list">current</#if>' href="${BASE_PATH}/shop/wechat/qrcode-list">二维码</a>
        </dd>
        <dd>
            <a class=' <#if subModule?? && subModule=="wechat-info">current</#if>' href="${BASE_PATH}/shop/wechat/wechat-info">资料维护</a>
        </dd>
    </dl>
</li>
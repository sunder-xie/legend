<#if SESSION_USER_IS_ADMIN == 1>
    <@account_tpl/>
<#else>
    <#list SESSION_USER_ROLE_FUNC as temp>
        <#if temp.name == "客户管理">
            <@account_tpl/>
        </#if>
    </#list>
</#if>
<#macro account_tpl>
    <li class="aside-nav-root">客户管理</li>
    <li class="aside-nav-list">
        <dl>
            <dd>
                <a class='<#if subModule?? && subModule=="account-index">current</#if>' href="${BASE_PATH}/account">客户查询</a>
            </dd>
            <dd>
                <a class='<#if subModule?? && subModule=="grant">current</#if>' href="${BASE_PATH}/account/grant">卡券办理</a>
            </dd>
            <dd>
                <a class='<#if subModule?? && subModule=="account-setting">current</#if>' href="${BASE_PATH}/account/setting">优惠设置</a>
            </dd>
        </dl>
    </li>
</#macro>

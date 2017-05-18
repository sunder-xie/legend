<#if SESSION_USER_IS_ADMIN == 1>
    <@marketing_tpl/>
<#else>
    <#list SESSION_USER_ROLE_FUNC as temp>
        <#if temp.name == "客户分析" || temp.name == "提醒中心" || temp.name == "精准营销" || temp.name == "短信充值" || temp.name == "门店推广">
            <@marketing_tpl/>
            <#break>
        </#if>
    </#list>
</#if>
<#macro marketing_tpl>
    <li class="aside-nav-root">客户营销</li>
    <li class="aside-nav-list">
        <dl>
            <#if SESSION_USER_IS_ADMIN == 1>
                <dd>
                    <a class='<#if subModule?? && subModule=="analysis">current</#if>' href="${BASE_PATH}/marketing/ng/analysis">客户分析</a>
                </dd>
                <#if !(YBD?? && YBD=="true")>
                    <dd>
                        <a class='<#if subModule?? && subModule=="maintain-center">current</#if>' href="${BASE_PATH}/marketing/ng/maintain/center">提醒中心</a>
                    </dd>
                </#if>
                <dd>
                    <a class='<#if subModule?? && subModule=="center-accurate">current</#if>' href="${BASE_PATH}/marketing/ng/center/accurate">精准营销</a>
                </dd>
                <dd>
                    <a class='<#if subModule?? && subModule=="center-sms">current</#if>' href="${BASE_PATH}/marketing/ng/center/sms">短信充值</a>
                </dd>
                <dd>
                    <a class='<#if subModule?? && subModule=="promotion">current</#if>' href="${BASE_PATH}/marketing/ng/analysis/promotion">门店推广</a>
                </dd>
            <#else>
                <#list SESSION_USER_ROLE_FUNC as temp>
                    <#if temp.name == "客户分析">
                        <dd>
                            <a class='<#if subModule?? && subModule=="analysis">current</#if>' href="${BASE_PATH}/marketing/ng/analysis">客户分析</a>
                        </dd>
                    </#if>
                </#list>
                <#if !(YBD?? && YBD=="true")>
                    <#list SESSION_USER_ROLE_FUNC as temp>
                        <#if temp.name == "提醒中心">
                            <dd>
                                <a class='<#if subModule?? && subModule=="maintain-center">current</#if>' href="${BASE_PATH}/marketing/ng/maintain/center">提醒中心</a>
                            </dd>
                        </#if>
                    </#list>
                </#if>
                <#list SESSION_USER_ROLE_FUNC as temp>
                    <#if temp.name == "精准营销">
                        <dd>
                            <a class='<#if subModule?? && subModule=="center-accurate">current</#if>' href="${BASE_PATH}/marketing/ng/center/accurate">精准营销</a>
                        </dd>
                    </#if>
                </#list>
                <#list SESSION_USER_ROLE_FUNC as temp>
                    <#if temp.name == "短信充值">
                        <dd>
                            <a class='<#if subModule?? && subModule=="center-sms">current</#if>' href="${BASE_PATH}/marketing/ng/center/sms">短信充值</a>
                        </dd>
                    </#if>
                </#list>
                <#list SESSION_USER_ROLE_FUNC as temp>
                    <#if temp.name == "门店推广">
                        <dd>
                            <a class='<#if subModule?? && subModule=="promotion">current</#if>' href="${BASE_PATH}/marketing/ng/analysis/promotion">门店推广</a>
                        </dd>
                    </#if>
                </#list>
            </#if>
        </dl>
    </li>
</#macro>

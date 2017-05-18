<li class="aside-nav-root">集客方案</li>
<li class="aside-nav-list">
    <dl>
    <#if SESSION_USER_IS_ADMIN == 1>
        <dd>
            <a class="<#if subModule?? && subModule=="gather-import">current</#if>" href="${BASE_PATH}/marketing/gather/import-page">导入客户会员</a>
        </dd>
        <dd>
            <a class="<#if subModule?? && subModule=="gather-allot">current</#if>" href="${BASE_PATH}/marketing/gather/allot/allot-list">分配客户</a>
        </dd>
        <dd>
            <a class="<#if subModule?? && subModule=="gather-rule">current</#if>" href="${BASE_PATH}/marketing/gather/rule">设置奖励规则</a>
        </dd>
        <dd>
            <a href="${BASE_PATH}/shop/report/staff/perf?hasBack=true">业绩提成</a>
        </dd>
        <dd>
            <a class='<#if subModule?? && subModule=="gather-plan">current</#if>' href="${BASE_PATH}/marketing/gather/plan">集客消费</a>
        </dd>
        <dd>
            <a class='<#if subModule?? && subModule=="gather-effect">current</#if>' href="${BASE_PATH}/marketing/gather/effect">集客效果</a>
        </dd>
        <dd>
            <a class='<#if subModule?? && subModule=="gather-detail">current</#if>' href="${BASE_PATH}/marketing/gather/detail">集客详情</a>
        </dd>
    <#else>
        <dd>
            <a class="<#if subModule?? && subModule=="gather-rule">current</#if>" href="${BASE_PATH}/marketing/gather/rule">设置奖励规则</a>
        </dd>
        <dd>
            <a href="${BASE_PATH}/shop/report/gather/staff/perf?hasBack=true">业绩提成</a>
        </dd>
    <#list SESSION_USER_ROLE_FUNC as temp>
        <#if temp.name == "提醒中心">
            <dd>
                <a class='<#if subModule?? && subModule=="gather-plan">current</#if>' href="${BASE_PATH}/marketing/gather/plan">集客消费</a>
            </dd>
            <dd>
                <a class='<#if subModule?? && subModule=="gather-effect">current</#if>' href="${BASE_PATH}/marketing/gather/effect">集客效果</a>
            </dd>
            <dd>
                <a class='<#if subModule?? && subModule=="gather-detail">current</#if>' href="${BASE_PATH}/marketing/gather/detail">集客详情</a>
            </dd>
        </#if>
    </#list>
    </#if>
    </dl>
</li>
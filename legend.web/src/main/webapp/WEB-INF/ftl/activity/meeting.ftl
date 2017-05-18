<#include "layout/ng-header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/base.css?9a6e71972981853d8244af7aed722478"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/drainage_activities/drainage_detail.css?58232dd9806f74e1b7d33a14f5972871"/>
<div class="qxy_wrapper clearfix">
<#include "activity/activity_left.ftl" >
    <div class="main">
        <div class="top">
            <div class="act-head">
                <p class="name">${activityTemplate.actName}</p>

                <p class="brief">${activityTemplate.summary}</p>
            </div>
            <ul class="flow">
                <li content="1">了解活动详情</li>
                <li content="2">签订协议并报名</li>
                <li content="3">云修审核</li>
                <li content="4">活动上线</li>
            </ul>
        </div>
        <div class="content" id="serviceTplDiv" service_tpl_id="${activityServiceTemplateVo.serviceTplId}" act_tpl_id="${activityServiceTemplateVo.actTplId}">
            <div class="act-detail">
                <div class="inner clearfix">
                    <div class="l">
                        <img src="${activityServiceTemplateVo.imgUrl}" onerror="this.src='${BASE_PATH}/resources/images/drainage_activities/tqyx_img.png'"
                             alt="${activityServiceTemplateVo.name}"/>
                    </div>
                    <div class="r">
                        <p class="join-row">
                            <span class="label">活动累计报名:</span>
                            <br/>
                            <span><strong>${activityServiceTemplateVo.joinNum}</strong>家门店</span>
                        </p>

                        <div class="join-box">
                            <div class="check-row">
                                <label for="J-agree"><input
                                        type="checkbox" class="J-agree" id="J-agree"<#if activityServiceTemplateVo.joinFlag ==1 || activityServiceTemplateVo.joinFlag ==2> checked="checked"</#if>/>我已经阅读并完全同意<a
                                        href="javascript:void(0);" class="protocol-btn" id="protocolBtn">《协议》</a></label>
                            </div>
                            <#if (activityServiceTemplateVo.joinFlag ==0 || activityServiceTemplateVo.joinFlag ==3) && activityServiceTemplateVo.editStatus == 1>
                            <div class="service-price-box">
                                <span class="label">服务价格</span>
                                <input class="input J_input_limit" data-limit_type="price" maxlength="8" type="text" id="servicePrice" value="<#if activityServiceTemplateVo.editStatus == 1 && activityServiceTemplateVo.shopServiceInfo == null >${activityServiceTemplateVo.servicePrice}<#else>${activityServiceTemplateVo.shopServiceInfo.servicePrice}</#if>"/>
                            </div>
                            </#if>
                            <#if activityServiceTemplateVo.joinFlag ==0><button type="button" class="join-btn" id="J-join">立即报名</button></#if>
                            <#if activityServiceTemplateVo.joinFlag ==1 ><button type="button" class="join-btn audit_ing">审核中</button></#if>
                            <#if activityServiceTemplateVo.joinFlag ==2 ><span class="notice-success">审核成功!</span> <button type="button" class="join-btn audit_fail" id="J-exit"> 取消参加</button></#if>
                            <#if activityServiceTemplateVo.joinFlag ==3 ><span class="notice-fail" >审核未通过!<br />
                            <a href="javascript:void(0);" id="J-view-fail" reason="${activityServiceTemplateVo.auditReason}">查看失败原因</a></span><button type="button" class="join-btn " id="J-rejoin">再次报名参加</button></#if>
                        </div>
                    </div>
                    <div class="m">
                        <p class="name-row">${activityServiceTemplateVo.name}</p>
                        <p class="price-row">
                            <#if activityServiceTemplateVo.servicePrice gt 0 >
                                <#if activityServiceTemplateVo.editStatus == 1>
                                    活动推荐价：<strong>${activityServiceTemplateVo.servicePrice}</strong>元　门店可自主填写　
                                <#else>
                                    活动市场价：<strong>${activityServiceTemplateVo.servicePrice}</strong>元　
                                </#if>
                            </#if>
                            <#if activityServiceTemplateVo.settlePrice gt 0 >
                            活动结算价：<strong>${activityServiceTemplateVo.settlePrice}</strong>元
                            </#if>
                        </p>

                        <p class="service-row"><span class="label">服务内容：</span><#if activityServiceTemplateVo.shopServiceNote>${activityServiceTemplateVo.shopServiceNote}</#if>
                        </p>
                    </div>
                </div>

            </div>
            <#if activityServiceTemplateVo.serviceInfoList>
            <div class="act-intro">
                <h3 class="title">活动介绍</h3>
                <div class="act-content">
                <#list  activityServiceTemplateVo.serviceInfoList as serviceInfo>
                    <div class="service-detail-box">
                        <h2>${serviceInfo.name}</h2>
                        <#list serviceInfo.list as imgUrl>

                            <ul class="list list-detail list-paddingleft-2">
                                <img src="${imgUrl.imgUrl}" style="width:100%"/>
                            </ul>
                        </#list>
                    </div>

                </#list>
                </div>
            </div>
            </#if>
        </div>
    </div>
</div>

<script id="protocolTpl" type="text/html">
    <div class="popup-box">
        <div class="popup-head">
            <h1>${activityTemplate.actName}合作协议</h1>
        </div>
        <div class="popup-content">
            <div class="popup-scroller">
                ${activityServiceTemplateVo.agreement}
            </div>
        </div>
        <div class="popup-foot">
            <label for="agreeProtocol"><input id="agreeProtocol" type="checkbox"/>我已仔细阅读并同意</label>
            <button class="P-closeBtn fr" type="button">确定</button>
        </div>
    </div>
</script>


<script id="exit_activity_dialog" type="text/html">
    <div class="popup-box">
        <div class="popup-head">
            <h1 class="title">退出活动</h1>
        </div>
        <div class="popup-content">
            <textarea class="reason" maxlength="500" placeholder="请填写退出活动的原因"></textarea>
        </div>
        <div class="popup-foot tc">
            <button class="P-exit btn-danger" type="button">退出活动</button>
        </div>
    </div>
</script>
<script id="rejoinTpl" type="text/html">
    <div class="popup-box">
        <div class="popup-head">
            <h1 class="title">申请理由</h1>
        </div>
        <div class="popup-content">
            <textarea class="reason" maxlength="500" placeholder="再次报名参加，需要填写申请理由"></textarea>
        </div>
        <div class="popup-foot tc">
            <button id="P-rejoin" type="button">再次报名参加</button>
        </div>
    </div>
</script>

<script type="text/javascript" src="${BASE_PATH}/resources/script/page/activity/meeting.js?7da78ab6aa61b5154655a5d2d93e758d"></script>
<#include "layout/ng-footer.ftl" >
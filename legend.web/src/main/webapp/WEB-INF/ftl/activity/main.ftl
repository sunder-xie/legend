<#include "layout/ng-header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/base.css?9a6e71972981853d8244af7aed722478"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/drainage_activities/drainage_display.css?65da8f010fbd86b5a43a460da6517722"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/banner.css?ce49e105e2f15bdeea782c45ef0a778c"/>

<div class="qxy_wrapper clearfix">
<#include "activity/activity_left.ftl" >
    <div class="main">
        <div class="top">
        <#if (bannerConfigList?size > 0)>
            <div class="banner">
                <ul>
                    <#list bannerConfigList as banner>
                        <li  <#if banner_index==0> class="img_current" </#if> >
                            <a href="${banner.jumpUrl}"><img width="826" height="150" src="${banner.imgUrl}" alt="" /></a>
                        </li>
                    </#list>
                </ul>
                <ol>
                    <#list bannerConfigList as banner>
                        <li <#if banner_index==0> class="point_current" </#if> ></li>
                    </#list>
                </ol>
            </div>
        </#if>
        </div>

        <div class="content">
            <ul class="flow">
                <li content="1">报名活动</li>
                <li content="2">客户到店服务</li>
                <li content="3">对账结款</li>
            </ul>
            <div class="tabs">
                <ul class="tab">
                    <#list activityChannelList as channel >
                    <li class="tab-item <#if channel_index==0> active </#if>" data-channel="${channel.id}"><p>${channel.channelName}</p></li>
                    </#list>
                </ul>
                <div class="tab-content">
                    <ul class="act-list" id="content">
                    </ul>
                    <script type="text/html" id="contentTpl">
                        <%for(var index in templateData){%>
                        <%item = templateData[index]%>

                        <li class="act-detail" onclick="window.location.href='${BASE_PATH}/shop/activity/meeting?actTplId=<%=item.actTplId%>&serviceTplId=<%=item.serviceTplId%>'">
                            <div class="inner clearfix">
                                <div class="l">
                                    <a href="javascript:void(0);"><img src="<%=item.imgUrl%>" onerror="this.src='${BASE_PATH}/resources/images/drainage_activities/tqyx_img.png'"
                                         alt="<%=item.name%>"/></a>
                                </div>
                                <div class="r">
                                    <p class="join-row">
                                        <span class="label">已报名门店数</span>
                                        <br/>
                                        <span><strong><%=item.joinNum%></strong>家</span>
                                    </p>
                                    <%if(item.joinFlag==0){%><button type="button" class="join-btn" onclick="window.location.href='${BASE_PATH}/shop/activity/meeting?actTplId=<%=item.actTplId%>&serviceTplId=<%=item.serviceTplId%>'">报名参加</button><%}%>
                                    <%if(item.joinFlag==1){%><button type="button" class="join-btn audit_ing" onclick="window.location.href='${BASE_PATH}/shop/activity/meeting?actTplId=<%=item.actTplId%>&serviceTplId=<%=item.serviceTplId%>'">审核中</button><%}%>
                                    <%if(item.joinFlag==2){%><button type="button" class="join-btn joined" onclick="window.location.href='${BASE_PATH}/shop/activity/meeting?actTplId=<%=item.actTplId%>&serviceTplId=<%=item.serviceTplId%>'">已参加</button><%}%>
                                    <%if(item.joinFlag==3){%><button type="button" class="join-btn audit_fail" onclick="window.location.href='${BASE_PATH}/shop/activity/meeting?actTplId=<%=item.actTplId%>&serviceTplId=<%=item.serviceTplId%>'">审核未通过</button><%}%>
                                </div>
                                <div class="m">
                                    <p class="name-row"><%=item.name%></p>

                                    <p class="gain-row"><% if(item.profitPrice>0){%><span class="label">每单立赚：</span>最高赚 <strong><%=item.profitPrice%></strong> 元<%}%></p>

                                    <p class="price-row">
                                        <% if(item.servicePrice>0){%>
                                            <% if(item.editStatus == 1){ %>活动推荐价：<strong><%=item.servicePrice%></strong>元   门店可自主填写
                                            <%}else {%>活动市场价：<strong><%=item.servicePrice%></strong>元　　　
                                            <%}%>　
                                        <%}%>　　　　
                                        <% if(item.settlePrice>0){%>
                                        活动结算价：<strong><%=item.settlePrice%></strong>元
                                        <%}%>
                                    </p>
                                    <p class="service-row"><span class="label">服务内容：</span><% if(item.shopServiceNote){%><%if(item.shopServiceNote.length>100){%><%==item.shopServiceNote.substring(0,100)%>...<%}else{%><%==item.shopServiceNote%>
                                            <%}%>
                                        <%}%>
                                    </p>
                                </div>
                            </div>
                        </li>
                        <%}%>
                    </script>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/resources/js/lib/jquery.lunbo.js?0d26aceb12e57e42a097d447fb0528b6"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/script/page/activity/main.js?b6468b9f1c23f91d6e702c31e742a773"></script>
<#include "layout/ng-footer.ftl" >

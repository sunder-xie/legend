<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/activity/activity-list.css?f80ac897249b11d39e67b77032142dc3"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <h3 class="Z-title">客户营销  > <a href="${BASE_PATH}/marketing/ng/analysis/promotion">门店推广</a> >  <i>微信推广</i></h3>
        <div class="order-head clearfix">
            <span class="head-tab js-tab-item <#if actType==null||actType==1>current</#if>" data-value="1" data-target="activityType">活动列表</span>
            <span class="head-tab <#if isWechatShop != 1>display-none</#if> <#if actType==2>current</#if> js-tab-item" data-value="2" data-target="activityType">游戏列表</span>
            <a class="yqx-btn yqx-btn-default help clearfix" href="${BASE_PATH}/shop/help?id=92">
                <i class="question-icon"></i><i>帮助中心</i>
            </a>
        </div>
        <div class="order-body">
            <!-- 查询表单区域 start -->
            <div class="row" id="searchForm">
                <fieldset>
                    <div class="form-item">
                        <input class="yqx-input yqx-input-small" id="activityName" name="search_actNameLike" placeholder="活动名称">
                    </div>
                    <div class="form-item">
                        <input class="yqx-input yqx-input-small" placeholder="开始时间" id="startTime" name="search_startTime">
                        <span class="fa icon-calendar"></span>
                    </div>
                    至
                    <div class="form-item">
                        <input class="yqx-input yqx-input-small" placeholder="结束时间" id="endTime" name="search_endTime">
                        <span class="fa icon-calendar"></span>
                    </div>
                    <button class="yqx-btn js-search-btn yqx-btn-3 yqx-btn-small">查询</button>
                </fieldset>
                <input type="hidden" name="search_joinStatus" id="isJoin" value="1"/>
                <input type="hidden" name="search_wechatActivityType" id="activityType" value="<#if actType>${actType}<#else>1</#if>"/>
            </div>
            <div class="tabs-control">
                <span class="activity-tab-item js-tab-item current " data-value="1" data-target="isJoin">全部</span>
                <span class="activity-tab-item js-tab-item" data-value="2" data-target="isJoin">已参加</span>
                <span class="activity-tab-item js-tab-item" data-value="3" data-target="isJoin">未参加</span>
            </div>
            <table class="yqx-table" id="tableList"></table>
            <!-- 分页容器 start -->
            <div class="yqx-page" id="paging"></div>
            <!-- 分页容器 end -->
        </div>
    </div>
</div>
<!-- 表格数据模板 start -->
<script type="text/template" id="tableTpl">
    <thead>
    <tr>
        <th width="60">序号</th>
        <th>活动名称</th>
        <th>活动时间</th>
        <th width="70">参加时间</th>
        <th>状态</th>
        <th>预览</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%if(json.data && json.data.content && json.data.content.length){%>
        <%for(var i=0;i< json.data.content.length;i++){%>
            <%var item=json.data.content[i]%>
            <tr>
                <td><%=i+1%></td>
                <td class="multi-row">
                    <h3><%= item.actName%>
                        <%if(item.canJoin == 0){%>
                        <span class="openTips">开通门店微信公众号才可以参加</span>
                        <%}else if(item.canJoin == 2){%>
                        <span class="openTips">开通服务包模式才可以参加</span>
                        <%}%>
                    </h3>
                    <p class="pre">访问：<%= item.visitCount||0%>   参与：<%= item.partCount||0%></p>
                </td>
                <td class="pre-row"><%= item.timeLimitStr%></td>
                <td>
                    <p><%= item.gmtCreateStr%></p>
                </td>
                <td>
                    <p><%= item.shopActivityStatus%></p>
                </td>
                <td>
                    <div class="preview-qrcode-td">
                        <div class="preview-qrcode-small" data-url="<%= item.previewUrl%>"></div>
                        <div class="preview-qrcode" data-url="<%= item.previewUrl%>"></div>
                    </div>
                </td>
                <td>
                    <div>
                        <a class="yqx-btn yqx-btn-3 js-view <%if(item.canJoin != 1){%> yqx-btn-disable <%}%>" data-type="<%=item.wechatActivityType%>" data-tpl-id="<%=item.actTplId%>"
                        href="
                        <%if(item.wechatActivityType==1){%>
                            ${BASE_PATH}/shop/wechat/activity-detail?actTplId=<%=item.actTplId%>
                        <%}else if(item.wechatActivityType==2){%>
                           ${BASE_PATH}/shop/wechat/activity-game-detail?gameId=<%=item.id%>
                        <%}else if(item.wechatActivityType==3 || item.wechatActivityType==4 || item.wechatActivityType==5){%>
                           javascript:;
                        <%}%>
                        ">查看
                        </a>
                    </div>
                    <#if isWechatShop == 1>
                    <div class="marT10">
                        <button class="yqx-btn  js-report yqx-btn-3 <%if(item.isJoin == 0 || item.canJoin != 1){%> yqx-btn-disable <%}%>" data-id="<%=item.id%>"
                                data-type="<%=item.wechatActivityType%>">报表查看
                        </button>
                    </div>
                    <#else>
                    <div class="marT10">
                        <button class="yqx-btn  js-share yqx-btn-3 <%if(item.shopActivityStatus != '已参加'){%> yqx-btn-disable <%}%>" data-id="<%=item.id%>">分享朋友圈</button>
                    </div>
                    </#if>
                </td>
            </tr>
        <%}%>
    <%}else{%>
    <tr>
        <td colspan="6">暂无数据！</td>
    </tr>
    <%}%>
    </tbody>
</script>
<!-- 表格数据模板 end -->

<!-- 服务报表查看浮层模板 start -->
<script type="text/template" id="serviceReportTpl">
<%if(json.data && json.data.data){%>
    <%var data = json.data.data%>
    <input type="hidden" id="parentId" value="<%=parentId%>"/>
    <input type="hidden" id="type" value="1"/>
    <div class="reportCheck">
        <h2 >活动已进行<%=data.durationDay||0%>天</h2>
        <div class="statistics report-box fl">
            <h3 class="headline">预约服务统计：<%=data.appointCount||0%>人</h3>
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>名称</th>
                    <th>预约用户数</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <%if(data.actServiceDTOs && data.actServiceDTOs.length){%>
                    <%for(var i = 0; i < data.actServiceDTOs.length; i++){%>
                        <%var item = data.actServiceDTOs[i]%>
                        <tr>
                            <td><%=i+1%></td>
                            <td class="td-ellipsis" title="<%=item.serviceName%>"><%=item.serviceName%></td>
                            <td><%=item.serviceNum%></td>
                            <td class="js-customer-detail <%if(item.serviceNum == 0){%> no-count <%}%>" data-id="<%=item.serviceId%>">查看</td>
                        </tr>
                    <%}%>
                <%}else{%>
                <tr>
                    <td colspan="4">暂无数据！</td>
                </tr>
                <%}%>
                </tbody>
            </table>
        </div>
        <div class="customerDetail report-box fr">

        </div>
    </div>
<%}%>
</script>
<script type="text/template" id="serviceDetailTpl">
    <h3 class="headline">用户详情</h3>
    <table class="yqx-table" >
        <thead>
            <tr>
                <th>序号</th>
                <th>手机号码</th>
            </tr>
        </thead>
        <tbody>
            <%if(!!json.data && !!json.data.content & !!json.data.content.length){%>
                <%var content = json.data.content;%>
                <%for(var i=0; i < content.length; i++){%>
                <%var item = content[i]%>
                <tr>
                    <td><%=i+1%></td>
                    <td><%=item.userMobile%></td>
                </tr>
                <%}%>
            <%}else{%>
            <tr>
                <td colspan="2">暂无数据！</td>
            </tr>
            <%}%>
        </tbody>
    </table>
    <div class="pageControl clearfix marT10">
        <button class="yqx-btn yqx-btn-1 js-pre-page page-btn fl">上一页</button>
        <button class="yqx-btn yqx-btn-3 js-next-page page-btn fr">下一页</button>
    </div>
</script>
<!-- 服务报表查看浮层模板 end -->

<!-- 游戏报表查看浮层模板 start -->
<script type="text/template" id="gameReportTpl">
    <%if(json.data ){%>
    <%var data = json.data%>
    <input type="hidden" id="parentId" value="<%=parentId%>"/>
    <input type="hidden" id="type" value="2"/>
    <div class="reportCheck">
        <h2 >活动已进行<%=data.durationDay||0%>天</h2>
        <div class="statistics report-box fl">
            <h3 class="headline">游戏兑换用户：<%=data.couponSum||0%>人</h3>
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>卡券名称</th>
                    <th>兑换用户数</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <%if(data.gameCouponStatisticDTOList && data.gameCouponStatisticDTOList.length){%>
                <%for(var i = 0; i < data.gameCouponStatisticDTOList.length; i++){%>
                <%var item = data.gameCouponStatisticDTOList[i]%>
                <tr>
                    <td><%=i+1%></td>
                    <td class="td-ellipsis" title="<%=item.couponName%>"><%=item.couponName%></td>
                    <td><%=item.couponNum%></td>
                    <td class="js-customer-detail <%if(item.couponNum == 0){%> no-count <%}%>" data-id="<%=item.couponInfoId%>">查看</td>
                </tr>
                <%}%>
                <%}else{%>
                <tr>
                    <td colspan="4">暂无数据！</td>
                </tr>
                <%}%>
                </tbody>
            </table>
        </div>
        <div class="customerDetail report-box fr">
        </div>
    </div>
    <%}%>
</script>
<script type="text/template" id="gameDetailTpl">
    <h3 class="headline">用户详情</h3>
    <table class="yqx-table" >
        <thead>
        <tr>
            <th>序号</th>
            <th>游戏得分</th>
            <th>手机号码</th>
        </tr>
        </thead>
        <tbody>
        <%if(!!json.data && !!json.data.content && !!json.data.content.length){%>
            <%var content = json.data.content;%>
            <%for(var i=0; i < content.length; i++){%>
                <%var item = content[i]%>
                <tr>
                    <td><%=i+1%></td>
                    <td><%=item.gameScore%></td>
                    <td><%=item.userMobile%></td>
                </tr>
            <%}%>
        <%}else{%>
        <tr>
            <td colspan="3">暂无数据！</td>
        </tr>
        <%}%>
        </tbody>
    </table>
    <div class="pageControl clearfix marT10">
        <button class="yqx-btn yqx-btn-1 js-pre-page page-btn fl">上一页</button>
        <button class="yqx-btn yqx-btn-3 js-next-page page-btn fr">下一页</button>
    </div>
</script>
<!-- 游戏报表查看浮层模板 end -->

<!-- 砍价活动报表查看浮层模板 start -->
<script type="text/template" id="discountReportTpl">
    <%if(json.data && json.data.data){%>
    <%var data = json.data.data%>
    <input type="hidden" id="parentId" value="<%=parentId%>"/>
    <input type="hidden" id="type" value="3"/>
    <div class="reportCheck">
        <h2 >活动已进行<%=data.durationDay||0%>天</h2>
        <div class="statistics report-box fl">
            <h3 class="headline">参与活动人数：<%=data.partakeNum||0%>人</h3>
            <table class="yqx-table">
                <thead>
                <tr>
                    <th width="20">序号</th>
                    <th width="110">服务名称</th>
                    <th>发起用户</th>
                    <th>参与用户</th>
                    <th>预约用户</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <%if(data.shopActDiscountDTOs && data.shopActDiscountDTOs.length){%>
                <%for(var i = 0; i < data.shopActDiscountDTOs.length; i++){%>
                <%var item = data.shopActDiscountDTOs[i]%>
                <tr>
                    <td><%=i+1%></td>
                    <td class="td-ellipsis" title="<%=item.serviceName%>"><%=item.serviceName%></td>
                    <td><%=item.fromOpNum||0%></td>
                    <td><%=item.partakeNum||0%></td>
                    <td><%=item.serviceNum%></td>
                    <td class="js-customer-detail <%if(item.serviceNum == 0){%> no-count <%}%>" data-id="<%=item.serviceId%>">查看</td>
                </tr>
                <%}%>
                <%}else{%>
                <tr>
                    <td colspan="6">暂无数据！</td>
                </tr>
                <%}%>
                </tbody>
            </table>
        </div>
        <div class="customerDetail report-box fr">

        </div>
    </div>
    <%}%>
</script>
<script type="text/template" id="discountDetailTpl">
    <h3 class="headline">用户详情</h3>
    <table class="yqx-table">
        <thead>
        <tr>
            <th>序号</th>
            <th>手机号码</th>
            <th>帮Ta砍价</th>
        </tr>
        </thead>
        <tbody>
        <%if(!!json.data && !!json.data.content & !!json.data.content.length){%>
        <%var content = json.data.content;%>
        <%for(var i=0; i < content.length; i++){%>
        <%var item = content[i]%>
        <tr>
            <td><%=i+1%></td>
            <td><%=item.userMobile%></td>
            <td><%=item.toFromNum%>人</td>
        </tr>
        <%}%>
        <%}else{%>
        <tr>
            <td colspan="3">暂无数据！</td>
        </tr>
        <%}%>
        </tbody>
    </table>
    <div class="pageControl clearfix marT10">
        <button class="yqx-btn yqx-btn-1 js-pre-page page-btn fl">上一页</button>
        <button class="yqx-btn yqx-btn-3 js-next-page page-btn fr">下一页</button>
    </div>
</script>
<!-- 砍价活动报表查看浮层模板 end -->

<!-- 拼团活动报表查看浮层模板 start -->
<script type="text/template" id="groupBuyReportTpl">
    <%if(json.data){%>
    <%var data = json.data%>
    <input type="hidden" id="parentId" value="<%=parentId%>"/>
    <input type="hidden" id="type" value="4"/>
    <div class="reportCheck">
        <h2 >活动已进行<%=data.durationDay||0%>天</h2>
        <div class="statistics report-box fl">
            <h3 class="headline">参与活动人数：<%=data.totalUser||0%>人</h3>
            <table class="yqx-table">
                <thead>
                <tr>
                    <th width="20">序号</th>
                    <th width="110">服务名称</th>
                    <th>发起用户</th>
                    <th>参与用户</th>
                    <th>拼团用户</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <%if(data.detail && data.detail.length){%>
                    <%for(var i = 0; i < data.detail.length; i++){%>
                        <%var item = data.detail[i]%>
                        <tr>
                            <td><%=i+1%></td>
                            <td class="td-ellipsis" title="<%=item.serviceName%>"><%=item.serviceName%></td>
                            <td><%=item.leaderNum||0%></td>
                            <td><%=item.joinNum||0%></td>
                            <td><%=item.groupBuyNum%></td>
                            <td class="js-customer-detail <%if(item.groupBuyNum == 0){%> no-count <%}%>" data-id="<%=item.serviceId%>">查看</td>
                        </tr>
                    <%}%>
                <%}else{%>
                <tr>
                    <td colspan="6">暂无数据！</td>
                </tr>
                <%}%>
                </tbody>
            </table>
        </div>
        <div class="customerDetail report-box fr">
        </div>
    </div>
    <%}%>
</script>
<script type="text/template" id="groupBuyDetailTpl">
    <h3 class="headline">用户详情</h3>
    <table class="yqx-table">
        <thead>
        <tr>
            <th>序号</th>
            <th>手机号码</th>
        </tr>
        </thead>
        <tbody>
        <%if(!!json.data && !!json.data.content & !!json.data.content.length){%>
            <%var content = json.data.content;%>
            <%for(var i=0; i < content.length; i++){%>
                <%var item = content[i]%>
                <tr>
                    <td><%=i+1%></td>
                    <td><%=item.userMobile%></td>
                </tr>
            <%}%>
        <%}else{%>
        <tr>
            <td colspan="2">暂无数据！</td>
        </tr>
        <%}%>
        </tbody>
    </table>
    <div class="pageControl clearfix marT10">
        <button class="yqx-btn yqx-btn-1 js-pre-page page-btn fl">上一页</button>
        <button class="yqx-btn yqx-btn-3 js-next-page page-btn fr">下一页</button>
    </div>
</script>
<!-- 拼团活动报表查看浮层模板 end -->

<!-- 保险优惠券砍价活动报表查看浮层模板 start -->
<script type="text/template" id="discountCouponReportTpl">
    <%if(json.data){%>
    <%var data = json.data%>
    <input type="hidden" id="parentId" value="<%=parentId%>"/>
    <input type="hidden" id="type" value="5"/>
    <div class="reportCheck">
        <h2 >活动已进行<%=data.durationDay||0%>天</h2>
        <div class="statistics report-box fl">
            <h3 class="headline">参与活动人数：<%=data.total||0%>人</h3>
            <table class="yqx-table">
                <thead>
                <tr>
                    <th width="20">序号</th>
                    <th width="110">卡券名称</th>
                    <th>参与用户数</th>
                    <th>获得卡券用户</th>
                </tr>
                </thead>
                <tbody>
                <%if(data.info && data.info.length){%>
                <%for(var i = 0; i < data.info.length; i++){%>
                <%var item = data.info[i]%>
                <tr>
                    <td><%=i+1%></td>
                    <td class="js-show-tips ellipsis-1"><%=item.couponName%></td>
                    <td><%=item.participationNum%></td>
                    <td class="js-customer-detail <%if(item.participationNum == 0){%> no-count <%}%>" data-id="<%=item.tmplId%>">查看</td>
                </tr>
                <%}%>
                <%}else{%>
                <tr>
                    <td colspan="4">暂无数据！</td>
                </tr>
                <%}%>
                </tbody>
            </table>
        </div>
        <div class="customerDetail report-box fr">

        </div>
    </div>
    <%}%>
</script>
<script type="text/template" id="discountCouponDetailTpl">
    <h3 class="headline">用户详情</h3>
    <table class="yqx-table">
        <thead>
        <tr>
            <th>序号</th>
            <th>手机号码</th>
            <th>帮Ta砍价</th>
        </tr>
        </thead>
        <tbody>
        <%if(!!json.data && !!json.data.content & !!json.data.content.length){%>
        <%var content = json.data.content;%>
        <%for(var i=0; i < content.length; i++){%>
        <%var item = content[i]%>
        <tr>
            <td><%=i+1%></td>
            <td><%=item.mobile%></td>
            <td><%=item.helpUserNum%>人</td>
        </tr>
        <%}%>
        <%}else{%>
        <tr>
            <td colspan="3">暂无数据！</td>
        </tr>
        <%}%>
        </tbody>
    </table>
    <div class="pageControl clearfix marT10">
        <button class="yqx-btn yqx-btn-1 js-pre-page page-btn fl">上一页</button>
        <button class="yqx-btn yqx-btn-3 js-next-page page-btn fr">下一页</button>
    </div>
</script>
<!-- 保险优惠券砍价活动报表查看浮层模板 end -->

<!--分享朋友圈模板 start-->
<script type="text/template" id="qrcodeTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            分享朋友圈
        </div>v
        <div class="bounce-content-qrcode">
            <div id="qrcodeView" class="qrcode-view">
                <div></div>
            </div>
            <div class="link-box">
                <p>链接地址</p>
                <input id="linkAddress" type="text" class="yqx-input" value="<%=url%>"/>
                <div>
                    <button class="yqx-btn yqx-btn-3" id="copyUrl">复制链接地址</button>
                </div>
            </div>
        </div>
    </div>
</script>
<!--分享朋友圈模板 end-->

<!--公共弹窗模板 start-->
<script type="text/template" id="bounceTpl">
    <div class="collection-bounce">
        <div class="bounce-title">提示</div>
        <div class="bounce-content">
            <div class="bounce-row"><%=data.limitMsg%></div>
        </div>
        <div class="bounce-foot">
            <%if(data.code == '003'){%>
            <a class="yqx-btn yqx-btn-3" href="http://t.cn/RVRfrl4">查看增粉秘籍</a>
            <%}else if(data.code == '-1'){%>
            <a class="yqx-btn yqx-btn-3" href="http://t.cn/RVRI5ey">查看秘籍</a>
            <a class="yqx-btn yqx-btn-3" href="${BASE_PATH}/shop/wechat/appservice/list">发布车主服务</a>
            <%}%>
        </div>
    </div>
</script>
<!--公共弹窗模板 end-->
<script src="${BASE_PATH}/static/third-plugin/zclip/jquery.zclip.min.js"></script>
<script src="${BASE_PATH}/static/third-plugin/qrcode/jquery.qrcode.min.js"></script>
<script src="${BASE_PATH}/static/js/page/wechat/activity/activity-list.js?0144dc27aba876d4e58065c2f7457f9a"></script>
<#include "yqx/layout/footer.ftl" >
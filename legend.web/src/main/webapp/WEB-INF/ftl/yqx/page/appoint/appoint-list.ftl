<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/appoint/appoint-list.css?b809c929f3552c740988ed694d6dd1e1"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline fl">预约单查询</h1>
        </div>

        <div class="search-box clearfix" id="orderListForm">
            <!--查询条件 start-->
            <div class="search-input fl">
                <div class="form-item search-zh js-search-zh">
                    <input type="text" name="search_keyword" class="yqx-input yqx-input-small" value="" placeholder="车牌/车主电话/车主">
                    <input type="hidden" name="search_payStatus" value="1"/>
                </div>
                <!--高级搜索 start-->
                <ul class="senior-box">
                    <li>
                    <div class="form-item senior-width">
                        <input type="text" name="search_licenseLIKE" class="yqx-input yqx-input-small" value="" placeholder="车牌">
                    </div><div class="form-item senior-width">
                        <input type="text" name="search_mobileLIKE" class="yqx-input yqx-input-small" value="" placeholder="车主电话">
                    </div><div class="form-item senior-width">
                        <input type="text" name="search_customerNameLIKE" class="yqx-input yqx-input-small" value="" placeholder="车主">
                    </div><div class="form-item senior-width">
                        <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-down-payment"
                               placeholder="有无预付定金"/>
                        <input type="hidden" name="search_hasDownPayment"/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    </li>
                    <li>
                    <div class="form-item senior-width">
                    <input type="text" name="search_startTime" class="yqx-input yqx-input-small yqx-input-icon" id="startDate" placeholder="预约开始时间"/>
                    <span class="fa icon-small icon-calendar"></span>
                </div>至<div class="form-item senior-width">
                    <input type="text" name="search_endTime" class="yqx-input yqx-input-small yqx-input-icon" id="endDate" placeholder="预约结束时间"/>
                    <span class="fa icon-small icon-calendar"></span>
                </div>
                    </li>
                </ul>
                <!-- 高级搜索 end -->
            </div>
                <div class="search-btn-box fr"><button class="yqx-btn yqx-btn-3 yqx-btn-small search-btn js-search-btn">查询</button>
                <#--<a href="javascript:;" class="senior-search js-senior">高级搜索</a>-->
            </div>
            <!--查询条件 end-->
        </div>
        <!--tab start-->
        <div class="tabbox">
            <a href="javascript:;" class="card-tab js-card"><i class="icon-th-large"></i>卡片</a>|
            <a href="javascript:;" class="card-tab js-table"><i class="icon-reorder"></i>列表</a>
            <a href="javascript:;" class="js-export fr"><i class="icon-signout"></i>导出Excel</a>
        </div>
        <!--tab end-->

        <!-- 卡片 start-->
        <div class="tabcon cardcon clearfix js-cardcon" id="cardCon">

        </div>
        <!-- 卡片 end-->

        <!-- 列表 start-->
        <div class="tabcon js-tabcon" id="tableCon">

        </div>
        <!-- 列表 end-->
    </div>
    <!-- 右侧内容区 end -->
</div>
<!-- 卡片模板 start -->
<script type="text/html" id="cardTpl">
    <ul class="card-box clearfix">
        <%if (json.success) {%>
        <%if (json.data && json.data.content){%>
        <%for (var i=0;i< json.data.content.length;i++) {%>
        <%var item = json.data.content[i]%>
        <li class="js-infor-btn" data-appoint-id="<%=item.id%>" data-customer-car-id="<%=item.customerCarId%>">
            <div class="card-top">
                <div class="plate-number">
                    <div class="plate-left fl"><%=item.license%></div>
                    <div class="status fr
                                <% if(item.status==0){%>to-confirm-color<%}%>
                                <% if(item.status==1){%>confirmed-color<%}%>
                                <% if(item.status==2){%>ordered-color<%}%>
                                <% if(item.status==3||item.status==4||item.status==5){%>canceled-color<%}%>">
                        <%=getStatus(item.status)%>
                    </div>
                </div>
                <div class="car-infor"><%=item.carInfo%></div>
            </div>
            <div class="card-middle">
                <div class="car-middle-infor">车主电话：<%=item.mobile%></div>
                <!--取消原因-->
                <%if(item.status == -1||item.status == 3 || item.status == 4 ||item.status == 5){%>
                <div class="car-middle-infor">取消原因：<%=item.cancelReason%></div>
                <%}else{%>
                <!---预约内容-->
                <div class="car-middle-infor">预约内容：<%=item.appointContent%></div>
                <%}%>
                <div class="car-middle-infor">预约时间：<span class="time-color"><%=item.appointTimeFormat%></span></div>
            </div>
            <div class="card-bot clearfix">
                <!--无效-->
                <%if(item.status == -1){%>
                <a href="javascript:;" class="yqx-btn-3 order">详情</a>
                <%}%>
                <!--待确认-->
                <%if(item.status == 0){%>
                <a href="javascript:;" class="yqx-btn-3 order js_confirm_btn">确认</a>
                <a href="javascript:;" class="yqx-btn-2 chanel js-chanel-dialog">取消</a>
                <%}%>
                <!--已确认-->
                <%if(item.status == 1){%>
                <a href="javascript:;" class="yqx-btn-3 order js-orderlist-dialog">开单</a>
                <a href="javascript:;" class="yqx-btn-2 chanel js-chanel-dialog ">取消</a>
                <%}%>
                <!--已开单-->
                <%if(item.status == 2){%>
                <a href="javascript:;" class="yqx-btn-3 order">详情</a>
                <%}%>
                <!--已取消-->
                <%if(item.status == 3 || item.status == 4 ||item.status == 5){%>
                <a href="javascript:;" class="yqx-btn-3 order">详情</a>
                <%}%>
            </div>
        </li>
        <%}}}%>
    </ul>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="cardPage"></div>
    <!-- 分页容器 end -->
</script>
<!-- 卡片模板 end -->

<!-- 列表模板 start -->
<script type="text/html" id="tableTpl">
   <div class="order-list-table-inner">
    <div class="table-scroll-box">
        <table class="yqx-table yqx-table-hover yqx-table-link appoint-table">
            <thead>
            <tr>
                <th class="serial-number">序号</th>
                <th>预约时间</th>
                <th>车主</th>
                <th>车牌</th>
                <th>车主电话</th>
                <th>预约内容</th>
                <th>预付定金</th>
                <th>状态</th>
                <th class="serial-number">操作</th>
            </tr>
            </thead>
            <tbody>
            <%if (json.success) {%>
            <%if (json.data && json.data.content){%>
            <%for (var i=0;i< json.data.content.length;i++) {%>
            <%var item = json.data.content[i]%>
            <tr data-appoint-tableid="<%=item.id%>" class="js-inforlink">
                <td class="serial-number"><%=json.data.size*(json.data.number)+i+1%></td>
                <td ><%=item.appointTimeFormat%></td>
                <td ><%=item.customerName%></td>
                <td><%=item.license%></td>
                <td><%=item.mobile%></td>
                <td><div class="appoint-content" title="<%=item.appointContent%>"><%=item.appointContent%></div></td>
                <td>￥<%=item.downPayment.toFixed(2)%></td>
                <td class="<% if(item.status==-1){%>invalid-color<%}%>
                           <% if(item.status==0){%>to-confirm-color<%}%>
                           <% if(item.status==1){%>confirmed-color<%}%>
                           <% if(item.status==2){%>ordered-color<%}%>
                           <% if(item.status==3||item.status==4||item.status==5){%>canceled-color<%}%>">
                    <%=getStatus(item.status)%>
                </td>
                <#--<%if(enList){%>
                <%for(var j=0;j<enList.length;j++){%>
                <td><%=item[enList[j]]%></td>
                <%}%>
                <%}%>-->
                <td class="js-td-operate serial-number" data-appoint-id="<%=item.id%>" data-customer-car-id="<%=item.customerCarId%>">
                    <!--已确认-->
                    <%if(item.status == 1){%>
                    <a href="javascript:;" class="table-order js-orderlist-dialog">开单</a>
                    <a href="javascript:;" class="table-chanel js-chanel-dialog">取消</a>
                    <%}%>
                    <!--待确认-->
                    <%if(item.status == 0){%>
                    <a href="javascript:;" class="table-confirm js_confirm_btn">确认</a>
                    <a href="javascript:;" class="table-chanel js-chanel-dialog">取消</a>
                    <%}%>
                    <!--已取消-->
                    <%if(item.status == 3 || item.status == 4 ||item.status == 5){%>

                    <%}%>
                    <!--已开单-->
                    <%if(item.status == 2){%>

                    <%}%>
                </td>
            </tr>
            <%}}}%>
            </tbody>
        </table>
    </div>
    </div>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="tablePage"></div>
    <!-- 分页容器 end -->
</script>
<!-- 列表模板 end -->

<!-- 开单dialog start -->
<script type="text/html" id="orderlist-dialog">
    <div class="tank">
        <div class="btn_group">
            开单
        </div>
        <div class="t_middle">
            <#if BPSHARE != 'true'>
            <a href="javascript:;" class="link-btn js-carwash" data-customer-car-id="<%=appoint.customerCarId%>" data-appoint-id="<%=appoint.appointId%>">开洗车单</a>
            <a href="javascript:;" class="link-btn js-fast" data-appoint-id="<%=appoint.appointId%>">开快修快保单</a>
            </#if>
            <!-- 档口店不显示综合维修单 -->
            <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
            <a href="javascript:;" class="link-btn js-zh" data-appoint-id="<%=appoint.appointId%>">开综合维修单</a>
            </#if>
        </div>
    </div>
</script>
<!-- 开单dialog end -->


<!--取消dialog start -->
<script type="text/html" id="chanel-dialog">
    <div class="tank">
        <div class="btn_group">
            取消原因
        </div>
        <div class="t_middle">
            <div class="link-chanel-btn js-hover">时间冲突</div>
            <div class="link-chanel-btn js-hover">物料不够</div>
            <div class="link-chanel-btn js-hover">和车主协商一致取消</div>
            <div class="link-chanel-btn js-hover">其他</div>
        </div>
        <div class="t-bottom">
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-cancelAppoint" data-appoint-id="<%=appointId%>">提交</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-chanel-close">取消</button>
        </div>
    </div>
</script>
<!-- 取消dialog end -->


<iframe id="my_iframe" style="display:none;"></iframe>
<script>
        $('#my_iframe').src = BASE_PATH + '/shop/appoint/appoint-list?search_formType=w#1';
</script>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/appoint/appoint-list.js?8c39a327fe9833a861015380dadb4d99"></script>

<!-- 脚本引入区 end -->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/customer/car-detail.css?fb99829a9d5fd5926f23de340c09020a"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <!-- 保存车辆的id -->
        <input type="hidden" id="id" value="${carDetailVo.customerCar.id}"/>
        <input type="hidden" id="license" value="${carDetailVo.customerCar.license}"/>
        <input type="hidden" id="refer" value="${refer}"/>
        <!-- 详细信息 -->
        <div class="query-detail">
            <div class="query-nav">
                <h3 class="query-heading">车辆详情页</h3>

                <div class="query-nav-btns">
                    <#if BPSHARE != 'true'>
                    <div class="yqx-btn nav-btn btn-blue" id="addCarwash"><i class="fa icon-plus btn-plus"></i> <span>洗车单</span></div>
                    <div class="yqx-btn nav-btn btn-org" id="addSpeedily"><i class="fa icon-plus btn-plus"></i> <span>快修快保单</span></div>
                    </#if>
                    <!-- 档口店不显示综合维修单 -->
                    <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                    <div class="yqx-btn nav-btn yqx-btn-3" id="addOrder"><i class="fa icon-plus btn-plus"></i> <span>综合维修单</span></div>
                    </#if>
                    <#if BPSHARE != 'true'>
                    <div class="yqx-btn nav-btn btn-brown" id="addSell"><i class="fa icon-plus btn-plus"></i> <span>销售单</span></div>
                    </#if>
                </div>
            </div>
            <div class="query-carinfo">
                <div class="info-head">
                    <h3 class="query-heading">${carDetailVo.customerCar.license}</h3>
                    <div class="info-tags">
                    <#list carDetailVo.customerTagList as customerTag>
                        <div tag-id="${customerTag.id}" class="info-tag<#if !customerTag.choose> tag-gray</#if><#if customerTag.choose> tag-green</#if><#if customerTag.tagRefer == 1> js-tag-edit</#if>">
                            <i>${customerTag.tagName}</i>
                        </div>
                    </#list>
                        <div class="info-tag tag-add" id="tagAdd">
                            <i class="icon-plus"></i>
                        </div>
                        <div class="info-tag input-box" style="display: none">
                            <input class="tag-input js-tag-input" placeholder="请输入" maxlength="6">
                            <i class="tag-btn" id="tagAddBtn">确定</i>
                        </div>
                    </div>
                    <div class="info-btn btn-complete">
                        <i class="icon-cog"></i>
                        <span class="edit-customer" customer-id="${carDetailVo.customerCar.id}">完善车辆信息</span>
                    </div>
                    <div class="info-btn btn-complete js-delete-car">
                        <i class="icon-trash"></i>
                        <span data-id="${carDetailVo.customerCar.id}" customer-id="${carDetailVo.customerCar.customerId}">删除车辆</span>
                    </div>
                    <div class="info-btn btn-complete js-change-customer">
                        <i class="icon-user"></i>
                        <span id="old-mobile" data-car-id="${carDetailVo.customerCar.id}" data-old-mobile="${carDetailVo.customer.mobile}">更换车主</span>
                    </div>
                </div>
                <div class="info-detail">
                    <div class="detail-table">
                        <div class="detail-tr">
                            <div class="detail-single max-width js-show-tips"><span class="padding-left-2 js-show-tips">车型：${carDetailVo.customerCar.carInfo}</span></div>
                            <div class="detail-single  max-width js-show-tips">
                            <#if carDetailVo.customerCar.carGearBox>
                                <span>　　年款排量：${carDetailVo.customerCar.carYear} ${carDetailVo.customerCar.carGearBox}</span>
                            <#else>
                                <span>　　年款排量：${carDetailVo.customerCar.carYear} ${carDetailVo.customerCar.carPower}</span>
                            </#if>
                            </div>
                            <div class="detail-single max-width js-show-tips"><span class="padding-left-2">　　车主：${carDetailVo.customer.customerName}</span></div>
                            <div class="detail-single"><span id="old_mobile">车主电话：${carDetailVo.customer.mobile}</span>
                                <#if accountInfo>
                                <div class="info-btn change-mobile">
                                    <a href="${BASE_PATH}/account/edit?refer=car_detail&accountId=${accountInfo.id}">修改车主电话</a>
                                </div>
                                </#if>
                            </div>
                        </div>
                        <div class="detail-tr">
                            <div class="detail-single max-width js-show-tips"><span>行驶里程：${carDetailVo.customerCar.mileage}km</span></div>
                            <div class="detail-single max-width js-show-tips"><span>下次保养里程：${carDetailVo.customerCar.upkeepMileage}km</span></div>
                            <div class="detail-single max-width js-show-tips"><span>下次保养时间：${carDetailVo.customerCar.keepupTimeStr}</span></div>
                            <div class="detail-single max-width js-show-tips"><span>保险到期：${carDetailVo.customerCar.insuranceTimeStr}</span></div>
                        </div>
                        <div class="detail-tr display-none">
                            <div class="detail-single max-width js-show-tips"><span>年审到期：${carDetailVo.customerCar.auditingTimeStr}</span></div>
                            <div class="detail-single max-width js-show-tips"><span class="padding-left-1">　　联系人：${carDetailVo.customer.contact}</span></div>
                            <div class="detail-single max-width js-show-tips"><span>　　联系电话：${carDetailVo.customer.contactMobile}</span></div>
                            <div class="detail-single max-width js-show-tips"><span>固定电话：${carDetailVo.customer.tel}</span></div>
                        </div>
                        <div class="detail-tr display-none">
                            <div class="detail-single js-show-tips"><span>客户单位：${carDetailVo.customer.company}</span></div>
                        </div>
                    </div>
                    <div class="detail-table display-none">
                        <div class="detail-tr">
                            <div class="detail-single js-show-tips"><span class="padding-left-1">VIN码：${carDetailVo.customerCar.vin}</span></div>
                            <div class="detail-single js-show-tips"><span>车辆级别：${carDetailVo.customerCar.carLevel}</span></div>
                            <div class="detail-single"><span>车辆别名：${carDetailVo.customerCar.byName}</span></div>
                            <div class="detail-single js-show-tips"><span>承保公司：${carDetailVo.customerCar.insuranceCompany}</span></div>
                        </div>
                        <div class="detail-tr">
                            <div class="detail-single"><span>车身颜色：${carDetailVo.customerCar.color}</span></div>
                            <div class="detail-single"><span>出厂年月：${carDetailVo.customerCar.productionDateStr}</span></div>
                            <div class="detail-single"><span>购车时间：${carDetailVo.customerCar.buyTimeStr}</span></div>
                            <div class="detail-single"><span>领证时间：${carDetailVo.customerCar.receiveLicenseTimeStr}</span></div>
                        </div>
                        <div class="detail-tr" >
                            <div class="detail-single js-show-tips"><span>发动机号：${carDetailVo.customerCar.engineNo}</span></div>
                            <div class="detail-single js-show-tips"><span>品牌型号：${carDetailVo.customerCar.carNumber}</span></div>
                            <div class="detail-single"><span>牌照类型：${carDetailVo.customerCar.licenseType}</span></div>
                            <div class="detail-single js-show-tips"><span title="${carDetailVo.customer.customerAddr}">客户地址：${carDetailVo.customer.customerAddr}</span></div>
                        </div>
                        <div class="detail-tr">
                            <div class="detail-single js-show-tips"><span>客户来源：${carDetailVo.customer.source}</span></div>
                            <div class="detail-single"><span>客户生日：${carDetailVo.customer.birthdayStr}</span></div>
                            <div class="detail-single js-show-tips"><span>身份证号：${carDetailVo.customer.identityCard}</span></div>
                            <div class="detail-single js-show-tips"><span title="${carDetailVo.customer.drivingLicense}">驾驶证号：${carDetailVo.customer.drivingLicense}</span></div>
                        </div>
                        <div class="detail-tr">
                            <div class="detail-single js-show-tips"><span class="padding-left-1">备注：${carDetailVo.customer.remark}</span></div>
                        </div>
                        <div class="detail-tr">
                            <div class="detail-single"><a href="javascript:;" class="img-link js-imgLayer color-orange">查看图片</a></div>
                        </div>

                    </div>
                    <div class="close-line">
                        <span class="icon-angle-down"></span>
                    </div>
                </div>
                <div class="info-more">
                    <div class="more-item">
                        <div class="more-head">
                            <h4>车辆分析</h4>
                        </div>
                        <section class="more-content sec-analy">
                            <p><span>到店次数<img src="${BASE_PATH}/static/img/common/base/question.png" class="notice" id="totalOrderCount"/>：</span><span>${carDetailVo.totalOrderCount}次</span></p>
                            <p><span>消费次数<img src="${BASE_PATH}/static/img/common/base/question.png" class="notice" id="validOrderCount"/>：</span><span>${carDetailVo.validOrderCount}次</span></p>
                            <p><span>挂帐工单数<img src="${BASE_PATH}/static/img/common/base/question.png" class="notice" id="suspendPaymentCount"/>：</span><span>${carDetailVo.suspendPaymentCount}单</span></p>
                            <p><span>近6月消费总金额<img src="${BASE_PATH}/static/img/common/base/question.png" class="notice" id="recent6MonthAmount"/>：</span><span>${carDetailVo.recent6MonthAmount}元</span></p>
                            <p><span>挂帐金额<img src="${BASE_PATH}/static/img/common/base/question.png" class="notice" id="suspendAmount"/>：</span><span class="suspendAmount">${carDetailVo.suspendAmount}</span>元</p>
                        </section>
                    </div><div class="more-item">
                    <div class="more-head">
                        <h4>客户优惠</h4>

                    </div>
                    <#--<div id="memberContent"></div>-->
                    <div id="discountInfo" class="discount-info">

                    </div>
                </div><div class="more-item" id="serviceContent">

                </div>
                </div>

            </div>
            <div class="query-orderdetail">
                <div class="orderdetail-nav">
                    <!-- 档口版门店没有回访信息-->
                <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                    <div class="tab-item current-item title-active js-tabs js-repair-order" data-class="repair-order">维修工单</div><div
                        class="tab-item js-tabs js-repair-parts" data-class="repair-parts">维修配件</div><div
                        class="tab-item js-tabs js-reserve-info" data-class="reserve-info">预约信息</div><div
                        class="tab-item js-tabs js-visit-info" data-class="visit-info">回访信息</div><div
                        class="tab-item js-tabs js-precheck-info" data-class="precheck-info">车况信息</div><div
                        class="tab-item js-tabs js-order-history" data-class="order-history">导入的工单</div><div
                        class="tab-item js-tabs js-tq-check" data-class="tq-check">淘汽检测单</div><div
                        class="tab-item js-tabs js-warehouse-out" data-class="warehouse-out">退货记录</div>
                <#else>
                    <div class="tab-item current-item title-active js-tabs js-repair-order" data-class="repair-order">维修工单</div><div
                        class="tab-item js-tabs js-repair-parts" data-class="repair-parts">维修配件</div><div
                        class="tab-item js-tabs js-reserve-info" data-class="reserve-info">预约信息</div><div
                        class="tab-item js-tabs js-precheck-info" data-class="precheck-info">车况信息</div><div
                        class="tab-item js-tabs js-order-history" data-class="order-history">导入的工单</div><div
                        class="tab-item js-tabs js-tq-check" data-class="tq-check">淘汽检测单</div><div
                        class="tab-item js-tabs js-warehouse-out" data-class="warehouse-out">退货记录</div>
                </#if>
                </div>
                <!-- 维修工单 start-->
                <div class="margin-body repair-order display-none">
                    <div class="condition-box" id="orderForm">
                        <!-- 保存车辆的id -->
                        <input type="hidden" name="search_orderTag" value="" id="search_orderTag"/>
                    </div>
                    <div class="orderdetail-subtab">
                        <div class="subtab subtab-active">全部工单</div>
                        <#if BPSHARE != 'true'>
                        <div class="subtab" order-tag="2">洗车单</div>
                        <div class="subtab" order-tag="3">快修快保单</div>
                        </#if>
                        <!-- 档口店不显示综合维修单和引流活动单 -->
                        <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                        <div class="subtab" order-tag="1">综合维修单</div>
                        </#if>
                        <#if BPSHARE != 'true'>
                        <div class="subtab" order-tag="5">销售单</div>
                        </#if>
                        <!-- 档口店不显示综合维修单和引流活动单 -->
                        <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                        <div class="subtab" order-tag="4">引流活动单</div>
                        </#if>
                    </div>
                    <div class="order-content order-repair" id="orderTable">
                    </div>
                </div>
                <!-- 维修工单 end -->
                <!-- 维修配件 start -->
                <div class="margin-body repair-parts display-none" id="goodsTable"></div>
                <!-- 维修配件 end -->
                <!-- 预约详细 start -->
                <div class="margin-body reserve-info display-none" id="appointTable"></div>
                <!-- 预约详细 end -->
                <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                <!-- 回访信息 start -->
                <div class="margin-body visit-info display-none" id="feedbackTable"></div>
                <!-- 回访信息 end -->
                </#if>
                <!-- 车况信息 start -->
                <div class="margin-body precheck-info display-none" id="precheckTable"></div>
                <!-- 车况信息 end -->
                <!-- 历史导入的工单信息 start -->
                <div class="margin-body order-history display-none" id="orderHistoryTable"></div>
                <!-- 历史导入的工单信息 end -->
                <!-- 淘汽检测单 start -->
                <div class="margin-body tq-check display-none" id="tqCheckTable"></div>
                <!-- 淘汽检测单 end -->
                <!-- 退货记录 start -->
                <div class="margin-body warehouse-out display-none" id="warehouseOutTable"></div>
                <!-- 退货记录 end -->
            </div>
        </div>
        <!-- 详细信息 结束 -->
    </div>
    <!-- 右侧内容区 end -->
</div>

<!-- 会员账户 start -->
<script type="text/html" id="memberTpl">
    <%if(data){
        var flags = false;
        var couponLength = 0;<!-- 遍历次数对多3个 -->
        var memberServiceCount = 0;<!-- 优惠券总数 -->
        if(data.accountCouponList){
            for(var i in data.accountCouponList){
                var accountCoupon = data.accountCouponList[i];
                memberServiceCount += accountCoupon.couponNum;
            }
        }
        if(data.accountComboList){
            memberServiceCount +=data.accountComboList.length;
        }
    }%>
    <%if(data.memberCard){%>
        <section class="more-sec">
            <p class="p-border">
                <span class="card-type js-show-tips ellipsis-1 width-160"><%if(data.memberCard.memberCardInfo)%><%= data.memberCard.memberCardInfo.typeName%>：<%= data.memberCard.cardNumber%></span>
                <span class="card-amount">余额：<%= data.memberCard.balance.toFixed(2)%>元</span>
            </p>
        </section>
    <%}else{%>
        <section class="more-sec">
            <p class="p-border">
                <span class="card-type"><img class="no-coupon" src="${BASE_PATH}/static/img/page/settlement/debit/no-coupon.png"/>没有会员卡</span>
            </p>
        </section>
    <%}%>
    <section class="more-sec sec-card">
        <p>
            <span>卡券：</span>
            <% if(memberServiceCount > 0) {%>
            <span class=""><%= memberServiceCount%>张</span>
            <%}%>
        </p>
        <%if(data.accountCouponList){%>
            <%for(var j = 0; j < data.accountCouponList.length; j++){%>
                <%if(couponLength < 3){%>
                    <% var accountCoupon = data.accountCouponList[j];%>
                    <p>
                        <span class="js-show-tips ellipsis-1 width-160"><%if(accountCoupon.couponInfo){%><%= accountCoupon.couponInfo.couponTypeName%><%}%>&nbsp;<%= accountCoupon.couponName%>：<%= accountCoupon.couponNum%>张</span>
                        <span class="deadline"><%= accountCoupon.effectiveDateStr%></span>
                    </p>
                    <% couponLength++;%>
                <%}%>
            <%}%>
        <%}%>
        <%if(data.accountComboList){%>
            <%for(var j = 0; j < data.accountComboList.length; j++){%>
                <%if(couponLength < 3){%>
                    <% var accountCombo = data.accountComboList[j];%>
                    <% var serviceList = accountCombo.serviceList;%>
                    <% var userServiceSize = 0;%>
                    <% for(var i in serviceList){%>
                        <% var service = serviceList[i];%>
                        <% userServiceSize += service.leftServiceCount;%>
                    <%}%>
                    <p>
                        <span class="js-show-tips ellipsis-1 width-160">计次卡&nbsp;<%= accountCombo.comboName%>：<%= userServiceSize%>次</span>
                        <span class="deadline"><%= accountCombo.expireDateStr%></span>
                    </p>
                    <% couponLength++;%>
                <%}%>
            <%}%>
        <%}%>
        <% if(memberServiceCount == 0) {%>
        <div class="service-no">
            <img class="member-img" src="${BASE_PATH}/static/img/page/customer/404card.png">
            <p>暂无优惠券</p>
        </div>
        <% }%>

    </section>
</script>
<!-- 会员账户 end -->

<!-- 推荐服务 start -->
<script type="text/html" id="serviceTpl">
    <div class="more-head">
        <h4>推荐服务</h4>
    </div>
    <section class="more-sec">
        <div class="sec-recoms">
            <%if(data){
            var serviceLength = 0;<!-- 推荐服务数 -->
            var serviceList = data.items;
            if(serviceList){
            serviceLength = serviceList.length;
            }
            %>
            <%for(var j = 0; j < serviceLength; j++){%>
            <%var service = serviceList[j];%>
            <p class="recom-cont"><%= service%></p>
            <%}%>
            <%}else{%>
            <div class="service-no">
                <img class="service-img" src="${BASE_PATH}/static/img/page/customer/404service.png">
                <p>暂无服务推荐</p>
            </div>
            <%}%>
        </div>
    </section>
</script>
<!-- 推荐服务 end -->

<!-- 维修工单 start -->
<script type="text/html" id="orderTpl">
    <%if(json.data && json.data.content){%>
    <%var orderTagList = ["综合维修","洗车","快修快保","引流活动","销售"];%>
    <%for (var i = 0;i < json.data.content.length; i++) {%>
    <%
    var orderInfo = json.data.content[i];
    var serviceList = orderInfo.serviceList;
    var goodsList = orderInfo.goodsList;
    var rowspan = 2;
    if (serviceList.length > 0) {
    rowspan += serviceList.length;
    } else {
    rowspan += 1;
    }
    if (goodsList.length > 0) {
    rowspan += goodsList.length;
    } else {
    rowspan += 1;
    }
    %>
    <table class="repair-table">
        <caption>
            <span class="padding-r10">工单编号：<a href="${BASE_PATH}/shop/order/detail?refer=customer&orderId=<%= orderInfo.id%>" class="subtab-active"><%= orderInfo.orderSn %></a></span>
            <span>开单日期：<%= orderInfo.createTimeStr %></span>
            <%if (orderInfo.orderTag == 1 || orderInfo.orderTag == 3) {%>
            <button class="copy-btn" order-id="<%= orderInfo.id%>" order-tag="<%= orderInfo.orderTag %>">复制</button>
            <%}%>
        </caption>
        <tr class="order-table-black">
            <td width="15%" rowspan="<%= rowspan %>" class="order-table-white"><%= orderTagList[orderInfo.orderTag-1] %></td>
            <td width="25%">服务名称</td>
            <td width="15%">工时费</td>
            <td width="15%">工时</td>
            <td width="15%">优惠</td>
            <td width="15%" rowspan="<%= rowspan %>" class="order-table-white"><p class="money-sum"><%if(orderInfo.orderStatus =='DDYFK'){%>应收金额：<%= orderInfo.payAmount%><%}else{%>总计：<%= orderInfo.orderAmount%><%}%></p><p><#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'><%= orderInfo.orderStatusName %>
            <#else><%= orderInfo.tqmallOrderStatusName %></#if></p></td>
        </tr>
        <%if (serviceList.length > 0) {%>
        <%for (var j = 0; j < serviceList.length; j++) {%>
        <%var serviceInfo = serviceList[j];%>
        <tr class="order-table-white">
            <td title class="js-show-tips"><%= serviceInfo.serviceName %></td>
            <td title class="js-show-tips"><%= serviceInfo.servicePrice %></td>
            <td><%= serviceInfo.serviceHour %></td>
            <td><%= serviceInfo.discount %></td>
        </tr>
        <%}%>
        <%} else {%>
        <tr class="order-table-white">
            <td>-</td>
            <td>-</td>
            <td>-</td>
            <td>-</td>
        </tr>
        <%}%>
        <tr class="order-table-black">
            <td>配件名称</td>
            <td>售价</td>
            <td>数量</td>
            <td>优惠</td>
        </tr>
        <%if (goodsList.length > 0) {%>
        <%for(var j = 0; j < goodsList.length; j++) {%>
        <%var goods = goodsList[j];%>
        <tr class="order-table-white">
            <td title class="js-show-tips"><%= goods.goodsName %></td>
            <td><%= goods.goodsPrice %></td>
            <td><%= goods.goodsNumber %></td>
            <td><%= goods.discount %></td>
        </tr>
        <%}%>
        <%} else {%>
        <tr class="order-table-white">
            <td>-</td>
            <td>-</td>
            <td>-</td>
            <td>-</td>
        </tr>
        <%}%>
    </table>
    <%}%>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="orderPage"></div>
    <!-- 分页容器 end -->
    <%}%>
</script>
<!-- 维修工单 end -->

<!-- 维修配件 start -->
<script type="text/html" id="goodsTpl">
    <table class="replacement">
        <thead>
        <tr>
            <th width="10%">工单编号</th>
            <th width="10%">开单日期</th>
            <th width="20%">零件号</th>
            <th width="20%">配件名称</th>
            <th width="10%">售价（元）</th>
            <th width="10%">数量</th>
            <th width="10%">金额（元）</th>
            <th width="10%">优惠（元）</th>
        </tr>
        </thead>
        <%if (json.data && json.data.content) {%>
        <%for (var i = 0; i < json.data.content.length; i++) {%>
        <%var goodsInfo = json.data.content[i];%>
        <tbody>
        <tr>
            <td><%= goodsInfo.orderSn %></td>
            <td><%= goodsInfo.orderCreateTimeStr %></td>
            <td title class="js-show-tips"><%= goodsInfo.goodsFormat %></td>
            <td title class="js-show-tips"><%= goodsInfo.goodsName %></td>
            <td><%= goodsInfo.goodsPrice %></td>
            <td><%= goodsInfo.goodsNumber %></td>
            <td><%= goodsInfo.goodsAmount %></td>
            <td><%= goodsInfo.discount %></td>
        </tr>
        </tbody>
        <%}%>
        <%}%>
    </table>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="goodsPage"></div>
    <!-- 分页容器 end -->
</script>
<!-- 维修配件 end -->

<!-- 预约信息 start -->
<script type="text/html" id="appointTpl">
    <%if (json.data && json.data.content) {%>
    <table class="replacement">
        <thead>
        <tr>
            <th width="20%">预约单号</th>
            <th width="20%">预约时间</th>
            <th width="15%">车主</th>
            <th width="15%">车主电话</th>
            <th width="30%">预约内容</th>
        </tr>
        </thead>
        <%for (var i = 0; i < json.data.content.length; i++) {%>
        <%var appoint = json.data.content[i];%>
        <tbody class="pointer js-appoint-detail" appoint-id="<%= appoint.id%>">
        <tr>
            <td><%= appoint.appointSn %></td>
            <td><%= appoint.appointTimeFormat %></td>
            <td><%= appoint.customerName %></td>
            <td><%= appoint.mobile %></td>
            <td><div class="js-show-tips ellipsis-2 text-c" title><%= appoint.appointContent %></div></td>
        </tr>
        </tbody>
        <%}%>
    </table>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="appointPage"></div>
    <!-- 分页容器 end -->
    <%}%>
</script>
<!-- 预约信息 end -->
<#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
<!-- 回访信息 start -->
<script type="text/html" id="feedbackTpl">
    <%if (json.data && json.data.content) {%>
    <table class="replacement">
        <thead>
        <tr>
            <th width="10%">回访方式</th>
            <th width="10%">回访人</th>
            <th width="15%">回访时间</th>
            <th width="15%">下次沟通时间</th>
            <th width="50%">回访记录</th>
        </tr>
        </thead>
        <%for (var i = 0; i < json.data.content.length; i++){%>
        <%var feedback = json.data.content[i];%>
        <tbody>
        <tr>
            <td><%= feedback.visitMethod%></td>
            <td><%= feedback.visitorName%></td>
            <td><%= feedback.visitTimeFormat%></td>
            <td><%= feedback.nextVisitTimeStr%></td>
            <td title="<%= feedback.customerFeedback%>"><%if(feedback.customerFeedback && feedback.customerFeedback.length>30){%>
                <%= feedback.customerFeedback.substring(0,30)%>...
                <%}else{%>
                <%= feedback.customerFeedback%>
                <%}%>
            </td>
        </tr>
        </tbody>
        <%}%>
    </table>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="feedbackPage"></div>
    <!-- 分页容器 end -->
    <%}%>
</script>
<!-- 回访信息 end -->
</#if>

<!-- 车况信息 start -->
<script type="text/html" id="precheckTpl">
    <%if(json.data && json.data.content){%>
    <table class="replacement">
        <thead>
        <tr>
            <th width="15%">预检单号</th>
            <th width="15%">预检时间</th>
            <th width="25%">外观检测</th>
            <th width="25%">轮胎检测</th>
            <th width="20%">其它检测</th>
        </tr>
        </thead>
        <%for(var i=0;i<json.data.content.length;i++){%>
        <%var precheck = json.data.content[i];%>
        <%var proMap = precheck.detailsMap%>
        <tbody class="js-precheck-detail pointer" data-precheck-id="<%= precheck.precheckId%>">
        <tr>
            <td><%= precheck.precheckSn%></td>
            <td><%= precheck.gmtCreate%></td>
            <td class="text-left">
                <%var proList = proMap[9]%>
                <%for(var key in proList){%>
                <%var value = proList[key]%>
                <span<%if(value.redFlag !=0){%> class="current"<%}%>><%=value.precheckItemName%>-<%=value.precheckValue%></span></br>
                <%}%>
                <%var proList = proMap[1]%>
                <%for(var key in proList){%>
                <%var value = proList[key]%>
                <span<%if(value.redFlag !=0){%> class="current"<%}%>><%=value.precheckItemName%>-<%=value.precheckValue%></span></br>
                <%}%>
            </td>
            <td class="text-left"><%var proList = proMap[2]%>
                <%for(var key in proList){%>
                <%var value = proList[key]%>
                <span<%if(value.redFlag !=0){%> class="current"<%}%>><%=value.precheckItemName%>-<%=value.precheckValue%></span></br>
                <%}%>
            </td>
            <td class="text-left"><%var proList = proMap[3]%>
                <%for(var key in proList){%>
                <%var value = proList[key]%>
                <span<%if(value.redFlag !=0){%> class="current"<%}%>><%=value.precheckItemName%>-<%=value.precheckValue%></span></br>
                <%}%>
            </td>
        </tr>
        </tbody>
        <%}%>
    </table>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="precheckPage"></div>
    <!-- 分页容器 end -->
    <%}%>
</script>
<!-- 车况信息 end -->

<!-- 历史导入的工单信息 start -->
<script type="text/html" id="orderHistoryTpl">
    <%if(json.data && json.data.content){%>
    <div class="order-history-div">
        <table class="order-history-table">
            <thead>
            <tr>
                <th>序号</th>
                <th>开单日期</th>
                <th>*工单编号</th>
                <th>服务顾问</th>
                <th>工单类型</th>
                <th>工单状态</th>
                <th>*车牌</th>
                <th>车型</th>
                <th>行驶里程</th>
                <th>VIN码</th>
                <th>发动机号</th>
                <th>车主</th>
                <th>车主电话</th>
                <th>联系人</th>
                <th>联系电话</th>
                <th>服务项目</th>
                <th>配件项目</th>
                <th>服务费合计</th>
                <th>服务费优惠</th>
                <th>配件费合计</th>
                <th>配件费优惠</th>
                <th>总计</th>
                <th>优惠</th>
                <th>应收金额</th>
                <th>实收金额</th>
                <th>挂账金额</th>
                <th>开单人</th>
                <th>维修工</th>
                <th>结算人</th>
                <th>完工日期</th>
                <th>结算日期</th>
                <th>备注</th>
            </tr>
            </thead>
            <%for (var i = 0; i < json.data.content.length; i++) {%>
            <%var item = json.data.content[i];%>
            <tbody <%if(index%2==0){%> class="tb-bk" <%}%>>
            <tr>
                <td><%=json.data.size*(json.data.number)+i+1%></td>
                <td><%if(item.orderCreateTime){%><%= item.orderCreateTime.substring(0,10)%><%}%></td>
                <td><%=item.orderSn%></td>
                <td><%=item.receiver%></td>
                <td><%=item.orderType%></td>
                <td><%=item.orderStatus%></td>
                <td class="js-show-tips"><%=item.carLicense%></td>
                <td><%=item.carModel%></td>
                <td><%=item.mileage%></td>
                <td><%=item.vin%></td>
                <td><%=item.engineNo%></td>
                <td><%=item.customerName%></td>
                <td><%=item.customerMobile%></td>
                <td><%=item.contactName%></td>
                <td><%=item.contactMobile%></td>
                <td class="js-show-tips"><%=item.serviceName%></td>
                <td class="js-show-tips"><%=item.goodsName%></td>
                <td><%=item.serviceAmount%></td>
                <td><%=item.serviceDiscount%></td>
                <td><%=item.goodsAmount%></td>
                <td><%=item.goodsDiscount%></td>
                <td><%=item.payableAmount%></td>
                <td><%=item.discountAmount%></td>
                <td><%=item.actualPayableAmount%></td>
                <td><%=item.payAmount%></td>
                <td><%=item.signAmount%></td>
                <td><%=item.operatorName%></td>
                <td class="js-show-tips"><%=item.worker%></td>
                <td><%=item.payName%></td>
                <td><%if(item.finishTime){%><%= item.finishTime.substring(0,10)%><%}%></td>
                <td><%if(item.payTime){%><%= item.payTime.substring(0,10)%><%}%></td>
                <td class="js-show-tips"><%=item.remark%></td>
            </tr>
            </tbody>
            <%}%>
        </table>
    </div>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="orderHistoryPage"></div>
    <!-- 分页容器 end -->
    <%}%>
</script>
<!-- 历史导入的工单信息 end -->

<!--淘汽检测单-->
<script type="text/html" id="tqCheckTpl">
    <%if(json.data && json.data.content){%>
    <table class="replacement">
        <thead>
        <tr>
            <th width="10%">检测时间</th>
            <th width="10%">机油寿命</th>
            <th width="10%">电池健康状态</th>
            <th width="10%">轮胎花纹深度</th>
            <th width="10%">灯泡光强度</th>
            <th width="10%">雨刮状态</th>
            <th width="10%">PM2.5检测</th>
            <th width="10%">室内是否有异味</th>
        </tr>
        </thead>
        <%for(var i=0;i<json.data.content.length;i++){%>
        <%var checkLog = json.data.content[i];%>
        <%var detailList = checkLog.detailDTOList%>
        <tbody class="pointer">
        <tr>
            <td><%=checkLog.gmtCreateStr%></td>
            <%for (var j = 0 ; j < 7 ; j++){%>
            <%var tqDetail = detailList[j];%>
            <td>
                <%var tqItemList = tqDetail.tqCheckItemDTOList;%>
                <%for(var z = 0 ; z < tqItemList.length ; z++){%>
                <% var tqItem = tqItemList[z];%>
                <%= tqItem.itemValueTypeName%> <br/>
                <%}%>
            </td>
            <%}%>
        </tr>
        </tbody>
        <%}%>
    </table>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="tqCheckPage"></div>
    <!-- 分页容器 end -->
    <%}%>
</script>
<!---->

<!--退货记录-->
<script type="text/html" id="warehouseOutTpl">
    <%if(json.data && json.data.content){%>
    <table class="replacement">
        <thead>
        <tr>
            <th width="30%">配件信息</th>
            <th width="10%">退货数量</th>
            <th width="10%">退货时间</th>
            <th width="10%">销售价</th>
            <th width="10%">备注</th>
            <th width="15%">工单号</th>
            <th width="15%">退货单号</th>
        </tr>
        </thead>
        <%for(var i=0;i<json.data.content.length;i++){%>
        <%var warehouseOutRefundVo = json.data.content[i];%>
        <tbody>
        <tr>
            <td><%= warehouseOutRefundVo.goodsName%><br><%= warehouseOutRefundVo.goodsFormat%></td>
            <td><%= warehouseOutRefundVo.goodsCount%></td>
            <td><%= warehouseOutRefundVo.outTimeStr%></td>
            <td>￥<%= warehouseOutRefundVo.salePrice%></td>
            <td><%= warehouseOutRefundVo.remark%></td>
            <td><%if(warehouseOutRefundVo.orderSn){%><a href="${BASE_PATH}/shop/order/detail?refer=customer&orderId=<%= warehouseOutRefundVo.orderId%>" class="subtab-active"><%=warehouseOutRefundVo.orderSn%></a><%}else{%>-<%}%></td>
            <td class="js-out-detail" data-id="<%= warehouseOutRefundVo.warehouseOutId%>"><a href="javascript:void(0);" class="subtab-active"><%= warehouseOutRefundVo.warehouseOutSn%></a></td>
        </tr>
        </tbody>
        <%}%>
    </table>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="warehouseOutPage"></div>
    <!-- 分页容器 end -->
    <%}%>
</script>
<!---->

<!--图片弹窗 start -->
<script type="text/html" id="img-dialog">
    <div class="tank">
        <div class="btn-group">图片</div>
        <div class="img-box">
            <%if(json.data){%>
            <%for(var i=0;i<1;i++){%>
            <% var carFile = json.data[i]%>
            <div class="big-img">
                <ul>
                    <li><img src="<%= carFile.filePath%>"/></li>
                </ul>
            </div>
            <%}%>
            <div class="thumbnail-box">
                <div class="prev-btn js-prev-btn"><span class="icon-angle-up"></span></div>
                <div class="smallpic">
                    <ul>
                        <%for(var i=0;i<json.data.length;i++){%>
                        <% var carFile = json.data[i]%>
                        <li><img src="<%= carFile.filePath%>"/></li>
                        <%}%>
                    </ul>
                </div>
                <div class="next-btn js-next-btn"> <span class="icon-angle-down"></span></div>
            </div>
            <%}%>
        </div>
    </div>
</script>
<!-- 图片弹窗 end -->

<!-- 弹框 换手机号 start -->
<script id="changeMobileTpl" type="text/html">
    <div class="change_mobile" >
        <p class="dialog_title">
            更换车主电话
        </p>
        <ul>
            <li>
                <label class="form-label">请输入车主电话：<span class="must">*</span></label>
                <div class="form-item">
                    <input type="text" class="dg_input" placeholder="" id="new_mobile" data-v-type="required | phone" data-label="车主电话">
                </div>
            </li>
        </ul>
        <div class="btn_center text-center">
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-3 js-mobile-save">提交</a>
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-1 js-mobile-cancel">取消</a>
        </div>
    </div>
</script>
<!-- 弹框 换手机号 end -->


<!--客户优惠-->
<script type="text/html" id="discountInfoTpl">
    <%if(json.success && json.data){%>
    <%for(var i=0; i< json.data.length; i++){%>
    <%var item=json.data[i]%>
    <div class="customer-info">
        <input type="hidden" name="customerId" value="<%=item.customerId%>">
        客户：<span><%=item.customerName%>     <%=item.mobile%></span>
    </div>
    <div class="customer">
        <%if(item.memberCardDiscountList){%>
        <%for(var j=0; j< item.memberCardDiscountList.length; j++){%>
        <%var subItem = item.memberCardDiscountList[j]%>
        <div class="vip-card">
            <p class="card-name-width js-show-tips ellipsis-1"><%=subItem.typeName%>：<span class="vip-num"><%=subItem.cardNumber%></span></p>
            <p class="balance js-show-tips ellipsis-1">余额：<span class="money-font"><%=subItem.balance%>元</span></p>
        </div>
        <%}}%>
        <%if(item.sumComboCouponNum){%>
        <div class="card-ticket">卡券：<%=item.sumComboCouponNum%>张</div>
        <%}else{%>
        <div class="card-ticket">卡券：无</div>
        <%}%>
        <%if(item.comboCouponDiscountList){%>
        <%for(var j=0; j< item.comboCouponDiscountList.length; j++){%>
        <%var subItem = item.comboCouponDiscountList[j]%>
        <input type="hidden" value="<%=subItem.id%>" name="comboCouponId"/>
        <div class="card-ticket"><%=subItem.typeName%></div>
        <div class="card-ticket"><strong><%=subItem.name%>：</strong><strong><%=subItem.num%></strong><%if(subItem.typeName == '计次卡'){%>次<%}else{%>张<%}%> <p><%=subItem.expireDateStr%></p></div>
        <%}}%>
    </div>
    <%}}%>
</script>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/customer/car-detail.js?63e9e581ec878b024fc45e210d40adfd"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
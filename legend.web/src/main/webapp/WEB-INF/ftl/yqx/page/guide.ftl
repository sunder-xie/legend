<!--首页-->
<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/guide.css?6731a5641b7b48025983fc31b646a879"/>

<div class="yqx-wrapper clearfix">

    <div class="process hide">
        <div class="status">
            <div class="s-up ">
                <input type="hidden" id="isYBD" value="${YBD}">
                <div class="repair block js-count" data-desc="reception">
                    <span class="text title"></span>
                    <div class="link">
                        <div class="h86">
                            <div class="circle repairimg"></div>
                        </div>
                        <span class="status-name text">接车维修</span>
                    </div>
                </div>
                <div class="shortarrow block mb28">
                    <img src="${BASE_PATH}/static/img/page/guide/shortarrow.png">
                </div>
                <div class="order block js-count" data-desc="newOrder">
                    <span class="text title"></span>
                    <div class="link1">
                        <div class="h86">
                            <div class="circle orderimg"></div>
                        </div>
                        <span class="status-name text">维修开单</span>
                    </div>
                </div>
                <div class="longarrow block mb28">
                    <img src="${BASE_PATH}/static/img/page/guide/longarrow.png">
                </div>
                <div class="settlement block js-count" data-desc="settle">
                    <span class="text title"></span>
                    <div class="link1">
                        <div class="h86">
                            <div class="circle settleimg"></div>
                        </div>
                        <span class="status-name text">收款</span>
                    </div>
                </div>
                <div class="shortarrow block mb28">
                    <img src="${BASE_PATH}/static/img/page/guide/shortarrow.png">
                </div>
                <div class="return block js-count" data-desc="<#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>visit<#else>join</#if>">
                    <span class="text title"></span>
                    <div class="link1">
                        <div class="h86">
                            <div class="circle returnimg"></div>
                        </div>
                        <span class="status-name text">客户回访</span>
                    </div>
                </div>
            </div>
            <div class="s-down">
                <div class="downarrow">
                    <img src="${BASE_PATH}/static/img/page/guide/down.png">
                </div>
                <div class="purchase block js-count" data-desc="stockIn" data-version="${SESSION_CHOOSE_VERSION}">
                    <span class="text title"></span>
                    <div class="link1">
                        <div class="h86">
                            <div class="circle purchaseimg"></div>
                        </div>
                        <span class="status-name text">采购入库</span>
                    </div>
                </div>
                <div class="shortarrow block">
                    <img src="${BASE_PATH}/static/img/page/guide/shortarrow.png">
                </div>
                <div class="inventory block js-count" data-desc="goodsShortage" data-version="${SESSION_CHOOSE_VERSION}">
                    <span class="text title"></span>
                    <div class="link1">
                        <div class="h86">
                            <div class="circle inventoryimg"></div>
                        </div>
                        <span class="status-name text">库存查询</span>
                    </div>
                </div>
                <div class="shortarrow block">
                    <img src="${BASE_PATH}/static/img/page/guide/shortarrow.png">
                </div>
                <div class="warehouse block js-count"
                     data-desc="goodsOut"
                     data-version="${SESSION_CHOOSE_VERSION}">
                    <span class="text title"></span>
                    <div class="link1">
                        <div class="h86">
                            <div class="circle wareimg"></div>
                        </div>
                        <span class="status-name text">领料出库</span>
                    </div>
                </div>
                <div class="uparrow block">
                    <img src="${BASE_PATH}/static/img/page/guide/up.png">
                </div>
            </div>
            <i class="process-hide js-process"><i class="fa icon-angle-up"></i>收起</i>
        </div>
    </div>
    <div class="appointtable text">
        <div class="table-content">
        <div class="tablehead" style="height: 38px;border-bottom: 1px solid #ddd;">
            <div class="tab-item current-item js-tab"
                 data-desc="appoint">预约提醒<div class="num">${remindCountVo.appointNumberStr}</div></div>
            <div class="tab-item js-tab"
                 data-desc="orderlist">工单提醒<div class="num">${remindCountVo.orderNumberStr}</div></div>
            <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
            <div class="tab-item js-tab"
                 data-tag="maintain"
                 data-desc="maintain">保养提醒<div class="num">${remindCountVo.maintainNumberStr}</div></div>
            <div class="tab-item js-tab"
                 data-tag="insurance"
                 data-desc="insurance">保险提醒<div class="num">${remindCountVo.insuranceNumberStr}</div></div>
            <div class="tab-item js-tab"
                 data-tag="auditing"
                 data-desc="auditing">年检提醒<div class="num">${remindCountVo.auditingNumberStr}</div></div>
                <div class="tab-item js-tab" data-desc="stockWarning">库存预警<div class="num">${remindCountVo.stockwarningNumberStr}</div></div>
            </#if>
        </div>
        <div class="process-show hide js-process">
            <i class="fa icon-list-ul"></i>
            <i class="text">业务流程</i>
        </div>

        <div class="table-box auditing hide" data-desc="auditing">
            <table class="guide-table">
                <thead>
                <tr>
                    <th class="width-carlicense">车牌</th>
                    <th class="w148">年检到期时间</th>
                    <th class="width-customer">车主</th>
                    <th class="width-phone">车主电话</th>
                    <th class="w194">车型</th>
                    <th class="width-time">最近消费时间</th>
                    <th>提醒</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
            <div class="tablebottom">
                <a href="javascript:;" class="js-list-more hide" data-desc="auditing">点击查看更多<img src="${BASE_PATH}/static/img/page/guide/bluearrow.png"></a>
            </div>
        </div>
         
         <div class="table-box insurance hide" data-desc="insurance">
            <table class="guide-table">
                <thead>
                <tr>
                    <th class="width-carlicense">车牌</th>
                    <th class="w148">保险到期时间</th>
                    <th class="width-customer">车主</th>
                    <th class="width-phone">车主电话</th>
                    <th class="w194">车型</th>
                    <th class="width-time">最近消费时间</th>
                    <th>提醒</th>
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
            <div class="tablebottom">
                <a href="javascript:;" class="js-list-more hide" data-desc="insurance">点击查看更多<img src="${BASE_PATH}/static/img/page/guide/bluearrow.png"></a>
            </div>
        </div>
         
        <div class="table-box maintain hide" data-desc="maintain">
            <table class="guide-table">
                <thead>
                <tr>
                    <th class="width-carlicense">车牌</th>
                    <th class="w148">预计保养到期时间</th>
                    <th class="width-customer">车主</th>
                    <th class="width-phone">车主电话</th>
                    <th class="w194">车型</th>
                    <th class="width-time">上次进厂里程</th>
                    <th>提醒</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
            <div class="tablebottom">
                <a href="javascript:;" class="js-list-more hide" data-desc="maintain">点击查看更多<img src="${BASE_PATH}/static/img/page/guide/bluearrow.png"></a>
            </div>
        </div>
  
        <div class="table-box orderlist hide" data-desc="orderlist">
            <table class="guide-table">
                <thead>
                <tr>
                    <th class="width-carlicense">车牌</th>
                    <th class="w148">工单状态</th>
                    <th class="w148">车型</th>
                    <th class="width-customer">车主</th>
                    <th class="width-phone">车主电话</th>
                    <th class="width-time">开单时间</th>
                    <th class="th-right">总计</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
            <div class="tablebottom">
                <a href="javascript:;" class="js-list-more hide" data-desc="order">点击查看更多<img src="${BASE_PATH}/static/img/page/guide/bluearrow.png"></a>
            </div>
        </div>
        <div class="table-box appoint table-current" data-desc="appoint">
            <table class="guide-table"">
                <thead>
                <tr>
                    <th class="width-time">预约时间</th>
                    <th class="width-carlicense">车牌</th>
                    <th class="width-customer">车主</th>
                    <th class="width-phone">车主电话</th>
                    <th class="w194">预约内容</th>
                    <th>状态</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
            <div class="tablebottom">
                <a href="javascript:;" class="js-list-more hide" data-desc="appoint">点击查看更多<img src="${BASE_PATH}/static/img/page/guide/bluearrow.png"></a>
            </div>
        </div>
            <div class="table-box stockWarning hide stock-warning" data-desc="stockWarning">
                <table class="guide-table">
                    <thead>
                <tr>
                    <th class="width-time">配件名称</th>
                    <th class="width-carlicense">库存数量</th>
                    <th class="width-customer">预警库存</th>
                    <th class="width-price text-right">云修采购价</th>
                    <th class="width-332">适配车型</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody></tbody>
                </table>
                <div class="tablebottom">
                    <a href="javascript:;" class="js-list-more hide" data-desc="stockWarning">点击查看更多<img src="${BASE_PATH}/static/img/page/guide/bluearrow.png"></a>
                </div>
            </div>
            </div>
    <#--<div class="head-banner">
        <a href="${BASE_PATH}/second_hand_car/order" target="_blank">
            <i class="banner-del js-banner-del"></i>
        </a>
    </div>-->
    </div>
</div>
</div>
<div class="wrapper">
    <#include "common_template/public_notice_dialog.ftl">
</div>
<#include "yqx/layout/footer.ftl">
<script type="text/html" id="tableTpl">
<% if(arr.length) {%>
    <% for(var i in arr) {%>
    <tr class="js-tr"  data-id="<%=arr[i].id%>"
        data-customer-id="<%=arr[i].customerId%>"
        data-mobile="<%=arr[i].mobile%>"
        data-license="<%=arr[i].carLicense%>"
        data-car-model="<%=arr[i].carModel%>"
        data-customer-name="<%=arr[i].customerName%>"
        data-customer-car-id="<%=arr[i].customerCarId%>">
        <td class="first-td <%if(descs[0] === 'carLicense')%><%='carlicense'%>
        <%if(descs[0] === 'appointTimeFormat' && getFormatDate(arr[i][ descs[0] ]) === getFormatDate()) { %>
        <%='has-tip' %><%}%>"><div><%=arr[i][ descs[0] ]%>
        <%if(descs[0] === 'appointTimeFormat' && getFormatDate(arr[i][ descs[0] ]) === getFormatDate()) {%>
            <span class="today">今日到店</span>
            <%}%>
        </div>
        </td>
        <% var a = arr[i][ descs[1] ]; %>
        <% if(descs[1] == 'orderStatusName' && isTqmall()) {
            a = arr[i].tqmallOrderStatusName;
        }%>
        <td class="<%if(descs[1] === 'license')%><%='carlicense '%>
                   <%if(arr[i].orderStatus=='DDWC'){%><%='ywg'%><%}%>
                   <%if(arr[i].orderStatus=='CJDD'){%><%='dbj'%><%}%>
                   <%if(arr[i].orderStatus=='FPDD'||arr[i].orderStatus=='DDSG'){%><%='ypg'%><%}%>
                   <%if(arr[i].orderStatus=='DDBJ'){%><%='ybj'%><%}%>
                   <%if(arr[i].orderStatus=='DDYFK'&&arr[i].payStatus==1){%><%='ygz'%><%}%>">
            <div><%=a%></div>
        </td>
        <td>
            <div class="<%if(descs[2] == 'customerName')%><%='width-customer js-show-tips show-tips'%>">
                <%if(descs[2] === 'carInfo') {%><i class="carmodel js-show-tips"><%=arr[i][ descs[2] ]%></i><%} else {%><%=arr[i][ descs[2] ]%><%}%>
            </div>
        </td>
        <td><div><%=arr[i][ descs[3] ]%></div></td>
        <td><div><%if(descs[4] === 'carModel') {%><i class="carmodel js-show-tips"><%=arr[i][ descs[4] ]%></i>
            <%} else if(descs[4] === 'appointContent') {%>
            <i class="appoint-content js-show-tips"><%=arr[i][ descs[4] ]%></i>
            <% }else {%><%=arr[i][ descs[4] ]%><%}%></div></td>
        <td class="<%if(!descs[6]){%><%='last-td'%><%}%>
                   <%if(arr[i].status==1&&descs[5]=='statusName'){%><%='confirm'%><%}%>
                   <%if(arr[i].status==0&&descs[5]=='statusName'){%><%='notconfirm'%><%}%>">
            <div><%=arr[i][ descs[5] ]%><%if(descs[5] === 'mileage') {%><%=' km'%><%}%></div>
        </td>
        <% if(descs[6]) {%>
        <td class="orange last-td">
            <%if(descs[6] === 'orderAmount') {%>
            <div><%='￥' + (arr[i][ descs[6] ] || 0)%></div>
            <%} else if(descs[6] === 'quickVisit') {%>
            <div class="quick-visit"><a>立即回访</a></div>
            <%} else {%>
            <div><%=arr[i][ descs[6] ]%></div>
            <%}%>
        </td>
        <%}%>
    </tr>
    <%}%>
<%}%>
</script>

<!-- 库存预警表格模板 start -->
<script type="text/template" id="stockWarningTpl">
        <%for(var i=0;i < json.data.content.length; i++){%>
        <%
        var item = json.data.content[i];
        var modifyAddress = "/shop/goods/goods-toedit?goodsid=";

        var itemInventoryPrice =0 ;
        if(item.inventoryPrice!=null){
        itemInventoryPrice = item.inventoryPrice.toFixed(2)
        }
        var itemStock =0;
        if(item.stock !=null){
        itemStock = item.stock
        }
        var itemTotalAmount = (itemStock * itemInventoryPrice).toFixed(2)

        var itemLastprice = "";
        if(item.lastInPrice !=0 && item.lastInPrice != nul){
        itemLastprice =item.lastInPrice
        }
        %>
        <tr class="js-tr" id="TR_<%=item.id%>"  data-id="<%=item.id%>" data-tqmall-status="<%=item.tqmallStatus%>">
            <td class="first-td">
                <div class="max-width-name js-show-tips show-tips"><%=item.name%></div>
            </td>
            <td class="width-carlicense">
                <div><%=item.stock%> <%=item.measureUnit%></div>
            </td>
            <td class="width-customer">
                <div><%=item.shortageNumber%> <%=item.measureUnit%></div>
            </td>
            <td>
                <div class="text-right js-inventory-price width-price ">--</div>
            </td>
            <td>
                <div class="js-show-tips carmodel show-tips">
                <#-- 关联车型，显示4个车型，其它隐藏，鼠标移上显示全部。-->
                    <% var carInfoList = item.carInfoList;%>
                    <% if(carInfoList && carInfoList.length>0){ %>
                    <% var tempHtml="";%>
                    <% for(var j=0;j
                    <carInfoList.length
                            ;j++){%>
                        <% var carBrandName = "" ;
                        var carSeriesName = "";
                        var carAlias ="";
                        %>
                        <% if(carInfoList[j].carBrandName!=null){%>
                        <% carBrandName = carInfoList[j].carBrandName; %>
                        <% }%>
                        <% if(carInfoList[j].carSeriesName !=null){%>
                        <% carSeriesName = carInfoList[j].carSeriesName; %>
                        <% }%>
                        <% if(carInfoList[j].carAlias!="[]" && carInfoList[j].carAlias !=null){%>
                        <% carAlias = carInfoList[j].carAlias; %>
                        <% }%>

                        <% tempHtml += carBrandName + carSeriesName + carAlias; %>
                        <% if((j+1)
                        <carInfoList.length
                                ){%>
                            <% tempHtml += " | "%>
                            <% }%>

                            <% }%>
                    <span>
                    <% for(var j=0;j<carInfoList.length;j++){%>
                        <%= carInfoList[j].carBrandName;%> <%= carInfoList[j].carSeriesName;%> <%= carInfoList[j].carAlias;%>
                    <%}%>
                    </span>
                            <%}%>
                </div>
            </td>
            <td class="last-td">
                <div><a href="${BASE_PATH}/shop/warehouse/in/in-edit/blue?goodsIds=<%=item.id%>" class="blue link">采购单</a></div>
            </td>
        </tr>
        <%}%>
</script>
<!-- 表格模板 end -->
<script src="${BASE_PATH}/static/js/page/guide.js?bc5c0c886e4657cd2df9f4df92cbeb98"></script>

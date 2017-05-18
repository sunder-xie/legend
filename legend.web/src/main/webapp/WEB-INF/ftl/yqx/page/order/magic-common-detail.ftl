<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/common-detail.css?9983ad91ef9832ecf9fc5558e910ae3f"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <input type="hidden" id="orderId" value="${orderInfo.id}"/>
        <input type="hidden" id="orderSn" value="${orderInfo.orderSn}"/>
        <input type="hidden" id="orderStatus" value="${orderInfo.orderStatus}"/>
        <input type="hidden" id="proxyType" value="${orderInfo.proxyType}"/>

        <div class="order-head">
            <h3 class="headline fl">工单查询－
                <small>综合维修单详情</small>
            </h3>
        <#if orderInfo.id gt 0>
            <#if virtualOrderId>
                <a href="${BASE_PATH}/shop/order/virtualorder-edit?parentId=${orderInfo.id}" class="new-order fr"><i
                        class=""></i>查看子单</a>
            <#else>
                <a href="${BASE_PATH}/shop/order/virtualorder-edit?parentId=${orderInfo.id}" class="new-order fr"><i
                        class="fa icon-plus btn-plus"></i>新建子单</a>
            </#if>
        </#if>

        </div>
        <div class="order-content">
            <section class="car-info">
                <div class="detail-row">
                    <div class="info-td info-td-2">
                        <div class="form-label detail-title order-bold">工单编号:</div><div class="form-item"><div class="yqx-text detail-width order-bold">${orderInfo.orderSn}(<span>开单日期:<#if orderInfo.createTime>${orderInfo.createTime?string("yyyy-MM-dd HH:mm")}</#if>)</span></div></div>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">车牌：</div>
                        <div class="form-item">
                            <div class="yqx-text car-license">
                            ${orderInfo.carLicense}
                            </div>
                        <#if orderInfo.carLicense>
                            <a class="license-detail" target="_blank"
                               href="${BASE_PATH}/shop/customer/car-detail?refer=common-detail&id=${orderInfo.customerCarId}">更多车辆信息</a>
                        </#if>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">车型：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                                <div class="car-text-width js-show-tips">${orderInfo.carInfo}</div>
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">年款排量：</div>
                        <div class="form-item">
                            <div class="yqx-text car-text-width js-show-tips">
                            <#if orderInfo.carGearBox>
                            ${orderInfo.carYear} ${orderInfo.carGearBox}
                            <#else>
                            ${orderInfo.carYear} ${orderInfo.carPower}
                            </#if>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">联系人：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message js-show-tips">
                            ${orderInfo.contactName}
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">联系电话：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.contactMobile}
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">车主：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message js-show-tips">
                            ${orderInfo.customerName}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">车主电话：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.customerMobile}
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">行驶里程：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.mileage}km
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">服务顾问：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.receiverName}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">油表油量：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.oilMeter}
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">VIN码：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.vin}
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">颜色：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.carColor}
                            </div>
                    </div>
                </div>
                </div>
                <div class="detail-row">
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">下次保养里程：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.upkeepMileage}km
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">下次保养时间：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                                <#if customerCar.keepupTime>${customerCar.keepupTime?string("yyyy-MM-dd")}</#if>
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">承保公司：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message js-show-tips">
                            ${orderInfo.insuranceCompanyName}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">对方保险：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.otherInsuranceCompanyName}
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">期望交车日期：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.expectTimeStr}
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">保险到期：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.insuranceTimeStr}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="info-td info-td-3">
                        <#if orderInfo.channelName>
                            <div class="info-td info-td-3">
                                <div class="form-label detail-title">渠道：</div>
                                <div class="form-item">
                                    <div class="yqx-text detail-message">
                                    ${orderInfo.channelName}
                                    </div>
                                </div>
                            </div>
                        </#if>
                    </div>
                </div>

                <div class="detail-row">
                    <#if proxyList>
                    <div class="info-td info-td-6">
                        <div class="form-label detail-title">委托单：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message js-show-tips proxy-with">
                                <#list proxyList as proxy>
                                <a href="${BASE_PATH}/proxy/detail?proxyOrderId=${proxy.id}" target="_blank">${proxy.proxySn}</a>&nbsp;
                                </#list>
                            </div>
                        </div>
                    </div>
                    </#if>
                    <#if proxyedList>
                    <div class="info-td info-td-6">
                        <div class="form-label detail-title">受托单：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                                <#list proxyedList as proxy>
                                <a href="${BASE_PATH}/proxy/detail?proxyOrderId=${proxy.id}" target="_blank">${proxy.proxySn}</a>&nbsp;
                                </#list>
                            </div>
                        </div>
                    </div>
                    </#if>
                </div>

                <!--印章-->
            <#if orderInfo.orderStatus == "CJDD">
                <div class="seal"></div>
            <#elseif orderInfo.orderStatus == "DDBJ">
                <div class="seal quoted"></div>
            <#elseif orderInfo.orderStatus == "FPDD" || orderInfo.orderStatus == "DDSG">
                <div class="seal dispatching"></div>
            <#elseif orderInfo.orderStatus == "DDWC">
                <div class="seal finished"></div>
            <#elseif orderInfo.orderStatus == 'DDYFK' && orderInfo.payStatus == 1>
                <div class="seal sign"></div>
            <#elseif orderInfo.orderStatus == 'DDYFK' && orderInfo.payStatus == 2>
                <div class="seal settlement"></div>
            <#elseif orderInfo.orderStatus == "WXDD">
                <div class="seal invalid"></div>
            </#if>
                </div>

            </section>
            <section class="car-consume">
                <!--服务项目-->
                <#if basicOrderService>
                <table class="yqx-table table-service">
                    <caption>服务项目</caption>
                    <thead>
                    <tr>
                        <th>服务名称</th>
                        <th class="yqx-td-4">服务类别</th>
                        <th class="yqx-td-3 th-right">工时费</th>
                        <th class="yqx-td-3 th-right">工时</th>
                        <th class="yqx-td-4 th-right">金额</th>
                        <th class="yqx-td-4 th-right table-boundary">优惠</th>
                        <th>维修工</th>
                        <th>服务备注</th>
                    </tr>
                    </thead>
                    <tbody id="basicServiceRow">
                    <#list basicOrderService as orderService>
                    <tr class="form_item">
                        <input type="hidden" name="workerIds" value="${orderService.workerIds}">
                        <td class="js-show-tips ellipsis-1">${orderService.serviceName}</td>
                        <td>${orderService.serviceCatName}</td>
                        <td class="td-right">${orderService.servicePrice}</td>
                        <td class="td-right">${orderService.serviceHour}</td>
                        <td class="td-right">${orderService.serviceAmount}</td>
                        <td class="td-right table-boundary">${orderService.discount}</td>
                        <td class="js-show-tips">${orderService.workerNames}</td>
                        <td class="js-show-tips">${orderService.serviceNote}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
                </#if>
                <!--配件物料-->
                <#if orderGoodsList>
                <table class="yqx-table table-service">
                    <caption>配件物料</caption>
                    <thead>
                    <tr>
                        <th>零件号</th>
                        <th>配件名称</th>
                        <th class="yqx-td-4 th-right">售价</th>
                        <th class="yqx-td-3 th-right">数量</th>
                        <th class="yqx-td-4 th-right">金额</th>
                        <th class="yqx-td-4 th-right">优惠</th>
                        <th class="yqx-td-3 th-right table-boundary">库存</th>
                        <th class="yqx-td-3">销售员</th>
                        <th>配件备注</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list orderGoodsList as orderGoods>
                    <tr>
                        <td class="ellipsis-1 js-show-tips">${orderGoods.goodsFormat}</td>
                        <td class="ellipsis-1 js-show-tips">${orderGoods.goodsName}</td>
                        <td class="td-right">${orderGoods.goodsPrice}</td>
                        <td class="td-right">${orderGoods.goodsNumber} ${orderGoods.measureUnit}</td>
                        <td class="td-right js-show-tips">${orderGoods.goodsAmount}</td>
                        <td class="td-right">${orderGoods.discount}</td>
                        <td class="td-right table-boundary js-show-tips">${orderGoods.stock}</td>
                        <td>${orderGoods.saleName}</td>
                        <td class="js-show-tips">${orderGoods.goodsNote}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
                </#if>
                <!-- 附加项目-->
                <#if additionalServices>
                <table class="yqx-table table-service">
                    <caption>附加项目</caption>
                    <thead>
                    <tr>
                        <th>附加名称</th>
                        <th class="yqx-td-4 th-right">金额</th>
                        <th class="yqx-td-4 table-boundary th-right">优惠</th>
                        <th>附加备注</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list additionalServices as orderService>
                    <tr>
                        <td class="ellipsis-1 js-show-tips">${orderService.serviceName}</td>
                        <td class="td-right">${orderService.serviceAmount}</td>
                        <td class="td-right table-boundary">${orderService.discount}</td>
                        <td class="ellipsis-1 js-show-tips">${orderService.serviceNote}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
                </#if>
                <!-- 备注及按钮 -->
                <div>
                <#if orderInfo.postscript>
                    <div class="input-mark">
                        <div class="form-label">备注</div>
                        <div class="form-item">
                        <div class="ellipsis-2 js-show-tips">${orderInfo.postscript}</div>
                        </div>
                    </div>
                </#if>
                    <p class="pay-line<#if !orderInfo.postscript> padding-top20</#if>">
                        <span>服务费用:</span>
                        <i id="serverMoneySum"
                           class="money-sum">${(orderInfo.serviceAmount - orderInfo.serviceDiscount)?string(",###.##")}</i>
                        元 +
                        <span>配件费用:</span>
                        <i id="partsMoneySum"
                           class="money-sum">${(orderInfo.goodsAmount - orderInfo.goodsDiscount)?string(",###.##")}</i>
                        元 +
                        <span>附加费用:</span>
                        <i id="feeMoneySum"
                           class="money-sum">${(orderInfo.feeAmount - orderInfo.feeDiscount)?string(",###.##")}</i>

                    </p>

                <#if orderInfo.orderStatus =='DDYFK' >
                    <p class="pay-line pay-total" >
                        <span>总计:</span>
                        <i id="allMoneySum" class="money-sum">${orderInfo.orderAmount?string(",###.##")}</i>
                        元
                    </p>
                    <p class="pay-line pay-total">
                        <span>应收金额:</span>
                        <i id="allMoneySum" class="money-sum">${orderInfo.payAmount?string(",###.##")}</i>
                        元
                    </p>
                    <#if orderInfo && orderInfo.downPayment gt 0.00>
                        <p class="pay-line pay-total">
                            <span>预付定金:</span>
                            <i id="allMoneySum" class="money-sum">${orderInfo.downPayment?string(",###.##")}</i>
                            元
                        </p>
                    </#if>
                    <#if redBill>
                        <p class="pay-line pay-total">
                            <span>冲红应收金额:</span>
                            <i class="money-sum">${redBill.receivableAmount?string(",###.##")}</i>
                            元
                        </p>
                    </#if>
                    <p class="pay-line pay-total">
                        <span>挂账金额:</span>
                        <i class="money-sum">${orderInfo.signAmount?string(",###.##")}</i>
                        元
                    </p>
                    <#if redBill && redBill.signAmount < 0>
                        <p class="pay-line pay-total">
                            <span>冲红挂账金额:</span>
                            <i class="money-sum">${redBill.signAmount?string(",###.##")}</i>
                            元
                        </p>
                    </#if>
                    <#if debitBill.badAmount gt 0>
                        <p class="pay-line pay-total">
                            <span>坏账金额:</span>
                            <i class="money-sum">${debitBill.badAmount?string(",###.##")}</i>
                            元
                        </p>
                    </#if>
                    <#if redBill && redBill.badAmount < 0>
                        <p class="pay-line pay-total">
                            <span>冲红坏账金额:</span>
                            <i class="money-sum">${redBill.badAmount?string(",###.##")}</i>
                            元
                        </p>
                    </#if>
                <#else>
                    <p class="pay-line pay-total">
                        <span>总计:</span>
                        <i id="allMoneySum" class="money-sum">${orderInfo.orderAmount?string(",###.##")}</i>
                        元
                        <#if orderInfo && orderInfo.downPayment gt 0.00>
                        <span class="pre-payment">
                            <span>预付定金: </span><i class="money-sum">${orderInfo.downPayment?string(",###.##")}</i>元
                        </span>
                        </#if>
                    </p>
                </#if>

                    <div class="form-last-btns clearfix"
                         style="padding-top: 10px; border-top: 1px solid rgb(221, 221, 221);">
                    <#if orderInfo.orderStatus=='CJDD' || orderInfo.orderStatus == "DDBJ">
                        <#if SESSION_SHOP_WORKSHOP_STATUS == 1>
                        <button class="yqx-btn yqx-btn-2 js-magic-common-paigongBtn">派工</button>
                        <#else>
                        <button class="yqx-btn yqx-btn-2 js-common-paigongBtn">派工</button>
                        </#if>
                    </#if>

                    <#if  (orderInfo.orderStatus =='FPDD' || orderInfo.orderStatus == "DDSG" || orderInfo.orderStatus == "CJDD" || orderInfo.orderStatus == "DDBJ")>
                        <button class="yqx-btn <#if orderInfo.orderStatus == "CJDD" || orderInfo.orderStatus == "DDBJ">yqx-btn-1<#else>yqx-btn-2</#if> js-common-wangongBtn" data-workshop="${SESSION_SHOP_WORKSHOP_STATUS}">完工</button>
                    </#if>

                    <#if orderInfo.orderStatus !='WXDD'>
                        <button class="yqx-btn <#if orderInfo.orderStatus =='DDWC' || orderInfo.orderStatus == 'DDYFK'>yqx-btn-2<#else>yqx-btn-1</#if> js-magic-print">打印</button>
                    </#if>
                        <#--参加体系，且工单不是委托单转的工单才能委托-->
                    <#if SESSION_SHOP_JOIN_STATUS ==1 && orderInfo.proxyType != 2 && (orderInfo.orderStatus =='FPDD' || orderInfo.orderStatus == "DDSG" || orderInfo.orderStatus == "CJDD" || orderInfo.orderStatus == "DDBJ")>
                        <button class="yqx-btn yqx-btn-2 js-create-proxy">委托</button>
                    </#if>
                    <#if orderInfo.orderStatus =='WXDD'>
                        <button class="yqx-btn yqx-btn-2 js-common-delete display-none">删除</button>
                    </#if>

                        <div class="fr">
                        <#if orderInfo.orderStatus =="DDWC">
                            <button class="yqx-btn yqx-btn-1 js-common-rePaiGong">重新派工</button>
                        </#if>
                        <#if orderInfo.orderStatus !="DDWC" && orderInfo.orderStatus !='DDYFK' && orderInfo.orderStatus !='WXDD'>
                            <button class="yqx-btn yqx-btn-1 js-common-edit">编辑</button>
                        </#if>
                            <button class="yqx-btn yqx-btn-1 js-common-copy">复制</button>
                        <#if orderInfo.id gt 0 && orderInfo.orderStatus != 'WXDD'>
                            <button class="yqx-btn yqx-btn-1 js-common-invalid display-none">无效</button>
                        </#if>

                            <button class="yqx-btn yqx-btn-1 js-return">返回</button>
                        </div>
                    </div>
                </div>
                <!-- 备注及按钮  end -->

            </section>
        </div>
    </div>
    <!-- 右侧内容区 end -->
</div>


<script id="moreWarehouseTpl" type="text/html">
    <div class="service-head">
        <h2>配件出库列表</h2>
    </div>
    <div class="mwhbox">
        <table class="mwtab">
            <tr>
                <th>序号</th>
                <th>配件名称</th>
                <th>单价</th>
                <th>出库单价</th>
                <th>数量</th>
                <th>领料数量</th>
                <th>总价</th>
                <th>出库金额</th>
                <th>库存数量</th>
                <th>成本金额</th>
            </tr>
            <%if(result.success == true){%>
            <% if(result.data.orderGoodsList != null && result.data.orderGoodsList.length>0){%>
            <% var orderGoodsList = result.data.orderGoodsList;%>
            <% var listSize= result.data.orderGoodsList.length;%>
            <% for(var i=0;i
            <listSize ;i++ ){%>
                <% var item = orderGoodsList[i]; %>
                <tr>
                    <td><%=i+1%></td>
                    <td class="ellipsis-1 js-show-tips"><%=item.goodsName%></td>
                    <td><%=item.goodsPrice%>元/<%=item.measureUnit%></td>
                    <td
                    <%if(item.goodsPrice!=item.warehouseOutPrice){%> class="red"
                    <%}%>><%=item.warehouseOutPrice%>元/<%=item.measureUnit%></td>
                    <td><%=item.goodsNumber%><%=item.measureUnit%></td>
                    <td
                    <%if(item.outNumber!=item.goodsNumber){%> class="red" <%}%>>
                    <%=item.outNumber%></td>
                    <td><%=item.goodsAmount%>元</td>
                    <td
                    <%if(item.warehouseOutAmount!=item.goodsAmount){%> class="red" <%}%>>
                    <%=item.warehouseOutAmount%>元</td>
                    <td><%=item.stock%></td>
                    <td><%=item.warehouseInventoryAmount%>元</td>
                </tr>
                <%}%>
                <%}%>

                <tr>
                    <td colspan="6"><p class="mhyic" style="color: red;font-size: medium;padding-top:12px;">
                        <% if(result.data.code==false){%>您的配件出库有异常，无法完工！<%}%>
                    </p>
                    </td>
                    <td>合计<%=result.data.totalGoodsAmount%>元</td>
                    <td
                    <%if(result.data.totalGoodsAmount!=result.data.totalWarehouseOutAmount){%> class="red" <%}%>
                    >
                    合计<%=result.data.totalWarehouseOutAmount%>元</td>
                    <td></td>
                    <td>合计<%=result.data.totalWarehouseInventoryAmount%>元</td>
                </tr>
                <%}%>
        </table>
    </div>
</script>

<!-- 打印dialog导入 -->
<#include "yqx/tpl/order/order-print-tpl.ftl">

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/order/common-detail.js?91eff2f4c87e1f72f4a046c8571623ad"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
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
                    <div class="info-td info-td-2">
                    <#if debitBill>
                        <div class="form-label form-label-width">
                            账单编号：
                        </div>
                        <div class="form-item">
                            <div class="yqx-text">
                            ${debitBill.billSn}
                                (<span>账单日期：<#if debitBill.billTime>${debitBill.billTime?string("yyyy-MM-dd HH:mm")}</#if>
                                )</span>
                            </div>
                        </div>
                    </#if>
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
                        <div class="form-label detail-title">下次保养里程：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.upkeepMileage}km
                            </div>
                        </div>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">下次保养时间：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            <#if customerCar.keepupTime>${customerCar.keepupTime?string("yyyy-MM-dd")}</#if>
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">服务顾问：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.receiverName}
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">预计完工日期：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.expectedTimeYMD}
                            </div>
                        </div>
                    </div>
                </div>

                <div class="detail-row">
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">业务类型：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.orderTypeName}
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">油表油量：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.oilMeter}
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
                        <div class="form-label detail-title">VIN码：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.vin}
                            </div>
                        </div>
                    </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">客户单位：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message js-show-tips ellipsis-1">
                            ${customer.company}
                            </div>
                        </div>
                    </div>
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
                            <span>预付定金: </span><i class="money-sum">${orderInfo.downPayment?string(",###.##")}</i>元
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
                        <button class="yqx-btn yqx-btn-2 js-common-paigongBtn">派工</button>
                    </#if>

                    <#if (orderInfo.orderStatus =='FPDD' || orderInfo.orderStatus == "DDSG" || orderInfo.orderStatus == "CJDD" || orderInfo.orderStatus == "DDBJ")>
                        <button class="yqx-btn <#if orderInfo.orderStatus == "CJDD" || orderInfo.orderStatus == "DDBJ">yqx-btn-1<#else>yqx-btn-2</#if> js-common-wangongBtn">完工</button>
                    </#if>

                    <#if orderInfo.orderStatus !='WXDD'>
                        <button class="yqx-btn
                            old-print-btn hide
                            yqx-btn-1
                             js-print">
                            打印
                        </button>
                        <div class="old-print-tips hide">
                            <p>新版打印可自定义模板，打印速度更快而且还支持小票打印哦</p>
                            <p class="money-font">
                                <a href="${BASE_PATH}/shop/print-config"><i class="underline">马上切换 >>></i></a>
                            </p>
                        </div>
                        <#list openPrintConfig as item>
                            <#assign url = "">
                            <#assign name = "">
                            <#if item.printTemplate == 1>
                                <#assign name="派工单">
                                <#assign url = "/shop/order/shop-order-print">
                            </#if>
                            <#if item.printTemplate == 2 && orderInfo.orderStatus =='DDYFK'>
                                <#assign name="结算单">
                                <#assign url = "/shop/settlement/shop-settle-print">
                            </#if>
                            <#if item.printTemplate == 3  && orderInfo.orderStatus !='CJDD'>
                                <#assign name="报价单">
                                <#assign url = "/shop/warehouse/out/shop_pricing_print">
                            </#if>
                            <#if item.printTemplate == 4>
                                <#assign name="试车单">
                                <#assign url = "/shop/order/trialrun-print">
                            </#if>
                            <#if name != ''>
                            <button
                                    data-href="${BASE_PATH}${url}"
                                    class="yqx-btn new-print-btn hide yqx-btn-1
                                     js-print-order">${name}打印</button>
                            </#if>
                        </#list>
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
                    <#if orderInfo.orderStatus !='WXDD'>
                        <div class="new-print-tips hide">
                            <p>不满意打印效果 <i class="money-font"><a href="${BASE_PATH}/shop/print-config"><i class="underline">马上设置</i> >>></a></i></p>
                        </div>
                    </#if>
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
                        <% if(result.data.code==false){%>您的配件出库异常，请检查(1)单价和出库单价是否一致(2)数量和领料数量是否一致。<br/>如果不一致，请到【仓库-出库管理-工单出库】选择该工单领料出库<%}%>
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
<script  src="${BASE_PATH}/static/js/common/print/printVersionChange.js?5da782cabc728d23a9b68bb38cd33fb4"></script>
<script src="${BASE_PATH}/static/js/page/order/common-detail.js?91eff2f4c87e1f72f4a046c8571623ad"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/speedily-detail.css?fe3c202b00fe8ec7b9f068eb36f7c722"/>
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
                <small><#if orderInfo.orderTag==3>快修快保单<#else>引流活动</#if>详情</small>
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
                        <div class="form-label detail-title order-bold">工单编号:</div>
                        <div class="form-item">
                            <div class="yqx-text detail-width order-bold">
                            ${orderInfo.orderSn}
                                (<span>开单日期：<#if orderInfo.createTime>${orderInfo.createTime?string("yyyy-MM-dd HH:mm")}</#if>)</span>
                            </div>
                        </div>
                    </div>
                    <div class="info-td info-td-2">
                    <#if debitBill>
                        <div class="form-label form-label-width">
                            账单编号：
                        </div>
                        <div class="form-item">
                            <div class="yqx-text">
                            ${debitBill.billSn}
                                (<span>账单日期：<#if debitBill.billTime>${debitBill.billTime?string("yyyy-MM-dd HH:mm")}</#if>)</span>
                            </div>
                        </div>
                    </#if>
                    </div>
                </div>

                <div class="detail-row">
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">车牌：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message car-license">
                            ${orderInfo.carLicense}
                            </div>
                            <#if orderInfo.carLicense>
                                <a class="license-detail" target="_blank"
                                   href="${BASE_PATH}/shop/customer/car-detail?refer=speedily-detail&id=${orderInfo.customerCarId}">更多车辆信息</a>
                            </#if>

                        </div>
                    </div>
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">车型：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                                <div class="car-text-width js-show-tips">${orderInfo.carInfo}</div>
                            </div>
                        </div>
                    </div>
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">年款排量：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message js-show-tips ellipsis-1">
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
                            <div class="yqx-text detail-message">
                            ${orderInfo.contactName}
                            </div>
                        </div>
                    </div>
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">联系电话：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.contactMobile}
                            </div>
                        </div>
                    </div>
                    <div class="info-td info-td-3">
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
                        <div class="form-label detail-title">行驶里程：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.mileage}km
                            </div>
                        </div>
                    </div>
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">下次保养里程：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.upkeepMileage}km
                            </div>
                        </div>
                    </div>
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">下次保养时间：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            <#if customerCar.keepupTime>${customerCar.keepupTime?string("yyyy-MM-dd")}</#if>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="detail-row">
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">VIN码：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message">
                            ${orderInfo.vin}
                            </div>
                        </div>
                    </div>
                    <div class="info-td info-td-3">
                        <div class="form-label detail-title">客户单位：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message-width js-show-tips">
                            ${customer.company}
                            </div>
                        </div>
                    </div>
                </div>

                <!--印章-->
            <#if orderInfo.orderStatus == "CJDD">
                <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                    <div class="seal"></div>
                <#else>
                    <!-- 档口版为待结算-->
                    <div class="seal no-settlement"></div>
                </#if>
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
                            <td class="js-show-tips">${orderService.serviceName}</td>
                            <td>${orderService.serviceCatName}</td>
                            <td class="td-right"><#if orderService.servicePrice>${(orderService.servicePrice)?string("0.00")}<#else>
                                0.00</#if></td>
                            <td class="td-right"><#if orderService.serviceHour>${orderService.serviceHour}<#else>
                                0</#if></td>
                            <td class="td-right js-show-tips"><#if orderService.serviceAmount>${(orderService.serviceAmount)?string("0.00")}<#else>
                                0.00</#if></td>
                            <td class="td-right table-boundary"><#if orderService.discount>${(orderService.discount)?string("0.00")}<#else>
                                0.00</#if></td>
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
                    <caption>配件项目</caption>
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
                        <tr class="goods-datatr" data-id="${orderGoods.goodsId}" data-format="${orderGoods.goodsFormat}" data-name="${orderGoods.goodsName}" data-number="${orderGoods.goodsNumber}" data-stock="${orderGoods.stock}">
                            <td class="js-show-tips">${orderGoods.goodsFormat}</td>
                            <td class="js-show-tips">${orderGoods.goodsName}</td>
                            <td class="td-right"><#if orderGoods.goodsPrice>${(orderGoods.goodsPrice)?string("0.00")}<#else>
                                0.00</#if></td>
                            <td class="td-right js-show-tips"><#if orderGoods.goodsNumber>${orderGoods.goodsNumber}<#else>
                                0</#if> ${orderGoods.measureUnit}</td>
                            <td class="td-right"><#if orderGoods.goodsAmount>${(orderGoods.goodsAmount)?string("0.00")}<#else>
                                0.00</#if></td>
                            <td class="td-right js-show-tips"><#if orderGoods.discount>${(orderGoods.discount)?string("0.00")}<#else>
                                0.00</#if></td>
                            <td class="td-right table-boundary js-show-tips">${orderGoods.stock}</td>
                            <td class="js-show-tips">${orderGoods.saleName}</td>
                            <td class="js-show-tips">${orderGoods.goodsNote}</td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
            </#if>

                <!-- 备注及按钮 -->
                <div class="box">
                <#if orderInfo.postscript>
                    <div class="input-mark">
                        <div class="form-label">备注</div>
                        <div class="form-item">
                        <div class="ellipsis-2 js-show-tips">${orderInfo.postscript}</div>
                        </div>
                    </div>
                </#if>
                    <p class="pay-line padding-top20">
                        <span>服务费用:</span>
                        <i id="serverMoneySum"
                           class="money-sum"><#if (orderInfo && orderInfo.serviceAmount && orderInfo.serviceDiscount)>${(orderInfo.serviceAmount - orderInfo.serviceDiscount)?string("0.00")}<#else>
                            0.00</#if></i>
                        元 +
                        <span>配件费用:</span>
                        <i id="partsMoneySum"
                           class="money-sum"><#if (orderInfo && orderInfo.goodsAmount && orderInfo.goodsDiscount)>${(orderInfo.goodsAmount - orderInfo.goodsDiscount)?string("0.00")}<#else>
                            0.00</#if></i>
                        元
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
                        <p class="pay-line pay-total"
                           style="padding-bottom: 10px; border-bottom: 1px solid rgb(221, 221, 221);">
                            <span>挂账金额:</span>
                            <i id="allMoneySum" class="money-sum">${orderInfo.signAmount?string(",###.##")}</i>
                            元
                        </p>
                    <#else>
                    <p class="pay-line pay-total" style="padding-bottom: 10px; border-bottom: 1px solid rgb(221, 221, 221);">
                        <span>总计:</span>
                        <i id="allMoneySum"
                           class="money-sum"><#if (orderInfo && orderInfo.orderAmount)>${orderInfo.orderAmount?string("0.00")}<#else>
                            0.00</#if></i>
                        元
                        <#if orderInfo && orderInfo.downPayment gt 0.00>
                            <span class="pre-payment">
                            <span>预付定金: </span><i class="money-sum">#{orderInfo.downPayment;m2M2} </i>元
                            </span>
                        </#if>
                    </p>
                    </#if>

                    <div class="form-last-btns clearfix">
                        <div class="form-last-btns-left">
                        <#if orderInfo.orderStatus=='CJDD'>
                            <button class="yqx-btn yqx-btn-2 js-speedily-settle">收款</button>
                        </#if>
                        <#if orderInfo.orderStatus=='DDBJ' || orderInfo.orderStatus=='FPDD' || orderInfo.orderStatus=='DDSG'>
                            <button class="yqx-btn yqx-btn-2 js-speedily-finish">完工</button>
                        </#if>
                        <#if orderInfo.orderStatus !='WXDD'>
                            <button class="yqx-btn
                            old-print-btn hide yqx-btn-1
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
                                    <#if item.printTemplate == 2 && orderInfo.orderStatus =='DDYFK'>
                                        <button
                                                data-href="${BASE_PATH}/shop/settlement/shop-settle-print"
                                                class="new-print-btn hide yqx-btn yqx-btn-1 js-print-new">结算单打印</button>
                                    </#if>
                                    <#if item.printTemplate == 3 && orderInfo.orderStatus !='DDYFK'>
                                        <button
                                                data-href="${BASE_PATH}/shop/warehouse/out/shop_pricing_print"
                                                class="new-print-btn hide yqx-btn yqx-btn-1 js-print-new">报价单打印</button>
                                    </#if>
                                    <#if item.printTemplate == 5 && orderInfo.orderStatus =='DDYFK'>
                                        <button
                                                data-target="receipt"
                                                data-href="${BASE_PATH}/shop/settlement/shop-receipt-print"
                                                class="new-print-btn hide yqx-btn yqx-btn-1 js-print-new ">小票打印</button>
                                    </#if>
                                </#list>
                        </#if>
                        <#if orderInfo.orderStatus =='WXDD'>
                            <button class="yqx-btn yqx-btn-2 js-speedily-delete display-none">删除</button>
                        </#if>
                        </div>

                        <div class="form-last-btns-right">
                        <#if orderInfo.orderStatus=='CJDD' || orderInfo.orderStatus=='DDBJ' || orderInfo.orderStatus=='FPDD' || orderInfo.orderStatus=='DDSG'>
                            <button class="yqx-btn yqx-btn-1 js-speedily-edit">编辑</button>
                        </#if>

                        <button class="yqx-btn yqx-btn-1 js-speedily-copy">复制</button>

                        <#if orderInfo.orderStatus !='WXDD'>
                            <button class="yqx-btn yqx-btn-1 js-speedily-invalid display-none">无效</button>
                        </#if>

                            <button class="yqx-btn yqx-btn-1 js-speedily-back">返回</button>
                        </div>
                    <#if orderInfo.orderStatus !='WXDD'>
                        <div class="new-print-tips hide">
                            <p>不满意打印效果 <i class="money-font"><a href="${BASE_PATH}/shop/print-config">马上设置</a> >>></i></p>
                        </div>
                    </#if>
                    </div>
                </div>
                <!-- 备注及按钮  end -->
                <input type="hidden" class="js-in-warehouse">

            </section>
        </div>
    </div>
    <!-- 右侧内容区 end -->
</div>

<!-- 转入库模板 -->
<#include "yqx/tpl/order/in-warehouse-tpl.ftl">
<!-- 添加配件 -->
<#include "yqx/tpl/common/goods-add-tpl.ftl">
<#--小票打印教程-->
<#include "yqx/tpl/print/receipt-guide-tpl.ftl">
<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/common/print/printVersionChange.js?5da782cabc728d23a9b68bb38cd33fb4"></script>
<script src="${BASE_PATH}/static/js/page/order/speedily-detail.js?24be0fcf44dfeb16125a16ad5a718cb5"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
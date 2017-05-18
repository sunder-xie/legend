<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/sell-good-detail.css?f9a8fbb3da44760af6411cd00421c41a"/>
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
            <h3 class="headline">工单查询－<small>销售单详情</small></h3>
        </div>
        <div class="order-content">
            <section class="car-info">
                <div class="detail-row">
                    <div class="info-td info-td-2">
                        <div class="form-label detail-title order-bold">工单编号：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-width js-show-tips">
                            ${orderInfo.orderSn}
                                <#if orderInfo.createTime>
                                    (<span class="order-bold">开单日期:${orderInfo.createTime?string("yyyy-MM-dd HH:mm")}</span>)
                                </#if>
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
                                   href="${BASE_PATH}/shop/customer/car-detail?refer=common-detail&id=${orderInfo.customerCarId}">更多车辆信息</a>
                            </#if>
                            </div>
                        </div><div class="info-td info-td-3">
                        <div class="form-label detail-title">车型：</div>
                        <div class="form-item">
                            <div class="yqx-text">
                                <div class="car-text-width js-show-tips">${orderInfo.carInfo}</div>
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
                            <div class="form-label detail-title">联系人：</div>
                            <div class="form-item">
                                <div class="yqx-text detail-message">
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
                        <div class="form-label detail-title">客户单位：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-message js-show-tips">
                            ${customer.company}
                            </div>
                        </div>
                    </div>
                    </div>

                    <div class="detail-row">
                        <div class="info-td info-td-9">
                            <div class="form-label detail-title">备注：</div>
                            <div class="form-item">
                                <div class="yqx-text detail-message-width js-show-tips">
                                ${orderInfo.postscript}
                                </div>
                            </div>
                        </div>
                    </div>
            </section>

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
            <section class="car-consume">
                <!--配件物料-->
                <table class="yqx-table table-service">
                    <caption>配件物料</caption>
                    <thead>
                    <tr>
                        <th>零件号</th>
                        <th>配件名称</th>
                        <th class="th-right">售价</th>
                        <th class="th-right">数量</th>
                        <th class="th-right">金额</th>
                        <th class="th-right">优惠</th>
                        <th class="th-right table-boundary">库存</th>
                        <th>销售员</th>
                        <th>配件备注</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list orderGoodsList as orderGoods>
                    <tr class="goods-datatr" data-id="${orderGoods.goodsId}" data-format="${orderGoods.goodsFormat}" data-name="${orderGoods.goodsName}" data-number="${orderGoods.goodsNumber}" data-stock="${orderGoods.stock}">
                        <td class="js-show-tips">${orderGoods.goodsFormat}</td>
                        <td class="js-show-tips">${orderGoods.goodsName}</td>
                        <td class="td-right">${orderGoods.goodsPrice}</td>
                        <td class="td-right">${orderGoods.goodsNumber} ${orderGoods.measureUnit}</td>
                        <td class="td-right js-show-tips">${orderGoods.goodsAmount}</td>
                        <td class="td-right">${orderGoods.discount}</td>
                        <td class="td-right table-boundary js-show-tips">${orderGoods.stock}</td>
                        <td class="js-show-tips">${orderGoods.saleName}</td>
                        <td class="js-show-tips">${orderGoods.goodsNote}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>

                <!-- 备注及按钮 -->
                <div style="padding-top: 28px   ">
                   <p class="pay-line">
                        <span>配件费用:</span>
                        <i id="partsMoneySum" class="money-sum">${(orderInfo.goodsAmount - orderInfo.goodsDiscount)?string(",###.##")}</i>
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
                        <p class="pay-line pay-total"
                           style="padding-bottom: 10px; border-bottom: 1px solid rgb(221, 221, 221);">
                            <span>挂账金额:</span>
                            <i id="allMoneySum" class="money-sum">${orderInfo.signAmount?string(",###.##")}</i>
                            元
                        </p>
                <#else>
                    <p class="pay-line pay-total"  style="padding-bottom: 10px; border-bottom: 1px solid rgb(221, 221, 221);">
                        <span>总计:</span>
                        <i id="allMoneySum" class="money-sum">${orderInfo.orderAmount?string(",###.##")}</i>
                        元
                    </p>
                    </#if>

                    <div class="form-last-btns clearfix">
                        <#if orderInfo == null || orderInfo.orderStatus=='CJDD'>
                            <button class="yqx-btn yqx-btn-2 js-settle">收款</button>
                        </#if>
                        <#if orderInfo.orderStatus !='WXDD'>
                            <button class="yqx-btn yqx-btn-1 js-print"
                                <#if orderInfo.orderStatus == 'DDYFK' && shopPrintVersion == 'new'>
                                    data-href="/shop/settlement/print/sale-print"
                                <#elseif orderInfo.orderStatus == 'DDYFK' && shopPrintVersion == 'old'>
                                    data-href="/shop/settlement/simple-settle-print"
                                <#else>
                                    data-href="/shop/order/sell-good-print"
                                </#if>
                            >打印</button>
                        </#if>
                        <#if orderInfo.orderStatus =='WXDD'>
                            <button class="yqx-btn yqx-btn-2 js-delete display-none">删除</button>
                        </#if>

                        <div class="fr">
                        <#if orderInfo == null || orderInfo.orderStatus=='CJDD'>
                            <button class="yqx-btn yqx-btn-1 js-edit">编辑</button>
                        </#if>
                        <button class="yqx-btn yqx-btn-1 js-copy">复制</button>

                        <#if orderInfo.id gt 0 && orderInfo.orderStatus != 'WXDD'>
                            <button class="yqx-btn yqx-btn-1 js-invalid display-none">无效</button>
                        </#if>
                        <button class="yqx-btn yqx-btn-1 js-return">返回</button>
                        </div>
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
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/order/sell-good-detail.js?88b29ee0a29fa025d4dfb19d2ae178e7"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
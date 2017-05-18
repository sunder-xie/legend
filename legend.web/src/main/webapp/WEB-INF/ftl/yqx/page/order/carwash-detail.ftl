<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/carwash-detail.css?4dfcd5cd7bbe87d0f2ae49c3ad307db6"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <input type="hidden" id="orderId" value="${formEntity.orderId}"/>
        <input type="hidden" id="orderSn" value="${formEntity.orderSn}"/>
        <input type="hidden" id="orderStatus" value="${formEntity.orderStatus}"/>
        <div class="order-head">
            <h3 class="headline">工单查询－
                <small>洗车单详情</small>
            </h3>
        </div>
        <div class="order-content">
            <div class="car-info">
                <ul class="info-list">
                <#--工单编号-->
                    <li class="num">
                        <div class="info-list-2">
                            <div class="form-label order-bold">工单编号:</div><div class="form-item">
                            <div class="yqx-text">
                                <span class="order-bold">${formEntity.orderSn}</span>
                                <div class="form-label order-bold">(开单日期：</div>
                                <div class="form-item">
                                    <div class="yqx-text order-bold">${formEntity.createTimeStr})</div>
                                </div>
                            </div>
                        </div>
                        </div>

                        <div class="info-list-2">
                            <#if debitBill>
                                <div class="form-label">
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
                    </li>
                    <li>
                        <div class="info-list-3">
                            <div class="form-label margin-left-2">车牌：</div>
                            <div class="form-item">
                                <div class="yqx-text car-license">${formEntity.carLicense}</div>
                            <#if formEntity.carLicense>
                                <a class="license-detail" target="_blank"
                                   href="${BASE_PATH}/shop/customer/car-detail?refer=carwash-detail&id=${formEntity.customerCarId}">更多车辆信息</a>
                            </#if>
                            </div>
                        </div><div class="info-list-3">
                        <div class="form-label">服务顾问：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">${formEntity.receiverName}</div>
                        </div>
                    </div><div class="info-list-3">
                        <div class="form-label margin-left-3">洗车工：</div>
                        <div class="form-item">
                            <div class="yqx-text  max-width js-show-tips">${formEntity.workerNames}</div>
                        </div>
                    </div>
                    </li><#--联系人-->
                    <li>
                        <div class="info-list-3">
                            <div class="form-label margin-left-2">总计：</div>
                            <div class="form-item">
                                <div class="yqx-text max-width js-show-tips">${formEntity.orderAmount}元
                                <#if orderInfo.downPayment gt 0.00 && orderInfo.orderStatus =='WXDD'>
                                    <span class="pre-payment">预付定金：<i class="money-sum">${orderInfo.downPayment?string(",###.##")}</i>元</span>
                                </#if>
                                </div>
                            </div>
                        </div><div class="info-list-3">
                        <div class="form-label margin-left-3">联系人：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">${formEntity.contactName}</div>
                        </div>
                    </div><div class="info-list-3">
                        <div class="form-label">联系电话：</div>
                        <div class="form-item max-width js-show-tips">
                            <div class="yqx-text js-show-tips">${formEntity.contactMobile}</div>
                        </div>
                    </div>
                    </li><#--年款排量-->
                    <li>
                        <div class="info-list-3">
                            <div class="form-label" style="margin-left: 15px;">VIN码：</div>
                            <div class="form-item">
                                <div class="yqx-text max-width js-show-tips">${formEntity.vin}</div>
                            </div>
                        </div><div class="info-list-3">
                        <div class="form-label margin-left-2">车型：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">${formEntity.carInfo}</div>
                        </div>
                    </div><div class="info-list-3">
                        <div class="form-label">年款排量：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            <#if formEntity.carGearBox>
                                ${formEntity.carYear} ${formEntity.carGearBox}
                            <#else>
                                ${formEntity.carYear} ${formEntity.carPower}
                            </#if>
                            </div>
                        </div>
                    </div>
                    </li><#--客户单位-->
                    <li class="company">
                        <div class="info-list-3">
                            <div class="form-label">行驶里程：</div>
                            <div class="form-item">
                                <div class="yqx-text max-width js-show-tips">${formEntity.mileage}km</div>
                            </div>
                        </div><div class="info-list-6">
                        <div class="form-label">客户单位：</div>
                        <div class="form-item">
                            <div class="yqx-text detail-company js-show-tips">${formEntity.company}</div>
                        </div>
                    </div>
                    </li><#--备注-->
                    <li class="postscript">
                    <#if formEntity.postscript>
                        <div class="form-label margin-left-2">备注：</div>
                        <div class="yqx-text detail-content">
                            <div class="bz-maxwidth js-show-tips"> ${formEntity.postscript}</div>
                        </div>
                    </#if>
                    </li>
                <#if orderInfo.orderStatus =='DDYFK'>
                    <li>
                        <div class="info-list-3">
                            <div class="form-label" style="margin-left: 15px;">应收金额：</div>
                            <div class="form-item">
                                <i id="allMoneySum" class="money-sum">${orderInfo.payAmount?string(",###.##")}</i>
                                元
                            </div>
                        </div>
                    </li>
                   <#if orderInfo.downPayment gt 0.00 >
                        <li>
                            <div class="info-list-3">
                                <div class="form-label" style="margin-left: 15px;">预付定金：</div>
                                <div class="form-item">
                                    <i id="allMoneySum" class="money-sum">${orderInfo.downPayment?string(",###.##")}</i>
                                    元
                                </div>
                            </div>
                        </li>
                    </#if>
                    <li>
                        <div class="info-list-3">
                            <div class="form-label" style="margin-left: 15px;">挂账金额：</div>
                            <div class="form-item">
                                <i id="allMoneySum" class="money-sum">${orderInfo.signAmount?string(",###.##")}</i>
                                元
                            </div>
                        </div>
                    </li>

                </#if>

                </ul>
            <#if formEntity.carLicensePicture>
                <img class="car-img" src="${formEntity.carLicensePicture}"/>
            </#if>

            </div>
            <!--印章-->
        <#if formEntity.orderStatus == 'DDYFK' && formEntity.payStatus == 2>
            <div class="seal settlement"></div>
        <#elseif formEntity.orderStatus == 'DDYFK' && formEntity.payStatus == 1>
            <div class="seal sign"></div>
        <#elseif formEntity.orderStatus == 'WXDD'>
            <div class="seal invalid"></div>
        </#if>
            <div class="form-btns clearfix">
            <#if formEntity.orderStatus !='WXDD'>
                <button class="yqx-btn yqx-btn-1 js-print btn-left old-print-btn hide">打印</button>
                    <#list openPrintConfig as item>
                        <#if item.printTemplate == 2>
                            <button
                                    data-href="${BASE_PATH}/shop/settlement/shop-settle-print"
                                    class="yqx-btn yqx-btn-1 js-print-new new-print-btn hide btn-left">
                                结算单打印
                            </button>
                        </#if>
                        <#if item.printTemplate == 5 && orderInfo.orderStatus =='DDYFK'>
                            <button data-href="${BASE_PATH}/shop/settlement/shop-receipt-print"
                                    data-target="receipt"
                                    class="yqx-btn yqx-btn-1 js-print-new new-print-btn hide btn-left">
                                小票打印
                            </button>
                        </#if>
                    </#list>
                <button class="yqx-btn yqx-btn-1 js-carwash-perfect btn-left">完善</button>
                <div class="old-print-tips hide btn-left">
                    <p>新版打印可自定义模板，打印速度更快而且还支持小票打印哦</p>
                    <p class="money-font">
                        <a href="${BASE_PATH}/shop/print-config"><i class="underline">马上切换 >>></i></a>
                    </p>
                </div>
            </#if>
                <button class="yqx-btn yqx-btn-1 js-carwash-back btn-right">返回</button>

            <#if formEntity.orderStatus !='WXDD'>
                <button class="yqx-btn yqx-btn-1 js-carwash-invalid btn-right display-none">无效</button>
            </#if>
            <#if formEntity.orderStatus =='WXDD'>
                <button class="yqx-btn yqx-btn-2 js-carwash-delete btn-left display-none">删除</button>
            </#if>

            </div>
        <#if orderInfo.orderStatus !='WXDD'>
            <div class="new-print-tips hide">
                <p>不满意打印效果 <i class="money-font"><a href="${BASE_PATH}/shop/print-config"><i class="underline">马上设置</i> >>></a></i></p>
            </div>
        </#if>
        </div>
    </div>
    <!-- 右侧内容区 end -->
</div>
<#--小票打印教程-->
<#include "yqx/tpl/print/receipt-guide-tpl.ftl">

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/common/print/printVersionChange.js?5da782cabc728d23a9b68bb38cd33fb4"></script>
<script src="${BASE_PATH}/static/js/page/order/carwash-detail.js?aaac238e07c13839645624bc9c5d7dab"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
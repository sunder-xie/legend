<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/debit/carwash-detail.css?5494f53f156c88715efd9dd5ad9e78df"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/settlement/settlement-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <input type="hidden" id="orderId" value="${formEntity.orderId}"/>
        <input type="hidden" id="orderSn" value="${formEntity.orderSn}"/>
        <input type="hidden" id="orderStatus" value="${formEntity.orderStatus}"/>
        <input type="hidden" id="customerCarId" value="${formEntity.customerCarId}"/>
        <div class="order-head clearfix">
            <h3 class="headline fl">工单收款－
                <small>洗车单详情</small>
            </h3>
            <!-- 工作进度 start -->
            <div class="order-process fr">
                <div class="order-step <#if orderInfo.orderStatus=='DDYFK'> order-step-finish </#if>">
                    <div class="order-step-circle">1</div>
                    <p class="order-step-title">工单确认账单</p>
                </div>
                <div class="order-step <#if orderInfo.orderStatus=='DDYFK' &&  debitBill != null && (debitBill.signAmount lt debitBill.receivableAmount || debitBill.receivableAmount ==0)>order-step-finish </#if>">
                    <div class="order-step-circle">2</div>
                    <p class="order-step-title">工单收款</p>
                </div>
            </div>
            <!-- 工作进度 end -->
        </div>
        <!-- 详情内容 start -->
        <div class="detail-box">
            <div class="car-info">
                <div class="show-grid bold">
                    <div class="form-label">
                        工单编号：
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        ${formEntity.orderSn}（开单日期： ${formEntity.createTimeStr}）
                        </div>
                    </div>
                </div>
                <div class="show-grid bold">
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

                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label form-label-width">
                            车牌：
                        </div>
                        <div class="form-item form-item-width">
                            <div class="yqx-text">
                                <span class="js-show-tips ellipsis-1 license-weight">${formEntity.carLicense}</span>
                                <a class="more-car" target="_blank"
                                   href="${BASE_PATH}/shop/customer/car-detail?refer=carwash-detail&id=${formEntity.customerCarId}">更多车辆信息</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">
                            服务顾问：
                        </div>
                        <div class="form-item form-item-width">
                            <div class="yqx-text">
                            ${formEntity.receiverName}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">
                            洗车工：
                        </div>
                        <div class="form-item form-item-width ">
                            <div class="yqx-text ellipsis-1 js-show-tips">
                            ${formEntity.workerNames}
                            </div>
                        </div>
                    </div>
                </div>

                <div class="show-grid">

                    <div class="col-4">
                        <div class="form-label form-label-width">
                            收款金额：
                        </div>
                        <div class="form-item form-item-width">
                            <div class="yqx-text">
                            ${formEntity.orderAmount}元
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">
                            联系人：
                        </div>
                        <div class="form-item form-item-width">
                            <div class="yqx-text">
                            ${formEntity.contactName}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">
                            联系电话：
                        </div>
                        <div class="form-item form-item-width">
                            <div class="yqx-text">
                            ${formEntity.contactMobile}
                            </div>
                        </div>
                    </div>
                </div>

                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label form-label-width">
                            VIN码：
                        </div>
                        <div class="form-item form-item-width">
                            <div class="yqx-text">
                            ${formEntity.vin}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">
                            车型：
                        </div>
                        <div class="form-item form-item-width">
                            <div class="yqx-text models js-show-tips">
                            ${formEntity.carInfo}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width js-show-tips">
                            年款排量：
                        </div>
                        <div class="form-item form-item-width">
                            <div class="yqx-text max-text">
                            <#if formEntity.carGearBox>
                            ${formEntity.carYear} ${formEntity.carGearBox}
                            <#else>
                            ${formEntity.carYear} ${formEntity.carPower}
                            </#if>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label form-label-width">
                            行驶里程：
                        </div>
                        <div class="form-item form-item-width">
                            <div class="yqx-text">
                            ${formEntity.mileage}km
                            </div>
                        </div>
                    </div>
                    <div class="col-8">
                        <div class="form-label form-label-width">
                            客户单位：
                        </div>
                        <div class="form-item form-item-width">
                            <div class="yqx-text company-width js-show-tips">
                            ${formEntity.company}
                            </div>
                        </div>
                    </div>
                </div>
                <div >
                <#if formEntity.orderStatus == "CJDD">
                    <div class="seal"></div>
                <#elseif formEntity.orderStatus == "DDBJ">
                    <div class="seal quoted"></div>
                <#elseif formEntity.orderStatus == "FPDD" || formEntity.orderStatus == "DDSG">
                    <div class="seal dispatching"></div>
                <#elseif formEntity.orderStatus == "DDWC">
                    <div class="seal finished"></div>
                <#elseif formEntity.orderStatus == 'DDYFK' && formEntity.payStatus == 1>
                    <div class="seal sign"></div>
                <#elseif formEntity.orderStatus == 'DDYFK' && formEntity.payStatus == 2>
                    <div class="seal settlement"></div>
                <#elseif formEntity.orderStatus == "WXDD">
                    <div class="seal invalid"></div>
                </#if>
                </div>
                <#if formEntity.carLicensePicture>
                <div class="show-grid">
                    <img class="car-img" src="${formEntity.carLicensePicture}"/>
                </div>
                </#if>
            </div>
            <div class="bot-box">
            <#if orderInfo.postscript >
                <div class="bot-text">工单备注：${orderInfo.postscript}</div>
            </#if>
                <div class="bot-text">
                    总计：<span class="money-font">&yen; ${orderInfo.orderAmount}</span>
                    <div class="total-details-tag js-tag"></div>
                    <div class="total-hide hide">
                        <div class="total-arrow"></div><div class="total-details">服务费用：<span class="money-font white">&yen;${orderInfo.serviceAmount - orderInfo.serviceDiscount}</span> + 配件费用：<span class="money-font white"> &yen;${orderInfo.goodsAmount-orderInfo.goodsDiscount}</span> = 总计：<span class="money-font white">&yen; ${orderInfo.orderAmount}</span></div>
                    </div>
                <#if !debitBill && orderInfo && orderInfo.downPayment gt 0.00>
                    预付定金：<span class="money-font">&yen; #{orderInfo.downPayment}</span>
                </#if>
                </div>
            <#if debitBill>
                <div class="bot-text">应收金额：
                    <span class="money-font">&yen; ${debitBill.receivableAmount}</span>
                    <div class="total-details-tag js-tag"></div>
                    <div class="total-hide hide">
                        <div class="total-arrow"></div><span class="total-details">总计：<span class="money-font white">&yen;${orderInfo.orderAmount}<#if orderInfo.taxAmount gt 0></span> + 费用：<span class="money-font white"> &yen;${orderInfo.taxAmount}</#if></span> - 优惠总金额：<span class="money-font white"> &yen;${orderInfo.orderDiscountAmount}</span> = 应收金额：<span class="money-font white"> &yen; ${debitBill.receivableAmount}</span></div>
                    </div>
                <#if orderInfo && orderInfo.downPayment gt 0.00>
                    <div class="bot-text">预付定金：
                        <span class="money-font">&yen; ${orderInfo.downPayment}</span>
                    </div>
                </#if>
                <div class="bot-text">实收金额：
                    <span class="money-font">&yen; ${debitBill.paidAmount}</span>
                </div>
                <div class="bot-text">挂账金额：
                    <span class="money-font">&yen; ${debitBill.signAmount}</span>
                    <div class="total-details-tag js-tag"></div>
                    <div class="total-hide hide">
                        <div class="total-arrow"></div><div class="total-details">应收金额：<span class="money-font white"> &yen;${debitBill.receivableAmount}</span> -　实收金额：<span class="money-font white"> &yen;${debitBill.paidAmount}</span> <#if debitBill.badAmount gt 0> - 坏账金额：<span class="money-font white"> &yen;${debitBill.badAmount}</span></#if> = 挂账金额：<span class="money-font white"> &yen;${debitBill.signAmount}</span> </div>
                    </div>
                </div>
                <#if debitBill.badAmount gt 0>
                    <div class="bot-text">坏账金额：
                        <span class="money-font">&yen; ${debitBill.badAmount}</span>
                    </div>
                </#if>
            </#if>
            </div>
            <div class="history-box">
               <span class="pay-history">
                    <img src="${BASE_PATH}/static/img/page/settlement/debit/history-ico1.png"><a href="javascript:;" class="js-history-record">收款记录</a>
                </span>
                <span class="sign-history display-none">
                <img src="${BASE_PATH}/static/img/page/settlement/debit/history-ico2.png"><a href="javascript:;" class="js-history-order" id="HistoryOrder">挂账工单</a>
                </span>
            <#if  formEntity.invoiceType gt 0>
                <span>
                <img src="${BASE_PATH}/static/img/page/settlement/debit/ticket.png"><a href="javascript:;" class="js-ticket-record" >开票记录</a>
                </span>
            </#if>
            </div>
            <!--按钮 start-->
            <div class="btn-box clearfix detail-btn-group">
                <#if (formEntity.payStatus ==1 || formEntity.payStatus == 0) && formEntity.orderStatus !='WXDD'>
                    <button class="yqx-btn yqx-btn-2 yqx-btn-small js-debit">收款</button>
                </#if><#if  formEntity.invoiceType ==0>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small js-ticket">开票</button>
                </#if>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-print old-print-btn hide">打印</button>
                <#list openPrintConfig as item>
                    <#if item.printTemplate == 2>
                        <button class="yqx-btn yqx-btn-small yqx-btn-1 js-print-new new-print-btn hide">结算单打印</button>
                    </#if>
                    <#if item.printTemplate == 5 && orderInfo.orderStatus =='DDYFK'>
                        <button data-href="${BASE_PATH}/shop/settlement/shop-receipt-print"
                                data-target="receipt"
                                class="yqx-btn yqx-btn-small yqx-btn-1 js-print-new new-print-btn hide">小票打印</button>
                    </#if>
                </#list>
                <#if formEntity.payStatus ==1>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small js-bad-bill">坏账</button>
                </#if>
                    <div class="old-print-tips hide">
                        <p>新版打印可自定义模板，打印速度更快而且还支持小票打印哦</p>
                        <p class="money-font"><a href="${BASE_PATH}/shop/print-config"><i class="underline">马上切换 >>></i></a></p>
                    </div>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
                <#if formEntity.orderStatus =='WXDD'>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-delete display-none">删除</button>
                <#else>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small fr invalid-btn js-invalid display-none">无效</button>
                </#if>
                <#if orderInfo.orderStatus !='WXDD'>
                    <div class="new-print-tips hide">
                        <p>不满意打印效果 <i class="money-font"><a href="${BASE_PATH}/shop/print-config"><i class="underline">马上设置</i> >>></a></i></p>
                    </div>
                </#if>
            </div>
            <!--按钮 end-->
        </div>
        </div>
    <!-- 右侧内容区 end -->
    </div>

<!-- 打印dialog start -->
<script type="text/html" id="print-dialog-tpl">
    <div class="dialog" data-tpl-ref="order-print-tpl">
        <div class="dialog-title">
            打印
        </div>
        <div class="dialog-con">
            <a href="javascript:;" class="link-btn js-common-print">结算单打印</a>
            <a href="javascript:;" class="link-btn js-simple-print">简化版结算单打印</a>
        </div>
    </div
</script>

<!-- 收支历史模版 -->
<#include "yqx/tpl/settlement/history-debit.ftl">
<!-- 挂帐工单模版 -->
<#include "yqx/tpl/settlement/history-order.ftl">
<!-- 开票模版 -->
<#include "yqx/tpl/settlement/ticket.ftl">
<!-- 已开票模板-->
<#include "yqx/tpl/settlement/ticket-record.ftl">
<!-- 冲红单模版 -->
<#include "yqx/tpl/settlement/red-order.ftl">
<!--坏帐弹窗-->
<#include "yqx/tpl/settlement/debit/bad-debit-tpl.ftl">

<#--小票打印教程-->
<#include "yqx/tpl/print/receipt-guide-tpl.ftl">
<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/common/print/printVersionChange.js?5da782cabc728d23a9b68bb38cd33fb4"></script>
<script  src="${BASE_PATH}/static/js/page/settlement/debit/carwash-detail.js?ba230e24ceb47d1df213d7ecdd1d8fdd"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
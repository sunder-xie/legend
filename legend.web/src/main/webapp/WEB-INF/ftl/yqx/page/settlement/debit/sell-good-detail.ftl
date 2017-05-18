<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/debit/sell-good-detail.css?79f30f543aaf27e729d26b9948fe25d8"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/settlement/settlement-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <input type="hidden" id="orderId" value="${orderInfo.id}"/>
        <input type="hidden" id="orderSn" value="${orderInfo.orderSn}"/>
        <input type="hidden" id="orderStatus" value="${orderInfo.orderStatus}"/>
        <input type="hidden" id="customerCarId" value="${orderInfo.customerCarId}"/>
        <div class="order-head clearfix">
            <h3 class="headline fl">工单收款－
                <small>销售单详情</small>
            </h3>
            <!-- 工作进度 start -->
            <div class="order-process fr">
                <div class="order-step <#if orderInfo.orderStatus=='DDYFK'> order-step-finish </#if>">
                    <div class="order-step-circle">1</div>
                    <p class="order-step-title">工单确认账单</p>
                </div>
                <div class="order-step <#if orderInfo.orderStatus=='DDYFK' && debitBill != null && (debitBill.signAmount lt debitBill.receivableAmount || debitBill.receivableAmount ==0)>order-step-finish </#if>">
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
                <div class="form-label form-label-width">
                    工单编号：
                </div>
                <div class="form-item">
                    <div class="yqx-text">
                    ${orderInfo.orderSn}
                        （开单日期： <#if orderInfo.createTime>${orderInfo.createTime?string("yyyy-MM-dd HH:mm")}</#if>）
                    </div>
                </div>
            </div>
            <div class="show-grid bold">
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
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-width">
                        车牌：
                    </div>
                    <div class="form-item form-item-width" style="white-space: nowrap">
                        <div class="yqx-text">
                        <span class="js-show-tips ellipsis-1 license-weight">${orderInfo.carLicense}</span>
                            <a class="more-car" target="_blank"
                                                  href="${BASE_PATH}/shop/customer/car-detail?refer=carwash-detail&id=${orderInfo.customerCarId}">更多车辆信息</a>
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        车型：
                    </div>
                    <div class="form-item form-item-width">
                        <div class="yqx-text  models js-show-tips">
                        ${orderInfo.carInfo}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        服务顾问：
                    </div>
                    <div class="form-item form-item-width" style="position: relative;z-index:1">
                        <div class="yqx-text">
                        ${orderInfo.receiverName}
                        </div>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-4">
                    <div class="form-label form-label-width">
                        联系人：
                    </div>
                    <div class="form-item form-item-width">
                        <div class="yqx-text">
                        ${orderInfo.contactName}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        联系电话：
                    </div>
                    <div class="form-item form-item-width">
                        <div class="yqx-text">
                        ${orderInfo.contactMobile}
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-label form-label-width">
                        客户单位：
                    </div>
                    <div class="form-item form-item-width">
                        <div class="yqx-text company-width js-show-tips">
                        ${customer.company}
                        </div>
                    </div>
                </div>
            </div>


            <div >
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
            </div>
            </div>
        </div>
        <!-- 详情内容 end -->

        <!-- 表格详情 start-->
        <div class="detail-table">
            <#if orderGoodsList>
            <div class="table-box">
                <h3>配件物料</h3>
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>零件号</th>
                        <th>配件名称</th>
                        <#--<th>适配车型</th>-->
                        <th>售价</th>
                        <th>数量</th>
                        <th>金额</th>
                        <th>优惠金额</th>
                        <th>库存</th>
                        <th>销售员</th>
                        <th>配件备注</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list orderGoodsList as orderGoods>
                    <tr>
                        <td>
                        <div class="text-overflow js-show-tips">${orderGoods.goodsFormat}</div>
                        </td>
                        <td>
                            <div class="text-overflow js-show-tips">${orderGoods.goodsName}</div>
                        </td>
                        <td class="td-right">${orderGoods.goodsPrice}</td>
                        <td class="td-right">${orderGoods.goodsNumber} ${orderGoods.measureUnit}</td>
                        <td class="td-right js-show-tips">${orderGoods.goodsAmount}</td>
                        <td class="td-right">${orderGoods.discount}</td>
                        <td class="td-right table-boundary js-show-tips">${orderGoods.stock}</td>
                        <td>
                            <div class="text-overflow js-show-tips">${orderGoods.saleName}</div>
                        </td>
                        <td>
                            <div class="text-overflow js-show-tips">${orderGoods.goodsNote}</div>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
                <p>配件总费用：<span class="money-font"> &yen;${orderInfo.goodsAmount}</span> - 配件总优惠：<span class="money-font"> &yen;${orderInfo.goodsDiscount}</span> = 配件费用：<span class="money-font">&yen;${orderInfo.goodsAmount-orderInfo.goodsDiscount}</span></p>
            </div>
            </#if>
        </div>
        <!-- 表格详情 end-->
        <div class="bot-box">
            <#if orderInfo.postscript >
            <div class="bot-text">工单备注：${orderInfo.postscript}</div>
            </#if>
            <div class="bot-text">
                总计：<span class="money-font">&yen; ${orderInfo.orderAmount}</span>
                <div class="total-details-tag js-tag"></div>
                <div class="total-hide hide">
                    <div class="total-arrow"></div><div class="total-details">配件费用：<span class="money-font white">&yen;${orderInfo.goodsAmount-orderInfo.goodsDiscount}</span></div>
                </div>
            </div>
        <#if debitBill>
            <div class="bot-text">应收金额：
                <span class="money-font">&yen; ${debitBill.receivableAmount}</span>
                <div class="total-details-tag js-tag"></div>
                <div class="total-hide hide">
                    <div class="total-arrow"></div><div class="total-details">总计：<span class="money-font white"> &yen;${orderInfo.orderAmount}</span><#if orderInfo.taxAmount gt 0> + 费用：<span class="money-font white"> &yen;${orderInfo.taxAmount}</span></#if> - 优惠总金额：<span class="money-font white"> &yen;${orderInfo.orderDiscountAmount}</span> = 应收金额：<span class="money-font white"> &yen; ${debitBill.receivableAmount}</span></div>
                </div>
            </div>
            <div class="bot-text">实收金额：
                <span class="money-font">&yen; ${debitBill.paidAmount}</span>
            </div>
            <div class="bot-text">挂账金额：
                <span class="money-font">&yen; ${debitBill.signAmount}</span>
                <div class="total-details-tag js-tag"></div>
                <div class="total-hide hide">
                    <div class="total-arrow"></div><div class="total-details">应收金额：<span class="money-font white"> &yen;${debitBill.receivableAmount}</span> -　实收金额：<span class="money-font white"> &yen;${debitBill.paidAmount}</span> <#if debitBill.badAmount gt 0> - 坏账金额：<span class="money-font white"> &yen;${debitBill.badAmount}</span></#if> = 挂账金额：<span class="money-font white"> &yen;${debitBill.signAmount}</span></div>
                </div>
            </div>
            <#if debitBill.badAmount gt 0>
                <div class="bot-text">坏账金额：
                    <span class="money-font">&yen; ${debitBill.badAmount}</span>
                </div>
            </#if>
        </#if>
            <div class="history-box">
                <span class="pay-history">
                    <img src="${BASE_PATH}/static/img/page/settlement/debit/history-ico1.png"><a href="javascript:;" class="js-history-record">收款记录</a>
                </span>
                <span class="sign-history display-none">
                <img src="${BASE_PATH}/static/img/page/settlement/debit/history-ico2.png"><a href="javascript:;" class="js-history-order" id="HistoryOrder">挂账工单</a>
                </span>
            <#if  orderInfo.invoiceType gt 0>
                <span>
                <img src="${BASE_PATH}/static/img/page/settlement/debit/ticket.png"><a href="javascript:;" class="js-ticket-record" >开票记录</a>
                </span>
            </#if>
            </div>
        </div>

        <!--按钮 start-->
        <div class="btn-box clearfix detail-btn-group">
        <#if ((orderInfo.payStatus ==1 || orderInfo.payStatus==0)&& orderInfo.orderStatus !='WXDD') >
            <button class="yqx-btn yqx-btn-2 yqx-btn-small js-debit" data-pay-status="${orderInfo.payStatus}">收款</button>
        </#if>
        <#if orderInfo.invoiceType ==0>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-ticket">开票</button>
        </#if>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-print old-print-btn hide">打印</button>
        <#list openPrintConfig as item>
            <#if item.printTemplate == 2>
                <button
                        data-href="${BASE_PATH}/shop/settlement/print/sale-print"
                        class="yqx-btn yqx-btn-1 js-print yqx-btn-small new-print-btn hide">结算单打印
                </button>
            </#if>
            <#if item.printTemplate == 5>
                <button data-href="${BASE_PATH}/shop/settlement/shop-receipt-print"
                        data-target="receipt"
                        class="yqx-btn yqx-btn-small yqx-btn-1 js-print new-print-btn hide">小票打印</button>
            </#if>
        </#list>
        <#if orderInfo.payStatus ==1>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-bad-bill">坏账</button>
        </#if>
            <div class="old-print-tips hide">
                <p>新版打印可自定义模板，打印速度更快而且还支持小票打印哦</p>
                <p class="money-font"><a href="${BASE_PATH}/shop/print-config"><i class="underline">马上切换 >>></i></a></p>
            </div>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-copy copy-margin">复制</button>
        <#if orderInfo.orderStatus =='WXDD'>
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
    </div>
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
<script  src="${BASE_PATH}/static/js/page/settlement/debit/sell-good-detail.js?2888b088255bb9c0083e62dfe6faceb1"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
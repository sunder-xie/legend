<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/pay/pay-supplier-behavior.css?745264659f4db03514da183216646b82"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/settlement/settlement-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->
    <!--隐藏域传值 start-->
        <input type="hidden" id="ids" value="${ids}">
    <input type="hidden" name="search_supplierId" value="${supplierSettlementVO.supplierId}">
    <input type="hidden" name="search_supplierName" value="${supplierSettlementVO.supplierName}">
    <!--隐藏域传值 end-->
    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h3 class="headline fl">供应商付款－
                <small>付款</small>
            </h3>
        </div>
        <!--付款明细表格 start-->
        <div class="pay-box clearfix js-tableShow">
            付款明细表格
            <div class="table-btn"><i class="icon-angle-down"></i><span>展开</span></div>
        </div>
        <div class="show-table" id="payTable">

        </div>
        <!--付款明细表格 end-->

        <!--付款信息 start-->
        <div class="pay-inforbox">
            <div class="pay-title">付款信息</div>
            <div class="pay-con">
                <div class="method-title">收款金额&方式</div>
                <div class="method-box">
                    <div class="form-label">
                        支付方式
                    </div>
                    <div class="form-item">
                        <input type="text" id="paymentName" name="" class="yqx-input yqx-input-icon yqx-input-small js-method" value="" placeholder="请选择" data-v-type=" required " >
                        <input type="hidden" id="paymentId" value="">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-label">
                        付款
                    </div>
                    <div class="form-item ">
                        <input type="text" id="amount" <#if modifyFlag >disabled</#if> name="" class="yqx-input yqx-input-icon yqx-input-small js-pay" value="${supplierSettlementVO.amountPayable}" placeholder="" data-v-type="floating | required ">
                        <span class="fa icon-small">元</span>
                    </div>
                </div>
                <div class="remarks">
                    <div class="form-label">
                        备注
                    </div>
                    <div class="form-item">
                        <textarea class="yqx-textarea text-area-width" name="" id="remark" rows="1" placeholder="请输入备注" data-v-type="maxLength:200"></textarea>
                    </div>
                </div>
                <div class="total-box">
                    <#setting number_format="0.00">
                    <p>应付金额：<span class="money-font">&yen; </span><span class="money-font" id="totalAmount">${supplierSettlementVO.amountPayable}</span> </p>
                    <p>实付金额：<span class="money-font">&yen; </span><span class="money-font" id="paidAmount">${supplierSettlementVO.amountPayable}</span></p>
                    <p>未付金额：<span class="money-font">&yen; </span><span class="money-font" id="unpaidAmount">0.00</span></p>

                    <div class="payee">
                        <span>收银人员：${operator}</span>
                        <span>付款日期：${.now?string("yyyy-MM-dd HH:mm")}</span>
                    </div>
                </div>
            </div>
            <div class="btn-box clearfix">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-save-btn">付款</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
            </div>
        </div>
        <!--付款信息 end-->
    </div>
    <!-- 右侧内容区 end -->
</div>

<script type="text/html" id="payTableTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>序号</th>
            <th>采购编号</th>
            <th>配件总价</th>
            <th>税费</th>
            <th>运费</th>
            <th>应付金额</th>
            <th>实付金额</th>
            <th>未付金额</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.data && json.data.content){%>
        <%var con = json.data.content[0];%>
        <%for(var i=0;i< con.warehouseInList.length;i++){%>
        <%var item = json.data.content[0].warehouseInList[i];%>
        <tr>
            <td><%=json.data.size*(json.data.number-1)+i+1%></td>
            <td><%=item.warehouseInSn%></td>
            <td class="money-font">&yen;<%=item.goodsAmount%></td>
            <td class="money-font">&yen;<%=item.tax%></td>
            <td class="money-font">&yen;<%=item.freight%></td>
            <td class="money-font">&yen;<%=item.totalAmount%></td>
            <td class="money-font">&yen;<%=item.amountPaid%></td>
            <td class="money-font">&yen;<%=item.amountPayable%></td>
        </tr>
        <%}%>
        <tr>
            <td>总计</td>
            <td></td>
            <td class="money-font">&yen;<%=con.goodsAmount%></td>
            <td class="money-font">&yen;<%=con.taxAmount%></td>
            <td class="money-font">&yen;<%=con.freightAmount%></td>
            <td class="money-font">&yen;<%=con.totalAmount%></td>
            <td class="money-font">&yen;<%=con.amountPaid%></td>
            <td class="money-font">&yen;<%=con.amountPayable%></td>
        </tr>
        <%}%>

        </tbody>
    </table>
</script>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/settlement/pay/pay-supplier-behavior.js?a944875fe4be715533091a4dafb24e40"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">
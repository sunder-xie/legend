<!--收支历史记录 模板-->
<style>
    .dialog-record li{ border-bottom: 1px solid #d6d6d6; line-height: 30px; text-align: left;}
    .dialog-record li span{ margin-right: 10px;}
    .history-debit-box .remark-box {line-height: 15px;word-break: break-all;margin-bottom: 10px;}
</style>
<script type="text/html" id="payHistoryTpl" >
    <div class="dialog history-debit-box" data-tpl-ref="history-debit">
        <div class="dialog-title">收款记录</div>
        <div class="dialog-con">
            <ul class="dialog-record">
                <#if debitBill>
                    <li>
                        <div class="remark-box">
                        <span>应收金额：&yen; ${debitBill.receivableAmount}</span>
                        <span>实收金额：&yen; ${debitBill.paidAmount}</span>
                        <span>挂账金额：&yen; ${debitBill.signAmount}</span>
                        <#if debitBill.badAmount gt 0>
                            <span>坏账金额：&yen; ${debitBill.badAmount}</span>
                        </#if>
                        <span>备注：${debitBill.remark}</span>
                            </div>
                    </li>
                </#if>
                <#if redBill>
                    <li>
                        <span>冲红应收金额：&yen; ${redBill.receivableAmount}</span>
                        <#if redBill.paidAmount < 0>
                            <span>冲红实收金额：&yen; ${redBill.paidAmount}</span>
                        </#if>
                        <#if redBill.signAmount < 0>
                            <span>冲红挂账金额：&yen; ${redBill.signAmount}</span>
                        </#if>
                        <#if redBill.badAmount < 0>
                            <span>冲红坏账金额：&yen; ${redBill.badAmount}</span>
                        </#if>
                    </li>
                </#if>
                <%if(json.data.discountFlowList){%>
                <%for(var i=0;i<json.data.discountFlowList.length; i++){%>
                <%var item = json.data.discountFlowList[i]%>
                <li>
                    <div>
                    <span>日期：<%= item.gmtCreateStr%></span>
                    <span><%=item.discountName%>: <%=item.discountAmount%></span>
                    <span>收银人员：<%=item.operatorName%></span>
                        </div>
                </li>
                <%}}%>

                <%if(json.data.debitBillFlowList){%>
                <%for(var i=0;i<json.data.debitBillFlowList.length; i++){%>
                <%var item = json.data.debitBillFlowList[i]%>
                <li>
                    <div class="remark-box">
                    <span>日期：<%= item.gmtCreateStr%></span>
                    <span><%if(item.flowStatus==0){%><%=item.paymentName%><%}else{%>坏账<%}%>: <%=item.payAmount%></span>
                    <span>收银人员：<%=item.operatorName%></span>
                    <span>备注：<%=item.remark%></span>
                        </div>
                </li>
                <%}}%>
            </ul>
        </div>
    </div>
</script>

<script>
    $(function(){
        var $doc =$(document);
        seajs.use([
            'art',
            'dialog'
        ],function(at,dg){
            $doc.on('click','.js-history-record',function(){
                var orderId = $('#orderId').val();
                $.ajax({
                    url: BASE_PATH + "/shop/settlement/debit/history-flow-list",
                    data:{
                        orderId: orderId
                    },
                    success:function(result){
                        if(result.success){
                            var html = at('payHistoryTpl',{json:result});
                            dg.open({
                                area: ['800px', '400px'],
                                content:html
                            })
                        }
                    }
                });
            });
        })
    })
</script>
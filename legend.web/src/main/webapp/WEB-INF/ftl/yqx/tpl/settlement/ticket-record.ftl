<#--
   zmx  2016/6/13
   开票模板
 -->
<style>
    .red-title p{ line-height: 20px; font-size: 12px; text-align: center;}
    .order-num span{ margin-right: 50px;}
    .attention img{ vertical-align: middle;}
    .dialog-con .form-label{color: #666;}
    .dialog-con .form-item{color: #333;}
    .dialog-con .cols{height:30px; line-height:30px;text-align: left;}
    .ticket-tbox{ height: 55px; line-height: 55px; border-bottom: 1px solid #cdcdcd; text-align: left;}
    .ticket-num{display: inline-block; font-size: 16px; font-weight: bold; color: #333;}
    .ticket-type{ float: right;}
    .dialog-con .money-font{color: #fa7b7c;}
    .ticket-record-box .ticket-remark {word-break: break-all;line-height: 15px;}
    .ticket-record-box .ticket-remark .form-item{word-break: break-all;display: inline}
</style>



<!--已开票 模板-->
<script type="text/html" id="ticketRecordTpl">
    <div class="dialog ticket-record-box" data-tpl-ref="ticket-report">
        <div class="dialog-title">开票信息</div>
        <div class="dialog-con" id="ticketForm">
            <div class="ticket-tbox">
                <div class="form-label">
                    发票号:
                </div>
                <div class="form-item ticket-num">
                    <%=json.invoiceSn%>
                </div>
                <div class="ticket-type">
                    <%if(json.invoiceType==1){%> 普通发票<%}%>
                    <%if(json.invoiceType==2){%> 增值发票<%}%>
                </div>
            </div>
            <div class="cols">
                <div class="form-label">
                    开票单位:
                </div>
                <div class="form-item">
                  <%=json.company%>
                </div>
            </div>
            <div class="cols">
                <div class="form-label">
                    开票金额:
                </div>
                <div class="form-item money-font">
                  &yen;<%=json.price%>
                </div>
            </div>
            <div class="cols">
                <div class="form-label">
                    开票人:
                </div>
                <div class="form-item">
                    <%=json.operatorName%>
                </div>
            </div>
            <div class="cols ticket-remark">
                <div class="form-label">
                    备注:
                </div>
                <div class="form-item">
                  <%=json.postscript%>
                </div>
            </div>


        </div>
    </div>
</script>



<script>
    $(function () {
        seajs.use([
            'formData',
            'dialog',
            'select',
            'art',
            'check'
        ], function (fd, dg, st, at, ck) {

            $(document).on('click', '.js-ticket-record', function () {
                var orderId = $('#orderId').val();
                $.ajax({
                    url: BASE_PATH + "/shop/order_invoice/invoice_get",
                    data: {
                        id: orderId
                    },
                    success: function (result) {
                        if (result.success) {
                            var html = at('ticketRecordTpl', {json: result.data});
                            dg.open({
                                area: ['480px', '300px'],
                                content: html
                            })
                        }
                    }
                });
            })
        });
    });
</script>
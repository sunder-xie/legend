<#--
   zmx  2016/6/13
   开票模板
 -->

<style>
    .red-title {
        height: 40px;
    }
    .red-title p{ line-height: 20px; font-size: 12px; text-align: center;}
    .order-num span{ margin-right: 50px;}
    .attention img{ vertical-align: middle;}
    .dialog-con .ticket-form-label{ width: 70px;}
    .dialog-con .ticket-form-item{ width: 370px; text-align: left;}
    .dialog-con .cols{ margin-bottom: 10px;}
    .form-item input{ vertical-align: middle; margin-right: 4px;}
    .dialogbtn-box{ border-top: 1px solid #cdcdcd; padding-top: 15px;margin-top: 40px; text-align: center;}
    .ticket .price-item {
        width: 170px;
    }
</style>


<!--开票 模板-->
<script type="text/html" id="ticketTpl">
    <div class="dialog ticket">
        <div class="dialog-title red-title">
            开票信息
            <p>开票人： ${SESSION_USER_NAME}</p>
        </div>
        <div class="dialog-con" id="ticketForm">
            <input type="hidden" name="orderId" value="<#if orderInfo == null>${formEntity.orderId}<#else>${orderInfo.id}</#if>">
            <input type="hidden" name="orderSn" value="<#if orderInfo == null>${formEntity.orderSn}<#else>${orderInfo.orderSn}</#if>">
            <div class="cols">
                <div class="form-label form-label-must ticket-form-label">
                    开票类型
                </div>
                <div class="form-item ticket-form-item">
                    <input type="radio" name="invoiceType" value="1" checked = "checked">普通发票
                    <input type="radio" name="invoiceType" value="2" >增值发票
                    <#--<input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-ticket-type"-->
                           <#--data-v-type="required" value="" placeholder="">-->
                    <#--<input type="hidden" name="invoiceType">-->
                    <#--<span class="fa icon-angle-down icon-small"></span>-->
                </div>
            </div>
            <div class="cols">
                <div class="form-label form-label-must ticket-form-label">
                    发票号
                </div>
                <div class="form-item ticket-form-item">
                    <input type="text" name="invoiceSn" class="yqx-input yqx-input-small"  data-v-type="required"  value="" placeholder="" maxlength="20">
                </div>
            </div>
            <div class="cols">
                <div class="form-label form-label-must ticket-form-label">
                    开票单位
                </div>
                <div class="form-item ticket-form-item">
                    <input type="text" name="company" class="yqx-input yqx-input-small" data-v-type="required" value="" placeholder=""  maxlength="30">
                </div>
            </div>
            <div class="cols">
                <div class="form-label form-label-must ticket-form-label">
                    开票金额
                </div>
                <div class="form-item ticket-form-item price-item">
                    <input type="text" name="price" class="yqx-input yqx-input-small"  data-v-type="required|number"  maxlength="9" value="" placeholder="">
                    <span class="fa icon-small">元</span>
                </div>
            </div>
            <div class="cols">
                <div class="form-label ticket-form-label">
                    备注
                </div>
                <div class="form-item ticket-form-item">
                    <textarea type="text" name="postscript" class="yqx-input yqx-input-small" value="" rows="2" placeholder="" maxlength="100"></textarea>
                </div>
            </div>
            <div class="dialogbtn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-ticket-save">提交</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-ticket-cancel">取消</button>
            </div>
        </div>
    </div>
</script>


<script>
$(function(){
    seajs.use([
      'formData',
      'dialog',
      'select',
      'art',
      'check'
    ],function(fd,dg,st,at,ck){

        //开票按钮
        var ticket = null;
        $(document).on('click','.js-ticket',function(){
            var html = at('ticketTpl',{});
            ticket = dg.open({
                area: ['500px', '380px'],
                content:html
            });
            ck.init('#ticketForm');
        });


        //取消按钮
        $(document).on('click','.js-ticket-cancel',function(){
            dg.close(ticket)
        });

        //提交按钮
        $(document).on('click','.js-ticket-save',function(){
            if (!ck.check()) {
                return false;
            }
            var orderInvoiceLog = fd.get('#ticketForm');
            $.ajax({
                type: 'POST',
                data: orderInvoiceLog,
                url: BASE_PATH + '/shop/order_invoice/invoice_put',
                success: function (result) {
                    if (result.success) {
                        dg.success("操作成功", function () {
                            window.location.reload();
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            })
        })
    })
});
</script>
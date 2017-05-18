<#--
   zmx  2016/6/13
   冲红单模板
 -->

<style>
    .red-title {
        height: 80px;
    }

    .red-title p {
        line-height: 20px;
        font-size: 12px;
        text-align: center;
    }

    .order-num {
        height: 30px;
        text-align: left;
    }
    .order-num-width,.order-num-date{
        width: 300px;
        float: left;
    }

    .dialog .form-label-width{
        width:85px;
    }
    .dialog .red－item-width{
        width: 140px;
        text-align: left;
    }

    .money {
        height: 30px;
        line-height: 30px;
        text-align: left;
        padding: 20px 0;
    }

    .money .form-label {
        margin-left: 40px;
    }

    .money-width {
        width: 200px;
    }

    .attention {
        text-align: left;
        line-height: 30px;
        padding: 0 10px;
    }

    .attention img {
        vertical-align: middle;
    }

    .separate {
        border-bottom: 1px dashed #d1d1d1;
    }

    #RedOrderTpl .layui-layer-page .layui-layer-content {
        overflow: inherit;
    }
</style>

<!--冲红单 模板-->
<script type="text/html" id="RedOrderTpl">
    <div class="dialog">
        <div class="dialog-title red-title">
            冲红单
            <p>冲红单只对工单的金额款项做抵扣操作，冲红操作只影响冲红操作日期下的金额汇总，之前金额不对不做变更</p>
        </div>
        <div class="dialog-con">
            <div class="order-num clearfix">
                <div class="order-num-width">
                    <div class="form-label form-label-width">工单编号:</div>
                <#if orderInfo == null>
                ${formEntity.orderSn}
                <#else>
                ${orderInfo.orderSn}
                </#if>
                </div>
                <div class="order-num-date">开单日期：
                <#if orderInfo == null>
                ${formEntity.createTimeStr}
                <#else>
                ${orderInfo.createTimeStr}
                </#if>
                </div>
            </div>
            <div class="order-num clearfix">
                <div class="order-num-width">
                    <div class="form-label form-label-width">账单编号:</div>
                <#if debitBill>${debitBill.billSn}</#if>
                </div>
                <div class="order-num-date">账单日期：
                <#if debitBill>
                ${debitBill.billTime?string("yyyy-MM-dd HH:mm")}
                </#if>
                </div>
            </div>

        <#if redBill>
            <div class="col-4">
                <div class="form-label form-label-width">
                    冲红编号:
                </div>
                <div class="form-item red－item-width">
                    <div class="yqx-text">
                        ${redBill.billSn}
                    </div>
                </div>
            </div>
            <div class="col-4">
                <div class="form-label form-label-width">
                    冲红日期:
                </div>
                <div class="form-item red－item-width">
                    <div class="yqx-text">
                    ${redBill.billTime?string("yyyy-MM-dd HH:mm")}
                    </div>
                </div>
            </div>
            <div class="col-4">
                <div class="form-label form-label-width">
                    冲红应收金额:
                </div>
                <div class="form-item red－item-width">
                    <div class="yqx-text">
                        &yen;${redBill.receivableAmount}
                    </div>
                </div>
            </div>
            <div class="col-4">
                <div class="form-label form-label-width">
                    冲红实收金额:
                </div>
                <div class="form-item red－item-width">
                    <div class="yqx-text">
                        &yen;${redBill.paidAmount}
                    </div>
                </div>
            </div>
            <div class="col-4">
                <div class="form-label form-label-width">
                    冲红挂账金额:
                </div>
                <div class="form-item red－item-width">
                    <div class="yqx-text">
                        &yen;${redBill.signAmount}
                    </div>
                </div>
            </div>
            <div class="col-4">
                <div class="form-label form-label-width">
                    冲红坏账金额:
                </div>
                <div class="form-item red－item-width">
                    <div class="yqx-text">
                        &yen;${redBill.badAmount}
                    </div>
                </div>
            </div>
        <#else>
            <div class="money separate">
                <input type="hidden" id="billId" value="${debitBill.id}"/>
                <input type="hidden" id="orderId" value="${debitBill.relId}">
                <input type="hidden" id="receivableAmount" value="${debitBill.receivableAmount}"/>
                <input type="hidden" id="paidAmount" value="${debitBill.paidAmount}"/>
                <input type="hidden" id="signAmount" value="${debitBill.signAmount}"/>
                <input type="hidden" id="badAmount" value="${debitBill.badAmount}"/>
                <span>应收金额：${debitBill.receivableAmount}</span>
                <div class="form-label form-label-width">
                    冲红应收金额
                </div>
                <div class="form-item money-width">
                    <input type="text" id="chreceivableAmount" class="yqx-input yqx-input-small receivableAmount" data-v-type="required | price" maxlength="10"
                           value="${debitBill.receivableAmount}" placeholder="默认读取结算单应收金额" disabled>
                </div>
            </div>
            <div class="money">
                <span>实收金额： ${debitBill.paidAmount}</span>
                <div class="form-label">
                    冲红实收金额
                </div>
                <div class="form-item money-width">
                    <input type="text" id="chpaidAmount" class="yqx-input yqx-input-small" value="${debitBill.paidAmount}"
                           placeholder="默认读取结算单实收金额" disabled>
                </div>
            </div>
        </#if>

            <div class="attention"><img src="${BASE_PATH}/static/img/page/settlement/debit/tip.png"/>
                注意事项：应收金额如部分冲红款项线上不做任何回退操作，请门店私下处理；应收金额全额冲红，则系统自动返还账户的券、会员卡余额、使用其他付款方式（现金、支付宝、微信支付等）支付的款项请门店自行私下处理
            </div>
           <#if redBill==null>
               <div class="dialog-btn">
                   <button class="yqx-btn yqx-btn-3 yqx-btn-small js-redorder-save">确认</button>
                   <button class="yqx-btn yqx-btn-1 yqx-btn-small js-cancel">取消</button>
               </div>
           </#if>
        </div>
    </div>
</script>


<script>
    $(function () {

        seajs.use([
            'dialog',
            'art',
            'select',
            'check'
        ], function (dg, at, st, ck) {
            ck.init();

            //冲红单
            var redOrder = null;
            $(document).on('click', '.js-red-order', function () {
                var html = at('RedOrderTpl', {});
                redOrder = dg.open({
                    area: ['750px', 'auto'],
                    content: html
                })
            });

            // 计算冲红实收金额
//            $(document).on('blur', '.receivableAmount', function () {
//
//                // 应收总金额
//                var receivableAmount = $('#receivableAmount').val();
//                // 挂账金额
//                var signAmount = $('#signAmount').val();
//                // 坏账金额
//                var badAmount = $('#badAmount').val();
//                // 冲红应收
//                var chreceivableAmount = $('#chreceivableAmount').val();
//                // 冲红实收
//                var chpaidAmount = $('#chpaidAmount').val();
//
//                if (Number(chreceivableAmount) > Number(receivableAmount)) {
//                    dg.fail("冲红应收金额不能大于应收金额,请重新输入");
//                    $('#chreceivableAmount').val(0);
//                    return false;
//                }
//
//                // 全额冲红
//                if (Number(chreceivableAmount) == Number(receivableAmount)){
//                    $('#chpaidAmount').val($('#paidAmount').val());
//                } else {
//                    // 部分冲红 (冲红金额-挂账)
//                    // 可冲红金额
//                    var mayChonghongAmount = Number(receivableAmount) - Number(badAmount);
//                    if (Number(chreceivableAmount) > Number(mayChonghongAmount)) {
//                        dg.fail("冲红应收金额不能大于(应收金额-坏账金额),请重新输入");
//                        return false;
//                    }
//
//                    var chpaidAmountTemp = Number(chreceivableAmount) - Number(signAmount);
//                    if (chpaidAmountTemp > 0) {
//                        chpaidAmountTemp = chpaidAmountTemp.toFixed(2);
//                        $('#chpaidAmount').val(chpaidAmountTemp);
//                    } else {
//                        $('#chpaidAmount').val(0);
//                    }
//                }
//            });

            //取消按钮
            $(document).on('click', '.js-cancel', function () {
                dg.close(redOrder);
            });

            //提交按钮
            $(document).on('click', '.js-redorder-save', function () {
                // 数据校验
                var result = ck.check();
                if (!result) {
                    return;
                }
                var chreceivableAmount = Number($('#chreceivableAmount').val());//冲红应收金额
                if (chreceivableAmount <= 0) {
                    dg.warn("冲红应收金额必须大于0");
                    return;
                }

                var orderId = $('#orderId').val();
                var billId = $('#billId').val();
                $.ajax({
                    type: 'GET',
                    dataType: 'json',
                    data: {
                        orderId: orderId,
                        billId: billId,
                        receivableAmount: chreceivableAmount
                    },
                    url: BASE_PATH + '/shop/settlement/chonghong',
                    success: function (result) {
                        if (result.success) {
                            dg.success("操作成功");
                            dg.close(redOrder);
                            window.location.reload();
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                })
            })
        })
    });
</script>
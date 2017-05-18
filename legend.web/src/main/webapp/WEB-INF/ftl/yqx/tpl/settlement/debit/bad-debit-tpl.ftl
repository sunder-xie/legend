<!--坏帐弹窗-->
<style>
    .bad-debit .debit-info {
        font-size: 14px;
        font-weight: bold;
    }

    .bad-debit .debit-info .money-font {
        font-size: 18px;
        font-weight: 900;
    }

    .dialog-text {
        line-height: 30px;
        text-align: center;
    }

    .bad-btn-box {
        padding: 15px 10px;
        margin-top: 10px;
        border-top: 1px solid #d2d2d2;
    }

    .form-text-width {
        width: 290px;
    }
</style>
<div class="hide">
    <input type="hidden" class="js-signAmount" value="<#if debitBill>${debitBill.signAmount}<#else>0</#if>"/>
    <input type="hidden" class="js-redSignAmount" value="<#if redBill>${redBill.signAmount}<#else>0</#if>"/>
<#if formEntity>
    <input type="hidden" class="js-badOrderId" value="${formEntity.orderId}"/>
<#elseif orderInfo>
    <input type="hidden" class="js-badOrderId" value="${orderInfo.id}"/>
</#if>
</div>
<script type="text/html" id="badDebitTpl">
    <div class="dialog bad-debit" data-tpl-ref="bad-deebit-tpl">
        <div class="dialog-title">坏账处理</div>
        <div class="dialog-con">
            <div class="dialog-text debit-info">您确定要处理坏账金额：&yen;<span class="money-font" id="badAmount"></span>元？</div>
            <div class="form-label">
                备注
            </div>
            <div class="form-item form-text-width">
                <input type="text" id="badRemark" class="yqx-input" value="" maxlength="200" placeholder="请填写备注">
            </div>
            <div class="dialog-text"><span>收银人员：${SESSION_USER_NAME}</span>
                <span>收款日期：${.now?string("yyyy-MM-dd HH:mm")}</span>
            </div>
            <div class="bad-btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-bad-confirm">确认</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-bad-cancel">取消</button>
            </div>
        </div>
    </div>
</script>

<script>
    seajs.use([
        'dialog',
        'art'
    ],function(dg,at){
        var $doc = $(document);
        // 坏账
        var badDebit = null;
        $doc.on('click', '.js-bad-bill', function () {
            // 挂账
            var badAmount = Number($('.js-signAmount').val());
            // 冲红挂账
            var redSignAmount = Number($('js-redSignAmount').val());
            if (redSignAmount < 0) {
                badAmount = badAmount + redSignAmount;
                if (badAmount <= 0) {
                    dg.warn("(挂账金额 + 冲红挂账金额)小于等于0, 无需进行坏账处理");
                    return;
                }
            }
            if (badAmount <= 0) {
                dg.warn("挂账金额小于等于0, 无需进行坏账处理");
                return;
            }

            var html = at('badDebitTpl', {});
            badDebit = dg.open({
                area: ['400px', 'auto'],
                content: html
            });
            $('#badAmount').text(badAmount);
        });

        // 坏账取消
        $doc.on('click', '.js-bad-cancel', function () {
            dg.close(badDebit);
        });

        // 坏账确认
        $doc.on('click', '.js-bad-confirm', function () {
            // 工单id
            var orderId = $.trim($(".js-badOrderId").val());
            if(orderId == ''){
                dg.warn("工单不存在");
                return;
            }
            var badAmount = Number($('#badAmount').text());
            if (badAmount <= 0) {
                dg.warn("坏账金额不能小于等于0");
                return;
            }

            var orderIds = [];
            orderIds.push(orderId);

            $.ajax({
                url: BASE_PATH + '/shop/settlement/debit/bad-bill',
                type: 'post',
                data: {"orderIds": orderIds.join(','), "remark": $.trim($('#badRemark').val())},
                success: function (result) {
                    if (result.success) {
                        dg.success("操作成功", function () {
                            window.location.href = BASE_PATH + "/shop/settlement/debit/order-detail?orderId=" + orderId + "&refer=order-debit";
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
            dg.close(badDebit)
        });
    });
</script>
/**
 * zmx  2016-06-03
 * 批量确认收款 页面
 */

$(function(){
    var $doc = $(document);

    seajs.use('dialog', function(dg) {
        dg.titleInit();
    });

    // 应收
    var totalReceivableAmount = Number($('input[name="totalReceivableAmount"]').val());
    // 实收
    var totalPaidAmount = Number($('input[name="totalPaidAmount"]').val());
    // 挂账
    var totalSignAmount = Number($('input[name="totalSignAmount"]').val());

    // 冲红应收
    var totalRedReceivableAmount = Number($('input[name="totalRedReceivableAmount"]').val());
    // 冲红实收
    var totalRedPaidAmount = Number($('input[name="totalRedPaidAmount"]').val());
    // 冲红挂账
    var totalRedSignAmount = Number($('input[name="totalRedSignAmount"]').val());

    var $newTotalPaidAmount = $('#newTotalPaidAmount');
    var $newTotalSignAmount = $('#newTotalSignAmount');

    //表格展开收缩
    $doc.on('click','.js-tableShow',function(){
        var btnText = $(this).find('span').text();
        $(this).next('.show-table').slideToggle(500);
        if(btnText == '展开'){
            $(this).find('span').text('收起');
            $(this).find('i').removeClass('icon-angle-down').addClass('icon-angle-up');
        }else{
            $(this).find('span').text('展开');
            $(this).find('i').removeClass('icon-angle-up').addClass('icon-angle-down');
        }
    });


    //返回按钮
    $doc.on('click','.js-goback',function(){
        util.goBack();
    });

    //批量收款
    seajs.use([
        'select',
        'dialog',
        'check',
        'art'
    ],function(st,dg,ck,at){
        ck.init();

        //收款方式下拉列表
        st.init({
            dom: '.js-method',
            url: BASE_PATH + '/shop/payment/get-order-payment',
            showKey: "id",
            showValue: "name"
        });

        // 收款金额
        $doc.on('input', '.js-pay', function () {
            var val = $(this).val();
            val = checkFloat(val, 2);
            if(val == '.') {
                val = 0;
            }
            $(this).val(val);
            calculate($(this), val);
        });

        // 计算金额
        function calculate(obj, payAmount) {
            if(totalRedSignAmount < 0 && (totalSignAmount + totalRedSignAmount - payAmount) < 0) {
                dg.warn("收款金额不能大于(挂账金额 + 冲红挂账金额)");
                obj.val(0);
                $newTotalPaidAmount.text((totalPaidAmount).toFixed(2));
                $newTotalSignAmount.text((totalSignAmount).toFixed(2));
                return;
            }

            $newTotalPaidAmount.text((totalPaidAmount + Number(payAmount)).toFixed(2));
            $newTotalSignAmount.text((totalSignAmount - payAmount).toFixed(2));
        }


        //确认收款
        $doc.on('click', '.js-comfirm', function () {
            // 数据校验
            var result = ck.check();
            if (!result) {
                return;
            }

            var newTotalSignAmount = Number($.trim($newTotalSignAmount.text()));
            if (newTotalSignAmount < 0) {
                dg.warn("'实收金额'大于'应收金额',请重新收款");
                return;
            }

            var data = {};
            var orderIdList = [];
            var orderSnList = [];
            $(".js-order-id").each(function () {
                var orderId = $(this).data("id");
                var orderSn = $.trim($(".js-order-sn", $(this)).text());
                orderIdList.push(orderId);
                orderSnList.push(orderSn);
            });
            data.orderIdList = orderIdList;

            var remark = $.trim($('#remark').val());
            if (remark) {
                remark = remark + ',';
            }
            remark = remark + "批量收款工单号:" + orderSnList.join(',');
            if(remark.length > 1000){
                dg.warn("备注太长，请分批结算");
                return;
            }
            data.remark = remark;

            var paymentId = $("input[name='paymentId']").val();
            var paymentName = $("input[name='paymentName']").val();
            var payAmount = Number($("input[name='payAmount']").val());
            if (!paymentId) {
                dg.warn("请选择收款方式");
                return;
            }
            if (payAmount <= 0) {
                dg.warn("收款金额必须大于0");
                return;
            }
            data.paymentId = paymentId;
            data.paymentName = paymentName;
            data.payAmount = payAmount;

            if (totalRedSignAmount < 0) {
                if (payAmount > (totalSignAmount + totalRedSignAmount)) {
                    dg.warn("收款总金额不能大于(挂账金额 + 冲红挂账金额)");
                    return;
                }
            } else {
                if (payAmount > totalSignAmount) {
                    dg.warn("收款总金额不能大于上次挂账金额");
                    return;
                }
            }

            $.ajax({
                url: BASE_PATH + '/shop/settlement/debit/batch-debit-post',
                type: 'post',
                contentType: 'application/json',
                data: JSON.stringify(data),
                dataType: 'json',
                success: function (result) {
                    if (result.success) {
                        dg.success("操作成功", function () {
                            window.location.href = BASE_PATH + "/shop/settlement/debit/batch-list";
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        });

        // 坏账
        var badDebit = null;
        $doc.on('click', '.js-bad-bill', function () {
            var badAmount = totalSignAmount;
            if (totalRedSignAmount < 0) {
                badAmount = badAmount + totalRedSignAmount;
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
            dg.close(badDebit)
        });

        // 坏账确认
        $doc.on('click', '.js-bad-confirm', function () {
            var badAmount = Number($('#badAmount').text());
            if (badAmount <= 0) {
                dg.warn("坏账金额不能小于等于0");
                return;
            }

            var orderIdList = [];
            var orderSnList = [];
            $(".js-order-id").each(function () {
                var orderId = $(this).data("id");
                var orderSn = $.trim($(".js-order-sn", $(this)).text());
                orderIdList.push(orderId);
                orderSnList.push(orderSn);
            });

            var remark = $.trim($('#badRemark').val());
            if (remark) {
                remark = remark + ',';
            }
            remark = remark + "批量坏账工单号:" + orderSnList.join(',');

            $.ajax({
                url: BASE_PATH + '/shop/settlement/debit/bad-bill',
                type: 'post',
                data: {"orderIds": orderIdList.join(','), "remark": remark},
                success: function (result) {
                    if (result.success) {
                        dg.success("操作成功", function () {
                            window.location.href = BASE_PATH + "/shop/settlement/debit/batch-list";
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
            dg.close(badDebit)
        });
    });
});
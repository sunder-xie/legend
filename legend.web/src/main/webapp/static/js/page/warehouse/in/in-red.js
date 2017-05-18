$(function () {
    seajs.use(['art', 'dialog', 'formData', 'select', 'downlist', "check", "ajax", "date"],
        function (at, dg, fd, st, dl, ck, aj, date) {
            var $doc = $(document);

            ck.regList.negativeNumber = function (val) {
                var msg = [
                    '',
                    '必须为负数或者0'
                ];

                if(!$.isNumeric(val) || val > 0) {
                    return {msg: msg[1], result: false};
                } else {
                    return {msg: msg[0], result: true};
                }
            };
            ck.regList.negPrice = function(val) {
                $(this).val( Number(val).toFixed(2) );
                // 触发本身的 input 事件
                // 更新合计等
                $(this).trigger('input');

                return {msg: '', result: true};
            };
            ck.init();
            dg.titleInit();
            //供应商
            st.init({
                dom: '.js-supplier',
                url: BASE_PATH + '/shop/setting/supplier/get_supplier_list',
                showValue: "supplierName",
                showKey: "id",
                canInput: true,
                callback: function (showKey, value, data) {
                    var index = $(this).data('index');
                    if (data) {
                        $('input[name=contact]').val(data[index].contact);
                        $('input[name=contactMobile]').val(data[index].mobile);
                    }
                }
            });

            //付款方式
            st.init({
                dom: '.js-pay-method',
                url: BASE_PATH + '/shop/warehouse/get_pay_method',
                showKey: "code",
                showValue: "name"
            });
            //发票类型
            st.init({
                dom: '.js-invoice-type',
                url: BASE_PATH + '/shop/setting/supplier/get_invoice_type',
                selectedKey: '0',
                showKey: "code",
                showValue: "name"
            });
            //采购人
            st.init({
                dom: ".js-agent",
                url: BASE_PATH + '/shop/manager/get_manager',
                showKey: "id",
                showValue: "name"
            });
            date.datePicker('.js-in-date', {
                maxDate: '%y-%M-%d',
                dateFmt: 'yyyy-MM-dd HH:mm'
            });

            // delete
            $('.yqx-table').on('click', '.js-del-btn', function () {
                var $tr = $(this).parent().parent();

                if ($tr.parent().find('.goods-datatr').length == 1) {
                    dg.warn('退货配件不能为空');
                    return;
                }
                $tr.remove();

                calMoney();
            });

            // 配件价格改变
            $doc.on('input', '.js-goods-price', function () {
                var val = $(this).val();
                var $tr = $(this).parents('tr');
                var servicePrice = +$tr.find('.js-goods-num').val();
                var t = servicePrice.Cheng(+val);

                $tr.find('i.js-goods-amount').text(t || 0)
                    .end().find('input.js-goods-amount').val(t || 0);
                calMoney();
            });
            // 配件个数输入
            $doc.on('input', '.js-goods-num', function () {
                var val = +$(this).val();
                var $tr = $(this).parents('tr');
                var servicePrice = +$tr.find('.js-goods-price').val();
                var t = servicePrice.Cheng(+val);

                t = t || 0;
                $tr.find('i.js-goods-amount').text(t)
                    .end().find('input.js-goods-amount').val(t);
                calMoney();
            });

            function calMoney() {
                var t = 0;
                $('.list-box .yqx-table').find('tr')
                    .each(function () {
                        t = t.Jia(+$(this).find('input.js-goods-amount').val() || 0);
                    });

                // NaN
                if(t !== t) {
                    t = 0;
                }
                $('.js-goods-total').val(t);

                $('.js-money-plus').eq(0).trigger('input');
            }

            $('.js-money-plus').on('input', function () {
                var val2 = +$(this).val(),
                    val3 = +$(this).parent().parent().siblings('.form-item')
                        .find('.js-money-plus').val(), val = +$('.js-goods-total').val() || 0;
                var t = val.Jia(val2 || 0).Jia(val3 || 0);
                if(t !== t) {
                    t = 0;
                }

                $('.money-total .money-text').text(t.toFixed(2));
                $('input[name="totalAmount"]').val(t);
            });

            //红字入库
            $doc.on('click', '.js-stock-refund', function () {
                var url = "/shop/warehouse/in/in-edit/red/save";
                var info = "退货成功";
                saveInfo(url, info);
            });


            function saveInfo(url, info) {
                var msg = checkGoods();
                if (!ck.check()) {
                    return;
                }

                if (msg.num) {
                    dg.warn(msg.num);
                    return;
                }
                submitForm(url, info);
            }

            function submitForm(url, info) {
                var warehouseIn = fd.get('.main', true),
                    warehouseInDetail = [];
                $.extend(warehouseIn, fd.get('.mark-box', true));

                $('.goods-table .goods-datatr').each(function () {
                    warehouseInDetail.push(fd.get($(this), true));
                });
                $.ajax({
                    url: BASE_PATH + url,
                    dataType: 'json',
                    data: {
                        warehouseIn: JSON.stringify(warehouseIn),
                        warehouseInDetails: JSON.stringify(warehouseInDetail)
                    },
                    type: 'POST',
                    success: function (data) {
                        if (data.success) {
                            dg.success(info, function () {
                                window.location.href = BASE_PATH + "/shop/warehouse/in/in-detail?id=" + data.data + "&refer=in-edit";
                            });
                        } else {
                            dg.fail(data.errorMsg);
                        }
                    }
                });
            }

            function checkGoods() {
                var str, t = [];

                $('.yqx-table .goods-datatr').toArray().forEach(function (e, i) {
                    // 退货数量是负的
                    if ($(e).find('input[name=goodsCount]').val() < (-1 * $(e).find('.js-goods-real').val() )) {
                        t.push(i + 1);
                    }
                });

                if (t.length)
                    str = '第' + t.join(',') + '行配件 退货数量不能大于可退数量,请修改';
                return {num: str};
            }
        });

    $('.js-back').on('click', util.goBack);
});
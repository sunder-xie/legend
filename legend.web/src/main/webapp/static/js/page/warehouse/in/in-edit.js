$(function () {

    var addSupplier = new addNewSupplier({
        dom: '.js-add-supplier',
        callback:function(result){
            $('.js-supplier').val(result.data.supplierName);
            $('input[name=supplierName]').val(result.data.supplierName);
            $('input[name=supplierId]').val(result.data.id);
            $('input[name=contact]').val(result.data.contact);
            $('input[name=contactMobile]').val(result.data.mobile);
            $('input[name=payMode]').val(result.data.paymentMode);
            $('input[name=paymentMode]').val(result.data.paymentMode);
        }
    });
    seajs.use(['art', 'dialog', 'formData', 'select', 'downlist', "check", "ajax", "date"],
        function (at, dg, fd, st, dl, ck, aj, date) {
            var $doc = $(document);

            ck.init();
            dg.titleInit();
            //供应商
            st.init({
                dom: '.js-supplier',
                url: BASE_PATH + '/shop/setting/supplier/get_supplier_list',
                showValue: "supplierName",
                showKey: "id",
                canInput: true,
                callback: function (showKey, showValue, data) {
                    $('input[name=supplierId]').val(showKey);
                    $('input[name=supplierName]').val(showValue);
                    var index = $(this).data('index');
                    if (data) {
                        $('input[name=contact]').val(data[index].contact);
                        $('input[name=contactMobile]').val(data[index].mobile);
                        $('input[name=payMode]').val(data[index].paymentMode);
                        $('input[name=paymentMode]').val(data[index].paymentMode);
                    }
                }
            });

            //付款方式
            st.init({
                dom: '.js-pay-method',
                url: BASE_PATH + '/shop/setting/supplier/get_pay_method',
                showKey: "code",
                showValue: "name",
                callback: function (showKey, showValue) {
                    $('input[name=paymentMode]').val(showValue);
                }
            });
            //发票类型
            st.init({
                dom: '.js-invoice-type',
                url: BASE_PATH + '/shop/setting/supplier/get_invoice_type',
                showKey: "code",
                showValue: "name",
                selectedKey: '0',
                callback: function (showKey, showValue) {
                    $('input[name=invoiceType]').val(showKey);
                    $('input[name=invoiceTypeName]').val(showValue);
                }
            });
            //采购人
            st.init({
                dom: ".js-agent",
                url: BASE_PATH + '/shop/manager/get_manager',
                showKey: "id",
                showValue: "name",
                callback: function (showKey, showValue) {
                    $('input[name=purchaseAgent]').val(showKey);
                    $('input[name=purchaseAgentName]').val(showValue);
                }

            });
            date.datePicker('.js-in-date', {
                maxDate: '%y-%M-%d',
                dateFmt: 'yyyy-MM-dd HH:mm'
            });

            // 配件 添加按钮
            addGoodsInit({
                dom: '.js-add-goods',
                callback: addGoods,
                tpl: 'add-goods-tpl2',
                notAllowRepeat: true,
                bodyTpl: 'add-goods2'
            });

            // 批量添加物料
            batchAddPart({
                dom: '.js-batch-add-goods',
                callback: addGoods
            });

            // 新增配件,需要引入模板 yqx/tpl/order/new-part-tpl.ftl
            newPart({
                dom: '.js-new-goods',
                callback: addGoods
            });

            // delete
            $('.yqx-table').on('click', '.js-del-btn', function () {
                var $tr = $(this).parent().parent();

                $tr.remove();

                calMoney();
            });

            // 配件价格改变
            $doc.on('input', '.js-goods-price', function () {
                var val = $(this).val();
                var $tr = $(this).parents('tr');
                var servicePrice = +$tr.find('.js-goods-num').val();
                var t = servicePrice.Cheng(+val);

                $tr.find('i.js-goods-amount').text((t || 0).toFixed(2))
                    .end().find('input.js-goods-amount').val((t || 0).toFixed(2));
                calMoney();
            });
            // 配件个数输入
            $doc.on('input', '.js-goods-num', function () {
                var val = +$(this).val();
                var $tr = $(this).parents('tr'),
                    stock = $tr.find('[name="stock"]').text();
                var servicePrice = +$tr.find('.js-goods-price').val();
                var t = servicePrice.Cheng(+val);

                $tr.find('i.js-goods-amount').text((t || 0).toFixed(2))
                    .end().find('input.js-goods-amount').val((t || 0).toFixed(2));
                calMoney();
            });

            function checkGoods() {
                var str, t = [], str_2, t_2 = [];

                $('.yqx-table .goods-datatr').toArray().forEach(function (e, i) {
                    if( $(e).find('input[name=goodsCount]').val() == 0 ) {
                        t.push(i+1);
                    }
                    if( $(e).find('input[name=purchasePrice]').val() == 0 ) {
                        t_2.push(i+1);
                    }
                });

                if(t.length)
                    str = '入库数量不能为0,第' + t.join(',') + '行配件 入库数量为0,请修改';
                if(t_2.length) {
                    str_2 = '第'+ t_2.join(',') + '行配件 入库单价为0,仍旧要提交么?'
                }
                return {num: str, price: str_2};
            }
            function addGoods(json) {
                var goodsHtml, arr;
                var goodsIds = [];

                $('.goods-datatr').find('input[name=goodsId]').each(function () {
                     goodsIds.push( $(this).val() );
                });

                if (json && json.success) {
                    arr = json.data.goodsList.map(function (e) {
                        if(goodsIds.indexOf(e.id + '') > -1) {
                           return {};
                        }
                    });
                }

                // 非ajax请求
                if (json && json.id) {
                    if(goodsIds.indexOf(json.id + '') > -1) {
                        dg.msg('该配件已存在');
                        return;
                    }
                    arr = [json];
                }

                // 批量添加物料或其他
                if (json && json.length) {
                    arr = json;
                }

                goodsHtml = $( at("goodsTpl", {json: arr}) );

                $('.goods-table').append(goodsHtml);

                calMoney();
            }

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
                $('.js-goods-total').val(t.toFixed(2));

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

                $('.money-total .money').text(t.toFixed(2));
                $('input[name="totalAmount"]').val(t.toFixed(2));

            });

            var url = "";
            var info = "";
            //蓝字入库
            $doc.on('click', '.js-stock', function () {
                url = "/shop/warehouse/in/in-edit/blue/save";
                info = "入库成功";
                saveInfo(url, info);
            });

            //保存草稿
            $doc.on('click', '.js-save-draft', function () {
                url = "/shop/warehouse/in/in-edit/draft/save";
                info = "保存成功";
                saveInfo(url, info);
            });

            //编辑草稿
            $doc.on('click', '.js-edit-draft', function () {
                url = "/shop/warehouse/in/in-edit/draft/update";
                info = "保存成功";
                saveInfo(url, info);
            });
            //转入库
            $doc.on('click', '.js-draft-stock', function () {
                url = "/shop/warehouse/in/in-edit/draft/stock";
                info = "入库成功";
                saveInfo(url, info);
            });

            function saveInfo(url, info) {
                var msg, re = false;

                if(!ck.check()) {
                    return;
                }

                msg = checkGoods();
                if(msg.num) {
                    dg.msg(msg.num, {icon: 0});
                    return;
                }
                if(msg.price) {
                    dg.confirm(msg.price, function () {
                        submitForm(url,info);
                    });
                } else {
                    submitForm(url,info);
                }
            }

            function submitForm(url,info) {
                var warehouseIn = fd.get('.main'),
                    warehouseInDetail = [];
                $.extend(warehouseIn, fd.get('.mark-box'));

                $('.goods-table .goods-datatr').each(function () {
                    warehouseInDetail.push(fd.get($(this)));
                });

                if(!warehouseInDetail.length) {
                    dg.warn('入库配件不能为空');
                    return;
                }
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
        });
    $('.js-back').on('click', util.goBack);
});
/**
 * Created by ende on 16/4/8.
 */
seajs.use(['art', 'dialog', 'formData', 'select', 'downlist', 'date', 'check', 'ajax'],
    function (at, dg, fd, st, dl, date, ck, ax) {


        // 结算时的转入库
        var inWarehouseGoods = (function () {
            var isSettle = false;

            return {
                setStatus: function (bool) {
                    isSettle = bool
                },
                fn: function () {
                    var data = [];
                    var _repeat = {}, _existed = {};

                    if (!isSettle) {
                        return null;
                    }

                    $('.goods-datatr').each(function () {
                        var $tr = $(this);
                        var id = $tr.find('input[name=goodsId]').val();
                        var goodsNumber = Number( $tr.find('input[name="goodsNumber"]').val() );
                        var stock = Number( $tr.find('input[name="stock"]').val() );

                        if (_repeat[id] === undefined) {
                            // 出库数量
                            _repeat[id] = goodsNumber;
                        } else {
                            // 剩余库存
                            stock -= _repeat[id];

                            // 总出库数量
                            _repeat[id] += goodsNumber;
                        }
                        if (goodsNumber > stock) {
                            if (_existed[id] === undefined) {
                                var t = {
                                    goodsId: id,
                                    goodsFormat: $tr.find('input[name="goodsFormat"]').val(),
                                    goodsName: $tr.find('input[name="goodsName"]').val(),
                                    goodsNumber: goodsNumber
                                };

                                // 保存索引
                                _existed[id] = data.length;

                                data.push(t);

                            }
                            data[ _existed[id] ].goodsNumber = _repeat[id];
                        }
                    });

                    isSettle = false;
                    return data;
                }
            };
        })();

        $(function () {
            var doc = $(document);

            ck.init();
            dg.titleInit();

            //错误信息停留时间
            var noTime = 4;

            // 维修工单，维修配件等 tab 切换
            $('.js-tabs').on('click', function () {
                var data = $(this).attr('data-class');

                $('.current-item').removeClass('current-item');
                $(this).addClass('current-item');

                $('.margin-body').each(function () {
                    $(this).hide();
                });
                $('.' + data).show();

                if (data === 'repair-goods') {
                    $(this).find('.tip').hide();
                    $('.tools-bar').addClass('tools-icons');
                } else {
                    $('.tools-bar').removeClass('tools-icons');
                }

            });

            // 更多车辆信息
            $('.js-speedily-to-more').on('click', function () {
                var customerCarId = $("input[name='orderInfo.customerCarId']").val();
                if (customerCarId) {
                    window.open(BASE_PATH + '/shop/customer/car-detail?refer=speedily&id=' + customerCarId);
                } else {
                    dg.warn('请输入有记录的车牌号')
                }
            });


            // delete
            $('.yqx-table').on('click', '.js-trdel', function () {
                var $tr = $(this).parent().parent();
                var trId = $tr.data('id');
                if($tr.find('.input-suiteNum').eq(0).val() > 0) {
                    dg.warn("此服务套餐包含配件物料，请自行删除！");
                }
                $tr.remove();

                // 对应的热门服务取消高亮
                $('.i-service.i-selected').each(function () {
                    var id = $(this).data('id');
                    if(id === trId) {
                        $(this).removeClass('i-selected');
                    }
                });

                calMoney('service', 'goods');
                calMoney('goods', 'service');
            });

            // 配件 添加按钮
            addGoodsInit({
                dom: '.js-add-goods',
                callback: addGoods
            });
            doc.on('blur','input[name="goodsPrice"]',function(){
                var price = $(this).val(),
                    currentPrice= $(this).parents(".goods-datatr"),
                    inventoryPrice = $('input[name="inventoryPrice"]', currentPrice).val();

                if( Number(price) < Number(inventoryPrice)){
                    dg.warn("售价不能小于成本价")
                };

            });


            // 工时输入
            doc.on('input', '.js-service-hour', function () {
                var val = $(this).val();

                var $tr = $(this).parent().parent().parent();

                val = checkFloat(val, 1);
                if(val !== '.')
                    $(this).val(val);
                else
                    val = 0;

                var servicePrice = +$tr.find('.js-service-price').val();

                $tr.find('.js-service-amount').val((servicePrice * +val).toFixed(2));
                calMoney('service', 'goods');
            });
            // 服务优惠输入
            doc.on('input', '.js-service-discount', function () {
                var val = $(this).val();

                val = checkFloat(val, 2);
                if(val !== '.')
                    $(this).val(val);
                else {
                    val = 0;
                }

                calMoney('service', 'goods');
            });

            $('.js-checkbox-all').on('click', function() {
                if(!$(this).prop('checked')) {
                    $('.repair-goods').find('.js-repair-checkbox').each(function() {
                        if(!$(this).attr('disabled') && !$(this).attr('readonly')) {
                            $(this).removeAttr('checked');
                        }
                    })
                } else {
                    $('.repair-goods').find('.js-repair-checkbox').each(function () {
                        if (!$(this).attr('disabled') && !$(this).attr('readonly')) {
                            $(this).prop('checked', 'checked');
                        }
                    })
                }
            });

            // 工时费改变
            doc.on('input', '.js-service-price', function () {
                var val = $(this).val();
                var $tr = $(this).parent().parent().parent();

                val = checkFloat(val, 2);
                if(val !== '.')
                    $(this).val(val);
                else {
                    val = 0;
                }
                var servicePrice = +$tr.find('.js-service-hour').val();

                $tr.find('.js-service-amount').val((servicePrice * +val).toFixed(2));
                calMoney('service', 'goods');
            });

            // 配件价格改变
            doc.on('input', '.js-goods-price', function () {
                var val = $(this).val();
                var $tr = $(this).parent().parent().parent();
                var servicePrice = +$tr.find('.js-goods-num').val();

                val = checkFloat(val, 2);
                if(val !== '.')
                    $(this).val(val);
                else
                    val = 0;

                $tr.find('.js-goods-amount').val((servicePrice * +val).toFixed(2));
                calMoney('goods', 'service');
            });
            // 配件个数输入
            doc.on('input', '.js-goods-num', function () {
                var val = +$(this).val();
                var $tr = $(this).parent().parent().parent(), stock = $tr.find('input[name=stock]').val();
                var servicePrice = +$tr.find('.js-goods-price').val();

                $tr.find('.js-goods-amount').val((servicePrice * +val).toFixed(2));
                calMoney('goods', 'service');

                var checkbox =  $tr.find('input[type=checkbox]');
                if(val > +stock || !stock) {
                    checkbox.removeAttr('disabled');
                    checkbox.attr('checked', true);
                } else {
                    checkbox.removeAttr('checked');
                    checkbox.attr('disabled', true);
                }
            });
            // 配件优惠输入
            doc.on('input', '.js-goods-discount', function () {
                var val = $(this).val();

                val = checkFloat(val, 2);
                if(val !== '.')
                    $(this).val(val);
                else {
                    val = 0;
                }

                calMoney('goods', 'service');
            });

            function calMoney(type, otherType) {
                var sum = 0;
                var amount, discount;
                var $tr, otherSum = +$('#' + otherType + 'MoneySum').text();

                $('.' + type + '-datatr').each(function () {
                    $tr = $(this);

                    amount = +$tr.find('.js-' + type + '-amount').val();
                    discount = +$tr.find('.js-' + type + '-discount').val();

                    discount = discount < 0 || !discount ? 0 : discount;

                    sum = sum.Jia(amount - discount);
                });
                if (sum < 0 || !sum || sum !== sum) {
                    sum = 0;
                }

                $('#' + type + 'MoneySum').text(sum);
                $('#allMoneySum').text((sum + otherSum).toFixed(2));
            }

            // 开单日期
            date.datePicker('.js-date', {
                maxDate: '%y-%M-%d',
                dateFmt: 'yyyy-MM-dd HH:mm'
            });
            //下次保养日期
            date.datePicker('.js-keepup-time-date', {
                minDate: '%y-%M-%d',
                dateFmt: 'yyyy-MM-dd'
            });

            // 选择推荐服务
            doc.on('click', '.js-select-service', function () {
                var html, selected = $(this).hasClass('i-selected');
                if (selected) {
                    $(this).removeClass('i-selected');
                    $('.service-datatr[data-id=' + $(this).attr('data-id') + ']').remove();
                    return false;
                }

                $(this).addClass('i-selected');
                $.ajax({
                    url: BASE_PATH + '/shop/shop_service_info/getShopService',
                    data: {
                        serviceId: $(this).attr('data-id')
                    },
                    success: serviceCallback
                });
            });

            //计算下次保养里程
            var getUpkeepMileage = function () {
                var carId = $("input[name='orderInfo.customerCarId']").val();
                var carModelId = $("input[name='orderInfo.carModelsId']").val();
                var mileage = $("input[name='orderInfo.mileage']").val();

                if (!$.isNumeric(carModelId) || !$.isNumeric(mileage) || mileage < 0) {
                    return;
                }
                $.ajax({
                    url: BASE_PATH + "/shop/order/get_upkeep_mileage",
                    data: {
                        carId: carId,
                        carModelId: carModelId,
                        mileage: mileage
                    },
                    success: function (json) {
                        if (json != null && json != undefined && json.success) {
                            $("input[name='orderInfo.upkeepMileage']").val(json.data);
                        } else {
                            $("input[name='orderInfo.upkeepMileage']").val(null);
                        }
                    }
                });
            };
            $("input[name='orderInfo.mileage']").change(getUpkeepMileage);

            //车牌值改变时清空其它联动框的值。
            doc.on('change', 'input[name="orderInfo.carLicense"]', function () {
                var arr = [
                    'orderInfo.customerCarId',
                    'carModeBak',
                    'orderInfo.carBrandId',
                    'orderInfo.carBrand',
                    'orderInfo.carSeriesId',
                    'orderInfo.carSeries',
                    'orderInfo.carModelsId',
                    'orderInfo.carModels',

                    'orderInfo.importInfo',
                    'orderInfo.contactName',
                    'orderInfo.contactMobile',
                    'orderInfo.mileage',
                    'orderInfo.upkeepMileage',
                    'customerCar.keepupTimeStr',

                    'orderInfo.company'
                ];
                for (var i = 0; i < arr.length; i++) {
                    $('input[name="' + arr[i] + '"]').val('');
                }
            });

            //初始化车牌下拉框
            dl.init({
                url: BASE_PATH + '/shop/customer/search/mobile',
                searchKey: 'com_license',
                tplId: 'carLicenceTpl',
                showKey: 'license',
                dom: 'input[name="orderInfo.carLicense"]',
                hasInput: false,
                callbackFn: function (obj, item) {
                    $.ajax({
                        url: BASE_PATH + '/shop/customer_car/get_car_by_license',
                        data: {
                            carLicense: item["license"]
                        },
                        success: function (json) {
                            if (json.success) {
                                //填充customerCarId
                                $("input[name='orderInfo.customerCarId']").val(json.data.id);
                                $("input[name='carModeBak']").val(json.data.carInfo);
                                $("input[name='orderInfo.carBrandId']").val(json.data.carBrandId);
                                $("input[name='orderInfo.carBrand']").val(json.data.carBrand);
                                $("input[name='orderInfo.carSeriesId']").val(json.data.carSeriesId);
                                $("input[name='orderInfo.carSeries']").val(json.data.carSeries);
                                $("input[name='orderInfo.carModelsId']").val(json.data.carModelId);
                                $("input[name='orderInfo.carModels']").val(json.data.carModel);

                                $("input[name='orderInfo.importInfo']").val(json.data.importInfo);
                                $("input[name='orderInfo.contactName']").val(json.data.contact);
                                $("input[name='orderInfo.contactMobile']").val(json.data.contactMobile);

                                // 行驶里程
                                var mileage = json.data.mileage;
                                $("input[name='orderInfo.mileage']").val(json.data.mileage);

                                // 下次保养里程
                                var upkeepMileage = json.data.upkeepMileage;
                                $("input[name='orderInfo.upkeepMileage']").val(upkeepMileage);

                                // 下次保养时间
                                $("input[name='customerCar.keepupTimeStr']").val(json.data.keepupTimeStr);

                                // VIN码
                                $("input[name='orderInfo.vin']").val(json.data.vin);

                                // 客户单位
                                $("input[name='orderInfo.company']").val(json.data.company);

                                // 如果下次保养里程大于行驶里程,则不获取推荐的下次保养里程
                                if(!(mileage && upkeepMileage && (upkeepMileage - mileage > 0))) {
                                    //计算下次保养里程
                                    getUpkeepMileage();
                                }

                            }
                        }
                    });
                }
            });

            // 下次保养里程校验
            doc.on("blur", "input[name='orderInfo.upkeepMileage']", function () {
                var mileage = $("input[name='orderInfo.mileage']").val();
                var upkeepMileage = $("input[name='orderInfo.upkeepMileage']").val();
                if (!$.isNumeric(mileage) || mileage < 0 || !$.isNumeric(upkeepMileage) || upkeepMileage < 0 || parseInt(mileage) < parseInt(upkeepMileage)) {
                    return;
                }
                dg.warn("下次保养里程不能小于或等于行驶里程");
                $("input[name='orderInfo.upkeepMileage']").val(null);
            });

            //服务顾问select下拉列表初始化。
            st.init({
                dom: ".js-speedily-receiver",
                url: BASE_PATH + '/shop/manager/get_manager',
                showKey: "id",
                showValue: "name"
            });

            //销售员select下拉列表初始化。
            st.init({
                dom: ".js-speedily-sale",
                url: BASE_PATH + '/shop/manager/get_manager',
                showKey: "id",
                showValue: "name"
            });

            //初始化客户单位下拉框
            dl.init({
                url: BASE_PATH + '/shop/customer/search/company',
                showKey: 'company',
                searchKey: 'company',
                tplCols: {'company': '客户单位'},
                dom: 'input[name="orderInfo.company"]',
                notClearInput: true,
                hasInput: false
            });

            // 批量添加物料
            batchAddPart({
                dom: '.js-batch-add-part-btn',
                callback: addGoods
            });

            // 新增配件,需要引入模板 yqx/tpl/order/new-part-tpl.ftl
            newPart({
                dom: '.js-new-part-btn',
                callback: addGoods
            });

            // 转入库
            inWarehouse({
                dom: '.js-in-warehouse',
                goodsData: inWarehouseGoods.fn,
                list: '.repair-goods .yqx-table .goods-datatr'
            });

            // 选择服务
            getService({
                dom: '.js-get-service',
                callback: serviceCallback
            });

            //洗车工下拉列表初始化。
            workerInit({
                dom: '.js-speedily-worker',
                hiddenDom: 'input[name="workerIds"]'
            });

            // 新增服务
            addServiceInit({
                dom: '#serviceAddBtn',
                callback: serviceCallback
            });

            doc.on('click','.js-in-warehouse',function(){
                $.ajax({
                    url:BASE_PATH + '/shop/user/operate/count?refer=speedily-warehouseIn'
                });
            });
            doc.on('click','.js-batch-add-part-btn',function(){
                $.ajax({
                    url:BASE_PATH + '/shop/user/operate/count?refer=speedily-batchAddGoods'
                });
            });

            doc.on('click','.js-tools-bar-new',function(){
                $.ajax({
                    url:BASE_PATH + '/shop/user/operate/count?refer=speedily-addService'
                });
            });
            doc.on('click','.js-new-part-btn',function(){
                $.ajax({
                    url:BASE_PATH + '/shop/user/operate/count?refer=speedily-addGoods'
                });
            });

            function addGoods(json) {
                var goodsHtml = '', arr;

                if (json && json.success) {
                    arr = json.data.goodsList;
                }

                // 非ajax请求
                if (json && json.id ) {
                    arr = [json];
                }

                // 批量添加物料或其他
                if(json && json.length) {
                    arr = json;
                }

                goodsHtml = at("goodsTpl", {json: arr});
                $('#goods').append(goodsHtml);

                calMoney('goods', 'service');
                $('.js-goods-num').trigger('input');

            }

            // 开单和结算公用方法
            function speedilySave(fn) {
                if(!ck.check()){//作用域为整个页面的验证
                    return;
                }
                // 获取整个页面的数据
                var data = fd.get(".order-form", true);
                // 获取服务列表数据
                data.orderServiceJson = (function () {
                    var arr = [];
                    $(".service-datatr").each(function () {
                        var tr = $(this);
                        var temp = fd.get(tr);

                        // IF discount =null THEN 0
                        temp.discount = temp.discount || 0;

                        // IF servicePrice =null THEN 0
                        temp.servicePrice = temp.servicePrice || 0;
                        // IF serviceHour THEN 1
                        temp.serviceHour = temp.serviceHour || 1;

                        temp.serviceNote = temp.serviceNote || "";

                        if (!temp.workerIds) {
                            delete temp.workerIds;
                        }

                        arr.push(temp);
                    });
                    return JSON.stringify(arr);
                })();

                // 获取配件列表数据
                data.orderGoodJson = (function () {
                    var arr = [];
                    $(".goods-datatr").each(function () {
                        var temp = fd.get($(this));

                        // IF goodsPrice =null THEN 0
                        temp.goodsPrice = temp.goodsPrice || 0;

                        // IF goodsNumber =null THEN 1
                        temp.goodsNumber = temp.goodsPrice ? temp.goodsNumber : 1;

                        // IF discount =null THEN 0
                        temp.discount = temp.discount || 0;

                        temp.goodsNote = temp.goodsNote || "";

                        arr.push(temp);
                    });
                    return JSON.stringify(arr);
                })();

                //备注
                data['orderInfo.postscript'] = $.trim($("input[name='orderInfo.postscript']").val());

                //数据发送
                var orderId = $("input[name='orderInfo.id']").val();
                var url = BASE_PATH + "/shop/order/speedily/save";
                if (orderId > 0) {
                    url = BASE_PATH + "/shop/order/speedily/update";
                }

                $.ajax({
                    url: url,
                    data: data,
                    type: 'POST',
                    success: function (json) {
                        if (json.success) {
                            fn && fn(json);
                        } else {
                            dg.fail(json.errorMsg);
                        }
                    }
                });
            }

            // 获取服务的回调函数
            function serviceCallback(json) {
                // 一种是ajax的回调， 另一种是一般函数的回调(无json.data)
                if ((json && json.data && json.success) || (json && !json.data)) {
                    var data = json.data ? json.data : json

                    if (data.suiteNum > 0) {
                        $.ajax({
                            global: false,
                            url: BASE_PATH + "/shop/shop_service_info/getPackageByServiceId",
                            data: {
                                serviceId: data.id
                            },
                            success: function (packageJson) {
                                if (packageJson.success) {
                                    var html = at("serviceTpl", {json: packageJson.data.shopServiceInfoList, parentService: packageJson.data.shopServiceInfo});
                                    $('#service').append(html);
                                    var goodsHtml = at("goodsTpl", {json: packageJson.data.goodsList});
                                    $('#goods').append(goodsHtml);

                                    //有物料显示提示框
                                    if (packageJson.data.goodsList && packageJson.data.goodsList.length) {
                                        $(".tab-item .tip").show();
                                    }
                                } else {
                                    var html = at("serviceTpl", {json: [data]});
                                    $('#service').append(html);
                                }
                                calMoney('goods', 'service');
                                calMoney('service', 'goods');
                            },
                            error: function () {
                                var html = at("serviceTpl", {json: [data]});
                                $('#service').append(html);

                                calMoney('service', 'goods');
                            }
                        });
                    } else {
                        var html = at("serviceTpl", {json: [data]});
                        $('#service').append(html);
                        calMoney('service', 'goods');
                    }
                }
            }

            // 开单
            doc.on("click", ".js-submit-create", function () {
                speedilySave(function (json) {
                    dg.success(['保存成功，', 3, '秒后跳转至工单详情页'], function () {
                        window.location.href = BASE_PATH + "/shop/order/detail?orderId=" + json.data;
                    })
                });
            });

            // 结算，并跳转
            doc.on("click", ".js-submit-settle", function () {
                var funcList = "结算首页,财务首页";
                var func = util.checkFuncList(funcList);
                if(!func){
                    return false;
                }

                var isWorkerExist = true;
                $(".service-datatr").each(function () {
                    var tr = $(this);
                    var temp = fd.get(tr);

                    if (!temp.workerIds) {
                        isWorkerExist = false;
                    }
                });

                if (!isWorkerExist) {
                    dg.confirm('存在服务项目没有选择维修工, 是否继续?',function(){
                        settle();
                    },function(){
                        return;
                    });
                } else {
                    settle();
                }
            });

            // 结算方法
            function settle() {
                speedilySave(function (orderInfo) {
                    $("input[name='orderInfo.id']").val(orderInfo.data);
                    $.ajax({
                        url: BASE_PATH + "/shop/warehouse/out/isstockout",
                        data: {orderid: orderInfo.data},
                        success: function (json) {
                            if (json.success) {
                                if (!json["data"]) {
                                    dg.success("快修快保单保存成功，即将跳转到收款页", function () {
                                        window.location.href = BASE_PATH + "/shop/settlement/debit/speedily-confirm-bill?refer=reception-speedily&orderId=" + orderInfo.data;
                                    });
                                } else {
                                    dg.fail("提交失败，库存不足请转入库<br\/>" + noTime + "秒后自动弹出转入库页面", function () {
                                        inWarehouseGoods.setStatus(true);

                                        $(".js-in-warehouse").trigger("click");
                                    });
                                }
                            } else {
                                dg.fail(json.errorMsg);
                            }

                        }
                    });
                });
            }

            doc.on('input', '.js-number', function() {
                $(this).val( checkFloat($(this).val(), 0) );
            });
            //返回
            doc.on('click', '.js-return', util.goBack);

            if($('.js-goods-num').length) {
                $('.js-goods-num').trigger('input');
            }
            //常用服务提示
            var tipId;
            doc.on("mouseenter", ".js-hot-service", function() {
                tipId = dg.tips(this, "统计当日前30天所有有效工单的服务项目使用次数（洗车单除外）");
            }).on("mouseleave", ".js-hot-service", function(e) {
                var target = e.target;
                if(target.parentNode === this) return;
                dg.close(tipId);
            });

            $.extend(ck.regList, {
                compareService: function (val) {
                    var msg = compare.call(this, +val, '.js-service-amount');

                    if(msg) {
                        return {msg: msg, result: false};
                    }
                    return {msg: '', result: true};
                },
                compareGoods: function (val) {
                    var msg = compare.call(this, +val, '.js-goods-amount');

                    if(msg) {
                        return {msg: msg, result: false};
                    }
                    return {msg: '', result: true};
                }
            });

            function compare(val, prevSelector) {
                var prev = +$(this).parents('tr').find(prevSelector).val();

                if(val > prev) {
                    return '优惠不能大于金额'
                }
            }
        });
    }
)
;
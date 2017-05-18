/**
 * ch 2016-04-06
 * 新建销售单页面。
 */

seajs.use([
    "art",
    "select",
    "downlist",
    "formData",
    'date',
    'dialog',
    'check',
], function (at, st, dl, fd, date, dg,ck) {

    ck.init();
    dg.titleInit();

    //错误信息停留时间
    var noTime = 4;

    var doc = $(document);

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

    //验证初始化
    seajs.use('check', function (ck) {
        ck.init();
    });

    //销售员select下拉列表初始化。
    st.init({
        dom: ".js-speedily-sale",
        url: BASE_PATH + '/shop/manager/get_manager',
        showKey: "id",
        showValue: "name"
    });

    //动态行删除
    doc.on('click', '.js-dynamic-del-btn', function () {
        $(this).parents('tr').remove();

        calculateUtil.drawup();
    });

    // 配置的日期
    date.datePicker('.js-createTime', {
        maxDate: '%y-%M-%d',
        dateFmt: 'yyyy-MM-dd HH:mm'
    });

    //初始化车牌下拉框
    dl.init({
        url: BASE_PATH + '/shop/customer/search/mobile',
        searchKey: 'com_license',
        tplId: 'carLicenceTpl',
        notClearInput: true,
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
                        $("input[name='orderInfo.contactName']").val(json.data.contact);
                        $("input[name='orderInfo.contactMobile']").val(json.data.contactMobile);
                        // 客户单位
                        $("input[name='orderInfo.company']").val(json.data.company);
                    }

                }
            });
        }
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

    //服务顾问select下拉列表初始化。
    st.init({
        dom: ".js-select-user",
        url: BASE_PATH + '/shop/manager/get_manager',
        showKey: "id",
        showValue: "name"
    });

    //配件数量发生变化，是否需要转入库操作判断
    doc.on("blur", "input[name='goodsNumber']", function () {
        var goodsNumber = Number($(this).val());
        var parents = $(this).parents("tr");
        var stock = Number($("input[name='stock']", parents).val());
        if (goodsNumber > stock) {
            //需要转入库操作,自动勾选
            $("input[type='checkbox']", parents).removeAttr("disabled");
            $("input[type='checkbox']", parents).attr("checked", "checked");
        } else {
            //不需要转入库操作,自动取消选择
            $("input[type='checkbox']", parents).attr("disabled", "disabled").removeAttr("checked");
        }
    });

    $(doc).on("blur", [
        "input[name='discount']",
        "input[name='goodsNumber']",
        "input[name='goodsPrice']"].join(","), function () {
        var element = $(this);
        var goodsTr = element.parents("tr");

        // 需要校验控件集合
        var checkInputs = [
            {"name": "goodsNumber", "tip": "无效配件数量，请输入数字 <br\/>数字精确到小数点后两位"},
            {"name": "discount", "tip": "无效配件优惠金额，请输入数字"},
            {"name": "goodsPrice", "tip": "无效配件单价，请输入数字"}
        ];
        // 无效控件集合
        var invalidInputs = [];

        // 物料编号
        var goodsSn = $('input[name="goodsSn"]', goodsTr).val();
        // 物料名称
        var goodsName = $('input[name="goodsName"]', goodsTr).val();
        if (typeof(goodsName) != "undefined" && goodsName != "") {
            // 数量
            var goodsNum = $('input[name="goodsNumber"]', goodsTr).val();
            // 单价
            var goodsPrice = $('input[name="goodsPrice"]', goodsTr).val();
            // 折扣
            var discount = $('input[name="discount"]', goodsTr).val();

            // verify data
            var decimal2 = new RegExp(/^\d+(\.\d{0,2})?$/);
            if (!($.isNumeric(goodsNum) && decimal2.test(goodsNum))) {
                invalidInputs.push("goodsNumber");
            }
            if (!$.isNumeric(goodsPrice)) {
                invalidInputs.push("goodsPrice");
            }
            if (!$.isNumeric(discount)) {
                invalidInputs.push("discount");
            }

            var isInvalid = false;

            for (var inputIndex in checkInputs) {
                var inputObj = checkInputs[inputIndex];
                var inputName = inputObj["name"];
                var tip = inputObj["tip"];
                var element = $('input[name="' + inputName + '"]', goodsTr);
                if (invalidInputs.indexOf(inputName) != -1) {
                    isInvalid = true;
                }
            }
            goodsNum = goodsNum || 1;
            goodsPrice = goodsPrice || 0;

            // 总金额
            if (!isInvalid) {
                var soldAmount = (Number(goodsPrice).Cheng(Number(goodsNum))).toFixed(2);
                $('input[name="goodsAmount"]', goodsTr).val(soldAmount);
            }
        }
    });

    //全选单选事件
    doc.on("click", ".checkbox_all", function () {
        var trAll = $(this);
        var scope = trAll.parents(".dynamic-table");
        if (!trAll.is(':checked')) {
            //全取消
            $("tbody input[type='checkbox']", scope).filter(function () {
                return ($(this).attr("disabled") === undefined);
            }).each(function () {
                $(this).removeAttr("checked");
            });
        } else {
            //全选
            // console.log("点了全选");
            $("tbody input[type='checkbox']", scope).filter(function () {
                return ($(this).attr("disabled") === undefined);
            }).each(function () {
                $(this).trigger("click");
            });
        }
    });

    //转入库,需要引入模板 yqx/tpl/order/in-warehouse-tpl.ftl
    inWarehouse({
        //转入口触发按钮
        dom: '.js-in-warehouse-btn',
        goodsData: inWarehouseGoods.fn,
        //需求转入库的配件列表
        list: ".dynamic-tbody"
    });

    // 公共配件回调函数
    function addGoodsCallback(json) {
        json.carTypeInfo = (function () {
            var arr = [];
            if (json.carInfoList) {
                for (var i = 0; i < json.carInfoList.length; i++) {
                    arr.push(json.carInfoList[i].carBrandName);
                }
            }
            if (arr.length > 0) {
                return arr.join('|');
            } else if (json.carInfoStr) {
                return json.carInfoStr;
            } else {
                return '';
            }
        })();
        $('#dynamic-box').append(at("partTpl", {json: json}));

        calculateUtil.drawup();
    };


    //批量添加物料,需要引入模板 yqx/tpl/order/new-part-tpl.ftl
    batchAddPart({
        dom: '.js-batch-add-part-btn',
        callback: function (data) {
            data.forEach(function (e) {
                addGoodsCallback(e);
            });
        }
    });

    //新增配件
    newPart({
        dom: '.js-new-part-btn',
        callback: addGoodsCallback
    });

    //添加配件，需要引入模板 yqx/tpl/common/goods-add-tpl.ftl
    addGoodsInit({
        dom: '.js-dynamic-add-btn',
        callback: addGoodsCallback
    });

    doc.on('blur','input[name="goodsPrice"]',function(){
        var price = $(this).val(),
            currentPrice= $(this).parents(".goods-datatr"),
            inventoryPrice = $('input[name="inventoryPrice"]', currentPrice).val();

        if( Number(price) < Number(inventoryPrice)){
            dg.warn("售价不能小于成本价")
        }

    });

    // 保存AND更新表单
    function saveAndUpdate(fn) {
        if(!ck.check()){//作用域为整个页面的验证
            return;
        }
        var data = fd.get('.js-sell-goods-form' , true),
            goodsList = [];
        $('tr', '#dynamic-box').each(function () {
            var item = fd.get($(this));
            item.goodsNote = item.goodsNote || "";
            goodsList.push(item);
        });
        data["orderInfo.postscript"] = $("input[name='orderInfo.postscript']").val();

        //配件列表
        data.orderGoodJson = JSON.stringify(goodsList);

        //数据发送
        var orderId = $("input[name='orderInfo.id']").val();
        var requestUrl = BASE_PATH + '/shop/order/sell-good/save';
        if (orderId > 0) {
            requestUrl = BASE_PATH + '/shop/order/sell-good/update';
        }

        $.ajax({
            type: 'post',
            data: data,
            url: requestUrl
        }).done(function (json) {
            if (json.success) {
                fn && fn(json);
            } else {
                dg.fail(json.errorMsg);
            }
        });
    };

    // 开单
    doc.on('click', '.js-create-sell', function () {
        if(!ck.check()){//作用域为整个页面的验证
            return;
        }
        saveAndUpdate(function (json) {
            dg.success(['保存成功，', 3,'秒后跳转到工单详情页'], function () {
                window.location.href = BASE_PATH + "/shop/order/detail?refer=sell-good&orderId=" + json["data"];
            })
        });
    });

    //打印
    $(".js-print").click(function () {
        var orderId = $("#orderId").val();
        window.open(BASE_PATH + "/shop/order/sell-good-print?id=" + orderId);
    });

    // settle
    doc.on('click', '.js-settle', function () {
        var funcList = "结算首页,财务首页";
        var func = util.checkFuncList(funcList);
        if(!func){
            return false;
        }
        saveAndUpdate(function (json) {
            var orderId = json["data"];
            $.ajax({
                type: 'get',
                url: BASE_PATH + "/shop/warehouse/out/isstockout",
                data: {orderid: orderId},
                success: function (json) {
                    if (json.success) {
                        if (!json["data"]) {
                            dg.success(['销售单保存成功，', 3,'秒后自动跳转到结算页面'], function () {
                                window.location.href = BASE_PATH + "/shop/settlement/debit/speedily-confirm-bill?orderId=" + orderId;
                            });
                        } else {
                            dg.fail("提交失败，库存不足请转入库<br\/>" + noTime + "秒后自动弹出转入库页面", function () {
                                inWarehouseGoods.setStatus(true);
                                $(".js-in-warehouse-btn").trigger("click");
                            });
                        }
                    } else {
                        dg.fail(json.errorMsg);
                    }
                }
            });
        });
    });

    //返回
    doc.on('click', '.js-return', function() {
        util.goBack();
    });

    // 作废
    doc.on('click', '.js-invalid', function () {
        var orderId = $("input[name='orderInfo.id']").val();
        dg.confirm("您确定要把该工单无效吗?", function () {
            $.ajax({
                type: 'post',
                url: BASE_PATH + '/shop/order/invalid',
                data: {
                    orderId: orderId
                },
                success: function (json) {
                    if (json["success"]) {
                        dg.success("操作成功", function () {
                            window.location.reload();
                        });
                    } else {
                        dg.fail(json["errorMsg"]);
                    }
                }
            });
        });
    });


    // 价格变化['售价'\'数量'\'优惠金额']
    var priceElement = ["input[name='goodsPrice']",
        "input[name='goodsNumber']",
        "input[name='discount']"];
    $(document).on('blur', priceElement.join(","), function () {
        calculateUtil.drawup();
    });

    $.extend(ck.regList, {
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


// calculate price
var calculateUtil = calculateUtil || {};
(function ($) {

    calculateUtil.drawup = function () {
        // 物料金额
        var partCount = (function () {
            var count = 0;
            var tbody = $("#dynamic-box");
            var arr = $("tr", tbody).get().reverse();
            $.each(arr, function (i, ele) {
                var $this = ele;
                // 单价
                var goodsPrice = Number($("input[name='goodsPrice']", $this).val());
                // 数量
                var goodsNumber = Number($("input[name='goodsNumber']", $this).val());
                // 优惠金额
                var discount = Number($("input[name='discount']", $this).val());

                // IF 有效金额 THEN 核算
                var isinValid = 1;
                var decimal2 = new RegExp(/^\d+(\.\d{0,2})?$/);
                if (!($.isNumeric(goodsNumber) && decimal2.test(goodsNumber))) {
                    isinValid = isinValid & 0;
                }
                if (!$.isNumeric(goodsPrice)) {
                    isinValid = isinValid & 0;
                }
                if (!($.isNumeric(discount) && decimal2.test(discount))) {
                    isinValid = isinValid & 0;
                }
                if (isinValid == 1) {
                    // 金额
                    var goodsAmount = goodsPrice.Cheng(goodsNumber).Jian(discount);
                    count += goodsAmount;
                }
            });
            return count;
        })();

        // 配件总金额
        $("#part_count").text(partCount.toFixed(2));
        // 应收金额
        $("#order_count").text(partCount.toFixed(2));
    };

})(jQuery);

// init page
calculateUtil.drawup();


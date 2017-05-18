// 核算费用
var calculatePriceUtil = calculatePriceUtil || {};
calculatePriceUtil.drawup = function () {
    // 折扣
    var preDiscountRate = $('input[name="orderInfo.preDiscountRate"]').val() || 1;
    // 费用
    var preTaxAmount = $('input[name="orderInfo.preTaxAmount"]').val() || 0;
    // 优惠
    var prePreferentiaAmount = $('input[name="orderInfo.prePreferentiaAmount"]').val() || 0;
    // 代金券
    var preCouponAmount = $('input[name="orderInfo.preCouponAmount"]').val() || 0;

    if (Number(preDiscountRate) < 0 || Number(preDiscountRate) > 1) {
        $('input[name="orderInfo.preDiscountRate"]').val(1);
        preDiscountRate = 1;
    }

    // 物料
    var orderGoods = [];
    var goodsRowArray = $('input[name="goodsId"]').parents(".goods-datatr");
    goodsRowArray.each(function () {
        var goodsId = $('input[name="goodsId"]', this).val();
        var goodsPrice = $('input[name="goodsPrice"]', this).val();
        var goodsNumber = $('input[name="goodsNumber"]', this).val();
        var goodsAmount = $('input[name="goodsAmount"]', this).val();
        var discount = $('input[name="discount"]', this).val();

        if (goodsPrice != ""
            && goodsNumber != ""
            && goodsAmount != ""
            && discount != "") {
            orderGoods.push({
                goodsId: goodsId,
                goodsPrice: goodsPrice,
                goodsNumber: goodsNumber,
                goodsAmount: goodsAmount,
                discount: discount
            });
        }
    });

    orderGoods = JSON.stringify(orderGoods);
    var $manageFeeAmount = null;
    var $manageFee = null;
    var orderServices = [];

    var serviceRowArray = $('input[name="serviceId"]').parents(".service-datatr");
    serviceRowArray.each(function () {
        var serviceId = $('input[name="serviceId"]', this).val();
        var servicePrice = $('input[name="servicePrice"]', this).val();
        var serviceHour = $('input[name="serviceHour"]', this).val();
        var serviceAmount = $('input[name="serviceAmount"]', this).val();
        var discount = $('input[name="discount"]', this).val();
        var type = $('input[name="type"]', this).val();
        // TODO flags 何意
        var flags = $('input[name="flags"]', this).val();
        if (typeof flags == "undefined" || flags == null) {
            flags = "";
        }
        if (flags == 'GLF') {
            $manageFeeAmount = $('input[name="serviceAmount"]', this);
            $manageFee = $('input[name="servicePrice"]', this);
        }
        var manageRate = $('input[name="manageRate"]', this).val();
        if (typeof manageRate == "undefined" || manageRate == '') {
            manageRate = 0;
        }

        if (servicePrice != "" &&
            serviceHour != "" &&
            discount != "") {
            if (serviceAmount == "") {
                $('input[name="serviceAmount"]', this).val((servicePrice * serviceHour).toFixed(2));
                serviceAmount = servicePrice * serviceHour;
            }
            orderServices.push({
                serviceId: serviceId,
                servicePrice: servicePrice,
                serviceHour: serviceHour,
                serviceAmount: serviceAmount,
                discount: discount,
                type: type,
                flags: flags,
                manageRate: manageRate
            });
        } else if (servicePrice == "" && serviceHour == "" &&
            serviceAmount == "" && discount == "") {
        }

    });
    orderServices = JSON.stringify(orderServices);

    $.ajax({
        type: 'post',
        data: {
            orderGoods: orderGoods,
            orderServices: orderServices
        },
        global: false,
        url: BASE_PATH + '/shop/order/calc_price'
    }).done(function (result) {
        if (result.success) {
            if (typeof $manageFee != 'undefined' && $manageFee != null) {
                $manageFee.val(parseFloat(result.data.manageFee).toFixed(2));
                $manageFeeAmount.val(parseFloat(result.data.manageFee).toFixed(2));
            }
            var serviceAmount = result.data.serviceAmount;
            var goodsAmount = result.data.goodsAmount;
            var totalAmount = result.data.totalAmount;
            var discount = result.data.discount;
            var orderAmount = result.data.orderAmount;
            var goodsDiscount = result.data.goodsDiscount;
            var serviceDiscount = result.data.serviceDiscount;
            var feeAmount = result.data.feeAmount;
            var feeDiscount = result.data.feeDiscount;

            $('input[name="orderInfo.serviceAmount"]').val(serviceAmount);
            $('input[name="orderInfo.goodsAmount"]').val(goodsAmount);
            $('input[name="orderInfo.goodsDiscount"]').val(goodsDiscount);
            $('input[name="orderInfo.serviceDiscount"]').val(serviceDiscount);
            $('input[name="orderInfo.feeAmount"]').val(parseFloat(feeAmount).toFixed(2));
            $('input[name="orderInfo.feeDiscount"]').val(parseFloat(feeDiscount).toFixed(2));

            // goodsAmount - goodsDiscount
            var goodsTotalAmount = (parseFloat(goodsAmount) - parseFloat(goodsDiscount)).toFixed(2);
            $("#goodsTotalAmount").text(goodsTotalAmount);
            // (serviceAmount-serviceDiscount) +(feeAmount -feeDiscount )
            var serviceTotalAmount = parseFloat(serviceAmount) - parseFloat(serviceDiscount);
            $("#serviceTotalAmount").text(serviceTotalAmount.toFixed(2));
            var feeTotalAmount = parseFloat(feeAmount) - parseFloat(feeDiscount);
            $("#feeTotalAmount").text(feeTotalAmount.toFixed(2));
            $("#orderTotalAmount").text(parseFloat(orderAmount).toFixed(2));

            var pretotalAmount = (parseFloat(orderAmount)
            * preDiscountRate
            + parseFloat(preTaxAmount)
            - parseFloat(prePreferentiaAmount)
            - parseFloat(preCouponAmount)).toFixed(2);

            $('input[name="orderInfo.preTotalAmount"]').val(pretotalAmount);
        }
    });
};


seajs.use([
    "art",
    "select",
    "ajax",
    'dialog',
    'downlist',
    'check',
    'date',
    'formData'
], function (at, st, ax, dg, dl, ck, date, fd) {

    var doc = $(document);

    ck.init();

    // 更多车辆信息
    $('.js-tocustomer').on('click', function () {
        var customerCarId = $("input[name='orderInfo.customerCarId']").val();
        if (typeof(customerCarId) != 'undefined' && customerCarId != "") {
            window.open(BASE_PATH + '/shop/customer/car-detail?refer=virtualorder&id=' + customerCarId);
        } else {
            dg.warn('请输入有记录的车牌号')
        }
    });

    //洗车工下拉列表初始化。
    workerInit({
        dom: '.js-worker',
        hiddenDom: 'input[name="workerIds"]'
    });

    /* 动态下拉列表初始化 start */
    // 车辆选择
    var dlCarBrand = dl.init({
        url: BASE_PATH + '/shop/car_category/car_model',
        dom: 'input[name="carModeBak"]',
        showKey: 'brand,importInfo,model',
        hiddenKey: 'brandId,brand,seriesId,series,modelId,model,importInfo',
        hiddenSelector: 'carBrandId,carBrand,carSeriesId,carSeries,carModelId,carModel,importInfo',
        tplColsWidth: [80, 50, 180],
        hasTitle: false,
        hasInput: false,
        autoFill: true,
        callbackFn: function($input, data, $scope) {
            carTypeCallBack(data);
            //激活年款排量输入框
            $("input[name='orderInfo.carBrand']").val(data.brand);
            $("input[name='orderInfo.carBrandId']").val(data.brandId);
            $("input[name='orderInfo.carModels']").val(data.model);
            $("input[name='orderInfo.carModelsId']").val(data.modelId);
            $("input[name='orderInfo.carSeries']").val(data.series);
            $("input[name='orderInfo.carSeriesId']").val(data.seriesId);
            $("input[name='orderInfo.importInfo']").val(data.importInfo);
        },
        clearCallbackFn: function($input, $scope) {
            var yearPowerHidden = 'carYear,carYearId,carPower,carPowerId,carGearBox,carGearBoxId'.split(','),
                name, i;
            // 年款排量清空
            for(i = 0; i < yearPowerHidden.length; i++) {
                name = yearPowerHidden[i];
                $('input[name="orderInfo.' + name + '"]').val('');
            }
            $("input[name='yearPowerBak']").attr('disabled', true).val('');
        }
    });

    // 年款排量
    var dlYearPower = dl.init({
        url: BASE_PATH + '/shop/car_category/car_model',
        dom: "input[name='yearPowerBak']",
        showKey: 'year,gearbox',
        hiddenKey: 'year,yearId,power,powerId,gearbox,gearboxId',
        hiddenSelector: 'carYear,carYearId,carPower,carPowerId,carGearBox,carGearBoxId',
        tplId: 'yearPowerDLTpl',
        hasTitle: false,
        hasInput: false,
        autoFill: true
    });
    /* 动态下拉列表初始化 end */

    //选择车型按钮
    carTypeInit({
        dom: '.js-car-type',
        callback: function (data) {
            //激活年款排量输入框
            $("input[name='orderInfo.carBrand']").val(data.carBrand);
            $("input[name='orderInfo.carBrandId']").val(data.carBrandId);
            $("input[name='orderInfo.carModels']").val(data.carModel);
            $("input[name='orderInfo.carModelsId']").val(data.carModelId);
            $("input[name='orderInfo.carSeries']").val(data.carSeries);
            $("input[name='orderInfo.carSeriesId']").val(data.carSeriesId);
            $("input[name='orderInfo.importInfo']").val(data.importInfo);
            //车型展示字段
            var carModel = data.carBrand + " " +data.carModel;
            $("input[name='carModeBak']").val(carModel);
            carTypeCallBack(data);
        }
    });

    //选择车型的回调函数
    function carTypeCallBack(data){
        var modelId = data.carModelId == null ? data.modelId : data.carModelId;
        /* 重置 start */
        $("input[name='yearPowerBak']")
            .removeAttr('disabled').val("")
            .siblings('[name="orderInfo.carYear"]').val("")
            .siblings('[name="orderInfo.carYearId"]').val("")
            .siblings('[name="orderInfo.carPower"]').val("")
            .siblings('[name="orderInfo.carPowerId"]').val("")
            .siblings('[name="orderInfo.carGearBox"]').val("")
            .siblings('[name="orderInfo.carGearBoxId"]').val("");



        // 下拉列表年款排量动态修改参数
        dl.update(dlYearPower, {globalData: {'model_id': modelId}});
    };

    // tab切换
    function tabSwitch() {
        var $this = $(this),
            ref = $this.data('ref');

        $this.addClass('current-item')
            .siblings('.current-item').removeClass('current-item');
        $('.js-tab-content-' + ref).show()
            .siblings('.js-tab-content').hide();
    }

    doc.on('click', '.js-tab-item', function () {
        tabSwitch.call(this);
    });
    tabSwitch.call($('.js-tab-item').eq(0));

    // jib显示更多
    doc.on('click', '.js-toggle-control', function () {
        var $this = $(this),
            ref = $this.data('ref'),
            currStatus = $this.data('status'),
            $ref = $('.js-toggle-' + ref),
            nextStatus;

        nextStatus = (currStatus === 'show') ? 'hide' : 'show';
        $this.data('status', nextStatus);

        if (nextStatus === 'show') {
            $ref.show(0, function () {
                $this.addClass('arrow-up').removeClass('arrow-down');
            });
        } else {
            $ref.hide(0, function () {
                $this.addClass('arrow-down').removeClass('arrow-up');
            });
        }
    });

    // 保存AND更新表单
    function saveAndUpdate(fn) {
        var data = fd.get('.base-box'),
            serviceList = [],
            goodsList = [];
        // 基本服务
        $('tr', '#orderServiceTB').each(function () {
            var item = fd.get($(this));
            serviceList.push(item);
        });
        // 附加费用服务
        $('tr', '#additionalOrderServiceTB').each(function () {
            var item = fd.get($(this));
            serviceList.push(item);
        });
        $('tr', '#orderGoodsTB').each(function () {
            var item = fd.get($(this));
            goodsList.push(item);
        });

        data["orderInfo.postscript"] = $("input[name='orderInfo.postscript']").val();

        // 服务列表
        data["orderServiceJson"] = JSON.stringify(serviceList);
        //配件列表
        data.orderGoodJson = JSON.stringify(goodsList);

        //数据发送
        var orderId = $("input[name='orderInfo.id']").val();
        var requestUrl = BASE_PATH + '/shop/order/virtual/save';
        if (orderId > 0) {
            requestUrl = BASE_PATH + '/shop/order/virtual/update';
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


    // 开单|编辑保存
    doc.on('click', '.js-create', function () {
        if ( !ck.check() ) return false;
        saveAndUpdate(function (json) {
            dg.success("保存成功", function () {
                window.location.href = BASE_PATH + "/shop/order/virtualorder-edit?parentId=" + json["data"];
            })
        });
    });

    // 删除
    doc.on('click', '.js-virtualorder-delete', function () {
        var orderId = $("input[name='orderInfo.id']").val();
        dg.confirm("您确定要删除子单吗?", function () {
            $.ajax({
                type: 'GET',
                url: BASE_PATH + '/shop/order/virtual/delete',
                data: {
                    orderId: orderId
                },
                success: function (json) {
                    if (json["success"]) {
                        dg.success("删除成功", function () {
                            window.location.reload();
                        });
                    } else {
                        dg.fail(json["errorMsg"]);
                    }
                }
            });
        });
    });

    // 回主工单页面
    doc.on('click', '.js-backmain', function () {
        // 主工单ID
        var orderId = $('input[name="orderInfo.parentId"]').val();
        if((typeof(orderId) !="undefined") && orderId >0){
            window.location.href = BASE_PATH + "/shop/order/detail?orderId=" + orderId;
        }else{
            util.goBack();
        }
    });

    // 打印
    doc.on('click', '.js-virtualorder-print', function () {
        // 主工单ID
        var orderId = $('input[name="orderInfo.id"]').val();
        util.print(BASE_PATH + "/shop/order/virtualorder-print?orderId=" + orderId);
    });

    // 销售员下拉列表初始化。
    st.init({
        dom: ".js-speedily-sale",
        url: BASE_PATH + '/shop/manager/get_manager',
        showKey: "id",
        showValue: "name"
    });

    // 配置的日期
    date.datePicker('.js-pay-time, .js-create-time, .js-print-time', {
        dateFmt: 'yyyy-MM-dd HH:mm',
        doubleCalendar: true
    });
    date.datePicker('.js-expected-time', {
        dateFmt: 'yyyy-MM-dd HH:mm',
        minDate: '%y-%M-%d',
        doubleCalendar: true
    });
    // 初始化客户单位下拉框
    dl.init({
        url: BASE_PATH + '/shop/customer/search/company',
        showKey: 'company',
        searchKey: 'company',
        tplCols: {'company': '客户单位'},
        dom: 'input[name="orderInfo.company"]',
        notClearInput: true,
        hasInput: false
    });

    // 保险初始化。
    st.init({
        dom: ".js-insurance",
        url: BASE_PATH + '/insurance/list',
        showKey: "id",
        showValue: "name"
    });
    // 对方保险初始化。
    st.init({
        dom: ".js-otherinsurance",
        url: BASE_PATH + '/insurance/list',
        showKey: "id",
        showValue: "name"
    });

    // 服务顾问初始化。
    st.init({
        dom: ".js-select-user",
        url: BASE_PATH + '/shop/manager/get_manager',
        showKey: "id",
        showValue: "name"
    });


    // 业务类型
    st.init({
        dom: ".js-ordertype",
        url: BASE_PATH + '/shop/order/order_type/list',
        showKey: "id",
        showValue: "name"
    });

    // 油表油量初始化。
    st.init({
        dom: 'input[name="orderInfo.oilMeter"]',
        showKey: "key",
        showValue: "value",
        data: [{
            key: '空',
            value: '空'
        },{
            key: '小于1/4',
            value: '小于1/4'
        },{
            key: '1/4',
            value: '1/4'
        },{
            key: '1/2',
            value: '1/2'
        },{
            key: '3/4',
            value: '3/4'
        },{
            key: '满',
            value: '满'
        }]
    });

    // 新增服务
    addServiceInit({
        dom: '#serviceAddBtn',
        callback: function(json){
            var html = at("serviceTpl", {json: [json.data]});
            $('#orderServiceTB').append(html);

            calculatePriceUtil.drawup();
        }
    });

    newAdditional({
        dom: '#feeAddBtn',
        callback: function(json) {
            var html = at("additionalTpl", {json: [json["data"]]});
            $('#orderAdditionalTB').append(html);

            calculatePriceUtil.drawup();
        }
    });

    newPart({
        dom: '.js-new-part-btn',
        callback: function(json){
            var html = at("goodsTpl", {json: [json]});
            $('#orderGoodsTB').append(html);

            calculatePriceUtil.drawup();
        }
    });

    // 选择服务
    getService({
        // 点击按钮的选择器
        dom: '.js-get-service',
        // 回调函数，处理选择的数据
        callback: function (json) {
            if (json.suiteNum > 0) {
                $.ajax({
                    async: false,
                    type: 'get',
                    url: BASE_PATH + "/shop/shop_service_info/getPackageByServiceId",
                    data: {
                        serviceId: json.id
                    },
                    success: function (packageJson) {
                        if (packageJson.success) {
                            var html = at("serviceTpl", {json: packageJson.data.shopServiceInfoList});
                            $('#orderServiceTB').append(html);
                            var googsList = packageJson.data.goodsList;
                            var goodsHtml = at("goodsTpl", {json: packageJson.data.goodsList});
                            $('#orderGoodsTB').append(goodsHtml);

                            //有物料显示提示框
                            var tip = $(".tip");
                            if (googsList && googsList.length > 0) {
                                tip.show();
                            }
                        }
                    }
                });
            } else {
                var jsons = new Array();
                jsons[0] = json;
                var html = at("serviceTpl", {json: jsons});
                $('#orderServiceTB').append(html);
            }

            calculatePriceUtil.drawup();
        }
    });

    // 添加物料
    addGoodsInit({
        dom: '#goodsAddBtn',
        callback: function (json) {
            var goodsJsonArray = [];
            goodsJsonArray[0] = json;
            var html = at("goodsTpl", {json: goodsJsonArray});
            $('#orderGoodsTB').append(html);

            // 计算价格
            calculatePriceUtil.drawup();
        }
    });

    // 添加其他服务
    getadditional({
        dom: '#additionalAddBtn',
        callback: function (json) {
            var jsonArray = [];
            jsonArray[0] = json;
            var html = at("additionalTpl", {json: jsonArray});
            $('#additionalOrderServiceTB').append(html);

            // 计算价格
            calculatePriceUtil.drawup();

        }
    });

    //删除行
    doc.on('click', '.js-trdel', function () {
        $(this).parents('tr').remove();
        // 计算价格
        calculatePriceUtil.drawup();
    });

    doc.on('click','.js-tab-item',function(){
        $(this).find('.js-tip').hide();
    })

    /***
     * 需要重新核算价格
     *
     * 变动项目：
     *
     * 服务项目 {‘工时’，‘工时费’，‘优惠’}
     * 物料 {‘单价’，‘数量’，‘优惠’}
     * 其他物料 {‘单价’，‘数量’，‘优惠’}
     * 附加费用 {‘金额’，‘优惠’}
     * 申请优惠 {‘折扣’，‘费用’，‘优惠’，‘代金券’}
     *
     */
        // 服务项目.工时
    $(document).on('blur', 'input[name="serviceHour"]', function () {
        var serviceHour = $(this).val();
        if (!$.isNumeric(serviceHour) || serviceHour < 0) {
            $(this).val(0);
            return false;
        }

        var currentRow = $(this).parents(".service-datatr");
        var servicePriceInput = $('input[name="servicePrice"]', currentRow);
        var servicePrice = servicePriceInput.val();
        if (servicePrice == '') {
            servicePrice = 0;
            servicePriceInput.val(0);
        }
        var discountInput = $('input[name="discount"]', currentRow);
        var discount = discountInput.val();
        if (discount == '') {
            discountInput.val(0);
        }
        var serviceAmount = $('input[name="serviceAmount"]', currentRow);
        serviceAmount.val((parseFloat(servicePrice) * parseFloat(serviceHour)).toFixed(2));

        // 核算价格
        calculatePriceUtil.drawup();
    });

    // 服务项目.工时费
    $(document).on('blur', 'input[name="servicePrice"]', function () {
        var servicePrice = $(this).val();
        if (!$.isNumeric(servicePrice) || servicePrice < 0) {
            $(this).val(0);
            return;
        }

        var currentRow = $(this).parents(".service-datatr");
        var serviceHourInput = $('input[name="serviceHour"]', currentRow);
        var serviceHour = serviceHourInput.val();
        if (serviceHour == '') {
            serviceHour = 1;
            serviceHourInput.val(1);
        }

        var discountInput = $('input[name="discount"]', currentRow);
        var discount = discountInput.val();
        if (discount == '') {
            discountInput.val(0);
        }

        var serviceAmount = $('input[name="serviceAmount"]', currentRow);
        serviceAmount.val((parseFloat(servicePrice) * parseFloat(serviceHour)).toFixed(2));

        // 核算价格
        calculatePriceUtil.drawup();
    });

    // 基本服务项目.优惠
    var basicServiceScope = $("#orderServiceTB");
    $(basicServiceScope).on('blur', 'input[name="discount"]', function () {
        var discount = $(this).val();
        if (!$.isNumeric(discount) || discount < 0) {
            $(this).val(0);
            return;
        }

        // 检验优惠金额 >总金额
        var currentRow = $(this).parents(".service-datatr");
        var serviceAmount = $('input[name="serviceAmount"]', currentRow).val();
        if (parseFloat(discount) > parseFloat(serviceAmount)) {
            $(this).val(0);
            return;
        }

        // 核算价格
        calculatePriceUtil.drawup();
    });

    // 附加服务项目.优惠
    var basicServiceScope = $("#additionalOrderServiceTB");
    $(basicServiceScope).on('blur', 'input[name="discount"]', function () {
        var discount = $(this).val();
        if (!$.isNumeric(discount) || discount < 0) {
            $(this).val(0);
            return;
        }

        // 检验优惠金额 >总金额
        var currentRow = $(this).parents(".service-datatr");
        var serviceAmount = $('input[name="serviceAmount"]', currentRow).val();
        if (parseFloat(discount) > parseFloat(serviceAmount)) {
            $(this).val(0);
            return;
        }

        // 核算价格
        calculatePriceUtil.drawup();
    });

    // 物料.单价
    $(document).on('blur', 'input[name="goodsPrice"]', function () {
        var goodsPrice = $(this).val();
        if (!$.isNumeric(goodsPrice) || goodsPrice < 0) {
            $(this).val(0);
            return;
        }

        var currentRow = $(this).parents(".goods-datatr");
        var goodsNumberInput = $('input[name="goodsNumber"]', currentRow);
        var goodsNumber = goodsNumberInput.val();
        if (goodsNumber == '') {
            goodsNumber = 1;
            goodsNumberInput.val(1);
        }

        var discountInput = $('input[name="discount"]', currentRow);
        var discount = discountInput.val();
        if (discount == '') {
            discountInput.val(0);
        }

        var goodsAmount = $('input[name="goodsAmount"]', currentRow);
        goodsAmount.val((goodsPrice * goodsNumber).toFixed(2));

        // 核算价格
        calculatePriceUtil.drawup();
    });

    // 物料.数量
    $(document).on('blur', 'input[name="goodsNumber"]', function () {
        var goodsNumber = $(this).val();
        if (!$.isNumeric(goodsNumber) || goodsNumber < 0) {
            $(this).val(1);
            return false;
        }

        // TODO 判断数量 >库存
        var currentRow = $(this).parents(".goods-datatr");
        var goodsPriceInput = $('input[name="goodsPrice"]', currentRow);
        var goodsPrice = goodsPriceInput.val();
        if (goodsPrice == '') {
            goodsPrice = 0;
            goodsPriceInput.val(0);
        }

        var discountInput = $('input[name="discount"]', currentRow);
        var discount = discountInput.val();
        if (discount == '') {
            $(this).parent().parent().find('.discount').val(0);
        }

        var goodsAmount = $('input[name="goodsAmount"]', currentRow);
        goodsAmount.val((goodsPrice * goodsNumber).toFixed(2));

        // 核算价格
        calculatePriceUtil.drawup();
    });

    // 物料.优惠
    var basicGoodsScope = $("#orderGoodsTB");
    $(basicGoodsScope).on('blur', 'input[name="discount"]', function () {
        var discount = $(this).val();
        if (!$.isNumeric(discount) || discount < 0) {
            $(this).val(0);
            return;
        }

        // 检验优惠金额 >总金额
        var currentRow = $(this).parents(".goods-datatr");
        var goodsAmount = $('input[name="goodsAmount"]', currentRow).val();
        if (parseFloat(discount) > parseFloat(goodsAmount)) {
            $(this).val(0);
            return;
        }

        // 核算价格
        calculatePriceUtil.drawup();
    });

    // 批量添加物料
    batchAddPart({
        dom: '.js-batch-add-part-btn',
        callback: addGoods
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
        $('#orderGoodsTB').append(goodsHtml);

        calMoney('goods', 'service');
        $('.js-goods-num').trigger('input');

    }
    function calMoney(type, otherType) {
        var sum = 0;
        var amount, discount;
        var $tr, otherSum = +$('#' + otherType + 'MoneySum').text();

        $('.' + type + '-datatr').each(function () {
            $tr = $(this);

            amount = +$tr.find('.js-' + type + '-amount').val();
            discount = +$tr.find('.js-' + type + '-discount').val();

            discount = discount < 0 || !discount ? 0 : discount;

            sum += amount - discount;
        });
        if (sum < 0 || !sum || sum !== sum) {
            sum = 0;
        }

        $('#' + type + 'TotalAmount').text(sum);
        $('#orderTotalAmount').text(sum + otherSum);
    }

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
});

calculatePriceUtil.drawup();




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
    dg.titleInit();
    var doc = $(document);

    ck.init();

    // 更多车辆信息
    $('.js-tocustomer').on('click', function () {
        var customerCarId = $("input[name='orderInfo.customerCarId']").val();
        if (typeof(customerCarId) != 'undefined' && customerCarId != "") {
            window.open(BASE_PATH + '/shop/customer/car-detail?refer=common&id=' + customerCarId);
        } else {
            dg.warn('请输入有记录的车牌号')
        }
    });

    //洗车工下拉列表初始化。
    workerInit({
        dom: '.js-worker',
        hiddenDom: 'input[name="workerIds"]'
    });


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
        var data = fd.get('.base-box', true),
            serviceList = [],
            goodsList = [];
        // 基本服务
        $('tr', '#orderServiceTB').each(function () {
            var item = fd.get($(this));
            item.serviceNote = item.serviceNote || "";
            serviceList.push(item);
        });
        //如果是委托单转工单，则需要添加相应的服务,且工单金额必须和委托单的金额相等
        if($("input[name='orderInfo.proxyType']").val() == 2){
            if(serviceList.length == 0){
                dg.fail("请选择委托项目对应的服务");
                return false;
            }
            var orderTotalAmount = $("#orderTotalAmount").html();
            var proxyAmount = $("#proxyAmount").html();
            if(proxyAmount && orderTotalAmount != proxyAmount){
                dg.fail("委托金额和工单总金额不一致，无法开单");
                return false;
            }
        }
        // 附加费用服务
        $('tr', '#additionalOrderServiceTB').each(function () {
            var item = fd.get($(this));
            item.serviceNote = item.serviceNote || "";
            serviceList.push(item);
        });
        $('tr', '#orderGoodsTB').each(function () {
            var item = fd.get($(this));
            item.goodsNote = item.goodsNote || "";
            goodsList.push(item);
        });

        data["orderInfo.postscript"] = $("input[name='orderInfo.postscript']").val();

        // 服务列表
        data["orderServiceJson"] = JSON.stringify(serviceList);
        //配件列表
        data.orderGoodJson = JSON.stringify(goodsList);
        //预检单信息
        if($("div").hasClass("carBox")){
            data.orderPrecheckDetailsJson = JSON.stringify(getCheckCarFormdata());
        }

        //数据发送
        var orderId = $("input[name='orderInfo.id']").val();
        var requestUrl = BASE_PATH + '/shop/order/common-save';
        if (orderId > 0) {
            requestUrl = BASE_PATH + '/shop/order/common-update';
        }
        //渠道过滤
        var channelId = data["orderInfo.channelId"];
        if(channelId == undefined || channelId <= 0){
            data["orderInfo.channelName"] = '';
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
    }


    // 下次保养里程校验
    doc.on("blur","input[name='orderInfo.upkeepMileage']",function(){
        var mileage = $("input[name='orderInfo.mileage']").val();
        var upkeepMileage = $("input[name='orderInfo.upkeepMileage']").val();
        if (!$.isNumeric(mileage) || mileage < 0 || !$.isNumeric(upkeepMileage) || upkeepMileage < 0 || parseInt(mileage) < parseInt(upkeepMileage)) {
            return;
        }
        dg.warn("下次保养里程不能小于或等于行驶里程");
        $("input[name='orderInfo.upkeepMileage']").val(upkeepMileage);

    });

    // 开单|编辑保存
    doc.on('click', '.js-create', function () {
        if ( !ck.check() ) return false;
        saveAndUpdate(function (json) {
            dg.success(['保存成功，', 3, '秒后跳转至工单详情页'], function () {
                window.location.href = BASE_PATH + "/shop/order/detail?orderId=" + json["data"];
            })
        });
    });

    //返回
    doc.on('click', '.js-return', function() {
        util.goBack();
    });

    // 销售员下拉列表初始化。
    st.init({
        dom: ".js-speedily-sale",
        url: BASE_PATH + '/shop/manager/get_manager',
        showKey: "id",
        showValue: "name"
    });

    // 配置的日期
    date.datePicker('.js-create-time', {
        maxDate: '%y-%M-%d',
        dateFmt: 'yyyy-MM-dd HH:mm'
    });
    date.datePicker('.js-expected-time', {
        minDate: '%y-%M-%d',
        dateFmt: 'yyyy-MM-dd HH:mm'
    });
    date.datePicker('.js-keepup-time-date', {
        minDate: '%y-%M-%d',
        dateFmt: 'yyyy-MM-dd'
    });
    // 保险到期
    date.datePicker('.js-insurance-time', {
        dateFmt: 'yyyy-MM-dd'
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
        canInput: true,
        showKey: "id",
        showValue: "name"
    });
    // 对方保险初始化。
    st.init({
        dom: ".js-otherinsurance",
        url: BASE_PATH + '/insurance/list',
        canInput: true,
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
            $('#additionalOrderServiceTB').append(html);

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


    // 计算下次保养里程
    var getUpkeepMileage = function () {
        var carId = $("input[name='orderInfo.customerCarId']").val();
        var carModelId = $("input[name='orderInfo.carModelsId']").val();
        var mileage = $("input[name='orderInfo.mileage']").val();

        if (!$.isNumeric(carModelId) || !$.isNumeric(mileage) || mileage <= 0) {
            return;
        }

        $.ajax({
            type: 'get',
            data: {
                carId: carId,
                carModelId: carModelId,
                mileage: mileage
            },
            url: BASE_PATH + "/shop/order/get_upkeep_mileage"
        }).done(function (json) {
            if (json != null && json != undefined && json.success) {
                $("input[name='orderInfo.upkeepMileage']").val(json.data);
            } else {
                $("input[name='orderInfo.upkeepMileage']").val(null);
            }
        });
    };

    // 车牌初始化
    dl.init({
        url: BASE_PATH + '/shop/customer/search/mobile',
        searchKey: 'com_license',
        tplCols: {'license': '车牌'},
        showKey: 'license',
        tplId: 'carLicenceTpl',
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
                        var carYear = json.data.carYear != null ? json.data.carYear : "";
                        var carGearBox = json.data.carGearBox != null ? json.data.carGearBox : "";
                        var carPower = json.data.carPower != null ? json.data.carPower : "";
                        if(carYear != "" && (carGearBox != "" || carPower != "")){
                            if(carGearBox != ""){
                                $("input[name='yearPowerBak']").val(carYear + " " + carGearBox);
                            }else if (carPower != ""){
                                $("input[name='yearPowerBak']").val(carYear + " " + carPower);
                            }
                        }else{
                            $("input[name='yearPowerBak']").val("");
                        }
                        $("input[name='orderInfo.carBrandId']").val(json.data.carBrandId);
                        $("input[name='orderInfo.carBrand']").val(json.data.carBrand);
                        $("input[name='orderInfo.carSeriesId']").val(json.data.carSeriesId);
                        $("input[name='orderInfo.carSeries']").val(json.data.carSeries);
                        $("input[name='orderInfo.carModelsId']").val(json.data.carModelId);
                        $("input[name='orderInfo.carModels']").val(json.data.carModel);

                        $("input[name='orderInfo.importInfo']").val(json.data.importInfo);
                        $("input[name='orderInfo.customerName']").val(json.data.customerName);
                        $("input[name='orderInfo.customerMobile']").val(json.data.mobile);
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
                        //承保公司
                        $("input[name='orderInfo.insuranceCompanyName']").val(json.data.insuranceCompany);
                        $("input[name='orderInfo.insuranceCompanyId']").val(json.data.insuranceId);
                        // VIN码
                        $("input[name='orderInfo.vin']").val(json.data.vin);
                        // 客户单位
                        $("input[name='orderInfo.company']").val(json.data.company);
                        //颜色
                        $("input[name='orderInfo.carColor']").val(json.data.color);
                        // 保险到期
                        $("input[name='orderInfo.insuranceTimeStr']").val(json.data.insuranceTimeStr);
                        // 如果下次保养里程大于行驶里程,则不获取推荐的下次保养里程
                        if (!(mileage && upkeepMileage && (upkeepMileage - mileage > 0))) {
                            //计算下次保养里程
                            getUpkeepMileage();
                        }
                    }
                }
            });
        },
        clearCallbackFn: function() {
            var nameList = ['orderInfo.customerCarId', 'carModeBak', 'orderInfo.carBrandId',
                'orderInfo.carBrand', 'orderInfo.carSeriesId', 'orderInfo.carSeries',
                'orderInfo.carModelsId', 'orderInfo.carModels', 'orderInfo.importInfo',
                'orderInfo.customerName', 'orderInfo.customerMobile', 'orderInfo.contactName',
                'orderInfo.contactMobile', 'orderInfo.mileage', 'orderInfo.upkeepMileage',
                'customerCar.keepupTimeStr', 'orderInfo.company'];
            //清空customerCarId
            $.each(nameList, function(i, v) {
                $('input[name="' + v + '"]').val('');
            })
        }
    });

    // 添加基本服务
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
                            var html = at("serviceTpl", {json: packageJson.data.shopServiceInfoList, parentService: packageJson.data.shopServiceInfo});
                            $('#orderServiceTB').append(html);
                            var googsList = packageJson.data.goodsList;
                            var goodsHtml = at("goodsTpl", {json: packageJson.data.goodsList});
                            $('#orderGoodsTB').append(goodsHtml);

                            //有物料显示提示框
                            var tip = $(".tip");
                            if (googsList && googsList.length > 0) {
                                tip.removeClass('hide');
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

    doc.on('blur','input[name="goodsPrice"]',function(){
        var price = $(this).val(),
            currentPrice= $(this).parents(".goods-datatr"),
            inventoryPrice = $('input[name="inventoryPrice"]', currentPrice).val();

        if( Number(price) < Number(inventoryPrice)){
            dg.warn("售价不能小于成本价")
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
        var currentRow =$(this).parents('tr');

        // 配件物料 新增/删除 ：
        // IF(物料删除 AND 出库数量 !=0) THEN 不可以删除
        var outNumber =$('input[name="outNumber"]', currentRow).val();
        if(typeof(outNumber) !="undefined"
            && parseInt(outNumber) !=0){
            dg.warn("该配件已出库，不能删除!");
            return false;
        }

        if(currentRow.find('.input-suiteNum').eq(0).val() > 0) {
            dg.warn("此服务套餐包含配件物料，请自行删除！");
        }

        currentRow.remove();

        // 计算价格
        calculatePriceUtil.drawup();
    });

    doc.on('click','.js-tab-item',function(){
        $(this).find('.js-tip').addClass('hide');
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
    var additionalServiceScope = $("#additionalOrderServiceTB");
    $(additionalServiceScope).on('blur', 'input[name="discount"]', function () {
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


    //校验是否有权限展示渠道开单
    if($("div").hasClass('js-check-channel')){
        //初始化渠道
        st.init({
            dom: "input[name='orderInfo.channelName']",
            url: BASE_PATH + '/shop/order/get-channel',
            showKey: "id",
            showValue: "channelName",
            pleaseSelect:true
        });
        return;
    }
});

calculatePriceUtil.drawup();




$(function () {

    var yesTime = 3;

    //第一次进入设置洗车价格,显示洗车价格弹窗
    var dialogIndex = null;

    var sid = $("#shopGroupId").val();

    // 会员卡实例
    var settleTypeVIPCard;

    // 优惠金额会员卡实例
    var discountAmountVIPCard;

    var discountAmountVIPCardSelected = false;
    
    // 最近一次结算方式选择
    var stLastIndex;
    var guestMobile;

    // 应收金额
    var receivableAmount = 0;

    // 会员卡说明
    var vipCardNote;

    var
    // 会员卡容器
        $settMemberCardBox = $('#settMemberCardBox');

    var model = {
        getCarByLicense: function (license) {
            var params = {
                carLicense: license
            };

            return $.ajax({
                url: BASE_PATH + '/shop/customer_car/get_car_by_license',
                data: params
            });
        },
        discountCarWashOrder: function (chooseDiscountBo) {
            return $.ajax({
                method: 'post',
                data: JSON.stringify(chooseDiscountBo),
                contentType: 'application/json'
            });
        },
        saveCarWashOrder: function (params) {
            return $.ajax({
                type: 'post',
                url: BASE_PATH + '/shop/order/carwash/save',
                contentType: 'application/json',
                data: JSON.stringify(params)
            });
        },
        saveServiceList: function (params) {
            return $.ajax({
                type: 'post',
                url: BASE_PATH + "/shop/shop_service_info/washcar/save_service_list",
                contentType: 'application/json',
                data: JSON.stringify(params)
            });
        }
    };

    /**
     * 创建会员卡结算方式
     */
    function createSettleTypeVIPCard () {
        destroySettleTypeVIPCard();
        settleTypeVIPCard = new Components.ChooseVIPCard({
            scope: '#settMemberCardBox',
            type: 2,
            tplType: 2,
            localSelect: true,
            inputTelFn: function (tel) {
                guestMobile = tel;
            },
            params: getCarWashOrderParams,
            selectedCardFn: function (cardInfo, selected, index) {
                // 更新优惠金额
                if (selected) {
                    discountAmountVIPCard.disabledCard(true, index);
                } else {
                    discountAmountVIPCard.disabledCard(false);
                }
            },
            updateViewFn: function () {
                if (!this.hasAvailableCard()) {
                    vipCardNote = '<p class="note"><i class="icon-exclamation-sign no-sign"></i>该车辆没有绑定会员卡</p>';
                    $settMemberCardBox.html(vipCardNote);
                }

                calcAccountsReceivable();
            }
        });

        distribute(-1, null, null, null, true);
    }

    /**
     * 销毁会员卡结算方式
     */
    function destroySettleTypeVIPCard () {
        if (settleTypeVIPCard) {
            settleTypeVIPCard.unbindUpdateView();
            settleTypeVIPCard = null;
        }
    }

    /**
     * 分发请求
     * @param guestMobile
     * @param selectedCard
     * @param selectedComboList
     * @param selectedCouponList
     * @param cache 是否读取缓存
     */
    function distribute (guestMobile, selectedCard, selectedComboList, selectedCouponList, cache) {
        var type, params;
        if (cache === true) {
            type = 0;
            params = null;
        } else {
            type = 2;
            params = getCarWashOrderParams(guestMobile, selectedCard, selectedComboList, selectedCouponList);
        }

        Components.$broadcast.distribute(Components.$broadcast.topic, type, params);
    }

    /**
     * 获得洗车单优惠请求参数
     * @param guestMobile
     * @param selectedCard
     * @param selectedComboList
     * @param selectedCouponList
     * @return { Object }
     *   @property carLicense
     *   @property carWashServiceId
     *   @property carWashAmount
     *   @property discountSelectedBo
     *
     */
    function getCarWashOrderParams (guestMobile, selectedCard, selectedComboList, selectedCouponList) {
        var data = getServiceAmount(),
            discountSelectedBo = _getDiscountSelectedBo(guestMobile, selectedCard, selectedComboList, selectedCouponList);

        data['carLicense'] = $('.js-carlicense-input').val();

        return $.extend(true, {}, data, discountSelectedBo);
    }

    /**
     * 获得已选择的优惠券
     * @param guestMobile
     * @param selectedCard
     * @param selectedComboList
     * @param selectedCouponList
     * @returns { Object }
     *   @property discountSelectedBo
     */
    function _getDiscountSelectedBo (guestMobile, selectedCard, selectedComboList, selectedCouponList) {
        var data = {};

        if (guestMobile !== undefined && guestMobile !== -1) {
            data['guestMobile'] = guestMobile;
        }

        if (selectedCard !== undefined && selectedCard !== -1) {
            data['selectedCard'] = selectedCard;
        }

        if (selectedComboList !== undefined && selectedComboList !== -1) {
            data['selectedComboList'] = selectedComboList;
        }

        if (selectedCouponList !== undefined && selectedCouponList !== -1) {
            data['selectedCouponList'] = selectedCouponList;
        }

        return { discountSelectedBo : data };
    }

    /**
     * 获得服务项目的服务Id和服务金额
     * @returns {{carWashServiceId: *, carWashAmount: *}}
     */
    function getServiceAmount () {
        var $carWashService = $('.js-in-money > li.selected'),
            carWashServiceId, carWashAmount;

        if ($carWashService.hasClass('js-bz-service')) {
            // 标准服务
            carWashServiceId = $carWashService.data('id');
            carWashAmount = $carWashService.data('servicePrice');
        } else if ($carWashService.hasClass('js-more-service')) {
            // 更多洗车
            carWashServiceId = $('.js-id', $carWashService).text();
            carWashAmount = $('.js-serviceprice', $carWashService).text();
        } else if ($carWashService.hasClass('js-custom-service')) {
            // 自定义
            carWashServiceId = null;
            carWashAmount = $('.js-custom-money', $carWashService).val();
        }

        return {
            carWashServiceId: carWashServiceId,
            carWashAmount: isNaN(carWashAmount) ? 0 : carWashAmount
        }
    }

    /**
     * 获得结算方式--所有优惠券的总额
     * @returns {number}
     */
    function getDiscountAmount () {
        var orderDiscountAmount = 0;

        if ($('.coupon-and-combo').hasClass('selected')) {
            // 遍历选中的优惠券
            $('.js-coupon').not('div-disable').each(function () {
                var $this = $(this);
                if ($this.find('.voucher-t').hasClass('voucher-hover')) {
                    orderDiscountAmount += Number($.trim($this.find('.js-discount-amount').val())) || 0;
                }
            });

            // 遍历选中的计次卡
            $('.js-combo').not('div-disable').each(function () {
                var $this = $(this);
                if ($this.find('.voucher-t').hasClass('voucher-hover')) {
                    orderDiscountAmount += Number($.trim($this.find('.js-discount-amount').val())) || 0;
                }
            });
        }

        return isNaN(orderDiscountAmount) || orderDiscountAmount == '' ? 0 : orderDiscountAmount;
    }

    /**
     * 计算应收金额
     */
    function calcAccountsReceivable() {
        var
            $discountAmount = $('.js-discount-money'),
        // 获得服务项目金额
            serviceAmount = getServiceAmount().carWashAmount,
        // 获得优惠金额
            vipCardDiscount = $discountAmount.val(),
        // 获得结算方式
            settleTypeDiscount = getDiscountAmount();

        if (isNaN(vipCardDiscount)) {
            vipCardDiscount = 0;
        }

        // 计算应收金额   应收金额 = 服务项目金额 - 优惠金额 - 结算方式优惠
        receivableAmount = Number((serviceAmount - vipCardDiscount - settleTypeDiscount).toFixed(2));

        // 优惠金额不能大于应收金额
        if (receivableAmount < 0) {
            seajs.use('dialog', function (dg) {
                dg.warn('优惠金额不能大于应收金额！');
            });
            $discountAmount.val('0.00');
            // 重新计算应收金额
            receivableAmount = Number((serviceAmount - settleTypeDiscount).toFixed(2));
        }

        $('.js-cash').text(receivableAmount.toFixed(2));
    }

    /**
     * 检查修改金额
     */
    function checkMoney() {
        var val = checkFloat(this.value, 2);
        this.value = val == '.' ? '0.00' : val;
    }

    /**
     * 管理结算方式视图
     * @param index
     */
    function settleManage(index) {
        var $settleTypeBox = $('.sett-type-box');
        $settleTypeBox.not(':eq(' + index + ')').hide(300);
        $settleTypeBox.eq(index).show(300);

        // 缓存本次选中结算方式
        stLastIndex = index;
    }

    function getSelectedCardData() {
        var selectedData = discountAmountVIPCard.getSelectedData();
        if (selectedData) {
            return selectedData;
        } else if (settleTypeVIPCard) {
            selectedData = settleTypeVIPCard.getSelectedData();
            return selectedData;
        }
        return null;
    }
    /**
     * 提交洗车单
     * @param confirmBillBo
     * @param dg
     */
    function confirmBill(confirmBillBo, dg) {
        //使用他人卡券需要发短信验证
        var otherVoucher = $('#guestCouponConunt').find('.voucher-t').hasClass('voucher-hover');

        var vipCardData = getSelectedCardData();
        if (otherVoucher || (guestMobile && vipCardData && guestMobile == vipCardData.mobile)) {
            //使用其他客户优惠券弹窗
            var name = $('#guestCouponConunt').find('.voucher-hover .vocher-name').eq(0).text() || vipCardData.customerName;
            var mobile = $('.js-guestMobile').val() || vipCardData.mobile;
            var carLicense = $.trim($('input[name="carLicense"]').val());
            var data = {
                name: name,
                mobile: mobile,
                license: carLicense
            };
            Components.codeDialog({
                data: data,
                callback: function () {
                    carWashSaveBill(confirmBillBo, dg);
                }
            })
        } else {
            carWashSaveBill(confirmBillBo, dg);
        }
    }

    function carWashSaveBill(data, dg){
        //提交数据。
        model.saveCarWashOrder(data)
            .done(function (json) {
                if (json.success) {
                    var isSign = json['data']['isSign'];
                    var signAmount = json['data']['signAmount'];
                    var orderId = json['data']['orderId'];

                    var title = '洗车单结算成功，' + yesTime + '秒后自动跳转到洗车单详情页';
                    var locationUrl = '/shop/order/detail?refer=carWash&orderId=' + orderId;

                    // IF全部结算 THEN 跳转到详情页 ELSE 跳转到收款页
                    if (Number(isSign) == 1) {
                        //校验结算权限
                        var funcList = '结算首页,财务首页';
                        var func = util.checkFuncList(funcList);
                        if (func) {
                            title = '洗车单结算成功 挂账' + signAmount + '元, <br\/> ' + yesTime + '秒后自动跳转到收款页';
                            locationUrl = "/shop/settlement/debit/order-debit?orderId=" + orderId;
                        }
                    }

                    dg.success(title, function () {
                        window.location = BASE_PATH + locationUrl;
                    });

                } else {
                    dg.fail(json.message);
                }
            });
    }

    seajs.use(['dialog', 'art', 'formData', 'select', 'downlist', 'date', 'check', 'ajax'],
        function (dg, at, fd, st, dl, date, ck) {

            var doc = $(document),
            // 挂账按钮
                $losses = $('#losses');

            var
            // 付款/挂账提示
                settleNote = '预付定金大余应收金额，请确认工单?';

            //成功信息停留时间
            var yesTime = 3;

            // 验证
            ck.init();

            dg.titleInit();

            date.datePicker('.js-createTime', {
                maxDate: '%y-%M-%d',
                dateFmt: 'yyyy-MM-dd HH:mm'
            });

            //服务顾问select下拉列表初始化。
            st.init({
                dom: ".js-carwash-receiver",
                url: BASE_PATH + '/shop/manager/get_manager',
                showKey: "id",
                showValue: "name"
            });

            //洗车工下拉列表初始化。
            workerInit({
                dom: '.js-carwash-worker',
                hiddenDom: 'input[name="workerIds"]'
            }, BASE_PATH + '/shop/manager/get-carwash-worker');

            // 初始化车牌下拉框
            dl.init({
                dom: 'input[name="carLicense"]',
                url: BASE_PATH + '/shop/customer/search/mobile',
                searchKey: 'com_license',
                showKey: 'license',
                hasTitle: false,
                notClearInput: true,
                hasInput: false,
                callbackFn: function (obj, item) {
                    model.getCarByLicense(item['license'])
                        .done(function (json) {
                            if (json.success) {
                                // 客户单位
                                $("input[name='company']").val(json.data.company);
                                initAccount(item['license']);
                            }
                        });
                }
            });

            /**
             * 换车牌时重新初始化账户信息
             */
            $(document).on('input','input[name="carLicense"]',function(){
                var $this = $(this);
                if(!ck.check()){
                    return false;
                }
                setTimeout(function(){
                    initAccount($this.val());
                },300);
            });

            /**
             * 重新初始化账户信息
             *
             * @param carLicense
             */
            function initAccount(carLicense){
                // 初始化会员卡信息
                discountAmountVIPCard.init();
                settleTypeVIPCard && settleTypeVIPCard.init();
                discountAmountVIPCardSelected = false;
                distribute(null, null, null, null);
                Components.cleanCouponCache();//先清理缓存
                Components.CouponCard.init({
                    carLicense:carLicense,
                    type:2
                });
                $('.js-sett-type li').eq(0).click();
            }

            //初始化客户单位下拉框
            dl.init({
                url: BASE_PATH + '/shop/customer/search/company',
                showKey: 'company',
                searchKey: 'company',
                notClearInput: true,
                tplCols: {'company': '客户单位'},
                dom: 'input[name="company"]',
                hasInput: false
            });

            // 实例化优惠金额会员卡
            discountAmountVIPCard = new Components.ChooseVIPCard({
                scope: '#discountAmountVIPCardBox',
                type: 2,
                params: getCarWashOrderParams,
                autoSelected: 1,
                inputTelFn: function (tel, changed) {
                    changed && createSettleTypeVIPCard();
                },
                selectedCardFn: function (cardInfo, selected) {
                    var index = $('.js-sett-type li.selected').index();

                    discountAmountVIPCardSelected = selected;

                    // 结算方式——会员卡更新
                    if (index == 1) {
                        if (selected) {

                            destroySettleTypeVIPCard();
                            // 如果"优惠金额"没有选择会员卡，#settMemberCardBox(结算方式)中的会员卡余额容器将被填充
                            $settMemberCardBox.html(vipCardNote);
                        } else {

                            settleTypeVIPCard || createSettleTypeVIPCard();
                        }
                    }
                },
                updateViewFn: function (data, params) {
                    var cardInfo = this.getSelectedData();
                    guestMobile = params.discountSelectedBo.guestMobile;
                    // 优惠金额更新
                    $('.js-discount-money').val(cardInfo ? cardInfo.discount.toFixed(2) : '0.00');

                    calcAccountsReceivable();

                    if (cardInfo) {
                        vipCardNote = '<p class="vip-note">说明：使用已选会员卡<em>' + cardInfo.cardNumber +
                            '（' + cardInfo.customerName + '，' + cardInfo.mobile + '）' + '</em>结算，卡内余额<em>' +
                            cardInfo.balance.toFixed(2) + '</em>元' + (cardInfo.balance > receivableAmount ? '' : '，请充值') + '</p>';
                        $('.js-discount-box').addClass('selected');
                    } else {
                        $('.js-discount-box').removeClass('selected');
                    }

                    if (discountAmountVIPCardSelected) {
                        $settMemberCardBox.html(vipCardNote);
                    }
                }
            });

            // 服务项目--单选
            doc.on('click', '.js-in-money li', function () {
                var $this = $(this);

                if ($this.hasClass('selected') || !ck.check()) {
                    return false;
                }

                $this.addClass('selected').siblings().removeClass('selected');

                if (!$this.hasClass('js-more-service')) {
                    Components.cleanCouponCache();//服务更改，清理缓存
                    distribute(-1,-1,null,null);
                }
            });

            // 结算方式--单选
            doc.on('click', '.js-sett-type li', function () {
                var $this = $(this);
                if ($this.hasClass('selected') || !ck.check()) {
                    return false;
                }

                var index = $this.index(),
                    carLicense = $.trim($('input[name="carLicense"]').val()),
                    selectedVIPCardInfo = discountAmountVIPCard.getSelectedData();
                
                // 防止重复点击同一个结算方式
                if (stLastIndex == index) {
                    return;
                }

                $this.addClass('selected').siblings().removeClass('selected');

                // 0:现金，1:会员卡，2:优惠券，3:其他
                index == 1 || index == 2 ? $losses.hide() : $losses.show();

                if (stLastIndex == 1 && index == 2) {
                    // 如果没有选中会员卡，清除selectedCard
                    selectedVIPCardInfo ? distribute(-1, -1, null, null) : distribute(-1, null, null, null);
                }
                
                // 如果从“优惠券”跳转到其他结算方式，更新卡券视图
                stLastIndex == 2 && distribute(-1, -1, null, null);

                if (index == 1) {

                    if (selectedVIPCardInfo) {

                        // 如果"优惠金额"没有选择会员卡，#settMemberCardBox(结算方式)中的会员卡余额容器将被填充
                        $settMemberCardBox.html(vipCardNote);
                    } else {

                        createSettleTypeVIPCard();
                    }
                } else {

                    destroySettleTypeVIPCard();
                }

                // 各个结算方式的内容容器管理
                settleManage(index);
            });

            /**
             * 车牌输入
             */
            doc.on('input', '.js-carlicense-input', function () {
                stLastIndex == 1 || stLastIndex == 2 ? $losses.hide() : $losses.show();
            });

            // 结算方式——其他结算方式
            doc.on('click', '.js-sett-type-other li', function () {
                $(this).addClass('selected').siblings().removeClass('selected');
            });

            // 优惠金额
            doc.on('input', '.js-discount-money', function () {
                if (!ck.check()) {
                    $(this).val(0);
                    return false;
                }
                checkMoney.call(this);
                var memberDiscountAmount = this.value;
                var carWashAmount = getServiceAmount().carWashAmount;

                // 输入优惠金额 > 洗车总价
                if (Number(memberDiscountAmount) > Number(carWashAmount)) {
                    ck.showCustomMsg('您输入的“会员卡的优惠金额”不能大于“洗车金额”！', this);
                    this.value = '0.00';
                }
                calcAccountsReceivable();
            });

            //提交结算 ssign{1:挂账 ; 0:全部支付}
            var dosettle = function (issign) {
                //先校验信息。
                var result = ck.check();
                if (!result) {
                    return;
                }

                //获取数据并组装数据。
                var data = fd.get(".carwash_form");

                if (typeof(issign) === 'undefined' ||
                    issign === "") {
                    issign = Number(0);
                }

                // 是否全部支付
                data["isSign"] = Number(issign);
                var service_check_box = $(".js-in-money li.selected");
                var serviceType;
                var serviceName;
                var payPrice;
                var servicecatid;
                var serviceCatName;
                var serviceSn;
                var serviceId;
                if (service_check_box.hasClass("js-more-service")) {
                    serviceType = $(".js-type").text();
                    serviceName = $(".js-servicename").text();
                    payPrice = $(".js-serviceprice").text();
                    servicecatid = $(".js-servicecatid").text();
                    serviceCatName = $(".js-servicecatname").text();
                    serviceSn = $(".js-servicesn").text();
                    serviceId = $(".js-id").text();
                } else {
                    var money_check_box = $(".js-in-money li.selected");
                    servicecatid = money_check_box.data("servicecatid");
                    if (typeof(servicecatid) === 'undefined' || servicecatid == "") {
                        servicecatid = "0"; //洗车服务类别
                    }
                    // 服务类别
                    serviceType = money_check_box.data("type");
                    if (typeof(serviceType) === 'undefined' || serviceType == "") {
                        serviceType = "1";// 基础服务
                    }
                    // 服务名称
                    serviceName = money_check_box.data("servicename");
                    if (typeof(serviceName) === 'undefined' || serviceName == "") {
                        serviceName = "洗车";
                    }

                    //洗车单支付的金额
                    payPrice = $(".money", money_check_box).text();
                    if (payPrice == "") {
                        //自定义金额
                        payPrice = $("input", money_check_box).val();
                    }
                    serviceCatName = money_check_box.data("servicecatname");
                    serviceSn = money_check_box.data("servicesn");
                    serviceId = money_check_box.data("id");
                }
                //收款金额列表数据组装
                data.orderServiceJson = JSON.stringify({
                    "serviceId": serviceId,
                    "serviceSn": serviceSn,
                    "type": serviceType,
                    "serviceName": serviceName,
                    "serviceCatId": servicecatid,
                    "serviceCatName": serviceCatName,
                    "servicePrice": payPrice,
                    "serviceHour": "1",
                    "serviceAmount": payPrice,
                    "discount": "0",
                    "soldPrice": payPrice,
                    "soldAmount": payPrice
                });

                // 本次支付金额
                var payAmount = Number(payPrice);
                if (issign == 1) {
                    payAmount = Number(0);
                }
                //优惠
                if (discountAmountVIPCard && discountAmountVIPCard.getSelectedData()) {
                    var memberCard = discountAmountVIPCard.getSelectedData();
                    data.memberCardId = memberCard.cardId;
                    data.cardNumber = memberCard.cardNumber;
                    data.cardDiscountReason = memberCard.discountDesc;
                    data.accountId = memberCard.accountId;
                }
                data.discountAmount = $('.js-discount-money').val();
                // 支付渠道
                var pay_check_box = $(".js-sett-type li.selected");
                var index = $(".js-sett-type li").index(pay_check_box);
                // 0:现金结算 1:会员卡 2:优惠券结算 3:其他结算
                if (index == 0) {
                    data.paymentJson = JSON.stringify([{
                        "paymentId": pay_check_box.data("id"),
                        "paymentName": $.trim(pay_check_box.text()),
                        "paymentValue": payAmount
                    }]);
                    confirmBill(data, dg);
                    return false;
                }
                // 会员卡结算
                if (index == 1) {
                    // 会员id
                    var memberCard = getSelectedCardData();
                    if(!memberCard){
                        dg.warn("请选择需要结算的会员卡,或请选其他方式结算");
                        return false;
                    }
                    var cardMemberId = memberCard.cardId;
                    var cardSn = memberCard.cardNumber;
                    var balance = memberCard.balance;
                    // 会员卡是否存在
                    if (typeof(cardMemberId) === 'undefined' || cardMemberId == "") {
                        dg.warn("没有绑定会员卡,请选其他方式结算");
                        return false;
                    }
                    if(balance <= 0){
                        dg.warn("会员卡余额不足，请选其他方式结算");
                        return false;
                    }
                    // 会员卡结算方式
                    data.paymentJson = "";
                    data.cardMemberJson = JSON.stringify({
                        "id": cardMemberId,
                        "cardSn": cardSn,
                        "memberPayAmount": payAmount,
                        "balance": balance
                    });
                    confirmBill(data, dg);
                    return false;
                }

                // 优惠券结算
                data.couponDetailJson = "";
                //优惠券结算方式

                if (index == 2) {
                    //券信息
                    var orderDiscountFlowBoList = Components.getVoucherData();

                    if (orderDiscountFlowBoList.length == 0) {
                        dg.warn("未选择优惠券或此账户没有优惠券，请选择其他结算方式结算");
                        return false;
                    }
                    //遍历优惠券，提示是否有优惠的券金额为0的
                    var flags = false;
                    if (orderDiscountFlowBoList.length > 0) {
                        for (var i in orderDiscountFlowBoList) {
                            var orderDiscountFlowBo = orderDiscountFlowBoList[i];
                            var discountAmount = orderDiscountFlowBo.discountAmount;
                            if (discountAmount <= 0) {
                                flags = true;
                            }
                        }
                    }
                    data.couponDetailJson = JSON.stringify(orderDiscountFlowBoList);
                    if (flags) {
                        dg.confirm("有优惠券优惠金额为0，您确定要确认账单吗？", function () {
                            //继续下面逻辑
                            confirmBill(data, dg);
                            return false;
                        }, function () {
                            return false;
                        });
                    } else {
                        confirmBill(data, dg);
                        return false;
                    }
                }

                // 其他结算
                if (index == 3) {
                    var scope = $('.sett-type-box').eq(index);
                    var other_box = $('li.selected', scope);
                    var payId = other_box.data("id");
                    if (typeof(payId) === 'undefined' || payId === "") {
                        dg.warn("未选择结算方式");
                        return false;
                    }

                    data.paymentJson = JSON.stringify([{
                        "paymentId": other_box.data("id"),
                        "paymentName": other_box.text(),
                        "paymentValue": payAmount
                    }]);
                    confirmBill(data, dg);
                    return false;
                }
            };

            //确认结算
            doc.on('click', '#settle', function () {
                var price = $('.js-cash').text();

                if (Number(price) < 0) {
                    dg.fail('应收金额应大于等于0');
                    return false;
                }

                //校验结算权限
                if (!util.checkFuncList('结算首页,财务首页')) {
                    return false;
                }
                //如果有预付定金,且预付定金大于工单应收金额，则提示是否收款
                var downPayment = $('.js-downPayment').val();

                if (downPayment != '' && Number(price) < Number(downPayment)) {
                    dg.confirm(settleNote, function () {
                        dosettle(0);
                    });
                } else {
                    dosettle(0);
                }
            });

            //挂账
            doc.on('click', '#losses', function () {
                //如果有预付定金,且预付定金大于工单应收金额，则提示是否收款
                var price = $('.js-cash').text();
                var downPayment = $('.js-downPayment').val();
                if (downPayment != '' && Number(price) < Number(downPayment)) {
                    dg.confirm(settleNote, function () {
                        dosettle(1);
                    });
                } else {
                    dosettle(1);
                }
            });

            //关闭洗车价格弹窗
            doc.on('click', '.setting_carwash_cancel', function () {
                dg.close(dialogIndex);
            });
            //弹窗价格设置数据提交
            doc.on('click', '.setting_carwash_submit', function () {
                var list = [];
                $('.setting_box li').each(function () {
                    var data = fd.get(this);
                    list.push(data);
                });
                model.saveServiceList({shopServiceInfoList: list})
                    .done(function (json) {
                        if (json.success) {
                            dialogIndex && dg.close(dialogIndex);
                            dg.success('洗车价格初始化设置成功', function () {
                                localStorage.setItem('carwash_setting_' + sid, true);
                                location.reload(true);
                            });
                        } else {
                            dg.fail(json.errorMsg);
                        }
                    });
            });

            //返回
            doc.on('click', '.js-return', function () {
                util.goBack();
            });

        });

    //新需求更改   16/9/29 by zmx
    seajs.use(['select', 'dialog', 'check'], function (st, dg, ck) {
        //套餐选择
        st.init({
            dom: '.js-carwash-type',
            url: BASE_PATH + '/shop/order/carwash/get-service',
            showKey: 'id',
            showValue: 'namePrice',
            beforeSelectClick: function () {
                if (!ck.check($('.carwash_form .show-grid').eq(0))) {
                    return false;
                }
            },
            callback: function (key, value, data, index) {
                var t = data[index];
                $('.js-id').text(t.id);
                $('.js-type').text(t.type);
                $('.js-servicesn').text(t.serviceSn);
                $('.js-servicecatid').text(t.categoryId);
                $('.js-servicename').text(t.name);
                $('.js-servicecatname').text(t.categoryName);
                $('.js-serviceprice').text(t.servicePrice);
                $(this).parents('li').click();
                Components.cleanCouponCache();
                distribute(-1,-1,null,null);
            }
        });

        $doc
            .on('input', '.js-custom-money', function () {
                if (!ck.check()) {
                    $(this).val(0);
                    return false;
                }
                checkMoney.call(this);
                Components.cleanCouponCache();//价格更改，清理缓存
                distribute(-1,-1,null,null);
            });

    });

    /*********************************券部分展示******************************/
    var $doc = $(document);
    // 初始化
    //如果有车牌信息（如车辆详情跳过来开洗车单），则查询会员卡信息
    (function initialize () {
        var carLicense = $.trim($("input[name='carLicense']").val());

        if (carLicense.length > 2) {
            distribute(null, null, null, null);
            Components.CouponCard.init({
                carLicense:carLicense,
                type:2
            });
        }

        //服务器标识
        var isFirst = $("#isFirst").val();
        // 本地标识
        var carwash_setting = localStorage.getItem("carwash_setting_" + sid);
        // 第一次 and 未设置过
        if (isFirst == 0 && !carwash_setting) {
            seajs.use(['dialog', 'art'], function (dg, at) {
                var html = at('setting_carwash', at);
                dialogIndex = dg.open({
                    area: ['440px', '300px'],
                    content: html
                });
            });
        }
    })();
});


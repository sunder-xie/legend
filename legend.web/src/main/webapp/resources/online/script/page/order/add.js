var order = order || {};

(function ($, S) {

	S.use("downlist");
	/*
	 * 新增会员卡弹框
	 * */
	$(".addNewMember").click(function () {
		$("input[name='customerName']").val('');
		seajs.use(["dialog"],function(dg){
			dialog = dg.dialog({
				"dom":"#addNew"
			});
		});
	});
	
	// 车牌弹出框，回调
	order.fillCustomer = function (obj, item) {
		var carLicense = item["license"];

		S.use("ajax", function (ajax) {
			ajax.get({
				url: BASE_PATH + '/shop/customer_car/get_car_by_license',
				data: {
					carLicense: carLicense
				},
				success: function (result) {
					if (result.success != true) {
						return false;
					} else {
						var item = result.data;
						// 车主姓名
						$('input[name="orderInfo.customerName"]').val(item["customerName"]);
						// 车主电话
						$('input[name="orderInfo.customerMobile"]').val(item["mobile"]);
						// 联系人姓名
						$('input[name="orderInfo.contactName"]').val(item["contact"]);
						// 联系电话
						$('input[name="orderInfo.contactMobile"]').val(item["contactMobile"]);
						// 车 架 号
						$('input[name="orderInfo.vin"]').val(item["vin"]);
						// 发动机号
						$('input[name="orderInfo.engineNo"]').val(item["engineNo"]);
						// customerCarId
						$('input[name="orderInfo.customerCarId"]').val(item["id"]);
						// 购车日期
						$('input[name="orderInfo.buyTimeYMD"]').val(item["buyTimeStr"]);
						// 地址
						$('input[name="orderInfo.customerAddress"]').val(item["customerAddr"]);
						// 车辆颜色
						$('input[name="orderInfo.carColor"]').val(item["color"]);
						// 行驶里程
						$('input[name="orderInfo.mileage"]').val(item["mileage"]);
						// 承保公司
						$('select[name="orderInfo.insuranceCompanyId"]').val(item["insuranceId"]);
						// 身份证号
						var identityCard = item["identityCard"];
						if (typeof(identityCard) != 'undefined' && identityCard != '') {
							$('input[name="orderInfo.identityCard"]').val(identityCard);
						}

						var carBrand = item["carBrand"];
						var carSeries = item["carSeries"];
						var importInfo = item["importInfo"];
						if (typeof(carBrand) == 'undefined' || carBrand == null) {
							carBrand = "";
						}
						if (typeof(carSeries) == 'undefined' || carSeries == null) {
							carBrand = "";
						}
						// TODO 引入工具类undersource.js
						if (typeof(importInfo) != 'undefined'
							&& importInfo != null
							&& importInfo !=""
						) {
							importInfo = '(' + importInfo + ')';
						} else {
							importInfo = "";
						}
						// 车　　型
						$('input[name="orderInfo.carModel"]').val([carBrand, carSeries, importInfo].join(""));
						// 车别名
						var byName = item["byName"];
						if (typeof(byName) == 'undefined' || byName == null) {
							byName = "";
						}
						$('input[name="orderInfo.carAlias"]').val(byName);

						/**
						 * 直营店会员需要根据车牌获取会员卡信息
						 */
						$cardNum = $("input[name='card_number']");
						if($cardNum.length == 1){
							ajax.get({
								url: BASE_PATH + '/shop/customer_car/getMember',
								data: {
									carLicense: carLicense
								},
								success:function(result){
									if(result && result.success && result.data){
										$cardNum.val(result.data.cardNumber);
									}
								}
							});
						}
					}
				},
				error: function (e) {
					console.log(e);
				}
			});
		});
	};

	//增加新会员
	order.addMember = function(obj, item){
		alert('新增会员');
	};

	order.selectMember = function(obj,item){
		license = $("input[name='orderInfo.carLicense']");
		license.val(item.license);
		order.fillCustomer(license,{license:item.license});
	};
	
	// 服务项目弹出框，回调
	order.fillService = function (obj, item) {
		var currentRow = obj.parents(".form_item");
		var serviceId = item["id"];
		// 关联商品数量
		var suiteNum =item["suiteNum"];


		// IF suiteNum >0 查询套餐
		if(typeof(suiteNum) !="undefined" && suiteNum >0){
			S.use("ajax", function (ajax) {
				ajax.get({
					url: BASE_PATH + '/shop/shop_service_info/getPackageByServiceId',
					data: {serviceId: serviceId},
					success: function (result) {
						fillShopPackage(result);
					}
				});
			});

			var fillShopPackage = function (result) {
				// IF 成功 THEN 先清空当前行，再填充服务
				if (result.success != true) {
					return false;
				} else {
					// 清空当前行
					currentRow.find('input').val('');
					// 服务类型{1：基本服务；2：附加服务}
					$('input[name="type"]', currentRow).val(1);
					// 金额单位“元”
					$('input[name="currencyunit"]', currentRow).val("元");

					// 填充服务
					var basicServiceScope =$("#basicServiceRow");
					var infoList = result.data.shopServiceInfoList;
					infoList && $.each(infoList, function (i, v) {
						var serviceId = $('input[name="serviceId"]', currentRow).val();
						if(serviceId !=""){
							$(".qxy_add_icon", basicServiceScope).click();
							currentRow =$(".qxy_add_icon", basicServiceScope).parents(".form_item");
						}

						// serviceId
						$('input[name="serviceId"]', currentRow).val(v["id"]);
						// serviceSn
						$('input[name="serviceSn"]', currentRow).val(v["serviceSn"]);
						// type
						$('input[name="type"]', currentRow).val(1);
						// serviceName
						$('input[name="serviceName"]', currentRow).val(v["name"]);
						// serviceCatName
						$('input[name="serviceCatId"]', currentRow).val(v["categoryId"]);
						$('input[name="serviceCatName"]', currentRow).val(v["serviceCatName"]);
						// servicePrice
						var servicePrice = v["servicePrice"];
						$('input[name="servicePrice"]', currentRow).val(servicePrice);
						// serviceHour
						var serviceNum = v["serviceNum"];
						serviceNum = (typeof(serviceNum) == 'undefined'
										|| serviceNum ==""
										|| serviceNum == null) ? 1 : serviceNum;
						$('input[name="serviceHour"]', currentRow).val(serviceNum);
						// serviceAmount
						$('input[name="serviceAmount"]', currentRow).val((servicePrice * serviceNum).toFixed(2));
						// discount
						$('input[name="discount"]', currentRow).val(0);

					});

					// 填充商品
					var goodsList = result.data.goodsList;
					var goodsListDynamicScope = $("#basicGoodsRow");
					goodsList && $.each(goodsList, function (i, v) {
						currentRow =$(".qxy_add_icon", goodsListDynamicScope).parents(".form_item");
						var goodsId = $('input[name="goodsId"]', currentRow).val();
						if(goodsId !=""){
							$(".qxy_add_icon", goodsListDynamicScope).click();
							currentRow =$(".qxy_add_icon", goodsListDynamicScope).parents(".form_item");
						}

						// 清空行
						currentRow.find('input').val('');
						// 物料类型{0：基本物料；1：其他物料}
						$('input[name="goodsType"]', currentRow).val(0);
						// 金额单位“元”
						$('input[name="currencyunit"]', currentRow).val("元");
						// 物料狀態:4
						$('input[name="tqmallStatus"]', currentRow).val(4);

						// goodsId
						$('input[name="goodsId"]', currentRow).val(v["id"]);
						// goodsSn
						$('input[name="goodsSn"]', currentRow).val(v["goodsSn"]);
						// goodsFormat
						$('input[name="goodsFormat"]', currentRow).val(v["format"]);
						// goodsName
						$('input[name="goodsName"]', currentRow).val(v["name"]);
						// goodsPrice
						var price = v["price"];
						$('input[name="goodsPrice"]', currentRow).val(price);
						// goodsNumber
						var goodsNum = v["goodsNum"];
						if (typeof(goodsNum) === 'undefined'
							|| goodsNum == null) {
							goodsNum = 1; // default 1
						}
						$('input[name="goodsNumber"]', currentRow).val(goodsNum);
						// unit
						$('input[name="unit"]', currentRow).val(v["measureUnit"]);
						// goodsAmount
						$('input[name="goodsAmount"]', currentRow).val((price * goodsNum).toFixed(2));
						// discount
						$('input[name="discount"]', currentRow).val(0);
						// stock
						$('input[name="stock"]', currentRow).val(v["stock"]);
					});

					// 核算价格
					order.calculatePrice();
				}
			}
		}else{
			// 清空当前行
			currentRow.find('input').val('');
			// 服务类型{1：基本服务；2：附加服务}
			$('input[name="type"]', currentRow).val(1);
			// 金额单位“元”
			$('input[name="currencyunit"]', currentRow).val("元");

			var serviceId = $('input[name="serviceId"]', currentRow).val();
			if(serviceId !=""){
				$(".qxy_add_icon", basicServiceScope).click();
				currentRow =$(".qxy_add_icon", basicServiceScope).parents(".form_item");
			}

			// TODO 填充服务项目，提取公共
			// serviceId
			$('input[name="serviceId"]', currentRow).val(item["id"]);
			// serviceSn
			$('input[name="serviceSn"]', currentRow).val(item["serviceSn"]);
			// type
			$('input[name="type"]', currentRow).val(1);
			// serviceName
			$('input[name="serviceName"]', currentRow).val(item["name"]);
			// serviceCatId
			$('input[name="serviceCatId"]', currentRow).val(item["categoryId"]);
			// serviceCatName
			$('input[name="serviceCatName"]', currentRow).val(item["serviceCatName"]);
			// servicePrice
			var servicePrice = item["servicePrice"];
			$('input[name="servicePrice"]', currentRow).val(servicePrice);
			// serviceHour
			var serviceNum = item["serviceNum"];
			serviceNum = (typeof(serviceNum) == 'undefined'
							|| serviceNum ==""
							|| serviceNum == null) ? 1 : serviceNum;
			$('input[name="serviceHour"]', currentRow).val(serviceNum);
			// serviceAmount
			$('input[name="serviceAmount"]', currentRow).val((servicePrice * serviceNum).toFixed(2));
			// discount
			$('input[name="discount"]', currentRow).val(0);
		}
	};

	// TODO 合并基本物料、其他物料，回调
	order.fillGood = function (obj, v) {
		var currentRow = obj.parents(".form_item");

		// 清空行
		currentRow.find('input[name !="carInfoLike"]').val('');
		// 物料类型{0：基本物料；1：其他物料}
		$('input[name="goodsType"]', currentRow).val(0);
		// 金额单位“元”
		$('input[name="currencyunit"]', currentRow).val("元");
        // 物料狀態:4
        $('input[name="tqmallStatus"]', currentRow).val(4);

		// goodsId
		$('input[name="goodsId"]', currentRow).val(v["id"]);
		// goodsSn
		$('input[name="goodsSn"]', currentRow).val(v["goodsSn"]);
		// goodsFormat
		$('input[name="goodsFormat"]', currentRow).val(v["format"]);
		// goodsName
		$('input[name="goodsName"]', currentRow).val(v["name"]);
		// goodsPrice
		var price = v["price"];
		$('input[name="goodsPrice"]', currentRow).val(price);
		// goodsNumber
		var goodsNum = v["goodsNum"];
		if (typeof(goodsNum) === 'undefined'
			|| goodsNum == null) {
			goodsNum = 1; // default 1
		}
		$('input[name="goodsNumber"]', currentRow).val(goodsNum);
		// unit
		$('input[name="unit"]', currentRow).val(v["measureUnit"]);
		// goodsAmount
		$('input[name="goodsAmount"]', currentRow).val((price * goodsNum).toFixed(2));
		// discount
		$('input[name="discount"]', currentRow).val(0);
		// stock
		$('input[name="stock"]', currentRow).val(v["stock"]);

		// 核算积分
		order.calculatePrice();
	}

	// 其他物料弹出框，回调
	order.otherFillGood = function (obj, v) {
		var currentRow = obj.parents(".form_item");

		// 清空行
		currentRow.find('input[name !="carInfoLike"]').val('');
		// 物料类型{0：基本物料；1：其他物料}
		$('input[name="goodsType"]', currentRow).val(1);
		// 金额单位“元”
		$('input[name="currencyunit"]', currentRow).val("元");
        // 物料狀態:4
        $('input[name="tqmallStatus"]', currentRow).val(4);

		// goodsId
		$('input[name="goodsId"]', currentRow).val(v["id"]);
		// goodsSn
		$('input[name="goodsSn"]', currentRow).val(v["goodsSn"]);
		// goodsFormat
		$('input[name="goodsFormat"]', currentRow).val(v["format"]);
		// goodsName
		$('input[name="goodsName"]', currentRow).val(v["name"]);
		// goodsPrice
		var price = v["price"];
		$('input[name="goodsPrice"]', currentRow).val(price);
		// goodsNumber
		var goodsNum = v["goodsNum"];
		if (typeof(goodsNum) === 'undefined'
			|| goodsNum == null) {
			goodsNum = 1; // default 1
		}
		$('input[name="goodsNumber"]', currentRow).val(goodsNum);
		// unit
		$('input[name="unit"]', currentRow).val(v["measureUnit"]);
		// goodsAmount
		$('input[name="goodsAmount"]', currentRow).val((price * goodsNum).toFixed(2));
		// discount
		$('input[name="discount"]', currentRow).val(0);
		// stock
		$('input[name="stock"]', currentRow).val(v["stock"]);

		// 核算积分
		order.calculatePrice();
	};

	// 附加费用 弹出框回调
	order.fillAdditionalService = function (obj, v) {
		var currentRow = obj.parents(".form_item");

		// 清空行
		currentRow.find('input').val('');
		// 服务类型{1：基本服务；2：附加服务}
		$('input[name="type"]', currentRow).val(2);
		// 金额单位“元”
		$('input[name="currencyunit"]', currentRow).val("元");

		// serviceId
		$('input[name="serviceId"]', currentRow).val(v["id"]);
		// serviceSn
		$('input[name="serviceSn"]', currentRow).val(v["serviceSn"]);
		// serviceName
		$('input[name="serviceName"]', currentRow).val(v["name"]);
		// servicePrice
		var servicePrice = v["servicePrice"];
		$('input[name="servicePrice"]', currentRow).val(servicePrice);
		// serviceHour(default : 1)
		var serviceNum = 1;
		$('input[name="serviceHour"]', currentRow).val(serviceNum);
		// serviceAmount
		$('input[name="serviceAmount"]', currentRow).val((servicePrice * serviceNum).toFixed(2));
		// discount(default：0)
		$('input[name="discount"]', currentRow).val(0);

		// 核算积分
		order.calculatePrice();
	};

	// 核算费用
	order.calculatePrice =function(){
		// 折扣
		var preDiscountRateInput =$('input[name="orderInfo.preDiscountRate"]');
		// 费用
		var preTaxAmountInput =$('input[name="orderInfo.preTaxAmount"]');
		// 优惠
		var prePreferentiaAmountInput =$('input[name="orderInfo.prePreferentiaAmount"]');
		// 代金券
		var preCouponAmountInput =$('input[name="orderInfo.preCouponAmount"]');
		// 申请实付金额
		var preTotalAmountInput =$('input[name="orderInfo.preTotalAmount"]');

		var preDiscountRate = preDiscountRateInput.val();
		if (isNaN(preDiscountRate) || preDiscountRate < 0 || preDiscountRate > 1) {
			preDiscountRateInput.val(1);
			preDiscountRate = 1;
		}
		var preTaxAmount = preTaxAmountInput.val();
		var prePreferentiaAmount = prePreferentiaAmountInput.val();
		var preCouponAmount = preCouponAmountInput.val();

		// 物料
		var orderGoods = [];
		var goodsRowArray =$('input[name="goodsId"]').parents(".form_item");
		goodsRowArray.each(function () {
			var goodsId =$('input[name="goodsId"]', this).val();
			var goodsPrice =$('input[name="goodsPrice"]', this).val();
			var goodsNumber =$('input[name="goodsNumber"]', this).val();
			var goodsAmount =$('input[name="goodsAmount"]', this).val();
			var discount =$('input[name="discount"]', this).val();

			if (goodsPrice != ""
				&& goodsNumber != ""
				&& goodsAmount != ""
				&& discount != "") {
				orderGoods.push({
					goodsId:goodsId,
					goodsPrice: goodsPrice,
					goodsNumber: goodsNumber,
					goodsAmount: goodsAmount,
					discount: discount
				});
			}
		});

		orderGoods =JSON.stringify(orderGoods);
		var $manageFeeAmount = null;
		var $manageFee = null;
		var orderServices = [];

		var serviceRowArray =$('input[name="serviceId"]').parents(".form_item");
		serviceRowArray.each(function () {
			var serviceId =$('input[name="serviceId"]', this).val();
			var servicePrice =$('input[name="servicePrice"]', this).val();
			var serviceHour =$('input[name="serviceHour"]', this).val();
			var serviceAmount =$('input[name="serviceAmount"]', this).val();
			var discount =$('input[name="discount"]', this).val();
			var type =$('input[name="type"]', this).val();
			// TODO flags 何意
			var flags = $('input[name="flags"]', this).val();
			if (typeof flags == "undefined" || flags == null) {
				flags = "";
			}
			if (flags == 'GLF') {
				$manageFeeAmount =$('input[name="serviceAmount"]', this);
				$manageFee =$('input[name="servicePrice"]', this);
			}
			var manageRate =$('input[name="manageRate"]', this).val();
			if (typeof manageRate == "undefined" || manageRate == '') {
				manageRate = 0;
			}

			if (servicePrice != "" &&
				serviceHour != "" &&
				discount != "") {
				if (serviceAmount == "") {
					$('input[name="serviceAmount"]', this).val((servicePrice * serviceHour).toFixed(0));
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
		orderServices =JSON.stringify(orderServices);

		S.use(["ajax","dialog"], function (ajax,dialog) {
			ajax.post({
				url: BASE_PATH + '/shop/order/calc_price',
				data: {
					orderGoods: orderGoods,
					orderServices: orderServices
				},
				loadShow : false,
				cache :false,
				success: function (result) {
					if (!result.success) {
						dialog.info(result.errorMsg);
						return false;
					} else {
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
						// TODO 待确认discount
						//$("#discount").val(discount);
						$('input[name="orderInfo.goodsDiscount"]').val(goodsDiscount);
						$('input[name="orderInfo.serviceDiscount"]').val(serviceDiscount);
						$('input[name="orderInfo.feeAmount"]').val(parseFloat(feeAmount).toFixed(2));
						$('input[name="orderInfo.feeDiscount"]').val(parseFloat(feeDiscount).toFixed(2));
						$('input[name="orderInfo.orderAmount"]').val(parseFloat(orderAmount).toFixed(2));
						var pretotalAmount = (parseFloat(orderAmount)
						* preDiscountRate
						+ parseFloat(preTaxAmount)
						- parseFloat(prePreferentiaAmount)
						- parseFloat(preCouponAmount)).toFixed(2);

						$('input[name="orderInfo.preTotalAmount"]').val(pretotalAmount);
					}
				},
				error :function(e){
					console.error(e);
				}
			})
		});
	};

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
	$(document).on('blur', '.form_item[dynamic_name] input[name="serviceHour"]', function () {
		var serviceHour = $(this).val();
		if (!$.isNumeric(serviceHour) || serviceHour <0) {
			$(this).val(0);
			return false;
		}

		var currentRow =$(this).parents(".form_item");
		var servicePriceInput =$('input[name="servicePrice"]', currentRow);
		var servicePrice =servicePriceInput.val();
		if (servicePrice == '') {
			servicePrice = 0;
			servicePriceInput.val(0);
		}
		var discountInput =$('input[name="discount"]', currentRow);
		var discount =discountInput.val();
		if (discount == '') {
			discountInput.val(0);
		}
		var serviceAmount =$('input[name="serviceAmount"]', currentRow);
		serviceAmount.val((servicePrice * serviceHour).toFixed(2));

		// 核算价格
		order.calculatePrice();
	});

	// 服务项目.工时费
	$(document).on('blur', '.form_item[dynamic_name] input[name="servicePrice"]', function () {
		var servicePrice = $(this).val();
		if (!$.isNumeric(servicePrice) || servicePrice <0) {
			$(this).val(0);
			return ;
		}

		var currentRow =$(this).parents(".form_item");
		var serviceHourInput =$('input[name="serviceHour"]', currentRow);
		var serviceHour =serviceHourInput.val();
		if (serviceHour == '') {
			serviceHour = 1;
			serviceHourInput.val(1);
		}

		var discountInput =$('input[name="discount"]', currentRow);
		var discount =discountInput.val();
		if (discount == '') {
			discountInput.val(0);
		}

		var serviceAmount =$('input[name="serviceAmount"]', currentRow);
		serviceAmount.val((servicePrice * serviceHour).toFixed(2));

		// 核算价格
		order.calculatePrice();
	});

	// 服务项目.优惠
	var basicServiceScope = $("#basicServiceRow");
	$(basicServiceScope).on('blur', 'input[name="discount"]', function () {
		var discount =$(this).val();
		if (!$.isNumeric(discount) || discount <0) {
			$(this).val(0);
			return ;
		}

		// 检验优惠金额 >总金额
		var currentRow =$(this).parents(".form_item");
		var serviceAmount =$('input[name="serviceAmount"]', currentRow).val();
		if(parseFloat(discount) > parseFloat(serviceAmount)){
			$(this).val(0);
			return ;
		}

		// 核算价格
		order.calculatePrice();
	});

	// 物料.单价
	$(document).on('blur', 'input[name="goodsPrice"]', function () {
		var goodsPrice = $(this).val();
		if (!$.isNumeric(goodsPrice) || goodsPrice <0) {
			$(this).val(0);
			return ;
		}

		var currentRow =$(this).parents(".form_item");
		var goodsNumberInput =$('input[name="goodsNumber"]', currentRow);
		var goodsNumber =goodsNumberInput.val();
		if (goodsNumber == '') {
			goodsNumber = 1;
			goodsNumberInput.val(1);
		}

		var discountInput =$('input[name="discount"]', currentRow);
		var discount =discountInput.val();
		if (discount == '') {
			discountInput.val(0);
		}

		var goodsAmount =$('input[name="goodsAmount"]', currentRow);
		goodsAmount.val((goodsPrice * goodsNumber).toFixed(2));

		// 核算价格
		order.calculatePrice();
	});

	// 物料.数量
	$(document).on('blur', 'input[name="goodsNumber"]', function () {
		var goodsNumber = $(this).val();
		if (!$.isNumeric(goodsNumber) || goodsNumber <0) {
			$(this).val(1);
			return false;
		}

		// TODO 判断数量 >库存
		var currentRow =$(this).parents(".form_item");
		var goodsPriceInput =$('input[name="goodsPrice"]', currentRow);
		var goodsPrice =goodsPriceInput.val();
		if (goodsPrice == '') {
			goodsPrice = 0;
			goodsPriceInput.val(0);
		}

		var discountInput =$('input[name="discount"]', currentRow);
		var discount =discountInput.val();
		if (discount == '') {
			$(this).parent().parent().find('.discount').val(0);
		}

		var goodsAmount =$('input[name="goodsAmount"]', currentRow);
		goodsAmount.val((goodsPrice * goodsNumber).toFixed(2));

		// 核算价格
		order.calculatePrice();
	});

	// 物料.优惠
	var basicGoodsScope = $("#basicGoodsRow");
	$(basicGoodsScope).on('blur', 'input[name="discount"]', function () {
		var discount =$(this).val();
		if (!$.isNumeric(discount) || discount <0) {
			$(this).val(0);
			return ;
		}

		// 检验优惠金额 >总金额
		var currentRow =$(this).parents(".form_item");
		var goodsAmount =$('input[name="goodsAmount"]', currentRow).val();
		if(parseFloat(discount) > parseFloat(goodsAmount)){
			$(this).val(0);
			return ;
		}

		// 核算价格
		order.calculatePrice();
	});

	// TODO 临时方案，“基本物料”增加适配车型
	$(basicGoodsScope).on('change', 'input[name="carInfoLike"]', function () {
		var currentRow =$(this).parents(".form_item");
		// 清空行
		currentRow.find('input[name !="carInfoLike"]').val('');
		// 物料类型{0：基本物料；1：其他物料}
		$('input[name="goodsType"]', currentRow).val(0);
		// 金额单位“元”
		$('input[name="currencyunit"]', currentRow).val("元");
		// 物料狀態:4
		$('input[name="tqmallStatus"]', currentRow).val(4);
	});

	// 其他物料.优惠
	var otherGoodsScope = $("#otherGoodsRow");
	$(otherGoodsScope).on('blur', 'input[name="discount"]', function () {
		var discount =$(this).val();
		if (!$.isNumeric(discount) || discount <0) {
			$(this).val(0);
			return ;
		}

		// 检验优惠金额 >总金额
		var currentRow =$(this).parents(".form_item");
		var goodsAmount =$('input[name="goodsAmount"]', currentRow).val();
		if(parseFloat(discount) > parseFloat(goodsAmount)){
			$(this).val(0);
			return ;
		}

		// 核算价格
		order.calculatePrice();
	});
	// TODO 临时方案，“其他物料”增加适配车型
	$(otherGoodsScope).on('change', 'input[name="carInfoLike"]', function () {
		var currentRow =$(this).parents(".form_item");
		// 清空行
		currentRow.find('input[name !="carInfoLike"]').val('');
		// 物料类型{0：基本物料；1：其他物料}
		$('input[name="goodsType"]', currentRow).val(1);
		// 金额单位“元”
		$('input[name="currencyunit"]', currentRow).val("元");
		// 物料狀態:4
		$('input[name="tqmallStatus"]', currentRow).val(4);
	});

	// 附加费用.优惠
	var additionalServicesScope = $("#additionalServicesRow");
	$(additionalServicesScope).on('blur', 'input[name="discount"]', function () {
		var discount =$(this).val();
		if (!$.isNumeric(discount) || discount <0) {
			$(this).val(0);
			return ;
		}

		// 检验优惠金额 >总金额
		var currentRow =$(this).parents(".form_item");
		var goodsAmount =$('input[name="servicePrice"]', currentRow).val();
		if(parseFloat(discount) > parseFloat(goodsAmount)){
			$(this).val(0);
			return ;
		}

		// 核算价格
		order.calculatePrice();
	});

	/**
	 * 需要重新核算价格
	 *
	 * 服务项目 新增/删除
	 * 配件物料 新增/删除 ：
	 *   IF (物料删除 AND 出库数量 !=0) : 不可以删除 THEN 可以删除
	 * 其他物料 新增/删除
	 *  IF(附加费用删除 AND flags =='GLF'): 不可以删除 THEN 删除
	 * 附加费用 新增/删除
	 *
	 *
	 */
	order.delDynamicRow =function(obj){
		var currentRow =$(obj);

		var outNumber =$('input[name="outNumber"]', currentRow).val();
		if(typeof(outNumber) !="undefined"
			&& parseInt(outNumber) !=0){
			S.use(["dialog"], function (dialog) {
				dialog.info("该商品已出库，不能删除",3,3);
			});
			return false;
		}

		var flags =$('input[name="flags"]', currentRow).val();
		if(typeof(flags) !="undefined"
			&& flags =="GLF"){
			S.use(["dialog"], function (dialog) {
				dialog.info("该附加费用，不能删除",3,3);
			});
			return false;
		}

		return true;
	};

	// 申请优惠.折扣
	$(document).on('blur', 'input[name="orderInfo.preDiscountRate"]', function () {
		var preDiscountRate =$(this).val();
		if (!$.isNumeric(preDiscountRate)
			|| preDiscountRate <0
			|| preDiscountRate >1) {
			$(this).val(1);
			return ;
		}

		// 核算价格
		order.calculatePrice();
	});

	// 申请优惠.费用
	$(document).on('blur', 'input[name="orderInfo.preTaxAmount"]', function () {
		var preTaxAmount =$(this).val();
		if (!$.isNumeric(preTaxAmount)
			|| preTaxAmount <0) {
			$(this).val(0);
			return ;
		}

		// 核算价格
		order.calculatePrice();
	});

	// 申请优惠.优惠
	$(document).on('blur', 'input[name="orderInfo.prePreferentiaAmount"]', function () {
		var prePreferentiaAmount =$(this).val();
		if (!$.isNumeric(prePreferentiaAmount)
			|| prePreferentiaAmount <0) {
			$(this).val(0);
			return ;
		}

		// TODO 校验优惠的金额

		// 核算价格
		order.calculatePrice();
	});

	// 申请优惠.代金券
	$(document).on('blur', 'input[name="orderInfo.preCouponAmount"]', function () {
		var preCouponAmount =$(this).val();
		if (!$.isNumeric(preCouponAmount)
			|| preCouponAmount <0) {
			$(this).val(0);
			return ;
		}

		// TODO 校验代金券的金额

		// 核算价格
		order.calculatePrice();
	});

	// 新增客户 弹出框
	order.createCustomer =function(){
		S.use([ "dialog","chosenSelect" ], function(dg,chosen) {
			var dialogId = dg.dialog({
				"dom" : "#customer_create_dialog",
				"init" : function(){
					chosen.handleChoosenSelect('.qxy_dialog select');
					$("input[name='license']").val("浙A");
				}
			});

			//取消按钮事件绑定
			$(".cancel","#customer_create_dialog").click(function(){
				dg.closeDialog("#customer_create_dialog");
			});

			//绑定车型选择框
			$("#carMode").off("click").on("click",function(){
				util.carTypeSelect(function(data){
					var carCompany = data["company"];
					var importInfo = data["importInfo"];
					var carBrand = data["brand"];
					var carSeries = data["name"];
					var temp = '';
					if (carBrand) {
						temp = temp + carBrand;
					}
					if (carSeries) {
						temp = temp + carSeries;
					}
					if (importInfo) {
						temp = temp + "(" + importInfo + ")"
					}

					$("input[name='carCompany']").val(carCompany);
					$("input[name='carBrandId']").val(data["pid"]);
					$("input[name='carBrand']").val(carBrand);
					$("input[name='carSeriesId']").val(data["id"]);
					$("input[name='carSeries']").val(carSeries);

					$('#carMode').val(temp);
					$("input[name='carModelId']").blur();
				});
			});
		});
	};

	// 工单.提交
	order.submit =function(){
		S.use("dialog",function(dialog){
			util.submit({
				formid:"order_form",
				loadShow : true,
				loadText : '正在保存信息中...',
				callback:function(json){
					if(json.success){
						dialog.info("保存成功",1,3);
						window.location.href = BASE_PATH+"/shop/order/detail?orderId="+json["data"];
					}else{
						dialog.info(json.errorMsg,3);
					}
				}
			});
		});
	};

	// 工单.打印
	order.printOrder =function(){
		var orderId =$('input[name="orderInfo.id"]').val();
		window.open(BASE_PATH+"/shop/order/order_print?id="+orderId);
	};

	// 工单.打印报价
	order.printBaoJia =function(){
		var orderId =$('input[name="orderInfo.id"]').val();
		window.open(BASE_PATH+"/shop/warehouse/out/pricing_print?id="+orderId);
	};

	// 工单.打印结算
	order.printJieSuan =function(){
		var orderId =$('input[name="orderInfo.id"]').val();
		window.open(BASE_PATH+"/shop/order/pre_pay_print?id="+orderId);
	}

	// 工单.无效
	order.invalid =function(){
		var orderId =$('input[name="orderInfo.id"]').val();
		var orderSn =$('input[name="orderInfo.orderSn"]').val();
		S.use("dialog",function(dialog){
			dialog.confirm("您确定要把该工单无效吗?",function(){
				S.use("ajax", function (ajax) {
					ajax.post({
						url: BASE_PATH + '/shop/order/order_track/invalid',
						data: {
							orderSn: orderSn,
							orderId: orderId
						},
						success: function (result) {
							if (result.success != true) {
								dialog.error(result.errorMsg)
								return false;
							} else {
								dialog.info("操作成功",1);
								window.location.reload();
							}
						}
					});
				});
			},function(){
				return false;
			});
		});
	};

	// 工单.派工
	order.paiGong =function(){
		S.use("dialog",function(dialog){
			dialog.confirm("您确定要把该工单派工吗?",function(){
				$('input[name="orderInfo.orderStatus"]').val("FPDD");
				order.submit();
			},function(){
				return false;
			});
		});
	};

	// TODO 待优化 编辑时，新增物料提示先保存后、再完工
	order.checkNewGoodsInEdit =function(){
		var goodsRowArray =$('input[name="goodsId"]').parents(".form_item");
		for(var i=0;i<goodsRowArray.length;i++){
			var currentObj =goodsRowArray[i];
			var goodsId =$('input[name="goodsId"]', currentObj).val();
			var sid =$('input[name="id"]', currentObj).val();
			if((typeof(sid) =="undefined" ||  sid =="")
				&& goodsId !=""){
				return true;
			}
		}

		var serviceRowArray =$('input[name="serviceId"]').parents(".form_item");
		for(var i=0;i<serviceRowArray.length;i++){
			var currentObj =serviceRowArray[i];
			var serviceId =$('input[name="serviceId"]', currentObj).val();
			var sid =$('input[name="id"]', currentObj).val();
			if((typeof(sid) =="undefined" ||  sid =="")
				&& goodsId !=""){
				return true;
			}
		}

		return false;
	};

	// 工单.完工
	order.wanGong =function(){
		var orderId =$('input[name="orderInfo.id"]').val();
		S.use(["ajax", "dialog"],function(ajax,dialog){
			if(order.checkNewGoodsInEdit()){
				dialog.info("你有新增物料或者服务，请先保存工单后再进行完工",0,5);
				return false;
			}else{
				ajax.get({
					url: BASE_PATH + '/shop/order/getRealOrderGoodsListByOrderId',
					data: {
						orderId: orderId,
						checkFinish: true
					},
					success: function (result) {
						if (result["success"] == true) {
							if (result["data"]["code"] == true) {
								$('input[name="orderInfo.orderStatus"]').val("DDWC");
								order.submit();
							} else {
								order.moreWarehouse(orderId);
							}
						} else {
							dialog.info(result["errorMsg"],3);
						}
					}
				});
			}
		});
	};

	// 完工时，配件信息不合格弹框
	order.moreWarehouse =function(orderId){
		seajs.use(["ajax","dialog","artTemplate"],function(ajax,dg,art){
			ajax.get({
				url: BASE_PATH + '/shop/order/getRealOrderGoodsListByOrderId',
				data:{orderId:orderId},
				success:function(result,dialog){
					if (!result.success) {
						dialog.info(result.errorMsg, 5);
						return;
					} else {
						var html = art.render("moreWarehouseTpl",result);
						dg.dialog({
							html : html
						});
					}

				}
			});
		});
	};

	// 工单.重新派工
	order.rePaiGong =function(){
		S.use(["ajax", "dialog"],function(ajax,dialog){
			dialog.confirm("你确定要把该工单派工吗？",function(){
				var orderId =$('input[name="orderInfo.id"]').val();
				var orderSn =$('input[name="orderInfo.orderSn"]').val();
				ajax.post({
					url: BASE_PATH + '/shop/order/order_track/tasking',
					data: {
						orderSn: orderSn,
						orderId: orderId
					},
					success: function (data) {
						if (data["success"] == true) {
							dialog.info("操作成功",1);
							window.location.reload();
						} else {
							dialog.info(data["errorMsg"],3);
						}
					}
				});
			},function(){
				return false;
			});
		});
	};

	// TODO 待优化 填充服务顾问名称
	order.fillReceiverName =function(obj){
		var receiverName =$(obj).find("option:selected").text();
		$('input[name="orderInfo.receiverName"]').val(receiverName);
	};
})(jQuery, seajs);

// 新增客户.弹框
var customerDialog = customerDialog || {};
(function($,S){
	// 填充表单
	customerDialog.fillCustomerSource =function(obj, item){
		// 来源名称
		$('input[name="customerSource"]').val(item["source"]);
	};

	// 提交
	customerDialog.submitForm =function(){
		S.use([ "dialog" ], function(dialog) {
			util.submit({
				formid:"customer_add_form",
				callback:function(json){
					if(json.success){
						dialog.info("操作成功",1);
						$('input[name="orderInfo.carLicense"]').val(json["data"]["license"]);

						// 填充客户信息
						order.fillCustomer(null,json["data"]);

						dialog.closeDialog("#customer_create_dialog");
					}else{
						dialog.info(json.errorMsg,3);
					}
				}
			});
		});
	};

	// 取消
	customerDialog.cancle =function(){
		seajs.use("dialog",function(dialog){
			dialog.closeDialog("#customer_create_dialog");
		});
	}
})(jQuery,seajs);

// 添加服务.弹框
var shopServiceDialog =shopServiceDialog || {};
(function($,S){
	shopServiceDialog.createService =function(obj){
		S.use([ "dialog","ajax","chosenSelect" ], function(dialog,ajax,chosen) {
			var dialogId = dialog.dialog({
				"dom" : "#service_create_dialog",
				init:function(){
					// 清空历史
					$("#service_create_dialog").find("input").val("");

					// 生成编号
					ajax.get({
						url: BASE_PATH + '/shop/sn/generate',
						data: {
							type: "FW"
						},
						success: function (result) {
							if (result["success"] != true) {
								dialog.error(result.errorMsg);
								return false;
							} else {
								$("#newServiceName").val(result["data"]);
							}
						}
					});
					chosen.handleChoosenSelect(".dialog_chosen select");
				}
			});
			//保存事件
			$(".ad_bc").off("click").on("click",function(){
				util.submit({
					formid:"service_add_form",
					callback:function(result1){
						if(result1.success){
							dialog.info("保存成功",1,3,function(){
								//  重新查询shopService
								var v =result1["data"];
								// 函数回填
								var optCurrentRow =optObject.parents(".form_item");

								$('input[name="serviceId"]', optCurrentRow).val(v["id"]);
								// serviceSn
								$('input[name="serviceSn"]', optCurrentRow).val(v["serviceSn"]);
								// type
								$('input[name="type"]', optCurrentRow).val(1);
								// serviceName
								$('input[name="serviceName"]', optCurrentRow).val(v["name"]);
								// serviceCatName
								$('input[name="serviceCatId"]', optCurrentRow).val(v["categoryId"]);
								$('input[name="serviceCatName"]', optCurrentRow).val(v["categoryName"]);
								// servicePrice
								var servicePrice = v["servicePrice"];
								$('input[name="servicePrice"]', optCurrentRow).val(servicePrice);
								// serviceHour
								var serviceNum = 1;
								$('input[name="serviceHour"]', optCurrentRow).val(serviceNum);
								// serviceAmount
								$('input[name="serviceAmount"]', optCurrentRow).val((servicePrice * serviceNum).toFixed(2));
								// discount
								$('input[name="discount"]', optCurrentRow).val(0);

								//核算
								order.calculatePrice();

								dialog.close(dialogId);
							});
						}else{
							dialog.info(result1.errorMsg,3);
						}
					}
				});
			});
			//取消事件
			$(".ad_qx").off("click").on("click",function(){
				dialog.closeDialog("#service_create_dialog");
			});
		});
	};


	shopServiceDialog.fillCategoryName =function(obj){
		var categoryName =$(obj).find("option:selected").text();
		$("#serviceCategoryName").val(categoryName);
	};
})(jQuery,seajs);

// 添加配件.弹框
var peijianDialog = peijianDialog || {} ;
(function($,S){
	// 生成编号
	peijianDialog.create =function(goodsType){
		S.use([ "dialog","ajax","chosenSelect" ], function(dialog,ajax,chosen) {
			var dialogId = dialog.dialog({
				"dom" : "#peijian_create_dialog",
				init:function(){
					// 清空历史
					$("#peijian_create_dialog").find("input").val("");
					// type(0:基本配件；1：其他配件)
					$("#peijianGoodsType").val(goodsType);
					// 物料状态：4
					$("#peijiantqmallStatus").val(4);
					// 预警库存：10
					$("#peijianShortageNumber").val(10);

					// 生成编号
					ajax.get({
						url: BASE_PATH + '/shop/sn/generate',
						data: {
							type: "PJ"
						},
						success: function (result) {
							if (result["success"] != true) {
								dialog.error(result.errorMsg);
								return false;
							} else {
								$("#peijianGoodsSn").val(result["data"]);
							}
						}
					});
					chosen.handleChoosenSelect(".dialog_chosen select");
				}
			});

			//保存事件
			$(".ad_bc").off("click").on("click",function(){
				util.submit({
					formid:"peijian_add_form",
					callback:function(result){
						if(result.success){
							// 保存成功
							dialog.info("保存成功",1,3);

							var data =result["data"];
							var goodsType =data["goodsType"];
							// 函数回填
							if(goodsType ==0){
								order.fillGood(optObject,data);
							}else{
								order.otherFillGood(optObject,data);
							}

							dialog.close(dialogId);
						}else{
							dialog.info(result.errorMsg,3);
						}
					}
				});
			});
			//取消事件
			$(".ad_qx").off("click").on("click",function(){
				dialog.closeDialog("#peijian_create_dialog");
			});

		});
	};
})(jQuery,seajs);

// 添加附加费用.弹框
var expenseDialog = expenseDialog || {} ;
(function($,S){
	// 生成编号
	expenseDialog.createExpense =function(){
		// 清空表单
		$("#expenses_create_dialog").find("input").val("");
		S.use([ "dialog","ajax","chosenSelect" ], function(dialog,ajax,chosen) {
			var dialogId = dialog.dialog({
				"dom" : "#expenses_create_dialog",
				init:function(){
					// 生成编号
					ajax.get({
						url: BASE_PATH + '/shop/sn/generate',
						data: {
							type: "FY"
						},
						success: function (result) {
							if (result["success"] != true) {
								dialog.info(result.errorMsg,3);
								return false;
							} else {
								$("#expenseSn").val(result["data"]);
							}
						}
					});
					chosen.handleChoosenSelect(".dialog_chosen select");
				}
			});

			//保存事件
			$(".ad_bc").off("click").on("click",function(){
				util.submit({
					formid:"expenses_add_form",
					callback:function(result1){
						if(result1.success){
							// 函数回填
							S.use(["ajax","dialog"],function(ajax,dialog){
								//  重新查询shopService
								var shopServiceId =result1["data"];
								ajax.get({
									url: BASE_PATH + '/shop/shop_service_info/getShopService',
									data: {
										serviceId: shopServiceId
									},
									success: function (result2) {
										if (result2.success != true) {
											return false;
										} else {
											var v = result2.data;
											order.fillAdditionalService(optObject,v);

											dialog.close(dialogId);
										}
									},
									error: function (e) {
										console.log(e);
									}
								});
							})
						}else{
							dialog.info(result1.errorMsg,3);
						}
					}
				});
			});
			//取消事件
			$(".ad_qx").off("click").on("click",function(){
				dialog.closeDialog("#expenses_create_dialog");
			});
		});
	};
})(jQuery,seajs);

//配件仓库信息弹出框
$(".warehouse_info").click(function(e){
	e.stopPropagation();
	seajs.use(["ajax","artTemplate","dialog"],function(ajax,art,dialog){
		ajax.get({
			url:BASE_PATH+"/shop/order/getRealOrderGoodsListByOrderId",
			data:{orderId:util.getQueryString("id")},
			success:function(json){
				var html = art("part_warehouse_template",json);
				dialog.dialog({
					html : html
				});
			}
		});

	});
});

// TODO 抽取公共 历史记录弹窗
$("#settlement_history").click(function(){
	var order_id =$('input[name="orderInfo.id"]').val();
	seajs.use(["ajax","dialog","artTemplate"],function(ajax,dg,art){
		ajax.post({
			url:BASE_PATH+"/shop/settlement/settlement_post",
			data:{id:order_id},
			success:function(result,dialog){
				if (!result.success) {
					dialog.info(result.errorMsg, 5);
					return;
				} else {
					var html = art.render("settlePostTpl",{data:result.data});
					dg.dialog({
						html : html
					});
				}

			}
		});
	});
});

// note 提示框
$.remark({
	selector : "input.orderNoteClass"
});

// 记住当前操作对象
var optObject ;
var rememberMe =function(obj){
	optObject =$(obj);
};

// 每次初始化页面后，重新计算金额
order.calculatePrice();

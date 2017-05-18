var appoint = appoint || {};

(function ($, S) {

	S.use("downlist");


	// 车牌弹出框，回调
	appoint.fillCustomer = function (obj, item) {
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
						// 联系人姓名
						$('input[name="appoint.customerName"]').val(item["contact"]);
						// 联系电话
						$('input[name="appoint.mobile"]').val(item["contactMobile"]);
						// customerCarId
						$('input[name="appoint.customerCarId"]').val(item["id"]);

						$('input[name="appoint.license"]').val(item["license"]);


						var carBrand = item["carBrand"];
						var carSeries = item["carSeries"];
                        //车型
                        var carModel = item["carModel"];

						$('input[name="appoint.carModel"]').val(item.carInfo);

						// 车别名
						var byName = item["byName"];
						if (byName == undefined || byName == null) {
							byName = "";
						}
						$('input[name="appoint.carAlias"]').val(byName);
					}
				}
			});
		});
	};

    //服务删除之后的联动
	appoint.delService = function(currentRow){
		//获取当前行服务相关配件的id
		var goodsIdStr = $("input[name='goodsIdStr']",currentRow).val()
		if (goodsIdStr) {
			var goodsIdStrList = goodsIdStr.split(",");
			if(goodsIdStrList && goodsIdStrList.length > 0){
				var goodsList = $(".form_item[dynamic_name='appointGoodJson']");
				//在配件列表里循环删除当前服务的相关配件
				for(var i=0;i<goodsIdStrList.length;i++){
					goodsList.each(function(){
						var row = $(this);
						var goodsId = $("input[name='goodsId']",row).val();
						if(goodsId == goodsIdStrList[i]){
							//删除配件列表里的配件
							row.remove();
							return false;
						}
					});
				}
			}
		};
		
		currentRow.remove();
		appoint.calcFee();
	}

	//计算价格
	appoint.calcFee = function(){
		//服务列表
		var serviceList = $(".form_item[dynamic_name='appointServiceJson']");
		//配件列表
		var goodsList = $(".form_item[dynamic_name='appointGoodJson']");
		var serviceCount = 0;
		var goodsCount = 0;
		//服务费用
		var servicePriceAmountObj = $("input[name='service_price_amount']");
		var goodsPriceAmountObj = $("input[name='goods_price_amount']");
		var appointAmountObj = $("input[name='appoint.appointAmount']");
		//循环服务列表
		serviceList.each(function(){
			var row =  $(this);
			var serviceAmount = Number($("[name='serviceAmount']",row).val());
			serviceCount += serviceAmount;
		});
		goodsList.each(function(){
			var row =  $(this);
			var goodsAmount = Number($("[name='goodsAmount']",row).val());
			goodsCount += goodsAmount;
		});
		//服务金额
		servicePriceAmountObj.val(serviceCount.toFixed(2));
		//配件费用
		goodsPriceAmountObj.val(goodsCount.toFixed(2));
		//总金额
		appointAmountObj.val((serviceCount + goodsCount).toFixed(2));
	}
	

	// 服务项目弹出框，回调
	appoint.fillService = function (obj, item) {
		var currentRow = obj.parents(".form_item");
		var parentServiceId = item["id"];
		var parentFlags =item["flags"];
		// 关联商品数量
		var suiteNum =item["suiteNum"];

		// IF suiteNum >0 查询套餐
		if(typeof(suiteNum) !="undefined" && suiteNum >0){
			S.use("ajax", function (ajax) {
				ajax.get({
					url: BASE_PATH + '/shop/shop_service_info/getPackageByServiceId',
					data: {serviceId: parentServiceId},
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
						// parentServiceId
						$('input[name="parentServiceId"]', currentRow).val(parentServiceId);
						$('input[name="flags"]', currentRow).val(parentFlags);
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
						// goodsIdStr
						$('input[name="goodsIdStr"]', currentRow).val(v["goodsIdStr"]);

					});

					// 填充商品
					var goodsList = result.data.goodsList;
					var goodsListDynamicScope = $("#basicGoodsRow");
					goodsList && $.each(goodsList, function (i, v) {
						$(".qxy_add_icon", goodsListDynamicScope).trigger("click");
						currentRow =$(".qxy_add_icon", goodsListDynamicScope).parents(".form_item").prev();
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
					//appoint.calculatePrice();

					appoint.calcFee();
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

			appoint.calcFee();
		}
	};

	// TODO 合并基本物料、其他物料，回调
	appoint.fillGood = function (obj, v) {
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
		//appoint.calculatePrice();
	}

	


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
		//appoint.calculatePrice();
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
		//appoint.calculatePrice();
	});


	// 新增客户 弹出框
	appoint.createCustomer =function(){
		S.use([ "dialog","chosenSelect","ajax" ], function(dg,chosen,ajax) {
			var dialogId = dg.dialog({
				"dom" : "#customer_create_dialog",
				"init" : function(){
					ajax.post({
						url: BASE_PATH + "/shop/customer/get_license_prefix",
						success: function (json) {
							if (json && json.success) {
								chosen.handleChoosenSelect('.qxy_dialog select');
								$("input[name='license']").val(json.data);
							} else {
								dialog.info(json.errorMsg, 3);
							}
						}
					});

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


	//提交
	appoint.submit =function(){
		$('input[name="flags"]').val("1");//保存
		S.use("dialog",function(dialog){
			util.submit({
				formid:"appoint_form",
				loadShow : true,
				loadText : '正在保存信息中...',
				callback:function(json){
					if(json.success){
						dialog.info("保存成功",1,3,function(){
							window.location.href = BASE_PATH+"/shop/customer_appoint/index/ng";
						},true);
					}else{
						dialog.info(json.errorMsg,3);
					}
				}
			});
		});
	};


	//确认预约
	appoint.confirm =function(){
		$('input[name="flags"]').val("2");//确认预约

		S.use("dialog",function(dialog){
			dialog.confirm("您确定要确认该预约单吗?",function(){
				util.submit({
					formid : "appoint_form",
					loadShow : true,
					loadText : '正在确认信息中...',
					callback : function(json){
						if(json.success){
							dialog.info(json.data,1,3,function(){
								window.location.href = BASE_PATH+"/shop/customer_appoint/index/ng";
							},true);
						}else{
							dialog.info(json.errorMsg,3);
						}
					}
				});

			},function () {
				return false;
			});
		});
	};
    //取消预约弹窗
    $(document).on("click",".appoint_cancel_btn",function(){
        var appointId = $(this).parents(".order_item").attr("appoint_id");
        seajs.use(["ajax","dialog"],function(ajax,dg){
            dg.dialog({
                dom : "#appoint_cancel_dialog",
                init : function(){
                    $("#appoint_cancel_dialog").data("appointId",appointId);
                }
            });
        });
    });
    //取消预约原因事件
    $("#appoint_cancel_dialog li").click(function(){
        if($(this).hasClass("f_li")){
            $(this).removeClass("f_li");
        }else{
            $(this).addClass("f_li").siblings().removeClass("f_li");
        }
    })
    //取消按钮事件绑定
    $(".cancel","#appoint_cancel_dialog").click(function(){
        seajs.use(["dialog"],function(dg){
            dg.closeDialog("#appoint_cancel_dialog");
        });
    });

    $(document).on('click','.cancelAppoint',function(){
        var appointId = $("input[name='appoint.id']").val();
        var cancelReason = $("#appoint_cancel_dialog li.f_li").text();
        seajs.use(["ajax","dialog"], function (ajax,dialog){
            if(cancelReason == ""){
                dialog.info("请选择取消预约的原因！",3);
                return false;
            }
            ajax.post({
                url : BASE_PATH + "/shop/customer_appoint/cancelAppoint",
                data : {
                    appointId : appointId,
                    cancelReason : cancelReason
                },
                success: function(result) {
                    if (result.success != true) {
                        dialog.info(result.errorMsg,3);
                        return false;
                    } else {
                        dialog.closeDialog("#appoint_cancel_dialog");
                        dialog.info("取消预约单成功",1,3,function(){
                            window.location.href = BASE_PATH+"/shop/customer_appoint/index/ng";
                        },true);
                    }
                }
            });
        });
    });
})(jQuery, seajs);


// 新增客户 弹框
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
						appoint.fillCustomer(null,json["data"]);

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


// 添加服务 弹框
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

								// 清空当前行
								optCurrentRow.find('input').val('');
								// 服务类型{1：基本服务；2：附加服务}
								$('input[name="type"]', optCurrentRow).val(1);
								// 金额单位“元”
								$('input[name="currencyunit"]', optCurrentRow).val("元");

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
								//appoint.calculatePrice();

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
;

// 记住当前操作对象
var optObject ;
var rememberMe =function(obj){
	optObject =$(obj);
};

// 每次初始化页面后，重新计算金额
appoint.calcFee();


// note 提示框
$.remark({
	selector : "input.orderNoteClass"
});

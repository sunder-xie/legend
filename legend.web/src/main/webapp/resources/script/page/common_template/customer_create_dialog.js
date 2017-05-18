
$(function(){
	var $doc = $(document);

	var car_title = $(".car_select li");
	var car_type = $(".car_type_cont");
	//全部品牌数据缓存。
	var PinPaiDataCache = null;
	//热门品牌数据缓存。
	var HotPinPaiDataCache = null;
	//车型导航选择
	$doc.on("click",".car_select li",function(){
		var $this = $(this);
		var index = $this.index();
		$this.addClass('current').siblings().removeClass('current');
		car_type.removeClass('current').eq(index).addClass('current');
	});
	//车型选择
	$doc.off("click",".car_type").on("click",".car_type",function(){
		var $this = $(this);
		var pid = $this.data("pid");
		var parents = $this.parents(".car_type_cont");
		var text = $("span",$this).text();
		var index = car_type.index(parents);
		var size = car_title.size();
		var importInfo = $("span",$this).attr("importinfo");
		//已选择的车型类别数量
		var selectedList = $(".car_select_show span");
		var selectedSize = selectedList.size();
		var selectedHtml = '';

		if(importInfo!=null&&importInfo!=""&&importInfo!=undefined){
			selectedHtml = '<span txt="'+text+'" importinfo="'+importInfo+'" pid="'+pid+'">'+text+'<i>×</i></span>';
		}else{
			selectedHtml = '<span txt="'+text+'" pid="'+pid+'">'+text+'<i>×</i></span>';
		}
		//请求下一级数据。
		var reqNextData = function(){
			seajs.use(["ajax","artTemplate"],function(ajax,art){
				ajax.get({
					url:BASE_PATH+"/shop/car_category/ng",
					data:{"pid":pid},
					success:function(json){
						var html = art("car_type_2",{"json":json});
						car_type.eq(index+1).find(".car_type_cont_inner").html(html);
					}
				});
			});
		}
		if((index+1) < selectedSize || ((index+1) == selectedSize && (index+1)!=3)){
			//已选择数据，做更改操作
			selectedList.eq(index).nextAll("span").remove();
			selectedList.eq(index).replaceWith(selectedHtml);
			reqNextData();

			//tab内容
			car_type.eq(index+1)
				.addClass('current')
				.siblings()
				.removeClass('current');
			//tab标签
			car_title
				.eq(index+1)
				.addClass('current')
				.siblings()
				.removeClass('current');

			car_title
				.eq(index+1)
				.nextAll()
				.hide();

		}else if((index+1) == selectedSize ){
			//最后一级，只删除当前数据，不请求下一级数据
			selectedList.eq(index).nextAll("span").remove();
			selectedList.eq(index).replaceWith(selectedHtml);
		}else{
			//没选择数据，新增操作
			if((index+1) < size){
				$(".car_select_show").append(selectedHtml);
				//不是最后一级
				car_type.eq(index+1)
					.addClass('current')
					.siblings()
					.removeClass('current');

				car_title
					.eq(index+1)
					.prevAll()
					.removeClass('current')
					.show();

				car_title
					.eq(index+1)
					.addClass('current')
					.show();

				//请求下一级数据，并渲染填充html
				seajs.use(["ajax","artTemplate"],function(ajax,art){
					ajax.get({
						url:BASE_PATH+"/shop/car_category/ng",
						data:{"pid":pid},
						success:function(json){
							var html = "";
							if(index+1 == 1){
								html = art("car_type_2",{"json":json,"type":2});
							}else{
								html = art("car_type_2",{"json":json,"type":0});
							}
							car_type.eq(index+1).find(".car_type_cont_inner").html(html);
						}
					});
				});
			}else if((index+1) == size){
				$(".car_select_show").append(selectedHtml);
				//最后一级，取值，关闭弹窗
				var str = "";
				var importInfo = "";
				$("span",".car_select_show").each(function(i){
					var _txt = $(this).attr("txt");
					var _id = $(this).attr("pid");

					if(i==1){
						var _importInfo = $(this).attr("importinfo");
						if(_importInfo!=null&&_importInfo!=""&&_importInfo!=undefined){
							str += "(" + _importInfo + ") ";
							$("input[name='importInfo'],input[name='customerCar.importInfo'],input[name='orderInfo.importInfo']").val(_importInfo);
						}
					}else{
						str += _txt + " ";
					}
					//车型数据分开回填
					//带前辍的字段是为了兼容客户管理编辑页面的字段
					switch(i){
						case 0:
							$("input[name='carBrandId'],input[name='customerCar.carBrandId'],input[name='orderInfo.carBrandId']").val(_id);
							$("input[name='carBrand'],input[name='customerCar.carBrand'],input[name='orderInfo.carBrand']").val(_txt);
							break;
						case 1:
							$("input[name='carSeriesId'],input[name='customerCar.carSeriesId'],input[name='orderInfo.carSeriesId']").val(_id);
							$("input[name='carSeries'],input[name='customerCar.carSeries'],input[name='orderInfo.carSeries']").val(_txt);
							break;
						case 2:
							$("input[name='carModelId'],input[name='customerCar.carModelId'],input[name='orderInfo.carModelsId']").val(_id);
							$("input[name='carModel'],input[name='customerCar.carModel'],input[name='orderInfo.carModels']").val(_txt);
							//获取车辆级别
							seajs.use(["ajax"],function(ajax){
								ajax.post({
									url: BASE_PATH + "/shop/customer/get_car_level?id="+_id,
									success:function (json) {
										if (json && json.success) {
											$("input[name='carLevel'],input[name='customerCar.carLevel'],input[name='orderInfo.carLevel']").val(json.data.guidePrice);
										} else {
											dg.info(json.errorMsg, 3);
										}
									}
								});
							});
							break;
					}
				});
				//车型数据连起来回填
				$("#carMode").val($.trim(str)).blur();

				seajs.use("dialog",function(dg){
					dg.closeDialog("#carType_select_dialog");
				});
				//激活年款排量输入框
				$("input[name='yearPowerBak']").removeAttr('disabled').val("");
			}
		}
	});
	//已选择的删掉
	$doc.off("click",".car_select_show i").on("click",".car_select_show i",function(){


		var index = $(".car_select_show i").index($(this));
		var parents = $(this).parents("span");
		parents.nextAll("span").remove();
		parents.remove();

		//tab内容
		car_type.eq(index)
			.addClass('current')
			.siblings()
			.removeClass('current');
		//tab标签
		car_title
			.eq(index)
			.addClass('current')
			.siblings()
			.removeClass('current')

		car_title
			.eq(index)
			.nextAll()
			.hide();
	});

	//删除掉热门品牌图片地址host
	var changeImgUrlData = function(json){
		var _json = null;
		if(json && json.data){
			for(var i=0;i<json.data.length;i++){
				var imgUrl = json.data[i].logo;
				if(imgUrl != null){
					imgUrl = imgUrl.substring(imgUrl.indexOf(".com/")+5);
					json.data[i].logo = imgUrl;
				}else{
					delete json.data[i];
				}
			}
			_json = json;
		}
		return _json;
	}

	//字母过滤品牌
	$doc.on("click",".letter_box span",function(){
		$(this).addClass('current').siblings().removeClass('current');
		var keyword = $(this).attr("keyword");
		seajs.use(["artTemplate"],function(art){
			if(keyword == "hot"){
				//热门车型
				if(HotPinPaiDataCache){
					var html = art("car_type_1",{"json":HotPinPaiDataCache});
					car_type.eq(0).find(".car_type_cont_inner").html(html);
				}
			}else{
				//根据字母筛选车型
				if(PinPaiDataCache){
					var obj = {};
					obj.data = PinPaiDataCache.data[keyword];

					//处理字母筛选数据与常用车型数据格式和字段名不一致问题
					for(var i=0;i<obj.data.length;i++){
						obj.data[i].carBrandId = obj.data[i].id;
						obj.data[i].carBrand = obj.data[i].name;
					}
					var html = art("car_type_1",{"json":obj});
					car_type.eq(0).find(".car_type_cont_inner").html(html);
				}
			}
		});
	});

	//首次访问加载热门数据
	seajs.use(["ajax","artTemplate"],function(ajax,art){
		art.helper('$toStr', function (obj) {
			return JSON.stringify(obj);
		});
		//获取热门或者常用车型品牌
		ajax.get({
			url : BASE_PATH + "/shop/car_category/comm_brand",
			success : function(json){
				HotPinPaiDataCache = changeImgUrlData(json);
				var html = art("car_type_1",{"json":HotPinPaiDataCache});
				car_type.eq(0).find(".car_type_cont_inner").html(html);
			}
		});
		//获取所有车型品牌
		ajax.get({
			url:BASE_PATH + '/shop/car_category/brand_letter',
			success:function(json){
				if(json.success){
					PinPaiDataCache = json;
				}
			}
		});
	});

	//绑定车型选择框
	$doc.off("click",".carType_btn").on("click",".carType_btn",function(){
		seajs.use("dialog",function(dg){
			dg.dialog({
				"dom":"#carType_select_dialog",
				"close":function(){
					clearCarDataHistory();
				}
			});
		});
	});

	//车型选择框初始化
	function clearCarDataHistory(){
		//清除已选车型数据
		$("span",".car_select_show").remove();
		//只保留第一级tab标签。
		$("li:gt(0)",".car_select").hide();
		$("li",".car_select").eq(0).addClass("current");
		$(".car_type_cont:gt(0)").removeClass('current');
		$(".car_type_cont").eq(0).addClass('current');
		//品牌筛选定位到第一级。
		$("span",".letter_box").removeClass('current').eq(0).addClass('current');

		$(".letter_box span:eq(0)").trigger('click');
	}

	//车型检索框清空时，所有的隐藏域值清空
	$doc.on("keyup","input[name='carModeBak']",function(e){
		if(e.keyCode!=8) return;
		var val = $(this).val();
		var fieldArr = [
			//品牌id
			"carBrandId",
			//品牌
			"carBrand",
			//车系id
			"carSeriesId",
			//车系
			"carSeries",
			//车型id
			"carModelId",
			//车型
			"carModel",
			//进口与国产
			"importInfo",
			//年款id
			"carYearId",
			//年款
			"carYear",
			//排量id
			"carPowerId",
			//排量
			"carPower",
			//变速箱id
			"carGearBoxId",
			//变速箱
			"carGearBox",
			//年款排量显示框
			"yearPowerBak",
		];
		if(val==""){
			for(var i = 0;i < fieldArr.length;i++){
				if((i+1) != fieldArr.length){
					$("input[name='"+fieldArr[i]+"']").val("");
					$("input[name='customerCar."+fieldArr[i]+"']").val("");
					$("input[name='orderInfo."+fieldArr[i]+"']").val("");
				}else{
					$("input[name='"+fieldArr[i]+"']").val("");
				}
			}
			$("input[name='yearPowerBak']").attr("disabled","disabled");
		}
	});

	//年款排量检索框清空时，有关年款排量变速箱的隐藏域值清空
	$doc.on("keyup","input[name='yearPowerBak']",function(e){
		if(e.keyCode!=8) return;
		var val = $(this).val();
		var fieldArr = [
			//年款id
			"carYearId",
			//年款
			"carYear",
			//排量id
			"carPowerId",
			//排量
			"carPower",
			//变速箱id
			"carGearBoxId",
			//变速箱
			"carGearBox",
			//年款排量显示框
			"yearPowerBak",
		];
		if(val==""){
			for(var i = 0;i < fieldArr.length;i++){
				if((i+1) != fieldArr.length){
					$("input[name='"+fieldArr[i]+"']").val("");
					$("input[name='customerCar."+fieldArr[i]+"']").val("");
					$("input[name='orderInfo."+fieldArr[i]+"']").val("");
				}else{
					$("input[name='"+fieldArr[i]+"']").val("");
				}
			}
		}
	});

	/*
	 * modify by sky 20160329
	 * VIN码搜索车型
	 */
	$doc.on('keyup', 'input[name=vin], input[name="customerCar.vin"]', function() {
		var $this = $(this),
			value = $this.val(),
			serviceUrl = BASE_PATH + '/shop/car_category/car_model_by_vin',
			$carModel,
			oldServiceUrl;

		// 达到VIN码长度

		if (value.length === 17) {
			$carModel = $('input[name=carModeBak]');
			oldServiceUrl = $carModel.attr('service_url');
			// 设置VIN码获取车型的接口
			$carModel.attr('service_url', serviceUrl);
			$carModel.attr('ext_data', 'vin_hidden');
			$carModel.attr('callback_fn', 'carModeByVinFn');
			$('[name=vin_hidden]').val(value);

			$carModel.trigger('click');
			// 重置车型选择接口
			$carModel.attr('service_url', oldServiceUrl);
			$carModel.removeAttr('ext_data');
			$carModel.attr('callback_fn', 'carModeFn');
			$this.val(value);
		}
	});
	/* modify by sky 20160329 end */

	seajs.use("downlist");
});

//计算下次保养里程
var getUpkeepMileage = function() {
	var carModelId = $("input[name='orderInfo.carModelsId']").val();
	var mileage = $("input[name='orderInfo.mileage']").val();

	if (!$.isNumeric(carModelId) || !$.isNumeric(mileage) || mileage <= 0) {
		return;
	}
	seajs.use(["ajax"],function(ajax){
		ajax.get({
			url: BASE_PATH + "/shop/order/get_upkeep_mileage",
			data: {carModelId: carModelId, mileage: mileage},
			success : function(json) {
				if (json != null && json != undefined && json.success) {
					$("input[name='orderInfo.upkeepMileage']").val(json.data);
				} else {
					$("input[name='orderInfo.upkeepMileage']").val(null);
				}
			}
		});
	});
};

//车辆型号的下拉框回调方法
function carModeFn(obj,item,scope){
	// <!-- 品牌 -->
	$("input[name='carBrandId'],input[name='customerCar.carBrandId'],input[name='orderInfo.carBrandId']").val(item.brandId);
	$("input[name='carBrand'],input[name='customerCar.carBrand'],input[name='orderInfo.carBrand']").val(item.brand);
	// <!-- 车系 -->
	$("input[name='carSeriesId'],input[name='customerCar.carSeriesId'],input[name='orderInfo.carSeriesId']").val(item.seriesId);
	$("input[name='carSeries'],input[name='customerCar.carSeries'],input[name='orderInfo.carSeries']").val(item.series);
	// <!-- 进口与国产 -->
	$("input[name='importInfo'],input[name='customerCar.importInfo'],input[name='orderInfo.importInfo']").val(item.importInfo);
	// <!-- 车型 -->
	//carModelId 作为后3级级联数据的一个筛选条件，放宽了赋值范围
	$("input[name='carModelId'],input[name='customerCar.carModelId'],input[name='orderInfo.carModelsId']").val(item.modelId);
	$("input[name='carModel'],input[name='customerCar.carModel'],input[name='orderInfo.carModels']").val(item.model);
	//只给用户展示一个字段，不会提交到后台-----车辆型号
	//获取车辆级别
	seajs.use(["ajax"],function(ajax){
		ajax.post({
			url: BASE_PATH + "/shop/customer/get_car_level?id="+item.modelId,
			success:function (json) {
				if (json && json.success) {
					$("input[name='carLevel'],input[name='customerCar.carLevel'],input[name='orderInfo.carLevel']").val(json.data.guidePrice);
				} else {
					dg.info(json.errorMsg, 3);
				}
				//计算下次保养里程
				getUpkeepMileage();
			}
		});
	});
	//blur为了失去焦点触发验证事件
	var carModeStr = "";
	if(item.importInfo!=null && item.importInfo!=""){
		carModeStr = item.brand+" ("+item.importInfo+") "+item.model;
	}else{
		carModeStr = item.brand+" "+item.model;
	}
	$("input[name='carModeBak']").val(carModeStr).blur();

	//激活年款排量输入框
	$("input[name='yearPowerBak']").removeAttr('disabled').val("");
}

// 通过VIN码触发的车辆型号的下拉框回调方法
function carModeByVinFn(obj,item,scope) {
	carModeFn(obj, item, scope);
    $('input[name="carPowerId"]').val(item.powerId);
    $('input[name="carPower"]').val(item.power);
    $('input[name="carYearId"]').val(item.yearId);
    $('input[name="carYear"]').val(item.year);
	// 年款排量输入框填充数据
	$("input[name='yearPowerBak']").val(item.power);
}

//年款排量的下拉框回调方法
function yearPowerFn(obj,item,scope){
	//<!-- 年款 -->
	$("input[name='carYearId'],input[name='customerCar.carYearId'],input[name='orderInfo.carYearId']").val(item.yearId);
	$("input[name='carYear'],input[name='customerCar.carYear'],input[name='orderInfo.carYear']").val(item.year);
	//<!-- 排量 -->
	$("input[name='carPowerId'],input[name='customerCar.carPowerId'],input[name='orderInfo.carPowerId']").val(item.powerId);
	$("input[name='carPower'],input[name='customerCar.carPower'],input[name='orderInfo.carPower']").val(item.power);
	//<!-- 变速箱 -->
	$("input[name='carGearBoxId'],input[name='customerCar.carGearBoxId'],input[name='orderInfo.carGearBoxId']").val(item.gearboxId);
	$("input[name='carGearBox'],input[name='customerCar.carGearBox'],input[name='orderInfo.carGearBox']").val(item.gearbox);

	//只给用户展示一个字段，不会提交到后台-----年款排量
	$("input[name='yearPowerBak']").val(item.year+" "+item.gearbox);
}
//客户来源的回调
function fillCustomerSource(obj, item) {
	// 来源名称
	$('input[name="customerSource"]').val(item["source"]);
}
// 新增客户弹出框
function createCustomer(obj){
	var scope = "#customer_create_dialog";
	seajs.use([ "dialog","chosenSelect","ajax" ], function(dg,chosen,ajax) {
		var dialogId = dg.dialog({
			"dom" : scope,
			"init" : function(){
				$("input[name='yearPowerBak']").attr("disabled","disabled");
				ajax.post({
					url: BASE_PATH + "/shop/customer/get_license_prefix",
					success: function (json) {
						if (json && json.success) {
							chosen.handleChoosenSelect('.qxy_dialog select');
							$("input[name='license']",scope).val(json.data);
						} else {
							dg.info(json.errorMsg, 3);
						}
					}
				});
				//在线预约客户，弹出新增客户窗口时需要部分数据回填到弹框。
				if(obj!=undefined){
					if(obj.license!=null&&obj.license!=""){
						$("input[name='license']",scope).val(obj.license);
					}
					$("input[name='contact']",scope).val(obj.customerName);
					$("input[name='contactMobile']",scope).val(obj.mobile);
					$("input[name='customerName']",scope).val(obj.customerName);
					$("input[name='mobile']",scope).val(obj.mobile);
					$("input[name='appointId']").val(obj.appointId);
				}
			}
		});

		//取消按钮事件绑定
		$(".cancel",scope).off("click").on("click",function(){
			dg.closeDialog(scope);
		});

		//保存按钮事件绑定
		$(".save",scope).off("click").on("click",function(){
			//先验证一下车型数据是否选择
			//品牌
			//var carBrand = $("input[name='carBrand']",scope).val();
			//车系
			//var carSeries = $("input[name='carSeries']",scope).val();
			//车型
			//var carModel = $("input[name='carModel']",scope).val();

			/*
			 * modify by sky 20160330
			 * vin码验证
			 */
			var vin = $('input[name=vin]', scope).val();
			if (vin && vin.length !== 17) {
				dg.info('VIN码必须为17位', 3);
				return;
			}
			/* modify by sky 20160330 */

			/*if(carBrand==""||carSeries==""||carModel==""){
			 seajs.use("dialog",function(dg){
			 dg.info("请选择一个车型数据",3,2,null,true);
			 });
			 return;
			 }*/

			util.submit({
				formid:"customer_add_form",
				callback:function(json){
					if(json.success){
						dg.closeDialog(scope);
						dg.info("操作成功",1,1,function(){
							//工单页面保存后的数据回填
							$('input[name="orderInfo.carLicense"]').val(json["data"]["license"]);
							// 填充客户信息,调用了工单script/page/order/add.js
							// 填充客户信息,调用了工单script/page/customer/appointment/add.js
							try{
								order.fillCustomer(null,json["data"]);
							}catch(e){}
							try{
								gift.fillCustomer(null,json["data"]);
							}catch(e){}
							try{
								appoint.fillCustomer(null,json["data"]);
							}catch(e){}
							//预约单列表
							try{
								appointList.reload();
							}catch(e){}
							/*
							 * modify by sky 20160330
							 * 会员新增填充数据
							 */
							try {
								member.fillNewMember(null, json['data']);
							} catch (e) {}
							/* modify by sky 20160330 end */
						},true);
					}else{
						dg.info(json.errorMsg,3);
					}
				}
			});
		});
	});
}
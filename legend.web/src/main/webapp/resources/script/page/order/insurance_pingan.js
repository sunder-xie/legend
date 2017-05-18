$(function () {
	seajs.use("downlist");

	var dgIndex = null;

	var doc = $(document);

	//成功信息停留时间
	var yesTime = 3;
	//错误信息停留时间
	var noTime = 4;

    // 获取错误信息隐藏域
	var $auditStatus = $("#auditStatus");

	//工单备注初始化。
	$.remark({
		"selector": "input[name='serviceNote'],input[name='goodsNote']"
	});

	//工单保存和结算保存公用方法
	var order_save = function (fn, postUrl) {
		seajs.use(["formData", "dialog", "ajax", "check"], function (fd, dg, ax, ck) {
			// 先校验服务项目之上的数据
			var result = ck.check2(".must_scope");
			if (!result) {
				return;
			}

			// 获取整个页面的数据
			var data = fd.get3(".must_scope");

			//数据发送
			ax.post({
				url: BASE_PATH + postUrl,
				data: data,
				success: function (json) {
					if (json.success) {
						fn && fn(json);
					} else {
						dg.info(json.errorMsg, 3, noTime, null, true);
					}
				}
			});
		});
	};

    if($auditStatus.val() == 3) {
        seajs.use(["ajax", "dialog"], function(ajax, dg) {
            var msg = $auditStatus.data("result");
            dg.info(msg, 3, noTime, null, true);
        });
    }

	//工单数据保存
	doc.on("click", ".save", function () {
		order_save(function (json) {
			seajs.use(["dialog", "artTemplate"], function (dg, at) {
				var html = at('saveDialog', {"orderid": json['data']['orderId']});
				//打开服务弹框
				dgIndex = dg.dialog({
					html: html,
					close: function () {
						window.location.href = BASE_PATH + "/shop/activity/join?acttplid=1&billid=" + json['data']['id'];
					}
				});
			}, '');
		}, '/shop/activity/bill/save');
	});

	//工单数据保存
	doc.on("click", ".update", function () {
		order_save(function (json) {
			seajs.use(["dialog", "artTemplate"], function (dg, at) {
				var html = at('saveDialog', {"orderid": json['data']['orderId']});
				//打开服务弹框
				dgIndex = dg.dialog({
					html: html,
					close: function () {
						window.location.href = BASE_PATH + "/shop/activity/join?acttplid=1&billid=" + json['data']['id'];
					}
				});
			}, '');
		}, '/shop/activity/bill/update');
	});

	//工单数据重新提交[审核失败状态]
	doc.on("click", ".resubmit", function () {
		seajs.use(["dialog", "artTemplate"], function (dg, at) {
			dg.confirm("你确认结算平安保养服务单?", function () {
				order_save(function (json) {
					var html = at('submitDialog', {"orderid": json['data']['orderId']});
					//打开服务弹框
					dgIndex = dg.dialog({
						html: html,
						close: function () {
							window.location.href = BASE_PATH + "/shop/activity/join?acttplid=1&billid=" + json['data']['id'];
						}
					});
				}, '/shop/activity/bill/resubmit');
			}, function () {
				return false;
			});

		}, '');
	});

	//工单数据保存
	doc.on("click", ".submit", function () {
		seajs.use(["dialog", "artTemplate"], function (dg, at) {
			dg.confirm("'提交审核'前,请单击'编辑保存'按钮,保存服务单信息,<br\/>你确认已保存服务单信息吗?", function () {
				dg.confirm("你确认结算平安保养服务单?", function () {
					order_save(function (json) {
						var html = at('submitDialog', {"orderid": json['data']['orderId']});
						//打开服务弹框
						dgIndex = dg.dialog({
							html: html,
							close: function () {
								window.location.href = BASE_PATH + "/shop/activity/join?acttplid=1&billid=" + json['data']['id'];
							}
						});
					}, '/shop/activity/bill/submit');
				}, function () {
					return false;
				});
			}, function () {
				return false;
			});

		});
	});

	//照片上传
	seajs.use(["upload", "dialog", "artTemplate"], function (a, dg, at) {
		var uploadInit = function (selectorName, btnText) {
			a();
			var layerIndex = null;
			var reg = /[\.gif|\.jpg|\.png|\.jpeg]/i;
			$(selectorName).uploadifive({
				buttonClass: "btn_upload btn btn_primary",
				width: 120,
				height: 30,
				fileTypeDesc: 'Image Files',
				fileTypeExts: '*.gif; *.jpg; *.png; *.GIF; *.JPG; *.PNG; *.jpeg; *.JPEG',
				buttonText: btnText,
				uploadScript: BASE_PATH + '/index/oss/upload_image',
				onSelect: function () {
					layerIndex = dg.load("图片正在上传中...");
				},
				onSelectError: function () {
					layerIndex != null && dg.close(layerIndex);
					dg.info('请选择正确的图片格式', 3, 4);
				},
				onUploadComplete: function (file, data) {
					layerIndex != null && dg.close(layerIndex);
					if (/出错/.test(data)) {
						if (reg.test(file.name)) {
							dg.info('上传失败，图片格式应为 *.gif *.jpg *.png *.jpeg', 3, 3);
						}
						return;
					}
					layerIndex != null && dg.close(layerIndex);
					data = eval('(' + data + ')');
					if (data && data.state.toUpperCase() == "SUCCESS") {
						var imgUrl = data.url;
						var parent = $(this).parents(".img_item");
						$('img', parent).attr("src", imgUrl);
						$('input[type="hidden"]', parent).val(imgUrl);
					}
				},
				onUploadError: function () {
					layerIndex != null && dg.close(layerIndex);
					dg.info('上传失败', 3, 3);
				}
			});
		}

		uploadInit('#u1', '请上传车牌照片');
		uploadInit('#u2', '请上传受损部位照片');
		uploadInit('#u3', '请上传服务后照片');

	});

	doc.on("keyup", ".J_input_limit", function () {
		var self = $(this);
		var limit_type = self.data('limit_type');
		var value = self.val();
		var t_value = '';

		if (limit_type === 'zh') {
			t_value = value.replace(/[\u4E00-\u9FA5]/gi, '');
			if (t_value !== value) {
				self.val(t_value);
			}
		}
	});

	// 保养套餐级别选择
	doc.on("change", "#BYChosen", function () {
		// clear 之前选中
		$("input[name='orderInfo.serviceId']").val("");

		var serviceId = $(this).val();
		seajs.use(["ajax", "artTemplate"], function (ajax, template) {
			ajax.get({
				url: BASE_PATH + "/shop/shop_service_info/shopservice/get",
				data: {serviceid: serviceId},
				success: function (result) {
					if (result["success"] == true) {
						var service = result["data"];
						$("input[name='orderInfo.serviceId']").val(service["id"]);
						$("#tbody").html(template("tbodyTpl", {"item":service}));
					}
				}
			})
		});
	});
});

var callback = {
	//车牌列表回调方法
	getCustomerId: function (obj, item) {
		seajs.use("ajax", function (ajax) {
			ajax.get({
				url: BASE_PATH + '/shop/customer_car/get_car_by_license',
				data: {carLicense: item.license},
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

						$("input[name='orderInfo.carYearId']").val(json.data.carYearId);
						$("input[name='orderInfo.carYear']").val(json.data.carYear);
						$("input[name='orderInfo.carPowerId']").val(json.data.carPowerId);
						$("input[name='orderInfo.carPower']").val(json.data.carPower);
						$("input[name='orderInfo.carGearBoxId']").val(json.data.carGearBoxId);
						$("input[name='orderInfo.carGearBox']").val(json.data.carGearBox);
						var carYear = json.data.carYear != null ? json.data.carYear : "";
						var carGearBox = json.data.carGearBox != null ? json.data.carGearBox : "";
						var carPower = json.data.carPower != null ? json.data.carPower : "";
						if(carYear != ""){
							if(carGearBox != ""){
								$("input[name='yearPowerBak']").val(carYear + " " + carGearBox);
							}else if (carPower != ""){
								$("input[name='yearPowerBak']").val(carYear + " " + carPower);
							}
						}
						$("input[name='orderInfo.importInfo']").val(json.data.importInfo);
						$("input[name='orderInfo.customerName']").val(json.data.customerName);
						$("input[name='orderInfo.customerMobile']").val(json.data.mobile);
						$("input[name='orderInfo.mileage']").val(json.data.mileage);
						$("input[name='orderInfo.vin']").val(json.data.vin);
						// 客户单位
						$("input[name='orderInfo.company']").val(json.data.company);
					}
				}
			});
		});
	}
};


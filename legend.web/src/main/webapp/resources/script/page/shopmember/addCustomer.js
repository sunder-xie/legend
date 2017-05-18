//新增客户。
$(function(){
	// 提交
	$("body").off("click",".add_customer_btn").on("click",".add_customer_btn",function(){
		seajs.use([ "dialog","chosenSelect","ajax" ], function(dg,chosen,ajax) {
			var dialogId = dg.dialog({
				dom : "#customer_create_dialog",
				init : function(){
					ajax.post({
						url: BASE_PATH+"/shop/customer/get_license_prefix",
						success:function(json){
							if(json && json.success){
								chosen.handleChoosenSelect('#customer_create_dialog select');
								$("input[name='license']").val(json.data);
								dg.closeDialog("#addNew");
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
			$(".save","#customer_create_dialog").click(function(){
				seajs.use(["dialog"], function (dialog) {
					util.submit({
						formid: "customer_add_form",
						callback: function (json) {
							if (json.success) {
								/**
								 * 反填会员信息
								 */
								if(json.data){
									$("input[name='customerName']").val(json.data.customerName);
									$("input[name='birthday']").val(json.data.birthdayStr);
									$("input[name='mobile']").val(json.data.mobile);
									$("input[name='vin']").val(json.data.vin);
									$("input[name='license']").val(json.data.license);

								}

								dialog.info("操作成功", 1);

								dialog.closeDialog("#customer_create_dialog");
							} else {
								dialog.info(json.errorMsg, 3);
							}
						}
					});
				});

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
	});
});
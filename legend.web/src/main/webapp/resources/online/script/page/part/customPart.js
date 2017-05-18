$(document).ready(function() {
	if ($("#is_common_part").attr("checked")) {
		$("#chexing").hide();
		$("#own_chexing").hide();
	}
	util.uploadInit();
	seajs.use("downlist");
	
	seajs.use("ajax",function(ajax){
		var goodsId = $("input[name='id']").val();
		if(goodsId){
			ajax.post({
				url:BASE_PATH+"/shop/goods/getCheckedOwnCar",
				data:{goodsId:goodsId},
				success:function(json){
					$.each(json.data,function(i,e){
						$("input[value='"+e.id+"']").attr('checked','checked');
						$("input[value='"+e.id+"']").attr('relId',e.relId);
					})
				}
			});
		}
	});
})
function addCustomCate() {
	$("#add_custom_cate").find("input").val('');
	seajs.use([ "dialog" ], function(dg) {
		dg.dialog({
			"dom" : "#add_custom_cate"
		});
	});
};
function submitCustomCate() {
	util.submit({
		formid : 'add_custom_cate_form',
		callback : function(json, dialog) {
			if (json.success) {
				dialog.info("操作成功", 1);
				seajs.use([ "select", "chosenSelect" ],
						function(select, chosen) {
							select.init();
							chosen.handleChoosenSelect('.chosen');
						});
				layer.closeAll();
			} else {
				dialog.info(json.errorMsg, 3);
			}
		}
	})
};
function isCommon() {
	if ($("#is_common_part").attr("checked")) {
		$("#chexing").hide();
		$("#own_chexing").hide();
	} else {
		$("#chexing").show();
		$("#own_chexing").show();
	}

}

function submitGoods() {
	seajs.use([ "check", "ajax", "formData", "dialog" ], function(check, ajax,
			formData, dialog) {
		var result = check.check("#custom_part_form");
		if (!result) {
			return;
		}
		var extData = {
			tqmallGoodsId : 0,
			tqmallGoodsSn : 0,
			tqmallStatus : 4,
			goodsStatus : 0,
			goodsType : 0,
			brandName : "",
			brandId : 0
		};
		var $cat = $("select[name='catId']").find("option:selected");
		var catId = $cat.val();
		var catName = $cat.text();
		extData["cat2Id"] = catId;
		extData["cat2Name"] = catName;
		if ($("#is_common_part").prop("checked")) {
			extData["carInfo"] = "0";
			extData['goodsCarList'] = [];
			extData['ownCarIdList'] = [];
		} else {
			var goodsCarList = [];
			$("#chexing .form_item").each(
					function(i, e) {
						var chexing = {};
						$e = $(e);

						// 获取品牌信息
						$carBrand = $e.find("select[name='carBrandId']");
						if ($carBrand.val()) {
							chexing['carBrandId'] = $carBrand.val();
							chexing['carBrandName'] = $carBrand.find(
									"option:selected").text();
						} else {
							return

							

						}
						// 获取车型信息
						$carSeries = $e.find("select[name='carSeriesId']");
						if ($carSeries.val()) {
							chexing['carSeriesId'] = $carSeries.val();
							chexing['carSeriesName'] = $carSeries.find(
									"option:selected").text();
						} else {
							return;
						}

						// 获取排量信息
						$carPower = $e.find("select[name='carPowerId']");
						if ($carPower.val()) {
							chexing['carPowerId'] = $carPower.val();
							chexing['carPowerName'] = $carPower.find(
									"option:selected").text();
						} else {
							return;
						}

						// 获取年份信息
						$carYear = $e.find("select[name='carYearId']");
						if ($carYear.val()) {
							chexing['carYearId'] = $carYear.val();
							chexing['carYearName'] = $carYear.find(
									"option:selected").text();
						} else {
							return;
						}

						chexing['carAlias'] = $e.find("input[name='carAlias']")
								.val();
						goodsCarList.push(chexing);
					})
			extData['goodsCarList'] = JSON.stringify(goodsCarList);
			var chk_own_ids = $("input[name='ownCarIds']:checked");
			if(chk_own_ids){
				var ownCarIds = [];
				chk_own_ids.each(function(i,e){
					ownCarIds.push({id:$(e).val(),relId:$(e).attr("relId")})
				})
				extData['ownCarIdList'] = JSON.stringify(ownCarIds);
			}
		}
		
		util.submit({
			formid : 'custom_part_form',
			extData : extData,
			callback : function(json, dialog) {
				if (json.success) {
					dialog.info("操作成功", 1);
					window.location.href = document.referrer;
				} else {
					dialog.info(json.errorMsg, 3);
				}
			}
		})
	})
}

function fillAttr() {
	cateId = $("#cate_id").find("option:selected").val();
	if (cateId == "-1") {
		// 添加分类
	} else if (cateId == "") {
		//
	} else {
		seajs.use([ "ajax", "artTemplate" ], function(ajax, artTemplate) {
			// 请求服务配件数据
			ajax.get({
				url : BASE_PATH + '/shop/goods_attributes/get_by_type_id',
				data : {
					type_id : cateId
				},
				success : function(json) {
					if (json && json.success && json.data) {
						var attr_list = json.data;
						$("#dynamicAttr").html('');
						var template = artTemplate.compile($("#attr_template")
								.html());
						var html = template({
							data : attr_list
						});
						$("#dynamicAttr").html(html);
					}
				}
			});

		});
	}
}

$(function(){
	//批量添加配件按钮事件
	$("body").on("click",".batch_add_goods",function(){
		var group = $(this).parents(".qxy_group");
		seajs.use(["dialog","ajax","artTemplate","chosenSelect"],function(dialog,ajax,artTemplate,chosen){
			
			$.when(
				//请求配件类别
				// ajax.post({
				// 	url: BASE_PATH + "/shop/goods_category/shop_list"
				// }),
				//请求配件品牌
				ajax.post({
					url: BASE_PATH + "/shop/goods_brand/shop_list"
				})
			).done(function(){
				//显示弹框
				dialog.dialog({
					dom:"#batch_add_goods"
				});
			}).done(function(data2){
				// var html = artTemplate("search_catId_tpl",{"json":data1});
				// html = '<option value="">请选择配件类别</option>' + html;
				// $("#search_catId").html(html);

				var html = artTemplate("search_brandId_tpl",{"json":data2});
				html = '<option value="">请选择配件品牌</option>' + html;
				$("#search_brandId").html(html);

				$("#batch_add_goods").data("group",group); 

				chosen.handleChoosenSelect(".chosen","#batch_add_goods");
			});
		});
		return false;
	});
	//批量添加配件搜索配件事件
	$("body").on("click",".batch_search_btn",function(){
		seajs.use([
			"dialog",
			"ajax",
			"artTemplate",
			"chosenSelect",
			"formData"],function(dialog,ajax,artTemplate,chosen,formData){
			var data = formData.get2(".batch_search_left");

			data["size"] = 1000;
			data["page"] = 1;
			ajax.get({
				url: BASE_PATH + "/shop/goods/search/json",
				data: data,
				success : function(json){
					artTemplate.helper("$toJsonString",function(data){
						return JSON.stringify(data);
					});
					var html = artTemplate("search_result_tpl",{"json":json});
					$(".batch_content","#batch_search_result").html(html);
					// chosen.handleChoosenSelect("#search_brandId");
				}
			});
		});
	});

	//搜索结果配件列表，添加到已选配件列表
	$("body").on("click","#batch_search_result .goods_data_item",function(){

		var selector = '#batch_chosen_result .batch_content';
		var id = $(this).data("id");
		var bool = true;
		$(".goods_data_item",selector).each(function(){
			if($(this).data("id") == id){
				seajs.use("dialog",function(dialog){
					dialog.info("请勿重复添加",3,2);
				});
				bool = false;
				return false;
			}
		});
		if(!bool) return;
		var html = $(this).clone();
		$("a",html).text('[删除]').removeClass('batch_add').addClass('batch_del');
		$(html).appendTo(selector);
		calcCount();
	});
	//统计已选配件数量
	var calcCount = function(){
		var chosenGoods = $(".goods_data_item","#batch_chosen_result");
		$(".batch_title span","#batch_chosen_result").text(chosenGoods.size());
	}
	//删除已选配件列表
	$("body").on("click","#batch_chosen_result .goods_data_item",function(){
		$(this).remove();
		calcCount();
	});
	//全部清空已选配件列表
	$("body").on("click",".batch_title .clear_all",function(){
		$(".batch_content",$(this).parents(".batch_box")).empty();
		calcCount();
	});
	//弹框取消关闭
	$("body").on("click",".dialog_cancel",function(){
		var id = $(this).parents(".qxy_dialog").attr("id");
		seajs.use("dialog",function(dialog){
			dialog.closeDialog("#"+id);
		});
	});
	//确认批量添加配件事件
	$("body").on("click","#batch_add_goods .dialog_confirm",function(){
		var chosenGoods = $(".goods_data_item","#batch_chosen_result");
		if(chosenGoods.size() < 1){
			seajs.use(["dialog"],function(dialog){
				dialog.info("您还没有选择任何配件",3,2);
			});
			return;
		}else{
			//循环添加到动态行
			var id = $(this).parents(".qxy_dialog").attr("id");
			var group = $("#"+id).data("group");
			var lastRow = $(".group_content_inner .form_row:last .form_item:last",group);
			seajs.use("dialog",function(dialog){
				dialog.closeDialog("#"+id);
			});
			$("input",lastRow).each(function(){
				if(!$(this).hasClass('qxy_input_unit') && $(this).hasClass('qxy_input')){
					if($(this).val()!=""){
						$(".qxy_add_icon",lastRow).trigger("click");
						return false;
					}
				}
			});
			chosenGoods.each(function(){
				
				var item = JSON.parse($(".item_json",$(this)).val());
				lastRow = $(".group_content_inner .form_row:last .form_item:last",group);
				//工单字段
				$("input[name='goodsFormat']",lastRow).val(item.format);
				$("input[name='goodsName']",lastRow).val(item.name);
				$("input[name='goodsId']",lastRow).val(item.id);
				$("input[name='goodsSn']",lastRow).val(item.goodsSn);
				$("input[name='inventoryPrice']",lastRow).val(item.inventoryPrice);
				$("input[name='goodsPrice']",lastRow).val(item.price);
				$("input[name='goodsNumber']",lastRow).val(1);
				$("input[name='measureUnit']",lastRow).val(item.measureUnit);
				$("input[name='goodsAmount']",lastRow).val((item.price).toFixed(2));
				$("input[name='discount']",lastRow).val(0);
				$("input[name='stock']",lastRow).val(item.stock);
				$("input[name='measureUnit']",lastRow).val(item.measureUnit);

				//入库字段
				$("input[name='goodsCount']",lastRow).val(1);
				$("input[name='unit']",lastRow).val(item.measureUnit);
				$("input[name='purchasePrice']",lastRow).val("0.00");
				$("input[name='purchaseAmount']",lastRow).val("0");
				$("input[name='stockCount']",lastRow).val(item.stock);

				$("input[name='id']",lastRow).val("");
				
				$(".qxy_add_icon",lastRow).trigger("click");
			});
			
			var url = window.location.href;
			if(/order/.test(url)){
				//工单页面调用工单的计算js
				order.calculatePrice();
			}
			
			chosenGoods.remove();
			
		}
	});
	//订单状态
	var orderStatus =$('input[name="orderInfo.orderStatus"]').val();
	if(orderStatus == "DDWC"){
		$(".batch_add_goods").hide();
	}
});
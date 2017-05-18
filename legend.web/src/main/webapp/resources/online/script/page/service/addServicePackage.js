//javascript code
seajs.use("downlist");

//选择服务套餐后的回调方法
function serviceListCallFn(obj,item){
	var scope = obj.parents(".form_item");
	$("input[name='serviceSn']",scope).val(item.serviceSn);
	$("input[name='name']",scope).val(item.name);
	$("input[name='serviceCatName']",scope).val(item.serviceCatName);
	$("input[name='servicePrice']",scope).val(item.servicePrice);
	$("input[name='serviceHour']",scope).val(item.serviceHour);
	$("input[name='serviceAmount']",scope).val(item.serviceAmount);	
	
	
	seajs.use(["ajax","artTemplate"],function(ajax,tt){
		//配件资料详情的模板
	    var template = [
	    '<div class="part-info">',
	        '<input class="img_url" type="hidden" value="<%= data.imgUrl %>" />',
	        '<img width="61" src="<%= data.imgUrl %>" alt="" onerror="http://tqmall-image.oss-cn-hangzhou.aliyuncs.com/images/no_picture.gif">',
	        '<div class="text-container">',
	            '<p class="item-name"><%= data.name %></p>',
	            '<p class="item-detail">',
	                '零件号：<%= data.format %>',
	                '购买单位：<b class="measure_unit_span"><%= data.measureUnit %></b><br>',
	                '配件编码：<%= data.goodsSn %><br>',
	            '</p>',
	        '</div>',
	    '</div>'].join("");
	    
	    //根据服务id请求配件资料后的回调方法
		var successFn = function(json){
			var inputsHtml = $(".form_item","#suiteGroup")[0].outerHTML;
			//配件套餐中从左到右每列input的Name
			var partName = ["goodsSn","name","format","price","goodsNum","goodsAmount"];
			//填充input的包裹元素
			$(".group_content_inner","#suiteGroup").append(inputsHtml);
			//循环填充配件资料
			for(var i=0;i<json.data.goodsList.length;i++){
				var item = json.data.goodsList[i];
				var contactServiceId = util.ccid(8);
				var dynamicId = util.ccid(16);
				//formItem添加随机id
				$(".form_item:last","#suiteGroup").attr("id",contactServiceId).attr("dynamic_id",dynamicId);
				//根据包裹元素填充input,并赋name和赋值
				var fieldElements = $(".field_box","#"+contactServiceId);
				fieldElements.each(function(i){ 
				    if((i+1) == fieldElements.size()){
				    	//最的一列，添加删除按钮
				    	$(this).html('<a href="javascript:;" class="qxy_del_icon"></a>');
				    	return true;
				    }
					$(this).html('<input name="'+partName[i]+'" class="qxy_input" '+(partName[i]=="goodsNum"?'':'readonly="true"')+' value="'+item[partName[i]]+'"/>');
				});
				var temp = tt.compile(template);
				var html = temp({data:json.data.goodsList[0]});
				$("#"+contactServiceId).append(html);
			}
		}
		//根据服务id请求配件资料
		ajax.get({
			url : BASE_PATH + "/shop/shop_service_info/getPackageByServiceId",
			data : {serviceId:item.id},
			success : successFn
		});
	});
}
//javascript code
seajs.use("downlist");
//配件列表选择后的回调
function goodsListCallFn(obj,item){
	var scope;
	if(obj.hasClass("form_item")){
		scope = obj;
	}else{
		scope = obj.parents(".form_item");
	}
	
	$("input[name='goodsSn']",scope).val(item.goodsSn);
	$("input[name='name']",scope).val(item.name);
	$("input[name='format']",scope).val(item.format);
	$("input[name='price']",scope).val(item.price);
	$("input[name='goodsSoldPrice']",scope).val(item.price);
	$("input[name='goodsNum']",scope).val(item.goodsNum);
	$("input[name='goodsAmount']",scope).val(function(){
		return $("input[name='price']",scope).val()*$("input[name='goodsNum']",scope).val();
	});

	//所有配件总金额
	var goodsAmountListCount = $("input[name='suite_goods_price_amount']");
	//服务费用
	var servicePrice = $("input[name='shopServiceInfo.servicePrice']");
	//服务定价
	var serviceCount = $("input[name='serviceGoodsSuite.suitePrice']");

	//配件总金额数组
	var goodsAmountList = $("input[name='goodsAmount']");
	goodsAmountListCount.val(function(){
		var temp = 0;
		goodsAmountList.each(function(){
			return temp += $(this).v();
		});
		return temp;
	});
	serviceCount.val(goodsAmountListCount.v() + servicePrice.v());

	//配件详情的显示
    var template = ['<div class="part-info">',
        '<input class="img_url"  name="imgUrl" type="hidden" value="<%= data.imgUrl %>" />',
        '<img width="61" <% if(data.imgUrl=="" || data.imgUrl==null){%>src="http://tqmall-image.oss-cn-hangzhou.aliyuncs.com/images/no_picture.gif"<%}else{%> src="<%= data.imgUrl %>"<%}%> alt="" onerror="http://tqmall-image.oss-cn-hangzhou.aliyuncs.com/images/no_picture.gif">',
        '<div class="text-container">',
            '<p class="item-name"><%= data.name %></p>',
            '<p class="item-detail">',
                '零件号：<%= data.format %>',
                '<input name="measureUnit" type="hidden" value="<%= data.measureUnit %>" />',
                '购买单位：<b class="measure_unit_span"><%= data.measureUnit %></b><br>',
                '配件编码：<%= data.goodsSn %><br>',
            '</p>',
        '</div>',
    '</div>'].join("");
    
	seajs.use("artTemplate",function(tt){
		var temp = tt.compile(template);
		var html = temp({data:item});
		scope.find(".part-info").remove();
		scope.append(html);
	});
}
//删除动态行时的回调方法
function delDynamicFn(scope){
	//重新计算,这里的scope是被删除的动态行的下一个元素。
	calc($("input[name='goodsNum']",scope));
}

//配件数量和服务价格在改变时从新计算总价
function calc(obj){
	var obj = $(obj);
	var name = obj.attr("name");
	switch(name){
		case "goodsNum":
			//配件数量框onkeyup计算
			var scope = obj.parents(".form_item");
			var priceInput = $("input[name='price']",scope);
			//配件每一条总金额
			var goodsAmountInput = $("input[name='goodsAmount']",scope);
			//配件总金额数组
			var goodsAmountList = $("input[name='goodsAmount']");
			//所有配件总金额
			var goodsAmountListCount = $("input[name='suite_goods_price_amount']");
			//服务费用
			var servicePrice = $("input[name='shopServiceInfo.servicePrice']");
			//服务定价
			var serviceCount = $("input[name='serviceGoodsSuite.suitePrice']");

			//每一条配件数量
			var num = obj.v();
			//每一条配件的单价
			var price = priceInput.v();

			//商品每一条总价计算
			goodsAmountInput.val(num*price);

			var goodsListCount = (function(){
				var temp = 0;
				goodsAmountList.each(function(){
					return temp += $(this).v();
				});
				return temp;
			})();
			goodsAmountListCount.val(goodsListCount);
			serviceCount.val(goodsListCount + servicePrice.v());
			break;
		case "shopServiceInfo.servicePrice":
			//服务费用输入框onkeyup计算
			//服务费用
			var servicePrice = $("input[name='shopServiceInfo.servicePrice']");
			//服务定价
			var serviceCount = $("input[name='serviceGoodsSuite.suitePrice']");
			//所有配件总金额
			var goodsAmountListCount = $("input[name='suite_goods_price_amount']");
			servicePrice.val(obj.v());
			serviceCount.val(obj.v() + goodsAmountListCount.v());
			break;
		default:
	}
}

//后端接口必须要的字段名
function suiteName(obj){
	$("input[name='serviceGoodsSuite.suiteName']").val(obj.value);
}
function serviceInfo(obj){
	var serviceInfoObj = {"servicePrice": obj.value};
    var serviceInfoStr = JSON.stringify(serviceInfoObj);
    $("input[name='serviceGoodsSuite.serviceInfo']").val(serviceInfoStr);
}

//更新操作
$(function(){
	var serviceId = util.getQueryString("id");
	if(serviceId!=null){
		seajs.use(["ajax"],function(ajax){
			var success = function(json){
				//反显配件数据
				if(json.success && json.data){
					var data = json.data;
					var formRow = $(".form_row:eq(1)","#servicePart");
					var formItem = $(".form_item:eq(1)","#servicePart");
					for(var i=0;i<json.data.length;i++){
						var item = json.data[i];
						var html = formItem[0].outerHTML;
						formRow.append(html);
						var obj = $(".form_item:last","#servicePart");
						if((i+1)<json.data.length){
							$(".qxy_add_icon",obj).removeClass("qxy_add_icon").addClass("qxy_del_icon");
						}
						goodsListCallFn(obj,item);
					}
					if(json.data.length != 0){
						formItem.remove();
					}
				}
			}
			//请求服务配件数据
			ajax.get({
				url : BASE_PATH + "/shop/shop_service_info/get_suit_goods",
				data : {id : serviceId},
				success : success
			});
			
		});
	}
});
//服务资料列表页业务逻辑

$(function(){
	seajs.use(["ajax","artTemplate"],function(ajaxModule,template){
		//渲染套餐详情模板
		var cardDetail = function(json){
			var tpl = ['<%' ,
	        'for (i = 0; i < tplData.length; i++) {',
	        'var item = tplData[i];',
	        '%>',
	        '<div class="card_item_append  clearfix">'+
	            '<div class="img_box"><img src="<%= item.imgUrl %>" alt="商品图片"/></div>',
	            '<dl>',
	                '<dd class="text1"><%= item.name %></dd>',
	                '<dd class="font12 text2">零件号： <%= item.format %></dd>',
	                '<dd class="font12 text2">配件编码： <%= item.goodsSn %></dd>',
	            '</dl>',
	        '</div>',
	        '<% } %>'].join("");
			var render = template.compile(tpl);
			var html = render({tplData : json.data});
			return html;
		}
	});
});
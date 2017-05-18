function formLayoutCalc(scope){
	scope = scope || "body";
	if($(scope).data("calced")){
		//已计算的布局有标识，将不再计算
		return;
	}else{
		$(scope).data("calced",true);
	}
	var calcWidth = function(obj, num) {
		var childs = obj.parent().children();
		var parentWidth = obj.parent().width();
		var fixedWidth = childs.eq(1).outerWidth();
		childs.eq(0).width(num ? parentWidth - fixedWidth - num : parentWidth - fixedWidth);
	}
	// 计算表单项每行的元素个数，分别自适应宽度
	// 搜索表单右边按钮固定，左边自适应两列元素
	$(".search_left",scope).each(function() {
		var sibCount = $(this).siblings().length;
		//小于1表示只有search_left,没有search_right,就不需要右间隙10px
		calcWidth($(this),sibCount<1?0:10);
	});
	// 普通表单循环每一行
	$(".form_row",scope).each(function() {
		var $this = $(this),
			formItem = $this.children(".form_item"),
			itemSize = formItem.size(),
            rowWidth = ~~$this.width(),
			//formitem间隙，单位是px
			itemSpcWidth = 10,
			itemWidth = 0,
			//formitem中的固定宽度的总
			itemFixedWidthCount = 0,
			//formitem之间空隙的总和
			itemSpcWidthCount = (itemSize-1) * itemSpcWidth,
			//动态元素的数量
			dynamicSize = $(".form_item[dynamic_name]",$this).size(),
			//动态元素在一行占几分之几
			dynamicColSize = itemSize - dynamicSize,
			//动态元素的宽度
			dynamicWidth = 0;


		// 计算form_row里的每一行元素 10代表外右边距
		formItem.each(function(i){
			var fixed = $(this).attr("fixed");
			if(fixed){
				itemSize -= 1;
				itemFixedWidthCount += $(this).width();
			}
		});
		itemWidth = (rowWidth - itemSpcWidthCount - itemFixedWidthCount)/ itemSize;

		//动态行占的不是整行，有可能是1/2,1/3,1/4,... rowWidth / ((itemSize - dynamicSize) + 1)

		if(dynamicColSize >= 1){
			dynamicWidth = (rowWidth - itemSpcWidthCount - itemFixedWidthCount)/(dynamicColSize+1);
		}else{
			//整行动态行
			dynamicWidth = rowWidth ;
		}

		formItem.each(function(i){
			var dynamicName = $(this).attr("dynamic_name");
			var fixed = $(this).attr("fixed");
			//formItem有固定定元素将不做计算
			if(!fixed){
				$(this).width(~~itemWidth);
			}
			if(dynamicName){
				$(this).width(~~dynamicWidth);
				var result = (i+1) % itemSize;
				if(result == 0 || isNaN(result)){
					$(this).css({"margin-right":0});
				}else{
					$(this).css({"margin-right":itemSpcWidth});
				}
			}else{
				if((i+1) == formItem.size()){
					$(this).css({"margin-right":0});
				}else{
					$(this).css({"margin-right":itemSpcWidth});
				}
			}
		});
		// 循环每一个form_item,计算左边固定，普通右边按元素个数平均，有固定元素，其它元素自适应
		formItem.each(function(){
			var labelBox = $(this).find(".label_box"),
				//form_item的宽度
				itemWidth = $(this).width(),
				//input前面label的宽度
				labelWidth = labelBox.width() || 0,
				//label的数量
				labelSize = labelBox.size() || 0,

				fieldBox = $(this).find(".field_box"),
				//field_box中表单元素的数量
				fieldSize = fieldBox.size(),
				fieldWidth = null,
				//元素是否是固定宽度元素
				hasFixed = false,
				//两个相邻input之间的间隙,单位是px
				spacWidth = 5,
				//field_box中所有元素间隙总和
				spacWidthCount = (fieldSize-1)*spacWidth,
				fixedWidthCount = 0;

			//循环给field_box中每个表单元素赋值宽度
			fieldBox.each(function(i){
				if(fieldBox.eq(i).attr("fixed")){
					//有固定元素,被平均的元素-1
					hasFixed = true;
					//固定元素总宽累加
					fixedWidthCount += fieldBox.eq(i).width();
					fieldSize -= 1;
				}
				//从第二个元素开始margin-left:10px;
				fieldBox.eq(i+1).css({"margin-left":spacWidth});
			});

			fieldWidth = ((itemWidth - labelSize*labelWidth )- fixedWidthCount - spacWidthCount)/fieldSize;

			fieldBox.each(function(i){
				if(fieldBox.eq(i).attr("fixed")){
					return true;
				}
				fieldBox.eq(i).width(~~fieldWidth);
			});
		});
	});
	$(".form_row",scope).css({opacity:1});
}

//计算表单布局
$(function(){
	formLayoutCalc();
});

function formLayoutCalc(scope){
	scope = scope || "body";
	if($(scope).data("calced")){
		//已计算的布局有标识，将不再计算
		return;
	}else{
		$(scope).data("calced",true);
	}
	var calcSearchFormWidth = function(obj, num) {
		obj = $(obj);
		var childs = obj.parent().children();
		var parentWidth = obj.parent().width();
		var fixedWidth = childs.eq(1).outerWidth();
		childs.eq(0).width(num ? parentWidth - fixedWidth - num : parentWidth - fixedWidth);
	}
	// 获取兄弟节点
	// 计算表单项每行的元素个数，分别自适应宽度
	// 搜索表单右边按钮固定，左边自适应两列元素
	var siblings = function(obj){
		var childs = obj.parentNode.children;
		var arr = [];
		for(var i=0;i<childs.length;i++){
			if(childs[i] != obj){
				arr.push(childs[i]);
			}
		}
		return arr;
	}
	//循环跌代搜索表单的元素，并根据父元素计算未设置宽度的元素。
	var searchLeftArr = document.getElementsByClassName("search_left");
	for(var i=0;i<searchLeftArr.length;i++){
		var obj = searchLeftArr[i];
		calcSearchFormWidth(obj,siblings(obj).length<1?0:10);
	}
	// 普通表单循环每一行
	$(".form_row",scope).each(function(){
		var $this = $(this),
			formItem = $(".form_item",$this),
			formItemFixed = $(".form_item[fixed]",$this),
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
		formItemFixed.each(function(i){
			itemSize -= 1;
			itemFixedWidthCount += $(this).width();
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
			var $$this = $(this);
			var dynamicName = $$this.attr("dynamic_name");
			var fixed = $$this.attr("fixed");
			//formItem有固定定元素将不做计算
			if(!fixed){
				$$this.width(~~itemWidth);
			}
			if(dynamicName){
				$$this.width(~~dynamicWidth);
				var result = (i+1) % itemSize;
				if(result == 0 || isNaN(result)){
					$$this.css({"margin-right":0});
				}else{
					$$this.css({"margin-right":itemSpcWidth});
				}
			}else{
				if((i+1) == itemSize){
					$$this.css({"margin-right":0});
				}else{
					$$this.css({"margin-right":itemSpcWidth});
				}
			}
		});
		// 循环每一个form_item,计算左边固定，普通右边按元素个数平均，有固定元素，其它元素自适应
		formItem.each(function(i){
			var __currentFormItem = formItem.eq(i),
				__prevFormItem = formItem.eq(i-1),
				__currentFormItemName = __currentFormItem.attr("dynamic_name"),
				__prevFormItemName = __prevFormItem.attr("dynamic_name"),
				__prevCalcedWidth = __prevFormItem && __prevFormItem.data("calcedWidth"),
				__fieldWidth = 0,
				//两个相邻input之间的间隙,单位是px
				__spacWidth = 5;
			if(__prevCalcedWidth && __currentFormItemName && (__currentFormItemName == __prevFormItemName)){
				__fieldWidth = __prevCalcedWidth;
			}else{
				var labelBox = $(".label_box",__currentFormItem),
					//form_item的宽度
					itemWidth = __currentFormItem.width(),
					//input前面label的宽度
					labelWidth = labelBox.width() || 0,
					//label的数量
					labelSize = labelBox.size(),

					fieldBox = $(".field_box",__currentFormItem),
					//field_box中表单元素的数量
					fieldSize = fieldBox.size(),
					//元素是否是固定宽度元素
					hasFixed = false,
					//field_box中所有元素间隙总和
					spacWidthCount = (fieldSize-1)*__spacWidth,
					fixedWidthCount = 0;

				//循环给field_box中每个表单元素赋值宽度
				$(".field_box[fixed]",__currentFormItem).each(function(){
					fixedWidthCount += $(this).width();
					fieldSize -= 1;
				});

				__fieldWidth = ((itemWidth - labelSize*labelWidth )- fixedWidthCount - spacWidthCount)/fieldSize;
			}
			
			$(".field_box:not([fixed])",__currentFormItem).width(~~__fieldWidth);
			$(".field_box:gt(0)",__currentFormItem).css({"margin-left":__spacWidth});
			__currentFormItem.data("calcedWidth",~~__fieldWidth);
		});
	});
	$('.form_row').css({opacity:1});
	//动态行序列号初始化
	$(".form_item[dynamic_name]").parent().each(function(i){
		var _parent = $(this);
		if(_parent.parent().attr("dynamic_group")){
			return true;
		}
		_parent.find(".form_item[dynamic_name]").each(function(j){
			var _child = $(this);
			_child.find("[num]").text(j+1);
		});
		_parent.parent().attr("dynamic_group","true");
	});;
}

//计算表单布局
$(function(){
	formLayoutCalc();
});
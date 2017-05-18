//重构后分页移植到重构前的页面中使用
//使用方法
/*
	$.paging({
		dom : $(".qxy_page"),
		itemSize : 0,
		pageCount : 0,
		current : 0,
		backFn : function(p){

		}
	});
*/
;(function($){
	var ms = {
		init:function(args){
			ms.fillHtml(args);
			ms.bindEvent(args);
		},
		//填充html
		fillHtml:function(args){
			var obj = $(".qxy_page_inner:eq(0)",args.dom);
			var obj_right = $(".opt_right").eq(0);   //页面右上角仅仅存在上一页、下一页
			obj.empty();
			
			//上一页之前
			obj.append('<span class="disabled qxy_page_count">共'+args.itemSize+'条记录</span>');
			if(args.current < 10000){
				if(args.current > 1 && args.current <= args.pageCount){
					obj.append('<a class="qxy_page_first" href="javascript:;">首页</a>');
					obj.append('<a href="javascript:;" class="qxy_page_prev">上一页</a>');
					obj_right.find("span:not(.disabled)").prev().remove();
					obj_right.find("span:not(.disabled)").before('<a href="javascript:;" class="qxy_link qxy_page_prev">上一页</a>');
				}else{
					obj.remove('.prevPage');
					obj.append('<span class="disabled">首页</span>');
					obj.append('<span class="disabled">上一页</span>');
					obj_right.find("span:not(.disabled)").prev().remove();
					obj_right.find("span:not(.disabled)").before('<span class="disabled">上一页</span>');
				}
			}
			//中间页码
			if(args.current != 1 && args.current >= 4 && args.pageCount != 4){
				obj.append('<a href="javascript:;" class="qxy_page_num">'+1+'</a>');
			}
			if(args.current-2 > 2 && args.current <= args.pageCount && args.pageCount > 5){
				obj.append('<span>...</span>');
			}
			var start = args.current -2,end = args.current+2;
			if((start > 1 && args.current < 4)||args.current == 1){
				end++;
			}
			if(args.current > args.pageCount-4 && args.current >= args.pageCount){
				start--;
			}
			for (;start <= end; start++) {
				if(start <= args.pageCount && start >= 1){
					if(start != args.current){
						obj.append('<a href="javascript:;" class="qxy_page_num">'+ start +'</a>');
					}else{
						obj.append('<span class="current">'+ start +'</span>');
					}
				}
			}
			if(args.current + 2 < args.pageCount - 1 && args.current >= 1 && args.pageCount > 5){
				obj.append('<span>...</span>');
			}
			if(args.current != args.pageCount && args.current < args.pageCount -2  && args.pageCount != 4){
				obj.append('<a href="javascript:;" class="qxy_page_num">'+args.pageCount+'</a>');
			}
			if(args.current<10000){
				//下一页之后
				if(args.current < args.pageCount){
					obj.append('<a href="javascript:;" class="qxy_page_next">下一页</a>');
					obj.append('<a class="qxy_page_last" href="javascript:;">末页</a>');
					obj_right.find("span:not(.disabled)").next().remove();
					obj_right.find("span:not(.disabled)").after('<a class="qxy_link qxy_page_next" href="javascript:;">下一页</a>');
				}else{
					obj.remove('.nextPage');
					obj.append('<span class="disabled">下一页</span>');
					obj.append('<span class="disabled qxy_page_last">末页</span>');
					obj_right.find("span:not(.disabled)").next().remove();
					obj_right.find("span:not(.disabled)").after('<span class="disabled">下一页</span>');
				}
			}
			if(args.pageCount <=1 ){
				obj.append('<span class="qxy_go_num disabled">共'+args.pageCount+'页,去第 <input type="text" disabled="disabled" value=""> 页</span>');
				obj.append('<span class="qxy_go_btn disabled" href="javascript:;">跳转</span>');
			}else{
				obj.append('<span class="qxy_go_num">共'+args.pageCount+'页,去第 <input type="text" value=""> 页</span>');
				obj.append('<a class="qxy_go_btn" href="javascript:;">跳转</a>');
			}
		},
		//绑定事件
		bindEvent:function(args){
			//页码事件绑定
			args.dom.off("click","a.qxy_page_num").on("click","a.qxy_page_num",function(){
				var current = parseInt($(this).text());
				ms.fillHtml({"current":current,"pageCount":args.pageCount,dom:args.dom,itemSize:args.itemSize});
				if(typeof(args.backFn)== "function"){
					args.backFn(current);
				}
			});
			//上一页
			args.dom.off("click","a.qxy_page_prev").on("click","a.qxy_page_prev",function(){
				var current = parseInt($("span.current",args.dom).text());
				if(current == 1) return;
				ms.fillHtml({"current":current-1,"pageCount":args.pageCount,dom:args.dom,itemSize:args.itemSize});
				if(typeof(args.backFn)=="function"){
					args.backFn(current-1);
				}
			});
			//下一页
			args.dom.off("click","a.qxy_page_next").on("click","a.qxy_page_next",function(){
				var current = parseInt($("span.current",args.dom).text());
				if(current == args.pageCount) return;
				ms.fillHtml({"current":current+1,"pageCount":args.pageCount,dom:args.dom,itemSize:args.itemSize});
				if(typeof(args.backFn)=="function"){
					args.backFn(current+1);
				}
			});
			//首页事件
			args.dom.off("click","a.qxy_page_first").on("click","a.qxy_page_first",function(){
				ms.fillHtml({"current":1,"pageCount":args.pageCount,dom:args.dom,itemSize:args.itemSize});
				if(typeof(args.backFn)=="function"){
					args.backFn(1);
				}
			});
			//末页事件
			args.dom.off("click","a.qxy_page_last").on("click","a.qxy_page_last",function(){
				ms.fillHtml({"current":args.pageCount,"pageCount":args.pageCount,dom:args.dom,itemSize:args.itemSize});
				if(typeof(args.backFn)=="function"){
					args.backFn(args.pageCount);
				}
			});
			//跳转事件
			args.dom.off("click","a.qxy_go_btn").on("click","a.qxy_go_btn",function(){
				var current = parseInt(args.dom.find("span.current").text());
				var goNumInput = $(this).prev().find("input");
				var goNum = parseInt(goNumInput.val());
				if(goNum > args.pageCount || !$.isNumeric(goNum) || goNum == current){
					goNumInput.val("").focus();
					return;	
				}
				ms.fillHtml({"current":args.pageCount,"pageCount":args.pageCount,dom:args.dom,itemSize:args.itemSize});
				if(typeof(args.backFn)=="function"){
					args.backFn(goNum || current);
				}
			});
		}
	}
	$.paging = function(options){
		var args = $.extend({
			dom : $(".qxy_page"),
			itemSize : 0,
			pageCount : 0,
			current : 0,
			backFn : function(){}
		},options);
		args.width = args.dom.width();
		ms.init(args);
	}
})(jQuery)
/**
 * create by wjc 2015.05.27
 */
$(function(){
	$("body").on("click",".order_list_toolbars a",function(){
		$(this).addClass("current").siblings().removeClass("current");
		$("#status").attr("value",$(this).attr("id"));
		seajs.use("table",function(table){
			table.fill("order_list_table");
		});
	});
});
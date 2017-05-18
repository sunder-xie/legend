seajs.use(["tab","downlist"],function(tab){
	tab.init();
});
$(function(){
	/** 切换tab
	 *  wjc 2015.6.5
	 */
	$(".rows_p li").click(function () {   
		var tabId = $.trim($(this).find("a").attr("tagurl").substr(1));
		if(tabId == "allMember") {
			$("#startAmount").attr("value","");
			$("#endAmount").attr("value","");
		}
		if(tabId == "normalMember") {
			$("#startAmount").attr("value","");
			$("#endAmount").attr("value",1000);
		}
		if(tabId == "silverMember") {
			$("#startAmount").attr("value",1000);
			$("#endAmount").attr("value",5000);
		}
		if(tabId == "goldMember") {
			$("#startAmount").attr("value",5000);
			$("#endAmount").attr("value","");
		}
		$(".search_btn").trigger('click');
	});
	
	//弹窗
	$(".addNewMember").click(function () {
		$("input[name='customerName']").val("");
		seajs.use(["dialog"],function(dg){
			dialog = dg.dialog({
				"dom":"#addNew",
			});
		});
	});
	
	//更改customerName
	$("#customerNameInput").change(function(){
		$("input[name='customerName']").val($("#customerNameInput").val());
	});
});

function payDlg($this){
	$("#cashfillForm").find("input").val('');
	$1dynamic = $("#cashfillForm").find("select:eq(0)").parents('.form_item').find('.field_box a');
	$1dynamic.removeClass('qxy_del_icon');
	$1dynamic.addClass('qxy_add_icon');
	$("#cashfillForm").find("select:gt(0)").each(function(i,e){
		$(e).parents('.form_item').remove();
	})
	$("#cashfillForm").find("select:eq(0)").find("option:eq(0)").attr("selected",true);

	var id = $($this).attr('rel_id');
	var amount = $($this).attr('rel_amount');
	$("#memberId").val(id);
	$("#amount").html(amount+"元");
	seajs.use(["dialog"],function(dg){
		dialog = dg.dialog({
			"dom":"#payDlg"
		});
	});
}
function cashfill(){
	util.submit({
		formid:'cashfillForm',
		callback:function(json,dialog){
			if(json.success){
				dialog.info("操作成功",1);
				$("#payDlg").find("input").val('');
				seajs.use('table',function(table){
					table.fill("chain_member_table");
				});
				layer.closeAll();
			}else{
				dialog.info(json.errorMsg,3);
			}
		}
	})
};
function closeDlg(){
	$("#payDlg").find("input").val('');
	layer.closeAll();
}
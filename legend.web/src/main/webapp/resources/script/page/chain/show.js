$(document).ready(function() {
	$(".coupon").on('click', function() {
		$select = '#' + $(this).attr('rel_id');
		$('.coupon').removeClass('current');
		$(".coupon_content").hide();
		$(this).addClass('current');
		$($select).show();
	})
	$("#info_group").find(".qxy_tab li").click(function(){
		$("#info_group").find(".qxy_tab li a").removeClass("current");
		var $a_tab =  $(this).find("a");
		$a_tab.addClass('current');
		var tabTitle = $a_tab.text().trim();
		if("交易记录"==tabTitle){
			$("#log_form").show();
			$("#log_table").show();
			
			$("#order_form").hide();
			$("#order_table").hide();
		} else if("工单信息" == tabTitle) {
			$("#log_form").hide();
			$("#log_table").hide();
			
			$("#order_form").show();
			$("#order_table").show();
		}
	})
	$("#info_group").find(".qxy_tab li:eq(0)").trigger('click');
	
	$("#chexing").click(function(){
		util.carTypeSelect(function(json){
			$("#chexing").val(json.carName);
		});
	})
})
function freshDataFill(json) {
	var cnt = 0;
	$.each(json.data, function(i, e) {
		cnt += e.usedCount;
	})
	$('a[rel_id="fresh"]').append("(" + cnt + ")");
}
function usedDataFill(json) {
	var cnt = 0;
	$.each(json.data, function(i, e) {
		cnt += e.usedCount;
	})
	$('a[rel_id="used"]').append("(" + cnt + ")");
}
function expiredDataFill(json) {
	var cnt = 0;
	$.each(json.data, function(i, e) {
		cnt += e.usedCount;
	})
	$('a[rel_id="expired"]').append("(" + cnt + ")");
}
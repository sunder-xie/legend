$(function() {

	var $document = $(document);

	var $infoGroup = $("#info_group");
	$(".coupon").on('click', function() {
		var $select = '#' + $(this).attr('rel_id');
		$('.coupon').removeClass('current');
		$(".coupon_content").hide();
		$(this).addClass('current');
		$($select).show();
	});
	$infoGroup.find(".qxy_tab li").click(function(){
		$infoGroup.find(".qxy_tab li a").removeClass("current");
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
	});
	$infoGroup.find(".qxy_tab li:eq(0)").trigger('click');

	/*
	 * modify by sky 20160330
	 * vin码验证
	 */
	$document.on('click', '.save', function() {

		var vin = $('input[name="customerCar.vin"]', 'body').val();
		if (vin && vin.length !== 17) {
			seajs.use(['dialog'], function(dg) {
				dg.info('VIN码必须为17位', 3);
			});
			return;
		}

		util.submit({
			formid: 'add_chain_member'
		});
	});
	/* modify by sky 20160330 */

	/*
	* modify by sky 20160409
	* 添加撤销\打印功能
	*/
	$document
		.on('click', '.js-revoke', function() {
			var id = $(this).data('id');
			seajs.use(['ajax', 'dialog'], function(ajax, dg) {
				dg.confirm('是否确定要撤销该条记录?', function() {
					ajax.get({
						url: BASE_PATH + '/member/revertCharge',
						data: {
							id: id
						},
						success: function(result) {
							if(result.success) {
                                dg.info(result.data, 1);
							} else {
								dg.info(result.errorMsg, 3);
							}
						}
					});
				});
			});
		})
		.on('click', '.js-print', function() {
			var id = $(this).data('id');
			util.print(BASE_PATH + '/member/printCharge?nodec&id=' + id);
		});
	/* modify by sky 20160409 */

	$document.on('click', '.js-back', function() {
		window.location.href = BASE_PATH + '/member';
	});

});

function freshDataFill(json) {
	var cnt = 0;
	$.each(json.data, function(i, e) {
		cnt += e.usedCount;
	});
	$('a[rel_id="fresh"]').append("(" + cnt + ")");
}
function usedDataFill(json) {
	var cnt = 0;
	$.each(json.data, function(i, e) {
		cnt += e.usedCount;
	});
	$('a[rel_id="used"]').append("(" + cnt + ")");
}
function expiredDataFill(json) {
	var cnt = 0;
	$.each(json.data, function(i, e) {
		cnt += e.usedCount;
	});
	$('a[rel_id="expired"]').append("(" + cnt + ")");
}
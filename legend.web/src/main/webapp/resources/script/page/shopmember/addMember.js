var member = {
	fillNewMember: function(obj, item) {
		$('input[name="license"]').val(item.license);
		fillVin(obj, item);
	}
};

//回调更改customerName
function fillCustomerName(obj,item){
	$("input[name='customerName']").val(item.customerName);
    $("input[name='birthday']").val(item.birthday);
    $("input[name='mobile']").val(item.mobile);
}
//车牌号回调
function fillVin(obj,item){
	$("input[name='vin']").val(item.vin);
	seajs.use(["ajax"],function(ajax){
		ajax.post({
            url:BASE_PATH + '/shop/customer/get_by_license',
            data:{license:item.license},
            success:function(result,dialog){
            	$("input[name='customerName']").val(result.data.customerName);
            	if(result.data.birthday != null && result.data.birthday != "null"){
            		$("input[name='birthday']").val(util.formatDate(result.data.birthday,'-'));
            	}
                $("input[name='mobile']").val(result.data.mobile);
            }
        });
	});
}

//添加新会员
function fillAddMember(){
	/*
	 * modify by sky 20160330
	 * vin码验证
	 */
	var vin = $('input[name=vin]', 'body').val();
	if (vin && vin.length !== 17) {
		seajs.use(['dialog'], function(dg) {
			dg.info('VIN码必须为17位', 3);
		});
		return;
	}
	/* modify by sky 20160330 */

	util.submit({
		formid:'addNewForm',
		callback:function(json,dialog){
			if(json.success){
				dialog.info("添加成功",1,1,function(){
                    history.go(0);
                },true);
				dialog.closeDialog("#addNew");
			}else{
				dialog.info(json.errorMsg,3);
			}
		}
	})
}

function closeAddMemberDlg(){
	layer.closeAll();
}
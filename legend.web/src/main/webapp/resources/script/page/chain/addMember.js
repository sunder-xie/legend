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
            url:BASE_PATH + '/shop/chain/member/getMember',
            data:{customerId:item.customerId},
            success:function(result,dialog){
            	$("input[name='customerName']").val(result.data.customerName);
                $("input[name='birthday']").val(util.formatDate(result.data.birthday,'-'));
                $("input[name='mobile']").val(result.data.mobile);
            }
        });
	});
}

//添加新会员
function fillAddMember(){
	util.submit({
		formid:'addNewForm',
		callback:function(json,dialog){
			if(json.success){
				dialog.info("添加成功",1);
				seajs.use('table',function(table){
					table.fill("chain_member_table");
				});
				layer.closeAll();
			}else{
				dialog.info(json.errorMsg,3);
			}
		}
	})
}

function closeAddMemberDlg(){
	layer.closeAll();
}
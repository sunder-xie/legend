//javascript code
seajs.use("downlist");

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
/**
 * add by moke 2015.05.18
 */
function changeMember(){
	var customer_id = $("#customerSel").val();
	if(customer_id){
		seajs.use("ajax",function(ajax){
			ajax.post({
				url:BASE_PATH+'/shop/chain/member/getCustomer',
				data:{id:customer_id},
				success:function(json){
					if(json){
						console.log(json);
						$("input[name='mobile']").val(json.mobile);
						$("input[name='customerName']").val(json.customerName);
					    $("input[name='identityCard']").val(json.identityCard);
					    $("input[name='birthday']").val(json.birthday);
					    $("input[name='customerAddr']").val(json.customerAddr);
					}
				}
			});
		})
	}
}

function addMember(obj, item) {
    if (obj.attr("name") == "customerName"){
        $("input[name='mobile']").val(item.mobile);
    }
    if (obj.attr("name") == "mobile") {
        $("input[name='customerName']").val(item.customerName);
    }
    $("input[name='customerName']").val(item.customerName);
    $("input[name='identityCard']").val(item.identityCard);
    $("input[name='birthday']").val(item.birthday);
    $("input[name='customerAddr']").val(item.customerAddr);

}

function addPlate(obj, item){
	
    $("input[name='vin']").val(item.vin);
    $("input[name='engineNo']").val(item.engineNo);
    if(item.receiveLicenseTime){
    	$("input[name='receiveLicenseTime']").val(new Date(item.receiveLicenseTime).Format('yyyy-MM-dd'));
    }
    if(item.buyTime){
    	$("input[name='buyTime']").val(new Date(item.buyTime).Format('yyyy-MM-dd'));
    }
    if(item.insuranceTime){
    	$("input[name='insuranceTime']").val(new Date(item.insuranceTime).Format('yyyy-MM-dd'));
    }
    if(item.auditingTime){
    	$("input[name='auditingTime']").val(new Date(item.auditingTime).Format('yyyy-MM-dd'));
    }
    $("select[name='insuranceId']").val(item.insuranceId);
    seajs.use("chosenSelect",function(cs){
        cs.handleChoosenSelect($("select[name='insuranceId']"));
    });
    
    $("input[name='carSeries']").val(item.carSeries);
    $("input[name='byName']").val(item.byName);
    //carSeries
    //byName
}
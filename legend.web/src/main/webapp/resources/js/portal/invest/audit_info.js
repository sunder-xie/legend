var status = $("#status").val();
if (status == null || status == '') {
    taoqi.userAgreement();
}
$(".tijiao").click(function(){
    var customerId = $("#customerId").val();
    var contactsMobile = $("#contactsMobile").val(); //手机号码
    var contactsName = $("#contactsName").val();  //联系人
    var companyName = $("#companyName").val(); //客户名称名
    var province = $("#province").val();// 省
    var city = $("#city").val();//市
    var district = $("#district").val();//区
    var street = $("#street").val();//街道
    var address = $("#address").val();//具体地址
    var storeArea = $("#storeArea").val();//门店面积
    var headCount = $("#headCount").val(); //员工数
    var qq = $("#qq").val(); //QQ
    var weixin = $("#weixin").val(); //微信
    var stationCnt = $("#stationCnt").val(); //工位数
    var post = $("#post").val();//申请人职务
    var mobilephone = $("#mobilephone").val();//座机
    var email = $("#email").val();//电子邮件

    var data = {
        customerId: customerId,
        contactsMobile: contactsMobile,
        contactsName: contactsName,
        companyName: companyName,
        province: province,
        city: city,
        district: district,
        street: street,
        address: address,
        storeArea: storeArea,
        headCount: headCount,
        qq: qq,
        weixin: weixin,
        stationCnt: stationCnt,
        post: post,
        mobilephone: mobilephone,
        email: email
    };
    var loading = taoqi.loading();
    $.ajax({
        url: BASE_PATH + "/portal/invest/save_customer_info",
        type: 'POST',
        contentType: 'application/json',
        data: $.toJSON(data),
        dataType: 'json',
        success: function (result) {
            taoqi.close(loading);
            if (result.success == true) {
                taoqi.submitAudit();
            } else {
                taoqi.failure(result.errorMsg);
                return false;
            }

        }

    });
});
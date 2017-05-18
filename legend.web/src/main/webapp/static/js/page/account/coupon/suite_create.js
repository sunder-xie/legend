/**
 * Created by wjc on 16/6/8.
 * 优惠券类型
 */
$(function () {
    $(".submit").on("click",function(){
        var flag = true;
        seajs.use('check', function(ck) {
            if(!ck.check()) {
                flag =  false;
            }
        });
        var length=$('.js-move').length;
        if(length==0){
            seajs.use('dialog',function(dg){
                dg.warn("请添加优惠券！");
                flag = false;
            })
        }else{
            for(var i=0;i<length;i++) {
                var couponNum = $("input[name='couponNum']").eq(i).val();
                if (couponNum == "") {
                    seajs.use('dialog', function (dg) {
                        dg.warn("请添加优惠券数量！");
                        flag = false;
                    })
                } else if (couponNum <= 0 || parseInt(couponNum) != couponNum) {
                    seajs.use('dialog', function (dg) {
                        dg.warn("请输入大于零的整数！");
                        flag = false;
                    })
                }
            }
        }
        if(!flag) {
            return;
        }
        var data={};
        data.suiteName = $("[name='suiteName']").val();
        data.salePrice = $("[name='salePrice']").val();
        var id = $("#id").val();
        var url = BASE_PATH+'/account/coupon/suite/create/insert';
        if(id != ""){
            url = BASE_PATH+'/account/coupon/suite/create/update';
            data.id = id;
        }
        var coupons = [];
        var index = 0;
        $("[name='couponId']").each(function(){
            var coupon = {};
            coupon.id=$(this).val();
            coupon.num=$("[name='couponNum']").eq(index).val();
            coupons.push(coupon);
            index++;
        });
        data.couponInfos = coupons;
        $.ajax({
            url:url,
            dataType: 'json',
            contentType : 'application/json',
            data: JSON.stringify(data),
            type:"POST",
            success:function(result){
                seajs.use('dialog',function(dg){
                    if(result.success){
                        dg.success('提交成功');
                        window.location = BASE_PATH + "/account/setting?flag=1";
                    }else{
                        dg.fail(result.errorMsg);
                    }
                });

            }
        });
    });
});
seajs.use([
    "art",
    "select",
    "ajax",
    'dialog',
    'downlist',
    'check',
    'date',
    'formData'
], function (at, st, ax, dg, dl, ck, date, fd) {
    dg.titleInit();
    var doc = $(document);
    doc.on('click','.delete',function(){
        $(this).parents('tr').remove();
    })

    doc.on('click', '.js-submit', function () {
        $.ajax({
            url:BASE_PATH + "/account/combo/add",
            data:{

            }
        });
    })
    ck.init();
    // 添加基本服务
    getService({
        // 点击按钮的选择器
        dom: '.js-get-service',
        // 回调函数，处理选择的数据
        callback: function (json) {
            if (json.suiteNum > 0) {
                $.ajax({
                    async: false,
                    type: 'get',
                    url: BASE_PATH + "/shop/shop_service_info/getPackageByServiceId",
                    data: {
                        serviceId: json.id
                    },
                    success: function (packageJson) {
                        if (packageJson.success) {
                            var html = at("serviceTpl", {json: packageJson.data.shopServiceInfoList});
                            $('#orderServiceTB').append(html);
                        }
                    }
                });
            } else {
                var jsons = [];
                jsons[0] = json;
                var html = at("serviceTpl", {json: jsons});
                $('#orderServiceTB').append(html);
            }
        }
    });


    //返回按钮
    $(document).on('click','.js-goback',function(){
       util.goBack();
    });

})


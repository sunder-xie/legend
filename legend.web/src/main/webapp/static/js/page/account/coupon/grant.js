/**
 * Created by zz on 2016/6/8.
 */
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
    doc.on('click', '.delete', function () {
        $(this).parents('tr').remove();
    })
    $('.input-info').hide();
    doc.on('click', '.frame', function () {
        var $el = $(this),
            id = $el.data('id'),
            price = $el.data('price');

        $el.addClass('js-current').siblings('.frame').removeClass('js-current');
        if (id == null) {
            $('#orderServiceTB').show();
            $('#tableFill').hide();
            $('.js-get-service').show();
            $('.input-info').hide();
        } else {
            $('.js-get-service').hide();
            $('#orderServiceTB').hide();
            $('.input-info').show();
            $("[name='price']").val(price);
            $.ajax({
                url:BASE_PATH +'/account/coupon/suite/get',
                contentType:'application/json',
                data: {
                    id: id
                },
                success: function (result) {
                    seajs.use(['art', 'dialog'], function(tpl, dg) {
                        if (result.success) {
                            var html = tpl('tableTpl', {json: result});
                            $('#tableFill').html(html).show();
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    });
                }
            });
        }
    })

    doc.on('click', '.js-submit', function () {
        $.ajax({
            url: BASE_PATH + "/account/combo/add",
            data: {}
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

})


$(function () {
    $(document).on("click",".submit", function () {
        var flag = true;
        seajs.use('check', function(ck) {
            if(!ck.check()) {
                flag =  false;
            }
        });
        var data = {};
        var coupons = [];
        data.paymentId = $("[name='paymentId']").val();
        data.paymentName = $("[name='paymentId']  option:selected").text();
        data.payAmount = $("[name='payAmount']").val();
        data.note = $("[name='note']").val();
        data.accountId = $("[name='accountId']").val();
        data.couponSuiteId = $(".js-current").data("id");
        var index = 0;
        var current=$('.frame').eq(0).hasClass('js-current');
        if(current){
            var length=$('.js-move').length;
            if(length==0){
                seajs.use('dialog',function(dg){
                    dg.warn("请添加优惠券！");
                    flag = false;
                });
            }else{
                for(var i=0;i<length;i++){
                    var couponCount=$("input[name='couponNum']").eq(i).val();
                    if(couponCount==""){
                        seajs.use('dialog',function(dg){
                            dg.warn("请添加优惠券数量！");
                            flag = false;
                        })
                    }else if(couponCount <= 0 || parseInt(couponCount) != couponCount){
                        seajs.use('dialog',function(dg){
                            dg.warn("请输入大于零的整数！");
                            flag = false;
                        })
                    }
                }
            }
        }
        $("[name='couponId']").each(function () {
            var coupon = {};
            coupon.id = $(this).val();
            coupon.num = $("[name='couponNum']").eq(index).val();
            //var accountId = $("input[name='accountId']").val();
            coupons.push(coupon);
            index++;
        });
        data.couponVos = coupons;
        if(!flag) {
            return;
        }
        $.ajax({
            url: BASE_PATH + '/account/coupon/grant/insert',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            type: "POST",
            success: function (result) {
                seajs.use('dialog', function (dg) {
                    if (result.success) {
                        dg.close();
                        dg.confirm('提交成功！是否打印回执单', function() {
                            util.print(
                                BASE_PATH + '/account/coupon/grantPrint?id=' +  + result.data.id, {
                                    afterPrint: function () {
                                        window.location.href = BASE_PATH +"/account/detail?accountId="
                                            + data.accountId + "&&flag=0";
                                    }
                                }
                            );
                        }, function () {
                            window.location.href = BASE_PATH +"/account/detail?accountId=" + data.accountId + "&&flag=0";
                        }, ['打印', '取消']);
                    } else {
                        dg.fail(result.errorMsg);
                    }
                });

            }
        });
    });


    $(document).on('click','.js-return',function(){
        util.goBack();
    });

});



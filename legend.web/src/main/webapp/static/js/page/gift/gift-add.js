$(function () {
    seajs.use([
        'downlist',
        'select',
        'check',
        'ajax',
        'dialog',
        'formData'
    ], function (dl, st, check, ajax, dialog, formData) {
        check.init();

        //初始化车牌下拉框
        dl.init({
            url: BASE_PATH + '/shop/customer/search/mobile',
            searchKey: 'com_license',
            tplId: 'carLicenceTpl',
            showKey: 'license',
            dom: 'input[name="license"]',
            hasInput: false,
            callbackFn: function (obj, item) {
                $.ajax({
                    url: BASE_PATH + '/shop/customer_car/get_car_by_license',
                    data: {
                        carLicense: item["license"]
                    },
                    success: function (json) {
                        if (json.success) {
                            //填充customerCarId
                            $("input[name='customerCarId']").val(json.data.id);
                            $("input[name='carModel2']").val(json.data.carInfo);

                            $("input[name='customerName']").val(json.data.contact);
                            $("input[name='mobile']").val(json.data.contactMobile);

                        }
                    }
                });
            }
        });

        //  礼品发放人
        st.init({
            dom: ".js-manager",
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });

        $('.js-publish').on('click', function () {
            if(!check.check()) {
                return;
            }

            var data = formData.get('.container');

            $.ajax({
                type: 'post',
                url: BASE_PATH + '/shop/shop_service_info/add_gift_info',
                data: data
            }).done(function (json) {
                if(json.success){
                    dialog.success("礼品发放成功,发放" + json.data, function(){
                        window.location.href = BASE_PATH+"/shop/shop_service_info/giftInfo";
                    });
                }else{
                    dialog.fail(json.errorMsg);
                }
            })
        });

        $('.js-back').on('click', util.goBack)
    });
});
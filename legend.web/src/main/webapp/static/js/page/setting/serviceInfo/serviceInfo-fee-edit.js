/*
 * create by zmx 2017/1/5
 * 新增费用资料
 */

$(function(){
    var doc = $(document);
    seajs.use([
        'check',
        'formData',
        'dialog',
        'select'
    ],function(check,formData,dialog,st){
        var nameRepeat = $.trim($('.js-name').val());
        check.init();

        st.init({
            dom:'.js-car-level',
            url: BASE_PATH + '/shop/car_level/get_car_level_by_name',
            showKey: "id",
            showValue: "name",
            // 能否输入
            canInput: true,
        });

        $('.js-save').on('click', function () {
            var name = $.trim($('.js-name').val());
            if (!check.check()) {
                return;
            }

            if (nameRepeat == name) {
                //更新
                doSave();
            } else {
                //新增
                //判断名称是否重复
                $.ajax({
                    url:BASE_PATH + '/shop/shop_service_info/checkServiceName',
                    data:{
                        serviceName:name
                    },
                    success: function (result) {
                        if (result.success != true) {
                            dialog.fail(result.message);
                            return;
                        } else {
                            if (!result.data) {
                                dialog.fail('名称已存在');
                                return;
                            } else {
                                doSave();
                            }
                        }
                    }
                });
            }
        });

        function doSave(){
            var shopServiceInfo = formData.get('.content', true);
            var data = {shopServiceInfo: shopServiceInfo};
            $.ajax({
                url: BASE_PATH + '/shop/setting/serviceInfo/saveShopFee',
                data: JSON.stringify(data),
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                success: function (json) {
                    if (json && json.success) {
                        dialog.success('保存成功', function () {
                            location.href = BASE_PATH + '/shop/setting/serviceInfo/serviceInfo-list';
                        });
                    } else {
                        dialog.fail(json.message || '保存失败');
                    }
                },
                error: function () {
                    dialog.fail('保存失败');
                }
            });
        }


        $('.js-back').on('click', function () {
            util.goBack()
        });
    });
});
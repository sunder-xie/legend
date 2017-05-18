/**
 * Created by zz on 2016/6/7.
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
    doc.on('click','.delete',function(){
        $(this).parents('tr').remove();
    })

    doc.on('click', '.js-submit', function () {
        //TODO 这里先做一下前端校验


        /**
         * 组装后台数据
         * @type {{}}
         */
        //组装基础信息
        var data = {
            comboName:$(".basic-info input[name=comboName]").val(),
            effectivePeriodDays:$(".basic-info input[name=effectivePeriodDays]").val(),
            salePrice:$("input[name=salePrice]").val()
        };
        var serviceList = [];
        //组装关联服务
        $("#orderServiceTB tr").each(function(idx, dd){
           var d = $(dd);
           var service = {serviceId: d.data("id")};
           service.serviceName = d.find("input[name=serviceName]").val();
           service.serviceCount = d.find("input[name=serviceCount]").val();
           serviceList.push(service);
        });

        data.content = serviceList;

        $.ajax({
            url:BASE_PATH+'/account/combo/create',
            dataType: 'json',
            contentType : 'application/json',
            data: JSON.stringify(data),
            type:"POST",
            success:function(result){
                seajs.use('dialog',function(dg){
                    if(result.success){
                        dg.success('提交成功');
                        window.location = BASE_PATH + "/account/setting"
                    }else{
                        dg.fail(result.message);
                    }
                });

            }
        });
    })

    doc.on('click', '.reset', function () {
        $('input[type="text"]').val("");
        $('.js-move').remove();
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
                var jsons = new Array();
                jsons[0] = json;
                var html = at("serviceTpl", {json: jsons});
                $('#orderServiceTB').append(html);
            }
        }
    });

})

var gift = gift || {};

(function ($, S) {

    //点击发放礼品buttong
    $(".qxy_row").on("click",".addGift",function(){
        var url = BASE_PATH+'/shop/shop_service_info/addGift';
        window.location.href = url;
    });

    S.use("downlist");


    // 车牌弹出框，回调
    gift.fillCustomer = function (obj, item) {
        var carLicense = item["license"];

        S.use("ajax", function (ajax) {
            ajax.get({
                url: BASE_PATH + '/shop/customer_car/get_car_by_license',
                data: {
                    carLicense: carLicense
                },
                success: function (result) {
                    if (result.success != true) {
                        return false;
                    } else {
                        var item = result.data;
                        // 车主姓名
                        $('input[name="customerName"]').val(item["contact"]);
                        // 车主电话
                        $('input[name="mobile"]').val(item["contactMobile"]);
                        // customerCarId
                        $('input[name="customerCarId"]').val(item["id"]);

                        $('input[name="license"]').val(item["license"]);
                        $('input[name="byName"]').val(item["byName"]);


                        var carBrand = item["carBrand"];
                        var carSeries = item["carSeries"];
                        var carModel = item["carModel"];
                        var importInfo = item["importInfo"];
                        if (typeof(carBrand) == 'undefined' || carBrand == null) {
                            carBrand = "";
                        }
                        if (typeof(carSeries) == 'undefined' || carSeries == null) {
                            carBrand = "";
                        }
                        if (!carModel) {
                            carModel = "";
                        }
                        // TODO 引入工具类undersource.js

                        if (importInfo != undefined && importInfo != null && importInfo !="") {
                            $('input[name="carModel2"]').val(carBrand+" ("+importInfo+") "+carModel);
                        } else {
                            $('input[name="carModel2"]').val(carBrand+" "+carModel);
                        }
                        // 车别名
                        var byName = item["byName"];
                        if (byName == undefined || byName == null) {
                            byName = "";
                        }
                        $('input[name="appoint.carAlias"]').val(byName);

                    }
                },
                error: function (e) {
                    console.log(e);
                }
            });
        });
    };

    // 新增客户 弹出框
    gift.createCustomer =function(){
        S.use([ "dialog","chosenSelect","ajax" ], function(dg,chosen,ajax) {
            var dialogId = dg.dialog({
                "dom" : "#customer_create_dialog",
                "init" : function(){
                    ajax.post({
                        url: BASE_PATH + "/shop/customer/get_license_prefix",
                        success: function (json) {
                            if (json && json.success) {
                                chosen.handleChoosenSelect('.qxy_dialog select');
                                $("input[name='license']").val(json.data);
                            } else {
                                dialog.info(json.errorMsg, 3);
                            }
                        }
                    });
                }
            });

            //取消按钮事件绑定
            $(".cancel","#customer_create_dialog").click(function(){
                dg.closeDialog("#customer_create_dialog");
            });

            //绑定车型选择框
            $("#carMode").off("click").on("click",function(){
                util.carTypeSelect(function(data){
                    var carCompany = data["company"];
                    var importInfo = data["importInfo"];
                    var carBrand = data["brand"];
                    var carSeries = data["name"];
                    var temp = '';
                    if (carBrand) {
                        temp = temp + carBrand;
                    }
                    if (carSeries) {
                        temp = temp + carSeries;
                    }
                    if (importInfo) {
                        temp = temp + "(" + importInfo + ")"
                    }

                    $("input[name='carCompany']").val(carCompany);
                    $("input[name='carBrandId']").val(data["pid"]);
                    $("input[name='carBrand']").val(carBrand);
                    $("input[name='carSeriesId']").val(data["id"]);
                    $("input[name='carSeries']").val(carSeries);

                    $('#carMode').val(temp);
                    $("input[name='carModelId']").blur();
                });
            });
        });
    };


    //提交
    gift.submit =function(){
        var license = $("input[name='license']").val();
        S.use(["ajax","dialog"],function(ajax,dialog){
            ajax.post({
                url: BASE_PATH + '/shop/customer/checkLicense',//创建新客户方法
                data: {
                    license:license
                },
                success: function (result) {
                    if(result.success==true){
                        dialog.info('该车辆还不是您的客户，请先点击"新增客户"按钮',3);
                    }else{
                        util.submit({
                            formid:"gift_form",
                            loadShow : true,
                            loadText : '正在保存信息中...',
                            callback:function(json){
                                if(json.success){
                                    dialog.info("礼品发放成功,发放"+json.data,1,3,function(){
                                        window.location.href = BASE_PATH+"/shop/shop_service_info/giftInfo";
                                    },true);
                                }else{
                                    dialog.info(json.errorMsg,3);
                                }
                            }
                        });
                    }
                }
            });


        });
    };

})(jQuery, seajs);


// 新增客户 弹框
var customerDialog = customerDialog || {};
(function($,S){
    // 填充表单
    customerDialog.fillCustomerSource =function(obj, item){
        // 来源名称
        $('input[name="customerSource"]').val(item["source"]);
    };

    // 提交
    customerDialog.submitForm =function(){
        S.use([ "dialog" ], function(dialog) {
            util.submit({
                formid:"customer_add_form",
                callback:function(json){
                    if(json.success){
                        dialog.info("操作成功",1);
                        $('input[name="orderInfo.carLicense"]').val(json["data"]["license"]);

                        // 填充客户信息
                        gift.fillCustomer(null,json["data"]);

                        dialog.closeDialog("#customer_create_dialog");
                    }else{
                        dialog.info(json.errorMsg,3);
                    }
                }
            });
        });
    };

    // 取消
    customerDialog.cancle =function(){
        seajs.use("dialog",function(dialog){
            dialog.closeDialog("#customer_create_dialog");
        });
    }
})(jQuery,seajs);


// 记住当前操作对象
var optObject ;
var rememberMe =function(obj){
    optObject =$(obj);


};


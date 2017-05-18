/**
 * hyx 2016-04-18
 * 洗车完善页面JS
 */
$(function () {
    var $doc = $(document);

    //成功信息停留时间
    var yesTime = 3;
    //判断上传文件的加号是否应该在加载时出现
    if($('.fileBtn').prev().is(':visible')){
        $('.fileBtn').hide();
    }
    //验证初始化
    seajs.use([
        'check',
        'downlist',
        'upload',
        'formData',
        'dialog',
        'ajax'], function (ck, dl, ud, fd, dg, ax) {
        //可以传一个作用域,如果不传默认的是整个body
        ck.init();
        dg.titleInit();

        var orderId = $("input[name='orderId']").val();

        // 初始化车牌下拉框
        dl.init({
            url: BASE_PATH + '/shop/customer/search/mobile',
            searchKey: 'com_license',
            showKey: 'license',
            hasTitle: false,
            notClearInput: true,
            dom: 'input[name="carLicense"]',
            hasInput: false,
            callbackFn: function (obj, item) {
            }
        });

        //初始化客户单位下拉框
        dl.init({
            url: BASE_PATH + '/shop/customer/search/company',
            showKey: 'company',
            searchKey: 'company',
            tplCols: {'company': '客户单位'},
            notClearInput: true,
            dom: 'input[name="company"]',
            hasInput: false
        });

        //关闭图片
        $doc.on("click", ".close", function () {
            $.ajax({
                url: BASE_PATH + '/shop/order/carwashperfect/delpic',
                data: {
                    orderId: orderId
                },
                success: function (result) {
                    if (result["success"] == true) {
                        dg.success("图片移除成功");
                        $('.close').parent().hide();
                        $('.fileBtn').show();
                        $("input[name='carLicensePicture']").val(null);

                    } else {
                        dg.fail("图片移除失败！");
                    }
                }
            });
        });

        //图片上传
        $doc.on("click", ".fileBtn", function () {
            $doc.off('error', '.example')
                .on('error','.example', function() {
                    if($('.info-text').is(':hidden')) {
                        $('.addDiv').show();
                    }
                });
            ud.init({
                dom: '#fileBtn',
                url: BASE_PATH + '/index/oss/upload_img',
                callback: function (result) {

                        var path = result.data.thumb;
                    $('.fileBtn').hide();
                    if($('.info-text').is(':visible')){
                        $('.car-img').attr('src',path);
                    }else{
                        $('.example').attr('src',path);
                        $('.addDiv').show();
                    }

                        $("input[name='carLicensePicture']").val(path);
                        // 清空type=file的value(upload根据change事件执行上传任务)
                        $('#fileBtn').val('');

                }
            });
        });

        //取消修改
        $doc.on('click', '.cancel', function () {
            window.location.href = BASE_PATH + "/shop/order/detail?orderId=" + orderId;
        });
        $doc.on('click', '.js-return', function () {
            util.goBack();
        });

        //提交表单
        $doc.on('click', '.submit', function () {
            //校验数据
            var result = ck.check(),
                url = BASE_PATH + '/shop/order/carwashperfect/save',
                data;
            if (!result) {
                return;
            }
            //读取表单数据
            data = fd.get("#form");
            //提交数据
            $.post(url, data, function (json) {
                if (json.success) {
                    dg.success("完善洗车单保存成功，" + yesTime + "秒后自动跳转到洗车单详情页", function () {
                        setTimeout(function () {
                            window.location.href = BASE_PATH + "/shop/order/detail?orderId=" + orderId;
                        }, yesTime);
                    });
                } else {
                    dg.fail(json.errorMsg);
                }
            });
        });

        /* 动态下拉列表初始化 start */
        // 车辆选择
        var dlCarBrand = dl.init({
            url: BASE_PATH + '/shop/car_category/car_model',
            dom: 'input[name="carModeBak"]',
            showKey: 'brand,importInfo,model',
            hiddenKey: 'brandId,brand,seriesId,series,modelId,model,importInfo',
            hiddenSelector: 'carBrandId,carBrand,carSeriesId,carSeries,carModelId,carModel,importInfo',
            tplColsWidth: [80, 50, 180],
            hasTitle: false,
            hasInput: false,
            autoFill: true,
            callbackFn: function($input, data, $scope) {
                carTypeCallBack(data);
            },
            clearCallbackFn: function($input, $scope) {
                var yearPowerHidden = 'carYear,carYearId,carPower,carPowerId,carGearBox,carGearBoxId'.split(','),
                    name, i;
                // 年款排量清空
                for(i = 0; i < yearPowerHidden.length; i++) {
                    name = yearPowerHidden[i];
                    $('input[name="' + name + '"]').val('');
                }
                $("input[name='yearPowerBak']").attr('disabled', true).val('');
                // vin码清空
                $('input[name="vin"]').val('');
            }
        });

        // 年款排量
        var dlYearPower = dl.init({
            url: BASE_PATH + '/shop/car_category/car_model',
            dom: "input[name='yearPowerBak']",
            showKey: 'year,gearbox',
            hiddenKey: 'year,yearId,power,powerId,gearbox,gearboxId',
            hiddenSelector: 'carYear,carYearId,carPower,carPowerId,carGearBox,carGearBoxId',
            tplId: 'yearPowerDLTpl',
            hasTitle: false,
            hasInput: false,
            autoFill: true
        });
        /* 动态下拉列表初始化 end */

        //选择车型按钮
        carTypeInit({
            dom: '.js-car-type',
            callback: function (data) {
                //激活年款排量输入框
                $("input[name='carBrand']").val(data.carBrand);
                $("input[name='carBrandId']").val(data.carBrandId);
                $("input[name='carModel']").val(data.carModel);
                $("input[name='carModelId']").val(data.carModelId);
                $("input[name='carSeries']").val(data.carSeries);
                $("input[name='carSeriesId']").val(data.carSeriesId);
                $("input[name='importInfo']").val(data.importInfo);
                //车型展示字段
                var carModel = data.carBrand + " " +data.carModel;
                $("input[name='carModeBak']").val(carModel);
                //回调方法
                carTypeCallBack(data);
            }
        });

        //选择车型的回调函数
        function carTypeCallBack(data){
            var modelId = data.carModelId == null ? data.modelId : data.carModelId;
            /* 重置 start */
            $("input[name='yearPowerBak']")
                .removeAttr('disabled').val("")
                .siblings('[name=carYear]').val("")
                .siblings('[name=carYearId]').val("")
                .siblings('[name=carPower]').val("")
                .siblings('[name=carPowerId]').val("")
                .siblings('[name=carGearBox]').val("")
                .siblings('[name=carGearBoxId]').val("");
            // 下拉列表年款排量动态修改参数
            dl.update(dlYearPower, {globalData: {'model_id': modelId}});
        };

        var oldVinAjax;
        // vin码输入17位后请求车型相关数据
        $doc.on('input', 'input[name="vin"]', function() {
            var value = $(this).val();
            // issue: 触发downlist的click.dl后，在document上绑定了keyup.dl事件，产生无谓的请求
            dlCarBrand.closeDownList();
            if (value.length === 17) {
                if (oldVinAjax) {
                    return false;
                }
                //先检验vin码是否存在
                oldVinAjax = $.ajax({
                    url: BASE_PATH + "/shop/customer/is-exist-vin",
                    type: 'GET',
                    data: {
                        vin:value,
                        customerCarId:$("#customerCarId").val()
                    },
                    success: function(result) {
                        oldVinAjax = null;
                        if (result.success) {
                            //vin码可用
                            dl.update(dlCarBrand, {
                                url: BASE_PATH + '/shop/car_category/car_model_by_vin',
                                showKey: 'brand,model',
                                tplColsWidth: [80, 180],
                                hiddenKey: 'brandId,brand,seriesId,series,modelId,model,importInfo',
                                hiddenSelector: 'carBrandId,carBrand,carSeriesId,carSeries,carModelId,carModel,importInfo',
                                globalData: {
                                    q: value
                                },
                                delay: 500,
                                callbackFn: function($input, data, $scope) {

                                    carTypeCallBack(data);

                                    // 年款排量下拉列表参数修订
                                    dl.update(dlYearPower, {globalData: {'model_id': data.modelId}});
                                    /* 重置 end */

                                    $("input[name='yearPowerBak']")
                                        .val(data.year + " " + data.power)
                                        .siblings('[name=carYear]').val(data.year)
                                        .siblings('[name=carYearId]').val(data.yearId)
                                        .siblings('[name=carPower]').val(data.power)
                                        .siblings('[name=carPowerId]').val(data.powerId);

                                },
                                clearCallbackFn: null,
                                reqCallbackFn: function(json){
                                    if(json && json.success) {
                                        return;
                                    }
                                    ck.showCustomMsg('找不到对应的车型数据', $("input[name='carModeBak']"));
                                    // 车辆型号下拉列表参数重置
                                    dl.reset(dlCarBrand);
                                }
                            });
                            $("input[name='carModeBak']").trigger('click.dl');
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            } else {
                // 车辆型号下拉列表参数重置
                dl.reset(dlCarBrand);
            }
        })
    });

});
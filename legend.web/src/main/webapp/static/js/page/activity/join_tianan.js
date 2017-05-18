$(function () {
    var doc = $(document);

    seajs.use([
        'check',
        'upload',
        'art',
        'dialog',
        'formData',
        'downlist'
    ], function (ck, ud, at, dg, fd, dl) {
        //验证
        ck.init();
        dg.titleInit();

        var url = window.location.href;
        var billid = url.indexOf('billid');
        if (billid != -1) {
            var licence = $('.js-vehicle-num').val();
            $.ajax({
                url: BASE_PATH + '/insurance/tianan/get_by_license?carLicense=' + licence,
                success: function (result) {
                    if (result.success) {
                        var html = at('serviceTpl', {json: result});
                        $('#serviceCon').html(html);
                        $('.timework').val('1');
                        $('.timework').attr('disabled', 'disabled');
                    } else {
                        dg.fail(result.errorMsg)
                    }
                }

            })
        }


        // 年款排量
        var dlYearPower = dl.init({
            url: BASE_PATH + '/shop/car_category/car_model',
            dom: '#yearPowerBak',
            showKey: 'year,gearbox',
            scope: 'yearPowerBak-box',
            hiddenKey: 'year,yearId,power,powerId,gearbox,gearboxId',
            hiddenSelector: 'carYear,carYearId,carPower,carPowerId,carGearBox,carGearBoxId',
            tplId: 'yearPowerDLTpl',
            globalData: {
                model_id: $('input[name=carModelsId]').val()
            },
            hasTitle: false,
            hasInput: false,
            autoFill: true
        });

        //初始化车牌下拉框
        dl.init({
            url: BASE_PATH + '/shop/customer/search/mobile',
            searchKey: 'com_license',
            tplId: 'carLicenceTpl',
            showKey: 'license',
            dom: 'input[name=carLicense]',
            hasInput: false,
            notClearInput: true,
            callbackFn: function (obj, item) {
                $('.js-vehicle-num').val(item.license);

                $.ajax({
                    url: BASE_PATH + '/shop/customer_car/get_car_by_license',
                    data: {
                        carLicense: item["license"]
                    },
                    success: function (json) {
                        if (json.success) {
                            var modelId = json.data.carModelId;
                            dl.update(dlYearPower, {globalData: {'model_id': modelId}});
                            //填充customerCarId
                            $("input[name='customerCarId']").val(json.data.id);

                            setCarData(json.data);

                            $("input[name='customerName']").val(json.data.customerName);
                            $("input[name='customerMobile']").val(json.data.mobile);

                            // 行驶里程
                            $("input[name='orderInfo.mileage']").val(json.data.mileage);
                            // VIN码
                            $("input[name='vin']").val(json.data.vin);
                            // 客户单位
                            $("input[name='company']").val(json.data.company);
                            //天安券号
                            $(".js-vehicle-num").val(json.data.license);
                            $('#carModel').val(json.data.carInfo);
                            var carYear = json.data.carYear != null ? json.data.carYear : "";
                            var carGearBox = json.data.carGearBox != null ? json.data.carGearBox : "";
                            var carPower = json.data.carPower != null ? json.data.carPower : "";
                            if(carYear != ""){
                                if(carGearBox != ""){
                                    $("#yearPowerBak").val(carYear + " " + carGearBox);
                                }else if (carPower != ""){
                                    $("#yearPowerBak").val(carYear + " " + carPower);
                                }
                            }
                        }
                    }
                });
            },
            clearCallbackFn: function ($input, $scope) {
                var yearPowerHidden = 'carYear,carYearId,carPower,carPowerId,carGearBox,carGearBoxId'.split(','),
                    name, i;
                // 年款排量清空
                for (i = 0; i < yearPowerHidden.length; i++) {
                    name = yearPowerHidden[i];
                    $('input[name="' + name + '"]').val('');
                }
                $('input[name=yearPowerBak]').attr('disabled', true).val('');
                // 车辆级别清空
                $('input[name="carLevel"]').val('');
                // vin码清空
                $('input[name="vin"]').val('');
            }
        });

        //车型选择
        carTypeInit({
            dom: '.js-car-type',
            callback: function (data) {
                $('.vehicle-text').val(data.carInfoStr);
                var modelId = data.carModelId;
                dl.update(dlYearPower, {globalData: {'model_id': modelId}});

                setCarData(data);
            }
        });

        // 车辆选择
        dl.init({
            url: BASE_PATH + '/shop/car_category/car_model',
            dom: '#carModel',
            showKey: 'brand,importInfo,model',
            hiddenKey: 'brandId,brand,seriesId,series,modelId,model,importInfo',
            hiddenSelector: 'carBrandId,carBrand,carSeriesId,carSeries,carModelId,carModel,importInfo',
            tplColsWidth: [80, 50, 180],
            hasTitle: false,
            hasInput: false,
            autoFill: true
        });

        $('input[name=carLicense]').on('input', function () {
            $('.js-vehicle-num').val($.trim($(this).val()));
        });

        //查询按钮
        doc.on('click', '.js-search', function () {
            checkCode();
        });


        //图片上传
        doc.on("click", ".js-upload-btn", function () {
            var $this = $(this);
            var fileCallBackData = $this.parents('.photo_box').find('.fileCallbackData');
            ud.init({
                dom: '.js-upload-btn .file-btn',
                url: BASE_PATH + '/index/oss/upload_img',
                callback: function (result) {
                    var path = result.data.original;
                    fileCallBackData.attr('src', path);
                    fileCallBackData.data('content', true);

                    fileCallBackData.data('date', new Date());
                }
            });
        });


        //图片上传增加
        var prevNum = 0;
        doc.on('blur', '.timework', function () {
            var num = $(this).val() <= 1 ? 0 : $(this).val() - 1, t = +num - prevNum, count = $(this).val();

            var surplus  = $('.js-surplus').text();
            var html = at('addPhotoTpl', {}), dataLen = $('.photo-data-list').length;

            if(!ck.check('.yqx-table')){
                return;
            }

            if (+count > Number(surplus)) {
                dg.warn('本次核销次数不能大于剩余核销次数');
                return false;
            }

            if(t < 0) {
                for(var i = 0; i < -t ; i++) {
                    $('.photo-data-list').eq(dataLen - i - 1)
                        .not('.notDel').remove();
                }
            } else {
                for (var i = 0; i < t; i++) {
                    $('#addPhoto').append(html);
                }
            }
            prevNum = num;
        });

        //返回
        doc.on('click', '.js-return', function () {
            util.goBack();
        });

        function checkCode(){
            var licence = $('.js-vehicle-num').val();
            var _flag;
            $.ajax({
                url: BASE_PATH + '/insurance/tianan/get_by_license?carLicense=' + licence,
                async: false,
                success: function (result) {
                    if (result.success) {
                        var html = at('serviceTpl', {json: result});
                        $('#serviceCon').html(html);
                        _flag = true;
                    } else {
                        dg.fail(result.errorMsg);
                        _flag = false;
                    }
                }
            });
            return _flag;
        }

        //保存按钮
        doc.on('click', '.js-save', function () {
            if (!ck.check()) {
                return false;
            }
            var num = $('.timework').val();
            var surplus = $('.js-surplus').text();
            if (num == '' || num == null || num == 0) {
                var _flag = checkCode();
                if (!_flag){
                    return;
                }
                dg.warn('请输入本次核销次数');
                return;
            }
            if (Number(num) > Number(surplus)) {
                dg.warn('本次核销次数不能大于剩余核销次数');
                return false;
            }
            dg.confirm('系统将自动扣除保险赠送的服务次数,确认保存?', function () {
                dg.confirm('核销成功，系统自动创建' + num + '张服务单，车辆修理完成之后您需要对' + num + '账服务单进行提交审核操作。', function () {
                    var formEntitys = [];
                    for (var i = 0; i < num; i++) {
                        var InsuranceBillFormEntity = {};
                        var orderInfo = fd.get('#formData');
                        //本次核销次数
                        orderInfo.usedTime = $('input[name="usedTime"]').val();
                        //备注
                        orderInfo.billNote = $('textarea[name="billNote"]').val();

                        //服务图片
                        var imgUrl = null, woundSnapshoot = null, acceptanceSnapshoot = null;
                        var _e = $('.photo-data-list').eq(i);
                        imgUrl = $('.car-imgurl').attr('src');
                        var flag3 =$('.car-imgurl').data('content');
                        if(flag3){
                            orderInfo.imgUrl = imgUrl;
                        }
                        var woundSnapshoot = _e.find('.woundSnapshoot').attr('src');
                        var flag1 = _e.find('.woundSnapshoot').data('content');
                        var acceptanceSnapshoot = _e.find('.acceptanceSnapshoot').attr('src');
                        var flag2 = _e.find('.acceptanceSnapshoot').data('content');
                        if(flag1){
                            orderInfo.woundSnapshoot = woundSnapshoot;
                        }
                        if(flag2){
                            orderInfo.acceptanceSnapshoot = acceptanceSnapshoot;
                        }
                        InsuranceBillFormEntity.orderInfo = orderInfo;
                        formEntitys.push(InsuranceBillFormEntity);
                    }
                    $.ajax({
                        url: BASE_PATH + '/insurance/tianan/bill/save',
                        type: 'post',
                        contentType: 'application/json',
                        data: JSON.stringify(formEntitys),
                        success: function (result) {
                            if (result.success) {
                                dg.success('保存成功', function () {
                                    window.location.href = BASE_PATH + '/shop/activity/bill/list'
                                });
                            } else {
                                dg.fail(result.errorMsg);
                            }
                        }
                    });
                }, function () {
                    return false;
                });
            }, function () {
                return false
            });


        });

        //编辑保存按钮
        doc.on('click', '.js-update', function () {
            var orderInfo = fd.get('#formData');
            //服务图片
            orderInfo.billNote = $('textarea[name="billNote"]').val();
            var imgUrl = null, woundSnapshoot = null, acceptanceSnapshoot = null;
            woundSnapshoot = $('.woundSnapshoot').attr("src");
            var flag1 = $('.woundSnapshoot').data("content");
            if(flag1){
                orderInfo.woundSnapshoot = woundSnapshoot;
            }
            acceptanceSnapshoot = $('.acceptanceSnapshoot').attr("src");
            var flag2 = $('.acceptanceSnapshoot').data("content");
            if(flag2){
                orderInfo.acceptanceSnapshoot = acceptanceSnapshoot;
            }
            imgUrl = $('.car-imgurl').attr("src");
            var flag3 = $('.car-imgurl').data("content");
            if(flag3){
                orderInfo.imgUrl = imgUrl;
            }
            var insuranceBillFormEntity = {};
            insuranceBillFormEntity.orderInfo = orderInfo;
            $.ajax({
                url: BASE_PATH + '/insurance/tianan/bill/update',
                type: 'post',
                contentType: 'application/json',
                data: JSON.stringify(insuranceBillFormEntity),
                success: function (result) {
                    if (result.success) {
                        dg.success('保存成功', function () {
                            location.reload();
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        });

        //提交审核按钮
        doc.on('click', '.js-submit', function () {
            bill_submit(2);
        });
        //重新提交审核
        doc.on('click', '.js-resubmit', function () {
            bill_submit(3);
        });

        //提交审核的方法
        function bill_submit(flag) {
            var orderInfo = fd.get('#formData');
            orderInfo.billNote = $('textarea[name="billNote"]').val();
            //服务图片
            var imgUrl = null, woundSnapshoot = null, acceptanceSnapshoot = null;
            woundSnapshoot = $('.woundSnapshoot').attr("src");
            var flag1 = $('.woundSnapshoot').data("content");
            if(flag1){
                orderInfo.woundSnapshoot = woundSnapshoot;
            }
            acceptanceSnapshoot = $('.acceptanceSnapshoot').attr("src");
            var flag2 = $('.acceptanceSnapshoot').data("content");
            if(flag2){
                orderInfo.acceptanceSnapshoot = acceptanceSnapshoot;
            }
            imgUrl = $('.car-imgurl').attr("src");
            var flag3 = $('.car-imgurl').data("content");
            if(flag3){
                orderInfo.imgUrl = imgUrl;
            }
            var insuranceBillFormEntity = {};
            insuranceBillFormEntity.orderInfo = orderInfo;
            insuranceBillFormEntity.flag = flag;
            $.ajax({
                url: BASE_PATH + '/insurance/tianan/bill/submit',
                type: 'post',
                contentType: 'application/json',
                data: JSON.stringify(insuranceBillFormEntity),
                success: function (result) {
                    if (result.success) {
                        dg.success('保存成功', function () {
                            location.reload();
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        }

        function setCarData(data) {
            var arr = ['carBrandId', 'carBrand', 'carSeriesId',
                    'carSeries', 'carModelsId', 'carModels',
                    'importInfo'],
                arr2 = ['carBrandId', 'carBrand', 'carSeriesId',
                    'carSeries', 'carModelId', 'carModel',
                    'importInfo'];

            arr.forEach(function (e, i) {
                $('input[name=' + e + ']').val(data[arr2[i]]);
            });

        }

    });


});
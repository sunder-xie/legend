/**
 * Created by ende on 16/4/8.
 */
$(function () {
    var $document = $(document);

    seajs.use([
        'ajax',
        'dialog',
        'date',
        'select',
        'downlist',
        'check'
    ],function(ax,dg,dt,st, dl,ck){
        //初始化电话号码
        var oldMobile = $('input[name="mobile"]').val();
        // 验证表单初始化
        ck.init();
        dg.titleInit();
        // 日历绑定
        dt.datePicker('.js-date',{
            maxDate:'%y-%M-%d'
        });
        dt.datePicker('.js-date-min',{
            minDate:'%y-%M-%d'
        });
        dt.datePicker('.js-date-2');
        dt.datePicker('.js-date-month',{
            dateFmt: 'yyyy-MM'
        });
        dt.datePicker('.js-date-month2',{
            maxDate:'%y-%M',
            dateFmt: 'yyyy-MM'
        });
        //初始化客户单位下拉框
        dl.init({
            url: BASE_PATH + '/shop/customer/search/company',
            showKey: 'company',
            searchKey: 'company',
            notClearInput: true,
            tplCols: {'company': '客户单位'},
            dom: 'input[name="company"]',
            hasInput: false
        });
        /* 动态下拉列表初始化 start */
        // 车辆选择
        var dlCarBrand = dl.init({
            url: BASE_PATH + '/shop/car_category/car_model',
            dom: '#carModel',
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
                $('#yearPowerBak').attr('disabled', true).val('');
                // 车辆级别清空
                $('input[name="carLevel"]').val('');
                // vin码清空
                $('input[name="vin"]').val('');
            }
        });

        // 年款排量
        var dlYearPower = dl.init({
            url: BASE_PATH + '/shop/car_category/car_model',
            dom: '#yearPowerBak',
            showKey: 'year,gearbox',
            hiddenKey: 'year,yearId,power,powerId,gearbox,gearboxId',
            hiddenSelector: 'carYear,carYearId,carPower,carPowerId,carGearBox,carGearBoxId',
            tplId: 'yearPowerDLTpl',
            hasTitle: false,
            hasInput: false,
            autoFill: true
        });
        /* 动态下拉列表初始化 end */

        //选择车型的回调函数
        function carTypeCallBack(data){
            var modelId = data.carModelId == null ? data.modelId : data.carModelId;
            /* 重置 start */
            $("#yearPowerBak")
                .removeAttr('disabled').val("")
                .siblings('[name=carYear]').val("")
                .siblings('[name=carYearId]').val("")
                .siblings('[name=carPower]').val("")
                .siblings('[name=carPowerId]').val("")
                .siblings('[name=carGearBox]').val("")
                .siblings('[name=carGearBoxId]').val("");
            // 下拉列表年款排量动态修改参数
            dl.update(dlYearPower, {globalData: {'model_id': modelId}});
            //异步获取下拉列表数据
            $.ajax({
                type:"POST",
                url: BASE_PATH + "/shop/customer/get_car_level?id=" + modelId,
                success:function (json) {
                    if (json && json.success) {
                        $('input[name="carLevel"]').val(json.data.guidePrice);
                    } else {
                        dg.fail(json.errorMsg);
                    }
                }
            });
        }

        function toOrder(dg,url,params,$this){
            $.ajax({
                url: url,
                type: 'POST',
                contentType:"application/json",
                data:params,
                success: function(result) {
                    if (result.success) {
                        var content = $this.data("content");
                        var customerCar = result.data;
                        var cid = customerCar.id;
                        if(content == 0 ){
                            location.href = BASE_PATH+"/shop/order/speedily?customerCarId="+cid;//快修快保单
                        }else if(content == 1){
                            location.href = BASE_PATH+"/shop/order/common-add?customercarid="+cid;//开综合维修单
                        }else if(content == 2){
                            location.href = BASE_PATH+"/shop/order/carwash?customerCarId="+cid;//开洗车单
                        }
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        }

        $document.on('input', '.js-number', function() {
            $(this).val( checkFloat($(this).val(), 0) );
        });

        function checkMileage(){
            var mileage = $("input[name='mileage']").val();
            var upkeepMileage = $("input[name='upkeepMileage']").val();
            if (!$.isNumeric(mileage) || mileage < 0 || !$.isNumeric(upkeepMileage) || upkeepMileage < 0 || parseInt(mileage) < parseInt(upkeepMileage)) {
                return true;
            }
            dg.warn("下次保养里程不能小于或等于行驶里程");
            return false;
        }
        // 下次保养里程校验
        $document.on("blur", "input[name='upkeepMileage'],input[name='mileage']", function () {
            checkMileage();
        });

        // 保存信息
        $document.on('click', '.js-save-btn', function() {

            seajs.use('check', function(ck) {
                var data = $('#customerInfo').serializeArray(),
                    params = {}, item, i;
                var id = $('#id').val();
                var url = BASE_PATH;

                url += id ? '/shop/customer/update': '/shop/customer/add';

                if (data && data.length) {
                    for (i = 0; i < data.length; i++) {
                        item = data[i];
                        params[item.name] = item.value;
                    }
                }

                if ( !ck.check() ) {
                    return false;
                }
                if(!checkMileage()){
                    return false;
                }
                var customerMobile = $('input[name="mobile"]').val();
                if(oldMobile != customerMobile){
                    $.ajax({
                        url: BASE_PATH + "/shop/customer/check_mobile",
                        data: {
                            mobile: customerMobile
                        },
                        success: function (result) {
                            if (result.success) {
                                saveCustomer(params, url ,false);
                            } else {
                                seajs.use('layer',function(){
                                    layer.confirm("您所填写的车主电话已存在客户，是否确认将此车牌绑定到该客户下",
                                        {
                                            btn: ['绑定', '不绑定']
                                        }, function () {
                                            saveCustomer(params, url ,true);
                                        },function(){
                                            $("#mobile").val("");
                                            dg.fail("请您填写新的车主电话信息");
                                            //$("#new_mobile").onfocus();
                                        });
                                });
                            }
                        }
                    });
                }else{
                    saveCustomer(params, url,false);
                }
            });
        })


        function saveCustomer(params,url,band){
            $.ajax({
                url: url,
                type: 'POST',
                data: params,
                success: function(result) {
                    if (result.success) {
                        if(band){
                            dg.success("车辆绑定到新账户下，车辆原有账户信息数据保留");
                        }
                        var name = util.getPara('page');
                        var license = $("input[name='license']").val();
                        if(name==='1'){
                            location.href = BASE_PATH + '/account/combo/grant?license='+license;
                        }else if(name==='0'){
                            location.href = BASE_PATH + '/account/member/grant?license='+license;
                        }else{
                            location.href = BASE_PATH + '/shop/customer/car-detail?id='+result.data.id+'&refer=customer';
                        }
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        }

        //按钮跳转
        $document.on('click','.js-to-order',function(){
            var data = $('#customerInfo').serializeArray(),
                params = {}, item, i;
            var id = $('#id').val();
            var url;
            if(id){
                url = BASE_PATH + '/shop/customer/update';
            }else{
                url = BASE_PATH + '/shop/customer/add';

            }

            if (data && data.length) {
                for (i = 0; i < data.length; i++) {
                    item = data[i];
                    params[item.name] = item.value;
                }
            }
            var s = JSON.stringify(params);

            toOrder(dg,url,s,$(this));
        });

        //js绑定保险公司下拉框
        st.init({
            dom: '.js-select',
            url: BASE_PATH + '/shop/customer/insurance_company_list',
            canInput: true,
            showKey: "id",
            showValue: "name"
        });
        //绑定来源下拉按钮
        dl.init({
            dom: '.source-select',
            url:BASE_PATH + '/shop/customer_source/list',
            showKey: 'source',
            hasTitle: false,
            notClearInput: true,
            hasInput: false
        });

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
                var carModel = data.carBrand+ " " +data.carModel;
                $("#carModel").val(carModel);
                //回调方法
                carTypeCallBack(data);
            }
        });

        var oldVinAjax;
        // vin码输入17位后请求车型相关数据
        $document.on('input', 'input[name="vin"]', function() {
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
                        customerCarId:$("#id").val()
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
                                    var $yearPowerBak = $('#yearPowerBak');

                                    carTypeCallBack(data);

                                    // 年款排量下拉列表参数修订
                                    dl.update(dlYearPower, {globalData: {'model_id': data.modelId}});
                                    /* 重置 end */

                                    $yearPowerBak
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
                                    ck.showCustomMsg('找不到对应的车型数据', $('#carModel'));
                                    // 车辆型号下拉列表参数重置
                                    dl.reset(dlCarBrand);
                                }
                            });

                            $('#carModel').trigger('click.dl');
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            } else {
                // 车辆型号下拉列表参数重置
                dl.reset(dlCarBrand);
            }
        });
        $document.on('click', '.js-return-btn', function() {
            util.goBack();
        });

        var tipId;
        $document.on("mouseenter", "#notice", function() {
            var self = this;
            seajs.use(["dialog"], function(dg) {
                var msg = "VIN车型数据不断更新完善，请将问题VIN码反馈给我们，我们会第一时间反馈给车型数据合作方，谢谢您的理解与支持！";
                tipId = dg.tips(self, msg);
            })
        }).on("mouseleave", "#notice", function(e) {
                var target = e.target;
                if(target.parentNode === this) return;
                seajs.use(["dialog"], function(dg) {
                    dg.close(tipId);
                })
            });

        //添加车辆重复blur
        $document.on('blur','.js-check-license',function(){
            var license =$.trim($("input[name='license']").val());
            if(license.length > 5){
                var id = $.trim($("input[name='id']").val());
                var data = {
                    license:license,
                    id : id
                }
                $.ajax({
                    type: 'POST',
                    url: BASE_PATH + '/shop/customer/checkLicense',//创建新客户方法
                    data: data,
                    success: function (result) {
                        if(!result.success){
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            }
        });

    });

});
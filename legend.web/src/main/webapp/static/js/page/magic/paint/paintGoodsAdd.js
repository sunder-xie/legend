/*
 * create by zmx 2016/11/19
 * 新增油漆资料
 */
$(function () {
    var doc = $(document),
        addBrandDialog = null;
    seajs.use([
        'check',
        'select',
        'dialog',
        'upload',
        'formData',
        'downlist'
    ], function (ck, st, dg, ud, fd,dl) {
        //验证
        ck.init();
        //确定有多少条车型数据
        carTypeLength();
        customAttributes();
        /************* 下拉列表 *******************/
        //选择零售单价单位
        st.init({
            dom: '.js-units',
            url: BASE_PATH + '/shop/goods_unit/get_by_name?name=' + name,
            showKey: "id",
            showValue: "name",
            canInput:true
        });
        //库位
        st.init({
            dom: '.js-depot',
            url: BASE_PATH + '/shop/goods/get_depot_list',
            showValue: "name",
            canInput: true
        });
        //选择油漆级别
        st.init({
            dom: '.js-paint-level',
            showKey: "key",
            showValue: "value",
            data: [{
                key: '1',
                value: '水性漆'
            }, {
                key: '2',
                value: '油性漆'
            }]
        });
        //选择油漆种类
        st.init({
            dom: '.js-paint-type',
            showKey: "key",
            showValue: "value",
            data: [{
                key: '1',
                value: '素色'
            }, {
                key: '2',
                value: '金属'
            }, {
                key: '3',
                value: '银粉'
            }, {
                key: '4',
                value: '珍珠'
            }, {
                key: '5',
                value: '弱色系'
            }]
        });
        //选择上架状态
        st.init({
            dom: '.js-onsale-status',
            showKey: "key",
            showValue: "value",
            selectedKey: 1,
            data: [{
                key: '1',
                value: '上架'
            }, {
                key: '0',
                value: '下架'
            }]
        });
        //选择品牌
        st.init({
            dom: '.js-brand',
            url: BASE_PATH + '/shop/goods_brand/shop_list',
            showKey: "id",
            showValue: "brandName",
            // 能否输入
            canInput: true,
            callback: function (showKey) {
                //回显品牌ID
                $('input[name="brandId"]').val(showKey)
            }
        });

        //上传图片
        ud.init({
            dom: '#fileBtn',
            url: BASE_PATH + '/index/oss/upload_image_name',
            fileSizeLimit: 1024 * 1024 * 5,//5M
            callback: function (json) {
                var str;
                for(var i in json.data) {
                    str = json.data[i].original;
                }
                $('.before-upload').hide();
                $('.upload-pic').html('<img class="upload-img" name="imgUrl" src='+ str +'>');
                $('input[name="imgUrl"]').val(str)
            }
        });

        /************* 事件绑定 *******************/

        var formatNum = true;
        var formatRepeat = '<div class="yqx-err-msg format-tips"><div class="yqx-err-msg-inner"> <p>零件号已存在,请重新填写</p><em></em><i></i> </div> </div>';
        var oldVal = $('input[name="format"]').val();
        // 零件号 
        doc.on('blur', 'input[name="format"]', function () {
            // 校验零件号是否存在 
            var formatInput = $(this);
            var format = formatInput.val();
            if(oldVal == format){
                formatNum = true;
                return;
            }else{
                if (typeof(format) != 'undefined' && $.trim(format) != "") {
                    $.ajax({
                        url: BASE_PATH + "/shop/goods/formatisexist",
                        type: 'GET',
                        data: {
                            'goodsFormat': format
                        },
                        success: function (result) {
                            if (result["success"]) {
                                if (!result["data"]) {
                                    $('input[name="format"]').before(formatRepeat);
                                    $('.format-tips').show();
                                    setTimeout(function () {
                                        $('.format-tips').remove();
                                    }, 3000);
                                    formatNum = false;
                                }else{
                                    formatNum = true;
                                }
                            } else {
                                return;
                            }
                        }
                    });
                } else {
                    $('input[name="format"]').before(formatRepeat);
                    $('.format-tips').find('p').text('请输入零件号');
                    $('.format-tips').show();
                    setTimeout(function () {
                        $('.format-tips').hide();
                    }, 3000);
                }
            }
        });


        //新增自定义品牌
        doc.on('click', '.js-add-brand', function () {
            var html = $('#addBrand').html();
            addBrandDialog = dg.open({
                area: ['400px', '180px'],
                content: html
            });
        });

        //新增自定义品牌保存按钮
        doc.on('click', '.js-save-brand-name', function () {
            if (!ck.check('#addBrandCheck')) {
                return;
            }
            var brandName = $('.js-addbrand-name').val();
            var goodsBrand = {brandName: brandName};
            $.ajax({
                type : "POST",
                dataType:"json",
                contentType: "application/json",
                url: BASE_PATH + '/shop/goods_brand/goods-brand-save',
                data: JSON.stringify(goodsBrand),
                success: function (result) {
                    if (result.success) {
                        dg.success(result.data);
                        dg.close(addBrandDialog);

                        dg.load({
                            time: 1000
                        });

                        $('.yqx-select-options').remove()
                    } else {
                        dg.fail(result.message)
                    }
                }
            })
        });

        //通用件（是）
        doc.on('click', '.js-general-yes', function () {
            if ($(this).is(':checked')) {
                $(this).prop('checked', true);
                $('.cartype-content').hide()
            }
        });
        //通用件（否）
        doc.on('click', '.js-general-no', function () {
            if ($(this).is(':checked')) {
                $(this).prop('checked', true);
                $('.cartype-content').show();
            }
        });
        //车型选择
        carTypeInit({
            dom: '.js-car-type',
            callback: function(data){
                $(this).prev('.form-item').find('input[name="carInfo"]').val(data.carBrand + " " + data.carModel);
                $(this).prev('.form-item').find('input[name="carBrandId"]').val(data.carBrandId);
                $(this).prev('.form-item').find('input[name="carBrandName"]').val(data.carBrand);
                $(this).prev('.form-item').find('input[name="carSeriesName"]').val(data.carSeries);
                $(this).prev('.form-item').find('input[name="carSeriesId"]').val(data.carSeriesId);
                $(this).prev('.form-item').find('input[name="carTypeName"]').val(data.carModel);
                $(this).prev('.form-item').find('input[name="carTypeId"]').val(data.carModelId);
            }
        });

        // 车辆选择
        dl.init({
            scope: 'yqx-downlist-wrap',
            url: BASE_PATH + '/shop/car_category/car_model',
            dom: '.js-carSeries',
            showKey: 'brand,importInfo,model',
            hiddenKey: 'brandId,brand,seriesId,series,modelId,model,importInfo',
            hiddenSelector: 'carBrandId,carBrand,carSeriesId,carSeries,carModelId,carModel,importInfo',
            tplColsWidth: [80, 50, 180],
            hasTitle: false,
            hasInput: false,
            autoFill: true,
            callbackFn: function($input, data, $scope) {
                $scope.parents('.form-item').find('input[name="carBrandId"]').val(data.brandId);
                $scope.parents('.form-item').find('input[name="carBrandName"]').val(data.brand);
                $scope.parents('.form-item').find('input[name="carSeriesId"]').val(data.seriesId);
                $scope.parents('.form-item').find('input[name="carSeriesName"]').val(data.series);
                $scope.parents('.form-item').find('input[name="carTypeId"]').val(data.modelId);
                $scope.parents('.form-item').find('input[name="carTypeName"]').val(data.model);
            }
        });


        //添加适用的车型
        doc.on('click', '.js-add-carType', function () {
            var html = $('#addCarTypeTpl').html();
            $('.cartype-content-box').append(html);
            //确定有多少条车型数据
            carTypeLength();
        });

        //删除适用的车型
        doc.on('click', '.js-remove-carType', function () {
            $(this).parents('.js-car-list').remove();
            //确定有多少条车型数据
            carTypeLength();
        });
        //添加自定义品牌（底部）
        doc.on('click', '.js-add-custom-brand', function () {
            var html = $('#addCustomBrandTpl').html();
            $('.addCustomBrandCon').append(html);
            customAttributes();
        });
        //删除自定义品牌(底部)
        doc.on('click', '.js-remove-custom-brand', function () {
            $(this).parents('.show-grid').remove();
            customAttributes();
        });

        //保存按钮
        doc.on('click', '.js-save', function () {
            // 表单校验
            if (!ck.check()) {
                return false;
            }
            if(formatNum == false){
                $('input[name="format"]').focus();
                $('input[name="format"]').before(formatRepeat);
                $('.format-tips').show();
                setTimeout(function () {
                    $('.format-tips').remove();
                }, 3000);
                return;
            }

            var goodsVo = fd.get('#formData');
            goodsVo.goodsSn = $('.goodsSn').text();
            goodsVo.cat2Name = '油漆及辅料';
            goodsVo.catId = '5098';
            goodsVo.stdCatId = '5098';
            goodsVo.paintLevel = $('input[name="paintLevel"]').val();
            goodsVo.paintType = $('input[name="paintType"]').val();
            goodsVo.netWeight = $('input[name="netWeight"]').val();
            goodsVo.bucketWeight = $('input[name="bucketWeight"]').val();
            goodsVo.stirWeight = $('input[name="stirWeight"]').val();
            goodsVo.brandId = $('input[name="brandId"]').val();
            goodsVo.partUsedTo = $('input[name="partUsedTo"]').val();
            goodsVo.imgUrl = $('input[name="imgUrl"]').val();
            goodsVo.goodsTag = '1';
            goodsVo.tqmallStatus = '5';   //5表示的是钣喷油漆类型
            goodsVo.goodsStatus='0';
            goodsVo.tqmallGoodsSn='0';
            goodsVo.tqmallGoodsId='0';

            //配件品牌(下拉列表不包含输入的内容则传空)
            if( $('input[name="brandId"]').val() == ''){
                goodsVo.brandName = '';
            }else{
                goodsVo.brandName = $('input[name="brandName"]').val();
            }
            //车型
            var goodsCarList = [];
            //自定义属性
            var goodsAttrRelList = [];
            //车型
            if (goodsCarList.length > 0) {
                goodsVo['goodsCarList'] = JSON.stringify(goodsCarList).toString();
            }
            if( $('.js-general-yes').is(':checked')){
                goodsVo.carInfo = '0'
            }
            if ($('.js-general-no').is(':checked')) {
                $('.js-car-list').each(function () {
                    if (!($('.cartype-content-box').is(':hidden'))
                        && (
                            $(this).find('input[name="carInfo"]').val() != ''
                            || $(this).find('input[name="carAlias"]').val() != ''
                        )
                    ) {
                        var carList = fd.get($(this));
                        goodsCarList.push(carList)
                    }
                });
            }

            //自定义品牌
            $('.rel-list').each(function () {
                var carList = fd.get($(this), 'submit');
                if (carList.attrValue != "") {
                    goodsAttrRelList.push(carList)
                }
            });

            if (goodsCarList.length > 0) {
                goodsVo['goodsCarList'] = JSON.stringify(goodsCarList).toString();
            }

            if (goodsAttrRelList.length > 0) {
                goodsVo['goodsAttrRelList'] = JSON.stringify(goodsAttrRelList).toString();
            }

            $.ajax({
                type: 'post',
                url: BASE_PATH + '/shop/goods/addBPGoods',
                data: JSON.stringify(goodsVo),
                dataType: 'json',
                contentType: "application/json",
                success: function (result) {
                    if (result.success) {
                        dg.success('成功', function () {
                            window.location.href = BASE_PATH +'/shop/goods/goods-list';
                        });
                    } else {
                        dg.fail(result.errorMsg)
                    }
                }
            })

        });

        //返回按钮
        doc.on('click','.js-return',function(){
            util.goBack()
        });

        function carTypeLength(){
            var carListLen = $('.js-car-list').length;
            if( carListLen <= 1){
                $('.js-car-list').find('.js-remove-carType').hide();
            }else{
                $('.js-car-list').eq(0).find('.js-remove-carType').show();
            }
        }

        function customAttributes(){
            var listLen = $('.rel-list').length;
            if( listLen <= 1){
                $('.rel-list').find('.js-remove-custom-brand').hide();
            }else{
                $('.rel-list').eq(0).find('.js-remove-custom-brand').show();
            }
        }
    });
});
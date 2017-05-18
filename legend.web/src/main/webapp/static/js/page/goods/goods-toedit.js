/*
 * create by zmx 2016/11/19
 * 新增油漆资料
 */
$(function () {
    var doc = $(document),
        addBrandDialog = null;
    seajs.use([
        'art',
        'check',
        'select',
        'dialog',
        'upload',
        'formData',
        'downlist'
    ], function (at, ck, st, dg, ud, fd,dl) {
        //验证
        ck.init();

        // 上架状态
        st.init({
            dom: '.js-sale',
            data: [{id: "1", name: "上架"}, {id: "0", name: "下架"}],
            showKey: "id",
            showValue: "name"
        });
        //库位
        st.init({
            dom: '.js-depot',
            url: BASE_PATH + '/shop/goods/get_depot_list',
            showValue: "name",
            canInput: true
        });

        // 零售单价单位
        st.init({
            dom: '.js-units',
            url: BASE_PATH + '/shop/goods_unit/get_by_name?name=' + name,
            showKey: "id",
            showValue: "name",
            canInput: true
        });
        //配件类别带出来的自定义属性
        st.init({
            dom:".js-add-val"
        });

        // 配件品牌
        st.init({
            dom: '.js-brand',
            url: BASE_PATH + '/shop/goods_brand/inwarehosue/list',
            showKey: "id",
            pleaseSelect: true,
            showValue: "brandName",
            canInput: true
        });

        // 淘汽配件品牌
        st.init({
            dom: '.js-tqmall-brand',
            url: BASE_PATH + '/shop/goods_brand/list',
            showKey: "id",
            pleaseSelect: true,
            showValue: "brandName",
            canInput: true
        });

        //上传图片
        ud.init({
            dom: '#fileBtn',
            url: BASE_PATH + '/index/oss/upload_image_name',
            fileSizeLimit: 1024 * 1024 * 5,//5M
            callback: function (json) {
                var str;
                for (var i in json.data) {
                    str = json.data[i].original;
                }
                $('.before-upload').hide();
                $('.upload-pic').html('<img class="upload-img" name="imgUrl" src=' + str + '>');
                $('input[name="imgUrl"]').val(str)
            }
        });

        setStandardGoodsAttrList();

        /************* 事件绑定 *******************/
        doc.on('click','input[name="cat2Name"]',function(){
            $('.js-stdCatId').remove();
        });
            //新增自定义品牌
        doc.on('click', '.js-add-brand', function () {
            var html = $('#addBrand').html();
            addBrandDialog = dg.open({
                area: ['400px', '180px'],
                content: html
            });
        });

        doc
            .on('blur', '.js-attr-name', function () {
                $(this).parent().toggleClass('form-label-must', $.trim($(this).val()) != '')
            });

        //新增自定义品牌保存按钮
        doc.on('click', '.js-save-brand-name', function () {
            if (!ck.check('#addBrandCheck')) {
                return;
            }
            var brandName = $('.js-addbrand-name').val();
            var goodsBrand = {brandName: brandName };
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


        //返回按钮
        doc.on('click', '.js-return', function () {
            util.goBack()
        });

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

        // 保存按钮
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

            // 表单数据
            var goodsVo = fd.get('#formData',true);
            // 配件ID
            goodsVo.id = $('input[name="id"]').val();
            // 配件编号
            goodsVo.goodsSn = $('.goodsSn').text();
            goodsVo.cat2Name = $('input[name="cat2Name"]').val();
            goodsVo.catId = $('input[name="catId"]').val();
            goodsVo.stdCatId = $('input[name="stdCatId"]').val();
            goodsVo.goodsTag = $('input[name="goodsTag"]').val();
            goodsVo.tqmallStatus = $("input[name='tqmallStatus']").val();
            goodsVo.goodsStatus = '1';
            goodsVo.origin = "";

            goodsVo.onsaleStatus = $('input[name="onsaleStatus"]').val();
            // 使用部位
            goodsVo.partUsedTo = $('input[name="partUsedTo"]').val();

            // 品牌
            goodsVo.brandId = $('input[name="brandId"]').val();
            goodsVo.brandName = $('input[name="brandName"]').val();

            // 配件为通用件
            if ($('.js-general-yes').is(':checked')) {
                goodsVo.carInfo = '0'
            }

            // 适用车型
            var goodsCarList = [];
            if ($('.js-general-no').is(':checked')) {
                $('.js-car-list').each(function () {
                    if (!($('.cartype-content-box').is(':hidden'))
                        && (
                            $.trim($(this).find('input[name="carInfo"]').val()) != ''
                            || $.trim($(this).find('input[name="carAlias"]').val()) != ''
                        )
                    ) {
                        var carList = fd.get($(this));
                        goodsCarList.push(carList)
                    }
                });
            }
            if (goodsCarList.length > 0) {
                goodsVo['goodsCarList'] = JSON.stringify(goodsCarList).toString();
            }

            // 自定义属性
            var goodsAttrRelList = [];
            $("[name='attrName']").each(function (index, element) {
                var attrValue = $(this).parents(".rel-list").find("[name='attrValue']");
                var attrId = $(this).parents(".rel-list").find("[name='attrId']");
                if (attrValue.val() != "") {
                    if (attrValue.type == "select") {
                        goodsAttrRelList.push({
                            "attrName": $(this).val(),
                            "attrValue": attrValue.val(),
                            "tqmallStatus": 1,
                            "tqmallGoodsAttrId": attrId.val()
                        });
                    } else {
                        goodsAttrRelList.push({
                            "attrName": $(this).val(),
                            "attrValue": attrValue.val(),
                            "tqmallStatus": 2,
                            "tqmallGoodsAttrId": 0
                        });
                    }
                }
            });
            if (goodsAttrRelList.length > 0) {
                goodsVo['goodsAttrRelList'] = JSON.stringify(goodsAttrRelList).toString();
            }

            // 图片地址
            goodsVo.imgUrl = $('input[name="imgUrl"]').val();

            $.ajax({
                type: 'POST',
                data: goodsVo,
                url: BASE_PATH + '/shop/goods/common/update'
            }).done(function (result) {
                if (result.success) {
                    dg.success('编辑成功', function () {
                        window.location.href = BASE_PATH + '/shop/goods/goods-list';
                    });
                } else {
                    dg.fail(result["errorMsg"]);
                }
            });
        });


        function carTypeLength() {
            var carListLen = $('.js-car-list').length;
            if (carListLen <= 1) {
                $('.js-car-list').find('.js-remove-carType').hide();
            } else {
                $('.js-car-list').eq(0).find('.js-remove-carType').show();
            }
        }
        carTypeLength();

        function customAttributes() {
            var listLen = $('.rel-list').length;
            if (listLen <= 1) {
                $('.rel-list').find('.js-remove-custom-brand').hide();
            } else {
                $('.rel-list').eq(0).find('.js-remove-custom-brand').show();
            }
        }

        function setStandardGoodsAttrList() {
            if($('input[name="customCat"]').val() != 'false') {
                return;
            }
            var typeId = $('input[name="stdCatId"]').eq(0).val();

            $.ajax({
                url:BASE_PATH + "/shop/goods_attributes/get_by_cat_ids",
                data:{'catIds': typeId}
            }).done(function(json){
                if(json && json.success){
                    var attrList = json.data;
                    var t;
                    $("#dynamicAttr").find('.rel-list').each(function () {
                        var isList = false;
                        var name = $.trim(
                            $(this).find('input[name="attrName"]').val()
                        );

                        for(var i in attrList) {
                            if(attrList[i].attrName == name) {
                                t = at('attrSelectList', {attrs: attrList[i].attrValueList});
                                $(this).find('.js-add-val').after(t)
                                    .after('<span class="fa icon-angle-down icon-small"></span>');

                                isList = true;
                                break;
                            }
                        }

                        if(!isList) {
                            $(this).find('.js-add-val').removeClass('js-add-val').prop('readonly', false)
                                .off('click.sel')
                                .end().find('.yqx-select-options').remove()
                        }
                    });

                } else {
                    $("#dynamicAttr").find('.rel-list').each(function () {
                        $(this).find('.js-add-val').removeClass('js-add-val').prop('readonly', false)
                            .off('click.sel')
                            .end().find('.yqx-select-options').remove();
                    });
                    dg.fail('获取分类属性列表失败.');
                }
            });
        }

        customAttributes();
    });
});
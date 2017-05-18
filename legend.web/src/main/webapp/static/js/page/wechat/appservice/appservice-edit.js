/**
 *
 * Created by ende on 16/6/15.
 */

$(function () {
    var imgUpload = true;
    seajs.use(['formData', 'dialog', 'upload', 'select', 'check', 'downlist'], function (formData, dialog, upload, select, check, dl) {
        check.init();
        // 价格显示方式
        select.init({
            dom: '.js-price-type',
            showKey: 'key',
            showValue: 'name',
            data: [{
                key: 1,
                name: '正常价格数值显示'
            }, {
                key: 2,
                name: '到店洽谈'
            }, {
                key: 3,
                name: '免费'
            }],
            callback: function(key){
                inputReadonly(key);
            }

        });
        //选择服务
        dl.init({
            url: BASE_PATH + '/shop/wechat/appservice/op/get-can-add-appService-list',
            dom: '.js-choose-service',
            showKey: 'name',
            searchKey: 'serviceName',
            hiddenKey: 'id',
            hasTitle: false,
            hasInput: false,
            callbackFn: function($input, itemData, $scope){
                $('#serviceId').val(itemData.id);
                //车主服务类型
                if(itemData.appCateId > 0){
                    $('#appCateId').val(itemData.appCateId);
                    $('.js-service-type').val(itemData.appCateName);
                    //服务图片
                    $.ajax({
                        url: BASE_PATH + '/shop/shop_service_cate/appCateName',
                        data: JSON.stringify({appCateId: itemData.appCateId}),
                        type: 'POST',
                        dataType: 'json',
                        contentType: 'application/json',
                        success: function (json) {
                            if (json && json.success) {
                                setServiceImg(json.data[0].defaultImgUrl);
                            }
                        }
                    });
                }

                //是否套餐处理
                var isSuit = itemData.suiteNum == 0 ? 0 : 1;
                $('#isSuit').val(isSuit);
                if (isSuit == 1) {
                    $('#servicePrice, #marketPrice').attr('readonly', true);
                } else {
                    $('#servicePrice, #marketPrice').attr('readonly', false);
                }

                //价格显示方式
                var priceType = itemData.priceType;
                $('#priceType').val(priceType);
                $('.js-price-type').val(['','正常价格数值显示','到店洽谈','免费'][priceType]);
                inputReadonly(priceType);

                //市场价
                $('#marketPrice').val(itemData.marketPrice || 0);
                $('#servicePrice').val(itemData.servicePrice || 0);
                $('#downPayment').val(itemData.downPayment || 0);
            }
        });


        //车主服务类型
        select.init({
            dom: '.js-service-type',
            url: BASE_PATH + '/shop/shop_service_cate/appCateName',
            showKey: 'id',
            showValue: 'name',
            callback: function (key, value, data,index) {
                setServiceImg(data[index].defaultImgUrl);
            }
        });
        var marketPrice = function (val) {
            var msg = [
                "",
                "市场价不能小于销售价"
            ];
            if (val < +$('#servicePrice').val()) {
                return {msg: msg[1], result: false};
            } else {
                return {msg: msg[0], result: true};
            }
        };

        check.helper('marketPrice', marketPrice);

        $(".img-box").sortable({axis: "y" , cursor: "move" ,opacity: 0.8, scroll:false}).disableSelection();

        // 保存
        $('.js-save').on('click', function () {
            if($('.img-item:visible').size() > 0){
                $('.textarea-content').data('vType','');
            }

            if(!check.check('.serviceForm')) {
                return;
            }
            if($('#service-img').attr('src') == ''){
                dialog.msg('服务图片不能为空');
                return;
            }
            var data = formData.get('.serviceForm', true);
            var imgList = [], serviceList = [];
            $('.img-item:visible').each(function(){
                imgList.push({'imgUrl': $(this).find($('.img-path')).val()});
            });
            data.thirdServiceInfo = JSON.stringify(imgList);
            serviceList.push(data);

            $.ajax({
                url: BASE_PATH + '/shop/wechat/appservice/op/save-appServiceList',
                data: JSON.stringify(serviceList),
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                success: function (json) {
                    if (json && json.success) {
                        dialog.success('保存成功', function () {
                            location.href = BASE_PATH + '/shop/wechat/appservice/list';
                        });
                    } else {
                        dialog.fail(json.errorMsg || '保存失败');
                    }
                },
                error: function () {
                    dialog.fail('保存失败');
                }
            });
        });

        // 文件上传与显示
        upload.init({
            dom: '.js-service-img',
            url: BASE_PATH + '/index/oss/upload_img_limit',
            fileSizeLimit:1024*1024*5,//5M
            callback: function (result) {
                var path = result.data.original;
                $('#service-img').show().attr('src', path).next().val(path);
                if($('#service-img').data('default')) {
                    $('.js-default-img').show();
                }
            }
        });
        upload.init({
            dom: '.js-decoration-img',
            url: BASE_PATH + '/index/oss/upload_img_limit',
            fileSizeLimit:1024*1024*5,//5M
            callback: function (result) {
                var path = result.data.original;
                $('.img-item:hidden').clone().insertBefore('.img-item:hidden').show()
                    .find('.image').attr('src', path).next().val(path);
                checkImgNum();
            }
        });
        // 点击显示完整图片
        $(document)
            .on('click', '.image', util.imgZoomBigger)
            //删除图片
            .on('click', '.img-delete', function () {
                $(this).closest('.img-item').remove();
                checkImgNum();
            })
            //拖拽按钮显示
            .on(
            {
                'mouseover': function () {
                    $(this).find('.arrows').addClass('current');
                },
                'mouseout': function () {
                    $(this).find('.arrows').removeClass('current');
                }
            },
            '.img-item'
        )
            //默认图片
            .on('click', '.js-default-img', function(){
                var serviceImg = $('#service-img');
                serviceImg.attr('src', serviceImg.data('default'));
                $(this).hide();
            });

        //服务说明图片上传
        $('.js-decoration-img').on('click', function(e){
            if(!imgUpload){
                e.stopPropagation();
                e.preventDefault();
                dialog.msg('最多上传10张图片');
            }
        })
    });
    $('.js-back').on('click', function(){
        history.go(-1);
    });

    function checkImgNum(){
        if($('.img-item:visible').size() == 10){
            imgUpload = false;
        }else{
            imgUpload = true;
        }
    }
    //选择价格显示方式之后控制只读
    function inputReadonly(key){
        if(key == 2 || key == 3){
            $('#servicePrice, #marketPrice').attr('readonly', true);
        }else if(key == 1 && $('#isSuit').val() == '0'){
            if($('#marketPrice').data('disable') != true) {
                $('#marketPrice').attr('readonly', false);
            }
            if($('#servicePrice').data('disable') != true){
                $('#servicePrice').attr('readonly', false);
            }
        }
    }

    //选中车主服务类型后设置服务图片
    function setServiceImg(defaultImgUrl){
        var serviceImg = $('#service-img');
        if(!serviceImg.data('default')){
            serviceImg.data('default', defaultImgUrl);
        }
        serviceImg.attr('src', defaultImgUrl).next().val(defaultImgUrl);
        if(defaultImgUrl != ''){
            $('#service-img').show();
        }else{
            $('#service-img').hide();
        }
    }
    //清空输入
    function clearInput(){
        $('.js-clear').val('');
        $('.image').attr('src', '');
        $('.js-default-img').hide();
        $('.img-item:visible').remove();
        $('#service-img').data('default', '').hide();
        $('#marketPrice, #servicePrice').data('disable', '');
    }
});

$(function () {
    var doc = $(document);
    seajs.use([
        'art',
        'check',
        'select',
        'dialog',
        'upload',
        'js/module/drag',
        'formData'
    ], function (at, ck, st, dg, upload, drag, fd) {
        var _serviceSuite = {};
        var nameRepeat = $.trim($('.js-name').val());
        var serviceRet = $('#service_note').val();
        ck.init();

        drag('picture-box', 'img-group', function (dragged) {
            var first = $(this);
            // 获取的元素可能是里面的子元素，因次需要判断
            dragged = !dragged.hasClass('picture-box') ? dragged.parents('.picture-box') : dragged;
            first = !first.hasClass('picture-box') ? first.parents('.picture-box') : first;

            var t = dragged.parent().find('.picture-box');
            var index = [t.index(first), t.index(dragged)];

            t.eq(index[1]).before(first);
            // 位置改变，重新获取
            t = dragged.parent().find('.picture-box');

            if(t.eq(index[0] - 1)[0] != dragged[0]) {
                t.eq(index[0]).before(dragged);
            }
        });

        //服务类别
        st.init({
            dom: '.js-service-type',
            url: BASE_PATH + '/shop/shop_service_cate/get_by_name',
            showKey: "id",
            showValue: 'name',
            canInput: true,
            callback: function (showKey, showValue, data, i) {
                if (data) {
                    var cateTag = data[i].cateTag;
                    $('input[name="cateTag"]').val(cateTag);
                    if (cateTag == 5 ) {
                        $('.item-group').removeClass("hide");
                    } else {
                        $('.item-group').addClass("hide");
                    }
                }
            }
        });

        //车辆级别
        st.init({
            dom: '.js-car-level',
            url: BASE_PATH + '/shop/car_level/get_car_level_by_name',
            showKey: "id",
            showValue: "name",
            // 能否输入
            canInput: true
        });

        doc.on('blur','input[name="serviceNum"]',function(){
            //计算价格
            calcGoodsPrice();
        });

        doc.on('blur','input[name="goodsNum"]',function(){
            //计算价格
            calcGoodsPrice();
        });

        doc.on('blur','input[name="servicePrice"]',function(){
            //计算价格
            calcGoodsPrice();
        });

        // 添加基本服务
        getService({
            allowRepeat: false,
            // 点击按钮的选择器
            dom: '.js-get-service',
            ajaxData: {
                data: {
                    suiteNumLT: 2
                }
            },
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
                            if (packageJson.success && packageJson.data) {
                                var service = packageJson.data.shopServiceInfoList[0];
                                var goods   = packageJson.data.goodsList;
                                
                                $('#orderServiceTB').append( at("serviceTpl", {json: [service]}) );
                                $('#orderGoodsTB').append( at("goodsTpl", {json: goods}) );
                                
                                _serviceSuite[service.id] = goods.map(function (e) {
                                    return e.id;
                                });

                            }
                        }
                    });
                } else {
                    var html = at("serviceTpl", {json: [json]});

                    $('#orderServiceTB').append(html);
                }
                // 计算价格
                calcGoodsPrice();
            }
        });

        $(document).on('blur','.js-name',function(){
            var suiteNum = $('input[name="suiteNum"]').val();
            var name = $.trim($('.js-name').val());

        });

        // 添加物料
        addGoodsInit({
            notAllowRepeat: true,
            dom: '#goodsAddBtn',
            callback: function (json) {
                var goodsJsonArray = [];
                goodsJsonArray[0] = json;
                var html = at("goodsTpl", {json: goodsJsonArray});
                $('#orderGoodsTB').append(html);
                // 计算价格
                calcGoodsPrice();
            }
        });

        function getPartDescription(id) {
            var $e = null;
            var ret = '';
            if(typeof id == 'string' || typeof id == 'number') {
                $e = $('.goods-datatr[data-id="' + id + '"]');
            } else if(id instanceof jQuery) {
                $e = id;
            }
            // 物料名称X物料数量
            if($e && $e.length)
                ret = $e.find('.js-parts-name').val() + 'x' + $e.find('input[name="goodsNum"]').val();

            return ret;
        }



        //删除行
        doc.on('click', '.js-trdel', function () {
            var currentRow = $(this).parents('tr');

            // 配件物料 新增/删除 ：
            // IF(物料删除 AND 出库数量 !=0) THEN 不可以删除
            var outNumber = $('input[name="outNumber"]', currentRow).val();
            if (typeof(outNumber) != "undefined"
                && parseInt(outNumber) != 0) {
                dg.warn("该配件已出库，不能删除!");
                return false;
            }
            if (currentRow.find('.input-suiteNum').eq(0).val() > 0) {
                dg.warn("此服务套餐包含配件物料，请自行删除！");
            }
            currentRow.remove();
            // 计算价格
            calcGoodsPrice();

        })
            .on('blur', 'input[name="goodsNum"]', function () {
            });

        // 图片上传
        upload.init({
            dom: '#fileBtn',
            url: BASE_PATH + '/index/oss/upload_img_limit',
            fileSizeLimit: 1024 * 1024 * 5,//5M
            callback: function (result) {
                var path = result.data.original;
                $('#service-img').attr('src', path);
                $('.default-hide').show();
                $('.upload-text').text('重新上传');
            }
        });

        // 图片上传（第二部分）
        upload.init({
            dom: '#fileBtn1',
            url: BASE_PATH + '/index/oss/upload_img_limit',
            fileSizeLimit: 1024 * 1024 * 5,//5M
            callback: function (result) {
                if ($('.img-item:visible').size() < 10) {
                    var path = result.data.original;
                    var $e = $('.img-item:hidden').clone().insertBefore('.img-item:hidden').removeClass('hide');
                    $e.find('.service-img1').attr('src', path);
                }else{
                    dg.warn('最多上传10张图片');
                }
            }
        });

        $('#fileBtn1').on('click', function (e) {
            if ($('.img-item:visible').size() >= 10) {
                e.stopPropagation();
                dg.warn('最多上传10张图片');
               return false;
            }
        });
        //删除图片
        doc.on('click', '.js-remove-pic', function () {
            $(this).parents('.picture-box').remove();
        })
            .on('click', '.js-set-first', function () {
                $('.img-group .picture-box').eq(0).before( $(this).parents('.picture-box') );
            });


        //车主服务
        st.init({
            dom: '.js-carName',
            url: BASE_PATH + '/shop/shop_service_cate/appCateName',
            showKey: 'id',
            showValue: 'name',
            callback: function (a, b, data, i) {
                if (data) {
                    var src = data[i].defaultImgUrl;
                    $('.default-pic').val(src);
                    $('.service-img').attr('src', src);
                    $('#fileBtn').val('');
                }
            }
        });

        //推荐状态
        st.init({
            dom: '.js-recommend',
            showKey: "key",
            showValue: "value",
            data: [{
                key: '1',
                value: '推荐'
            }, {
                key: '0',
                value: '不推荐'
            }]
        });

        //价格显示方式
        st.init({
            dom: '.js-price-type',
            showKey: "key",
            showValue: "value",
            data: [{
                key: '1',
                value: '正常显示'
            }, {
                key: '2',
                value: '到店洽谈'
            }, {
                key: '3',
                value: '免费'
            }]
        });
        //设置为车主服务
        doc.on('click', 'input[name="appPublishStatus"]', function () {
            if ($(this).is(':checked')) {
                $('.owner-service').removeClass('owner-hide');
                $(this).val('1');
            } else {
                $('.owner-service').addClass('owner-hide');
                $(this).val('0');
            }
        });
        //设置默认图片
        doc.on('click', '.js-default-pic', function () {
            var src = $('.default-pic').val();
            var defaultPic = $('#service-img').data('defaultImg');
            if (defaultPic != '') {
                $('#service-img').attr('src', defaultPic);
            } else {
                $('#service-img').attr('src', src);
            }
            $(this).hide();
            $('#fileBtn').val('');
        });


        //计算商品列表价格
        function calcGoodsPrice() {
            var goodsAmountTotal = 0;
            var serviceAmountTotal = 0;
            var serviceAmount = 0;
            var goodsAmount = 0;
            var price = toFixed2($('input[name="servicePrice"]').val());
            var suiteNum = $('input[name="suiteNum"]').val();

            $('.service-list').each(function () {
                var servicePrice =  toFixed2($(this).find('input[name="servicePrice"]').val());
                var serviceNum = toFixed2($(this).find('input[name="serviceNum"]').val());
                serviceAmount = servicePrice * serviceNum;
                $(this).find('input[name="serviceAmount"]').val(serviceAmount);
                serviceAmountTotal += serviceAmount;
            });

            $('.goods-datatr').each(function () {
                var goodsPrice = toFixed2($(this).find('input[name="price"]').val());
                var goodsNum = toFixed2($(this).find('input[name="goodsNum"]').val());
                goodsAmount = goodsPrice * goodsNum;
                $(this).find('input[name="goodsAmount"]').val(goodsAmount);
                goodsAmountTotal += goodsAmount;
            });
            if (suiteNum != 2){
                $('.js-service-price').text(price);
                $('.js-parts-price').text(goodsAmountTotal.toFixed(2));
                $('.js-service-amount').text((Number(price) + goodsAmountTotal).toFixed(2));
            }else{
                $('.js-service-price').text(serviceAmountTotal.toFixed(2));
                $('.js-parts-price').text(goodsAmountTotal.toFixed(2));
                $('.js-service-amount').text((serviceAmountTotal + goodsAmountTotal).toFixed(2))
            }
        }

        //转成数字并保留两位小数
        function toFixed2(num){
            var number =  !num ? Number(0).toFixed(2) : Number(num).toFixed(2) ;
                return number;
        }

        //市场价与服务定价对比
        ck.regList.marketPriceCheck = function () {
            var marketPriceNum = Number($('.js-market-price').val());
            var serviceAmount = Number($('.js-service-amount').text());
            var msg = [
                "",
                "市场价不能小于服务定价"
            ];
            if (marketPriceNum < serviceAmount && marketPriceNum != '') {
                return {msg: msg[1], result: false};
            } else {
                return {msg: msg[0], result: true};
            }
        };


        //预付金额与服务定价对比
        ck.regList.downPaymentCheck = function () {
            var downPayment = Number($('input[name="downPayment"]').val());
            var serviceAmount = Number($('.js-service-amount').text());
            var msg = [
                "",
                "预付金额不能大于服务定价"
            ];
            if (downPayment > serviceAmount) {
                return {msg: msg[1], result: false};
            } else {
                return {msg: msg[0], result: true};
            }
        };

        //返回
        doc.on('click', '.js-goBack', function () {
            util.goBack()
        });

        //保存
        doc.on('click', '.js-save', function () {
            var val = $.trim($('.js-name').val());
            if (!ck.check()) {
                return;
            }
            if (nameRepeat == val) {
                //更新
                doSave();
            } else {
                //新增
                //.名称校验
                $.ajax({
                    url:BASE_PATH + '/shop/shop_service_info/checkServiceName',
                    data:{
                        serviceName:val
                    },
                    success:function(result) {
                        if (result.success != true) {
                            dialog.fail(result.message);
                            return;
                        } else {
                            if (!result.data) {
                                dg.fail('名称已存在');
                                return;
                            } else {
                                doSave();
                            }
                        }
                    }
                });
            }
        });
        //保存函数
        function doSave(){
            //服务基本信息部分
            var shopServiceInfo = fd.get('#shopServiceInfo'),
                suiteGoodsNum = $('.goods-datatr').length,
                suiteServiceNum = $('.service-list').length,
                suiteNum = $('input[name="suiteNum"]').val();

            if (suiteNum == 2 && suiteServiceNum < 1) {
                dg.fail('请选择服务');
                return;
            }

            //.单独取出serviceNote
            shopServiceInfo.serviceNote = $('textarea[name="serviceNote"]').val();

            //设置suiteNum
            if (suiteServiceNum > 0) {
                suiteNum = 2;
            } else {
                if (suiteGoodsNum > 0) {
                    suiteNum = 1;
                } else {
                    suiteNum = 0;
                }
            }
            shopServiceInfo.suiteGoodsNum = suiteGoodsNum;
            shopServiceInfo.suiteServiceNum = suiteServiceNum;
            shopServiceInfo.suiteNum = suiteNum;

            //.包含服务的套餐设置servicePrice
            if(suiteNum==2){
                shopServiceInfo.servicePrice = Number($.trim($('.js-service-price').text()));
            }

            //车主服务信息部分
            var appServiceInfo = fd.get('#appServiceInfo');
            var appPublishStatus = $('input[name="appPublishStatus"]').val();
            var imgUrl = $('.js-single-img').find('img').attr("src");
            appServiceInfo.appPublishStatus = appPublishStatus;
            appServiceInfo.imgUrl = imgUrl;
            var thirdServiceInfo = [];
            //图片组
            $('.js-picture-box').each(function () {
                if (!($(this).hasClass('hide'))) {
                    var picSrc = $(this).find('img').attr("src");
                    thirdServiceInfo.push({imgUrl: picSrc})
                }
            });
            shopServiceInfo.thirdServiceInfo = JSON.stringify(thirdServiceInfo);
            shopServiceInfo = $.extend(shopServiceInfo, appServiceInfo);
            //.服务标签部分
            if(appServiceInfo.appPublishStatus==1){
                //.发布到车主端
                if(shopServiceInfo.flags==undefined||shopServiceInfo.flags==""||shopServiceInfo.flags==null){
                    //没有标签的服务打成"CZFW"的标签
                    shopServiceInfo.flags="CZFW";
                }
            } else {
                //.不发布到车主端
                if(shopServiceInfo.flags=="CZFW"){
                    //"CZFW"的标签去掉
                    shopServiceInfo.flags="";
                }
            }

            var saveData = {shopServiceInfo: shopServiceInfo};

            //套餐部分
            if (suiteNum > 0) {
                var serviceGoodsSuite={};
                serviceGoodsSuite.suiteName = shopServiceInfo.name;

                //服务信息
                var serviceInfoList = [];
                $('.service-list').each(function(){
                    var  serviceInfo= fd.get($(this));

                    delete serviceInfo.serviceId;
                    serviceInfoList.push(serviceInfo);
                });
                serviceGoodsSuite.serviceInfo = JSON.stringify(serviceInfoList);

                //配件信息
                var goodsInfoList = [];
                $('.goods-datatr').each(function(){
                    var  goodsInfo= fd.get($(this));

                    delete goodsInfo.goodsId;
                    goodsInfoList.push(goodsInfo);
                });
                serviceGoodsSuite.goodsInfo = JSON.stringify(goodsInfoList);
                serviceGoodsSuite.goodsNumber = goodsInfoList.length;
                serviceGoodsSuite.suitePrice = Number($.trim($('.js-service-amount').text()));
                saveData.shopServiceInfo.suiteAmount = serviceGoodsSuite.suitePrice;

                //.设置id
                if($('.js-serviceGoodsSuiteId').val()!=""){
                    serviceGoodsSuite.id = $('.js-serviceGoodsSuiteId').val();
                }
                saveData.serviceGoodsSuite = serviceGoodsSuite;
            }

            $.ajax({
                type: 'post',
                url: BASE_PATH + '/shop/setting/serviceInfo/saveShopServiceInfo',
                data: JSON.stringify(saveData),
                dataType: 'json',
                contentType: "application/json",
                success: function (result) {
                    if (result.success) {
                        dg.success('保存成功', function () {
                            window.location.href = BASE_PATH + '/shop/setting/serviceInfo/serviceInfo-list';
                        });
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        }
        //控制显示
        function isDisabled(){
            var editStatus = $('input[name="editStatus"]').val();
            var scope = $('#shopServiceInfo');
            if( editStatus == 1 ){
                scope.find('input[name="name"]').prop('disabled',true);
                scope.find('input[name="categoryName"]').prop('disabled',true);
                scope.find('input[name="carLevelName"]').prop('disabled',true);
                scope.find('input[name="surfaceNum"]').prop('disabled',true);
                $('input[name="goodsNum"]').prop('disabled',true);
            }else if( editStatus == 2 ){
                $('input').prop('disabled',true);
                $('.js-save').hide();
                $('.js-trdel').removeClass('js-trdel').css('cursor','not-allowed');
            }
        }
        isDisabled();



    });
});
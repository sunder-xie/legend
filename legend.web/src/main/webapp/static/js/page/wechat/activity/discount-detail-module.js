$(function(){

    seajs.use(['dialog', 'art', 'select', 'check', 'downlist', 'formData'], function (dialog, art, select, check, dl, formData) {
        //选择服务
        dl.init({
            url: BASE_PATH + '/shop/wechat/op/qry-discount-service-list',
            dom: '.js-appcar-service-select',
            showKey: 'name',
            searchKey: 'serviceName',
            hasTitle: false,
            hasInput: false,
            callbackFn: function($input, itemData, $scope) {
                //.校验所选服务是否已经被选中过
                var isExist = false;
                $('.discount-item').find('input[name="id"]').each(function (index, element) {
                    if ($(element).val()==itemData.id){
                        isExist = true;
                        return false;
                    }
                });
                if(isExist){
                    dialog.warn('此活动已在当前活动中,请选择其它服务');
                    $('.js-appcar-service-select').val('');
                    return;
                }
                //.校验所选服务是否已经发布在活动中
                $.ajax({
                    url: BASE_PATH + '/shop/wechat/op/whether-service-in-activity',
                    data:{serviceId: itemData.id},
                    success: function (json) {
                        if (json && json.success) {
                            if(json.data){
                                dialog.warn('此活动已发布在其它活动中,请选择其它服务');
                                $('.js-appcar-service-select').val('');
                            }else{
                                $('#discountServicePop').find('input[name="marketPrice"]').val(itemData.marketPrice);
                                $('#discountServicePop').find('input[name="servicePrice"]').val(itemData.servicePrice);
                                $scope.find('input[name="shopServiceId"]').val(itemData.id);
                                $scope.find('input[name="serviceNote"]').val(itemData.serviceNote);
                                $scope.find('input[name="imgUrl"]').val(itemData.imgUrl);
                            }
                        }else{
                            dialog.fail(json.errorMsg ||'请求失败');
                        }
                    }
                })
            }
        });

        $('.js-head-choose-service').on('click', function(){
            $('#discountServicePop').show();
        });

        $(document)
            //选择车主服务
            .on('click', '.discount-service-choose-y', function(){
                if(check.check('#discountServicePop')){
                    var serviceData = formData.get('#discountServicePop', true);
                    var html = art('discountAddService', {data: serviceData});
                    $('.discount-item-collection').append(html);
                    $('#discountServicePop').hide().find('input').val('');
                }
            })
            .on('click', '.discount-service-choose-n, .discount-bounce-bg, .js-discount-pop-close', function() {
                $('#discountServicePop').hide().find('input').val('');
            })
            //参加
            .on('click', '.js-discount-join', function(){
                var $this = $(this);
                var parent = $this.closest('.js-choose-form');
                if(!check.check(parent)){
                    return;
                }

                var data = {
                    shopServiceId: parent.find('[name="id"]').val(),
                    servicePrice: parent.find('[name="servicePrice"]').val(),
                    serviceTplId: parent.find('[name="tplId"]').val(),
                    marketPrice: parent.find('[name="marketPrice"]').val(),
                    downPayment: parent.find('[name="downPayment"]').val()
                };

                $.ajax({
                    url: BASE_PATH + '/shop/wechat/op/save-shop-service-info',
                    dataType: 'json',
                    contentType: 'application/json',
                    type:'POST',
                    data: JSON.stringify(data),
                    success: function (json) {
                        if (json && json.success) {
                            $this.hide().siblings('.discount-long-button').show();
                            parent.find('[name="id"]').val(json.data.id);
                            dialog.success('保存成功');
                        }else{
                            dialog.fail(json.errorMsg ||'保存失败');
                        }
                    }
                })
            })
            //取消参加
            .on('click', '.js-discount-no-join', function(){
                $(this).hide().siblings('.discount-long-button').show();
            })
            //活动内容弹窗
            .on('click', '.js-discount-show', function(){
                var $this = $(this),
                    title = $this.find('h3').text(),
                    src = $this.find('img').attr('src'),
                    contentList = $this.find(':input[name="contentList"]').val();

                var html = art('discountContentTpl', {data: {title: title, src: src, contentList: contentList}});
                dialog.open({
                    area: ['390px', 'auto'],
                    content: html
                });
            })
            

        var thisModule = $('#moduleDiscount');
        module.moduleDiscount = {
            getModuleVo: function(){
                var serviceVoList = [];
                $('.discount-item:has(".js-discount-no-join:visible")',thisModule).each(function () {
                    serviceVoList.push(formData.get($(this)));
                });
                var moduleVo = {
                    uniqueCode: thisModule.find('[name="uniqueCode"]').val(),
                    moduleType: thisModule.find('[name="moduleType"]').val(),
                    moduleIndex: thisModule.find('[name="moduleIndex"]').val(),
                    serviceVoList: serviceVoList
                };
                return moduleVo;
            },
            checkValid: function(){
                var submitItem = $('.discount-item:has(".js-discount-no-join:visible")',thisModule);
                if(!check.check(submitItem)){
                    return false;
                }
                return true;
            },
            isEmpty: function(){
                if($('.js-discount-no-join:visible', thisModule).length){
                    return false;
                }
                return true;

            }

        }

    });
});




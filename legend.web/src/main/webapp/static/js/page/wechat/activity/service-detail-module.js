$(function(){
    var serviceDialog;

    seajs.use(['dialog', 'art', 'check'], function (dialog, art, check) {
        $(document)
            .on('click', '.js-service-show', function(){
                var $this = $(this),
                    title = $this.find('h3').text(),
                    src = $this.find('img').attr('src'),
                    price = $this.next().find(':input[name="shopPrice"]').val(),
                    contentList = $this.find(':input[name="contentList"]').val();

                var html = art('serviceContentTpl', {data: {title: title, src: src, contentList: contentList, price: price}});
                dialog.open({
                    area: ['390px', 'auto'],
                    content: html
                });
            })
            .on('click', '.js-service-join', function(){
                var $this = $(this);
                if($this.hasClass('disable')){
                    return;
                }
                var title = $this.siblings(':input[name="title"]').val();
                var defaultPrice =$this.siblings(':input[name="defaultPrice"]').val();
                if($this.siblings(':input[name="editStatus"]').val() == 1){
                    var data = {
                        title: title,
                        defaultPrice: defaultPrice,
                        confirmClass: 'service-price-confirm',
                        cancelClass: 'service-price-cancel'
                    }
                    var html = art('serviceModifyPriceTpl', {data: data});
                }else{
                    var data = {
                        title: title,
                        defaultPrice: defaultPrice,
                        content: '淘汽设置的统一售价为' + defaultPrice + '元，请确认',
                        confirmClass: 'service-price-confirm',
                        cancelClass: 'service-price-cancel'
                    }
                    var html = art('servicePriceTpl', {data: data});
                }
                serviceDialog = dialog.open({
                    area: ['420px', 'auto'],
                    content: html,
                    offset: ['30%'],
                    end: $('.service-will-join').removeClass('service-will-join')
                });
                $this.closest('.service-item').addClass('service-will-join');

             })
            .on('click', '.js-service-no-join', function(){
                var $this = $(this);
                var editStatus = $this.closest('.service-item').find('[name="editStatus"]').val();
                var defaultPrice = $this.closest('.service-item').find('[name="defaultPrice"]').val();
                var html,title;
                title = editStatus == 1? '推荐价格': '价格';
                html = title + '<strong><small>￥</small>' + defaultPrice + '</strong>';
                $this.closest('.service-item').removeClass('service-selected').find('.service-price-box').html(html);
                $this.hide().siblings('.js-service-join').show();
                serviceButtonCss();
            })
            .on('click', '.js-service-agreement', function(){
                var $this = $(this),
                checkbox = $this.prev(),
                html = $this.next().html();
                checkbox.addClass('agree-checkbox');

                serviceDialog = dialog.open({
                    area: ['900px', '520px'],
                    content: html
                })
            })

            //弹窗按钮
            .on('click', '.js-service-agree', function() {
                $('.agree-checkbox').prop('checked', true).trigger('change').removeClass('agree-checkbox');

                dialog.close(serviceDialog);
            })
            .on('click', '.service-price-confirm', function(){
                if (!check.check($(this).closest('.collection-bounce'), false)) {
                    return;
                }
                var $target = $('.service-will-join').find('.service-operate');
                var tplId = $target.find('[name="tplId"]').val();
                var instanceId = $target.find('[name="instanceId"]').val();
                var price = $('#servicePrice').val() || $(this).data('defaultPrice');
                $.ajax({
                    url: BASE_PATH + '/shop/wechat/op/save-shop-service-info',
                    dataType: 'json',
                    contentType: 'application/json',
                    type:'POST',
                    data: JSON.stringify({shopServiceId:instanceId, serviceTplId:tplId, servicePrice:price}),
                    success: function (json) {
                        if (json && json.success) {
                            dialog.success('保存成功');
                            var $target =  $('.service-will-join');
                            $target.addClass('service-selected').find('.js-service-join').hide().siblings('.js-service-no-join').show();
                            if(json && json.data){
                                json.data.id && $target.find('[name="instanceId"]').val(json.data.id);
                                json.data.servicePrice && $target.find('[name="shopPrice"]').val(json.data.servicePrice);
                                if($target.find('[name="editStatus"]').val()==1){
                                    var html = '价格'+ '<strong><small>￥</small>' + price + '</strong>';
                                    $target.find('.price-box').html(html);
                                }
                            }

                        } else {
                            dialog.fail(json.errorMsg || '保存失败');
                        }
                    },
                    error: function () {
                        dialog.fail('保存失败');
                    },
                    complete: function(){
                        dialog.close(serviceDialog);
                        serviceButtonCss();
                    }
                })
            })
            .on('click', '.service-price-cancel', function(){
                dialog.close(serviceDialog);
            })
            
        $('.service-agree-checkbox').on('change', function(){
            var button =  $(this).closest('.service-item-footer').find('.js-service-join');
            if($(this).prop('checked')){
                button.removeClass('disable');
            }else{
                button.addClass('disable');
            }
        })

    });

//控制按钮的样式
    function serviceButtonCss(){
        // $('.totalNum').text($('.service-selected').length);
        $('.service-agree-checkbox').each(function(){
            if($(this).closest('.service-item').hasClass('service-selected')){
                $(this).attr('disabled', true);
            }else{
                $(this).removeAttr('disabled');
            }
        })
    }
    
    
    var thisModule = $('#moduleService');
    module.moduleService = {
        getModuleVo : function(){
            var moduleVo = {
                uniqueCode: thisModule.find('[name="uniqueCode"]').val(),
                moduleType: thisModule.find('[name="moduleType"]').val(),
                moduleIndex: thisModule.find('[name="moduleIndex"]').val(),
                serviceVoList: $('.service-item.service-selected').map(function(){
                    return {
                        id: $(this).find('input[name="instanceId"]').val(),
                        servicePrice: $(this).find('input[name="shopPrice"]').val()
                    };
                }).get()
            };
            return moduleVo;
        },
        isEmpty: function(){
            if($('.service-item.service-selected').length){
                return false;
            }
            return true;
        }
    }

});



/**
 * create by zmx 16/12/07
 * 标准化服务
 */

$(function(){
    seajs.use([
        'dialog',
        'formData',
        'check'
    ],function(dg,fd,ck){
        dg.titleInit();
        ck.init();
        //全选处理
        select();
        //全选按钮
        $(document).on('click','.js-all-select',function(){
            var item = $(this).parents('.yqx-group-head').next('.yqx-group-content').find('.js-service-checkbox');
            if( $(this).is(':checked')){
                item.prop('checked',true);
                item.siblings('.js-serviceprice-input').removeClass('display-none')
            }else{
                item.each(function(){
                    if( !($(this).is(':disabled')) ){
                        $(this).prop('checked',false);
                        $(this).siblings('.js-serviceprice-input').addClass('display-none')
                    }
                });
            }
        });
        //单个选择（作用在li上）
        $(document).on('click','.js-service-item',function() {
            $(this).find('.js-service-checkbox').trigger('click')
        });
        //单个选择（作用在input上）
        $(document).on('click','.js-service-checkbox',function(event){
            event.stopImmediatePropagation();
            var selectItem = $(this);
            //不处理不可编辑的选项
            if( selectItem.is(':disabled')){
                return;
            }
            selectItem.siblings('.js-serviceprice-input').toggleClass('display-none');
            select();
            //不处理不可编辑的选项
            if (!(selectItem.is(':checked'))) {
                selectItem.parents('.yqx-group-content').prev('.yqx-group-head').find('.js-all-select').prop('checked', false)
            }
        });
        //金额输入阻止冒泡事件
        $(document).on('click','.js-serviceprice-input',function(event){
            event.stopImmediatePropagation();
        });

        //初始化判断
        function select(){
            $('.yqx-group-content').each(function() {
                var allLength = $(this).find('.js-service-checkbox').length,
                    selLength = $(this).find('.js-service-checkbox:checked').length;
                if (allLength == selLength) {
                    $(this).prev('.yqx-group-head').find('.js-all-select').prop('checked', true)
                }
            })
        };


        //保存
        $(document).on('click','.js-save',function(){
            if(!ck.check()){
                return;
            }
            var shopServiceInfoList = [];
            $('.js-service-item').each(function(){
                var item = $(this).find('.js-service-checkbox')
                if( item.is(':checked')){
                    var serviceItem = fd.get($(this));
                    shopServiceInfoList.push(serviceItem)
                }
            });

            $.ajax({
                method:'post',
                url:BASE_PATH +'/shop/conf/normal-service/save',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(shopServiceInfoList),
                success:function(result){
                    if(result.success){
                        dg.success('保存成功')
                    }else{
                        dg.fail(result.message);
                    }
                }
            })
        })
    })
});
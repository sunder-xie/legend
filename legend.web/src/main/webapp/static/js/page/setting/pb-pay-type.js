/*
 * create by zmx 2017/1/16
 * 付款类型页面(钣喷)
 */

$(function () {
    var doc = $(document);
    seajs.use([
        'dialog',
        'check'
    ], function (dialog, ck) {
        dialog.titleInit();
        ck.init();
        //新增付款类型
        doc.on('click', '.js-pay-btn', function () {
            $(this).next('.add-pay-type').show();
            $(this).hide();
        });
        seajs.use([
            'dialog',
            'art'
        ], function (dg, at) {
            $.ajax({
                url: BASE_PATH + '/shop/setting/pay-type/list',
                success:function(result){
                    if(result.success){
                        //变动费用
                        var changeHtml = at('changeCostTpl',{json:result});
                        $('#changeCostCon').html(changeHtml);
                        //固定费用
                        var fixedHtml = at('fixedCostTpl',{json:result});
                        $('#fixedCostCon').html(fixedHtml);
                    }else{
                        dg.fail(result.message)
                    }
                }
            });

            //更改开启状态
            doc.on('click', '.js-on-btn', function () {
                var showStatus;
                var id = $(this).parents('.on-off').data("id");
                if( $(this).hasClass('off-btn')){
                    showStatus = 1;
                    $(this).parents('.on-off').removeClass('red');
                    $(this).removeClass('off-btn').parents('.on-off').addClass('green');
                } else {
                    showStatus = 0;
                    $(this).parents('.on-off').removeClass('green');
                    $(this).addClass('off-btn').parents('.on-off').addClass('red');
                }
                $.ajax({
                    url: BASE_PATH + '/shop/setting/pay-type/change-status',
                    data: {
                        id:id,
                        showStatus:showStatus
                    },
                    success: function (result) {
                        if (!result.success) {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            });

            //保存新增付款类型
            doc.on('click', '.js-pay-save', function () {
                var name = $.trim($(this).parents('.add-pay-type').find(".pay-type-name").val());
                var costType = $(this).data('costType');
                var addPayType = $(this).parents('.add-pay-type');

                if (!ck.check()) {
                    return
                }
                $.ajax({
                    post: 'post',
                    url: BASE_PATH + '/shop/setting/pay-type/save',
                    data: {
                        name: name,
                        costType:costType
                    },
                    success: function (result) {
                        if (result.success) {
                            var html = $('#payTypeTpl').html();
                            if(costType == 1){
                                $('#changeCostCon').append(html);
                            }else if(costType == 2){
                                $('#fixedCostCon').append(html);
                            }
                            $('.add-pay-type-name').next('.on-off').data('id',result.data);
                            $('.add-pay-type-name').next('.on-off').data('showStatus',true);
                            $('.add-pay-type-name').text(name).removeClass('add-pay-type-name');
                            addPayType.find('.pay-type-name').val('');
                            addPayType.prev('.js-pay-btn').show();
                            addPayType.hide();
                        } else {
                            dg.fail(result.errorMsg)
                        }
                    }
                });
            });
        });

        //取消保存新增收款类型
        doc.on('click', '.js-pay-cancel', function () {
            $(this).parents('.add-pay-type').hide().find('.pay-type-name').val('');
            $(this).parents('.add-pay-type').prev('.js-pay-btn').show();
        });
    });
});
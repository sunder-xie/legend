/**
 * Created by zmx on 16/11/7.
 * 设置-业务类型
 */
$(function () {
    var doc = $(document);

    seajs.use([
        "ajax",
        "dialog",
        "check"
    ], function (ajax, dg,ck) {
        dg.titleInit();

        //开关
        doc.on('click','.js-on-btn',function(){
            var name = $.trim($(this).data("name"));
            var showStatus;
            if( $(this).hasClass('off-btn')){
                showStatus = 1;
                $(this).removeClass('off-btn').parents('.on-off').removeClass('background-red').addClass('background-green');
            }else{
                showStatus = 0;
                $(this).addClass('off-btn').parents('.on-off').removeClass('background-green').addClass('background-red');
            }
            var id = $.trim($(this).data("id"));
            var orderType = {
                id:id,
                name: name,
                showStatus:showStatus
            }
            $.ajax({
                type: 'POST',
                dataType: "json",
                contentType: "application/json",
                url: BASE_PATH + '/shop/setting/order-type/update',
                data: JSON.stringify(orderType),
                success: function (result) {
                    if (!result.success) {
                        dg.warn(result.errorMsg);
                        return false;
                    } else {
                    }
                }
            });

        });

        //新增收款类型
        doc.on('click','.js-pay-btn',function(){
            $('.add-pay-type').show();
            $('.js-pay-btn').hide();
        });

        //取消保存新增收款类型
        doc.on('click','.js-pay-cancel',function(){
            $('.add-pay-type').hide().find('.pay-type-name').val('');
            $('.js-pay-btn').show();
        });

        //添加业务类型
        doc.on('click','.js-btn-save',function () {
            var showStatus = 1;
            var name = $.trim($(".pay-type-name").val());
            var orderType = {
                name:name,
                showStatus:showStatus
            };
            if (!(ck.check())) {
                return false;
            }
            $.ajax({
                type: 'POST',
                dataType: "json",
                contentType: "application/json",
                url: BASE_PATH + '/shop/setting/order-type/update',
                data: JSON.stringify(orderType),
                success: function (result) {
                    if (!result.success) {
                        dg.warn(result.errorMsg);
                        return false;
                    } else {
                        //添加业务类型
                        window.location.reload();
                    }
                }
            });
        });
    });
});
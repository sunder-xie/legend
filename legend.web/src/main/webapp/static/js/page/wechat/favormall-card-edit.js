/**
 * Created by zz on 2016/9/7.
 */
seajs.use(['select','check','formData','check','dialog'], function(st,ck,formData,check,dialog){
    ck.init();
    st.init({
        dom: '.js-choose-card',
        url: BASE_PATH + '/shop/wechat/op/qry-free-memberCard-list',
        showKey: "id",
        showValue: "typeName",
        callback:function(){
            var cardTypeId = $('input[name="cardTypeId"]').val();
            var cardTypeName = $('.js-choose-card').val();
            $.ajax({
                url: BASE_PATH + "/shop/wechat/op/isExist-favormall-config",
                data: {
                    configType :1,
                    configId:cardTypeId
                },
                dataType: "json",
                success: function (result) {
                    if (result.success &&result.data) {
                        dialog.warn("会员卡["+cardTypeName+"]已经配置在卡券商城中，请选择其他会员卡，或者取消配置中的会员卡");
                        $('.js-choose-card').val('');
                        $('input[name="cardTypeId"]').val('');
                    }
                }
            });
        }
    });

    //赠送展示切换
    var isSend = $('input[name="isGiving"]');
    isSend.on('click', function(){
        var id = $('input[name="id"]').val();
        var radioVal = $('input[name="isGiving"]:checked').val();
        var setting = $('.card-setting');
        if(radioVal == 0){
            if (id != '') {
                dialog.confirm('修改后将无法继续赠送会员卡啦', function () {
                    setting.hide();
                }, function () {
                    isSend.eq(0).prop('checked', 'true');
                });
            } else {
                setting.hide();
            }
        }else{
            setting.show();
        }
    });

    //保存修改
    $(document).on('click', '.js-save', function () {
        var data = formData.get('.js-card-config', true);
        if (!check.check(null, false)) {
            return;
        }
        var givingAmountOld = $('input[name="givingAmountOld"]').val();
        var givingNumberNew = $('input[name="givingNumber"]').val();
        var receiveNumber = $('input[name="receiveNumber"]').val();
        var isGiving = $('input[name="isGiving"]:checked').val();
        if(isGiving ==1){
            data.givingStatus = 1;
            if(parseInt(givingNumberNew) <= parseInt(receiveNumber)){
                dialog.fail('新赠送数量必须大于已领取数量['+receiveNumber+']');
                return;
            }
        } else{
            if(givingAmountOld==null||givingAmountOld==''){
                data.givingAmount = null;
            }
            data.givingStatus = 0;
        }
        data.isCanceled = 0;
        save(data);
    });
    //取消赠送
    $(document).on('click', '.js-cancel', function () {
        var data = formData.get('.js-card-config', true);
        if (!check.check(null, false)) {
            return;
        }
        data.isCanceled = 1;
        data.givingStatus = 3;
        save(data);
    });

    function save(param) {
        $.ajax({
            type: "POST",
            contentType: 'application/json',
            url: BASE_PATH + "/shop/wechat/op/save-favormall-card",
            data: JSON.stringify(param),
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    dialog.success('操作成功', function () {
                        location.href = BASE_PATH + '/shop/wechat/favormall-list';
                    });
                } else {
                    dialog.fail(result.message);
                }
            }
        });
    }
});
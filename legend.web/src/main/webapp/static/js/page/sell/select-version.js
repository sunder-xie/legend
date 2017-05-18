$(function () {
    seajs.use([
        'dialog',
        'art'
    ], function (dg, at) {
        var dialogBox = null;
        var protocolNum;

        initialize();

        function initialize() {
            var mobile = $('input[name="mobile"]').val();
            $.ajax({
                url: BASE_PATH + '/portal/shop/sell/sell-shop-level/list',
                data:{
                    mobile:mobile
                },
                success: function (result) {
                    if (result.success) {
                        var html = at('versionTpl', {json: result});
                        $('#versionBox').html(html);
                        $('.js-version').eq(1).addClass('version-wrap-hover');
                    } else {
                        dg.fail(result.message)
                    }
                }
            });
        }

        //鼠标移入
        $(document).on('mousemove', '.js-version', function (ev) {
            ev.stopPropagation();
            $(this).addClass('version-wrap-hover').siblings().removeClass('version-wrap-hover')
        });

        //查看协议
        $(document).on('click', '.js-protocol', function () {
            var html = $('#protocolDialog').html();
            protocolNum = $(this).data('shopLevel');
            dialogBox = dg.open({
                area: ['810px', '470px'],
                content: html
            });
        });
        //立即购买
        $(document).on('click', '.js-buy', function () {
            var shopLevel = $(this).data('shopLevel');
            var mobile = $('input[name="mobile"]').val();
            $.ajax({
                url: BASE_PATH + '/portal/shop/sell/sell-shop-level/detail',
                data: {
                    shopLevel: shopLevel,
                    mobile:mobile
                },
                success: function (result) {
                    if (result.success) {
                        var html = $('#buyDialog').html();
                        dg.open({
                            area: ['315px', '350px'],
                            content: html
                        });
                        $('.edition').find('.name').text(result.data.name);
                        $('.edition').find('.price').text(result.data.price);
                        $('.edition').find('.effectiveStr').text(result.data.effectiveStr);
                        $('.edition').find('.shopLevel').val(shopLevel);
                    } else {
                        dg.fail(result.message)
                    }
                }

            });
        });

        //同意协议
        $(document).on('click', '.js-agree', function () {
            var $this = $(this).parents('.js-version').siblings();
            if ($(this).is(':checked')) {
                $this.find('.js-agree').prop('checked', false);
                $this.find('.buy-btn').prop('disabled', true).removeClass('buy-hover');
                $(this).parents('.js-version').find('.buy-btn').prop('disabled', false).addClass('buy-hover');
            } else {
                $(this).prop('checked', false);
                $(this).parents('.btn-box').find('.buy-btn').prop('disabled', true).removeClass('buy-hover');
            }
        });

        //立即支付
        $(document).on('click', '.js-pay', function () {
            var shopLevel = $('.shopLevel').val();
            var sellAmount = $('.price').text();
            var data = {
                shopLevel: shopLevel,
                sellAmount:sellAmount
            };
            $.ajax({
                type: 'post',
                url: BASE_PATH + '/portal/shop/sell/sell-order/save',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: "application/json",
                success: function (result) {
                    if (result.success) {
                        var orderSn = result.data.sellOrderSn;
                        var payMethod = 1;
                        var source = result.data.source;
                        window.location.href = BASE_PATH + '/onlinepay/pay-card-route?orderSn=' + orderSn + '&payMethod=' + payMethod + '&source=' + source;
                    } else {
                        dg.fail(result.message)
                    }
                }
            })
        });

        $(document).on('click','.js-join',function(){
            $('.js-agree').each(function(){
                var thisNum = $(this).next().data('shopLevel');
                if(thisNum == protocolNum){
                    var $this = $(this).parents('.js-version').siblings();
                    $(this).prop('checked',true);
                    $this.find('.js-agree').prop('checked', false);
                    $this.find('.buy-btn').prop('disabled', true).removeClass('buy-hover');
                    $(this).parents('.js-version').find('.buy-btn').prop('disabled', false).addClass('buy-hover');
                }
            });
            dg.close(dialogBox)

        })

    });
});
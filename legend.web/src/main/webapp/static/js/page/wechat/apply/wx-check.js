
$(function() {
    var refreshCountLocalKey = "shop.wechat.grant.refreshCount";
    seajs.use(['ajax','dialog','art'],
        function (aj,dialog,at) {
            var grantConfirm = null;
            $('.js-grant').on('click', function() {
                var html = at('grantConfirmTpl', {});
                grantConfirm = dialog.open({
                    area: ['400px', 'auto'],
                    offset: '20%',
                    content: html
                });
            });

            // 取消
            $(document).on('click', '.js-go-cancel', function () {
                dialog.close(grantConfirm);
            });

            // 确认
            $(document).on('click', '.js-go-confirm', function () {
                localStorage.setItem(refreshCountLocalKey,0);
                //打开新标签操作置于ajax外,防止被浏览器拦截
                var newTab=window.open('about:blank');
                $.ajax({
                    url: BASE_PATH + '/shop/wechat/op/get-grant-url',
                    dataType: 'json',
                    success: function(json) {
                        if(json && json.success) {
                            $('.affirm').show().siblings().hide();
                            newTab.location.href=json.data;
                        } else {
                            dialog.fail(json.errorMsg || '获取授权地址失败');
                        }
                    },
                    error: function() {
                        dialog.fail('获取授权地址失败');
                    }
                });
                dialog.close(grantConfirm)
            });
        });

    $('.js-yes, .js-no, .js-refresh').on('click',function(){
        location.href = BASE_PATH + '/shop/wechat';
    })
});

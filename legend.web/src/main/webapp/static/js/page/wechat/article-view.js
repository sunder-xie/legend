/**
 * Created by ende on 16/6/16.
 */
$(function () {
    var back = BASE_PATH + '/shop/wechat/article-list';
    var id = $('input[name="shopAricleRelId"]').val();
    var click = true,
        url = $('#viewUrl').val();

    //Render in canvas
    $("#qrcodeView div").qrcode({
        width: 128,
        height: 128,
        text: url
    });

    $('#qrcodeView img').attr('title', $('#articleTitle').val());

    seajs.use(['dialog', 'ajax'], function (dialog) {
        $('.js-send').on('click', function () {
            if(!click) {
                return;
            }
            dialog.confirm('每个用户，每个月，只能接受4篇文章，确定发送文章么？', function(){
                //确定
                click = false;
                $.ajax({
                    url: BASE_PATH + '/shop/wechat/op/send-article',
                    data: {
                        shopAricleRelId: id
                    },
                    type: 'POST',
                    success: function (json) {
                        if (json && json.success) {
                            dialog.success('发送成功', function () {
                                location.href = back;
                                click = true;
                            });
                        } else {
                            dialog.fail(json.errorMsg || '发送失败');
                            click = true;
                        }
                    },
                    error: function () {
                        dialog.fail('发送失败');
                        click = true;
                    }
                }); // ajax
            });

        });

        $('.js-back').on('click', function() {
            history.back(-1);
        });
    });
});
/**
 * Created by huage on 16/9/2.
 */
$(function () {
    addHeadStyle(4);
    seajs.use(["art", 'dialog',"qrCode"],function (at, dg) {
        var sn = $('#orderSn').val()||'', rule, codeUrl = 'http://' + location.host + '/legend/insurance/anxin/flow/mobile/upload-img?orderSn={0}'.format(sn),
            queryUrl = '/legend/insurance/anxin/flow/loop-confirm?orderSn={0}'.format(sn),
            confirmUrl = '/legend/insurance/anxin/flow/upload-confirm?orderSn={0}'.format(sn), ok = true;
            $('.QR_code').qrcode({
                width: 190,
                height: 190,
                text: codeUrl
            });
        rule = setInterval(function() {
            var xhr =new XMLHttpRequest();
            xhr.onreadystatechange = function() {
                var res = xhr, data;
                if(res.readyState == 4 && res.status == 200) {
                    data = JSON.parse(res.responseText);
                    if(data.success && data.data) {
                        if(!ok) return;
                        ok = false;
                        clearInterval(rule);
                        $.get(confirmUrl)
                            .done(function(result) {
                                if(result.success && result.data) {
                                    location.href = '/legend/insurance/anxin/pay/choose?sn={0}'.format(sn);
                                } else {
                                    dg.warn('上传失败,请重新上传');
                                }
                            })
                    }
                }
            };
            xhr.open('get', queryUrl, true);
            xhr.send(null);
        }, 1000);
    });
});
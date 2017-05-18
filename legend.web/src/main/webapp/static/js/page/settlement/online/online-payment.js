/**
 * zsy  20160-10-18
 * 线上支付
 */

$(function () {
    var base_url = BASE_PATH + '/shop/settlement/online';
    var canApply = true;

    seajs.use(['ajax', 'dialog'], function (ajax, dialog) {
        var $process = $('.process');
        $.getJSON(base_url + '/apply-record', function (result) {
            if(result.success) {
                if(result.data && typeof result.data == 'object') {
                    switch (result.data.applyStatus) {
                        // 中断或未申请
                        case 2:
                            break;
                        // 申请中
                        case 0:
                        case 1:
                        case 3:
                            $('.btn-apply').text('申请中...').addClass('in-apply');
                            $process.eq(0).addClass('current-process');
                            canApply = false;
                            break;
                        // 申请成功
                        case 4:
                            $('.apply-info').addClass('hide');
                            $('#wechatAccount').text(result.data.applyAccount);
                            $('.apply-success').removeClass('hide');
                            $process.addClass('current-process');
                            canApply = false;
                            break;
                        default:
                            canApply = false;
                            break;
                    }
                }
            }
            $('.js-apply').click(setApplyEvent);
        });

        function setApplyEvent() {
            if(canApply) {
                $.post(base_url + '/apply-pay', function (result) {
                    if(result.success) {
                        dialog.success('您的申请已提交');
                        $('.btn-apply').text('申请中...').addClass('in-apply');
                        $process.eq(0).addClass('current-process');
                        canApply = false;
                    } else {
                        if(result.code == '20028000') {
                            dialog.confirm(result.message || '您尚未开通微信公众号，请先申请开通微信公众号后再申请开通微信支付。',
                                null,
                                function () {
                                    window.location.href = BASE_PATH + '/shop/wechat?refer=online-pay'
                                },
                                ['取消', '申请开通微信公众号']
                            );
                        } else if(result.code == '20028001') {
                            dialog.msg(result.message || '您的微信公众号申请正在处理中，待微信公众号开通成功后再申请微信支付。')
                        } else {
                            dialog.fail(result.errorMsg || result.message || '申请失败');
                        }
                    }
                });
            } else {
                dialog.warn('您已申请，请不要重复申请');
            }
        }

    });
});
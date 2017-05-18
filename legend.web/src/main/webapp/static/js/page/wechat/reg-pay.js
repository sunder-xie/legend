/**
 *
 * Created by ende on 16/6/12.
 */
$(function () {
    var eles = [
        $('input[name="payType"]'),
        $('input[name="payTplId"]'),
        $('#agree')
    ];

    seajs.use(['ajax', 'upload', 'layer'], function (ajax, upload, layer) {
        var imgOuterHTMl = '<div class="file-show">' +
            '<img class="btn-upload-img">' +
            '<i class="file-del js-file-del" title="删除"></i>' +
            '</div>';
        upload.init({
            dom: '.js-file',
            url: BASE_PATH + '/index/oss/upload_img_limit',
            callback: function (result) {
                var path = result.data.original;
                var img = $(this).parent().parent().find('.file-show');

                if (!img.length) {
                    img = $(imgOuterHTMl);
                }

                img.find('img').attr('src', path);

                $('.btn-upload').hide();
                $(this).parent().after(img);
                $(this).parent().find('.img-path').val(path);
            }
        });

        $(document).on('click', 'img', function() {
            var src = $(this).attr('src');
            layer.open({
                type: 1,
                title: false,
                closeBtn: 1,
                area: '516px',
                skin: 'layui-layer-nobg', //没有背景色
                shadeClose: true,
                content: '<img src=' + src + '>'
            });
        });
    });

    // 图片删除
    $(document).on('click', '.js-file-del', function () {
        var $fileset = $(this).parents('fieldset');

        $fileset.find('.img-path').val('');
        $(this).parent().remove();

        $('.btn-upload').show();
    });

    // 保存与下一步
    $('.js-next').on('click', function (e) {
        // 未同意协议时不能点击下一步
        // form 表单提交 action
        if (!$('.agree').prop('checked')) {
            e.preventDefault();
        }
    });

    // 协议相关的
    $('.wechat-agreement-back, .js-agreement-close').on('click', function () {
        $('.wechat-agreement-box, .wechat-agreement-back').addClass('hide');
    });

    $('.js-agreement').on('click', function () {
        $('.wechat-agreement-box, .wechat-agreement-back').removeClass('hide');
    });

    $('.js-agree-agreement').on('click', function () {
        $('.agree').prop('checked', 'checked');
        if (!canSubmit()) {
            $('.js-next').attr('disabled', 'disabled');
        } else {
            $('.js-next').removeAttr('disabled');
        }
        $('.wechat-agreement-box, .wechat-agreement-back').addClass('hide');
    });

    $('.agree').on('click', function () {
        var t = $(this).prop('checked');

        if (!canSubmit()) {
            $('.js-next').attr('disabled', 'disabled');
        } else {
            $('.js-next').removeAttr('disabled');
        }
    });
    // 协议相关的 end

    eles.forEach(function (e) {
        e.on('click', function () {
            var target, name = $(this).attr('name');
            if (name === 'payType') {
                target = $(this).data('target');

                $('.type-current').removeClass('type-current').addClass('hide');
                $(target).removeClass('hide').addClass('type-current');

                // 上传支付凭证显示、隐藏
                if(target === '.offline') {
                    $('.file-box').removeClass('hide');
                } else {
                    $('.file-box').addClass('hide');
                }
            } else if(name === 'payTplId') {
                $('.pay-selected').removeClass('pay-selected');
                $(this).next().addClass('pay-selected');
            }
            if (canSubmit()) {
                $('.js-next').removeAttr('disabled');
            }
        });
    });

    function canSubmit() {
        return eles.every(function (e) {
            if (e.parent().find('input:checked').val() == null) {
                return false;
            }
            return true;
        });
    }

});

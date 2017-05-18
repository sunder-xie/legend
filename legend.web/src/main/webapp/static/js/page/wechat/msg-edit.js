/**
 *
 * Created by ende on 16/6/15.
 */

$(function () {
    var url = BASE_PATH + '/shop/wechat/op/save-replymsg';
    var id  = util.getPara('id');
    var imgOuterHTMl = '<div class="file-show">' +
        '<img class="btn-upload-img">' +
        '<i class="file-del js-file-del" title="删除"></i>' +
        '</div>';
    var deleteBtn = '<i class="tag-btn js-tag-subbtn btn-cancel">删除</i>';
    var tagHTML = '<div class="info-tag js-tag-edit replyKeyword">'
        + '<i></i>'
        + '</div>';
    var questions = {
        '0': '设置用户消息回复后，会在粉丝给您发送微信消息时，会自动回复您设置的文字',
        '1': '设置关键词自动回复，可以通过添加规则（规则名最多为60字数），订阅用户发送的消息内如果有您设置的关键字，即可把您设置在此规则名中回复的内容自动发送给订阅用户',
        '2': '用户在关注您的公众号时，会自动回复您设置的消息'
    };

    if($('.js-hidden-status').val()==1){
        $('.keyword-box').show();
    }else{
        $('.keyword-box').hide();
    }

    if(id) {
        $('#subtitle').text('编辑回复');
    }
    $('#subtitle').removeClass('hide');

    // 提示
    $('.question-icon').attr('data-tips',
        questions[ $('input[name="replyStatus"]').val() ]);

    // 模板切换
    $('.js-type').on('click', function () {
        var target = $(this).data('target');
        var another = $(this).parent().siblings().find('input');

        another.removeAttr('checked');
        $('.type-current').addClass('hide').removeClass('type-current');
        $(target).removeClass('hide').addClass('type-current');
        if(target=='.mix-box'){
            if(this.value==1){
                $('.picture-article').show()
            }else{
                $('.picture-article').hide();
            }
        }
    });

    // dialog.tips 显示的位置有点问题
    $('.js-show-tips').hover(function () {
        var tips = $('.msg-tips').text( $(this).attr('data-tips') );

        if(+tips.css('height').replace('px', '') <= 32) {
            tips.css('top', 0);
        } else {
            tips.css('top', '-20px');
        }
        tips.removeClass('hide');
    }, function () {
        $('.msg-tips').addClass('hide');
    });

    seajs.use(['formData', 'dialog', 'upload', 'select', 'check'], function (formData, dialog, upload, select, check) {
        check.init();
        // 下拉选择
        select.init({
            dom: '.js-reply-status',
            showKey: 'key',
            showValue: 'name',
            data: [{
                key: 0,
                name: '用户消息回复'
            }, {
                key: 1,
                name: '关键字回复'
            }, {
                key: 2,
                name: '被添加回复'
            }],
            callback: function (key) {
                if(key == 1) {
                    $('.keyword-box').show();
                } else {
                    $('.keyword-box').hide();
                }
                $('.question-icon').attr('data-tips', questions[key]);

                // 控制问号的提示
            }
        });

        // 保存
        $('.js-save').on('click', function () {
            var data = formData.get('.content', true);
            var type = $('.js-type:checked').val();

            if(!check.check('.must-box')) {
                return;
            }
            if($('.keyword-box').is(':visible')){
                if(!$('.replyKeyword').length){
                    dialog.warn('关键字不能为空');
                    return;
                }
                data.replyKeywords = $('.replyKeyword').map(function(){
                    return $(this).find('i').text();
                }).get();
            }else{
                data.replyKeywords = [];
            }
            // 对不同模板的清除数据
            if(type == 0) {
                if(!check.check('.txt-box')) {
                    return;
                }
                data.articleTitle = '';
                data.picUrl = '';
                data.replyDescription = '';
                data.articleUrl = '';
            }else {
                if(!data.picUrl) {
                    dialog.warn('请添加图片');
                    return;
                }
                if(!check.check('.mix-box')) {
                    return;
                }
                if(type == 2){
                    data.replyDescription = '';
                    data.articleUrl = '';
                }
                data.replyContent = '';
            }

            // ajax 提交
            $.ajax({
                url: url,
                data: JSON.stringify(data),
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                success: function (json) {
                    if (json && json.success) {
                        dialog.success('保存成功', function () {
                            location.href = BASE_PATH + '/shop/wechat/msg-list';
                        });
                    } else {
                        dialog.fail(json.errorMsg || '保存失败');
                    }
                },
                error: function () {
                    dialog.fail('保存失败');
                }
            });
        });

        // 文件上传与显示
        upload.init({
            dom: '.js-file',
            url: BASE_PATH + '/index/oss/upload_img_limit',
            fileSizeLimit:1024*1024*2,//2M
            callback: function (result) {
                var path = result.data.original;
                var img = $(this).parent().parent().find('.file-show');

                if (!img.length) {
                    img = $(imgOuterHTMl);
                }

                img.find('img').attr('src', path);

                $(this).parent().hide();
                $(this).parent().after(img);
                $(this).parent().find('.img-path').val(path);
            }
        });
        // 点击显示完整图片
        $(document).on('click', '.file-show img', util.imgZoomBigger);

        $(document).on('click', '.js-file-del', function () {
            var $fileset = $(this).parents('fieldset');

            $fileset.find('.img-path').val('');
            $(this).parent().remove();
            $fileset.find('.btn-upload').show();
        });

        $('.js-back').on('click', function () {
            history.back(-1);
        });

        // tag add 点击
        $('#tagAdd').on('click', function(){
            if( $('.replyKeyword').size() == 10){
                dialog.warn('最多添加10个关键字');
                return;
            }
            $(this).hide();
            $('#input-box').show().find('input').focus();
        });

        // tag 添加确定按钮
        $('#tagAddBtn').on('click', function(){
            var input = $('#tag-input');
            if(!input.val()){
                dialog.warn('请输入关键字');
                return;
            }
            if(input.val().length>8){
                dialog.warn('长度不能大于8');
                return;
            }
            var newTag = $(tagHTML);
            newTag.find('i').text(input.val());
            $('#tagWrapper').append(newTag);
            input.val('');
            $('#input-box').hide();
            $('#tagAdd').show();
        });

        // 点击tag
        $('.info-tags').on('click', '.js-tag-edit', function() {
            var tagBtn = $(this).find('.tag-btn');
            if(tagBtn.length){
                tagBtn.remove();
                $(this).css('padding-right', '10px');
                return false;
            }
            $(this).css('padding-right', '0').append($(deleteBtn));

        });

        // tag 上的删除按钮
        $('.info-tags').on('click', '.js-tag-subbtn', function() {
            $(this).parent().remove();
        });

    });


});

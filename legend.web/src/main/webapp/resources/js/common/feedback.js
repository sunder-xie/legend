$(function () {
    var linkStr = '<link rel="stylesheet">';
    var $body = $(document.body);
    var MAX_IMAGE = 1;
    var feedbackChunk = '<div class="feedback-box js-feedback" data-tpl-ref="feedback-js">'
        + '<div class="feedback-icon feedback-icon-1"></div><p class="feedback-text">问题反馈</p>'
        + '</div>';
    var feedbackHtml =
        '<div class="yqx-dialog-back hide"></div>\
        <div class="feedback-dialog main-dialog hide">\
        <div class="dialog-title"><i class="current-tab js-feedback-tab" data-target=".feedback-form">我要反馈问题</i><i class="history-tab js-feedback-tab" data-target=".feedback-history">历史反馈问题</i>\
        <span class="feedback-icon close-dialog js-close-dialog"></span>\
        </div>\
        <div class="dialog-content feedback-history hide">\
        <div class="history-main">\
        <div class="feedback-time"></div>\
        <p class="history-text"></p>\
        <div class="feedback-img">\
        </div>\
        </div>\
        <div class="history-reply hide">\
        <div class="feedback-time"></div>\
        <p>回复：<i class="history-text"></i></p>\
    </div>\
    <a href="javascript:void(0)" class="continue-feedback js-continue-feedback hide">继续反馈</a>\
        <a href="' + BASE_PATH + '/shop/help/feedback/feedback-list" class="more-feedback">更多历史反馈 ></a>\
        </div>\
        <div class="dialog-content feedback-form current-content">\
        <div class="form-item">\
        <select class="yqx-input js-feedback-select" name="feedbackModule"></select>\
        <i class="fa icon-angle-down"></i>\
        </div>\
        <div class="show-grid">\
        <div class="form-item">\
        <textarea placeholder="您的反馈对我们来说很重要" name="feedbackContent" class="yqx-input height-105"></textarea>\
        <div class="feedback-view-box">\
        </div>\
        </div>\
        <div class="add-pic js-feedback-pic">\
        <i class="feedback-icon feedback-pic"></i>添加截图\
        </div>\
        </div>\
        <input type="hidden" id="websiteUrl" name="websiteUrl">\
        <button class="yqx-btn yqx-btn-3 js-feedback-submit">提交</button>\
        </div>\
        </div>';
    var continueFeedbackHtml = '<div class="feedback-dialog continue-feedback-dialog hide">\
        <div class="dialog-title"><i class="current-tab js-feedback-tab" data-target=".feedback-form">继续反馈</i>\
        <span class="feedback-icon close-dialog js-close-dialog"></span>\
        </div>\
    <div class="dialog-content feedback-form">\
        <div class="show-grid" style="margin-top: 0;">\
        <div class="form-item">\
        <textarea placeholder="您的反馈对我们来说很重要" name="answerContent" class="yqx-input height-105"></textarea>\
        <div class="feedback-view-box">\
        </div>\
        </div>\
        </div>\
        <input type="hidden" id="websiteUrl" name="websiteUrl">\
        <button class="yqx-btn yqx-btn-3 js-feedback-submit">提交</button>\
        </div>\
        </div>';
    var fileHTML = '<input type="file" hidden class="js-feedback-file" accept="image/*">';
    var viewHTML = '<div class="feedback-view js-dialog-view">\
                <i class="feedback-file-name"></i><i class="feedback-icon white-del-icon js-feedback-del"></i>\
                </div>';
    var feedbackThanksHTML = '<div class="feedback-dialog feedback-thanks hide">\
        <div class="thanks-main">\
        <img src="' + BASE_PATH + '/static/img/common/feedback/feedback-smile.png">\
        </div>\
        <div class="thanks-text font-yahei">亲，您的反馈已提交成功，淘汽客服稍后会与您联系，请保持电话畅通，感谢您对淘汽云修的支持，祝您工作愉快！</div>\
    </div>';
    var imageViewBox = '<div class="feedback-dialog feedback-image-view-box hide"><i title="关闭" class="js-image-view-close">x</i></div>';

    var inlineStyle = {
        '.feedback-dialog .height-105': {
            'height': '105px'
        },
        '.yqx-dialog-back': {
            position: 'fixed',
            background: '#000',
            height: '100%',
            width: '100%',
            top: 0,
            opacity: '0.3'
        },
        '.feedback-dialog select[name=feedbackModule]': {
            width : 150,
            height: 30
        }
    };
    var layerExisted = false, right;

    $(linkStr).attr('href', BASE_PATH + '/static/css/common/feedback.min.css')
        .appendTo(document.head);

    try {
        if (layer || $.layer) {
            layerExisted = true;
        }
    } catch (e) {}

    if(!layerExisted)
        $('<script></script>').attr('src', BASE_PATH + '/resources/script/libs/layer/layer.js')
            .appendTo(document.head);
    
    $(feedbackChunk).appendTo('.js-extension-feedback');
    $(feedbackHtml).appendTo($body);
    $(continueFeedbackHtml).appendTo($body);
    var thanksBox = $(feedbackThanksHTML).appendTo($body);

    for (var selector in inlineStyle) {
        if (inlineStyle.hasOwnProperty(selector)) {
            $(selector).css(inlineStyle[selector]);
        }
    }

    $('.js-feedback').on('click', function () {
        showFeedbackDialog();
    });


    $('.js-feedback-cancel').on('click', function () {
        $('.yqx-dialog-back').addClass('hide');
        $('.feedback-dialog').addClass('hide');
    });

    $('.js-feedback-submit').on('click', function () {
        var data = {
            feedbackModule: $('.feedback-dialog select[name=feedbackModule]').val(),
            feedbackContent: $.trim($('.feedback-dialog textarea[name=feedbackContent]').val()),
            websiteUrl: location.pathname
            };
        var str = data.feedbackContent || '';
        var url  = BASE_PATH + '/shop/help/feedback/save', type = 'get';

        if(window.feedbackId != null) {
            delete data.feedbackContent;
            data.answerContent = $.trim(
                $('.feedback-dialog').find('textarea[name=answerContent]').val()
            );
            str =  data.answerContent || '';
        }
        if( str.length == 0) {
            layer.msg('请输入反馈内容', 3, {
                type: 3
            });
            return;
        }
        if( str.length > 500 ) {
            layer.msg('反馈内容请不要超过500个字', 3, {
                type: 3
            });
            return;
        }
        data.imgUrl = $('.feedback-dialog .feedback-view').data('imgUrl');

        if(window.feedbackId != null) {
            url = BASE_PATH + '/shop/help/feedback/feedback-answer-save';
            type = 'post';
            data.feedbackId = window.feedbackId;
            delete data.imgUrl;
        }

        $.ajax({
            url: url,
            data: data,
            type: type,
            success: function (json) {
                if (json && json.success) {
                    showThanksBox();

                    $('.feedback-dialog select[name=feedbackModule]').val('');
                    $('.feedback-dialog textarea[name=feedbackContent]').val('');
                    $('.js-feedback-cancel').click();
                }
                else {
                    layer.msg(json.errorMsg, 3, {
                        type: 3
                    });
                    $('.js-feedback-cancel').click();
                }
            }
        });
    });

    upload({
        dom: '.js-feedback-file',
        url: BASE_PATH + '/index/oss/upload_img_limit',
        fileSizeLimit: 2097152,
        callback: addView,
        error: removeView
    });

    $(document).on('click', '.js-feedback-pic', function (e) {
        var count = 0;

        $('.feedback-dialog .js-feedback-file').each(function () {
            if(this.files.length == 0) {
                $(this).remove();
            } else
                count += 1;
        });
        if(count >= MAX_IMAGE) {
            layer.msg('最多只能上传 ' + MAX_IMAGE + ' 张截图', 3, {
                type: 3
            });
            return;
        }
        $(fileHTML).appendTo( $('.feedback-dialog.main-dialog .dialog-content.feedback-form') )
            .click();

        e.stopImmediatePropagation();
    });


    $(document).on('click.feedback', '.js-feedback-del', function (e) {
        var self = this;
        var t = layer.confirm('确认删除该图片么', function () {
            layer.close(t);
            removeView.call(self.parentNode);
        });

        e.stopPropagation();
    });

    $(document).on('click.feedback', '.js-continue-feedback', function (e) {

        window.feedbackId = $(this).data('id');

        hideFeedbackDialog();
        showContinueFeedback();
        e.stopImmediatePropagation();
    }).on('click.feedback', '.feedback-dialog .js-image-view-close', function () {
        $(this).parent().remove();
        $('.main-dialog.feedback-dialog').removeClass('hide');
    })
    $(document).on('click', '.js-feedback-tab', function () {
        var target = $(this).attr('data-target');
        target = $('.feedback-dialog').find(target);

        if(this == target[0]) {
            return;
        }

        $('.feedback-dialog .current-tab').removeClass('current-tab');
        $(this).addClass('current-tab');
        if(target[0].className.indexOf('history') > -1) {
            getHistory.call(target[0]);
        }

        $('.feedback-dialog .current-content').addClass('hide');
        target.removeClass('hide').addClass('current-content');
    })
        .on('click.feedback', '.js-dialog-view', function (e) {
            var src = $(this).data('imgUrl');
            var view = $(imageViewBox).appendTo( $body );
            var img = $('<img src=' + src +'>')
                .appendTo( view );

            img.on('load', function () {
                var width, height;
                width = this.naturalWidth > 1000 ? 1000 : this.naturalWidth;
                height = (width / this.naturalWidth * this.naturalHeight) >> 0;

                height = height > 530 ? 530 : height;

                if(height && width) {
                    height += 'px';
                    width += 'px';

                    $(this).css({
                        height: height,
                        width: width
                    });
                }

                hideFeedbackDialog();
                view.removeClass('hide').css('width', width);
            });

            e.stopImmediatePropagation();
        })
        .on('click.feedback', '.js-close-dialog', function () {
            hideAll();
        });

    function getHistory() {
        $.ajax({url: BASE_PATH + '/shop/help/feedback/get-new-feedback'})
            .done(function (json) {
                var data = json.data;
                var box  = $('.feedback-dialog .feedback-history');

                if(json.success) {
                    box.find('.history-main')
                        .find('.history-text').text(data.feedbackContent)
                        .end().find('.feedback-time').text(data.gmtCreateStr);

                    if(data.newFeedbackAnswer && data.newFeedbackAnswer.gmtCeateStr) {
                        box.find('.continue-feedback').removeClass('hide')
                            .attr('data-id', data.id);
                        box.find('.history-reply').removeClass('hide')
                            .find('.history-text').text(
                            $('<div>' + data.newFeedbackAnswer.answerContent + '</div>').text()
                        ).end()
                            .find('.feedback-time').text(data.newFeedbackAnswer.gmtCreateStr)
                    }
                }
            })
    }

    function addView(result) {
        var view = $(viewHTML).appendTo( $('.feedback-view-box') );

        view.find('.feedback-file-name').text(this.files[0].name);
        view.data('file', $(this) );
        view.data('imgUrl', result.data.original);
    }

    function removeView(json) {
        try {
            $(this).data('file').remove();
        } catch(e) {
            layer.msg(json.errorMsg || '图片上传失败', 3, {
                type: 3
            });
        }
        $(this).remove();
    }

    $.ajax({
        url: BASE_PATH + '/shop/help/feedback/modules',
        success: function (json) {
            if(json.success) {
                var html = '';
                for(var i in json.data) {
                    html += '<option>' + json.data[i] + '</option>';
                }

                $('.js-feedback-select').append($(html));
            }
        }
    });

    function upload (opt) {
        var args = $.extend({
            dom: "",
            url: BASE_PATH + '/index/oss/upload_img',
            callback: null,
            fileSizeLimit: 5242880//1024*1024*5byte,即5M
        },opt);
        if(args.dom == ""){
            console.error('请传入上传按钮的选择器');
            return;
        }
        $(document).off('change.file').on('change.file', args.dom, function(e){
            var formData = new FormData();
            var self = this, loading;
            // 无数据时
            if(!this.files.length) {
                return;
            }
            formData.append('xxx',$(this).get(0).files[0]);
            formData.append('_fileSizeLimit',args.fileSizeLimit);

            loading = layer.msg('上传图片中...', 2, {
                type: 9
            });
            $.ajax({
                url: args.url,
                type: 'post',
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                dataType: 'json'
            }).done(function(json){
                layer.close(loading);
                if(json.success){
                    layer.msg('图片上传成功', 3, {
                        type: 1
                    });
                    args.callback && args.callback.call(self, json);
                }else{
                    args.error && args.error.call(self, json);
                }
            }).fail(function(json) {
                args.error && args.error.call(self, json);
            });
        });
    }

    $('.yqx-dialog-back').on('click.feedback', function () {
        $('.feedback-dialog').addClass('hide');
        $(this).addClass('hide');
    });

    function showFeedbackDialog() {
        $('.yqx-dialog-back').removeClass('hide');
        $('.feedback-dialog').eq(0).removeClass('hide');
    }

    function hideFeedbackDialog() {
        $('.feedback-dialog').eq(0).addClass('hide');
    }

    function hideAll() {
        $('.feedback-dialog').addClass('hide');
        $('.yqx-dialog-back').addClass('hide');
    }

    function showContinueFeedback() {
        $('.continue-feedback-dialog.feedback-dialog').removeClass('hide')
    }

    function showThanksBox() {
        hideFeedbackDialog();
        thanksBox.removeClass('hide');

        setTimeout(function () {
            hideAll();
        }, 2000);
    }
});
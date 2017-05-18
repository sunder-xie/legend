<style>
    .height-105 {
        height: 105px !important;
    }
</style>
<div class="feedback-box js-feedback hide" data-tpl-ref="feedback-tpl">
    <div class="feedback-icon feedback-icon-1"></div><p class="feedback-text">问题反馈</p>
</div>
<div class="feedback-dialog hide">
    <div class="dialog-title"><i class="current-tab js-feedback-tab" data-target=".feedback-form">我要反馈问题</i><i class="history-tab js-feedback-tab" data-target=".feedback-history">历史反馈问题</i></div>
    <div class="dialog-content feedback-history hide">
        <div class="history-main">
            <div class="feedback-time"></div>
            <p class="history-text"></p>
            <div class="feedback-img">
            </div>
        </div>
        <div class="history-reply hide">
            <div class="feedback-time"></div>
            <p>回复：<i class="history-text"></i></p>
        </div>
        <a href="javascript:void(0)" class="continue-feedback js-continue-feedback hide">继续反馈</a>
        <a href="${BASE_PATH}/shop/help/feedback/feedback-list" class="more-feedback">更多历史反馈 ></a>
    </div>
    <div class="dialog-content feedback-form current-content">
            <div class="form-item">
                <input class="yqx-input js-feedback-select"
                       name="feedbackModule"
                       value="首页"
                       placeholder="请选择模块">
                <i class="fa icon-angle-down"></i>
            </div>
        <div class="show-grid">
            <div class="form-item">
                <textarea placeholder="您的反馈对我们来说很重要" name="feedbackContent" class="yqx-input height-105"></textarea>
                <div class="feedback-view-box">
                </div>
            </div>
            <div class="add-pic js-feedback-pic">
                <i class="feedback-icon feedback-pic"></i>添加截图
            </div>
        </div>
        <input type="hidden" id="websiteUrl" name="websiteUrl">
        <button class="yqx-btn yqx-btn-3 js-feedback-submit">提交</button>
    </div>
</div>
<div class="feedback-dialog continue-feedback-dialog hide">
    <div class="dialog-title"><i class="current-tab js-feedback-tab" data-target=".feedback-form">继续反馈</i></div>
    <div class="dialog-content feedback-form current-content">
       <div class="show-grid" style="margin-top: 0;">
            <div class="form-item">
                <textarea placeholder="您的反馈对我们来说很重要" name="answerContent" class="yqx-input height-105"></textarea>
                <div class="feedback-view-box">
                </div>
            </div>
        </div>
        <input type="hidden" id="websiteUrl" name="websiteUrl">
        <button class="yqx-btn yqx-btn-3 js-feedback-submit">提交</button>
    </div>
</div>
<div class="feedback-dialog feedback-thanks hide">
    <div class="thanks-main">
        <img src="${BASE_PATH}/static/img/common/feedback/feedback-smile.png">
    </div>
    <div class="thanks-text font-yahei">亲，您的反馈已提交成功，淘汽客服稍后会与您联系，请保持电话畅通，感谢您对淘汽云修的支持，祝您工作愉快！</div>
</div>
<script type="text/template" id="feedbackSelectTpl">
    <div class="yqx-select-options" style="display: none;">
        <dl>
            <%for(var i in data) {%>
            <dd class="yqx-select-option" data-key="<%=data[i].id%>"><%=data[i].name%>
            </dd>
            <%}%>
        </dl>
    </div>
</script>
<script>
    $(function () {
        var MAX_IMAGE = 1;
        var html = $('.feedback-dialog.hide').eq(0).remove()
                .removeClass('hide')[0].outerHTML,
            continueFeedbackHtml = $('.feedback-dialog.continue-feedback-dialog')
                    .remove().removeClass('hide')[0].outerHTML;
        var feedbackThanksHtml = $('.feedback-dialog.feedback-thanks')
                .remove().removeClass('hide')[0].outerHTML;
        var fileHTML = '<input type="file" hidden class="js-feedback-file" accept="image/*">';
        var viewHTML = '<div class="feedback-view js-dialog-view">\
                <i class="feedback-file-name"></i><i class="feedback-icon white-del-icon js-feedback-del"></i>\
                </div>';

        try {
            seajs.use(['dialog', 'select', 'formData', 'upload'],
                    function (dialog, select, formData, upload) {
                var t;

                select.init({
                    dom: '.js-feedback-select',
                    tplId: '#feedbackSelectTpl',
                    url: BASE_PATH + '/shop/help/feedback/modules'
                });

                $('.js-feedback').on('click.feedback', function () {
                    t = dialog.open({
                        content: html,
                        area: ['532px', 'auto']
                    });
                });

                $(document).on('click.feedback', '.js-feedback-cancel', function () {
                    dialog.close(t);
                });

                $(document).on('click.feedback', '.js-feedback-submit', function () {
                    $('#websiteUrl').val(location.pathname);
                    var data = formData.get('.feedback-dialog');
                    var str = data.feedbackContent || data.answerContent || '';
                    var url  = BASE_PATH + '/shop/help/feedback/save', type = 'get';

                    if( str.length == 0) {
                        dialog.msg('请输入反馈内容');
                        return;
                    }
                    if( str.length > 500) {
                        dialog.msg('反馈内容请不要超过500个字');
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
                            if(json && json.success)
                                showSmile();
                            else
                                dialog.fail(json.errorMsg, function () {
                                    dialog.close();
                                });
                        }
                    });
                });

                upload.init({
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
                        dialog.warn('最多只能上传 ' + MAX_IMAGE + ' 张截图');
                        return;
                    }
                    $(fileHTML).appendTo( $('.feedback-dialog .dialog-content.feedback-form') )
                        .click();

                    e.stopImmediatePropagation();
                });


                // 删除图片
                $(document).on('click.feedback', '.js-feedback-del', function (e) {
                    var self = this;
                    dialog.confirm('确认删除该图片么', function () {
                        removeView.call(self.parentNode);
                    });

                    e.stopPropagation();
                });

                // 继续反馈
                $(document).on('click.feedback', '.js-continue-feedback', function (e) {
                    dialog.close();

                    window.feedbackId = $(this).data('id');
                    dialog.open({
                        content: continueFeedbackHtml,
                        area: ['530px', 'auto'],
                        end: function () {
                            delete window.feedbackId;
                        }
                    });
                    e.stopImmediatePropagation();
                });
         // 提交成功后显示的
        function showSmile() {
            dialog.close();
            dialog.open({
                content: feedbackThanksHtml,
                area: ['532px', 'auto'],
                time: 2000
            });

        }
            });
        } catch(e) {

        }

        // tab 切换
        $(document).on('click', '.js-feedback-tab', function () {
            var target = $(this).attr('data-target');
            target = $('.feedback-dialog').find(target);

            if(this == target[0]) {
                return;
            }

            $('.feedback-dialog .current-tab').removeClass('current-tab');
            $(this).addClass('current-tab');
            // 是历史反馈时
            if(target[0].className.indexOf('history') > -1) {
                getHistory.call(target[0]);
                $('.layui-layer-content .feedback-dialog').parent().css('height', 'auto');
            }

            $('.feedback-dialog .current-content').addClass('hide');
            target.removeClass('hide').addClass('current-content');
        })
                // 查看图片
                .on('click.feedback', '.js-dialog-view', function (e) {
                    var src = $(this).data('imgUrl');
                    var img = $('<img src=' + src +'>').addClass('hide').appendTo(document.body);

                    // 图片大小调整
                    img.on('load', function () {
                        util.imgZoomBigger.call(this, {maxWidth: 600, maxHeight: 400});
                    });

                    e.stopImmediatePropagation();
                });

        // 历史反馈信息
        function getHistory() {
            $.ajax({url: BASE_PATH + '/shop/help/feedback/get-new-feedback'})
                    .done(function (json) {
                        var data = json.data;
                        var box  = $('.feedback-dialog .feedback-history');

                        if(json.success) {
                            box.find('.history-main')
                                    .find('.history-text').text(data.feedbackContent)
                                .end().find('.feedback-time').text(data.gmtCreateStr);

                            // 是否有回复
                            if(data.newFeedbackAnswer && data.newFeedbackAnswer.gmtCreateStr) {
                                box.find('.continue-feedback').removeClass('hide')
                                        .attr('data-id', data.id);

                                box.find('.history-reply').removeClass('hide')
                                        .find('.history-text').text(
                                        $('<div>' + data.newFeedbackAnswer.answerContent + '</div>').text()
                                ).end()
                                        .find('.feedback-time').text(data.newFeedbackAnswer.gmtCreateStr)
                            }
                        }
                    });
        }

        // 添加图片 的展示
        function addView(result) {
            var view = $(viewHTML).appendTo( $('.feedback-view-box') );

            view.find('.feedback-file-name').text(this.files[0].name);
            view.data('file', $(this) );
            view.data('imgUrl', result.data.original);
        }

        // 删除图片
        function removeView(json) {
            try {
                $(this).data('file').remove();
            } catch(e) {
                seajs.use('dialog', function (dialog) {
                    dialog.fail(json.errorMsg || '上传失败');
                });
            }
            $(this).remove();
        }



        $('.js-feedback').removeClass('hide');
        $(window).trigger('resize');
    });
</script>
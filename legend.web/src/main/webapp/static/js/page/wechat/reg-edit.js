/**
 * Created by ende on 16/6/8.
 */
$(function () {
    var bankLists = [{name:"工商银行"},{"name":"农业银行"},{"name":"中国银行"},{"name":"建设银行"},{"name":"招商银行"}];

    seajs.use(['upload', 'ajax', 'check', 'select', 'art', 'formData', 'dialog', 'layer'],
        function (upload, aj, check, select, art, formData, dialog, layer) {
            var imgOuterHTMl = '<div class="file-show">' +
                '<img class="btn-upload-img">' +
                '<i class="file-del js-file-del" title="删除"></i>' +
                '</div>';
            // 文件上传与显示
            upload.init({
                dom: '.js-file',
                url: BASE_PATH + '/index/oss/upload_img_limit',
                callback: function (result) {
                    var path = result.data.original;
                    var img = $(this).parent().parent().find('.file-show');

                    if (!img.length || img.hasClass('template-img')) {
                        img = $(imgOuterHTMl);
                    }

                    img.find('img').attr('src', path);

                    $(this).parent().hide();
                    $(this).parent().after(img);
                    $(this).parent().find('.img-path').val(path);
                }
            });
            // 点击显示完整图片
            $(document).on('click', 'img', function() {
                var src = $(this).attr('src');
                if($(this).attr('disabled')) {
                    return;
                }
                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: 1,
                    area: ['712px', '300px'],
                    skin: 'layui-layer-nobg', //没有背景色
                    shadeClose: true,
                    content: '<img src=' + src + ' style="width:712px;height:300px">'
                });
            });

            check.init();
            // 下拉框 初始化
            select.init({
                dom: '.js-address',
                data: [],
                callback: function(e, val) {
                    var target = e.data('target');
                    var t = target, i = 0;
                    $(target).val('');

                    if(!target) {
                        return;
                    }

                    // 清除下级的数据
                    while(t) {
                        $(t).parent().find('input').val('');
                        $(t).parent().find('.yqx-select-options').remove();
                        t = $(t).data('target');
                    }

                    // 更新下级列表数据
                    setSelect(target, {pid: val});
                }
            });

            $('.bank').after( $(selectRender(bankLists, {name: 'name', key: 'name'})) );

            // 下拉列表渲染, 来自于 select.js
            function selectRender(data, opt, width, pleaseSelect, isClear) {
                // 默认数据
                var tpl;
                opt = opt || {name: 'regionName', key: 'id'};
                width = width || '100%';

                if(!data || !data.length) {
                    return '';
                }
                tpl = [
                    '<div class="yqx-select-options" style="width:' + width + ';display:none">',
                    '<dl>',
                    (pleaseSelect ? '<dd class="yqx-select-option" data-key="">请选择</dd>' : ''),
                    (isClear ? '<dd class="yqx-select-clean">请选择</dd>' : ''),
                    '<%for(var i=0;i<data.length;i++){%>',
                    '<dd class="yqx-select-option js-show-tips" title="<%=data[i][\"'+ opt.name
                    +'\"]%>" data-key="<%=data[i]["'+ opt.key +'"]%>"><%=data[i]["'+ opt.name+'"]%></dd>',
                    '<%}%>',
                    '</dl>',
                    '</div>'
                ].join('');

                return art.compile(tpl)({data:data});
            }

            // 下拉列表数据更新、请求
            function setSelect(target, data) {
                data = data || {pid: 1};

                $.ajax({
                    url: BASE_PATH + '/index/region/sub',
                    data: data,
                    global: false,
                    success: function (json) {
                        var html, text,
                            val = $(target).parent().find('input[type=hidden]').val();
                        // 有初始值的时候
                        var next = $(target).data('target');
                        var nextVal = $(next).parent().find('input[type="hidden"]').val();
                        if(!json || !json.success || !json.data) {
                            return;
                        }

                        // 有初始值的时候
                        json.data.some(function(e) {
                            if(e.id == val && e.id != null) {
                                text = e.regionName;
                                return true;
                            }
                        });

                        $(target).val(text || '');
                        // 有初始值的时候
                        if(next && nextVal != null) {
                            setSelect(next,
                                {pid: val});
                        }

                        html = selectRender(json.data);
                        $(target).parent().find('.yqx-select-options').remove();

                        $(target).after($(html));
                    }
                });
            }

            // 初始化省份列表
            setSelect('#province');
            setSelect('#bankProvince');
            setSelect('#bankProvince2');

            // 已有账号的保存
            $('.js-save').on('click', function() {
                var data = formData.get('.content', true);

                if(!check.check()) {
                    return;
                }

                save(data);
            });

            // 下一步与保存提交
            $('.js-next').on('click', function() {
                var data = formData.get('.basic',true);
                var target = $('input[name=companyType]:checked').data('target');
                var secondData = formData.get(target, true);

                $.extend(data, secondData);

                if(!check.check()) {
                    return;
                }

                save(data);
            });

            function save(data) {
                $.ajax({
                    url: BASE_PATH + '/shop/wechat/op/save-wechat-info',
                    data: JSON.stringify(data),
                    type: 'POST',
                    contentType: 'application/json',
                    dataType: 'json',
                    success: function(json) {
                        if(json && json.success) {
                            dialog.success('提交成功', function() {
                                location.href = BASE_PATH + '/shop/wechat';
                            });
                        } else {
                            dialog.fail(json.errorMsg || '提交失败');
                        }
                    },
                    error: function() {
                        dialog.fail('提交失败');
                    }
                });
            }
        }); // seajs


    $(document).on('click', '.js-file-del', function () {
        var $fileset = $(this).parents('fieldset');

        $fileset.find('.img-path').val('');
        $(this).parent().remove();
        $fileset.find('.btn-upload').show();
    });

    // 公众号类型切换
    $('.js-radio').on('click', function () {
        var target = $(this).data('target');
        var self = this;
        $('.check-radio').find('input').each(function () {
            if (this != self) {
                $(this).removeAttr('checked');
            }
        });

        $('.form-current').addClass('hide').removeClass('form-current');
        $(target).removeClass('hide').addClass('form-current');
    });

});
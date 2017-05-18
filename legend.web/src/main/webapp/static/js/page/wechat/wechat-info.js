$(function () {
    seajs.use(['select', 'art', 'dialog', 'check', 'formData','date'], function (select, art, dialog, check, formData, date) {
        check.init();

        date.datePicker('.js-certificationTime', {
            dateFmt: 'yyyy-MM-dd',
            maxDate: '%y-%M-%d'
        });

        select.init({
            dom: '.js-address',
            data: [],
            callback: function (e, val) {
                var target = e.data('target');
                var t = target, i = 0;
                $(target).val('');

                if (!target) {
                    return;
                }

                // 清除下级的数据
                while (t) {
                    $(t).parent().find('input').val('');
                    $(t).parent().find('.yqx-select-options').remove();
                    t = $(t).data('target');
                }

                // 更新下级列表数据
                setSelect(target, {pid: val});
            }
        });

        $(document).on('click', '.qrcode-img', function () {
            util.imgZoomBigger.call(this, {height: 400})
        });

        // 下拉列表渲染, 来自于 select.js
        function selectRender(data, opt, width, pleaseSelect, isClear) {
            // 默认数据
            var tpl;
            opt = opt || {name: 'regionName', key: 'id'};
            width = width || '100%';

            if (!data || !data.length) {
                return '';
            }
            tpl = [
                '<div class="yqx-select-options" style="width:' + width + ';display:none">',
                '<dl>',
                (pleaseSelect ? '<dd class="yqx-select-option" data-key="">请选择</dd>' : ''),
                (isClear ? '<dd class="yqx-select-clean">请选择</dd>' : ''),
                '<%for(var i=0;i<data.length;i++){%>',
                '<dd class="yqx-select-option js-show-tips" title="<%=data[i][\"' + opt.name
                + '\"]%>" data-key="<%=data[i]["' + opt.key + '"]%>"><%=data[i]["' + opt.name + '"]%></dd>',
                '<%}%>',
                '</dl>',
                '</div>'
            ].join('');

            return art.compile(tpl)({data: data});
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
                    if (!json || !json.success || !json.data) {
                        return;
                    }

                    // 有初始值的时候
                    json.data.some(function (e) {
                        if (e.id == val && e.id != null) {
                            text = e.regionName;
                            return true;
                        }
                    });

                    $(target).val(text || '');
                    // 有初始值的时候
                    if (next && nextVal != null) {
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
        // 下一步与保存提交
        $('.js-next').on('click', function (e) {
            var data = formData.get('.order-body', true);

            if (!check.check()) {
                return;
            }
            $.ajax({
                url: BASE_PATH + '/shop/wechat/op/save-wechat-info',
                data: JSON.stringify(data),
                type: 'POST',
                contentType: 'application/json',
                dataType: 'json',
                success: function (json) {
                    if (json && json.success) {
                        dialog.success('提交成功', function () {
                            location.href = BASE_PATH + '/shop/wechat/wechat-info';
                        });
                    } else {
                        dialog.fail(json.errorMsg || '提交失败');
                    }
                },
                error: function () {
                    dialog.fail('提交失败');
                }
            }); // ajax
        });
    });// seajs
});
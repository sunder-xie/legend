/**
 *
 * Created by ende on 16/11/9.
 */
$(function () {
    var baseUrl = BASE_PATH + '/shop/print-config';
    var printData = {
        '编号': 'GD239394840930303',
        '开单时间': '2015/09/09 10:49',
        '预完工时间': '2015/09/10 10:49',
        '服务顾问': '张三',
        '车主': '罗小灰 12345678901(车主)',
        '联系人': '罗小灰 12345678901(联系人)',
        '单位': '武装部',
        '车牌': '浙A12344',
        '车辆颜色': '黑色',
        '行驶里程数': '1000000 km',
        '承保公司': '阿萨德保险公司',
        '发动机号': 'GD123456',
        'VIN码': 'GD123456789012345',
        '油表油量': '小于1/4',
        '车辆型号': '大众 Garave lle VR6'
    };
    var config = {};
    var printBox = $('.order-right .main');
    var t;
    var target = $('.current-item').data('target');
    var _current = {
        config: null,
        vo: null
    },
        // 保存时置为 true，避免保存其他版本后，选择自定义版本还是原来的版本
        _save = false;
    var dataChange = {};
    // 切换到其他页面或者关闭本标签页时
    var closeWarning = (function () {
        // dialog 是否显示了
        // 避免 dialog 和 系统提示会一起出现的情况
        var warning = false;
        return function (e) {
            var that = this;
            var str;

            if(warning) {
                return;
            }

            if ( (str = getChangedSettingName()) ) {
                if (e.target == document) {
                    return '\o';
                } else {
                    warning = true;
                    seajs.use('dialog', function (dialog) {
                        dialog.confirm(str, function (){
                            warning = false;
                        }, function () {
                            $(that).click();
                            var href = $(that).find('a').eq(0).trigger('click')
                                .attr('href');

                            if( $(that).hasClass('js-back') ) {
                                util.goBack();
                            }

                            window.location.href = href;
                        }, ['留下', '离开']);
                    });
                    e.preventDefault();
                }
            } else if( $(this).hasClass('js-back') ) {
                util.goBack();
            }
        };
    }());

    t = {};
    printBox.each(function () {
        var ver = $(this).find('.print-box').data('print');

        t[ver] = $(this);
        $(this).removeClass('hide');

        if (target != ver) {
            this.remove();
            return;
        }
        t[ver].loadData = true;
    });
    printBox = t;
    t = null;

    loadData(printData);

    // 关闭标签页
    $(window).on('beforeunload', closeWarning);

    getConfig(config, true);

    $('.js-tab .tab-item').click(function () {
        var target = this.dataset.target;
        var that = this;
        var current = $('.current-item.tab-item');
        var str = getChangedSettingName();

        if (current[0] == this) {
            return;
        }

        if(str) {
            seajs.use('dialog', function (dialog) {
                dialog.confirm(str, null, function () {
                    dataChange[ getCurrentPrint().name ] = false;

                    current.removeClass('current-item');

                    for (var i in printBox) {
                        printBox[i].remove();
                    }
                    $('.order-right').append(printBox[target]);
                    if (printBox[target] && !printBox[target].loadData) {
                        loadData(printData, target);
                        printBox[target].loadData = true;
                    }

                    $(that).addClass('current-item');
                    getConfig(config);
                },
                    ['留下', '离开']);
            });
        } else {
            current.removeClass('current-item');

            for (var i in printBox) {
                printBox[i].remove();
            }
            $('.order-right').append(printBox[target]);
            if (printBox[target] && !printBox[target].loadData) {
                loadData(printData);
                printBox[target].loadData = true;
            }

            $(that).addClass('current-item');
            getConfig(config);
        }
    });

    $(document)
        .on('click', '.option-box input[type="checkbox"]', function () {
            setCheckBoxConfig.call(this);
        })
        .on('click', '.js-back', closeWarning)
        // 留白的选择
        .on('click', '.js-empty-line', function () {
            var target = $(this).data('target').split('|');
            // 保存的 key
            var name = $(this).data('name');
            // 几行留白
            var number = target[1], len;
            // 留白 html
            var html = '<tr class="empty-line">';
            // 目标 box
            var targetBox = $('.print-box').find('.' + target[0] + '-box');

            len = targetBox.find('tr th').length;
            for(var i = 0; i < len; i ++) {
                html += '<td></td>';
            }
            html += '</tr>';
            // 已存在的空行
            len = targetBox.find('.empty-line').length;

            if(len == number) {
                return;
            }

            $(this).parents('.radio-box').parent()
                .find('.js-enable-empty').prop('checked', true);

            if (targetBox.length) {
                if (this.checked) {
                    _current.obj[name].extValue = target[1];
                    // 有空行
                    if (len) {
                        // 增减
                        targetBox.find('.empty-line').each(function () {
                            if (len > number) {
                                this.remove();
                                len -= 1;
                            } else if (len < number) {
                                targetBox.append(html);
                                len += 1;
                            } else if (len == number) {
                                return false;
                            }
                        });
                    } else {
                        // 无空行
                        while (--number) {
                            html += html;
                        }
                        targetBox.append(html);
                    }
                }
            }
        })
        // 启用空行的选择
        .on('click', '.js-enable-empty', function () {
            var target = $(this).data('target');
            var name = $(this).parent().parent()
                .find('.js-empty-line')
                .eq(0).data('name');
            // li
            //    label>input.js-enable-empty
            //    ul>li>input[type=radio]
            var checked = $(this).parent().parent()
                    .find('input[type=radio]:checked');

            if (!this.checked) {
                $('.' + target + '-box').find('.empty-line').remove();

                checked.prop('checked', false);
                _current.obj[name].extValue = 0;
            } else {
                checked.trigger('click');
            }
        })
        .on('change', '.js-table', function () {
            var target = $(this).data('target');

            $('.print-container table').toggleClass(target, this.checked);
        })
        // 字体的选择
        .on('click', '.js-font', function (e) {
            fontChange.call(this);
        })
        // footer 的信息，提示语
        .on('blur', '.js-info-show', function () {
            var parent = $('#提示语');
            var value = $.trim($(this).val());
            var arr = value.split('\n');

            if (arr.length) {
                parent.empty();
                if (!_current.obj['提示语']) {
                    _current.obj['提示语'] = {};
                }
                _current.obj['提示语'].extValue = value;
            }
            for (var i in arr) {
                parent.append('<li>' + arr[i] + '</li>');
            }
        })
        // 版本的选择，标准版、自定义...
        .on('click', '.js-choose-version li', function () {
            var current = $(this).parents('.option-box').find('.selected')[0];
            var printType = $(this).data('print-type');

            if (current == this) {
                return;
            }

            // 不是自定义版本的话
            if (printType != 4) {
                // 版本选择，打印样式，门店信息除外
                $(this).parents('.option-box').children().slice(3, -1)
                    .addClass('hide')
                    // 字段分隔线
                    .end().end().find('.striped-content').toggleClass('hide', true)
            } else {
                $(this).parents('.option-box').children()
                    .removeClass('hide')
                    // 字段分隔线
                    .end().find('.striped-content').toggleClass('hide', false)
            }

            $(this).addClass('tag-control selected');
            $(current).removeClass('tag-control selected');

            getConfig(config);
        })
        // a 标签跳转
        .on('click', '.yqx-header li', closeWarning)
        // 保存
        .on('click', '.js-save', function () {
            var currentPrint = getCurrentPrint().name;
            var printTemplate = $('.current-item').data('printTemplate'),
                printType = $('.main .version-box .selected.tag-control').data('printType') || 2,
                data;

            if (config[printTemplate][printType]) {
                data = copySaveData(config[printTemplate][printType].vo, printType);
            }

            $.ajax({
                url: baseUrl + '/save',
                type: 'post',
                data: data,
                success: function (json) {
                    seajs.use('dialog', function (dialog) {
                        if (json.success) {
                            dialog.success('保存成功');
                            dataChange[ currentPrint ] = false;
                            _save = true;
                        } else {
                            dialog.fail(json.errorMsg || '保存失败');
                        }
                    })
                }
            })
        });

    function loadData(data) {
        for (var id in data) {
            $('#' + id).find('.text').text(data[id]);
        }
    }

    function mergeTable(merge) {
        $('#总优惠').removeClass('merge-fee');

        var target = $('.print-box .merge-table').add( $('.merge-fee') );
        var other = $('.print-box .normal-table').add( $('.normal-fee') );

        if (merge) {
            other.addClass('hide');
            target.removeClass('hide');

            $('#总优惠').addClass('merge-fee');
        } else {
            target.addClass('hide');
            other.removeClass('hide');

        }
    }

    function convertVoToObj(configInfo, fontSize) {
        var key, i;
        var t, ret = {}, field;
        var name;

        // 现在是用中文作为 key，即 fieldName
        for (key in configInfo) {
            t = configInfo[key];
            for (i in t) {
                name = t[i].field;
                field = t[i].fieldName;

                // 这里使用 field 作为 key
                if(name == 'line' || name == 'showAccount'
                    || name == 'fixRecord') {
                    field = name;
                }
                ret[field] = t[i];

            }
        }

        if (fontSize == 1) {
            ret['大字体'] = {
                isChecked: true
            }
        } else {
            ret['小字体'] = {
                isChecked: true
            }
        }

        return ret;
    }

    function setConfig(data) {
        var checkboxList = $('.option-box label');
        var versionControl = $('.version-box .tag-control.selected');
        var t, emptyLines = [], index, box;
        var config = data.obj,
            printType = data.vo.printType,
            fontSize = data.vo.fontStyle;

        if (printType != 4) {
            // 版本选择，打印样式，门店信息除外
            $('.option-box').children().slice(3, -1)
                .addClass('hide')
                .end().end().find('.striped-content').toggleClass('hide', true);

        } else {
            $('.option-box').children()
                .removeClass('hide')
                .end().find('.striped-content').toggleClass('hide', false);
        }

        $('.main .js-font').each(function () {
            var val = $(this).val();

            if (fontSize == 1 && val == 'font-12') {
                this.checked = true;
            } else if (fontSize == 0 && val == '') {
                this.checked = true;
            }
            if(this.checked) {
                fontChange.call(this, true);
            }
        });

        if (config) {
            checkboxList.each(function (i, e) {
                var input = $(e).find('input');
                var key = input.data('key') || input.data('target') || $.trim($(e).text());

                if (!key || !config[key]) {
                    return;
                }

                // 大小字体
                if (input.attr('type') == 'radio') {
                    return;
                }

                // 触发 checkbox 的点击事件
                if (input[0].checked != config[key].isChecked) {
                    input.prop('checked', config[key].isChecked || false);

                    if(key == 'line') {
                        input.trigger('change');
                    } else
                        setCheckBoxConfig.call(input[0], true);
                }
                if (key == '提示语') {
                    $('.js-info-show')
                        .text(config[key].extValue)
                        .trigger('blur');
                }
            });
            // 留白
            if ((t = config['服务留白'])) {
                emptyLines.push({
                    name: 'service',
                    val: t.extValue
                });
            }
            if ((t = config['配件留白'])) {
                emptyLines.push({
                    name: 'goods',
                    val: t.extValue
                });
            }
            if ((t = config['附加费用项目留白'])) {
                emptyLines.push({
                    name: 'addon',
                    val: t.extValue
                });
            }

            emptyLines.forEach(function (e) {
                if (e.val > 0) {
                    $('.js-empty-line[name=' + e.name + ']').eq(e.val - 1)
                        .prop('checked', true)
                        .parents('.radio-box').parent()
                        .find('.js-enable-empty').prop('checked', true);

                    $('.js-empty-line[name=' + e.name + ']').eq(e.val - 1).trigger('click');
                } else {
                    $('.' + e.name + '-box').find('.empty-line').remove();
                }
            });
            if($('#承保公司').length && $('#承保公司')[0].clientHeight > 17 ) {
                $('#承保公司').removeClass('width-57 width-64');
            }
            if( $('#车辆型号').length && $('#车辆型号')[0].clientHeight > 17 ) {
                $('#车辆型号').removeClass('width-57 width-64');
            }
        }

        box = $('.print-box .split-vertical');
        if (box) {
            t = box.children();
            index = 0;
            t.each(function () {
                if ($(this).is(':hidden')) {
                    return;
                }
                if (index > 0) {
                    $(this).addClass('border-left');
                } else {
                    $(this).removeClass('border-left');
                }
                index += 1;
            });
        }

        if (!versionControl.length && printType !== undefined) {
            $('.version-box li[data-print-type="' + printType + '"]').addClass('tag-control selected');
        }

    }

    function getConfig(config) {
        var data = {
            printTemplate: $('.current-item').data('printTemplate'),
            printType: $('.main .version-box .selected.tag-control').data('printType') || null
        }, t;

        if (config[data.printTemplate]
            && config[data.printTemplate][data.printType]) {

            if(_save && data.printType == 4) {
                config[data.printTemplate][data.printType] = null;
                getConfig(config);
                return;
            }
            _current = config[data.printTemplate][data.printType];

            setConfig(_current);
            return;
        }

        $.ajax({
            url: baseUrl + '/get-print-config',
            data: data,
            success: function (json) {
                if (json.success && json.data) {
                    var data = json.data;
                    if (!config[data.printTemplate]) {
                        config[data.printTemplate] = {};
                    }

                    t = config[data.printTemplate][data.printType] = {
                        // 转换的数据
                        obj: convertVoToObj(json.data.configFieldVO, json.data.fontStyle),
                        // 原始数据
                        vo: json.data
                    };

                    _current = t;

                    setConfig(t);
                    _save = false;
                }
            }
        });
    }

    function copySaveData(obj, realType) {
        var ret = {};

        for (var key in obj) {
            switch (key) {
                case 'creator':
                case 'gmtCreate':
                case 'gmtModified':
                case 'id':
                case 'isDeleted':
                case 'modifier':
                case 'configField':
                case 'printConfigName':
                case 'shopId':
                    break;
                case 'configFieldVO':
                    ret.configField = JSON.stringify(obj[key]);
                    break;
                default:
                    ret[key] = obj[key];
                    break;
            }
        }

        ret.printType = realType;

        return ret;
    }

    function getCurrentPrint() {
        var name = $.trim( $('.current-item.tab-item').text() );

        return {
            name: name
        };
    }

    function setCheckBoxConfig(isDefault) {
        var key = $(this).data('key') || $(this).data('target') || $.trim($(this).parent().text());
        var target = $('#' + key),
            desc = $(this).data('desc');
        var box = target ? target.parents('.box') : null;
        var t, index;

        if (_current.obj[key]) {
            _current.obj[key].isChecked = this.checked;
        }

        // 表格中的字段, 备注
        if (desc == 'note') {
            if (key == '配件备注') {
                t = '.goods-tr';
            }
            if (key == '服务备注') {
                t = '.service-tr';
            }
            if (key == '附件备注') {
                t = '.addon-tr';
            }

            if(!this.checked) {
                $(t).find('.note').addClass('hide');
            } else {
                $(t).find('.note').removeClass('hide');
            }
        }
        if (key == '合并展示') {
            target = $();
            mergeTable(this.checked, _current.obj);
        }

        if(!target) {
            return;
        }

        if (!this.checked) {
            target.addClass('hide');
        } else {
            target.removeClass('hide');
        }

        if(box.hasClass('car-info')
            && box.parents('.print-box').hasClass('font-12')) {
            index = 1;
            box.find('ul>li').each(function () {
                if($(this).is(':visible')) {
                    return;
                }
                if(index % 3 == 2) {
                    $(this).addClass('width-64')
                        .removeClass('width-57');
                } else if(index % 3 == 1) {
                    $(this).addClass('width-57')
                        .removeClass('width-64');
                } else {
                    $(this).removeClass('width-57')
                        .removeClass('width-64');
                }
                index += 1;
            })
        }

        if(isDefault !== true) {
            dataChange[ getCurrentPrint().name ] = true;
        }

        if (box) {
            if (box.hasClass('split-vertical')) {
                t = target.parents('li');
                if (t.find('.hide').length
                    === t.find('.text').length) {
                    t.addClass('hide');
                } else {
                    t.removeClass('hide')
                }

                index = 0;
                t = box.children();
                t.each(function () {
                    if ($(this).is(':hidden')) {
                        return;
                    }
                    if (index > 0) {
                        $(this).addClass('border-left');
                    } else {
                        $(this).removeClass('border-left');
                    }
                    index += 1;
                });
            }
            if (box.find('.hide[id]').length
                === box.find('.text').length) {
                box.addClass('hide');
            } else {
                box.removeClass('hide')
            }
        }

        if($('#承保公司').length && $('#承保公司')[0].clientHeight > 17 ) {
            $('#承保公司').removeClass('width-57 width-64');
        }
        if( $('#车辆型号').length && $('#车辆型号')[0].clientHeight > 17 ) {
            $('#车辆型号').removeClass('width-57 width-64');
        }
    }

    function fontChange(isDefault) {
        var box = $('.print-box .car-info'), index;
        var val = this.value;
        if (this.checked) {
            $('.print-box').removeClass('font-12').addClass(val);
        }

        if(isDefault !== true) {
            dataChange[ getCurrentPrint().name ] = true;
        }

        if(box.parents('.print-box').hasClass('font-12')) {
            $('#车辆型号').before( $('#承保公司').removeClass('width-57').removeClass('width-64') );
            index = 1;
            box.find('ul>li').each(function () {
                if(!$(this).is(':visible')) {
                    return;
                }
                if(index % 3 == 2) {
                    $(this).addClass('width-64')
                        .removeClass('width-57')
                } else if(index % 3 == 1) {
                    $(this).addClass('width-57')
                        .removeClass('width-64');
                } else {
                    $(this).removeClass('width-57')
                        .removeClass('width-64');
                }
                index += 1;
            });
        } else {
            box.find('ul>li').removeClass('width-57')
                .removeClass('width-64');

            $('#行驶里程数').after( $('#承保公司') );
        }
        _current.vo.fontStyle = val == 'font-12' ? 1 : 0;

        if($('#承保公司').length && $('#承保公司')[0].clientHeight > 17 ) {
            $('#承保公司').removeClass('width-57 width-64');
        }
        if( $('#车辆型号').length && $('#车辆型号')[0].clientHeight > 17 ) {
            $('#车辆型号').removeClass('width-57 width-64');
        }
    }

    function getChangedSettingName() {
        var str = [];
        for (var i in dataChange) {
            if (dataChange[i]) {
                str.push(i);
            }
        }
        return str.length ? ('您的' + str.join(',') + '更改可能会丢失，是否留下？') : false;
    }
});

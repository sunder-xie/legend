/**
 * Created by ende on 16/11/14.
 */
$(function () {
    window.Components = $.extend(window.Components || {}, {
        printDisplay: function (config, callback) {
            config = typeof config == 'string'
                ? this.convertVoToObj(JSON.parse(config))
                : config;
            var target, box, t, index,
                key, _key,
                arr, parent;
            var emptyLines = [];
            var emptyLineStr = '<tr class="empty-line">';

            box = $('.print-box .car-info');
            if (box.parents('.print-box').hasClass('font-12')) {
                $('#车辆型号').before($('#承保公司')
                    .removeClass('width-57').removeClass('width-64'));
                index = 1;
                box.find('ul>li').each(function () {
                    if (!$(this).is(':visible')) {
                        return;
                    }
                    if (index % 3 == 2) {
                        $(this).addClass('width-64')
                            .removeClass('width-57')
                    } else if (index % 3 == 1) {
                        $(this).addClass('width-57')
                            .removeClass('width-64');
                    } else {
                        $(this).removeClass('width-57')
                            .removeClass('width-64');
                    }
                    index += 1;
                });
            } else {
                $('#行驶里程数').after($('#承保公司'));
            }

            for (key in config) {
                _key = key;
                if(key == 'line') {
                    $('table').toggleClass('striped', config[key].isChecked);
                }
                if (key == '合并展示') {
                    target = $();
                    this.mergeTable(config[key].isChecked, config);
                }

                if (key == '配件备注' || key == '服务备注' || key == '附件备注') {
                    // 备注的显示隐藏
                    if (key == '配件备注') {
                        t = '.goods-tr';
                    }
                    if (key == '服务备注') {
                        t = '.service-tr';
                    }
                    if (key == '附件备注') {
                        t = '.addon-tr';
                    }


                    if(!config[key].isChecked) {
                        $(t).find('.note').remove();
                    }
                }

                target = target && target.length ? target : $('#' + _key);

                if (target.length) {
                    box = target.parents('.box');

                    if (key == '提示语') {
                        parent = $('#提示语');
                        arr = config[key].extValue.split('\n');

                        if (arr.length) {
                            parent.empty();
                            for (var i in arr) {
                                parent.append('<li>' + arr[i] + '</li>');
                            }
                        }
                    }

                    if (!config[key].isChecked) {
                        target.addClass('hide');
                    } else {
                        target.removeClass('hide');
                    }

                    if (box && box.length) {
                        if (box.hasClass('split-vertical')) {
                            t = target.parents('li');
                            if (t.find('.hide').length
                                === t.find('.text').length) {
                                t.addClass('hide');
                            } else {
                                t.removeClass('hide')
                            }
                        }

                        if (box.find('.hide[id]').length
                            === box.find('.text').length) {
                            box.addClass('hide');
                        } else {
                            box.removeClass('hide')
                        }
                    }
                }
                target = null;
            }
            box = $('.print-box .split-vertical');
            if (box) {
                t = box.children();
                index = 0;
                t.each(function () {
                    if (!$(this).is(':visible')) {
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
                var box = $('.' + e.name + '-box');
                var len = box.find('tr th').length;

                emptyLineStr = '<tr class="empty-line">';

                for (i = 0; i < len; i++) {
                    emptyLineStr += '<td></td>';
                }
                emptyLineStr += '</tr>';

                var t = '';
                if (e.val <= 0) {
                    return;
                }
                while (e.val--) {
                    t += emptyLineStr;
                }
                box.append(t);
            });

            if($('#承保公司').length && $('#承保公司')[0].clientHeight > 17 ) {
                $('#承保公司').removeClass('width-57 width-64');
            }
            if( $('#车辆型号').length && $('#车辆型号')[0].clientHeight > 17 ) {
                $('#车辆型号').removeClass('width-57 width-64');
            }
            if( $('#联系人')[0].clientWidth == 0 || $('#联系人')[0].clientHeight == 0 ) {
                $('#单位').css('max-width', '129mm');
            }
        },

        convertVoToObj: function (configInfo) {
            var key, i;
            var t, ret = {},
                field;
            var name;

            for (key in configInfo) {
                t = configInfo[key];
                for (i in t) {
                    name = t[i].field;
                    field = t[i].fieldName;

                    if(name == 'line' || name == 'showAccount'
                        || name == 'fixRecord') {
                        field = name;
                    }
                    ret[field] = t[i];
                }
            }

            return ret;
        },

        mergeTable: function (merge) {
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
        },
        changeCompanyWidth: function () {
            if( $.trim($('.relative-customer').text()) == '' ) {
                $('.customer-company').addClass('width-125');
            }
        },
        // 大写金额转换
        digitUppercaseCN: function (n) {
            if(typeof n === 'string') {
                n = parseFloat($.trim(n));
            }
            var fraction = ['角', '分'];
            var digit = [
                '零', '壹', '贰', '叁', '肆',
                '伍', '陆', '柒', '捌', '玖'
            ];
            var unit = [
                ['元', '万', '亿'],
                ['', '拾', '佰', '仟']
            ];
            var head = n < 0 ? '欠' : '';
            n = Math.abs(n);
            var s = '';
            for (var i = 0; i < fraction.length; i++) {
                s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
            }
            s = s || '';
            n = Math.floor(n);
            for (var i = 0; i < unit[0].length && n > 0; i++) {
                var p = '';
                for (var j = 0; j < unit[1].length && n > 0; j++) {
                    p = digit[n % 10] + unit[1][j] + p;
                    n = Math.floor(n / 10);
                }
                s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;
            }
            return head + s.replace(/(零.)*零元/, '元')
                    .replace(/(零.)+/g, '零');
        }
    });
});
